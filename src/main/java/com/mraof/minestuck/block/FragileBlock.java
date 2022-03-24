package com.mraof.minestuck.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tags.BlockTags;
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
	//collision shape is not a full block in order for the entityInside function to work. entityInside is used instead of stepOn as stepOn can be bypassed via sneaking
	private static final VoxelShape COLLISION_SHAPE = Block.box(0, 0, 0, 16, 15, 16);
	
	public FragileBlock(Properties properties)
	{
		super(properties);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public VoxelShape getCollisionShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context)
	{
		return COLLISION_SHAPE;
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
				
				if(steppingPlayer.getBoundingBox().intersects(obfuscateBB) && !isSecure(worldIn.getBlockState(pos.below())))
					worldIn.destroyBlock(pos, false);
			}
		}
	}
	
	/**
	 * will pretend to be solid if the block in this direction can support a players weight by itself
	 */
	public boolean directionNeedsObfuscation(World worldIn, BlockPos pos)
	{
		BlockState state = worldIn.getBlockState(pos);
		//returns true if the block in question is also a fragile block and the block below it cannot be replaced or if the block itself cannot be replaced
		return state.getBlock() == this ? isSecure(worldIn.getBlockState(pos.below())) : isSecure(state);
	}
	
	public static boolean isSecure(BlockState state) {
		Material material = state.getMaterial();
		return !state.isAir() && !state.is(BlockTags.FIRE) && !material.isLiquid() && !material.isReplaceable();
	}
}