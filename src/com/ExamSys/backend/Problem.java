/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ExamSys.backend;

import com.mysql.cj.jdbc.Blob;
import com.mysql.cj.jdbc.MysqlDataSource;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;


/**
 *
 * @author Andy
 */
public class Problem implements IDBRecord{
    int ID;
    char answer;
    List<ProblemSection> sections=new ArrayList();

    
    
    public int getID() {
        return ID;
    }

    public char getAnswer() {
        return answer;
    }

    public void setAnswer(char answer) {
        this.answer = answer;
    }

    public List<ProblemSection> getSections() {
        return sections;
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
            statement = conn.prepareStatement("Insert into problem ( answer ) Values(?)");
            statement.setInt(1, this.answer);
           
            
            statement.executeUpdate(); 
            ResultSet rs=statement.getGeneratedKeys();
            if(rs.next()){
                this.ID=rs.getInt(1);
            }
            statement.close();
            
            for(int i=0;i<this.sections.size();i++){
               ProblemSection problemSection =this.sections.get(i);
                statement=conn.prepareStatement("Insert into problemsection (problemid, index,text ,picpath ) Values(?,?,?,?)");
                statement.setInt(1, this.ID);
                statement.setInt(2, i);
                statement.setString(3, problemSection.text);
                
                if(problemSection.pic!=null){
                    String picPath=generateRandomFileName();
                ImageIO.write(problemSection.pic, "png", new File(picPath));
                statement.setString(4, picPath);
                }else{
                    statement.setString(4, null);
                }
                
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
        }catch (IOException e){
              try{
                conn.rollback();
            }catch(Exception ee){
                
            }
            System.out.println("write picture error");
            System.out.println(e.getMessage());
            System.out.println(e.getStackTrace());
            return false;
        } 
        finally {
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
            
             statement = conn.prepareStatement("update problem set answer=? where id=?");
            statement.setInt(1, this.answer);
            statement.setInt(2, this.ID);
            statement.executeUpdate();
            statement.close();
            
            
            statement=conn.prepareStatement("delete problemsection where problemid=?");
            statement.setInt(1, this.ID);
            
             for(int i=0;i<this.sections.size();i++){
               ProblemSection problemSection =this.sections.get(i);
                statement=conn.prepareStatement("Insert into problemsection (problemid, index,text ,picpath ) Values(?,?,?,?)");
                statement.setInt(1, this.ID);
                statement.setInt(2, i);
                statement.setString(3, problemSection.text);
                
                if(problemSection.pic!=null){
                    String picPath=generateRandomFileName();
                ImageIO.write(problemSection.pic, "png", new File(picPath));
                statement.setString(4, picPath);
                }else{
                    statement.setString(4, null);
                }
                
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
        }catch (IOException e){
              try{
                conn.rollback();
            }catch(Exception ee){
                
            }
            System.out.println("write picture error");
            System.out.println(e.getMessage());
            System.out.println(e.getStackTrace());
            return false;
        } 
        
        finally {
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
            
             statement = conn.prepareStatement("delete problem where id=?");
            statement.setInt(1, id);
            
            statement.executeUpdate();
            statement.close();
            
            
            statement=conn.prepareStatement("delete problemsection where examid=?");
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
    public boolean remove() {
        return remove(this.ID);
        
        
    }

    @Override
    public boolean get(int id) {        
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
            statement = conn.prepareStatement("Select answer from problem where id =?");
            statement.setInt(1, id);
            rs = statement.executeQuery();

            if (rs.next()) {
                this.ID=id;
                this.answer=(char)rs.getInt("answer");
                
            }
            statement.close();
            
            

            
            List sectionList=this.sections;
            sectionList.clear();
            sectionList.addAll(getSectionsOfProblem(id, conn));            
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

    private Collection getSectionsOfProblem(int id, Connection conn) throws SQLException {
        PreparedStatement statement=conn.prepareStatement("Select text, picpath  from problemsection Where id=? order by index");
              statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            
            
            List sectionList=new ArrayList<Integer>();
            sectionList.clear();
            while(rs.next()){
                ProblemSection section =new ProblemSection();
                section.text=rs.getString(1);
                String filePath=rs.getString(2);
                if(filePath!=null){
                    try{
                        section.pic=ImageIO.read(new File(filePath));
                    }catch(IOException e){
                        System.err.println("Cannot read image file  ");
                        section.pic=null;
                    }
                    
                    
                      
                }
            }
            return sectionList;

    }

    private String generateRandomFileName() {
        
        long time = new Date().getTime();
        return ".\\img\\"+Long.toHexString(time)+ ".png";
        
        
        
        
        
        
    }

    

    
    
    
    
    
    
}
