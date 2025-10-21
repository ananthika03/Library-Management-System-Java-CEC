package model;

public class Student {
    private int studentId;
    private String name;
    private String department;
    private String contact;
    private int issuedBooks;

    // getters & setters
    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }
    public int getIssuedBooks() { return issuedBooks; }
    public void setIssuedBooks(int issuedBooks) { this.issuedBooks = issuedBooks; }
}
