// File: src/main/java/servlet/StudentServlet.java
package servlet;

import dao.StudentDAO;
import model.Student;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/students")
public class StudentServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    // ✅ ADDED: Handles GET request to fetch data and display the JSP
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        StudentDAO dao = new StudentDAO();
        List<Student> students = dao.getAllStudents(); 
        
        request.setAttribute("studentList", students); 
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("/pages/student.jsp");
        dispatcher.forward(request, response);
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        StudentDAO dao = new StudentDAO();
        String action = request.getParameter("action");
        
        // 1. Handle Delete Action (uses hidden input 'action'="delete")
        if ("delete".equals(action)) {
            try {
                int studentId = Integer.parseInt(request.getParameter("studentId"));
                dao.deleteStudent(studentId);
            } catch (NumberFormatException e) {
                // Log error
            }
            response.sendRedirect("students"); // Redirects to doGet
            return;
        }

        // 2. Handle Add Student Action (Original POST logic)
        String name = request.getParameter("name");
        String department = request.getParameter("department");
        String contact = request.getParameter("contact");
        
        int issuedBooks = 0;
        try {
            issuedBooks = Integer.parseInt(request.getParameter("issuedBooks"));
        } catch (NumberFormatException ignored) {}

        Student s = new Student();
        s.setName(name);
        s.setDepartment(department);
        s.setContact(contact);
        s.setIssuedBooks(issuedBooks);

        dao.addStudent(s);

        response.sendRedirect("students"); // Redirects to doGet
    }
}