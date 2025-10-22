<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Return Book</title>
  <link rel="stylesheet" href="../css/styles.css">
  <style>
    body {
      margin: 0;
      font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
      background: #f4f7fb;
      display: flex;
    }
    .sidebar { width: 210px; background: #30465d; color: white; min-height: 100vh; padding-top: 20px; position: fixed; }
    .sidebar ul { list-style: none; padding: 0; }
    .sidebar li { margin: 15px 0; }
    .sidebar a { text-decoration: none; color: white; display: block; padding: 10px 20px; transition: background 0.3s; }
    .sidebar a:hover { background: #34495e; border-radius: 6px; }
    .dashboard-main { margin-left: 250px; padding: 30px 20px; }
    .dashboard-main h2 { color: #2c3e50; margin-bottom: 20px; }
    .card { background: #eaf3fc; padding: 20px; border-radius: 10px; box-shadow: 0 2px 6px rgba(0,0,0,0.1); margin-bottom: 30px; }
    .form-group { margin-bottom: 12px; }
    label { display: block; margin-bottom: 5px; font-weight: bold; }
    input { width: 100%; padding: 8px; border: 1px solid #ccc; border-radius: 6px; font-size: 14px; }
    .btn { padding: 8px 15px; border: none; border-radius: 6px; cursor: pointer; margin: 8px 5px 0 0; }
    .btn-submit { background: #3498db; color: white; }
    .btn-cancel { background: #e74c3c; color: white; }
    .fine-box { background: white; padding: 10px; border: 1px solid #ccc; border-radius: 6px; text-align: center; margin-top: 10px; font-weight: bold; box-shadow: 0 1px 4px rgba(0,0,0,0.1); }
    .fine-note { font-size: 13px; color: #555; margin-top: 6px; padding: 8px; background: #f9f9f9; border-left: 4px solid #3498db; border-radius: 4px; }
    
    table { width: 100%; border-collapse: collapse; margin-top: 20px; background: white; border-radius: 10px; overflow: hidden; box-shadow: 0 2px 6px rgba(0,0,0,0.1); }
    th, td { padding: 12px; text-align: center; border-bottom: 1px solid #ddd; }
    th { background: #3498db; color: white; }
    tr:hover { background: #f1f1f1; }
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
      <h2>Return Book</h2>
      <form id="returnForm" action="returns" method="post">
  		<div class="form-group">
    	<label>Student ID</label>
    	<input type="number" id="studentId" name="studentId" required>
  		</div>
  		<div class="form-group">
    		<label>Book ID</label>
    		<input type="number" id="bookId" name="bookId" required>
  		</div>
  		<div class="form-group">
    		<label>Returning Date</label>
    		<input type="date" id="returnDate" name="returnDate" required>
  		</div>
  		<div class="fine-box" id="fineBox">Fine Amount: ₹0</div>
  			<p class="fine-note">ℹ️ Keeping the book beyond the due date will incur an additional fine of ₹10/day.</p>
  			<input type="hidden" name="fine" id="fineInput" value="0">
  			<button type="submit" class="btn btn-submit">Return</button>
 			 <button type="reset" class="btn btn-cancel">Cancel</button>
	  </form>
      
      <h3 style="margin-top: 30px; color: #04315f;">Past Return History</h3>
      <table id="returnTable" style="margin-top: 10px;">
          <thead>
              <tr>
                  <th>Student ID</th>
                  <th>Book ID</th>
                  <th>Return Date</th>
                  <th>Fine Paid</th>
              </tr>
          </thead>
          <tbody>
              <c:forEach var="returnRecord" items="${returnList}">
                  <tr>
                      <td><c:out value="${returnRecord.studentId}"/></td>
                      <td><c:out value="${returnRecord.bookId}"/></td>
                      <td><c:out value="${returnRecord.returnDate}"/></td>
                      <td>₹<c:out value="${returnRecord.fine}"/></td>
                  </tr>
              </c:forEach>
              <c:if test="${empty returnList}">
                   <tr><td colspan="4">No return records found.</td></tr>
              </c:if>
          </tbody>
      </table>
    </div>
  </main>

  <script>
    const form = document.getElementById("returnForm");
    const fineBox = document.getElementById("fineBox");
    const fineInput = document.getElementById("fineInput");

    form.addEventListener("submit", function(e){
      const returnDate = new Date(document.getElementById("returnDate").value);
      // NOTE: You must implement server-side logic to fetch the correct DUE DATE 
      const dueDate = new Date(); 
      let fine = 0;
      
      if (returnDate > dueDate) {
        const diff = Math.ceil((returnDate - dueDate) / (1000 * 60 * 60 * 24));
        fine = diff * 10;
      }
      fineBox.textContent = "Fine Amount: ₹" + fine;
      fineInput.value = fine;
    });
  </script>

</body>
</html>