<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Book Management</title>
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
    .dashboard-main { margin-left: 250px; padding-left: 30px; padding-right: 20px; }
    .dashboard-main h2 { color: #2c3e50; margin-bottom: 20px; }

    /* Form */
    form { margin: 15px 0; padding: 15px; background: white; border-radius: 10px; box-shadow: 0 2px 6px rgba(0,0,0,0.1); display: flex; flex-wrap: wrap; gap: 10px; }
    form input, form select { flex: 1 1 calc(30% - 10px); padding: 8px; border: 1px solid #ddd; border-radius: 6px; font-size: 14px; }
    form button { flex: 1 1 100%; }

    /* Buttons */
    .btn { background: #3498db; color: white; padding: 8px 14px; border: none; cursor: pointer; border-radius: 6px; font-size: 14px; transition: background 0.3s; }
    .btn:hover { background: #2980b9; }
    .deleteBtn { background: #e74c3c; }
    .deleteBtn:hover { background: #c0392b; }

    /* Search Bar */
    .search-bar { margin: 20px 0; display: flex; gap: 10px; background: white; padding: 10px; border-radius: 10px; box-shadow: 0 2px 6px rgba(0,0,0,0.1); }
    .search-bar input, .search-bar select { padding: 8px; border: 1px solid #ddd; border-radius: 6px; font-size: 14px; flex: 1; }

    /* Table */
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
    <h2>Book Management</h2>
    
    <c:if test="${not empty errorMessage}">
        <p style="color: red; background: #ffe0e0; padding: 10px; border-radius: 5px;">
            <c:out value="${errorMessage}"/>
        </p>
    </c:if>

    <form id="bookForm" action="books" method="post">
      <input type="text" name="title" placeholder="Title" required>
      <input type="text" name="author" placeholder="Author" required>
      <input type="text" name="publisher" placeholder="Publisher" required>
      <input type="number" name="year" placeholder="Year" required>
      <input type="number" name="availableCopies" placeholder="Available Copies" required>
      <button type="submit" class="btn">Add Book</button>
    </form>
    
    <div class="search-bar">
      <input type="text" id="searchInput" placeholder="Search by Title, Author, or Publisher...">
      <select id="filterAvailability">
        <option value="">Filter by Availability</option>
        <option value="1">Available</option>
        <option value="0">Not Available</option>
      </select>
    </div>
    
    <table id="bookTable" class="styled-table">
      <thead>
        <tr>
          <th>Book ID</th>
          <th>Title</th>
          <th>Author</th>
          <th>Publisher</th>
          <th>Year</th>
          <th>Available</th>
          <th>Action</th>
        </tr>
      </thead>
      <tbody>
        <c:forEach var="book" items="${bookList}">
            <tr>
                <td><c:out value="${book.bookId}"/></td>
                <td><c:out value="${book.title}"/></td>
                <td><c:out value="${book.author}"/></td>
                <td><c:out value="${book.publisher}"/></td>
                <td><c:out value="${book.year}"/></td>
                <td><c:out value="${book.availableCopies > 0 ? 'Yes' : 'No'}"/> (<c:out value="${book.availableCopies}"/>)</td>
                <td>
                    <form action="books" method="post" style="display:inline;">
                        <input type="hidden" name="action" value="delete">
                        <input type="hidden" name="bookId" value="<c:out value='${book.bookId}'/>">
                        <button type="submit" class="btn deleteBtn" 
                                onclick="return confirm('Are you sure you want to delete book: ${book.title}?');">Delete</button>
                    </form>
                </td>
            </tr>
        </c:forEach>
        
        <c:if test="${empty bookList}">
             <tr><td colspan="7">No books found.</td></tr>
        </c:if>
      </tbody>
    </table>
  </main>

  <script>
    const bookTable = document.getElementById('bookTable').querySelector('tbody');
    const searchInput = document.getElementById('searchInput');
    const filterAvailability = document.getElementById('filterAvailability');

    function filterBooks() {
      const searchValue = searchInput.value.toLowerCase();
      const availabilityValue = filterAvailability.value;

      const rows = bookTable.querySelectorAll('tr');
      rows.forEach(row => {
        const cells = row.querySelectorAll('td');
        
        if (cells.length < 7) {
            row.style.display = "none";
            return;
        }

        const title = cells[1].textContent.toLowerCase();
        const author = cells[2].textContent.toLowerCase();
        const publisher = cells[3].textContent.toLowerCase();
        const availableText = cells[5].textContent.toLowerCase(); 

        const matchesSearch = title.includes(searchValue) || author.includes(searchValue) || publisher.includes(searchValue);
        
        let matchesAvailability = true;
        if (availabilityValue === "1") {
            matchesAvailability = availableText.startsWith("yes");
        } else if (availabilityValue === "0") {
            matchesAvailability = availableText.startsWith("no");
        }
        
        row.style.display = (matchesSearch && matchesAvailability) ? "" : "none";
      });
    }

    searchInput.addEventListener('input', filterBooks);
    filterAvailability.addEventListener('change', filterBooks);
  </script>
</body>
</html>