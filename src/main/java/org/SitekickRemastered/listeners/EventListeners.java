package org.SitekickRemastered.listeners;

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
import java.net.URI;
import java.net.URLConnection;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class EventListeners extends ListenerAdapter {

    String statusURL;
    ScheduledExecutorService pingThread = Executors.newSingleThreadScheduledExecutor();

    List<String> bannedUsers;


    public EventListeners(Dotenv dotenv, List<String> bannedUsers) {
        statusURL = dotenv.get("PAUL_PING_LINK");
        this.bannedUsers = bannedUsers;
    }


    public void onReady(@NotNull ReadyEvent e) {

        // Sets a thread to run every minute to ping Paul's status URL. If it fails, another bot alerts us, so we can fix it.
        pingThread.scheduleAtFixedRate(() -> {
            try {
                URLConnection conn = URI.create(statusURL).toURL().openConnection();
                conn.setRequestProperty("Accept-Charset", "UTF-8");
                InputStream response = conn.getInputStream();
                response.close();
            }
            catch (IOException ex) {
                System.err.println("ERROR: Failed to send status ping to Paul.");
            }
        }, 0, 60, TimeUnit.SECONDS);
    }


    public void onMessageReceived(MessageReceivedEvent e) {

        if (e.getAuthor().isBot() || bannedUsers.contains(e.getAuthor().getId()))
            return;

        if (e.getChannelType() == ChannelType.PRIVATE)
            e.getChannel().sendMessage("Paul").queue(message -> message.delete().queueAfter(10, TimeUnit.SECONDS));

        else {

            // Catches any type of "paul" and regional indicators
            if (e.getMessage().getContentRaw().matches(".*([pP\uD83C\uDDF5]+)\\s*([aA\uD83C\uDDE6]+)\\s*([uU\uD83C\uDDFA]+)\\s*([lL\uD83C\uDDF1]+).*")) {
                e.getChannel().sendMessage("Paul").queue();
            }

            // Checks if an attachment contains "paul"
            else if (!e.getMessage().getAttachments().isEmpty()) {
                for (Message.Attachment a : e.getMessage().getAttachments()) {
                    if (a.getFileName().matches("([pP]+)\\s*([aA]+)\\s*([uU]+)\\s*([lL]+)\\.(.*)$"))
                        e.getChannel().sendMessage("Paul").queue();
                }
            }

            // Catches mentions of "paul"
            else {
                for (IMentionable m : e.getMessage().getMentions().getMentions()) {
                    if (m.toString().contains("Paul"))
                        e.getChannel().sendMessage("Paul").queue();
                }
            }
        }
    }
}