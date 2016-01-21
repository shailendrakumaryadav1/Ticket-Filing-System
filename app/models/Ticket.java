package models;

import play.db.ebean.Model;


import javax.persistence.Column;
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
    @Column(length=5000)
    public String comment;
    public void saveTicket()
    {
        save();
    }
    public String validateTicketEntries()
    {
        ticketID=ticketID.trim();
        name=name.trim();
        nameID=nameID.trim();
        createdBy=createdBy.trim();
        assignedTo=assignedTo.trim();
        comment=comment.trim();
        String msg;
        if(ticketID.equals(""))
        {
            msg="Ticket ID can't be empty";
        }
        else if(name.equals(""))
        {
            msg="Customer Name can't be empty";
        }
        else if(nameID.equals(""))
        {
            msg="Customer ID can't be empty";
        }
        else if(createdBy.equals(""))
        {
            msg="\"Created by\" field is necessary";
        }
        else if(!name.matches("^[a-zA-Z0-9. ]+$"))
        {
            msg="Invalid Customer Name";
        }
        else if(!nameID.matches("^[a-zA-Z0-9 -/]+$"))
        {
            msg="Invalid Customer ID";
        }
        else if(!createdBy.matches("^[a-zA-Z0-9. ]+$"))
        {
            msg="Invalid \"Created By\" field";
        }
        else if(!assignedTo.matches("^[a-zA-Z0-9. ]*$"))
        {
            msg="Invalid \"Assigned To\" field";
        }
        else if(status.equals("OPEN") && assignedTo.equals(""))
        {
            msg="Ticket with OPEN Status must be \"Assigned To\" someone";
        }
        else if(status.equals("NEW") && !assignedTo.equals(""))
        {
            msg="Ticket can't be in NEW State once it is assigned to someone";
        }
        else
        {
            msg="OK";
        }
        return msg;
    }
    public void setStatus()
    {
        assignedTo=assignedTo.trim();
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

        UIManager.put("swing.boldMetal", Boolean.FALSE);
        JFrame frame = new JFrame("Ticket ID: "+ticketID);
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setMinimumSize(new Dimension(200,200));
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
        JPanel detailsPane = new JPanel();
        GridBagLayout gridbag = new GridBagLayout();
        detailsPane.setLayout(gridbag);
        detailsPane.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createTitledBorder("Details"),
                        BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        addLabelTextRowsInViewDialogBox(labels, textFields, gridbag, detailsPane);
        JTextArea textCommentArea = new JTextArea(comment);
        textCommentArea.setEditable(false);
        textCommentArea.setForeground(Color.BLUE);
        textCommentArea.setFont(new Font("Serif", Font.ITALIC, 16));
        textCommentArea.setLineWrap(true);
        textCommentArea.setWrapStyleWord(true);

        JScrollPane commentsPane = new JScrollPane(textCommentArea);
        commentsPane.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
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
                frame.dispose();
            }
        });
        actionsPane.add(closeButton );
        frame.add(detailsPane, BorderLayout.PAGE_START);
        frame.add(commentsPane,BorderLayout.CENTER);
        frame.add(actionsPane, BorderLayout.PAGE_END);
        frame.setSize(500, 500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void addLabelTextRowsInViewDialogBox(JLabel[] labels,JTextField[] textFields,
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

    public void showTicketEditDialogBox()
    {
        UIManager.put("swing.boldMetal", Boolean.FALSE);
        JFrame frame = new JFrame("Ticket ID: "+ticketID);
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setMinimumSize(new Dimension(200,200));

        String left[]={"Ticket ID","Customer Name","Customer ID","Created By","Assigned To","Issues related to","Status"};
        String right[]={ticketID,name,nameID,createdBy,assignedTo,issues,status};
        JLabel labels[]=new JLabel[7];
        JTextField textFields[]=new JTextField[7];
        int i;
        for(i=0;i<7;i++) {
            labels[i] = new JLabel(left[i] + ": ");

            textFields[i] = new JTextField(right[i]);
            textFields[i].setForeground(Color.BLUE);
            textFields[i].setBackground(Color.WHITE);
            if(left[i].equals("Ticket ID"))
                textFields[i].setEditable(false);
        }
        String issuesNames[]={"Account","Customer Service","Delivery",
                "Exchange","General","Legal","Logistics","Payment","Personalization",
                "Refund","Returns","Services","Shipping","Site Navigation","Supply",};
        DefaultComboBoxModel issueModel = new DefaultComboBoxModel(issuesNames);
        JComboBox issueComboBox = new JComboBox(issueModel);

        for(i=0;i<issuesNames.length;i++)
        if(issuesNames[i].equals(issues))
            break;
        issueComboBox.setSelectedIndex(i);


        String statusNames[]={"NEW","OPEN","CLOSED"};
        DefaultComboBoxModel statusModel = new DefaultComboBoxModel(statusNames);
        JComboBox statusComboBox = new JComboBox(statusModel);

        for(i=0;i<statusNames.length;i++)
            if(statusNames[i].equals(status))
                break;
        statusComboBox.setSelectedIndex(i);

        JPanel detailsPane = new JPanel();
        GridBagLayout gridbag = new GridBagLayout();

        detailsPane.setLayout(gridbag);
        detailsPane.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createTitledBorder("Details"),
                        BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        addLabelTextRowsInEditDialogBox(labels, textFields, left, right, issueComboBox, statusComboBox, gridbag, detailsPane);

        JTextArea textCommentArea = new JTextArea(comment);
        //textCommentArea.setEditable(false);
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

        JButton saveButton=new JButton("SAVE");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                name=textFields[1].getText();
                nameID=textFields[2].getText();
                createdBy=textFields[3].getText();
                assignedTo=textFields[4].getText();
                issues=issueComboBox.getSelectedItem().toString();
                status=statusComboBox.getSelectedItem().toString();
                comment=textCommentArea.getText();

                String validateMsg=validateTicketEntries();
                    if (validateMsg.equals("OK")) {
                        save();
                        JOptionPane.showMessageDialog(null, "Ticket ID: "+ticketID+" updated Successfully");
                        frame.dispose();
                    } else {
                        JOptionPane.showMessageDialog(null, validateMsg);
                    }
            }
        });
        JButton closeButton=new JButton("CANCEL");
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //redirect(routes.Application.index());
                frame.dispose();
            }
        });
        actionsPane.add(saveButton );
        actionsPane.add(closeButton );
        frame.add(detailsPane, BorderLayout.PAGE_START);
        frame.add(commentsPane,BorderLayout.CENTER);
        frame.add(actionsPane, BorderLayout.PAGE_END);
        frame.setSize(500, 500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    private void addLabelTextRowsInEditDialogBox(JLabel[] labels,JTextField[] textFields,String left[],String right[],JComboBox issueComboBox,JComboBox statusComboBox,
                                                 GridBagLayout gridbag, Container container) {
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
            if(left[i].equals("Issues related to"))
            {
                container.add(issueComboBox, c);
            }
            else if(left[i].equals("Status"))
            {
                container.add(statusComboBox,c);
            }
            else
            {
                container.add(textFields[i], c);
            }
        }
    }
}
