package com.mraof.minestuck.item;

import java.util.List;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.World;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.tileentity.TileEntityComputer;
import com.mraof.minestuck.util.Debug;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * An item ID just filled with useless things used only as combination recipes.
 * @author iconmaster
 *
 */
public class ItemComponent extends Item {
	
	private Icon[] icons = new Icon[3];
	private String[] subNames = {"WoodenSpoon","SilverSpoon","Chessboard"};

	public ItemComponent(int par1) {
		super(par1);
		this.maxStackSize = 1;
		this.setHasSubtypes(true);
		this.setCreativeTab(Minestuck.tabMinestuck);
		this.setUnlocalizedName("component");
	}
	
	@Override
	public Icon getIconFromDamage(int meta) {
		return icons[meta];
	}
	
	@Override
	public void registerIcons(IconRegister par1IconRegister) {
		for (int i = 0;i < subNames.length;i++) {
			icons[i] = par1IconRegister.registerIcon("minestuck:Compo"+subNames[i]);
		}
	}
	
	@Override
	public String getUnlocalizedName(ItemStack itemstack) 
	{
		return getUnlocalizedName() + "." + subNames[itemstack.getItemDamage()];
	}
	
	@Override
	public int getMetadata (int damageValue) 
	{
		return damageValue;
	}
	
	@SideOnly(Side.CLIENT)
	public void getSubItems(int unknown, CreativeTabs tab, List subItems) 
	{
		for(int i = 0; i < subNames.length; i++)
			subItems.add(new ItemStack(this, 1, i));
	}
}
