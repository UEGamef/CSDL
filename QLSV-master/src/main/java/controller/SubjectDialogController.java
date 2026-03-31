package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Subject;
import service.DataService;

public class SubjectDialogController {
    @FXML private TextField txtMaMonHoc, txtTenMonHoc, txtSoTinChi, txtTrongSoCC, txtTrongSoQT, txtTrongSoCK;

    private Subject subject;
    private boolean saveClicked = false;
    private boolean isEdit = false;

    public void setSubject(Subject subject, boolean isEdit) {
        this.subject = subject;
        this.isEdit = isEdit;

        if (isEdit) {
            txtMaMonHoc.setText(subject.getMaMonHoc());
            txtMaMonHoc.setEditable(false);
            txtTenMonHoc.setText(subject.getTenMonHoc());
            txtSoTinChi.setText(String.valueOf(subject.getSoTinChi()));
            txtTrongSoCC.setText(String.valueOf(subject.getTrongSoChuyenCan()));
            txtTrongSoQT.setText(String.valueOf(subject.getTrongSoQuaTrinh()));
            txtTrongSoCK.setText(String.valueOf(subject.getTrongSoCuoiKy()));
        }
    }

    public boolean isSaveClicked() { return saveClicked; }

    @FXML
    private void handleSave() {
        if (txtMaMonHoc.getText() == null || txtMaMonHoc.getText().isBlank()
                || txtTenMonHoc.getText() == null || txtTenMonHoc.getText().isBlank()) {
            new Alert(Alert.AlertType.WARNING, "Vui lòng nhập ít nhất Mã môn và Tên môn!").showAndWait();
            return;
        }

        String maMon = txtMaMonHoc.getText().trim();

        if (!isEdit && DataService.getInstance().getSubjects().stream().anyMatch(s -> maMon.equals(s.getMaMonHoc()))) {
            new Alert(Alert.AlertType.WARNING, "Mã môn học đã tồn tại!").showAndWait();
            return;
        }

        try {
            int soTinChi = Integer.parseInt(txtSoTinChi.getText().trim());
            double tsCC = Double.parseDouble(txtTrongSoCC.getText().trim());
            double tsQT = Double.parseDouble(txtTrongSoQT.getText().trim());
            double tsCK = Double.parseDouble(txtTrongSoCK.getText().trim());

            double sum = tsCC + tsQT + tsCK;
            if (Math.abs(sum - 1.0) > 0.0001) {
                new Alert(Alert.AlertType.WARNING, "Tổng trọng số phải bằng 1.0!").showAndWait();
                return;
            }

            subject.setMaMonHoc(maMon);
            subject.setTenMonHoc(txtTenMonHoc.getText().trim());
            subject.setSoTinChi(soTinChi);
            subject.setTrongSoChuyenCan(tsCC);
            subject.setTrongSoQuaTrinh(tsQT);
            subject.setTrongSoCuoiKy(tsCK);

            saveClicked = true;
            closeStage();
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Số tín chỉ và trọng số phải là số hợp lệ!").showAndWait();
        }
    }

    @FXML private void handleCancel() { closeStage(); }
    private void closeStage() { ((Stage) txtMaMonHoc.getScene().getWindow()).close(); }
}