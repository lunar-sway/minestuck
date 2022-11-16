package com.mraof.minestuck.block.machine;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class MachineMultiblock implements ItemLike    //An abstraction for large machines that might be expanded upon in the future
{
	//No placed state or states are identical
	public static final BiPredicate<BlockState, BlockState> BASE_PREDICATE = (state1, state2) -> state1 == null || state1.equals(state2);
	//The above or states has the same block and direction
	public static final BiPredicate<BlockState, BlockState> DEFAULT_PREDICATE = BASE_PREDICATE.or((state1, state2) -> state2 != null && state1.getBlock() == state2.getBlock() && state1.getValue(MachineBlock.FACING) == state2.getValue(MachineBlock.FACING));
	
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
	
	protected PlacementEntry registerPlacement(BlockPos pos, Supplier<BlockState> stateSupplier)
	{
		return registerPlacement(pos, stateSupplier, DEFAULT_PREDICATE);
	}
	
	protected PlacementEntry registerPlacement(BlockPos pos, Supplier<BlockState> stateSupplier, BiPredicate<BlockState, BlockState> stateValidator)
	{
		for(PlacementEntry entry : blockEntries)
			if(entry.pos.equals(pos))
				throw new IllegalArgumentException("Can't add placement for the same position " + pos + " twice.");
		
		PlacementEntry entry = new PlacementEntry(stateSupplier, stateValidator, pos);
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
	
	public void placeWithRotation(LevelAccessor level, BlockPos pos, Rotation rotation)
	{
		blockEntries.forEach(entry -> entry.placeWithRotation(level, pos, rotation));
	}
	
	private boolean isInvalid(BlockGetter level, BlockPos pos, Rotation rotation)
	{
		for(PlacementEntry entry : blockEntries)
			if(!entry.matchesWithRotation(level, pos, rotation))
				return true;
		return false;
	}
	
	protected boolean isInvalidFromPlacement(BlockGetter level, BlockPos pos, PlacementEntry entry)
	{
		BlockState worldState = level.getBlockState(pos);
		Rotation rotation = entry.findRotation(worldState);
		BlockPos zeroPos = entry.getZeroPos(pos, rotation);
		return isInvalid(level, zeroPos, rotation);
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
	
	protected static class PlacementEntry
	{
		@Nonnull
		private final Supplier<BlockState> stateSupplier;
		@Nullable
		private final BiPredicate<BlockState, BlockState> stateValidator;
		private final BlockPos pos;
		
		private PlacementEntry(Supplier<BlockState> stateSupplier, @Nullable BiPredicate<BlockState, BlockState> stateValidator, BlockPos pos)
		{
			this.stateSupplier = Objects.requireNonNull(stateSupplier);
			this.stateValidator = stateValidator;
			this.pos = pos;
		}
		
		private BlockState getRotatedState(Rotation rotation)
		{
			BlockState state = stateSupplier.get();
			if(state != null)
				state = state.rotate(rotation);
			return state;
		}
		
		private void placeWithRotation(LevelAccessor level, BlockPos pos, Rotation rotation)
		{
			BlockState state = getRotatedState(rotation);
			if(state != null)
				level.setBlock(pos.offset(this.pos.rotate(rotation)), state, Block.UPDATE_ALL);
		}
		
		private boolean matchesWithRotation(BlockGetter level, BlockPos pos, Rotation rotation)
		{
			BlockState machineState = getRotatedState(rotation);
			
			if(stateValidator != null)
			{
				BlockState worldState = level.getBlockState(pos.offset(this.pos.rotate(rotation)));
				return stateValidator.test(machineState, worldState);
			} else return true;
		}
		
		public BlockPos getZeroPos(BlockPos pos, BlockState rotatedState)
		{
			return getZeroPos(pos, findRotation(rotatedState));
		}
		
		public BlockPos getPos(BlockPos pos, Rotation rotation)
		{
			return pos.offset(this.pos.rotate(rotation));
		}
		
		public BlockPos getZeroPos(BlockPos pos, Rotation rotation)
		{
			return pos.subtract(this.pos.rotate(rotation));
		}
		
		public Rotation findRotation(BlockState rotatedState)
		{
			BlockState defaultState = stateSupplier.get();
			if(defaultState != null)
			{
				if(stateValidator != null)
				{
					for(Rotation rotation : Rotation.values())
						if(stateValidator.test(defaultState.rotate(rotation), rotatedState))
							return rotation;
				} else return Rotation.NONE;
			}
			throw new IllegalArgumentException("No valid rotation found to match state "+rotatedState+" with "+defaultState);
		}
	}
	
	protected static Supplier<BlockState> applyDirection(RegistryObject<Block> regBlock, Direction direction)
	{
		return regBlock.lazyMap(block -> block.defaultBlockState().setValue(MachineBlock.FACING, direction));
	}
}