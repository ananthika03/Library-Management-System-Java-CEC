<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>


<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Library Dashboard</title>
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
    .dashboard-cards {
        display: grid;
        grid-template-columns: repeat(2, 1fr);
        gap: 20px;
    }
    .dashboard-cards .card {
        padding: 40px;
        text-align: center;
        font-size: 18px;
        font-weight: bold;
        border: 1px solid #ddd;
        cursor: pointer;
        transition: background 0.3s;
    }
    .dashboard-cards .card:hover {
        background: #cce7ff;
    }
  </style>
</head>
<body>
  <aside class="sidebar">
    <ul>
      <li><a href="../books">Books</a></li>
      <li><a href="../students">Students</a></li>
      <li><a href="../issues">Issue Book</a></li>
      <li><a href="../returns">Return Book</a></li>
      <li><a href="../reports">Reports</a></li>
      <li><a href="../logout">Logout</a></li>
      <li><a href="../index.html">Main Page</a></li>
    </ul>
  </aside>

  <main class="dashboard-main">
    <h2>Welcome, Admin!</h2>
    
    <div class="card">
      <p>Use the navigation panel on the left to manage library records.</p>
    </div>
    
    <div class="dashboard-cards">
      <div class="card" onclick="window.location.href='${pageContext.request.contextPath}/books'">Manage Books</div>
      <div class="card" onclick="window.location.href='${pageContext.request.contextPath}/students'">Manage Students</div>
      <div class="card" onclick="window.location.href='${pageContext.request.contextPath}/issues'">Issue Books</div>
      <div class="card" onclick="window.location.href='${pageContext.request.contextPath}/returns'">Process Returns</div>
    </div>
  </main>
</body>
</html>