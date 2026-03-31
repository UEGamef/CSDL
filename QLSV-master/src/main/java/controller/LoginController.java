package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Account;
import service.AccountDao;
import service.SessionManager;

public class LoginController {
    @FXML private TextField txtUsername;
    @FXML private PasswordField txtPassword;

    private final AccountDao accountDao = new AccountDao();

    @FXML
    private void handleLogin() {
        String username = txtUsername.getText() != null ? txtUsername.getText().trim() : "";
        String password = txtPassword.getText() != null ? txtPassword.getText().trim() : "";

        if (username.isBlank() || password.isBlank()) {
            showAlert("Vui lòng nhập username và password!");
            return;
        }

        Account account = accountDao.login(username, password);
        if (account == null) {
            showAlert("Sai tài khoản hoặc mật khẩu!");
            return;
        }

        SessionManager.getInstance().login(
                account.getUsername(),
                account.getFullName(),
                account.getRole(),
                account.getStudentId()

        );

        openMainView();
        closeStage();
    }

    @FXML
    private void handleExit() {
        closeStage();
    }

    private void openMainView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MainView.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Quản lý sinh viên");
            stage.setScene(new Scene(loader.load()));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Không thể mở màn hình chính!");
        }
    }

    private void closeStage() {
        ((Stage) txtUsername.getScene().getWindow()).close();
    }

    private void showAlert(String message) {
        new Alert(Alert.AlertType.WARNING, message).showAndWait();
    }
}