package com.mraof.minestuck.block;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * If the block is stepped on by a player and there is nothing directly underneath it to support it, it crumbles
 */
public class FragileBlock extends Block
{
	//collision shape is not a full block in order for the entityInside function to work. entityInside is used instead of stepOn as stepOn can be bypassed via sneaking. fallOn is used as well for additional checking opportunities
	private static final VoxelShape COLLISION_SHAPE = Block.box(0, 0, 0, 16, 15, 16);
	
	public FragileBlock(Properties properties)
	{
		super(properties);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context)
	{
		return COLLISION_SHAPE;
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public void entityInside(BlockState stateIn, Level level, BlockPos pos, Entity entityIn)
	{
		attemptBreak(level, entityIn, pos);
	}
	
	@Override
	public void fallOn(Level level, BlockState state, BlockPos pos, Entity entityIn, float fallDistance)
	{
		super.fallOn(level, state, pos, entityIn, fallDistance);
		//TODO entityInside function covers most conditions but if a player sprint jumps there is a ~50% chance they will skip the check
		attemptBreak(level, entityIn, pos);
	}
	
	public void attemptBreak(Level level, Entity entityIn, BlockPos pos)
	{
		if(entityIn instanceof Player steppingPlayer)
		{
			
			if(!level.isClientSide)
			{
				AABB obfuscateBB = new AABB(pos);
				
				if(directionNeedsObfuscation(level, pos.east()))
					obfuscateBB = obfuscateBB.contract(0.6, 0, 0); //reducing the size by .6 is necessary so that it breaks only once it has begun supporting the player
				if(directionNeedsObfuscation(level, pos.west()))
					obfuscateBB = obfuscateBB.contract(-0.6, 0, 0);
				if(directionNeedsObfuscation(level, pos.south()))
					obfuscateBB = obfuscateBB.contract(0, 0, 0.6);
				if(directionNeedsObfuscation(level, pos.north()))
					obfuscateBB = obfuscateBB.contract(0, 0, -0.6);
				
				if(steppingPlayer.getBoundingBox().intersects(obfuscateBB) && !isSecure(level.getBlockState(pos.below())))
				{
					entityIn.makeStuckInBlock(level.getBlockState(pos), new Vec3(0.9F, 0.2, 0.9F));
					level.destroyBlock(pos, false);
				}
			}
		}
	}
	
	/**
	 * will pretend to be solid if the block in this direction can support a players weight by itself
	 */
	public boolean directionNeedsObfuscation(Level level, BlockPos pos)
	{
		BlockState state = level.getBlockState(pos);
		//returns true if the block in question is also a fragile block and the block below it cannot be replaced or if the block itself cannot be replaced
		return state.is(this) ? isSecure(level.getBlockState(pos.below())) : isSecure(state);
	}
	
	public static boolean isSecure(BlockState state) {
		return !state.isAir() && !state.is(BlockTags.FIRE) && !state.liquid() && !state.canBeReplaced();
	}
}