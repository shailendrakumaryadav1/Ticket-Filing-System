package models;

import play.db.ebean.Model;
import play.db.ebean.Model;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.swing.*;
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

}
