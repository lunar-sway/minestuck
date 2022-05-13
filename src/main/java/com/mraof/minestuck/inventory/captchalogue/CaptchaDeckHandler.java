package com.mraof.minestuck.inventory.captchalogue;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.advancements.MSCriteriaTriggers;
import com.mraof.minestuck.computer.editmode.ServerEditHandler;
import com.mraof.minestuck.item.BoondollarsItem;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.item.crafting.alchemy.AlchemyHelper;
import com.mraof.minestuck.network.MSPacketHandler;
import com.mraof.minestuck.network.data.ModusDataPacket;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.world.storage.ClientPlayerData;
import com.mraof.minestuck.world.storage.PlayerData;
import com.mraof.minestuck.world.storage.PlayerSavedData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.GameRules;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CaptchaDeckHandler
{
	public static final int EMPTY_SYLLADEX = -1;
	public static final int EMPTY_CARD = -2;
	
	@SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = false)
	public static void onPlayerDrops(LivingDropsEvent event)
	{
		if(!event.getEntity().level.isClientSide && !event.getEntity().level.getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY) && event.getEntity() instanceof ServerPlayerEntity)
		{
			dropSylladex((ServerPlayerEntity) event.getEntity());
		}
	}
	
	public static Modus createClientModus(ResourceLocation name)
	{
		ModusType<?> type = ModusTypes.REGISTRY.getValue(name);
		return type != null ? type.createClientSide() : null;
	}
	
	public static Modus createServerModus(ResourceLocation name, PlayerSavedData savedData)
	{
		ModusType<?> type = ModusTypes.REGISTRY.getValue(name);
		return type != null ? type.createServerSide(savedData) : null;
	}
	
	public static void launchItem(ServerPlayerEntity player, ItemStack item)
	{
		if(item.getItem().equals(MSItems.CAPTCHA_CARD) && !AlchemyHelper.hasDecodedItem(item))
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
		ItemEntity entity = new ItemEntity(player.level, player.getX(), player.getY()+1, player.getZ(), item);
		entity.setDeltaMovement(player.level.random.nextDouble() - 0.5, entity.getDeltaMovement().y, player.level.random.nextDouble() - 0.5);
		entity.setDefaultPickUpDelay();
		player.level.addFreshEntity(entity);
	}
	
	public static void useItem(ServerPlayerEntity player)
	{
		if(!(player.containerMenu instanceof CaptchaDeckContainer) || !canPlayerUseModus(player))
			 return;
		CaptchaDeckContainer container = (CaptchaDeckContainer) player.containerMenu;
		if(container.inventory.getItem(0).isEmpty())
			return;
		ItemStack stack = container.inventory.getItem(0);
		Modus modus = getModus(player);
		
		ModusType<?> type = ModusTypes.getTypeFromItem(stack.getItem());
		if(type != null)
		{
			ItemStack newItem = changeModus(player, stack, modus, type);
			container.inventory.setItem(0, newItem);
		}
		else if(stack.getItem().equals(MSItems.CAPTCHA_CARD) && !AlchemyHelper.isPunchedCard(stack)
				&& modus != null)
		{
			consumeCards(player, stack, modus);
		}
	}
	
	private static ItemStack changeModus(ServerPlayerEntity player, ItemStack modusItem, @Nullable Modus oldModus, ModusType<?> newType)
	{
		final Modus newModus = newType.createServerSide(PlayerSavedData.get(player.server));
		
		if(oldModus == null)
		{
			PlayerData data = PlayerSavedData.getData(player);
			newModus.initModus(modusItem, player, null, data.hasGivenModus() ? 0 : MinestuckConfig.SERVER.initialModusSize.get());
		}
		else
		{
			ModusType<?> oldType = oldModus.getType();
			if(newType.equals(oldType))
				return modusItem;
			if(newModus.canSwitchFrom(oldModus))
				newModus.initModus(modusItem, player, oldModus.getItems(), oldModus.getSize());
			else
			{
				for(ItemStack content : oldModus.getItems())
					if(!content.isEmpty())
						launchAnyItem(player, content);
				newModus.initModus(modusItem, player, null, oldModus.getSize());
			}
		}
		
		setModus(player, newModus);
		MSPacketHandler.sendToPlayer(ModusDataPacket.create(CaptchaDeckHandler.writeToNBT(newModus)), player);
		
		MSCriteriaTriggers.CHANGE_MODUS.trigger(player, newModus);
		
		return oldModus == null ? ItemStack.EMPTY : oldModus.getModusItem();
	}
	
	private static void consumeCards(ServerPlayerEntity player, ItemStack cards, Modus modus)
	{
		ItemStack content = AlchemyHelper.getDecodedItem(cards, true);
		
		int failed = 0;
		for(int i = 0; i < cards.getCount(); i++)
			if(!modus.increaseSize(player))
				failed++;
		
		if(!content.isEmpty())
			for(int i = 0; i < cards.getCount() - failed; i++)
			{
				ItemStack toPut = content.copy();
				if(!putInModus(player, modus, toPut))
					launchItem(player, toPut);
			}
		
		cards.setCount(failed);
		
		modus.checkAndResend(player);
	}
	
	public static void captchalogueItem(ServerPlayerEntity player)
	{
		if(canPlayerUseModus(player) && hasModus(player))
		{
			captchalogueItem(player, player.getMainHandItem());
		}
	}
	
	public static void captchalogueItemInSlot(ServerPlayerEntity player, int slotIndex, int windowId)
	{
		if(canPlayerUseModus(player) && hasModus(player) && player.containerMenu.containerId == windowId && player.containerMenu.isSynched(player))
		{
			Slot slot = slotIndex >= 0 && slotIndex < player.containerMenu.slots.size() ? player.containerMenu.getSlot(slotIndex) : null;
			
			if(slot != null && !slot.getItem().isEmpty() && slot.mayPickup(player))
			{
				ItemStack stack = slot.remove(slot.getItem().getMaxStackSize());
				captchalogueItem(player, stack);
				//It is not guaranteed that we can put the item back, so if it wasn't captchalogued, launch it
				if(!stack.isEmpty())
					launchItem(player, stack);
				
				player.containerMenu.broadcastChanges();
			}
		}
	}
	
	private static void captchalogueItem(ServerPlayerEntity player, ItemStack stack)
	{
		Modus modus = getModus(player);
		
		if(stack.getItem() == MSItems.BOONDOLLARS)
		{
			PlayerSavedData.getData(player).addBoondollars(BoondollarsItem.getCount(stack));
			stack.shrink(1);
			return;
		}
		
		if(modus != null && !stack.isEmpty())
		{
			if(stack.getItem() == MSItems.CAPTCHA_CARD && AlchemyHelper.hasDecodedItem(stack)
					&& !AlchemyHelper.isPunchedCard(stack))
				handleCardCaptchalogue(player, modus, stack);
			else putInModus(player, modus, stack);
			
			modus.checkAndResend(player);
		}
	}
	
	private static void handleCardCaptchalogue(ServerPlayerEntity player, Modus modus, ItemStack card)
	{
		ItemStack stackInCard = AlchemyHelper.getDecodedItem(card, true);
		boolean spentCard = modus.increaseSize(player);
		
		if(spentCard)
			card.shrink(1);
		
		if(!stackInCard.isEmpty())
		{
			boolean captchaloguedItem = putInModus(player, modus, stackInCard);
			
			if(captchaloguedItem && !spentCard)
			{	//Item was captchalogued, but the card remained
				launchAnyItem(player, new ItemStack(MSItems.CAPTCHA_CARD, 1));    //TODO split existing stack and instead remove the content to keep any other nbt data
				card.shrink(1);
			} else if(!captchaloguedItem && spentCard)
			{	//The card was used, but the item failed to captchalogue
				launchAnyItem(player, stackInCard);
			}
			
		}
	}
	
	private static boolean putInModus(ServerPlayerEntity player, Modus modus, ItemStack stack)
	{
		boolean result = modus.putItemStack(player, stack.copy());
		if(result)
		{
			MSCriteriaTriggers.CAPTCHALOGUE.trigger(player, modus, stack);
			stack.setCount(0);
		}
		return result;
	}
	
	public static void getItem(ServerPlayerEntity player, int index, boolean asCard)
	{
		if(!canPlayerUseModus(player))
			return;
		Modus modus = getModus(player);
		if(modus == null)
			return;
		ItemStack stack = modus.getItem(player, index, asCard);
		if(!stack.isEmpty())
		{
			ItemStack otherStack = player.getMainHandItem();
			if(otherStack.isEmpty())
				player.setItemInHand(Hand.MAIN_HAND, stack);
			else if(canMergeItemStacks(stack, otherStack))
			{
				otherStack.grow(stack.getCount());
				stack.setCount(0);
			}
			else
			{
				boolean placed = false;
				for(int i = 0; i < player.inventory.items.size(); i++)
				{
					otherStack = player.inventory.items.get(i);
					if(otherStack.isEmpty())
						player.inventory.items.set(i, stack.copy());
					else if(canMergeItemStacks(stack, otherStack))
						otherStack.grow(stack.getCount());
					else continue;
					
					stack.setCount(0);
					placed = true;
					player.inventory.setChanged();
					player.inventoryMenu.broadcastChanges();
					break;
				}
				if(!placed)
					launchAnyItem(player, stack);
			}
		}
		modus.checkAndResend(player);
	}
	
	private static void dropSylladex(ServerPlayerEntity player)
	{
		Modus modus = getModus(player);
		
		if(modus == null)
			return;
		
		NonNullList<ItemStack> stacks = modus.getItems();
		int size = modus.getSize();
		int cardsToKeep;
		switch(MinestuckConfig.SERVER.sylladexDropMode.get())
		{
			case ITEMS:
				cardsToKeep = size;
				break;
			case CARDS_AND_ITEMS:
				cardsToKeep = MinestuckConfig.SERVER.initialModusSize.get();
				break;
			case ALL: default:
				cardsToKeep = 0;
		}
		
		for(ItemStack stack : stacks)
			if(!stack.isEmpty() && !EnchantmentHelper.hasVanishingCurse(stack))
				if(size > cardsToKeep && MinestuckConfig.SERVER.dropItemsInCards.get())
				{
					ItemStack card = AlchemyHelper.createCard(stack, false);
					player.drop(card, true, false);
					size--;
				} else player.drop(stack, true, false);
		
		int stackLimit = new ItemStack(MSItems.CAPTCHA_CARD).getMaxStackSize();
		for(; size > cardsToKeep; size = Math.max(size - stackLimit, cardsToKeep))
			player.drop(new ItemStack(MSItems.CAPTCHA_CARD, Math.min(stackLimit, size - cardsToKeep)), true, false);
		
		if(MinestuckConfig.SERVER.sylladexDropMode.get() == MinestuckConfig.DropMode.ALL)
		{
			player.drop(modus.getModusItem(), true, false);
			setModus(player, null);
		} else modus.initModus(null, player, null, size);
		
		ModusDataPacket packet = ModusDataPacket.create(writeToNBT(getModus(player)));
		MSPacketHandler.sendToPlayer(packet, player);
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
	
	public static Modus readFromNBT(CompoundNBT nbt, PlayerSavedData savedData)
	{
		boolean clientSide = savedData == null;
		if(nbt == null)
			return null;
		Modus modus;
		ResourceLocation name = new ResourceLocation(nbt.getString("type"));
		
		if(clientSide && ClientPlayerData.getModus() != null && name.equals(ClientPlayerData.getModus().getType().getRegistryName()))
			modus = ClientPlayerData.getModus();
		else
		{
			modus = clientSide ? createClientModus(name) : createServerModus(name, savedData);
			if(modus == null)
			{
				Debug.warnf("Failed to load modus from nbt with the name \"%s\"", name.toString());
				return null;
			}
		}
		modus.readFromNBT(nbt);
		return modus;
	}
	
	private static boolean hasModus(ServerPlayerEntity player)
	{
		return getModus(player) != null;
	}
	
	public static Modus getModus(ServerPlayerEntity player)
	{
		return PlayerSavedData.getData(player).getModus();
	}
	
	public static void setModus(ServerPlayerEntity player, Modus modus)
	{
		PlayerSavedData.getData(player).setModus(modus);
	}
	
	private static boolean canMergeItemStacks(ItemStack stack1, ItemStack stack2)
	{
		return stack1.getItem() == stack2.getItem() && ItemStack.tagMatches(stack1, stack2)
				&& stack1.isStackable() && stack1.getCount() + stack2.getCount() < stack1.getMaxStackSize();
	}
	
	private static boolean canPlayerUseModus(ServerPlayerEntity player)
	{
		return !player.isSpectator() && ServerEditHandler.getData(player) == null;
	}
}