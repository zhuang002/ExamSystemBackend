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
 * The class representing an Exam.
 * @author Andy
 */
public class Exam implements IDBRecord {

    /**
     * The static method to get all exams.
     * @return A list of all Exams that has not been deleted.
     */
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

    /**
     * Get the available exams that the student can take. 
     * The available exams including all undeleted exams that has not been taken by the student.
     * @param user the student.
     * @return A list of available exams for the student.
     */
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

    /**
     * Get the exam id.
     * @return the integer representing the id of the exam.
     */
    public int getID() {
        return ID;
    }

    /**
     * Get the description of the exam.
     * @return The string describe the exam.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set the description of the exam.
     * @param description The description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Get the date of the exam. default is the date that the exam is created.
     * @return the date of the exam.
     */
    public Date getDateTime() {
        return dateTime;
    }

    /**
     * Set the date for the exam.
     * @param dateTime The date of the exam. 
     */
    public void setDateTime(Date dateTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date dateWithoutTime = sdf.parse(sdf.format(dateTime));
            this.dateTime = dateWithoutTime;
        } catch (Exception e) {
            System.out.println("Exam date format is not valid.");
        }

    }

    /**
     * Get the problems of the exam.
     * @return A list of problems of the exam. The entry of the list is 
     * ProblemScore which contains the problem with its assigned score for the exam.
     */
    public List<ProblemScore> getProblems() {
        return this.problems;
    }

    /**
     * Get the time limit for the exam.
     * @return the time limit for the exam.
     */
    public Duration getTimeLimit() {
        return timeLimit;
    }

    /**
     * Set the time limit for the exam.
     * @param timeLimit The time limit for the exam.
     */
    public void setTimeLimit(Duration timeLimit) {
        this.timeLimit = timeLimit;
    }

    /**
     * The static method to find the exam by its ID.
     * @param examID The exam ID
     * @return The exam object. null if the examid does not exist.
     */
    static Exam getExamByID(int examID) {
        Exam exam = new Exam();
        if (exam.get(examID)) {
            return exam;
        } else {
            return null;
        }

    }

    /**
     * The static method to remove an exam by its ID.
     * @param id The exam ID.
     * @return true if successful. false if unsuccessfull.
     */
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

    /**
     * Create this exam in database.
     * @return true if succeeds. false if fails.
     */
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

    /**
     * Update this exam in database.
     * @return true if succeeds. false if fails.
     */
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

    /**
     * Remove this exam form the database.
     * @return true if succeeds. false if fails.
     */
    @Override
    public boolean remove() {

        return removeByID(this.ID);

    }

    /**
     * Fill this exam object by retrieving from database with the exam ID.
     * @param id the examID
     * @return true if succeeds. false if fails.
     */
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

    /**
     * Get all problems within the exam.
     * @param id the exam id.
     * @param conn a database connection to be used.
     * @return a collection of ProblemScore which contains the problem and assigned score for the exam.
     * @throws SQLException 
     */
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
