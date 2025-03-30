package com.mraof.minestuck.block.machine;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class MachineMultiblock implements ItemLike    //An abstraction for large machines that might be expanded upon in the future
{
	//No placed state or states are identical
	public static final BiPredicate<BlockState, BlockState> BASE_PREDICATE = (state1, state2) -> state1.getBlock() == state2.getBlock();
	//The above or states has the same block and direction
	public static final BiPredicate<BlockState, BlockState> ROTATION_PREDICATE = BASE_PREDICATE.and((state1, state2) -> state1.getValue(MachineBlock.FACING) == state2.getValue(MachineBlock.FACING));
	
	private final DeferredRegister.Blocks register;
	private final List<Supplier<? extends Block>> registryEntries = new ArrayList<>();
	private final List<PlacementEntry> blockEntries = new ArrayList<>();
	
	protected MachineMultiblock(DeferredRegister.Blocks register)
	{
		this.register = register;
	}
	
	protected <B extends Block> DeferredBlock<B> register(String name, Supplier<B> constructor)
	{
		DeferredBlock<B> registryObject = register.register(name, constructor);
		registryEntries.add(registryObject);
		return registryObject;
	}
	
	/**
	 * Registers a placed block at the given coordinates.
	 * This block will then be placed by {@link #placeWithRotation(LevelAccessor, Placement)},
	 * and can then be removed by {@link #removeAt(LevelAccessor, Placement)}.
	 * Meant for undirectional blocks which lacks a FACING property.
	 * {@link PlacementEntry#findPlacement(BlockPos, BlockState)} will not work for the returned entry.
	 * @param mustExist if true, then {@link #isInvalidFromPlacement(BlockGetter, BlockPos, PlacementEntry)} will return true if this block is missing.
	 */
	@SuppressWarnings({"SameParameterValue", "UnusedReturnValue"})
	protected PlacementEntry addPlacement(int x, int y, int z, Supplier<BlockState> stateSupplier, boolean mustExist)
	{
		return addPlacement(new BlockPos(x, y, z), stateSupplier, mustExist, false, true, BASE_PREDICATE);
	}
	
	/**
	 * Registers a placed block with the given direction at the given coordinates.
	 * This block will then be placed by {@link #placeWithRotation(LevelAccessor, Placement)},
	 * and can then be removed by {@link #removeAt(LevelAccessor, Placement)}.
	 * Should only be used for blocks which has a FACING property.
	 * Assumes that the block must exist for a valid machine,
	 * so {@link #isInvalidFromPlacement(BlockGetter, BlockPos, PlacementEntry)} will return true if this block is missing.
	 */
	protected PlacementEntry addDirectionPlacement(int x, int y, int z, Supplier<Block> regBlock, Direction direction)
	{
		return addPlacement(new BlockPos(x, y, z), applyDirection(regBlock, direction), true, true, true, ROTATION_PREDICATE);
	}
	
	/**
	 * Registers a placed block with the given direction at the given coordinates.
	 * This block will NOT be placed by {@link #placeWithRotation(LevelAccessor, Placement)},
	 * however it can be removed by {@link #removeAt(LevelAccessor, Placement)}.
	 * Should only be used for blocks which has a FACING property.
	 * Assumes that the block is not needed for a valid machine,
	 * so {@link #isInvalidFromPlacement(BlockGetter, BlockPos, PlacementEntry)} will ignore this block.
	 */
	@SuppressWarnings("SameParameterValue")
	protected PlacementEntry addDirectionOptional(int x, int y, int z, Supplier<Block> regBlock, Direction direction)
	{
		return addPlacement(new BlockPos(x, y, z), applyDirection(regBlock, direction), false, true, false, ROTATION_PREDICATE);
	}
	
	protected PlacementEntry addPlacement(BlockPos pos, Supplier<BlockState> stateSupplier, boolean mustExist, boolean isDirectional, boolean isPlaced, BiPredicate<BlockState, BlockState> stateValidator)
	{
		for(PlacementEntry entry : blockEntries)
			if(entry.pos.equals(pos))
				throw new IllegalArgumentException("Can't add placement for the same position " + pos + " twice.");
		
		PlacementEntry entry = new PlacementEntry(stateSupplier, stateValidator, mustExist, isDirectional, isPlaced, pos);
		blockEntries.add(entry);
		return entry;
	}
	
	public Block getMainBlock()
	{
		return !registryEntries.isEmpty() ? registryEntries.get(0).get() : null;
	}
	
	public void forEachBlock(Consumer<Block> consumer)
	{
		registryEntries.forEach(block -> consumer.accept(block.get()));
	}
	
	public void placeWithRotation(LevelAccessor level, Placement placement)
	{
		blockEntries.forEach(entry -> entry.placeWithRotation(level, placement));
	}
	
	/**
	 * Additional code to run after the multiblock has been placed.
	 */
	public void placeAdditional(Level level, Placement placement)
	{
	
	}
	
	private boolean isInvalid(BlockGetter level, Placement placement)
	{
		for(PlacementEntry entry : blockEntries)
			if(entry.mustExist && !entry.matchesWithRotation(level, entry.getPos(placement), placement.rotation))
				return true;
		return false;
	}
	
	/**
	 * Checks if the machine is valid or not, based on a given position and its matching placement entry.
	 * @return true if the machine is not valid i.e. missing some important block. False otherwise.
	 */
	protected boolean isInvalidFromPlacement(BlockGetter level, BlockPos pos, PlacementEntry entry)
	{
		BlockState worldState = level.getBlockState(pos);
		return isInvalid(level, entry.findPlacementOrThrow(pos, worldState));
	}
	
	public List<Placement> guessPlacement(BlockPos pos, BlockState state)
	{
		List<Placement> placements = new ArrayList<>();
		for(PlacementEntry entry : this.blockEntries)
			entry.findPlacement(pos, state).ifPresent(placements::add);
		
		return placements;
	}
	
	public void removeAt(LevelAccessor level, Placement placement)
	{
		for(PlacementEntry entry : blockEntries)
			entry.removeIfMatching(level, placement);
	}
	
	public BoundingBox getBoundingBox(Rotation rotation)
	{
		return BoundingBox.encapsulatingPositions(blockEntries.stream().map(entry -> entry.pos.rotate(rotation)).toList()).orElseThrow();
	}
	
	@Override
	@Nonnull
	public Item asItem()
	{
		return getMainBlock().asItem();
	}
	
	/**
	 * Represents the placement of one block in the machine.
	 */
	protected static class PlacementEntry
	{
		@Nonnull
		private final Supplier<BlockState> stateSupplier;
		private final BiPredicate<BlockState, BlockState> stateValidator;
		private final boolean mustExist, isDirectional, isPlaced;
		private final BlockPos pos;
		
		private PlacementEntry(Supplier<BlockState> stateSupplier, BiPredicate<BlockState, BlockState> stateValidator, boolean mustExist, boolean isDirectional, boolean isPlaced, BlockPos pos)
		{
			this.stateSupplier = Objects.requireNonNull(stateSupplier);
			this.stateValidator = Objects.requireNonNull(stateValidator);
			this.mustExist = mustExist;
			this.isDirectional = isDirectional;
			this.isPlaced = isPlaced;
			this.pos = pos;
		}
		
		private BlockState getRotatedState(Rotation rotation)
		{
			return Objects.requireNonNull(stateSupplier.get()).rotate(rotation);
		}
		
		private void placeWithRotation(LevelAccessor level, Placement placement)
		{
			if(this.isPlaced)
			{
				BlockState state = this.getRotatedState(placement.rotation);
				level.setBlock(this.getPos(placement), state, Block.UPDATE_ALL);
			}
		}
		
		private boolean matchesWithRotation(BlockGetter level, BlockPos pos, Rotation rotation)
		{
			BlockState machineState = this.getRotatedState(rotation);
			BlockState worldState = level.getBlockState(pos);
			return stateValidator.test(machineState, worldState);
		}
		
		private void removeIfMatching(LevelAccessor level, Placement placement)
		{
			BlockPos pos = this.getPos(placement);
			if(matchesWithRotation(level, pos, placement.rotation))
				level.destroyBlock(pos, false);
		}
		
		/**
		 * Calculates the position for this entry based on the placement of the machine.
		 */
		public BlockPos getPos(Placement placement)
		{
			return placement.zeroPos.offset(this.pos.rotate(placement.rotation));
		}
		
		/**
		 * Calculates the placement of the machine given the position and rotation of the block corresponding this entry.
		 */
		public Placement getPlacement(BlockPos pos, Rotation rotation)
		{
			return new Placement(pos.subtract(this.pos.rotate(rotation)), rotation);
		}
		
		/**
		 * Checks possible rotations of the block and returns the placement for the rotation that matches.
		 */
		public Optional<Placement> findPlacement(BlockPos pos, BlockState rotatedState)
		{
			if(!this.isDirectional)
				return Optional.empty();
			
			for(Rotation rotation : Rotation.values())
				if(stateValidator.test(getRotatedState(rotation), rotatedState))
					return Optional.of(this.getPlacement(pos, rotation));
			return Optional.empty();
		}
		
		public Placement findPlacementOrThrow(BlockPos pos, BlockState rotatedState)
		{
			return this.findPlacement(pos, rotatedState).orElseThrow(() ->
					new IllegalArgumentException("No valid rotation found to match state " + rotatedState + " with " + stateSupplier.get()));
		}
	}
	
	/**
	 * Represents how a machine might be placed in a level.
	 */
	public record Placement(BlockPos zeroPos, Rotation rotation)
	{}
	
	protected static Supplier<BlockState> applyDirection(Supplier<Block> block, Direction direction)
	{
		return () -> block.get().defaultBlockState().setValue(MachineBlock.FACING, direction);
	}
}