package com.mraof.minestuck.world.lands.decorator;

import java.util.Random;

import com.mraof.minestuck.block.BlockMinestuckLeaves1;
import com.mraof.minestuck.block.BlockMinestuckLog1;
import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.world.biome.BiomeMinestuck;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockLog;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenTaiga1;
import net.minecraft.world.gen.feature.WorldGenTaiga2;

public class SpruceTreeDecorator extends TreeDecoratorBase
{
   
    public IBlockState trunk;
    public IBlockState leaf;
    private Biome biome;
   
    public SpruceTreeDecorator(IBlockState trunk, IBlockState leaf, Biome biome) {
        this.biome = biome;
        this.trunk = trunk;
        this.leaf = leaf;
    }
   
   
    protected WorldGenTaiga1 tree1 = new WorldGenTaiga1()
    {
        @Override
        protected boolean canGrowInto(Block block) {return super.canGrowInto(block) || block == Blocks.SNOW_LAYER;}
       
        @Override
        public boolean isReplaceable(World world, BlockPos pos) {
           
            return super.isReplaceable(world, pos) || world.getBlockState(pos).getBlock() == Blocks.SNOW_LAYER;
        }
       
        @Override
        public boolean generate(World worldIn, Random rand, BlockPos position) {
             int i = rand.nextInt(5) + 7;
                int j = i - rand.nextInt(2) - 3;
                int k = i - j;
                int l = 1 + rand.nextInt(k + 1);
 
                if (position.getY() >= 1 && position.getY() + i + 1 <= 256)
                {
                    boolean flag = true;
 
                    for (int i1 = position.getY(); i1 <= position.getY() + 1 + i && flag; ++i1)
                    {
                        int j1 = 1;
 
                        if (i1 - position.getY() < j)
                        {
                            j1 = 0;
                        }
                        else
                        {
                            j1 = l;
                        }
 
                        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
 
                        for (int k1 = position.getX() - j1; k1 <= position.getX() + j1 && flag; ++k1)
                        {
                            for (int l1 = position.getZ() - j1; l1 <= position.getZ() + j1 && flag; ++l1)
                            {
                                if (i1 >= 0 && i1 < 256)
                                {
                                    if (!this.isReplaceable(worldIn,blockpos$mutableblockpos.setPos(k1, i1, l1)))
                                    {
                                        flag = false;
                                    }
                                }
                                else
                                {
                                    flag = false;
                                }
                            }
                        }
                    }
 
                    if (!flag)
                    {
                        return false;
                    }
                    else
                    {
                        BlockPos down = position.down();
                        IBlockState state = worldIn.getBlockState(down);
                        boolean isSoil = state.getBlock().canSustainPlant(state, worldIn, down, net.minecraft.util.EnumFacing.UP, (net.minecraft.block.BlockSapling)Blocks.SAPLING);
 
                        if (isSoil && position.getY() < 256 - i - 1)
                        {
                            state.getBlock().onPlantGrow(state, worldIn, down, position);
                            int k2 = 0;
 
                            for (int l2 = position.getY() + i; l2 >= position.getY() + j; --l2)
                            {
                                for (int j3 = position.getX() - k2; j3 <= position.getX() + k2; ++j3)
                                {
                                    int k3 = j3 - position.getX();
 
                                    for (int i2 = position.getZ() - k2; i2 <= position.getZ() + k2; ++i2)
                                    {
                                        int j2 = i2 - position.getZ();
 
                                        if (Math.abs(k3) != k2 || Math.abs(j2) != k2 || k2 <= 0)
                                        {
                                            BlockPos blockpos = new BlockPos(j3, l2, i2);
                                            state = worldIn.getBlockState(blockpos);
 
                                            if (state.getBlock().canBeReplacedByLeaves(state, worldIn, blockpos))
                                            {
                                                this.setBlockAndNotifyAdequately(worldIn, blockpos, leaf);
                                            }
                                        }
                                    }
                                }
 
                                if (k2 >= 1 && l2 == position.getY() + j + 1)
                                {
                                    --k2;
                                }
                                else if (k2 < l)
                                {
                                    ++k2;
                                }
                            }
 
                            for (int i3 = 0; i3 < i - 1; ++i3)
                            {
                                BlockPos upN = position.up(i3);
                                state = worldIn.getBlockState(upN);
 
                                if (state.getBlock().isAir(state, worldIn, upN) || state.getBlock().isLeaves(state, worldIn, upN) || state.getBlock() == Blocks.SNOW_LAYER)
                                {
                                    this.setBlockAndNotifyAdequately(worldIn, position.up(i3), trunk);
                                }
                            }
 
                            return true;
                        }
                        else
                        {
                            return false;
                        }
                    }
                }
                else
                {
                    return false;
                }
        }
    };
   
