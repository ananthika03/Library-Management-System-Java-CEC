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
import java.sql.SQLException;
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
    private ReportDAO reportDAO = new ReportDAO(); // Keep for report generation

    private static final BigDecimal FINE_PER_DAY = new BigDecimal("10.00");

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
        String redirectUrl = request.getContextPath() + "/returns";

        try {
            int studentId = Integer.parseInt(request.getParameter("studentId"));
            int bookId = Integer.parseInt(request.getParameter("bookId"));
            LocalDate returnDate = LocalDate.parse(request.getParameter("returnDate"));

            Issue issueToReturn = issueDAO.getActiveIssue(studentId, bookId);

            if (issueToReturn == null) {
                LOGGER.warning("Return failed: No active issue found for Student ID " + studentId + " and Book ID " + bookId);
                response.sendRedirect(redirectUrl + "?error=noissue");
                return;
            }

            BigDecimal fineAmount = calculateFine(issueToReturn.getDueDate(), returnDate);

            // 1. Create the Return object
            Return newReturn = new Return();
            newReturn.setStudentId(studentId);
            newReturn.setBookId(bookId);
            newReturn.setIssueId(issueToReturn.getIssueId());
            newReturn.setReturnDate(returnDate);
            newReturn.setFine(fineAmount.doubleValue());

            // 2. Execute the entire return process as a single transaction
            // Note: ReportDAO is not part of this simple transaction for clarity, but could be added.
            returnDAO.processBookReturnTransaction(newReturn);

            // 3. Generate the report (this is separate from the core transaction)
            Report report = new Report();
            report.setStudentId(studentId);
            report.setBookId(bookId);
            report.setIssueDate(issueToReturn.getIssueDate());
            report.setDueDate(issueToReturn.getDueDate());
            report.setFine(fineAmount.doubleValue());
            report.setStatus(fineAmount.compareTo(BigDecimal.ZERO) > 0 ? "Overdue" : "Returned");
            reportDAO.addReport(report); // Assumes this operation is safe to do outside transaction
            LOGGER.info("SUCCESS: Transaction complete for book return.");

            redirectUrl += "?success=true";

        } catch (SQLException e) {
            LOGGER.severe("DATABASE ERROR during book return: " + e.getMessage());
            redirectUrl += "?error=dberror";
        } catch (Exception e) {
            LOGGER.severe("An error occurred while processing a return: " + e.getMessage());
            redirectUrl += "?error=generic";
        }

        response.sendRedirect(redirectUrl);
    }

    private BigDecimal calculateFine(LocalDate dueDate, LocalDate returnDate) {
        long daysOverdue = ChronoUnit.DAYS.between(dueDate, returnDate);
        if (daysOverdue > 0) {
            return FINE_PER_DAY.multiply(new BigDecimal(daysOverdue));
        }
        return BigDecimal.ZERO;
    }
}