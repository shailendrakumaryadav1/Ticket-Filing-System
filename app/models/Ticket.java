package models;

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

}
