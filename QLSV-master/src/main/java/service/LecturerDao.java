package service;

import model.Lecturer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LecturerDao {

    public List<Lecturer> findAll() {
        List<Lecturer> result = new ArrayList<>();

        String sql = """
                SELECT ma_gv, ho, ten, gioi_tinh, sdt, email, hoc_vi, dia_chi, ma_khoa, ma_mon_hoc
                FROM lecturers
                ORDER BY ma_gv
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                result.add(new Lecturer(
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public boolean insert(Lecturer l) {
        String sql = """
                INSERT INTO lecturers(ma_gv, ho, ten, gioi_tinh, sdt, email, hoc_vi, dia_chi, ma_khoa, ma_mon_hoc)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

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

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(Lecturer l) {
        String sql = """
                UPDATE lecturers
                SET ho = ?, ten = ?, gioi_tinh = ?, sdt = ?, email = ?, hoc_vi = ?, dia_chi = ?, ma_khoa = ?, ma_mon_hoc = ?
                WHERE ma_gv = ?
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, l.getHo());
            ps.setString(2, l.getTen());
            ps.setString(3, l.getGioiTinh());
            ps.setString(4, l.getSdt());
            ps.setString(5, l.getEmail());
            ps.setString(6, l.getHocVi());
            ps.setString(7, l.getDiaChi());
            ps.setString(8, l.getMaKhoa());
            ps.setString(9, l.getMaMonHoc());
            ps.setString(10, l.getMaGv());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteById(String maGv) {
        String sql = "DELETE FROM lecturers WHERE ma_gv = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maGv);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean existsById(String maGv) {
        String sql = "SELECT COUNT(*) FROM lecturers WHERE ma_gv = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maGv);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}