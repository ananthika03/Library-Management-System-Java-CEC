package servlet;

import dao.BookDAO;
import dao.IssueDAO;
import dao.StudentDAO;
import model.Book;
import model.Issue;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Logger;

@WebServlet("/issues")
public class IssueServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(IssueServlet.class.getName());

    private IssueDAO issueDAO = new IssueDAO();
    private BookDAO bookDAO = new BookDAO();
    private StudentDAO studentDAO = new StudentDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        List<Issue> issues = issueDAO.getAllIssues();
        request.setAttribute("issueList", issues);
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("/pages/issue.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        
        LOGGER.info("POST request to add a new issue.");
        String redirectUrl = request.getContextPath() + "/issues";

        try {
            int studentId = Integer.parseInt(request.getParameter("studentId"));
            int bookId = Integer.parseInt(request.getParameter("bookId"));

            // --- Server-side validation ---
            if (studentDAO.getStudentById(studentId) == null) {
                response.sendRedirect(redirectUrl + "?error=studentnotfound");
                return;
            }
            Book book = bookDAO.getBookById(bookId);
            if (book == null) {
                response.sendRedirect(redirectUrl + "?error=booknotfound");
                return;
            }
            if (book.getAvailableCopies() <= 0) {
                response.sendRedirect(redirectUrl + "?error=nocopies");
                return;
            }

            // Create the issue object
            Issue newIssue = new Issue();
            newIssue.setStudentId(studentId);
            newIssue.setBookId(bookId);
            newIssue.setIssueDate(LocalDate.parse(request.getParameter("issueDate")));
            newIssue.setDueDate(LocalDate.parse(request.getParameter("dueDate")));
            
            // ✅ **FIX**: Execute the operation as a single, safe transaction
            issueDAO.issueBookTransaction(newIssue);
            
            LOGGER.info("SUCCESS: Transaction complete for issuing book " + bookId + " to student " + studentId);
            redirectUrl += "?success=true";

        } catch (SQLException e) {
            LOGGER.severe("DATABASE ERROR during book issue: " + e.getMessage());
            // Check if the error message is about no copies
            if (e.getMessage().contains("No available copies")) {
                redirectUrl += "?error=nocopies";
            } else {
                redirectUrl += "?error=dberror";
            }
        
        } catch (Exception e) {
            LOGGER.severe("A critical error occurred while adding a new issue: " + e.getMessage());
            redirectUrl += "?error=generic";
        }

        response.sendRedirect(redirectUrl);
    }
}