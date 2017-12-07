package com.mraof.minestuck.block;

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

import javax.annotation.Nullable;
import java.util.Random;


public class BlockMobSpawner extends Block
{
    public BlockMobSpawner()
    {
        super(Material.AIR);
        this.setTickRandomly(true);
        
    }
   //*
   //*should just delete it's self for now
   //*    
    @Override
    public void randomTick(World worldIn, BlockPos pos, IBlockState state, Random random)
    {
      //  if (worldIn.isAirBlock(pos.down()))
      //  {
            worldIn.setBlockState(pos, Blocks.AIR.getDefaultState());
     //   }
     //   else
      //  {
      //      EntityRabbit entity = new EntityRabbit(worldIn);
      //      entity.setPosition(pos.getX() + .5, pos.getY(), pos.getZ() + .5);
      //      entity.onInitialSpawn(null, null);
      //      worldIn.spawnEntity(entity);
            
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
    //make it so you can pass through it
    @Nullable
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos)
    {
        return NULL_AABB;
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
