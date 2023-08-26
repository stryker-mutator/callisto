package callisto;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.tinylog.Logger;
import org.tinylog.TaggedLogger;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.io.File;
import java.io.IOException;

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
        private String solver;

        @Option(names = {"-v", "--verbose"}, description = "Enable verbose logging")
        private boolean isVerbose = false;

        @Option(names = {"-k", "--killed-only"}, description = "Only use killed mutants")
        private boolean usekilledOnly = false;

        @Option(names = {"-t", "--static"}, description = "Include static mutants")
        private boolean useStatic = false;

        @Override
        public void run() {
            // validate that input files exist
//            for (String file : inputFiles) {
//                File inputFile = new File(file);
//                if (!inputFile.isFile()) {
//                    throw new RuntimeException(file + ": file not found");
//                }
//            }
            TaggedLogger logger = initLogger(isVerbose);
            logger.warn("this is a warning!");
            logger.info("this is an info message");
            logger.debug("this is for debugging");

            MutationReport testReport = parseReport(inputFiles[0]);
            logger.info("number of files in report: " + testReport.getFiles().size());
        }
    }

    @Command(name = "test", description = "Test the effectiveness of a mutation level")
    static class TestCommand implements Runnable {
        @Option(names = {"-i", "--input"}, description = "Input file describing mutation level to test", required = true)
        private String inputFile;

        @Option(names = {"-o", "--output"}, description = "Output file to report findings", required = true)
        private String outputFile;

        @Option(names = {"-s", "--solver"}, description = "Solver to use")
        private String solver = "GLOP";

        @Option(names = {"-v", "--verbose"}, description = "Enable verbose logging")
        private boolean verbose = false;

        @Option(names = {"-k", "--killed-only"}, description = "Only use killed mutants")
        private boolean killed = false;

        @Option(names = {"-t", "--static"}, description = "Include static mutants")
        private boolean useStatic = false;

        @Override
        public void run() {
            System.out.println("this is the test command!");
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
            System.out.println("This is the merge command!");
        }
    }

    private static TaggedLogger initLogger(boolean verbose) {
        if (verbose) {
            return Logger.tag("VERBOSE");
        } else {
            return Logger.tag("DEFAULT");
        }
    }

    private static MutationReport parseReport(String filePath) {
        File inputFile = new File(filePath);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        MutationReport mutationReport;
        try {
            mutationReport = objectMapper.readValue(inputFile, MutationReport.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return mutationReport;
    }
}
