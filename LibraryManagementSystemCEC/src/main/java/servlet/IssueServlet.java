// File: src/main/java/servlet/IssueServlet.java
package servlet;

import dao.IssueDAO;
import model.Issue;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/issues") // Using a RESTful plural name for clarity
public class IssueServlet extends HttpServlet {
    
    private static final long serialVersionUID = 1L;

    // doGet: Fetches and displays all current issue records
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        IssueDAO dao = new IssueDAO();
        List<Issue> issues = dao.getAllIssues();
        
        request.setAttribute("issueList", issues);
        
        // Forward to the JSP page
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WebContent/pages/issue.jsp");
        dispatcher.forward(request, response);
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // ... (Existing parameter fetching code)
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

        // Redirect to the servlet's doGet method to display the updated list
        response.sendRedirect("issues");
    }
}