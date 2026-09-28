package com.mraof.minestuck.strife;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.entity.MSAttributes;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.item.StrifeCardItem;
import com.mraof.minestuck.item.components.MSItemComponents;
import com.mraof.minestuck.network.StrifePackets;
import com.mraof.minestuck.player.KindAbstratusType;
import com.mraof.minestuck.player.StrifePortfolioData;
import com.mraof.minestuck.player.StrifeSpecibus;
import com.mraof.minestuck.util.MSAttachments;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Unit;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;

import javax.annotation.Nullable;

/**
 * Server-side helper that encapsulates all mutations to a player's Strife Portfolio.
 * Every method that changes portfolio state MUST (!!!) call {@link #syncToClient} at the end.
 *
 * <p>Model: while a weapon is <i>armed</i> it lives in the player's main hand (tagged with
 * {@code STRIFE_ASSIGNED}) and is <b>not</b> part of the deck list. {@link StrifePortfolioData#getSelectedWeaponIndex()}
 * then remembers the deck slot that the weapon returns to when it is disarmed.
 * Every weapon index that is exchanged with the client refers to the deck <i>including</i> the armed weapon
 * (see {@link StrifePortfolioData#getDeckWithArmed}).</p>
 */
public final class StrifePortfolioHandler
{
	
	public static StrifePortfolioData getData(Player player)
	{
		return player.getData(MSAttachments.STRIFE_PORTFOLIO.get());
	}
	
	public static boolean isFull(Player player)
	{
		return getData(player).isPortfolioFull();
	}
	
	public static boolean isEmpty(Player player)
	{
		return getData(player).isPortfolioEmpty();
	}
	
	/**
	 * Returns true when the ItemStack has been drawn from a strife deck.
	 */
	public static boolean isAssigned(ItemStack stack)
	{
		return !stack.isEmpty() && stack.has(MSItemComponents.STRIFE_ASSIGNED.get());
	}
	
	public static void syncToClient(ServerPlayer player)
	{
		PacketDistributor.sendToPlayer(player, new StrifePackets.SyncPortfolioPacket(getData(player)));
	}
	
	/**
	 * Puts the stack in the player's inventory, or drops it at the player's feet if there is no room.
	 */
	public static void giveOrDrop(ServerPlayer player, ItemStack stack)
	{
		if(stack.isEmpty()) return;
		if(!player.getInventory().add(stack) && !stack.isEmpty()) player.drop(stack, false);
	}
	
	public static boolean addSpecibus(ServerPlayer player, StrifeSpecibus specibus)
	{
		StrifePortfolioData data = getData(player);
		
		if(data.isPortfolioFull())
		{
			player.displayClientMessage(Component.translatable("status.strife.portfolioFull"), true);
			return false;
		}
		if(specibus.isAssigned() && data.portfolioHasAbstratus(specibus.getAbstratusName()))
		{
			player.displayClientMessage(Component.translatable("status.strife.portfolioDuplicate", specibus.getDisplayName()), true);
			return false;
		}
		
		data.addSpecibus(specibus);
		
		if(specibus.isAssigned())
			player.displayClientMessage(Component.translatable("status.strife.assign", specibus.getDisplayName()), true);
		
		syncToClient(player);
		return true;
	}
	
	/**
	 * Tries to assign the item in the player's hand to any compatible specibus slot.
	 * If the card is blank, an {@link StrifePackets.OpenStrifeCardGuiPacket} is sent instead.
	 */
	public static void assignStrife(ServerPlayer player, InteractionHand hand)
	{
		ItemStack stack = player.getItemInHand(hand);
		
		if(stack.getItem() instanceof StrifeCardItem)
		{
			StrifeSpecibus specibus = stack.get(MSItemComponents.STRIFE_SPECIBUS_DATA.get());
			if(specibus != null && specibus.isAssigned())
			{
				if(addSpecibus(player, specibus)) stack.shrink(1);
			} else
			{
				// Blank card - open the abstrata-selection GUI on client
				PacketDistributor.sendToPlayer(player, new StrifePackets.OpenStrifeCardGuiPacket(hand));
			}
		} else
		{
			// Non-card item: try to put it in a weapon deck
			if(addWeapon(player, stack, true)) player.setItemInHand(hand, ItemStack.EMPTY);
		}
	}
	
