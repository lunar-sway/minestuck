package com.mraof.minestuck.inventory.captchalogue;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.advancements.MSCriteriaTriggers;
import com.mraof.minestuck.alchemy.AlchemyHelper;
import com.mraof.minestuck.computer.editmode.ServerEditHandler;
import com.mraof.minestuck.item.BoondollarsItem;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.network.CaptchaDeckPackets;
import com.mraof.minestuck.player.ClientPlayerData;
import com.mraof.minestuck.player.PlayerBoondollars;
import com.mraof.minestuck.player.PlayerData;
import com.mraof.minestuck.util.MSAttachments;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.GameRules;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.LogicalSide;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

//todo this class could use some spring cleaning
@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class CaptchaDeckHandler
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	public static final int EMPTY_SYLLADEX = -1;
	public static final int EMPTY_CARD = -2;
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	private static void onPlayerDrops(LivingDropsEvent event)
	{
		if(event.getEntity() instanceof ServerPlayer player && !event.getEntity().level().getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY))
		{
			dropSylladex(player);
		}
	}
	
	@SubscribeEvent
	private static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event)
	{
		ServerPlayer player = (ServerPlayer) event.getEntity();
		
		ModusHolder modusHolder = getHolder(player);
		if(modusHolder.modus != null)
			player.connection.send(CaptchaDeckPackets.ModusData.create(modusHolder.modus));
		
		if(modusHolder.modus == null && !modusHolder.givenModus)
			CaptchaDeckHandler.tryGiveStartingModus(modusHolder, player);
	}
	
	public static Modus createClientModus(ResourceLocation name)
	{
		ModusType<?> type = ModusTypes.REGISTRY.get(name);
		return type != null ? type.createClientSide() : null;
	}
	
	public static Modus createServerModus(ResourceLocation name)
	{
		ModusType<?> type = ModusTypes.REGISTRY.get(name);
		return type != null ? type.createServerSide() : null;
	}
	
	public static void launchItem(ServerPlayer player, ItemStack item)
	{
		if(item.getItem().equals(MSItems.CAPTCHA_CARD.get()) && !AlchemyHelper.hasDecodedItem(item))
			while(item.getCount() > 0)
			{
				if(getModus(player).increaseSize(player))
					item.shrink(1);
				else break;
			}
		if(item.getCount() > 0)
			launchAnyItem(player, item);
	}
	
	public static void launchAnyItem(Player player, ItemStack item)
	{
		ItemEntity entity = new ItemEntity(player.level(), player.getX(), player.getY()+1, player.getZ(), item);
		entity.setDeltaMovement(player.level().random.nextDouble() - 0.5, entity.getDeltaMovement().y, player.level().random.nextDouble() - 0.5);
		entity.setDefaultPickUpDelay();
		player.level().addFreshEntity(entity);
	}
	
	public static void useItem(ServerPlayer player)
	{
		if(!(player.containerMenu instanceof CaptchaDeckMenu containerMenu) || !canPlayerUseModus(player))
			 return;
		ItemStack stack = containerMenu.getMenuItem();
		if(stack.isEmpty())
			return;
		Modus modus = getModus(player);
		
		ModusType<?> type = ModusTypes.getTypeFromItem(stack.getItem());
		if(type != null)
		{
			ItemStack newItem = changeModus(player, stack, modus, type);
			containerMenu.setMenuItem(newItem);
		}
		else if(stack.getItem().equals(MSItems.CAPTCHA_CARD.get()) && !AlchemyHelper.isPunchedCard(stack)
				&& modus != null)
		{
			consumeCards(player, stack, modus);
		}
	}
	
	private static ItemStack changeModus(ServerPlayer player, ItemStack modusItem, @Nullable Modus oldModus, ModusType<?> newType)
	{
		final Modus newModus = newType.createServerSide();
		
		if(oldModus == null)
		{
			ModusHolder modusHolder = getHolder(player);
			newModus.initModus(modusItem, player, null, modusHolder.givenModus
					? 0
					: MinestuckConfig.SERVER.initialModusSize.get());
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
		
		setModus(getHolder(player), player, newModus);
		
		MSCriteriaTriggers.CHANGE_MODUS.get().trigger(player, newModus);
		
		return oldModus == null ? ItemStack.EMPTY : oldModus.getModusItem();
	}
	
	private static void consumeCards(ServerPlayer player, ItemStack cards, Modus modus)
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
	
	public static void captchalogueItem(ServerPlayer player)
	{
		if(canPlayerUseModus(player) && hasModus(player))
		{
			captchalogueItem(player, player.getMainHandItem());
		}
	}
	
	public static void captchalogueItemInSlot(ServerPlayer player, int slotIndex, int windowId)
	{
		if(!canPlayerUseModus(player) || !hasModus(player) || player.containerMenu.containerId != windowId)
			return;
		
		if(slotIndex < 0 || slotIndex >= player.containerMenu.slots.size())
			return;
		
		Slot slot = player.containerMenu.getSlot(slotIndex);
		
		ItemStack stack = slot.safeTake(slot.getItem().getCount(), slot.getItem().getMaxStackSize(), player);
		if(stack.isEmpty())
			return;
		
		captchalogueItem(player, stack);
		//It is not guaranteed that we can put the item back, so if it wasn't captchalogued, launch it
		if(!stack.isEmpty())
			launchItem(player, stack);
		
		player.containerMenu.broadcastChanges();
	}
	
	private static void captchalogueItem(ServerPlayer player, ItemStack stack)
	{
		Modus modus = getModus(player);
		
		if(stack.is(MSItems.BOONDOLLARS))
		{
			PlayerBoondollars.addBoondollars(PlayerData.get(player).orElseThrow(), BoondollarsItem.getCount(stack));
			stack.shrink(1);
			return;
		}
		
		if(modus != null && !stack.isEmpty())
		{
			if(stack.is(MSItems.CAPTCHA_CARD) && AlchemyHelper.hasDecodedItem(stack)
					&& !AlchemyHelper.isPunchedCard(stack))
				handleCardCaptchalogue(player, modus, stack);
			else putInModus(player, modus, stack);
			
			modus.checkAndResend(player);
		}
	}
	
	private static void handleCardCaptchalogue(ServerPlayer player, Modus modus, ItemStack card)
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
				launchAnyItem(player, new ItemStack(MSItems.CAPTCHA_CARD.get(), 1));    //TODO split existing stack and instead remove the content to keep any other nbt data
				card.shrink(1);
			} else if(!captchaloguedItem && spentCard)
			{	//The card was used, but the item failed to captchalogue
				launchAnyItem(player, stackInCard);
			}
			
		}
	}
	
	private static boolean putInModus(ServerPlayer player, Modus modus, ItemStack stack)
	{
		boolean result = modus.putItemStack(player, stack.copy());
		if(result)
		{
			MSCriteriaTriggers.CAPTCHALOGUE.get().trigger(player, modus, stack);
			stack.setCount(0);
		}
		return result;
	}
	
	public static void getItem(ServerPlayer player, int index, boolean asCard)
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
				player.setItemInHand(InteractionHand.MAIN_HAND, stack);
			else if(canMergeItemStacks(stack, otherStack))
			{
				otherStack.grow(stack.getCount());
				stack.setCount(0);
			}
			else
			{
				boolean placed = false;
				for(int i = 0; i < player.getInventory().items.size(); i++)
				{
					otherStack = player.getInventory().items.get(i);
					if(otherStack.isEmpty())
						player.getInventory().items.set(i, stack.copy());
					else if(canMergeItemStacks(stack, otherStack))
						otherStack.grow(stack.getCount());
					else continue;
					
					stack.setCount(0);
					placed = true;
					player.getInventory().setChanged();
					player.inventoryMenu.broadcastChanges();
					break;
				}
				if(!placed)
					launchAnyItem(player, stack);
			}
		}
		modus.checkAndResend(player);
	}
	
	private static void dropSylladex(ServerPlayer player)
	{
		Modus modus = getModus(player);
		
		if(modus == null)
			return;
		
		NonNullList<ItemStack> stacks = modus.getItems();
		int size = modus.getSize();
		int cardsToKeep = switch(MinestuckConfig.SERVER.sylladexDropMode.get())
				{
					case ITEMS -> size;
					case CARDS_AND_ITEMS -> MinestuckConfig.SERVER.initialModusSize.get();
					case ALL -> 0;
				};
		
		for(ItemStack stack : stacks)
			if(!stack.isEmpty() && !EnchantmentHelper.hasVanishingCurse(stack))
				if(size > cardsToKeep && MinestuckConfig.SERVER.dropItemsInCards.get())
				{
					ItemStack card = AlchemyHelper.createCard(stack, player.server);
					player.drop(card, true, false);
					size--;
				} else player.drop(stack, true, false);
		
		int stackLimit = new ItemStack(MSItems.CAPTCHA_CARD.get()).getMaxStackSize();
		for(; size > cardsToKeep; size = Math.max(size - stackLimit, cardsToKeep))
			player.drop(new ItemStack(MSItems.CAPTCHA_CARD.get(), Math.min(stackLimit, size - cardsToKeep)), true, false);
		
		if(MinestuckConfig.SERVER.sylladexDropMode.get() == MinestuckConfig.DropMode.ALL)
		{
			player.drop(modus.getModusItem(), true, false);
			setModus(getHolder(player), player, null);
		} else
		{
			modus.initModus(null, player, null, size);
			player.connection.send(CaptchaDeckPackets.ModusData.create(modus));
		}
	}
	
	@Nullable
	public static CompoundTag writeToNBT(@Nullable Modus modus)
	{
		if(modus == null)
			return null;
		
		ResourceLocation name = ModusTypes.REGISTRY.getKey(modus.getType());
		if(name != null)
		{
			CompoundTag nbt = modus.writeToNBT(new CompoundTag());
			nbt.putString("type", name.toString());
			return nbt;
		} else return null;
	}
	
	public static Modus readFromNBT(CompoundTag nbt, LogicalSide side)
	{
		if(nbt == null)
			return null;
		Modus modus;
		ResourceLocation name = new ResourceLocation(nbt.getString("type"));
		
		if(side.isClient() && ClientPlayerData.getModus() != null && name.equals(ModusTypes.REGISTRY.getKey(ClientPlayerData.getModus().getType())))
			modus = ClientPlayerData.getModus();
		else
		{
			modus = side.isClient() ? createClientModus(name) : createServerModus(name);
			if(modus == null)
			{
				LOGGER.warn("Failed to load modus from nbt with the name \"{}\"", name.toString());
				return null;
			}
		}
		modus.readFromNBT(nbt);
		return modus;
	}
	
	private static boolean hasModus(ServerPlayer player)
	{
		return getModus(player) != null;
	}
	
	@Nullable
	public static Modus getModus(ServerPlayer player)
	{
		Optional<PlayerData> playerData = PlayerData.get(player);
		if(playerData.isEmpty())
			return null;
		return playerData.get().getData(MSAttachments.MODUS_HOLDER).modus;
	}
	
	private static boolean canMergeItemStacks(ItemStack stack1, ItemStack stack2)
	{
		return ItemStack.isSameItemSameTags(stack1, stack2)
				&& stack1.isStackable() && stack1.getCount() + stack2.getCount() < stack1.getMaxStackSize();
	}
	
	private static boolean canPlayerUseModus(ServerPlayer player)
	{
		return !player.isSpectator() && !ServerEditHandler.isInEditmode(player);
	}
	
	private static void setModus(ModusHolder modusHolder, ServerPlayer player, @Nullable Modus modus)
	{
		if(modusHolder.modus == modus)
			return;
		
		modusHolder.modus = modus;
		if(modus != null)
			modusHolder.givenModus = true;
		player.connection.send(CaptchaDeckPackets.ModusData.create(modus));
	}
	
	private static void tryGiveStartingModus(ModusHolder modusHolder, ServerPlayer player)
	{
		List<ModusType<?>> startingTypes = StartingModusManager.getStartingModusTypes();
		
		if(startingTypes.isEmpty())
			return;
		
		ModusType<?> type = startingTypes.get(player.level().random.nextInt(startingTypes.size()));
		if(type == null)
		{
			modusHolder.givenModus = true;
			return;
		}
		
		Modus modus = type.createServerSide();
		if(modus == null)
		{
			LOGGER.warn("Couldn't create a starting modus type {}.", ModusTypes.REGISTRY.getKey(type));
			return;
		}
		
		modus.initModus(null, player, null, MinestuckConfig.SERVER.initialModusSize.get());
		setModus(modusHolder, player, modus);
	}
	
	private static ModusHolder getHolder(ServerPlayer player)
	{
		PlayerData data = PlayerData.get(player).orElseThrow();
		return data.getData(MSAttachments.MODUS_HOLDER);
	}
	
	public static class ModusHolder implements INBTSerializable<CompoundTag>
	{
		private boolean givenModus = false;
		@Nullable
		private Modus modus = null;
		
		@Override
		public CompoundTag serializeNBT()
		{
			CompoundTag nbt = new CompoundTag();
			
			CompoundTag modusTag = writeToNBT(modus);
			if(modusTag != null)
				nbt.put("modus", modusTag);
			else
				nbt.putBoolean("given_modus", givenModus);
			
			return nbt;
		}
		
		@Override
		public void deserializeNBT(CompoundTag nbt)
		{
			if (nbt.contains("modus"))
			{
				this.modus = readFromNBT(nbt.getCompound("modus"), LogicalSide.SERVER);
				givenModus = true;
			}
			else
				givenModus = nbt.getBoolean("given_modus");
		}
	}
}

