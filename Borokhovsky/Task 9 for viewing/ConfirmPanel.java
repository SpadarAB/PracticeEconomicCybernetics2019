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
import java.beans.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;
public class ConfirmPanel extends Panel {
    protected String messageText;
    protected String okLabel;


    protected Label label;
    protected Button ok;


    public ConfirmPanel() { this("Message"); }

    public ConfirmPanel(String messageText) {
        this(messageText, "Ok");
    }


    public ConfirmPanel(String messageText, String okLabel) {
        setLayout(new BorderLayout(15, 15));
        label = new Label(messageText);
        add(label, BorderLayout.CENTER);
        Panel buttonbox = new Panel();
        buttonbox.setLayout(new FlowLayout(FlowLayout.CENTER, 25, 15));
        add(buttonbox, BorderLayout.SOUTH);
        ok = new Button();
        buttonbox.add(ok);

        // Register listeners for each button
        ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fireEvent(new AnswerEvent(ConfirmPanel.this, AnswerEvent.OK));
            }
        });
        setMessageText(messageText);
        setOkLabel(okLabel);
    }


    public String getMessageText() { return messageText; }

    public String getOkLabel() { return okLabel; }


    public void setMessageText(String messageText) {
        this.messageText = messageText;
        label.setText(messageText);
        validate();
    }

    public void setOkLabel(String l) {
        okLabel = l;
        ok.setLabel(l);
        ok.setVisible((l != null) && (l.length() > 0));
        validate();
    }


    protected Vector<AnswerListener> listeners = new Vector<AnswerListener>();

    public void addAnswerListener(AnswerListener l) {
        listeners.addElement(l);
    }

    public void removeAnswerListener(AnswerListener l) {
        listeners.removeElement(l);
    }


    public void fireEvent(AnswerEvent e) {
        Vector list = (Vector) listeners.clone();
        for (int i = 0; i < list.size(); i++) {
            AnswerListener listener = (AnswerListener) list.elementAt(i);
            listener.ok(e);
        }
    }
}