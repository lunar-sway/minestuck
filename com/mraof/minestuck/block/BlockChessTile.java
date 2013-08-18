package com.mraof.minestuck.block;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.World;

import com.mraof.minestuck.Minestuck;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockChessTile extends Block 
{
	public static final String[] iconNames = {"BlackChessTile", "WhiteChessTile", "DarkGreyChessTile", "LightGreyChessTile"};
	private Icon[] textures;
	public BlockChessTile(int id)
	{
		super(id, Material.ground);
		setUnlocalizedName("chessTile");
		setHardness(0.5F);
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
	@Override
	public boolean canCreatureSpawn(EnumCreatureType type, World world, int x, int y, int z) 
	{
		return true;
	}
    @SideOnly(Side.CLIENT)

    /**
     * When this method is called, your block should register all the icons it needs with the given IconRegister. This
     * is the only chance you get to register icons.
     */
    public void registerIcons(IconRegister par1IconRegister)
    {
        this.textures = new Icon[iconNames.length];

        for (int i = 0; i < this.textures.length; i++)
            this.textures[i] = par1IconRegister.registerIcon("minestuck:" + iconNames[i]);
    }
}