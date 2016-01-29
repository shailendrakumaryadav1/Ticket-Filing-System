`
    $(function() {
        $.get("/tickets", function(tickets) {
            $.each(tickets, function(index, ticket) {
                var t0="<tr>";
                var t1=$("<td>").text(ticket.ticketID);
                var t2=$("<td>").text(ticket.name);
                var t3=$("<td>").text(ticket.nameID);
                var t4=$("<td>").text(ticket.createdBy);
                var t5=$("<td>").text(ticket.assignedTo);
                var t6=$("<td>").text(ticket.issues);
                var t7;
                if(ticket.status=="NEW")
                    t7=$("<td style=\"color: #ff0000\">").text(ticket.status);
                else if(ticket.status=="OPEN")
                    t7=$("<td style=\"color: #00B300\">").text(ticket.status);
                else if(ticket.status=="CLOSED")
                    t7=$("<td style=\"color: #0000ff\">").text(ticket.status);
                else
                    t7=$("<td style=\"color: #000000\">").text(ticket.status);
                var t8=""; //$("<td></td>").text(ticket.comment);
                var t9=$("<td></td>").html( "<button  onclick=\"viewTicket_Link( "+ticket.ticketID+" )\">view</button>"  );


  var t10=$("<td></td>").html( " <form action=\"/ticketEditForm/"+ticket.ticketID+"\" method=\"get\" ><button >edit</button></form>");

                var t11=$("<td></td>").html( "<a href=\"\"  onclick=\"closeTicket_Link( "+ticket.ticketID+" )\"> <button>close</button>  </a>"  );

                    //$("<td></td>").html( "<button  onclick=\"closeTicket_Link( "+ticket.ticketID+" )\">close</button>"  );

                var t12 ="</tr>";

                $("#table").append(t0,t1,t2,t3,t4,t5,t6,t7,t9,t10,t11,t12);
            });
        });
    });
$.put
`