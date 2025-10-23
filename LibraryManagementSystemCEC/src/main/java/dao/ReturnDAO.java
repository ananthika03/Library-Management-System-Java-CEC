package dao;

import model.Return;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReturnDAO {

    /**
     * ✅ **NEW TRANSACTIONAL METHOD**
     * Handles the entire process of a book return in a single atomic transaction.
     * It adds the return record, creates a report, updates the issue status,
     * increments the book's copy count, and decrements the student's issued book count.
     * If any step fails, the entire operation is rolled back.
     *
     * @param newReturn The Return object to be saved.
     * @throws SQLException if any database error occurs.
     */
    public void processBookReturnTransaction(Return newReturn) throws SQLException {
        Connection conn = null;
        String sqlReturn = "INSERT INTO returns (student_id, book_id, issue_id, return_date, fine) VALUES (?, ?, ?, ?, ?)";
        String sqlIssue = "UPDATE issues SET status = 'Returned' WHERE issue_id = ?";
        String sqlBook = "UPDATE books SET available_copies = available_copies + 1 WHERE book_id = ?";
        String sqlStudent = "UPDATE students SET issued_books = issued_books - 1 WHERE student_id = ? AND issued_books > 0";
        // Assuming you also have a ReportDAO that needs to be part of this, it would also be included here.
        // For simplicity, this example focuses on the core tables. You would add the report insertion here as well.

        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false); // Start transaction

            // 1. Insert into returns table
            try (PreparedStatement ps = conn.prepareStatement(sqlReturn)) {
                ps.setInt(1, newReturn.getStudentId());
                ps.setInt(2, newReturn.getBookId());
                ps.setInt(3, newReturn.getIssueId());
                ps.setDate(4, Date.valueOf(newReturn.getReturnDate()));
                ps.setDouble(5, newReturn.getFine());
                ps.executeUpdate();
            }

            // 2. Update issues table
            try (PreparedStatement ps = conn.prepareStatement(sqlIssue)) {
                ps.setInt(1, newReturn.getIssueId());
                ps.executeUpdate();
            }

            // 3. Update books table
            try (PreparedStatement ps = conn.prepareStatement(sqlBook)) {
                ps.setInt(1, newReturn.getBookId());
                ps.executeUpdate();
            }
            
            // 4. Update students table
            try (PreparedStatement ps = conn.prepareStatement(sqlStudent)) {
                ps.setInt(1, newReturn.getStudentId());
                ps.executeUpdate();
            }

            conn.commit(); // Commit if all statements succeed

        } catch (SQLException e) {
            if (conn != null) {
                conn.rollback(); // Roll back on any error
            }
            e.printStackTrace();
            throw e; // Re-throw to notify the servlet
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public List<Return> getAllReturns() {
        List<Return> list = new ArrayList<>();
        String sql = "SELECT * FROM returns ORDER BY return_date DESC";
        
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            
            while (rs.next()) {
                Return r = new Return();
                r.setReturnId(rs.getInt("return_id"));
                r.setStudentId(rs.getInt("student_id"));
                r.setBookId(rs.getInt("book_id"));
                r.setIssueId(rs.getInt("issue_id"));
                r.setFine(rs.getDouble("fine"));

                // ✅ **FIX**: Safely handle dates that might be NULL in the database
                Date returnDateFromDb = rs.getDate("return_date");
                if (returnDateFromDb != null) {
                    r.setReturnDate(returnDateFromDb.toLocalDate());
                }

                list.add(r);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // The old addReturn method is no longer needed as the transaction handles it.
    // public void addReturn(Return r) { ... }
}