package com.mraof.minestuck.block.machine;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.Optional;

import static com.mraof.minestuck.block.MSBlockShapes.*;

public class TotemLatheMultiblock extends MachineMultiblock
{
	public final RegistryObject<Block> CARD_SLOT = register("totem_lathe_card_slot", () -> new TotemLatheBlock.Slot(this, TOTEM_LATHE_CARD_SLOT, Block.Properties.of(Material.METAL).strength(3.0F).noLootTable()));
	public final RegistryObject<Block> BOTTOM_LEFT = register("totem_lathe_bottom_left", () -> new TotemLatheBlock(this, TOTEM_LATHE_BOTTOM_LEFT, new BlockPos(1, 0, 0), Block.Properties.of(Material.METAL).strength(3.0F, 4.5F).noLootTable()));
	public final RegistryObject<Block> BOTTOM_RIGHT = register("totem_lathe_bottom_right", () -> new TotemLatheBlock(this, TOTEM_LATHE_BOTTOM_RIGHT, new BlockPos(2, 0, 0), Block.Properties.of(Material.METAL).strength(3.0F, 4.5F).noLootTable()));
	public final RegistryObject<Block> BOTTOM_CORNER = register("totem_lathe_bottom_corner", () -> new TotemLatheBlock(this, TOTEM_LATHE_BOTTOM_CORNER, new BlockPos(3, 0, 0), Block.Properties.of(Material.METAL).strength(3.0F, 4.5F).noLootTable()));
	public final RegistryObject<Block> MIDDLE = register("totem_lathe_middle", () -> new TotemLatheBlock(this, TOTEM_LATHE_MIDDLE, new BlockPos(0, -1, 0), Block.Properties.of(Material.METAL).strength(3.0F, 4.5F).noLootTable()));
	public final RegistryObject<Block> WHEEL = register("totem_lathe_wheel", () -> new TotemLatheBlock.Rod(this, TOTEM_LATHE_MIDDLE_RIGHT, TOTEM_LATHE_MIDDLE_RIGHT_ACTIVE, new BlockPos(3, -1, 0), Direction.EAST, Block.Properties.of(Material.METAL).strength(3.0F, 4.5F).noLootTable()));
	public final RegistryObject<Block> ROD = register("totem_lathe_rod", () -> new TotemLatheBlock.Rod(this, TOTEM_LATHE_ROD_LEFT, TOTEM_LATHE_ROD_LEFT_ACTIVE, new BlockPos(1, -1, 0), Direction.WEST, Block.Properties.of(Material.METAL).strength(3.0F, 4.5F).noLootTable()));
	public final RegistryObject<Block> DOWEL_ROD = register("totem_lathe_dowel_rod", () -> new TotemLatheBlock.DowelRod(this, TOTEM_LATHE_ROD_RIGHT, TOTEM_LATHE_ROD_RIGHT_CARVED, new BlockPos(2, -1, 0), Block.Properties.of(Material.METAL).strength(3.0F, 4.5F).noLootTable()));
	public final RegistryObject<Block> TOP_CORNER = register("totem_lathe_top_corner", () -> new TotemLatheBlock(this,TOTEM_LATHE_TOP_LEFT, new BlockPos(0, -2, 0), Block.Properties.of(Material.METAL).strength(3.0F, 4.5F).noLootTable()));
	public final RegistryObject<Block> TOP = register("totem_lathe_top", () -> new TotemLatheBlock(this, TOTEM_LATHE_TOP_MIDDLE, new BlockPos(1, -2, 0), Block.Properties.of(Material.METAL).strength(3.0F, 4.5F).noLootTable()));
	public final RegistryObject<Block> CARVER = register("totem_lathe_carver", () -> new TotemLatheBlock(this, TOTEM_LATHE_CARVER, new BlockPos(2, -2, 0), Block.Properties.of(Material.METAL).strength(3.0F, 4.5F).noLootTable()));
	
	private final PlacementEntry slotPlacement;
	private final PlacementEntry dowelPlacement;
	private final PlacementEntry wheelPlacement;
	private final PlacementEntry rodPlacement;
	
	public TotemLatheMultiblock(DeferredRegister<Block> register)
	{
		super(register);
		slotPlacement = addDirectionPlacement(3, 0, 0, CARD_SLOT, Direction.NORTH);
		addDirectionPlacement(2, 0, 0, BOTTOM_LEFT, Direction.NORTH);
		addDirectionPlacement(1, 0, 0, BOTTOM_RIGHT, Direction.NORTH);
		addDirectionPlacement(0, 0, 0, BOTTOM_CORNER, Direction.NORTH);
		addDirectionPlacement(3, 1, 0, MIDDLE, Direction.NORTH);
		rodPlacement = addDirectionPlacement(2, 1, 0, ROD, Direction.NORTH);
		dowelPlacement = addDirectionOptional(1, 1, 0, DOWEL_ROD, Direction.NORTH);
		wheelPlacement = addDirectionPlacement(0, 1, 0, WHEEL, Direction.NORTH);
		addDirectionPlacement(3, 2, 0, TOP_CORNER, Direction.NORTH);
		addDirectionPlacement(2, 2, 0, TOP, Direction.NORTH);
		addDirectionPlacement(1, 2, 0, CARVER, Direction.NORTH);
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
}