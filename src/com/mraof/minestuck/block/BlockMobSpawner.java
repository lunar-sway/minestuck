package com.mraof.minestuck.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


public class BlockMobSpawner extends Block {
	public BlockMobSpawner() {
		super(Material.ROCK);
		this.setUnlocalizedName("mobSpawner");
		this.setTickRandomly(true);
		
		
	}

	@Override
	public boolean isOpaqueCube(IBlockState state){
		return false;
	}
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
    	return null;
    }
	@Override
	public boolean isTranslucent(IBlockState state){
		return true;
	}

	@Override
	public void randomTick(World worldIn,BlockPos pos,IBlockState state,Random random){
		if(worldIn.getBlockState(pos.down())==Blocks.AIR.getDefaultState()){
			worldIn.setBlockState(pos, Blocks.AIR.getDefaultState());	
		}else{
		EntityRabbit entity = new EntityRabbit(worldIn);
		entity.setPosition(pos.getX()+.5, pos.getY(), pos.getZ()+.5);
		entity.onInitialSpawn(null, null);
		worldIn.spawnEntity(entity);

		}
	}
	@Override
	public boolean getTickRandomly(){
		return true;
	}
	@Override
	public boolean canCollideCheck(IBlockState state,boolean hitIfLiquid){
		return false;
	}
	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState,World worldIn,BlockPos pos){
		return null;
	}
	@Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }
	


	

}
