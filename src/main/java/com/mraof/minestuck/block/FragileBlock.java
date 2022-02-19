package com.mraof.minestuck.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

/**
 * If the block is stepped on by a player and there is nothing directly underneath it to support it, it crumbles
 */
public class FragileBlock extends Block
{
	protected FragileBlock(Properties properties)
	{
		super(properties);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public VoxelShape getCollisionShape(BlockState p_220071_1_, IBlockReader p_220071_2_, BlockPos p_220071_3_, ISelectionContext p_220071_4_)
	{
		return Block.box(0, 0, 0, 16, 15, 16);
	}
	
	@Override
	public void entityInside(BlockState stateIn, World worldIn, BlockPos pos, Entity entityIn)
	{
		if(entityIn instanceof PlayerEntity)
		{
			PlayerEntity steppingPlayer = (PlayerEntity) entityIn;
			
			if(!worldIn.isClientSide)
			{
				AxisAlignedBB obfuscateBB = new AxisAlignedBB(pos);
				
				if(directionNeedsObfuscation(worldIn, pos.east()))
					obfuscateBB = obfuscateBB.contract(0.6, 0, 0); //reducing the size by .6 is necessary so that it breaks only once it has begun supporting the player
				if(directionNeedsObfuscation(worldIn, pos.west()))
					obfuscateBB = obfuscateBB.contract(-0.6, 0, 0);
				if(directionNeedsObfuscation(worldIn, pos.south()))
					obfuscateBB = obfuscateBB.contract(0, 0, 0.6);
				if(directionNeedsObfuscation(worldIn, pos.north()))
					obfuscateBB = obfuscateBB.contract(0, 0, -0.6);
				
				if(steppingPlayer.getBoundingBox().intersects(obfuscateBB) && worldIn.getBlockState(pos.below()).isAir())
					worldIn.destroyBlock(pos, false);
			}
		}
	}
	
	/**
	 * will pretend to be solid if the block in this direction can support a players weight by itself
	 */
	public boolean directionNeedsObfuscation(World worldIn, BlockPos pos)
	{
		return (!(worldIn.getBlockState(pos).getBlock() instanceof FragileBlock) && !worldIn.getBlockState(pos).isAir() && !(worldIn.getBlockState(pos).getBlock() instanceof FlowingFluidBlock)) || (worldIn.getBlockState(pos).getBlock() instanceof FragileBlock && !worldIn.getBlockState(pos.below()).isAir());
	}
}