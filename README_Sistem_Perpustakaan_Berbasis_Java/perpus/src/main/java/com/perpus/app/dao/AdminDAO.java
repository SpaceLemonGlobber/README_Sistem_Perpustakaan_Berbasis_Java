package com.perpus.app.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.perpus.app.models.Admin;
import com.perpus.config.Database;

public class AdminDAO {

    public List<Admin> getAll() {
        List<Admin> list = new ArrayList<>();
        String sql = "SELECT u.userId, u.username, u.password, a.nama_admin, a.adminId, a.email " +
                     "FROM user u JOIN admin a ON u.userId = a.userId";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Admin admin = new Admin(
                    rs.getInt("userId"),      
                    rs.getString("username"), 
                    rs.getString("password"), 
                    rs.getString("nama_admin"), 
                    rs.getInt("adminId"),    
                    rs.getString("email")     
                );
                list.add(admin);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean insert(Admin admin) {
        return false; 
    }
}