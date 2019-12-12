/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ExamSys.backend;

import java.awt.image.BufferedImage;

/**
 *一道题内可能有多组problemsection
 * @author Andy
 */
public class ProblemSection {
    String text;
    BufferedImage pic;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public BufferedImage getPic() {
        return pic;
    }

    public void setPic(BufferedImage pic) {
        this.pic = pic;
    }


}
