package MessageEvents;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.requests.RestAction;
import net.dv8tion.jda.core.entities.PrivateChannel;
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class GardenMsgEvents {

    /* Takes in a string that holds character name (Non-optional) Starter (Optional), and Route(Optional) all seperated by spaces
    and returns all corresponding routes that satisfy the conditions provided
     */
    public static void GardenRoutes(String[] str, Connection conn, MessageChannel chn, User author) throws SQLException{
        String finalStmt = "Select * from sg_routes Where ";

        try {
            finalStmt+= "Name = \"" + str[1] + "\"";
        }
        catch(IndexOutOfBoundsException e){
            return ; //means it was an empty call with no parameters
        }

        try{

            finalStmt += " AND Starter = \"" + str[2] + "\"";

        }
        catch(IndexOutOfBoundsException e){
            //Do nothing since that means the starter was not provided
        }

        Statement s = conn.createStatement();
        ResultSet set = s.executeQuery(finalStmt);
        EmbedBuilder builder = new EmbedBuilder();

        int size =0;
        if (set != null)
        {
            set.last();    // moves cursor to the last row
            size = set.getRow(); // get row id
        }
        set.beforeFirst();
        if (size > 3){
            builder.setTitle("Sending DM");
            builder.addField("Notice", "Found more than 3 suitable entries, sending DM to avoid clutter", true);
            chn.sendMessage(builder.build()).queue();
            author.openPrivateChannel().queue((channel) ->
            {
                channel.sendMessage("Sending requested info").queue();
            });
        }
        while(set.next()){
            builder.clear();
            String Name = set.getString(1);
            String Starter = set.getString(2);
            String Route = set.getString(3);
            String Notes = set.getString(4);
            builder.setColor(Color.MAGENTA);
            builder.setTitle("Garden Routes");
            //builder.setThumbnail("https://pbs.twimg.com/media/CmVZ097UEAAUKyn.jpg");
            builder.addField("Character:", Name, true);
            builder.addField("Route", Route, true);
            if(Notes != null) {
                builder.addField("Notes", Notes, true);
            }
            MessageEmbed em = builder.build();
            if(size < 3) {
                chn.sendMessage(builder.build()).queue();
            }
            else{
                author.openPrivateChannel().queue((c) ->
                {
                    c.sendMessage(em).queue();
                });
            }
        }

    }

    /* str is the command that was entered split into an array with the " " being where each new entry is determined
       as of now only my account is able to update the database
     */
    public static void addToGarden(String[] str, User author, Connection conn)throws SQLException{
        if(author.getId().equals("178407700797849600")) {
            String stmt = "INSERT INTO sg_routes (Name, Starter, Route,";
            if(str.length ==6){
                 stmt += "Notes";
            }

            stmt += ") VALUES(\"" + str[2] +"\", \"" +
                    str[3] +"\", \"" +
                    str[4] +"\"";
            if(str.length == 6){
                stmt += ", \"" + str[5] + "\"";
            }
            stmt += ")";

            Statement s = conn.createStatement();
            s.executeUpdate(stmt);


        }
    }
}
