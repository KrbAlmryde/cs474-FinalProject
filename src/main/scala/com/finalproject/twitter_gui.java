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


public class twitter_gui extends JFrame implements ActionListener
{
    final ActorSystem system = ActorSystem.create("FinalProject");
    final ActorRef masterActor = system.actorOf(Props.create(MasterActor.class));
    //final ActorMaterializer materializer = ActorMaterializer.create();
    static twitter_gui app;
    static JFrame frame = new JFrame("CS474 Project");
    private JLabel projectLabel = new JLabel("Twitter Analyzer");
    private JLabel trendsLabel = new JLabel("Today's Twitter Trends:");
    private JLabel resultsLabel = new JLabel("Your Search Results:");
    JMenuBar menuBar = new JMenuBar();
    JMenu menu = new JMenu("Menu");
    JMenu menuHelp = new JMenu("Help");
    JMenuItem exit = new JMenuItem("Exit", KeyEvent.VK_X);
    JMenuItem help = new JMenuItem("Help",KeyEvent.VK_L);
    JMenuItem about = new JMenuItem("About",KeyEvent.VK_A);

    //panels and what they'll contain for the EAST and WEST portions of the main window(frame)
    private JPanel topicsPanel = new JPanel(new BorderLayout());
    private JPanel resultsPanel = new JPanel(new BorderLayout());
    private DefaultListModel<String> topics_list = new DefaultListModel<>(); // list of tweets
    private JList<String> list = new JList<String>(topics_list);
    private DefaultListModel<String> results_list = new DefaultListModel<>(); // list of tweets
    private JList<String> list2 = new JList<String>(results_list);
    JScrollPane topicsPane = new JScrollPane();
    JScrollPane resultsPane = new JScrollPane();

    //items for South portion of main window(frame)
    JPanel searchPanel = new JPanel();
    private JButton analyze = new JButton("Analyze");
    private JTextField searchTerms = new JTextField("Enter Keywords to search Twitter here.",50);

    //items for Center portion of main window(frame)
    JPanel trendPanel = new JPanel();
    private JButton trends = new JButton();

    // class constructor
    public twitter_gui(JFrame frame)
    {
        super("Twitter Analyzer");
        menuBar.add(menu);
        menuBar.add(menuHelp);
        exit.addActionListener(this);
        help.addActionListener(this);
        about.addActionListener(this);
        menu.add(exit);
        menuHelp.add(about);
        menuHelp.add(help);
        frame.setJMenuBar(menuBar);

        //main label on gui
        projectLabel.setFont(new Font("Arial",Font.BOLD , 30));
        projectLabel.setHorizontalAlignment(JLabel.CENTER);

        //Labels for other panels
        trendsLabel.setFont(new Font("Arial",Font.BOLD , 20));
        resultsLabel.setFont(new Font("Arial",Font.BOLD , 20));

        //South panel on gui
        searchPanel.setLayout(new BoxLayout(searchPanel,BoxLayout.LINE_AXIS));
        searchPanel.add(searchTerms);
        searchPanel.add(analyze);
        frame.add(searchPanel,BorderLayout.SOUTH);

        //Center panel on gui
        trendPanel.setLayout(new BoxLayout(trendPanel, BoxLayout.PAGE_AXIS));
        trendPanel.add(Box.createRigidArea(new Dimension(30,50)));
        trendPanel.add(trends);
        trends.setHorizontalAlignment(JButton.CENTER);
        trends.setText("<html>Today's<br/>Trends</html>");
        trends.addMouseListener( new MouseHandler() );
        trends.setFont(new Font("Arial",Font.BOLD ,20));
        trends.setMaximumSize(new Dimension(230,200));
        frame.add(trendPanel,BorderLayout.CENTER);

        //more operations on elements in South panel
        analyze.setFont(new Font("Arial",Font.BOLD , 30));
        analyze.addMouseListener( new MouseHandler() );
        analyze.setPreferredSize(new Dimension(200,55));
        searchTerms.setPreferredSize(new Dimension(50,30));
        searchTerms.setFont(new Font("Arial",Font.BOLD,16));

        //setup the Panels for the display lists
        topicsPane.setViewportView(list);
        resultsPane.setViewportView(list2);

        topicsPanel.add(trendsLabel,BorderLayout.NORTH);
        trendsLabel.setHorizontalAlignment(JLabel.CENTER);
        resultsPanel.add(resultsPane, BorderLayout.CENTER);
        resultsPanel.add(resultsLabel,BorderLayout.NORTH);
        resultsLabel.setHorizontalAlignment(JLabel.CENTER);
        topicsPanel.setPreferredSize(new Dimension(400,250));
        topicsPanel.add(topicsPane,BorderLayout.CENTER);
        list.setFont(new Font("Arial",Font.BOLD,14));
        resultsPanel.setPreferredSize(new Dimension(400,250));
        list2.setFont(new Font("Arial",Font.BOLD,14));

        //set how many rows will be shown before scrolling
        list.setVisibleRowCount(10);
        list2.setVisibleRowCount(10);

        //add panels to main window(frame)
        frame.add(projectLabel,BorderLayout.NORTH);
        frame.add(topicsPanel,BorderLayout.WEST);
        frame.add(resultsPanel,BorderLayout.EAST);


    } // end class constructor

    //
    // handles JMenuItem actions from the "Menu" and "Help" menus
    //
    public void actionPerformed (ActionEvent e)
    {
        Object item;
        item = e.getSource(); // get menu item that triggered the event

        // match the menu item to the its resulting action

        if(item == exit)
        {
            System.exit(0);
        }
        else if(item == about)
        {
            displayAbout();
        }
        else if(item == help)
        {
            displayHelp();
        }

    }// end actionPerformed


    class MyAdjustmentListener implements AdjustmentListener
    {
        public void adjustmentValueChanged(AdjustmentEvent e) {
            //label.setText("    New Value is " + e.getValue() + "      ");
            repaint();
        }
    }


    // the main function of the program
    public static void main(String args[])
    {
        frame.setSize(1125,600);
        frame.setLayout(new BorderLayout());

        //create and start the gui
        app = new twitter_gui(frame);
        app.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frame.setVisible(true);

    }// end of main



    //
    // display the about page
    //
    public void displayAbout()
    {
        JOptionPane.showMessageDialog(null,"                                                       ABOUT Twitter Analyzer \n\n"
                + "Created by:  Kyle Almryde, Abhishek Tripathi and Mike McClory.\n\n"
                + "Created for CS474: Object Oriented Languages and Environments with Mark Grechanik   \n\n");



    } // end of displayAbout



    //
    // display the help page
    //
    public void displayHelp()
    {
        JOptionPane.showMessageDialog (null, "               HELP CENTER\n\n ");



    } // end of displayHelp


    //
    // handles mouse click events
    //
    class MouseHandler extends MouseAdapter
    {

        public void mouseClicked (MouseEvent e)
        {
            String s = "";
            JButton temp = (JButton)e.getSource();


            if(temp == analyze)
                topics_list.addElement(searchTerms.getText());
            else if(temp.getText().equals("<html>Today's<br/>Trends</html>"))
            {
                Timeout timeout = new Timeout(
                        scala.concurrent.duration.Duration.create(1,"seconds"));

                Future<Object> f1 = Patterns.ask(masterActor,
                                            new Locations(topics_list),timeout);

                try {
                    String result = (String) Await.result(f1,timeout.duration());
                    //System.out.println("RESULT = " + result);
                }
                catch (Exception e1)
                {
                    System.out.println("Error Caught. Moving On.......");
                }

            }

        }

    }



}
