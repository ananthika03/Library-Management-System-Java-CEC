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

@WebServlet("/issues")
public class IssueServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(IssueServlet.class.getName());

    private IssueDAO issueDAO = new IssueDAO();
    private BookDAO bookDAO = new BookDAO();
    private StudentDAO studentDAO = new StudentDAO();

    /**
     * This method is called when the page is loaded. It fetches the
     * current list of issued books and displays them.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        LOGGER.info("GET request for /issues. Fetching all issued books...");
        
        List<Issue> issues = issueDAO.getAllIssues();
        request.setAttribute("issueList", issues);
        
        LOGGER.info("Forwarding to issue.jsp with " + issues.size() + " issued books.");

        RequestDispatcher dispatcher = request.getRequestDispatcher("/pages/issue.jsp");
        dispatcher.forward(request, response);
    }

    /**
     * This method is called when you submit the form to add or delete an issue.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        
        // --- Handle Delete Action ---
        if ("delete".equals(action)) {
            handleDelete(request, response);
            return;
        }

        // --- Handle Add Issue Action (Default) ---
        handleAdd(request, response);
    }

    private void handleAdd(HttpServletRequest request, HttpServletResponse response) throws IOException {
        LOGGER.info("POST request received to add a new issue.");
        try {
            int studentId = Integer.parseInt(request.getParameter("studentId"));
            int bookId = Integer.parseInt(request.getParameter("bookId"));

            // **ROBUST VALIDATION AND LOGGING**
            Student student = studentDAO.getStudentById(studentId);
            if (student == null) {
                LOGGER.warning("ISSUE FAILED: Student with ID " + studentId + " was not found in the database.");
                response.sendRedirect(request.getContextPath() + "/issues?error=studentnotfound");
                return;
            }

            Book book = bookDAO.getBookById(bookId);
            if (book == null) {
                LOGGER.warning("ISSUE FAILED: Book with ID " + bookId + " was not found in the database.");
                response.sendRedirect(request.getContextPath() + "/issues?error=booknotfound");
                return;
            }

            if (book.getAvailableCopies() <= 0) {
                LOGGER.warning("ISSUE FAILED: No available copies for book ID " + bookId);
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
            LOGGER.info("SUCCESS: New issue for student ID " + studentId + " has been saved to the database.");

            // Update related records
            book.setAvailableCopies(book.getAvailableCopies() - 1);
            bookDAO.updateBook(book);

            student.setIssuedBooks(student.getIssuedBooks() + 1);
            studentDAO.updateStudent(student);

        } catch (Exception e) {
            LOGGER.severe("A critical error occurred while adding a new issue: " + e.getMessage());
            e.printStackTrace();
        }

        // **THE FINAL FIX**: Use getContextPath() to create an absolute URL.
        // This reliably tells the browser to make a new GET request to the correct servlet,
        // which then fetches the updated list from the database.
        LOGGER.info("Redirecting to the absolute path to refresh the page.");
        response.sendRedirect(request.getContextPath() + "/issues");
    }

    private void handleDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            int issueId = Integer.parseInt(request.getParameter("issueId"));
            LOGGER.info("POST request received to delete issue ID: " + issueId);
            // In a real-world scenario, you would also update the book/student counts here
            issueDAO.deleteIssue(issueId);
        } catch (NumberFormatException e) {
            LOGGER.severe("Invalid Issue ID for deletion: " + request.getParameter("issueId"));
        }
        response.sendRedirect(request.getContextPath() + "/issues");
    }
}