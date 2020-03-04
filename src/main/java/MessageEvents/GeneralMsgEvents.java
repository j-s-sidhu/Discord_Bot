package MessageEvents;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageChannel;

import java.awt.*;
import java.sql.Connection;

public class GeneralMsgEvents {

    public static void help(Connection conn, MessageChannel chn){
        EmbedBuilder builder = new EmbedBuilder();

        builder.setTitle("Command List");
        builder.setColor(Color.CYAN);
        builder.addField("!garden", "Provide a secret garden setup route. Format: \"!garden character [required] starter [optional]\"", true);
        builder.addField("!burstsafe", "Provide a burst safe corner setup. Format: \"!burstsafe character [required] starter [optional]\"", true);
        builder.addField("!addTo", "add a route to the specified table. Format: \"!addTo table [required] character [required] starter [required] route (seperate with \">\" no spaces allowed) [required] notes [optional]\"", true);
        chn.sendMessage(builder.build()).queue();
    }
}
