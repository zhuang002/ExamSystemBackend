/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ExamSys.backend;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.imageio.ImageIO;

/**
 *
 * @author Andy
 */
public class Problem implements IDBRecord {

    public static List<Problem> getAllProblems() {
        ArrayList<Problem> arrayList = new ArrayList<>();

        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            conn = Utility.getConnection();
            statement = conn.prepareStatement("Select id, answer, name  From problem Where deleted=0 order by id ");

            rs = statement.executeQuery();

            while (rs.next()) {
                Problem problem = new Problem();
                problem.ID = rs.getInt("id");
                problem.answer = (char) rs.getByte("answer");
                problem.name = rs.getString("name");
                List sectionList = problem.sections;
                sectionList.clear();
                sectionList.addAll(problem.getSectionsOfProblem(problem.ID, conn));
                arrayList.add(problem);
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
    char answer;
    String name;
    List<ProblemSection> sections = new ArrayList();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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

        Connection conn = null;
        PreparedStatement statement = null;
        try {
            conn = Utility.getConnection();
            conn.setAutoCommit(false);
            statement = conn.prepareStatement("Insert into problem ( name, answer ) Values(?,?)", PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setString(1, name);
            statement.setInt(2, this.answer);

            statement.executeUpdate();
            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()) {
                this.ID = rs.getInt(1);
            }
            statement.close();

            for (int i = 0; i < this.sections.size(); i++) {
                ProblemSection problemSection = this.sections.get(i);
                statement = conn.prepareStatement("Insert into problemsection (problemid, sectionindex,text ,picpath) Values(?,?,?,?)");
                statement.setInt(1, this.ID);
                statement.setInt(2, i);
                statement.setString(3, problemSection.text);

                if (problemSection.pic != null) {
                    String picPath = generateRandomFileName();
                    ImageIO.write(problemSection.pic, "png", new File(picPath));
                    statement.setString(4, picPath);
                } else {
                    statement.setString(4, null);
                }

                statement.executeUpdate();
                statement.close();
            }
            conn.commit();
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (Exception ee) {

            }
            System.out.println("create exam error");
            System.out.println(e.getMessage());
            System.out.println(e.getStackTrace());
            return false;
        } catch (IOException e) {
            try {
                conn.rollback();
            } catch (Exception ee) {

            }
            System.out.println("write picture error");
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

            statement = conn.prepareStatement("update problem set answer=?, name=? where id=?");
            statement.setInt(1, this.answer);
            statement.setString(2, name);
            statement.setInt(3, this.ID);
            statement.executeUpdate();
            statement.close();

            statement = conn.prepareStatement("delete from problemsection where problemid=?");
            statement.setInt(1, this.ID);
            statement.executeUpdate();
            statement.close();

            for (int i = 0; i < this.sections.size(); i++) {
                ProblemSection problemSection = this.sections.get(i);
                statement = conn.prepareStatement("Replace into problemsection (problemid, sectionindex,text ,picpath ) Values(?,?,?,?)");
                statement.setInt(1, this.ID);
                statement.setInt(2, i);
                statement.setString(3, problemSection.text);

                if (problemSection.pic != null) {
                    String picPath = generateRandomFileName();
                    ImageIO.write(problemSection.pic, "png", new File(picPath));
                    statement.setString(4, picPath);
                } else {
                    statement.setString(4, null);
                }

                statement.executeUpdate();
                statement.close();
            }

            conn.commit();

        } catch (SQLException e) {
            try {
                if (conn!=null)
                    conn.rollback();
            } catch (Exception ee) {

            }
            System.out.println("update exam error");
            System.out.println(e.getMessage());
            System.out.println(e.getStackTrace());
            return false;
        } catch (IOException e) {
            try {
                if (conn!=null)
                    conn.rollback();
            } catch (Exception ee) {

            }
            System.out.println("write picture error");
            System.out.println(e.getMessage());
            System.out.println(e.getStackTrace());
            return false;
        } finally {
            try {
                if (conn!=null)
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

    public static boolean remove(int id) {

        Connection conn = null;
        PreparedStatement statement = null;
        try {
            conn = Utility.getConnection();

            statement = conn.prepareStatement("Update problem Set deleted=1 where id=?");
            statement.setInt(1, id);

            statement.executeUpdate();
            statement.close();


        } catch (SQLException e) {
            try {
                if (conn!=null)
                    conn.rollback();
            } catch (Exception ee) {

            }
            System.out.println("remove exam error");
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
        return remove(this.ID);

    }

    @Override
    public boolean get(int id) {

        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            conn = Utility.getConnection();
            statement = conn.prepareStatement("Select answer,name from problem where deleted=0 and id =?");
            statement.setInt(1, id);
            rs = statement.executeQuery();

            if (rs.next()) {
                this.ID = id;
                this.answer = (char) rs.getByte("answer");
                this.name = rs.getString("name");
                List sectionList = this.sections;
                sectionList.clear();
                sectionList.addAll(getSectionsOfProblem(id, conn));

            } else {
                return false;
            }
            statement.close();

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
        PreparedStatement statement = conn.prepareStatement("Select text, picpath  from problemsection Where problemid=? order by sectionindex");
        statement.setInt(1, id);
        ResultSet rs = statement.executeQuery();

        List sectionList = new ArrayList<Integer>();
        sectionList.clear();
        while (rs.next()) {
            ProblemSection section = new ProblemSection();
            section.text = rs.getString(1);
            String filePath = rs.getString(2);
            if (filePath != null) {
                try {
                    section.pic = ImageIO.read(new File(filePath));
                } catch (IOException e) {
                    System.err.println("Cannot read image file  ");
                    section.pic = null;
                }
            }
            sectionList.add(section);
        }
        return sectionList;

    }

    private String generateRandomFileName() {

        long time = new Date().getTime();
        return ".\\img\\" + Long.toHexString(time) + ".png";

    }

    @Override
    public String toString() {
        return this.name;
    }

}
