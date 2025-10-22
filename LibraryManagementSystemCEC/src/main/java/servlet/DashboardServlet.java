package servlet;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Forwards the request to the dashboard JSP after the login filter passes
        RequestDispatcher dispatcher = request.getRequestDispatcher("/pages/dashboard.jsp");
        dispatcher.forward(request, response);
    }
}