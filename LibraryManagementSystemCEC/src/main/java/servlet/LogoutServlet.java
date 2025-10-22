package servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/logout") // Maps this servlet to the /logout URL
public class LogoutServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false); // Get session if exists, don't create new

        if (session != null) {
            session.removeAttribute("adminUserId"); // Remove the attribute that identifies the logged-in user
            session.invalidate(); // Invalidate the session completely
        }

        // Redirect to the login page after logout
        // Make sure the path is correct relative to the context root
        response.sendRedirect(request.getContextPath() + "/pages/login.html?logout=success");
    }

    // Optional: Handle POST requests the same way if needed, though GET is typical for logout links
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}