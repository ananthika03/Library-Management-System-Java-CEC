// File: src/main/java/dao/ReturnDAO.java
package dao;

import model.Return;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReturnDAO {

    // This method is confirmed to be WORKING.
    public void addReturn(Return r) {
        String sql = "INSERT INTO book_returns (student_id, book_id, return_date, fine) VALUES (?, ?, ?, ?)";
        System.out.println("--- ReturnDAO addReturn() CALLED ---");
        System.out.println("SQL Query: " + sql);
        System.out.println("Attempting to add: StudentID=" + r.getStudentId() + ", BookID=" + r.getBookId());
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false); 
            System.out.println("DB Connection successful."); 
            ps = conn.prepareStatement(sql); 
            ps.setInt(1, r.getStudentId());
            ps.setInt(2, r.getBookId());
            ps.setDate(3, Date.valueOf(r.getReturnDate()));
            ps.setDouble(4, r.getFine());
            System.out.println("Executing update..."); 
            ps.executeUpdate();
            System.out.println("...Update successful! Record inserted."); 
            conn.commit();
            System.out.println("...Changes COMMITTED to database.");
        } catch (SQLException e) {
            System.out.println("!!! --- DATABASE ERROR in addReturn --- !!!");
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("ErrorCode: " + e.getErrorCode());
            System.out.println("Message: " + e.getMessage());
            System.out.println("---------------------------------");
            e.printStackTrace(); 
            try { if (conn != null) { System.out.println("...Rolling back transaction."); conn.rollback(); } } catch (SQLException ex) { ex.printStackTrace(); }
        } finally {
            try { if (ps != null) ps.close(); if (conn != null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    
    // --- THE PROBLEM IS HERE ---
    // This method now has logging.
    public List<Return> getAllReturns() {
        // 1. This line will print first
        System.out.println("--- ReturnDAO getAllReturns() CALLED ---"); 
        List<Return> list = new ArrayList<>();
        String sql = "SELECT * FROM book_returns";
        
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            
            // 2. If the query works, this line will print
            System.out.println("...getAllReturns: Database query successful."); 

            while (rs.next()) {
                // 3. If there's an error (e.g., misspelled column), it will happen HERE
                Return r = new Return();
                r.setStudentId(rs.getInt("student_id"));
                r.setBookId(rs.getInt("book_id"));
                r.setReturnDate(rs.getDate("return_date").toLocalDate());
                r.setFine(rs.getDouble("fine"));
                list.add(r);
                
                // 4. This will print for every record found
                System.out.println("...getAllReturns: Found record for StudentID=" + r.getStudentId()); 
            }
        } catch (SQLException e) {
            // 5. ***IF THERE IS AN ERROR, IT WILL PRINT THIS***
            System.out.println("!!! --- DATABASE ERROR in getAllReturns --- !!!"); 
            System.out.println("Message: " + e.getMessage());
            e.printStackTrace();
        }
        
        // 6. This line will print last
        System.out.println("--- ReturnDAO getAllReturns() FINISHED. Found " + list.size() + " records. ---"); 
        return list;
    }
}