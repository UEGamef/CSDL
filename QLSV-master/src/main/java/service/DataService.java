package service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.*;

import java.sql.*;
import java.time.LocalDate;

public class DataService {
    private static DataService instance;

    private final ObservableList<Department> departments = FXCollections.observableArrayList();
    private final ObservableList<ClassUni> classes = FXCollections.observableArrayList();
    private final ObservableList<Student> students = FXCollections.observableArrayList();
    private final ObservableList<Subject> subjects = FXCollections.observableArrayList();
    private final ObservableList<Grade> grades = FXCollections.observableArrayList();
    private final ObservableList<Lecturer> lecturers = FXCollections.observableArrayList();
    private final ObservableList<Major> majors = FXCollections.observableArrayList();

    private DataService() {
    }

    public static DataService getInstance() {
        if (instance == null) {
            instance = new DataService();
        }
        return instance;
    }

    public void saveData() {
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);

            try (Statement st = conn.createStatement()) {
                st.executeUpdate("DELETE FROM grades");
                st.executeUpdate("DELETE FROM students");
                st.executeUpdate("DELETE FROM subjects");
                st.executeUpdate("DELETE FROM classes");
                st.executeUpdate("DELETE FROM departments");
                st.executeUpdate("DELETE FROM lecturers");
                st.executeUpdate("DELETE FROM majors");
            }

            try (PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO departments(ma_khoa, ten_khoa, sdt, email, phong) VALUES (?, ?, ?, ?, ?)")) {
                for (Department d : departments) {
                    ps.setString(1, d.getMaKhoa());
                    ps.setString(2, d.getTenKhoa());
                    ps.setString(3, d.getSoDienThoai());
                    ps.setString(4, d.getEmail());
                    ps.setString(5, d.getVanPhong());
                    ps.addBatch();
                }
                ps.executeBatch();
            }

            try (PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO majors(ma_nganh, ten_nganh, ma_khoa, mo_ta) VALUES (?, ?, ?, ?)")) {
                for (Major m : majors) {
                    ps.setString(1, m.getMaNganh());
                    ps.setString(2, m.getTenNganh());
                    ps.setString(3, m.getMaKhoa());
                    ps.setString(4, m.getMoTa());
                    ps.addBatch();
                }
                ps.executeBatch();
            }

            try (PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO classes(ma_lop, ten_lop, khoa, nien_khoa, ma_nganh) VALUES (?, ?, ?, ?, ?)")) {
                for (ClassUni c : classes) {
                    ps.setString(1, c.getMaLop());
                    ps.setString(2, c.getTenLop());
                    ps.setString(3, c.getMaKhoa());
                    ps.setString(4, c.getNienKhoa());
                    ps.setString(5, c.getMaNganh());
                    ps.addBatch();
                }
                ps.executeBatch();
            }

            try (PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO subjects(ma_mon_hoc, ten_mon_hoc, so_tin_chi, trong_so_1, trong_so_2, trong_so_3) VALUES (?, ?, ?, ?, ?, ?)")) {
                for (Subject s : subjects) {
                    ps.setString(1, s.getMaMonHoc());
                    ps.setString(2, s.getTenMonHoc());
                    ps.setInt(3, s.getSoTinChi());
                    ps.setDouble(4, s.getTrongSoChuyenCan());
                    ps.setDouble(5, s.getTrongSoQuaTrinh());
                    ps.setDouble(6, s.getTrongSoCuoiKy());
                    ps.addBatch();
                }
                ps.executeBatch();
            }

            try (PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO students(ma_sv, ho, ten, gioi_tinh, sdt, email, cccd, ngay_sinh, dia_chi, ma_lop, ma_nganh, ma_khoa) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")) {
                for (Student s : students) {
                    ps.setString(1, s.getMaSV());
                    ps.setString(2, s.getHo());
                    ps.setString(3, s.getTen());
                    ps.setString(4, s.getGioiTinh());
                    ps.setString(5, s.getSoDienThoai());
                    ps.setString(6, s.getEmail());
                    ps.setString(7, s.getCccd());
                    if (s.getNgaySinh() != null) ps.setDate(8, Date.valueOf(s.getNgaySinh()));
                    else ps.setNull(8, Types.DATE);
                    ps.setString(9, s.getDiaChi());
                    ps.setString(10, s.getMaLop());
                    ps.setString(11, s.getMaNganh());
                    ps.setString(12, s.getMaKhoa());
                    ps.addBatch();
                }
                ps.executeBatch();
            }

            try (PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO grades(ma_sv, ma_mon_hoc, hoc_ky, diem_qt, diem_gk, diem_ck) VALUES (?, ?, ?, ?, ?, ?)")) {
                for (Grade g : grades) {
                    ps.setString(1, g.getMaSV());
                    ps.setString(2, g.getMaMonHoc());
                    ps.setString(3, g.getHocKy());
                    ps.setDouble(4, g.getDiemChuyenCan());
                    ps.setDouble(5, g.getDiemQuaTrinh());
                    ps.setDouble(6, g.getDiemCuoiKy());
                    ps.addBatch();
                }
                ps.executeBatch();
            }

