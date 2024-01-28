package callisto.commands;

import callisto.Callisto;
import callisto.model.MutationMatrices;
import callisto.model.MutationReport;
import callisto.model.MutationReport.Mutant;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class TestCommand {
    public static void run(String levelFile, String inputFile, String outputFile, String solver, boolean verbose) {
        Callisto.validateInput(new String[] { levelFile, inputFile } );
        String[] levelOperators = getMutationOperators(levelFile);
        if (verbose) {
            System.out.println("Testing effectiveness of level with mutation operators:");
            for (String mutOperator : levelOperators) {
                System.out.println("  " + mutOperator);
            }
        }
        System.out.println("Processing inputfile " + inputFile);
        if (verbose) {
            System.out.println("Parsing json");
        }
        MutationReport report = Callisto.parseReport(inputFile);

        report.deduceMutationOperators();
        String[] mutators = report.getUsedMutators();
        int totalTestExecutions = 0;
        for (String mutator : mutators) {
            totalTestExecutions += report.countTestExecutions(mutator);
        }
        if (verbose) {
            System.out.println("Extracting matrices");
        }
        MutationMatrices matrices = report.getMatrices();
        int totalMutants = matrices.getNumberOfMutants();
        if (verbose) {
            System.out.println("Determining minimal test suite size using all operators");
            System.out.println("  Minimalizing test suite");
        }
        MutationMatrices matricesAll = matrices.minimalize(solver);
        int testSuiteSizeAll = matricesAll.getNumberOfTests();
        if (verbose) {
            System.out.println("  Minimal test suite size: " + testSuiteSizeAll);
            System.out.println("Determining minimal test suite size using level operators");
            System.out.println("  Selecting relevant mutants");
        }
        List<Mutant> allMutants = new ArrayList<>();
        for (String levelOperator : levelOperators) {
            if (Arrays.stream(mutators).noneMatch(x -> x.equals(levelOperator))) {
                System.out.printf("WARNING: mutation operator %s does not occur in %s\n", levelOperator, inputFile);
                continue;
            }
            // using all mutants, so killedOnly = false & useStatic = true
            allMutants.addAll(Arrays.asList(report.getMutantsByMutatorName(levelOperator, false, true)));
        }
        int levelMutants = allMutants.size();
        int levelTestExecutions = 0;
        List<Mutant> mutants = new ArrayList<>();
        for (String levelOperator : levelOperators) {
            // We're going to minimize based on the selected mutants here, so including unkilled mutants makes no sense (they have no influence on minimizing)
            mutants.addAll(Arrays.asList(report.getMutantsByMutatorName(levelOperator, true, true)));
            levelTestExecutions += report.countTestExecutions(levelOperator);
        }
        List<Integer> mutantIds = new ArrayList<>();
        for (Mutant mutant : mutants) {
            mutantIds.add(Integer.parseInt(mutant.getId()));
        }
        MutationMatrices matricesLevel = matrices.extractColumns(mutantIds.stream().mapToInt(i -> i).toArray());
        if (verbose) {
            double reduction = (((double) (totalMutants - levelMutants) / totalMutants) * 100);
            System.out.printf("  Kept %d out of %d mutants. Reduction of %.0f%% (%d mutants)\n", levelMutants, totalMutants, reduction, totalMutants - levelMutants);
            System.out.println("  Minimalizing test suite");
        }
        matricesLevel = matricesLevel.minimalize(solver);
        int testSuiteSizeLevel = matricesLevel.getNumberOfTests();
        if (verbose) {
            System.out.println("  Minimal test suite size: " +  testSuiteSizeLevel);
            double effectiveness = (((double)testSuiteSizeLevel / testSuiteSizeAll) * 100);
            System.out.printf("Mutation level effectiveness: %.0f%%\n", effectiveness);
            System.out.println("Writing result to " + outputFile);
        }
        try (PrintWriter printWriter = new PrintWriter(new FileWriter(outputFile))) {
            printWriter.println("Mutation level consisting of operators:");
            for (String levelOperator : levelOperators) {
                printWriter.println("  " + levelOperator);
            }
            double effectiveness = (((double)testSuiteSizeLevel / testSuiteSizeAll) * 100);
            printWriter.printf("Tested using program %s\n", inputFile);
            printWriter.printf("Mutation level effectiveness: %.0f%%: %d / %d  (level minimal test suite size / all operators minimal test suite size)\n", effectiveness, testSuiteSizeLevel, testSuiteSizeAll);

            double mutantReduction = (((double)(totalMutants - levelMutants) / totalMutants) * 100);
            printWriter.printf("Kept %d out of %d mutants. Reduction of %.0f%% (%d mutants)\n", levelMutants, totalMutants, mutantReduction, totalMutants - levelMutants);
            double executionReduction = (((double)(totalTestExecutions - levelTestExecutions) / totalTestExecutions) * 100);
            printWriter.printf("Kept %d out of %d test executions. Reduction of %.0f%% (%d test executions)\n", levelTestExecutions, totalTestExecutions, executionReduction, totalTestExecutions - levelTestExecutions);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String[] getMutationOperators(String levelFile) {
        try (Stream<String> stream = Files.lines(Paths.get(levelFile))) {
            return stream.toArray(String[]::new);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
