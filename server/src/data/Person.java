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

    private final LocalDate creationDate;
    private final String name;
    private final Coordinates coordinates;
    private final int height;
    private final EyeColor eyeColor;
    private final HairColor hairColor;
    private final Country nationality;
    private final Location location;

    public String getName() {
        return name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public EyeColor getEyeColor() {
        return eyeColor;
    }

    public Country getNationality() {
        return nationality;
    }

    public Location getLocation() {
        return location;
    }
    /**
     * @return person's ID
     */
    public int getId() {
        return id;
    }

    /**
     * @return person's creation date
     */
    public LocalDate getCreationDate() {
        return creationDate;
    }

    /**
     * @return person's hair color
     */
    public HairColor getHairColor() {
        return hairColor;
    }

    /**
     * @return person's height
     */
    public int getHeight() {
        return height;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getCreator() {
        return creator;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Person(int id, LocalDate creationDate, String name, Coordinates coordinates, int height, EyeColor eyeColor, HairColor hairColor, Country nationality, Location location) {
        this.id = id;
        this.creationDate = creationDate;
        this.name = name;
        this.coordinates = coordinates;
        this.height = height;
        this.eyeColor = eyeColor;
        this.hairColor = hairColor;
        this.nationality = nationality;
        this.location = location;

        idCounter = Math.max(id, idCounter) + 1;
    }

    public Person(String name, Coordinates coordinates, int height, EyeColor eyeColor, HairColor hairColor, Country nationality, Location location) {
        id = idCounter++;
        this.creationDate = LocalDate.now();
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
