package com.mraof.minestuck.block;

<<<<<<< HEAD
import java.util.Random;

import javax.annotation.Nullable;

=======
>>>>>>> 1.10.2
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.init.Blocks;
<<<<<<< HEAD
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
=======
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;
>>>>>>> 1.10.2


public class BlockMobSpawner extends Block
{
    public BlockMobSpawner()
    {
<<<<<<< HEAD
        super(Material.AIR);
        this.setTickRandomly(true);
        
    }
   
    
=======
        super(Material.ROCK);
        this.setTickRandomly(true);
    }

    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.SOLID;
    }

    @Override
    public boolean isTranslucent(IBlockState state)
    {
        return true;
    }

>>>>>>> 1.10.2
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
<<<<<<< HEAD
            
        }
    }
    @Override
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.INVISIBLE;
    }
=======

        }
    }
>>>>>>> 1.10.2

    @Override
    public boolean getTickRandomly()
    {
        return true;
    }
<<<<<<< HEAD
    //make it so you can pass through it
    @Nullable
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos)
    {
        return NULL_AABB;
    }
=======
>>>>>>> 1.10.2

    @Override
    public boolean canCollideCheck(IBlockState state, boolean hitIfLiquid)
    {
        return false;
<<<<<<< HEAD
        
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
=======
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, World worldIn, BlockPos pos)
    {
        return null;
    }

    @Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

>>>>>>> 1.10.2

}
