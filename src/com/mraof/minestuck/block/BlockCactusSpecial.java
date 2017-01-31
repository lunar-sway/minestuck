package com.mraof.minestuck.block;

import java.util.Random;

import com.mraof.minestuck.Minestuck;

import net.minecraft.block.BlockCactus;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockCactusSpecial extends BlockCactus
{
	private String toolType;
	
	public BlockCactusSpecial(SoundType soundType, String effectiveTool)
	{
		super();
		this.setCreativeTab(Minestuck.tabMinestuck);
		setSoundType(soundType);
		this.toolType = effectiveTool;
	}
	
	@Override
	public boolean isToolEffective(String type, IBlockState state)
	{
		if(type.equals(toolType))
			return true;
		else
			return super.isToolEffective(type, state);
	}
	
	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
	{
		super.updateTick(worldIn, pos, state, rand);
	}
	
	@Override
	public boolean canBlockStay(World worldIn, BlockPos pos)
	{
		for(EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL)
		{
			Material material = worldIn.getBlockState(pos.offset(enumfacing)).getMaterial();
			
			if(material.isSolid() || material == Material.LAVA)
			{
				return false;
			}
		}
		
		IBlockState state = worldIn.getBlockState(pos.down());
		return state.getBlock() == this || state.getBlock().canSustainPlant(state, worldIn, pos.down(), EnumFacing.UP, this)
				&& !worldIn.getBlockState(pos.up()).getMaterial().isLiquid();
	}
}