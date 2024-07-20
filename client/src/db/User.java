package db;

import java.io.Serializable;
import java.util.Objects;

public record User(String username, String password) implements Serializable {
    private static final long serialVersionUID = 669L;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username) && Objects.equals(password, user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password);
    }
}
