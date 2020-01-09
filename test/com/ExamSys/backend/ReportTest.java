/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ExamSys.backend;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Andy
 */
public class ReportTest {
    
    public ReportTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        
        Connection conn=null;
        PreparedStatement statement=null;
        try {
            conn=Utility.getConnection();
            statement = conn.prepareStatement("delete from problem");
            statement.executeUpdate();
            statement.close();
            
            statement = conn.prepareStatement("delete from problemsection");
            statement.executeUpdate();
            statement.close();
            
            statement = conn.prepareStatement("delete from exam_problem");
            statement.executeUpdate();
            statement.close();
            
            statement = conn.prepareStatement("delete from exam");
            statement.executeUpdate();
            statement.close();
            
            statement = conn.prepareStatement("delete from user");
            statement.executeUpdate();
            statement.close();
            
            statement = conn.prepareStatement("delete from reportscore");
            statement.executeUpdate();
            statement.close();
            
            statement = conn.prepareStatement("delete from report");
            statement.executeUpdate();
               
        } catch (Exception e) {
            System.out.println("User test setup error");
        } finally {
            try {
            if (statement!=null) statement.close();
            if (conn!=null) conn.close();
            } catch (Exception e) {
            }
        }
    }
    
    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
    
    @Test
    public void test_report_cmr() {
        ArrayList<Exam> exams=createExams();
        ArrayList<User> users=createUsers();
        
        Report report=new Report();
        report.setDate(new Date());
        report.setExam(exams.get(0));
        report.setStudent(users.get(0));
        
        report.getScoreList().add(2);
        report.getScoreList().add(3);
        
        assertTrue(report.create());
        
        Report report2=new Report();
        assertTrue(report2.get(report.getID()));
        assertEquals(report.getDate(), report2.getDate());
        assertEquals(report2.getExam().getID(), exams.get(0).getID());
        assertEquals(report2.getStudent().getUserName(), users.get(0).getUserName());
        
        assertEquals(report2.getScore(), 5);
        int score=report2.getScoreList().get(0);
        assertEquals(score, 2);
        score=report2.getScoreList().get(1);
        assertEquals(score, 3);
        
        Exam exam1=exams.get(0);
        Exam exam2=report2.getExam();
        assertEquals(exam1.getDateTime(),exam2.getDateTime());
        assertEquals(exam1.getProblems().size(), exam2.getProblems().size());
        assertEquals(exam1.getProblems().get(0).getName(),exam2.getProblems().get(0).getName());
        
        User user1=users.get(0);
        User user2=report.getStudent();
        assertEquals(user1.getUserName(), user2.getUserName());
        assertEquals(user1.getRole(), user2.getRole());
        assertEquals(user1.getName(), user2.getName());

        
    }

    private ArrayList<Exam> createExams() {
        ArrayList<Exam> ret=new ArrayList();
        
        Exam exam1= new Exam();
        
        Date date=new Date();
        exam1.setDateTime(date);
        exam1.setDescription("Exam 1");
        exam1.setTimeLimit(Duration.ofHours(1));
        
        Problem problem1=new Problem();
        problem1.setAnswer('a');
        problem1.setName("problem 1");
        
        ProblemSection section1=new ProblemSection();
        section1.setText("ps 1.1");
        
        ProblemSection section2=new ProblemSection();
        section2.setText("ps 1.2");
        
        problem1.getSections().add(section1);
        problem1.getSections().add(section2);
        problem1.create();
        
        exam1.getProblems().add(problem1);
        
        
        Problem problem2=new Problem();
        problem2.setAnswer('b');
        problem2.setName("problem 2");
        
        
        ProblemSection section3=new ProblemSection();
        section3.setText("ps 2.1");
        
        ProblemSection section4=new ProblemSection();
        section4.setText("ps 2.2");
        
        problem2.getSections().add(section3);
        problem2.getSections().add(section4);
        problem2.create();
        
        exam1.getProblems().add(problem2);
        
        exam1.create();
        
        ret.add(exam1);
        return ret;
        
    }

    private ArrayList<User> createUsers() {
        ArrayList<User> ret=new ArrayList();
        
        User user=new User();
        user.setName("Andy Hou");
        user.setPassword("password");
        user.setRole(Role.Student);
        user.setUserName("andy001");
        user.create();
        
        ret.add(user);
        return ret;
    }
    
    
}
