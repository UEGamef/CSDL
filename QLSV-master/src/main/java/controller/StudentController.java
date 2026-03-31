package controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.ClassUni;
import model.Department;
import model.Major;
import model.Student;
import service.DataService;
import service.StudentDao;

public class StudentController {
    @FXML private TableView<Student> tableStudents;
    @FXML private TableColumn<Student, String> colMaSV, colHo, colTen, colGioiTinh, colSdt, colEmail, colCccd, colNgaySinh, colDiaChi, colMaLop, colTenNganh, colTenKhoa;
    @FXML private TextField txtSearch;
    @FXML private ComboBox<ClassUni> cbFilterClass;

    private final DataService dataService = DataService.getInstance();
    private final StudentDao studentDao = new StudentDao();

    @FXML
    public void initialize() {
        colMaSV.setCellValueFactory(d -> d.getValue().maSVProperty());
        colHo.setCellValueFactory(d -> d.getValue().hoProperty());
        colTen.setCellValueFactory(d -> d.getValue().tenProperty());
        colGioiTinh.setCellValueFactory(d -> d.getValue().gioiTinhProperty());
        colSdt.setCellValueFactory(d -> d.getValue().soDienThoaiProperty());
        colEmail.setCellValueFactory(d -> d.getValue().emailProperty());
        colCccd.setCellValueFactory(d -> d.getValue().cccdProperty());
        colNgaySinh.setCellValueFactory(d -> d.getValue().ngaySinhProperty().asString());
        colDiaChi.setCellValueFactory(d -> d.getValue().diaChiProperty());
        colMaLop.setCellValueFactory(d -> d.getValue().maLopProperty());

        colTenNganh.setCellValueFactory(d -> {
            String maNganh = d.getValue().getMaNganh();
            Major major = dataService.getMajors().stream()
                    .filter(m -> m.getMaNganh() != null && m.getMaNganh().equals(maNganh))
                    .findFirst()
                    .orElse(null);
            return new SimpleStringProperty(major != null ? major.getTenNganh() : "");
        });

        colTenKhoa.setCellValueFactory(d -> {
            String maKhoa = d.getValue().getMaKhoa();
            Department dep = dataService.getDepartments().stream()
                    .filter(k -> k.getMaKhoa() != null && k.getMaKhoa().equals(maKhoa))
                    .findFirst()
                    .orElse(null);
            return new SimpleStringProperty(dep != null ? dep.getTenKhoa() : "");
        });

        cbFilterClass.setItems(dataService.getClasses());
        cbFilterClass.setPromptText("Tất cả lớp");

        dataService.getStudents().setAll(studentDao.findAll());

        FilteredList<Student> filteredData = new FilteredList<>(dataService.getStudents(), p -> true);

        txtSearch.textProperty().addListener((obs, oldVal, newVal) -> filteredData.setPredicate(s -> {
            if (newVal == null || newVal.isBlank()) return true;
            String filter = newVal.toLowerCase();
            return s.getMaSV().toLowerCase().contains(filter)
                    || s.getHo().toLowerCase().contains(filter)
                    || s.getTen().toLowerCase().contains(filter)
                    || (s.getEmail() != null && s.getEmail().toLowerCase().contains(filter))
                    || (s.getSoDienThoai() != null && s.getSoDienThoai().toLowerCase().contains(filter))
                    || (s.getCccd() != null && s.getCccd().toLowerCase().contains(filter));
        }));

        cbFilterClass.valueProperty().addListener((obs, oldVal, newVal) -> filteredData.setPredicate(s -> {
            boolean matchSearch = true;
            String search = txtSearch.getText();
            if (search != null && !search.isBlank()) {
                String filter = search.toLowerCase();
                matchSearch = s.getMaSV().toLowerCase().contains(filter)
                        || s.getHo().toLowerCase().contains(filter)
                        || s.getTen().toLowerCase().contains(filter)
                        || (s.getEmail() != null && s.getEmail().toLowerCase().contains(filter))
                        || (s.getSoDienThoai() != null && s.getSoDienThoai().toLowerCase().contains(filter))
                        || (s.getCccd() != null && s.getCccd().toLowerCase().contains(filter));
            }

            boolean matchClass = true;
            if (newVal != null) {
                matchClass = newVal.getMaLop().equals(s.getMaLop());
            }

            return matchSearch && matchClass;
        }));

        tableStudents.setItems(filteredData);
    }

    @FXML
    private void handleAddStudent() {
        Student newStudent = new Student();
        if (showStudentDialog(newStudent, false)) {
            if (studentDao.insert(newStudent)) {
                updateStudentClassInfo(newStudent);
                dataService.getStudents().add(newStudent);
                tableStudents.refresh();
            } else {
                showAlert("Không thể thêm sinh viên!");
            }
        }
    }

    @FXML
    private void handleEditStudent() {
        Student selected = tableStudents.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Vui lòng chọn một sinh viên để sửa!");
            return;
        }

        if (showStudentDialog(selected, true)) {
            if (studentDao.update(selected)) {
                updateStudentClassInfo(selected);
                tableStudents.refresh();
            } else {
                showAlert("Không thể cập nhật sinh viên!");
            }
        }
    }

    @FXML
    private void handleDeleteStudent() {
        Student selected = tableStudents.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Vui lòng chọn một sinh viên để xóa!");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Bạn có chắc muốn xóa sinh viên này?",
                ButtonType.YES, ButtonType.NO);
        confirm.setHeaderText(null);

        confirm.showAndWait().ifPresent(result -> {
            if (result == ButtonType.YES) {
                if (studentDao.deleteById(selected.getMaSV())) {
                    dataService.getStudents().remove(selected);
                    tableStudents.refresh();
                } else {
                    showAlert("Không thể xóa sinh viên!");
                }
            }
        });
    }

    private boolean showStudentDialog(Student student, boolean isEdit) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/StudentDialog.fxml"));
            Stage stage = new Stage();
            stage.setTitle(isEdit ? "Sửa sinh viên" : "Thêm sinh viên mới");
            stage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(loader.load()));

            StudentDialogController controller = loader.getController();
            controller.setStudent(student, isEdit);

            stage.showAndWait();
            return controller.isSaveClicked();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void updateStudentClassInfo(Student student) {
        for (ClassUni cls : dataService.getClasses()) {
            if (cls.getMaLop().equals(student.getMaLop())) {
                student.setMaNganh(cls.getMaNganh());
                student.setMaKhoa(cls.getMaKhoa());
                break;
            }
        }
    }

    private void showAlert(String content) {
        new Alert(Alert.AlertType.WARNING, content).showAndWait();
    }
}