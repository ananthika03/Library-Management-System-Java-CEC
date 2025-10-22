<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Reports</title>
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

    /* Card/Form */
    .card {
      background: #eaf3fc;
      padding: 20px;
      border-radius: 10px;
      box-shadow: 0 2px 6px rgba(0,0,0,0.1);
      margin-bottom: 30px;
    }

    /* Summary Stats */
    .stats { display: flex; gap: 15px; margin-bottom: 20px; }
    .stat-box {
      background: white;
      padding: 15px;
      border-radius: 8px;
      flex: 1;
      text-align: center;
      font-weight: bold;
      box-shadow: 0 1px 4px rgba(0,0,0,0.1);
    }

    /* Form */
    .form-group { margin-bottom: 12px; }
    label { display: block; margin-bottom: 5px; font-weight: bold; }
    input, select { padding: 8px; width: 100%; border: 1px solid #ccc; border-radius: 6px; font-size: 14px; }
    .btn { padding: 8px 15px; border: none; border-radius: 6px; cursor: pointer; margin: 8px 5px 0 0; }
    .btn-submit { background: #2ecc71; color: white; }
    .btn-cancel { background: #e74c3c; color: white; }

    /* Table */
    table { width: 100%; border-collapse: collapse; margin-top: 20px; background: white; border-radius: 10px; overflow: hidden; box-shadow: 0 2px 6px rgba(0,0,0,0.1); }
    th, td { padding: 12px; text-align: center; border-bottom: 1px solid #ddd; }
    th { background: #3498db; color: white; }
    tr:hover { background: #f1f1f1; }
    .status-returned { background: #2ecc71; color: white; padding: 5px 8px; border-radius: 5px; display: inline-block; }
    .status-overdue { background: #e74c3c; color: white; padding: 5px 8px; border-radius: 5px; display: inline-block; }
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
      <h2>Reports</h2>

      <div class="stats">
        <div class="stat-box" id="booksIssued">Books Issued: 0</div>
        <div class="stat-box" id="returnsToday">Returns Today: 0</div>
        <div class="stat-box" id="overdue">Overdue: 0</div>
      </div>

      <form id="reportForm" action="reports" method="post">
        <div class="form-group">
          <label>Student ID</label>
          <input type="text" id="studentId" name="studentId" required>
        </div>
        <div class="form-group">
          <label>Book ID</label>
          <input type="text" id="bookId" name="bookId" required>
        </div>
        <div class="form-group">
          <label>Issue Date</label>
          <input type="date" id="issueDate" name="issueDate" required>
        </div>
        <div class="form-group">
          <label>Due Date</label>
          <input type="date" id="dueDate" name="dueDate" required>
        </div>
        <div class="form-group">
          <label>Fine Amount</label>
          <input type="number" id="fine" name="fine" value="0" required>
        </div>
        <div class="form-group">
          <label>Status</label>
          <select id="status" name="status" required>
            <option value="Returned">Returned</option>
            <option value="Overdue">Overdue</option>
          </select>
        </div>
        <button type="submit" class="btn btn-submit">Add Record</button>
        <button type="reset" class="btn btn-cancel">Clear</button>
      </form>

      <table id="reportTable">
        <thead>
          <tr>
            <th>Report ID</th>
            <th>Student ID</th>
            <th>Book ID</th>
            <th>Issue Date</th>
            <th>Due Date</th>
            <th>Fine Amount</th>
            <th>Status</th>
          </tr>
        </thead>
        <tbody>
           <c:forEach var="report" items="${reportList}">
                <tr>
                    <td><c:out value="${report.reportId}"/></td>
                    <td><c:out value="${report.studentId}"/></td>
                    <td><c:out value="${report.bookId}"/></td>
                    <td><c:out value="${report.issueDate}"/></td>
                    <td><c:out value="${report.dueDate}"/></td>
                    <td>₹<c:out value="${report.fine}"/></td>
                    <td>
                        <c:choose>
                            <c:when test="${report.status eq 'Returned'}">
                                <span class="status-returned">Returned</span>
                            </c:when>
                            <c:otherwise>
                                <span class="status-overdue"><c:out value="${report.status}"/></span>
                            </c:otherwise>
                        </c:choose>
                    </td>
                </tr>
            </c:forEach>
             <c:if test="${empty reportList}">
                 <tr><td colspan="7">No report records found.</td></tr>
             </c:if>
        </tbody>
      </table>
    </div>
  </main>

  <script>
    const tableBody = document.querySelector("#reportTable tbody");
    const booksIssuedBox = document.getElementById("booksIssued");
    const returnsTodayBox = document.getElementById("returnsToday");
    const overdueBox = document.getElementById("overdue");

    function updateStats() {
      let booksIssued = 0;
      let returnsToday = 0;
      let overdueCount = 0;
      
      const rows = tableBody.querySelectorAll("tr");
      rows.forEach(row => {
        if(row.cells.length < 7) return; 
        
        booksIssued++;
        const statusText = row.cells[6].textContent.trim();
        
        if (statusText === "Returned") {
          returnsToday++;
        } else if (statusText === "Overdue") {
          overdueCount++;
        }
      });

      booksIssuedBox.textContent = "Books Issued: " + booksIssued;
      returnsTodayBox.textContent = "Returns Today: " + returnsToday; 
      overdueBox.textContent = "Overdue: " + overdueCount;
    }
    
    window.onload = updateStats;
  </script>

</body>
</html>