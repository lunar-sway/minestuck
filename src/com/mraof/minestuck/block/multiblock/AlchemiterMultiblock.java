package com.mraof.minestuck.block.multiblock;

import com.mraof.minestuck.block.AlchemiterBlock;
import com.mraof.minestuck.block.MSBlockShapes;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.registries.IForgeRegistry;

public class AlchemiterMultiblock extends MachineMultiblock
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
		registry.register(CENTER = new AlchemiterBlock(this, MSBlockShapes.ALCHEMITER_CENTER, true, false, new BlockPos(1, 0, -1), Block.Properties.create(Material.IRON).hardnessAndResistance(3.0F)).noDrops()).setRegistryName("alchemiter_center"));
		registry.register(CORNER = new AlchemiterBlock(this, MSBlockShapes.ALCHEMITER_CORNER, true, true, new BlockPos(0, 0, 3), Block.Properties.create(Material.IRON).hardnessAndResistance(3.0F)).noDrops()).setRegistryName("alchemiter_corner"));
		registry.register(LEFT_SIDE = new AlchemiterBlock(this,MSBlockShapes.ALCHEMITER_LEFT_SIDE, true, false, new BlockPos(1, 0, 0), Block.Properties.create(Material.IRON).hardnessAndResistance(3.0F)).noDrops()).setRegistryName("alchemiter_left_side"));
		registry.register(RIGHT_SIDE = new AlchemiterBlock(this, MSBlockShapes.ALCHEMITER_RIGHT_SIDE, true, false, new BlockPos(2, 0, 0), Block.Properties.create(Material.IRON).hardnessAndResistance(3.0F)).noDrops()).setRegistryName("alchemiter_right_side"));
		registry.register(TOTEM_CORNER = new AlchemiterBlock(this, MSBlockShapes.ALCHEMITER_TOTEM_CORNER, false, true, new BlockPos(0, 1, 0), Block.Properties.create(Material.IRON).hardnessAndResistance(3.0F)).noDrops()).setRegistryName("alchemiter_totem_corner"));
		registry.register(TOTEM_PAD = new AlchemiterBlock.Pad(this, MSBlockShapes.ALCHEMITER_TOTEM_PAD, Block.Properties.create(Material.IRON).hardnessAndResistance(3.0F)).noDrops()).setRegistryName("alchemiter_totem_pad"));
		registry.register(LOWER_ROD = new AlchemiterBlock(this, MSBlockShapes.ALCHEMITER_LOWER_ROD, false, false, new BlockPos(0, -1, 0), Block.Properties.create(Material.IRON).hardnessAndResistance(3.0F)).noDrops()).setRegistryName("alchemiter_lower_rod"));
		registry.register(UPPER_ROD = new AlchemiterBlock(this, MSBlockShapes.ALCHEMITER_UPPER_ROD, false, false, new BlockPos(0, -2, 0), Block.Properties.create(Material.IRON).hardnessAndResistance(3.0F)).noDrops()).setRegistryName("alchemiter_upper_rod"));
	}
}
