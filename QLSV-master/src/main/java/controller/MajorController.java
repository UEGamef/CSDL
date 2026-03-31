package controller;

import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Major;
import service.DataService;
import service.MajorDao;

public class MajorController {
    @FXML private TableView<Major> tableMajors;
    @FXML private TableColumn<Major, String> colMaNganh, colTenNganh, colMaKhoa, colMoTa;
    @FXML private TextField txtSearch;

    private final DataService dataService = DataService.getInstance();
    private final MajorDao majorDao = new MajorDao();

    @FXML
    public void initialize() {
        colMaNganh.setCellValueFactory(d -> d.getValue().maNganhProperty());
        colTenNganh.setCellValueFactory(d -> d.getValue().tenNganhProperty());
        colMaKhoa.setCellValueFactory(d -> d.getValue().maKhoaProperty());
        colMoTa.setCellValueFactory(d -> d.getValue().moTaProperty());

        dataService.getMajors().setAll(majorDao.findAll());

        FilteredList<Major> filteredData = new FilteredList<>(dataService.getMajors(), p -> true);
        txtSearch.textProperty().addListener((obs, oldVal, newVal) -> filteredData.setPredicate(m -> {
            if (newVal == null || newVal.isBlank()) return true;
            String filter = newVal.toLowerCase();
            return m.getMaNganh().toLowerCase().contains(filter)
                    || m.getTenNganh().toLowerCase().contains(filter)
                    || (m.getMaKhoa() != null && m.getMaKhoa().toLowerCase().contains(filter))
                    || (m.getMoTa() != null && m.getMoTa().toLowerCase().contains(filter));
        }));

        tableMajors.setItems(filteredData);
    }

    @FXML
    private void handleAddMajor() {
        Major major = new Major();
        if (showMajorDialog(major, false)) {
            if (majorDao.insert(major)) {
                dataService.getMajors().add(major);
                tableMajors.refresh();
            } else {
                showAlert("Không thể thêm ngành học!");
            }
        }
    }

    @FXML
    private void handleEditMajor() {
        Major selected = tableMajors.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Vui lòng chọn một ngành để sửa!");
            return;
        }

        if (showMajorDialog(selected, true)) {
            if (majorDao.update(selected)) {
                tableMajors.refresh();
            } else {
                showAlert("Không thể cập nhật ngành học!");
            }
        }
    }

    @FXML
    private void handleDeleteMajor() {
        Major selected = tableMajors.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Vui lòng chọn một ngành để xóa!");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Bạn có chắc muốn xóa ngành học này?",
                ButtonType.YES, ButtonType.NO);
        confirm.setHeaderText(null);

        confirm.showAndWait().ifPresent(result -> {
            if (result == ButtonType.YES) {
                if (majorDao.deleteById(selected.getMaNganh())) {
                    dataService.getMajors().remove(selected);
                    tableMajors.refresh();
                } else {
                    showAlert("Không thể xóa ngành học!");
                }
            }
        });
    }

    private boolean showMajorDialog(Major major, boolean isEdit) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MajorDialog.fxml"));
            Stage stage = new Stage();
            stage.setTitle(isEdit ? "Sửa ngành học" : "Thêm ngành học mới");
            stage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(loader.load()));

            MajorDialogController controller = loader.getController();
            controller.setMajor(major, isEdit);

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