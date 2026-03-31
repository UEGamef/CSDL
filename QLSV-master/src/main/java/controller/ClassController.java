package controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.ClassUni;
import service.ClassDao;
import service.DataService;

public class ClassController {
    @FXML private TableView<ClassUni> tableClasses;
    @FXML private TableColumn<ClassUni, String> colMaLop, colTenLop, colTenNganh, colTenKhoa, colNienKhoa;
    @FXML private TextField txtSearch;

    private final DataService dataService = DataService.getInstance();
    private final ClassDao classDao = new ClassDao();

    @FXML
    public void initialize() {
        colMaLop.setCellValueFactory(d -> d.getValue().maLopProperty());
        colTenLop.setCellValueFactory(d -> d.getValue().tenLopProperty());

        colTenNganh.setCellValueFactory(d -> {
            var major = dataService.getMajors().stream()
                    .filter(m -> m.getMaNganh() != null && m.getMaNganh().equals(d.getValue().getMaNganh()))
                    .findFirst()
                    .orElse(null);
            return new SimpleStringProperty(major != null ? major.getTenNganh() : d.getValue().getMaNganh());
        });

        colTenKhoa.setCellValueFactory(d -> {
            var dep = dataService.getDepartments().stream()
                    .filter(x -> x.getMaKhoa() != null && x.getMaKhoa().equals(d.getValue().getMaKhoa()))
                    .findFirst()
                    .orElse(null);
            return new SimpleStringProperty(dep != null ? dep.getTenKhoa() : d.getValue().getMaKhoa());
        });

        colNienKhoa.setCellValueFactory(d -> d.getValue().nienKhoaProperty());

        dataService.getClasses().setAll(classDao.findAll());

        FilteredList<ClassUni> filteredData = new FilteredList<>(dataService.getClasses(), p -> true);
        txtSearch.textProperty().addListener((obs, oldVal, newVal) -> filteredData.setPredicate(cls -> {
            if (newVal == null || newVal.isBlank()) return true;
            String filter = newVal.toLowerCase();

            String maLop = cls.getMaLop() != null ? cls.getMaLop().toLowerCase() : "";
            String tenLop = cls.getTenLop() != null ? cls.getTenLop().toLowerCase() : "";
            String nienKhoa = cls.getNienKhoa() != null ? cls.getNienKhoa().toLowerCase() : "";

            String tenNganh = dataService.getMajors().stream()
                    .filter(m -> m.getMaNganh() != null && m.getMaNganh().equals(cls.getMaNganh()))
                    .map(m -> m.getTenNganh() != null ? m.getTenNganh() : "")
                    .findFirst().orElse("");

            String tenKhoa = dataService.getDepartments().stream()
                    .filter(d -> d.getMaKhoa() != null && d.getMaKhoa().equals(cls.getMaKhoa()))
                    .map(d -> d.getTenKhoa() != null ? d.getTenKhoa() : "")
                    .findFirst().orElse("");

            return maLop.contains(filter)
                    || tenLop.contains(filter)
                    || tenNganh.toLowerCase().contains(filter)
                    || tenKhoa.toLowerCase().contains(filter)
                    || nienKhoa.contains(filter);
        }));

        tableClasses.setItems(filteredData);
    }

    @FXML
    private void handleAddClass() {
        ClassUni newClass = new ClassUni();
        if (showClassDialog(newClass, false)) {
            if (classDao.insert(newClass)) {
                dataService.getClasses().add(newClass);
                tableClasses.refresh();
            } else {
                showAlert("Không thể thêm lớp!");
            }
        }
    }

    @FXML
    private void handleEditClass() {
        ClassUni selected = tableClasses.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Vui lòng chọn một lớp để sửa!");
            return;
        }

        if (showClassDialog(selected, true)) {
            if (classDao.update(selected)) {
                tableClasses.refresh();
            } else {
                showAlert("Không thể cập nhật lớp!");
            }
        }
    }

    @FXML
    private void handleDeleteClass() {
        ClassUni selected = tableClasses.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Vui lòng chọn một lớp để xóa!");
            return;
        }

        boolean hasStudent = dataService.getStudents().stream()
                .anyMatch(s -> selected.getMaLop().equals(s.getMaLop()));

        if (hasStudent) {
            new Alert(Alert.AlertType.WARNING,
                    "Không thể xóa lớp này vì vẫn còn sinh viên thuộc lớp đó!").showAndWait();
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Bạn có chắc muốn xóa lớp này?",
                ButtonType.YES, ButtonType.NO);
        confirm.setHeaderText(null);

        confirm.showAndWait().ifPresent(result -> {
            if (result == ButtonType.YES) {
                if (classDao.deleteById(selected.getMaLop())) {
                    dataService.getClasses().remove(selected);
                    tableClasses.refresh();
                } else {
                    showAlert("Không thể xóa lớp!");
                }
            }
        });
    }

    private boolean showClassDialog(ClassUni cls, boolean isEdit) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ClassDialog.fxml"));
            Stage stage = new Stage();
            stage.setTitle(isEdit ? "Sửa lớp học" : "Thêm lớp học mới");
            stage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(loader.load()));

            ClassDialogController controller = loader.getController();
            controller.setClassUni(cls, isEdit);

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