package service;

import model.Admin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AdminDao {

    public Admin login(String username, String password) {
        String sql = """
                SELECT admin_id, username, password, full_name, active
                FROM admins
                WHERE username = ? AND password = ? AND active = 1
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Admin(
                            rs.getInt("admin_id"),
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getString("full_name"),
                            rs.getBoolean("active")
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}