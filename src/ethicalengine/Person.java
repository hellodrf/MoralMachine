/*
 * Moral Machine: ethicalengine/Person.java
 * Persons in scenarios.
 *
 * Runfeng Du 900437
 */

package ethicalengine;

public class Person extends Character{

    public enum Profession {DOCTOR, CEO, FIREFIGHTER, JANITOR, CRIMINAL, HOMELESS,
        STUDENT, PROFESSOR, UNEMPLOYED, UNKNOWN, NONE}
    public enum AgeCategory {BABY, CHILD, ADULT, SENIOR}

    private Profession profession = Profession.UNKNOWN;
    private boolean isPregnant;
    private boolean isYou;

    /**
     * Empty constructor
     */
    public Person() {}

    /**
     * Default constructor
     * @param age age of person
     * @param profession profession of person
     * @param gender gender of person
     * @param bodytype bodytype of person
     * @param isPregnant is the person pregnant
     */
    public Person(int age, Profession profession, Gender gender,
                  BodyType bodytype, boolean isPregnant) {
        super(age, gender, bodytype);
        this.setProfession(profession);
        this.setPregnant(isPregnant);
    }

    public Person(int age, Gender gender, BodyType bodytype) {
        super(age, gender, bodytype);
        this.setProfession(Profession.UNKNOWN);
        this.setPregnant(false);
    }
    /**
     * Copy constructor
     * @param p a person
     */
    public Person(Person p){
        super(p.getAge(), p.getGender(), p.getBodyType());
        this.profession = p.profession;
        this.isPregnant = p.isPregnant;
        this.isYou = p.isYou;
    }

    /**
     * Get profession of person
     * @return profession of person
     */
    public Profession getProfession() {
        return profession;
    }

    /**
     * Set profession of person
     * @param profession profession of person
     */
    public void setProfession(Profession profession) {
        if (getAgeCategory() == AgeCategory.ADULT){
            this.profession = profession;
        }
        else {
            this.profession = Profession.NONE;
        }
    }

    /**
     * Set profession of person (specified for parseScenario)
     * @param profession profession of person
     * @param lineNumber line count of parsing scenario
     * @throws InvalidCharacteristicException when invalid input received
     * @see ScenarioGenerator
     */
    public void setProfession(String profession, int lineNumber)
            throws InvalidCharacteristicException {
        boolean professionSet = false;
        for (Profession p: Profession.values()) {
            if (p.name().equals(profession.toUpperCase())){
                setProfession(p);
                professionSet = true;
            }
        }
        // if invalid profession name received: alert user & set to unknown
        if (!professionSet && !profession.equals("")){
            setProfession(Profession.UNKNOWN);
            throw new InvalidCharacteristicException(
                    "WARNING: invalid characteristic in config file in line " + lineNumber);
        }
    }

    /**
     * Is the person pregnant
     * @return is pregnant?
     */
    public boolean isPregnant() {
        return isPregnant;
    }

    /**
     * Set the person as pregnant/not pregnant
     * @param isPregnant is pregnant?
     */
    public void setPregnant(boolean isPregnant) {
        if (getGender()==Gender.FEMALE) {
            this.isPregnant = isPregnant;
        }
        else {
            this.isPregnant = false;
        }
    }

    /**
     * Get age category of person
     * @return age category of person
     */
    public AgeCategory getAgeCategory() {
        if (getAge() <= 4) {
            return AgeCategory.BABY;
        }
        else if (getAge() <= 16) {
            return AgeCategory.CHILD;
        }
        else if (getAge() <= 68) {
            return AgeCategory.ADULT;
        }
        else{
            return AgeCategory.SENIOR;
        }
    }

    /**
     * Is the person you?
     * @return is you?
     */
    @Override
    public boolean isYou() {
        return isYou;
    }

    /**
     * Set the person as you/not you
     * @param isYou is you?
     */
    public void setAsYou(boolean isYou) {
        this.isYou = isYou;
    }

    /**
     * Convert the person to string
     * @return string represents a person
     */
    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        if (isYou()){
            string.append("you ");
        }
        string.append(getBodyType().name().toLowerCase()).append(" ");
        string.append(getAgeCategory().name().toLowerCase()).append(" ");
        if (getAgeCategory()==AgeCategory.ADULT){
            string.append(getProfession().name().toLowerCase()).append(" ");
        }
        string.append(getGender().name().toLowerCase());
        if (isPregnant()){
            string.append(" pregnant");
        }
        return string.toString();
    }
}
