/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ExamSys.backend;

import com.mysql.cj.jdbc.MysqlDataSource;
import java.sql.*;
import java.text.SimpleDateFormat;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 *
 * @author Andy
 */
public class Exam implements IDBRecord {

    public static List<Exam> getAllExams() {
      ArrayList<Exam> arrayList = new ArrayList<Exam>();
       
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            conn = Utility.getConnection();
            statement = conn.prepareStatement("Select id, description, date, timelimit  From exam order by id desc ");

            rs = statement.executeQuery();

            while(rs.next())
            {
               Exam exam = new Exam();
               exam.ID=rs.getInt("id");
               exam.description=rs.getString("description");
                exam.dateTime=new Date(rs.getDate("date").getTime());
                exam.timeLimit=Duration.ofSeconds(rs.getLong("timelimit"));
                arrayList.add(exam);
            }

        } catch (SQLException e) {
            System.out.println("get all exam error");
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


    int ID;
    String description;
    Date dateTime;
    List<Problem> problems= new ArrayList<Problem>();
    Duration timeLimit;

    public int getID() {
        return ID;
    }
    
    public String getDescription() {
         timeLimit=Duration.ofMinutes(120);
        return description;
        
       
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date dateWithoutTime = sdf.parse(sdf.format(dateTime));
            this.dateTime = dateWithoutTime;
        } catch (Exception e) {
            System.out.println("Exam date format is not valid.");
        }
        
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
        
       
        Connection conn = null;
        PreparedStatement statement = null;
        try {
            conn = Utility.getConnection();
            conn.setAutoCommit(false);
            
             statement = conn.prepareStatement("delete from  Exam where id=?");
            statement.setInt(1, id);
            
            statement.executeUpdate();
            statement.close();
            
            
            statement=conn.prepareStatement("delete from exam_problem where examid=?");
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
        
        Connection conn = null;
        PreparedStatement statement = null;
        try {
            conn = Utility.getConnection();
            conn.setAutoCommit(false);
            statement = conn.prepareStatement("Insert into Exam ( description, date, timelimit) Values(?,?,?)", Statement.RETURN_GENERATED_KEYS);
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
                statement=conn.prepareStatement("Insert into exam_problem (examid,problemid) Values(?,?)");
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
      
        Connection conn = null;
        PreparedStatement statement = null;
        try {
            conn = Utility.getConnection();
            conn.setAutoCommit(false);
            
             statement = conn.prepareStatement("update Exam set description=?, date=? , timelimit=? where id=?");
            statement.setString(1,this.description);
            statement.setDate(2, new  java.sql.Date(this.dateTime.getTime()));
            statement.setLong(3, this.timeLimit.getSeconds());
            statement.setInt(4, this.ID);
            
            statement.executeUpdate();
            statement.close();
            
            
            statement=conn.prepareStatement("delete from exam_problem where examid=?");
            statement.setInt(1, this.ID);
            statement.executeUpdate();
            
             for(int i=0;i<this.problems.size();i++){
                int problemId=this.problems.get(i).getID();
                statement=conn.prepareStatement("Insert into exam_problem (examid,problemid) Values(?,?)");
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
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            conn = Utility.getConnection();
            statement = conn.prepareStatement("Select description,date,timelimit from exam Where id=?");
            statement.setInt(1, id);
            rs = statement.executeQuery();

            if (rs.next()) {
                this.description=rs.getString("description");
                this.dateTime=new Date(rs.getDate("date").getTime());
                this.timeLimit=Duration.ofSeconds(rs.getLong("timelimit"));
                this.ID=id;
            } else return false;
            statement.close();
            
//            statement=conn.prepareStatement("Select score from reportscore Where id=? order by index");
//              statement.setInt(1, id);
//            rs = statement.executeQuery();
            
            
            List problemList=this.getProblems();
            problemList.clear();
            problemList.addAll(getProblemsOfExam(id, conn));            
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

    private Collection getProblemsOfExam(int id, Connection conn) throws SQLException {
        PreparedStatement statement = conn.prepareStatement("Select problemid from exam_problem Where examid=? order by problemid");
        statement.setInt(1, id);
        ResultSet rs = statement.executeQuery();
        List<Problem> problemList = new ArrayList<Problem>();
        while (rs.next()) {
            Problem problem=new Problem();
            problem.ID=rs.getInt(1);
            problemList.add(problem);
        }
        
        for (Problem problem:problemList) {
            problem.get(problem.getID());
        }
        return problemList;
    }

    
    @Override
    public String toString() {
        return this.description;
    }


    
}
