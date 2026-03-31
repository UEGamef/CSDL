package controller;

import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Department;
import service.DataService;
import service.DepartmentDao;

public class DepartmentController {
    @FXML private TableView<Department> tableDepartments;
    @FXML private TableColumn<Department, String> colMaKhoa, colTenKhoa, colSDT, colEmail, colVanPhong;
    @FXML private TextField txtSearch;

    private final DataService dataService = DataService.getInstance();
    private final DepartmentDao departmentDao = new DepartmentDao();

    @FXML
    public void initialize() {
        colMaKhoa.setCellValueFactory(d -> d.getValue().maKhoaProperty());
        colTenKhoa.setCellValueFactory(d -> d.getValue().tenKhoaProperty());
        colSDT.setCellValueFactory(d -> d.getValue().soDienThoaiProperty());
        colEmail.setCellValueFactory(d -> d.getValue().emailProperty());
        colVanPhong.setCellValueFactory(d -> d.getValue().vanPhongProperty());

        dataService.getDepartments().setAll(departmentDao.findAll());

        FilteredList<Department> filteredData = new FilteredList<>(dataService.getDepartments(), p -> true);
        txtSearch.textProperty().addListener((obs, oldVal, newVal) -> filteredData.setPredicate(dep -> {
            if (newVal == null || newVal.isBlank()) return true;
            String filter = newVal.toLowerCase();
            return dep.getMaKhoa().toLowerCase().contains(filter)
                    || dep.getTenKhoa().toLowerCase().contains(filter)
                    || dep.getSoDienThoai().toLowerCase().contains(filter)
                    || dep.getEmail().toLowerCase().contains(filter)
                    || dep.getVanPhong().toLowerCase().contains(filter);
        }));

        tableDepartments.setItems(filteredData);
    }

    @FXML
    private void handleAddDepartment() {
        Department newDepartment = new Department();
        if (showDepartmentDialog(newDepartment, false)) {
            if (departmentDao.insert(newDepartment)) {
                dataService.getDepartments().add(newDepartment);
                tableDepartments.refresh();
            } else {
                showAlert("Không thể thêm khoa mới!");
            }
        }
    }

    @FXML
    private void handleEditDepartment() {
        Department selected = tableDepartments.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Vui lòng chọn một khoa để sửa!");
            return;
        }

        if (showDepartmentDialog(selected, true)) {
            if (departmentDao.update(selected)) {
                tableDepartments.refresh();
            } else {
                showAlert("Không thể cập nhật khoa!");
            }
        }
    }

    @FXML
    private void handleDeleteDepartment() {
        Department selected = tableDepartments.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Vui lòng chọn một khoa để xóa!");
            return;
        }

        boolean inUse = dataService.getClasses().stream()
                .anyMatch(c -> selected.getTenKhoa().equals(c.getMaKhoa()));

        if (inUse) {
            new Alert(Alert.AlertType.WARNING,
                    "Không thể xóa khoa này vì vẫn còn lớp thuộc khoa đó!").showAndWait();
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Bạn có chắc muốn xóa khoa này?",
                ButtonType.YES, ButtonType.NO);
        confirm.setHeaderText(null);

        confirm.showAndWait().ifPresent(result -> {
            if (result == ButtonType.YES) {
                if (departmentDao.deleteById(selected.getMaKhoa())) {
                    dataService.getDepartments().remove(selected);
                    tableDepartments.refresh();
                } else {
                    showAlert("Không thể xóa khoa!");
                }
            }
        });
    }

    private boolean showDepartmentDialog(Department dep, boolean isEdit) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/DepartmentDialog.fxml"));
            Stage stage = new Stage();
            stage.setTitle(isEdit ? "Sửa khoa" : "Thêm khoa mới");
            stage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(loader.load()));

            DepartmentDialogController controller = loader.getController();
            controller.setDepartment(dep, isEdit);

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