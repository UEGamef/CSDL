package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Department;
import model.Major;
import service.DataService;

public class MajorDialogController {
    @FXML private TextField txtMaNganh, txtTenNganh, txtMoTa;
    @FXML private ComboBox<Department> cbKhoa;

    private Major major;
    private boolean saveClicked = false;

    @FXML
    public void initialize() {
        cbKhoa.setItems(DataService.getInstance().getDepartments());
        cbKhoa.setPromptText("Chọn khoa");
    }

    public void setMajor(Major major, boolean isEdit) {
        this.major = major;

        if (isEdit) {
            txtMaNganh.setText(major.getMaNganh());
            txtMaNganh.setEditable(false);
            txtTenNganh.setText(major.getTenNganh());
            txtMoTa.setText(major.getMoTa());

            if (major.getMaKhoa() != null) {
                for (Department dep : cbKhoa.getItems()) {
                    if (major.getMaKhoa().equals(dep.getMaKhoa())) {
                        cbKhoa.setValue(dep);
                        break;
                    }
                }
            }
        }
    }

    public boolean isSaveClicked() {
        return saveClicked;
    }

    @FXML
    private void handleSave() {
        String maNganh = txtMaNganh.getText() != null ? txtMaNganh.getText().trim() : "";
        String tenNganh = txtTenNganh.getText() != null ? txtTenNganh.getText().trim() : "";
        String moTa = txtMoTa.getText() != null ? txtMoTa.getText().trim() : "";
        Department khoa = cbKhoa.getValue();

        if (maNganh.isBlank() || tenNganh.isBlank() || khoa == null) {
            new Alert(Alert.AlertType.WARNING, "Vui lòng nhập đầy đủ thông tin bắt buộc!").showAndWait();
            return;
        }

        major.setMaNganh(maNganh);
        major.setTenNganh(tenNganh);
        major.setMaKhoa(khoa.getMaKhoa());
        major.setMoTa(moTa);

        saveClicked = true;
        closeStage();
    }

    @FXML
    private void handleCancel() {
        closeStage();
    }

    private void closeStage() {
        ((Stage) txtMaNganh.getScene().getWindow()).close();
    }
}