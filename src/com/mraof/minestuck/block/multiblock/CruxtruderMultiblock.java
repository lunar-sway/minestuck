package com.mraof.minestuck.block.multiblock;

import com.mraof.minestuck.block.CruxtruderBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraftforge.registries.IForgeRegistry;

public class CruxtruderMultiblock extends MachineMultiblock
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
		registry.register(CORNER = new CruxtruderBlock(this, VoxelShapes.fullCube(), false, new BlockPos(1, 1, 1), Block.Properties.create(Material.IRON).hardnessAndResistance(3.0F)).setRegistryName("cruxtruder_corner"));
		registry.register(SIDE = new CruxtruderBlock(this, VoxelShapes.fullCube(), false, new BlockPos(0, 1, 1), Block.Properties.create(Material.IRON).hardnessAndResistance(3.0F)).setRegistryName("cruxtruder_side"));
		registry.register(CENTER = new CruxtruderBlock(this, VoxelShapes.fullCube(), true, new BlockPos(0, 1, 0), Block.Properties.create(Material.IRON).hardnessAndResistance(3.0F)).setRegistryName("cruxtruder_center"));
		registry.register(TUBE = new CruxtruderBlock(this, CruxtruderBlock.TUBE_SHAPE, false, new BlockPos(0, 0, 0), Block.Properties.create(Material.IRON).hardnessAndResistance(3.0F)).setRegistryName("cruxtruder_tube"));
	}
}
