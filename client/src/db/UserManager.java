package db;

public class UserManager {
    private static User currentUser;
    private static User possibleUser;

    /**
     * @return variable of possible user
     */
    public static User getPossibleUser() {
        return possibleUser;
    }

    /**
     * @param possibleUser variable of possible user
     */
    public static void setPossibleUser(User possibleUser) {
        UserManager.possibleUser = possibleUser;
    }

    /**
     * @return variable of current user after login
     */
    public static User getCurrentUser() {
        return currentUser;
//        return new User("urasha", "12345");
    }

    /**
     * Set current user, clear possible user
     * @param possibleUser variable of possible user
     */
    public static void setCurrentUser(User possibleUser) {
        currentUser = possibleUser;
        UserManager.possibleUser = null;
    }
}
