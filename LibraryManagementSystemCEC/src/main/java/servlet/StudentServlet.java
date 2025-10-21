package servlet;

import dao.StudentDAO;
import model.Student;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;

public class StudentServlet extends HttpServlet {

    private static final long serialVersionUID = 1610862681682523822L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String name = request.getParameter("name");
        String department = request.getParameter("department");
        String contact = request.getParameter("contact");
        int issuedBooks = Integer.parseInt(request.getParameter("issuedBooks"));

        Student s = new Student();
        s.setName(name);
        s.setDepartment(department);
        s.setContact(contact);
        s.setIssuedBooks(issuedBooks);

        StudentDAO dao = new StudentDAO();
        dao.addStudent(s);

        response.sendRedirect("student.html");
    }
}
