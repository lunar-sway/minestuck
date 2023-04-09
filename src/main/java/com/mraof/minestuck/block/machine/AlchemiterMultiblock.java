package com.mraof.minestuck.block.machine;

import com.mraof.minestuck.block.MSBlockShapes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class AlchemiterMultiblock extends MachineMultiblock
{
	public final RegistryObject<Block> CENTER = register("alchemiter_center", () -> new AlchemiterBlock(this, MSBlockShapes.ALCHEMITER_CENTER, true, false, new BlockPos(1, 0, -1), Block.Properties.of(Material.METAL).strength(3.0F, 4.5F).noLootTable()));
	public final RegistryObject<Block> CORNER = register("alchemiter_corner", () -> new AlchemiterBlock(this, MSBlockShapes.ALCHEMITER_CORNER, true, true, new BlockPos(0, 0, 3), Block.Properties.of(Material.METAL).strength(3.0F, 4.5F).noLootTable()));
	public final RegistryObject<Block> LEFT_SIDE = register("alchemiter_left_side", () -> new AlchemiterBlock(this,MSBlockShapes.ALCHEMITER_LEFT_SIDE, true, false, new BlockPos(1, 0, 0), Block.Properties.of(Material.METAL).strength(3.0F, 4.5F).noLootTable()));
	public final RegistryObject<Block> RIGHT_SIDE = register("alchemiter_right_side", () -> new AlchemiterBlock(this, MSBlockShapes.ALCHEMITER_RIGHT_SIDE, true, false, new BlockPos(2, 0, 0), Block.Properties.of(Material.METAL).strength(3.0F, 4.5F).noLootTable()));
	public final RegistryObject<Block> TOTEM_CORNER = register("alchemiter_totem_corner", () -> new AlchemiterBlock(this, MSBlockShapes.ALCHEMITER_TOTEM_CORNER, false, true, new BlockPos(0, 1, 0), Block.Properties.of(Material.METAL).strength(3.0F, 4.5F).noLootTable()));
	public final RegistryObject<Block> TOTEM_PAD = register("alchemiter_totem_pad", () -> new AlchemiterBlock.Pad(this, MSBlockShapes.ALCHEMITER_TOTEM_PAD, MSBlockShapes.ALCHEMITER_TOTEM_PAD_DOWEL, Block.Properties.of(Material.METAL).strength(3.0F, 4.5F).noLootTable()));
	
	private final PlacementEntry totemPadPos;
	
	public AlchemiterMultiblock(DeferredRegister<Block> register)
	{
		super(register);
		
		registerPlacement(new BlockPos(0, 0, 0), applyDirection(CORNER, Direction.WEST));
		registerPlacement(new BlockPos(0, 0, 1), applyDirection(LEFT_SIDE, Direction.WEST));
		registerPlacement(new BlockPos(0, 0, 2), applyDirection(RIGHT_SIDE, Direction.WEST));
		registerPlacement(new BlockPos(1, 0, 1), applyDirection(CENTER, Direction.WEST));
		
		registerPlacement(new BlockPos(3, 0, 0), applyDirection(CORNER, Direction.NORTH));
		registerPlacement(new BlockPos(2, 0, 0), applyDirection(LEFT_SIDE, Direction.NORTH));
		registerPlacement(new BlockPos(1, 0, 0), applyDirection(RIGHT_SIDE, Direction.NORTH));
		registerPlacement(new BlockPos(2, 0, 1), applyDirection(CENTER, Direction.NORTH));
		
		registerPlacement(new BlockPos(0, 0, 3), applyDirection(CORNER, Direction.SOUTH));
		registerPlacement(new BlockPos(2, 0, 3), applyDirection(RIGHT_SIDE, Direction.SOUTH));
		registerPlacement(new BlockPos(1, 0, 3), applyDirection(LEFT_SIDE, Direction.SOUTH));
		registerPlacement(new BlockPos(1, 0, 2), applyDirection(CENTER, Direction.SOUTH));
		
		registerPlacement(new BlockPos(3, 0, 3), applyDirection(TOTEM_CORNER, Direction.EAST));
		registerPlacement(new BlockPos(3, 0, 2), applyDirection(LEFT_SIDE, Direction.EAST));
		registerPlacement(new BlockPos(3, 0, 1), applyDirection(RIGHT_SIDE, Direction.EAST));
		registerPlacement(new BlockPos(2, 0, 2), applyDirection(CENTER, Direction.EAST));
		totemPadPos = registerPlacement(new BlockPos(3, 1, 3), applyDirection(TOTEM_PAD, Direction.EAST));
	}
	
	public boolean isInvalidFromPad(LevelAccessor level, BlockPos pos)
	{
		return isInvalidFromPlacement(level, pos, totemPadPos);
	}
}