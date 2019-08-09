package com.mraof.minestuck.inventory.captchalogue;

import com.mraof.minestuck.Minestuck;
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
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.LogicalSide;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;

public class CaptchaDeckHandler
{
	public static final ResourceLocation STACK = new ResourceLocation(Minestuck.MOD_ID, "stack");
	public static final ResourceLocation QUEUE = new ResourceLocation(Minestuck.MOD_ID, "queue");
	public static final ResourceLocation QUEUE_STACK = new ResourceLocation(Minestuck.MOD_ID, "queue_stack");
	public static final ResourceLocation TREE = new ResourceLocation(Minestuck.MOD_ID, "tree");
	public static final ResourceLocation HASH_MAP = new ResourceLocation(Minestuck.MOD_ID, "hash_map");
	public static final ResourceLocation SET = new ResourceLocation(Minestuck.MOD_ID, "set");
	
	private static Map<ResourceLocation, Function<LogicalSide, Modus>> modusConstructors = new HashMap<>();
	private static Map<ResourceLocation, ItemStack> modusItemMap = new HashMap<>();
	private static String[] metaConvert = new String[] {"stack", "queue", "queue_stack", "tree", "hash_map", "set"};
	
	public static final int EMPTY_SYLLADEX = -1;
	public static final int EMPTY_CARD = -2;
	
	public static Random rand;
	
	public static Modus clientSideModus;
	
	static
	{
		registerModusType(STACK, StackModus::new, new ItemStack(MinestuckItems.STACK_MODUS_CARD));
		registerModusType(QUEUE, QueueModus::new, new ItemStack(MinestuckItems.QUEUE_MODUS_CARD));
		registerModusType(QUEUE_STACK, QueueStackModus::new, new ItemStack(MinestuckItems.QUEUESTACK_MODUS_CARD));
		registerModusType(TREE, TreeModus::new, new ItemStack(MinestuckItems.TREE_MODUS_CARD));
		registerModusType(HASH_MAP, HashMapModus::new, new ItemStack(MinestuckItems.HASHMAP_MODUS_CARD));
		registerModusType(SET, SetModus::new, new ItemStack(MinestuckItems.SET_MODUS_CARD));
	}
	
	public static void registerModusType(ResourceLocation registryName, Function<LogicalSide, Modus> provider, ItemStack item)
	{
		modusConstructors.put(registryName, provider);
		modusItemMap.put(registryName, item);
	}
	
	public static Modus createInstance(ResourceLocation location, LogicalSide side)
	{
		Function<LogicalSide, Modus> provider = modusConstructors.get(location);
		if(provider == null)
			return null;
		return provider.apply(side);
	}
	
	public static boolean isInRegistry(ResourceLocation type)
	{
		return modusConstructors.containsKey(type);
	}
	
	public static ResourceLocation getType(ItemStack item)
	{
		for(Map.Entry<ResourceLocation, ItemStack> entry : modusItemMap.entrySet())
			if(ItemStack.areItemsEqual(entry.getValue(), item))
				return entry.getKey();
		return null;
	}
	
	public static ItemStack getItem(ResourceLocation location)
	{
		return modusItemMap.get(location).copy();
	}
	
	public static void launchItem(ServerPlayerEntity player, ItemStack item)
	{
		if(item.getItem().equals(MinestuckItems.CAPTCHA_CARD) && (!item.hasTag() || !item.getTag().contains("contentID")))
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
		ItemStack item = container.inventory.getStackInSlot(0);
		Modus modus = getModus(player);
		
		ResourceLocation type = getType(item);
		if(type != null)
		{
			if(modus == null)
			{
				PlayerSavedData.PlayerData data = PlayerSavedData.getData(player);
				modus = createInstance(type, LogicalSide.SERVER);
				modus.initModus(player, null, data.givenModus ? 0 : MinestuckConfig.initialModusSize);
				data.givenModus = true;
				setModus(player, modus);
				container.inventory.setInventorySlotContents(0, ItemStack.EMPTY);
			}
			else
			{
				Modus oldModus = modus;
				ResourceLocation oldType = oldModus.getRegistryName();
				if(type.equals(oldType))
					return;
				modus = createInstance(type, LogicalSide.SERVER);
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
				container.inventory.setInventorySlotContents(0, getItem(oldType));
			}
			
			MinestuckCriteriaTriggers.CHANGE_MODUS.trigger(player, modus);
		}
		else if(item.getItem().equals(MinestuckItems.CAPTCHA_CARD) && !AlchemyRecipes.isPunchedCard(item)
				&& modus != null)
		{
			ItemStack content = AlchemyRecipes.getDecodedItem(item, true);
			
			System.out.println(content);
			int failed = 0;
			for(int i = 0; i < item.getCount(); i++)
				if(!modus.increaseSize(player))
					failed++;
			
			if(!content.isEmpty())
				for(int i = 0; i < item.getCount() - failed; i++)
				{
					ItemStack toPut = content.copy();
					if(!modus.putItemStack(player, toPut))
						launchItem(player, toPut);
					else MinestuckCriteriaTriggers.CAPTCHALOGUE.trigger(player, modus, toPut);
				}
			
			if(failed == 0)
				container.inventory.setInventorySlotContents(0, ItemStack.EMPTY);
			else item.setCount(failed);
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
			player.dropItem(getItem(modus.getRegistryName()), true, false);	//TODO Add a method to the modus to get the itemstack instead
			setModus(player, null);
		} else modus.initModus(player, null, size);
		
		CaptchaDeckPacket packet = CaptchaDeckPacket.data(writeToNBT(getModus(player)));
		MinestuckPacketHandler.sendToPlayer(packet, player);
	}
	
	public static CompoundNBT writeToNBT(Modus modus)
	{
		if(modus == null)
			return null;
		ResourceLocation name = modus.getRegistryName();
		CompoundNBT nbt = modus.writeToNBT(new CompoundNBT());
		nbt.putString("type", name.toString());
		return nbt;
	}
	
	public static Modus readFromNBT(CompoundNBT nbt, boolean clientSide)
	{
		if(nbt == null)
			return null;
		Modus modus;
		ResourceLocation name;
		if(nbt.contains("type", 99))	//Integer from the old format
		{
			int i = nbt.getInt("type");
			name = new ResourceLocation(Minestuck.MOD_ID, metaConvert[MathHelper.clamp(i, 0, 5)]);
		} else
		{
			name = new ResourceLocation(nbt.getString("type"));
		}
		
		if(clientSide && clientSideModus != null && name.equals(clientSideModus.getRegistryName()))
			modus = clientSideModus;
		else
		{
			modus = createInstance(name, clientSide ? LogicalSide.CLIENT : LogicalSide.SERVER);
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