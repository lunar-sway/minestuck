package com.mraof.minestuck.block.multiblock;

import com.mraof.minestuck.block.BlockAlchemiter;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.registries.IForgeRegistry;

public class MultiblockAlchemiter extends MultiblockMachine
{
	public Block CENTER, CORNER, LEFT_SIDE, RIGHT_SIDE;
	public Block TOTEM_CORNER, TOTEM_PAD, LOWER_ROD, UPPER_ROD;
	
	@Override
	public Block getMainBlock()
	{
		return CENTER;
	}
	
	@Override
	public void registerBlocks(IForgeRegistry<Block> registry)
	{
		registry.register(CENTER = new BlockAlchemiter(this, BlockAlchemiter.FULL_BLOCK_SHAPE, BlockAlchemiter.SOLID_FACE_SHAPES, true, true, false, new BlockPos(-1, 0, 1), Block.Properties.create(Material.IRON).hardnessAndResistance(3.0F)).setRegistryName("alchemiter_center"));
		registry.register(CORNER = new BlockAlchemiter(this, BlockAlchemiter.FULL_BLOCK_SHAPE, BlockAlchemiter.CORNER_FACE_SHAPES, false, true, true, new BlockPos(0, 0, -3), Block.Properties.create(Material.IRON).hardnessAndResistance(3.0F)).setRegistryName("alchemiter_corner"));
		registry.register(LEFT_SIDE = new BlockAlchemiter(this, BlockAlchemiter.FULL_BLOCK_SHAPE, BlockAlchemiter.SIDE_FACE_SHAPES, false, true, false, new BlockPos(-1, 0, 0), Block.Properties.create(Material.IRON).hardnessAndResistance(3.0F)).setRegistryName("alchemiter_left_side"));
		registry.register(RIGHT_SIDE = new BlockAlchemiter(this, BlockAlchemiter.FULL_BLOCK_SHAPE, BlockAlchemiter.SIDE_FACE_SHAPES, false, true, false, new BlockPos(-2, 0, 0), Block.Properties.create(Material.IRON).hardnessAndResistance(3.0F)).setRegistryName("alchemiter_right_side"));
		registry.register(TOTEM_CORNER = new BlockAlchemiter(this, BlockAlchemiter.FULL_BLOCK_SHAPE, BlockAlchemiter.CORNER_FACE_SHAPES, false, false, true, new BlockPos(0, 1, 0), Block.Properties.create(Material.IRON).hardnessAndResistance(3.0F)).setRegistryName("alchemiter_totem_corner"));
		registry.register(TOTEM_PAD = new BlockAlchemiter.Pad(this, BlockAlchemiter.TOTEM_PAD_SHAPE, BlockAlchemiter.UNDEFINED_FACE_SHAPES, false, Block.Properties.create(Material.IRON).hardnessAndResistance(3.0F)).setRegistryName("alchemiter_totem_pad"));
		registry.register(LOWER_ROD = new BlockAlchemiter(this, BlockAlchemiter.LOWER_ROD_SHAPE, BlockAlchemiter.UNDEFINED_FACE_SHAPES, false, false, false, new BlockPos(0, -1, 0), Block.Properties.create(Material.IRON).hardnessAndResistance(3.0F)).setRegistryName("alchemiter_lower_rod"));
		registry.register(UPPER_ROD = new BlockAlchemiter(this, BlockAlchemiter.UPPER_ROD_SHAPE, BlockAlchemiter.UNDEFINED_FACE_SHAPES, false, false, false, new BlockPos(0, -2, 0), Block.Properties.create(Material.IRON).hardnessAndResistance(3.0F)).setRegistryName("alchemiter_upper_rod"));
		
	}
}
