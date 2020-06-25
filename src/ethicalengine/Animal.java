/*
 * Moral Machine: ethicalengine/Animal.java
 * Animals in scenarios.
 *
 * Runfeng Du 900437
 */
package ethicalengine;

public class Animal extends Character{
    enum Species {cat, dog, monkey, pig, duck, chicken, horse}

    private String species;
    private boolean isPet;

    /**
     * Empty constructor
     */
    public Animal() {}

    /**
     * Constructor with species specified
     * @param species species of animal
     */
    public Animal(String species){
        this.setSpecies(species);
    }

    /**
     * Default Constructor
     * @param age age of animal
     * @param species species of animal
     * @param gender gender of animal
     * @param bodytype bodytype of animal
     * @param isPet is the animal pet?
     */
    public Animal(int age, String species, Gender gender, BodyType bodytype, boolean isPet){
        super(age, gender, bodytype);
        this.setSpecies(species);
        this.setPet(isPet);
    }

    /**
     * Copy constructor
     * @param a an animal
     */
    public Animal(Animal a){
        this.species = a.species;
        this.isPet = a.isPet;
    }

    /**
     * Set species of animal
     * @param species species of animal
     */
    public void setSpecies(String species) {
        this.species = species;
    }

    /**
     * Get species of animal
     * @return species of animal
     */
    public String getSpecies() {
        return species;
    }

    /**
     * is the animal pet?
     * @return is pet?
     */
    public boolean isPet() {
        return isPet;
    }

    /**
     * Set the animal as pet/not pet
     * @param isPet is pet?
     */
    public void setPet(boolean isPet) {
        this.isPet = isPet;
    }

    /**
     * Convert animal to string
     * @return a string representing the animal
     */
    @Override
    public String toString(){
        StringBuilder string = new StringBuilder();
        string.append(species.toLowerCase());
        if (isPet()){
            string.append(" is pet");
        }
        return string.toString();
    }

    /**
     * Is you?
     * @return always false
     */
    @Override
    public boolean isYou() {
        return false;
    }
}

