
`




    $(function() {

        /*
         var t1="<tr>";
         var t2=$("<th>").text("Name");
         var t3=$("<th></th>").text("Comment");
         var t4 ="</tr>";

         $("#table").append(t1,t2,t3,t4);
         */






        return $.get("/tickets", function(tickets) {
            $.each(tickets, function(index, ticket) {


                var t0="<tr>";
                var t1=$("<td>").text(ticket.ticketID);
                var t2=$("<td>").text(ticket.name);
                var t21=$("<td>").text(ticket.nameID);
                var t22=$("<td>").text(ticket.createdBy);
                var t23=$("<td>").text(ticket.assignedTo);
                var t24=$("<td>").text(ticket.issues);




                var t25;


                if(ticket.status=="NEW")
                    t25=$("<td style=\"color: #ff0000\">").text(ticket.status);
                else if(ticket.status=="OPEN")
                    t25=$("<td style=\"color: #00B300\">").text(ticket.status);
                else
                    t25=$("<td style=\"color: #0000ff\">").text(ticket.status);






                var t3=$("<td></td>").text(ticket.comment);

                var t31=$("<td></td>").html( "<a href=\"\"  onclick=\"viewTicket_Link( "+ticket.ticketID+" )\">view</a>"  );


                var t4=$("<td></td>").html( "<a href=\"\"  onclick=\"editTicket_Link( "+ticket.ticketID+" )\">edit</a>"  );
                var t41=$("<td></td>").html( "<a href=\"\"  onclick=\"closeTicket_Link( "+ticket.ticketID+" )\">close</a>"  );
                var t5 ="</tr>";


                $("#table").append(t0,t1,t2,t21,t22,t23,t24,t25,t3,t31,t4,t41,t5);
            });

        });
    });
$.put

`

###
  $ ->
  $.get "/tickets", (tickets) ->
    $.each tickets, (index,ticket) ->
      $("#table").append $("<td>").text ticket.name
      $("#table").append $("<td>").text ticket.comment



$(function()
  {

  return $.get("/tasks", function(data) {

                                            return $.each(data, function(index, task) {
                                                                                  return $("#tasks").append($("<li>").text(task.contents));
                                                                                      } );
                                        } );
  } );

  <!DOCTYPE html>
<html>
<head>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
<script>
function appendText() {
    var txt1 = "<p>Text.</p>";              // Create text with HTML
    var txt2 = $("<p></p>").text("Text.");  // Create text with jQuery
    var txt3 = document.createElement("p");
    txt3.innerHTML = "Text.";               // Create text with DOM
    $("body").append(txt1, txt2, txt3);     // Append new elements
}
</script>
</head>
<body>

<p>This is a paragraph.</p>
<button onclick="appendText()">Append text</button>

</body>
</html>




  `
    $(function() {
        return $.get("/tickets", function(tickets) {
            return $.each(tickets, function(index, ticket) {
                return $("#table"). append("<tr>").append($("<td></td>").text(ticket.name)).
                    append($("<td></td>").text(ticket.comment)).append("</tr>");


                var txt3 = document.createElement();
                txt3.innerHTML = "<tr><td>"+ticket.name+"</td><td>"+ticket.comment+"</td></tr>";               // Create text with DOM
                $("body").append(txt1, txt2, txt3);
            });
        });
    });
`

  ###