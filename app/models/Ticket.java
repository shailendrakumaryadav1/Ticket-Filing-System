package models;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import controllers.MongoDBConnection;

public class Ticket
{
    public String ticketID;
    public String name;
    public String nameID;
    public String createdBy;
    public String assignedTo;
    public String issues;
    public String status;
    public String comment;
    /*
    Only required get and set methods are defined.
    */

    public void setInitialStatus()
    {
        assignedTo = assignedTo.trim();
        if (assignedTo.equals(""))
            status = "NEW";
        else
            status = "OPEN";
    }

    public void setStatus(String s)
    {
        status = s;
    }

    public void setTicketID()
    {
        try
        {
            DB db = MongoDBConnection.connectToMongo();
            DBCollection coll = db.getCollection("TicketCollection");
            ticketID= (coll.getCount() +1) +"";
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public String getTicketID()
    {
        return ticketID;
    }

    public String getStatus()
    {
        return status;
    }

    public void saveTicket()
    {
        try
        {
            DB db = MongoDBConnection.connectToMongo();
            DBCollection coll = db.getCollection("TicketCollection");

            BasicDBObject ob;
            ob = new BasicDBObject("ticketID",ticketID).append("name",name).append("nameID",nameID).append("createdBy",createdBy)
            .append("assignedTo",assignedTo).append("issues",issues).append("status",status).append("comment",comment);
            coll.insert(ob);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void copyTicket(Ticket ticket)
    {
        ticketID=ticket.ticketID;
        name=ticket.name;
        nameID=ticket.nameID;
        createdBy=ticket.createdBy;
        assignedTo=ticket.assignedTo;
        issues=ticket.issues;
        status=ticket.status;
        comment=ticket.comment;
    }

    public void makeTicketFromMap(Map map)
    {
        ticketID = map.get("ticketID").toString();
        name = map.get("name").toString();
        nameID = map.get("nameID").toString();
        createdBy = map.get("createdBy").toString();
        assignedTo = map.get("assignedTo").toString();
        issues = map.get("issues").toString();
        status = map.get("status").toString();
        comment = map.get("comment").toString();
    }

    public String validateTicketEntries()
    {
        ticketID = ticketID.trim();
        name = name.trim();
        nameID = nameID.trim();
        createdBy = createdBy.trim();
        assignedTo = assignedTo.trim();
        comment = comment.trim();
        String msg;
        if (ticketID.equals(""))
        {
            msg = "Ticket ID can't be empty";
        }
        else if (name.equals(""))
        {
            msg = "Customer Name can't be empty";
        }
        else if (nameID.equals(""))
        {
            msg = "Customer ID can't be empty";
        }
        else if (createdBy.equals(""))
        {
            msg = "\"Created by\" field is necessary";
        }
        else if (!name.matches("^[a-zA-Z0-9. ]+$"))
        {
            msg = "Invalid Customer Name";
        }
        else if (!nameID.matches("^[a-zA-Z0-9 -/]+$"))
        {
            msg = "Invalid Customer ID";
        }
        else if (!createdBy.matches("^[a-zA-Z0-9. ]+$"))
        {
            msg = "Invalid \"Created By\" field";
        }
        else if (!assignedTo.matches("^[a-zA-Z0-9. ]*$"))
        {
            msg = "Invalid \"Assigned To\" field";
        }
        else if (status.equals("OPEN") && assignedTo.equals(""))
        {
            msg = "Ticket with OPEN Status must be \"Assigned To\" someone";
        }
        else if (status.equals("NEW") && !assignedTo.equals(""))
        {
            msg = "Ticket can't be in NEW State once it is assigned to someone";
        }
        else
        {
            msg = "OK";
        }
        return msg;
    }

    public void showTicketViewDialogBox()
    {
        JFrame frame = new JFrame("Ticket ID: " + ticketID);
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setMinimumSize(new Dimension(200, 200));
        String left[] = {"Ticket ID", "Customer Name", "Customer ID", "Created By", "Assigned To", "Issues related to", "Status"};
        String right[] = {ticketID, name, nameID, createdBy, assignedTo, issues, status};
        JLabel labels[] = new JLabel[7];
        JTextField textFields[] = new JTextField[7];
        for (int i = 0; i < 7; i++)
        {
            labels[i] = new JLabel(left[i] + ": ");
            textFields[i] = new JTextField(right[i]);
            textFields[i].setForeground(Color.BLUE);
            textFields[i].setBackground(Color.WHITE);
            textFields[i].setEditable(false);
            if (left[i].equals("Status"))
            {
                switch (status)
                {
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
        JPanel actionsPane = new JPanel();
        actionsPane.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createTitledBorder("Action"),
                        BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        JButton closeButton = new JButton("CLOSE");
        closeButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                frame.dispose();
            }
        });
        actionsPane.add(closeButton);
        frame.add(detailsPane, BorderLayout.PAGE_START);
        frame.add(commentsPane, BorderLayout.CENTER);
        frame.add(actionsPane, BorderLayout.PAGE_END);
        frame.setSize(500, 500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void addLabelTextRowsInViewDialogBox(JLabel[] labels, JTextField[] textFields,
                                                 GridBagLayout gridbag,
                                                 Container container)
    {
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.EAST;
        int numLabels = labels.length;

        for (int i = 0; i < numLabels; i++)
        {
            c.gridwidth = GridBagConstraints.RELATIVE;
            c.fill = GridBagConstraints.NONE;
            c.weightx = 0.0;
            container.add(labels[i], c);

            c.gridwidth = GridBagConstraints.REMAINDER;     //end row
            c.fill = GridBagConstraints.HORIZONTAL;
            c.weightx = 1.0;
            container.add(textFields[i], c);
        }
    }

    public void updateTicketWith(Ticket ticket_new)
    {
        Ticket ticket_old=this;
        try
        {
            DB db = MongoDBConnection.connectToMongo();
            DBCollection coll = db.getCollection("TicketCollection");

            // search document where ticketID="ticket_old.ticketID" and update it with new values
            BasicDBObject query = new BasicDBObject();
            query.put("ticketID", ticket_old.ticketID);

            BasicDBObject newDocument = new BasicDBObject();
            newDocument.put("ticketID", ticket_new.ticketID); //redundant
            newDocument.put("name", ticket_new.name);
            newDocument.put("nameID", ticket_new.nameID);
            newDocument.put("createdBy", ticket_new.createdBy);
            newDocument.put("assignedTo", ticket_new.assignedTo);
            newDocument.put("issues", ticket_new.issues);
            newDocument.put("status", ticket_new.status);
            newDocument.put("comment", ticket_new.comment);

            BasicDBObject updateObj = new BasicDBObject();
            updateObj.put("$set", newDocument);
            coll.update(query, updateObj);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }
}
