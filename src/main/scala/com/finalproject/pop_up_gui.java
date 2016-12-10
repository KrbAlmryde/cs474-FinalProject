package com.finalproject;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import akka.util.Timeout;
import akka.pattern.Patterns;
import com.finalproject.actors.*;
//import com.finalproject.Utils.instructions;
import akka.actor.*;
import com.finalproject.patterns.Messages.*;
import scala.concurrent.Await;
import scala.concurrent.Future;


public class pop_up_gui
{
    final ActorSystem system = ActorSystem.create("FinalProject");
    final ActorRef masterActor = system.actorOf(Props.create(MasterActor.class));
    static pop_up_gui app;
    static JFrame frame = new JFrame("Keyword Search");
    private JLabel keywordLabel = new JLabel();
    private JPanel searchResultsPanel = new JPanel(new BorderLayout());

    private DefaultListModel<String> search_results_list = new DefaultListModel<>(); // list of tweets
    private JList<String> search = new JList<String>(search_results_list);
    JScrollPane resultsPane = new JScrollPane();

    public pop_up_gui(JFrame frame, String keywords)
    {
        keywordLabel.setText("Keywords Searched: " + keywords);
        frame.getContentPane().setLayout(new BorderLayout());
        resultsPane.setViewportView(search);
        search_results_list.addElement("The Chicago Cubs Win The Fucking World Series!!!! Go Wild Chicago!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        searchResultsPanel.setPreferredSize(new Dimension(400,250));
        searchResultsPanel.add(resultsPane,BorderLayout.CENTER);
        search.setFont(new Font("Arial",Font.BOLD,16));
        keywordLabel.setFont(new Font("Arial", Font.BOLD, 20));

        frame.getContentPane().add(keywordLabel,BorderLayout.NORTH);
        keywordLabel.setHorizontalAlignment(JLabel.CENTER);
        frame.getContentPane().add(searchResultsPanel,BorderLayout.WEST);
        frame.setSize(1350,600);
        frame.setVisible(true);
    }


}
