package com.mraof.minestuck.block.multiblock;

import com.mraof.minestuck.block.BlockCruxtruder;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraftforge.registries.IForgeRegistry;

public class MultiblockCruxtruder extends MultiblockMachine
{
	public Block CORNER, SIDE, CENTER, TUBE;
	
	@Override
	public Block getMainBlock()
	{
		return CENTER;
	}
	
	@Override
	public void registerBlocks(IForgeRegistry<Block> registry)
	{
		registry.register(CORNER = new BlockCruxtruder(this, VoxelShapes.fullCube(), false, false, new BlockPos(1, 1, 1), Block.Properties.create(Material.IRON).hardnessAndResistance(3.0F)).setRegistryName("cruxtruder_corner"));
		registry.register(SIDE = new BlockCruxtruder(this, VoxelShapes.fullCube(), false, false, new BlockPos(0, 1, 1), Block.Properties.create(Material.IRON).hardnessAndResistance(3.0F)).setRegistryName("cruxtruder_side"));
		registry.register(CENTER = new BlockCruxtruder(this, VoxelShapes.fullCube(), false, true, new BlockPos(0, 1, 0), Block.Properties.create(Material.IRON).hardnessAndResistance(3.0F)).setRegistryName("cruxtruder_center"));
		registry.register(TUBE = new BlockCruxtruder(this, BlockCruxtruder.TUBE_SHAPE, true, false, new BlockPos(0, 0, 0), Block.Properties.create(Material.IRON).hardnessAndResistance(3.0F)).setRegistryName("cruxtruder_tube"));
	}
}
