package com.mraof.minestuck.block;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLiving.SpawnPlacementType;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.mraof.minestuck.Minestuck;

public class BlockColoredDirt extends Block
{
	
	public static enum BlockType implements IStringSerializable
	{
		BLUE("blue"),
		THOUGHT("thought");
		
		public final String name;
		BlockType(String name)
		{
			this.name = name;
		}
		@Override
		public String getName()
		{
			return name;
		}
		
	}
	
	public static final PropertyEnum BLOCK_TYPE = PropertyEnum.create("blockType", BlockType.class);
	
	public BlockColoredDirt()
	{
		super(Material.ground);
		this.setCreativeTab(Minestuck.tabMinestuck);
		setStepSound(Block.soundTypeGravel);
		setDefaultState(getBlockState().getBaseState().withProperty(BLOCK_TYPE, BlockType.BLUE));
	}
	
	@Override
	protected BlockState createBlockState()
	{
		return new BlockState(this, BLOCK_TYPE);
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return getDefaultState().withProperty(BLOCK_TYPE, BlockType.values()[meta]);
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return ((BlockType) state.getValue(BLOCK_TYPE)).ordinal();
	}
	
	@Override
	public int damageDropped(IBlockState state)
	{
		return getMetaFromState(state);
	}
	
	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item item, CreativeTabs tab, List subItems) 
	{
		for(int i = 0; i < BlockType.values().length; i++)
			subItems.add(new ItemStack(this, 1, i));
	}
	
	@Override
	public boolean canCreatureSpawn(IBlockAccess world, BlockPos pos, SpawnPlacementType type)
	{
		return true;
	}
	
	@Override
	public MapColor getMapColor(IBlockState state)
	{
		switch((BlockType) state.getValue(BLOCK_TYPE))
		{
		case BLUE: return MapColor.blueColor;
		case THOUGHT: return MapColor.limeColor;
		default: return super.getMapColor(state);
		}
	}
	
	@Override
	public boolean canSustainPlant(IBlockAccess world, BlockPos pos, EnumFacing direction, IPlantable plantable)
	{
		return plantable == Blocks.sapling;
	}
	
}