	/**
	 * Removes the specibus at {@code index} from the portfolio, wraps it in a
	 * {@link StrifeCardItem} and gives it to the player (or drops it).
	 */
	public static void retrieveCard(ServerPlayer player, int index)
	{
		StrifePortfolioData data = getData(player);
		
		// If this slot is currently armed, return the weapon to its deck first so it ends up on the card
		if(data.isArmed() && data.getSelectedSpecibusIndex() == index) disarm(player, data);
		
		StrifeSpecibus removed = data.removeSpecibus(index);
		if(removed == null) return;
		
		ItemStack card = createStrifeCard(removed);
		if(!player.addItem(card)) player.drop(card, false);
		
		syncToClient(player);
	}
	
	/**
	 * Convenience overload that always sends status messages.
	 */
	public static boolean addWeapon(ServerPlayer player, ItemStack stack)
	{
		return addWeapon(player, stack, true);
	}
	
	private record Placement(@Nullable StrifeSpecibus placed, @Nullable StrifeSpecibus fullButCompatible)
	{
	}
	
	/**
	 * Finds the first compatible specibus slot (selected first, then others) and adds a copy of {@code stack} to its deck.
	 */
	private static Placement placeWeapon(ServerPlayer player, StrifePortfolioData data, ItemStack stack)
	{
		int maxSize = getStrifeDeckCapacity(player);
		StrifeSpecibus fullButCompatible = null;
		
		StrifeSpecibus selected = data.getSelectedSpecibus();
		if(selected != null)
		{
			KindAbstratusType type = selected.getKindAbstratus();
			if(type != null && type.partOf(stack))
			{
				if(maxSize >= 0 && selected.getContents().size() >= maxSize) fullButCompatible = selected;
				else if(selected.putItemStack(stack)) return new Placement(selected, null);
			}
		}
		
		StrifeSpecibus[] portfolio = data.getPortfolio();
		for(int i = 0; i < StrifePortfolioData.PORTFOLIO_SIZE; i++)
		{
			StrifeSpecibus sp = portfolio[i];
			if(sp == null || sp == selected) continue;
			KindAbstratusType type = sp.getKindAbstratus();
			if(type == null || !type.partOf(stack)) continue;
			
			if(maxSize >= 0 && sp.getContents().size() >= maxSize)
			{
				if(fullButCompatible == null) fullButCompatible = sp;
				continue;
			}
			if(sp.putItemStack(stack)) return new Placement(sp, null);
		}
		return new Placement(null, fullButCompatible);
	}
	
	/**
	 * Adds a copy of {@code stack} to the first compatible strife deck.
	 *
	 * <p>Respects the {@code strifeDeckMaxSize} config option and the strife deck capacity attribute.</p>
	 */
	public static boolean addWeapon(ServerPlayer player, ItemStack stack, boolean sendMessage)
	{
		if(stack.isEmpty()) return false;
		StrifePortfolioData data = getData(player);
		
		Placement placement = placeWeapon(player, data, stack);
		if(placement.placed() != null)
		{
			if(sendMessage)
				player.displayClientMessage(Component.translatable("status.strife.assignWeapon", stack.getHoverName(), placement.placed().getDisplayName()), true);
			syncToClient(player);
			return true;
		}
		
		if(sendMessage)
		{
			if(placement.fullButCompatible() != null)
				player.displayClientMessage(Component.translatable("status.strife.strifeDeckFull", placement.fullButCompatible().getDisplayName()), true);
			else
				player.displayClientMessage(Component.translatable("status.strife.weaponMismatch", stack.getHoverName()), true);
		}
		return false;
	}
	
	/**
	 * Automatically stows a picked up weapon into a matching strife deck.
	 * @return true if the whole stack was moved into a deck and the caller should remove it from the world
	 */
	public static boolean autoStow(ServerPlayer player, ItemStack stack)
	{
		if(stack.isEmpty() || stack.getMaxStackSize() > 1) return false;
		if(isAssigned(stack)) return false;
		
		StrifePortfolioData data = getData(player);
		if(data.isPortfolioEmpty() || !data.hasMatchingSpecibus(stack)) return false;
		
		Placement placement = placeWeapon(player, data, stack);
		if(placement.placed() == null) return false;
		
		player.displayClientMessage(Component.translatable("status.strife.autoStow", stack.getHoverName(), placement.placed().getDisplayName()), true);
		syncToClient(player);
		return true;
	}
	
