/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ExamSys.backend;

import com.mysql.cj.jdbc.MysqlDataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 *
 * @author Andy
 */
class Exam implements IDBRecord {


    int ID;
    String description;
    Date dateTime;
    List<Problem> problems= new ArrayList<Problem>();
    Duration timeLimit;

    public int getID() {
        return ID;
    }
    
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public List<Problem> getProblems() {
        return problems;
    }

    public Duration getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(Duration timeLimit) {
        this.timeLimit = timeLimit;
    }
    
   
    static Exam getExamByID(int examID) {
       Exam exam = new Exam();
       if(exam.get(examID)){
           return exam;
       }else{
           return null;
       }
        
    }
    
    public static boolean removeByID(int id){
        
         MysqlDataSource ds = new MysqlDataSource();
        Properties properties = new Properties();
        ds.setURL("jdbc:mysql://localhost:3306/examsystem");
        ds.setUser("root");
        ds.setPassword("admin");
        Connection conn = null;
        PreparedStatement statement = null;
        try {
            conn = ds.getConnection();
            conn.setAutoCommit(false);
            
             statement = conn.prepareStatement("delete Exam where id=?");
            statement.setInt(1, id);
            
            statement.executeUpdate();
            statement.close();
            
            
            statement=conn.prepareStatement("delete exam-problem where examid=?");
            statement.setInt(1, id);
            
             statement.executeUpdate();
            statement.close();
            conn.commit();
        
        } catch (SQLException e) {
            try{
                conn.rollback();
            }catch(Exception ee){
                
            }
            System.out.println("remove exam error");
            System.out.println(e.getMessage());
            System.out.println(e.getStackTrace());
            return false;
        } finally {
            try {
                conn.setAutoCommit(true);
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
    public boolean create() {
        MysqlDataSource ds = new MysqlDataSource();
        Properties properties = new Properties();
        ds.setURL("jdbc:mysql://localhost:3306/examsystem");
        ds.setUser("root");
        ds.setPassword("admin");
        Connection conn = null;
        PreparedStatement statement = null;
        try {
            conn = ds.getConnection();
            conn.setAutoCommit(false);
            statement = conn.prepareStatement("Insert into Exam ( description, date, timelimit) Values(?,?,?)");
            statement.setString(1,this.description);
            statement.setDate(2, new  java.sql.Date(this.dateTime.getTime()));
            statement.setLong(3, this.timeLimit.getSeconds());
            
            statement.executeUpdate(); 
            ResultSet rs=statement.getGeneratedKeys();
            if(rs.next()){
                this.ID=rs.getInt(1);
            }
            statement.close();
            
            for(int i=0;i<this.problems.size();i++){
                int problemId=this.problems.get(i).getID();
                statement=conn.prepareStatement("Insert into exam-problem (examid,problemid) Values(?,?)");
                statement.setInt(1, this.ID);
                statement.setInt(2, problemId);
                statement.executeUpdate();
                statement.close();
            }
            conn.commit();
        } catch (SQLException e) {
            try{
                conn.rollback();
            }catch(Exception ee){
                
            }
            System.out.println("create exam error");
            System.out.println(e.getMessage());
            System.out.println(e.getStackTrace());
            return false;
        } finally {
            try {
                conn.setAutoCommit(true);
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
        MysqlDataSource ds = new MysqlDataSource();
        Properties properties = new Properties();
        ds.setURL("jdbc:mysql://localhost:3306/examsystem");
        ds.setUser("root");
        ds.setPassword("admin");
        Connection conn = null;
        PreparedStatement statement = null;
        try {
            conn = ds.getConnection();
            conn.setAutoCommit(false);
            
             statement = conn.prepareStatement("update Exam set description=?, date=? , timelimit=? where id=?");
            statement.setString(1,this.description);
            statement.setDate(2, new  java.sql.Date(this.dateTime.getTime()));
            statement.setLong(3, this.timeLimit.getSeconds());
            statement.setInt(4, this.ID);
            
            statement.executeUpdate();
            statement.close();
            
            
            statement=conn.prepareStatement("delete exam-problem where examid=?");
            statement.setInt(1, this.ID);
            
             for(int i=0;i<this.problems.size();i++){
                int problemId=this.problems.get(i).getID();
                statement=conn.prepareStatement("Insert into exam-problem (examid,problemid) Values(?,?)");
                statement.setInt(1, this.ID);
                statement.setInt(2, problemId);
                statement.executeUpdate();
                statement.close();
            }
            conn.commit();
        
        } catch (SQLException e) {
            try{
                conn.rollback();
            }catch(Exception ee){
                
            }
            System.out.println("update exam error");
            System.out.println(e.getMessage());
            System.out.println(e.getStackTrace());
            return false;
        } finally {
            try {
                conn.setAutoCommit(true);
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
     
       return removeByID(this.ID);
   
        
    }

    
    
    
    
    
    
    
    
    
    @Override
    public boolean get(int id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }





    
}
