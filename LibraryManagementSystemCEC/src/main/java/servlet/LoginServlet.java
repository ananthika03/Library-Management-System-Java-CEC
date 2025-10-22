package servlet;

import dao.AdminDAO;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private static final long serialVersionUID = 4532373161142028853L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        String userid = request.getParameter("userid");
        String password = request.getParameter("password");

        AdminDAO dao = new AdminDAO();
        if (dao.validate(userid, password)) {
            HttpSession session = request.getSession();
            session.setAttribute("adminUserId", userid);
            session.setMaxInactiveInterval(30 * 60);
            
            // Redirect to the Dashboard Servlet
            response.sendRedirect("pages/dashboard.jsp"); 
        } else {
            // Redirect back to login page with error parameter
            response.sendRedirect("pages/login.html?error=invalid");
        }
    }
}