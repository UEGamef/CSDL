package model;

import javafx.beans.property.*;
import service.DataService;

public class Grade {
    private final StringProperty maSV = new SimpleStringProperty(this, "maSV");
    private final StringProperty maMonHoc = new SimpleStringProperty(this, "maMonHoc");
    private final StringProperty hocKy = new SimpleStringProperty(this, "hocKy");

    private final DoubleProperty diemChuyenCan = new SimpleDoubleProperty(this, "diemChuyenCan", 0.0);
    private final DoubleProperty diemQuaTrinh = new SimpleDoubleProperty(this, "diemQuaTrinh", 0.0);
    private final DoubleProperty diemCuoiKy = new SimpleDoubleProperty(this, "diemCuoiKy", 0.0);

    public Grade() {}

    public Grade(String maSV, String maMon, String hocKy, double dcc, double dqt, double dck) {
        setMaSV(maSV);
        setMaMonHoc(maMon);
        setHocKy(hocKy);
        setDiemChuyenCan(dcc);
        setDiemQuaTrinh(dqt);
        setDiemCuoiKy(dck);
    }

    public StringProperty maSVProperty() { return maSV; }
    public String getMaSV() { return maSV.get(); }
    public void setMaSV(String value) { maSV.set(value); }

    public StringProperty maMonHocProperty() { return maMonHoc; }
    public String getMaMonHoc() { return maMonHoc.get(); }
    public void setMaMonHoc(String value) { maMonHoc.set(value); }

    public StringProperty hocKyProperty() { return hocKy; }
    public String getHocKy() { return hocKy.get(); }
    public void setHocKy(String value) { hocKy.set(value); }

    public DoubleProperty diemChuyenCanProperty() { return diemChuyenCan; }
    public double getDiemChuyenCan() { return diemChuyenCan.get(); }
    public void setDiemChuyenCan(double value) {
        if (value >= 0 && value <= 10) diemChuyenCan.set(value);
    }

    public DoubleProperty diemQuaTrinhProperty() { return diemQuaTrinh; }
    public double getDiemQuaTrinh() { return diemQuaTrinh.get(); }
    public void setDiemQuaTrinh(double value) {
        if (value >= 0 && value <= 10) diemQuaTrinh.set(value);
    }

    public DoubleProperty diemCuoiKyProperty() { return diemCuoiKy; }
    public double getDiemCuoiKy() { return diemCuoiKy.get(); }
    public void setDiemCuoiKy(double value) {
        if (value >= 0 && value <= 10) diemCuoiKy.set(value);
    }

    public double getDiemTongKet() {
        Subject s = DataService.getInstance().findSubjectById(getMaMonHoc());
        if (s == null) {
            return (getDiemChuyenCan() * 0.1)
                    + (getDiemQuaTrinh() * 0.3)
                    + (getDiemCuoiKy() * 0.6);
        }
        return (getDiemChuyenCan() * s.getTrongSoChuyenCan())
                + (getDiemQuaTrinh() * s.getTrongSoQuaTrinh())
                + (getDiemCuoiKy() * s.getTrongSoCuoiKy());
    }

    public double getDiemHe4(double diem10) {
        if (diem10 >= 8.5) return 4.0;
        if (diem10 >= 7.0) return 3.0;
        if (diem10 >= 5.5) return 2.0;
        if (diem10 >= 4.0) return 1.0;
        return 0.0;
    }

    public String getDiemChu(double diem10) {
        if (diem10 >= 8.5) return "A";
        if (diem10 >= 8.0) return "B+";
        if (diem10 >= 7.0) return "B";
        if (diem10 >= 6.5) return "C+";
        if (diem10 >= 5.5) return "C";
        if (diem10 >= 5.0) return "D+";
        if (diem10 >= 4.0) return "D";
        return "F";
    }
}