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


public class twitter_gui extends JFrame //implements ActionListener,FocusListener
{
    // akka Actor stuff
    final ActorSystem system = ActorSystem.create("FinalProject");
    final ActorRef masterActor = system.actorOf(Props.create(MasterActor.class));

    //Gui variables
    static twitter_gui app;
    static pop_up_gui app2;

    //listener variables
    private Listeners listener;
    private Listeners.buttonListener buttonListener;
    private Listeners.focusListener focusListener;
    private Listeners.selectionListener selectionListener;

    // Gui Items
    static JFrame frame = new JFrame("CS474 Project");
    private JLabel projectLabel = new JLabel("Twitter Analyzer");
    private JLabel trendsLabel = new JLabel("Trend Locations:");
    private JLabel resultsLabel = new JLabel("What's trending in ");
    JMenuBar menuBar = new JMenuBar();
    JMenu menu = new JMenu("Menu");
    JMenu menuHelp = new JMenu("Help");
    JMenuItem exit = new JMenuItem("Exit", KeyEvent.VK_X);
    JMenuItem help = new JMenuItem("Help",KeyEvent.VK_L);
    JMenuItem about = new JMenuItem("About",KeyEvent.VK_A);

    //panels and what they'll contain for the EAST and WEST portions of the main window(frame)
    private JPanel topicsPanel = new JPanel(new BorderLayout());
    private JPanel resultsPanel = new JPanel(new BorderLayout());
    private DefaultListModel<String> locations_list = new DefaultListModel<>(); // list of tweets
    private JList<String> list = new JList<String>(locations_list);
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
        listener = new Listeners(exit,help,about,trends,analyze,searchTerms,list,resultsLabel);
        buttonListener = new Listeners.buttonListener();
        focusListener = new Listeners.focusListener();
        selectionListener = new Listeners.selectionListener();
        menuBar.add(menu);
        menuBar.add(menuHelp);
        exit.addActionListener(buttonListener);
        help.addActionListener(buttonListener);
        about.addActionListener(buttonListener);
        menu.add(exit);
        menuHelp.add(about);
        menuHelp.add(help);
        frame.setJMenuBar(menuBar);

        //main label on gui
        projectLabel.setFont(new Font("Arial",Font.BOLD , 36));
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
        trends.setText("<html>Get Trend<br/>Locations</html>");
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
        searchTerms.addFocusListener(focusListener);

        //setup the Panels for the display lists
        topicsPane.setViewportView(list);
        resultsPane.setViewportView(list2);

        //selection listener for locations list
        list.addListSelectionListener(selectionListener);

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
    // handles mouse click events
    //
    class MouseHandler extends MouseAdapter
    {

        public void mouseClicked (MouseEvent e)
        {
            String s = "";
            JButton temp = (JButton)e.getSource();

            if(temp == analyze)
            {
                app2 = new pop_up_gui(new JFrame("Keyword Search"), searchTerms.getText());
            }
            else if(temp == trends)
            {
                Timeout timeout = new Timeout(
                        scala.concurrent.duration.Duration.create(1,"seconds"));

                Future<Object> f1 = Patterns.ask(masterActor,
                                            new Locations(locations_list),timeout);

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
