
package model;

import javafx.beans.property.*;

public class Lecturer {
    private final StringProperty maGv = new SimpleStringProperty(this, "maGv");
    private final StringProperty ho = new SimpleStringProperty(this, "ho");
    private final StringProperty ten = new SimpleStringProperty(this, "ten");
    private final StringProperty gioiTinh = new SimpleStringProperty(this, "gioiTinh");
    private final StringProperty sdt = new SimpleStringProperty(this, "sdt");
    private final StringProperty email = new SimpleStringProperty(this, "email");
    private final StringProperty hocVi = new SimpleStringProperty(this, "hocVi");
    private final StringProperty diaChi = new SimpleStringProperty(this, "diaChi");
    private final StringProperty maKhoa = new SimpleStringProperty(this, "maKhoa");
    private final StringProperty maMonHoc = new SimpleStringProperty(this, "maMonHoc");

    public Lecturer() {
    }

    public Lecturer(String maGv, String ho, String ten, String gioiTinh, String sdt, String email, String hocVi, String diaChi, String maKhoa) {
        setMaGv(maGv);
        setHo(ho);
        setTen(ten);
        setGioiTinh(gioiTinh);
        setSdt(sdt);
        setEmail(email);
        setHocVi(hocVi);
        setDiaChi(diaChi);
        setMaKhoa(maKhoa);
    }

    public Lecturer(String maGv, String ho, String ten, String gioiTinh, String sdt, String email, String hocVi, String diaChi, String maKhoa, String maMonHoc) {
        this(maGv, ho, ten, gioiTinh, sdt, email, hocVi, diaChi, maKhoa);
        setMaMonHoc(maMonHoc);
    }

    public StringProperty maGvProperty() { return maGv; }
    public String getMaGv() { return maGv.get(); }
    public void setMaGv(String value) { maGv.set(value); }

    public StringProperty hoProperty() { return ho; }
    public String getHo() { return ho.get(); }
    public void setHo(String value) { ho.set(value); }

    public StringProperty tenProperty() { return ten; }
    public String getTen() { return ten.get(); }
    public void setTen(String value) { ten.set(value); }

    public StringProperty gioiTinhProperty() { return gioiTinh; }
    public String getGioiTinh() { return gioiTinh.get(); }
    public void setGioiTinh(String value) { gioiTinh.set(value); }

    public StringProperty sdtProperty() { return sdt; }
    public String getSdt() { return sdt.get(); }
    public void setSdt(String value) { sdt.set(value); }

    public StringProperty emailProperty() { return email; }
    public String getEmail() { return email.get(); }
    public void setEmail(String value) { email.set(value); }

    public StringProperty hocViProperty() { return hocVi; }
    public String getHocVi() { return hocVi.get(); }
    public void setHocVi(String value) { hocVi.set(value); }

    public StringProperty diaChiProperty() { return diaChi; }
    public String getDiaChi() { return diaChi.get(); }
    public void setDiaChi(String value) { diaChi.set(value); }

    public StringProperty maKhoaProperty() { return maKhoa; }
    public String getMaKhoa() { return maKhoa.get(); }
    public void setMaKhoa(String value) { maKhoa.set(value); }

    public StringProperty maMonHocProperty() { return maMonHoc; }
    public String getMaMonHoc() { return maMonHoc.get(); }
    public void setMaMonHoc(String value) { maMonHoc.set(value); }

    @Override
    public String toString() {
        return getHo() + " " + getTen();
    }
}