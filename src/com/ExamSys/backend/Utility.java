/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ExamSys.backend;

import java.sql.*;

/**
 *
 * @author Andy
 */
public class Utility {
    static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.jdbc.Driver");  
        } catch (ClassNotFoundException e) {
            System.out.println(e);
        }
        Connection conn= DriverManager.getConnection("jdbc:mysql://localhost:3306/examsystem","root","Admin");
        return conn;
    }
}
