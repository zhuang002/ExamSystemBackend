/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ExamSys.backend;

/**
 *
 * @author zhuan
 */
public class ProblemScore {
    public ProblemScore(Problem p,int s) {
        this.problem=p;
        this.score=s;
    }

    @Override
    public String toString() {
        return this.problem.toString()+" - score:"+this.score; //To change body of generated methods, choose Tools | Templates.
    }

    public Problem getProblem() {
        return problem;
    }

    public int getScore() {
        return score;
    }
    Problem problem;
    int score;
}
