package dao;

import model.Return;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReturnDAO {

    // --- CRITICAL FIX: Changed table name and added issue_id ---
    public void addReturn(Return r) {
        String sql = "INSERT INTO returns (student_id, book_id, issue_id, return_date, fine) VALUES (?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false); 
            ps = conn.prepareStatement(sql); 
            ps.setInt(1, r.getStudentId());
            ps.setInt(2, r.getBookId());
            ps.setInt(3, r.getIssueId()); // <-- CRITICAL: Insert issue_id
            ps.setDate(4, Date.valueOf(r.getReturnDate()));
            ps.setDouble(5, r.getFine());
            ps.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            System.out.println("!!! --- DATABASE ERROR in addReturn --- !!!");
            e.printStackTrace(); 
            try { if (conn != null) { conn.rollback(); } } catch (SQLException ex) { ex.printStackTrace(); }
        } finally {
            try { if (ps != null) ps.close(); if (conn != null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    // --- CRITICAL FIX: Changed table name and fetched issue_id ---
    public List<Return> getAllReturns() {
        List<Return> list = new ArrayList<>();
        String sql = "SELECT * FROM returns"; // Table name corrected to 'returns'
        
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            
            while (rs.next()) {
                Return r = new Return();
                r.setReturnId(rs.getInt("return_id"));
                r.setStudentId(rs.getInt("student_id"));
                r.setBookId(rs.getInt("book_id"));
                r.setIssueId(rs.getInt("issue_id")); // <-- CRITICAL: Get issue_id
                r.setReturnDate(rs.getDate("return_date").toLocalDate());
                r.setFine(rs.getDouble("fine"));
                list.add(r);
            }
        } catch (SQLException e) {
            System.out.println("!!! --- DATABASE ERROR in getAllReturns --- !!!"); 
            System.out.println("Message: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }
}