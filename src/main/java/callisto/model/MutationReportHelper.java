package callisto.model;

import callisto.model.MutationReport.Mutant;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class MutationReportHelper {
    private static final String[] ARITHMETIC_OPERATORS = { "+", "-", "*", "/", "%"};
    private static final String[] COMPARATOR_OPERATORS = { "<", "<=", ">", ">=", "=", "!"};
    private static final String[] LOGICAL_OPERATORS = { "&&", "||", "??" };
    private static final String[] UNARY_OPERATORS = { "+", "-" };

    public static String[] filterSurvivedMutationOperators(String[] mutationOperators, MutationReport report, MutationMatrices matrices, boolean killedOnly, boolean useStatic) {
        int[] equivalentMutants = matrices.getEquivalentMutants();
        return Arrays.stream(mutationOperators)
                .filter(mutationOperator -> {
                    boolean filter = mutationOperatorHasKilledMutants(report.getMutantsByMutatorName(mutationOperator, killedOnly, useStatic), equivalentMutants);
                    if (!filter) {
                        System.out.println("Mutation Operator " + mutationOperator + " has no killed mutants, and therefore has no coverage quality.");
                    }
                    return filter;
                })
                .toArray(String[]::new);
    }

    private static boolean mutationOperatorHasKilledMutants(Mutant[] mutants, int[] equivalentMutants) {
        List<Integer> mutantsList = Arrays.stream(mutants).mapToInt(mutant -> Integer.parseInt(mutant.getId())).boxed().toList();
        List<Integer> equivalentList = Arrays.stream(equivalentMutants).boxed().toList();
        return !new HashSet<>(equivalentList).containsAll(mutantsList);
    }
    
    public static String deduceMutationOperatorName(Mutant mutant, String sourceCode) {
        String replacement = mutant.getReplacement();
        String original = findCode(sourceCode, mutant.getLocation());
        return deduceMutationOperatorName(mutant.getMutatorName(), original, replacement);
    }

    private static String deduceMutationOperatorName(String mutator, String original, String replacement) {
        return switch (mutator) {
            case "ArithmeticOperator" -> getArithmeticOperatorName(original, replacement);
            case "ArrayDeclaration" -> getArrayDeclarationOperatorName(original, replacement);
            case "AssignmentExpression" -> getAssignmentExpressionOperatorName(original, replacement);
            case "BooleanLiteral" -> getBooleanLiteralOperatorName(original, replacement);
            case "ConditionalExpression" -> getConditionalExpressionOperatorName(original, replacement);
            case "EqualityOperator" -> getEqualityOperatorName(original, replacement);
            case "LogicalOperator" -> getLogicalOperatorName(original, replacement);
            case "StringLiteral" -> getStringLiteralOperatorName(original, replacement);
            case "UnaryOperator" -> getUnaryOperatorName(original, replacement);
            case "UpdateOperator" -> getUpdateOperatorName(original, replacement);
            // ArrowFunction, BlockStatement, ObjectLiteral only consist of one possible mutation, so no further distinction needed
            // OptionalChaining consists of three very similar mutations, so no further distinction is made
            // Regex is its own language with many mutation operators, thus is untouched for now for complexity reasons.
            case "ArrowFunction", "BlockStatement", "ObjectLiteral", "OptionalChaining", "Regex" -> mutator;
            default -> {
                System.out.printf("Unknown mutator: %s - Unknown mutation: %s -> %s", mutator, original, replacement);
                yield mutator; //by default use mutator name
            }
        };
    }
    
    private static String getArithmeticOperatorName(String original, String replacement) {
        StringDifference difference = stringDifference(original, replacement);
        boolean originalIsKnown = Arrays.stream(ARITHMETIC_OPERATORS).anyMatch(x -> x.equals(difference.original));
        boolean replacementIsKnown = Arrays.stream(ARITHMETIC_OPERATORS).anyMatch(x -> x.equals(difference.replacement));
        if (!(originalIsKnown && replacementIsKnown)) {
            System.out.printf("Unknown arithmetic mutation operator: %s -> %s\n", original, replacement);
            return "ArithmeticOperatorUnknown";
        } else {
            return "ArithmeticOperator%sTo%s".formatted(difference.original, difference.replacement);
        }
    }
    
    private static String getArrayDeclarationOperatorName(String original, String replacement) {
        switch (replacement) {
            case "[]", "Array()" -> {
                return "ArrayDeclarationEmpty";
            }
            case "new Array()", "new Array([])" -> {
                return "ArrayDeclarationEmptyConstructor";
            }
            case "[\"Stryker was here\"]" -> {
                return "ArrayDeclarationFill";
            }
            default -> {
                System.out.printf("ArrayDeclaration - Unknown mutation: %s -> %s\n", original, replacement);
                return "ArrayDeclarationUnknown";
            }
        }
    } 
    
    private static String getAssignmentExpressionOperatorName(String original, String replacement) {
        StringDifference difference = stringDifference(original, replacement);
        String originalDifference = difference.original;
        String replacementDifference = difference.replacement;
        if (originalDifference.equals("+") && replacementDifference.equals("-")) {
            return "AssignmentExpression+=To-=";
        }
        else if (originalDifference.equals("-") && replacementDifference.equals("+")) {
            return "AssignmentExpression-=To+=";
        }
        else if (originalDifference.equals("*") && replacementDifference.equals("/")) {
            return "AssignmentExpression*=To/=";
        }
        else if (originalDifference.equals("/") && replacementDifference.equals("*")) {
            return "AssignmentExpression/=To*=";
        }
        else if (originalDifference.equals("%") && replacementDifference.equals("*")) {
            return "AssignmentExpression%=To*=";
        }
        else if (originalDifference.equals("<<") && replacementDifference.equals(">>")) {
            return "AssignmentExpression<<=To>>=";
        }
        else if (originalDifference.equals(">>") && replacementDifference.equals("<<")) {
            return "AssignmentExpression>>=To<<=";
        }
        else if (originalDifference.equals("&") && replacementDifference.equals("\\|")) {
            return "AssignmentExpression&=To\\|=";
        }
        else {
            System.out.printf("AssignmentExpression - Unknown mutation: %s -> %s\n", original, replacement);
            return "AssignmentExpressionUnknown";
        }
    } 
    
    private static String getBooleanLiteralOperatorName(String original, String replacement) {
        if (replacement.equals("true")) {
            return "BooleanLiteralfalseTotrue";
        }
        else if (replacement.equals("false")) {
            return "BooleanLiteraltrueTofalse";
        }
        else if (original.contains("!")) {
            return "BooleanLiteralRemoveNegation";
        }
        else {
            System.out.printf("BooleanLiteral - Unknown mutation: %s -> %s\n", original, replacement);
            return "BooleanLiteralUnknown";
        }
    } 
    
    private static String getConditionalExpressionOperatorName(String original, String replacement) {
        if (replacement.startsWith("case") || replacement.startsWith("default")) {
            return "ConditionalExpressionEmptyCase";
        }
        if (!replacement.equals("true") && !replacement.equals("false")) {
            StringDifference difference = stringDifference(original, replacement);
            if (difference.replacement.contains("true")) {
                return "ConditionalExpressionConditionTotrue";
            }
            else if (difference.replacement.contains("false")) {
                return "ConditionalExpressionConditionTofalse";
            }
            else {
                System.out.printf("ConditionalExpression - Unknown mutation: %s -> %s\n", original, replacement);
                return "ConditionalExpressionUnknown";
            }
        }
        if (original.contains("<=")) {
            return "ConditionalExpression<=To" + replacement;
        }
        else if (original.contains(">=")) {
            return "ConditionalExpression>=To" + replacement;
        }
        else if (original.contains(">")) {
            return "ConditionalExpression>To" + replacement;
        }
        else if (original.contains("<")) {
            return "ConditionalExpression<To" + replacement;
        }
        else if (original.contains("===")) {
            return "ConditionalExpression===To" + replacement;
        }
        else if (original.contains("!==")) {
            return "ConditionalExpression!==To" + replacement;
        }
        else if (original.contains("==")) {
            return "ConditionalExpression==To" + replacement;
        }
        else if (original.contains("!=")) {
            return "ConditionalExpression!=To" + replacement;
        }
        else {
            return "ConditionalExpressionConditionTo" + replacement;
        }
    }
    
    private static String getEqualityOperatorName(String original, String replacement) {
        StringDifference difference = stringDifference(original, replacement);
        boolean originalIsknown = Arrays.stream(COMPARATOR_OPERATORS).anyMatch(x -> x.equals(difference.original));
        boolean replacementIsknown = Arrays.stream(COMPARATOR_OPERATORS).anyMatch(x -> x.equals(difference.replacement));
        if (!(originalIsknown && replacementIsknown)) {
            System.out.printf("EqualityOperator - Unknown mutation: %s -> %s\n", original, replacement);
            return "EqualityOperatorUnknown";
        }
        if (difference.original.equals("!") && original.charAt(difference.index + 3) == '=') {
            return "EqualityOperator!==To===";
        }
        else if (difference.original.equals("=") && original.charAt(difference.index + 3) == '=') {
            return "EqualityOperator===To!==";
        }
        else if (difference.original.equals("!") && original.charAt(difference.index + 3) != '=') {
            return "EqualityOperator!=To==";
        }
        else if (difference.original.equals("=") && original.charAt(difference.index + 3) != '=') {
            return "EqualityOperator==To!=";
        }
        else {
            return "EqualityOperator%sTo%s".formatted(difference.original, difference.replacement);
        }
    } 
    
    private static String getLogicalOperatorName(String original, String replacement) {
        StringDifference difference = stringDifference(original, replacement);
        boolean originalIsKnown = Arrays.stream(LOGICAL_OPERATORS).anyMatch(x -> x.equals(difference.original));
        boolean replacementIsKnown = Arrays.stream(LOGICAL_OPERATORS).anyMatch(x -> x.equals(difference.replacement));
        if (!(originalIsKnown && replacementIsKnown)) {
            System.out.printf("LogicalOperator - Unknown mutation: %s -> %s\n", original, replacement);
            return "LogicalOperatorUnknown";
        }
        return "LogicalOperator%sTo%s".formatted(difference.original, difference.replacement);
    } 
    
    private static String getStringLiteralOperatorName(String original, String replacement) {
        switch (replacement) {
            case "\"\"", "``" -> {
                return "StringLiteralEmpty";
            }
            case "\"Stryker was here!\"", "`Stryker was here!`" -> {
                return "StringLiteralFill";
            }
            case "s\"\"" -> {
                return "StringLiteralInterpolationEmpty";
            }
            default -> {
                System.out.printf("StringLiteral - Unknown mutation: %s -> %s\n", original, replacement);
                return "StringLiteralUnknown";
            }
        }
    } 
    
    private static String getUnaryOperatorName(String original, String replacement) {
        StringDifference difference = stringDifference(original, replacement);
        if (difference.original.equals("~") && difference.replacement.isEmpty()) {
            return "UnaryOperatorRemove~";
        }
        boolean originalIsKnown = Arrays.stream(UNARY_OPERATORS).anyMatch(x -> x.equals(difference.original));
        boolean replacmentIsKnown = Arrays.stream(UNARY_OPERATORS).anyMatch(x -> x.equals(difference.replacement));
        if (!(originalIsKnown && replacmentIsKnown)) {
            System.out.printf("UnaryOperator - Unknown mutation: %s -> %s\n", original, replacement);
            return "UnaryOperatorUnknown";
        }
        return "UnaryOperator%sTo%s".formatted(difference.original, difference.replacement);
    } 
    
    private static String getUpdateOperatorName(String original, String replacement) {
        if (original.startsWith("++")) {
            return "UpdateOperatorPre++To--";
        }
        else if (original.startsWith("--")) {
            return "UpdateOperatorPre--To++";
        }
        else if (original.endsWith("--")) {
            return "UpdateOperatorPost--To++";
        }
        else if (original.endsWith("++")) {
            return "UpdateOperatorPost++To--";
        }
        else {
            System.out.printf("UpdateOperator - Unknown mutation: %s -> %s\n", original, replacement);
            return "UpdateOperatorUnknown";
        }
    } 
    

    private static StringDifference stringDifference(String original, String replacement) {
        original = original.replace("(", "");
        original = original.replace(")", "");
        replacement = replacement.replace("(", "");
        replacement = replacement.replace(")", "");
        //if encountering breaklines, then remove them, and remove any spaces from both the original and replacement, so that they don't show up as a difference
        replacement = replacement.replace("\n", "");
        original = original.replace("\n", "");
        replacement = replacement.replace(" ", "");
        original = original.replace(" ", "");
        int indexFront = -1;
        do {
            indexFront++;
            if (indexFront == original.length() || indexFront == replacement.length()) {
                indexFront--;
                break;
            }
        } while (original.charAt(indexFront) == replacement.charAt(indexFront));
        int indexBack = -1;
        do {
            indexBack++;
            if (indexBack == original.length() || indexBack == replacement.length()) {
                //indexBack--; //causes off-by-one error?
                break;
            }
        } while (original.charAt(original.length() - 1 - indexBack) == replacement.charAt(replacement.length() - 1 - indexBack));
        if ((original.length() - indexBack) - indexFront == 0 || (replacement.length() - indexBack) - indexFront == 0) {
            if (indexFront > 0) {
                indexFront--;
            }
        }
        String originalDifference = original.substring(indexFront, (original.length() - indexBack));
        String replacementDifference = replacement.substring(indexFront, (replacement.length() - indexBack));
        return new StringDifference(originalDifference, replacementDifference, indexFront);
    }

    private static String findCode(String sourceCode, MutationReport.Location location) {
        String breakline = sourceCode.contains("\r\n") ? "\r\n" : "\n";
        String[] lines = sourceCode.split(breakline);
        int startLine = location.getStart().getLine() - 1;
        int startColumn = location.getStart().getColumn() - 1;
        int endLine = location.getEnd().getLine() - 1;
        int endColumn = location.getEnd().getColumn() - 1;
        StringBuilder result = new StringBuilder();
        if (startLine == endLine) {
            result = new StringBuilder(lines[startLine].substring(startColumn, endColumn));
        } else {
            for (int line = startLine; line <= endLine; line++) {
                if (line == startLine) {
                    result.append(lines[line].substring(startColumn));
                } else if (line == endLine) {
                    result.append(lines[line], 0, endColumn);
                } else {
                    result.append(lines[line]);
                }
            }
        }
        return result.toString();
    }

    private record StringDifference(String original, String replacement, int index) { }
}
