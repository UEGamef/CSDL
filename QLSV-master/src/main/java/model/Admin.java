package model;

public class Admin {
    private int adminId;
    private String username;
    private String password;
    private String fullName;
    private boolean active;

    public Admin() {
    }

    public Admin(int adminId, String username, String password, String fullName, boolean active) {
        this.adminId = adminId;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.active = active;
    }

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}