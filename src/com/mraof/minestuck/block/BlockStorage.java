package com.mraof.minestuck.block;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import com.mraof.minestuck.Minestuck;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;


public class BlockStorage extends Block {
	public static final String[] iconNames = {"CruxiteBlock","GenericObject"};
	private IIcon[] textures;

	public BlockStorage() {
		super(Material.rock);
		
		setBlockName("blockStorage");
		setHardness(3.0F);
		this.setCreativeTab(Minestuck.tabMinestuck);
	}

	@Override
	public IIcon getIcon(int side, int metadata) 
	{
		return textures[metadata];
	}
	
	@Override
	public int damageDropped(int metadata) 
	{
		return metadata;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@SideOnly(Side.CLIENT)
	@Override
	public void getSubBlocks(Item item, CreativeTabs tab, List subItems) 
	{
		for(int i = 0; i < iconNames.length; i++)
			subItems.add(new ItemStack(this, 1, i));
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerBlockIcons(IIconRegister par1IconRegister)
	{
		this.textures = new IIcon[iconNames.length];

		for (int i = 0; i < this.textures.length; i++)
			this.textures[i] = par1IconRegister.registerIcon("minestuck:" + iconNames[i]);
	}
}
