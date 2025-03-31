package com.mraof.minestuck.event;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.effects.CreativeShockEffect;
import com.mraof.minestuck.effects.MSEffects;
import com.mraof.minestuck.entity.MSAttributes;
import com.mraof.minestuck.entity.underling.UnderlingEntity;
import com.mraof.minestuck.entry.EntryEvent;
import com.mraof.minestuck.inventory.captchalogue.CaptchaDeckHandler;
import com.mraof.minestuck.inventory.captchalogue.HashMapModus;
import com.mraof.minestuck.inventory.captchalogue.Modus;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.player.EnumAspect;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.player.Title;
import com.mraof.minestuck.skaianet.TitleSelectionHook;
import com.mraof.minestuck.world.storage.MSExtraData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.AABB;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.event.ServerChatEvent;
import net.neoforged.neoforge.event.entity.item.ItemExpireEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import net.neoforged.neoforge.event.entity.player.CriticalHitEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

import java.util.List;

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
	}
	
	@SubscribeEvent(priority=EventPriority.LOW, receiveCanceled=false)
	public static void onServerChat(ServerChatEvent event)
	{
		Modus modus = CaptchaDeckHandler.getModus(event.getPlayer());
		if(modus instanceof HashMapModus hashMapModus)
			hashMapModus.onChatMessage(event.getPlayer(), event.getMessage().getString());
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
