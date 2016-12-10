package com.finalproject;

import akka.pattern.Patterns;
import akka.util.Timeout;
import com.finalproject.patterns.Messages;
import scala.concurrent.Await;
import scala.concurrent.Future;
import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import java.awt.event.*;


public class Listeners
{
    static JMenuItem exit;
    static JMenuItem help;
    static JMenuItem about;
    static JButton trends;
    static JButton analyze;
    static JTextField searchTerms;
    static JList location_list;
    static JLabel location;


    public Listeners(JMenuItem m,JMenuItem m2,JMenuItem m3,JButton b1,JButton b2, JTextField s,
                                                                                JList list, JLabel loc)
    {
        exit = m;
        help = m2;
        about = m3;
        trends = b1;
        analyze = b2;
        searchTerms = s;
        location_list = list;
        location = loc;
    }


    //
    // handles JMenuItem actions from the "Menu" and "Help" menus
    //
    public static class buttonListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            Object item;
            item = e.getSource(); // get menu item that triggered the event

            // match the menu item to the its resulting action

            if (item == exit) {
                System.exit(0);
            } else if (item == about) {
                displayAbout();
            } else if (item == help) {
                displayHelp();
            }

        }// end actionPerformed

    }


    //
    // for FocusListener
    //
    public static class focusListener implements FocusListener
    {
        public void focusGained (FocusEvent e)
        {
            Object t = e.getSource();
            if (t == searchTerms)
                searchTerms.setText("");
        }

        public void focusLost(FocusEvent e)
        {

        }
    }


    public static class selectionListener implements ListSelectionListener
    {
        public void valueChanged(ListSelectionEvent evt) {


            if (evt.getValueIsAdjusting())
                return;

            location.setText("What's trending in " + location_list.getSelectedValue() + ":");
        }

    }


    //
    // display the about page
    //
    public static void displayAbout()
    {
        JOptionPane.showMessageDialog(null,"                                                       ABOUT Twitter Analyzer \n\n"
                + "Created by:  Kyle Almryde, Abhishek Tripathi and Mike McClory.\n\n"
                + "Created for CS474: Object Oriented Languages and Environments with Mark Grechanik   \n\n");



    } // end of displayAbout


    //
    // display the help page
    //
    public static void displayHelp()
    {
        JOptionPane.showMessageDialog (null, "                                                HELP CENTER\n\n      " +
                                             "(1)Press the 'Get Trend Location' button to get locations of trending things.\n\n" +
                                             " (2)Select a location from list to see trending topics at that location\n\n" +
                                             " (3)Type keywords in text field and press 'analyze' to open new window with\n" +
                                             "        a list of tweets containing those keywords.\n\n");



    } // end of displayHelp

}
