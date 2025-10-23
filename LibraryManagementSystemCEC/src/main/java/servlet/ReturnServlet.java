package servlet;

import dao.ReturnDAO;
import model.Return;
import java.time.LocalDate;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

// --- IMPORTS ADDED/UPDATED ---
import dao.IssueDAO;
import model.Issue; 
import java.time.temporal.ChronoUnit; 

@WebServlet("/returns")
public class ReturnServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    // doGet method remains correct for displaying the return history
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        ReturnDAO dao = new ReturnDAO();
        List<Return> returns = dao.getAllReturns();
        
        request.setAttribute("returnList", returns);
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("/pages/return.jsp");
        dispatcher.forward(request, response);
    }

    // --- doPost METHOD IS NOW FULLY CORRECTED ---
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Get data from the form
        int studentId = Integer.parseInt(request.getParameter("studentId"));
        int bookId = Integer.parseInt(request.getParameter("bookId"));
        LocalDate returnDate = LocalDate.parse(request.getParameter("returnDate"));
        
        // 2. Get the active issue record (one that is "Issued")
        IssueDAO issueDAO = new IssueDAO();
        Issue issueToReturn = issueDAO.getActiveIssue(studentId, bookId);

        // 3. Check if the book was actually "Issued"
        if (issueToReturn != null) {
            
            // 4. Calculate the fine (only if overdue)
            LocalDate dueDate = issueToReturn.getDueDate();
            double calculatedFine = 0.0;
            
            if (returnDate.isAfter(dueDate)) {
                // Calculate days difference (positive if returnDate > dueDate)
                long daysOverdue = ChronoUnit.DAYS.between(dueDate, returnDate);
                calculatedFine = daysOverdue * 10; // Assuming a fine rate of 10 per day
            }

            // 5. Create the Return object
            Return r = new Return();
            r.setStudentId(studentId);
            r.setBookId(bookId);
            r.setReturnDate(returnDate);
            r.setFine(calculatedFine); 
            r.setIssueId(issueToReturn.getIssueId()); // <-- CRITICAL: Set the FK

            // 6. Save the return record to the 'returns' table
            ReturnDAO returnDAO = new ReturnDAO();
            returnDAO.addReturn(r);
            
            // 7. CRITICAL STEP: Update the original issue's status to "Returned"
            issueDAO.updateIssueStatus(issueToReturn.getIssueId(), "Returned");

        } else {
            // Log if no active issue was found (already returned or never issued)
            System.out.println("WARN: Attempted to return a book that has no 'Issued' record. StudentID=" + studentId + ", BookID=" + bookId);
        }

        // 8. Redirect back to the page
        response.sendRedirect("returns"); 
    }
}