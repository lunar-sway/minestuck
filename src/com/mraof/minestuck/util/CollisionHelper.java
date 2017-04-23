package com.mraof.minestuck.util;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by mraof on 2017 April 18 at 11:57 AM.
 * Used for faster collision detection with blocks for large entities
 */
public class CollisionHelper
{
    private static HashMap<Integer, HashMap<ChunkPos, CollisionChunk>> chunks = new HashMap<Integer, HashMap<ChunkPos, CollisionChunk>>();

    public static boolean collides(World world, AxisAlignedBB collisionBox)
    {
        return !getCollidingBlocks(world, collisionBox, true).isEmpty();
    }

    public static ArrayList<BlockPos> getCollidingBlocks(World world, AxisAlignedBB collisionBox)
    {
        return getCollidingBlocks(world, collisionBox, false);
    }

    public static ArrayList<BlockPos> getCollidingBlocks(World world, AxisAlignedBB collisionBox, boolean returnFirst)
    {
        ArrayList<BlockPos> collidingBlocks = new ArrayList<BlockPos>();
        int minX = MathHelper.floor(collisionBox.minX);
        int maxX = MathHelper.ceil(collisionBox.maxX);
        int minY = MathHelper.clamp(0, MathHelper.ceil(collisionBox.minY), 255);
        int maxY = MathHelper.clamp(0, MathHelper.ceil(collisionBox.maxY), 255);
        int minZ = MathHelper.floor(collisionBox.minZ);
        int maxZ = MathHelper.ceil(collisionBox.maxZ);
        for (int chunkX = minX >> 4; chunkX <= maxX >> 4; chunkX++)
        {
            int chunkMinX = (minX > (chunkX << 4)) ? (minX & 15) : 0;
            int chunkMaxX = (maxX < ((chunkX << 4) + 15)) ? (maxX & 15) : 15;
            for (int chunkZ = minZ >> 4; chunkZ <= maxZ >> 4; chunkZ++)
            {
                int chunkMinZ = (minZ > (chunkZ << 4)) ? (minZ & 15) : 0;
                int chunkMaxZ = (maxZ < ((chunkZ << 4) + 15)) ? (maxZ & 15) : 15;
                CollisionChunk chunk = getChunk(world, new ChunkPos(chunkX, chunkZ));
                if (chunk == null)
                {
                    continue;
                }
                for (int y16 = minY >> 4; y16 <= maxY >> 4; y16++)
                {
                    if(chunk.cube8[y16] == 0) {
                        continue;
                    }
                    int cubeMinY = (minY > (y16 << 4)) ? (minY & 15) : 0;
                    int cubeMaxY = (maxY < ((y16 << 4) + 15)) ? (maxY & 15) : 15;
                    int minX8 = chunkMinX > 7 ? 1 : 0;
                    int maxX8 = chunkMaxX < 8 ? 0 : 1;
                    int minY8 = cubeMinY > 7 ? 1 : 0;
                    int maxY8 = cubeMaxY < 8 ? 0 : 1;
                    int minZ8 = chunkMinZ > 7 ? 1 : 0;
                    int maxZ8 = chunkMaxZ < 8 ? 0 : 1;
                    for (int y8 = minY8; y8 <= maxY8; y8++)
                    {
                        for (int z8 = minZ8; z8 <= maxZ8; z8++)
                        {
                            for (int x8 = minX8; x8 <= maxX8; x8++)
                            {
                                //index of current 8x8 block relative to 16x16 block
                                int index8 = (y8 << 2 | z8 << 1 | x8);
                                if (chunk.cube4[y16 << 3 | index8] == 0)
                                {
                                    continue;
                                }
                                int minX4 = x8 == minX8 && (chunkMinX % 8) > 3 ? 1 : 0;
                                int maxX4 = x8 == maxX8 && (chunkMaxX % 8) < 4 ? 0 : 1;
                                int minY4 = y8 == minY8 && (cubeMinY % 8) > 3 ? 1 : 0;
                                int maxY4 = y8 == maxY8 && (cubeMaxY % 8) < 4 ? 0 : 1;
                                int minZ4 = z8 == minZ8 && (chunkMinZ % 8) > 3 ? 1 : 0;
                                int maxZ4 = z8 == maxZ8 && (chunkMaxZ % 8) < 4 ? 0 : 1;
                                for (int y4 = minY4; y4 <= maxY4; y4++)
                                {
                                    for (int z4 = minZ4; z4 <= maxZ4; z4++)
                                    {
                                        for (int x4 = minX4; x4 <= maxX4; x4++)
                                        {
                                            //index of current 4x4 block relative to 8x8 block
                                            int index4 = (y4 << 2 | z4 << 1 | x4);
                                            byte bitflags = chunk.cube2[y16 << 6 | index8 << 3 | index4];
                                            if (bitflags == 0)
                                            {
                                                continue;
                                            }
                                            int minX2 = x8 == minX8 && x4 == minX4 && (chunkMinX % 4) > 1 ? 1 : 0;
                                            int maxX2 = x8 == maxX8 && x4 == maxX4 && (chunkMaxX % 4) < 2 ? 0 : 1;
                                            int minY2 = y8 == minY8 && y4 == minY4 && (cubeMinY % 4) > 1 ? 1 : 0;
                                            int maxY2 = y8 == maxY8 && y4 == maxY4 && (cubeMaxY % 4) < 2 ? 0 : 1;
                                            int minZ2 = z8 == minZ8 && z4 == minZ4 && (chunkMinZ % 4) > 1 ? 1 : 0;
                                            int maxZ2 = z8 == maxZ8 && z4 == maxZ4 && (chunkMaxZ % 4) < 2 ? 0 : 1;
                                            for (int y2 = minY2; y2 <= maxY2; y2++)
                                            {
                                                for (int z2 = minZ2; z2 <= maxZ2; z2++)
                                                {
                                                    for (int x2 = minX2; x2 <= maxX2; x2++)
                                                    {
                                                        //index of current 2x2 block relative to 4x4 block
                                                        int index2 = (y2 << 2 | z2 << 1 | x2);
                                                        if ((bitflags & (1 << index2)) == 0)
                                                        {
                                                            continue;
                                                        }
                                                        int minX1 = x8 == minX8 && x4 == minX4 && x2 == minX2 && (chunkMinX % 2) > 0 ? 1 : 0;
                                                        int maxX1 = x8 == maxX8 && x4 == maxX4 && x2 == maxX2 && (chunkMaxX % 2) < 1 ? 0 : 1;
                                                        int minY1 = y8 == minY8 && y4 == minY4 && y2 == minY2 && (cubeMinY % 2) > 0 ? 1 : 0;
                                                        int maxY1 = y8 == maxY8 && y4 == maxY4 && y2 == maxY2 && (cubeMaxY % 2) < 1 ? 0 : 1;
                                                        int minZ1 = z8 == minZ8 && z4 == minZ4 && z2 == minZ2 && (chunkMinZ % 2) > 0 ? 1 : 0;
                                                        int maxZ1 = z8 == maxZ8 && z4 == maxZ4 && z2 == maxZ2 && (chunkMaxZ % 2) < 1 ? 0 : 1;
                                                        for (int y = minY1; y <= maxY1; y++)
                                                        {
                                                            for (int z = minZ1; z <= maxZ1; z++)
                                                            {
                                                                for (int x = minX1; x <= maxX1; x++)
                                                                {
                                                                    BlockPos pos = new BlockPos((chunkX << 4) + (x | (x2 << 1) | (x4 << 2) | (x8 << 3)),
                                                                            (y16 << 4) + (y | (y2 << 1) | (y4 << 2) | (y8 << 3)),
                                                                            (chunkZ << 4) + (z | (z2 << 1) | (z4 << 2) | (z8 << 3)));
                                                                    if (!world.getBlockState(pos).getBlock().isPassable(world, pos))
                                                                    {
                                                                        collidingBlocks.add(pos);
                                                                        if (returnFirst)
                                                                        {
                                                                            return collidingBlocks;
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return collidingBlocks;
    }

    private static CollisionChunk getChunk(World world, ChunkPos pos)
    {
        if (!chunks.containsKey(world.provider.getDimension()))
        {
            chunks.put(world.provider.getDimension(), new HashMap<ChunkPos, CollisionChunk>());
        }
        Chunk chunk = world.getChunkFromChunkCoords(pos.chunkXPos, pos.chunkZPos);
        if (!chunk.isLoaded())
        {
            return null;
        }
        CollisionChunk collisionChunk = chunks.get(world.provider.getDimension()).get(pos);
        if (collisionChunk != null)
        {
            return collisionChunk;
        }

        return new CollisionChunk(chunk);
    }

    private static class CollisionChunk
    {
        byte[] cube8 = new byte[16];
        byte[] cube4 = new byte[128];
        byte[] cube2 = new byte[1024];

        CollisionChunk(Chunk chunk)
        {
            ExtendedBlockStorage[] storageArray = chunk.getBlockStorageArray();
            for (int y16 = 0; y16 < storageArray.length; y16++)
            {
                if (storageArray[y16] == Chunk.NULL_BLOCK_STORAGE)
                {
                    continue;
                }
                for (int y8 = 0; y8 <= 1; y8++)
                {
                    for (int z8 = 0; z8 <= 1; z8++)
                    {
                        for (int x8 = 0; x8 <= 1; x8++)
                        {
                            //index of current 8x8 block relative to 16x16 block
                            int index8 = (y8 << 2 | z8 << 1 | x8);
                            for (int y4 = 0; y4 <= 1; y4++)
                            {
                                for (int z4 = 0; z4 <= 1; z4++)
                                {
                                    for (int x4 = 0; x4 <= 1; x4++)
                                    {
                                        //index of current 4x4 block relative to 8x8 block
                                        int index4 = (y4 << 2 | z4 << 1 | x4);
                                        for (int y2 = 0; y2 <= 1; y2++)
                                        {
                                            for (int z2 = 0; z2 <= 1; z2++)
                                            {
                                                for (int x2 = 0; x2 <= 1; x2++)
                                                {
                                                    //index of current 2x2 block relative to 4x4 block
                                                    int index2 = (y2 << 2 | z2 << 1 | x2);
                                                    for (int y = 0; y <= 1; y++)
                                                    {
                                                        for (int z = 0; z <= 1; z++)
                                                        {
                                                            for (int x = 0; x <= 1; x++)
                                                            {
                                                                IBlockState blockState = storageArray[y16].get(
                                                                        x | x2 << 1 | x4 << 2 | x8 << 3,
                                                                        y | y2 << 1 | y4 << 2 | y8 << 3,
                                                                        z | z2 << 1 | z4 << 2 | z8 << 3
                                                                );
                                                                //This will ignore stuff like open doors, but something big enough to require this probably can't fit through a door anyways
                                                                if (blockState.getMaterial().blocksMovement())
                                                                {
                                                                    cube2[y16 << 6 | index8 << 3 | index4] |= 1 << index2;
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }

                                        if (cube2[y16 << 6 | index8 << 3 | index4] != 0)
                                        {
                                            cube4[y16 << 3 | index8] |= 1 << index4;
                                        }
                                    }
                                }
                            }

                            if (cube4[y16 << 3 | index8] != 0)
                            {
                                cube8[y16] |= 1 << index8;
                            }
                        }
                    }
                }
            }
        }
    }
}
