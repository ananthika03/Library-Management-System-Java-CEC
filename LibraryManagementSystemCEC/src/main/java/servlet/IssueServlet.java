package servlet;

import dao.IssueDAO;
import model.Issue;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.time.LocalDate;

public class IssueServlet extends HttpServlet {


	private static final long serialVersionUID = -420251022888451057L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int studentId = Integer.parseInt(request.getParameter("studentId"));
        int bookId = Integer.parseInt(request.getParameter("bookId"));
        LocalDate issueDate = LocalDate.parse(request.getParameter("issueDate"));
        LocalDate dueDate = LocalDate.parse(request.getParameter("dueDate"));

        Issue i = new Issue();
        i.setStudentId(studentId);
        i.setBookId(bookId);
        i.setIssueDate(issueDate);
        i.setDueDate(dueDate);

        IssueDAO dao = new IssueDAO();
        dao.addIssue(i);

        response.sendRedirect("issue.html");
    }
}
