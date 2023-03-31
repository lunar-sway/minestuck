package com.mraof.minestuck.block.machine;

import com.mraof.minestuck.block.MSBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import static com.mraof.minestuck.block.MSBlockShapes.*;

public class CruxtruderMultiblock extends MachineMultiblock
{
	public final RegistryObject<Block> CORNER = register("cruxtruder_corner", () -> new CruxtruderBlock(this, CRUXTRUDER_BASE, false, new BlockPos(1, 2, 1), Block.Properties.of(Material.METAL).strength(3.0F, 4.5F).noLootTable()));
	public final RegistryObject<Block> SIDE = register("cruxtruder_side", () -> new CruxtruderBlock(this, CRUXTRUDER_BASE, false, new BlockPos(0, 2, 1), Block.Properties.of(Material.METAL).strength(3.0F, 4.5F).noLootTable()));
	public final RegistryObject<Block> CENTER = register("cruxtruder_center", () -> new CruxtruderBlock(this, CRUXTRUDER_BASE, false, new BlockPos(0, 2, 0), Block.Properties.of(Material.METAL).strength(3.0F, 4.5F).noLootTable()));
	public final RegistryObject<Block> TOP_CORNER = register("cruxtruder_top_corner", () -> new CruxtruderBlock(this, CRUXTRUDER_TOP_CORNER, false, new BlockPos(1, 1, 1), Block.Properties.of(Material.METAL).strength(3.0F, 4.5F).noLootTable()));
	public final RegistryObject<Block> TOP_SIDE = register("cruxtruder_top_side", () -> new CruxtruderBlock(this, CRUXTRUDER_TOP_SIDE, false, new BlockPos(0, 1, 1), Block.Properties.of(Material.METAL).strength(3.0F, 4.5F).noLootTable()));
	public final RegistryObject<Block> TOP_CENTER = register("cruxtruder_top_center", () -> new CruxtruderBlock(this, CRUXTRUDER_TOP_CENTER, false, new BlockPos(0, 1, 1), Block.Properties.of(Material.METAL).strength(3.0F, 4.5F).noLootTable()));
	public final RegistryObject<Block> TUBE = register("cruxtruder_tube", () -> new CruxtruderBlock(this, CRUXTRUDER_TUBE, true, new BlockPos(0, 0, 0), Block.Properties.of(Material.METAL).strength(3.0F, 4.5F).noLootTable()));
	
	private final PlacementEntry tubePlacement;
	
	public CruxtruderMultiblock(DeferredRegister<Block> register)
	{
		super(register);
		registerPlacement(new BlockPos(0, 0, 0), applyDirection(CORNER, Direction.NORTH));
		registerPlacement(new BlockPos(1, 0, 0), applyDirection(SIDE, Direction.NORTH));
		registerPlacement(new BlockPos(2, 0, 0), applyDirection(CORNER, Direction.EAST));
		registerPlacement(new BlockPos(2, 0, 1), applyDirection(SIDE, Direction.EAST));
		registerPlacement(new BlockPos(2, 0, 2), applyDirection(CORNER, Direction.SOUTH));
		registerPlacement(new BlockPos(1, 0, 2), applyDirection(SIDE, Direction.SOUTH));
		registerPlacement(new BlockPos(0, 0, 2), applyDirection(CORNER, Direction.WEST));
		registerPlacement(new BlockPos(0, 0, 1), applyDirection(SIDE, Direction.WEST));
		registerPlacement(new BlockPos(1, 0, 1), applyDirection(CENTER, Direction.NORTH));

		registerPlacement(new BlockPos(0, 1, 0), applyDirection(TOP_CORNER, Direction.NORTH));
		registerPlacement(new BlockPos(1, 1, 0), applyDirection(TOP_SIDE, Direction.NORTH));
		registerPlacement(new BlockPos(2, 1, 0), applyDirection(TOP_CORNER, Direction.EAST));
		registerPlacement(new BlockPos(2, 1, 1), applyDirection(TOP_SIDE, Direction.EAST));
		registerPlacement(new BlockPos(2, 1, 2), applyDirection(TOP_CORNER, Direction.SOUTH));
		registerPlacement(new BlockPos(1, 1, 2), applyDirection(TOP_SIDE, Direction.SOUTH));
		registerPlacement(new BlockPos(0, 1, 2), applyDirection(TOP_CORNER, Direction.WEST));
		registerPlacement(new BlockPos(0, 1, 1), applyDirection(TOP_SIDE, Direction.WEST));
		registerPlacement(new BlockPos(1, 1, 1), applyDirection(TOP_CENTER, Direction.NORTH));

		tubePlacement = registerPlacement(new BlockPos(1, 2, 1), applyDirection(TUBE, Direction.NORTH));
		//noinspection Convert2MethodRef
		registerPlacement(new BlockPos(1, 3, 1), () -> MSBlocks.CRUXTRUDER_LID.get().defaultBlockState(), (state1, state2) -> true);
	}
	
	public BlockPos getBEPos(BlockPos placementPos, Rotation rotation)
	{
		return tubePlacement.getPos(placementPos, rotation);
	}
	
	public boolean isInvalidFromTube(LevelAccessor level, BlockPos pos)
	{
		return this.isInvalidFromPlacement(level, pos, this.tubePlacement);
	}
}