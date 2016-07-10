package com.mraof.minestuck.world.lands.structure.blocks;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;

public class StructureBlockRegistry
{
	
	private static Map<String, BlockEntry> staticRegistry = new HashMap<String, BlockEntry>();
	
	public static void registerBlock(String name, IBlockState defaultBlock)
	{
		if(defaultBlock == null || name == null)
			throw new IllegalArgumentException("Null parameters not allowed.");
		if(staticRegistry.containsKey(name))
			throw new IllegalStateException("\""+name+"\" has already been registered!");
		
		staticRegistry.put(name, new BlockEntry(defaultBlock));
	}
	
	public static void registerBlock(String name, String parent)
	{
		if(parent == null || name == null)
			throw new IllegalArgumentException("Null parameters not allowed.");
		if(staticRegistry.containsKey(name))
			throw new IllegalStateException("\""+name+"\" has already been registered!");
		
		if(!staticRegistry.containsKey(parent))
			throw new IllegalStateException("The parent entry \""+parent+"\" isn't registered! Make sure to register the parent first.");
		
		staticRegistry.put(name, new BlockEntry(parent));
	}
	
	static
	{
		registerBlock("ground", Blocks.STONE.getDefaultState());
		registerBlock("upper", "ground");
		registerBlock("surface", "upper");
		registerBlock("ocean", Blocks.WATER.getDefaultState());
		registerBlock("river", "ocean");
		registerBlock("structure_primary", "ground");
		registerBlock("structure_primary_decorative", "structure_primary");
		registerBlock("structure_secondary", "structure_primary");
		registerBlock("structure_secondary_decorative", "structure_secondary");
		registerBlock("bucket1", Blocks.QUARTZ_BLOCK.getDefaultState());
		registerBlock("bucket2", Blocks.IRON_BLOCK.getDefaultState());
	}
	
	private static class BlockEntry
	{
		IBlockState defaultBlock;
		String parentEntry;
		BlockEntry(IBlockState state)
		{
			defaultBlock = state;
		}
		BlockEntry(String str)
		{
			parentEntry = str;
		}
		
		IBlockState getBlockState(StructureBlockRegistry registry)
		{
			if(parentEntry != null)
				return registry.getBlockState(parentEntry);
			else return defaultBlock;
		}
	}
	
	//Nonstatic stuff
	private Map<String, IBlockState> blockRegistry = new HashMap<String, IBlockState>();
	
	public void setBlockState(String name, IBlockState state)
	{
		if(state == null || name == null)
			throw new IllegalArgumentException("Null parameters not allowed.");
		if(!staticRegistry.containsKey(name))
			throw new IllegalStateException("Structure block \""+name+"\" isn't registered, and can therefore not be set.");
		
		blockRegistry.put(name, state);
	}
	
	public IBlockState getBlockState(String name)
	{
		if(name == null)
			throw new IllegalArgumentException("Null parameters not allowed.");
		if(!staticRegistry.containsKey(name))
			throw new IllegalStateException("Structure block \""+name+"\" isn't registered, and can therefore not be obtained.");
		
		if(blockRegistry.containsKey(name))
			return blockRegistry.get(name);
		else return staticRegistry.get(name).getBlockState(this);
	}
	
	public IBlockState getCustomBlock(String name)
	{
		return blockRegistry.get(name);
	}
}