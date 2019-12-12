/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/*import com.ExamSys.backend.Role;
import com.ExamSys.backend.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;*/

package com.ExamSys.backend;

import org.junit.After;
import static org.junit.Assert.*;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Andy
 */
public class UserTest {

    public UserTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
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
    public void test_user_cmr() {
        User user = new User();
        user.setUserName("andy001");
        user.setName("Andy Hou");
        user.setPassword("password");
        user.setRole(Role.Admin);
        
        int a=20;

        assertTrue(user.create());
        assertFalse(user.create());
        
        

        user = User.getUserByUsername("andy002");
        assertNull(user);

        user = User.getUserByUsername("andy001");
        assertEquals(user.getUserName(), "andy001");
        assertEquals(user.getName(), "Andy Hou");
        assertEquals(user.getRole(), Role.Admin);

        user.setRole(Role.Student);
        user.update();
        user = User.getUserByUsername("andy001");
        assertEquals(user.getRole(), Role.Student);

        user.remove();
        user = User.getUserByUsername("andy001");
        assertNull(user);

    }

    @Test
    public void test_user_login() {
        User user = new User();
        user.setUserName("andy001");
        user.setName("Andy Hou");
        user.setPassword("password");
        user.setRole(Role.Admin);
        user.create();

        user = User.login("andy001", "passwordWrong");
        assertNull(user);

        user = User.login("andy002", "password");
        assertNull(user);
        
         user = User.login("andy001", "password");
        assertNotNull(user);
        assertEquals(user.getUserName(), "andy001");
         assertEquals(user.getName(), "Andy Hou"); 
         assertEquals(user.getRole(), Role.Admin);
         
        
        
    }
}
