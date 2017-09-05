package com.mraof.minestuck.block;

import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.init.Blocks;

import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

public class BlockMobSpawner extends Block
{
    public BlockMobSpawner()
    {
        super(Material.AIR);
        this.setTickRandomly(true);
    }

    @Override
    public void randomTick(World worldIn, BlockPos pos, IBlockState state, Random random)
    {
        if (worldIn.getBlockState(pos.down()) == Blocks.AIR.getDefaultState())
        {
            worldIn.setBlockState(pos, Blocks.AIR.getDefaultState());
        }
        else
        {
            EntityRabbit entity = new EntityRabbit(worldIn);
            entity.setPosition(pos.getX() + .5, pos.getY(), pos.getZ() + .5);
            entity.onInitialSpawn(null, null);
            worldIn.spawnEntity(entity);
            
        }
    }
    @Override
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.INVISIBLE;
    }


    @Override
    public boolean getTickRandomly()
    {
        return true;
    }

    @Override
    public boolean canCollideCheck(IBlockState state, boolean hitIfLiquid)
    {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }
    
    @Override
    public boolean isAir(IBlockState state,IBlockAccess world,BlockPos pos){
    	return true;
    }
    
    @Override
    public boolean isReplaceable(IBlockAccess worldIn,BlockPos pos){
    	return true;
    }
    //turn it invisable
    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }
    
    @Override
    public boolean isTranslucent(IBlockState state)
    {
        return true;
    }

}
