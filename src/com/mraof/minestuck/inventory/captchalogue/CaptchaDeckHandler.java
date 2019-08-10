package com.mraof.minestuck.inventory.captchalogue;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.advancements.MinestuckCriteriaTriggers;
import com.mraof.minestuck.item.BoondollarsItem;
import com.mraof.minestuck.item.MinestuckItems;
import com.mraof.minestuck.network.CaptchaDeckPacket;
import com.mraof.minestuck.network.MinestuckPacketHandler;
import com.mraof.minestuck.alchemy.AlchemyRecipes;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.world.storage.PlayerSavedData;

import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.LogicalSide;

import java.util.Random;

public class CaptchaDeckHandler
{
	public static final int EMPTY_SYLLADEX = -1;
	public static final int EMPTY_CARD = -2;
	
	public static Random rand;
	
	public static Modus clientSideModus;
	
	public static Modus createModus(ResourceLocation name, LogicalSide side)
	{
		ModusType<?> type = ModusTypes.REGISTRY.getValue(name);
		return type != null ? type.create(side) : null;
	}
	
	public static void launchItem(ServerPlayerEntity player, ItemStack item)
	{
		if(item.getItem().equals(MinestuckItems.CAPTCHA_CARD) && !AlchemyRecipes.hasDecodedItem(item))
			while(item.getCount() > 0)
			{
				if(getModus(player).increaseSize(player))
					item.shrink(1);
				else break;
			}
		if(item.getCount() > 0)
			launchAnyItem(player, item);
	}
	
	public static void launchAnyItem(PlayerEntity player, ItemStack item)
	{
		ItemEntity entity = new ItemEntity(player.world, player.posX, player.posY+1, player.posZ, item);
		entity.setMotion(rand.nextDouble() - 0.5, entity.getMotion().y, rand.nextDouble() - 0.5);
		entity.setDefaultPickupDelay();
		player.world.addEntity(entity);
	}
	
	public static void useItem(ServerPlayerEntity player)
	{
		if(!(player.openContainer instanceof CaptchaDeckContainer))
			 return;
		CaptchaDeckContainer container = (CaptchaDeckContainer) player.openContainer;
		if(container.inventory.getStackInSlot(0).isEmpty())
			return;
		ItemStack stack = container.inventory.getStackInSlot(0);
		Modus modus = getModus(player);
		
		ModusType<?> type = ModusTypes.getTypeFromItem(stack.getItem());
		if(type != null)
		{
			if(modus == null)
			{
				PlayerSavedData.PlayerData data = PlayerSavedData.getData(player);
				modus = type.create(LogicalSide.SERVER);
				modus.initModus(player, null, data.givenModus ? 0 : MinestuckConfig.initialModusSize);
				data.givenModus = true;
				setModus(player, modus);
				container.inventory.setInventorySlotContents(0, ItemStack.EMPTY);
			}
			else
			{
				Modus oldModus = modus;
				ModusType<?> oldType = oldModus.getType();
				if(type.equals(oldType))
					return;
				modus = type.create(LogicalSide.SERVER);
				if(modus.canSwitchFrom(oldModus))
					modus.initModus(player, oldModus.getItems(), oldModus.getSize());
				else
				{
					for(ItemStack content : oldModus.getItems())
						if(!content.isEmpty())
							launchAnyItem(player, content);
					modus.initModus(player, null, oldModus.getSize());
				}
				
				setModus(player, modus);
				container.inventory.setInventorySlotContents(0, oldType.getStack().copy());
			}
			
			MinestuckCriteriaTriggers.CHANGE_MODUS.trigger(player, modus);
		}
		else if(stack.getItem().equals(MinestuckItems.CAPTCHA_CARD) && !AlchemyRecipes.isPunchedCard(stack)
				&& modus != null)
		{
			ItemStack content = AlchemyRecipes.getDecodedItem(stack, true);
			
			System.out.println(content);
			int failed = 0;
			for(int i = 0; i < stack.getCount(); i++)
				if(!modus.increaseSize(player))
					failed++;
			
			if(!content.isEmpty())
				for(int i = 0; i < stack.getCount() - failed; i++)
				{
					ItemStack toPut = content.copy();
					if(!modus.putItemStack(player, toPut))
						launchItem(player, toPut);
					else MinestuckCriteriaTriggers.CAPTCHALOGUE.trigger(player, modus, toPut);
				}
			
			if(failed == 0)
				container.inventory.setInventorySlotContents(0, ItemStack.EMPTY);
			else stack.setCount(failed);
		}
		
		if(modus != null)
		{
			CaptchaDeckPacket packet = CaptchaDeckPacket.data(writeToNBT(modus));
			MinestuckPacketHandler.sendToPlayer(packet, player);
		}
	}
	
