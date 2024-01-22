package callisto.logic;

import callisto.logic.Calculator.CoverageQualityResult;
import callisto.model.MutationMatrices;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CalculatorTest {
    @Test
    void testQualityCalculation() {
        //arrange
        boolean[][] killMatrix = {
            { true, false, false },
            { false, true, false },
            { false, false, false },
        };
        boolean[][] coverageMatrix = {
            { true, true, true },
            { false, true, false },
            { true, true, true },
        };
        MutationMatrices matrices = new MutationMatrices(killMatrix, coverageMatrix);
        double expected = (double) 8 / 15;

        //act
        CoverageQualityResult result = Calculator.calculateMutationOperatorCoverageQuality(matrices);

        //assert
        assertTrue(Math.abs(expected - result.quality()) < 0.001);
    }

    @Test
    void TestQualityCalculationBig() {
        //arrange
        boolean[][] killMatrix = {
            { true, false, true, false },
            { false, true, false, false },
            { true, true, false, true },
            { false, false, false, true }
        };
        boolean[][] coverageMatrix = {
            { true, true, true, false },
            { false, true, false, true },
            { true, true, true, true },
            { true, false, true, true }
        };
        MutationMatrices matrices = new MutationMatrices(killMatrix, coverageMatrix);
        double expected = (double) 217 / 360;

        //act
        CoverageQualityResult result = Calculator.calculateMutationOperatorCoverageQuality(matrices);

        //assert
        assertTrue(Math.abs(expected - result.quality()) < 0.001);
    }

    @Test
    void TestQualityCalculationEquivalent()
    {
        //arrange
        //all mutants are equivalent (not killed)
        boolean[][] killMatrix = {
            { false, false, false },
            { false, false, false },
            { false, false, false },
        };
        boolean[][] coverageMatrix = {
            { true, true, true },
            { false, true, false },
            { true, true, true },
        };
        MutationMatrices matrices = new MutationMatrices(killMatrix, coverageMatrix);
        double expected = 0;

        //act
        CoverageQualityResult result = Calculator.calculateMutationOperatorCoverageQuality(matrices);

        //assert
        assertTrue(Math.abs(expected - result.quality()) < 0.001);
    }

    @Test
    void TestQualityCalculationHardToReach()
    {
        //arrange
        boolean[][] killMatrix = {
            { true, true, false },
            { false, false, false },
            { false, false, true },
        };
        //all mutants are hard to reach, with MCOV = 4
        boolean[][] coverageMatrix = {
            { true, true, true },
            { false, true, false },
            { false, false, true },
        };
        MutationMatrices matrices = new MutationMatrices(killMatrix, coverageMatrix);
        double expected = (double) 2 / 3;

        //act
        CoverageQualityResult result = Calculator.calculateMutationOperatorCoverageQuality(matrices);

        //assert
        assertTrue(Math.abs(expected - result.quality()) < 0.001);
    }
}