            try (PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO lecturers(ma_gv, ho, ten, gioi_tinh, sdt, email, hoc_vi, dia_chi, ma_khoa, ma_mon_hoc) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")) {
                for (Lecturer l : lecturers) {
                    ps.setString(1, l.getMaGv());
                    ps.setString(2, l.getHo());
                    ps.setString(3, l.getTen());
                    ps.setString(4, l.getGioiTinh());
                    ps.setString(5, l.getSdt());
                    ps.setString(6, l.getEmail());
                    ps.setString(7, l.getHocVi());
                    ps.setString(8, l.getDiaChi());
                    ps.setString(9, l.getMaKhoa());
                    ps.setString(10, l.getMaMonHoc());
                    ps.addBatch();
                }
                ps.executeBatch();
            }

            conn.commit();
            System.out.println(">>> ĐÃ LƯU DỮ LIỆU XUỐNG SQL SERVER!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadData() {
        departments.clear();
        classes.clear();
        students.clear();
        subjects.clear();
        grades.clear();
        lecturers.clear();
        majors.clear();

        try (Connection conn = DBConnection.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM departments");
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    departments.add(new Department(
                            rs.getString("ma_khoa"),
                            rs.getString("ten_khoa"),
                            rs.getString("sdt"),
                            rs.getString("email"),
                            rs.getString("phong")
                    ));
                }
            }

            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM majors");
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    majors.add(new Major(
                            rs.getString("ma_nganh"),
                            rs.getString("ten_nganh"),
                            rs.getString("ma_khoa"),
                            rs.getString("mo_ta")
                    ));
                }
            }

            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM classes");
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    classes.add(new ClassUni(
                            rs.getString("ma_lop"),
                            rs.getString("ten_lop"),
                            rs.getString("ma_nganh"),
                            rs.getString("khoa"),
                            rs.getString("nien_khoa")
                    ));
                }
            }

            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM subjects");
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    subjects.add(new Subject(
                            rs.getString("ma_mon_hoc"),
                            rs.getString("ten_mon_hoc"),
                            rs.getInt("so_tin_chi"),
                            rs.getDouble("trong_so_1"),
                            rs.getDouble("trong_so_2"),
                            rs.getDouble("trong_so_3")
                    ));
                }
            }

            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM students");
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Student s = new Student(
                            rs.getString("ma_sv"),
                            rs.getString("ho"),
                            rs.getString("ten"),
                            rs.getString("gioi_tinh"),
                            rs.getString("sdt"),
                            rs.getString("email"),
                            rs.getString("cccd"),
                            rs.getDate("ngay_sinh") != null ? rs.getDate("ngay_sinh").toLocalDate() : null,
                            rs.getString("dia_chi"),
                            rs.getString("ma_lop")
                    );
                    s.setMaNganh(rs.getString("ma_nganh"));
                    s.setMaKhoa(rs.getString("ma_khoa"));
                    students.add(s);
                }
            }

            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM grades");
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    grades.add(new Grade(
                            rs.getString("ma_sv"),
                            rs.getString("ma_mon_hoc"),
                            rs.getString("hoc_ky"),
                            rs.getDouble("diem_qt"),
                            rs.getDouble("diem_gk"),
                            rs.getDouble("diem_ck")
                    ));
                }
            }

            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM lecturers");
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lecturers.add(new Lecturer(
                            rs.getString("ma_gv"),
                            rs.getString("ho"),
                            rs.getString("ten"),
                            rs.getString("gioi_tinh"),
                            rs.getString("sdt"),
                            rs.getString("email"),
                            rs.getString("hoc_vi"),
                            rs.getString("dia_chi"),
                            rs.getString("ma_khoa"),
                            rs.getString("ma_mon_hoc")
                    ));
                }
            }

            System.out.println(">>> ĐÃ TẢI DỮ LIỆU TỪ SQL SERVER!");
        } catch (Exception e) {
            e.printStackTrace();
            initMockData();
        }
    }

    private void initMockData() {
        departments.add(new Department("K01", "Công nghệ thông tin", "0123456789", "cntt@example.com", "A101"));
        majors.add(new Major("CNTT", "Công nghệ thông tin", "K01", "Ngành công nghệ thông tin"));
        classes.add(new ClassUni("L01", "CNTT K1", "CNTT", "Công nghệ thông tin", "2023-2027"));
        subjects.add(new Subject("JAVA01", "Lập trình Java", 3, 0.1, 0.3, 0.6));
        students.add(new Student("SV001", "Nguyễn", "An", "Nam", "09876", "an@g.com", "123", LocalDate.of(2005, 1, 15), "HN", "L01"));
        grades.add(new Grade("SV001", "JAVA01", "2023.1", 8.5, 7.0, 9.0));
        lecturers.add(new Lecturer("GV001", "Trần", "Bình", "Nam", "0900000000", "gv@example.com", "ThS", "Hà Nội", "K01"));
    }

    public ObservableList<Department> getDepartments() { return departments; }
    public ObservableList<ClassUni> getClasses() { return classes; }
    public ObservableList<Student> getStudents() { return students; }
    public ObservableList<Subject> getSubjects() { return subjects; }
    public ObservableList<Grade> getGrades() { return grades; }
    public ObservableList<Lecturer> getLecturers() { return lecturers; }
    public ObservableList<Major> getMajors() { return majors; }

    public Subject findSubjectById(String id) {
        return subjects.stream().filter(s -> s.getMaMonHoc().equals(id)).findFirst().orElse(null);
    }

    public ObservableList<Grade> getGradesByStudent(String maSV) {
        return grades.filtered(g -> g.getMaSV().equals(maSV));
    }
}