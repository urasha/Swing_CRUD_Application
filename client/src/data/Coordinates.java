package data;

import utility.Validatable;

import java.io.Serializable;
import java.util.Objects;

/**
 * X and Y person coordinates
 */
public class Coordinates implements Validatable, Serializable {
    private static final long serialVersionUID = 55L;

    private Double x;
    private double y;

    public Double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    /**
     * @param coordinates x and y coordinates of Person "x;y"
     * @throws IllegalArgumentException if bad string format
     */
    public Coordinates(String coordinates) throws IllegalArgumentException {
        String[] xy = coordinates.split(";");
        this.x = Double.parseDouble(xy[0]);
        this.y = Double.parseDouble(xy[1]);
    }

    @Override
    public boolean validate() {
        if (x == null || x <= -868) return false;
        if (y <= -230) return false;

        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinates that = (Coordinates) o;
        return Double.compare(y, that.y) == 0 && Objects.equals(x, that.x);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "%s;%s".formatted(x, y);
    }
}
