package model;

import java.time.LocalDate;

public class Return {
    
    private int studentId;   // instead of issueId
    private int bookId;      // new field to identify the book
    private LocalDate returnDate;
    private double fine;

    // getters & setters
 
    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }

    public int getBookId() { return bookId; }
    public void setBookId(int bookId) { this.bookId = bookId; }

    public LocalDate getReturnDate() { return returnDate; }
    public void setReturnDate(LocalDate returnDate) { this.returnDate = returnDate; }

    public double getFine() { return fine; }
    public void setFine(double fine) { this.fine = fine; }
}
