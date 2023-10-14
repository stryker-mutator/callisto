package callisto.model;

import java.net.URI;
import java.util.*;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * MutationTestResult
 * <p>
 * Schema for a mutation testing report.
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "config",
        "schemaVersion",
        "files",
        "testFiles",
        "thresholds",
        "projectRoot",
        "performance",
        "framework",
        "system"
})
public class MutationReport {
    @JsonIgnore
    private Mutant[] mutants;

    @JsonIgnore
    private Test[] tests;

    /**
     * Free-format object that represents the configuration used to run mutation testing.
     *
     */
    @JsonProperty("config")
    @JsonPropertyDescription("Free-format object that represents the configuration used to run mutation testing.")
    private Map<String, Object> config;

    /**
     * Major version of this report. Used for compatibility.
     * (Required)
     *
     */
    @JsonProperty("schemaVersion")
    @JsonPropertyDescription("Major version of this report. Used for compatibility.")
    private String schemaVersion;

    /**
     * FileResultDictionary
     * <p>
     * All mutated files.
     * (Required)
     *
     */
    @JsonProperty("files")
    @JsonPropertyDescription("All mutated files.")
    private Map<String, FileResult> files;

    /**
     * TestFileDefinitionDictionary
     * <p>
     * Test file definitions by file path OR class name.
     *
     */
    @JsonProperty("testFiles")
    @JsonPropertyDescription("Test file definitions by file path OR class name.")
    private Map<String, TestFile> testFiles;

    /**
     * Thresholds
     * <p>
     * Thresholds for the status of the reported application.
     * (Required)
     *
     */
    @JsonProperty("thresholds")
    @JsonPropertyDescription("Thresholds for the status of the reported application.")
    private Thresholds thresholds;

    /**
     * The optional location of the project root.
     *
     */
    @JsonProperty("projectRoot")
    @JsonPropertyDescription("The optional location of the project root.")
    private String projectRoot;
    /**
     * PerformanceStatistics
     * <p>
     * The performance statistics per phase. Total time should be roughly equal to the sum of all these.
     *
     */
    @JsonProperty("performance")
    @JsonPropertyDescription("The performance statistics per phase. Total time should be roughly equal to the sum of all these.")
    private Performance performance;

    /**
     * FrameworkInformation
     * <p>
     * Extra information about the framework used
     *
     */
    @JsonProperty("framework")
    @JsonPropertyDescription("Extra information about the framework used")
    private Framework framework;

    /**
     * SystemInformation
     * <p>
     * Information about the system that performed mutation testing.
     *
     */
    @JsonProperty("system")
    @JsonPropertyDescription("Information about the system that performed mutation testing.")
    private System system;

    public Mutant[] getMutants() {
        return mutants;
    }

    public Test[] getTests() {
        return tests;
    }

    /**
     * Free-format object that represents the configuration used to run mutation testing.
     *
     */
    @JsonProperty("config")
    public Map<String, Object> getConfig() {
        return config;
    }

    /**
     * Free-format object that represents the configuration used to run mutation testing.
     *
     */
    @JsonProperty("config")
    public void setConfig(Map<String, Object> config) {
        this.config = config;
    }

    /**
     * Major version of this report. Used for compatibility.
     * (Required)
     *
     */
    @JsonProperty("schemaVersion")
    public String getSchemaVersion() {
        return schemaVersion;
    }

    /**
     * Major version of this report. Used for compatibility.
     * (Required)
     *
     */
    @JsonProperty("schemaVersion")
    public void setSchemaVersion(String schemaVersion) {
        this.schemaVersion = schemaVersion;
    }

    /**
     * FileResultDictionary
     * <p>
     * All mutated files.
     * (Required)
     *
     */
    @JsonProperty("files")
    public Map<String, FileResult> getFiles() {
        return files;
    }

    /**
     * FileResultDictionary
     * <p>
     * All mutated files.
     * (Required)
     *
     */
    @JsonProperty("files")
    public void setFiles(Map<String, FileResult> files) {
        this.files = files;
    }

    /**
     * TestFileDefinitionDictionary
     * <p>
     * Test file definitions by file path OR class name.
     *
     */
    @JsonProperty("testFiles")
    public Map<String, TestFile> getTestFiles() {
        return testFiles;
    }

    /**
     * TestFileDefinitionDictionary
     * <p>
     * Test file definitions by file path OR class name.
     *
     */
    @JsonProperty("testFiles")
    public void setTestFiles(Map<String, TestFile> testFiles) {
        this.testFiles = testFiles;
    }

    /**
     * Thresholds
     * <p>
     * Thresholds for the status of the reported application.
     * (Required)
     *
     */
    @JsonProperty("thresholds")
    public Thresholds getThresholds() {
        return thresholds;
    }

    /**
     * Thresholds
     * <p>
     * Thresholds for the status of the reported application.
     * (Required)
     *
     */
    @JsonProperty("thresholds")
    public void setThresholds(Thresholds thresholds) {
        this.thresholds = thresholds;
    }

    /**
     * The optional location of the project root.
     *
     */
    @JsonProperty("projectRoot")
    public String getProjectRoot() {
        return projectRoot;
    }

    /**
     * The optional location of the project root.
     *
     */
    @JsonProperty("projectRoot")
    public void setProjectRoot(String projectRoot) {
        this.projectRoot = projectRoot;
    }

