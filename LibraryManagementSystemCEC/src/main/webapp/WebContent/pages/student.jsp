<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Student Management</title>
  <link rel="stylesheet" href="../css/styles.css">
  <style>
    body {
      margin: 0;
      font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
      background: #f4f7fb;
      display: flex;
    }

    /* Sidebar */
    .sidebar {
      width: 210px;
      background: #30465d;
      color: white;
      min-height: 100vh;
      padding-top: 20px;
      position: fixed;
    }
    .sidebar ul { list-style: none; padding: 0; }
    .sidebar li { margin: 15px 0; }
    .sidebar a { text-decoration: none; color: white; display: block; padding: 10px 20px; transition: background 0.3s; }
    .sidebar a:hover { background: #34495e; border-radius: 6px; }

    /* Main */
    .dashboard-main { margin-left: 250px; padding: 30px 20px; }
    .dashboard-main h2 { color: #2c3e50; margin-bottom: 20px; }

    /* Card Form */
    .card { background: #eaf3fc; padding: 20px; border-radius: 10px; box-shadow: 0 2px 6px rgba(0,0,0,0.1); margin-bottom: 30px; }
    form { display: flex; flex-wrap: wrap; gap: 10px; }
    form input { flex: 1 1 calc(30% - 10px); padding: 8px; border: 1px solid #ddd; border-radius: 6px; font-size: 14px; }
    form button { flex: 1 1 100%; padding: 10px; background: #3498db; color: white; border: none; border-radius: 6px; cursor: pointer; font-size: 14px; transition: background 0.3s; }
    form button:hover { background: #2980b9; }

    /* Table */
    table { width: 100%; border-collapse: collapse; margin-top: 20px; background: white; border-radius: 10px; overflow: hidden; box-shadow: 0 2px 6px rgba(0,0,0,0.1); }
    th, td { padding: 12px; text-align: center; border-bottom: 1px solid #ddd; }
    th { background: #3498db; color: white; }
    tr:hover { background: #f1f1f1; }
    .deleteBtn { background: #e74c3c; color: white; padding: 6px 10px; border: none; border-radius: 5px; cursor: pointer; transition: background 0.3s; }
    .deleteBtn:hover { background: #c0392b; }
  </style>
</head>
<body>

  <aside class="sidebar">
    <ul>
      <li><a href="books">Books</a></li>
      <li><a href="students">Students</a></li>
      <li><a href="issues">Issue Book</a></li>
      <li><a href="returns">Return Book</a></li>
      <li><a href="reports">Reports</a></li>
      <li><a href="logout">Logout</a></li>
      <li><a href="../index.html">Main Page</a></li>
    </ul>
  </aside>

  <main class="dashboard-main">
    <h2>Student Management</h2>

    <div class="card">
      <form action="students" method="post">
         <input type="text" name="name" placeholder="Name" required>
         <input type="text" name="department" placeholder="Department" required>
         <input type="text" name="contact" placeholder="Contact" required>
         <input type="number" name="issuedBooks" placeholder="Issued Books" min="0" required>
         <button type="submit">Add Student</button>
     </form>
    </div>

    <table id="studentTable" class="styled-table">
      <thead>
        <tr>
          <th>Student ID</th>
          <th>Name</th>
          <th>Department</th>
          <th>Contact</th>
          <th>Issued Books</th>
          <th>Action</th>
        </tr>
      </thead>
      <tbody>
        <c:forEach var="student" items="${studentList}">
            <tr>
                <td><c:out value="${student.studentId}"/></td>
                <td><c:out value="${student.name}"/></td>
                <td><c:out value="${student.department}"/></td>
                <td><c:out value="${student.contact}"/></td>
                <td><c:out value="${student.issuedBooks}"/></td>
                <td>
                    <form action="students" method="post" style="display:inline;">
                        <input type="hidden" name="action" value="delete">
                        <input type="hidden" name="studentId" value="<c:out value='${student.studentId}'/>">
                        <button type="submit" class="deleteBtn" 
                                onclick="return confirm('Are you sure you want to delete student: ${student.name}?');">Delete</button>
                    </form>
                </td>
            </tr>
        </c:forEach>
        
        <c:if test="${empty studentList}">
             <tr><td colspan="6">No students found.</td></tr>
        </c:if>
      </tbody>
    </table>
  </main>
  </body>
</html>