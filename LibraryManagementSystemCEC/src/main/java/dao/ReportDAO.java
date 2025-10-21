package dao;

import model.Report;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReportDAO {

    public void addReport(Report r) {
        String sql = "INSERT INTO reports (student_id, book_id, issue_date, due_date, fine, status) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, r.getStudentId());
            ps.setInt(2, r.getBookId());
            ps.setDate(3, Date.valueOf(r.getIssueDate()));
            ps.setDate(4, Date.valueOf(r.getDueDate()));
            ps.setDouble(5, r.getFine());
            ps.setString(6, r.getStatus());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Report> getAllReports() {
        List<Report> list = new ArrayList<>();
        String sql = "SELECT * FROM reports";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Report r = new Report();
                r.setReportId(rs.getInt("report_id"));
                r.setStudentId(rs.getInt("student_id"));
                r.setBookId(rs.getInt("book_id"));
                r.setIssueDate(rs.getDate("issue_date").toLocalDate());
                r.setDueDate(rs.getDate("due_date").toLocalDate());
                r.setFine(rs.getDouble("fine"));
                r.setStatus(rs.getString("status"));
                list.add(r);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
