package com.mraof.minestuck.block.multiblock;

import com.mraof.minestuck.block.CruxtruderBlock;
import static com.mraof.minestuck.block.MSBlockShapes.*;
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
		registry.register(CORNER = new CruxtruderBlock(this, CRUXTRUDER_BASE_CORNER, false, new BlockPos(1, 1, 1), Block.Properties.create(Material.IRON).hardnessAndResistance(3.0F).noDrops()).setRegistryName("cruxtruder_corner"));
		registry.register(SIDE = new CruxtruderBlock(this, CRUXTRUDER_BASE_SIDE, false, new BlockPos(0, 1, 1), Block.Properties.create(Material.IRON).hardnessAndResistance(3.0F).noDrops()).setRegistryName("cruxtruder_side"));
		registry.register(CENTER = new CruxtruderBlock(this, CRUXTRUDER_CENTER, true, new BlockPos(0, 1, 0), Block.Properties.create(Material.IRON).hardnessAndResistance(3.0F).noDrops()).setRegistryName("cruxtruder_center"));
		registry.register(TUBE = new CruxtruderBlock(this, CRUXTRUDER_TUBE, false, new BlockPos(0, 0, 0), Block.Properties.create(Material.IRON).hardnessAndResistance(3.0F).noDrops()).setRegistryName("cruxtruder_tube"));
	}
}
