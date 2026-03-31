package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Department;
import service.DataService;

public class DepartmentDialogController {
    @FXML private TextField txtMaKhoa, txtTenKhoa, txtSDT, txtEmail, txtVanPhong;

    private Department department;
    private boolean saveClicked = false;
    private boolean isEdit = false;

    public void setDepartment(Department department, boolean isEdit) {
        this.department = department;
        this.isEdit = isEdit;

        if (isEdit) {
            txtMaKhoa.setText(department.getMaKhoa());
            txtMaKhoa.setEditable(false);
            txtTenKhoa.setText(department.getTenKhoa());
            txtSDT.setText(department.getSoDienThoai());
            txtEmail.setText(department.getEmail());
            txtVanPhong.setText(department.getVanPhong());
        }
    }

    public boolean isSaveClicked() {
        return saveClicked;
    }

    @FXML
    private void handleSave() {
        String maKhoa = txtMaKhoa.getText() != null ? txtMaKhoa.getText().trim() : "";
        String tenKhoa = txtTenKhoa.getText() != null ? txtTenKhoa.getText().trim() : "";
        String sdt = txtSDT.getText() != null ? txtSDT.getText().trim() : "";
        String email = txtEmail.getText() != null ? txtEmail.getText().trim() : "";
        String vanPhong = txtVanPhong.getText() != null ? txtVanPhong.getText().trim() : "";

        if (maKhoa.isBlank() || tenKhoa.isBlank()) {
            new Alert(Alert.AlertType.WARNING, "Vui lòng nhập ít nhất Mã khoa và Tên khoa!").showAndWait();
            return;
        }

        if (!isEdit && DataService.getInstance().getDepartments().stream().anyMatch(d -> maKhoa.equalsIgnoreCase(d.getMaKhoa()))) {
            new Alert(Alert.AlertType.WARNING, "Mã khoa đã tồn tại!").showAndWait();
            return;
        }

        if (!email.isBlank() && !email.matches("^[\\w.-]+@[\\w.-]+\\.[A-Za-z]{2,}$")) {
            new Alert(Alert.AlertType.WARNING, "Email không hợp lệ!").showAndWait();
            return;
        }

        department.setMaKhoa(maKhoa);
        department.setTenKhoa(tenKhoa);
        department.setSoDienThoai(sdt);
        department.setEmail(email);
        department.setVanPhong(vanPhong);

        saveClicked = true;
        closeStage();
    }

    @FXML
    private void handleCancel() {
        closeStage();
    }

    private void closeStage() {
        ((Stage) txtMaKhoa.getScene().getWindow()).close();
    }
}