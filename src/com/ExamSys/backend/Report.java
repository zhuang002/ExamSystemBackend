/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ExamSys.backend;

import com.mysql.cj.jdbc.MysqlDataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.sql.ResultSet;
import java.util.Collection;
/**
 *
 * @author Andy
 */
public class Report implements IDBRecord{

  
    
    int ID;
    int examID;
    ArrayList<Integer> scoreList = new ArrayList<Integer>();
    String studentUserName=null;
    int totalScore=0;
    Date  dateTime; 
    
    
    public int getID(){
        return this.ID;
    }
    
    public void setID(int id){
        this.ID=id;
    }
    
     public Exam getExam(){
        return Exam.getExamByID(this.examID);
    }
     
    public void setExam(Exam exam){
        this.examID=exam.getID();
    }
    private void setExam(int examid){
         this.examID=examID;
    }
    
    public List<Integer> getScoreList(){
        return this.scoreList;
    }
    
    public int getScore(){
        int total=0;
        for(int score:scoreList){
            total+=score;
        }
        return total;
    }
    
    public User getStudent(){
        return  User.getUserByUsername(this.studentUserName);
    }
    
    public void setStudent(User student){
        this.studentUserName=student.getUserName();
    }
    
    private void setStudent(String studentUserName){
        this.studentUserName=studentUserName;
    }
        
    public Date getDate(){
        return this.dateTime;
    }
    public void setDate(Date dateTime){
        this.dateTime=dateTime;
    }
    
    public boolean create(){
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
            statement = conn.prepareStatement("Insert into report (examid,student,date) Values(?,?,?)");
            statement.setInt(1, this.examID);
            statement.setString(2, this.studentUserName);
            statement.setDate(3, new  java.sql.Date(this.dateTime.getTime()));
           
            statement.executeUpdate();
             ResultSet rs=statement.getGeneratedKeys();
            if(rs.next()){
                this.ID=rs.getInt(1);
            }
            statement.close();
            
            for(int i=0;i<scoreList.size();i++){
                int score=this.scoreList.get(i);
                statement=conn.prepareStatement("Insert into reportscore (index,report,score) Values(?,?,?)");
                statement.setInt(1, i);
                statement.setInt(2, this.ID);
                statement.setInt(3, score);
                statement.executeUpdate();
                statement.close();
            }
            conn.commit();
        } catch (SQLException e) {
            try{
                conn.rollback();
            }catch(Exception ee){
                
            }
            System.out.println("create report error");
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
    
    public boolean update(){
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
            
            for(int i=0;i<scoreList.size();i++){
                int score=this.scoreList.get(i);
                statement=conn.prepareStatement("update reportscore set score=? where index=? And report=?");
                statement.setInt(1, score);
                statement.setInt(2, i);
                statement.setInt(3, this.ID);
                statement.executeUpdate();
                statement.close();
            }
            conn.commit();
        } catch (SQLException e) {
            try{
                conn.rollback();
            }catch(Exception ee){
                
            }
            System.out.println("update report error");
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
    
    public boolean remove(){
        return remove(this.ID);
    }
      
    public static boolean remove(int id){
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
            statement = conn.prepareStatement("delete from report where id=?");
            statement.setInt(1, id);
            statement.executeUpdate();
            statement.close();
            
            statement = conn.prepareStatement("delete from reportscore where report=?");
            statement.setInt(1, id);
            statement.executeUpdate();
            statement.close();
            
            conn.commit();
        } catch (SQLException e) {
            try{
                conn.rollback();
            }catch(Exception ee){
                
            }
            System.out.println("remove report error");
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
    
    public boolean get(int id) {
//        Report report = new Report();
        MysqlDataSource ds = new MysqlDataSource();
        Properties properties = new Properties();
        ds.setURL("jdbc:mysql://localhost:3306/examsystem");
        ds.setUser("root");
        ds.setPassword("admin");
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            conn = ds.getConnection();
            statement = conn.prepareStatement("Select examid, studnet,date from report Where id=? ");
            statement.setInt(1, id);
            rs = statement.executeQuery();

            if (rs.next()) {
                this.setID(id);
                this.setExam(rs.getInt("examid"));
                this.setStudent(rs.getString("student"));
                this.setDate(new Date(rs.getDate("date").getTime()));
            }
            statement.close();
            
//            statement=conn.prepareStatement("Select score from reportscore Where id=? order by index");
//              statement.setInt(1, id);
//            rs = statement.executeQuery();

            
            List scoreList=this.getScoreList();
            scoreList.clear();
            scoreList.addAll(getScoreOfReport(id, conn));            
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
    
    public static List<Report> getReportsByStudentName(String StudentUsername){
        ArrayList<Report> ret=new ArrayList();
       
        MysqlDataSource ds = new MysqlDataSource();
        Properties properties = new Properties();
        ds.setURL("jdbc:mysql://localhost:3306/examsystem");
        ds.setUser("root");
        ds.setPassword("admin");
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            conn = ds.getConnection();
            statement = conn.prepareStatement("Select id,examid,date from report Where student=? ");
            statement.setString(1, StudentUsername);
            rs = statement.executeQuery();

            while (rs.next()) {
                Report report=new Report();
                report.setID(rs.getInt("id"));
                report.setExam(rs.getInt("examid"));
                report.setStudent(StudentUsername);
                report.setDate(new Date(rs.getDate("date").getTime()));
                ret.add(report);
        
            }
            statement.close();
        
            
            for(Report report:ret){
                List scoreList=report.getScoreList();
                scoreList.clear();
                scoreList.addAll(getScoreOfReport(report.getID(),conn));
            
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
        return ret;
    
    }
      
    private static Collection getScoreOfReport(int id, Connection conn) throws SQLException {
       PreparedStatement statement=conn.prepareStatement("Select score from reportscore Where id=? order by index");
              statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            List scoreList=new ArrayList<Integer>();
            scoreList.clear();
            while(rs.next()){
                scoreList.add(rs.getInt(1));
            }
            return scoreList;
    }

   
    public static Report getReportByID(int id) {
        Report report=new Report();
        if(report.get(id)){
            return report;
        }else{
            return null;
        }
    }
    
    
    
    
}
