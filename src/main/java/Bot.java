
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.user.update.UserUpdateOnlineStatusEvent;

public class Bot{

    public static void main(String[] args) throws Exception{
        String key = "_____";
        JDA api = new JDABuilder(key).build();
        api.addEventListener(new MyListener());
        api.getPresence().setGame(Game.playing("!help"));



    }


}

