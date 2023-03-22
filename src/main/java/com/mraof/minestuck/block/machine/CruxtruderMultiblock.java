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

import java.util.Optional;

import static com.mraof.minestuck.block.MSBlockShapes.*;

public class CruxtruderMultiblock extends MachineMultiblock
{
	public final RegistryObject<Block> CORNER = register("cruxtruder_corner", () -> new CruxtruderBlock(this, CRUXTRUDER_BASE_CORNER, false, new BlockPos(1, 1, 1), Block.Properties.of(Material.METAL).strength(3.0F, 4.5F).noLootTable()));
	public final RegistryObject<Block> SIDE = register("cruxtruder_side", () -> new CruxtruderBlock(this, CRUXTRUDER_BASE_SIDE, false, new BlockPos(0, 1, 1), Block.Properties.of(Material.METAL).strength(3.0F, 4.5F).noLootTable()));
	public final RegistryObject<Block> CENTER = register("cruxtruder_center", () -> new CruxtruderBlock(this, CRUXTRUDER_CENTER, false, new BlockPos(0, 1, 0), Block.Properties.of(Material.METAL).strength(3.0F, 4.5F).noLootTable()));
	public final RegistryObject<Block> TUBE = register("cruxtruder_tube", () -> new CruxtruderBlock(this, CRUXTRUDER_TUBE, true, new BlockPos(0, 0, 0), Block.Properties.of(Material.METAL).strength(3.0F, 4.5F).noLootTable()));
	
	private final PlacementEntry tubePlacement;
	
	public CruxtruderMultiblock(DeferredRegister<Block> register)
	{
		super(register);
		addDirectionPlacement(0, 0, 0, CORNER, Direction.NORTH);
		addDirectionPlacement(1, 0, 0, SIDE, Direction.NORTH);
		addDirectionPlacement(2, 0, 0, CORNER, Direction.EAST);
		addDirectionPlacement(2, 0, 1, SIDE, Direction.EAST);
		addDirectionPlacement(2, 0, 2, CORNER, Direction.SOUTH);
		addDirectionPlacement(1, 0, 2, SIDE, Direction.SOUTH);
		addDirectionPlacement(0, 0, 2, CORNER, Direction.WEST);
		addDirectionPlacement(0, 0, 1, SIDE, Direction.WEST);
		addDirectionPlacement(1, 0, 1, CENTER, Direction.NORTH);
		tubePlacement = addDirectionPlacement(1, 1, 1, TUBE, Direction.NORTH);
		addPlacement(1, 2, 1, MSBlocks.CRUXTRUDER_LID.lazyMap(Block::defaultBlockState), false);
	}
	
	public BlockPos getBEPos(BlockPos placementPos, Rotation rotation)
	{
		return tubePlacement.getPos(new Placement(placementPos, rotation));
	}
	
	public Optional<Placement> findPlacementFromTube(LevelAccessor level, BlockPos pos)
	{
		return tubePlacement.findPlacement(pos, level.getBlockState(pos));
	}
	
	public boolean isInvalidFromTube(LevelAccessor level, BlockPos pos)
	{
		return this.isInvalidFromPlacement(level, pos, this.tubePlacement);
	}
}