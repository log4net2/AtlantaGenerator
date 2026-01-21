package me.sfclog.atlantagenerator;

import me.sfclog.atlantagenerator.chunk.AtlantaGenerator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {


    public ChunkGenerator getDefaultWorldGenerator(String worldName, String id) {
        return new AtlantaGenerator();
    }

}
