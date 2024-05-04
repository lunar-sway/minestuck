package com.mraof.minestuck.block.machine;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Optional;

import static com.mraof.minestuck.block.MSBlockShapes.*;

public class TotemLatheMultiblock extends MachineMultiblock
{
	public final DeferredBlock<Block> CARD_SLOT = register("totem_lathe_card_slot", () -> new TotemLatheBlock.Slot(this, TOTEM_LATHE_CARD_SLOT, Block.Properties.of().mapColor(MapColor.METAL).strength(3.0F).noLootTable()));
	public final DeferredBlock<Block> BOTTOM_LEFT = register("totem_lathe_bottom_left", () -> new TotemLatheBlock(this, TOTEM_LATHE_BOTTOM_LEFT, new BlockPos(1, 0, 0), Block.Properties.of().mapColor(MapColor.METAL).strength(3.0F, 4.5F).noLootTable()));
	public final DeferredBlock<Block> BOTTOM_RIGHT = register("totem_lathe_bottom_right", () -> new TotemLatheBlock(this, TOTEM_LATHE_BOTTOM_RIGHT, new BlockPos(2, 0, 0), Block.Properties.of().mapColor(MapColor.METAL).strength(3.0F, 4.5F).noLootTable()));
	public final DeferredBlock<Block> MIDDLE = register("totem_lathe_middle", () -> new TotemLatheBlock(this, TOTEM_LATHE_MIDDLE, new BlockPos(0, -1, 0), Block.Properties.of().mapColor(MapColor.METAL).strength(3.0F, 4.5F).noLootTable()));
	public final DeferredBlock<Block> WHEEL = register("totem_lathe_wheel", () -> new TotemLatheBlock(this, TOTEM_LATHE_MIDDLE_RIGHT, new BlockPos(2, -1, 0), Block.Properties.of().mapColor(MapColor.METAL).strength(3.0F, 4.5F).noLootTable()));
	public final DeferredBlock<Block> DOWEL_ROD = register("totem_lathe_dowel_rod", () -> new TotemLatheBlock.DowelRod(this, TOTEM_LATHE_ROD, TOTEM_LATHE_ROD_ACTIVE, TOTEM_LATHE_ROD_ACTIVE, new BlockPos(1, -1, 0), Block.Properties.of().mapColor(MapColor.METAL).strength(3.0F, 4.5F).noLootTable()));
	public final DeferredBlock<Block> TOP = register("totem_lathe_top", () -> new TotemLatheBlock(this, TOTEM_LATHE_TOP, new BlockPos(1, -2, 0), Block.Properties.of().mapColor(MapColor.METAL).strength(3.0F, 4.5F).noLootTable()));
	public final DeferredBlock<Block> TOP_CORNER = register("totem_lathe_top_corner", () -> new TotemLatheBlock(this, TOTEM_LATHE_TOP_CORNER, new BlockPos(0, -2, 0), Block.Properties.of().mapColor(MapColor.METAL).strength(3.0F, 4.5F).noLootTable()));
	
	private final PlacementEntry slotPlacement;
	private final PlacementEntry dowelPlacement;
	
	public TotemLatheMultiblock(DeferredRegister.Blocks register)
	{
		super(register);
		slotPlacement = addDirectionPlacement(2, 0, 0, CARD_SLOT, Direction.NORTH);
		addDirectionPlacement(1, 0, 0, BOTTOM_LEFT, Direction.NORTH);
		addDirectionPlacement(0, 0, 0, BOTTOM_RIGHT, Direction.NORTH);
		
		addDirectionPlacement(2, 1, 0, MIDDLE, Direction.NORTH);
		dowelPlacement = addDirectionPlacement(1, 1, 0, DOWEL_ROD, Direction.NORTH);
		addDirectionPlacement(0, 1, 0, WHEEL, Direction.NORTH);
		
		addDirectionPlacement(1, 2, 0, TOP, Direction.NORTH);
		addDirectionPlacement(2, 2, 0, TOP_CORNER, Direction.NORTH);
	}
	
	public boolean isInvalidFromSlot(BlockGetter level, BlockPos pos)
	{
		return isInvalidFromPlacement(level, pos, slotPlacement);
	}
	
	public BlockPos getDowelPos(BlockPos tilePos, BlockState slotState)
	{
		return dowelPlacement.getPos(slotPlacement.findPlacementOrThrow(tilePos, slotState));
	}
	
	public Optional<Placement> findPlacementFromSlot(LevelAccessor level, BlockPos pos)
	{
		return slotPlacement.findPlacement(pos, level.getBlockState(pos));
	}
	
	public BlockPos getSlotPos(BlockPos tilePos, BlockState state)
	{
		return slotPlacement.getPos(dowelPlacement.findPlacementOrThrow(tilePos, state));
	}
}