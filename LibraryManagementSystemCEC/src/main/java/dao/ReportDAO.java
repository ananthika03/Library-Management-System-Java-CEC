package dao;

import model.Report;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ReportDAO {

    private static final Logger LOGGER = Logger.getLogger(ReportDAO.class.getName());

    /**
     * This method is now used by the ReturnServlet to automatically save a new report
     * every time a book is returned.
     * @param report The Report object containing all the necessary data.
     */
    public void addReport(Report report) {
        // The SQL query includes all the fields that are now set automatically
        String sql = "INSERT INTO reports (student_id, book_id, issue_date, due_date, fine, status) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, report.getStudentId());
            ps.setInt(2, report.getBookId());
            ps.setDate(3, Date.valueOf(report.getIssueDate()));
            ps.setDate(4, Date.valueOf(report.getDueDate()));
            ps.setDouble(5, report.getFine()); // Correctly uses setDouble for the fine
            ps.setString(6, report.getStatus());
            
            ps.executeUpdate();
            LOGGER.info("Successfully added a new report to the database.");

        } catch (SQLException e) {
            LOGGER.severe("Error adding a report: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * This method retrieves all reports from the database to be displayed on the report page.
     * The records are ordered by the 'created_at' timestamp to show the most recent reports first.
     * @return A list of all Report objects.
     */
    public List<Report> getAllReports() {
        List<Report> list = new ArrayList<>();
        String sql = "SELECT * FROM reports ORDER BY created_at DESC";
        
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
                r.setFine(rs.getDouble("fine")); // Correctly uses getDouble for the fine
                r.setStatus(rs.getString("status"));
                list.add(r);
            }
        } catch (SQLException e) {
            LOGGER.severe("Error fetching reports: " + e.getMessage());
            e.printStackTrace();
        }
        
        LOGGER.info("Fetched " + list.size() + " reports from the database.");
        return list;
    }
}