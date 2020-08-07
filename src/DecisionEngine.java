/*
 * Moral Machine: ethicalengine/DecisionEngine.java
 * The engine to make decisions.
 *
 * Runfeng Du
 */

import ethicalengine.Animal;
import ethicalengine.Character;
import ethicalengine.Person;
import ethicalengine.Scenario;
import java.util.HashMap;
import static ethicalengine.Character.BodyType.*;
import static ethicalengine.Person.AgeCategory.*;
import static ethicalengine.Person.Profession.*;

public class DecisionEngine {
    /*
     * Merits of characteristics
     */
    final HashMap<Person.Profession, Double> professionMerit =
            new HashMap<Person.Profession, Double>() {
        { put(CEO, 1.0);
          put(CRIMINAL, 0.5);
          put(DOCTOR, 1.5);
          put(FIREFIGHTER, 1.5);
          put(JANITOR, 1.0);
          put(HOMELESS, 0.7);
          put(UNEMPLOYED, 1.0);
          put(STUDENT, 1.1);
          put(PROFESSOR, 1.1);
          put(Person.Profession.UNKNOWN, 1.0);
          put(NONE, 1.0);
          put(null, 1.0);
        }
    };
    final HashMap<Person.AgeCategory, Double> ageTypeMerit =
            new HashMap<Person.AgeCategory, Double>() {
        { put(BABY, 3.0);
          put(CHILD, 2.5);
          put(ADULT, 1.0);
          put(SENIOR, 0.8);
          put(null, 1.0);
        }
    };
    final HashMap<Character.BodyType, Double> bodyTypeMerit =
            new HashMap<Character.BodyType, Double>() {
        { put(AVERAGE, 1.0);
          put(ATHLETIC, 1.05);
          put(OVERWEIGHT, 0.95);
          put(UNSPECIFIED, 1.0);
          put(null, 1.0);
        }
    };
    final double illegalCrossingDemerit = 0.7;
    final double pregnantMerit = 3.0;
    final double animalMerit = 0.01;
    final double petMerit = 20.0;
    final double youMerit = 2.0;

    /**
     * Make a decision on a scenario
     * @param scenario a scenario
     * @return decision
     */
    public EthicalEngine.Decision decide(Scenario scenario){
        double passengerRating = 0;
        double pedestrianRating = 0;

        for (Character c : scenario.getPassengersList()) {
            passengerRating += rateCharacter(c);
        }
        for (Character c: scenario.getPedestriansList()) {
            pedestrianRating += rateCharacter(c);
        }
        if (!scenario.isLegalCrossing()){
            pedestrianRating = pedestrianRating * illegalCrossingDemerit;
        }
        return (passengerRating > pedestrianRating) ?
                EthicalEngine.Decision.PASSENGERS : EthicalEngine.Decision.PEDESTRIANS;
    }

    /**
     * Rate characters
     * @param c a character
     * @return rating
     */
    private double rateCharacter(Character c) {
        if (c instanceof Person) {
            return professionMerit.get(((Person) c).getProfession()) *
                    ageTypeMerit.get(((Person) c).getAgeCategory()) *
                    bodyTypeMerit.get(c.getBodyType()) *
                    ((((Person) c).isPregnant()) ? pregnantMerit : 1.0) *
                    (c.isYou() ? youMerit : 1.0);
        }
        if (c instanceof Animal) {
            return animalMerit * (((Animal) c).isPet()? petMerit : 1.0);
        }
        return 0;
    }
}
