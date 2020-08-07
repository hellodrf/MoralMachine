/*
 * Moral Machine: ethicalengine/Character.java
 * Characters in scenarios.
 *
 * Runfeng Du
 */
package ethicalengine;

public abstract class Character {

    public static class InvalidCharacteristicException extends RuntimeException {
        /**
         * Default constructor
         * @param errorMessage the error message
         */
        public InvalidCharacteristicException(String errorMessage) {
            super(errorMessage);
        }
    }

    public enum Gender {MALE, FEMALE, UNKNOWN}
    public enum BodyType {AVERAGE, ATHLETIC, OVERWEIGHT, UNSPECIFIED}

    private int age;
    private Gender gender = Gender.UNKNOWN;
    private BodyType bodyType = BodyType.UNSPECIFIED;

    /**
     * Empty constructor
     */
    public Character() {}

    /**
     * Default constructor
     * @param age age of character
     * @param gender gender of character
     * @param bodyType body type of character
     */
    public Character(int age, Gender gender, BodyType bodyType) {
        try {
            this.setAge(age);
            this.setGender(gender);
            this.setBodyType(bodyType);
        }
        catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Copy constructor
     * @param c a character
     */
    public Character(Character c) {
        this.age = c.age;
        this.gender = c.gender;
        this.bodyType = c.bodyType;
    }

    /**
     * Get age of character
     * @return age of character
     */
    public int getAge(){
        return this.age;
    }

    /**
     * Set age of character
     * @param age age of character (int)
     * @throws NumberFormatException when invalid age received
     */
    public void setAge(int age) throws NumberFormatException {
        if (age>=0) {
            this.age = age;
        }
        else{
            throw new NumberFormatException("Invalid input: age must be positive");
        }
    }

    /**
     * Set age of character (specified for parseScenario)
     * @param age age of character (int)
     * @param lineCount line count of parsing scenario
     * @throws NumberFormatException when invalid input received
     * @see ScenarioGenerator
     */
    public void setAge(String age, int lineCount) throws NumberFormatException {
        try {
            setAge(Integer.parseInt(age));
        }
        catch (java.lang.NumberFormatException e) {
            System.out.println("WARNING: invalid number format in config file in line " + lineCount);
            setAge(25);
        }
    }

    /**
     * Get gender of character
     * @return gender of character
     */
    public Gender getGender(){
        return this.gender;
    }

    /**
     * Set gender of character
     * @param gender gender of character
     */
    public void setGender(Gender gender) {
        this.gender = gender;
    }

    /**
     * Set gender of character (specified for parseScenario)
     * @param gender gender of character
     * @param lineNumber line count of parsing scenario
     * @throws InvalidCharacteristicException when invalid input received
     * @see ScenarioGenerator
     */
    public void setGender(String gender, int lineNumber)
            throws InvalidCharacteristicException {
        if (gender.toLowerCase().equals("female")){
            this.gender = Gender.FEMALE;
        }
        else if (gender.toLowerCase().equals("male")){
            this.gender = Gender.MALE;
        }
        else {
            this.gender = Gender.UNKNOWN;
            if (!gender.equals("")){
                throw new InvalidCharacteristicException(
                        "WARNING: invalid characteristic in config file in line " + lineNumber);
            }
        }
    }

    /**
     * Get body type of character
     * @return body type of character
     */
    public BodyType getBodyType() {
        return bodyType;
    }

    /**
     * Set body type of character
     * @param bodyType body type of character
     */
    public void setBodyType(BodyType bodyType) {
        this.bodyType = bodyType;
    }

    /**
     * Set body type of character (specified for parseScenario)
     * @param bodyType body type of character
     * @param lineNumber line count of parsing scenario
     * @throws InvalidCharacteristicException when invalid input received
     */
    public void setBodyType(String bodyType, int lineNumber)
            throws InvalidCharacteristicException {
        boolean bodyTypeSet = false;
        for (BodyType b: BodyType.values()) {
            if (b.name().equals(bodyType.toUpperCase())){
                this.bodyType = b;
                bodyTypeSet = true;
            }
        }
        if (!bodyTypeSet && !bodyType.equals("")){
            this.bodyType = BodyType.UNSPECIFIED;
            throw new InvalidCharacteristicException(
                    "WARNING: invalid characteristic in config file in line " + lineNumber);
        }
    }

    /**
     * Convert character to string
     * @return string represents a character
     * @see Person
     * @see Animal
     */
    public abstract String toString();

    /**
     * Is the character you
     * @return is you?
     * @see Person
     * @see Animal
     */
    public abstract boolean isYou();
}


