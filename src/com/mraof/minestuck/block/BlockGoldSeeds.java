package com.mraof.minestuck.block;

import java.util.Random;

import com.mraof.minestuck.Minestuck;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockGoldSeeds extends Block
{
	
	public BlockGoldSeeds()
	{
		super(Material.plants);
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.25F, 1F);
		setStepSound(soundTypeMetal);
		setHardness(0.1F);
		setUnlocalizedName("goldSeeds");
	}
	
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		return Minestuck.goldSeeds;
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state)
	{
		return null;
	}
	
	public boolean isOpaqueCube()
	{
		return false;
	}
	
	public boolean isFullCube()
	{
		return false;
	}
	
	@SideOnly(Side.CLIENT)
	public EnumWorldBlockLayer getBlockLayer()
	{
		return EnumWorldBlockLayer.CUTOUT;
	}
	
	@Override
	public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
	{
		if(worldIn.getBlockState(pos.down()).getBlock() != Blocks.farmland)
		{
			this.dropBlockAsItem(worldIn, pos, state, 0);
			worldIn.setBlockState(pos, Blocks.air.getDefaultState(), 3);
		}
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public Item getItem(World worldIn, BlockPos pos)
	{
		return Minestuck.goldSeeds;
	}
	
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
	{
		// TODO Auto-generated method stub
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
	}
	
}
