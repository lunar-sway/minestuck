package com.mraof.minestuck.item;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.tileentity.TileEntityComputer;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.util.UsernameHandler;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemDisk extends Item {

	private IIcon[] icons = new IIcon[2];
	private String[] subNames = { "Client", "Server" };

	public ItemDisk() {
		super();
		this.maxStackSize = 1;
		this.setHasSubtypes(true);
		this.setCreativeTab(Minestuck.tabMinestuck);
		this.setUnlocalizedName("disk");
	}

	@Override
	public IIcon getIconFromDamage(int meta) {
		return icons[meta];
	}

	@Override
	public void registerIcons(IIconRegister par1IIconRegister) {
		for (int i = 0; i < subNames.length; i++)
			icons[i] = par1IIconRegister.registerIcon("minestuck:"
					+ subNames[i] + "Disk");
	}

	@Override
	public String getUnlocalizedName(ItemStack itemstack) {
		return getUnlocalizedName() + "." + subNames[itemstack.getItemDamage()];
	}

	@Override
	public int getMetadata(int damageValue) {
		return damageValue;
	}

	@Override
	public boolean onItemUse(ItemStack item, EntityPlayer player, World world,
			int x, int y, int z, int side, float par8, float par9, float par10) {
		if (!world.isRemote) {
			if (world.getBlock(x, y, z) == Minestuck.blockComputerOff) {
				int meta = world.getBlockMetadata(x, y, z);
				world.setBlock(x, y, z, Minestuck.blockComputerOn);
				world.setBlockMetadataWithNotify(x, y, z, meta, 2);
			}
			if (world.getBlock(x, y, z) == Minestuck.blockComputerOn) {
				TileEntityComputer te = (TileEntityComputer) world
						.getTileEntity(x, y, z);
				if (te == null || te.installedPrograms.size() >= 2
						|| te.errored())
					return false;
				if (te.owner.isEmpty())
					te.owner = UsernameHandler.encode(player
							.getCommandSenderName());
				int i = item.getItemDamage();
				if (te.installedPrograms.contains(i))
					return false;
				te.installedPrograms.put(i, true);
				world.markBlockForUpdate(x, y, z);
				Debug.print("Installed program with id " + item.getItemDamage());
				
				player.destroyCurrentEquippedItem();

				if (te.gui != null)
					te.gui.updateGui();
				return true;
			}
		}
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs tab, List subItems) {
		for (int i = 0; i < subNames.length; i++)
			subItems.add(new ItemStack(this, 1, i));
	}
}
