package callisto.model;

import callisto.Callisto;
import callisto.model.MutationReport.Mutant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URL;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MutationReportTest {
    MutationReport testReport;

    @BeforeEach
    public void Initialize() {
        URL testReportURL = MutationReportTest.class.getClassLoader().getResource("testreport.json");
        testReport = Callisto.parseReport(testReportURL.getPath());
    }

    @Test
    public void TestConstructor() {
        //assert
        assertEquals(27, testReport.getMutants().length);
        assertEquals(24, testReport.getTests().length);
    }

    @Test
    public void TestUsedMutators() {
        //assemble
        String[] trueUsedMutators = new String[] { "StringLiteral", "ArrayDeclaration", "BlockStatement", "UpdateOperator", "ConditionalExpression", "EqualityOperator", "ArrowFunction", "ArithmeticOperator" };
        Arrays.sort(trueUsedMutators);

        //act
        String[] usedMutators = testReport.getUsedMutators();
        Arrays.sort(usedMutators);

        //assert
        assertArrayEquals(trueUsedMutators, usedMutators);
    }

    @Test
    public void TestgetMutantsByMutatorNameAll() {
        //assemble
        String mutator = "StringLiteral";

        //act
        Mutant[] result = testReport.getMutantsByMutatorName(mutator, false, true);

        //assert
        assertEquals(4, result.length);
    }

    @Test
    public void TestgetMutantsByMutatorNameNonStatic() {
        //assemble
        String mutator = "StringLiteral";

        //act
        Mutant[] result = testReport.getMutantsByMutatorName(mutator, false, false);

        //assert
        assertEquals(1, result.length);
    }

    @Test
    public void TestgetMutantsByMutatorNameKilledOnly() {
        //assemble
        String mutator = "EqualityOperator";

        //act
        Mutant[] result = testReport.getMutantsByMutatorName(mutator, true, false);

        //assert
        assertEquals(3, result.length);
    }

    @Test
    public void TestGetMatrices() {
        //assemble

        //act
        MutationMatrices matrices = testReport.getMatrices();

        //assert
        assertEquals(11, matrices.getEquivalentMutants()[0]); //also asserts that rest of mutants are killed
    }

    @Test
    public void TestCoverageMatrix() {
        //act
        MutationMatrices matrices = testReport.getMatrices();

        //assert
        for (int m = 0; m < testReport.getMutants().length; m++)
        {
            int[] killersArray = matrices.getKillersOfMutant(m);
            List<Integer> killers = Arrays.stream(killersArray).boxed().toList();
            int[] coverersArray = matrices.getCoverersOfMutant(m);
            List<Integer> coverers = Arrays.stream(coverersArray).boxed().toList();
            assertTrue(coverers.containsAll(killers));
        }
    }

    @Test
    public void TestDeduceMutationOperators() {
        //arrange
        URL mutationsTestReportURL = MutationReportTest.class.getClassLoader().getResource("report-MutationsTest.json");
        MutationReport mutationsTestReport = Callisto.parseReport(mutationsTestReportURL.getPath());

        //act
        mutationsTestReport.deduceMutationOperators();

        //assert
        assertEquals(1, mutationsTestReport.getMutantsByMutatorName("ArithmeticOperator+To-", false, true).length);
        assertEquals(1, mutationsTestReport.getMutantsByMutatorName("ArithmeticOperator-To+", false, true).length);
        assertEquals(1, mutationsTestReport.getMutantsByMutatorName("ArithmeticOperator*To/", false, true).length);
        assertEquals(1, mutationsTestReport.getMutantsByMutatorName("ArithmeticOperator/To*", false, true).length);
        assertEquals(1, mutationsTestReport.getMutantsByMutatorName("ArithmeticOperator%To*", false, true).length);

        assertEquals(1, mutationsTestReport.getMutantsByMutatorName("ArrayDeclarationEmpty", false, true).length);
        assertEquals(1, mutationsTestReport.getMutantsByMutatorName("ArrayDeclarationEmptyConstructor", false, true).length);
        assertEquals(1, mutationsTestReport.getMutantsByMutatorName("ArrayDeclarationFill", false, true).length);


        assertEquals(1, mutationsTestReport.getMutantsByMutatorName("BooleanLiteralfalseTotrue", false, true).length);
        assertEquals(1, mutationsTestReport.getMutantsByMutatorName("BooleanLiteraltrueTofalse", false, true).length);
        assertEquals(1, mutationsTestReport.getMutantsByMutatorName("BooleanLiteralRemoveNegation", false, true).length);

        assertEquals(4, mutationsTestReport.getMutantsByMutatorName("ConditionalExpression<Tofalse", false, true).length);
        assertEquals(2, mutationsTestReport.getMutantsByMutatorName("ConditionalExpression<Totrue", false, true).length);
        assertEquals(1, mutationsTestReport.getMutantsByMutatorName("ConditionalExpression<=Tofalse", false, true).length);
        assertEquals(1, mutationsTestReport.getMutantsByMutatorName("ConditionalExpression<=Totrue", false, true).length);
        assertEquals(4, mutationsTestReport.getMutantsByMutatorName("ConditionalExpression>Tofalse", false, true).length);
        assertEquals(3, mutationsTestReport.getMutantsByMutatorName("ConditionalExpression>Totrue", false, true).length);
        assertEquals(1, mutationsTestReport.getMutantsByMutatorName("ConditionalExpression!==Tofalse", false, true).length);
        assertEquals(1, mutationsTestReport.getMutantsByMutatorName("ConditionalExpression!==Totrue", false, true).length);
        assertEquals(1, mutationsTestReport.getMutantsByMutatorName("ConditionalExpression===Tofalse", false, true).length);
        assertEquals(1, mutationsTestReport.getMutantsByMutatorName("ConditionalExpression===Totrue", false, true).length);
        assertEquals(2, mutationsTestReport.getMutantsByMutatorName("ConditionalExpression>=Totrue", false, true).length);
        assertEquals(2, mutationsTestReport.getMutantsByMutatorName("ConditionalExpression>=Tofalse", false, true).length);
        assertEquals(1, mutationsTestReport.getMutantsByMutatorName("ConditionalExpressionEmptyCase", false, true).length);

        assertEquals(1, mutationsTestReport.getMutantsByMutatorName("EqualityOperator!==To===", false, true).length);
        assertEquals(1, mutationsTestReport.getMutantsByMutatorName("EqualityOperator!=To==", false, true).length);
        assertEquals(4, mutationsTestReport.getMutantsByMutatorName("EqualityOperator<To<=", false, true).length);
        assertEquals(4, mutationsTestReport.getMutantsByMutatorName("EqualityOperator<To>=", false, true).length);
        assertEquals(1, mutationsTestReport.getMutantsByMutatorName("EqualityOperator<=To<", false, true).length);
        assertEquals(1, mutationsTestReport.getMutantsByMutatorName("EqualityOperator<=To>", false, true).length);
        assertEquals(1, mutationsTestReport.getMutantsByMutatorName("EqualityOperator===To!==", false, true).length);
        assertEquals(2, mutationsTestReport.getMutantsByMutatorName("EqualityOperator==To!=", false, true).length);
        assertEquals(2, mutationsTestReport.getMutantsByMutatorName("EqualityOperator>=To<", false, true).length);
        assertEquals(2, mutationsTestReport.getMutantsByMutatorName("EqualityOperator>=To>", false, true).length);
        assertEquals(4, mutationsTestReport.getMutantsByMutatorName("EqualityOperator>To<=", false, true).length);
        assertEquals(4, mutationsTestReport.getMutantsByMutatorName("EqualityOperator>To>=", false, true).length);

        assertEquals(1, mutationsTestReport.getMutantsByMutatorName("LogicalOperator&&To||", false, true).length);
        assertEquals(1, mutationsTestReport.getMutantsByMutatorName("LogicalOperator||To&&", false, true).length);
        assertEquals(1, mutationsTestReport.getMutantsByMutatorName("LogicalOperator??To&&", false, true).length);

        assertEquals(1, mutationsTestReport.getMutantsByMutatorName("OptionalChaining", false, true).length);

        assertEquals(3, mutationsTestReport.getMutantsByMutatorName("StringLiteralEmpty", false, true).length);
        assertEquals(2, mutationsTestReport.getMutantsByMutatorName("StringLiteralFill", false, true).length);

        assertEquals(1, mutationsTestReport.getMutantsByMutatorName("UnaryOperator-To+", false, true).length);
        assertEquals(1, mutationsTestReport.getMutantsByMutatorName("UnaryOperator+To-", false, true).length);
        assertEquals(1, mutationsTestReport.getMutantsByMutatorName("UnaryOperatorRemove~", false, true).length);

        assertEquals(2, mutationsTestReport.getMutantsByMutatorName("UpdateOperatorPost++To--", false, true).length);
        assertEquals(1, mutationsTestReport.getMutantsByMutatorName("UpdateOperatorPost--To++", false, true).length);
        assertEquals(1, mutationsTestReport.getMutantsByMutatorName("UpdateOperatorPre++To--", false, true).length);
        assertEquals(1, mutationsTestReport.getMutantsByMutatorName("UpdateOperatorPre--To++", false, true).length);
    }

//    @Test
//    public void TestMergeReports()
//    {
//        //arrange
//        MutationReport secondReport = Callisto.parseReport("report-MutationsTest.json");
//        int oldNumberOfTests = testReport.getTests().length;
//        int oldNumberOfMutants = testReport.getMutants().length;
//
//        //act
//        testReport.mergeReports(secondReport);
//
//        //assert
//        assertEquals(oldNumberOfTests + secondReport.getTests().length, testReport.getTests().length);
//        assertEquals(oldNumberOfMutants + secondReport.getMutants().length, testReport.getMutants().length);
//    }
}
