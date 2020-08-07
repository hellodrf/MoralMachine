/*
 * Moral Machine: ethicalengine/Scenario.java
 * Scenarios to be judged.
 *
 * ©Runfeng Du
 */
package ethicalengine;

import java.util.ArrayList;
import java.util.Arrays;

public class Scenario {
    private final ArrayList<Character> passengers;
    private final ArrayList<Character> pedestrians;
    private boolean isLegalCrossing;

    /**
     * Default constructor (Array input)
     * @param passengers Array of passengers
     * @param pedestrians Array of pedestrians
     * @param isLegalCrossing is crossing at green light
     */
    public Scenario(Character[] passengers, Character[] pedestrians,
                    boolean isLegalCrossing){
        this.passengers = new ArrayList<>(Arrays.asList(passengers));
        this.pedestrians = new ArrayList<>(Arrays.asList(pedestrians));
        setLegalCrossing(isLegalCrossing);
    }

    /**
     * Default constructor (ArrayList input)
     * @param passengers ArrayList of passengers
     * @param pedestrians ArrayList of pedestrians
     * @param isLegalCrossing is crossing at green light
     */
    public Scenario(ArrayList<Character> passengers, ArrayList<Character> pedestrians,
                    boolean isLegalCrossing){
        this.passengers = passengers;
        this.pedestrians = pedestrians;
        setLegalCrossing(isLegalCrossing);
    }

    /**
     * Get passengers
     * @return ArrayList of passengers
     */
    public ArrayList<Character> getPassengersList() {
        return passengers;
    }

    /**
     * Get pedestrians
     * @return ArrayList of pedestrians
     */
    public ArrayList<Character> getPedestriansList() {
        return pedestrians;
    }

    /**
     * Get pedestrians
     * @return Array of pedestrians
     */
    public Character[] getPedestrians() {
        return pedestrians.toArray(new Character[0]);
    }

    /**
     * Get pedestrians
     * @return Array of pedestrians
     */
    public Character[] getPassengers() {
        return passengers.toArray(new Character[0]);
    }

    /**
     * Is you in car
     * @return Is you in car
     */
    public boolean hasYouInCar() {
        for (Character c: getPassengersList()) {
            if (c.isYou()){
                return true;
            }
        }
        return false;
    }

    /**
     * Is you in lane
     * @return Is you in lane
     */
    public boolean hasYouInLane() {
        for (Character c: getPedestriansList()) {
            if (c.isYou()){
                return true;
            }
        }
        return false;
    }

    /**
     * Is legal crossing
     * @return Is legal crossing
     */
    public boolean isLegalCrossing() {
        return isLegalCrossing;
    }

    /**
     * Set legal crossing
     * @param isLegalCrossing Is legal crossing
     */
    public void setLegalCrossing(boolean isLegalCrossing) {
        this.isLegalCrossing = isLegalCrossing;
    }

    /**
     * Count passengers
     * @return passenger count
     */
    public int getPassengerCount() {
        return getPassengersList().size();
    }

    /**
     * Count pedestrians
     * @return pedestrian count
     */
    public int getPedestrianCount() {
        return getPedestriansList().size();
    }

    /**
     * Convert scenario to string
     * @return String representing the scenario
     */
    public String toString() {
        StringBuilder string = new StringBuilder();
        string.append("======================================\n" +
                "# Scenario\n======================================\n");
        string.append("Legal Crossing: ").append((isLegalCrossing) ? "yes\n" : "no\n");
        string.append("Passengers (").append(getPassengerCount()).append(")\n");
        for (Character c: passengers) {
            string.append("- ").append(c.toString()).append("\n");
        }
        string.append("Pedestrians (").append(getPedestrianCount()).append(")\n");
        for (Character c: pedestrians) {
            string.append("- ").append(c.toString()).append("\n");
        }
        string.deleteCharAt(string.lastIndexOf("\n"));
        return string.toString();
    }
}
