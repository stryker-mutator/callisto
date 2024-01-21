package callisto.logic;

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
        final boolean result = Minimalizer.simpleLPProblem();

        //assert
        assertTrue(result);
    }
}
