package com.mraof.minestuck.block;

import com.mraof.minestuck.command.DebugLandsCommand;
import com.mraof.minestuck.util.Debug;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import java.util.Random;
import java.util.function.Predicate;

/**
 * If the block is stepped on by a player and there is nothing directly underneath it to support it, it crumbles
 */
public class FragileBlock extends Block
{
	protected FragileBlock(Properties properties)
	{
		super(properties);
	}
	
	/*@Override
	@SuppressWarnings("deprecation")
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
	{
		return Block.box(0, 0, 0, 16, 15, 16);
	}*/
	
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
				boolean needToObfuscate = false;
				//BlockState blockStateBelow = worldIn.getBlockState(pos.below());
				AxisAlignedBB obfuscateBB = new AxisAlignedBB(pos)/*.deflate(0.5).move(0, 0.5, 0)*/;
				
				if(!directionNeedsObfuscation(worldIn, pos.east()))
					obfuscateBB.contract(-0.5, 0, 0);
				if(!directionNeedsObfuscation(worldIn, pos.west()))
					obfuscateBB.contract(0.5, 0, 0);
				if(!directionNeedsObfuscation(worldIn, pos.south()))
					obfuscateBB.contract(0, 0, -0.5);
				if(!directionNeedsObfuscation(worldIn, pos.north()))
					obfuscateBB.contract(0, 0, 0.5);
				
				/*
				if(!directionNeedsObfuscation(worldIn, pos.east()))
					obfuscateBB.contract(0.5, 0, 0);
				if(!directionNeedsObfuscation(worldIn, pos.west()))
					obfuscateBB.contract(-0.5, 0, 0);
				if(!directionNeedsObfuscation(worldIn, pos.south()))
					obfuscateBB.contract(0, 0, 0.5);
				if(!directionNeedsObfuscation(worldIn, pos.north()))
					obfuscateBB.contract(0, 0, -0.5);
				 */
				//obfuscateBB.expandTowards(0.5, 0, 0);
				
				if(steppingPlayer.getBoundingBox().intersects(obfuscateBB) && worldIn.getBlockState(pos.below()).isAir())
					worldIn.destroyBlock(pos, false);
				/*for(BlockPos iteratePos : BlockPos.betweenClosed(pos.offset(-1, 0, -1), pos.offset(1, 0, 1)))
				{
					boolean currentDirectionObfuscate = false;
					BlockState stateAtIteratePos = worldIn.getBlockState(iteratePos);
					if(iteratePos != pos &&
							((!(stateAtIteratePos.getBlock() instanceof FragileBlock) && (stateAtIteratePos.isPathfindable(worldIn, iteratePos, PathType.LAND))) ||
							(stateAtIteratePos.getBlock() instanceof FragileBlock && !blockStateBelow.isAir()))) //if there is a solid block in an adjacent position, the block should be prevented from breaking while the player is still supported by another block
						currentDirectionObfuscate = true;
					
					if(currentDirectionObfuscate)
					{
						obfuscateBB.expandTowards(); //TODO for each direction that has a solid block, expand the BB towards it
						needToObfuscate = true;
					}
				}
				
				Debug.debugf("%s", needToObfuscate);
				
				Vector3d vector3d = new Vector3d(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
				if(needToObfuscate && blockStateBelow.isAir() && steppingPlayer.position().distanceTo(vector3d) < 0.5) //TODO this works when travelling from a stable block to a fragile block but allows players to hang off the edge next to air
					worldIn.destroyBlock(pos, false);
				else if(!needToObfuscate && blockStateBelow.isAir())
					worldIn.destroyBlock(pos, false);*/
			}
		}
	}
	
	public boolean directionNeedsObfuscation(World worldIn, BlockPos pos)
	{
		return (!(worldIn.getBlockState(pos).getBlock() instanceof FragileBlock) && (worldIn.getBlockState(pos).isPathfindable(worldIn, pos, PathType.LAND))) || (worldIn.getBlockState(pos).getBlock() instanceof FragileBlock && !worldIn.getBlockState(pos.below()).isAir());
	}
}