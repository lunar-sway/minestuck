package com.mraof.minestuck.block.multiblock;

import com.mraof.minestuck.block.BlockTotemLathe;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.registries.IForgeRegistry;

public class MultiblockTotemLathe extends MultiblockMachine
{
	public Block CARD_SLOT, BOTTOM_LEFT, BOTTOM_RIGHT, BOTTOM_CORNER;
	public Block MIDDLE, WHEEL, ROD, DOWEL_ROD;
	public Block TOP_CORNER, TOP, CARVER;
	
	@Override
	public Block getMainBlock()
	{
		return MIDDLE;
	}
	
	@Override
	public void registerBlocks(IForgeRegistry<Block> registry)
	{
		registry.register(CARD_SLOT = new BlockTotemLathe.Slot(this, BlockTotemLathe.CARD_SLOT_SHAPE, Block.Properties.create(Material.IRON).hardnessAndResistance(3.0F)).setRegistryName("totem_lathe_card_slot"));
		registry.register(BOTTOM_LEFT = new BlockTotemLathe(this, BlockTotemLathe.BOTTOM_LEFT_SHAPE, new BlockPos(1, 0, 0), Block.Properties.create(Material.IRON).hardnessAndResistance(3.0F)).setRegistryName("totem_lathe_bottom_left"));
		registry.register(BOTTOM_RIGHT = new BlockTotemLathe(this, BlockTotemLathe.BOTTOM_RIGHT_SHAPE, new BlockPos(2, 0, 0), Block.Properties.create(Material.IRON).hardnessAndResistance(3.0F)).setRegistryName("totem_lathe_bottom_right"));
		registry.register(BOTTOM_CORNER = new BlockTotemLathe(this, BlockTotemLathe.BOTTOM_CORNER_SHAPE, new BlockPos(3, 0, 0), Block.Properties.create(Material.IRON).hardnessAndResistance(3.0F)).setRegistryName("totem_lathe_bottom_corner"));
		registry.register(MIDDLE = new BlockTotemLathe(this, BlockTotemLathe.MIDDLE_SHAPE, new BlockPos(0, -1, 0), Block.Properties.create(Material.IRON).hardnessAndResistance(3.0F)).setRegistryName("totem_lathe_middle"));
		registry.register(WHEEL = new BlockTotemLathe.Rod(this, BlockTotemLathe.WHEEL_SHAPE, new BlockPos(3, -1, 0), Block.Properties.create(Material.IRON).hardnessAndResistance(3.0F)).setRegistryName("totem_lathe_wheel"));
		registry.register(ROD = new BlockTotemLathe.Rod(this, BlockTotemLathe.ROD_SHAPE, new BlockPos(1, -1, 0), Block.Properties.create(Material.IRON).hardnessAndResistance(3.0F)).setRegistryName("totem_lathe_rod"));
		registry.register(DOWEL_ROD = new BlockTotemLathe.DowelRod(this, BlockTotemLathe.ROD_SHAPE, new BlockPos(2, -1, 0), Block.Properties.create(Material.IRON).hardnessAndResistance(3.0F)).setRegistryName("totem_lathe_dowel_rod"));
		registry.register(TOP_CORNER = new BlockTotemLathe(this, BlockTotemLathe.TOP_CORNER_SHAPE, new BlockPos(0, -2, 0), Block.Properties.create(Material.IRON).hardnessAndResistance(3.0F)).setRegistryName("totem_lathe_top_corner"));
		registry.register(TOP = new BlockTotemLathe(this, BlockTotemLathe.TOP_SHAPE, new BlockPos(1, -2, 0), Block.Properties.create(Material.IRON).hardnessAndResistance(3.0F)).setRegistryName("totem_lathe_top"));
		registry.register(CARVER = new BlockTotemLathe(this, BlockTotemLathe.CARVER_SHAPE, new BlockPos(2, -2, 0), Block.Properties.create(Material.IRON).hardnessAndResistance(3.0F)).setRegistryName("totem_lathe_carver"));
		
	}
}
