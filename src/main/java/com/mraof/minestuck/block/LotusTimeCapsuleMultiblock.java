package com.mraof.minestuck.block;

import com.mraof.minestuck.block.machine.MachineMultiblock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.RegistryObject;

public class LotusTimeCapsuleMultiblock extends MachineMultiblock
{
	public final RegistryObject<Block> CORNER = register("lotus_time_capsule_base", () -> new LotusTimeCapsuleBlock(this, MSBlockShapes.LOTUS_TIME_CAPSULE, Block.Properties.create(Material.ROCK).hardnessAndResistance(-1F).noDrops()));
	
	public LotusTimeCapsuleMultiblock(String modId)
	{
		super(modId);
		
		registerPlacement(new BlockPos(0, 0, 0), applyDirection(CORNER, Direction.WEST));
		
		registerPlacement(new BlockPos(1, 0, 0), applyDirection(CORNER, Direction.NORTH));
		
		registerPlacement(new BlockPos(0, 0, 1), applyDirection(CORNER, Direction.SOUTH));
		
		registerPlacement(new BlockPos(1, 0, 1), applyDirection(CORNER, Direction.EAST));
	}
	
}