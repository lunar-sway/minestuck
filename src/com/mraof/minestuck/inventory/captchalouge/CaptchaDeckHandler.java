package com.mraof.minestuck.inventory.captchalouge;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.advancements.MinestuckCriteriaTriggers;
import com.mraof.minestuck.client.ClientProxy;
import com.mraof.minestuck.item.ItemBoondollars;
import com.mraof.minestuck.item.MinestuckItems;
import com.mraof.minestuck.network.CaptchaDeckPacket;
import com.mraof.minestuck.network.MinestuckChannelHandler;
import com.mraof.minestuck.network.MinestuckPacket;
import com.mraof.minestuck.alchemy.AlchemyRecipes;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.util.MinestuckPlayerData;

import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class CaptchaDeckHandler
{
	
	private static Map<ResourceLocation, Class<? extends Modus>> modusClassMap = new HashMap<>();
	private static Map<ResourceLocation, ItemStack> modusItemMap = new HashMap<>();
	private static String[] metaConvert = new String[] {"stack", "queue", "queue_stack", "tree", "hashmap", "set"};
	
	public static final int EMPTY_SYLLADEX = -1;
	public static final int EMPTY_CARD = -2;
	
	public static Random rand;
	
	@SideOnly(Side.CLIENT)
	public static Modus clientSideModus;
	
	static
	{
		registerModusType(new ResourceLocation(Minestuck.MOD_ID, "stack"), StackModus.class, new ItemStack(MinestuckItems.modusCard, 1, 0));
		registerModusType(new ResourceLocation(Minestuck.MOD_ID, "queue"), QueueModus.class, new ItemStack(MinestuckItems.modusCard, 1, 1));
		registerModusType(new ResourceLocation(Minestuck.MOD_ID, "queue_stack"), QueuestackModus.class, new ItemStack(MinestuckItems.modusCard, 1, 2));
		registerModusType(new ResourceLocation(Minestuck.MOD_ID, "tree"), TreeModus.class, new ItemStack(MinestuckItems.modusCard, 1, 3));
		registerModusType(new ResourceLocation(Minestuck.MOD_ID, "hashmap"), HashmapModus.class, new ItemStack(MinestuckItems.modusCard, 1, 4));
		registerModusType(new ResourceLocation(Minestuck.MOD_ID, "set"), SetModus.class, new ItemStack(MinestuckItems.modusCard, 1, 5));
	}
	
	public static void registerModusType(ResourceLocation registryName, Class<? extends Modus> c, ItemStack item)
	{
		modusClassMap.put(registryName, c);
		modusItemMap.put(registryName, item);
	}
	
	public static Modus createInstance(ResourceLocation location, Side side)
	{
		Class<? extends Modus> c = modusClassMap.get(location);
		if(c == null)
			return null;
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
	
	public static boolean isInRegistry(ResourceLocation type)
	{
		return modusClassMap.containsKey(type);
	}
	
	public static ResourceLocation getType(Class<? extends Modus> modus)
	{
		for(Map.Entry<ResourceLocation, Class<? extends Modus>> entry : modusClassMap.entrySet())
			if(entry.getValue().equals(modus))
				return entry.getKey();
		return null;
	}
	
	public static ResourceLocation getType(ItemStack item)
	{
		for(Map.Entry<ResourceLocation, ItemStack> entry : modusItemMap.entrySet())
			if(ItemStack.areItemsEqual(entry.getValue(), item) && entry.getValue().getItemDamage() == item.getItemDamage())
				return entry.getKey();
		return null;
	}
	
	public static ItemStack getItem(ResourceLocation location)
	{
		return modusItemMap.get(location).copy();
	}
	
	public static void launchItem(EntityPlayer player, ItemStack item)
	{
		if(item.getItem().equals(MinestuckItems.captchaCard) && (!item.hasTagCompound() || !item.getTagCompound().hasKey("contentID")))
			while(item.getCount() > 0)
			{
				if(getModus(player).increaseSize())
					item.shrink(1);
				else break;
			}
		if(item.getCount() > 0)
			launchAnyItem(player, item);
	}
	
	public static void launchAnyItem(EntityPlayer player, ItemStack item)
	{
		EntityItem entity = new EntityItem(player.world, player.posX, player.posY+1, player.posZ, item);
		entity.motionX = rand.nextDouble() - 0.5;
		entity.motionZ = rand.nextDouble() - 0.5;
		entity.setDefaultPickupDelay();
		player.world.spawnEntity(entity);
	}
	
	public static void useItem(EntityPlayerMP player)
	{
		if(!(player.openContainer instanceof ContainerCaptchaDeck))
			 return;
		ContainerCaptchaDeck container = (ContainerCaptchaDeck) player.openContainer;
		if(container.inventory.getStackInSlot(0).isEmpty())
			return;
		ItemStack item = container.inventory.getStackInSlot(0);
		Modus modus = getModus(player);
		
		ResourceLocation type = getType(item);
		if(type != null)
		{
			if(modus == null)
			{
				MinestuckPlayerData.PlayerData data = MinestuckPlayerData.getData(player);
				modus = createInstance(type, Side.SERVER);
				modus.player = player;
				modus.initModus(null, data.givenModus ? 0 : MinestuckConfig.initialModusSize);
				data.givenModus = true;
				setModus(player, modus);
				container.inventory.setInventorySlotContents(0, ItemStack.EMPTY);
			}
			else
			{
				Modus oldModus = modus;
				ResourceLocation oldType = getType(oldModus.getClass());
				if(type.equals(oldType))
					return;
				modus = createInstance(type, Side.SERVER);
				modus.player = player;
				if(modus.canSwitchFrom(oldModus))
					modus.initModus(oldModus.getItems(), oldModus.getSize());
				else
				{
					for(ItemStack content : oldModus.getItems())
						if(!content.isEmpty())
							launchAnyItem(player, content);
					modus.initModus(null, oldModus.getSize());
				}
				
				setModus(player, modus);
				container.inventory.setInventorySlotContents(0, getItem(oldType));
			}
			
			MinestuckCriteriaTriggers.CHANGE_MODUS.trigger(player, modus);
		}
		else if(item.getItem().equals(MinestuckItems.captchaCard) && !AlchemyRecipes.isPunchedCard(item)
				&& modus != null)
		{
			ItemStack content = AlchemyRecipes.getDecodedItem(item, true);
			
			System.out.println(content);
			int failed = 0;
			for(int i = 0; i < item.getCount(); i++)
				if(!modus.increaseSize())
					failed++;
			
			if(!content.isEmpty())
				for(int i = 0; i < item.getCount() - failed; i++)
				{
					ItemStack toPut = content.copy();
					if(!modus.putItemStack(toPut))
						launchItem(player, toPut);
					else MinestuckCriteriaTriggers.CAPTCHALOGUE.trigger(player, modus, toPut);
				}
			
			if(failed == 0)
				container.inventory.setInventorySlotContents(0, ItemStack.EMPTY);
			else item.setCount(failed);
		}
		
		if(modus != null)
		{
			MinestuckPacket packet = MinestuckPacket.makePacket(MinestuckPacket.Type.CAPTCHA, CaptchaDeckPacket.DATA, writeToNBT(modus));
			MinestuckChannelHandler.sendToPlayer(packet, player);
		}
	}
	
	public static void captchalougeItem(EntityPlayerMP player)
	{
		ItemStack stack = player.getHeldItemMainhand();
		Modus modus = getModus(player);
		
		if(stack.getItem() == MinestuckItems.boondollars)
		{
			MinestuckPlayerData.addBoondollars(player, ItemBoondollars.getCount(stack));
			stack.setCount(0);
			return;
		}
		
		if(modus != null && !stack.isEmpty())
		{
			boolean card1 = false, card2 = true;
			if(stack.getItem() == MinestuckItems.captchaCard && AlchemyRecipes.hasDecodedItem(stack)
					&& !AlchemyRecipes.isPunchedCard(stack))
			{
				ItemStack newStack = AlchemyRecipes.getDecodedItem(stack, true);
				if(!newStack.isEmpty())
				{
					card1 = true;
					stack = newStack;
					card2 = modus.increaseSize();
				}
			}
			if(modus.putItemStack(stack))
			{
				MinestuckCriteriaTriggers.CAPTCHALOGUE.trigger(player, modus, stack);
				if(!card2)
					launchAnyItem(player, new ItemStack(MinestuckItems.captchaCard, 1));
				
				stack = player.getHeldItemMainhand();
				if(card1 && stack.getCount() > 1)
					stack.shrink(1);
				else player.setHeldItem(EnumHand.MAIN_HAND, ItemStack.EMPTY);
			}
			else if(card1 && card2)
			{
				launchAnyItem(player, stack);
				stack = player.getHeldItemMainhand();
				if(stack.getCount() == 1)
					player.setHeldItem(EnumHand.MAIN_HAND, ItemStack.EMPTY);
				else stack.shrink(1);
			}
			MinestuckPacket packet = MinestuckPacket.makePacket(MinestuckPacket.Type.CAPTCHA, CaptchaDeckPacket.DATA, writeToNBT(modus));
			MinestuckChannelHandler.sendToPlayer(packet, player);
		}
		
	}
	
	public static void captchalougeInventoryItem (EntityPlayerMP player, int slotIndex) {
		ItemStack stack;
		Modus modus = getModus(player);
		System.out.println("Raw Slot: " + slotIndex);
		//This statement is so that the server knows whether the item is in the hotbar or not because apparently THE "openContainer" CANT EDIT THE HOTBAR SLOTS.
		if(player.openContainer.equals(player.inventoryContainer) && player.inventory.isHotbar(slotIndex)) {
			int hotbarIndex = slotIndex;
			
			stack = player.inventory.mainInventory.get(hotbarIndex);

			if(stack.getItem() == MinestuckItems.boondollars)
			{
				MinestuckPlayerData.addBoondollars(player, ItemBoondollars.getCount(stack));
				stack.setCount(0);
				return;
			}

			if(modus != null && !stack.isEmpty())
			{
				boolean card1 = false, card2 = true;
				if(stack.getItem() == MinestuckItems.captchaCard && AlchemyRecipes.hasDecodedItem(stack)
						&& !AlchemyRecipes.isPunchedCard(stack))
				{
					ItemStack newStack = AlchemyRecipes.getDecodedItem(stack, true);
					if(!newStack.isEmpty())
					{
						card1 = true;
						stack = newStack;
						card2 = modus.increaseSize();
					}
				}
				if(modus.putItemStack(stack))
				{
					MinestuckCriteriaTriggers.CAPTCHALOGUE.trigger(player, modus, stack);
					if(!card2)
						launchAnyItem(player, new ItemStack(MinestuckItems.captchaCard, 1));
					stack = player.inventory.mainInventory.get(hotbarIndex);
					if(card1 && stack.getCount() > 1)
						stack.shrink(1);
					else {
						player.inventory.setInventorySlotContents(hotbarIndex, ItemStack.EMPTY);
					}
				}
				else if(card1 && card2)
				{
					launchAnyItem(player, stack);
					stack = player.inventory.mainInventory.get(hotbarIndex);
					if(stack.getCount() == 1) {
						player.inventory.setInventorySlotContents(hotbarIndex, ItemStack.EMPTY);
					} else stack.shrink(1);
				}
				MinestuckPacket packet = MinestuckPacket.makePacket(MinestuckPacket.Type.CAPTCHA, CaptchaDeckPacket.DATA, writeToNBT(modus));
				MinestuckChannelHandler.sendToPlayer(packet, player);
			}
		}
		else {
			Slot slot = player.openContainer.getSlot(slotIndex);
			stack = slot.getStack();

			if(stack.getItem() == MinestuckItems.boondollars)
			{
				MinestuckPlayerData.addBoondollars(player, ItemBoondollars.getCount(stack));
				stack.setCount(0);
				return;
			}

			if(modus != null && !stack.isEmpty())
			{
				boolean card1 = false, card2 = true;
				if(stack.getItem() == MinestuckItems.captchaCard && AlchemyRecipes.hasDecodedItem(stack)
						&& !AlchemyRecipes.isPunchedCard(stack))
				{
					ItemStack newStack = AlchemyRecipes.getDecodedItem(stack, true);
					if(!newStack.isEmpty())
					{
						card1 = true;
						stack = newStack;
						card2 = modus.increaseSize();
					}
				}
				if(modus.putItemStack(stack))
				{
					MinestuckCriteriaTriggers.CAPTCHALOGUE.trigger(player, modus, stack);
					if(!card2)
						launchAnyItem(player, new ItemStack(MinestuckItems.captchaCard, 1));
					stack = slot.getStack();
					if(card1 && stack.getCount() > 1)
						stack.shrink(1);
					else {
						slot.putStack(ItemStack.EMPTY);
					}
				}
				else if(card1 && card2)
				{
					launchAnyItem(player, stack);
					stack = slot.getStack();
					if(stack.getCount() == 1) {
						slot.putStack(ItemStack.EMPTY);
					} else stack.shrink(1);
				}
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
		if(!stack.isEmpty())
		{
			ItemStack otherStack = player.getHeldItemMainhand();
			if(otherStack.isEmpty())
				player.setHeldItem(EnumHand.MAIN_HAND, stack);
			else if(canMergeItemStacks(stack, otherStack))
			{
				otherStack.grow(stack.getCount());
				stack.setCount(0);
			}
			else
			{
				boolean placed = false;
				for(int i = 0; i < player.inventory.mainInventory.size(); i++)
				{
					otherStack = player.inventory.mainInventory.get(i);
					if(otherStack.isEmpty())
						player.inventory.mainInventory.set(i, stack.copy());
					else if(canMergeItemStacks(stack, otherStack))
						otherStack.grow(stack.getCount());
					else continue;
					
					stack.setCount(0);
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
		
		NonNullList<ItemStack> stacks = modus.getItems();
		int size = modus.getSize();
		int cardsToKeep = MinestuckConfig.sylladexDropMode == 2 ? 0 : MinestuckConfig.initialModusSize;
		
		if(!MinestuckConfig.dropItemsInCards || MinestuckConfig.sylladexDropMode == 0)
		{
			for(ItemStack stack : stacks)
				if(!stack.isEmpty())
					player.dropItem(stack, true, false);
		} else
			for(ItemStack stack : stacks)
				if(!stack.isEmpty())
					if(size > cardsToKeep)
					{
						ItemStack card = AlchemyRecipes.createCard(stack, false);
						player.dropItem(card, true, false);
						size--;
					} else player.dropItem(stack, true, false);
		
		int stackLimit = MinestuckItems.captchaCard.getItemStackLimit(new ItemStack(MinestuckItems.captchaCard));
		if(MinestuckConfig.sylladexDropMode >= 1)
			for(; size > cardsToKeep; size = Math.max(size - stackLimit, cardsToKeep))
				player.dropItem(new ItemStack(MinestuckItems.captchaCard, Math.min(stackLimit, size - cardsToKeep)), true, false);
		
		if(MinestuckConfig.sylladexDropMode == 2)
		{
			player.dropItem(getItem(getType(modus.getClass())), true, false);	//TODO Add a method to the modus to get the itemstack instead
			setModus(player, null);
		} else modus.initModus(null, size);
		
		MinestuckPacket packet = MinestuckPacket.makePacket(MinestuckPacket.Type.CAPTCHA, CaptchaDeckPacket.DATA, writeToNBT(getModus(player)));
		MinestuckChannelHandler.sendToPlayer(packet, player);
	}
	
	public static NBTTagCompound writeToNBT(Modus modus)
	{
		if(modus == null)
			return null;
		ResourceLocation name = getType(modus.getClass());
		NBTTagCompound nbt = modus.writeToNBT(new NBTTagCompound());
		nbt.setString("type", name.toString());
		return nbt;
	}
	
	public static Modus readFromNBT(NBTTagCompound nbt, boolean clientSide)
	{
		if(nbt == null)
			return null;
		Modus modus;
		ResourceLocation name;
		if(nbt.hasKey("type", 99))	//Integer from the old format
		{
			int i = nbt.getInteger("type");
			name = new ResourceLocation(Minestuck.MOD_ID, metaConvert[MathHelper.clamp(i, 0, 5)]);
		} else
		{
			name = new ResourceLocation(nbt.getString("type"));
		}
		
		if(clientSide && clientSideModus != null && name.equals(getType(clientSideModus.getClass())))
			modus = clientSideModus;
		else
		{
			modus = createInstance(name, clientSide ? Side.CLIENT : Side.SERVER);
			if(modus == null)
			{
				Debug.warnf("Failed to load modus from nbt with the name \"%s\"", name.toString());
				return null;
			}
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
				&& stack1.isStackable() && stack1.getCount() + stack2.getCount() < stack1.getMaxStackSize();
	}
}