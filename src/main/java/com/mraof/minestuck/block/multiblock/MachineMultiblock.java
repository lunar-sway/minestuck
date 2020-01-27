package com.mraof.minestuck.block.multiblock;

import com.mraof.minestuck.block.MachineBlock;
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
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class MachineMultiblock implements IItemProvider    //An abstraction for large machines that might be expanded upon in the future
{
	//No placed state or states are identical
	public static final BiPredicate<BlockState, BlockState> BASE_PREDICATE = (state1, state2) -> state1 == null || state1.equals(state2);
	//The above or states has the same block and direction
	public static final BiPredicate<BlockState, BlockState> DEFAULT_PREDICATE = BASE_PREDICATE.or((state1, state2) -> state2 != null && state1.getBlock() == state2.getBlock() && state1.get(MachineBlock.FACING) == state2.get(MachineBlock.FACING));
	
	private final String modId;
	private final Map<RegistryObject<Block>, Supplier<? extends Block>> registryEntries = new LinkedHashMap<>();
	private final List<Entry> blockEntries = new ArrayList<>();
	
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
	
	protected void registerPlacement(BlockPos pos, Supplier<BlockState> stateSupplier)
	{
		registerPlacement(pos, stateSupplier, DEFAULT_PREDICATE);
	}
	
	protected void registerPlacement(BlockPos pos, Supplier<BlockState> stateSupplier, BiPredicate<BlockState, BlockState> stateValidator)
	{
		//TODO add check to prevent duplicate positions
		blockEntries.add(new Entry(stateSupplier, stateValidator, pos));
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
	
	public boolean isInvalid(IWorld world, BlockPos pos, Rotation rotation)
	{
		for(Entry entry : blockEntries)
			if(!entry.matchesWithRotation(world, pos, rotation))
				return true;
		return false;
	}
	
	public MutableBoundingBox getBoundingBox()
	{
		MutableBoundingBox bb = new MutableBoundingBox();
		blockEntries.forEach(entry -> bb.expandTo(new MutableBoundingBox(entry.pos, entry.pos)));
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
	
	private static class Entry
	{
		private final Supplier<BlockState> stateSupplier;
		private final BiPredicate<BlockState, BlockState> stateValidator;
		private final BlockPos pos;
		
		private Entry(Supplier<BlockState> stateSupplier, BiPredicate<BlockState, BlockState> stateValidator, BlockPos pos)
		{
			this.stateSupplier = stateSupplier;
			this.stateValidator = stateValidator;
			this.pos = pos;
		}
		
		private void placeWithRotation(IWorld world, BlockPos pos, Rotation rotation)
		{
			BlockState state = stateSupplier.get();
			if(state != null)
				world.setBlockState(pos.add(this.pos.rotate(rotation)), state.rotate(rotation), 11);
		}
		
		private boolean matchesWithRotation(IWorld world, BlockPos pos, Rotation rotation)
		{
			BlockState state = stateSupplier.get();
			if(stateValidator != null)
				return stateValidator.test(state, world.getBlockState(pos.add(this.pos.rotate(rotation))));
			else return true;
		}
	}
	
	protected static Supplier<BlockState> applyDirection(RegistryObject<Block> regBlock, Direction direction)
	{
		return regBlock.lazyMap(block -> block.getDefaultState().with(MachineBlock.FACING, direction));
	}
}