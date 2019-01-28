package com.mraof.minestuck.world.lands.structure.blocks;

import net.minecraft.block.*;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Rotation;

import java.util.HashMap;
import java.util.Map;

public class StructureBlockRegistry
{
	
	private static Map<String, BlockEntry> staticRegistry = new HashMap<String, BlockEntry>();
	
	public static void registerBlock(String name, IBlockState defaultBlock)
	{
		registerBlock(name, defaultBlock, Block.class);
	}
	
	public static void registerBlock(String name, IBlockState defaultBlock, Class<? extends Block> extention)
	{
		if(defaultBlock == null || name == null)
			throw new IllegalArgumentException("Null parameters not allowed.");
		if(staticRegistry.containsKey(name))
			throw new IllegalStateException("\""+name+"\" has already been registered!");
		if(!extention.isInstance(defaultBlock.getBlock()))
			throw new IllegalArgumentException("The default block \""+defaultBlock.getBlock()+"\" has to extend the minimum class \""+extention+"\"!");
		
		staticRegistry.put(name, new BlockEntry(defaultBlock, extention));
	}
	
	public static void registerBlock(String name, String parent)
	{
		registerBlock(name, parent, Block.class);
	}
	
	public static void registerBlock(String name, String parent, Class<? extends Block> extention)
	{
		if(parent == null || name == null)
			throw new IllegalArgumentException("Null parameters not allowed.");
		if(staticRegistry.containsKey(name))
			throw new IllegalStateException("\""+name+"\" has already been registered!");
		
		if(!staticRegistry.containsKey(parent))
			throw new IllegalStateException("The parent entry \""+parent+"\" isn't registered! Make sure to register the parent first.");
		
		if(!extention.isAssignableFrom(staticRegistry.get(parent).extention))
			throw new IllegalArgumentException("The class specified must be the same or a superclass to the class used by the parent \""+parent+"\".");
		
		staticRegistry.put(name, new BlockEntry(parent, extention));
	}
	
	static
	{
		registerBlock("ground", Blocks.STONE.getDefaultState());
		registerBlock("upper", "ground");
		registerBlock("surface", "upper");
		registerBlock("ocean", Blocks.WATER.getDefaultState());
		registerBlock("river", "ocean");
		registerBlock("structure_primary", Blocks.STONEBRICK.getDefaultState());
		registerBlock("structure_primary_decorative", "structure_primary");
		registerBlock("structure_primary_stairs", "structure_primary");
		registerBlock("structure_secondary", "structure_primary");
		registerBlock("structure_secondary_decorative", "structure_secondary");
		registerBlock("structure_secondary_stairs", "structure_secondary");
		registerBlock("structure_planks", Blocks.PLANKS.getDefaultState());
		registerBlock("structure_planks_slab", Blocks.WOODEN_SLAB.getDefaultState(), BlockSlab.class);
		registerBlock("structure_wool_1", Blocks.WOOL.getDefaultState());
		registerBlock("structure_wool_2", Blocks.WOOL.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.SILVER));
		registerBlock("structure_wool_3", Blocks.WOOL.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.GRAY));
		registerBlock("carpet", Blocks.CARPET.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.WHITE));
		registerBlock("village_door", Blocks.OAK_DOOR.getDefaultState(), BlockDoor.class);
		registerBlock("salamander_floor", "upper");
		registerBlock("village_path", "structure_secondary");
		registerBlock("village_fence", Blocks.OAK_FENCE.getDefaultState());
		registerBlock("fall_fluid", "ocean");
		registerBlock("light_block", Blocks.GLOWSTONE.getDefaultState());
		registerBlock("mushroom_1", Blocks.RED_MUSHROOM.getDefaultState());
		registerBlock("mushroom_2", Blocks.BROWN_MUSHROOM.getDefaultState());
		registerBlock("bush", Blocks.DEADBUSH.getDefaultState());
		registerBlock("torch", Blocks.TORCH.getDefaultState(), BlockTorch.class);	//Class restriction needed because of the facing property
		registerBlock("bucket1", Blocks.QUARTZ_BLOCK.getDefaultState());
		registerBlock("bucket2", Blocks.IRON_BLOCK.getDefaultState());
		registerBlock("glass", Blocks.GLASS.getDefaultState());
		registerBlock("stained_glass_1", Blocks.STAINED_GLASS.getDefaultState().withProperty(BlockStainedGlass.COLOR, EnumDyeColor.GRAY));
		registerBlock("stained_glass_2", Blocks.STAINED_GLASS.getDefaultState().withProperty(BlockStainedGlass.COLOR, EnumDyeColor.SILVER));
		registerBlock("slime", Blocks.SLIME_BLOCK.getDefaultState());
	}
	
	private static class BlockEntry
	{
		Class<? extends Block> extention;
		IBlockState defaultBlock;
		String parentEntry;
		BlockEntry(IBlockState state, Class<? extends Block> clazz)
		{
			defaultBlock = state;
			extention = clazz;
		}
		BlockEntry(String str, Class<? extends Block> clazz)
		{
			parentEntry = str;
			extention = clazz;
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
		if(!staticRegistry.get(name).extention.isInstance(state.getBlock()))
			throw new IllegalArgumentException("The provided block must extend \""+staticRegistry.get(name).extention+"\".");
		
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
	
	public IBlockState getStairs(String name, EnumFacing facing, boolean upsideDown)
	{
		Rotation rotation;
		switch(facing)
		{
		case EAST:
			rotation = Rotation.CLOCKWISE_90;
			break;
		case SOUTH:
			rotation = Rotation.CLOCKWISE_180;
			break;
		case WEST:
			rotation = Rotation.COUNTERCLOCKWISE_90;
			break;
		default: rotation = Rotation.NONE;
		}
		
		IBlockState state = getBlockState(name);
		state = state.withRotation(rotation);
		
		if(upsideDown)
			for(IProperty<?> property : state.getPropertyKeys())
				if(property.getValueClass().equals(BlockStairs.EnumHalf.class))
				{
					state = state.withProperty((IProperty<BlockStairs.EnumHalf>)property, BlockStairs.EnumHalf.TOP);
					break;
				}
		
		return state;
	}
}