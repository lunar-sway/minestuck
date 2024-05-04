package com.mraof.minestuck.block.machine;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Optional;

import static com.mraof.minestuck.block.MSBlockShapes.*;

public class PunchDesignixMultiblock extends MachineMultiblock
{
	public final DeferredBlock<Block> LEFT_LEG = register("punch_designix_left_leg", () -> new PunchDesignixBlock(this, PUNCH_DESIGNIX_BOTTOM_LEFT, new BlockPos(0, 1, 0), Block.Properties.of().mapColor(MapColor.METAL).strength(3.0F, 4.5F).noLootTable()));
	public final DeferredBlock<Block> RIGHT_LEG = register("punch_designix_right_leg", () -> new PunchDesignixBlock(this, PUNCH_DESIGNIX_BOTTOM_RIGHT, new BlockPos(1, 1, 0), Block.Properties.of().mapColor(MapColor.METAL).strength(3.0F, 4.5F).noLootTable()));
	public final DeferredBlock<Block> SLOT = register("punch_designix_slot", () -> new PunchDesignixBlock.Slot(this, PUNCH_DESIGNIX_TOP_LEFT, Block.Properties.of().mapColor(MapColor.METAL).strength(3.0F, 4.5F).noLootTable()));
	public final DeferredBlock<Block> KEYBOARD = register("punch_designix_keyboard", () -> new PunchDesignixBlock(this, PUNCH_DESIGNIX_TOP_RIGHT, new BlockPos(1, 0, 0), Block.Properties.of().mapColor(MapColor.METAL).strength(3.0F, 4.5F).noLootTable()));
	
	private final PlacementEntry slotPlacement;
	
	public PunchDesignixMultiblock(DeferredRegister.Blocks register)
	{
		super(register);
		addDirectionPlacement(0, 0, 0, RIGHT_LEG, Direction.NORTH);
		addDirectionPlacement(1, 0, 0, LEFT_LEG, Direction.NORTH);
		addDirectionPlacement(0, 1, 0, KEYBOARD, Direction.NORTH);
		slotPlacement = addDirectionPlacement(1, 1, 0, SLOT, Direction.NORTH);
	}
	
	public boolean isInvalidFromSlot(LevelAccessor level, BlockPos pos)
	{
		return isInvalidFromPlacement(level, pos, slotPlacement);
	}
	
	public Optional<Placement> findPlacementFromSlot(LevelAccessor level, BlockPos pos)
	{
		return slotPlacement.findPlacement(pos, level.getBlockState(pos));
	}
}