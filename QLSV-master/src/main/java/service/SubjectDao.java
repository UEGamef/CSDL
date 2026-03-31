package service;

import model.Subject;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SubjectDao {

    public List<Subject> findAll() {
        List<Subject> result = new ArrayList<>();

        String sql = """
                SELECT ma_mon_hoc, ten_mon_hoc, so_tin_chi, trong_so_1, trong_so_2, trong_so_3
                FROM subjects
                ORDER BY ma_mon_hoc
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                result.add(new Subject(
                        rs.getString("ma_mon_hoc"),
                        rs.getString("ten_mon_hoc"),
                        rs.getInt("so_tin_chi"),
                        rs.getDouble("trong_so_1"),
                        rs.getDouble("trong_so_2"),
                        rs.getDouble("trong_so_3")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public boolean existsById(String maMonHoc) {
        String sql = "SELECT COUNT(*) FROM subjects WHERE ma_mon_hoc = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maMonHoc);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean insert(Subject s) {
        String sql = """
                INSERT INTO subjects(ma_mon_hoc, ten_mon_hoc, so_tin_chi, trong_so_1, trong_so_2, trong_so_3)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, s.getMaMonHoc());
            ps.setString(2, s.getTenMonHoc());
            ps.setInt(3, s.getSoTinChi());
            ps.setDouble(4, s.getTrongSoChuyenCan());
            ps.setDouble(5, s.getTrongSoQuaTrinh());
            ps.setDouble(6, s.getTrongSoCuoiKy());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(Subject s) {
        String sql = """
                UPDATE subjects
                SET ten_mon_hoc = ?, so_tin_chi = ?, trong_so_1 = ?, trong_so_2 = ?, trong_so_3 = ?
                WHERE ma_mon_hoc = ?
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, s.getTenMonHoc());
            ps.setInt(2, s.getSoTinChi());
            ps.setDouble(3, s.getTrongSoChuyenCan());
            ps.setDouble(4, s.getTrongSoQuaTrinh());
            ps.setDouble(5, s.getTrongSoCuoiKy());
            ps.setString(6, s.getMaMonHoc());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteById(String maMonHoc) {
        String sql = "DELETE FROM subjects WHERE ma_mon_hoc = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maMonHoc);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}