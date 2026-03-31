package model;

public class Account {
    private int accountId;
    private String username;
    private String password;
    private String fullName;
    private String role;
    private boolean active;
    private String studentId;

    public Account() {
    }

    public Account(int accountId, String username, String password, String fullName, String role, boolean active) {
        this.accountId = accountId;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.role = role;
        this.active = active;
    }

    public Account(int accountId, String username, String password, String fullName, String role, boolean active, String studentId) {
        this(accountId, username, password, fullName, role, active);
        this.studentId = studentId;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }
}