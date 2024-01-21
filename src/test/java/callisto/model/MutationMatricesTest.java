package callisto.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class MutationMatricesTest {
    MutationMatrices testMatrices;
    boolean[][] killMatrix;
    boolean[][] coverageMatrix;

    @BeforeEach
    void Initialize() {
        killMatrix = new boolean[][] {
            { true, false, false, false, false, false, true},
            { true, true, false, false, true, false, false },
            { false, false, true, true, false, true, false},
            { false, false, false, true, false, false, false},
            { false, true, false, true, true, false, true}
        };
        coverageMatrix = new boolean[][] {
            { true, false, false, true, true, false, true},
            { true, true, false, false, true, true, false },
            { true, false, true, true, false, true, true},
            { false, true, true, true, false, true, false},
            { true, true, false, true, true, true, true}
        };
        testMatrices = new MutationMatrices(killMatrix, coverageMatrix);
    }

    @Test
    void TestConstructor() {
        //assert
        assertTrue(Arrays.deepEquals(killMatrix, testMatrices.getKillMatrix()));
        assertTrue(Arrays.deepEquals(coverageMatrix, testMatrices.getCoverageMatrix()));
        assertEquals(5, testMatrices.getNumberOfTests());
        assertEquals(7, testMatrices.getNumberOfMutants());
    }

    @Test
    void TestKillersOfMutant() {
        //act
        int[] killers = testMatrices.getKillersOfMutant(0);

        //assert
        assertArrayEquals(new int[] { 0, 1 }, killers);
    }

    @Test
    public void TestCoverersOfMutant() {
        //act
        int[] coverers = testMatrices.getCoverersOfMutant(0);

        //assert
        assertArrayEquals(new int[] { 0, 1, 2, 4}, coverers);
    }

    @Test
    void TestMutantsKilledByTest() {
        //act
        int[] killed = testMatrices.getMutantsKilledByTest(0);

        //assert
        assertArrayEquals(new int[] { 0, 6 }, killed);
    }

    @Test
    void TestMutantsCoveredByTest() {
        //act
        int[] covered = testMatrices.getMutantsCoveredByTest(0);

        //assert
        assertArrayEquals(new int[] { 0, 3, 4, 6 }, covered);
    }

    @Test
    void TestIsMutantDifficultToReach() {
        //arrange
        boolean[][] smallKillMatrix = {
            { false, true, false},
            { true, false, false},
            { false, false, true},
        };
        boolean[][] smallCoverageMatrix = {
            { false, true, true},
            { true, true, true},
            { false, true, true},
        };
        MutationMatrices smallMatrices = new MutationMatrices(smallKillMatrix, smallCoverageMatrix);

        //assert
        assertTrue(smallMatrices.isMutantDifficultToReach(0));
        assertFalse(smallMatrices.isMutantDifficultToReach(1));
        assertFalse(smallMatrices.isMutantDifficultToReach(2));
    }

    @Test
    void TestRemoveRows() {
        //arrange
        int[] rowsToRemove = { 1, 3 };
        boolean[][] expectedKillMatrix = {
            { true, false, false, false, false, false, true},
            { false, false, true, true, false, true, false},
            { false, true, false, true, true, false, true}
        };
        boolean[][] expectedCoverageMatrix = {
            { true, false, false, true, true, false, true},
            { true, false, true, true, false, true, true},
            { true, true, false, true, true, true, true}
        };
        //act
        MutationMatrices reducedMatrices = testMatrices.removeRows(rowsToRemove);

        //assert
        assertTrue(Arrays.deepEquals(expectedKillMatrix, reducedMatrices.getKillMatrix()));
        assertTrue(Arrays.deepEquals(expectedCoverageMatrix, reducedMatrices.getCoverageMatrix()));
    }

    @Test
    void TestExtractColumns() {
        //arrange
        int[] columnsToKeep = { 1, 3 };
        boolean[][] expectedKillMatrix = {
            { false, false },
            { true, false },
            { false, true },
            { false, true },
            { true, true }
        };
        boolean[][] expectedCoverageMatrix = {
            { false, true },
            { true, false },
            { false, true },
            { true, true },
            { true, true }
        };
        //act
        MutationMatrices reducedMatrices = testMatrices.extractColumns(columnsToKeep);

        //assert
        assertTrue(Arrays.deepEquals(expectedKillMatrix, reducedMatrices.getKillMatrix()));
        assertTrue(Arrays.deepEquals(expectedCoverageMatrix, reducedMatrices.getCoverageMatrix()));
    }

    @Test
    void TestRemoveDuplicateTests() {
        //arrange
        boolean[][] smallKillMatrix = {
            { false, true, false },
            { true, false, false },
            { false, false, true },
            { true, false, false },
            { false, false, true },
            { true, false, false },
            { false, false, true },
            { true, false, false },
            { false, false, true },
        };
        boolean[][] smallCoverageMatrix = {
            { false, true, true },
            { true, true, true },
            { false, true, true },
            { true, false, false },
            { false, false, true },
            { true, false, false },
            { false, false, true },
            { true, false, false },
            { false, false, true },
        };
        MutationMatrices smallMatrices = new MutationMatrices(smallKillMatrix, smallCoverageMatrix);
        boolean[][] expectedKillMatrix = {
            { false, true, false },
            { true, false, false },
            { false, false, true },
        };

        //act
        MutationMatrices reducedMatrices = smallMatrices.removeDuplicateTests();

        //assert
        assertTrue(Arrays.deepEquals(expectedKillMatrix, reducedMatrices.getKillMatrix()));
    }

    @Test
    void TestIsKilled() {
        //assert
        for (int m = 0; m < testMatrices.getNumberOfMutants(); m++)
        {
            assertTrue(testMatrices.isKilled(m));
        }
    }

    @Test
    void TestGetEquivalentMutants() {
        //arrange
        boolean[][] smallKillMatrix = {
            { true, true, false, false, false, true, false },
            { true, false, false, true, false, false, false },
        };
        boolean[][] smallCoverageMatrix = {
            { false, true, true, true, true, true, true },
            { true, true, true, true, true, true, true },
        };
        MutationMatrices smallMatrices = new MutationMatrices(smallKillMatrix, smallCoverageMatrix);
        int[] expected = { 2, 4, 6 };

        //act
        int[] equivalentMutants = smallMatrices.getEquivalentMutants();

        //assert
        assertArrayEquals(expected, equivalentMutants);
    }
}
