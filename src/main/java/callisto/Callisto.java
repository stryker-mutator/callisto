package callisto;

import callisto.model.MutationReport;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.tinylog.Logger;
import org.tinylog.TaggedLogger;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Command(name = "callisto", description = "Calculate the quality of mutators", subcommands = {Callisto.RunCommand.class, Callisto.TestCommand.class, Callisto.MergeCommand.class, CommandLine.HelpCommand.class})
public class Callisto {
    public static void main(String[] args) {
        int exitCode = new CommandLine(new Callisto()).execute(args);
        System.exit(exitCode);
    }

    @Command(name = "run", description = "Calculate the quality of mutators")
    static class RunCommand implements Runnable {
        @Option(names = {"-i", "--input"}, arity = "1..*", description = "Input Stryker mutation json reports to analyse", required = true)
        private String[] inputFiles;

        @Option(names = {"-o", "--output"}, description = "Output file to report findings", required = true)
        private String outputFile;

        @Option(names = {"-s", "--solver"}, description = "Solver to use")
        private String solver = "GLOP";

        @Option(names = {"-v", "--verbose"}, description = "Enable verbose logging")
        private boolean isVerbose = false;

        @Option(names = {"-k", "--killed-only"}, description = "Only use killed mutants")
        private boolean usekilledOnly = false;

        @Option(names = {"-t", "--static"}, description = "Include static mutants")
        private boolean useStatic = false;

        @Override
        public void run() {
            callisto.commands.RunCommand.run(inputFiles, outputFile, solver, usekilledOnly, useStatic);
        }
    }

    @Command(name = "test", description = "Test the effectiveness of a mutation level")
    static class TestCommand implements Runnable {
        @Option(names = {"-l", "--level"}, description = "File describing the mutation level to test", required = true)
        private String levelFile;

        @Option(names = {"-i", "--input"}, description = "Input Stryker mutation report to use when testing the level", required = true)
        private String inputFile;

        @Option(names = {"-o", "--output"}, description = "Output file to report findings", required = true)
        private String outputFile;

        @Option(names = {"-s", "--solver"}, description = "Solver to use")
        private String solver = "GLOP";

        @Option(names = {"-v", "--verbose"}, description = "Enable verbose logging")
        private boolean verbose = false;

        @Override
        public void run() {
            callisto.commands.TestCommand.run(levelFile, inputFile, outputFile, solver, verbose);
        }
    }

    @Command(name = "merge", description = "Merge multiple json reports together")
    static class MergeCommand implements Runnable {
        @Option(names = {"-i", "--input"}, arity = "1..*", description = "Input Stryker mutation json reports to merge", required = true)
        private String[] inputFiles;

        @Option(names = {"-o", "--output"}, description = "Output file to save merged reports", required = true)
        private String outputFile;

        @Option(names = {"-v", "--verbose"}, description = "Enable verbose logging")
        private boolean verbose = false;

        @Override
        public void run() {
            System.out.println("The merge command has not been implemented yet...");
        }
    }

    private static TaggedLogger initLogger(boolean verbose) {
        if (verbose) {
            return Logger.tag("VERBOSE");
        } else {
            return Logger.tag("DEFAULT");
        }
    }

    public static MutationReport parseReport(String filePath) {
        File inputFile = new File(filePath);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        MutationReport mutationReport;
        try {
            mutationReport = objectMapper.readValue(inputFile, MutationReport.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        mutationReport.initialize();
        return mutationReport;
    }

    public static void validateInput(String[] inputFiles) {
        for (String file : inputFiles) {
            if (!Files.exists(Paths.get(file))) {
                throw new RuntimeException("File not found: " + file);
            }
        }
    }


}
