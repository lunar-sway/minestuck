package com.mraof.minestuck.block;

import com.mraof.minestuck.block.machine.MachineMultiblock;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class LotusTimeCapsuleMultiblock extends MachineMultiblock
{
	public final RegistryObject<Block> CORNER = register("lotus_time_capsule_base", () -> new LotusTimeCapsuleBlock(this, MSBlockShapes.LOTUS_TIME_CAPSULE, Block.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).strength(-1F).noLootTable()));
	
	public LotusTimeCapsuleMultiblock(DeferredRegister<Block> register)
	{
		super(register);
		
		addDirectionPlacement(0, 0, 0, CORNER, Direction.WEST);
		addDirectionPlacement(1, 0, 0, CORNER, Direction.NORTH);
		addDirectionPlacement(0, 0, 1, CORNER, Direction.SOUTH);
		addDirectionPlacement(1, 0, 1, CORNER, Direction.EAST);
	}
	
}