	/**
	 * The deck size limit for this player, or -1 if there is no limit.
	 * The config value is the base, and the {@code player.strife_deck_capacity} attribute adds to it.
	 */
	public static int getStrifeDeckCapacity(ServerPlayer player)
	{
		int base = MinestuckConfig.SERVER.strifeDeckMaxSize.get();
		if(base < 0) return -1;
		return base + (int) player.getAttributeValue(MSAttributes.STRIFE_DECK_CAPACITY);
	}
	
	/**
	 * Called from the armed tick when the armed weapon has left the main hand (the player scrolled the hotbar)
	 * and a new, non-assigned item is held instead. If that item fits a specibus it becomes the armed weapon,
	 * possibly switching the selected specibus.
	 *
	 * <p>The new item stays in the hand; it is only tagged as assigned. It does not enter the deck while it is armed.</p>
	 *
	 * @return the specibus slot the item was armed from, or {@code null} if it doesn't fit anywhere
	 */
	@Nullable
	public static StrifeSpecibus moveSelectedWeapon(ServerPlayer player, ItemStack newStack)
	{
		StrifePortfolioData data = getData(player);
		int maxSize = getStrifeDeckCapacity(player);
		StrifeSpecibus selSp = data.getSelectedSpecibus();
		StrifeSpecibus[] portfolio = data.getPortfolio();
		
		if(selSp != null)
		{
			KindAbstratusType type = selSp.getKindAbstratus();
			if(type != null && type.partOf(newStack) && (maxSize < 0 || selSp.getContents().size() < maxSize))
			{
				newStack.set(MSItemComponents.STRIFE_ASSIGNED.get(), Unit.INSTANCE);
				data.setSelectedWeaponIndex(selSp.getContents().size());
				data.setArmed(true);
				syncToClient(player);
				return selSp;
			}
		}
		
		for(int i = 0; i < StrifePortfolioData.PORTFOLIO_SIZE; i++)
		{
			StrifeSpecibus sp = portfolio[i];
			if(sp == null || sp == selSp) continue;
			KindAbstratusType type = sp.getKindAbstratus();
			if(type == null || !type.partOf(newStack)) continue;
			if(maxSize >= 0 && sp.getContents().size() >= maxSize) continue;
			
			newStack.set(MSItemComponents.STRIFE_ASSIGNED.get(), Unit.INSTANCE);
			data.setSelectedSpecibusIndex(i);
			data.setSelectedWeaponIndex(sp.getContents().size());
			data.setArmed(true);
			syncToClient(player);
			return sp;
		}
		
		return null;
	}
	
	/**
	 * Arms the weapon at {@code weaponIndex} (an index into the deck including the armed weapon)
	 * of the currently selected specibus slot, or disarms it if that weapon is already armed.
	 *
	 * <ul>
	 *   <li>Hand occupied by a real (non-assigned) item: does nothing.</li>
	 *   <li>Hand empty or holding the armed weapon: arm / switch / disarm.</li>
	 * </ul>
	 */
	public static void retrieveWeapon(ServerPlayer player, int weaponIndex, InteractionHand hand)
	{
		StrifePortfolioData data = getData(player);
		StrifeSpecibus selSp = data.getSelectedSpecibus();
		if(selSp == null) return;
		
		ItemStack heldItem = player.getItemInHand(hand);
		boolean handArmed = data.isArmed() && isAssigned(heldItem);
		
		if(!heldItem.isEmpty() && !handArmed) return;
		
		int armedSlot = handArmed ? data.armedWeaponSlot(selSp.getContents().size()) : -1;
		
		if(handArmed)
		{
			// Put the armed weapon back so that weaponIndex refers to the complete deck
			ItemStack back = heldItem.copy();
			back.remove(MSItemComponents.STRIFE_ASSIGNED.get());
			selSp.getContents().add(armedSlot, back);
			player.setItemInHand(hand, ItemStack.EMPTY);
			data.setArmed(false);
			
			if(weaponIndex == armedSlot)
			{
				syncToClient(player);
				return;
			}
		}
		
		if(weaponIndex < 0 || weaponIndex >= selSp.getContents().size())
		{
			syncToClient(player);
			return;
		}
		
		ItemStack weapon = selSp.getContents().remove(weaponIndex);
		weapon.set(MSItemComponents.STRIFE_ASSIGNED.get(), Unit.INSTANCE);
		player.setItemInHand(hand, weapon);
		data.setSelectedWeaponIndex(weaponIndex);
		data.setArmed(true);
		syncToClient(player);
	}
	
