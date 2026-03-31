package model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Major {
    private final StringProperty maNganh = new SimpleStringProperty(this, "maNganh");
    private final StringProperty tenNganh = new SimpleStringProperty(this, "tenNganh");
    private final StringProperty maKhoa = new SimpleStringProperty(this, "maKhoa");
    private final StringProperty moTa = new SimpleStringProperty(this, "moTa");

    public Major() {
    }

    public Major(String maNganh, String tenNganh, String maKhoa, String moTa) {
        setMaNganh(maNganh);
        setTenNganh(tenNganh);
        setMaKhoa(maKhoa);
        setMoTa(moTa);
    }

    public StringProperty maNganhProperty() { return maNganh; }
    public String getMaNganh() { return maNganh.get(); }
    public void setMaNganh(String value) { maNganh.set(value); }

    public StringProperty tenNganhProperty() { return tenNganh; }
    public String getTenNganh() { return tenNganh.get(); }
    public void setTenNganh(String value) { tenNganh.set(value); }

    public StringProperty maKhoaProperty() { return maKhoa; }
    public String getMaKhoa() { return maKhoa.get(); }
    public void setMaKhoa(String value) { maKhoa.set(value); }

    public StringProperty moTaProperty() { return moTa; }
    public String getMoTa() { return moTa.get(); }
    public void setMoTa(String value) { moTa.set(value); }

    @Override
    public String toString() {
        return getTenNganh();
    }
}