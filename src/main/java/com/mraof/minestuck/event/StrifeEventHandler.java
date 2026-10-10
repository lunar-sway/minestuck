package com.mraof.minestuck.event;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.entity.MSAttributes;
import com.mraof.minestuck.entity.underling.UnderlingEntity;
import com.mraof.minestuck.item.components.MSItemComponents;
import com.mraof.minestuck.player.Echeladder;
import com.mraof.minestuck.player.KindAbstratusList;
import com.mraof.minestuck.player.KindAbstratusType;
import com.mraof.minestuck.player.Rungs;
import com.mraof.minestuck.player.StrifePortfolioData;
import com.mraof.minestuck.player.StrifeSpecibus;
import com.mraof.minestuck.strife.StrifeCardDrops;
import com.mraof.minestuck.strife.StrifeEvolution;
import com.mraof.minestuck.strife.StrifePortfolioHandler;
import com.mraof.minestuck.util.MSAttachments;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.common.util.TriState;
import net.neoforged.neoforge.event.entity.item.ItemTossEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.player.ItemEntityPickupEvent;
import net.neoforged.neoforge.event.entity.player.PlayerDestroyItemEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import java.util.Iterator;

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
	public static void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event)
	{
		if(event.getEntity() instanceof ServerPlayer player) StrifeEvolution.clearPending(player);
	}
	
	@SubscribeEvent
	public static void onPlayerRespawned(PlayerEvent.PlayerRespawnEvent event)
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
		if(StrifePortfolioHandler.isLockedByEditmode(player)) return;
		
		StrifeEvolution.applyPending(player);
		checkArmedState(player);
		checkAbstrataSwitcherUnlock(player);
	}
	
	/**
	 * While armed, the weapon has to be in the main hand. If it left the hand (hotbar scroll, inventory click, ...)
	 * it goes back into its deck, and a compatible weapon that is held instead is armed automatically.
	 */
	private static void checkArmedState(ServerPlayer player)
	{
		StrifePortfolioData data = StrifePortfolioHandler.getData(player);
		
		if(data.isArmed())
		{
			StrifeSpecibus selSp = data.getSelectedSpecibus();
			ItemStack mainHand = player.getMainHandItem();
			
			if(selSp == null)
			{
				// Nowhere to return the weapon to: it simply stays as a normal item
				data.setArmed(false);
				StrifePortfolioHandler.syncToClient(player);
			} else if(MinestuckConfig.SERVER.keepArmedWeaponInInventory.get())
			{
				//The armed weapon may stay anywhere in the inventory. All that has to be noticed is that it is gone (thrown away, for example).
				if(player.tickCount % 10 == 0 && !StrifePortfolioHandler.hasAssignedWeapon(player))
				{
					data.setArmed(false);
					StrifePortfolioHandler.syncToClient(player);
				}
			} else if(!StrifePortfolioHandler.isAssigned(mainHand))
			{
				returnStrayArmedWeapon(player, data, selSp);
				data.setArmed(false);
				
				ItemStack held = player.getMainHandItem();
				if(!held.isEmpty() && player.containerMenu == player.inventoryMenu)
					StrifePortfolioHandler.moveSelectedWeapon(player, held);
				
				StrifePortfolioHandler.syncToClient(player);
			}
		}
		
		clearStrayAssigned(player);
	}
	
	/**
	 * Finds the armed weapon somewhere else than the main hand and puts it back into the deck.
	 */
	private static void returnStrayArmedWeapon(ServerPlayer player, StrifePortfolioData data, StrifeSpecibus deck)
	{
		StrifePortfolioHandler.returnStrayArmedWeapon(player, data, deck);
	}
	
	/**
	 * Strips STRIFE_ASSIGNED from every inventory slot that shouldn't have it.
	 */
	private static void clearStrayAssigned(ServerPlayer player)
	{
		boolean armed = StrifePortfolioHandler.getData(player).isArmed();
		boolean keepInInventory = MinestuckConfig.SERVER.keepArmedWeaponInInventory.get();
		boolean armedWeaponFound = false;
		
		for(int i = 0; i < player.getInventory().getContainerSize(); i++)
		{
			ItemStack stack = player.getInventory().getItem(i);
			if(!StrifePortfolioHandler.isAssigned(stack)) continue;
			
			boolean isArmedWeapon = armed && (keepInInventory ? !armedWeaponFound : i == player.getInventory().selected);
			if(isArmedWeapon) armedWeaponFound = true;
			else stack.remove(MSItemComponents.STRIFE_ASSIGNED.get());
		}
		
		ItemStack carried = player.containerMenu.getCarried();
		if(StrifePortfolioHandler.isAssigned(carried)) carried.remove(MSItemComponents.STRIFE_ASSIGNED.get());
	}
	
	private static void checkAbstrataSwitcherUnlock(ServerPlayer player)
	{
		//TODO: useless checks
		int threshold = MinestuckConfig.SERVER.abstrataSwitcherRung.get();
		int rung = Echeladder.get(player).getRung();
		boolean shouldUnlock = threshold != Rungs.finalRung() && (threshold == -1 || rung >= threshold);
		
		StrifePortfolioData data = StrifePortfolioHandler.getData(player);
		if(data.abstrataSwitcherUnlocked() != shouldUnlock)
		{
			data.unlockAbstrataSwitcher(shouldUnlock);
			StrifePortfolioHandler.syncToClient(player);
			if(shouldUnlock)
				player.sendSystemMessage(Component.translatable("status.strife.unlockSwitcher"), false);
		}
	}
	
	/**
	 * True if the stack is a weapon of at least one kind abstratus.
	 */
	private static boolean isKindWeapon(ItemStack stack)
	{
		if(stack.isEmpty()) return false;
		for(KindAbstratusType type : KindAbstratusList.getTypeList())
			if(type.partOf(stack)) return true;
		return false;
	}
	
	private static boolean isBypassed(ItemStack stack)
	{
		ResourceLocation id = BuiltInRegistries.ITEM.getKey(stack.getItem());
		return MinestuckConfig.SERVER.restrictedStrifeBypass.get().contains(id.toString());
	}
	
	/**
	 * If {@code restrictedStrife} is enabled, cancels melee attacks made with
	 * weapons that are not assigned to the player's portfolio.
	 */
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public static void onEntityAttack(LivingIncomingDamageEvent event)
	{
		Entity attacker = event.getSource().getEntity();
		if(!(attacker instanceof ServerPlayer player) || attacker instanceof FakePlayer) return;
		if(StrifePortfolioHandler.isLockedByEditmode(player)) return;
		
		if(event.getEntity() instanceof UnderlingEntity)
		{
			double mod = player.getAttributeValue(MSAttributes.UNDERLING_DAMAGE_MODIFIER);
			event.setAmount((float) (event.getAmount() * mod));
		}
		
		if(!MinestuckConfig.SERVER.restrictedStrife.get()) return;
		if(event.getSource().getDirectEntity() != player) return; // only melee attacks are restricted here
		
		ItemStack held = player.getMainHandItem();
		if(held.isEmpty() || StrifePortfolioHandler.isAssigned(held) || isBypassed(held)) return;
		if(StrifePortfolioHandler.getData(player).isPortfolioEmpty()) return;
		
		event.setCanceled(true);
	}
	
	/**
	 * With {@code restrictedStrife}, weapons that aren't assigned to the portfolio cannot be used with right-click either.
	 * Items in the {@code restrictedStrifeBypass} config list are still usable.
	 */
	@SubscribeEvent
	public static void onItemRightClick(PlayerInteractEvent.RightClickItem event)
	{
		if(!MinestuckConfig.SERVER.restrictedStrife.get()) return;
		if(event.getEntity() instanceof FakePlayer) return;
		if(StrifePortfolioHandler.isLockedByEditmode(event.getEntity())) return;
		
		ItemStack stack = event.getItemStack();
		if(stack.isEmpty() || StrifePortfolioHandler.isAssigned(stack) || isBypassed(stack)) return;
		if(StrifePortfolioHandler.getData(event.getEntity()).isPortfolioEmpty()) return;
		if(!isKindWeapon(stack)) return;
		
		event.setCancellationResult(InteractionResult.PASS);
		event.setCanceled(true);
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
		if(StrifePortfolioHandler.isLockedByEditmode(player)) return;
		if(event.getSource().getDirectEntity() != player) return;
		if(event.getEntity() instanceof UnderlingEntity) return; // full damage vs underlings
		
		ItemStack held = player.getMainHandItem();
		if(!isKindWeapon(held) || StrifePortfolioHandler.isAssigned(held)) return;
		if(StrifePortfolioHandler.getData(player).isPortfolioEmpty()) return;
		
		float mult = MinestuckConfig.SERVER.weaponAttackMultiplier.get().floatValue();
		event.setNewDamage(event.getNewDamage() * mult);
	}
	
	/**
	 * When a weapon breaks it may evolve, see {@link StrifeEvolution}.
	 */
	@SubscribeEvent
	public static void onItemDestroyed(PlayerDestroyItemEvent event)
	{
		if(!(event.getEntity() instanceof ServerPlayer player) || player instanceof FakePlayer) return;
		if(StrifePortfolioHandler.isLockedByEditmode(player)) return;
		if(event.getHand() != InteractionHand.MAIN_HAND) return;
		
		ItemStack broken = event.getOriginal();
		if(!broken.isDamageableItem()) return;
		
		StrifeEvolution.onItemBroken(player, broken);
	}
	
	@SubscribeEvent
	public static void onPlayerDropItem(ItemTossEvent event)
	{
		ItemEntity dropped = event.getEntity();
		if(StrifePortfolioHandler.isAssigned(dropped.getItem()))
			dropped.getItem().remove(MSItemComponents.STRIFE_ASSIGNED.get());
	}
	
	/**
	 * Weapons that a player walks over are moved straight into a matching strife deck (if enabled in the config).
	 */
	@SubscribeEvent
	public static void onItemPickupPre(ItemEntityPickupEvent.Pre event)
	{
		if(!MinestuckConfig.SERVER.autoStowWeapons.get()) return;
		if(!(event.getPlayer() instanceof ServerPlayer player) || player instanceof FakePlayer) return;
		if(StrifePortfolioHandler.isLockedByEditmode(player)) return;
		
		ItemEntity itemEntity = event.getItemEntity();
		ItemStack stack = itemEntity.getItem();
		
		if(StrifePortfolioHandler.autoStow(player, stack))
		{
			player.take(itemEntity, stack.getCount());
			itemEntity.discard();
			event.setCanPickup(TriState.FALSE);
		}
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
	
	/**
	 * Hostile mobs killed by a player may drop strife cards.
	 */
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onMobDrops(LivingDropsEvent event)
	{
		StrifeCardDrops.onMobDrops(event);
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onPlayerDrops(LivingDropsEvent event)
	{
		if(!(event.getEntity() instanceof ServerPlayer player)) return;
		
		StrifePortfolioData data = StrifePortfolioHandler.getData(player);
		
		// The armed weapon is not part of its deck, so it goes back before the drops are cleaned
		StrifeSpecibus selected = data.getSelectedSpecibus();
		boolean returned = false;
		Iterator<ItemEntity> iterator = event.getDrops().iterator();
		while(iterator.hasNext())
		{
			ItemStack stack = iterator.next().getItem();
			if(!StrifePortfolioHandler.isAssigned(stack)) continue;
			
			if(!returned && selected != null && data.isArmed())
			{
				ItemStack weapon = stack.copy();
				weapon.remove(MSItemComponents.STRIFE_ASSIGNED.get());
				selected.getContents().add(data.armedWeaponSlot(selected.getContents().size()), weapon);
				returned = true;
			}
			iterator.remove();
		}
		data.setArmed(false);
		
		if(!MinestuckConfig.SERVER.keepPortfolioOnDeath.get())
		{
			for(StrifeSpecibus specibus : data.getPortfolio())
			{
				if(specibus == null) continue;
				
				ItemEntity card = new ItemEntity(player.level(), player.getX(), player.getY(), player.getZ(), StrifePortfolioHandler.createStrifeCard(specibus));
				card.setDefaultPickUpDelay();
				event.getDrops().add(card);
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
		StrifePortfolioData copy = new StrifePortfolioData();
		
		// unlocked switcher, completed evolutions and card drop counter always survive death
		copy.copyPersistentStateFrom(original);
		
		if(MinestuckConfig.SERVER.keepPortfolioOnDeath.get())
		{
			for(int i = 0; i < StrifePortfolioData.PORTFOLIO_SIZE; i++)
				copy.setSpecibus(original.getPortfolio()[i], i);
			copy.setSelectedSpecibusIndex(original.getSelectedSpecibusIndex());
			copy.setSelectedWeaponIndex(original.getSelectedWeaponIndex());
			copy.setArmed(false); // always respawn unarmed
		}
		// ff keepPortfolioOnDeath=false, the portfolio was already dropped as cards in onPlayerDrops
		event.getEntity().setData(MSAttachments.STRIFE_PORTFOLIO.get(), copy);
	}
}
