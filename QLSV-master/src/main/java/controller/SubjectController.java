package controller;

import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Subject;
import service.DataService;
import service.SubjectDao;

public class SubjectController {
    @FXML private TableView<Subject> tableSubjects;
    @FXML private TableColumn<Subject, String> colMaMon, colTenMon, colSoTinChi, colTrongSoCC, colTrongSoQT, colTrongSoCK;
    @FXML private TextField txtSearch;

    private final DataService dataService = DataService.getInstance();
    private final SubjectDao subjectDao = new SubjectDao();

    @FXML
    public void initialize() {
        dataService.getSubjects().setAll(subjectDao.findAll());

        colMaMon.setCellValueFactory(d -> d.getValue().maMonHocProperty());
        colTenMon.setCellValueFactory(d -> d.getValue().tenMonHocProperty());
        colSoTinChi.setCellValueFactory(d -> d.getValue().soTinChiProperty().asString());
        colTrongSoCC.setCellValueFactory(d -> d.getValue().trongSoChuyenCanProperty().asString("%.2f"));
        colTrongSoQT.setCellValueFactory(d -> d.getValue().trongSoQuaTrinhProperty().asString("%.2f"));
        colTrongSoCK.setCellValueFactory(d -> d.getValue().trongSoCuoiKyProperty().asString("%.2f"));

        FilteredList<Subject> filteredData = new FilteredList<>(dataService.getSubjects(), p -> true);
        txtSearch.textProperty().addListener((obs, oldVal, newVal) -> filteredData.setPredicate(sub -> {
            if (newVal == null || newVal.isBlank()) return true;
            String filter = newVal.toLowerCase();
            return sub.getMaMonHoc().toLowerCase().contains(filter)
                    || sub.getTenMonHoc().toLowerCase().contains(filter);
        }));

        tableSubjects.setItems(filteredData);
    }

    @FXML
    private void handleAddSubject() {
        Subject subject = new Subject();
        if (showSubjectDialog(subject, false)) {
            if (subjectDao.insert(subject)) {
                dataService.getSubjects().add(subject);
                tableSubjects.refresh();
            } else {
                showAlert("Không thể thêm môn học mới!");
            }
        }
    }

    @FXML
    private void handleEditSubject() {
        Subject selected = tableSubjects.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Vui lòng chọn một môn học để sửa!");
            return;
        }

        if (showSubjectDialog(selected, true)) {
            if (subjectDao.update(selected)) {
                tableSubjects.refresh();
            } else {
                showAlert("Không thể cập nhật môn học!");
            }
        }
    }

    @FXML
    private void handleDeleteSubject() {
        Subject selected = tableSubjects.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Vui lòng chọn một môn học để xóa!");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Bạn có chắc muốn xóa môn học này?",
                ButtonType.YES, ButtonType.NO);
        confirm.setHeaderText(null);

        confirm.showAndWait().ifPresent(result -> {
            if (result == ButtonType.YES) {
                if (subjectDao.deleteById(selected.getMaMonHoc())) {
                    dataService.getSubjects().remove(selected);
                    tableSubjects.refresh();
                } else {
                    showAlert("Không thể xóa môn học!");
                }
            }
        });
    }

    private boolean showSubjectDialog(Subject subject, boolean isEdit) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/SubjectDialog.fxml"));
            Stage stage = new Stage();
            stage.setTitle(isEdit ? "Sửa môn học" : "Thêm môn học mới");
            stage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(loader.load()));

            SubjectDialogController controller = loader.getController();
            controller.setSubject(subject, isEdit);

            stage.showAndWait();
            return controller.isSaveClicked();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void showAlert(String content) {
        new Alert(Alert.AlertType.WARNING, content).showAndWait();
    }
}