    protected WorldGenTaiga2 tree2 = new WorldGenTaiga2(false)
    {
        @Override
        protected boolean canGrowInto(Block block) {return super.canGrowInto(block) || block == Blocks.SNOW_LAYER;}
   
        @Override
        public boolean isReplaceable(World world, BlockPos pos) {
           
            return super.isReplaceable(world, pos) || world.getBlockState(pos).getBlock() == Blocks.SNOW_LAYER;
        }
       
        @Override
        public boolean generate(World worldIn, Random rand, BlockPos position) {
             int i = rand.nextInt(5) + 7;
                int j = i - rand.nextInt(2) - 3;
                int k = i - j;
                int l = 1 + rand.nextInt(k + 1);
 
                if (position.getY() >= 1 && position.getY() + i + 1 <= 256)
                {
                    boolean flag = true;
 
                    for (int i1 = position.getY(); i1 <= position.getY() + 1 + i && flag; ++i1)
                    {
                        int j1 = 1;
 
                        if (i1 - position.getY() < j)
                        {
                            j1 = 0;
                        }
                        else
                        {
                            j1 = l;
                        }
 
                        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
 
                        for (int k1 = position.getX() - j1; k1 <= position.getX() + j1 && flag; ++k1)
                        {
                            for (int l1 = position.getZ() - j1; l1 <= position.getZ() + j1 && flag; ++l1)
                            {
                                if (i1 >= 0 && i1 < 256)
                                {
                                    if (!this.isReplaceable(worldIn,blockpos$mutableblockpos.setPos(k1, i1, l1)))
                                    {
                                        flag = false;
                                    }
                                }
                                else
                                {
                                    flag = false;
                                }
                            }
                        }
                    }
 
                    if (!flag)
                    {
                        return false;
                    }
                    else
                    {
                        BlockPos down = position.down();
                        IBlockState state = worldIn.getBlockState(down);
                        boolean isSoil = state.getBlock().canSustainPlant(state, worldIn, down, net.minecraft.util.EnumFacing.UP, (net.minecraft.block.BlockSapling)Blocks.SAPLING);
 
                        if (isSoil && position.getY() < 256 - i - 1)
                        {
                            state.getBlock().onPlantGrow(state, worldIn, down, position);
                            int k2 = 0;
 
                            for (int l2 = position.getY() + i; l2 >= position.getY() + j; --l2)
                            {
                                for (int j3 = position.getX() - k2; j3 <= position.getX() + k2; ++j3)
                                {
                                    int k3 = j3 - position.getX();
 
                                    for (int i2 = position.getZ() - k2; i2 <= position.getZ() + k2; ++i2)
                                    {
                                        int j2 = i2 - position.getZ();
 
                                        if (Math.abs(k3) != k2 || Math.abs(j2) != k2 || k2 <= 0)
                                        {
                                            BlockPos blockpos = new BlockPos(j3, l2, i2);
                                            state = worldIn.getBlockState(blockpos);
 
                                            if (state.getBlock().canBeReplacedByLeaves(state, worldIn, blockpos))
                                            {
                                                this.setBlockAndNotifyAdequately(worldIn, blockpos, leaf);
                                            }
                                        }
                                    }
                                }
 
                                if (k2 >= 1 && l2 == position.getY() + j + 1)
                                {
                                    --k2;
                                }
                                else if (k2 < l)
                                {
                                    ++k2;
                                }
                            }
 
                            for (int i3 = 0; i3 < i - 1; ++i3)
                            {
                                BlockPos upN = position.up(i3);
                                state = worldIn.getBlockState(upN);
 
                                if (state.getBlock().isAir(state, worldIn, upN) || state.getBlock().isLeaves(state, worldIn, upN) || state.getBlock() == Blocks.SNOW_LAYER)
                                {
                                    this.setBlockAndNotifyAdequately(worldIn, position.up(i3), trunk);
                                }
                            }
 
                            return true;
                        }
                        else
                        {
                            return false;
                        }
                    }
                }
                else
                {
                    return false;
                }
        }
       
    };
   
    @Override
    public int getCount(Random random)
    {
        return getCount(random, this.biome);
    }
   
    public int getCount(Random random, Biome biome) {
        if(biome == BiomeMinestuck.mediumRough) {
            return Math.max(0, random.nextInt(20) - 7);
        }
       
        else {
            return Math.max(0, random.nextInt(5) - 7);
        }
    }
 
    @Override
    protected WorldGenAbstractTree getTreeToGenerate(World world, BlockPos pos, Random rand)
    {
        return rand.nextBoolean() ? tree1 : tree2;
    }
 
}