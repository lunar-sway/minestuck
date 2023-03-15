package com.mraof.minestuck.block.machine;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class MachineMultiblock implements ItemLike    //An abstraction for large machines that might be expanded upon in the future
{
	//No placed state or states are identical
	public static final BiPredicate<BlockState, BlockState> BASE_PREDICATE = (state1, state2) -> state1.getBlock() == state2.getBlock();
	//The above or states has the same block and direction
	public static final BiPredicate<BlockState, BlockState> ROTATION_PREDICATE = BASE_PREDICATE.and((state1, state2) -> state1.getValue(MachineBlock.FACING) == state2.getValue(MachineBlock.FACING));
	
	private final DeferredRegister<Block> register;
	private final Set<RegistryObject<? extends Block>> registryEntries = new HashSet<>();
	private final List<PlacementEntry> blockEntries = new ArrayList<>();
	
	protected MachineMultiblock(DeferredRegister<Block> register)
	{
		this.register = register;
	}
	
	protected <B extends Block> RegistryObject<B> register(String name, Supplier<B> constructor)
	{
		RegistryObject<B> registryObject = register.register(name, constructor);
		registryEntries.add(registryObject);
		return registryObject;
	}
	
	protected PlacementEntry addPlacement(BlockPos pos, Supplier<BlockState> stateSupplier, @SuppressWarnings("SameParameterValue") boolean mustExist)
	{
		return addPlacement(pos, stateSupplier, mustExist, BASE_PREDICATE);
	}
	
	protected PlacementEntry addDirectionPlacement(BlockPos pos, RegistryObject<Block> regBlock, Direction direction)
	{
		return addPlacement(pos, applyDirection(regBlock, direction), true, ROTATION_PREDICATE);
	}
	
	protected PlacementEntry addPlacement(BlockPos pos, Supplier<BlockState> stateSupplier, boolean mustExist, BiPredicate<BlockState, BlockState> stateValidator)
	{
		for(PlacementEntry entry : blockEntries)
			if(entry.pos.equals(pos))
				throw new IllegalArgumentException("Can't add placement for the same position " + pos + " twice.");
		
		PlacementEntry entry = new PlacementEntry(stateSupplier, stateValidator, mustExist, pos);
		blockEntries.add(entry);
		return entry;
	}
	
	public Block getMainBlock()
	{
		return !registryEntries.isEmpty() ? registryEntries.iterator().next().get() : null;
	}
	
	public void forEachBlock(Consumer<Block> consumer)
	{
		registryEntries.forEach(blockRegistryObject -> blockRegistryObject.ifPresent(consumer));
	}
	
	public void placeWithRotation(LevelAccessor level, Placement placement)
	{
		blockEntries.forEach(entry -> entry.placeWithRotation(level, placement));
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
		Rotation rotation = entry.findRotationOrThrow(worldState);
		return isInvalid(level, entry.getPlacement(pos, rotation));
	}
	
	protected void removeAt(LevelAccessor level, Placement placement)
	{
		for(PlacementEntry entry : blockEntries)
			entry.removeIfMatching(level, placement);
	}
	
	public BoundingBox getBoundingBox(Rotation rotation)
	{
		return BoundingBox.encapsulatingPositions(blockEntries.stream().map(entry -> entry.pos.rotate(rotation)).toList()).orElseThrow();
	}
	
	@Override
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
		private final boolean mustExist;
		private final BlockPos pos;
		
		private PlacementEntry(Supplier<BlockState> stateSupplier, BiPredicate<BlockState, BlockState> stateValidator, boolean mustExist, BlockPos pos)
		{
			this.stateSupplier = Objects.requireNonNull(stateSupplier);
			this.stateValidator = Objects.requireNonNull(stateValidator);
			this.mustExist = mustExist;
			this.pos = pos;
		}
		
		private BlockState getRotatedState(Rotation rotation)
		{
			return Objects.requireNonNull(stateSupplier.get()).rotate(rotation);
		}
		
		private void placeWithRotation(LevelAccessor level, Placement placement)
		{
			BlockState state = this.getRotatedState(placement.rotation);
			level.setBlock(this.getPos(placement), state, Block.UPDATE_ALL);
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
		 * Finds the rotation of the machine based on this entry and its corresponding in-world block state.
		 */
		public Optional<Rotation> findRotation(BlockState rotatedState)
		{
			for(Rotation rotation : Rotation.values())
				if(stateValidator.test(getRotatedState(rotation), rotatedState))
					return Optional.of(rotation);
			return Optional.empty();
		}
		
		public Rotation findRotationOrThrow(BlockState rotatedState)
		{
			return this.findRotation(rotatedState).orElseThrow(() ->
					new IllegalArgumentException("No valid rotation found to match state "+rotatedState+" with "+stateSupplier.get()));
		}
	}
	
	/**
	 * Represents how a machine might be placed in a level.
	 */
	public record Placement(BlockPos zeroPos, Rotation rotation)
	{}
	
	protected static Supplier<BlockState> applyDirection(RegistryObject<Block> regBlock, Direction direction)
	{
		return regBlock.lazyMap(block -> block.defaultBlockState().setValue(MachineBlock.FACING, direction));
	}
}