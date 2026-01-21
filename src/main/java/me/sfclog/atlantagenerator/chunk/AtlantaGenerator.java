package me.sfclog.atlantagenerator.chunk;

import me.sfclog.atlantagenerator.biome.CustomBiome;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.CoralWallFan;
import org.bukkit.block.data.type.SeaPickle;
import org.bukkit.generator.BiomeProvider;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.WorldInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class AtlantaGenerator extends ChunkGenerator {


    public static final Material[] CORAL_FANS = {
            Material.BRAIN_CORAL_FAN,
            Material.BUBBLE_CORAL_FAN,
            Material.FIRE_CORAL_FAN,
            Material.HORN_CORAL_FAN,
            Material.TUBE_CORAL_FAN,
            Material.BRAIN_CORAL_BLOCK,
            Material.BUBBLE_CORAL_BLOCK,
            Material.FIRE_CORAL_BLOCK,
            Material.HORN_CORAL_BLOCK,
            Material.TUBE_CORAL_BLOCK,
    };
    public static final Material[] CORAL_WALL_FANS = {
            Material.BRAIN_CORAL_WALL_FAN,
            Material.BUBBLE_CORAL_WALL_FAN,
            Material.FIRE_CORAL_WALL_FAN,
            Material.HORN_CORAL_WALL_FAN,
            Material.TUBE_CORAL_WALL_FAN,
    };
    public static final BlockFace[] FACES = {
            BlockFace.EAST, BlockFace.WEST,
            BlockFace.NORTH, BlockFace.SOUTH,
            BlockFace.EAST, BlockFace.WEST,
            BlockFace.NORTH, BlockFace.SOUTH,
            BlockFace.EAST, BlockFace.WEST,
            BlockFace.NORTH, BlockFace.SOUTH,
            BlockFace.UP, BlockFace.DOWN
    };

    @Override
    public @Nullable BiomeProvider getDefaultBiomeProvider(@NotNull WorldInfo worldInfo) {
        return new CustomBiome(worldInfo);
    }

    @Override
    public void generateNoise(@NotNull WorldInfo worldInfo, @NotNull Random random, int chunkX, int chunkZ, @NotNull ChunkGenerator.ChunkData chunkData) {
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {

                //bedrock
                chunkData.setBlock(x, 0, z, Material.BEDROCK);
                //sandstone
                for(int i = 1 ; i < 5 ; i ++) {
                    chunkData.setBlock(x, i, z, Material.SANDSTONE);
                }
                //sand
                for(int i = 5 ; i < 8 ; i ++) {
                    chunkData.setBlock(x, i, z, randMaterial(
                            Material.SAND,
                            Material.SANDSTONE,
                            Material.GRAVEL
                    ));
                }

                //water
                for (int y = 8; y < 200 ; y++) {
                    chunkData.setBlock(x, y, z, Material.WATER);
                }

                //random kelp
               if(randInt(1, 70) == 5) generateKelpGrowth(chunkData,x,8,z);
                //random coral
                if(randInt(1, 50) == 5) generateSingleCoral(chunkData,x,8,z);
                //random pick
                if(randInt(1, 100) == 5) generateSeaPickles(chunkData,x,8,z);



                //bedrock
                chunkData.setBlock(x, 201, z, Material.BEDROCK);


            }
        }
    }


    public static void generateSingleCoral(ChunkData data, int x, int y, int z) {
        BlockFace face = getRandomBlockFace();

        if (face == BlockFace.DOWN) face = BlockFace.UP;
        Material coral = CORAL_FANS[randInt(0, CORAL_FANS.length - 1)];
        if (face != BlockFace.UP) coral = CORAL_WALL_FANS[randInt(0, CORAL_WALL_FANS.length - 1)];

        attemptReplace(data, x + face.getModX(), y , z + face.getModZ(), coral);
        if (face != BlockFace.UP) {
            if (data.getBlockData(x + face.getModX(), y , z + face.getModZ()) instanceof CoralWallFan) {
                CoralWallFan bdata = (CoralWallFan) data.getBlockData(x + face.getModX(), y , z + face.getModZ());
                bdata.setFacing(face);
                data.setBlock(x + face.getModX(), y , z + face.getModZ(), bdata);
            }
        }

    }

    public static Material randMaterial(Material... candidates) {
        if(candidates.length == 1) return candidates[0]; //avoid invocation to randInt
        return candidates[randInt(0, candidates.length - 1)];
    }
    public static int randInt(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    public static void generateSeaPickles(ChunkGenerator.ChunkData chunkData, int x, int y, int z) {
        int fullSize = randInt(1, 4);
        if (attemptReplace(chunkData, x, y, z, Material.SEA_PICKLE)) {
            if (chunkData.getBlockData(x, y, z) instanceof SeaPickle) {
                SeaPickle state = (SeaPickle) chunkData.getBlockData(x, y, z);
                state.setPickles(fullSize);
                chunkData.setBlock(x, y, z, state);
            }
        }
    }

    public static void generateKelpGrowth(ChunkGenerator.ChunkData chunkData, int x, int y, int z) {
        int fullSize = randInt(1, 2);
        if (new Random().nextBoolean()) fullSize += randInt(1, 30);
        if (fullSize == 1) {
            attemptReplace(chunkData, x, y, z, Material.SEAGRASS);
        } else if (fullSize == 2 && y <  3) {
            attemptReplace(chunkData, x, y, z, Material.TALL_SEAGRASS);
        } else {
            for (int size = 0; size < fullSize; size++) {
                if (!attemptReplace(chunkData, x, y, z, Material.KELP_PLANT))
                    break;
                y++;
            }
        }
    }
    public static boolean attemptReplace(ChunkData chunkData, int x, int y, int z, Material newType) {
        Material type = chunkData.getType(x,y,z);
        if (type != Material.WATER && type != Material.SEAGRASS && type != Material.TALL_SEAGRASS && type != Material.KELP_PLANT) return false;
        chunkData.setBlock(x,y,z,newType);
        return true;
    }


    public static BlockFace getRandomBlockFace() {
        return FACES[randInt(0, 13)];
    }

    @Override
    public boolean canSpawn(World world, int x, int z) {
        return true;
    }

    @Override
    public List<BlockPopulator> getDefaultPopulators(World world) {
        return Collections.emptyList();
    }
}