package com.mraof.minestuck.item.block;

import com.mraof.minestuck.block.AlchemiterBlock;
import com.mraof.minestuck.block.multiblock.MachineMultiblock;
import com.mraof.minestuck.util.MSRotationUtil;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.World;

import java.util.Map;

public class MultiblockItem extends BlockItem
{
	private final MachineMultiblock multiblock;
	
	public MultiblockItem(MachineMultiblock multiblock, Properties properties)
	{
		super(multiblock.getMainBlock(), properties);
		this.multiblock = multiblock;
	}
	
	public MachineMultiblock getMultiblock()
	{
		return multiblock;
	}
	
	@Override
	public void addToBlockToItemMap(Map<Block, Item> blockToItemMap, Item itemIn)
	{
		multiblock.forEachBlock(block -> blockToItemMap.put(block, itemIn));
	}
	
	@Override
	public void removeFromBlockToItemMap(Map<Block, Item> blockToItemMap, Item itemIn)
	{
		multiblock.forEachBlock(block -> blockToItemMap.remove(block));
	}
	
	@Override
	public ActionResultType tryPlace(BlockItemUseContext context)
	{
		World world = context.getWorld();
		Direction sideFace = context.getFace();
		if (world.isRemote)
		{
			return ActionResultType.SUCCESS;
		} else if (sideFace != Direction.UP)
		{
			return ActionResultType.FAIL;
		} else
		{
			Direction facing = context.getPlacementHorizontalFacing().getOpposite();
			BlockPos pos = getPlacementPos(context);
			
			if(!canPlaceAt(context, pos, facing))
				return ActionResultType.FAIL;
			
			BlockState state = getBlock().getDefaultState().with(AlchemiterBlock.FACING, facing);
			this.placeBlock(context, state);
			onBlockPlaced(pos, world, context.getPlayer(), context.getItem(), state);
			return ActionResultType.SUCCESS;
		}
	}
	
	public boolean canPlaceAt(BlockItemUseContext context, BlockPos pos, Direction facing)
	{
		MutableBoundingBox boundingBox = multiblock.getBoundingBox(MSRotationUtil.fromDirection(facing));
		for(int x = boundingBox.minX; x <= boundingBox.maxX; x++)
		{
			for(int z = boundingBox.minZ; z <= boundingBox.maxZ; z++)
			{
				if(!context.getPlayer().canPlayerEdit(pos.add(x, 0, z), Direction.UP, context.getItem()))
					return false;
				for(int y = boundingBox.minY; y <= boundingBox.maxY; y++)
				{
					if(!context.getWorld().getBlockState(pos.add(x, y, z)).isReplaceable(context))
						return false;
				}
			}
		}
		return true;
	}
	
	@Override
	protected boolean placeBlock(BlockItemUseContext context, BlockState newState)
	{
		World world = context.getWorld();
		if(!world.isRemote)
		{
			BlockPos pos = getPlacementPos(context);
			
			multiblock.placeWithRotation(world, pos, MSRotationUtil.fromDirection(context.getPlacementHorizontalFacing().getOpposite()));
			
			PlayerEntity player = context.getPlayer();
			if(player instanceof ServerPlayerEntity)
				CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayerEntity) player, pos, context.getItem());
		}
		return true;
	}
	
	private BlockPos getPlacementPos(BlockItemUseContext context)
	{
		BlockPos pos = context.getPos();
		if(!context.getWorld().getBlockState(pos).isReplaceable(context))
		{
			pos = pos.up();
		}
		Direction facing = context.getPlacementHorizontalFacing().getOpposite();
		
		return getPlacementPos(pos, facing, context.getHitVec().x - pos.getX(), context.getHitVec().z - pos.getZ());
	}
	
	public BlockPos getPlacementPos(BlockPos pos, Direction direction, double hitX, double hitZ)
	{
		MutableBoundingBox bb = multiblock.getBoundingBox(MSRotationUtil.fromDirection(direction));
		
		if(direction.getAxis() == Direction.Axis.X)
			return pos.south((int) Math.floor(hitZ - (bb.maxZ - bb.minZ)*direction.rotateY().getZOffset()/2D));
		else if(direction.getAxis() == Direction.Axis.Z)
			return pos.east((int) Math.floor(hitX - (bb.maxX - bb.minX)*direction.rotateY().getXOffset()/2D));
		else throw new IllegalArgumentException("Direction should be horizontal");
	}
}