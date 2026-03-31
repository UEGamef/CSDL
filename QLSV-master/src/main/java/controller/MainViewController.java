package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import service.SessionManager;

public class MainViewController {
    @FXML private TabPane tabPane;

    @FXML private Tab tabDepartment;
    @FXML private Tab tabClass;
    @FXML private Tab tabStudent;
    @FXML private Tab tabStudentGradeLookup;
    @FXML private Tab tabSubject;
    @FXML private Tab tabGrade;
    @FXML private Tab tabLecturer;
    @FXML private Tab tabMajor;
    @FXML private Tab tabStatistics;

    @FXML private Label lblCurrentUser;
    @FXML private Button btnAccountManager;

    private Stage accountStage;

    @FXML
    public void initialize() {
        SessionManager session = SessionManager.getInstance();

        if (lblCurrentUser != null) {
            lblCurrentUser.setText("Đăng nhập: " + (session.getFullName() != null ? session.getFullName() : session.getUsername()));
        }

        if (btnAccountManager != null) {
            boolean isAdmin = session.isAdmin();
            btnAccountManager.setVisible(isAdmin);
            btnAccountManager.setManaged(isAdmin);
        }

        if (session.isAdmin()) {
            return;
        }

        if (session.isLecturer()) {
            tabPane.getTabs().removeAll(
                    tabDepartment,
                    tabClass,
                    tabLecturer,
                    tabMajor,
                    tabStatistics
            );
            return;
        }

        if (session.isStudent()) {
            tabPane.getTabs().removeAll(
                    tabDepartment,
                    tabClass,
                    tabStudent,
                    tabSubject,
                    tabLecturer,
                    tabMajor,
                    tabStatistics,
                    tabGrade
            );
            return;
        }

        tabPane.getTabs().clear();
    }

    @FXML
    private void handleOpenAccountWindow() {
        try {
            if (accountStage != null && accountStage.isShowing()) {
                accountStage.toFront();
                accountStage.requestFocus();
                return;
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AccountView.fxml"));
            accountStage = new Stage();
            accountStage.setTitle("Tạo Tài Khoản / Phân Quyền");
            accountStage.initModality(Modality.NONE);
            accountStage.setScene(new Scene(loader.load()));
            accountStage.setOnHidden(e -> accountStage = null);
            accountStage.show();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.WARNING, "Không thể mở cửa sổ quản lý tài khoản!").showAndWait();
        }
    }

    @FXML
    private void handleLogout() {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Bạn có muốn đăng xuất không?",
                ButtonType.YES, ButtonType.NO);
        confirm.setHeaderText(null);

        confirm.showAndWait().ifPresent(result -> {
            if (result == ButtonType.YES) {
                SessionManager.getInstance().logout();
                closeAccountWindow();
                openLoginView();
                closeCurrentStage();
            }
        });
    }

    private void closeAccountWindow() {
        if (accountStage != null) {
            accountStage.close();
            accountStage = null;
        }
    }

    private void openLoginView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/LoginView.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Đăng nhập");
            stage.initModality(Modality.NONE);
            stage.setScene(new Scene(loader.load()));
            stage.show();
            stage.toFront();
            stage.requestFocus();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.WARNING, "Không thể mở màn hình đăng nhập!").showAndWait();
        }
    }

    private void closeCurrentStage() {
        Stage stage = (Stage) tabPane.getScene().getWindow();
        stage.close();
    }
}