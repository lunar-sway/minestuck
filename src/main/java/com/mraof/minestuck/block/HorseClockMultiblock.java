package com.mraof.minestuck.block;

import com.mraof.minestuck.block.machine.MachineMultiblock;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class HorseClockMultiblock extends MachineMultiblock
{
	public final RegistryObject<Block> BOTTOM = register("horse_clock_bottom", () -> new HorseClockBlock.Bottom(this, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(0.5F).sound(SoundType.WOOD).noOcclusion())); //has drops
	public final RegistryObject<Block> CENTER = register("horse_clock_center", () -> new HorseClockBlock(this, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(0.5F).sound(SoundType.WOOD).noLootTable().noOcclusion()));
	public final RegistryObject<Block> TOP = register("horse_clock_top", () -> new HorseClockBlock(this, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(0.5F).sound(SoundType.WOOD).noLootTable().noOcclusion()));
	
	public HorseClockMultiblock(DeferredRegister<Block> register)
	{
		super(register);
		
		addDirectionPlacement(0, 0, 0, BOTTOM, Direction.NORTH);
		addDirectionPlacement(0, 1, 0, CENTER, Direction.NORTH);
		addDirectionPlacement(0, 2, 0, TOP, Direction.NORTH);
	}
}