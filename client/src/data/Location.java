package data;

import utility.Validatable;

import java.io.Serializable;
import java.util.Objects;

/**
 * X, Y, Z location of the person
 */
public class Location implements Validatable, Serializable {
    private static final long serialVersionUID = 14L;

    private int x;
    private Double y;
    private float z;

    /**
     * @param location x,y,z location of Person "x;y;z"
     * @throws IllegalArgumentException if bad string format
     */
    public Location(String location) throws IllegalArgumentException {
        String[] xyz = location.split(";");
        this.x = Integer.parseInt(xyz[0]);
        this.y = Double.parseDouble(xyz[1]);
        this.z = Float.parseFloat(xyz[2]);
    }

    @Override
    public boolean validate() {
        return y != null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return x == location.x && Float.compare(z, location.z) == 0 && Objects.equals(y, location.y);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }

    @Override
    public String toString() {
        return "%s;%s;%s".formatted(x, y, z);
    }
}
