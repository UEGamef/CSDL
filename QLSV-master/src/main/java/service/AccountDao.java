package service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Account;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AccountDao {

    public Account login(String username, String password) {
        String sql = """
                SELECT account_id, username, password, full_name, role, active, student_id
                FROM accounts
                WHERE username = ? AND password = ? AND active = 1
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapAccount(rs);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public ObservableList<Account> findAll() {
        ObservableList<Account> result = FXCollections.observableArrayList();
        String sql = """
                SELECT account_id, username, password, full_name, role, active, student_id
                FROM accounts
                ORDER BY account_id DESC
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                result.add(mapAccount(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public boolean insert(Account a) {
        String sql = """
                INSERT INTO accounts(username, password, full_name, role, active, student_id)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, a.getUsername());
            ps.setString(2, a.getPassword());
            ps.setString(3, a.getFullName());
            ps.setString(4, a.getRole());
            ps.setBoolean(5, a.isActive());
            ps.setString(6, a.getStudentId());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(Account a) {
        String sql = """
                UPDATE accounts
                SET username = ?, password = ?, full_name = ?, role = ?, active = ?, student_id = ?
                WHERE account_id = ?
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, a.getUsername());
            ps.setString(2, a.getPassword());
            ps.setString(3, a.getFullName());
            ps.setString(4, a.getRole());
            ps.setBoolean(5, a.isActive());
            ps.setString(6, a.getStudentId());
            ps.setInt(7, a.getAccountId());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteById(int accountId) {
        String sql = "DELETE FROM accounts WHERE account_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, accountId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean resetPassword(int accountId, String newPassword) {
        String sql = "UPDATE accounts SET password = ? WHERE account_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, newPassword);
            ps.setInt(2, accountId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Account findByStudentId(String studentId) {
        String sql = """
                SELECT account_id, username, password, full_name, role, active, student_id
                FROM accounts
                WHERE student_id = ?
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, studentId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapAccount(rs);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private Account mapAccount(ResultSet rs) throws Exception {
        return new Account(
                rs.getInt("account_id"),
                rs.getString("username"),
                rs.getString("password"),
                rs.getString("full_name"),
                rs.getString("role"),
                rs.getBoolean("active"),
                rs.getString("student_id")
        );
    }
}