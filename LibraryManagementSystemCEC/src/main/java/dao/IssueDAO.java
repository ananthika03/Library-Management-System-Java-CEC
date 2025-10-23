package dao;

import model.Issue;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class IssueDAO {

    // 1. UPDATED: Now sets the 'status' column on creation
    public void addIssue(Issue i) {
        String sql = "INSERT INTO issues (student_id, book_id, issue_date, due_date, status) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, i.getStudentId());
            ps.setInt(2, i.getBookId());
            ps.setDate(3, Date.valueOf(i.getIssueDate()));
            ps.setDate(4, Date.valueOf(i.getDueDate()));
            ps.setString(5, "Issued"); // Set status
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 2. UPDATED: Now fetches the 'status'
    public List<Issue> getAllIssues() {
        List<Issue> list = new ArrayList<>();
        // Note: Change SQL to "SELECT * FROM issues WHERE status = 'Issued'" if you only want to see active issues
        String sql = "SELECT * FROM issues"; 
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Issue i = new Issue();
                i.setIssueId(rs.getInt("issue_id"));
                i.setStudentId(rs.getInt("student_id"));
                i.setBookId(rs.getInt("book_id"));
                i.setIssueDate(rs.getDate("issue_date").toLocalDate());
                i.setDueDate(rs.getDate("due_date").toLocalDate());
                i.setStatus(rs.getString("status")); // Get status
                list.add(i);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // 3. deleteIssue is unchanged
    public void deleteIssue(int issueId) {
        String sql = "DELETE FROM issues WHERE issue_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, issueId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 4. NEW: This method finds the active (status = 'Issued') issue
    public Issue getActiveIssue(int studentId, int bookId) {
        String sql = "SELECT * FROM issues WHERE student_id = ? AND book_id = ? AND status = 'Issued' ORDER BY issue_id DESC LIMIT 1";
        Issue i = null;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, studentId);
            ps.setInt(2, bookId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    i = new Issue();
                    i.setIssueId(rs.getInt("issue_id"));
                    i.setStudentId(rs.getInt("student_id"));
                    i.setBookId(rs.getInt("book_id"));
                    i.setIssueDate(rs.getDate("issue_date").toLocalDate());
                    i.setDueDate(rs.getDate("due_date").toLocalDate());
                    i.setStatus(rs.getString("status"));
                }
            }
        } catch (SQLException e) {
            System.out.println("!!! --- DATABASE ERROR in getActiveIssue --- !!!");
            e.printStackTrace();
        }
        return i; // Will be null if no 'Issued' record is found
    }
    
    // 5. NEW: This method updates the status of an issue
    public void updateIssueStatus(int issueId, String status) {
        String sql = "UPDATE issues SET status = ? WHERE issue_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, status);
            ps.setInt(2, issueId);
            ps.executeUpdate();
            
        } catch (SQLException e) {
            System.out.println("!!! --- DATABASE ERROR in updateIssueStatus --- !!!");
            e.printStackTrace();
        }
    }
}