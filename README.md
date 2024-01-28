# Stryker Callisto
Calculate quality of mutation operators from Stryker JSON reports.

## Running Callisto
Currently Callisto is not yet packaged into a native executable. You can run the jar using:
```shell
java -jar stryker-callisto-0.1.0.jar <program arguments>
```
Java version 17 or later is required. Run `java -version` to see what you are using.

## Usage
Callisto has 4 commands:
- run - Calculate the quality of mutation operators.
- test - Test the effectiveness of a mutation level.
- merge - Merge multiple json reports together (not yet implemented).
- help - Display help information about the specified command.

### Run
Usage of the run command is as follows:
```shell
callisto run [-ktv] -i=<inputFiles> [-i=<inputFiles>...]... -o=<outputFile> [-s=<solver>]

-i, --input=<inputFiles>... Input Stryker mutation json reports to analyse
-k, --killed-only           Only use killed mutants (default false)
-o, --output=<outputFile>   Output file to report findings
-s, --solver=<solver>       Solver to use
-t, --static                Include static mutants (default false)
-v, --verbose               Enable verbose logging
```

#### Filtering mutants
By default Callisto assumes you have a near 100% mutation score, meaning you have killed all the mutants in the project you want to analyse with Callisto, and the only survived mutants are [equivalent mutants](https://stryker-mutator.io/docs/mutation-testing-elements/equivalent-mutants/). However, this can be quite difficult to achieve, so the option `-k` can be used to only consider killed mutants in the analysis of mutation operators, ignoring any survived mutants and making it seem to Callisto that you have a mutation score of 100%. This will affect the results of the analysis though: the more mutants you can include in the analysis the better.

You can also filter out [static mutants](https://stryker-mutator.io/docs/mutation-testing-elements/static-mutants/), which is done by default. The test coverage of static mutants cannot be analysed by Stryker, so Callisto assumes static mutants are only covered by those tests that kill it. This will lower the quality of mutation operators.
When your project contains a high percentage of static mutants, you may want to include them in the analysis using `-t`, so that you have more mutants to analyse.
#### Solvers
To calculate mutator quality Callisto needs to solve a binary integer linear programming problem. For this the open-source library [OR-Tools](https://developers.google.com/optimization) by Google is used.
The backend solver that Callisto will choose for solving the problem is set by the `-s <solver>` commandline flag. by default Google's `GLOP` is used. However, in some circumstances this solver does not cut it, and Callisto will warn you that you should use the solver `SAT` instead.

### Test
```shell
callisto test [-v] -i=<inputFile> -l=<levelFile> -o=<outputFile> [-s=<solver>]

-i, --input=<inputFile>   Input Stryker mutation report to use when testing
                          the level
-l, --level=<levelFile>   File describing the mutation level to test
-o, --output=<outputFile> Output file to report findings
-s, --solver=<solver>     Solver to use
-v, --verbose             Enable verbose logging
```
`test` allows you to see how a mutation level would perform when mutation testing a specific project. Specify the mutation level with `-l` as a text file with line-separated mutation operators. Examples can be found in [example-levels](./example-levels).
Specify the project with `-i` as a mutation testing JSON report, and an output text file with `-o` to report findings. Using verbose mode (`-v`) is recommended here to follow the progress. With `-s` you can specify the solver Callisto will use exactly as with the `run` command.

## Requirements for input reports
#### StrykerJS
- Make sure to generate a JSON report; configure the `jsonReporter` option.
- Set disableBail to `true`.
- Set coverageAnalysis to `perTest` (the default since StrykerJS v5).
- (Optional) Set a sufficiently high enough `TimeoutMS`, for example 60000 (one minute), to ensure mutants get the chance to be killed.

#### Stryker.NET
- Make sure to generate a JSON report; configure the `reporter` option to include `json`.
- Set disable-bail to `true`.
- Set coverage-analysis to `perTest` (default).
- (Optional) Set a sufficiently high enough `additional-timeout`, for example 60000 (one minute), to ensure mutants get the chance to be killed.

#### Stryker4s
At the time of writing Stryker4s does not include all the information that Callisto needs (such as which mutants were killed by which test) in its JSON reports, see [this issue](https://github.com/stryker-mutator/stryker4s/issues/677). Therefore Scala mutation operators cannot be analysed by Callisto yet.

## FAQ
#### What's the difference between mutators and mutation operators?
Stryker defines a list of *mutators* that it has implemented [on its website](https://stryker-mutator.io/docs/mutation-testing-elements/supported-mutators/).
For example the arithmetic operator that is able to replace many arithmetic operators (`+ - / *` etc.) by another. The definition of a *mutation operator* is then one of these specific replacements, for example mutation a `+` to a `-`.
In Callisto this would then be called the `ArithmeticOperator+to-` *mutation operator*. This definition is taken from scientific literature on the subject of mutation testing. Callisto has built-in support to deduce the mutation operator from the JSON report by looking at the original code and the replacement. Should Callisto encounter a mutation operator that it does not know, it will warn you and label it as `<mutatorname>Unknown`. In this case, please open an issue so it can be added to Callisto.

## Build from source
The project can be easily built using maven:
```shell
mvn clean package
```
The `target` folder will then contain a executable jar with all dependencies that you can run as indicated above.