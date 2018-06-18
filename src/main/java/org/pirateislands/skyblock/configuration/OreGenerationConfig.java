package org.pirateislands.skyblock.configuration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import org.bukkit.Material;
import org.pirateislands.skyblock.SkyBlock;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Owned by SethyCorp, and KueMedia respectively.
 **/
public class OreGenerationConfig {
    private final transient Gson gson;
    private final transient String fileName;
    private final Map<Material, Double> generationMap;

    public OreGenerationConfig() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.generationMap = new HashMap<>();
        this.generationMap.put(Material.COBBLESTONE, 35.0);
        this.generationMap.put(Material.COAL_ORE, 15.0);
        this.generationMap.put(Material.IRON_ORE, 10.0);
        this.generationMap.put(Material.DIAMOND_ORE, 5.0);
        this.generationMap.put(Material.LAPIS_ORE, 10.0);
        this.generationMap.put(Material.REDSTONE_ORE, 10.0);
        this.generationMap.put(Material.GOLD_ORE, 10.0);
        this.generationMap.put(Material.EMERALD_ORE, 5.0);
        this.fileName = SkyBlock.getPlugin().getModuleDir() + File.separator + "ores.json";
    }

    public Map<Material, Double> getGenerationMap() {
        return generationMap;
    }

    public Double getValue(final Material key) {
        return generationMap.get(key);
    }

    public void save() {
        String json = this.gson.toJson(generationMap);
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

    public void loadValues() {
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
                Type type = new TypeToken<Map<Material, Double>>() {
                }.getType();
                Map<Material, Double> ores = this.gson.fromJson(element, type);
                if (ores == null) {
                    save();
                    return;
                }

                ores.forEach(this.generationMap::put);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
