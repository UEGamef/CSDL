package service;

import model.Grade;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GradeDao {

    public List<Grade> findAll() {
        List<Grade> result = new ArrayList<>();

        String sql = """
                SELECT ma_sv, ma_mon_hoc, hoc_ky, diem_qt, diem_gk, diem_ck
                FROM grades
                ORDER BY ma_sv, ma_mon_hoc, hoc_ky
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                result.add(new Grade(
                        rs.getString("ma_sv"),
                        rs.getString("ma_mon_hoc"),
                        rs.getString("hoc_ky"),
                        rs.getDouble("diem_qt"),
                        rs.getDouble("diem_gk"),
                        rs.getDouble("diem_ck")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public boolean insert(Grade g) {
        String sql = """
                INSERT INTO grades(ma_sv, ma_mon_hoc, hoc_ky, diem_qt, diem_gk, diem_ck)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, g.getMaSV());
            ps.setString(2, g.getMaMonHoc());
            ps.setString(3, g.getHocKy());
            ps.setDouble(4, g.getDiemChuyenCan());
            ps.setDouble(5, g.getDiemQuaTrinh());
            ps.setDouble(6, g.getDiemCuoiKy());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(Grade g) {
        String sql = """
                UPDATE grades
                SET diem_qt = ?, diem_gk = ?, diem_ck = ?
                WHERE ma_sv = ? AND ma_mon_hoc = ? AND hoc_ky = ?
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDouble(1, g.getDiemChuyenCan());
            ps.setDouble(2, g.getDiemQuaTrinh());
            ps.setDouble(3, g.getDiemCuoiKy());
            ps.setString(4, g.getMaSV());
            ps.setString(5, g.getMaMonHoc());
            ps.setString(6, g.getHocKy());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteByKey(String maSV, String maMonHoc, String hocKy) {
        String sql = """
                DELETE FROM grades
                WHERE ma_sv = ? AND ma_mon_hoc = ? AND hoc_ky = ?
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maSV);
            ps.setString(2, maMonHoc);
            ps.setString(3, hocKy);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean existsByKey(String maSV, String maMonHoc, String hocKy) {
        String sql = """
                SELECT COUNT(*)
                FROM grades
                WHERE ma_sv = ? AND ma_mon_hoc = ? AND hoc_ky = ?
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maSV);
            ps.setString(2, maMonHoc);
            ps.setString(3, hocKy);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}