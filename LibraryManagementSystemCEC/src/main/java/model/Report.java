package model;

import java.time.LocalDate;

public class Report {
    private int reportId;
    private int studentId;
    private int bookId;
    private LocalDate issueDate;
    private LocalDate dueDate;
    private double fine;
    private String status;

    // getters & setters
    public int getReportId() { return reportId; }
    public void setReportId(int reportId) { this.reportId = reportId; }
    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }
    public int getBookId() { return bookId; }
    public void setBookId(int bookId) { this.bookId = bookId; }
    public LocalDate getIssueDate() { return issueDate; }
    public void setIssueDate(LocalDate issueDate) { this.issueDate = issueDate; }
    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
    public double getFine() { return fine; }
    public void setFine(double fine) { this.fine = fine; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
