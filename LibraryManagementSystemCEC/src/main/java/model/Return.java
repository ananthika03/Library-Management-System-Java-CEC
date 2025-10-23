package model;

import java.time.LocalDate;

public class Return {
    private int returnId;
    private int studentId;
    private int bookId;
    private int issueId; // <-- NEW FIELD for Foreign Key
    private LocalDate returnDate;
    private double fine;
    
    // --- Getters and Setters ---
    public int getReturnId() { return returnId; }
    public void setReturnId(int returnId) { this.returnId = returnId; }
    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }
    public int getBookId() { return bookId; }
    public void setBookId(int bookId) { this.bookId = bookId; }
    public int getIssueId() { return issueId; } // <-- NEW GETTER
    public void setIssueId(int issueId) { this.issueId = issueId; } // <-- NEW SETTER
    public LocalDate getReturnDate() { return returnDate; }
    public void setReturnDate(LocalDate returnDate) { this.returnDate = returnDate; }
    public double getFine() { return fine; }
    public void setFine(double fine) { this.fine = fine; }
}