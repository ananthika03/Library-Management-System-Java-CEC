package servlet;

import dao.AdminDAO;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;

public class LoginServlet extends HttpServlet {

    private static final long serialVersionUID = 4532373161142028853L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String userid = request.getParameter("userid");
        String password = request.getParameter("password");

        AdminDAO dao = new AdminDAO();
        if (dao.validate(userid, password)) {
            HttpSession session = request.getSession();
            session.setAttribute("adminUserId", userid); // store logged-in user
            session.setMaxInactiveInterval(30 * 60); // session expires in 30 mins
            response.sendRedirect("dashboard.html");
        } else {
            response.sendRedirect("login.html?error=invalid");
        }
    }
}
