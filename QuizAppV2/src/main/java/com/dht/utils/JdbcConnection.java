/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dht.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author admin
 */
public class JdbcConnection {
    private static JdbcConnection instance;
    private final Connection conn;
    
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(JdbcConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private JdbcConnection() throws SQLException {
        conn = DriverManager.getConnection("jdbc:mysql://localhost/quizdb", "root", "root");
    }
    
    public static JdbcConnection getInstance() throws SQLException {
        if (instance == null)
            instance = new JdbcConnection();
        return instance;
    }
    
    public Connection connect() {
        return this.conn;
    }
}
