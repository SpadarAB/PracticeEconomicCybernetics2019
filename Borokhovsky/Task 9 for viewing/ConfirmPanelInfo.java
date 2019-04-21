/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bsu.fpmi.educational_practice2019;
import java.beans.*;
import java.awt.*;
/**
 *
 * @author me
 */
public class ConfirmPanelInfo extends SimpleBeanInfo {



    static PropertyDescriptor prop(String name, String description) {
        try {
            PropertyDescriptor p = new PropertyDescriptor(name, ConfirmPanel.class);
            p.setShortDescription(description);
            return p;
        } catch (IntrospectionException e) {
            return null;
        }
    }

    static PropertyDescriptor[] props = { prop("messageText", "The message text that appears in the bean body"),
                                          prop("okLabel", "The label for the Yes button"), };


    public PropertyDescriptor[] getPropertyDescriptors() { return props; }

    public int getDefaultPropertyIndex() { return 0; }
}