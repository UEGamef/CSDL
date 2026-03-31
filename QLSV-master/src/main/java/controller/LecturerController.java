package controller;

import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Lecturer;
import service.DataService;
import service.LecturerDao;

public class LecturerController {
    @FXML private TableView<Lecturer> tableLecturers;
    @FXML private TableColumn<Lecturer, String> colMaGv, colHo, colTen, colGioiTinh, colSdt, colEmail, colHocVi, colDiaChi, colMaKhoa, colMaMonHoc;
    @FXML private TextField txtSearch;

    private final LecturerDao lecturerDao = new LecturerDao();

    @FXML
    public void initialize() {
        colMaGv.setCellValueFactory(d -> d.getValue().maGvProperty());
        colHo.setCellValueFactory(d -> d.getValue().hoProperty());
        colTen.setCellValueFactory(d -> d.getValue().tenProperty());
        colGioiTinh.setCellValueFactory(d -> d.getValue().gioiTinhProperty());
        colSdt.setCellValueFactory(d -> d.getValue().sdtProperty());
        colEmail.setCellValueFactory(d -> d.getValue().emailProperty());
        colHocVi.setCellValueFactory(d -> d.getValue().hocViProperty());
        colDiaChi.setCellValueFactory(d -> d.getValue().diaChiProperty());
        colMaKhoa.setCellValueFactory(d -> d.getValue().maKhoaProperty());
        colMaMonHoc.setCellValueFactory(d -> d.getValue().maMonHocProperty());

        DataService.getInstance().getLecturers().setAll(lecturerDao.findAll());

        FilteredList<Lecturer> filteredData = new FilteredList<>(DataService.getInstance().getLecturers(), p -> true);
        txtSearch.textProperty().addListener((obs, oldVal, newVal) -> filteredData.setPredicate(l -> {
            if (newVal == null || newVal.isBlank()) return true;
            String filter = newVal.toLowerCase();
            return l.getMaGv().toLowerCase().contains(filter)
                    || l.getHo().toLowerCase().contains(filter)
                    || l.getTen().toLowerCase().contains(filter)
                    || (l.getEmail() != null && l.getEmail().toLowerCase().contains(filter))
                    || (l.getSdt() != null && l.getSdt().toLowerCase().contains(filter));
        }));

        tableLecturers.setItems(filteredData);
    }

    @FXML
    private void handleAddLecturer() {
        Lecturer newLecturer = new Lecturer();
        if (showLecturerDialog(newLecturer, false)) {
            if (lecturerDao.insert(newLecturer)) {
                DataService.getInstance().getLecturers().add(newLecturer);
                tableLecturers.refresh();
            } else {
                showAlert("Không thể thêm giảng viên!");
            }
        }
    }

    @FXML
    private void handleEditLecturer() {
        Lecturer selected = tableLecturers.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Vui lòng chọn một giảng viên để sửa!");
            return;
        }

        if (showLecturerDialog(selected, true)) {
            if (lecturerDao.update(selected)) {
                tableLecturers.refresh();
            } else {
                showAlert("Không thể cập nhật giảng viên!");
            }
        }
    }

    @FXML
    private void handleDeleteLecturer() {
        Lecturer selected = tableLecturers.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Vui lòng chọn một giảng viên để xóa!");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Bạn có chắc muốn xóa giảng viên này?",
                ButtonType.YES, ButtonType.NO);
        confirm.setHeaderText(null);

        confirm.showAndWait().ifPresent(result -> {
            if (result == ButtonType.YES) {
                if (lecturerDao.deleteById(selected.getMaGv())) {
                    DataService.getInstance().getLecturers().remove(selected);
                    tableLecturers.refresh();
                } else {
                    showAlert("Không thể xóa giảng viên!");
                }
            }
        });
    }

    private boolean showLecturerDialog(Lecturer lecturer, boolean isEdit) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/LecturerDialog.fxml"));
            Stage stage = new Stage();
            stage.setTitle(isEdit ? "Sửa giảng viên" : "Thêm giảng viên mới");
            stage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(loader.load()));

            LecturerDialogController controller = loader.getController();
            controller.setLecturer(lecturer, isEdit);

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