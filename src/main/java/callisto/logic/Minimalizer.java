package callisto.logic;

import com.google.ortools.Loader;
import com.google.ortools.linearsolver.MPConstraint;
import com.google.ortools.linearsolver.MPObjective;
import com.google.ortools.linearsolver.MPSolver;
import com.google.ortools.linearsolver.MPVariable;

import java.util.ArrayList;
import java.util.List;

public class Minimalizer {
    /**
     * Upper bound for the contraints during minimalizing. This value should always be higher than the test suite size.
     * Setting {@code Double.MAX_VALUE} will break the solver. Value of 1 million is deemed high enough.
     */
    private static final double CONSTRAINT_UPPER_BOUND = Math.pow(10.0, 6.0);

    public static int[] minimalize(boolean[][] matrix) {
        return minimalize(matrix, "GLOP");
    }

    public static int[] minimalize(boolean[][] matrix, String solverType) {
        int T = matrix.length;
        int M = matrix[0].length;
        Loader.loadNativeLibraries();
        MPSolver solver = MPSolver.createSolver(solverType);
        MPVariable[] x = solver.makeBoolVarArray(T);
        for (int m = 0; m < M; m++) {
            // check to see if this mutant is equivalent
            boolean isEquivalent = true;
            for (int i = 0; i < T; i++) {
                if (matrix[i][m]) {
                    isEquivalent = false;
                    break;
                }
            }
            // no constraint for equivalent mutants, so they are ignored
            if (isEquivalent) {
                continue;
            }
            MPConstraint constraint = solver.makeConstraint(1, CONSTRAINT_UPPER_BOUND);
            for (int t = 0; t < T; t++) {
                constraint.setCoefficient(x[t], matrix[t][m] ? 1 : 0);
            }
        }
        MPObjective objective = solver.objective();
        for (int t = 0; t < T; t++) {
            objective.setCoefficient(x[t], 1);
        }
        objective.setMinimization();

        MPSolver.ResultStatus resultStatus = solver.solve();

        // Check that the problem has an optimal solution.
        if (resultStatus != MPSolver.ResultStatus.OPTIMAL) {
            System.out.println("The problem does not have an optimal solution!");
            return new int[] {};
        }
        List<Integer> result = new ArrayList<>();
        boolean warning = false;
        for (int t = 0; t < T; t++) {
            if (!warning && x[t].solutionValue() != 0 && x[t].solutionValue() != 1) {
                System.err.printf("WARNING: Indecisive minimalization occurred. Decision variable of value %.4f. Use solver 'SAT' to prevent%n", x[t].solutionValue());
                warning = true;
            }
            if (x[t].solutionValue() == 0.0) {
                result.add(t);
            }
        }
        return result.stream().mapToInt(i -> i).toArray();
    }
}
