package model;

import javafx.beans.property.*;

public class Subject {
    private final StringProperty maMonHoc = new SimpleStringProperty(this, "maMonHoc");
    private final StringProperty tenMonHoc = new SimpleStringProperty(this, "tenMonHoc");
    private final IntegerProperty soTinChi = new SimpleIntegerProperty(this, "soTinChi");

    private final DoubleProperty trongSoChuyenCan = new SimpleDoubleProperty(this, "trongSoChuyenCan");
    private final DoubleProperty trongSoQuaTrinh = new SimpleDoubleProperty(this, "trongSoQuaTrinh");
    private final DoubleProperty trongSoCuoiKy = new SimpleDoubleProperty(this, "trongSoCuoiKy");

    public Subject() {
        this("", "", 0, 0.1, 0.3, 0.6);
    }

    public Subject(String maMon, String tenMon, int tinChi, double tsCC, double tsQT, double tsCK) {
        setMaMonHoc(maMon);
        setTenMonHoc(tenMon);
        setSoTinChi(tinChi);
        setTrongSoChuyenCan(tsCC);
        setTrongSoQuaTrinh(tsQT);
        setTrongSoCuoiKy(tsCK);
    }

    public StringProperty maMonHocProperty() { return maMonHoc; }
    public String getMaMonHoc() { return maMonHoc.get(); }
    public void setMaMonHoc(String value) { maMonHoc.set(value); }

    public StringProperty tenMonHocProperty() { return tenMonHoc; }
    public String getTenMonHoc() { return tenMonHoc.get(); }
    public void setTenMonHoc(String value) { tenMonHoc.set(value); }

    public IntegerProperty soTinChiProperty() { return soTinChi; }
    public int getSoTinChi() { return soTinChi.get(); }
    public void setSoTinChi(int value) { soTinChi.set(value); }

    public DoubleProperty trongSoChuyenCanProperty() { return trongSoChuyenCan; }
    public double getTrongSoChuyenCan() { return trongSoChuyenCan.get(); }
    public void setTrongSoChuyenCan(double value) { trongSoChuyenCan.set(value); }

    public DoubleProperty trongSoQuaTrinhProperty() { return trongSoQuaTrinh; }
    public double getTrongSoQuaTrinh() { return trongSoQuaTrinh.get(); }
    public void setTrongSoQuaTrinh(double value) { trongSoQuaTrinh.set(value); }

    public DoubleProperty trongSoCuoiKyProperty() { return trongSoCuoiKy; }
    public double getTrongSoCuoiKy() { return trongSoCuoiKy.get(); }
    public void setTrongSoCuoiKy(double value) { trongSoCuoiKy.set(value); }

    @Override
    public String toString() {
        return getTenMonHoc();
    }
}