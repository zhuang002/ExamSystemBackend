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
public interface IDBRecord {
    boolean create();
    boolean update();
    boolean remove();
    boolean get(int id);
    
    
    
    
}