	public static void captchalogueItem(ServerPlayerEntity player)
	{
		ItemStack stack = player.getHeldItemMainhand();
		Modus modus = getModus(player);
		
		if(stack.getItem() == MinestuckItems.BOONDOLLARS)
		{
			PlayerSavedData.addBoondollars(player, BoondollarsItem.getCount(stack));
			stack.setCount(0);
			return;
		}
		
		if(modus != null && !stack.isEmpty())
		{
			boolean card1 = false, card2 = true;
			if(stack.getItem() == MinestuckItems.CAPTCHA_CARD && AlchemyRecipes.hasDecodedItem(stack)
					&& !AlchemyRecipes.isPunchedCard(stack))
			{
				ItemStack newStack = AlchemyRecipes.getDecodedItem(stack, true);
				if(!newStack.isEmpty())
				{
					card1 = true;
					stack = newStack;
					card2 = modus.increaseSize(player);
				}
			}
			if(modus.putItemStack(player, stack))
			{
				MinestuckCriteriaTriggers.CAPTCHALOGUE.trigger(player, modus, stack);
				if(!card2)
					launchAnyItem(player, new ItemStack(MinestuckItems.CAPTCHA_CARD, 1));
				
				stack = player.getHeldItemMainhand();
				if(card1 && stack.getCount() > 1)
					stack.shrink(1);
				else player.setHeldItem(Hand.MAIN_HAND, ItemStack.EMPTY);
			}
			else if(card1 && card2)
			{
				launchAnyItem(player, stack);
				stack = player.getHeldItemMainhand();
				if(stack.getCount() == 1)
					player.setHeldItem(Hand.MAIN_HAND, ItemStack.EMPTY);
				else stack.shrink(1);
			}
			CaptchaDeckPacket packet = CaptchaDeckPacket.data(writeToNBT(modus));
			MinestuckPacketHandler.sendToPlayer(packet, player);
		}
		
	}
	
	public static void captchalogueInventoryItem (ServerPlayerEntity player, int slotIndex) {
		ItemStack stack;
		Modus modus = getModus(player);
		System.out.println("Raw Slot: " + slotIndex);
		//This statement is so that the server knows whether the item is in the hotbar or not because apparently THE "openContainer" CANT EDIT THE HOTBAR SLOTS.
		if(player.openContainer.equals(player.container) && PlayerInventory.isHotbar(slotIndex)) {
			int hotbarIndex = slotIndex;
			
			stack = player.inventory.mainInventory.get(hotbarIndex);

			if(stack.getItem() == MinestuckItems.BOONDOLLARS)
			{
				PlayerSavedData.addBoondollars(player, BoondollarsItem.getCount(stack));
				stack.setCount(0);
				return;
			}

			if(modus != null && !stack.isEmpty())
			{
				boolean card1 = false, card2 = true;
				if(stack.getItem() == MinestuckItems.CAPTCHA_CARD && AlchemyRecipes.hasDecodedItem(stack)
						&& !AlchemyRecipes.isPunchedCard(stack))
				{
					ItemStack newStack = AlchemyRecipes.getDecodedItem(stack, true);
					if(!newStack.isEmpty())
					{
						card1 = true;
						stack = newStack;
						card2 = modus.increaseSize(player);
					}
				}
				if(modus.putItemStack(player, stack))
				{
					MinestuckCriteriaTriggers.CAPTCHALOGUE.trigger(player, modus, stack);
					if(!card2)
						launchAnyItem(player, new ItemStack(MinestuckItems.CAPTCHA_CARD, 1));
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
				CaptchaDeckPacket packet = CaptchaDeckPacket.data(writeToNBT(modus));
				MinestuckPacketHandler.sendToPlayer(packet, player);
			}
		}
		else {
			Slot slot = player.openContainer.getSlot(slotIndex);
			stack = slot.getStack();

			if(stack.getItem() == MinestuckItems.BOONDOLLARS)
			{
				PlayerSavedData.addBoondollars(player, BoondollarsItem.getCount(stack));
				stack.setCount(0);
				return;
			}

			if(modus != null && !stack.isEmpty())
			{
				boolean card1 = false, card2 = true;
				if(stack.getItem() == MinestuckItems.CAPTCHA_CARD && AlchemyRecipes.hasDecodedItem(stack)
						&& !AlchemyRecipes.isPunchedCard(stack))
				{
					ItemStack newStack = AlchemyRecipes.getDecodedItem(stack, true);
					if(!newStack.isEmpty())
					{
						card1 = true;
						stack = newStack;
						card2 = modus.increaseSize(player);
					}
				}
				if(modus.putItemStack(player, stack))
				{
					MinestuckCriteriaTriggers.CAPTCHALOGUE.trigger(player, modus, stack);
					if(!card2)
						launchAnyItem(player, new ItemStack(MinestuckItems.CAPTCHA_CARD, 1));
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
				CaptchaDeckPacket packet = CaptchaDeckPacket.data(writeToNBT(modus));
				MinestuckPacketHandler.sendToPlayer(packet, player);
			}
		}
	}

	public static void getItem(ServerPlayerEntity player, int index, boolean asCard)
	{
		Modus modus = getModus(player);
		if(modus == null)
			return;
		ItemStack stack = modus.getItem(player, index, asCard);
		if(!stack.isEmpty())
		{
			ItemStack otherStack = player.getHeldItemMainhand();
			if(otherStack.isEmpty())
				player.setHeldItem(Hand.MAIN_HAND, stack);
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
					player.container.detectAndSendChanges();
					break;
				}
				if(!placed)
					launchAnyItem(player, stack);
			}
		}
		CaptchaDeckPacket packet = CaptchaDeckPacket.data(writeToNBT(modus));
		MinestuckPacketHandler.sendToPlayer(packet, player);
	}
	
