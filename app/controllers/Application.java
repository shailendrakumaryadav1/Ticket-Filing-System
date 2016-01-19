package controllers;


import models.Ticket;
import play.*;

import play.data.Form;
import play.db.ebean.Model;
import play.mvc.*;

import views.html.*;

import javax.swing.*;
import java.util.Collections;
import java.util.List;

import static play.libs.Json.toJson;

public class Application extends Controller {


    public Result index() {
        return ok(index.render("Your new application is ready."));
    }

    public Result addTicket()
    {

        Ticket ticket = Form.form(Ticket.class).bindFromRequest().get();





        if(ticket.validateTicketEntries()) {
            ticket.setTicketID();
            ticket.setStatus();
            ticket.save();
            JOptionPane.showMessageDialog(null, "Ticket Added Successfully\nTicket ID : " + ticket.ticketID);
        }
        else
        {
            JOptionPane.showMessageDialog(null, "Invalid Entries for the Ticket");
        }

        return redirect(routes.Application.index());

    }

    public Result getTicket(String ticketID)
    {

        Ticket ticket = (Ticket) new Model.Finder(String.class,Ticket.class).byId(ticketID);


        JOptionPane.showMessageDialog(null, "view window "+ticket.ticketID);
        return redirect(routes.Application.index());
    }
    public Result editTicket(String ticketID)
    {

        Ticket ticket = (Ticket) new Model.Finder(String.class,Ticket.class).byId(ticketID);

        //JOptionPane.showMessageDialog(null, "edit window hii"+ticket.ticketID);
        if(ticket.status.equals("CLOSED")) {
            JOptionPane.showMessageDialog(null,"Ticket "+ticket.ticketID+" once closed, cannot be edited","Error",JOptionPane.ERROR_MESSAGE);
        }
        else
        {
            JOptionPane.showMessageDialog(null, "edit window " + ticket.ticketID);
        }

        return redirect(routes.Application.index());
        //return null;
    }
    public Result getTickets()
    {
        List <Ticket> tickets = new Model.Finder(String.class,Ticket.class).all();

        java.util.Collections.sort(tickets,(o1, o2) -> {
            if(o1.ticketID.length() == o2.ticketID.length())
                return o1.ticketID.compareTo(o2.ticketID);
            return o1.ticketID.length() - o2.ticketID.length();
        });

        return ok(toJson(tickets));
    }
    public Result closeTicket(String ticketID)
    {
        Ticket ticket = (Ticket) new Model.Finder(String.class,Ticket.class).byId(ticketID);

        if(ticket.status.equals("CLOSED")) {
            JOptionPane.showMessageDialog(null, "Ticket "+ticket.ticketID+" is already Closed","Error",JOptionPane.ERROR_MESSAGE);
        }
        else
        {
            ticket.status="CLOSED";
            ticket.save();
            JOptionPane.showMessageDialog(null, "Ticket " + ticket.ticketID + " has been closed!");
        }
        return redirect(routes.Application.index());
    }

}
