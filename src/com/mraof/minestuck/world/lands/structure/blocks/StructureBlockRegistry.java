package com.mraof.minestuck.world.lands.structure.blocks;

import com.mraof.minestuck.world.gen.LandGenSettings;
import net.minecraft.block.*;
import net.minecraft.state.IProperty;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.Half;
import net.minecraft.util.Direction;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.feature.OreFeatureConfig;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class StructureBlockRegistry
{
	
	private static Map<String, BlockEntry> staticRegistry = new HashMap<>();
	private static Map<Block, String> templateBlockMap = new HashMap<>();
	private static StructureBlockRegistry defaultRegistry = new StructureBlockRegistry();
	
	public static void registerBlock(String name, BlockState defaultBlock)
	{
		registerBlock(name, defaultBlock, Block.class);
	}
	
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
	
	static
	{
		registerBlock("ground", Blocks.STONE.getDefaultState());
		registerBlock("upper", "ground", Blocks.DIRT);
		registerBlock("surface", "upper", Blocks.GRASS_BLOCK);
		registerBlock("ocean", Blocks.WATER.getDefaultState());
		registerBlock("ocean_surface", "upper", Blocks.GRAVEL);
		registerBlock("river", "ocean", Blocks.BLUE_WOOL);
		registerBlock("sand", Blocks.SAND.getDefaultState());
		registerBlock("structure_primary", Blocks.STONE_BRICKS.getDefaultState());
		registerBlock("structure_primary_decorative", "structure_primary", Blocks.CHISELED_STONE_BRICKS);
		registerBlock("structure_primary_stairs", "structure_primary", Blocks.STONE_BRICK_STAIRS);
		registerBlock("structure_secondary", "structure_primary", Blocks.NETHER_BRICKS);
		registerBlock("structure_secondary_decorative", "structure_secondary", Blocks.RED_NETHER_BRICKS);
		registerBlock("structure_secondary_stairs", "structure_secondary", Blocks.NETHER_BRICK_STAIRS);
		registerBlock("structure_planks", Blocks.OAK_PLANKS.getDefaultState());
		registerBlock("structure_planks_slab", Blocks.OAK_SLAB.getDefaultState(), SlabBlock.class);
		registerBlock("structure_wool_1", Blocks.WHITE_WOOL.getDefaultState());
		registerBlock("structure_wool_2", Blocks.LIGHT_GRAY_WOOL.getDefaultState());
		registerBlock("structure_wool_3", Blocks.GRAY_WOOL.getDefaultState());
		registerBlock("carpet", Blocks.WHITE_CARPET.getDefaultState());
		registerBlock("village_door", Blocks.OAK_DOOR.getDefaultState(), DoorBlock.class);
		registerBlock("salamander_floor", "upper", Blocks.COARSE_DIRT);
		registerBlock("village_path", "structure_secondary", Blocks.COBBLESTONE);
		registerBlock("village_fence", Blocks.OAK_FENCE.getDefaultState());
		registerBlock("fall_fluid", "ocean", Blocks.LIGHT_BLUE_WOOL);
		registerBlock("light_block", Blocks.GLOWSTONE.getDefaultState());
		registerBlock("mushroom_1", Blocks.RED_MUSHROOM.getDefaultState());
		registerBlock("mushroom_2", Blocks.BROWN_MUSHROOM.getDefaultState());
		registerBlock("bush", Blocks.DEAD_BUSH.getDefaultState());
		registerBlock("torch", Blocks.TORCH.getDefaultState(), TorchBlock.class);	//Class restriction needed because of the facing property
		registerBlock("bucket1", Blocks.QUARTZ_BLOCK.getDefaultState());
		registerBlock("bucket2", Blocks.IRON_BLOCK.getDefaultState());
		registerBlock("glass", Blocks.GLASS.getDefaultState());
		registerBlock("stained_glass_1", Blocks.GRAY_STAINED_GLASS.getDefaultState());
		registerBlock("stained_glass_2", Blocks.LIGHT_GRAY_STAINED_GLASS.getDefaultState());
		registerBlock("slime", Blocks.SLIME_BLOCK.getDefaultState());
		
		defaultRegistry.setBlockState("surface", Blocks.GRASS_BLOCK.getDefaultState());
		defaultRegistry.setBlockState("upper", Blocks.DIRT.getDefaultState());
		defaultRegistry.setBlockState("ocean_surface", Blocks.GRAVEL.getDefaultState());
	}
	
	public static StructureBlockRegistry getOrDefault(GenerationSettings settings)
	{
		if(settings instanceof LandGenSettings)
			return ((LandGenSettings) settings).getBlockRegistry();
		else return defaultRegistry;
	}
	
	private static class BlockEntry
	{
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
	private Map<String, BlockState> blockRegistry = new HashMap<>();
	private OreFeatureConfig.FillerBlockType groundType = OreFeatureConfig.FillerBlockType.NATURAL_STONE;
	
	public void setBlockState(String name, BlockState state)
	{
		if(state == null || name == null)
			throw new NullPointerException("Null parameters not allowed.");
		if(!staticRegistry.containsKey(name))
			throw new IllegalStateException("Structure block \""+name+"\" isn't registered, and can therefore not be set.");
		if(!staticRegistry.get(name).extention.isInstance(state.getBlock()))
			throw new IllegalArgumentException("The provided block must extend \""+staticRegistry.get(name).extention+"\".");
		if(name.equals("ground"))
			throw new IllegalArgumentException("Should use setGroundState() for setting the ground block.");
		
		blockRegistry.put(name, state);
	}
	
	public void setGroundState(BlockState state, OreFeatureConfig.FillerBlockType groundType)
	{
		Objects.requireNonNull(state,  "Null parameters not allowed.");
		Objects.requireNonNull(groundType,  "Null parameters not allowed.");
		
		blockRegistry.put("ground", state);
		this.groundType = groundType;
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
	
	public BlockState getCustomBlock(String name)
	{
		return blockRegistry.get(name);
	}
	
	public BlockState getStairs(String name, Direction facing, boolean upsideDown)
	{
		BlockState state = getBlockState(name);
		
		state = withOptionally(state, BlockStateProperties.HORIZONTAL_FACING, facing);
		
		if(upsideDown)
			state = withOptionally(state, BlockStateProperties.HALF, Half.TOP);
		
		return state;
	}
	
	public OreFeatureConfig.FillerBlockType getGroundType()
	{
		return groundType;
	}
	
	public BlockState getTemplateState(BlockState state)
	{
		if(templateBlockMap.containsKey(state.getBlock()))
		{
			BlockState newState = getBlockState(templateBlockMap.get(state.getBlock()));
			for(IProperty<?> property : state.getProperties())
				newState = with(state, newState, property);
			return newState;
		} else return state;
	}
	
	private static <T extends Comparable<T>> BlockState with(BlockState fromState, BlockState toState, IProperty<T> property)
	{
		if(toState.has(property))
			return toState.with(property, fromState.get(property));
		else return toState;
	}
	
	public static <T extends Comparable<T>> BlockState withOptionally(BlockState state, IProperty<T> property, T value)
	{
		if(state.has(property))
		{
			state = state.with(property, value);
		}
		return state;
	}
}