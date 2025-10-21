package servlet;

import dao.BookDAO;
import model.Book;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/books")
public class BookServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String title = request.getParameter("title");
        String author = request.getParameter("author");
        String publisher = request.getParameter("publisher");
        int year = 0;
        int availableCopies = 0;

        try {
            year = Integer.parseInt(request.getParameter("year"));
            availableCopies = Integer.parseInt(request.getParameter("availableCopies"));
        } catch (NumberFormatException e) {
            response.getWriter().println("Year and Available Copies must be numbers!");
            return;
        }

        Book b = new Book();
        b.setTitle(title);
        b.setAuthor(author);
        b.setPublisher(publisher);
        b.setYear(year);
        b.setAvailableCopies(availableCopies);

        BookDAO dao = new BookDAO();
        boolean success = dao.addBook(b);

        response.setContentType("text/html; charset=UTF-8");
        if (success) {
            response.getWriter().println("<h3>Book added successfully!</h3>");
            response.getWriter().println("<a href='book.html'>Add Another Book</a>");
        } else {
            response.getWriter().println("<h3>Error: Could not add book. Check server logs.</h3>");
            response.getWriter().println("<a href='book.html'>Try Again</a>");
        }
    }
}
