// File: src/main/java/dao/AdminDAO.java
package dao;

import model.Admin;
import util.PasswordUtil; 
import java.sql.*;

public class AdminDAO {

    public boolean validate(String userid, String password) {
        String storedPasswordHash = null;
        // 1. Select ONLY the password hash from the database using the userid
        String sql = "SELECT password FROM admins WHERE userid=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, userid);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                // Get the stored hash
                storedPasswordHash = rs.getString("password");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        
        // 2. Use PasswordUtil to securely compare the plaintext password with the hash
        return storedPasswordHash != null && PasswordUtil.verifyPassword(password, storedPasswordHash);
    }

    // Get admin details by Staff ID (Used by the validate method to retrieve the hash)
    public Admin getAdminByUserId(String userid) {
        String sql = "SELECT * FROM admins WHERE userid=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, userid);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Admin admin = new Admin();
                admin.setAdminId(rs.getInt("admin_id"));
                admin.setName(rs.getString("name"));
                admin.setEmail(rs.getString("email"));
                admin.setUserid(rs.getString("userid"));
                admin.setPassword(rs.getString("password"));
                return admin;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}