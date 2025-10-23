package dao;

import model.Issue;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class IssueDAO {

    /**
     * ✅ **NEW TRANSACTIONAL METHOD**
     * Handles the entire process of issuing a book as a single atomic operation.
     * If any step fails (e.g., no copies available), the whole transaction is rolled back.
     *
     * @param issue The Issue object containing student/book IDs and dates.
     * @throws SQLException if a database error occurs or if no copies are available.
     */
    public void issueBookTransaction(Issue issue) throws SQLException {
        Connection conn = null;
        String sqlIssue = "INSERT INTO issues (student_id, book_id, issue_date, due_date, status) VALUES (?, ?, ?, ?, 'Issued')";
        String sqlBookUpdate = "UPDATE books SET available_copies = available_copies - 1 WHERE book_id = ? AND available_copies > 0";
        String sqlStudentUpdate = "UPDATE students SET issued_books = issued_books + 1 WHERE student_id = ?";

        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false); // Start transaction

            // Step 1: Update book copies. This is done first to ensure availability.
            try (PreparedStatement ps = conn.prepareStatement(sqlBookUpdate)) {
                ps.setInt(1, issue.getBookId());
                int rowsAffected = ps.executeUpdate();
                if (rowsAffected == 0) {
                    throw new SQLException("No available copies for book ID: " + issue.getBookId());
                }
            }

            // Step 2: Insert the new issue record
            try (PreparedStatement ps = conn.prepareStatement(sqlIssue)) {
                ps.setInt(1, issue.getStudentId());
                ps.setInt(2, issue.getBookId());
                ps.setDate(3, Date.valueOf(issue.getIssueDate()));
                ps.setDate(4, Date.valueOf(issue.getDueDate()));
                ps.executeUpdate();
            }

            // Step 3: Update the student's issued book count
            try (PreparedStatement ps = conn.prepareStatement(sqlStudentUpdate)) {
                ps.setInt(1, issue.getStudentId());
                ps.executeUpdate();
            }

            conn.commit(); // If all steps succeed, commit the transaction

        } catch (SQLException e) {
            if (conn != null) {
                conn.rollback(); // If any error occurs, roll back all changes
            }
            throw e; // Re-throw the exception to let the servlet know something went wrong
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

    // Fetches all currently issued books
    public List<Issue> getAllIssues() {
        List<Issue> list = new ArrayList<>();
        String sql = "SELECT * FROM issues WHERE status = 'Issued' ORDER BY issue_date DESC";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Issue i = new Issue();
                i.setIssueId(rs.getInt("issue_id"));
                i.setStudentId(rs.getInt("student_id"));
                i.setBookId(rs.getInt("book_id"));
                Date issueDate = rs.getDate("issue_date");
                if(issueDate != null) i.setIssueDate(issueDate.toLocalDate());
                Date dueDate = rs.getDate("due_date");
                if(dueDate != null) i.setDueDate(dueDate.toLocalDate());
                i.setStatus(rs.getString("status"));
                list.add(i);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // The find active issue method you already corrected
    public Issue getActiveIssue(int studentId, int bookId) {
        String sql = "SELECT * FROM issues WHERE student_id = ? AND book_id = ? AND status = 'Issued' ORDER BY issue_id DESC LIMIT 1";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            ps.setInt(2, bookId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Issue i = new Issue();
                i.setIssueId(rs.getInt("issue_id"));
                i.setStudentId(rs.getInt("student_id"));
                i.setBookId(rs.getInt("book_id"));
                i.setIssueDate(rs.getDate("issue_date").toLocalDate());
                i.setDueDate(rs.getDate("due_date").toLocalDate());
                i.setStatus(rs.getString("status"));
                return i;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    // The old, non-transactional addIssue is no longer needed.
}