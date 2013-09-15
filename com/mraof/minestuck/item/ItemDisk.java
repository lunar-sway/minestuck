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

public class ItemDisk extends Item {
	
	private Icon[] icons = new Icon[2];
	private String[] subNames = {"client","server"};

	public ItemDisk(int par1) {
		super(par1);
		this.maxStackSize = 1;
		this.setHasSubtypes(true);
		this.setCreativeTab(Minestuck.tabMinestuck);
		this.setUnlocalizedName("disk");
	}
	
	@Override
	public Icon getIconFromDamage(int meta) {
		return icons[meta];
	}
	
	@Override
	public void registerIcons(IconRegister par1IconRegister) {
	   icons[0] = par1IconRegister.registerIcon("minestuck:ClientDisk");
	   icons[1] = par1IconRegister.registerIcon("minestuck:ServerDisk");
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
	
	@Override
	public boolean onItemUseFirst(ItemStack item, EntityPlayer player, World world, int x, int y, int z, int side, float par8, float par9, float par10) {
		if (world.getBlockId(x,y,z) == Minestuck.blockComputerOff.blockID) {
			int meta = world.getBlockMetadata(x,y,z);
			world.setBlock(x,y,z,Minestuck.blockComputerOn.blockID);
			world.setBlockMetadataWithNotify(x, y, z, meta, 2);
		}
		if(world.getBlockId(x,y,z) == Minestuck.blockComputerOn.blockID){
			TileEntityComputer te =  (TileEntityComputer) world.getBlockTileEntity(x, y, z);
			if (te == null) {return false;}
			int i = item.getItemDamage();
			if(i == 0)
				if(te.hasClient)
					return false;
				else te.hasClient = true;
			else if(te.hasServer)
				return false;
			else te.hasServer = true;
			Debug.print("Installed program with id "+item.getItemDamage());
			
			player.destroyCurrentEquippedItem();
			
			return true;
		}
		return false;
	}
	
	@SideOnly(Side.CLIENT)
	public void getSubItems(int unknown, CreativeTabs tab, List subItems) 
	{
		for(int i = 0; i < subNames.length; i++)
			subItems.add(new ItemStack(this, 1, i));
	}
}
