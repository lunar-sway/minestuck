package com.mraof.minestuck.inventory.captchalouge;

import java.util.HashMap;
import java.util.Random;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.client.ClientProxy;
import com.mraof.minestuck.network.CaptchaDeckPacket;
import com.mraof.minestuck.network.MinestuckChannelHandler;
import com.mraof.minestuck.network.MinestuckPacket;
import com.mraof.minestuck.util.AlchemyRecipeHandler;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.util.MinestuckPlayerData;
import com.mraof.minestuck.util.UsernameHandler;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CaptchaDeckHandler
{
	
	public static enum ModusType
	{
		STACK(StackModus.class),
		QUEUE(QueueModus.class),
		QUEUE_STACK(QueuestackModus.class),
		TREE(TreeModus.class);
		
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
		
		public static ModusType getType(Modus modus)
		{
			for(ModusType type : values())
				if(type.c == modus.getClass())
					return type;
			return null;
		}
		
	}
	
	public static final ModusType[] modusList = {ModusType.STACK, ModusType.QUEUE};
	
	public static final int EMPTY_SYLLADEX = -1;
	public static final int EMPTY_CARD = -2;
	
	public static Random rand;
	
	@SideOnly(Side.CLIENT)
	public static Modus clientSideModus;
	
	public static void launchItem(EntityPlayer player, ItemStack item)
	{
		boolean b = true;
		if(item.getItem().equals(Minestuck.captchaCard) && (!item.hasTagCompound() || !item.getTagCompound().hasKey("contentID")))
			while(item.stackSize > 0)
			{
				b = !getModus(player).increaseSize();
				if(!b)
					item.stackSize--;
				else break;
			}
		if(b)
			launchAnyItem(player, item);
	}
	
	public static void launchAnyItem(EntityPlayer player, ItemStack item)
	{
		EntityItem entity = new EntityItem(player.worldObj, player.posX, player.posY+1, player.posZ, item);
		entity.motionX = rand.nextDouble() - 0.5;
		entity.motionZ = rand.nextDouble() - 0.5;
		entity.setDefaultPickupDelay();
		player.worldObj.spawnEntityInWorld(entity);
	}
	
	public static void useItem(EntityPlayerMP player)
	{
		if(!(player.openContainer instanceof ContainerCaptchaDeck))
			return;
		ContainerCaptchaDeck container = (ContainerCaptchaDeck) player.openContainer;
		if(container.inventory.getStackInSlot(0) == null)
			return;
		ItemStack item = container.inventory.getStackInSlot(0);
		Modus modus = getModus(player);
		
		if(item.getItem().equals(Minestuck.captchaModus) && ModusType.values().length > item.getItemDamage())
		{
			if(modus == null)
			{
				modus = ModusType.values()[item.getItemDamage()].createInstance();
				modus.player = player;
				modus.initModus(null, Minestuck.defaultModusSize);
				setModus(player, modus);
				container.inventory.setInventorySlotContents(0, null);
			}
			else
			{
				Modus oldModus = modus;
				ModusType oldType = ModusType.getType(oldModus);
				if(oldType.ordinal() == item.getItemDamage())
					return;
				modus = ModusType.values()[item.getItemDamage()].createInstance();
				modus.player = player;
				if(modus.canSwitchFrom(oldType))
					modus.initModus(oldModus.getItems(), oldModus.getSize());
				else
				{
					for(ItemStack content : oldModus.getItems())
						if(content != null)
							launchAnyItem(player, content);
					modus.initModus(null, oldModus.getSize());
				}
				
				setModus(player, modus);
				item.setItemDamage(oldType.ordinal());
			}
			
		}
		else if(item.getItem().equals(Minestuck.captchaCard) && (!item.hasTagCompound() || !item.getTagCompound().getBoolean("punched"))
				&& modus != null)
		{
			ItemStack content = AlchemyRecipeHandler.getDecodedItem(item, false);
			if(!modus.increaseSize())
				return;
			container.inventory.setInventorySlotContents(0, null);
			if(content != null && !modus.putItemStack(content))
				launchItem(player, content);
		}
		
		if(modus != null)
		{
			MinestuckPacket packet = MinestuckPacket.makePacket(MinestuckPacket.Type.CAPTCHA, CaptchaDeckPacket.DATA, writeToNBT(modus));
			MinestuckChannelHandler.sendToPlayer(packet, player);
		}
	}
	
	public static void captchalougeItem(EntityPlayerMP player)
	{
		ItemStack item = player.getCurrentEquippedItem();
		Modus modus = getModus(player);
		if(modus != null && item != null)
		{
			boolean card = true;
			if(item.getItem() == Minestuck.captchaCard && item.hasTagCompound() && !item.getTagCompound().getBoolean("punched"))
			{
				ItemStack newItem = AlchemyRecipeHandler.getDecodedItem(item, false);
				if(newItem != null)
				{
					item = newItem;
					card = modus.increaseSize();
					player.setCurrentItemOrArmor(0, item);	//To prevent problems when increaseSize succeeds and putItemStack fails.
				}
			}
			if(modus.putItemStack(item))
			{
				if(card)
					player.setCurrentItemOrArmor(0, null);
				else player.setCurrentItemOrArmor(0, new ItemStack(Minestuck.captchaCard));
				MinestuckPacket packet = MinestuckPacket.makePacket(MinestuckPacket.Type.CAPTCHA, CaptchaDeckPacket.DATA, writeToNBT(modus));
				MinestuckChannelHandler.sendToPlayer(packet, player);
			}
		}
		
	}
	
	public static void getItem(EntityPlayerMP player, int index, boolean asCard)
	{
		Modus modus = getModus(player);
		if(modus == null)
			return;
		ItemStack stack = modus.getItem(index, asCard);
		if(stack != null)
		{
			ItemStack otherStack = player.getCurrentEquippedItem();
			if(otherStack == null)
				player.setCurrentItemOrArmor(0, stack);
			else if(stack.getItem() == otherStack.getItem() && stack.getItemDamage() == otherStack.getItemDamage()
					&& ItemStack.areItemStackTagsEqual(stack, otherStack) && stack.stackSize + otherStack.stackSize <= stack.getMaxStackSize())
			{
				stack.stackSize += otherStack.stackSize;
				player.setCurrentItemOrArmor(0, stack);
			}
			else
			{
				boolean placed = false;
				for(int i = 0; i < player.inventory.mainInventory.length; i++)
				{
					otherStack = player.inventory.mainInventory[i];
					if(otherStack != null && stack.getItem() == otherStack.getItem() && stack.getItemDamage() == otherStack.getItemDamage()
							&& ItemStack.areItemStackTagsEqual(stack, otherStack) && stack.stackSize + otherStack.stackSize <= stack.getMaxStackSize())
						stack.stackSize += otherStack.stackSize;
					else if(otherStack != null) continue;
					
					player.inventory.mainInventory[i] = stack;
					placed = true;
					break;
				}
				if(!placed)
					launchAnyItem(player, stack);
			}
		}
		MinestuckPacket packet = MinestuckPacket.makePacket(MinestuckPacket.Type.CAPTCHA, CaptchaDeckPacket.DATA, writeToNBT(modus));
		MinestuckChannelHandler.sendToPlayer(packet, player);
	}
	
	public static NBTTagCompound writeToNBT(Modus modus)
	{
		int index = ModusType.getType(modus).ordinal();
		NBTTagCompound nbt = modus.writeToNBT(new NBTTagCompound());
		nbt.setInteger("type", index);
		return nbt;
	}
	
	public static Modus readFromNBT(NBTTagCompound nbt, boolean clientSide)
	{
		Modus modus;
		if(clientSide && clientSideModus != null && nbt.getInteger("type") == ModusType.getType(clientSideModus).ordinal())
			modus = clientSideModus;
		else
		{
			modus = ModusType.values()[nbt.getInteger("type")].createInstance();
			if(clientSide)
				modus.player = ClientProxy.getPlayer();
		}
		modus.readFromNBT(nbt);
		return modus;
	}
	
	public static Modus getModus(EntityPlayer player)
	{
		return MinestuckPlayerData.getData(UsernameHandler.encode(player.getName())).modus;
	}
	
	public static void setModus(EntityPlayer player, Modus modus)
	{
		MinestuckPlayerData.getData(UsernameHandler.encode(player.getName())).modus = modus;
	}
	
}
