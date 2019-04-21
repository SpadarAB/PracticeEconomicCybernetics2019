/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bsu.fpmi.educational_practice2019;

/**
 *
 * @author me
 */
public class AnswerEvent extends java.util.EventObject {
    public static final int OK = 0;
    protected int id;

    public AnswerEvent(Object source, int id) {
        super(source);
        this.id = id;
    }

    public int getID() { return id; }
}