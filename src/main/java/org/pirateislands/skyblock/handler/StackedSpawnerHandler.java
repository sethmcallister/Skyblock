package org.pirateislands.skyblock.handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.bukkit.Location;
import org.pirateislands.skyblock.SkyBlock;
import org.pirateislands.skyblock.dto.StackedSpawner;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StackedSpawnerHandler {
    private final transient Gson gson;
    private final transient String fileName;
    private List<StackedSpawner> stackedSpawners;

    public StackedSpawnerHandler() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.fileName = SkyBlock.getInstance().getModuleDir() + File.separator + "spawners.json";
        this.stackedSpawners = new ArrayList<>();
    }

    public StackedSpawner findByLocation(final Location location) {
        return this.stackedSpawners.stream().filter(stackedSpawner -> stackedSpawner.getLocation().equals(location)).findFirst().orElse(null);
    }

    public List<StackedSpawner> findAll() {
        return this.stackedSpawners;
    }

    public void save() {
        String json = this.gson.toJson(this);
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
                StackedSpawnerHandler stackedSpawnerHandler = this.gson.fromJson(element, StackedSpawnerHandler.class);
                if (stackedSpawnerHandler == null) {
                    save();
                    return;
                }

                if (stackedSpawnerHandler.stackedSpawners == null)
                    return;

                this.stackedSpawners = stackedSpawnerHandler.stackedSpawners;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
