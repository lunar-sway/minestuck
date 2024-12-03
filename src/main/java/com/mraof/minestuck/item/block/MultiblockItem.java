package com.mraof.minestuck.item.block;

import com.mraof.minestuck.block.machine.MachineMultiblock;
import com.mraof.minestuck.util.MSRotationUtil;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.levelgen.structure.BoundingBox;

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
	public InteractionResult place(BlockPlaceContext context)
	{
		Level level = context.getLevel();
		Direction sideFace = context.getClickedFace();
		if (sideFace != Direction.UP)
		{
			return InteractionResult.FAIL;
		} else
		{
			Direction facing = context.getHorizontalDirection().getOpposite();
			BlockPos pos = getPlacementPos(context);
			BlockPos clickedPos = context.getClickedPos();
			Player player = context.getPlayer();
			ItemStack stack = context.getItemInHand();
			
			if(!canPlaceAt(context, pos, facing))
				return InteractionResult.FAIL;
			
			BlockState state = getBlock().defaultBlockState();
			this.placeBlock(context, state);
			updateCustomBlockEntityTag(pos, level, player, stack, state);
			
			if (player instanceof ServerPlayer serverPlayer) {
				CriteriaTriggers.PLACED_BLOCK.trigger(serverPlayer, clickedPos, stack);
			}
			
			level.gameEvent(player, GameEvent.BLOCK_PLACE, clickedPos);
			SoundType soundtype = state.getSoundType(level, clickedPos, context.getPlayer());
			level.playSound(player, clickedPos, this.getPlaceSound(state, level, clickedPos, player), SoundSource.BLOCKS, (soundtype.getVolume() + 1) / 2, soundtype.getPitch() * 0.8F);
			if (player == null || !player.getAbilities().instabuild) {
				stack.shrink(1);
			}
			
			return InteractionResult.sidedSuccess(level.isClientSide);
		}
	}
	
	public boolean canPlaceAt(BlockPlaceContext context, BlockPos pos, Direction facing)
	{
		Player player = context.getPlayer();
		if(player != null && !player.mayUseItemAt(pos, Direction.UP, context.getItemInHand()))
			return false;
		BoundingBox boundingBox = multiblock.getBoundingBox(MSRotationUtil.fromDirection(facing));
		for(int x = boundingBox.minX(); x <= boundingBox.maxX(); x++)
		{
			for(int z = boundingBox.minZ(); z <= boundingBox.maxZ(); z++)
			{
				for(int y = boundingBox.minY(); y <= boundingBox.maxY(); y++)
				{
					if(context.getLevel().isOutsideBuildHeight(pos.offset(x, y, z)) || player != null && !context.getLevel().mayInteract(player, pos)
							|| !context.getLevel().getBlockState(pos.offset(x, y, z)).canBeReplaced(context))
						return false;
				}
			}
		}
		return true;
	}
	
	@Override
	protected boolean placeBlock(BlockPlaceContext context, BlockState newState)
	{
		Level level = context.getLevel();
		if(!level.isClientSide)
		{
			BlockPos pos = getPlacementPos(context);
			
			MachineMultiblock.Placement placement = new MachineMultiblock.Placement(pos,
					MSRotationUtil.fromDirection(context.getHorizontalDirection().getOpposite()));
			multiblock.placeWithRotation(level, placement);
			multiblock.placeAdditional(level, placement);
			
			if(context.getPlayer() instanceof ServerPlayer player)
				CriteriaTriggers.PLACED_BLOCK.trigger(player, pos, context.getItemInHand());
		}
		return true;
	}
	
	private BlockPos getPlacementPos(BlockPlaceContext context)
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
		BoundingBox bb = multiblock.getBoundingBox(MSRotationUtil.fromDirection(direction));
		
		if(direction.getAxis() == Direction.Axis.X)
			return pos.south((int) Math.floor(hitZ - (bb.maxZ() - bb.minZ())*direction.getClockWise().getStepZ()/2D));
		else if(direction.getAxis() == Direction.Axis.Z)
			return pos.east((int) Math.floor(hitX - (bb.maxX() - bb.minX())*direction.getClockWise().getStepX()/2D));
		else throw new IllegalArgumentException("Direction should be horizontal");
	}
}