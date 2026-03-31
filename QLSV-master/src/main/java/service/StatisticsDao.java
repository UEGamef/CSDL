package service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Pair;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class StatisticsDao {

    public int countDepartments() {
        return count("SELECT COUNT(*) FROM departments");
    }

    public int countClasses() {
        return count("SELECT COUNT(*) FROM classes");
    }

    public int countStudents() {
        return count("SELECT COUNT(*) FROM students");
    }

    public int countSubjects() {
        return count("SELECT COUNT(*) FROM subjects");
    }

    public int countGrades() {
        return count("SELECT COUNT(*) FROM grades");
    }

    public ObservableList<Pair<String, Double>> getTop5Gpa() {
        ObservableList<Pair<String, Double>> result = FXCollections.observableArrayList();

        String sql = """
                SELECT TOP 5
                    s.ma_sv,
                    CONCAT(s.ho, ' ', s.ten) AS ho_ten,
                    AVG(
                        CASE
                            WHEN g.diem_qt IS NULL OR g.diem_gk IS NULL OR g.diem_ck IS NULL THEN 0
                            ELSE
                                CASE
                                    WHEN (
                                        g.diem_qt * 0.1 +
                                        g.diem_gk * 0.3 +
                                        g.diem_ck * 0.6
                                    ) >= 8.5 THEN 4.0
                                    WHEN (
                                        g.diem_qt * 0.1 +
                                        g.diem_gk * 0.3 +
                                        g.diem_ck * 0.6
                                    ) >= 7.0 THEN 3.0
                                    WHEN (
                                        g.diem_qt * 0.1 +
                                        g.diem_gk * 0.3 +
                                        g.diem_ck * 0.6
                                    ) >= 5.5 THEN 2.0
                                    WHEN (
                                        g.diem_qt * 0.1 +
                                        g.diem_gk * 0.3 +
                                        g.diem_ck * 0.6
                                    ) >= 4.0 THEN 1.0
                                    ELSE 0.0
                                END
                        END
                    ) AS gpa
                FROM students s
                LEFT JOIN grades g ON s.ma_sv = g.ma_sv
                GROUP BY s.ma_sv, CONCAT(s.ho, ' ', s.ten)
                ORDER BY gpa DESC
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                result.add(new Pair<>(
                        rs.getString("ho_ten"),
                        rs.getDouble("gpa")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public ObservableList<String> getStudentsByClass() {
        ObservableList<String> result = FXCollections.observableArrayList();

        String sql = """
                SELECT c.ten_lop, COUNT(s.ma_sv) AS so_luong
                FROM classes c
                LEFT JOIN students s ON c.ma_lop = s.ma_lop
                GROUP BY c.ten_lop
                ORDER BY so_luong DESC, c.ten_lop
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                result.add(rs.getString("ten_lop") + " - " + rs.getInt("so_luong") + " SV");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public ObservableList<String> getStudentsByDepartment() {
        ObservableList<String> result = FXCollections.observableArrayList();

        String sql = """
                SELECT d.ten_khoa, COUNT(s.ma_sv) AS so_luong
                FROM departments d
                LEFT JOIN classes c ON d.ten_khoa = c.khoa
                LEFT JOIN students s ON c.ma_lop = s.ma_lop
                GROUP BY d.ten_khoa
                ORDER BY so_luong DESC, d.ten_khoa
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                result.add(rs.getString("ten_khoa") + " - " + rs.getInt("so_luong") + " SV");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    private int count(String sql) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}