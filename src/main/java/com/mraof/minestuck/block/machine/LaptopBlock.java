package com.mraof.minestuck.block.machine;

import com.mraof.minestuck.blockentity.ComputerBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Map;

public class LaptopBlock extends ComputerBlock
{
	public LaptopBlock(Map<Direction, VoxelShape> shapeOn, Map<Direction, VoxelShape> shapeOff, Properties properties)
	{
		super(shapeOn, shapeOff, properties);
	}
	
	public LaptopBlock(Map<Direction, VoxelShape> shapeOn, Map<Direction, VoxelShape> shapeOff, ResourceLocation defaultTheme, Properties properties)
	{
		super(shapeOn, shapeOff, defaultTheme, properties);
	}
	
	@Override
	protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult)
	{
		if(hand == InteractionHand.OFF_HAND)
			return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
		
		if(player.isShiftKeyDown())
		{
			if(state.getValue(STATE) == State.OFF)
			{
				if(!level.isClientSide)
					pickUpLaptop(state, level, pos, player);
				return ItemInteractionResult.SUCCESS;
			}
			else
			{
				level.setBlock(pos, state.setValue(STATE, State.OFF), Block.UPDATE_CLIENTS);
				return ItemInteractionResult.SUCCESS;
			}
		}
		
		return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
	}
	
	private void pickUpLaptop(BlockState state, Level level, BlockPos pos, Player player)
	{
		if(!(level.getBlockEntity(pos) instanceof ComputerBlockEntity computer))
			return;
		
		computer.closeAll();
		
		CompoundTag beTag = computer.saveWithoutMetadata(level.registryAccess());
		ItemStack pickupStack = new ItemStack(state.getBlock().asItem());
		BlockItem.setBlockEntityData(pickupStack, computer.getType(), beTag);
		
		computer.setPickedUp(true);
		level.removeBlock(pos, false);
		
		if(!player.addItem(pickupStack))
			player.drop(pickupStack, false);
	}
}
