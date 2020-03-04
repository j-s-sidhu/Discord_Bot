
import MessageEvents.BurstMsgEvents;
import MessageEvents.GardenMsgEvents;
import MessageEvents.GardenMsgEvents;
import MessageEvents.GeneralMsgEvents;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.user.update.UserUpdateOnlineStatusEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;

public class MyListener extends ListenerAdapter {

    Connection conn;

    public MyListener() throws Exception{
        // Connect to database
        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "blank");

    }

    // @Override
    public void onMessageReceived(MessageReceivedEvent event)  {
        if (!event.getAuthor().isBot()) {
            //Get author id, channel, and message content
            Message msg = event.getMessage();
            String[] message = msg.getContentRaw().toLowerCase().split(" ");
            User author = event.getAuthor();
            MessageChannel chn = event.getChannel();

            if((message[0].substring(0,1)).equals("!")){
                String cmd = (message[0]).toLowerCase(); //Determine which command to use

                try {
                    switch (cmd) {
                        case "!garden":
                            GardenMsgEvents.GardenRoutes(message, conn, chn, author);
                            break;

                        case "!addto":
                            String cmd2 = message[1];
                            switch(cmd2){
                                case "sg_routes":
                                    GardenMsgEvents.addToGarden(message, author, conn);
                                    break;
                                case "burst_safe":
                                    BurstMsgEvents.addToBurst(message, author, conn);
                                    break;
                            }
                            break;
                        case "!burstsafe":
                            BurstMsgEvents.BurstRoutes(message, conn, chn, author);
                            break;
                        case "!help":
                            GeneralMsgEvents.help(conn, chn);
                            break;

                        default:
                            chn.sendMessage("Unknown command enter !help for a list of commands and their formatting");
                            break;
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }

            }


        }
    }
}

