package dao;

import model.Issue;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class IssueDAO {

    public void addIssue(Issue i) {
        String sql = "INSERT INTO issues (student_id, book_id, issue_date, due_date) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, i.getStudentId());
            ps.setInt(2, i.getBookId());
            ps.setDate(3, Date.valueOf(i.getIssueDate()));
            ps.setDate(4, Date.valueOf(i.getDueDate()));
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Issue> getAllIssues() {
        List<Issue> list = new ArrayList<>();
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
                list.add(i);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

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
}
