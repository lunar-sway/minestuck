package com.mraof.minestuck.block.machine;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.util.Direction;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraftforge.fml.RegistryObject;

import static com.mraof.minestuck.block.MSBlockShapes.*;

public class TotemLatheMultiblock extends MachineMultiblock
{
	public final RegistryObject<Block> CARD_SLOT = register("totem_lathe_card_slot", () -> new TotemLatheBlock.Slot(this, TOTEM_LATHE_CARD_SLOT, AbstractBlock.Properties.of(Material.METAL).strength(3.0F).noDrops()));
	public final RegistryObject<Block> BOTTOM_LEFT = register("totem_lathe_bottom_left", () -> new TotemLatheBlock(this, TOTEM_LATHE_BOTTOM_LEFT, new BlockPos(1, 0, 0), AbstractBlock.Properties.of(Material.METAL).strength(3.0F, 4.5F).noDrops()));
	public final RegistryObject<Block> BOTTOM_RIGHT = register("totem_lathe_bottom_right", () -> new TotemLatheBlock(this, TOTEM_LATHE_BOTTOM_RIGHT, new BlockPos(2, 0, 0), AbstractBlock.Properties.of(Material.METAL).strength(3.0F, 4.5F).noDrops()));
	public final RegistryObject<Block> MIDDLE = register("totem_lathe_middle", () -> new TotemLatheBlock(this, TOTEM_LATHE_MIDDLE, new BlockPos(0, -1, 0), AbstractBlock.Properties.of(Material.METAL).strength(3.0F, 4.5F).noDrops()));
	public final RegistryObject<Block> WHEEL = register("totem_lathe_wheel", () -> new TotemLatheBlock(this, TOTEM_LATHE_MIDDLE_RIGHT, new BlockPos(2, -1, 0), AbstractBlock.Properties.of(Material.METAL).strength(3.0F, 4.5F).noDrops()));
	public final RegistryObject<Block> DOWEL_ROD = register("totem_lathe_rod", () -> new TotemLatheBlock.DowelRod(this, TOTEM_LATHE_ROD, TOTEM_LATHE_ROD_ACTIVE, TOTEM_LATHE_ROD_ACTIVE, new BlockPos(1, -1, 0), AbstractBlock.Properties.of(Material.METAL).strength(3.0F, 4.5F).noDrops()));
	public final RegistryObject<Block> TOP = register("totem_lathe_top", () -> new TotemLatheBlock(this, TOTEM_LATHE_TOP, new BlockPos(1, -2, 0), AbstractBlock.Properties.of(Material.METAL).strength(3.0F, 4.5F).noDrops()));
	public final RegistryObject<Block> TOP_CORNER = register("totem_lathe_top_corner", () -> new TotemLatheBlock(this, TOTEM_LATHE_TOP_CORNER, new BlockPos(0, -2, 0), AbstractBlock.Properties.of(Material.METAL).strength(3.0F, 4.5F).noDrops()));

	private final PlacementEntry slotPlacement;
	private final PlacementEntry dowelPlacement;

	public TotemLatheMultiblock(String modId)
	{
		super(modId);
		slotPlacement = registerPlacement(new BlockPos(2, 0, 0), applyDirection(CARD_SLOT, Direction.NORTH));
		registerPlacement(new BlockPos(1, 0, 0), applyDirection(BOTTOM_LEFT, Direction.NORTH));
		registerPlacement(new BlockPos(0, 0, 0), applyDirection(BOTTOM_RIGHT, Direction.NORTH));

		registerPlacement(new BlockPos(2, 1, 0), applyDirection(MIDDLE, Direction.NORTH));
		dowelPlacement = registerPlacement(new BlockPos(1, 1, 0), applyDirection(DOWEL_ROD, Direction.NORTH));
		registerPlacement(new BlockPos(0, 1, 0), applyDirection(WHEEL, Direction.NORTH));

		registerPlacement(new BlockPos(1, 2, 0), applyDirection(TOP, Direction.NORTH));
		registerPlacement(new BlockPos(2, 2, 0), applyDirection(TOP_CORNER, Direction.NORTH));
	}

	public boolean isInvalidFromSlot(IWorld world, BlockPos pos)
	{
		return isInvalidFromPlacement(world, pos, slotPlacement);
	}

	public BlockPos getDowelPos(BlockPos tilePos, BlockState slotState)
	{
		Rotation rotation = slotPlacement.findRotation(slotState);
		return dowelPlacement.getPos(slotPlacement.getZeroPos(tilePos, rotation), rotation);
	}
}