	public static void dropSylladex(ServerPlayerEntity player)
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
		
		int stackLimit = MinestuckItems.CAPTCHA_CARD.getItemStackLimit(new ItemStack(MinestuckItems.CAPTCHA_CARD));
		if(MinestuckConfig.sylladexDropMode >= 1)
			for(; size > cardsToKeep; size = Math.max(size - stackLimit, cardsToKeep))
				player.dropItem(new ItemStack(MinestuckItems.CAPTCHA_CARD, Math.min(stackLimit, size - cardsToKeep)), true, false);
		
		if(MinestuckConfig.sylladexDropMode == 2)
		{
			player.dropItem(modus.getType().getStack().copy(), true, false);	//TODO Add a method to the modus to get the itemstack instead
			setModus(player, null);
		} else modus.initModus(player, null, size);
		
		CaptchaDeckPacket packet = CaptchaDeckPacket.data(writeToNBT(getModus(player)));
		MinestuckPacketHandler.sendToPlayer(packet, player);
	}
	
	public static CompoundNBT writeToNBT(Modus modus)
	{
		if(modus == null)
			return null;
		
		ResourceLocation name = modus.getType().getRegistryName();
		if(name != null)
		{
			CompoundNBT nbt = modus.writeToNBT(new CompoundNBT());
			nbt.putString("type", name.toString());
			return nbt;
		} else return null;
	}
	
	public static Modus readFromNBT(CompoundNBT nbt, boolean clientSide)
	{
		if(nbt == null)
			return null;
		Modus modus;
		ResourceLocation name = new ResourceLocation(nbt.getString("type"));
		
		if(clientSide && clientSideModus != null && name.equals(clientSideModus.getType().getRegistryName()))
			modus = clientSideModus;
		else
		{
			modus = createModus(name, clientSide ? LogicalSide.CLIENT : LogicalSide.SERVER);
			if(modus == null)
			{
				Debug.warnf("Failed to load modus from nbt with the name \"%s\"", name.toString());
				return null;
			}
		}
		modus.readFromNBT(nbt);
		return modus;
	}
	
	public static Modus getModus(ServerPlayerEntity player)
	{
		return PlayerSavedData.getData(player).modus;
	}
	
	public static void setModus(ServerPlayerEntity player, Modus modus)
	{
		PlayerSavedData.getData(player).modus = modus;
		if(modus != null)
			PlayerSavedData.getData(player).givenModus = true;
		PlayerSavedData.get(player.world).markDirty();
	}
	
	private static boolean canMergeItemStacks(ItemStack stack1, ItemStack stack2)
	{
		return stack1.getItem() == stack2.getItem() && ItemStack.areItemStackTagsEqual(stack1, stack2)
				&& stack1.isStackable() && stack1.getCount() + stack2.getCount() < stack1.getMaxStackSize();
	}
}