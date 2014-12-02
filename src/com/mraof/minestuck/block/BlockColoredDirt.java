package com.mraof.minestuck.block;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.mraof.minestuck.Minestuck;

public class BlockColoredDirt extends Block
{
	public final String[] iconNames;
	public static final PropertyInteger BLOCK_TYPE = PropertyInteger.create("blockType", 0, 1);
//	public IIcon[] textures;

	public BlockColoredDirt(String[] iconNames)
	{
		super(Material.ground);
		this.setCreativeTab(Minestuck.tabMinestuck);
		this.iconNames = iconNames;
		setDefaultState(getBlockState().getBaseState().withProperty(BLOCK_TYPE, 0));
	}
	
	@Override
	protected BlockState createBlockState()
	{
		return new BlockState(this, BLOCK_TYPE);
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return getDefaultState().withProperty(BLOCK_TYPE, meta % BLOCK_TYPE.getAllowedValues().size());
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return (Integer) state.getValue(BLOCK_TYPE);
	}
	
//	@Override
//	public IIcon getIcon(int side, int metadata)
//	{
//		return textures[metadata];
//	}

//	@Override
//	public int damageDropped(int metadata)
//	{
//		return metadata;
//	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item item, CreativeTabs tab, List subItems) 
	{
		for(int i = 0; i < iconNames.length; i++)
			subItems.add(new ItemStack(this, 1, i));
	}

//	@Override
//	public boolean canCreatureSpawn(EnumCreatureType type, IBlockAccess world, int x, int y, int z) 
//	{
//		return true;
//	}

//	@SideOnly(Side.CLIENT)
//	@Override
//	public void registerBlockIcons(IIconRegister par1IconRegister)
//	{
//		this.textures = new IIcon[iconNames.length];
//
//		for (int i = 0; i < this.textures.length; i++)
//			this.textures[i] = par1IconRegister.registerIcon("minestuck:" + iconNames[i]);
//	}
}

