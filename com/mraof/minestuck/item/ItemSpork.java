package com.mraof.minestuck.item;

import com.mraof.minestuck.Minestuck;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

//I called it a spork because it includes both
public class ItemSpork extends Item {
	private int weaponDamage;
	private final EnumSporkType sporkType;
    public float efficiencyOnProperMaterial = 4.0F;
    //whether it's a spoon or a fork
    public boolean isSpoon;
    private Icon[] crockerTypes = new Icon[2];
    
	public ItemSpork(int id, EnumSporkType sporkType) 
	{
		super(id);
		this.isSpoon = sporkType.getIsSpoon();
		this.sporkType = sporkType;
		this.maxStackSize = 1;
		this.setMaxDamage(sporkType.getMaxUses());
		this.setCreativeTab(Minestuck.tabMinestuck);
		switch(sporkType)
		{
		case CROCKER:
			this.setUnlocalizedName("crockerSpork");
//			this.setIconIndex(23);
			break;
		case SKAIA:
			this.setUnlocalizedName("skaiaFork");
//			this.setIconIndex(25);
			break;
		}
		this.weaponDamage = 2 + sporkType.getDamageVsEntity();
	}

    
	public int getDamageVsEntity(Entity par1Entity) {
		return isSpoon ? weaponDamage : weaponDamage + 2;
	}
	
	public int getItemEnchantability()
	{
		return this.sporkType.getEnchantability();
	}
	 
	public boolean hitEntity(ItemStack itemStack, EntityLiving target, EntityLiving player)
	{
		itemStack.damageItem((isSpoon && !sporkType.equals(sporkType.CROCKER)) ? 1 : 2, player);
		return true;
	}
	
	public boolean onBlockDestroyed(ItemStack itemStack, World world, int par3, int par4, int par5, int par6, EntityLiving par7EntityLiving)
	{
		if ((double)Block.blocksList[par3].getBlockHardness(world, par4, par5, par6) != 0.0D)
		{
			itemStack.damageItem(2, par7EntityLiving);
		}
		
		return true;
	}
	@SideOnly(Side.CLIENT)
	public boolean isFull3D()
	{
		return true;
	}
	public int getMaxItemUseDuration(ItemStack itemStack)
    {
        return 72000;
    }
	
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) 
	{
		if(!world.isRemote)
		if(sporkType.equals(sporkType.CROCKER))
		{
			System.out.print("Spork Changed");
			if(isSpoon)itemIcon = crockerTypes[1];
			else itemIcon = crockerTypes[0];
			isSpoon = !isSpoon;
			return true;
		}
		return false;
	}
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegister) 
	{
		switch(sporkType)
		{
		case CROCKER:
			crockerTypes[0] = iconRegister.registerIcon("Minestuck:CrockerSpoon");
			crockerTypes[1] = iconRegister.registerIcon("Minestuck:CrockerFork");
			itemIcon = crockerTypes[0];
			break;
		case SKAIA:
			itemIcon = iconRegister.registerIcon("Minestuck:SkaianFork");
			break;
		}
	}
}
