package org.SitekickRemastered;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import org.SitekickRemastered.commands.*;
import org.SitekickRemastered.listeners.CommandManager;
import org.SitekickRemastered.listeners.EventListeners;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.configure().directory("src/main/java/org/SitekickRemastered/.env").load();
        String token = dotenv.get("PAUL_TOKEN");
        List<String> bannedUsers = new ArrayList<>();
        String listPath = "src/main/java/org/SitekickRemastered/bannedUsers.txt";
        loadBannedUsers(bannedUsers, listPath);

        DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(token);
        builder.setActivity(Activity.of(Activity.ActivityType.PLAYING, "Paul"));
        builder.enableIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.MESSAGE_CONTENT);

        ShardManager sm = builder.build();
        sm.addEventListener(new EventListeners(bannedUsers));

        CommandManager cm = new CommandManager(dotenv);
        cm.add(new PollCommand());
        cm.add(new EndPollCommand());
        cm.add(new BlacklistCommand(bannedUsers, listPath));
        cm.add(new DelistCommand(bannedUsers, listPath));
        // cm.add(new DeleteMessageCommand());

        sm.addEventListener(cm);
    }

    /** Loads the list of banned users from the .txt file.
     * @param bannedUsers - The list to save the banned users in.
     * @param listPath - The path of the banned users list.
     */
    public static void loadBannedUsers(List<String> bannedUsers, String listPath){
        try {

            // If the file doesn't exist, it's created.
            File bannedUsersFile = new File(listPath);
            boolean fileCreated = bannedUsersFile.createNewFile();
            if (fileCreated)
                System.out.println(listPath + " didn't exist! File was created successfully!");

            // Read all lines and add them to the list.
            bannedUsers.addAll(Files.readAllLines(Paths.get(listPath)));
        }
        catch (IOException ex) { throw new RuntimeException(ex); }
    }
}