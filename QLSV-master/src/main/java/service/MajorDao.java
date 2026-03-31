package service;

import model.Major;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MajorDao {

    public List<Major> findAll() {
        List<Major> result = new ArrayList<>();

        String sql = """
                SELECT ma_nganh, ten_nganh, ma_khoa, mo_ta
                FROM majors
                ORDER BY ma_nganh
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                result.add(new Major(
                        rs.getString("ma_nganh"),
                        rs.getString("ten_nganh"),
                        rs.getString("ma_khoa"),
                        rs.getString("mo_ta")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public boolean insert(Major major) {
        String sql = """
                INSERT INTO majors(ma_nganh, ten_nganh, ma_khoa, mo_ta)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, major.getMaNganh());
            ps.setString(2, major.getTenNganh());
            ps.setString(3, major.getMaKhoa());
            ps.setString(4, major.getMoTa());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(Major major) {
        String sql = """
                UPDATE majors
                SET ten_nganh = ?, ma_khoa = ?, mo_ta = ?
                WHERE ma_nganh = ?
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, major.getTenNganh());
            ps.setString(2, major.getMaKhoa());
            ps.setString(3, major.getMoTa());
            ps.setString(4, major.getMaNganh());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteById(String maNganh) {
        String sql = "DELETE FROM majors WHERE ma_nganh = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maNganh);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean existsById(String maNganh) {
        String sql = "SELECT COUNT(*) FROM majors WHERE ma_nganh = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maNganh);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}