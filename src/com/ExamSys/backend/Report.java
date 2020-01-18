/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ExamSys.backend;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Collection;

/**
 *
 * @author Andy
 */
public class Report implements IDBRecord {

    int ID;
    int examID;
    ArrayList<int[]> scoreList = new ArrayList<>();

    String studentUserName = null;
    int totalScore = 0;
    Date dateTime;

    
    public int getID() {
        return this.ID;
    }


    public Exam getExam() {
        return Exam.getExamByID(this.examID);
    }

    public void setExam(Exam exam) {
        this.examID = exam.getID();
    }

    private void setExam(int examid) {
        this.examID = examid;
    }

    public List<int[]> getScoreList() {
        return this.scoreList;
    }

    public int getScore() {
        int total = 0;
        for (int[] score : scoreList) {
            total += score[0];
        }
        return total;
    }

    public User getStudent() {
        return User.getUserByUsername(this.studentUserName);
    }

    public void setStudent(User student) {
        this.studentUserName = student.getUserName();
    }

    private void setStudent(String studentUserName) {
        this.studentUserName = studentUserName;
    }

    public Date getDate() {
        return this.dateTime;
    }

    public void setDate(Date dateTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date dateWithoutTime = sdf.parse(sdf.format(dateTime));
            this.dateTime = dateWithoutTime;
        } catch (Exception e) {
            System.out.println("Exam date format is not valid.");
        }
    }

    @Override
    public boolean create() {

        Connection conn = null;
        PreparedStatement statement = null;
        try {
            conn = Utility.getConnection();
            conn.setAutoCommit(false);
            statement = conn.prepareStatement("Insert into report (examid,student,date) Values(?,?,?)",Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, this.examID);
            statement.setString(2, this.studentUserName);
            statement.setDate(3, new java.sql.Date(this.dateTime.getTime()));

            statement.executeUpdate();
            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()) {
                this.ID = rs.getInt(1);
            }
            statement.close();

            for (int i = 0; i < scoreList.size(); i++) {
                int score = this.scoreList.get(i)[0];
                int answer = this.scoreList.get(i)[1];
                statement = conn.prepareStatement("Insert into reportscore (idx,report,score,answer) Values(?,?,?,?)");
                statement.setInt(1, i);
                statement.setInt(2, this.ID);
                statement.setInt(3, score);
                statement.setInt(4, answer);
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
            System.out.println("create report error");
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

    @Override
    public boolean update() {

        Connection conn = null;
        PreparedStatement statement = null;
        try {
            conn = Utility.getConnection();
            conn.setAutoCommit(false);

            for (int i = 0; i < scoreList.size(); i++) {
                int[] score = this.scoreList.get(i);
                
                statement = conn.prepareStatement("update reportscore set score=? where idx=? And report=?");
                statement.setInt(1, score[0]);
                statement.setInt(2, i);
                statement.setInt(3, this.ID);
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
            System.out.println("update report error");
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

    @Override
    public boolean remove() {
        return remove(this.ID);
    }

    public static boolean remove(int id) {

        Connection conn = null;
        PreparedStatement statement = null;
        try {
            conn = Utility.getConnection();
            statement = conn.prepareStatement("Update report Set deleted=1 where id=?");
            statement.setInt(1, id);
            statement.executeUpdate();
            statement.close();

        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (Exception ee) {

            }
            System.out.println("remove report error");
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
    public boolean get(int id) {
//        Report report = new Report();

        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            conn = Utility.getConnection();
            statement = conn.prepareStatement("Select examid, student,date from report Where deleted=0 And id=? ");
            statement.setInt(1, id);
            rs = statement.executeQuery();

            if (rs.next()) {
                this.ID=id;
                this.setExam(rs.getInt("examid"));
                this.setStudent(rs.getString("student"));
                this.setDate(new Date(rs.getDate("date").getTime()));
            }
            statement.close();

            this.scoreList.clear();
            this.scoreList.addAll(getScoreOfReport(id, conn));
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

    public static List<Report> getReportsByStudentName(String StudentUsername) {
        ArrayList<Report> ret = new ArrayList();

        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            conn = Utility.getConnection();
            statement = conn.prepareStatement("Select id,examid,date from report Where deleted=0 And student=? ");
            statement.setString(1, StudentUsername);
            rs = statement.executeQuery();

            while (rs.next()) {
                Report report = new Report();
                report.ID=rs.getInt("id");
                report.setExam(rs.getInt("examid"));
                report.setStudent(StudentUsername);
                report.setDate(new Date(rs.getDate("date").getTime()));
                ret.add(report);

            }
            statement.close();

            for (Report report : ret) {
                List<int[]> scoreList = report.getScoreList();
                scoreList.clear();
                scoreList.addAll(getScoreOfReport(report.getID(), conn));

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

    private static Collection<int[]> getScoreOfReport(int id, Connection conn) throws SQLException {
        PreparedStatement statement = conn.prepareStatement("Select score,answer from reportscore Where report=? order by idx");
        statement.setInt(1, id);
        ResultSet rs = statement.executeQuery();
        List<int[]> scoreList = new ArrayList<>();
        scoreList.clear();
        while (rs.next()) {
            int[] sa=new int[2];
            sa[0]=rs.getInt("score");
            sa[1]=rs.getInt("answer");
            scoreList.add(sa);
        }
        return scoreList;
    }

    public static Report getReportByID(int id) {
        Report report = new Report();
        if (report.get(id)) {
            return report;
        } else {
            return null;
        }
    }

    @Override
    public String toString() {
        SimpleDateFormat format=new SimpleDateFormat("MM-dd-yyyy");
        return this.getExam().toString()+"@"+format.format(this.getDate());
    }

}
