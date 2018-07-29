package org.pirateislands.skyblock.configuration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.islesmc.islandapi.goose.GooseLocation;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.pirateislands.skyblock.SkyBlock;
import org.pirateislands.skyblock.goose.GooseLocationHelper;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ServerConfig {
    private final transient String fileName;
    private final transient Gson gson;
    private GooseLocation spawn;
    private Integer playersJoined;
    private GooseLocation lastIslandLocation;
    private String scoreboardName;
    private String primaryColor;
    private ServerType serverType;

    public ServerConfig() {
        this.fileName = SkyBlock.getInstance().getModuleDir() + File.separator + "config.json";
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.spawn = new GooseLocation(Bukkit.getWorld("Spawn").getUID(), 0d, 0d, 0d, 0d);
        this.playersJoined = 0;
        this.lastIslandLocation = new GooseLocation(null, 0D, 0D, 0D, 0D);
        this.scoreboardName = "";
        this.primaryColor = "&a";
        this.serverType = ServerType.SKY;
    }

    public Location getSpawnLocation() {
        return GooseLocationHelper.toLocation(spawn);
    }

    public void setSpawnLocation(final GooseLocation location) {
        this.spawn = location;
    }

    public void save() {
        String json = this.gson.toJson(this);
        System.out.println(json);
        File file = new File(this.fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try (FileWriter writer = new FileWriter(file)) {
            writer.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void load() {
        File file = new File(this.fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            JsonParser parser = new JsonParser();

            try (FileReader fileReader = new FileReader(this.fileName)) {
                JsonElement element = parser.parse(fileReader);
                ServerConfig serverConfig = this.gson.fromJson(element, ServerConfig.class);
                if (serverConfig == null) {
                    save();
                    return;
                }
                this.spawn = serverConfig.spawn;
                this.lastIslandLocation = serverConfig.lastIslandLocation;
                this.playersJoined = serverConfig.playersJoined;
                this.scoreboardName = serverConfig.scoreboardName;
                this.primaryColor = serverConfig.primaryColor;
                this.serverType = serverConfig.serverType;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public GooseLocation getLastIslandLocation() {
        return lastIslandLocation;
    }

    public void setLastIslandLocation(GooseLocation lastIslandLocation) {
        this.lastIslandLocation = lastIslandLocation;
    }

    public Integer getPlayersJoined() {
        return playersJoined;
    }

    public void incrementPlayersJoined() {
        this.playersJoined += 1;
    }

    public String getScoreboardName() {
        return scoreboardName;
    }

    public String getPrimaryColor() {
        return primaryColor;
    }

    public ServerType getServerType() {
        return serverType;
    }
}
