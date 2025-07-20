package com.mraof.minestuck.world.gen.structure.blocks;

import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.world.gen.LandChunkGenerator;
import com.mraof.minestuck.world.lands.LandTypePair;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockStateMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public final class StructureBlockRegistry
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	private static final Map<String, BlockEntry> staticRegistry = new HashMap<>();
	private static final Map<Block, String> templateBlockMap = new HashMap<>();
	private static final StructureBlockRegistry fallbackRegistry = new StructureBlockRegistry(); //fallback for when getting a registry from non-land dimension or if exception is thrown
	
	public static void registerBlock(String name, BlockState defaultBlock)
	{
		registerBlock(name, defaultBlock, Block.class);
	}
	
	//TODO With async modloading, perhaps we should synchronize this in some way?
	public static void registerBlock(String name, BlockState defaultBlock, Class<? extends Block> extension)
	{
		if(defaultBlock == null || name == null)
			throw new IllegalArgumentException("Null parameters not allowed.");
		if(staticRegistry.containsKey(name))
			throw new IllegalStateException("\"" + name + "\" has already been registered!");
		if(!extension.isInstance(defaultBlock.getBlock()))
			throw new IllegalArgumentException("The default block \"" + defaultBlock.getBlock() + "\" has to extend the minimum class \"" + extension + "\"!");
		if(templateBlockMap.containsKey(defaultBlock.getBlock()))
			throw new IllegalStateException("Can't have two identical template blocks!");
		
		staticRegistry.put(name, new BlockEntry(defaultBlock, extension));
		templateBlockMap.put(defaultBlock.getBlock(), name);
	}
	
	public static void registerBlock(String name, String parent, Block templateState)
	{
		registerBlock(name, parent, templateState, Block.class);
	}
	
	public static void registerBlock(String name, String parent, Block templateState, Class<? extends Block> extension)
	{
		if(parent == null || name == null)
			throw new IllegalArgumentException("Null parameters not allowed.");
		if(staticRegistry.containsKey(name))
			throw new IllegalStateException("\"" + name + "\" has already been registered!");
		
		if(!staticRegistry.containsKey(parent))
			throw new IllegalStateException("The parent entry \"" + parent + "\" isn't registered! Make sure to register the parent first.");
		
		if(!extension.isAssignableFrom(staticRegistry.get(parent).extension))
			throw new IllegalArgumentException("The class specified must be the same or a superclass to the class used by the parent \"" + parent + "\".");
		if(templateBlockMap.containsKey(templateState))
			throw new IllegalStateException("Can't have two identical template blocks!");
		
		staticRegistry.put(name, new BlockEntry(parent, extension));
		templateBlockMap.put(templateState, name);
	}
	
	public static void registerEmptyFallbackBlock(String name, Block defaultBlock)
	{
		registerEmptyFallbackBlock(name, defaultBlock, Block.class);
	}
	
	public static void registerEmptyFallbackBlock(String name, Block defaultBlock, Class<? extends Block> extension)
	{
		if(defaultBlock == null || name == null)
			throw new IllegalArgumentException("Null parameters not allowed.");
		if(staticRegistry.containsKey(name))
			throw new IllegalStateException("\"" + name + "\" has already been registered!");
		
		staticRegistry.put(name, new BlockEntry("air", extension));
		templateBlockMap.put(defaultBlock, name);
	}
	
	public static final String CRUXITE_ORE = "cruxite_ore";
	public static final String URANIUM_ORE = "uranium_ore";
	public static final String GROUND = "ground";
	public static final String GROUND_SLAB = "ground_slab";
	public static final String UPPER = "upper";
	public static final String SURFACE = "surface";
	public static final String SURFACE_ROUGH = "surface_rough";
	public static final String OCEAN = "ocean";
	public static final String OCEAN_SURFACE = "ocean_surface";
	public static final String RIVER = "river";
	public static final String SAND = "sand";
	
	public static final String STRUCTURE_PRIMARY = "structure_primary";
	public static final String STRUCTURE_PRIMARY_DECORATIVE = "structure_primary_decorative";
	public static final String STRUCTURE_PRIMARY_CRACKED = "structure_primary_cracked";
	public static final String STRUCTURE_PRIMARY_COLUMN = "structure_primary_column";
	public static final String STRUCTURE_PRIMARY_STAIRS = "structure_primary_stairs";
	public static final String STRUCTURE_PRIMARY_SLAB = "structure_primary_slab";
	public static final String STRUCTURE_PRIMARY_WALL = "structure_primary_wall";
	
	public static final String STRUCTURE_PRIMARY_MOSSY = "structure_primary_mossy";
	public static final String STRUCTURE_PRIMARY_MOSSY_STAIRS = "structure_primary_mossy_stairs";
	public static final String STRUCTURE_PRIMARY_MOSSY_SLAB = "structure_primary_mossy_slab";
	public static final String STRUCTURE_PRIMARY_MOSSY_WALL = "structure_primary_mossy_wall";
	
	public static final String STRUCTURE_SECONDARY = "structure_secondary";
	public static final String STRUCTURE_SECONDARY_DECORATIVE = "structure_secondary_decorative";
	public static final String STRUCTURE_SECONDARY_STAIRS = "structure_secondary_stairs";
	public static final String STRUCTURE_SECONDARY_SLAB = "structure_secondary_slab";
	public static final String STRUCTURE_SECONDARY_WALL = "structure_secondary_wall";
	
	public static final String STRUCTURE_WOOD = "structure_wood";
	public static final String STRUCTURE_LOG = "structure_log";
	public static final String STRUCTURE_STRIPPED_WOOD = "structure_stripped_wood";
	public static final String STRUCTURE_STRIPPED_LOG = "structure_stripped_log";
	public static final String STRUCTURE_PLANKS = "structure_planks";
	public static final String STRUCTURE_BOOKSHELF = "structure_bookshelf";
	public static final String STRUCTURE_PLANKS_STAIRS = "structure_planks_stairs";
	public static final String STRUCTURE_PLANKS_SLAB = "structure_planks_slab";
	public static final String STRUCTURE_PLANKS_FENCE = "structure_planks_fence";
	public static final String STRUCTURE_PLANKS_FENCE_GATE = "structure_planks_fence_gate";
	public static final String STRUCTURE_PLANKS_DOOR = "structure_planks_door";
	public static final String STRUCTURE_PLANKS_TRAPDOOR = "structure_planks_trapdoor";
	
	public static final String STRUCTURE_WOOL_1 = "structure_wool_1";
	public static final String STRUCTURE_WOOL_2 = "structure_wool_2";
	public static final String STRUCTURE_WOOL_3 = "structure_wool_3";
	public static final String CARPET = "carpet";
	
	public static final String STRUCTURE_GROUND_COVER = "structure_ground_cover";
	public static final String STRUCTURE_ROOF_COVER = "structure_roof_cover";
	
	public static final String VILLAGE_DOOR = "village_door";
	public static final String VILLAGE_PATH = "village_path";
	public static final String VILLAGE_FENCE = "village_fence";
	public static final String SALAMANDER_FLOOR = "salamander_floor";
	
	public static final String FALL_FLUID = "fall_fluid";
	public static final String LIGHT_BLOCK = "light_block";
	public static final String MUSHROOM_1 = "mushroom_1";
	public static final String MUSHROOM_2 = "mushroom_2";
	public static final String BUSH = "bush";
	public static final String TORCH = "torch";
	public static final String WALL_TORCH = "wall_torch";
	public static final String BUCKET_1 = "bucket_1";
	public static final String BUCKET_2 = "bucket_2";
	public static final String GLASS = "glass";
	public static final String STAINED_GLASS_1 = "stained_glass_1";
	public static final String STAINED_GLASS_2 = "stained_glass_2";
	public static final String SLIME = "slime";
	
	static
	{
		staticRegistry.put("air", new BlockEntry(Blocks.AIR.defaultBlockState(), Block.class));
		
		registerBlock(CRUXITE_ORE, MSBlocks.STONE_CRUXITE_ORE.get().defaultBlockState());
		registerBlock(URANIUM_ORE, MSBlocks.STONE_URANIUM_ORE.get().defaultBlockState());
		registerBlock(GROUND, Blocks.STONE.defaultBlockState());
		registerBlock(GROUND_SLAB, GROUND, Blocks.STONE_SLAB);
		registerBlock(UPPER, GROUND, Blocks.DIRT);
		registerBlock(SURFACE, UPPER, Blocks.GRASS_BLOCK);
		registerBlock(SURFACE_ROUGH, SURFACE, Blocks.PODZOL);
		registerBlock(OCEAN, Blocks.WATER.defaultBlockState());
		registerBlock(OCEAN_SURFACE, UPPER, Blocks.GRAVEL);
		registerBlock(RIVER, OCEAN, Blocks.BLUE_WOOL);
		registerBlock(SAND, Blocks.SAND.defaultBlockState());
		
		registerBlock(STRUCTURE_PRIMARY, Blocks.STONE_BRICKS.defaultBlockState());
		registerBlock(STRUCTURE_PRIMARY_DECORATIVE, STRUCTURE_PRIMARY, Blocks.CHISELED_STONE_BRICKS);
		registerBlock(STRUCTURE_PRIMARY_CRACKED, STRUCTURE_PRIMARY, Blocks.CRACKED_STONE_BRICKS);
		registerBlock(STRUCTURE_PRIMARY_COLUMN, STRUCTURE_PRIMARY_DECORATIVE, MSBlocks.COARSE_STONE_COLUMN.get());
		registerBlock(STRUCTURE_PRIMARY_STAIRS, STRUCTURE_PRIMARY, Blocks.STONE_BRICK_STAIRS);
		registerBlock(STRUCTURE_PRIMARY_SLAB, STRUCTURE_PRIMARY, Blocks.STONE_BRICK_SLAB);
		registerBlock(STRUCTURE_PRIMARY_WALL, STRUCTURE_PRIMARY, Blocks.STONE_BRICK_WALL);
		
		//TODO consider renaming mossy to something more neutral like contaminated or overgrown
		registerBlock(STRUCTURE_PRIMARY_MOSSY, STRUCTURE_PRIMARY, Blocks.MOSSY_STONE_BRICKS);
		registerBlock(STRUCTURE_PRIMARY_MOSSY_STAIRS, STRUCTURE_PRIMARY_MOSSY, Blocks.MOSSY_STONE_BRICK_STAIRS);
		registerBlock(STRUCTURE_PRIMARY_MOSSY_SLAB, STRUCTURE_PRIMARY_MOSSY, Blocks.MOSSY_STONE_BRICK_SLAB);
		registerBlock(STRUCTURE_PRIMARY_MOSSY_WALL, STRUCTURE_PRIMARY_MOSSY, Blocks.MOSSY_STONE_BRICK_WALL);
		
		registerBlock(STRUCTURE_SECONDARY, STRUCTURE_PRIMARY, Blocks.NETHER_BRICKS);
		registerBlock(STRUCTURE_SECONDARY_DECORATIVE, STRUCTURE_SECONDARY, Blocks.RED_NETHER_BRICKS); //TODO consider cracked variant
		registerBlock(STRUCTURE_SECONDARY_STAIRS, STRUCTURE_SECONDARY, Blocks.NETHER_BRICK_STAIRS);
		registerBlock(STRUCTURE_SECONDARY_SLAB, STRUCTURE_SECONDARY, Blocks.NETHER_BRICK_SLAB);
		registerBlock(STRUCTURE_SECONDARY_WALL, STRUCTURE_SECONDARY, Blocks.NETHER_BRICK_WALL);
		
		//TODO add button/ladder/pressure plate/sign
		registerBlock(STRUCTURE_WOOD, Blocks.OAK_WOOD.defaultBlockState());
		registerBlock(STRUCTURE_LOG, Blocks.OAK_LOG.defaultBlockState());
		registerBlock(STRUCTURE_STRIPPED_WOOD, STRUCTURE_WOOD, Blocks.STRIPPED_OAK_WOOD);
		registerBlock(STRUCTURE_STRIPPED_LOG, STRUCTURE_LOG, Blocks.STRIPPED_OAK_LOG);
		registerBlock(STRUCTURE_PLANKS, Blocks.OAK_PLANKS.defaultBlockState());
		registerBlock(STRUCTURE_BOOKSHELF, Blocks.BOOKSHELF.defaultBlockState());
		registerBlock(STRUCTURE_PLANKS_STAIRS, STRUCTURE_PLANKS, Blocks.OAK_STAIRS);
		registerBlock(STRUCTURE_PLANKS_SLAB, STRUCTURE_PLANKS, Blocks.OAK_SLAB);
		registerBlock(STRUCTURE_PLANKS_FENCE, STRUCTURE_PLANKS, Blocks.OAK_FENCE);
		registerBlock(STRUCTURE_PLANKS_TRAPDOOR, Blocks.OAK_TRAPDOOR.defaultBlockState());
		registerEmptyFallbackBlock(STRUCTURE_PLANKS_FENCE_GATE, Blocks.OAK_FENCE_GATE);
		registerEmptyFallbackBlock(STRUCTURE_PLANKS_DOOR, Blocks.OAK_DOOR, DoorBlock.class);
		
		registerBlock(STRUCTURE_WOOL_1, Blocks.WHITE_WOOL.defaultBlockState());
		registerBlock(STRUCTURE_WOOL_2, Blocks.LIGHT_GRAY_WOOL.defaultBlockState());
		registerBlock(STRUCTURE_WOOL_3, Blocks.GRAY_WOOL.defaultBlockState());
		registerBlock(CARPET, Blocks.WHITE_CARPET.defaultBlockState());
		
		registerBlock(STRUCTURE_GROUND_COVER, Blocks.MOSS_CARPET.defaultBlockState());
		registerBlock(STRUCTURE_ROOF_COVER, Blocks.HANGING_ROOTS.defaultBlockState());
		
		registerBlock(VILLAGE_DOOR, STRUCTURE_PLANKS_DOOR, Blocks.DARK_OAK_DOOR, DoorBlock.class);
		registerBlock(VILLAGE_PATH, STRUCTURE_SECONDARY, Blocks.COBBLESTONE);
		registerBlock(VILLAGE_FENCE, STRUCTURE_PLANKS_FENCE, Blocks.DARK_OAK_FENCE);
		registerBlock(SALAMANDER_FLOOR, UPPER, Blocks.COARSE_DIRT);
		
		registerBlock(FALL_FLUID, OCEAN, Blocks.LIGHT_BLUE_WOOL);
		registerBlock(LIGHT_BLOCK, Blocks.GLOWSTONE.defaultBlockState());
		registerBlock(MUSHROOM_1, Blocks.RED_MUSHROOM.defaultBlockState());
		registerBlock(MUSHROOM_2, Blocks.BROWN_MUSHROOM.defaultBlockState());
		registerBlock(BUSH, Blocks.DEAD_BUSH.defaultBlockState());
		registerBlock(TORCH, Blocks.TORCH.defaultBlockState());
		registerBlock(WALL_TORCH, Blocks.WALL_TORCH.defaultBlockState());
		registerBlock(BUCKET_1, Blocks.QUARTZ_BLOCK.defaultBlockState());
		registerBlock(BUCKET_2, Blocks.IRON_BLOCK.defaultBlockState());
		registerBlock(GLASS, Blocks.GLASS.defaultBlockState());
		registerBlock(STAINED_GLASS_1, Blocks.GRAY_STAINED_GLASS.defaultBlockState());
		registerBlock(STAINED_GLASS_2, Blocks.LIGHT_GRAY_STAINED_GLASS.defaultBlockState());
		registerBlock(SLIME, Blocks.SLIME_BLOCK.defaultBlockState());
		
		fallbackRegistry.setBlock(SURFACE, Blocks.GRASS_BLOCK);
		fallbackRegistry.setBlock(UPPER, Blocks.DIRT);
		fallbackRegistry.setBlock(OCEAN_SURFACE, Blocks.GRAVEL);
	}
	
	public static StructureBlockRegistry getOrDefault(ChunkGenerator generator)
	{
		if(generator instanceof LandChunkGenerator)
			return ((LandChunkGenerator) generator).blockRegistry;
		else return fallbackRegistry;
	}
	
	public static StructureBlockRegistry init(LandTypePair landTypes)
	{
		try
		{
			StructureBlockRegistry blockRegistry = new StructureBlockRegistry();
			landTypes.getTerrain().registerBlocks(blockRegistry);
			landTypes.getTitle().registerBlocks(blockRegistry);
			return blockRegistry;
		} catch(RuntimeException e)
		{
			LOGGER.error("Caught exception while setting up StructureBlockRegistry.", e);
			return fallbackRegistry;
		}
	}
	
	private static class BlockEntry
	{
		//TODO define restriction with a set of block state properties instead of base class
		Class<? extends Block> extension;
		BlockState defaultBlock;
		String parentEntry;
		
		BlockEntry(BlockState state, Class<? extends Block> clazz)
		{
			defaultBlock = state;
			extension = clazz;
		}
		
		BlockEntry(String str, Class<? extends Block> clazz)
		{
			parentEntry = str;
			extension = clazz;
		}
		
		BlockState getBlockState(StructureBlockRegistry registry)
		{
			if(parentEntry == null)
				return defaultBlock;
			
			if(parentEntry.equals("air"))
				return Blocks.AIR.defaultBlockState();
			
			return registry.getBlockState(parentEntry);
		}
	}
	
	//Nonstatic stuff
	private final Map<String, BlockState> blockRegistry = new HashMap<>();
	private RuleTest groundType = new TagMatchTest(BlockTags.BASE_STONE_OVERWORLD);
	
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
			throw new IllegalStateException("Structure block \"" + name + "\" isn't registered, and can therefore not be set.");
		if(!staticRegistry.get(name).extension.isInstance(state.getBlock()))
			throw new IllegalArgumentException("The provided block must extend \"" + staticRegistry.get(name).extension + "\".");
		
		blockRegistry.put(name, state);
		
		if(name.equals(StructureBlockRegistry.GROUND))
			groundType = new BlockStateMatchTest(state);
	}
	
	public BlockState getBlockState(String name)
	{
		if(name == null)
			throw new IllegalArgumentException("Null parameters not allowed.");
		if(!staticRegistry.containsKey(name))
			throw new IllegalStateException("Structure block \"" + name + "\" isn't registered, and can therefore not be obtained.");
		
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
	
	public static BlockState getModifiedTorch(BlockState state, Direction facing)
	{
		state = withOptionally(state, WallTorchBlock.FACING, facing);
		
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