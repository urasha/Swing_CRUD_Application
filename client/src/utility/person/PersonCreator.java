package utility.person;

import data.Person;

/**
 * An object's ability to create Person classes
 */
public interface PersonCreator {
    /**
     * Create new person (ID and creation date automatically set)
     * @return new person object
     */
    Person create();

    /**
     * Checks the name is correct
     * @param arg Possible name
     * @return The result of checking the name for correctness
     */
    boolean validateName(String arg);

    /**
     * Checks the coordinates is correct
     * @param arg Possible coordinates
     * @return The result of checking the coordinates for correctness
     */
    boolean validateCoordinates(String arg);

    /**
     * Checks the height is correct
     * @param arg Possible height
     * @return The result of checking the height for correctness
     */
    boolean validateHeight(String arg);

    /**
     * Checks the eyeColor is correct
     * @param arg Possible eyeColor
     * @return The result of checking the eyeColor for correctness
     */
    boolean validateEyeColor(String arg);

    /**
     * Checks the hairColor is correct
     * @param arg Possible hairColor
     * @return The result of checking the hairColor for correctness
     */
    boolean validateHairColor(String arg);

    /**
     * Checks the nationality is correct
     * @param arg Possible nationality
     * @return The result of checking the nationality for correctness
     */
    boolean validateNationality(String arg);

    /**
     * Checks the location is correct
     * @param arg Possible location
     * @return The result of checking the location for correctness
     */
    boolean validateLocation(String arg);
}
