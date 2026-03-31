package service;

public class SessionManager {
    private static SessionManager instance;

    private String username;
    private String fullName;
    private String role;
    private String studentId;

    private SessionManager() {
    }

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public void login(String username, String fullName, String role, String studentId) {
        this.username = username;
        this.fullName = fullName;
        this.role = role;
        this.studentId = studentId;
    }

    public void logout() {
        this.username = null;
        this.fullName = null;
        this.role = null;
        this.studentId = null;
    }

    public boolean isLoggedIn() {
        return username != null && role != null;
    }

    public boolean isAdmin() {
        return "ADMIN".equalsIgnoreCase(role);
    }

    public boolean isLecturer() {
        return "LECTURER".equalsIgnoreCase(role);
    }

    public boolean isStudent() {
        return "STUDENT".equalsIgnoreCase(role);
    }

    public String getUsername() {
        return username;
    }

    public String getFullName() {
        return fullName;
    }

    public String getRole() {
        return role;
    }

    public String getStudentId() {
        return studentId;
    }
}