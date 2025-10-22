// File: src/main/java/servlet/BookServlet.java
package servlet;

import dao.BookDAO;
import model.Book;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/books")
public class BookServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    // ✅ ADDED: Handles GET request to fetch data and display the JSP
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        BookDAO dao = new BookDAO();
        List<Book> books = dao.getAllBooks();
        request.setAttribute("bookList", books);
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WebContent/pages/book.jsp");
        dispatcher.forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        BookDAO dao = new BookDAO();
        String action = request.getParameter("action");

        // 1. Handle Delete Action
        if ("delete".equals(action)) {
            try {
                int bookId = Integer.parseInt(request.getParameter("bookId"));
                dao.deleteBook(bookId);
            } catch (NumberFormatException ignored) {}
            response.sendRedirect("books"); // Redirects to doGet
            return;
        }

        // 2. Handle Add Book Action
        String title = request.getParameter("title");
        String author = request.getParameter("author");
        String publisher = request.getParameter("publisher");
        int year = 0;
        int availableCopies = 0;

        try {
            year = Integer.parseInt(request.getParameter("year"));
            availableCopies = Integer.parseInt(request.getParameter("availableCopies"));
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Year and Available Copies must be numbers!");
            doGet(request, response);
            return;
        }

        Book b = new Book();
        b.setTitle(title);
        b.setAuthor(author);
        b.setPublisher(publisher);
        b.setYear(year);
        b.setAvailableCopies(availableCopies);

        dao.addBook(b);

        response.sendRedirect("books"); // Redirects to doGet
    }
}