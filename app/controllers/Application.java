package controllers;

import com.mongodb.*;
import models.Ticket;
import play.data.Form;
import play.mvc.*;
import views.html.*;
import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import static play.libs.Json.toJson;

public class Application extends Controller {

    public Result index()
    {
        return ok(index.render("Ticket Management System"));
    }

    public Result addTicket()
    {
        Ticket ticket = Form.form(Ticket.class).bindFromRequest().get();
        ticket.setTicketID();
        ticket.setInitialStatus();
        String validateMsg=ticket.validateTicketEntries();

        if (validateMsg.equals("OK"))
        {
            ticket.saveTicket();
            JOptionPane.showMessageDialog(null, "Ticket Added Successfully\nTicket ID : " + ticket.getTicketID());
        }
        else
        {
            JOptionPane.showMessageDialog(null, validateMsg);
        }

        return redirect(routes.Application.index());
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

            ticket.showTicketViewDialogBox();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return ok(index.render("Ticket Management System"));
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
                JOptionPane.showMessageDialog(null, msg , "Error", JOptionPane.ERROR_MESSAGE);
            }
            else
            {
                msg=ticket_new.validateTicketEntries();
                if (msg.equals("OK"))
                {
                    ticket_old.updateTicketWith(ticket_new);
                    JOptionPane.showMessageDialog(null, "Ticket ID: " + ticket_new.ticketID + " updated Successfully");
                }
                else
                {
                    JOptionPane.showMessageDialog(null, msg);
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
                JOptionPane.showMessageDialog(null, "Ticket " + ticket.getTicketID() + " once closed, cannot be edited", "Error", JOptionPane.ERROR_MESSAGE);
                return redirect(routes.Application.index());
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

            /*
            System.out.println("\n\n"+cursor.toArray().get(1).get("name")+"\n\n");

            List <DBObject> tickets = new ArrayList<DBObject>();List tickets = new ArrayList<Ticket>();List tickets = cursor.toArray();
            JSON json =new JSON();
            String serialize = JSON.serialize(cursor);
            System.out.println(serialize);
            System.out.println("-------------");
            System.out.println(JSON.parse(serialize));
            System.out.println("-------------");
            JsonReader jsonReader = Json.createReader(new StringReader(serialize));
            JsonObject object = jsonReader.readObject();
            jsonReader.close();

            DBObject t=((DBObject)tickets.get(2));
            System.out.println("\n\n"+t.get("name"));
            */

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
                JOptionPane.showMessageDialog(null, "Ticket " + ticket.getTicketID() + " is already Closed", "Error", JOptionPane.ERROR_MESSAGE);
            }
            else
            {
                int dialogButton = JOptionPane.showConfirmDialog(null, "Are you sure to close the Ticket with Ticket ID: " + ticket.getTicketID(), "WARNING", JOptionPane.YES_NO_OPTION);
                if (dialogButton == JOptionPane.YES_OPTION)
                {
                    Ticket ticket_new=new Ticket();
                    ticket_new.copyTicket(ticket);
                    ticket_new.setStatus("CLOSED");
                    ticket.updateTicketWith(ticket_new);
                    JOptionPane.showMessageDialog(null, "Ticket " + ticket.getTicketID() + " has been closed!");
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return redirect(routes.Application.index());
    }

}
