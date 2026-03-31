package controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.Grade;
import model.Student;
import model.Subject;
import service.DataService;
import service.GradeDao;
import service.SessionManager;

public class StudentGradeLookupController {
    @FXML private TextField txtMaSV;
    @FXML private Label lblGPA, lblRank;
    @FXML private TableView<Grade> tableGrades;
    @FXML private TableColumn<Grade, String> colSubject, colSemester, colAttendance, colProcess, colExam, colFinal, colLetter;
    @FXML private Button btnSearch;

    private final DataService dataService = DataService.getInstance();
    private final GradeDao gradeDao = new GradeDao();

    @FXML
    public void initialize() {
        dataService.getGrades().setAll(gradeDao.findAll());

        colSubject.setCellValueFactory(d -> {
            Subject s = dataService.findSubjectById(d.getValue().getMaMonHoc());
            return new SimpleStringProperty(s != null ? s.getTenMonHoc() : "N/A");
        });
        colSemester.setCellValueFactory(d -> d.getValue().hocKyProperty());
        colAttendance.setCellValueFactory(d -> d.getValue().diemChuyenCanProperty().asString("%.1f"));
        colProcess.setCellValueFactory(d -> d.getValue().diemQuaTrinhProperty().asString("%.1f"));
        colExam.setCellValueFactory(d -> d.getValue().diemCuoiKyProperty().asString("%.1f"));
        colFinal.setCellValueFactory(d -> new SimpleStringProperty(String.format("%.1f", d.getValue().getDiemTongKet())));
        colLetter.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getDiemChu(d.getValue().getDiemTongKet())));

        SessionManager session = SessionManager.getInstance();
        if (session.isStudent()) {
            String studentId = session.getStudentId();
            if (studentId != null && !studentId.isBlank()) {
                txtMaSV.setText(studentId);
                txtMaSV.setEditable(false);
                if (btnSearch != null) {
                    btnSearch.setDisable(true);
                }
                loadGradesByStudentId(studentId);
            }
        }
    }

    @FXML
    private void handleSearch() {
        String maSV = txtMaSV.getText() != null ? txtMaSV.getText().trim() : "";
        if (maSV.isBlank()) {
            showAlert("Vui lòng nhập Mã SV!");
            return;
        }

        Student student = dataService.getStudents().stream()
                .filter(s -> s.getMaSV() != null && s.getMaSV().equalsIgnoreCase(maSV))
                .findFirst()
                .orElse(null);

        if (student == null) {
            tableGrades.getItems().clear();
            lblGPA.setText("GPA Hệ 4: 0.00");
            lblRank.setText("Xếp loại: ---");
            showAlert("Không tìm thấy sinh viên!");
            return;
        }

        loadGradesByStudentId(maSV);
    }

    private void loadGradesByStudentId(String maSV) {
        FilteredList<Grade> filtered = new FilteredList<>(dataService.getGradesByStudent(maSV), p -> true);
        tableGrades.setItems(filtered);
        calculateGPA(maSV);
    }

    private void calculateGPA(String maSV) {
        double totalPoints = 0;
        int totalCredits = 0;

        for (Grade g : dataService.getGradesByStudent(maSV)) {
            Subject s = dataService.findSubjectById(g.getMaMonHoc());
            if (s != null) {
                totalPoints += g.getDiemHe4(g.getDiemTongKet()) * s.getSoTinChi();
                totalCredits += s.getSoTinChi();
            }
        }

        double gpa = totalCredits > 0 ? totalPoints / totalCredits : 0.0;
        lblGPA.setText(String.format("GPA Hệ 4: %.2f", gpa));

        if (gpa >= 3.6) lblRank.setText("Xếp loại: Xuất sắc");
        else if (gpa >= 3.2) lblRank.setText("Xếp loại: Giỏi");
        else if (gpa >= 2.5) lblRank.setText("Xếp loại: Khá");
        else lblRank.setText("Xếp loại: Trung bình/Yếu");
    }

    private void showAlert(String message) {
        new Alert(Alert.AlertType.WARNING, message).showAndWait();
    }
}