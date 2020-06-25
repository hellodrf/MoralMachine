/*
 * Moral Machine: ethicalengine/ScenarioGenerator.java
 * Generate scenarios.
 *
 * Runfeng Du 900437
 */
package ethicalengine;

import java.util.ArrayList;
import java.util.Random;
import static ethicalengine.Animal.*;
import static ethicalengine.Person.*;

public class ScenarioGenerator {

    public static class InvalidDataFormatException extends RuntimeException {
        /**
         * Default constructor
         * @param errorMessage the error message
         */
        public InvalidDataFormatException (String errorMessage){
            super(errorMessage);
        }
    }

    private final Random randObj = new Random();
    private int passMax = 5;
    private int passMin = 1;
    private int pedMax = 5;
    private int pedMin = 1;

    // stores all enumerated classes
    Gender[] genders = Gender.values();
    Profession[] professions = Profession.values();
    BodyType[] bodyTypes = BodyType.values();
    Species[] speciesL = Species.values();

    /**
     * Empty constructor
     */
    public ScenarioGenerator() {
        randObj.setSeed(randObj.nextLong());
    }

    /**
     * Constructor with seed specified
     * @param seed seed for random generation
     */
    public ScenarioGenerator(long seed) {
        randObj.setSeed(seed);
    }

    /**
     * Constructor with more specified parameters
     * @param seed seed for random generation
     * @param passMax maximum passengers
     * @param passMin minimum passengers
     * @param pedMax maximum pedestrians
     * @param pedMin minimum pedestrians
     * @throws NumberFormatException when max is smaller than min
     */
    public ScenarioGenerator(long seed, int passMin, int passMax, int pedMin, int pedMax)
            throws NumberFormatException {
        randObj.setSeed(seed);
        if (passMax >= passMin && pedMax >= pedMin){
            setPassengerCountMax(passMax);
            setPassengerCountMin(passMin);
            setPedestrianCountMax(pedMax);
            setPedestrianCountMin(pedMin);
        }
        else{
            throw new NumberFormatException("Minimum value cannot be bigger than maximum value.");
        }
    }

    /**
     * Generate a random person
     * @return a random person
     */
    public Person getRandomPerson() {
        int age = randObj.nextInt(100);
        Gender gender = genders[randObj.nextInt(genders.length)];
        BodyType bodyType = bodyTypes[randObj.nextInt(bodyTypes.length)];
        Profession profession = professions[randObj.nextInt(professions.length-1)];
        boolean isPregnant = (gender == Gender.FEMALE) && (age > 16) && (age <= 68)
                && (randObj.nextInt(10) == 0);

        return new Person(age, profession, gender, bodyType, isPregnant);
    }

    /**
     * Generate a random animal
     * @return a random animal
     */
    public Animal getRandomAnimal() {
        int age = randObj.nextInt(20);
        Gender gender = genders[randObj.nextInt(genders.length)];
        BodyType bodyType = bodyTypes[randObj.nextInt(bodyTypes.length)];
        String species = speciesL[randObj.nextInt(speciesL.length)].toString();
        boolean isPet =  randObj.nextInt(3) == 0;

        return new Animal(age, species, gender, bodyType, isPet);
    }

    /**
     * Generate a random scenario
     * @return a random scenario
     */
    public Scenario generate() {
        int passCount = randObj.nextInt(passMax-passMin+1) + passMin;
        int pedCount = randObj.nextInt(pedMax-pedMin+1) + pedMin;
        int yourPosition = randObj.nextInt(3); // 0: absent, 1: passenger, 2: pedestrian.
        boolean isLegalCrossing = randObj.nextInt(2) == 0;
        ArrayList<Character> passList = new ArrayList<>();
        ArrayList<Character> pedList= new ArrayList<>();

        Person you = getRandomPerson();
        you.setAsYou(true);
        switch (yourPosition){
            case 1:
                passList.add(you);
                passCount -= 1;
                break;

            case 2:
                pedList.add(you);
                pedCount -= 1;
                break;
        }
        // passengers
        for (int i = 0; i < passCount ; i++) {
            passList.add((randObj.nextInt(2) == 0) ? getRandomPerson() : getRandomAnimal());
        }
        // pedestrians
        for (int i = 0; i < pedCount ; i++) {
            pedList.add((randObj.nextInt(2) == 0) ? getRandomPerson() : getRandomAnimal());
        }
        return new Scenario(passList, pedList, isLegalCrossing);
    }

