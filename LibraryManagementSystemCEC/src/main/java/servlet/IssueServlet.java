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

@WebServlet("/issues")
public class IssueServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private IssueDAO issueDAO = new IssueDAO();
    private BookDAO bookDAO = new BookDAO();
    private StudentDAO studentDAO = new StudentDAO();

    /**
     * This method correctly fetches the list of issued books with status = 'Issued'
     * and forwards them to the JSP page.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Issue> issues = issueDAO.getAllIssues();
        request.setAttribute("issueList", issues); // Sets the attribute the JSP page expects

        // Correctly dispatches to your JSP file's location
        RequestDispatcher dispatcher = request.getRequestDispatcher("/pages/issue.jsp");
        dispatcher.forward(request, response);
    }

    /**
     * This method handles both adding and deleting issues.
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
        try {
            int studentId = Integer.parseInt(request.getParameter("studentId"));
            int bookId = Integer.parseInt(request.getParameter("bookId"));

            Student student = studentDAO.getStudentById(studentId);
            Book book = bookDAO.getBookById(bookId);

            if (student == null || book == null) {
                System.err.println("Attempted to issue with non-existent Student or Book ID.");
                response.sendRedirect("issues?error=notfound");
                return;
            }

            if (book.getAvailableCopies() <= 0) {
                System.err.println("Attempted to issue a book with no available copies.");
                response.sendRedirect("issues?error=nocopies");
                return;
            }

            Issue newIssue = new Issue();
            newIssue.setStudentId(studentId);
            newIssue.setBookId(bookId);
            newIssue.setIssueDate(LocalDate.parse(request.getParameter("issueDate")));
            newIssue.setDueDate(LocalDate.parse(request.getParameter("dueDate")));
            // The 'addIssue' method in your DAO will automatically set the status to "Issued"
            issueDAO.addIssue(newIssue);

            // Update book and student records
            book.setAvailableCopies(book.getAvailableCopies() - 1);
            bookDAO.updateBook(book);

            student.setIssuedBooks(student.getIssuedBooks() + 1);
            studentDAO.updateStudent(student);

        } catch (Exception e) {
            e.printStackTrace();
        }
        response.sendRedirect("issues");
    }

    private void handleDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            int issueId = Integer.parseInt(request.getParameter("issueId"));
            issueDAO.deleteIssue(issueId);
        } catch (NumberFormatException e) {
            System.err.println("Invalid Issue ID for deletion: " + request.getParameter("issueId"));
        }
        response.sendRedirect("issues");
    }
}