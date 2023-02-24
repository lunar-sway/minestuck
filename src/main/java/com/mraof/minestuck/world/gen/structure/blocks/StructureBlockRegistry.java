package com.mraof.minestuck.world.gen.structure.blocks;

import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.world.gen.LandChunkGenerator;
import net.minecraft.core.Direction;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockStateMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public final class StructureBlockRegistry
{
	
	private static final Map<String, BlockEntry> staticRegistry = new HashMap<>();
	private static final Map<Block, String> templateBlockMap = new HashMap<>();
	private static final StructureBlockRegistry defaultRegistry = new StructureBlockRegistry();
	
	public static void registerBlock(String name, BlockState defaultBlock)
	{
		registerBlock(name, defaultBlock, Block.class);
	}
	
	//TODO With async modloading, perhaps we should synchronize this in some way?
	public static void registerBlock(String name, BlockState defaultBlock, Class<? extends Block> extention)
	{
		if(defaultBlock == null || name == null)
			throw new IllegalArgumentException("Null parameters not allowed.");
		if(staticRegistry.containsKey(name))
			throw new IllegalStateException("\""+name+"\" has already been registered!");
		if(!extention.isInstance(defaultBlock.getBlock()))
			throw new IllegalArgumentException("The default block \""+defaultBlock.getBlock()+"\" has to extend the minimum class \""+extention+"\"!");
		if(templateBlockMap.containsKey(defaultBlock.getBlock()))
			throw new IllegalStateException("Can't have two identical template blocks!");
		
		staticRegistry.put(name, new BlockEntry(defaultBlock, extention));
		templateBlockMap.put(defaultBlock.getBlock(), name);
	}
	
	public static void registerBlock(String name, String parent, Block templateState)
	{
		registerBlock(name, parent, templateState, Block.class);
	}
	
	public static void registerBlock(String name, String parent, Block templateState, Class<? extends Block> extention)
	{
		if(parent == null || name == null)
			throw new IllegalArgumentException("Null parameters not allowed.");
		if(staticRegistry.containsKey(name))
			throw new IllegalStateException("\""+name+"\" has already been registered!");
		
		if(!staticRegistry.containsKey(parent))
			throw new IllegalStateException("The parent entry \""+parent+"\" isn't registered! Make sure to register the parent first.");
		
		if(!extention.isAssignableFrom(staticRegistry.get(parent).extention))
			throw new IllegalArgumentException("The class specified must be the same or a superclass to the class used by the parent \""+parent+"\".");
		if(templateBlockMap.containsKey(templateState))
			throw new IllegalStateException("Can't have two identical template blocks!");
		
		staticRegistry.put(name, new BlockEntry(parent, extention));
		templateBlockMap.put(templateState, name);
	}
	
	static	//TODO Use public static final Strings as a standard for names to reduce risk of typos
	{
		registerBlock("cruxite_ore", MSBlocks.STONE_CRUXITE_ORE.get().defaultBlockState());
		registerBlock("uranium_ore", MSBlocks.STONE_URANIUM_ORE.get().defaultBlockState());
		registerBlock("ground", Blocks.STONE.defaultBlockState());
		registerBlock("upper", "ground", Blocks.DIRT);
		registerBlock("surface", "upper", Blocks.GRASS_BLOCK);
		registerBlock("surface_rough", "surface", Blocks.PODZOL);
		registerBlock("ocean", Blocks.WATER.defaultBlockState());
		registerBlock("ocean_surface", "upper", Blocks.GRAVEL);
		registerBlock("river", "ocean", Blocks.BLUE_WOOL);
		registerBlock("sand", Blocks.SAND.defaultBlockState());
		registerBlock("structure_primary", Blocks.STONE_BRICKS.defaultBlockState());
		registerBlock("structure_primary_decorative", "structure_primary", Blocks.CHISELED_STONE_BRICKS);
		registerBlock("structure_primary_cracked", "structure_primary", Blocks.CRACKED_STONE_BRICKS);
		registerBlock("structure_primary_mossy", "structure_primary", Blocks.MOSSY_STONE_BRICKS);
		registerBlock("structure_primary_column", "structure_primary", MSBlocks.COARSE_STONE_COLUMN.get());
		registerBlock("structure_primary_stairs", "structure_primary", Blocks.STONE_BRICK_STAIRS);
		registerBlock("structure_secondary", "structure_primary", Blocks.NETHER_BRICKS);
		registerBlock("structure_secondary_decorative", "structure_secondary", Blocks.RED_NETHER_BRICKS);
		registerBlock("structure_secondary_stairs", "structure_secondary", Blocks.NETHER_BRICK_STAIRS);
		registerBlock("structure_planks", Blocks.OAK_PLANKS.defaultBlockState());
		registerBlock("structure_planks_slab", Blocks.OAK_SLAB.defaultBlockState(), SlabBlock.class);
		registerBlock("structure_wool_1", Blocks.WHITE_WOOL.defaultBlockState());
		registerBlock("structure_wool_2", Blocks.LIGHT_GRAY_WOOL.defaultBlockState());
		registerBlock("structure_wool_3", Blocks.GRAY_WOOL.defaultBlockState());
		registerBlock("carpet", Blocks.WHITE_CARPET.defaultBlockState());
		registerBlock("village_door", Blocks.OAK_DOOR.defaultBlockState(), DoorBlock.class);
		registerBlock("salamander_floor", "upper", Blocks.COARSE_DIRT);
		registerBlock("village_path", "structure_secondary", Blocks.COBBLESTONE);
		registerBlock("village_fence", Blocks.OAK_FENCE.defaultBlockState());
		registerBlock("fall_fluid", "ocean", Blocks.LIGHT_BLUE_WOOL);
		registerBlock("light_block", Blocks.GLOWSTONE.defaultBlockState());
		registerBlock("mushroom_1", Blocks.RED_MUSHROOM.defaultBlockState());
		registerBlock("mushroom_2", Blocks.BROWN_MUSHROOM.defaultBlockState());
		registerBlock("bush", Blocks.DEAD_BUSH.defaultBlockState());
		registerBlock("torch", Blocks.TORCH.defaultBlockState());
		registerBlock("wall_torch", Blocks.WALL_TORCH.defaultBlockState());
		registerBlock("bucket_1", Blocks.QUARTZ_BLOCK.defaultBlockState());
		registerBlock("bucket_2", Blocks.IRON_BLOCK.defaultBlockState());
		registerBlock("glass", Blocks.GLASS.defaultBlockState());
		registerBlock("stained_glass_1", Blocks.GRAY_STAINED_GLASS.defaultBlockState());
		registerBlock("stained_glass_2", Blocks.LIGHT_GRAY_STAINED_GLASS.defaultBlockState());
		registerBlock("slime", Blocks.SLIME_BLOCK.defaultBlockState());
		
		defaultRegistry.setBlock("surface", Blocks.GRASS_BLOCK);
		defaultRegistry.setBlock("upper", Blocks.DIRT);
		defaultRegistry.setBlock("ocean_surface", Blocks.GRAVEL);
	}
	
	public static StructureBlockRegistry getOrDefault(ChunkGenerator generator)
	{
		if(generator instanceof LandChunkGenerator)
			return ((LandChunkGenerator) generator).blockRegistry;
		else return defaultRegistry;
	}
	
	private static class BlockEntry
	{
		//TODO define restriction with a set of block state properties instead of base class
		Class<? extends Block> extention;
		BlockState defaultBlock;
		String parentEntry;
		BlockEntry(BlockState state, Class<? extends Block> clazz)
		{
			defaultBlock = state;
			extention = clazz;
		}
		BlockEntry(String str, Class<? extends Block> clazz)
		{
			parentEntry = str;
			extention = clazz;
		}
		
		BlockState getBlockState(StructureBlockRegistry registry)
		{
			if(parentEntry != null)
				return registry.getBlockState(parentEntry);
			else return defaultBlock;
		}
	}
	
	//Nonstatic stuff
	private final Map<String, BlockState> blockRegistry = new HashMap<>();
	private RuleTest groundType = OreFeatures.NATURAL_STONE;
	
	public void setBlock(String name, Supplier<? extends Block> block)
	{
		setBlock(name, block.get());
	}
	
	public void setBlock(String name, Block block)
	{
		setBlockState(name, block.defaultBlockState());
	}
	
	public void setBlockState(String name, BlockState state)
	{
		if(state == null || name == null)
			throw new NullPointerException("Null parameters not allowed.");
		if(!staticRegistry.containsKey(name))
			throw new IllegalStateException("Structure block \""+name+"\" isn't registered, and can therefore not be set.");
		if(!staticRegistry.get(name).extention.isInstance(state.getBlock()))
			throw new IllegalArgumentException("The provided block must extend \""+staticRegistry.get(name).extention+"\".");
		
		blockRegistry.put(name, state);
		
		if(name.equals("ground"))
			groundType = new BlockStateMatchTest(state);
	}
	
	public BlockState getBlockState(String name)
	{
		if(name == null)
			throw new IllegalArgumentException("Null parameters not allowed.");
		if(!staticRegistry.containsKey(name))
			throw new IllegalStateException("Structure block \""+name+"\" isn't registered, and can therefore not be obtained.");
		
		if(blockRegistry.containsKey(name))
			return blockRegistry.get(name);
		else return staticRegistry.get(name).getBlockState(this);
	}
	
	public boolean isUsingDefault(String name)
	{
		return blockRegistry.get(name) == null;
	}
	
	public BlockState getStairs(String name, Direction facing, boolean upsideDown)
	{
		BlockState state = getBlockState(name);
		
		state = withOptionally(state, BlockStateProperties.HORIZONTAL_FACING, facing);
		
		if(upsideDown)
			state = withOptionally(state, BlockStateProperties.HALF, Half.TOP);
		
		return state;
	}
	
	public RuleTest getGroundType()
	{
		return groundType;
	}
	
	public BlockState getTemplateState(BlockState state)
	{
		if(templateBlockMap.containsKey(state.getBlock()))
		{
			BlockState newState = getBlockState(templateBlockMap.get(state.getBlock()));
			for(Property<?> property : state.getProperties())
				newState = with(state, newState, property);
			return newState;
		} else return state;
	}
	
	private static <T extends Comparable<T>> BlockState with(BlockState fromState, BlockState toState, Property<T> property)
	{
		if(toState.hasProperty(property))
			return toState.setValue(property, fromState.getValue(property));
		else return toState;
	}
	
	public static <T extends Comparable<T>> BlockState withOptionally(BlockState state, Property<T> property, T value)
	{
		if(state.hasProperty(property))
		{
			state = state.setValue(property, value);
		}
		return state;
	}
}