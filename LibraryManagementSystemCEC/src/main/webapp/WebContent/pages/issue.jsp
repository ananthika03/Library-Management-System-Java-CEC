<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Issue Book</title>
  <link rel="stylesheet" href="../css/styles.css">
  <style>
    body {
      margin: 0;
      font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
      background: #f4f7fb;
      display: flex;
    }

    /* Sidebar */
    .sidebar { width: 210px; background: #30465d; color: white; min-height: 100vh; padding-top: 20px; position: fixed; }
    .sidebar ul { list-style: none; padding: 0; }
    .sidebar li { margin: 15px 0; }
    .sidebar a { text-decoration: none; color: white; display: block; padding: 10px 20px; transition: background 0.3s; }
    .sidebar a:hover { background: #34495e; border-radius: 6px; }

    /* Main */
    .dashboard-main { margin-left: 250px; padding: 30px 20px; }
    .dashboard-main h2 { color: #2c3e50; margin-bottom: 20px; }

    /* Card/Form */
    .card {
      background: #eaf3fc;
      padding: 20px;
      border-radius: 10px;
      box-shadow: 0 2px 6px rgba(0,0,0,0.1);
      margin-bottom: 30px;
    }
    .form-group { margin-bottom: 12px; }
    label { display: block; margin-bottom: 5px; font-weight: bold; }
    input, select {
      width: 100%;
      padding: 8px;
      border: 1px solid #ccc;
      border-radius: 6px;
    }
    .btn { padding: 8px 15px; border: none; border-radius: 6px; cursor: pointer; margin: 8px 5px 0 0; }
    .btn-submit { background: #2ecc71; color: white; }
    .btn-cancel { background: #e74c3c; color: white; }

    /* Table */
    .search-bar { margin: 20px 0; display: flex; gap: 10px; background: white; padding: 10px; border-radius: 10px; box-shadow: 0 2px 6px rgba(0,0,0,0.1); }
    .search-bar input { padding: 8px; border: 1px solid #ddd; border-radius: 6px; font-size: 14px; flex: 1; }
    table { width: 100%; border-collapse: collapse; margin-top: 20px; background: white; border-radius: 10px; overflow: hidden; box-shadow: 0 2px 6px rgba(0,0,0,0.1); }
    th, td { padding: 12px; text-align: center; border-bottom: 1px solid #ddd; }
    th { background: #3498db; color: white; }
    tr:hover { background: #f1f1f1; }
    .deleteBtn { background: #e74c3c; color: white; padding: 5px 10px; border: none; border-radius: 5px; cursor: pointer; }
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
    <div class="card">
      <h2>Issue Book</h2>
      <form id="issueForm" action="issues" method="post">
        <div class="form-group">
          <label>Student ID</label>
          <input type="number" name="studentId" placeholder="Enter Student ID" required>
        </div>
        <div class="form-group">
          <label>Book ID</label>
          <input type="number" name="bookId" placeholder="Enter Book ID" required>
        </div>
        <div class="form-group">
          <label>Issue Date</label>
          <input type="date" name="issueDate" required>
        </div>
        <div class="form-group">
          <label>Return Date</label>
          <input type="date" name="dueDate" required>
        </div>
        <button type="submit" class="btn btn-submit">Submit</button>
        <button type="reset" class="btn btn-cancel">Cancel</button>
      </form>
    </div>

    <div class="search-bar">
      <input type="text" id="searchInput" placeholder="Search issued books...">
    </div>

    <table id="issuedTable">
      <thead>
        <tr>
          <th>Issue ID</th>
          <th>Student ID</th>
          <th>Book ID</th>
          <th>Issue Date</th>
          <th>Due Date</th>
          <th>Action</th>
        </tr>
      </thead>
      <tbody>
        <c:forEach var="issue" items="${issueList}">
            <tr>
                <td><c:out value="${issue.issueId}"/></td>
                <td><c:out value="${issue.studentId}"/></td>
                <td><c:out value="${issue.bookId}"/></td>
                <td><c:out value="${issue.issueDate}"/></td>
                <td><c:out value="${issue.dueDate}"/></td>
                <td><button class="deleteBtn">Delete</button></td>
            </tr>
        </c:forEach>
        <c:if test="${empty issueList}">
             <tr><td colspan="6">No books currently issued.</td></tr>
        </c:if>
      </tbody>
    </table>
  </main>

  <script>
    const issuedTable = document.getElementById('issuedTable').querySelector('tbody');
    const searchInput = document.getElementById('searchInput');

    searchInput.addEventListener('input', function(){
      const val = this.value.toLowerCase();
      issuedTable.querySelectorAll('tr').forEach(row=>{
        const studentId = row.cells[1]?.textContent.toLowerCase() || '';
        const bookId = row.cells[2]?.textContent.toLowerCase() || '';
        row.style.display = (studentId.includes(val) || bookId.includes(val)) ? '' : 'none';
      });
    });
  </script>

</body>
</html>