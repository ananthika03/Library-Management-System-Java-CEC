package servlet;

import dao.BookDAO;
import dao.IssueDAO;
import dao.ReportDAO;
import dao.ReturnDAO;
import dao.StudentDAO;
import model.Book;
import model.Issue;
import model.Report;
import model.Return;
import model.Student;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.logging.Logger;

@WebServlet("/returns")
public class ReturnServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(ReturnServlet.class.getName());

    private ReturnDAO returnDAO = new ReturnDAO();
    private IssueDAO issueDAO = new IssueDAO();
    private BookDAO bookDAO = new BookDAO();
    private StudentDAO studentDAO = new StudentDAO();
    private ReportDAO reportDAO = new ReportDAO();

    // You can easily change the fine amount here. E.g., "2.50" for 2.50 per day.
    private static final BigDecimal FINE_PER_DAY = new BigDecimal("1.00");

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        LOGGER.info("GET request for /returns. Fetching all return records.");
        List<Return> returns = returnDAO.getAllReturns();
        request.setAttribute("returnList", returns);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/pages/return.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        LOGGER.info("POST request to process a book return.");
        try {
            int studentId = Integer.parseInt(request.getParameter("studentId"));
            int bookId = Integer.parseInt(request.getParameter("bookId"));

            Issue issueToReturn = issueDAO.getActiveIssue(studentId, bookId);

            if (issueToReturn == null) {
                LOGGER.warning("Return failed: No active issue found for Student ID " + studentId + " and Book ID " + bookId);
                response.sendRedirect(request.getContextPath() + "/returns?error=noissue");
                return;
            }

            // --- 1. AUTOMATIC FINE CALCULATION WITH LOGGING ---
            BigDecimal fineAmount = calculateFine(issueToReturn.getDueDate());

            // --- 2. CREATE THE RETURN RECORD ---
            Return newReturn = new Return();
            newReturn.setStudentId(studentId);
            newReturn.setBookId(bookId);
            newReturn.setIssueId(issueToReturn.getIssueId());
            newReturn.setReturnDate(LocalDate.now());
            newReturn.setFine(fineAmount.doubleValue());
            returnDAO.addReturn(newReturn);

            // --- 3. AUTOMATICALLY CREATE A REPORT RECORD ---
            Report report = new Report();
            report.setStudentId(studentId);
            report.setBookId(bookId);
            report.setIssueDate(issueToReturn.getIssueDate());
            report.setDueDate(issueToReturn.getDueDate());
            report.setFine(fineAmount.doubleValue());
            report.setStatus(fineAmount.compareTo(BigDecimal.ZERO) > 0 ? "Overdue" : "Returned");
            reportDAO.addReport(report);
            LOGGER.info("Automatically generated a report for this return.");

            // --- 4. UPDATE SYSTEM STATE ---
            issueDAO.updateIssueStatus(issueToReturn.getIssueId(), "Returned");

            Book book = bookDAO.getBookById(bookId);
            if (book != null) {
                book.setAvailableCopies(book.getAvailableCopies() + 1);
                bookDAO.updateBook(book);
            }

            Student student = studentDAO.getStudentById(studentId);
            if (student != null) {
                student.setIssuedBooks(student.getIssuedBooks() - 1);
                studentDAO.updateStudent(student);
            }

        } catch (Exception e) {
            LOGGER.severe("An error occurred while processing a return: " + e.getMessage());
            e.printStackTrace();
        }

        response.sendRedirect(request.getContextPath() + "/returns");
    }

    /**
     * Calculates the fine and logs the process for debugging.
     */
    private BigDecimal calculateFine(LocalDate dueDate) {
        LocalDate currentDate = LocalDate.now();
        
        // **ENHANCED LOGGING TO HELP YOU DEBUG**
        LOGGER.info("--- Starting Fine Calculation ---");
        LOGGER.info("Due Date from Database: " + dueDate);
        LOGGER.info("Current System Date: " + currentDate);
        
        // This calculates the number of full days between the two dates.
        // It will be POSITIVE only if the current date is AFTER the due date.
        long daysOverdue = ChronoUnit.DAYS.between(dueDate, currentDate);
        
        LOGGER.info("Calculated Days Overdue: " + daysOverdue);

        if (daysOverdue > 0) {
            BigDecimal calculatedFine = FINE_PER_DAY.multiply(new BigDecimal(daysOverdue));
            LOGGER.info("Result: Fine IS applicable. Amount: " + calculatedFine);
            return calculatedFine;
        } else {
            LOGGER.info("Result: Book is NOT overdue. Fine is 0.");
            return BigDecimal.ZERO;
        }
    }
}