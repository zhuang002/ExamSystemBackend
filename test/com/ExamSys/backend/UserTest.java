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
import java.sql.*;
import java.util.List;

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
        Connection conn = null;
        PreparedStatement statement = null;
        try {
            conn = Utility.getConnection();
            statement = conn.prepareStatement("delete from user");
            statement.executeUpdate();
        } catch (Exception e) {
            System.out.println("User test setup error");
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

        User user2 = new User();
        user2.setUserName("andy002");
        user2.setName("Andy Hou Admin");
        user2.setPassword("password");
        user2.setRole(Role.Admin);
        user2.create();

        User user3 = new User();
        user3.setUserName("andy003");
        user3.setName("Andy Hou Admin2");
        user3.setPassword("password");
        user3.setRole(Role.Admin);
        user3.create();

        List<User> list = User.getAllUsers(Role.Admin);
        assertEquals(list.size(), 2);
        assertEquals(user2.getName(), list.get(0).getName());
        assertEquals(user3.getName(), list.get(1).getName());

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

        user.remove();

    }
}
