/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ExamSys.backend;

import com.mysql.cj.xdevapi.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
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
public class ProblemTest {

    public ProblemTest() {
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
    public void test_ProblemSection_cmr() {
        Problem problem = new Problem();

        problem.setAnswer('a');
        problem.setName("problem1");

        ProblemSection section = new ProblemSection();
        section.setText("section 1 text");
        problem.getSections().add(section);

        section = new ProblemSection();
        section.setText("section 2 text");
        problem.getSections().add(section);

        assertTrue(problem.create());

        int id = problem.getID();

        problem = new Problem();

        problem.setAnswer('a');
        problem.setName("problem2");

        section = new ProblemSection();
        section.setText("111 section 1 text");
        problem.getSections().add(section);

        section = new ProblemSection();
        section.setText("111 section 2 text");
        problem.getSections().add(section);

        assertTrue(problem.create());

        assertFalse(problem.get(-1));
        assertTrue(problem.get(id));

        assertEquals(problem.getName(), "problem1");
        assertEquals(problem.answer, 'a');
        assertEquals(2, problem.getSections().size());
        assertEquals(problem.getSections().get(0).getText(), "section 1 text");
        assertEquals(problem.getSections().get(1).getText(), "section 2 text");
        
        problem.getSections().get(1).setText("section 2 text modified");
        problem.setAnswer('b');
        problem.update();
        
        assertTrue(problem.get(id));
        assertEquals(problem.getName(), "problem1");
        assertEquals(problem.answer, 'b');
        assertEquals(2, problem.getSections().size());
        assertEquals(problem.getSections().get(0).getText(), "section 1 text");
        assertEquals(problem.getSections().get(1).getText(), "section 2 text modified");
        
        assertTrue(problem.remove());
        assertFalse(problem.get(id));

    }
}