    /**
     * PerformanceStatistics
     * <p>
     * The performance statistics per phase. Total time should be roughly equal to the sum of all these.
     *
     */
    @JsonProperty("performance")
    public Performance getPerformance() {
        return performance;
    }

    /**
     * PerformanceStatistics
     * <p>
     * The performance statistics per phase. Total time should be roughly equal to the sum of all these.
     *
     */
    @JsonProperty("performance")
    public void setPerformance(Performance performance) {
        this.performance = performance;
    }

    /**
     * FrameworkInformation
     * <p>
     * Extra information about the framework used
     *
     */
    @JsonProperty("framework")
    public Framework getFramework() {
        return framework;
    }

    /**
     * FrameworkInformation
     * <p>
     * Extra information about the framework used
     *
     */
    @JsonProperty("framework")
    public void setFramework(Framework framework) {
        this.framework = framework;
    }

    /**
     * SystemInformation
     * <p>
     * Information about the system that performed mutation testing.
     *
     */
    @JsonProperty("system")
    public System getSystem() {
        return system;
    }

    /**
     * SystemInformation
     * <p>
     * Information about the system that performed mutation testing.
     *
     */
    @JsonProperty("system")
    public void setSystem(System system) {
        this.system = system;
    }

    public void initialize() {
        // calculate and organize some derived information of the report

        // determine number of mutants:
        int numOfMutants = 0;
        for (FileResult file : files.values()) {
            numOfMutants += file.mutants.size();
        }
        // determine number of tests:
        int numOfTests = 0;
        for (TestFile testFile : testFiles.values()) {
            numOfTests += testFile.tests.size();
        }
        this.mutants = new Mutant[numOfMutants];
        this.tests = new Test[numOfTests];

        // collect all mutants into array
        int index = 0;
        for (FileResult file : files.values()) {
            for (Mutant mutant : file.mutants) {
                this.mutants[index] = mutant;
                index++;
            }
        }

        //collect all tests into array
        index = 0;
        for (TestFile file : testFiles.values()) {
            for (Test test : file.tests) {
                this.tests[index] = test;
                index++;
            }
        }
    }

    public void deduceMutationOperators() {
        //TODO: implement
    }

    public Mutant[] getMutantsByMutatorName(String mutatorName, boolean killedOnly, boolean useStatic) {
        List<Mutant> mutants = new ArrayList<>();
        for (Mutant mutant : this.mutants) {
            if (mutant.mutatorName.equals(mutatorName)) {
                if ((killedOnly && mutant.status == Mutant.Status.KILLED) || !killedOnly) {
                    if ((!useStatic && !mutant.isStatic) || useStatic) {
                        mutants.add(mutant);
                    }
                }
            }
        }
        return mutants.toArray(Mutant[]::new);
    }

    public MutationMatrices getMatrices() {
        int numberOfMutants = mutants.length;
        int numberOfTests = tests.length;
        boolean[][] killMatrix = new boolean[numberOfTests][numberOfMutants];
        boolean[][] coverageMatrix = new boolean[numberOfTests][numberOfMutants];
        for (Mutant mutant : mutants) {
            int mutantId = Integer.parseInt(mutant.id);
            //fill in killmatrix
            if (mutant.killedBy != null) {
                for (String testIdString : mutant.killedBy) {
                    int testId = Integer.parseInt(testIdString);
                    killMatrix[testId][mutantId] = true;
                }
            }
            // fill in coveragematrix
            // static mutants have no coverage info, so use killed info instead
            if (mutant.isStatic)  {
                if (mutant.killedBy != null) {
                    for (String testIdString : mutant.killedBy) {
                        int testId = Integer.parseInt(testIdString);
                        coverageMatrix[testId][mutantId] = true;
                    }
                }
            } else {
                if (mutant.coveredBy != null) {
                    for (String testIdString : mutant.coveredBy) {
                    int testId = Integer.parseInt(testIdString);
                        coverageMatrix[testId][mutantId] = true;
                    }
                }
            }
        }
        return new MutationMatrices(killMatrix, coverageMatrix);
    }

    public String[] getUsedMutators() {
        List<String> mutators = new ArrayList<>();
        for (Mutant mutant : mutants) {
            if (!mutators.contains(mutant.mutatorName)) {
                mutators.add(mutant.mutatorName);
            }
        }
        mutators.sort(String::compareTo);
        return mutators.toArray(String[]::new);
    }

    public int countTestExecutions(String mutator) {
        double result = 0; //TODO: try to use int instead of double in the report class, see if jackson likes it
        for (Mutant mutant : mutants) {
            //static test executions included
            if (mutant.mutatorName.equals(mutator) && mutant.testsCompleted != null) {
                result += mutant.testsCompleted;
            }
        }
        return (int) result;
    }


    /**
     * BrandingInformation
     * <p>
     * Extra branding information about the framework used.
     *
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonPropertyOrder({
            "homepageUrl",
            "imageUrl"
    })
    public static class Branding {

        /**
         * URL to the homepage of the framework.
         * (Required)
         *
         */
        @JsonProperty("homepageUrl")
        @JsonPropertyDescription("URL to the homepage of the framework.")
        private URI homepageUrl;
        /**
         * URL to an image for the framework, can be a data URL.
         *
         */
        @JsonProperty("imageUrl")
        @JsonPropertyDescription("URL to an image for the framework, can be a data URL.")
        private String imageUrl;

