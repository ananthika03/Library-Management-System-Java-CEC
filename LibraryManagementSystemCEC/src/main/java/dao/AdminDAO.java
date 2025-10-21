package dao;

import model.Admin;
import java.sql.*;

public class AdminDAO {

    // Validate login using userid + password
    public boolean validate(String userid, String password) {
        String sql = "SELECT * FROM admins WHERE userid=? AND password=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, userid);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();
            return rs.next(); // true only if matching record exists

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Get admin details by Staff ID
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
