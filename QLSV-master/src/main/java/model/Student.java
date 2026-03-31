package model;

import javafx.beans.property.*;
import java.time.LocalDate;

public class Student {
    // Sử dụng các Property để hỗ trợ Data Binding
    private final StringProperty maSV = new SimpleStringProperty(this, "maSV");
    private final StringProperty ho = new SimpleStringProperty(this, "ho");
    private final StringProperty ten = new SimpleStringProperty(this, "ten");
    private final StringProperty gioiTinh = new SimpleStringProperty(this, "gioiTinh");
    private final StringProperty soDienThoai = new SimpleStringProperty(this, "soDienThoai");
    private final StringProperty email = new SimpleStringProperty(this, "email");
    private final StringProperty cccd = new SimpleStringProperty(this, "cccd");
    private final ObjectProperty<LocalDate> ngaySinh = new SimpleObjectProperty<>(this, "ngaySinh");
    private final StringProperty diaChi = new SimpleStringProperty(this, "diaChi");
    private final StringProperty maLop = new SimpleStringProperty(this, "maLop");
    private final StringProperty maNganh = new SimpleStringProperty(this, "maNganh");
    private final StringProperty maKhoa = new SimpleStringProperty(this, "maKhoa");

    // Constructor mặc định
    public Student() {}

    // Constructor đầy đủ tham số
    public Student(String maSV, String ho, String ten, String gioiTinh, String sdt, String email, String cccd, LocalDate ngaySinh, String diaChi , String malop) {
        setMaSV(maSV);
        setHo(ho);
        setTen(ten);
        setGioiTinh(gioiTinh);
        setSoDienThoai(sdt);
        setEmail(email);
        setCccd(cccd);
        setNgaySinh(ngaySinh);
        setDiaChi(diaChi);
        setMaLop(malop);
    }

    // --- Cấu trúc chuẩn cho mỗi thuộc tính (Property, Getter, Setter) ---

    // Mã Sinh Viên
    public StringProperty maSVProperty() { return maSV; }
    public String getMaSV() { return maSV.get(); }
    public void setMaSV(String value) { maSV.set(value); }

    // Họ
    public StringProperty hoProperty() { return ho; }
    public String getHo() { return ho.get(); }
    public void setHo(String value) { ho.set(value); }

    // Tên
    public StringProperty tenProperty() { return ten; }
    public String getTen() { return ten.get(); }
    public void setTen(String value) { ten.set(value); }

    // Giới tính
    public StringProperty gioiTinhProperty() { return gioiTinh; }
    public String getGioiTinh() { return gioiTinh.get(); }
    public void setGioiTinh(String value) { gioiTinh.set(value); }

    // Số điện thoại
    public StringProperty soDienThoaiProperty() { return soDienThoai; }
    public String getSoDienThoai() { return soDienThoai.get(); }
    public void setSoDienThoai(String value) { soDienThoai.set(value); }

    // Email
    public StringProperty emailProperty() { return email; }
    public String getEmail() { return email.get(); }
    public void setEmail(String value) { email.set(value); }

    // CCCD
    public StringProperty cccdProperty() { return cccd; }
    public String getCccd() { return cccd.get(); }
    public void setCccd(String value) { cccd.set(value); }

    // Ngày sinh
    public ObjectProperty<LocalDate> ngaySinhProperty() { return ngaySinh; }
    public LocalDate getNgaySinh() { return ngaySinh.get(); }
    public void setNgaySinh(LocalDate value) { ngaySinh.set(value); }

    // Địa chỉ
    public StringProperty diaChiProperty() { return diaChi; }
    public String getDiaChi() { return diaChi.get(); }
    public void setDiaChi(String value) { diaChi.set(value); }


    // Mã Lớp
    public StringProperty maLopProperty() { return maLop; }
    public String getMaLop() { return maLop.get(); }
    public void setMaLop(String value) { maLop.set(value); }

    @Override
    public String toString() {
        // Hiển thị định dạng: "SV001 - Nguyễn An"
        return String.format("%s - %s %s", getMaSV(), getHo(), getTen());
    }

    public StringProperty maNganhProperty() { return maNganh; }
    public String getMaNganh() { return maNganh.get(); }
    public void setMaNganh(String value) { maNganh.set(value); }

    public StringProperty maKhoaProperty() { return maKhoa; }
    public String getMaKhoa() { return maKhoa.get(); }
    public void setMaKhoa(String value) { maKhoa.set(value); }
}