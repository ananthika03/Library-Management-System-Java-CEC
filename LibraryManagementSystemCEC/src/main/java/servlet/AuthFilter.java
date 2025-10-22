// File: src/main/java/servlet/AuthFilter.java
package servlet;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;

public class AuthFilter implements Filter {

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        HttpSession session = req.getSession(false);

        // Check if the user is logged in
        if (session != null && session.getAttribute("adminUserId") != null) {
            // User is logged in, allow access
            chain.doFilter(request, response);
        } else {
            // Not logged in, redirect to login page
            // ✅ CHANGE: Send the error to the correct static login page path
            res.sendRedirect("pages/login.html?error=unauthorized"); 
        }
    }
    
    // init() and destroy() methods are typically empty but required by the Filter interface.
}