package callisto.logic;

import callisto.model.CallistoResult;
import callisto.model.MutationMatrices;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class Calculator {
    public static boolean intArrayContains(int[] array, int key) {
        for (int i : array) {
            if (i == key) {
                return true;
            }
        }
        return false;
    }

    public static CoverageQualityResult calculateMutationOperatorCoverageQuality(MutationMatrices matrices) {
        //assumptions of matrix contents:
        //- all mutants are from one mutator
        //- the test suite is minimalized
        if (matrices.getNumberOfMutants() == 0) {
            return new CoverageQualityResult(0, 0);
        }
        double[] mutantQualities = new double[matrices.getNumberOfMutants()];
        int[] equivalentMutants = matrices.getEquivalentMutants();
        for (int m = 0; m < matrices.getNumberOfMutants(); m++) {
            //equivalent mutants get quality 0
            if (intArrayContains(equivalentMutants, m)) {
                mutantQualities[m] = 0;
                continue;
            }

            double sum = 0;
            int[] killerTests = matrices.getKillersOfMutant(m);
            for (int test : killerTests) {
                sum += matrices.getMutantsKilledByTest(test).length;
            }

            double denominatorSum = 0;
            int[] testsToConsider;
            // determine if mutant is difficult to reach:
            if (!matrices.isMutantDifficultToReach(m)) {
                testsToConsider = matrices.getCoverersOfMutant(m);
            } else {
                // consider all tests:
                testsToConsider = IntStream.range(0, matrices.getNumberOfTests()).toArray();
            }
            for (int test : testsToConsider) {
                denominatorSum += Arrays.stream(matrices.getMutantsCoveredByTest(test)).filter(mutant -> Calculator.intArrayContains(equivalentMutants, mutant)).count();
            }
            // final calculation:
            double quality = 1.0 - (sum / denominatorSum);
            mutantQualities[m] = quality;
        }
        //mutator quality = average of mutant qualities
        double averageQuality = Arrays.stream(mutantQualities).average().getAsDouble();
        // calculate absolute deviation w.r.t. average (mean)
        double deviationSum = 0;
        for (double mutantQuality : mutantQualities) {
            deviationSum += Math.abs(mutantQuality - averageQuality);
        }
        double deviation = deviationSum / mutantQualities.length;
        return new CoverageQualityResult(averageQuality, deviation);
    }

    public static CallistoResult[] calculateAverageOverReports(CallistoResult[][] reportResults) {
        // if only one report, then just return that one
        if (reportResults.length == 1) {
            return reportResults[0];
        }
        // get all mutation operators used
        List<String> mutationOperators = new ArrayList<>();
        for (CallistoResult[] reportResult : reportResults) {
            for (CallistoResult mutatorResult : reportResult) {
                if (!mutationOperators.contains(mutatorResult.mutationOperatorName())) {
                    mutationOperators.add(mutatorResult.mutationOperatorName());
                }
            }
        }
        // calculate average for each mutator
        List<CallistoResult> result = new ArrayList<>();
        for (String mutationOperator : mutationOperators) {
            int totalMutantCount = 0;
            int totalTestsExecuted = 0;
            List<CallistoResult> mutationOperatorResults = new ArrayList<>();
            for (CallistoResult[] reportResult : reportResults) {
                for (CallistoResult mutationOperatorResult : reportResult) {
                    if (mutationOperatorResult.mutationOperatorName().equals(mutationOperator)) {
                        totalMutantCount += mutationOperatorResult.mutantCount();
                        totalTestsExecuted += mutationOperatorResult.numberOfTestExecutions();
                        mutationOperatorResults.add(mutationOperatorResult);
                    }
                }
            }
            List<Double> weightedQuality = new ArrayList<>();
            for (CallistoResult mutationOperatorResult : mutationOperatorResults) {
                weightedQuality.add(mutationOperatorResult.mutantCount() * mutationOperatorResult.quality());
            }
            double sum = weightedQuality.stream().reduce(0.0, Double::sum);
            double weightedAverage = sum / totalMutantCount;
            // combining absolute deviations currently not possible, so value 0 is used.
            result.add(new CallistoResult(mutationOperator, weightedAverage, 0, totalMutantCount, totalTestsExecuted));
        }
        return result.toArray(CallistoResult[]::new);
    }

    public record CoverageQualityResult(double quality, double deviation) {}
}
