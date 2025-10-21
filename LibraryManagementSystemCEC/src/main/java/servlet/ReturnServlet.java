package servlet;

import dao.ReturnDAO;
import model.Return;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.time.LocalDate;

public class ReturnServlet extends HttpServlet {

    private static final long serialVersionUID = -2007048205797849879L;

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

        response.sendRedirect("return.html");
    }
}
