package service;

import model.Department;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DepartmentDao {

    public List<Department> findAll() {
        List<Department> result = new ArrayList<>();

        String sql = "SELECT ma_khoa, ten_khoa, sdt, email, phong FROM departments ORDER BY ma_khoa";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                result.add(new Department(
                        rs.getString("ma_khoa"),
                        rs.getString("ten_khoa"),
                        rs.getString("sdt"),
                        rs.getString("email"),
                        rs.getString("phong")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public boolean existsById(String maKhoa) {
        String sql = "SELECT COUNT(*) FROM departments WHERE ma_khoa = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maKhoa);

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

    public boolean insert(Department dep) {
        String sql = "INSERT INTO departments(ma_khoa, ten_khoa, sdt, email, phong) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, dep.getMaKhoa());
            ps.setString(2, dep.getTenKhoa());
            ps.setString(3, dep.getSoDienThoai());
            ps.setString(4, dep.getEmail());
            ps.setString(5, dep.getVanPhong());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(Department dep) {
        String sql = "UPDATE departments SET ten_khoa = ?, sdt = ?, email = ?, phong = ? WHERE ma_khoa = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, dep.getTenKhoa());
            ps.setString(2, dep.getSoDienThoai());
            ps.setString(3, dep.getEmail());
            ps.setString(4, dep.getVanPhong());
            ps.setString(5, dep.getMaKhoa());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteById(String maKhoa) {
        String sql = "DELETE FROM departments WHERE ma_khoa = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maKhoa);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}