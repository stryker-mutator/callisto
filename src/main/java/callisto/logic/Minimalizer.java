package callisto.logic;

import com.google.ortools.Loader;
import com.google.ortools.linearsolver.MPConstraint;
import com.google.ortools.linearsolver.MPObjective;
import com.google.ortools.linearsolver.MPSolver;
import com.google.ortools.linearsolver.MPVariable;

import java.util.ArrayList;
import java.util.List;

public class Minimalizer {
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
            MPConstraint c = solver.makeConstraint(1, Double.MAX_VALUE);
            for (int t = 0; t < T; t++) {
                c.setCoefficient(x[t], matrix[t][m] ? 1 : 0);
            }
        }
        //Console.WriteLine("Number of variables = " + solver.numVariables());
        //Console.WriteLine("Number of constraints = " + solver.numConstraints());
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
}
