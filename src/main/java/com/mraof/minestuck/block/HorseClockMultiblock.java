package com.mraof.minestuck.block;

import com.mraof.minestuck.block.machine.MachineMultiblock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class HorseClockMultiblock extends MachineMultiblock
{
	public final RegistryObject<Block> BOTTOM = register("horse_clock_bottom", () -> new HorseClockBlock.Bottom(this, Block.Properties.of(Material.WOOD).strength(0.5F).sound(SoundType.WOOD).noOcclusion())); //has drops
	public final RegistryObject<Block> CENTER = register("horse_clock_center", () -> new HorseClockBlock(this, Block.Properties.of(Material.WOOD).strength(0.5F).sound(SoundType.WOOD).noLootTable().noOcclusion()));
	public final RegistryObject<Block> TOP = register("horse_clock_top", () -> new HorseClockBlock(this, Block.Properties.of(Material.WOOD).strength(0.5F).sound(SoundType.WOOD).noLootTable().noOcclusion()));
	
	public HorseClockMultiblock(DeferredRegister<Block> register)
	{
		super(register);
		
		registerPlacement(new BlockPos(0, 0, 0), applyDirection(BOTTOM, Direction.NORTH));
		registerPlacement(new BlockPos(0, 1, 0), applyDirection(CENTER, Direction.NORTH));
		registerPlacement(new BlockPos(0, 2, 0), applyDirection(TOP, Direction.NORTH));
	}
}