	/**
	 * Moves a weapon from a specibus deck slot (index into the deck including the armed weapon)
	 * into the player's offhand, and tries to store the previous offhand item in the portfolio in return.
	 */
	public static void swapOffhandWeapon(ServerPlayer player, int specibusIndex, int weaponIndex)
	{
		StrifePortfolioData data = getData(player);
		if(specibusIndex < 0 || specibusIndex >= StrifePortfolioData.PORTFOLIO_SIZE) return;
		StrifeSpecibus sp = data.getPortfolio()[specibusIndex];
		if(sp == null) return;
		
		// The weapon indexes count the armed weapon, so put it back first
		if(data.isArmed() && data.getSelectedSpecibusIndex() == specibusIndex) disarm(player, data);
		
		if(weaponIndex < 0 || weaponIndex >= sp.getContents().size())
		{
			syncToClient(player);
			return;
		}
		
		ItemStack weapon = sp.getContents().remove(weaponIndex);
		if(specibusIndex == data.getSelectedSpecibusIndex() && data.getSelectedWeaponIndex() >= sp.getContents().size())
			data.setSelectedWeaponIndex(0);
		
		ItemStack currentOffhand = player.getItemInHand(InteractionHand.OFF_HAND);
		if(currentOffhand.isEmpty() || addWeapon(player, currentOffhand, false))
		{
			player.setItemInHand(InteractionHand.OFF_HAND, weapon);
		} else
		{
			// The offhand item doesn't fit anywhere, so nothing is swapped
			sp.getContents().add(Math.min(weaponIndex, sp.getContents().size()), weapon);
		}
		
		syncToClient(player);
	}
	
	/**
	 * Takes the currently selected weapon out of the active specibus deck and gives it back to the player.
	 */
	public static void unassignSelected(ServerPlayer player)
	{
		StrifePortfolioData data = getData(player);
		StrifeSpecibus selSp = data.getSelectedSpecibus();
		if(selSp == null) return;
		
		if(data.isArmed())
		{
			ItemStack held = player.getMainHandItem();
			if(isAssigned(held))
			{
				ItemStack weapon = held.copy();
				weapon.remove(MSItemComponents.STRIFE_ASSIGNED.get());
				player.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
				giveOrDrop(player, weapon);
			}
		} else if(data.getSelectedWeaponIndex() >= 0 && data.getSelectedWeaponIndex() < selSp.getContents().size())
		{
			giveOrDrop(player, selSp.getContents().remove(data.getSelectedWeaponIndex()));
		}
		
		if(data.getSelectedWeaponIndex() >= selSp.getContents().size()) data.setSelectedWeaponIndex(0);
		data.setArmed(false);
		syncToClient(player);
	}
	
	/**
	 * Changes the active specibus slot. Returns the armed weapon to its deck first.
	 *
	 * @return true if the selection actually changed
	 */
	public static boolean setSelectedSpecibus(ServerPlayer player, int index)
	{
		StrifePortfolioData data = getData(player);
		if(index == data.getSelectedSpecibusIndex()) return false;
		
		disarm(player, data);
		data.setSelectedSpecibusIndex(index);
		syncToClient(player);
		return true;
	}
	
	/**
	 * Returns the armed weapon from the main hand to its slot in the selected deck and marks the data as unarmed.
	 * Does not sync, the caller has to do that.
	 */
	public static void disarm(ServerPlayer player, StrifePortfolioData data)
	{
		if(!data.isArmed()) return;
		
		StrifeSpecibus selSp = data.getSelectedSpecibus();
		ItemStack held = player.getMainHandItem();
		if(isAssigned(held))
		{
			ItemStack weapon = held.copy();
			weapon.remove(MSItemComponents.STRIFE_ASSIGNED.get());
			player.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
			
			if(selSp != null) selSp.getContents().add(data.armedWeaponSlot(selSp.getContents().size()), weapon);
			else giveOrDrop(player, weapon);
		}
		data.setArmed(false);
	}
	
	/**
	 * Wraps a {@link StrifeSpecibus} into a {@link StrifeCardItem} ItemStack.
	 */
	public static ItemStack createStrifeCard(@Nullable StrifeSpecibus specibus)
	{
		ItemStack card = new ItemStack(MSItems.STRIFE_CARD.get());
		if(specibus != null) card.set(MSItemComponents.STRIFE_SPECIBUS_DATA.get(), specibus);
		return card;
	}
}