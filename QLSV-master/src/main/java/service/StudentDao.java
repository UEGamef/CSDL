package service;

import model.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDao {

    public List<Student> findAll() {
        List<Student> result = new ArrayList<>();

        String sql = """
                SELECT ma_sv, ho, ten, gioi_tinh, sdt, email, cccd, ngay_sinh, dia_chi, ma_lop, ma_nganh, ma_khoa
                FROM students
                ORDER BY ma_sv
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
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
                result.add(s);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public boolean insert(Student s) {
        String sql = """
                INSERT INTO students(ma_sv, ho, ten, gioi_tinh, sdt, email, cccd, ngay_sinh, dia_chi, ma_lop, ma_nganh, ma_khoa)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

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

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(Student s) {
        String sql = """
                UPDATE students
                SET ho = ?, ten = ?, gioi_tinh = ?, sdt = ?, email = ?, cccd = ?, ngay_sinh = ?, dia_chi = ?, ma_lop = ?, ma_nganh = ?, ma_khoa = ?
                WHERE ma_sv = ?
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, s.getHo());
            ps.setString(2, s.getTen());
            ps.setString(3, s.getGioiTinh());
            ps.setString(4, s.getSoDienThoai());
            ps.setString(5, s.getEmail());
            ps.setString(6, s.getCccd());
            if (s.getNgaySinh() != null) ps.setDate(7, Date.valueOf(s.getNgaySinh()));
            else ps.setNull(7, Types.DATE);
            ps.setString(8, s.getDiaChi());
            ps.setString(9, s.getMaLop());
            ps.setString(10, s.getMaNganh());
            ps.setString(11, s.getMaKhoa());
            ps.setString(12, s.getMaSV());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteById(String maSV) {
        String sql = "DELETE FROM students WHERE ma_sv = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maSV);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean existsById(String maSV) {
        String sql = "SELECT COUNT(*) FROM students WHERE ma_sv = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maSV);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}