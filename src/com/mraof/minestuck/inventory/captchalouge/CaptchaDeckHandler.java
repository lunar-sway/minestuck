package com.mraof.minestuck.inventory.captchalouge;

import java.util.Random;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.client.ClientProxy;
import com.mraof.minestuck.item.MinestuckItems;
import com.mraof.minestuck.network.CaptchaDeckPacket;
import com.mraof.minestuck.network.MinestuckChannelHandler;
import com.mraof.minestuck.network.MinestuckPacket;
import com.mraof.minestuck.util.AlchemyRecipeHandler;
import com.mraof.minestuck.util.MinestuckPlayerData;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CaptchaDeckHandler
{
	
	public static enum ModusType
	{
		STACK(StackModus.class, 0),
		QUEUE(QueueModus.class, 1),
		QUEUE_STACK(QueuestackModus.class, 2),
		TREE(TreeModus.class, 3),
		HASHMAP(HashmapModus.class, 4),
		SET(SetModus.class, 5);
		
		private final Class<? extends Modus> c;
		public final int metadata;
		ModusType(Class<? extends Modus> c, int metadata)
		{
			this.c = c;
			this.metadata = metadata;
		}
		
		public Modus createInstance(Side side)
		{
			try
			{
				Modus modus = c.newInstance();
				modus.side = side;
				return modus;
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
		
		public static ModusType getType(int metadata)
		{
			for(ModusType type : values())
				if(type.metadata == metadata)
					return type;
			return null;
		}
		
	}
	
	public static final int EMPTY_SYLLADEX = -1;
	public static final int EMPTY_CARD = -2;
	
	public static Random rand;
	
	@SideOnly(Side.CLIENT)
	public static Modus clientSideModus;
	
	public static void launchItem(EntityPlayer player, ItemStack item)
	{
		if(item.getItem().equals(MinestuckItems.captchaCard) && (!item.hasTagCompound() || !item.getTagCompound().hasKey("contentID")))
			while(item.stackSize > 0)
			{
				if(getModus(player).increaseSize())
					item.stackSize--;
				else break;
			}
		if(item.stackSize > 0)
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
		
		if(item.getItem().equals(MinestuckItems.modusCard) && ModusType.getType(item.getItemDamage()) != null)
		{
			if(modus == null)
			{
				modus = ModusType.getType(item.getItemDamage()).createInstance(Side.SERVER);
				modus.player = player;
				modus.initModus(null, MinestuckConfig.initialModusSize);
				setModus(player, modus);
				container.inventory.setInventorySlotContents(0, null);
			}
			else
			{
				Modus oldModus = modus;
				ModusType oldType = ModusType.getType(oldModus);
				if(oldType.metadata == item.getItemDamage())
					return;
				modus = ModusType.getType(item.getItemDamage()).createInstance(Side.SERVER);
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
				item.setItemDamage(oldType.metadata);
			}
			
		}
		else if(item.getItem().equals(MinestuckItems.captchaCard) && (!item.hasTagCompound() || !item.getTagCompound().getBoolean("punched"))
				&& modus != null)
		{
			ItemStack content = AlchemyRecipeHandler.getDecodedItem(item);
			int failed = 0;
			for(int i = 0; i < item.stackSize; i++)
				if(!modus.increaseSize())
					failed++;
			
			if(content != null)
				for(int i = 0; i < item.stackSize - failed; i++)
				{
					ItemStack toPut = content.copy();
					if(!modus.putItemStack(toPut))
						launchItem(player, toPut);
				}
			
			if(failed == 0)
				container.inventory.setInventorySlotContents(0, null);
			else item.stackSize = failed;
		}
		
		if(modus != null)
		{
			MinestuckPacket packet = MinestuckPacket.makePacket(MinestuckPacket.Type.CAPTCHA, CaptchaDeckPacket.DATA, writeToNBT(modus));
			MinestuckChannelHandler.sendToPlayer(packet, player);
		}
	}
	
	public static void captchalougeItem(EntityPlayerMP player)
	{
		ItemStack item = player.getHeldItemMainhand();
		Modus modus = getModus(player);
		if(modus != null && item != null)
		{
			boolean card1 = false, card2 = true;
			if(item.getItem() == MinestuckItems.captchaCard && item.hasTagCompound() && !item.getTagCompound().getBoolean("punched"))
			{
				ItemStack newItem = AlchemyRecipeHandler.getDecodedItem(item);
				if(newItem != null)
				{
					card1 = true;
					item = newItem;
					card2 = modus.increaseSize();
				}
			}
			if(modus.putItemStack(item))
			{
				if(!card2)
					launchAnyItem(player, new ItemStack(MinestuckItems.captchaCard, 1));
				
				item = player.getHeldItemMainhand();
				if(card1 && item.stackSize > 1)
					item.stackSize--;
				else player.setHeldItem(EnumHand.MAIN_HAND, null);
				
			}
			else if(card1 && card2)
			{
				launchAnyItem(player, item);
				item = player.getHeldItemMainhand();
				if(item.stackSize == 1)
					player.setHeldItem(EnumHand.MAIN_HAND, null);
				else item.stackSize--;
			}
			MinestuckPacket packet = MinestuckPacket.makePacket(MinestuckPacket.Type.CAPTCHA, CaptchaDeckPacket.DATA, writeToNBT(modus));
			MinestuckChannelHandler.sendToPlayer(packet, player);
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
			ItemStack otherStack = player.getHeldItemMainhand();
			if(otherStack == null)
				player.setHeldItem(EnumHand.MAIN_HAND, stack);
			else if(canMergeItemStacks(stack, otherStack))
			{
				otherStack.stackSize += stack.stackSize;
				stack.stackSize = 0;
			}
			else
			{
				boolean placed = false;
				for(int i = 0; i < player.inventory.mainInventory.length; i++)
				{
					otherStack = player.inventory.mainInventory[i];
					if(otherStack == null)
						player.inventory.mainInventory[i] = stack.copy();
					else if(canMergeItemStacks(stack, otherStack))
						otherStack.stackSize += stack.stackSize;
					else continue;
					
					stack.stackSize = 0;
					placed = true;
					player.inventory.markDirty();
					player.inventoryContainer.detectAndSendChanges();
					break;
				}
				if(!placed)
					launchAnyItem(player, stack);
			}
		}
		MinestuckPacket packet = MinestuckPacket.makePacket(MinestuckPacket.Type.CAPTCHA, CaptchaDeckPacket.DATA, writeToNBT(modus));
		MinestuckChannelHandler.sendToPlayer(packet, player);
	}
	
	public static void dropSylladex(EntityPlayer player)
	{
		Modus modus = getModus(player);
		
		if(modus == null)
			return;
		
		ItemStack[] stacks = modus.getItems();
		int size = modus.getSize();
		int cardsToKeep = MinestuckConfig.sylladexDropMode == 2 ? 0 : MinestuckConfig.initialModusSize;
		
		if(!MinestuckConfig.dropItemsInCards || MinestuckConfig.sylladexDropMode == 0)
		{
			for(ItemStack stack : stacks)
				if(stack != null)
					player.dropItem(stack, true, false);
		} else
			for(ItemStack stack : stacks)
				if(stack != null)
					if(size > cardsToKeep)
					{
						ItemStack card = AlchemyRecipeHandler.createCard(stack, false);
						player.dropItem(card, true, false);
						size--;
					} else player.dropItem(stack, true, false);
		
		if(MinestuckConfig.sylladexDropMode >= 1)
			for(; size > cardsToKeep; size = Math.max(size - 16, cardsToKeep))
				player.dropItem(new ItemStack(MinestuckItems.captchaCard, Math.min(16, size - cardsToKeep)), true, false);
		
		if(MinestuckConfig.sylladexDropMode == 2)
		{
			player.dropItem(new ItemStack(MinestuckItems.modusCard, 1, ModusType.getType(modus).metadata), true, false);
			setModus(player, null);
		} else modus.initModus(null, size);
		
		MinestuckPacket packet = MinestuckPacket.makePacket(MinestuckPacket.Type.CAPTCHA, CaptchaDeckPacket.DATA, writeToNBT(getModus(player)));
		MinestuckChannelHandler.sendToPlayer(packet, player);
	}
	
	public static NBTTagCompound writeToNBT(Modus modus)
	{
		if(modus == null)
			return null;
		int index = ModusType.getType(modus).metadata;
		NBTTagCompound nbt = modus.writeToNBT(new NBTTagCompound());
		nbt.setInteger("type", index);
		return nbt;
	}
	
	public static Modus readFromNBT(NBTTagCompound nbt, boolean clientSide)
	{
		if(nbt == null)
			return null;
		Modus modus;
		if(clientSide && clientSideModus != null && nbt.getInteger("type") == ModusType.getType(clientSideModus).metadata)
			modus = clientSideModus;
		else
		{
			modus = ModusType.getType(nbt.getInteger("type")).createInstance(clientSide ? Side.CLIENT : Side.SERVER);
			if(clientSide)
				modus.player = ClientProxy.getClientPlayer();
		}
		modus.readFromNBT(nbt);
		return modus;
	}
	
	public static Modus getModus(EntityPlayer player)
	{
		return MinestuckPlayerData.getData(player).modus;
	}
	
	public static void setModus(EntityPlayer player, Modus modus)
	{
		MinestuckPlayerData.getData(player).modus = modus;
		if(modus != null)
			MinestuckPlayerData.getData(player).givenModus = true;
	}
	
	private static boolean canMergeItemStacks(ItemStack stack1, ItemStack stack2)
	{
		return stack1.getItem() == stack2.getItem() && (!stack1.getHasSubtypes() || stack1.getMetadata() == stack2.getMetadata()) && ItemStack.areItemStackTagsEqual(stack1, stack2)
				&& stack1.isStackable() && stack1.stackSize + stack2.stackSize < stack1.getMaxStackSize();
	}
}
