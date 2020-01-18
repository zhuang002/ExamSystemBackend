/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ExamSys.backend;

/**
 *
 * @author Andy
 */
public enum Role {
    Admin(0),
    Teacher(1),
    Student(2);
   
    private final int value;
    
    private Role(int num){
        this.value=num;
    }
    
    public int getValue(){
        return this.value;
    }
    
    static public Role getRole(int rv) {
        switch (rv) {
            case 0: return Role.Admin;
            case 1: return Role.Teacher;
            case 2: return Role.Student;
            default: return Role.Student;
        }
    }

}
