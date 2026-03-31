package controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.Account;
import service.AccountDao;

public class AccountController {
    @FXML private TextField txtUsername, txtFullName, txtStudentId;
    @FXML private PasswordField txtPassword;
    @FXML private ComboBox<String> cbRole;
    @FXML private CheckBox chkActive;

    @FXML private TableView<Account> tableAccounts;
    @FXML private TableColumn<Account, String> colId, colUsername, colFullName, colRole, colActive, colStudentId;

    private final AccountDao accountDao = new AccountDao();

    @FXML
    public void initialize() {
        cbRole.setItems(FXCollections.observableArrayList("ADMIN", "LECTURER", "STUDENT"));
        chkActive.setSelected(true);

        colId.setCellValueFactory(d -> new SimpleStringProperty(String.valueOf(d.getValue().getAccountId())));
        colUsername.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getUsername()));
        colFullName.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getFullName()));
        colRole.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getRole()));
        colActive.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().isActive() ? "Hoạt động" : "Khóa"));
        colStudentId.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getStudentId()));

        loadTable();

        tableAccounts.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) fillForm(newVal);
        });

        cbRole.valueProperty().addListener((obs, oldVal, newVal) -> {
            boolean isStudent = "STUDENT".equalsIgnoreCase(newVal);
            txtStudentId.setDisable(!isStudent);
            if (!isStudent) {
                txtStudentId.clear();
            }
        });
    }

    @FXML
    private void handleAdd() {
        Account account = readForm();
        if (account == null) return;

        if (accountDao.insert(account)) {
            loadTable();
            clearForm();
        } else {
            showAlert("Không thể thêm tài khoản!");
        }
    }

    @FXML
    private void handleUpdate() {
        Account selected = tableAccounts.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Vui lòng chọn một tài khoản để cập nhật!");
            return;
        }

        Account account = readForm();
        if (account == null) return;
        account.setAccountId(selected.getAccountId());

        if (accountDao.update(account)) {
            loadTable();
        } else {
            showAlert("Không thể cập nhật tài khoản!");
        }
    }

    @FXML
    private void handleDelete() {
        Account selected = tableAccounts.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Vui lòng chọn một tài khoản để xóa!");
            return;
        }

        if (accountDao.deleteById(selected.getAccountId())) {
            loadTable();
            clearForm();
        } else {
            showAlert("Không thể xóa tài khoản!");
        }
    }

    @FXML
    private void handleResetPassword() {
        Account selected = tableAccounts.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Vui lòng chọn một tài khoản để reset mật khẩu!");
            return;
        }

        if (accountDao.resetPassword(selected.getAccountId(), "123456")) {
            showAlert("Đã reset mật khẩu về 123456");
            loadTable();
        } else {
            showAlert("Không thể reset mật khẩu!");
        }
    }

    private Account readForm() {
        String username = txtUsername.getText() != null ? txtUsername.getText().trim() : "";
        String password = txtPassword.getText() != null ? txtPassword.getText().trim() : "";
        String fullName = txtFullName.getText() != null ? txtFullName.getText().trim() : "";
        String role = cbRole.getValue();
        String studentId = txtStudentId.getText() != null ? txtStudentId.getText().trim() : "";

        if (username.isBlank() || password.isBlank() || fullName.isBlank() || role == null) {
            showAlert("Vui lòng nhập đầy đủ thông tin!");
            return null;
        }

        if ("STUDENT".equalsIgnoreCase(role) && studentId.isBlank()) {
            showAlert("Tài khoản sinh viên phải có Mã SV!");
            return null;
        }

        Account account = new Account();
        account.setUsername(username);
        account.setPassword(password);
        account.setFullName(fullName);
        account.setRole(role);
        account.setActive(chkActive.isSelected());
        account.setStudentId("STUDENT".equalsIgnoreCase(role) ? studentId : null);
        return account;
    }

    private void fillForm(Account a) {
        txtUsername.setText(a.getUsername());
        txtPassword.setText(a.getPassword());
        txtFullName.setText(a.getFullName());
        cbRole.setValue(a.getRole());
        chkActive.setSelected(a.isActive());
        txtStudentId.setText(a.getStudentId());
    }

    private void clearForm() {
        txtUsername.clear();
        txtPassword.clear();
        txtFullName.clear();
        cbRole.setValue(null);
        chkActive.setSelected(true);
        txtStudentId.clear();
        txtStudentId.setDisable(false);
    }

    private void loadTable() {
        tableAccounts.setItems(accountDao.findAll());
    }

    private void showAlert(String message) {
        new Alert(Alert.AlertType.WARNING, message).showAndWait();
    }
}