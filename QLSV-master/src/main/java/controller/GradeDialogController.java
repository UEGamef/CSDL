package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Grade;
import model.Subject;
import service.DataService;

public class GradeDialogController {
    @FXML private ComboBox<Subject> cbSubject;
    @FXML private TextField txtSemester, txtAttendance, txtProcess, txtExam;

    private Grade grade;
    private boolean saveClicked = false;
    private final DataService dataService = DataService.getInstance();

    @FXML
    public void initialize() {
        cbSubject.setItems(dataService.getSubjects());
        cbSubject.setPromptText("Chọn môn học");
    }

    public void setGrade(Grade grade, boolean isEdit) {
        this.grade = grade;

        if (isEdit) {
            cbSubject.setValue(dataService.findSubjectById(grade.getMaMonHoc()));
            cbSubject.setDisable(true);
            txtSemester.setText(grade.getHocKy());
            txtAttendance.setText(String.valueOf(grade.getDiemChuyenCan()));
            txtProcess.setText(String.valueOf(grade.getDiemQuaTrinh()));
            txtExam.setText(String.valueOf(grade.getDiemCuoiKy()));
        }
    }

    public boolean isSaveClicked() {
        return saveClicked;
    }

    @FXML
    private void handleSave() {
        Subject subject = cbSubject.getValue();
        String hocKy = txtSemester.getText() != null ? txtSemester.getText().trim() : "";
        String attendanceText = txtAttendance.getText() != null ? txtAttendance.getText().trim() : "";
        String processText = txtProcess.getText() != null ? txtProcess.getText().trim() : "";
        String examText = txtExam.getText() != null ? txtExam.getText().trim() : "";

        if (subject == null || hocKy.isBlank() || attendanceText.isBlank()
                || processText.isBlank() || examText.isBlank()) {
            new Alert(Alert.AlertType.WARNING, "Vui lòng nhập đầy đủ thông tin điểm!").showAndWait();
            return;
        }

        if (!hocKy.matches("\\d{4}\\.\\d+")) {
            new Alert(Alert.AlertType.WARNING, "Học kỳ phải đúng định dạng, ví dụ: 2023.1").showAndWait();
            return;
        }

        try {
            double attendance = Double.parseDouble(attendanceText);
            double process = Double.parseDouble(processText);
            double exam = Double.parseDouble(examText);

            if (!isValidScore(attendance) || !isValidScore(process) || !isValidScore(exam)) {
                new Alert(Alert.AlertType.WARNING, "Điểm phải nằm trong khoảng từ 0 đến 10!").showAndWait();
                return;
            }

            grade.setMaMonHoc(subject.getMaMonHoc());
            grade.setHocKy(hocKy);
            grade.setDiemChuyenCan(attendance);
            grade.setDiemQuaTrinh(process);
            grade.setDiemCuoiKy(exam);

            saveClicked = true;
            closeStage();
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Các điểm phải là số thực hợp lệ!").showAndWait();
        }
    }

    private boolean isValidScore(double value) {
        return value >= 0.0 && value <= 10.0;
    }

    @FXML
    private void handleCancel() {
        closeStage();
    }

    private void closeStage() {
        ((Stage) txtSemester.getScene().getWindow()).close();
    }
}