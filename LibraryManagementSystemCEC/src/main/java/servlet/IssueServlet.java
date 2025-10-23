package servlet;

import dao.BookDAO;
import dao.IssueDAO;
import dao.StudentDAO;
import model.Book;
import model.Issue;
import model.Student;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Logger;

// The URL pattern for this servlet is "/issues"
@WebServlet("/issues")
public class IssueServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    // Logger to help with debugging in the server console
    private static final Logger LOGGER = Logger.getLogger(IssueServlet.class.getName());

    private IssueDAO issueDAO = new IssueDAO();
    private BookDAO bookDAO = new BookDAO();
    private StudentDAO studentDAO = new StudentDAO();

    /**
     * This method is called when you visit the page. It's responsible for
     * fetching the list of issued books from the database and displaying them.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        LOGGER.info("GET request received. Fetching all issued books...");
        
        // Fetch the list of books with "Issued" status
        List<Issue> issues = issueDAO.getAllIssues();
        
        // Set the list as an attribute that the JSP page can access
        request.setAttribute("issueList", issues);
        
        LOGGER.info("Forwarding to issue.jsp with " + issues.size() + " issued books.");

        // Forward the request to the JSP page to display the data
        RequestDispatcher dispatcher = request.getRequestDispatcher("/pages/issue.jsp");
        dispatcher.forward(request, response);
    }

    /**
     * This method is called ONLY when you submit the form on the page.
     * It handles saving the new issue to the database.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        // --- Handle Delete Action ---
        if ("delete".equals(action)) {
            try {
                int issueId = Integer.parseInt(request.getParameter("issueId"));
                LOGGER.info("POST request received to delete issue ID: " + issueId);
                issueDAO.deleteIssue(issueId);
            } catch (NumberFormatException e) {
                LOGGER.severe("Invalid Issue ID for deletion: " + request.getParameter("issueId"));
            }
            // After deleting, redirect to refresh the list
            response.sendRedirect(request.getContextPath() + "/issues");
            return;
        }

        // --- Handle Add Issue Action ---
        LOGGER.info("POST request received to add a new issue.");
        try {
            int studentId = Integer.parseInt(request.getParameter("studentId"));
            int bookId = Integer.parseInt(request.getParameter("bookId"));

            Student student = studentDAO.getStudentById(studentId);
            Book book = bookDAO.getBookById(bookId);

            // Validation checks
            if (student == null || book == null) {
                LOGGER.warning("Issue failed: Student or Book not found.");
                response.sendRedirect(request.getContextPath() + "/issues?error=notfound");
                return;
            }
            if (book.getAvailableCopies() <= 0) {
                LOGGER.warning("Issue failed: No available copies for book ID " + bookId);
                response.sendRedirect(request.getContextPath() + "/issues?error=nocopies");
                return;
            }

            // Create and save the new issue
            Issue newIssue = new Issue();
            newIssue.setStudentId(studentId);
            newIssue.setBookId(bookId);
            newIssue.setIssueDate(LocalDate.parse(request.getParameter("issueDate")));
            newIssue.setDueDate(LocalDate.parse(request.getParameter("dueDate")));
            issueDAO.addIssue(newIssue);
            LOGGER.info("Successfully saved new issue for student ID " + studentId);

            // Update related data
            book.setAvailableCopies(book.getAvailableCopies() - 1);
            bookDAO.updateBook(book);
            student.setIssuedBooks(student.getIssuedBooks() + 1);
            studentDAO.updateStudent(student);

        } catch (Exception e) {
            LOGGER.severe("An error occurred while adding a new issue: " + e.getMessage());
            e.printStackTrace();
        }

        // **THE MOST IMPORTANT STEP**
        // After the data is saved, we send a redirect command to the browser.
        // This tells the browser to make a brand new GET request to the "/issues" URL.
        // This new request will trigger the doGet method above, which fetches the
        // complete, updated list from the database.
        LOGGER.info("Redirecting to /issues to refresh the page.");
        response.sendRedirect(request.getContextPath() + "/issues");
    }
}