package MessageEvents;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.User;

import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class BurstMsgEvents {

    public static void BurstRoutes(String[] str, Connection conn, MessageChannel chn, User author) throws SQLException {
        String finalStmt = "Select * from burst_safe Where ";
        EmbedBuilder builder = new EmbedBuilder();
        try {
            finalStmt+= "Name = \"" + str[1] + "\"";
        }
        catch(IndexOutOfBoundsException e){
            builder.setTitle("Notice");
            builder.addField("Notice", "Please specify character name (required), and starter (optional)", true);
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

        // Format output
        while(set.next()){
            builder.clear();
            String Name = set.getString(1);
            String Starter = set.getString(2);
            String Route = set.getString(3);
            String Notes = set.getString(4);
            builder.setColor(Color.MAGENTA);
            builder.setTitle("Burst Safe Routes");
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
    public static void addToBurst(String[] str, User author, Connection conn)throws SQLException{
        if(author.getId().equals("178407700797849600")) {
            String stmt = "INSERT INTO burst_safe (Name, Starter, Route";
            if(str.length ==6){
                stmt += ",Notes";
            }
            // Format to proper syntax for SQL
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

