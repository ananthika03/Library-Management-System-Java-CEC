// File: src/main/java/servlet/ReportServlet.java
package servlet;

import dao.ReportDAO;
import model.Report;
import java.time.LocalDate;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/reports")
public class ReportServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    
    // ✅ ADDED: Handles GET request to fetch data and display the JSP
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        ReportDAO dao = new ReportDAO();
        List<Report> reports = dao.getAllReports();
        
        request.setAttribute("reportList", reports);
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("/pages/report.jsp");
        dispatcher.forward(request, response);
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int studentId = Integer.parseInt(request.getParameter("studentId"));
        int bookId = Integer.parseInt(request.getParameter("bookId"));
        LocalDate issueDate = LocalDate.parse(request.getParameter("issueDate"));
        LocalDate dueDate = LocalDate.parse(request.getParameter("dueDate"));
        double fine = Double.parseDouble(request.getParameter("fine"));
        String status = request.getParameter("status");

        Report r = new Report();
        r.setStudentId(studentId);
        r.setBookId(bookId);
        r.setIssueDate(issueDate);
        r.setDueDate(dueDate);
        r.setFine(fine);
        r.setStatus(status);

        ReportDAO dao = new ReportDAO();
        dao.addReport(r);

        response.sendRedirect("reports"); // Redirects to doGet
    }
}