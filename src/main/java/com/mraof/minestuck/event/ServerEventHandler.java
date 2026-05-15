package com.mraof.minestuck.event;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.advancements.MSCriteriaTriggers;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.effects.CreativeShockEffect;
import com.mraof.minestuck.effects.MSEffects;
import com.mraof.minestuck.entity.MSAttributes;
import com.mraof.minestuck.entity.underling.UnderlingEntity;
import com.mraof.minestuck.entry.EntryEvent;
import com.mraof.minestuck.inventory.captchalogue.ArrayModus;
import com.mraof.minestuck.inventory.captchalogue.CaptchaDeckHandler;
import com.mraof.minestuck.inventory.captchalogue.HashMapModus;
import com.mraof.minestuck.inventory.captchalogue.Modus;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.network.SyncSpecibusPacket;
import com.mraof.minestuck.player.*;
import com.mraof.minestuck.skaianet.TitleSelectionHook;
import com.mraof.minestuck.util.MSAttachments;
import com.mraof.minestuck.util.MSTags;
import com.mraof.minestuck.world.storage.MSExtraData;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.AABB;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.event.ServerChatEvent;
import net.neoforged.neoforge.event.TagsUpdatedEvent;
import net.neoforged.neoforge.event.entity.item.ItemExpireEvent;
import net.neoforged.neoforge.event.entity.living.*;
import net.neoforged.neoforge.event.entity.player.CriticalHitEvent;
import net.neoforged.neoforge.event.entity.player.PlayerDestroyItemEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@EventBusSubscriber(modid = Minestuck.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class ServerEventHandler
{
	@SubscribeEvent
	public static void serverStopped(ServerStoppedEvent event)
	{
		IdentifierHandler.clear();
	}
	
	@SubscribeEvent
	public static void onServerTick(ServerTickEvent.Post event)
	{
		MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
		
		if(event.hasTime())
			MSExtraData.get(server).executeEntryTasks(server);
		
		if(MinestuckConfig.SERVER.hardMode.get())
			EntryEvent.tick(server);
	
	}
	
	@SubscribeEvent(priority=EventPriority.LOWEST, receiveCanceled=false)
	public static void onEntityDeath(LivingDeathEvent event)
	{
		if(event.getEntity() instanceof ServerPlayer)
		{
			TitleSelectionHook.cancelSelection((ServerPlayer) event.getEntity());
		}
	}

	// Stores the crit result from the CriticalHitEvent, to be used during LivingHurtEvent to trigger special effects of any weapons.
	// This method is reliable only as long as LivingHurtEvent is posted only on the main thread and after a matching CriticalHitEvent
	private static boolean cachedCrit;

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onCrit(CriticalHitEvent event)
	{
		if(!event.getEntity().level().isClientSide)
			cachedCrit = event.isCriticalHit() && event.isVanillaCritical();
	}
	
	public static boolean wasLastHitCrit(LivingEntity entity)
	{
		return entity instanceof ServerPlayer && cachedCrit;
	}
	
	@SubscribeEvent(priority=EventPriority.NORMAL)
	public static void onEntityAttack(LivingIncomingDamageEvent event)
	{
		if(event.getSource().getEntity() != null)
		{
			Entity attacker = event.getSource().getEntity();
			Entity injured = event.getEntity();
			
			boolean attackerIsRealPlayer = attacker instanceof ServerPlayer && !(attacker instanceof FakePlayer);
			boolean injuredIsRealPlayer = injured instanceof ServerPlayer && !(injured instanceof FakePlayer);
			
			if(attackerIsRealPlayer)
			{
				ServerPlayer player = (ServerPlayer) attacker;
				List<String> selected = player.getData(MSAttachments.SELECTED_SPECIBUS);
				
				if(!selected.isEmpty() && !hasSpecibusMatch(player))
				{
					event.setAmount(event.getAmount() * 0.15f);
				}
			}
			
			if(attackerIsRealPlayer && injured instanceof UnderlingEntity)
			{
				//Increase damage to underling
				double modifier = ((ServerPlayer) attacker).getAttributeValue(MSAttributes.UNDERLING_DAMAGE_MODIFIER);
				event.setAmount((float) (event.getAmount() * modifier));
			} else if (injuredIsRealPlayer && attacker instanceof UnderlingEntity)
			{
				//Decrease damage to player
				double modifier = ((ServerPlayer) injured).getAttributeValue(MSAttributes.UNDERLING_PROTECTION_MODIFIER);
				event.setAmount((float) (event.getAmount() * modifier));
			}
		}
		
		if(event.getEntity() instanceof UnderlingEntity underling)
		{
			underling.onEntityDamaged(event.getSource(), event.getAmount());
		}
	}
	
	//	private static KindAbstratusType getSelectedSpecibus(ServerPlayer player)
//	{
//		List<String> selected = player.getData(MSAttachments.SELECTED_SPECIBUS);
//
//		if(selected.isEmpty())
//			return null;
//
//		return KindAbstratusList.getTypeFromName(selected);
//	}
	
	private static void syncSpecibus(ServerPlayer player)
	{
		List<String> selected = player.getData(MSAttachments.SELECTED_SPECIBUS);
		
		if(!selected.isEmpty())
		{
			PacketDistributor.sendToPlayer(player, new SyncSpecibusPacket(selected, MinestuckConfig.SERVER.maxSpecibusCount.get()));
		}
	}
	
	@SubscribeEvent
	public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event)
	{
		if(event.getEntity() instanceof ServerPlayer player)
			syncSpecibus(player);
	}
	
	private static boolean hasSpecibusMatch(ServerPlayer player)
	{
		List<String> selected = player.getData(MSAttachments.SELECTED_SPECIBUS);
		if(selected.isEmpty()) return false;
		
		ItemStack held = player.getMainHandItem();
		
		return selected.stream()
				.map(KindAbstratusList::getTypeFromName)
				.filter(Objects::nonNull)
				.anyMatch(type -> type.partOf(held));
	}

//		@SubscribeEvent
//	public static void onLivingAttack(LivingIncomingDamageEvent event) {
//		if (!(event.getSource().getEntity() instanceof ServerPlayer player)) return;
//
//		String selected = player.getData(MSAttachments.SELECTED_SPECIBUS);
//		if (selected.isEmpty()) return;
//
//		KindAbstratusType type = KindAbstratusList.getTypeFromName(selected);
//		if (type == null) return;
//
//		if (!type.partOf(player.getMainHandItem())) {
//			float reducedDamage = event.getAmount() * 0.15f;
//			event.setAmount(reducedDamage);
//		}
//	}
	// [debug_stuff]
/*	@SubscribeEvent
	public static void onLivingAttack(LivingIncomingDamageEvent event)
	{
		if(!(event.getSource().getEntity() instanceof ServerPlayer attacker)) return;
		
		String selected = attacker.getData(MSAttachments.SELECTED_SPECIBUS);
		if(selected.isEmpty()) return;
		
		KindAbstratusType type = KindAbstratusList.getTypeFromName(selected);
		if(type == null) return;
		
		ItemStack held = attacker.getMainHandItem();
		
		if(!type.partOf(held))
		{
			event.setCanceled(true);
		}
	}*/
	private static float getDamageMultiplier(int selectedCount)
	{
		int max = MinestuckConfig.SERVER.maxSpecibusCount.get();
		if(selectedCount <= 0 || max <= 1) return 1.0f;
		float step = 0.75f / (max - 1);
		return 2.0f - step * (selectedCount - 1);
	}

	@SubscribeEvent
	public static void onLivingDamagePre(LivingDamageEvent.Pre event)
	{
		if(!(event.getSource().getEntity() instanceof ServerPlayer player)) return;
		
		List<String> selected = player.getData(MSAttachments.SELECTED_SPECIBUS);
		if(selected.isEmpty()) return;
		
		if(hasSpecibusMatch(player))
		{
			float multiplier = getDamageMultiplier(selected.size());
			event.setNewDamage(event.getNewDamage() * multiplier);
		}
	}
	
	@SubscribeEvent
	public static void onTagsUpdated(TagsUpdatedEvent event) {
		KindAbstratusList.reloadFromTags();
	}
	
	@SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = false)
	public static void onPlayerInjured(LivingDamageEvent.Post event)
	{
		if(event.getEntity() instanceof ServerPlayer injuredPlayer && !(injuredPlayer instanceof FakePlayer))
		{
			boolean isDoom = Title.isPlayerOfAspect(injuredPlayer, EnumAspect.DOOM);
			ItemStack handItem = injuredPlayer.getMainHandItem();
			float activateThreshold = ((injuredPlayer.getMaxHealth() / (injuredPlayer.getHealth() + 1)) / injuredPlayer.getMaxHealth()); //fraction of players health that rises dramatically the more injured they are
			
			//TODO make a property
			if(handItem.getItem() == MSItems.LUCERNE_HAMMER_OF_UNDYING.get())
			{
				if(isDoom)
					activateThreshold = activateThreshold * 1.5F;
				
				activateThreshold = activateThreshold + injuredPlayer.getRandom().nextFloat() * .9F;
				
				if(activateThreshold >= 1.0F && injuredPlayer.getRandom().nextFloat() >= .75)
				{
					injuredPlayer.level().playSound(null, injuredPlayer.getX(), injuredPlayer.getY(), injuredPlayer.getZ(), SoundEvents.TOTEM_USE, SoundSource.PLAYERS, 1.0F, 1.4F);
					injuredPlayer.setHealth(injuredPlayer.getHealth() + 3);
					injuredPlayer.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 450, 0));
					if(isDoom)
					{
						
						injuredPlayer.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 100, 0));
						handItem.hurtAndBreak(100, injuredPlayer, EquipmentSlot.MAINHAND);
					} else
					{
						handItem.hurtAndBreak(250, injuredPlayer, EquipmentSlot.MAINHAND);
					}
				}
			}
			
			//TODO make a property
			if(handItem.getItem() == MSItems.CRUEL_FATE_CRUCIBLE.get())
			{
				activateThreshold = activateThreshold * 8 + injuredPlayer.getRandom().nextFloat() * .9F;
				
				if((isDoom && activateThreshold >= 1.0F && injuredPlayer.getRandom().nextFloat() <= .2) || (!isDoom && activateThreshold >= 1.0F && injuredPlayer.getRandom().nextFloat() <= .05))
				{
					AABB axisalignedbb = injuredPlayer.getBoundingBox().inflate(4.0D, 2.0D, 4.0D);
					List<LivingEntity> list = injuredPlayer.level().getEntitiesOfClass(LivingEntity.class, axisalignedbb);
					list.remove(injuredPlayer);
					if(!list.isEmpty())
					{
						injuredPlayer.level().playSound(null, injuredPlayer.getX(), injuredPlayer.getY(), injuredPlayer.getZ(), SoundEvents.WITHER_HURT, SoundSource.PLAYERS, 0.5F, 1.6F);
						if(isDoom)
							handItem.hurtAndBreak(2, injuredPlayer, EquipmentSlot.MAINHAND);
						else
							handItem.hurtAndBreak(10, injuredPlayer, EquipmentSlot.MAINHAND);
						for(LivingEntity livingentity : list)
						{
							livingentity.addEffect(new MobEffectInstance(MobEffects.HARM, 1, 1));
						}
					}
				}
			}
		}
	}
	
	@SubscribeEvent
	public static void playerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event)
	{
		TitleSelectionHook.cancelSelection((ServerPlayer) event.getEntity());
		ServerPlayer player = (ServerPlayer) event.getEntity();
		TitleSelectionHook.cancelSelection(player);
		List<String> selected = player.getData(MSAttachments.SELECTED_SPECIBUS);
		
		if(!selected.isEmpty())
		{
			PacketDistributor.sendToPlayer(player,
					new SyncSpecibusPacket(selected, MinestuckConfig.SERVER.maxSpecibusCount.get()));
		}
	}
	
	@SubscribeEvent
	public static void onEquipmentChange(LivingEquipmentChangeEvent event)
	{
		
		if(!(event.getEntity() instanceof ServerPlayer player)) return;
		if(event.getSlot() != EquipmentSlot.MAINHAND) return;
		
		ItemStack from = event.getFrom();
		ItemStack to = event.getTo();
		
		if(from.isEmpty() || !to.isEmpty()) return;
		if(from.getMaxDamage() <= 0) return;
		if(from.getDamageValue() < from.getMaxDamage() - 2) return;
		
		Item halfItem = getHalfBlade(from);
		if(halfItem == null) return;
		
		List<String> selected = new ArrayList<>(player.getData(MSAttachments.SELECTED_SPECIBUS));
		if(!selected.contains(KindAbstratusList.SWORD)) return;
		
		player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(halfItem));
		
		String halfSword = KindAbstratusList.HALF_SWORD;
		if(!selected.contains(halfSword))
		{
			selected.remove(KindAbstratusList.SWORD);
			selected.add(halfSword);
			player.setData(MSAttachments.SELECTED_SPECIBUS, selected);
			PacketDistributor.sendToPlayer(player, new SyncSpecibusPacket(selected, MinestuckConfig.SERVER.maxSpecibusCount.get()));
		}
		
		MSCriteriaTriggers.BLADEKIND_BREAK.get().trigger(player);
	}
	
	private static Item getHalfBlade(ItemStack stack)
	{
		if(!stack.is(MSTags.Items.KIND_SWORD)) return null;
		ResourceLocation id = BuiltInRegistries.ITEM.getKey(stack.getItem());
		ResourceLocation halfId = Minestuck.id("half_" + id.getPath());
		
		return BuiltInRegistries.ITEM.getOptional(halfId)
				.filter(item -> new ItemStack(item).is(MSTags.Items.KIND_HALF_SWORD))
				.orElse(null);
	}
	
	@SubscribeEvent(priority=EventPriority.LOW, receiveCanceled=false)
	public static void onServerChat(ServerChatEvent event)
	{
		Modus modus = CaptchaDeckHandler.getModus(event.getPlayer());
		if(modus instanceof HashMapModus hashMapModus)
			hashMapModus.onChatMessage(event.getPlayer(), event.getMessage().getString());
		else if(modus instanceof ArrayModus arrayModus)
			arrayModus.onChatMessage(event.getPlayer(), event.getMessage().getString());
	}
	
	@SubscribeEvent
	public static void onGetItemBurnTime(FurnaceFuelBurnTimeEvent event)
	{
		if(event.getItemStack().getItem() == MSBlocks.TREATED_PLANKS.get().asItem())
			event.setBurnTime(50);	//Do not set this number to 0.
	}
	
	@SubscribeEvent
	public static void onEffectRemove(MobEffectEvent.Remove event)
	{
		onEffectEnd(event.getEntity(), event.getEffect().value());
	}
	
	@SubscribeEvent
	public static void onEffectExpire(MobEffectEvent.Expired expiryEvent)
	{
		onEffectEnd(expiryEvent.getEntity(), expiryEvent.getEffectInstance().getEffect().value());
	}
	
	private static void onEffectEnd(LivingEntity entityLiving, MobEffect effect)
	{
		if(entityLiving instanceof ServerPlayer player)
		{
			if(effect == MSEffects.CREATIVE_SHOCK.get())
				CreativeShockEffect.onEffectEnd(player);
		}
	}
	
	@SubscribeEvent
	public static void breadStaling(ItemExpireEvent event)
	{
		ItemEntity e = event.getEntity();
		if(e.getItem().getCount() == 1 && (e.getItem().getItem() == Items.BREAD)) {
			ItemEntity stalebread = new ItemEntity(e.level(), e.getX(), e.getY(), e.getZ(), new ItemStack(MSItems.STALE_BAGUETTE.get()));
			e.level().addFreshEntity(stalebread);
		}
	}
}
