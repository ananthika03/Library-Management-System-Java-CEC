<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Issue Book</title>
  <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600&display=swap" rel="stylesheet">
  <style>
    /* Reset and Base Styles */
    * {
      margin: 0;
      padding: 0;
      box-sizing: border-box;
    }
    html, body {
      height: 100%;
      width: 100%;
      font-family: 'Poppins', sans-serif;
      color: #fff;
    }
    body {
      display: flex;
      background: #0d0517; /* Fallback */
      overflow: hidden;
    }

    /* Animated Starfield Background */
    #starfield-bg {
      position: fixed;
      top: 0;
      left: 0;
      width: 100%;
      height: 100%;
      z-index: 1;
      background: radial-gradient(ellipse at bottom, #1a0b2e, #0d0517);
    }

    /* Sidebar (Same as dashboard.jsp) */
    .sidebar {
      position: relative;
      z-index: 3;
      width: 260px;
      height: 100vh;
      display: flex;
      flex-direction: column;
      background: rgba(20, 10, 30, 0.75);
      backdrop-filter: blur(10px);
      border-right: 1px solid rgba(188, 19, 254, 0.3);
      box-shadow: 0 0 20px rgba(188, 19, 254, 0.1);
      flex-shrink: 0;
    }
    .sidebar-header {
      padding: 20px;
      text-align: center;
      border-bottom: 1px solid rgba(188, 19, 254, 0.2);
    }
    .sidebar-header img {
      height: 40px;
      margin-bottom: 10px;
    }
    .sidebar-header h2 {
      font-size: 1.2rem;
      color: #f0f0f0;
      font-weight: 500;
    }
    .sidebar-nav {
      flex: 1;
      overflow-y: auto;
    }
    .sidebar-nav ul {
      list-style: none;
      padding-top: 20px;
    }
    .sidebar-nav li a {
      display: flex;
      align-items: center;
      padding: 16px 25px;
      color: #aaa;
      text-decoration: none;
      font-weight: 500;
      font-size: 0.95rem;
      transition: all 0.3s ease;
      border-left: 4px solid transparent;
    }
    .sidebar-nav li a svg {
      width: 20px;
      height: 20px;
      margin-right: 15px;
      stroke-width: 2px;
    }
    .sidebar-nav li a:hover {
      background: rgba(0, 229, 255, 0.1);
      color: #00e5ff;
      border-left-color: #00e5ff;
    }
    .sidebar-nav li.active a {
      background: rgba(0, 229, 255, 0.15);
      color: #00e5ff;
      border-left-color: #00e5ff;
    }
    .sidebar-footer {
      padding: 20px;
      border-top: 1px solid rgba(188, 19, 254, 0.2);
    }
    .logout-btn {
      display: block;
      text-align: center;
      background: #00e5ff;
      color: #0d0517;
      padding: 12px;
      border: none;
      border-radius: 8px;
      font-size: 1rem;
      font-weight: 600;
      cursor: pointer;
      text-decoration: none;
      transition: all 0.2s ease;
    }
    .logout-btn:hover {
      transform: translateY(-2px);
      box-shadow: 0 5px 20px rgba(0, 229, 255, 0.4);
    }

    /* Main Content */
    .main-content {
      position: relative;
      z-index: 2;
      flex: 1;
      height: 100vh;
      overflow-y: auto;
      padding: 40px;
    }
    .main-content h1 {
      font-size: 2.2rem;
      font-weight: 600;
      color: #f0f0f0;
      margin-bottom: 25px;
    }
    
    /* Glowing "Card" container */
    .form-container {
      background: rgba(20, 10, 30, 0.75);
      backdrop-filter: blur(10px);
      padding: 25px;
      border-radius: 20px;
      border: 1px solid rgba(188, 19, 254, 0.3);
      box-shadow: 0 0 15px rgba(188, 19, 254, 0.1);
      margin-bottom: 25px;
    }

    /* Issue Form */
    #issueForm {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
      gap: 20px;
    }
    .form-group {
      display: flex;
      flex-direction: column;
    }
    .form-group label {
      font-size: 0.9rem;
      font-weight: 500;
      color: #aaa;
      margin-bottom: 8px;
    }
    .form-group input[type="text"],
    .form-group input[type="number"],
    .form-group input[type="date"] {
      padding: 12px 15px;
      border: 1px solid #bc13fe;
      background: rgba(255, 255, 255, 0.05);
      border-radius: 8px;
      font-size: 0.95rem;
      color: #fff;
      font-family: 'Poppins', sans-serif;
      transition: all 0.3s ease;
    }
    .form-group input::placeholder { color: #888; }
    .form-group input:focus {
      outline: none;
      border-color: #00e5ff;
      background: rgba(255, 255, 255, 0.1);
      box-shadow: 0 0 15px rgba(0, 229, 255, 0.5);
    }
    
    /* Buttons */
    .btn {
      padding: 12px 20px;
      border: none;
      border-radius: 8px;
      font-size: 1rem;
      font-weight: 600;
      cursor: pointer;
      transition: all 0.2s ease;
      margin-top: 10px; /* Align with other inputs */
    }
    .btn-submit {
      background: #00e5ff;
      color: #0d0517;
    }
    .btn-submit:hover {
      transform: translateY(-2px);
      box-shadow: 0 5px 20px rgba(0, 229, 255, 0.4);
    }
    .btn-cancel {
      background: #e74c3c;
      color: white;
    }
    .btn-cancel:hover {
      background: #c0392b;
      transform: translateY(-2px);
      box-shadow: 0 5px 15px rgba(231, 76, 60, 0.3);
    }
    .deleteBtn { /* For table button */
      background: #e74c3c;
      color: white;
      padding: 8px 12px;
      font-size: 0.9rem;
      font-weight: 500;
      border: none;
      border-radius: 6px;
      cursor: pointer;
    }
    .deleteBtn:hover {
      background: #c0392b;
      transform: translateY(-1px);
    }

    /* Search Bar */
    .search-bar {
      display: flex;
    }
    .search-bar input {
      width: 100%;
      padding: 12px 15px;
      border: 1px solid #bc13fe;
      background: rgba(255, 255, 255, 0.05);
      border-radius: 8px;
      font-size: 0.95rem;
      color: #fff;
      font-family: 'Poppins', sans-serif;
      transition: all 0.3s ease;
    }
    .search-bar input::placeholder { color: #888; }
    .search-bar input:focus {
      outline: none;
      border-color: #00e5ff;
      background: rgba(255, 255, 255, 0.1);
      box-shadow: 0 0 15px rgba(0, 229, 255, 0.5);
    }

    /* Table Container */
    .table-container {
      background: rgba(20, 10, 30, 0.75);
      backdrop-filter: blur(10px);
      border-radius: 20px;
      border: 1px solid rgba(188, 19, 254, 0.3);
      box-shadow: 0 0 15px rgba(188, 19, 254, 0.1);
      overflow: hidden; /* For border-radius */
    }
    table {
      width: 100%;
      border-collapse: collapse;
    }
    th, td {
      padding: 14px 18px;
      text-align: left;
      border-bottom: 1px solid rgba(188, 19, 254, 0.2);
    }
    th {
      background: rgba(188, 19, 254, 0.2);
      color: #00e5ff;
      font-weight: 600;
      font-size: 0.9rem;
      text-transform: uppercase;
    }
    tr {
      transition: background 0.2s ease;
    }
    tr:last-child td {
      border-bottom: none;
    }
    tr:hover {
      background: rgba(0, 229, 255, 0.05);
    }
    
  </style>
</head>
<body>

  <canvas id="starfield-bg"></canvas>

  <aside class="sidebar">
    <div class="sidebar-header">
      <img src="https://ceconline.edu/wp-content/uploads/2024/06/CEC-logo.png" alt="CEC Logo">
      <h2>Librarian's Hub</h2>
    </div>
    
    <nav class="sidebar-nav">
      <ul>
        <li>
          <a href="${pageContext.request.contextPath}/dashboard"> <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-linecap="round" stroke-linejoin="round"><rect x="3" y="3" width="7" height="7"></rect><rect x="14" y="3" width="7" height="7"></rect><rect x="14" y="14" width="7" height="7"></rect><rect x="3" y="14" width="7" height="7"></rect></svg>
            Dashboard
          </a>
        </li>
        <li> 
          <a href="${pageContext.request.contextPath}/books">
            <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-linecap="round" stroke-linejoin="round"><path d="M4 19.5A2.5 2.5 0 0 1 6.5 17H20"></path><path d="M6.5 2H20v20H6.5A2.5 2.5 0 0 1 4 19.5v-15A2.5 2.5 0 0 1 6.5 2z"></path></svg>
            Manage Books
          </a>
        </li>
        <li>
          <a href="${pageContext.request.contextPath}/students">
            <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-linecap="round" stroke-linejoin="round"><path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"></path><circle cx="9" cy="7" r="4"></circle><path d="M23 21v-2a4 4 0 0 0-3-3.87"></path><path d="M16 3.13a4 4 0 0 1 0 7.75"></path></svg>
            Manage Students
          </a>
        </li>
        <li class="active">
          <a href="${pageContext.request.contextPath}/issues">
            <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-linecap="round" stroke-linejoin="round"><path d="M15 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V7Z"></path><path d="M14 2v4a2 2 0 0 0 2 2h4"></path><path d="m12 18 4-4-4-4"></path><path d="m8 14h8"></path></svg>
            Issue Book
          </a>
        </li>
        <li>
          <a href="${pageContext.request.contextPath}/returns">
            <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-linecap="round" stroke-linejoin="round"><path d="M15 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V7Z"></path><path d="M14 2v4a2 2 0 0 0 2 2h4"></path><path d="m12 18-4-4 4-4"></path><path d="m16 14H8"></path></svg>
            Return Book
          </a>
        </li>
        <li>
          <a href="${pageContext.request.contextPath}/reports">
            <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-linecap="round" stroke-linejoin="round"><path d="M16 4h2a2 2 0 0 1 2 2v14a2 2 0 0 1-2 2H6a2 2 0 0 1-2-2V6a2 2 0 0 1 2-2h2"></path><rect x="8" y="2" width="8" height="4" rx="1" ry="1"></rect><line x1="12" y1="11" x2="12" y2="17"></line><line x1="9" y1="14" x2="15" y2="14"></line></svg>
            View Reports
          </a>
        </li>
         <li>
          <a href="${pageContext.request.contextPath}/index.html">
            <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="m3 9 9-7 9 7v11a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z"></path><polyline points="9 22 9 12 15 12 15 22"></polyline></svg>
            Main Page
          </a>
        </li>
      </ul>
    </nav>
    <div class="sidebar-footer">
      <a href="${pageContext.request.contextPath}/logout" class="logout-btn">Logout</a>
    </div>
  </aside>

  <main class="main-content">
    
    <div class="form-container">
      <h1>Issue Book</h1>
      <form id="issueForm" action="issues" method="post" style="width: 100%;">
        <div class="form-group">
          <label for="studentId">Student ID</label>
          <input type="number" id="studentId" name="studentId" placeholder="Enter Student ID" required>
        </div>
        <div class="form-group">
          <label for="bookId">Book ID</label>
          <input type="number" id="bookId" name="bookId" placeholder="Enter Book ID" required>
        </div>
        <div class="form-group">
          <label for="issueDate">Issue Date</label>
          <input type="date" id="issueDate" name="issueDate" required>
        </div>
        <div class="form-group">
          <label for="dueDate">Return Date</label>
          <input type="date" id="dueDate" name="dueDate" required>
        </div>
        <div class="form-group">
            <label style="opacity: 0;">Submit</label> <button type="submit" class="btn btn-submit">Submit</button>
        </div>
        <div class="form-group">
            <label style="opacity: 0;">Cancel</label> <button type="reset" class="btn btn-cancel">Cancel</button>
        </div>
      </form>
    </div>

    <div class="form-container search-bar">
      <input type="text" id="searchInput" placeholder="Search by Student ID or Book ID...">
    </div>

    <div class="table-container">
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
                  <td><button class="deleteBtn">Delete</button></td> </tr>
          </c:forEach>
          <c:if test="${empty issueList}">
               <tr><td colspan="6" style="text-align: center; color: #aaa;">No books currently issued.</td></tr>
          </c:if>
        </tbody>
      </table>
    </div>
  </main>

  <script>
    const issuedTable = document.getElementById('issuedTable').querySelector('tbody');
    const searchInput = document.getElementById('searchInput');

    searchInput.addEventListener('input', function(){
      const val = this.value.toLowerCase();
      issuedTable.querySelectorAll('tr').forEach(row=>{
        
        // Ensure row has cells before trying to access them
        if (row.cells.length < 6) return; 

        const studentId = row.cells[1]?.textContent.toLowerCase() || '';
        const bookId = row.cells[2]?.textContent.toLowerCase() || '';
        
        row.style.display = (studentId.includes(val) || bookId.includes(val)) ? '' : 'none';
      });
    });
  </script>

  <script>
    const canvas = document.getElementById('starfield-bg');
    const ctx = canvas.getContext('2d');
    canvas.width = window.innerWidth;
    canvas.height = window.innerHeight;

    let stars = [];
    const numStars = 200;

    class Star {
      constructor(x, y, radius, color, velocity) {
        this.x = x; this.y = y; this.radius = radius; this.color = color; this.velocity = velocity;
      }
      draw() {
        ctx.beginPath();
        ctx.arc(this.x, this.y, this.radius, 0, Math.PI * 2, false);
        ctx.fillStyle = this.color;
        ctx.fill();
      }
      update() {
        this.y += this.velocity;
        if (this.y - this.radius > canvas.height) {
          this.y = 0 - this.radius;
          this.x = Math.random() * canvas.width;
          this.radius = Math.random() * 1.5 + 0.5;
          this.velocity = Math.random() * 0.5 + 0.1;
        }
        this.draw();
      }
    }
    
    function init() {
      stars = [];
      for (let i = 0; i < numStars; i++) {
        const radius = Math.random() * 1.5 + 0.5;
        const x = Math.random() * canvas.width;
        const y = Math.random() * canvas.height;
        const color = 'rgba(188, 19, 254, 0.6)';
        const velocity = Math.random() * 0.5 + 0.1;
        stars.push(new Star(x, y, radius, color, velocity));
      }
    }
    
    function animate() {
      requestAnimationFrame(animate);
      ctx.clearRect(0, 0, canvas.width, canvas.height);
      for (let i = 0; i < stars.length; i++) {
        stars[i].update();
      }
    }
    
    window.addEventListener('resize', () => {
      canvas.width = window.innerWidth;
      canvas.height = window.innerHeight;
      init();
    });
    
    init();
    animate();
  </script>

</body>
</html>