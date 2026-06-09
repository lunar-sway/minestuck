package com.mraof.minestuck.event;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.advancements.MSCriteriaTriggers;
import com.mraof.minestuck.entity.underling.UnderlingEntity;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.item.components.MSItemComponents;
import com.mraof.minestuck.player.*;
import com.mraof.minestuck.strife.StrifePortfolioHandler;
import com.mraof.minestuck.util.MSAttachments;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Unit;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.event.entity.item.ItemTossEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.player.ItemEntityPickupEvent;
import net.neoforged.neoforge.event.entity.player.PlayerDestroyItemEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Handles all game-event logic for the Strife Portfolio system
 */
@EventBusSubscriber(modid = Minestuck.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public final class StrifeEventHandler
{
	@SubscribeEvent
	public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event)
	{
		if(event.getEntity() instanceof ServerPlayer player) StrifePortfolioHandler.syncToClient(player);
	}
	
	@SubscribeEvent
	public static void onPlayerDimensionChange(PlayerEvent.PlayerChangedDimensionEvent event)
	{
		if(event.getEntity() instanceof ServerPlayer player) StrifePortfolioHandler.syncToClient(player);
	}
	
	@SubscribeEvent
	public static void onPlayerTick(PlayerTickEvent.Post event)
	{
		if(!(event.getEntity() instanceof ServerPlayer player)) return;
		if(player instanceof FakePlayer) return;
		
		checkArmedState(player);
		checkAbstrataSwitcherUnlock(player);
	}
	
	private static void checkArmedState(ServerPlayer player)
	{
		StrifePortfolioData data = StrifePortfolioHandler.getData(player);
		
		if(!data.isArmed())
		{
			clearStrayAssigned(player);
			return;
		}
		
		StrifeSpecibus selSp = data.getSelectedSpecibus();
		if(selSp == null || selSp.getContents().isEmpty())
		{
			data.setArmed(false);
			StrifePortfolioHandler.syncToClient(player);
			return;
		}
		
		int wIdx = data.getSelectedWeaponIndex();
		if(wIdx < 0 || wIdx >= selSp.getContents().size())
		{
			data.setArmed(false);
			StrifePortfolioHandler.syncToClient(player);
			return;
		}
		
		ItemStack deckWeapon = selSp.getContents().get(wIdx);
		ItemStack mainHand = player.getMainHandItem();
		
		boolean weaponHeld = StrifePortfolioHandler.isAssigned(mainHand) && ItemStack.isSameItemSameComponents(withoutAssigned(mainHand), withoutAssigned(deckWeapon));
		
		if(!weaponHeld && !mainHand.isEmpty() && !StrifePortfolioHandler.isAssigned(mainHand) && player.containerMenu == player.inventoryMenu)
		{
			StrifeSpecibus moved = StrifePortfolioHandler.moveSelectedWeapon(player, mainHand);
			weaponHeld = (moved != null);
		}
		
		if(!weaponHeld)
		{
			data.setArmed(false);
			StrifePortfolioHandler.syncToClient(player);
		}
		
		clearStrayAssigned(player);
	}
	
	/**
	 * Strips STRIFE_ASSIGNED from every inventory slot that shouldn't have it.
	 */
	private static void clearStrayAssigned(ServerPlayer player)
	{
		StrifePortfolioData data = StrifePortfolioHandler.getData(player);
		boolean armed = data.isArmed();
		
		// Main inventory
		for(int i = 0; i < player.getInventory().getContainerSize(); i++)
		{
			ItemStack stack = player.getInventory().getItem(i);
			if(!StrifePortfolioHandler.isAssigned(stack)) continue;
			
			boolean isArmedWeapon = armed && i == player.getInventory().selected && matchesArmedWeapon(stack, data);
			
			if(!isArmedWeapon) stack.remove(MSItemComponents.STRIFE_ASSIGNED.get());
		}
		
		// Offhand
		ItemStack offhand = player.getOffhandItem();
		if(StrifePortfolioHandler.isAssigned(offhand) && !armed) offhand.remove(MSItemComponents.STRIFE_ASSIGNED.get());
	}
	
	private static boolean matchesArmedWeapon(ItemStack stack, StrifePortfolioData data)
	{
		StrifeSpecibus sp = data.getSelectedSpecibus();
		if(sp == null || sp.getContents().isEmpty()) return false;
		int wIdx = data.getSelectedWeaponIndex();
		if(wIdx < 0 || wIdx >= sp.getContents().size()) return false;
		return ItemStack.isSameItemSameComponents(withoutAssigned(stack), withoutAssigned(sp.getContents().get(wIdx)));
	}
	
	private static ItemStack withoutAssigned(ItemStack stack)
	{
		if(stack.isEmpty() || !StrifePortfolioHandler.isAssigned(stack)) return stack;
		ItemStack copy = stack.copy();
		copy.remove(MSItemComponents.STRIFE_ASSIGNED.get());
		return copy;
	}
	
	private static void checkAbstrataSwitcherUnlock(ServerPlayer player)
	{
		int threshold = MinestuckConfig.SERVER.abstrataSwitcherRung.get();
		if(threshold == Rungs.finalRung()) return; // permanently disabled in config
		
		int rung = Echeladder.get(player).getRung();
		boolean shouldUnlock = threshold == -1 || rung >= threshold;
		
		StrifePortfolioData data = StrifePortfolioHandler.getData(player);
		if(data.abstrataSwitcherUnlocked() != shouldUnlock)
		{
			data.unlockAbstrataSwitcher(shouldUnlock);
			StrifePortfolioHandler.syncToClient(player);
			if(shouldUnlock)
				player.sendSystemMessage(net.minecraft.network.chat.Component.translatable("status.strife.unlockSwitcher"), false);
		}
	}
	
	/**
	 * If {@code restrictedStrife} is enabled, cancels attacks made with
	 * weapons that are not assigned to the player's portfolio.
	 */
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public static void onEntityAttack(LivingIncomingDamageEvent event)
	{
		Entity attacker = event.getSource().getEntity();
		if(!(attacker instanceof ServerPlayer player) || attacker instanceof FakePlayer) return;
		
		ItemStack held = player.getMainHandItem();
		StrifePortfolioData data = StrifePortfolioHandler.getData(player);
		
		if(event.getEntity() instanceof UnderlingEntity)
		{
			double mod = player.getAttributeValue(com.mraof.minestuck.entity.MSAttributes.UNDERLING_DAMAGE_MODIFIER);
			event.setAmount((float) (event.getAmount() * mod));
		}
		
		if(!MinestuckConfig.SERVER.restrictedStrife.get()) return;
		if(data.isPortfolioEmpty()) return;
		
		// Empty hand is allowed
		if(held.isEmpty()) return;
		
		if(!StrifePortfolioHandler.isAssigned(held)) event.setAmount(0f); // cancel attack
	}
	
	/**
	 * Applies the weapon-attack multiplier for non-assigned weapons against
	 * non-underling targets when the portfolio is not empty.
	 */
	@SubscribeEvent
	public static void onLivingDamagePre(LivingDamageEvent.Pre event)
	{
		if(!(event.getSource().getEntity() instanceof ServerPlayer player)) return;
		if(player instanceof FakePlayer) return;
		if(event.getEntity() instanceof UnderlingEntity) return; // full damage vs underlings
		
		ItemStack held = player.getMainHandItem();
		StrifePortfolioData data = StrifePortfolioHandler.getData(player);
		
		if(data.isPortfolioEmpty()) return;
		if(StrifePortfolioHandler.isAssigned(held)) return; // correctly armed weapon
		
		float mult = MinestuckConfig.SERVER.weaponAttackMultiplier.get().floatValue();
		event.setNewDamage(event.getNewDamage() * mult);
	}
	
	@SubscribeEvent
	public static void onItemDestroyed(PlayerDestroyItemEvent event)
	{
		if(!(event.getEntity() instanceof ServerPlayer player)) return;
		if(event.getHand() != InteractionHand.MAIN_HAND) return;
		
		ItemStack from = event.getOriginal();
		if(!from.isDamageableItem()) return;
		if(!StrifePortfolioHandler.isAssigned(from)) return;
		
		Item halfItem = getHalfBlade(from);
		if(halfItem == null) return;
		
		StrifePortfolioData data = StrifePortfolioHandler.getData(player);
		StrifeSpecibus selSp = data.getSelectedSpecibus();
		if(selSp == null) return;
		if(!KindAbstratusList.SWORD.equals(selSp.getAbstratusName())) return;
		
		int wIdx = data.getSelectedWeaponIndex();
		if(wIdx < 0 || wIdx >= selSp.getContents().size()) return;
		
		// Build half-sword with same enchantments
		ItemStack halfStack = new ItemStack(halfItem);
		EnchantmentHelper.updateEnchantments(halfStack, mutable -> EnchantmentHelper.getEnchantmentsForCrafting(from).keySet().forEach(ench -> mutable.set(ench, EnchantmentHelper.getEnchantmentsForCrafting(from).getLevel(ench))));
		halfStack.set(MSItemComponents.STRIFE_ASSIGNED.get(), Unit.INSTANCE);
		
		selSp.getContents().set(wIdx, halfStack);
		
		// Upgrade specibus to half_sword (drops nothing since half-sword matches half_sword kind)
		List<ItemStack> dropped = selSp.switchKindAbstratus(KindAbstratusList.HALF_SWORD);
		dropped.forEach(d -> player.drop(d, false));
		
		// Update hand
		player.setItemInHand(InteractionHand.MAIN_HAND, halfStack);
		
		MSCriteriaTriggers.BLADEKIND_BREAK.get().trigger(player);
		StrifePortfolioHandler.syncToClient(player);
	}
	
	@Nullable
	private static Item getHalfBlade(ItemStack from)
	{
		if(from.is(com.mraof.minestuck.util.MSTags.Items.KIND_SWORD))
		{
			if(from.getItem() == MSItems.KATANA.get()) return MSItems.HALF_KATANA.get();
			if(from.getItem() == MSItems.CALEDFWLCH.get()) return MSItems.HALF_CALEDFWLCH.get();
			if(from.getItem() == MSItems.ROYAL_DERINGER.get()) return MSItems.HALF_ROYAL_DERINGER.get();
			if(from.getItem() == MSItems.SCARLET_RIBBITAR.get()) return MSItems.HALF_SCARLET_RIBBITAR.get();
			if(from.getItem() == MSItems.CALEDSCRATCH.get()) return MSItems.HALF_CALEDSCRATCH.get();
		}
		return null;
	}
	
	@SubscribeEvent
	public static void onPlayerDropItem(ItemTossEvent event)
	{
		ItemEntity dropped = event.getEntity();
		if(StrifePortfolioHandler.isAssigned(dropped.getItem()))
			dropped.getItem().remove(MSItemComponents.STRIFE_ASSIGNED.get());
	}
	
	/**
	 * Clean up any entity items that somehow ended up with STRIFE_ASSIGNED in the world.
	 */
	@SubscribeEvent
	public static void onItemPickup(ItemEntityPickupEvent.Post event)
	{
		if(!(event.getPlayer() instanceof ServerPlayer player)) return;
		
		StrifePortfolioData data = StrifePortfolioHandler.getData(player);
		
		if(!data.isArmed()) event.getCurrentStack().remove(MSItemComponents.STRIFE_ASSIGNED.get());
	}
	
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onPlayerDrops(LivingDropsEvent event)
	{
		if(!(event.getEntity() instanceof ServerPlayer player)) return;
		
		StrifePortfolioData data = StrifePortfolioHandler.getData(player);
		
		event.getDrops().removeIf(e -> StrifePortfolioHandler.isAssigned(e.getItem()));
		data.setArmed(false);
		
		if(!MinestuckConfig.SERVER.keepPortfolioOnDeath.get())
		{
			for(StrifeSpecibus specibus : data.getPortfolio())
			{
				if(specibus == null) continue;
				
				ItemStack card = StrifePortfolioHandler.createStrifeCard(specibus);
				ItemEntity dropped = player.drop(card, true, false);
				
				if(dropped != null) event.getDrops().add(dropped);
			}
			
			data.clearPortfolio();
			data.setSelectedSpecibusIndex(-1);
		}
	}
	
	@SubscribeEvent
	public static void onPlayerRespawn(PlayerEvent.Clone event)
	{
		if(!event.isWasDeath()) return;
		
		StrifePortfolioData original = event.getOriginal().getData(MSAttachments.STRIFE_PORTFOLIO.get());
		
		if(MinestuckConfig.SERVER.keepPortfolioOnDeath.get())
		{
			// Original still has portfolio data; copy to the new player entity
			StrifePortfolioData copy = new StrifePortfolioData();
			for(int i = 0; i < StrifePortfolioData.PORTFOLIO_SIZE; i++)
				copy.setSpecibus(original.getPortfolio()[i], i);
			copy.setSelectedSpecibusIndex(original.getSelectedSpecibusIndex());
			copy.setSelectedWeaponIndex(original.getSelectedWeaponIndex());
			copy.setArmed(false); // always respawn unarmed
			copy.unlockAbstrataSwitcher(original.abstrataSwitcherUnlocked());
			
			event.getEntity().setData(MSAttachments.STRIFE_PORTFOLIO.get(), copy);
		}
		// If keepPortfolioOnDeath=false: portfolio was already cleared in onPlayerDrops;
		// new entity gets a fresh empty StrifePortfolioData automatically.
	}
}