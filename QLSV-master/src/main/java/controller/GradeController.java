package controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Grade;
import model.Student;
import model.Subject;
import service.DataService;
import service.GradeDao;

public class GradeController {
    @FXML private ComboBox<Student> cbStudent;
    @FXML private TableView<Grade> tableGrades;
    @FXML private TableColumn<Grade, String> colSubject, colSemester, colAttendance, colProcess, colExam, colFinal, colLetter;
    @FXML private Label lblGPA, lblRank;

    private final DataService dataService = DataService.getInstance();
    private final GradeDao gradeDao = new GradeDao();

    @FXML
    public void initialize() {
        dataService.getGrades().setAll(gradeDao.findAll());
        cbStudent.setItems(dataService.getStudents());

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

        cbStudent.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                updateGradeTable(newVal.getMaSV());
            } else {
                tableGrades.getItems().clear();
                lblGPA.setText("GPA Hệ 4: 0.00");
                lblRank.setText("Xếp loại: ---");
            }
        });

        tableGrades.setItems(dataService.getGrades());
    }

    private void updateGradeTable(String maSV) {
        tableGrades.setItems(dataService.getGradesByStudent(maSV));
        calculateGPA(maSV);
    }

    private void calculateGPA(String maSV) {
        double totalPoints = 0;
        int totalCredits = 0;

        for (Grade g : dataService.getGradesByStudent(maSV)) {
            Subject s = dataService.findSubjectById(g.getMaMonHoc());
            if (s != null) {
                double diem10 = g.getDiemTongKet();
                totalPoints += g.getDiemHe4(diem10) * s.getSoTinChi();
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

    @FXML
    private void handleAddNewGrade() {
        Student selectedStudent = cbStudent.getSelectionModel().getSelectedItem();
        if (selectedStudent == null) {
            new Alert(Alert.AlertType.WARNING, "Vui lòng chọn sinh viên trước!").showAndWait();
            return;
        }

        Grade newGrade = new Grade();
        newGrade.setMaSV(selectedStudent.getMaSV());

        if (showGradeDialog(newGrade, false)) {
            if (gradeDao.insert(newGrade)) {
                dataService.getGrades().add(newGrade);
                updateGradeTable(selectedStudent.getMaSV());
            } else {
                new Alert(Alert.AlertType.WARNING, "Không thể thêm điểm!").showAndWait();
            }
        }
    }

    @FXML
    private void handleEditGrade() {
        Grade selected = tableGrades.getSelectionModel().getSelectedItem();
        if (selected == null) {
            new Alert(Alert.AlertType.WARNING, "Vui lòng chọn một dòng điểm để sửa!").showAndWait();
            return;
        }

        if (showGradeDialog(selected, true)) {
            if (gradeDao.update(selected)) {
                tableGrades.refresh();
                Student currentStudent = cbStudent.getValue();
                if (currentStudent != null) {
                    updateGradeTable(currentStudent.getMaSV());
                }
            } else {
                new Alert(Alert.AlertType.WARNING, "Không thể cập nhật điểm!").showAndWait();
            }
        }
    }

    @FXML
    private void handleDeleteGrade() {
        Grade selected = tableGrades.getSelectionModel().getSelectedItem();
        if (selected == null) {
            new Alert(Alert.AlertType.WARNING, "Vui lòng chọn một dòng điểm để xóa!").showAndWait();
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Bạn có chắc muốn xóa dòng điểm này?",
                ButtonType.YES, ButtonType.NO);
        confirm.setHeaderText(null);

        confirm.showAndWait().ifPresent(result -> {
            if (result == ButtonType.YES) {
                if (gradeDao.deleteByKey(selected.getMaSV(), selected.getMaMonHoc(), selected.getHocKy())) {
                    dataService.getGrades().remove(selected);
                    Student currentStudent = cbStudent.getValue();
                    if (currentStudent != null) {
                        updateGradeTable(currentStudent.getMaSV());
                    }
                } else {
                    new Alert(Alert.AlertType.WARNING, "Không thể xóa điểm!").showAndWait();
                }
            }
        });
    }

    private boolean showGradeDialog(Grade grade, boolean isEdit) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/GradeDialog.fxml"));
            Stage stage = new Stage();
            stage.setTitle(isEdit ? "Sửa điểm" : "Nhập điểm mới");
            stage.setScene(new Scene(loader.load()));
            stage.initModality(javafx.stage.Modality.APPLICATION_MODAL);

            GradeDialogController controller = loader.getController();
            controller.setGrade(grade, isEdit);

            stage.showAndWait();
            return controller.isSaveClicked();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}