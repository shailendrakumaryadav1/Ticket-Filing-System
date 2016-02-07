package controllers;

import com.mongodb.*;
import models.Ticket;
import play.data.Form;
import play.mvc.*;
import views.html.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import static play.libs.Json.toJson;

public class Application extends Controller {

    public Result index()
    {
        return ok(index.render("Ticket Filing System"));
    }

    public Result addTicket()
    {
        Ticket ticket = Form.form(Ticket.class).bindFromRequest().get();
        ticket.setTicketID();
        ticket.setInitialStatus();
        String msg=ticket.validateTicketEntries();
        if (msg.equals("OK"))
        {
            saveTicket(ticket);
            msg="Ticket Added Successfully\nTicket ID : " + ticket.getTicketID();
        }
        return ok(msg);
    }

    public Result getTicket(String ticketID)
    {
        try
        {
            Ticket ticket=null;
            DB db = MongoDBConnection.connectToMongo();
            DBCollection coll = db.getCollection("TicketCollection");
            BasicDBObject query = new BasicDBObject("ticketID", ticketID);
            DBCursor cursor = coll.find(query);
            try
            {
                DBObject dbOb=cursor.next();
                Map map=dbOb.toMap();
                ticket = new Ticket();
                ticket.makeTicketFromMap(map);
            }
            finally
            {
                cursor.close();
            }
            return ok(viewTicket.render(ticket));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return ok(index.render("Ticket Filing System"));
    }

    public Result editTicket(String ticketID)
    {
        try
        {
            Ticket ticket=null;
            DB db = MongoDBConnection.connectToMongo();
            DBCollection coll = db.getCollection("TicketCollection");

            BasicDBObject query = new BasicDBObject("ticketID", ticketID);
            DBCursor cursor = coll.find(query);// only one document will be retrieved
            try
            {
                DBObject dbOb=cursor.next();
                Map map=dbOb.toMap();
                ticket = new Ticket();
                ticket.makeTicketFromMap(map);
            }
            finally
            {
                cursor.close();
            }

            Ticket ticket_new = Form.form(Ticket.class).bindFromRequest().get();
            Ticket ticket_old = ticket;

            String msg;
            if (ticket_old.getStatus().equals("CLOSED"))
            {
                msg="Ticket " + ticket_old.getTicketID() + " once closed, cannot be edited";
            }
            else
            {
                msg=ticket_new.validateTicketEntries();
                if (msg.equals("OK"))
                {
                    updateTicketWith(ticket_old, ticket_new);
                    msg="Ticket ID: " + ticket_new.ticketID + " updated Successfully";
                }
            }
            return ok(msg);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return redirect(routes.Application.index());
    }

    public Result ticketEditForm(String ticketID)
    {
        Ticket ticket=null;
        try
        {
            DB db = MongoDBConnection.connectToMongo();
            DBCollection coll = db.getCollection("TicketCollection");

            BasicDBObject query = new BasicDBObject("ticketID", ticketID);
            DBCursor cursor = coll.find(query);
            try
            {
                DBObject dbOb=cursor.next();
                Map map=dbOb.toMap();
                ticket = new Ticket();
                ticket.makeTicketFromMap(map);
            }
            finally
            {
                cursor.close();
            }

            if (ticket.getStatus().equals("CLOSED"))
            {
                String msg="Error!!!\nTicket " + ticket.getTicketID() + " once closed, cannot be edited";
                return ok(msg);
            }
            return ok(editTicket.render(ticket));

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return redirect(routes.Application.index());
    }

    public Result getTickets() {
        try
        {
            DB db = MongoDBConnection.connectToMongo();
            DBCollection coll = db.getCollection("TicketCollection");

            DBCursor cursor = coll.find();
            List tickets = new ArrayList<Ticket>();
            try
            {
                while(cursor.hasNext())
                {
                    DBObject dbOb = cursor.next();
                    tickets.add(dbOb);
                }
            }
            finally
            {
                cursor.close();
            }

            return ok(toJson(tickets));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return ok();
    }

    public Result closeTicket(String ticketID)
    {
        try
        {
            Ticket ticket=null;
            DB db = MongoDBConnection.connectToMongo();
            DBCollection coll = db.getCollection("TicketCollection");

            BasicDBObject query = new BasicDBObject("ticketID", ticketID);
            DBCursor cursor = coll.find(query);
            try
            {
                DBObject dbOb=cursor.next();
                Map map=dbOb.toMap();
                ticket = new Ticket();
                ticket.makeTicketFromMap(map);
            }
            finally
            {
                cursor.close();
            }

            if (ticket.getStatus().equals("CLOSED"))
            {
                String msg="Ticket " + ticket.getTicketID() + " is already Closed";
                return ok(msg);
            }
            else
            {
                Ticket ticket_new=new Ticket();
                ticket_new.copyTicket(ticket);
                ticket_new.setStatus("CLOSED");
                updateTicketWith(ticket, ticket_new);
                String msg="Ticket " + ticket.getTicketID() + " has been closed!";
                return ok(msg);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return redirect(routes.Application.index());
    }

    public void saveTicket(Ticket ticket)
    {
        try
        {
            DB db = MongoDBConnection.connectToMongo();
            DBCollection coll = db.getCollection("TicketCollection");

            BasicDBObject ob;
            ob = new BasicDBObject("ticketID",ticket.ticketID).append("name",ticket.name).append("nameID",ticket.nameID).append("createdBy",ticket.createdBy)
                    .append("assignedTo",ticket.assignedTo).append("issues",ticket.issues).append("status",ticket.status).append("comment",ticket.comment);
            coll.insert(ob);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void updateTicketWith(Ticket ticket_old,Ticket ticket_new)
    {
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

