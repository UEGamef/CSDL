package model;

import javafx.beans.property.*;

public class Department {
    // Sử dụng các Property để hỗ trợ Data Binding
    private final StringProperty maKhoa = new SimpleStringProperty(this, "maKhoa");
    private final StringProperty tenKhoa = new SimpleStringProperty(this, "tenKhoa");
    private final StringProperty soDienThoai = new SimpleStringProperty(this, "soDienThoai");
    private final StringProperty email = new SimpleStringProperty(this, "email");
    private final StringProperty vanPhong = new SimpleStringProperty(this, "vanPhong"); // Vị trí văn phòng khoa

    // Constructor mặc định
    public Department() {}

    // Constructor đầy đủ tham số
    public Department(String maKhoa, String tenKhoa, String sdt, String email, String vanPhong) {
        setMaKhoa(maKhoa);
        setTenKhoa(tenKhoa);
        setSoDienThoai(sdt);
        setEmail(email);
        setVanPhong(vanPhong);
    }

    // --- Mã Khoa ---
    public StringProperty maKhoaProperty() { return maKhoa; }
    public String getMaKhoa() { return maKhoa.get(); }
    public void setMaKhoa(String value) { maKhoa.set(value); }

    // --- Tên Khoa ---
    public StringProperty tenKhoaProperty() { return tenKhoa; }
    public String getTenKhoa() { return tenKhoa.get(); }
    public void setTenKhoa(String value) { tenKhoa.set(value); }

    // --- Số Điện Thoại ---
    public StringProperty soDienThoaiProperty() { return soDienThoai; }
    public String getSoDienThoai() { return soDienThoai.get(); }
    public void setSoDienThoai(String value) { soDienThoai.set(value); }

    // --- Email ---
    public StringProperty emailProperty() { return email; }
    public String getEmail() { return email.get(); }
    public void setEmail(String value) { email.set(value); }

    // --- Văn Phòng ---
    public StringProperty vanPhongProperty() { return vanPhong; }
    public String getVanPhong() { return vanPhong.get(); }
    public void setVanPhong(String value) { vanPhong.set(value); }

    @Override
    public String toString() {
        return getTenKhoa(); // Hiển thị tên khoa khi đưa vào ComboBox hoặc List
    }
}
