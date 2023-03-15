package com.mraof.minestuck.block.machine;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.Optional;

import static com.mraof.minestuck.block.MSBlockShapes.*;

public class PunchDesignixMultiblock extends MachineMultiblock
{
	public final RegistryObject<Block> LEFT_LEG = register("punch_designix_left_leg", () -> new PunchDesignixBlock(this, PUNCH_DESIGNIX_BOTTOM_LEFT, new BlockPos(0, 1, 0), Block.Properties.of(Material.METAL).strength(3.0F, 4.5F).noLootTable()));
	public final RegistryObject<Block> RIGHT_LEG = register("punch_designix_right_leg", () -> new PunchDesignixBlock(this, PUNCH_DESIGNIX_BOTTOM_RIGHT, new BlockPos(1, 1, 0), Block.Properties.of(Material.METAL).strength(3.0F, 4.5F).noLootTable()));
	public final RegistryObject<Block> SLOT = register("punch_designix_slot", () -> new PunchDesignixBlock.Slot(this, PUNCH_DESIGNIX_TOP_LEFT, Block.Properties.of(Material.METAL).strength(3.0F, 4.5F).noLootTable()));
	public final RegistryObject<Block> KEYBOARD = register("punch_designix_keyboard", () -> new PunchDesignixBlock(this, PUNCH_DESIGNIX_TOP_RIGHT, new BlockPos(1, 0, 0), Block.Properties.of(Material.METAL).strength(3.0F, 4.5F).noLootTable()));
	
	private final PlacementEntry slotPlacement;
	
	public PunchDesignixMultiblock(DeferredRegister<Block> register)
	{
		super(register);
		addDirectionPlacement(new BlockPos(0, 0, 0), RIGHT_LEG, Direction.NORTH);
		addDirectionPlacement(new BlockPos(1, 0, 0), LEFT_LEG, Direction.NORTH);
		addDirectionPlacement(new BlockPos(0, 1, 0), KEYBOARD, Direction.NORTH);
		slotPlacement = addDirectionPlacement(new BlockPos(1, 1, 0), SLOT, Direction.NORTH);
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