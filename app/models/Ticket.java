package models;

import play.db.ebean.Model;
import play.db.ebean.Model;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Created by SKY on 1/15/2016.
 */
@Entity
public class Ticket extends Model{
    @Id
    public String ticketID;
    public String name;
    public String nameID;
    public String createdBy;
    public String assignedTo;
    public String issues;
    public String status;
    public String comment;
    public void saveTicket()
    {
        JOptionPane.showMessageDialog(null, "Work?");

        System.out.println("hii");

    }
    public boolean validateTicketEntries()
    {
        return true;
    }

    public void setStatus()
    {
        if(assignedTo.equals(""))
            status="NEW";
        else
            status="OPEN";
    }
    public void setTicketID()
    {
        List<String> list=new Model.Finder(String.class,Ticket.class).findIds();

        int idMax=0;
        for(int i=0;i<list.size();i++)
        {
            int t=Integer.parseInt(list.get(i));
            if(t>idMax)
                idMax=t;
        }
        ticketID= Integer.toString(1+idMax);
    }
    public void showTicketViewDialogBox(){

        String textFieldString = "JTextField";
        String passwordFieldString = "JPasswordField";
        String ftfString = "JFormattedTextField";


        UIManager.put("swing.boldMetal", Boolean.FALSE);
        JFrame frame = new JFrame("Ticket ID: "+ticketID);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setMinimumSize(new Dimension(200,200));
        //

        String left[]={"Ticket ID","Customer Name","Customer ID","Created By","Assigned To","Issues related to","Status"};
        String right[]={ticketID,name,nameID,createdBy,assignedTo,issues,status};
        JLabel labels[]=new JLabel[7];
        JTextField textFields[]=new JTextField[7];
        for(int i=0;i<7;i++) {

            labels[i] = new JLabel(left[i] + ": ");

            textFields[i] = new JTextField(right[i]);
            textFields[i].setForeground(Color.BLUE);
            textFields[i].setBackground(Color.WHITE);
            textFields[i].setEditable(false);

            if (left[i].equals("Status")) {

                switch (status) {
                    case "NEW":
                        textFields[i].setForeground(Color.RED);
                        break;
                    case "OPEN":
                        textFields[i].setForeground(Color.GREEN);
                        break;
                    case "CLOSED":
                        textFields[i].setForeground(Color.BLUE);
                        break;
                    default:
                        textFields[i].setForeground(Color.BLACK);
                }
            }
        }





        for(int i=0;i<7;i++) {

        }


        JPanel detailsPane = new JPanel();
        GridBagLayout gridbag = new GridBagLayout();

        detailsPane.setLayout(gridbag);
        detailsPane.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createTitledBorder("Details"),
                        BorderFactory.createEmptyBorder(5, 5, 5, 5)));


        addLabelTextRows(labels, textFields, gridbag, detailsPane);



        JTextArea textCommentArea = new JTextArea(comment);
        textCommentArea.setEditable(false);
        textCommentArea.setForeground(Color.BLUE);
        textCommentArea.setFont(new Font("Serif", Font.ITALIC, 16));
        textCommentArea.setLineWrap(true);
        textCommentArea.setWrapStyleWord(true);

        JScrollPane commentsPane = new JScrollPane(textCommentArea);
        commentsPane.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        //areaScrollPane.setPreferredSize(new Dimension(250, 250));
        commentsPane.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createCompoundBorder(
                                BorderFactory.createTitledBorder("Comments"),
                                BorderFactory.createEmptyBorder(5, 5, 5, 5)),
                        commentsPane.getBorder()));


        JPanel actionsPane=new JPanel();
        actionsPane.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createTitledBorder("Action"),
                        BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        JButton closeButton=new JButton("CLOSE");
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //JOptionPane.showMessageDialog(frame, "Roseindia.net");
                frame.dispose();
            }
        });
        actionsPane.add(closeButton );


        //Put everything together.

        frame.add(detailsPane, BorderLayout.PAGE_START);
        frame.add(commentsPane,BorderLayout.CENTER);
        frame.add(actionsPane, BorderLayout.PAGE_END);
        //

        frame.setSize(500, 500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

    }

    private void addLabelTextRows(JLabel[] labels,JTextField[] textFields,
                                  GridBagLayout gridbag,
                                  Container container) {
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.EAST;
        int numLabels = labels.length;

        for (int i = 0; i < numLabels; i++) {
            c.gridwidth = GridBagConstraints.RELATIVE; //next-to-last
            c.fill = GridBagConstraints.NONE;      //reset to default
            c.weightx = 0.0;                       //reset to default
            container.add(labels[i], c);

            c.gridwidth = GridBagConstraints.REMAINDER;     //end row
            c.fill = GridBagConstraints.HORIZONTAL;
            c.weightx = 1.0;
            container.add(textFields[i], c);

        }
    }
}
