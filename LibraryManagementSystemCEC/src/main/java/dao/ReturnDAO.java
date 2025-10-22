// File: src/main/java/dao/ReturnDAO.java
package dao;

import model.Return;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReturnDAO {

    public void addReturn(Return r) {
    	String sql = "INSERT INTO book_returns (student_id, book_id, return_date, fine) VALUES (?, ?, ?, ?)";        // --- DEBUG PRINT START ---
        System.out.println("--- ReturnDAO addReturn() CALLED ---");
        System.out.println("SQL Query: " + sql);
        System.out.println("Attempting to add: StudentID=" + r.getStudentId() + ", BookID=" + r.getBookId());
        // --- DEBUG PRINT END ---

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            System.out.println("DB Connection successful."); // DEBUG

            ps.setInt(1, r.getStudentId());
            ps.setInt(2, r.getBookId());
            ps.setDate(3, Date.valueOf(r.getReturnDate()));
            ps.setDouble(4, r.getFine());
            
            System.out.println("Executing update..."); // DEBUG
            ps.executeUpdate();
            System.out.println("...Update successful! Record should be inserted."); // DEBUG

        } catch (SQLException e) {
            // --- THIS IS THE MOST IMPORTANT PRINT ---
            System.out.println("!!! --- DATABASE ERROR --- !!!");
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("ErrorCode: " + e.getErrorCode());
            System.out.println("Message: " + e.getMessage());
            System.out.println("---------------------------------");
            e.printStackTrace(); // Prints the full stack trace
            // --- END ---
        }
    }

    // ... (getAllReturns method is unchanged) ...
    public List<Return> getAllReturns() {
        List<Return> list = new ArrayList<>();
        String sql = "SELECT * FROM book_returns";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Return r = new Return();
                r.setStudentId(rs.getInt("student_id"));
                r.setBookId(rs.getInt("book_id"));
                r.setReturnDate(rs.getDate("return_date").toLocalDate());
                r.setFine(rs.getDouble("fine"));
                list.add(r);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}