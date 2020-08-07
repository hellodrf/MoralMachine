/*
  Moral Machine: EthicalEngine.java
  Main script for process management.

  ©Runfeng Du
 */

import ethicalengine.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class EthicalEngine {
    public enum Decision {PEDESTRIANS, PASSENGERS}
    /**
     * The main method for commandline management
     * @param args commandline arguments
     */
    public static void main(String[] args) {
        boolean isInteractive = false;
        boolean isConfig = false;
        ArrayList<Scenario> configBuffer = new ArrayList<>();
        String resultOutput = "result.log";
        String joinedArgs = String.join(" ",args);
        String[] commandBundle = (" "+ joinedArgs).split(" -");

        for (String s: commandBundle) {
            String[] parameters = s.split(" ");
            try {
                switch (parameters[0]) {
                    case "c":
                    case "-config":
                        // if config path not provided: open help screen & exit
                        if (parameters.length < 2) {
                            helpScreen();
                        } else {
                            ArrayList<String[]> importedFile = new ArrayList<>();
                            try {
                                BufferedReader inputStream = new BufferedReader(new FileReader(parameters[1]));
                                String line = inputStream.readLine();
                                int lineCount = 1;

                                while (line != null) {
                                    // if not the first row: read that row
                                    if (lineCount != 1) {
                                        importedFile.add(line.split(",", -1));
                                    }
                                    lineCount += 1;
                                    line = inputStream.readLine();
                                }
                                inputStream.close();
                                // send imported file to scenario generator
                                ScenarioGenerator generator = new ScenarioGenerator();
                                configBuffer = generator.parseScenario(importedFile);
                                isConfig = true;
                            } catch (FileNotFoundException e) {
                                System.out.println("ERROR: could not find config file.");
                                System.exit(0);
                            } catch (IOException ignored) {
                            }
                        }
                        break;

                    case "h":
                    case "-help":
                        helpScreen();
                        break;

                    case "i":
                    case "-interactive":
                        isInteractive = true;
                        break;

                    case "r":
                    case "-results":
                        if (parameters.length < 2) {
                            helpScreen();
                        }
                        try {
                            new FileReader(parameters[1]);
                        } catch (FileNotFoundException e) {
                            System.out.println("ERROR: could not print results. Target directory does not exist.");
                            System.exit(0);
                        }
                        // input is fine: change save path
                        resultOutput = parameters[1];
                }
            }
            catch (ArrayIndexOutOfBoundsException ignored) {}
        }

        /*
         * Initiate sessions by parameters parsed
         */
        if (isInteractive) {
            // interactive mode
            InteractiveMode interactive = new InteractiveMode(configBuffer, isConfig);
            interactive.setResultPath(resultOutput);
            interactive.run();
        }
        else {
            // normal mode
            boolean response = true;
            Audit audit = new Audit();
            if (isConfig) {
                audit = new Audit(configBuffer);
                audit.run();
                audit.printToFile(resultOutput);
                audit.printStatistic();
            }
            else {
                while (response) {
                    InteractiveMode.welcomeMessage();
                    System.out.println();
                    System.out.println("How many runs do you want?");
                    audit.run(Integer.parseInt(Audit.scannerObject.nextLine()));
                    audit.printToFile(resultOutput);
                    audit.printStatistic();
                    System.out.println("Would you like to continue? (yes/no)");
                    if (!Audit.scannerObject.nextLine().equals("yes")){
                        response = false;
                    }
                }
            }
        }
    }

    /**
     * Decide a scenario (implemented by decision engine)
     * @param scenario a scenario
     * @return decision of survivors
     * @see DecisionEngine
     */
    public static Decision decide(Scenario scenario) {
        return (new DecisionEngine()).decide(scenario);
    }

    /**
     * Display the help screen and exit
     */
    public static void helpScreen() {
        System.out.println("EthicalEngine - COMP90041 - Final Project");
        System.out.println();
        System.out.println("Usage: java EthicalEngine [arguments]");
        System.out.println();
        System.out.println("Arguments:");
        System.out.println("   -c or --config      Optional: path to config file");
        System.out.println("   -h or --help        Print Help (this message) and exit");
        System.out.println("   -r or --results     Optional: path to result log file");
        System.out.println("   -i or --interactive Optional: launches interactive mode");
        System.exit(0);
    }
}

