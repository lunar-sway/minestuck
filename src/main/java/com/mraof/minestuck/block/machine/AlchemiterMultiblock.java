package com.mraof.minestuck.block.machine;

import com.mraof.minestuck.block.MSBlockShapes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.Optional;

public class AlchemiterMultiblock extends MachineMultiblock
{
	public final RegistryObject<Block> CENTER = register("alchemiter_center", () -> new AlchemiterBlock(this, MSBlockShapes.ALCHEMITER_CENTER, true, false, new BlockPos(1, 0, -1), Block.Properties.of(Material.METAL).strength(3.0F, 4.5F).noLootTable()));
	public final RegistryObject<Block> CORNER = register("alchemiter_corner", () -> new AlchemiterBlock(this, MSBlockShapes.ALCHEMITER_CORNER, true, true, new BlockPos(0, 0, 3), Block.Properties.of(Material.METAL).strength(3.0F, 4.5F).noLootTable()));
	public final RegistryObject<Block> LEFT_SIDE = register("alchemiter_left_side", () -> new AlchemiterBlock(this,MSBlockShapes.ALCHEMITER_LEFT_SIDE, true, false, new BlockPos(1, 0, 0), Block.Properties.of(Material.METAL).strength(3.0F, 4.5F).noLootTable()));
	public final RegistryObject<Block> RIGHT_SIDE = register("alchemiter_right_side", () -> new AlchemiterBlock(this, MSBlockShapes.ALCHEMITER_RIGHT_SIDE, true, false, new BlockPos(2, 0, 0), Block.Properties.of(Material.METAL).strength(3.0F, 4.5F).noLootTable()));
	public final RegistryObject<Block> TOTEM_CORNER = register("alchemiter_totem_corner", () -> new AlchemiterBlock(this, MSBlockShapes.ALCHEMITER_TOTEM_CORNER, false, true, new BlockPos(0, 1, 0), Block.Properties.of(Material.METAL).strength(3.0F, 4.5F).noLootTable()));
	public final RegistryObject<Block> TOTEM_PAD = register("alchemiter_totem_pad", () -> new AlchemiterBlock.Pad(this, MSBlockShapes.ALCHEMITER_TOTEM_PAD, Block.Properties.of(Material.METAL).strength(3.0F, 4.5F).noLootTable()));
	public final RegistryObject<Block> LOWER_ROD = register("alchemiter_lower_rod", () -> new AlchemiterBlock(this, MSBlockShapes.ALCHEMITER_LOWER_ROD, false, false, new BlockPos(0, -1, 0), Block.Properties.of(Material.METAL).strength(3.0F, 4.5F).noLootTable()));
	public final RegistryObject<Block> UPPER_ROD = register("alchemiter_upper_rod", () -> new AlchemiterBlock(this, MSBlockShapes.ALCHEMITER_UPPER_ROD, false, false, new BlockPos(0, -2, 0), Block.Properties.of(Material.METAL).strength(3.0F, 4.5F).noLootTable()));
	
	private final PlacementEntry totemPadPos;
	
	public AlchemiterMultiblock(DeferredRegister<Block> register)
	{
		super(register);
		
		addDirectionPlacement(0, 0, 0, CORNER, Direction.WEST);
		addDirectionPlacement(0, 0, 1, LEFT_SIDE, Direction.WEST);
		addDirectionPlacement(0, 0, 2, RIGHT_SIDE, Direction.WEST);
		addDirectionPlacement(1, 0, 1, CENTER, Direction.WEST);
		
		addDirectionPlacement(3, 0, 0, CORNER, Direction.NORTH);
		addDirectionPlacement(2, 0, 0, LEFT_SIDE, Direction.NORTH);
		addDirectionPlacement(1, 0, 0, RIGHT_SIDE, Direction.NORTH);
		addDirectionPlacement(2, 0, 1, CENTER, Direction.NORTH);
		
		addDirectionPlacement(0, 0, 3, CORNER, Direction.SOUTH);
		addDirectionPlacement(2, 0, 3, RIGHT_SIDE, Direction.SOUTH);
		addDirectionPlacement(1, 0, 3, LEFT_SIDE, Direction.SOUTH);
		addDirectionPlacement(1, 0, 2, CENTER, Direction.SOUTH);
		
		addDirectionPlacement(3, 0, 3, TOTEM_CORNER, Direction.EAST);
		addDirectionPlacement(3, 0, 2, LEFT_SIDE, Direction.EAST);
		addDirectionPlacement(3, 0, 1, RIGHT_SIDE, Direction.EAST);
		addDirectionPlacement(2, 0, 2, CENTER, Direction.EAST);
		totemPadPos = addDirectionPlacement(3, 1, 3, TOTEM_PAD, Direction.EAST);
		addDirectionPlacement(3, 2, 3, LOWER_ROD, Direction.EAST);
		addDirectionPlacement(3, 3, 3, UPPER_ROD, Direction.EAST);
	}
	
	public boolean isInvalidFromPad(LevelAccessor level, BlockPos pos)
	{
		return isInvalidFromPlacement(level, pos, totemPadPos);
	}
	
	public Optional<Placement> findPlacementFromPad(LevelAccessor level, BlockPos pos)
	{
		return this.totemPadPos.findPlacement(pos, level.getBlockState(pos));
	}
}