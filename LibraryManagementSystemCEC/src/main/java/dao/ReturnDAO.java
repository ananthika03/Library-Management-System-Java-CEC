package dao;

import model.Return;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReturnDAO {

    public void addReturn(Return r) {
        String sql = "INSERT INTO returns (student_id, book_id, return_date, fine) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, r.getStudentId());
            ps.setInt(2, r.getBookId());
            ps.setDate(3, Date.valueOf(r.getReturnDate()));
            ps.setDouble(4, r.getFine());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Return> getAllReturns() {
        List<Return> list = new ArrayList<>();
        String sql = "SELECT * FROM returns";
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
