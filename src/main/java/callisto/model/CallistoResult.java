package callisto.model;

public record CallistoResult(
        String mutationOperatorName,
        double quality,
        double deviation,
        int mutantCount,
        int numberOfTestExecutions) {
    @Override
    public String toString() {
        return "Mutator: %s, Count: %d, Tests Executed: %d, Quality: %.3f, Deviation: %.3f".formatted(mutationOperatorName, mutantCount, numberOfTestExecutions, quality, deviation);
    }
}
