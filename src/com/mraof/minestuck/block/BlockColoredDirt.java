package com.mraof.minestuck.block;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
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
//	public IIcon[] textures;

	public BlockColoredDirt(String[] iconNames)
	{
		super(Material.ground);
		this.setCreativeTab(Minestuck.tabMinestuck);
		this.iconNames = iconNames;
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

