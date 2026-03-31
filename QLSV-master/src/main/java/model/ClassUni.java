package model;

import javafx.beans.property.*;

public class ClassUni {
    private final StringProperty maLop = new SimpleStringProperty(this, "maLop");
    private final StringProperty tenLop = new SimpleStringProperty(this, "tenLop");
    private final StringProperty maNganh = new SimpleStringProperty(this, "maNganh");
    private final StringProperty maKhoa = new SimpleStringProperty(this, "maKhoa");
    private final StringProperty nienKhoa = new SimpleStringProperty(this, "nienKhoa");

    public ClassUni() {
    }

    public ClassUni(String maLop, String tenLop, String maNganh, String maKhoa, String nienKhoa) {
        setMaLop(maLop);
        setTenLop(tenLop);
        setMaNganh(maNganh);
        setMaKhoa(maKhoa);
        setNienKhoa(nienKhoa);
    }

    public StringProperty maLopProperty() { return maLop; }
    public String getMaLop() { return maLop.get(); }
    public void setMaLop(String value) { maLop.set(value); }

    public StringProperty tenLopProperty() { return tenLop; }
    public String getTenLop() { return tenLop.get(); }
    public void setTenLop(String value) { tenLop.set(value); }

    public StringProperty maNganhProperty() { return maNganh; }
    public String getMaNganh() { return maNganh.get(); }
    public void setMaNganh(String value) { maNganh.set(value); }

    public StringProperty maKhoaProperty() { return maKhoa; }
    public String getMaKhoa() { return maKhoa.get(); }
    public void setMaKhoa(String value) { maKhoa.set(value); }

    public StringProperty nienKhoaProperty() { return nienKhoa; }
    public String getNienKhoa() { return nienKhoa.get(); }
    public void setNienKhoa(String value) { nienKhoa.set(value); }

    @Override
    public String toString() {
        return String.format("%s (%s)", getTenLop(), getMaLop());
    }
}