    /**
     * Parse a scenario from a config string
     * @param importedFile string of imported configs
     * @return parsed scenario
     */
    public ArrayList<Scenario> parseScenario(ArrayList<String[]> importedFile) {
        int lineCount = 1;
        boolean isGreen = true;
        ArrayList<Character> passengers = new ArrayList<>();
        ArrayList<Character> pedestrians = new ArrayList<>();
        ArrayList<Scenario> scenarios = new ArrayList<>();

        for (String[] line : importedFile) {
            lineCount += 1;
            try {
                if (line.length != 10) {
                    throw new InvalidDataFormatException(
                            "WARNING: invalid data format in config file in line " + lineCount);
                }
                switch (line[0].toLowerCase()){
                    case "person":
                        try {
                            Person person = new Person();
                            person.setGender(line[1], lineCount);
                            person.setAge(line[2], lineCount);
                            person.setBodyType(line[3], lineCount);
                            person.setProfession(line[4], lineCount);
                            person.setPregnant(Boolean.parseBoolean(line[5]));
                            person.setAsYou(Boolean.parseBoolean(line[6]));

                            if (line[9].equals("passenger")) {
                                passengers.add(person);
                            }
                            else {
                                pedestrians.add(person);
                            }
                        }
                        catch (NumberFormatException e) {
                            System.out.println("WARNING: invalid number format in config file in line " + lineCount);
                        }
                        catch (RuntimeException e) {
                            System.out.println(e.getMessage());
                        }
                        break;

                    case "animal":
                        try {
                            Animal animal = new Animal();
                            animal.setGender(line[1],lineCount);
                            animal.setAge(line[2], lineCount);
                            animal.setBodyType(line[3], lineCount);
                            animal.setPet(Boolean.parseBoolean(line[8]));

                            if (!line[7].equals("")) {
                                animal.setSpecies(line[7]);
                            }
                            else {
                                animal.setSpecies("dog");
                            }
                            if (line[9].equals("passenger")) {
                                    passengers.add(animal);
                            }
                            else {
                                pedestrians.add(animal);
                            }
                        }
                        catch (RuntimeException e) {
                            System.out.println(e.getMessage());
                        }
                        break;

                    case "scenario:green":
                    case "scenario:red":
                        if (lineCount != 2) {
                            Scenario s = new Scenario(passengers, pedestrians, isGreen);
                            scenarios.add(s);
                            passengers = new ArrayList<>();
                            pedestrians = new ArrayList<>();
                        }
                        isGreen = line[0].toLowerCase().equals("scenario:green");
                        break;
                }
            }
            catch (RuntimeException e) {
                System.out.println(e.getMessage());
            }
        }
        Scenario s = new Scenario(passengers, pedestrians, isGreen);
        scenarios.add(s);
        return scenarios;
    }

    /**
     * Set maximum passenger
     * @param passMin maximum passenger
     */
    public void setPassengerCountMin(int passMin) {
        this.passMin = passMin;
    }


    /**
     * Set minimum passengers
     * @param passMax minimum passengers
     */
    public void setPassengerCountMax(int passMax) {
        this.passMax = passMax;
    }

    /**
     * Set maximum pedestrians
     * @param pedMax maximum pedestrians
     */
    public void setPedestrianCountMax(int pedMax) {
        this.pedMax = pedMax;
    }

    /**
     * Set minimum pedestrians
     * @param pedMin minimum pedestrians
     */
    public void setPedestrianCountMin(int pedMin) {
        this.pedMin = pedMin;
    }
}
