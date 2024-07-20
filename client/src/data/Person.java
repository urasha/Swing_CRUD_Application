package data;

import utility.Validatable;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Main object of the collection
 */
public class Person implements Validatable, Comparable<Person>, Serializable {
    private static final long serialVersionUID = 69L;

    private static int idCounter = 1;

    private String creator;

    private int id;
    private LocalDate creationDate;
    private String name;
    private Coordinates coordinates;
    private int height;
    private EyeColor eyeColor;
    private HairColor hairColor;
    private Country nationality;
    private Location location;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }


    public LocalDate getCreationDate() {
        return creationDate;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getName() {
        return name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public int getHeight() {
        return height;
    }

    public EyeColor getEyeColor() {
        return eyeColor;
    }

    public HairColor getHairColor() {
        return hairColor;
    }

    public Country getNationality() {
        return nationality;
    }

    public Location getLocation() {
        return location;
    }

    /**
     * @param name Person's name
     * @param coordinates Person's coordinates (x, y)
     * @param height Person's height
     * @param eyeColor Person's eye color
     * @param hairColor Person's hair color
     * @param nationality Person's nationality (country)
     * @param location Person's location (x, y, z)
     */
    public Person(String name, Coordinates coordinates, int height, EyeColor eyeColor, HairColor hairColor, Country nationality, Location location) {
        id = idCounter++;
        this.creationDate = LocalDate.now();

        createBaseFields(name, coordinates, height, eyeColor, hairColor, nationality, location);
    }

    /**
     * Make fields for Person's constructor
     * @param name
     * @param coordinates
     * @param height
     * @param eyeColor
     * @param hairColor
     * @param nationality
     * @param location
     */
    private void createBaseFields(String name, Coordinates coordinates, int height, EyeColor eyeColor, HairColor hairColor, Country nationality, Location location) {
        this.name = name;
        this.coordinates = coordinates;
        this.height = height;
        this.eyeColor = eyeColor;
        this.hairColor = hairColor;
        this.nationality = nationality;
        this.location = location;
    }

    @Override
    public boolean validate() {
        if (id <= 0) return false;
        if (name == null || name.isEmpty()) return false;
        if (coordinates == null || !coordinates.validate()) return false;
        if (creationDate == null) return false;
        if (height <= 0) return false;
        if (eyeColor == null) return false;
        if (hairColor == null) return false;
        if (nationality == null) return false;
        if (location == null || !location.validate()) return false;

        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return id == person.id && height == person.height && Objects.equals(name, person.name) && Objects.equals(coordinates, person.coordinates) && Objects.equals(creationDate, person.creationDate) && eyeColor == person.eyeColor && hairColor == person.hairColor && nationality == person.nationality && Objects.equals(location, person.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, coordinates, creationDate, height, eyeColor, hairColor, nationality, location);
    }

    @Override
    public String toString() {
        return "%s,%s,%s,%s,%s,%s,%s,%s,%s".formatted(id, creationDate, name, coordinates, height, eyeColor, hairColor, nationality, location);
    }

    @Override
    public int compareTo(Person person) {
        return name.compareTo(person.name);
    }
}
