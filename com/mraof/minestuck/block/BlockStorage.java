package com.mraof.minestuck.block;

import java.util.List;

import com.mraof.minestuck.Minestuck;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.World;


public class BlockStorage extends Block {
	public static final String[] iconNames = {"CruxiteBlock"};
	private Icon[] textures;
	
	public BlockStorage(int id) {
		super(id,Material.rock);
		
		setUnlocalizedName("blockStorage");
		setHardness(3.0F);
		this.setCreativeTab(Minestuck.tabMinestuck);
	}
	
	@Override
	public Icon getIcon(int side, int metadata) 
	{
		return textures[metadata];
	}
	@Override
	public int damageDropped(int metadata) 
	{
		return metadata;
	}
	
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(int unknown, CreativeTabs tab, List subItems) 
	{
		for(int i = 0; i < iconNames.length; i++)
			subItems.add(new ItemStack(this, 1, i));
	}
	
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister par1IconRegister)
    {
        this.textures = new Icon[iconNames.length];

        for (int i = 0; i < this.textures.length; i++)
            this.textures[i] = par1IconRegister.registerIcon("Minestuck:" + iconNames[i]);
    }
}
