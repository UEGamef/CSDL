package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.ClassUni;
import model.Department;
import model.Major;
import service.DataService;

public class ClassDialogController {
    @FXML private TextField txtMaLop, txtTenLop, txtNienKhoa;
    @FXML private ComboBox<Major> cbNganh;
    @FXML private ComboBox<Department> cbKhoa;

    private ClassUni classUni;
    private boolean saveClicked = false;
    private boolean isEdit = false;

    @FXML
    public void initialize() {
        cbNganh.setItems(DataService.getInstance().getMajors());
        cbNganh.setPromptText("Chọn ngành");

        cbKhoa.setItems(DataService.getInstance().getDepartments());
        cbKhoa.setPromptText("Chọn khoa");
    }

    public void setClassUni(ClassUni cls, boolean isEdit) {
        this.classUni = (cls != null) ? cls : new ClassUni();
        this.isEdit = isEdit;

        if (isEdit) {
            txtMaLop.setText(this.classUni.getMaLop());
            txtMaLop.setEditable(false);
            txtTenLop.setText(this.classUni.getTenLop());
            txtNienKhoa.setText(this.classUni.getNienKhoa());

            if (this.classUni.getMaNganh() != null) {
                for (Major major : cbNganh.getItems()) {
                    if (this.classUni.getMaNganh().equals(major.getMaNganh())) {
                        cbNganh.setValue(major);
                        break;
                    }
                }
            }

            if (this.classUni.getMaKhoa() != null) {
                for (Department dep : cbKhoa.getItems()) {
                    if (this.classUni.getMaKhoa().equals(dep.getMaKhoa())) {
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
        if (classUni == null) {
            classUni = new ClassUni();
        }

        String maLop = txtMaLop.getText() != null ? txtMaLop.getText().trim() : "";
        String tenLop = txtTenLop.getText() != null ? txtTenLop.getText().trim() : "";
        String nienKhoa = txtNienKhoa.getText() != null ? txtNienKhoa.getText().trim() : "";
        Major nganh = cbNganh.getValue();
        Department khoa = cbKhoa.getValue();

        if (maLop.isBlank() || tenLop.isBlank() || nienKhoa.isBlank()) {
            new Alert(Alert.AlertType.WARNING, "Vui lòng nhập Mã lớp, Tên lớp và Niên khóa!").showAndWait();
            return;
        }

        if (!nienKhoa.matches("\\d{4}-\\d{4}")) {
            new Alert(Alert.AlertType.WARNING, "Niên khóa phải có định dạng yyyy-yyyy, ví dụ: 2023-2027").showAndWait();
            return;
        }

        if (!isEdit && DataService.getInstance().getClasses().stream()
                .anyMatch(c -> maLop.equalsIgnoreCase(c.getMaLop()))) {
            new Alert(Alert.AlertType.WARNING, "Mã lớp đã tồn tại!").showAndWait();
            return;
        }

        if (nganh == null) {
            new Alert(Alert.AlertType.WARNING, "Vui lòng chọn ngành!").showAndWait();
            return;
        }

        if (khoa == null) {
            new Alert(Alert.AlertType.WARNING, "Vui lòng chọn khoa!").showAndWait();
            return;
        }

        classUni.setMaLop(maLop);
        classUni.setTenLop(tenLop);
        classUni.setMaKhoa(khoa.getMaKhoa());
        classUni.setNienKhoa(nienKhoa);
        classUni.setMaNganh(nganh.getMaNganh());

        saveClicked = true;
        closeStage();
    }

    @FXML
    private void handleCancel() {
        closeStage();
    }

    private void closeStage() {
        ((Stage) txtMaLop.getScene().getWindow()).close();
    }
}