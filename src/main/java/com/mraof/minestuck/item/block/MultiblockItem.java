package com.mraof.minestuck.item.block;

import com.mraof.minestuck.block.machine.AlchemiterBlock;
import com.mraof.minestuck.block.machine.MachineMultiblock;
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
	public void registerBlocks(Map<Block, Item> blockToItemMap, Item itemIn)
	{
		multiblock.forEachBlock(block -> blockToItemMap.put(block, itemIn));
	}
	
	@Override
	public void removeFromBlockToItemMap(Map<Block, Item> blockToItemMap, Item itemIn)
	{
		multiblock.forEachBlock(block -> blockToItemMap.remove(block));
	}
	
	@Override
	public ActionResultType place(BlockItemUseContext context)
	{
		World world = context.getLevel();
		Direction sideFace = context.getClickedFace();
		if (world.isClientSide)
		{
			return ActionResultType.SUCCESS;
		} else if (sideFace != Direction.UP)
		{
			return ActionResultType.FAIL;
		} else
		{
			Direction facing = context.getHorizontalDirection().getOpposite();
			BlockPos pos = getPlacementPos(context);
			
			if(!canPlaceAt(context, pos, facing))
				return ActionResultType.FAIL;
			
			BlockState state = getBlock().defaultBlockState().setValue(AlchemiterBlock.FACING, facing);
			this.placeBlock(context, state);
			updateCustomBlockEntityTag(pos, world, context.getPlayer(), context.getItemInHand(), state);
			return ActionResultType.SUCCESS;
		}
	}
	
	public boolean canPlaceAt(BlockItemUseContext context, BlockPos pos, Direction facing)
	{
		PlayerEntity player = context.getPlayer();
		if(player != null && !player.mayUseItemAt(pos, Direction.UP, context.getItemInHand()))
			return false;
		MutableBoundingBox boundingBox = multiblock.getBoundingBox(MSRotationUtil.fromDirection(facing));
		for(int x = boundingBox.x0; x <= boundingBox.x1; x++)
		{
			for(int z = boundingBox.z0; z <= boundingBox.z1; z++)
			{
				for(int y = boundingBox.y0; y <= boundingBox.y1; y++)
				{
					if(World.isOutsideBuildHeight(pos.offset(x, y, z)) || player != null && !context.getLevel().mayInteract(player, pos)
							|| !context.getLevel().getBlockState(pos.offset(x, y, z)).canBeReplaced(context))
						return false;
				}
			}
		}
		return true;
	}
	
	@Override
	protected boolean placeBlock(BlockItemUseContext context, BlockState newState)
	{
		World world = context.getLevel();
		if(!world.isClientSide)
		{
			BlockPos pos = getPlacementPos(context);
			
			multiblock.placeWithRotation(world, pos, MSRotationUtil.fromDirection(context.getHorizontalDirection().getOpposite()));
			
			PlayerEntity player = context.getPlayer();
			if(player instanceof ServerPlayerEntity)
				CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayerEntity) player, pos, context.getItemInHand());
		}
		return true;
	}
	
	private BlockPos getPlacementPos(BlockItemUseContext context)
	{
		BlockPos pos = context.getClickedPos();
		if(!context.getLevel().getBlockState(pos).canBeReplaced(context))
		{
			pos = pos.above();
		}
		Direction facing = context.getHorizontalDirection().getOpposite();
		
		return getPlacementPos(pos, facing, context.getClickLocation().x - pos.getX(), context.getClickLocation().z - pos.getZ());
	}
	
	public BlockPos getPlacementPos(BlockPos pos, Direction direction, double hitX, double hitZ)
	{
		MutableBoundingBox bb = multiblock.getBoundingBox(MSRotationUtil.fromDirection(direction));
		
		if(direction.getAxis() == Direction.Axis.X)
			return pos.south((int) Math.floor(hitZ - (bb.z1 - bb.z0)*direction.getClockWise().getStepZ()/2D));
		else if(direction.getAxis() == Direction.Axis.Z)
			return pos.east((int) Math.floor(hitX - (bb.x1 - bb.x0)*direction.getClockWise().getStepX()/2D));
		else throw new IllegalArgumentException("Direction should be horizontal");
	}
}