        /**
         * URL to the homepage of the framework.
         * (Required)
         *
         */
        @JsonProperty("homepageUrl")
        public URI getHomepageUrl() {
            return homepageUrl;
        }

        /**
         * URL to the homepage of the framework.
         * (Required)
         *
         */
        @JsonProperty("homepageUrl")
        public void setHomepageUrl(URI homepageUrl) {
            this.homepageUrl = homepageUrl;
        }

        /**
         * URL to an image for the framework, can be a data URL.
         *
         */
        @JsonProperty("imageUrl")
        public String getImageUrl() {
            return imageUrl;
        }

        /**
         * URL to an image for the framework, can be a data URL.
         *
         */
        @JsonProperty("imageUrl")
        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }
    }

    /**
     * CpuInformation
     * <p>
     *
     *
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonPropertyOrder({
            "baseClock",
            "logicalCores",
            "model"
    })
    public static class Cpu {

        /**
         * Clock speed in MHz
         *
         */
        @JsonProperty("baseClock")
        @JsonPropertyDescription("Clock speed in MHz")
        private Double baseClock;
        /**
         *
         * (Required)
         *
         */
        @JsonProperty("logicalCores")
        private Double logicalCores;
        @JsonProperty("model")
        private String model;

        /**
         * Clock speed in MHz
         *
         */
        @JsonProperty("baseClock")
        public Double getBaseClock() {
            return baseClock;
        }

        /**
         * Clock speed in MHz
         *
         */
        @JsonProperty("baseClock")
        public void setBaseClock(Double baseClock) {
            this.baseClock = baseClock;
        }

        /**
         *
         * (Required)
         *
         */
        @JsonProperty("logicalCores")
        public Double getLogicalCores() {
            return logicalCores;
        }

        /**
         *
         * (Required)
         *
         */
        @JsonProperty("logicalCores")
        public void setLogicalCores(Double logicalCores) {
            this.logicalCores = logicalCores;
        }

        @JsonProperty("model")
        public String getModel() {
            return model;
        }

        @JsonProperty("model")
        public void setModel(String model) {
            this.model = model;
        }
    }

    /**
     * FileResult
     * <p>
     * Mutated file, with the relative path of the file as the key.
     *
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonPropertyOrder({
            "language",
            "mutants",
            "source"
    })
    public static class FileResult {

        /**
         * Programming language that is used. Used for code highlighting, see https://prismjs.com/#examples.
         * (Required)
         *
         */
        @JsonProperty("language")
        @JsonPropertyDescription("Programming language that is used. Used for code highlighting, see https://prismjs.com/#examples.")
        private String language;
        /**
         *
         * (Required)
         *
         */
        @JsonProperty("mutants")
        @JsonDeserialize(as = java.util.LinkedHashSet.class)
        private Set<Mutant> mutants;
        /**
         * Full source code of the original file (without mutants), this is used to display exactly what was changed for each mutant.
         * (Required)
         *
         */
        @JsonProperty("source")
        @JsonPropertyDescription("Full source code of the original file (without mutants), this is used to display exactly what was changed for each mutant.")
        private String source;

        /**
         * Programming language that is used. Used for code highlighting, see https://prismjs.com/#examples.
         * (Required)
         *
         */
        @JsonProperty("language")
        public String getLanguage() {
            return language;
        }

        /**
         * Programming language that is used. Used for code highlighting, see https://prismjs.com/#examples.
         * (Required)
         *
         */
        @JsonProperty("language")
        public void setLanguage(String language) {
            this.language = language;
        }

        /**
         *
         * (Required)
         *
         */
        @JsonProperty("mutants")
        public Set<Mutant> getMutants() {
            return mutants;
        }

        /**
         *
         * (Required)
         *
         */
        @JsonProperty("mutants")
        public void setMutants(Set<Mutant> mutants) {
            this.mutants = mutants;
        }

        /**
         * Full source code of the original file (without mutants), this is used to display exactly what was changed for each mutant.
         * (Required)
         *
         */
        @JsonProperty("source")
        public String getSource() {
            return source;
        }

        /**
         * Full source code of the original file (without mutants), this is used to display exactly what was changed for each mutant.
         * (Required)
         *
         */
        @JsonProperty("source")
        public void setSource(String source) {
            this.source = source;
        }
    }

    /**
     * FrameworkInformation
     * <p>
     * Extra information about the framework used
     *
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonPropertyOrder({
            "name",
            "version",
            "branding",
            "dependencies"
    })
    public static class Framework {

        /**
         * Name of the framework used.
         * (Required)
         *
         */
        @JsonProperty("name")
        @JsonPropertyDescription("Name of the framework used.")
        private String name;
        /**
         * Version of the framework.
         *
         */
        @JsonProperty("version")
        @JsonPropertyDescription("Version of the framework.")
        private String version;
        /**
         * BrandingInformation
         * <p>
         * Extra branding information about the framework used.
         *
         */
        @JsonProperty("branding")
        @JsonPropertyDescription("Extra branding information about the framework used.")
        private Branding branding;
        /**
         * Dependencies
         * <p>
         * Dependencies used by the framework. Key-value pair of dependencies and their versions.
         *
         */
        @JsonProperty("dependencies")
        @JsonPropertyDescription("Dependencies used by the framework. Key-value pair of dependencies and their versions.")
        private Map<String, String> dependencies;

        /**
         * Name of the framework used.
         * (Required)
         *
         */
        @JsonProperty("name")
        public String getName() {
            return name;
        }

        /**
         * Name of the framework used.
         * (Required)
         *
         */
        @JsonProperty("name")
        public void setName(String name) {
            this.name = name;
        }

        /**
         * Version of the framework.
         *
         */
        @JsonProperty("version")
        public String getVersion() {
            return version;
        }

        /**
         * Version of the framework.
         *
         */
        @JsonProperty("version")
        public void setVersion(String version) {
            this.version = version;
        }

        /**
         * BrandingInformation
         * <p>
         * Extra branding information about the framework used.
         *
         */
        @JsonProperty("branding")
        public Branding getBranding() {
            return branding;
        }

        /**
         * BrandingInformation
         * <p>
         * Extra branding information about the framework used.
         *
         */
        @JsonProperty("branding")
        public void setBranding(Branding branding) {
            this.branding = branding;
        }

        /**
         * Dependencies
         * <p>
         * Dependencies used by the framework. Key-value pair of dependencies and their versions.
         *
         */
        @JsonProperty("dependencies")
        public Map<String, String> getDependencies() {
            return dependencies;
        }

        /**
         * Dependencies
         * <p>
         * Dependencies used by the framework. Key-value pair of dependencies and their versions.
         *
         */
        @JsonProperty("dependencies")
        public void setDependencies(Map<String, String> dependencies) {
            this.dependencies = dependencies;
        }
    }

    /**
     * Location
     * <p>
     * A location with start and end. Start is inclusive, end is exclusive.
     *
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonPropertyOrder({
            "start",
            "end"
    })
    public static class Location {

        /**
         * Position
         * <p>
         * Position of a mutation. Both line and column start at one.
         * (Required)
         *
         */
        @JsonProperty("start")
        @JsonPropertyDescription("Position of a mutation. Both line and column start at one.")
        private Position start;
        /**
         * Position
         * <p>
         * Position of a mutation. Both line and column start at one.
         * (Required)
         *
         */
        @JsonProperty("end")
        @JsonPropertyDescription("Position of a mutation. Both line and column start at one.")
        private Position end;

        /**
         * Position
         * <p>
         * Position of a mutation. Both line and column start at one.
         * (Required)
         *
         */
        @JsonProperty("start")
        public Position getStart() {
            return start;
        }

        /**
         * Position
         * <p>
         * Position of a mutation. Both line and column start at one.
         * (Required)
         *
         */
        @JsonProperty("start")
        public void setStart(Position start) {
            this.start = start;
        }

        /**
         * Position
         * <p>
         * Position of a mutation. Both line and column start at one.
         * (Required)
         *
         */
        @JsonProperty("end")
        public Position getEnd() {
            return end;
        }

        /**
         * Position
         * <p>
         * Position of a mutation. Both line and column start at one.
         * (Required)
         *
         */
        @JsonProperty("end")
        public void setEnd(Position end) {
            this.end = end;
        }
    }

    /**
     * MutantResult
     * <p>
     * Single mutation.
     *
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonPropertyOrder({
            "coveredBy",
            "description",
            "duration",
            "id",
            "killedBy",
            "location",
            "mutatorName",
            "replacement",
            "static",
            "status",
            "statusReason",
            "testsCompleted"
    })
    public static class Mutant {

        /**
         * The test ids that covered this mutant. If a mutation testing framework doesn't measure this information, it can simply be left out.
         *
         */
        @JsonProperty("coveredBy")
        @JsonPropertyDescription("The test ids that covered this mutant. If a mutation testing framework doesn't measure this information, it can simply be left out.")
        private List<String> coveredBy;
        /**
         * Description of the applied mutation.
         *
         */
        @JsonProperty("description")
        @JsonPropertyDescription("Description of the applied mutation.")
        private String description;
        /**
         * The net time it took to test this mutant in milliseconds. This is the time measurement without overhead from the mutation testing framework.
         *
         */
        @JsonProperty("duration")
        @JsonPropertyDescription("The net time it took to test this mutant in milliseconds. This is the time measurement without overhead from the mutation testing framework.")
        private Double duration;
        /**
         * Unique id, can be used to correlate this mutant across reports.
         * (Required)
         *
         */
        @JsonProperty("id")
        @JsonPropertyDescription("Unique id, can be used to correlate this mutant across reports.")
        private String id;
        /**
         * The test ids that killed this mutant. It is a best practice to "bail" on first failing test, in which case you can fill this array with that one test.
         *
         */
        @JsonProperty("killedBy")
        @JsonPropertyDescription("The test ids that killed this mutant. It is a best practice to \"bail\" on first failing test, in which case you can fill this array with that one test.")
        private List<String> killedBy;
        /**
         * Location
         * <p>
         * A location with start and end. Start is inclusive, end is exclusive.
         * (Required)
         *
         */
        @JsonProperty("location")
        @JsonPropertyDescription("A location with start and end. Start is inclusive, end is exclusive.")
        private Location location;
        /**
         * Category of the mutation.
         * (Required)
         *
         */
        @JsonProperty("mutatorName")
        @JsonPropertyDescription("Category of the mutation.")
        private String mutatorName;
        /**
         * Actual mutation that has been applied.
         *
         */
        @JsonProperty("replacement")
        @JsonPropertyDescription("Actual mutation that has been applied.")
        private String replacement;
        /**
         * A static mutant means that it was loaded once at during initialization, this makes it slow or even impossible to test, depending on the mutation testing framework.
         *
         */
        @JsonProperty("static")
        @JsonPropertyDescription("A static mutant means that it was loaded once at during initialization, this makes it slow or even impossible to test, depending on the mutation testing framework.")
        private Boolean isStatic;
        /**
         * MutantStatus
         * <p>
         * Result of the mutation.
         * (Required)
         *
         */
        @JsonProperty("status")
        @JsonPropertyDescription("Result of the mutation.")
        private Mutant.Status status;
        /**
         * The reason that this mutant has this status. In the case of a killed mutant, this should be filled with the failure message(s) of the failing tests. In case of an error mutant, this should be filled with the error message.
         *
         */
        @JsonProperty("statusReason")
        @JsonPropertyDescription("The reason that this mutant has this status. In the case of a killed mutant, this should be filled with the failure message(s) of the failing tests. In case of an error mutant, this should be filled with the error message.")
        private String statusReason;
        /**
         * The number of tests actually completed in order to test this mutant. Can differ from "coveredBy" because of bailing a mutant test run after first failing test.
         *
         */
        @JsonProperty("testsCompleted")
        @JsonPropertyDescription("The number of tests actually completed in order to test this mutant. Can differ from \"coveredBy\" because of bailing a mutant test run after first failing test.")
        private Double testsCompleted;

        /**
         * The test ids that covered this mutant. If a mutation testing framework doesn't measure this information, it can simply be left out.
         *
         */
        @JsonProperty("coveredBy")
        public List<String> getCoveredBy() {
            return coveredBy;
        }

        /**
         * The test ids that covered this mutant. If a mutation testing framework doesn't measure this information, it can simply be left out.
         *
         */
        @JsonProperty("coveredBy")
        public void setCoveredBy(List<String> coveredBy) {
            this.coveredBy = coveredBy;
        }

        /**
         * Description of the applied mutation.
         *
         */
        @JsonProperty("description")
        public String getDescription() {
            return description;
        }

        /**
         * Description of the applied mutation.
         *
         */
        @JsonProperty("description")
        public void setDescription(String description) {
            this.description = description;
        }

        /**
         * The net time it took to test this mutant in milliseconds. This is the time measurement without overhead from the mutation testing framework.
         *
         */
        @JsonProperty("duration")
        public Double getDuration() {
            return duration;
        }

        /**
         * The net time it took to test this mutant in milliseconds. This is the time measurement without overhead from the mutation testing framework.
         *
         */
        @JsonProperty("duration")
        public void setDuration(Double duration) {
            this.duration = duration;
        }

        /**
         * Unique id, can be used to correlate this mutant across reports.
         * (Required)
         *
         */
        @JsonProperty("id")
        public String getId() {
            return id;
        }

        /**
         * Unique id, can be used to correlate this mutant across reports.
         * (Required)
         *
         */
        @JsonProperty("id")
        public void setId(String id) {
            this.id = id;
        }

        /**
         * The test ids that killed this mutant. It is a best practice to "bail" on first failing test, in which case you can fill this array with that one test.
         *
         */
        @JsonProperty("killedBy")
        public List<String> getKilledBy() {
            return killedBy;
        }

        /**
         * The test ids that killed this mutant. It is a best practice to "bail" on first failing test, in which case you can fill this array with that one test.
         *
         */
        @JsonProperty("killedBy")
        public void setKilledBy(List<String> killedBy) {
            this.killedBy = killedBy;
        }

        /**
         * Location
         * <p>
         * A location with start and end. Start is inclusive, end is exclusive.
         * (Required)
         *
         */
        @JsonProperty("location")
        public Location getLocation() {
            return location;
        }

        /**
         * Location
         * <p>
         * A location with start and end. Start is inclusive, end is exclusive.
         * (Required)
         *
         */
        @JsonProperty("location")
        public void setLocation(Location location) {
            this.location = location;
        }

        /**
         * Category of the mutation.
         * (Required)
         *
         */
        @JsonProperty("mutatorName")
        public String getMutatorName() {
            return mutatorName;
        }

        /**
         * Category of the mutation.
         * (Required)
         *
         */
        @JsonProperty("mutatorName")
        public void setMutatorName(String mutatorName) {
            this.mutatorName = mutatorName;
        }

        /**
         * Actual mutation that has been applied.
         *
         */
        @JsonProperty("replacement")
        public String getReplacement() {
            return replacement;
        }

        /**
         * Actual mutation that has been applied.
         *
         */
        @JsonProperty("replacement")
        public void setReplacement(String replacement) {
            this.replacement = replacement;
        }

        /**
         * A static mutant means that it was loaded once at during initialization, this makes it slow or even impossible to test, depending on the mutation testing framework.
         *
         */
        @JsonProperty("static")
        public Boolean getStatic() {
            return isStatic;
        }

        /**
         * A static mutant means that it was loaded once at during initialization, this makes it slow or even impossible to test, depending on the mutation testing framework.
         *
         */
        @JsonProperty("static")
        public void setStatic(Boolean _static) {
            this.isStatic = _static;
        }

        /**
         * MutantStatus
         * <p>
         * Result of the mutation.
         * (Required)
         *
         */
        @JsonProperty("status")
        public Mutant.Status getStatus() {
            return status;
        }

        /**
         * MutantStatus
         * <p>
         * Result of the mutation.
         * (Required)
         *
         */
        @JsonProperty("status")
        public void setStatus(Mutant.Status status) {
            this.status = status;
        }

        /**
         * The reason that this mutant has this status. In the case of a killed mutant, this should be filled with the failure message(s) of the failing tests. In case of an error mutant, this should be filled with the error message.
         *
         */
        @JsonProperty("statusReason")
        public String getStatusReason() {
            return statusReason;
        }

        /**
         * The reason that this mutant has this status. In the case of a killed mutant, this should be filled with the failure message(s) of the failing tests. In case of an error mutant, this should be filled with the error message.
         *
         */
        @JsonProperty("statusReason")
        public void setStatusReason(String statusReason) {
            this.statusReason = statusReason;
        }

        /**
         * The number of tests actually completed in order to test this mutant. Can differ from "coveredBy" because of bailing a mutant test run after first failing test.
         *
         */
        @JsonProperty("testsCompleted")
        public Double getTestsCompleted() {
            return testsCompleted;
        }

        /**
         * The number of tests actually completed in order to test this mutant. Can differ from "coveredBy" because of bailing a mutant test run after first failing test.
         *
         */
        @JsonProperty("testsCompleted")
        public void setTestsCompleted(Double testsCompleted) {
            this.testsCompleted = testsCompleted;
        }

        /**
         * MutantStatus
         * <p>
         * Result of the mutation.
         *
         */
        public enum Status {
            KILLED("Killed"),
            SURVIVED("Survived"),
            NO_COVERAGE("NoCoverage"),
            COMPILE_ERROR("CompileError"),
            RUNTIME_ERROR("RuntimeError"),
            TIMEOUT("Timeout"),
            IGNORED("Ignored"),
            PENDING("Pending");
            private final String value;
            private final static Map<String, Mutant.Status> CONSTANTS = new HashMap<String, Status>();

            static {
                for (Mutant.Status c: values()) {
                    CONSTANTS.put(c.value, c);
                }
            }

            Status(String value) {
                this.value = value;
            }

            @Override
            public String toString() {
                return this.value;
            }

            @JsonValue
            public String value() {
                return this.value;
            }

            @JsonCreator
            public static Mutant.Status fromValue(String value) {
                Mutant.Status constant = CONSTANTS.get(value);
                if (constant == null) {
                    throw new IllegalArgumentException(value);
                } else {
                    return constant;
                }
            }

        }

    }

    /**
     * OpenEndLocation
     * <p>
     * A location where "end" is not required. Start is inclusive, end is exclusive.
     *
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonPropertyOrder({
            "start",
            "end"
    })
    public static class OpenEndLocation {

        /**
         * Position
         * <p>
         * Position of a mutation. Both line and column start at one.
         * (Required)
         *
         */
        @JsonProperty("start")
        @JsonPropertyDescription("Position of a mutation. Both line and column start at one.")
        private Position start;
        /**
         * Position
         * <p>
         * Position of a mutation. Both line and column start at one.
         *
         */
        @JsonProperty("end")
        @JsonPropertyDescription("Position of a mutation. Both line and column start at one.")
        private Position end;

        /**
         * Position
         * <p>
         * Position of a mutation. Both line and column start at one.
         * (Required)
         *
         */
        @JsonProperty("start")
        public Position getStart() {
            return start;
        }

        /**
         * Position
         * <p>
         * Position of a mutation. Both line and column start at one.
         * (Required)
         *
         */
        @JsonProperty("start")
        public void setStart(Position start) {
            this.start = start;
        }

        /**
         * Position
         * <p>
         * Position of a mutation. Both line and column start at one.
         *
         */
        @JsonProperty("end")
        public Position getEnd() {
            return end;
        }

        /**
         * Position
         * <p>
         * Position of a mutation. Both line and column start at one.
         *
         */
        @JsonProperty("end")
        public void setEnd(Position end) {
            this.end = end;
        }
    }

    /**
     * OSInformation
     * <p>
     *
     *
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonPropertyOrder({
            "description",
            "platform",
            "version"
    })
    public static class Os {

        /**
         * Human-readable description of the OS
         *
         */
        @JsonProperty("description")
        @JsonPropertyDescription("Human-readable description of the OS")
        private String description;
        /**
         * Platform identifier
         * (Required)
         *
         */
        @JsonProperty("platform")
        @JsonPropertyDescription("Platform identifier")
        private String platform;
        /**
         * Version of the OS or distribution
         *
         */
        @JsonProperty("version")
        @JsonPropertyDescription("Version of the OS or distribution")
        private String version;

        /**
         * Human-readable description of the OS
         *
         */
        @JsonProperty("description")
        public String getDescription() {
            return description;
        }

        /**
         * Human-readable description of the OS
         *
         */
        @JsonProperty("description")
        public void setDescription(String description) {
            this.description = description;
        }

        /**
         * Platform identifier
         * (Required)
         *
         */
        @JsonProperty("platform")
        public String getPlatform() {
            return platform;
        }

        /**
         * Platform identifier
         * (Required)
         *
         */
        @JsonProperty("platform")
        public void setPlatform(String platform) {
            this.platform = platform;
        }

        /**
         * Version of the OS or distribution
         *
         */
        @JsonProperty("version")
        public String getVersion() {
            return version;
        }

        /**
         * Version of the OS or distribution
         *
         */
        @JsonProperty("version")
        public void setVersion(String version) {
            this.version = version;
        }
    }

    /**
     * PerformanceStatistics
     * <p>
     * The performance statistics per phase. Total time should be roughly equal to the sum of all these.
     *
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonPropertyOrder({
            "setup",
            "initialRun",
            "mutation"
    })
    public static class Performance {

        /**
         * Time it took to run the setup phase in milliseconds.
         * (Required)
         *
         */
        @JsonProperty("setup")
        @JsonPropertyDescription("Time it took to run the setup phase in milliseconds.")
        private Double setup;
        /**
         * Time it took to run the initial test phase (dry-run) in milliseconds.
         * (Required)
         *
         */
        @JsonProperty("initialRun")
        @JsonPropertyDescription("Time it took to run the initial test phase (dry-run) in milliseconds.")
        private Double initialRun;
        /**
         * Time it took to run the mutation test phase in milliseconds.
         * (Required)
         *
         */
        @JsonProperty("mutation")
        @JsonPropertyDescription("Time it took to run the mutation test phase in milliseconds.")
        private Double mutation;

        /**
         * Time it took to run the setup phase in milliseconds.
         * (Required)
         *
         */
        @JsonProperty("setup")
        public Double getSetup() {
            return setup;
        }

        /**
         * Time it took to run the setup phase in milliseconds.
         * (Required)
         *
         */
        @JsonProperty("setup")
        public void setSetup(Double setup) {
            this.setup = setup;
        }

        /**
         * Time it took to run the initial test phase (dry-run) in milliseconds.
         * (Required)
         *
         */
        @JsonProperty("initialRun")
        public Double getInitialRun() {
            return initialRun;
        }

        /**
         * Time it took to run the initial test phase (dry-run) in milliseconds.
         * (Required)
         *
         */
        @JsonProperty("initialRun")
        public void setInitialRun(Double initialRun) {
            this.initialRun = initialRun;
        }

        /**
         * Time it took to run the mutation test phase in milliseconds.
         * (Required)
         *
         */
        @JsonProperty("mutation")
        public Double getMutation() {
            return mutation;
        }

        /**
         * Time it took to run the mutation test phase in milliseconds.
         * (Required)
         *
         */
        @JsonProperty("mutation")
        public void setMutation(Double mutation) {
            this.mutation = mutation;
        }
    }

    /**
     * Position
     * <p>
     * Position of a mutation. Both line and column start at one.
     *
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonPropertyOrder({
            "line",
            "column"
    })
    public static class Position {

        /**
         *
         * (Required)
         *
         */
        @JsonProperty("line")
        private Integer line;
        /**
         *
         * (Required)
         *
         */
        @JsonProperty("column")
        private Integer column;

        /**
         *
         * (Required)
         *
         */
        @JsonProperty("line")
        public Integer getLine() {
            return line;
        }

        /**
         *
         * (Required)
         *
         */
        @JsonProperty("line")
        public void setLine(Integer line) {
            this.line = line;
        }

        /**
         *
         * (Required)
         *
         */
        @JsonProperty("column")
        public Integer getColumn() {
            return column;
        }

        /**
         *
         * (Required)
         *
         */
        @JsonProperty("column")
        public void setColumn(Integer column) {
            this.column = column;
        }
    }

    /**
     * RamInformation
     * <p>
     *
     *
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonPropertyOrder({
            "total"
    })
    public static class Ram {

        /**
         * The total RAM of the system that performed mutation testing in MB.
         * (Required)
         *
         */
        @JsonProperty("total")
        @JsonPropertyDescription("The total RAM of the system that performed mutation testing in MB.")
        private Double total;

        /**
         * The total RAM of the system that performed mutation testing in MB.
         * (Required)
         *
         */
        @JsonProperty("total")
        public Double getTotal() {
            return total;
        }

        /**
         * The total RAM of the system that performed mutation testing in MB.
         * (Required)
         *
         */
        @JsonProperty("total")
        public void setTotal(Double total) {
            this.total = total;
        }
    }

    /**
     * SystemInformation
     * <p>
     * Information about the system that performed mutation testing.
     *
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonPropertyOrder({
            "ci",
            "os",
            "cpu",
            "ram"
    })
    public static class System {

        /**
         * Did mutation testing run in a Continuous Integration environment (pipeline)? Note that there is no way of knowing this for sure. It's done on a best-effort basis.
         * (Required)
         *
         */
        @JsonProperty("ci")
        @JsonPropertyDescription("Did mutation testing run in a Continuous Integration environment (pipeline)? Note that there is no way of knowing this for sure. It's done on a best-effort basis.")
        private Boolean ci;
        /**
         * OSInformation
         * <p>
         *
         *
         */
        @JsonProperty("os")
        private Os os;
        /**
         * CpuInformation
         * <p>
         *
         *
         */
        @JsonProperty("cpu")
        private Cpu cpu;
        /**
         * RamInformation
         * <p>
         *
         *
         */
        @JsonProperty("ram")
        private Ram ram;

        /**
         * Did mutation testing run in a Continuous Integration environment (pipeline)? Note that there is no way of knowing this for sure. It's done on a best-effort basis.
         * (Required)
         *
         */
        @JsonProperty("ci")
        public Boolean getCi() {
            return ci;
        }

        /**
         * Did mutation testing run in a Continuous Integration environment (pipeline)? Note that there is no way of knowing this for sure. It's done on a best-effort basis.
         * (Required)
         *
         */
        @JsonProperty("ci")
        public void setCi(Boolean ci) {
            this.ci = ci;
        }

        /**
         * OSInformation
         * <p>
         *
         *
         */
        @JsonProperty("os")
        public Os getOs() {
            return os;
        }

        /**
         * OSInformation
         * <p>
         *
         *
         */
        @JsonProperty("os")
        public void setOs(Os os) {
            this.os = os;
        }

        /**
         * CpuInformation
         * <p>
         *
         *
         */
        @JsonProperty("cpu")
        public Cpu getCpu() {
            return cpu;
        }

        /**
         * CpuInformation
         * <p>
         *
         *
         */
        @JsonProperty("cpu")
        public void setCpu(Cpu cpu) {
            this.cpu = cpu;
        }

        /**
         * RamInformation
         * <p>
         *
         *
         */
        @JsonProperty("ram")
        public Ram getRam() {
            return ram;
        }

        /**
         * RamInformation
         * <p>
         *
         *
         */
        @JsonProperty("ram")
        public void setRam(Ram ram) {
            this.ram = ram;
        }
    }

    /**
     * TestDefinition
     * <p>
     * A test in your test file.
     *
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonPropertyOrder({
            "id",
            "name",
            "location"
    })
    public static class Test {

        /**
         * Unique id of the test, used to correlate what test killed a mutant.
         * (Required)
         *
         */
        @JsonProperty("id")
        @JsonPropertyDescription("Unique id of the test, used to correlate what test killed a mutant.")
        private String id;
        /**
         * Name of the test, used to display the test.
         * (Required)
         *
         */
        @JsonProperty("name")
        @JsonPropertyDescription("Name of the test, used to display the test.")
        private String name;
        /**
         * OpenEndLocation
         * <p>
         * A location where "end" is not required. Start is inclusive, end is exclusive.
         *
         */
        @JsonProperty("location")
        @JsonPropertyDescription("A location where \"end\" is not required. Start is inclusive, end is exclusive.")
        private OpenEndLocation location;

        /**
         * Unique id of the test, used to correlate what test killed a mutant.
         * (Required)
         *
         */
        @JsonProperty("id")
        public String getId() {
            return id;
        }

        /**
         * Unique id of the test, used to correlate what test killed a mutant.
         * (Required)
         *
         */
        @JsonProperty("id")
        public void setId(String id) {
            this.id = id;
        }

        /**
         * Name of the test, used to display the test.
         * (Required)
         *
         */
        @JsonProperty("name")
        public String getName() {
            return name;
        }

        /**
         * Name of the test, used to display the test.
         * (Required)
         *
         */
        @JsonProperty("name")
        public void setName(String name) {
            this.name = name;
        }

        /**
         * OpenEndLocation
         * <p>
         * A location where "end" is not required. Start is inclusive, end is exclusive.
         *
         */
        @JsonProperty("location")
        public OpenEndLocation getLocation() {
            return location;
        }

        /**
         * OpenEndLocation
         * <p>
         * A location where "end" is not required. Start is inclusive, end is exclusive.
         *
         */
        @JsonProperty("location")
        public void setLocation(OpenEndLocation location) {
            this.location = location;
        }
    }

    /**
     * TestFile
     * <p>
     * A file containing one or more tests
     *
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonPropertyOrder({
            "source",
            "tests"
    })
    public static class TestFile {

        /**
         * Full source code of the test file, this can be used to display in the report.
         *
         */
        @JsonProperty("source")
        @JsonPropertyDescription("Full source code of the test file, this can be used to display in the report.")
        private String source;
        /**
         *
         * (Required)
         *
         */
        @JsonProperty("tests")
        private List<Test> tests;

        /**
         * Full source code of the test file, this can be used to display in the report.
         *
         */
        @JsonProperty("source")
        public String getSource() {
            return source;
        }

        /**
         * Full source code of the test file, this can be used to display in the report.
         *
         */
        @JsonProperty("source")
        public void setSource(String source) {
            this.source = source;
        }

        /**
         *
         * (Required)
         *
         */
        @JsonProperty("tests")
        public List<Test> getTests() {
            return tests;
        }

        /**
         *
         * (Required)
         *
         */
        @JsonProperty("tests")
        public void setTests(List<Test> tests) {
            this.tests = tests;
        }
    }

    /**
     * Thresholds
     * <p>
     * Thresholds for the status of the reported application.
     *
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonPropertyOrder({
            "high",
            "low"
    })
    public static class Thresholds {

        /**
         * Higher bound threshold.
         * (Required)
         *
         */
        @JsonProperty("high")
        @JsonPropertyDescription("Higher bound threshold.")
        private Integer high;
        /**
         * Lower bound threshold.
         * (Required)
         *
         */
        @JsonProperty("low")
        @JsonPropertyDescription("Lower bound threshold.")
        private Integer low;

        /**
         * Higher bound threshold.
         * (Required)
         *
         */
        @JsonProperty("high")
        public Integer getHigh() {
            return high;
        }

        /**
         * Higher bound threshold.
         * (Required)
         *
         */
        @JsonProperty("high")
        public void setHigh(Integer high) {
            this.high = high;
        }

        /**
         * Lower bound threshold.
         * (Required)
         *
         */
        @JsonProperty("low")
        public Integer getLow() {
            return low;
        }

        /**
         * Lower bound threshold.
         * (Required)
         *
         */
        @JsonProperty("low")
        public void setLow(Integer low) {
            this.low = low;
        }
    }
}
