package callisto.logic;

import com.google.ortools.Loader;
import com.google.ortools.linearsolver.MPConstraint;
import com.google.ortools.linearsolver.MPObjective;
import com.google.ortools.linearsolver.MPSolver;
import com.google.ortools.linearsolver.MPVariable;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class MinimalizerTest {
    @Test
    void TestSimpleMinimalize() {
        // arrange
        boolean[][] matrix = {
            { true, false, false, false, false, false, false},
            { false, true, false, false, false, false, false},
            { false, false, true, false, false, false, false},
            { false, false, false, true, false, false, false},
            { false, false, false, false, true, false, false},
            { false, false, false, false, false, true, false},
            { false, false, false, false, false, false, true},
            { true, true, true, true, true, true, true},
        };

        //act
        int[] redundantRows = Minimalizer.minimalize(matrix);

        //assert
        assertArrayEquals(new int[] { 0, 1, 2, 3, 4, 5, 6 }, redundantRows);
    }

        @Test
    void TestTwoSolutions() {
        //arrange
        boolean[][] matrix = {
            { true, true, true, false },
            { false, true, false, true },
            { false, false, true, true }

        };

        //act
        int[] redundantRows = Minimalizer.minimalize(matrix);

        //assert
        assertEquals(1, redundantRows.length);
        assertNotEquals(0, redundantRows[0]);
        //assertArrayEquals(new int[] { 1 }, redundantRows);
    }

        @Test
    void TestThreeSolutions() {
        //arrange
        boolean[][] matrix = {
            { true, true, true, true, false },
            { false, true, false, false, true },
            { false, false, true, false, true },
            { false, false, false, true, true,},
        };

        //act
        int[] redundantRows = Minimalizer.minimalize(matrix);

        //assert
        assertEquals(2, redundantRows.length);
        assertFalse(Arrays.stream(redundantRows).anyMatch(x -> x == 0));
        //assertArrayEquals(new int[] { 2, 3 }, redundantRows);
    }

        @Test
    void TestFourSolutions() {
        //arrange
        boolean[][] matrix = {
            { true, true, true, true, true, false },
            { false, true, false, false, false, true },
            { false, false, true, false, false, true },
            { false, false, false, true, false, true,},
            { false, false, false, false, true, true,}
        };

        //act
        int[] redundantRows = Minimalizer.minimalize(matrix);

        //assert
        assertEquals(3, redundantRows.length);
        assertFalse(Arrays.stream(redundantRows).anyMatch(x -> x == 0));
        //assertArrayEquals(new int[] { 2, 3, 4 }, redundantRows);
    }

        @Test
    void TestDualSolution() {
        //arrange
        boolean[][] matrix = {
            { true, true, true, false, false, false },
            { false, false, false, true, true, true },
            { true, false, true, false, true, false },
            { false, true, false, true, false, true,},
        };

        //act
        int[] redundantRows = Minimalizer.minimalize(matrix);

        //assert
        assertEquals(2, redundantRows.length);
        assertTrue((Arrays.stream(redundantRows).anyMatch(x -> x == 0) && Arrays.stream(redundantRows).anyMatch(x -> x == 1) || (Arrays.stream(redundantRows).anyMatch(x -> x == 2) && Arrays.stream(redundantRows).anyMatch(x -> x == 3))));
        //assertArrayEquals(new int[] { 2, 3 }, redundantRows);
    }

        @Test
    void TestThreewaySolution() {
        //arrange
        boolean[][] matrix = {
            { true, false, false, false, false, false, true},
            { true, true, false, false, true, false, false },
            { false, false, true, true, false, true, false},
            { false, true, false, true, true, false, true}
        };

        //act
        int[] redundantRows = Minimalizer.minimalize(matrix, "SAT");
        //foreach (var t in redundantRows)
        //{
        //    Console.WriteLine(t);
        //}

        //assert
        assertEquals(1, redundantRows.length);
        //assertArrayEquals(new int[] { 3 }, redundantRows);
    }

        @Test
    void TestFourwaySolution() {
        //arrange
        boolean[][] matrix = {
            { true, true, true, false },
            { true, true, false, true },
            { true, false, true, true },
            { false, true, true, true },
        };

        //act
        int[] redundantRows = Minimalizer.minimalize(matrix, "SAT");

        //assert
        assertEquals(2, redundantRows.length);
        //assertArrayEquals(new int[] { 2, 3 }, redundantRows);
    }

        @Test
    void TestFivewaySolution() {
        //arrange
        boolean[][] matrix = {
            { true, true, true, true, false },
            { true, true, true, false, true },
            { true, true, false, true, true },
            { true, false, true, true, true },
            { false, true, true, true, true }
        };

        //act
        int[] redundantRows = Minimalizer.minimalize(matrix, "SAT");

        //assert
        assertEquals(3, redundantRows.length);
        //assertArrayEquals(new int[] { 2, 3, 4 }, redundantRows);
    }

        @Test
    void TestThreewayAndFourwaySolution() {
        //arrange
        boolean[][] matrix = {
            { true, true, false, false, false, false, false },
            { true, false, true, false, false, false, false },
            { false, true, true, false, false, false, false },
            { false, false, false, true, true, true, false },
            { false, false, false, true, true, false, true },
            { false, false, false, true, false, true, true },
            { false, false, false, false, true, true, true }
        };

        //act
        int[] redundantRows = Minimalizer.minimalize(matrix, "SAT");

        //assert
        assertEquals(3, redundantRows.length);
        //assertArrayEquals(new int[] { 2, 5, 6 }, redundantRows);
    }

        @Test
    void TestStrange() {
        //arrange
        boolean[][] matrix = {
            { true, true, false, false, false },
            { false, false, true, true, true },
            { true, true, false, true, true },
            { true, true, true, true, false },
            { true, false, false, true, true }
        };

        //act
        int[] redundantRows = Minimalizer.minimalize(matrix, "SAT");

        //assert
        assertEquals(3, redundantRows.length);
    }

    @Test
    void simpleLPProblemTest() {
        //act
        final boolean result = simpleLPProblem();

        //assert
        assertTrue(result);
    }

    private static boolean simpleLPProblem() {
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
