package util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordUtil {

    /**
     * Hashes a plain-text password using SHA-256.
     * @param password The plain password
     * @return Hexadecimal hashed password
     */
    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }

    /**
     * Verifies if a plain password matches the hashed password.
     * @param plainPassword The plain password
     * @param hashedPassword The stored hashed password
     * @return true if match, false otherwise
     */
    public static boolean verifyPassword(String plainPassword, String hashedPassword) {
        return hashPassword(plainPassword).equals(hashedPassword);
    }

    // Simple testing
    public static void main(String[] args) {
        String password = "admin123";
        String hashed = hashPassword(password);
        System.out.println("Hashed: " + hashed);

        boolean match = verifyPassword("admin123", hashed);
        System.out.println("Password match: " + match);
    }
}
