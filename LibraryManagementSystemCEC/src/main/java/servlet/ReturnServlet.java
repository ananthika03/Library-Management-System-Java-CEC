// File: src/main/java/servlet/ReturnServlet.java
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

@WebServlet("/returns")
public class ReturnServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    // ✅ ADDED: Handles GET request to fetch data and display the JSP
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        ReturnDAO dao = new ReturnDAO();
        List<Return> returns = dao.getAllReturns();
        
        request.setAttribute("returnList", returns);
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WebContent/pages/return.jsp");
        dispatcher.forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int studentId = Integer.parseInt(request.getParameter("studentId"));
        int bookId = Integer.parseInt(request.getParameter("bookId"));
        LocalDate returnDate = LocalDate.parse(request.getParameter("returnDate"));
        double fine = Double.parseDouble(request.getParameter("fine"));

        Return r = new Return();
        r.setStudentId(studentId);
        r.setBookId(bookId);
        r.setReturnDate(returnDate);
        r.setFine(fine);

        ReturnDAO dao = new ReturnDAO();
        dao.addReturn(r);

        response.sendRedirect("returns"); // Redirects to doGet
    }
}