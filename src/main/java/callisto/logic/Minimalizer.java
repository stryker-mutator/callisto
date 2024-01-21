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
//        System.out.println("Number of variables = " + solver.numVariables());
//        System.out.println("Number of constraints = " + solver.numConstraints());
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

        //Console.WriteLine($"Number of tests removed = {T - solver.objective().value()}");
        List<Integer> result = new ArrayList<>();
        boolean warning = false;
        for (int t = 0; t < T; t++) {
            if (!warning && x[t].solutionValue() != 0 && x[t].solutionValue() != 1) {
                System.err.printf("WARNING: Indecisive minimalization occurred. Decision variable of value %.4f. Use solver 'SAT' to prevent%n", x[t].solutionValue());
                warning = true;
            }
            //Console.WriteLine(x[t].SolutionValue());
            if (x[t].solutionValue() == 0.0) {
                result.add(t);
            }
        }
        //Console.WriteLine($"Time taken: {solver.WallTime()} ms");
        return result.stream().mapToInt(i -> i).toArray();
    }


    public static boolean simpleLPProblem() {
        Loader.loadNativeLibraries();
        MPSolver solver = MPSolver.createSolver("GLOP");

        double infinity = java.lang.Double.POSITIVE_INFINITY;
        // x and y are continuous non-negative variables.
        MPVariable x = solver.makeNumVar(0.0, infinity, "x");
        MPVariable y = solver.makeNumVar(0.0, infinity, "y");

        // x + 2*y <= 14.
        MPConstraint c0 = solver.makeConstraint(-infinity, 14.0, "c0");
        c0.setCoefficient(x, 1);
        c0.setCoefficient(y, 2);

        // 3*x - y >= 0.
        MPConstraint c1 = solver.makeConstraint(0.0, infinity, "c1");
        c1.setCoefficient(x, 3);
        c1.setCoefficient(y, -1);

        // x - y <= 2.
        MPConstraint c2 = solver.makeConstraint(-infinity, 2.0, "c2");
        c2.setCoefficient(x, 1);
        c2.setCoefficient(y, -1);

        // Maximize 3 * x + 4 * y.
        MPObjective objective = solver.objective();
        objective.setCoefficient(x, 3);
        objective.setCoefficient(y, 4);
        objective.setMaximization();

        final MPSolver.ResultStatus resultStatus = solver.solve();

        if (resultStatus == MPSolver.ResultStatus.OPTIMAL) {
            if (Math.round(x.solutionValue()) == 6 && Math.round(y.solutionValue()) == 4 && Math.round(objective.value()) == 34) {
                return true;
            } else {
                System.out.println("Solution is incorrect!");
                return false;
            }
        } else {
            System.err.println("The problem does not have an optimal solution!");
            return false;
        }
    }
}
