package callisto.commands;

import callisto.Callisto;
import callisto.logic.Calculator;
import callisto.model.CallistoResult;
import callisto.model.MutationMatrices;
import callisto.model.MutationReport;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class RunCommand {
    public static void run(String[] inputFiles, String outputFile, String solver, boolean useKilledOnly, boolean useStatic) {
        Callisto.validateInput(inputFiles);
        CallistoResult[][] totalResults = new CallistoResult[inputFiles.length][];
        for (int i = 0; i < inputFiles.length; i++) {
            MutationReport report = Callisto.parseReport(inputFiles[i]);
            report.deduceMutationOperators();
            MutationMatrices matrices = report.getMatrices();
            matrices = matrices.minimalize(solver);
            String[] mutationOperators = report.getUsedMutators(); //TODO fix: this will also include mutators which do not have killed mutants (=mistake)
            CallistoResult[] results = new CallistoResult[mutationOperators.length];
            for (int j = 0; j < mutationOperators.length; j++) {
                results[j] = getResultForOperator(mutationOperators[j], report, matrices, solver, useKilledOnly, useStatic);
            }
            totalResults[i] = results;
        }
        CallistoResult[] averagedResult = Calculator.calculateAverageOverReports(totalResults);
        writeToOutput(outputFile, averagedResult);
    }

    private static CallistoResult getResultForOperator(String mutationOperatorName, MutationReport report, MutationMatrices matrices, String solver, boolean useKilledOnly, boolean useStatic) {
        //reduce to matrices containing only the desired mutants from the chosen mutator
        MutationReport.Mutant[] mutants = report.getMutantsByMutatorName(mutationOperatorName, useKilledOnly, useStatic);
        List<Integer> mutantIds = new ArrayList<>();
        for (MutationReport.Mutant mutant : mutants) {
            mutantIds.add(Integer.parseInt(mutant.getId()));
        }
        MutationMatrices mutationOperatorMatrices = matrices.extractColumns(mutantIds.stream().mapToInt(i -> i).toArray());
        //minimalize again for mutator-specific matrices
        mutationOperatorMatrices = mutationOperatorMatrices.minimalize(solver);
        //calculate quality and other stats
        Calculator.CoverageQualityResult result = Calculator.calculateMutationOperatorCoverageQuality(mutationOperatorMatrices);
        double quality = result.quality();
        double deviation = result.deviation();
        int mutantCount = mutationOperatorMatrices.getNumberOfMutants();
        int numberOfTestExecutions = report.countTestExecutions(mutationOperatorName);
        return new CallistoResult(mutationOperatorName, quality, deviation, mutantCount, numberOfTestExecutions);
    }

    private static void writeToOutput(String outputFile, CallistoResult[] results) {
        try (PrintWriter writer = new PrintWriter(outputFile)) {
            for (CallistoResult mutatorResult : results) {
                writer.println(mutatorResult.toString());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
