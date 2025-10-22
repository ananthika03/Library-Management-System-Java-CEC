package util;

public class PasswordUtil {

    /**
     * !!! THIS IS INSECURE - Does not hash. Returns plain text. !!!
     */
    public static String hashPassword(String password) {
        // Returns the password as-is
        return password;
    }

    /**
     * !!! THIS IS INSECURE - Compares plain text. !!!
     */
    public static boolean verifyPassword(String plainPassword, String storedPassword) {
        // Simple string comparison
        return plainPassword.equals(storedPassword);
    }
}