package controllers;

import models.Ticket;
import play.*;
import play.data.Form;
import play.db.ebean.Model;
import play.mvc.*;
import views.html.*;
import javax.swing.*;
import java.util.List;
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
        Ticket ticket = (Ticket) new Model.Finder(String.class, Ticket.class).byId(ticketID);
        ticket.showTicketViewDialogBox();
        return redirect(routes.Application.index());
    }

    public Result editTicket(String ticketID) {
        Ticket ticket = (Ticket) new Model.Finder(String.class, Ticket.class).byId(ticketID);
        if (ticket.getStatus().equals("CLOSED"))
        {
            JOptionPane.showMessageDialog(null, "Ticket " + ticket.getTicketID() + " once closed, cannot be edited", "Error", JOptionPane.ERROR_MESSAGE);
        }
        else
        {
            ticket.showTicketEditDialogBox();
        }
        return redirect(routes.Application.index());
    }

    public Result getTickets() {
        List<Ticket> tickets = new Model.Finder(String.class, Ticket.class).all();
        java.util.Collections.sort(tickets, (o1, o2) -> {
            if (o1.getTicketID().length() == o2.getTicketID().length())
                return o1.getTicketID().compareTo(o2.getTicketID());
            return o1.getTicketID().length() - o2.getTicketID().length();
        });
        return ok(toJson(tickets));
    }

    public Result closeTicket(String ticketID)
    {
        Ticket ticket = (Ticket) new Model.Finder(String.class, Ticket.class).byId(ticketID);
        if (ticket.getStatus().equals("CLOSED"))
        {
            JOptionPane.showMessageDialog(null, "Ticket " + ticket.getTicketID() + " is already Closed", "Error", JOptionPane.ERROR_MESSAGE);
        }
        else
        {
            int dialogButton = JOptionPane.showConfirmDialog(null, "Are you sure to close the Ticket with Ticket ID: " + ticket.getTicketID(), "WARNING", JOptionPane.YES_NO_OPTION);
            if (dialogButton == JOptionPane.YES_OPTION)
            {
                ticket.setStatus("CLOSED");
                ticket.saveTicket();
                JOptionPane.showMessageDialog(null, "Ticket " + ticket.getTicketID() + " has been closed!");
            }
        }
        return redirect(routes.Application.index());
    }
}
