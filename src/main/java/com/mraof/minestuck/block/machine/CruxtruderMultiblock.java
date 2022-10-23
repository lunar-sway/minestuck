package com.mraof.minestuck.block.machine;

import com.mraof.minestuck.block.MSBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import static com.mraof.minestuck.block.MSBlockShapes.*;

public class CruxtruderMultiblock extends MachineMultiblock
{
	public final RegistryObject<Block> CORNER = register("cruxtruder_corner", () -> new CruxtruderBlock(this, CRUXTRUDER_BASE_CORNER, false, new BlockPos(1, 1, 1), Block.Properties.of(Material.METAL).strength(3.0F, 4.5F).noDrops()));
	public final RegistryObject<Block> SIDE = register("cruxtruder_side", () -> new CruxtruderBlock(this, CRUXTRUDER_BASE_SIDE, false, new BlockPos(0, 1, 1), Block.Properties.of(Material.METAL).strength(3.0F, 4.5F).noDrops()));
	public final RegistryObject<Block> CENTER = register("cruxtruder_center", () -> new CruxtruderBlock(this, CRUXTRUDER_CENTER, false, new BlockPos(0, 1, 0), Block.Properties.of(Material.METAL).strength(3.0F, 4.5F).noDrops()));
	public final RegistryObject<Block> TUBE = register("cruxtruder_tube", () -> new CruxtruderBlock(this, CRUXTRUDER_TUBE, true, new BlockPos(0, 0, 0), Block.Properties.of(Material.METAL).strength(3.0F, 4.5F).noDrops()));
	
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
		tubePlacement = registerPlacement(new BlockPos(1, 1, 1), applyDirection(TUBE, Direction.NORTH));
		//noinspection Convert2MethodRef
		registerPlacement(new BlockPos(1, 2, 1), () -> MSBlocks.CRUXTRUDER_LID.get().defaultBlockState(), (state1, state2) -> true);
	}
	
	public BlockPos getBEPos(BlockPos placementPos, Rotation rotation)
	{
		return tubePlacement.getPos(placementPos, rotation);
	}
}