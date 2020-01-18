/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ExamSys.backend;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Andy
 */
public class User implements IDBRecord{

    private String username = null;
    private String name = null;
    private Role role = null;
    private String password = null;

    public String getUserName() {
        return username;
    }

    public void setUserName(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public boolean create() {
       
        Connection conn = null;
        PreparedStatement statement = null;
        try {
            conn = Utility.getConnection();
            statement = conn.prepareStatement("Insert into User (username,password,name,role) Values(?,?,?,?)");
            statement.setString(1, this.username);
            statement.setString(2, this.password);
            statement.setString(3, this.name);
            statement.setInt(4, this.role.getValue());
            statement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("create user error");
            System.out.println(e.getMessage());
            System.out.println(e.getStackTrace());
            return false;
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception e) {
            }
        }
        return true;
    }

    @Override
    public boolean update() {
      
        Connection conn = null;
        PreparedStatement statement = null;
        try {
            conn = Utility.getConnection();
            statement = conn.prepareStatement("Update User Set name=?,role=? Where username=?");
            statement.setString(1, this.name);
            statement.setInt(2, this.role.getValue());
            statement.setString(3, this.username);
            statement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("update user error");
            System.out.println(e.getMessage());
            System.out.println(e.getStackTrace());
            return false;
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception e) {
            }
        }
        return true;

    }

    @Override
    public boolean remove() {
        return remove(this.username);
    }

    public static boolean remove(String username) {
       
        Connection conn = null;
        PreparedStatement statement = null;
        try {
            conn = Utility.getConnection();
            statement = conn.prepareStatement("Update User Set deleted=1  Where username=? ");
            statement.setString(1, username);
            statement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("remove user error");
            System.out.println(e.getMessage());
            System.out.println(e.getStackTrace());
            return false;
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception e) {
            }
        }
        return true;
    }

    public static User getUserByUsername(String username) {
        User user = new User();
       
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            conn = Utility.getConnection();
            statement = conn.prepareStatement("Select name,role,password From user Where deleted=0 And username=? ");
            statement.setString(1, username);
            rs = statement.executeQuery();

            if (rs.next()) {
                user.setUserName(username);
                user.setName(rs.getString("name"));
                user.setRole(Role.values()[rs.getInt("role")]);
                user.password=rs.getString("password");

            }else{
                return null;
            }

        } catch (SQLException e) {
            System.out.println("create user error");
            System.out.println(e.getMessage());
            System.out.println(e.getStackTrace());
            return null;
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception e) {
            }
        }
        return user;
    }

    public static List<User> getAllUsers(Role role) {
        ArrayList<User> arrayList = new ArrayList<User>();
       
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            conn = Utility.getConnection();
            statement = conn.prepareStatement("Select username,name,role From user Where deleted=0 And role=? ");
            statement.setInt(1, role.getValue());
            rs = statement.executeQuery();

            while(rs.next())
            {
                User user = new User();
                user.setUserName(rs.getString("username"));
                user.setName(rs.getString("name"));
                user.setRole(Role.values()[rs.getInt("role")]);
                arrayList.add(user);
            }

        } catch (SQLException e) {
            System.out.println("create user error");
            System.out.println(e.getMessage());
            System.out.println(e.getStackTrace());
            return null;
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception e) {
            }
        }

        return arrayList;

    }

    @Override
    public boolean get(int id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    public static User login(String username,String password){
        
        User user =User.getUserByUsername(username);
        if(user==null){
            return null;
        }
        if(user.password.equals(password)){
            return user;
        }
        
        return null;
    }
    
    public static boolean isTested(int examID,User user){
       List<Report> reportList=Report.getReportsByStudentName(user.getUserName());
       Report report=null;
       for(int i=0;i<reportList.size();i++){
           if(reportList.get(i).getExam().getID()==examID){
               report=reportList.get(i);
               break;
           }
       }
       if(report==null||report.getScore()==0){
           return false;
       }
       else{
       return false;
       }
    }
    
    @Override
    public String toString() {
        return this.username+" - "+this.name;
    }
}
