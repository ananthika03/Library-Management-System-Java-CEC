package model;

public class Admin {
    private int adminId;
    private String name;
    private String email;
    private String userid;   // ✅ Add this line
    private String password;

    // Getters & Setters
    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserid() {    // ✅ Add this getter
        return userid;
    }

    public void setUserid(String userid) {   // ✅ Add this setter
        this.userid = userid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
