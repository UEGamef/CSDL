package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Department;
import model.Lecturer;
import model.Subject;
import service.DataService;

public class LecturerDialogController {
    @FXML private TextField txtMaGv, txtHo, txtTen, txtGioiTinh, txtSdt, txtEmail, txtHocVi, txtDiaChi;
    @FXML private ComboBox<Department> cbKhoa;
    @FXML private ComboBox<Subject> cbMonHoc;

    private Lecturer lecturer;
    private boolean saveClicked = false;
    private boolean isEdit = false;

    @FXML
    public void initialize() {
        cbKhoa.setItems(DataService.getInstance().getDepartments());
        cbKhoa.setPromptText("Chọn khoa");

        cbMonHoc.setItems(DataService.getInstance().getSubjects());
        cbMonHoc.setPromptText("Chọn môn học");
    }

    public void setLecturer(Lecturer lecturer, boolean isEdit) {
        this.lecturer = lecturer;
        this.isEdit = isEdit;

        if (isEdit) {
            txtMaGv.setText(lecturer.getMaGv());
            txtMaGv.setEditable(false);
            txtHo.setText(lecturer.getHo());
            txtTen.setText(lecturer.getTen());
            txtGioiTinh.setText(lecturer.getGioiTinh());
            txtSdt.setText(lecturer.getSdt());
            txtEmail.setText(lecturer.getEmail());
            txtHocVi.setText(lecturer.getHocVi());
            txtDiaChi.setText(lecturer.getDiaChi());

            if (lecturer.getMaKhoa() != null) {
                for (Department dep : cbKhoa.getItems()) {
                    if (lecturer.getMaKhoa().equals(dep.getMaKhoa())) {
                        cbKhoa.setValue(dep);
                        break;
                    }
                }
            }

            if (lecturer.getMaMonHoc() != null) {
                for (Subject subject : cbMonHoc.getItems()) {
                    if (lecturer.getMaMonHoc().equals(subject.getMaMonHoc())) {
                        cbMonHoc.setValue(subject);
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
        String maGv = txtMaGv.getText() != null ? txtMaGv.getText().trim() : "";
        String ho = txtHo.getText() != null ? txtHo.getText().trim() : "";
        String ten = txtTen.getText() != null ? txtTen.getText().trim() : "";
        String gioiTinh = txtGioiTinh.getText() != null ? txtGioiTinh.getText().trim() : "";
        String sdt = txtSdt.getText() != null ? txtSdt.getText().trim() : "";
        String email = txtEmail.getText() != null ? txtEmail.getText().trim() : "";
        String hocVi = txtHocVi.getText() != null ? txtHocVi.getText().trim() : "";
        String diaChi = txtDiaChi.getText() != null ? txtDiaChi.getText().trim() : "";
        Department khoa = cbKhoa.getValue();
        Subject monHoc = cbMonHoc.getValue();

        if (maGv.isBlank() || ho.isBlank() || ten.isBlank() || khoa == null) {
            new Alert(Alert.AlertType.WARNING, "Vui lòng nhập đầy đủ thông tin bắt buộc!").showAndWait();
            return;
        }

        if (!email.isBlank() && !email.matches("^[\\w.-]+@[\\w.-]+\\.[A-Za-z]{2,}$")) {
            new Alert(Alert.AlertType.WARNING, "Email không hợp lệ!").showAndWait();
            return;
        }

        lecturer.setMaGv(maGv);
        lecturer.setHo(ho);
        lecturer.setTen(ten);
        lecturer.setGioiTinh(gioiTinh);
        lecturer.setSdt(sdt);
        lecturer.setEmail(email);
        lecturer.setHocVi(hocVi);
        lecturer.setDiaChi(diaChi);
        lecturer.setMaKhoa(khoa.getMaKhoa());
        lecturer.setMaMonHoc(monHoc != null ? monHoc.getMaMonHoc() : null);

        saveClicked = true;
        closeStage();
    }

    @FXML
    private void handleCancel() {
        closeStage();
    }

    private void closeStage() {
        ((Stage) txtMaGv.getScene().getWindow()).close();
    }
}