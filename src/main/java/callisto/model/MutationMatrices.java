package callisto.model;

import callisto.logic.Calculator;
import callisto.logic.Minimalizer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class MutationMatrices {
    public static final int MCOV = 4;
    private final boolean[][] killMatrix;

    private final boolean[][] coverageMatrix;

    private final int numberOfMutants;

    private final int numberOfTests;

    public MutationMatrices(boolean[][] killMatrix, boolean[][] coverageMatrix) {
        this.killMatrix = killMatrix;
        this.coverageMatrix = coverageMatrix;
        this.numberOfMutants = killMatrix[0].length; //take any row to see the number of columns
        this.numberOfTests = killMatrix.length;
    }

    public boolean[][] getKillMatrix() {
        return killMatrix;
    }

    public boolean[][] getCoverageMatrix() {
        return coverageMatrix;
    }

    public int getNumberOfMutants() {
        return numberOfMutants;
    }

    public int getNumberOfTests() {
        return numberOfTests;
    }

    public MutationMatrices minimalize(String solver) {
        MutationMatrices noDuplicates = removeDuplicateTests();
        int[] rowsToRemove = Minimalizer.minimalize(killMatrix, solver);
        return noDuplicates.removeRows(rowsToRemove);
    }

    public MutationMatrices removeDuplicateTests() {
        List<Integer> duplicateTests = new ArrayList<>();
        for (int test1 = 0; test1 < numberOfTests; test1++) {
            //don't check a test that is marked duplicate already
            if (!duplicateTests.contains(test1)) {
                for (int test2 = test1 + 1; test2 < numberOfTests; test2++) {
                    // no need to mark a test duplicate if it already is
                    if (duplicateTests.contains(test2)) {
                        continue;
                    }
                    // check if test1 and test2 are equal:
                    for (int m = 0; m < numberOfMutants; m++) {
                        if (killMatrix[test1][m] != killMatrix[test2][m]) {
                            break; //difference detected, not a duplicate
                        }
                        //reached end of loop, no differences detected, so duplicate!
                        if (m == numberOfMutants - 1) {
                            duplicateTests.add(test2);
                        }
                    }
                }
            }
        }
        return removeRows(duplicateTests.stream().mapToInt(i -> i).toArray());
    }

    public MutationMatrices removeRows(int[] rowsToRemove) {
        if (rowsToRemove.length == 0) {
            return this;
        }
        boolean[][] newKillMatrix = new boolean[numberOfTests - rowsToRemove.length][numberOfMutants];
        boolean[][] newCoverageMatrix = new boolean[numberOfTests - rowsToRemove.length][numberOfMutants];
        int r = 0;
        for (int i = 0; i < numberOfTests; i++) {
            if (Calculator.intArrayContains(rowsToRemove, i)) {
                continue;
            }
            for (int m = 0; m < numberOfMutants; m++) {
                newKillMatrix[r][m] = killMatrix[i][m];
                newCoverageMatrix[r][m] = coverageMatrix[i][m];
            }
            r++;
        }
        return new MutationMatrices(newKillMatrix, newCoverageMatrix);
    }

    public MutationMatrices extractColumns(int[] columnsToKeep) {
        boolean[][] newKillMatrix = new boolean[numberOfTests][columnsToKeep.length];
        boolean[][] newCoverageMatrix = new boolean[numberOfTests][columnsToKeep.length];
        for (int t = 0; t < numberOfTests; t++) {
            int c = 0;
            for (int j = 0; j < numberOfMutants; j++) {
                if (!Calculator.intArrayContains(columnsToKeep, j)) {
                    continue;
                }
                newKillMatrix[t][c] = killMatrix[t][j];
                newCoverageMatrix[t][c] = coverageMatrix[t][j];
                c++;
            }
        }
        return new MutationMatrices(newKillMatrix, newCoverageMatrix);
    }

    public int[] getEquivalentMutants() {
        List<Integer> result = new ArrayList<>();
        for (int m = 0; m < numberOfMutants; m++) {
            //everything which is not killed is assumed to be equivalent (i.e. adequate test suite)
            if (!isKilled(m)) {
                result.add(m);
            }
        }
        return result.stream().mapToInt(i -> i).toArray();
    }

    public boolean isKilled(int mutant) {
        for (int i = 0; i < numberOfTests; i++) {
            if (killMatrix[i][mutant]){
                return true;
            }
        }
        return false;
    }

    public int[] getKillersOfMutant(int mutant) {
        List<Integer> result = new ArrayList<>();
        for (int test = 0; test < numberOfTests; test++) {
            if (killMatrix[test][mutant]){
                result.add(test);
            }
        }
        return result.stream().mapToInt(i -> i).toArray();
    }

    public int[] getCoverersOfMutant(int mutant) {
        List<Integer> result = new ArrayList<>();
        for (int test = 0; test < numberOfTests; test++) {
            if (coverageMatrix[test][mutant]){
                result.add(test);
            }
        }
        return result.stream().mapToInt(i -> i).toArray();
    }

    public int[] getMutantsKilledByTest(int test) {
        List<Integer> result = new ArrayList<>();
        for (int mutant = 0; mutant < numberOfMutants; mutant++) {
            if (killMatrix[test][mutant]) {
                result.add(mutant);
            }
        }
        return result.stream().mapToInt(i -> i).toArray();
    }

    public int[] getMutantsCoveredByTest(int test) {
        List<Integer> result = new ArrayList<>();
        for (int mutant = 0; mutant < numberOfMutants; mutant++) {
            if (coverageMatrix[test][mutant]) {
                result.add(mutant);
            }
        }
        return result.stream().mapToInt(i -> i).toArray();
    }

    public boolean isMutantDifficultToReach(int mutant) {
        int sum = 0;
        int[] coverersOfMutant = getCoverersOfMutant(mutant);
        int[] equivalentMutants = getEquivalentMutants();
        for (int test : coverersOfMutant) {
            sum += (int) Arrays.stream(getMutantsCoveredByTest(test)).filter(m -> Calculator.intArrayContains(equivalentMutants, m)).count();
        }
        return sum <= MCOV;
    }
}
