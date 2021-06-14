package com.mraof.minestuck.block.machine;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.util.Direction;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IWorld;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class MachineMultiblock implements IItemProvider    //An abstraction for large machines that might be expanded upon in the future
{
	//No placed state or states are identical
	public static final BiPredicate<BlockState, BlockState> BASE_PREDICATE = (state1, state2) -> state1 == null || state1.equals(state2);
	//The above or states has the same block and direction
	public static final BiPredicate<BlockState, BlockState> DEFAULT_PREDICATE = BASE_PREDICATE.or((state1, state2) -> state2 != null && state1.getBlock() == state2.getBlock() && state1.getValue(MachineBlock.FACING) == state2.getValue(MachineBlock.FACING));
	
	private final String modId;
	private final Map<RegistryObject<Block>, Supplier<? extends Block>> registryEntries = new LinkedHashMap<>();
	private final List<PlacementEntry> blockEntries = new ArrayList<>();
	
	protected MachineMultiblock(String modId)
	{
		this.modId = modId;
	}
	
	@SuppressWarnings("unchecked")
	protected <B extends Block> RegistryObject<B> register(String name, Supplier<B> constructor)
	{
		ResourceLocation key = new ResourceLocation(modId, name);
		RegistryObject<B> obj = RegistryObject.of(key, ForgeRegistries.BLOCKS);
		if(registryEntries.putIfAbsent((RegistryObject<Block>) obj, constructor) != null)
			throw new IllegalArgumentException("Can't register "+name+" twice");
		return obj;
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
		return !registryEntries.isEmpty() ? registryEntries.keySet().iterator().next().get() : null;
	}
	
	public void forEachBlock(Consumer<Block> consumer)
	{
		registryEntries.keySet().forEach(blockRegistryObject -> blockRegistryObject.ifPresent(consumer));
	}
	
	public void placeWithRotation(IWorld world, BlockPos pos, Rotation rotation)
	{
		blockEntries.forEach(entry -> entry.placeWithRotation(world, pos, rotation));
	}
	
	private boolean isInvalid(IWorld world, BlockPos pos, Rotation rotation)
	{
		for(PlacementEntry entry : blockEntries)
			if(!entry.matchesWithRotation(world, pos, rotation))
				return true;
		return false;
	}
	
	protected boolean isInvalidFromPlacement(IWorld world, BlockPos pos, PlacementEntry entry)
	{
		BlockState worldState = world.getBlockState(pos);
		Rotation rotation = entry.findRotation(worldState);
		BlockPos zeroPos = entry.getZeroPos(pos, rotation);
		return isInvalid(world, zeroPos, rotation);
	}
	
	public MutableBoundingBox getBoundingBox(Rotation rotation)
	{
		MutableBoundingBox bb = new MutableBoundingBox();
		blockEntries.forEach(entry -> bb.expand(new MutableBoundingBox(entry.pos.rotate(rotation), entry.pos.rotate(rotation))));
		return bb;
	}
	
	public void registerBlocks(IForgeRegistry<Block> registry)
	{
		for(Map.Entry<RegistryObject<Block>, Supplier<? extends Block>> entry : registryEntries.entrySet())
		{
			registry.register(entry.getValue().get().setRegistryName(entry.getKey().getId()));
			entry.getKey().updateReference(registry);
		}
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
		
		private void placeWithRotation(IWorld world, BlockPos pos, Rotation rotation)
		{
			BlockState state = getRotatedState(rotation);
			if(state != null)
				world.setBlock(pos.offset(this.pos.rotate(rotation)), state, Constants.BlockFlags.DEFAULT);
		}
		
		private boolean matchesWithRotation(IWorld world, BlockPos pos, Rotation rotation)
		{
			BlockState machineState = getRotatedState(rotation);
			
			if(stateValidator != null)
			{
				BlockState worldState = world.getBlockState(pos.offset(this.pos.rotate(rotation)));
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