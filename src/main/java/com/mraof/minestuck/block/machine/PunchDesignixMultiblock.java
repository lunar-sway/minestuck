package com.mraof.minestuck.block.machine;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraftforge.fml.RegistryObject;

import static com.mraof.minestuck.block.MSBlockShapes.*;

public class PunchDesignixMultiblock extends MachineMultiblock
{
	public final RegistryObject<Block> LEFT_LEG = register("punch_designix_left_leg", () -> new PunchDesignixBlock(this, PUNCH_DESIGNIX_BOTTOM_LEFT, new BlockPos(0, 1, 0), Block.Properties.create(Material.IRON).hardnessAndResistance(3.0F).noDrops()));
	public final RegistryObject<Block> RIGHT_LEG = register("punch_designix_right_leg", () -> new PunchDesignixBlock(this, PUNCH_DESIGNIX_BOTTOM_RIGHT, new BlockPos(1, 1, 0), Block.Properties.create(Material.IRON).hardnessAndResistance(3.0F).noDrops()));
	public final RegistryObject<Block> SLOT = register("punch_designix_slot", () -> new PunchDesignixBlock.Slot(this, PUNCH_DESIGNIX_TOP_LEFT, Block.Properties.create(Material.IRON).hardnessAndResistance(3.0F).noDrops()));
	public final RegistryObject<Block> KEYBOARD = register("punch_designix_keyboard", () -> new PunchDesignixBlock(this, PUNCH_DESIGNIX_TOP_RIGHT, new BlockPos(1, 0, 0), Block.Properties.create(Material.IRON).hardnessAndResistance(3.0F).noDrops()));
	
	private final PlacementEntry slotPlacement;
	
	public PunchDesignixMultiblock(String modId)
	{
		super(modId);
		registerPlacement(new BlockPos(0, 0, 0), applyDirection(RIGHT_LEG, Direction.NORTH));
		registerPlacement(new BlockPos(1, 0, 0), applyDirection(LEFT_LEG, Direction.NORTH));
		registerPlacement(new BlockPos(0, 1, 0), applyDirection(KEYBOARD, Direction.NORTH));
		slotPlacement = registerPlacement(new BlockPos(1, 1, 0), applyDirection(SLOT, Direction.NORTH));
	}
	
	public boolean isInvalidFromSlot(IWorld world, BlockPos pos)
	{
		return isInvalidFromPlacement(world, pos, slotPlacement);
	}
}