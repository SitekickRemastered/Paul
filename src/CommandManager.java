import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.entities.IMentionable;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CommandManager extends ListenerAdapter {

    Dotenv dotenv = Dotenv.configure().filename(".env").load();

    //These are for checking Paul's status
    String statusURL = dotenv.get("PAUL_PING_LINK");
    ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(3);

    /**
     * This function runs when the bot is ready. It gets the Status URL of the bot, and then
     * gets the URL every 45 seconds. If this fails, another bot will alert us, so it can be fixed.
     *
     * @param e - The ReadyEvent listener. Activates when the bot is ready / starts up
     */
    public void onReady(@NotNull ReadyEvent e) {

        // This is for the ping to make sure the Bot is working
        scheduler.scheduleAtFixedRate(() -> {
            try {
                URLConnection conn = new URL(statusURL).openConnection();
                conn.setRequestProperty("Accept-Charset", "UTF-8");
                InputStream response = conn.getInputStream();
                response.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }, 0, 45, TimeUnit.SECONDS);

    }

    public void onMessageReceived(MessageReceivedEvent e){
        if (!e.getAuthor().isBot()){
            if (e.getChannelType() == ChannelType.PRIVATE)
                e.getChannel().sendMessage("P̶̱͉̼̮̞͓̣͈̘̠̄̾̑̈͂̑͘ͅá̴͜u̵͉͈̲̘̺̰͖͓͕͎̠͈̭͗̈́̉́͗̅͌͒́̕̕l̴̞̻̰̩̣͌́̉̓̊̑̕͜").queue(message -> message.delete().queueAfter(10, TimeUnit.SECONDS));

            else{
                if (e.getMessage().getContentRaw().matches(".*([pP]+)\\s*([aA]+)\\s*([uU]+)\\s*([lL]+).*")){
                    e.getChannel().sendMessage("Paul").queue();
                }
                else if (e.getMessage().getContentRaw().contains("\uD83C\uDDF5 \uD83C\uDDE6 \uD83C\uDDFA \uD83C\uDDF1") ||
                        e.getMessage().getContentRaw().contains("\uD83C\uDDF5\uD83C\uDDE6\uD83C\uDDFA\uD83C\uDDF1")){
                    e.getChannel().sendMessage("Paul").queue();
                }
                else if (!e.getMessage().getAttachments().isEmpty()){
                    for (Message.Attachment a : e.getMessage().getAttachments()){
                        if (a.getFileName().matches("([pP]+)\\s*([aA]+)\\s*([uU]+)\\s*([lL]+)\\.(.*)$"))
                            e.getChannel().sendMessage("Paul").queue();
                    }
                }
                else{
                    for (IMentionable m : e.getMessage().getMentions().getMentions()){
                        if (m.toString().contains("Paul"))
                            e.getChannel().sendMessage("Paul").queue();
                    }
                }
            }
        }
    }
}
