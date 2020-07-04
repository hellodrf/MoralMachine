/*
 * Moral Machine: Audit.java
 * manages audit sessions.
 *
 * Runfeng Du 900437
 */

import ethicalengine.*;
import ethicalengine.Character;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Scanner;

public class Audit {
    private String auditType = "Unspecified";
    private final HashMap<String, int[]> statisticsDatabase = new HashMap<>();
    private int runCount = 0;
    private int surviveCount = 0;
    private double ageSum = 0.0;
    private ArrayList<Scenario> scenarioBuffer;
    // a public scanner
    public static Scanner scannerObject = new Scanner(System.in);

    /**
     * Empty constructor
     */
    public Audit() {}

    /**
     * Constructor with scenarios specified
     * @param scenarioSet set of scenarios to be run
     */
    public Audit(ArrayList<Scenario> scenarioSet) {
        scenarioBuffer = scenarioSet;
    }

    /**
     * Manually load scenarios into audit
     * @param scenarioSet set of scenarios to be run
     */
    public void loadScenarios (ArrayList<Scenario> scenarioSet) {
        scenarioBuffer = scenarioSet;
    }

    /**
     * run audit by randomly generated scenarios.
     * @param runs number of audit to be run
     */
    public void run(int runs) {
        ScenarioGenerator generator = new ScenarioGenerator();
        for (int i = 0; i < runs ; i++) {
            Scenario scenario = generator.generate();
            EthicalEngine.Decision result = EthicalEngine.decide(scenario);
            this.updateStatistics(scenario, result);
            runCount += 1;
        }
    }

    /**
     * Run audit by imported scenarios.
     */
    public void run() {
        for (Scenario s: scenarioBuffer) {
            EthicalEngine.Decision result = EthicalEngine.decide(s);
            this.updateStatistics(s, result);
            runCount += 1;
        }
    }

    /**
     * Run audit in interactive mode
     */
    public void interactiveRun() {
        for (Scenario s: scenarioBuffer) {
            EthicalEngine.Decision decision = EthicalEngine.Decision.PASSENGERS;
            boolean decisionMade = false;

            System.out.print(s.toString());
            while (!decisionMade){
                System.out.println();
                System.out.println("Who should be saved? (passenger(s) [1] or pedestrian(s) [2])");
                switch (scannerObject.nextLine()){
                    case "passenger":
                    case "passengers":
                    case "1":
                        decision = EthicalEngine.Decision.PASSENGERS;
                        decisionMade = true;
                        break;

                    case "pedestrian":
                    case "pedestrians":
                    case "2":
                        decision = EthicalEngine.Decision.PEDESTRIANS;
                        decisionMade = true;
                        break;

                    default:
                        System.out.print("Invalid response. ");
                        break;
                }
            }
            this.updateStatistics(s, decision);
            runCount += 1;
        }
        // clear buffer
        scenarioBuffer = new ArrayList<>();
    }

    /**
     * get audit type
     * @return the audit type
     */
    public String getAuditType() {
        return auditType;
    }

    /**
     * Set audit type
     * @param auditType the audit type
     */
    public void setAuditType(String auditType) {
        this.auditType = auditType;
    }

    private void updateStatistics(Scenario scenario, EthicalEngine.Decision decision) {
        ArrayList<Character> survivors = (decision ==
                EthicalEngine.Decision.PASSENGERS) ?
                scenario.getPassengersList() : scenario.getPedestriansList();
        ArrayList<Character> sacrifices = (decision ==
                EthicalEngine.Decision.PASSENGERS) ?
                scenario.getPedestriansList() : scenario.getPassengersList();
        //survivors
        for (Character c: survivors) {
            characterUpdate(c, 1, scenario.isLegalCrossing());
        }
        //sacrifices
        for (Character c: sacrifices) {
            characterUpdate(c,0, scenario.isLegalCrossing());
        }

    }

    private void entryUpdate(String key, int modifier) {
        if (!key.equals("UNKNOWN") && !key.equals("NONE") && !key.equals("UNSPECIFIED")){
            statisticsDatabase.putIfAbsent(key, new int[]{0, 0});
            int[] entry = statisticsDatabase.get(key);
            statisticsDatabase.replace(key, new int[]{entry[0]+1, entry[1]+modifier});
        }
    }

    private void characterUpdate(Character c, int modifier, boolean isLegalCrossing) {
        entryUpdate(isLegalCrossing? "green": "red", modifier);
        if (c instanceof Person) {
            if (modifier == 1){
                ageSum += c.getAge();
                surviveCount += 1;
            }
            entryUpdate(c.getBodyType().name(), modifier);
            entryUpdate(c.getGender().name(), modifier);
            entryUpdate(((Person) c).getProfession().name(), modifier);
            entryUpdate(((Person) c).getAgeCategory().name(), modifier);
            entryUpdate("person", modifier);
            if ((c).isYou()){
                entryUpdate("you", modifier);
            }
            if (((Person) c).isPregnant()){
                entryUpdate("pregnant", modifier);
            }
        }
        else if (c instanceof Animal) {
            entryUpdate(((Animal) c).getSpecies(), modifier);
            entryUpdate("animal", modifier);
            if (((Animal) c).isPet()){
                entryUpdate("pet", modifier);
            }
        }
    }

    /**
     * Convert audit statistics to string
     * @return string representing an audit
     */
    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        string.append("======================================\n");
        string.append("# ").append(getAuditType()).append(" Audit\n");
        string.append("======================================\n");
        string.append("- % SAVED AFTER ").append(runCount).append(" RUNS\n");

        // sort database by survival rate.
        ArrayList <String[]> sortedDatabase = new ArrayList<>();
        for (String key:statisticsDatabase.keySet()) {
            int[] entry = statisticsDatabase.get(key);
            double survivalRate = (double)entry[1]/(double) entry[0];
            sortedDatabase.add(new String[]{key, String.valueOf(survivalRate)});
        }
        //sortedDatabase.sort(Comparator.comparingDouble(o -> -1 * Double.parseDouble(o[1])));
        sortedDatabase.sort(Comparator.comparing((String[] o) -> -1 * Double.parseDouble(o[1]))
                .thenComparing(o -> o[0]));

        for (String[] s: sortedDatabase) {
            string.append(s[0].toLowerCase()).append(": ")
                    .append(String.format("%.1f", Double.parseDouble(s[1])))
                    .append("\n");
        }
        string.append("--\n");
        string.append("average age: ").append(new DecimalFormat("#.#")
                .format(ageSum/surviveCount)).append("\n");
        return string.toString();
    }

    /**
     * Print statistics out
     */
    public void printStatistic() {
        System.out.println(toString());
    }

    /**
     * Print statistics to file
     * @param path path of file output
     */
    public void printToFile(String path) {
        try {
            PrintWriter outputStream = new PrintWriter(new FileOutputStream(path, true));
            outputStream.println(toString());
            outputStream.close();
        }
        catch (FileNotFoundException e) {
            System.out.println(
                    "ERROR: could not print results. Target directory does not exist.");
        }
    }
}