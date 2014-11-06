package com.mraof.minestuck.inventory.captchalouge;

import java.util.HashMap;
import java.util.Random;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.network.CaptchaDeckPacket;
import com.mraof.minestuck.network.MinestuckChannelHandler;
import com.mraof.minestuck.network.MinestuckPacket;
import com.mraof.minestuck.util.AlchemyRecipeHandler;
import com.mraof.minestuck.util.UsernameHandler;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class CaptchaDeckHandler
{
	
	public static enum ModusType
	{
		STACK(StackModus.class),
		QUEUE(QueueModus.class);
		
		private final Class<? extends Modus> c;
		ModusType(Class<? extends Modus> c)
		{
			this.c = c;
		}
		
		public Modus createInstance()
		{
			try
			{
				return c.newInstance();
			} catch (Exception e)
			{
				e.printStackTrace();
				return null;
			}
		}
		
	}
	
	public static Random rand;
	
	@SideOnly(Side.CLIENT)
	public static Modus clientSideModus;
	
	public static HashMap<String, Modus> playerMap = new HashMap<String, Modus>();
	
	public static void launchItem(EntityPlayer player, ItemStack item)
	{
		boolean b = true;
		if(item.getItem().equals(Minestuck.captchaCard) && (!item.hasTagCompound() || !item.getTagCompound().hasKey("contentID")))
			b = !playerMap.get(UsernameHandler.encode(player.getCommandSenderName())).increaseSize();
		if(b)
			launchAnyItem(player, item);
	}
	
	public static void launchAnyItem(EntityPlayer player, ItemStack item)
	{
		//TODO Fill metod
	}
	
	public static void useItem(EntityPlayer player)
	{
		if(!(player.openContainer instanceof ContainerCaptchaDeck))
			return;
		ContainerCaptchaDeck container = (ContainerCaptchaDeck) player.openContainer;
		if(container.inventory.getStackInSlot(0) == null)
			return;
		ItemStack item = container.inventory.getStackInSlot(0);
		Modus modus = playerMap.get(UsernameHandler.encode(player.getCommandSenderName()));
		if(item.getItem().equals(Minestuck.captchaModus) && ModusType.values().length > item.getItemDamage())
		{
			if(modus == null)
			{
				modus = ModusType.values()[item.getItemDamage()].createInstance();
				modus.player = player;
				modus.initModus(null);
				playerMap.put(UsernameHandler.encode(player.getCommandSenderName()), modus);
				container.inventory.setInventorySlotContents(0, null);
			}
			else
			{
				Modus oldModus = modus;
				ModusType oldType = null;
				for(ModusType type : ModusType.values())
					if(type.c == modus.getClass())
					{
						oldType = type;
						break;
					}
				modus = ModusType.values()[item.getItemDamage()].createInstance();
				modus.player = player;
				if(modus.canSwitchFrom(oldType))
					modus.initModus(oldModus.getItems());
				else
				{
					for(ItemStack content : oldModus.getItems())
						launchAnyItem(player, content);
					modus.initModus(null);
				}
				
				if(oldType != null)
					item.setItemDamage(oldType.ordinal());
			}
			
		}
		else if(item.getItem().equals(Minestuck.captchaCard) && (!item.hasTagCompound() || !item.getTagCompound().getBoolean("punched")))
		{
			ItemStack content = AlchemyRecipeHandler.getDecodedItem(item, false);
			if(!modus.increaseSize())
				return;
			container.inventory.setInventorySlotContents(0, null);
			if(content != null && !modus.putItemStack(content))
				launchItem(player, content);
		}
		
		MinestuckPacket packet = MinestuckPacket.makePacket(MinestuckPacket.Type.CAPTCHA, CaptchaDeckPacket.DATA, modus.writeToNBT(new NBTTagCompound()));
		MinestuckChannelHandler.sendToPlayer(packet, player);
	}
	
}
