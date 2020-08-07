/*
 * Moral Machine: InteractiveMode.java
 * Manages interactive sessions
 *
 * ©Runfeng Du
 */

import ethicalengine.Scenario;
import ethicalengine.ScenarioGenerator;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class InteractiveMode {
    private ArrayList<Scenario> configs = new ArrayList<>();
    private boolean configMode = false;
    private boolean saveToFile = false;
    private String resultPath = "result.log";

    /**
     * Empty constructor
     */
    public InteractiveMode() {}

    /**
     * Constructor with configs provided
     * @param configs configs of scenarios
     * @param isConfig is config provided
     */
    public InteractiveMode(ArrayList<Scenario> configs, boolean isConfig) {
        this.configs = configs;
        configMode = isConfig;
    }

    /**
     * Query for file saving consent
     */
    private void setConsent() {
        boolean consentSet = false;
        while (!consentSet){
            System.out.println("Do you consent to have your decisions saved to a file? (yes/no)");
            String commandReceived = Audit.scannerObject.nextLine();
            if (commandReceived.equals("yes")){
                saveToFile = true;
                consentSet = true;
            }
            else if (commandReceived.equals("no")){
                saveToFile = false;
                consentSet = true;
            }
            else {
                System.out.print("Invalid response.");
            }
        }
    }

    /**
     * Display welcome messages
     */
    public static void welcomeMessage() {
        try {
            BufferedReader inputStream = new BufferedReader(new FileReader("welcome.ascii"));
            String line = inputStream.readLine();
            while (line != null){
                System.out.println(line);
                line = inputStream.readLine();
            }
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void runAudit(Audit audit, ArrayList<Scenario> config) {
        audit.loadScenarios(config);
        audit.interactiveRun();
        audit.printStatistic();
        if (saveToFile){
            audit.printToFile(resultPath);
        }
    }

    /**
     * Start the interactive mode
     */
    public void run() {
        welcomeMessage();
        setConsent();
        Audit audit = new Audit();
        audit.setAuditType("User");

        if (!configMode) {
            ScenarioGenerator generator = new ScenarioGenerator();
            for (int i = 0; i < 3; i++) {
                configs.add(generator.generate());
            }
        }
        if (configs.size() <= 3){
            if (configMode) {
                runAudit(audit, configs);
            } else {
                boolean response = true;
                while (response){
                    configs = new ArrayList<>();
                    ScenarioGenerator generator = new ScenarioGenerator();
                    for (int i = 0; i < 3; i++) {
                        configs.add(generator.generate());
                    }
                    runAudit(audit, configs);
                    System.out.print("Would you like to continue? (yes/no)");
                    String commandReceived = Audit.scannerObject.nextLine();
                    if (!commandReceived.equals("yes")){
                        response = false;
                    }
                }
            }
        }
        else {
            for (int i = 0; i < configs.size()/3; i++) {
                ArrayList<Scenario> configSlice = new ArrayList<>(configs.subList(3*i, 3*i+3));
                runAudit(audit, configSlice);
                System.out.println("Would you like to continue? (yes/no)");
                String commandReceived = Audit.scannerObject.nextLine();
                if (!commandReceived.equals("yes")){
                    break;
                }
            }
            if (configs.size()%3>0){
                ArrayList<Scenario> configSlice = new ArrayList<>(configs.subList(3*(configs.size()/3),
                        3*(configs.size()/3) + configs.size()%3));
                runAudit(audit, configSlice);
            }
        }
        System.out.println("That's all. Press any key to quit.");
        Audit.scannerObject.nextLine();
        System.exit(0);
    }

    /**
     * Set path of file saving
     * @param resultPath path for file saving
     */
    public void setResultPath(String resultPath) {
        this.resultPath = resultPath;
    }

    /**
     * Get path of file saving
     * @return path of file saving
     */
    public String getResultPath() {
        return resultPath;
    }
}
