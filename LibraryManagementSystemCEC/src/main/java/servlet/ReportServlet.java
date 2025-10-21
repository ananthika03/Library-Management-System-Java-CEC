package servlet;

import dao.ReportDAO;

import model.Report;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.time.LocalDate;

public class ReportServlet extends HttpServlet {

    private static final long serialVersionUID = 5276892633421698997L;

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

        response.sendRedirect("report.html");
    }
}
