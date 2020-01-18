/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ExamSys.backend;

import java.sql.*;
import java.time.Duration;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Date;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.util.List;
/**
 *
 * @author Andy
 */
public class ExamTest {

    public ExamTest() {
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
    public void test_Exam_crm() {
        Exam exam1= new Exam();
        
        Date date=new Date();
        exam1.setDateTime(date);
        exam1.setDescription("Exam 1");
        exam1.setTimeLimit(Duration.ofHours(1));
        
        Problem problem1=new Problem();
        problem1.setAnswer('A');
        problem1.setName("problem 1");
        
        ProblemSection section1=new ProblemSection();
        section1.setText("ps 1.1");
        
        ProblemSection section2=new ProblemSection();
        section2.setText("ps 1.2");
        
        problem1.getSections().add(section1);
        problem1.getSections().add(section2);
        problem1.create();
        
        exam1.getProblems().add(new ProblemScore(problem1,1));
        
        
        Problem problem2=new Problem();
        problem2.setAnswer('B');
        problem2.setName("problem 2");
        
        
        ProblemSection section3=new ProblemSection();
        section3.setText("ps 2.1");
        
        ProblemSection section4=new ProblemSection();
        section4.setText("ps 2.2");
        
        problem2.getSections().add(section3);
        problem2.getSections().add(section4);
        problem2.create();
        
        exam1.getProblems().add(new ProblemScore(problem2,2));
        
        assertTrue(exam1.create());
        
        
        Exam exam2= new Exam();
        
        date=new Date();
        exam2.setDateTime(date);
        exam2.setDescription("Exam 1");
        exam2.setTimeLimit(Duration.ofHours(1));
        
        problem1=new Problem();
        problem1.setAnswer('c');
        problem1.setName("problem 3");
        
        section1=new ProblemSection();
        section1.setText("ps 3.1");
        
        section2=new ProblemSection();
        section2.setText("ps 3.2");
        
        problem1.getSections().add(section1);
        problem1.getSections().add(section2);
        problem1.create();
        
        exam2.getProblems().add(new ProblemScore(problem1,3));
        
        
        problem2=new Problem();
        problem2.setAnswer('d');
        problem2.setName("problem 4");
        
        
        section3=new ProblemSection();
        section3.setText("ps 4.1");
        
        section4=new ProblemSection();
        section4.setText("ps 4.2");
        
        problem2.getSections().add(section3);
        problem2.getSections().add(section4);
        problem2.create();
        
        exam2.getProblems().add(new ProblemScore(problem2,4));
        
        exam2.create();
        
        List<Exam> list = Exam.getAllExams();
        assertEquals(list.size(), 2);
        assertEquals(list.get(0).getDescription(),exam2.getDescription());
        assertEquals(list.get(1).getDescription(),exam1.getDescription());
        
        
        exam2=new Exam();
        assertTrue(exam2.get(exam1.getID()));
        assertEquals(exam1.getID(),exam2.getID());
        assertEquals(exam1.getDescription(),exam2.getDescription());
        assertEquals(exam1.getDateTime(),exam2.getDateTime());
        assertEquals(exam1.getTimeLimit().getSeconds(),exam2.getTimeLimit().getSeconds());
        
        assertEquals(exam2.getProblems().size(),2);
        assertEquals(exam2.getProblems().get(0).problem.getSections().size(), 2);
        assertEquals(exam2.getProblems().get(1).problem.name,"problem 2");
        assertEquals(exam2.getProblems().get(1).problem.getSections().get(0).getText(),"ps 2.1");
        
        try {
            exam1.setDateTime(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2000"));
        } catch (Exception e) {
            System.out.println("Should not be here.");
        }
        exam1.problems.remove(0);
        exam1.update();
        
        exam2.get(exam1.getID());
        assertEquals(exam1.getDateTime(),exam2.getDateTime());
        assertEquals(exam2.getProblems().size(),1);
        assertEquals(exam2.getProblems().get(0).problem.name, "problem 2");
        
        
        exam2.remove();
        assertTrue(exam2.get(exam1.getID()));
        assertEquals(Exam.getAllExams().size(),1);
    }
    
   
}
