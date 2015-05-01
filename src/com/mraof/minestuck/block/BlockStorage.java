package com.mraof.minestuck.block;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.mraof.minestuck.Minestuck;

public class BlockStorage extends Block
{
	public static enum BlockType implements IStringSerializable
	{
		CRUXITE("cruxite"),
		GENERIC_OBJECT("generic_object");
		
		public final String name;
		BlockType(String name)
		{
			this.name = name;
		}
		public String getName()
		{
			return this.name;
		}
	}
	
	public static final String[] iconNames = {"CruxiteBlock","GenericObject"};
	public static final PropertyEnum BLOCK_TYPE = PropertyEnum.create("block_type", BlockType.class);
	
	public BlockStorage() {
		super(Material.rock);
		
		setUnlocalizedName("blockStorage");
		setHardness(3.0F);
		this.setCreativeTab(Minestuck.tabMinestuck);
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
	protected BlockState createBlockState()
	{
		return new BlockState(this, BLOCK_TYPE);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@SideOnly(Side.CLIENT)
	@Override
	public void getSubBlocks(Item item, CreativeTabs tab, List subItems) 
	{
		for(int i = 0; i < iconNames.length; i++)
			subItems.add(new ItemStack(this, 1, i));
	}
	
}
