/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ExamSys.backend;

import java.sql.*;
import java.text.SimpleDateFormat;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Andy
 */
public class Exam implements IDBRecord {

    public static List<Exam> getAllExams() {
        ArrayList<Exam> arrayList = new ArrayList<>();

        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            conn = Utility.getConnection();
            statement = conn.prepareStatement("Select id, description, date, timelimit  From exam "
                    + "Where deleted=0 order by id desc ");

            rs = statement.executeQuery();

            while (rs.next()) {
                Exam exam = new Exam();
                exam.ID = rs.getInt("id");
                exam.description = rs.getString("description");
                exam.dateTime = new Date(rs.getDate("date").getTime());
                exam.timeLimit = Duration.ofSeconds(rs.getLong("timelimit"));
                arrayList.add(exam);

                exam.getProblems().clear();
                exam.getProblems().addAll(exam.getProblemsOfExam(exam.ID, conn));
                
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

    public static List<Exam> getAllAvailableExams(User user) {
        ArrayList<Exam> arrayList = new ArrayList<>();

        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            conn = Utility.getConnection();
            statement = conn.prepareStatement("Select distinct id,description,date,timeLimit from examreport_view " +
                    "where deleted=0  and (reportid is null or studentid<>?) "
                    + " order by id desc ");
            statement.setString(1, user.getUserName());
            rs = statement.executeQuery();

            while (rs.next()) {
                Exam exam = new Exam();
                exam.ID = rs.getInt("id");
                exam.description = rs.getString("description");
                exam.dateTime = new Date(rs.getDate("date").getTime());
                exam.timeLimit = Duration.ofSeconds(rs.getLong("timelimit"));
                arrayList.add(exam);

                exam.getProblems().clear();
                exam.getProblems().addAll(exam.getProblemsOfExam(exam.ID, conn));
                
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
    Date dateTime=new Date();
    List<ProblemScore> problems = new ArrayList<>();
    Duration timeLimit;
    //HashMap<Problem,Integer> scores=new HashMap<>();

    public int getID() {
        return ID;
    }

    public String getDescription() {
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

    public List<ProblemScore> getProblems() {
        return this.problems;
    }

    public Duration getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(Duration timeLimit) {
        this.timeLimit = timeLimit;
    }

    static Exam getExamByID(int examID) {
        Exam exam = new Exam();
        if (exam.get(examID)) {
            return exam;
        } else {
            return null;
        }

    }

    public static boolean removeByID(int id) {

        Connection conn = null;
        PreparedStatement statement = null;
        try {
            conn = Utility.getConnection();

            statement = conn.prepareStatement("update  Exam Set deleted=1 where id=?");
            statement.setInt(1, id);

            statement.executeUpdate();
            statement.close();
            

        } catch (SQLException e) {
            
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
    public boolean create() {

        Connection conn = null;
        PreparedStatement statement = null;
        try {
            conn = Utility.getConnection();
            conn.setAutoCommit(false);
            statement = conn.prepareStatement("Insert into Exam ( description, date, timelimit) Values(?,?,?)", Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, this.description);
            statement.setDate(2, new java.sql.Date(this.dateTime.getTime()));
            statement.setLong(3, this.timeLimit.getSeconds());

            statement.executeUpdate();
            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()) {
                this.ID = rs.getInt(1);
            }
            statement.close();

            for (int i = 0; i < this.problems.size(); i++) {
                int problemId = this.problems.get(i).problem.getID();
                int score=this.problems.get(i).score;
                statement = conn.prepareStatement("Insert into exam_problem (examid,problemid,score,idx) Values(?,?,?,?)");
                statement.setInt(1, this.ID);
                statement.setInt(2, problemId);
                statement.setInt(3, score);
                statement.setInt(4,i);
                statement.executeUpdate();
                statement.close();
                
                statement.close();
            }
            conn.commit();
        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (Exception ee) {

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
            statement.setString(1, this.description);
            statement.setDate(2, new java.sql.Date(this.dateTime.getTime()));
            statement.setLong(3, this.timeLimit.getSeconds());
            statement.setInt(4, this.ID);

            statement.executeUpdate();
            statement.close();

            statement = conn.prepareStatement("delete from exam_problem where examid=?");
            statement.setInt(1, this.ID);
            statement.executeUpdate();

            for (int i = 0; i < this.problems.size(); i++) {
                int problemId = this.problems.get(i).problem.getID();
                int score=this.problems.get(i).score;
                statement = conn.prepareStatement("Insert into exam_problem (examid,problemid,score,idx) Values(?,?,?,?)");
                statement.setInt(1, this.ID);
                statement.setInt(2, problemId);
                statement.setInt(3,score);
                statement.setInt(4,i);
                statement.executeUpdate();
                statement.close();
            }
            conn.commit();

        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (Exception ee) {

            }
            System.out.println("update exam error");
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
                this.description = rs.getString("description");
                this.dateTime = new Date(rs.getDate("date").getTime());
                this.timeLimit = Duration.ofSeconds(rs.getLong("timelimit"));
                this.ID = id;
            } else {
                return false;
            }
            statement.close();

            
            this.getProblems().clear();
            this.getProblems().addAll(getProblemsOfExam(id, conn));
            
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

    private Collection<ProblemScore> getProblemsOfExam(int id, Connection conn) throws SQLException {
        PreparedStatement statement = conn.prepareStatement("Select problemid,score from exam_problem Where examid=? order by idx");
        statement.setInt(1, id);
        ResultSet rs = statement.executeQuery();
        List<ProblemScore> problemList = new ArrayList<>();
        while (rs.next()) {
            ProblemScore problemScore = new ProblemScore(new Problem(),0);
            problemScore.problem=new Problem();
            problemScore.problem.ID = rs.getInt(1);
            problemScore.score=rs.getInt(2);
            problemList.add(problemScore);
        }

        for (ProblemScore problemScore : problemList) {
            problemScore.problem.get(problemScore.problem.getID());
        }
        return problemList;
    }

    @Override
    public String toString() {
        return this.description; //To change body of generated methods, choose Tools | Templates.
    }

    public boolean isReadOnly() {
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            conn = Utility.getConnection();
            statement = conn.prepareStatement("Select count(*) from report Where examid=?");
            statement.setInt(1, this.ID);
            rs = statement.executeQuery();

            int count=0;
            if (rs.next()) {
                count=rs.getInt(1);
            } else {
                return false;
            }
            statement.close();

            
            return count!=0;
            
        } catch (SQLException e) {
            System.out.println("get exam.readOnly() error");
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
    }
}
