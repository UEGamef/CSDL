package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.Account;
import service.AccountDao;
import service.SessionManager;

public class StudentAccountLookupController {
    @FXML private TextField txtMaSV;
    @FXML private Label lblUsername;
    @FXML private Label lblFullName;
    @FXML private Label lblRole;
    @FXML private Label lblActive;
    @FXML private Label lblStudentId;

    private final AccountDao accountDao = new AccountDao();

    @FXML
    public void initialize() {
        SessionManager session = SessionManager.getInstance();

        if (session.isStudent() && session.getStudentId() != null && !session.getStudentId().isBlank()) {
            txtMaSV.setText(session.getStudentId());
            loadAccount(session.getStudentId());
        } else {
            clearView();
        }
    }

    @FXML
    private void handleSearch() {
        String maSV = txtMaSV.getText() != null ? txtMaSV.getText().trim() : "";
        if (maSV.isBlank()) {
            showAlert("Vui lòng nhập mã sinh viên!");
            return;
        }

        loadAccount(maSV);
    }

    private void loadAccount(String studentId) {
        Account account = accountDao.findByStudentId(studentId);

        if (account == null) {
            clearView();
            showAlert("Không tìm thấy tài khoản cho sinh viên này!");
            return;
        }

        lblUsername.setText(valueOrDash(account.getUsername()));
        lblFullName.setText(valueOrDash(account.getFullName()));
        lblRole.setText(valueOrDash(account.getRole()));
        lblActive.setText(account.isActive() ? "Đang hoạt động" : "Đã khóa");
        lblStudentId.setText(valueOrDash(account.getStudentId()));
    }

    private void clearView() {
        lblUsername.setText("---");
        lblFullName.setText("---");
        lblRole.setText("---");
        lblActive.setText("---");
        lblStudentId.setText("---");
    }

    private String valueOrDash(String value) {
        return value != null && !value.isBlank() ? value : "---";
    }

    private void showAlert(String message) {
        new Alert(Alert.AlertType.WARNING, message).showAndWait();
    }
}