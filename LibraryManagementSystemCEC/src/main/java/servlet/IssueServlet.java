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
        RequestDispatcher dispatcher = request.getRequestDispatcher("/pages/issue.jsp");
        dispatcher.forward(request, response);
    }
    
    // doPost: Handles both ADDING a new issue and DELETING an existing one
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        IssueDAO dao = new IssueDAO();
        String action = request.getParameter("action"); // Check for action parameter

        // --- NEW: Handle Delete Action ---
        if ("delete".equals(action)) {
            try {
                int issueId = Integer.parseInt(request.getParameter("issueId"));
                dao.deleteIssue(issueId); // Call the DAO method
            } catch (NumberFormatException e) {
                // Log or handle error appropriately
                System.err.println("Invalid Issue ID for deletion: " + request.getParameter("issueId"));
            }
            response.sendRedirect("issues"); // Redirect back to the issue list
            return; // Important to stop further processing
        }
        
        // --- Existing code for ADDING an issue ---
        
        // Check if action is "delete", if so, the code above would have run and returned.
        // If not "delete" (e.g., it's null from the add form), proceed to add.
        
        int studentId = Integer.parseInt(request.getParameter("studentId"));
        int bookId = Integer.parseInt(request.getParameter("bookId"));
        LocalDate issueDate = LocalDate.parse(request.getParameter("issueDate"));
        LocalDate dueDate = LocalDate.parse(request.getParameter("dueDate"));

        Issue i = new Issue();
        i.setStudentId(studentId);
        i.setBookId(bookId);
        i.setIssueDate(issueDate);
        i.setDueDate(dueDate);

        // dao object is already created at the top of the method
        dao.addIssue(i);

        // Redirect to the servlet's doGet method to display the updated list
        response.sendRedirect("issues");
    }
}