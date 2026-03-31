package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.ClassUni;
import model.Department;
import model.Major;
import model.Student;
import service.DataService;

import java.time.LocalDate;

public class StudentDialogController {
    @FXML private TextField txtMaSV, txtHo, txtTen, txtGioiTinh, txtSdt, txtEmail, txtCccd, txtDiaChi;
    @FXML private DatePicker dpNgaySinh;
    @FXML private ComboBox<ClassUni> cbLop;
    @FXML private ComboBox<Major> cbNganh;
    @FXML private ComboBox<Department> cbKhoa;

    private Student student;
    private boolean saveClicked = false;
    private boolean isEdit = false;
    private boolean updating = false;

    @FXML
    public void initialize() {
        cbLop.setItems(DataService.getInstance().getClasses());
        cbLop.setPromptText("Chọn lớp");

        cbNganh.setItems(DataService.getInstance().getMajors());
        cbNganh.setPromptText("Chọn ngành");

        cbKhoa.setItems(DataService.getInstance().getDepartments());
        cbKhoa.setPromptText("Chọn khoa");

        cbLop.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (updating || newVal == null) return;

            cbNganh.setValue(findMajorById(newVal.getMaNganh()));
            cbKhoa.setValue(findDepartmentById(newVal.getMaKhoa()));
        });
    }

    public void setStudent(Student student, boolean isEdit) {
        this.student = student;
        this.isEdit = isEdit;
        updating = true;

        if (isEdit) {
            txtMaSV.setText(student.getMaSV());
            txtMaSV.setEditable(false);
            txtHo.setText(student.getHo());
            txtTen.setText(student.getTen());
            txtGioiTinh.setText(student.getGioiTinh());
            txtSdt.setText(student.getSoDienThoai());
            txtEmail.setText(student.getEmail());
            txtCccd.setText(student.getCccd());
            txtDiaChi.setText(student.getDiaChi());

            if (student.getNgaySinh() != null) {
                dpNgaySinh.setValue(student.getNgaySinh());
            }

            ClassUni foundClass = findClassById(student.getMaLop());
            if (foundClass != null) {
                cbLop.setValue(foundClass);
                cbNganh.setValue(findMajorById(foundClass.getMaNganh()));
                cbKhoa.setValue(findDepartmentById(foundClass.getMaKhoa()));
            }
        }

        updating = false;
    }

    public boolean isSaveClicked() {
        return saveClicked;
    }

    @FXML
    private void handleSave() {
        String maSV = txtMaSV.getText() != null ? txtMaSV.getText().trim() : "";
        String ho = txtHo.getText() != null ? txtHo.getText().trim() : "";
        String ten = txtTen.getText() != null ? txtTen.getText().trim() : "";
        String gioiTinh = txtGioiTinh.getText() != null ? txtGioiTinh.getText().trim() : "";
        String sdt = txtSdt.getText() != null ? txtSdt.getText().trim() : "";
        String email = txtEmail.getText() != null ? txtEmail.getText().trim() : "";
        String cccd = txtCccd.getText() != null ? txtCccd.getText().trim() : "";
        String diaChi = txtDiaChi.getText() != null ? txtDiaChi.getText().trim() : "";
        LocalDate ngaySinh = dpNgaySinh.getValue();

        ClassUni lop = cbLop.getValue();
        Major nganh = cbNganh.getValue();
        Department khoa = cbKhoa.getValue();

        if (maSV.isBlank() || ho.isBlank() || ten.isBlank() || lop == null || nganh == null || khoa == null) {
            new Alert(Alert.AlertType.WARNING, "Vui lòng nhập đầy đủ thông tin bắt buộc!").showAndWait();
            return;
        }

        if (!email.isBlank() && !email.matches("^[\\w.-]+@[\\w.-]+\\.[A-Za-z]{2,}$")) {
            new Alert(Alert.AlertType.WARNING, "Email không hợp lệ!").showAndWait();
            return;
        }

        student.setMaSV(maSV);
        student.setHo(ho);
        student.setTen(ten);
        student.setGioiTinh(gioiTinh);
        student.setSoDienThoai(sdt);
        student.setEmail(email);
        student.setCccd(cccd);
        student.setDiaChi(diaChi);
        student.setNgaySinh(ngaySinh);
        student.setMaLop(lop.getMaLop());
        student.setMaNganh(nganh.getMaNganh());
        student.setMaKhoa(khoa.getMaKhoa());

        saveClicked = true;
        closeStage();
    }

    @FXML
    private void handleCancel() {
        closeStage();
    }

    private ClassUni findClassById(String maLop) {
        if (maLop == null) return null;
        for (ClassUni cls : cbLop.getItems()) {
            if (maLop.equals(cls.getMaLop())) return cls;
        }
        return null;
    }

    private Major findMajorById(String maNganh) {
        if (maNganh == null) return null;
        for (Major major : cbNganh.getItems()) {
            if (maNganh.equals(major.getMaNganh())) return major;
        }
        return null;
    }

    private Department findDepartmentById(String maKhoa) {
        if (maKhoa == null) return null;
        for (Department dept : cbKhoa.getItems()) {
            if (maKhoa.equals(dept.getMaKhoa())) return dept;
        }
        return null;
    }

    private void closeStage() {
        ((Stage) txtMaSV.getScene().getWindow()).close();
    }
}