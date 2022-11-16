package com.mraof.minestuck.block.machine;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import static com.mraof.minestuck.block.MSBlockShapes.*;
import static com.mraof.minestuck.block.machine.MachineBlock.FACING;

public class TotemLatheMultiblock extends MachineMultiblock
{
	public final RegistryObject<Block> CARD_SLOT = register("totem_lathe_card_slot", () -> new TotemLatheBlock.Slot(this, TOTEM_LATHE_CARD_SLOT, Block.Properties.of(Material.METAL).strength(3.0F).noDrops()));
	public final RegistryObject<Block> BOTTOM_LEFT = register("totem_lathe_bottom_left", () -> new TotemLatheBlock(this, TOTEM_LATHE_BOTTOM_LEFT, new BlockPos(1, 0, 0), Block.Properties.of(Material.METAL).strength(3.0F, 4.5F).noDrops()));
	public final RegistryObject<Block> BOTTOM_RIGHT = register("totem_lathe_bottom_right", () -> new TotemLatheBlock(this, TOTEM_LATHE_BOTTOM_RIGHT, new BlockPos(2, 0, 0), Block.Properties.of(Material.METAL).strength(3.0F, 4.5F).noDrops()));
	public final RegistryObject<Block> MIDDLE = register("totem_lathe_middle", () -> new TotemLatheBlock(this, TOTEM_LATHE_MIDDLE, new BlockPos(0, -1, 0), Block.Properties.of(Material.METAL).strength(3.0F, 4.5F).noDrops()));
	public final RegistryObject<Block> WHEEL = register("totem_lathe_wheel", () -> new TotemLatheBlock(this, TOTEM_LATHE_MIDDLE_RIGHT, new BlockPos(2, -1, 0), Block.Properties.of(Material.METAL).strength(3.0F, 4.5F).noDrops()));
	public final RegistryObject<Block> DOWEL_ROD = register("totem_lathe_dowel_rod", () -> new TotemLatheBlock.DowelRod(this, TOTEM_LATHE_ROD, TOTEM_LATHE_ROD_ACTIVE, TOTEM_LATHE_ROD_ACTIVE, new BlockPos(1, -1, 0), Block.Properties.of(Material.METAL).strength(3.0F, 4.5F).noDrops()));
	public final RegistryObject<Block> TOP = register("totem_lathe_top", () -> new TotemLatheBlock(this, TOTEM_LATHE_TOP, new BlockPos(1, -2, 0), Block.Properties.of(Material.METAL).strength(3.0F, 4.5F).noDrops()));
	public final RegistryObject<Block> TOP_CORNER = register("totem_lathe_top_corner", () -> new TotemLatheBlock(this, TOTEM_LATHE_TOP_CORNER, new BlockPos(0, -2, 0), Block.Properties.of(Material.METAL).strength(3.0F, 4.5F).noDrops()));
	
	private final PlacementEntry slotPlacement;
	private final PlacementEntry dowelPlacement;
	
	public TotemLatheMultiblock(DeferredRegister<Block> register)
	{
		super(register);
		slotPlacement = registerPlacement(new BlockPos(2, 0, 0), applyDirection(CARD_SLOT, Direction.NORTH));
		registerPlacement(new BlockPos(1, 0, 0), applyDirection(BOTTOM_LEFT, Direction.NORTH));
		registerPlacement(new BlockPos(0, 0, 0), applyDirection(BOTTOM_RIGHT, Direction.NORTH));
		
		registerPlacement(new BlockPos(2, 1, 0), applyDirection(MIDDLE, Direction.NORTH));
		dowelPlacement = registerPlacement(new BlockPos(1, 1, 0), applyDirection(DOWEL_ROD, Direction.NORTH));
		registerPlacement(new BlockPos(0, 1, 0), applyDirection(WHEEL, Direction.NORTH));
		
		registerPlacement(new BlockPos(1, 2, 0), applyDirection(TOP, Direction.NORTH));
		registerPlacement(new BlockPos(2, 2, 0), applyDirection(TOP_CORNER, Direction.NORTH));
	}
	
	public boolean isInvalidFromSlot(BlockGetter level, BlockPos pos)
	{
		return isInvalidFromPlacement(level, pos, slotPlacement);
	}
	
	public BlockPos getDowelPos(BlockPos tilePos, BlockState slotState)
	{
		Rotation rotation = slotPlacement.findRotation(slotState);
		return dowelPlacement.getPos(slotPlacement.getZeroPos(tilePos, rotation), rotation);
	}
	
	public BlockPos getSlotPos(BlockPos tilePos, BlockState state)
	{
		Rotation rotation = dowelPlacement.findRotation(state);
		return slotPlacement.getPos(dowelPlacement.getZeroPos(tilePos, rotation), rotation);
	}
}