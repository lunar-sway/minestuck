package com.mraof.minestuck.strife;

import com.mraof.minestuck.MinestuckConfig;
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
	
	/** Returns true when the ItemStack has been drawn from a strife deck. */
	public static boolean isAssigned(ItemStack stack)
	{
		return !stack.isEmpty() && stack.has(MSItemComponents.STRIFE_ASSIGNED.get());
	}
	
	
	public static void syncToClient(ServerPlayer player)
	{
		PacketDistributor.sendToPlayer(player, new StrifePackets.SyncPortfolioPacket(getData(player)));
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
			player.displayClientMessage(
					Component.translatable("status.strife.portfolioDuplicate",
							specibus.getDisplayName()), true);
			return false;
		}
		
		data.addSpecibus(specibus);
		
		if(specibus.isAssigned())
			player.displayClientMessage(
					Component.translatable("status.strife.assign", specibus.getDisplayName()), true);
		
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
			if(specibus != null)
			{
				if(addSpecibus(player, specibus))
					stack.shrink(1);
			}
			else
			{
				// Blank card – open the abstrata-selection GUI on client
				PacketDistributor.sendToPlayer(player, new StrifePackets.OpenStrifeCardGuiPacket(hand));
			}
		}
		else
		{
			// Non-card item: try to put it in a weapon deck
			if(addWeapon(player, stack, true))
			{
				player.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
			}
		}
	}
	
	/**
	 * Removes the specibus at {@code index} from the portfolio, wraps it in a
	 * {@link StrifeCardItem} and gives it to the player (or drops it).
	 */
	public static void retrieveCard(ServerPlayer player, int index)
	{
		StrifePortfolioData data = getData(player);
		
		// If this slot is currently armed, disarm before removing
		if(data.isArmed() && data.getSelectedSpecibusIndex() == index)
			clearArmedWeapon(player, data);
		
		StrifeSpecibus removed = data.removeSpecibus(index);
		if(removed == null) return;
		
		ItemStack card = createStrifeCard(removed);
		if(!player.addItem(card))
			player.drop(card, false);
		
		syncToClient(player);
	}
	
	/** Convenience overload that always sends status messages. */
	public static boolean addWeapon(ServerPlayer player, ItemStack stack)
	{
		return addWeapon(player, stack, true);
	}
	
	/**
	 * Finds the first compatible specibus slot (selected first, then others) and
	 * adds a copy of {@code stack} to its deck.
	 *
	 * <p>Respects {@code strifeDeckMaxSize} config option.</p>
	 */
	public static boolean addWeapon(ServerPlayer player, ItemStack stack, boolean sendMessage)
	{
		if(stack.isEmpty()) return false;
		StrifePortfolioData data  = getData(player);
		int maxSize = MinestuckConfig.SERVER.strifeDeckMaxSize.get();
		
		StrifeSpecibus fullButCompatible = null;
		
		// 1 - try the selected slot first
		StrifeSpecibus selected = data.getSelectedSpecibus();
		if(selected != null)
		{
			KindAbstratusType type = selected.getKindAbstratus();
			if(type != null && type.partOf(stack))
			{
				if(maxSize >= 0 && selected.getContents().size() >= maxSize)
				{
					fullButCompatible = selected;
				}
				else if(selected.putItemStack(stack))
				{
					if(sendMessage)
						player.displayClientMessage(
								Component.translatable("status.strife.assignWeapon",
										stack.getHoverName(), selected.getDisplayName()), true);
					syncToClient(player);
					return true;
				}
			}
		}
		
		// 2 – try remaining slots
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
			if(sp.putItemStack(stack))
			{
				if(sendMessage)
					player.displayClientMessage(
							Component.translatable("status.strife.assignWeapon",
									stack.getHoverName(), sp.getDisplayName()), true);
				syncToClient(player);
				return true;
			}
		}
		
		// 3 – failure feedback
		if(sendMessage)
		{
			if(fullButCompatible != null)
				player.displayClientMessage(
						Component.translatable("status.strife.strifeDeckFull",
								fullButCompatible.getDisplayName()), true);
			else
				player.displayClientMessage(
						Component.translatable("status.strife.weaponMismatch",
								stack.getHoverName()), true);
		}
		return false;
	}
	
	/**
	 * Called from the armed tick when the player places a new (non-assigned) item
	 * in their main hand while armed.  Attempts to find the item a compatible slot
	 * and – if found – relocates the arm from the old slot to the new one.
	 *
	 * @return the specibus slot the item was moved into, or {@code null}
	 */
	@Nullable
	public static StrifeSpecibus moveSelectedWeapon(ServerPlayer player, ItemStack newStack)
	{
		StrifePortfolioData data = getData(player);
		int maxSize = MinestuckConfig.SERVER.strifeDeckMaxSize.get();
		StrifeSpecibus selSp = data.getSelectedSpecibus();
		int prevSelIndex = data.getSelectedSpecibusIndex();
		
		// Helper:: try a single specibus slot
		// Returns the specibus if the item fits, null otherwise
		StrifeSpecibus[] portfolio = data.getPortfolio();
		
		// Try selected slot first
		if(selSp != null)
		{
			KindAbstratusType type = selSp.getKindAbstratus();
			if(type != null && type.partOf(newStack)
					&& (maxSize < 0 || selSp.getContents().size() < maxSize))
			{
				newStack.set(MSItemComponents.STRIFE_ASSIGNED.get(), Unit.INSTANCE);
				selSp.getContents().add(newStack);
				selSp.unassign(data.getSelectedWeaponIndex()); // remove old weapon from deck
				data.setSelectedWeaponIndex(selSp.getContents().indexOf(newStack));
				syncToClient(player);
				return selSp;
			}
		}
		
		// Try other slots
		for(int i = 0; i < StrifePortfolioData.PORTFOLIO_SIZE; i++)
		{
			StrifeSpecibus sp = portfolio[i];
			if(sp == null || sp == selSp) continue;
			KindAbstratusType type = sp.getKindAbstratus();
			if(type == null || !type.partOf(newStack)) continue;
			if(maxSize >= 0 && sp.getContents().size() >= maxSize) continue;
			
			newStack.set(MSItemComponents.STRIFE_ASSIGNED.get(), Unit.INSTANCE);
			sp.getContents().add(newStack);
			
			if(selSp != null) selSp.unassign(data.getSelectedWeaponIndex());
			data.setSelectedSpecibusIndex(i);
			data.setSelectedWeaponIndex(sp.getContents().indexOf(newStack));
			syncToClient(player);
			return sp;
		}
		
		return null;
	}
	
	/**
	 * Toggles the "armed" state for the weapon at {@code weaponIndex} of the
	 * currently selected specibus slot.
	 *
	 * <ul>
	 *   <li>Hand occupied by a real (non-assigned) item → does nothing.</li>
	 *   <li>Hand empty or has an assigned item → arm / disarm.</li>
	 * </ul>
	 */
	public static void retrieveWeapon(ServerPlayer player, int weaponIndex, InteractionHand hand)
	{
		StrifePortfolioData data = getData(player);
		StrifeSpecibus selSp = data.getSelectedSpecibus();
		if(selSp == null) return;
		
		ItemStack heldItem = player.getItemInHand(hand);
		boolean handEmpty = heldItem.isEmpty();
		boolean handArmed = isAssigned(heldItem);
		
		if(data.isArmed() && data.getSelectedWeaponIndex() == weaponIndex && handArmed)
		{
			heldItem.remove(MSItemComponents.STRIFE_ASSIGNED.get());
			int at = Math.min(weaponIndex, selSp.getContents().size());
			selSp.getContents().add(at, heldItem);
			player.setItemInHand(hand, ItemStack.EMPTY);
			data.setArmed(false);
			syncToClient(player);
			return;
		}
		
		if(!handEmpty && !handArmed) return;
		
		if(handArmed)
		{
			heldItem.remove(MSItemComponents.STRIFE_ASSIGNED.get());
			selSp.getContents().add(
					Math.min(data.getSelectedWeaponIndex(), selSp.getContents().size()),
					heldItem);
			player.setItemInHand(hand, ItemStack.EMPTY);
		}
		
		if(weaponIndex < 0 || weaponIndex >= selSp.getContents().size())
		{
			data.setArmed(false);
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
	 * Moves a weapon from a specibus deck slot into the player's offhand
	 * (and tries to assign the current offhand item to the portfolio in return).
	 */
	public static void swapOffhandWeapon(ServerPlayer player, int specibusIndex, int weaponIndex)
	{
		StrifePortfolioData data = getData(player);
		StrifeSpecibus sp = data.getPortfolio()[specibusIndex];
		if(sp == null) return;
		
		ItemStack weapon = sp.retrieveStack(weaponIndex);
		if(weapon.isEmpty()) return;
		
		// Disarm if this was the armed weapon
		if(data.isArmed()
				&& data.getSelectedSpecibusIndex() == specibusIndex
				&& data.getSelectedWeaponIndex()   == weaponIndex)
		{
			data.setArmed(false);
			for(InteractionHand h : InteractionHand.values())
				if(isAssigned(player.getItemInHand(h)))
					player.setItemInHand(h, ItemStack.EMPTY);
		}
		
		sp.unassign(weaponIndex);
		if(weaponIndex >= sp.getContents().size())
			data.setSelectedWeaponIndex(0);
		
		ItemStack currentOffhand = player.getItemInHand(InteractionHand.OFF_HAND);
		if(currentOffhand.isEmpty() || addWeapon(player, currentOffhand, false))
		{
			weapon.set(MSItemComponents.STRIFE_ASSIGNED.get(), Unit.INSTANCE);
			player.setItemInHand(InteractionHand.OFF_HAND, weapon);
		}
		else
		{
			player.drop(weapon, false);
		}
		
		syncToClient(player);
	}
	
	/**
	 * Removes the currently selected weapon from the active specibus deck and
	 * disarms the player.
	 */
	public static void unassignSelected(ServerPlayer player)
	{
		StrifePortfolioData data = getData(player);
		StrifeSpecibus selSp = data.getSelectedSpecibus();
		if(selSp == null) return;
		
		if(data.isArmed())
		{
			for(InteractionHand h : InteractionHand.values())
			{
				ItemStack held = player.getItemInHand(h);
				if(isAssigned(held))
				{
					held.remove(MSItemComponents.STRIFE_ASSIGNED.get());
					selSp.getContents().add(
							Math.min(data.getSelectedWeaponIndex(), selSp.getContents().size()),
							held);
					player.setItemInHand(h, ItemStack.EMPTY);
					break;
				}
			}
		}
		
		selSp.unassign(data.getSelectedWeaponIndex());
		if(data.getSelectedWeaponIndex() >= selSp.getContents().size())
			data.setSelectedWeaponIndex(0);
		data.setArmed(false);
		syncToClient(player);
	}
	/**
	 * Changes the active specibus slot.  Disarms the player if they were armed.
	 */
	public static void setSelectedSpecibus(ServerPlayer player, int index)
	{
		StrifePortfolioData data = getData(player);
		
		if(data.isArmed())
			clearArmedWeapon(player, data);
		
		data.setSelectedSpecibusIndex(index);
		syncToClient(player);
	}
	
	/** Clears the assigned item from the player's hands and marks data as unarmed. */
	private static void clearArmedWeapon(ServerPlayer player, StrifePortfolioData data)
	{
		for(InteractionHand hand : InteractionHand.values())
		{
			ItemStack held = player.getItemInHand(hand);
			if(isAssigned(held))
			{
				player.setItemInHand(hand, ItemStack.EMPTY);
				break;
			}
		}
		data.setArmed(false);
	}
	
	/** Returns a copy of the stack with the STRIFE_ASSIGNED component removed (for comparison). */
	private static ItemStack stripAssigned(ItemStack stack)
	{
		if(stack.isEmpty()) return stack;
		ItemStack copy = stack.copy();
		copy.remove(MSItemComponents.STRIFE_ASSIGNED.get());
		return copy;
	}
	/** Wraps a {@link StrifeSpecibus} into a {@link StrifeCardItem} ItemStack. */
	public static ItemStack createStrifeCard(@Nullable StrifeSpecibus specibus)
	{
		ItemStack card = new ItemStack(MSItems.STRIFE_CARD.get());
		if(specibus != null)
			card.set(MSItemComponents.STRIFE_SPECIBUS_DATA.get(), specibus);
		return card;
	}
}