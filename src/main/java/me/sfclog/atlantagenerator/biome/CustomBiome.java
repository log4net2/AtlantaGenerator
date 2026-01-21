package me.sfclog.atlantagenerator.biome;


import org.bukkit.block.Biome;
import org.bukkit.generator.BiomeProvider;
import org.bukkit.generator.WorldInfo;

import java.util.ArrayList;
import java.util.List;


public class CustomBiome extends BiomeProvider {

    public WorldInfo world;
    public CustomBiome(WorldInfo world) {
        this.world = world;
    }
    @Override
    public Biome getBiome( WorldInfo worldInfo, int i, int i1, int i2) {
        return Biome.COLD_OCEAN;
    }
    @Override
    public List<Biome> getBiomes( WorldInfo worldInfo) {
        List<Biome> biome = new ArrayList<>();
        biome.add(Biome.COLD_OCEAN);
        return biome;
    }
}
