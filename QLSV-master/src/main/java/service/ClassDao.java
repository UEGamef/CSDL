package service;

import model.ClassUni;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClassDao {

    public List<ClassUni> findAll() {
        List<ClassUni> result = new ArrayList<>();

        String sql = """
                SELECT ma_lop, ten_lop, khoa, nien_khoa, ma_nganh
                FROM classes
                ORDER BY ma_lop
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                result.add(new ClassUni(
                        rs.getString("ma_lop"),
                        rs.getString("ten_lop"),
                        rs.getString("ma_nganh"),
                        rs.getString("khoa"),
                        rs.getString("nien_khoa")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public boolean insert(ClassUni cls) {
        String sql = """
                INSERT INTO classes(ma_lop, ten_lop, khoa, nien_khoa, ma_nganh)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, cls.getMaLop());
            ps.setString(2, cls.getTenLop());
            ps.setString(3, cls.getMaKhoa());
            ps.setString(4, cls.getNienKhoa());
            ps.setString(5, cls.getMaNganh());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(ClassUni cls) {
        String sql = """
                UPDATE classes
                SET ten_lop = ?, khoa = ?, nien_khoa = ?, ma_nganh = ?
                WHERE ma_lop = ?
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, cls.getTenLop());
            ps.setString(2, cls.getMaKhoa());
            ps.setString(3, cls.getNienKhoa());
            ps.setString(4, cls.getMaNganh());
            ps.setString(5, cls.getMaLop());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteById(String maLop) {
        String sql = "DELETE FROM classes WHERE ma_lop = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maLop);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean existsById(String maLop) {
        String sql = "SELECT COUNT(*) FROM classes WHERE ma_lop = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maLop);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}