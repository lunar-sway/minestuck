package com.mraof.minestuck.event;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.effects.CreativeShockEffect;
import com.mraof.minestuck.effects.MSEffects;
import com.mraof.minestuck.entity.consort.ConsortDialogue;
import com.mraof.minestuck.entity.underling.UnderlingEntity;
import com.mraof.minestuck.entry.EntryEvent;
import com.mraof.minestuck.inventory.captchalogue.HashMapModus;
import com.mraof.minestuck.inventory.captchalogue.Modus;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.player.Echeladder;
import com.mraof.minestuck.player.EnumAspect;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.player.Title;
import com.mraof.minestuck.skaianet.SburbHandler;
import com.mraof.minestuck.skaianet.SkaianetHandler;
import com.mraof.minestuck.skaianet.TitleSelectionHook;
import com.mraof.minestuck.world.storage.MSExtraData;
import com.mraof.minestuck.player.PlayerData;
import com.mraof.minestuck.player.PlayerSavedData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.item.ItemExpireEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.PotionEvent;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.event.server.ServerStoppedEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.List;

@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus=Mod.EventBusSubscriber.Bus.FORGE)
public class ServerEventHandler
{
	public static long lastDay;
	
	@SubscribeEvent
	public static void serverStarting(ServerStartingEvent event)
	{
		ConsortDialogue.serverStarting();
		lastDay = event.getServer().overworld().getGameTime() / 24000L;
	}
	
	@SubscribeEvent
	public static void serverStopped(ServerStoppedEvent event)
	{
		IdentifierHandler.clear();
		SkaianetHandler.clear();
	}
	
	@SubscribeEvent
	public static void onServerTick(TickEvent.ServerTickEvent event)
	{
		if(event.phase == TickEvent.Phase.END)
		{
			MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
			if(!MinestuckConfig.SERVER.hardMode.get())
			{
				long time = server.overworld().getGameTime() / 24000L;
				if(time != lastDay)
				{
					lastDay = time;
					SburbHandler.resetGivenItems(server);
				}
			}
			
			MSExtraData.get(server).executeEntryTasks(server);
			
			if(MinestuckConfig.SERVER.hardMode.get())
				EntryEvent.tick(server);
		}
	}
	
	@SubscribeEvent(priority=EventPriority.LOWEST, receiveCanceled=false)
	public static void onEntityDeath(LivingDeathEvent event)
	{
		if(event.getEntity() instanceof Enemy && event.getSource().getEntity() instanceof ServerPlayer player)
		{
			if(!(player instanceof FakePlayer))
			{
				int exp = 0;
				if(event.getEntity() instanceof Zombie || event.getEntity() instanceof Skeleton)
					exp = 1;
				else if(event.getEntity() instanceof Creeper || event.getEntity() instanceof Spider || event.getEntity() instanceof Silverfish)
					exp = 2;
				else if(event.getEntity() instanceof EnderMan || event.getEntity() instanceof Blaze || event.getEntity() instanceof Witch || event.getEntity() instanceof Guardian)
					exp = 3;
				else if(event.getEntity() instanceof Slime)
					exp = Math.min(((Slime) event.getEntity()).getSize() - 1, 9);
				
				if(exp > 0)
					Echeladder.increaseProgress(player, exp);
			}
		}
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
		if(!event.getEntity().level.isClientSide)
			cachedCrit = event.getResult() == Event.Result.ALLOW || event.getResult() == Event.Result.DEFAULT && event.isVanillaCritical();
	}
	
	public static boolean wasLastHitCrit(LivingEntity entity)
	{
		return entity instanceof ServerPlayer && cachedCrit;
	}
	
	@SubscribeEvent(priority=EventPriority.NORMAL)
	public static void onEntityAttack(LivingHurtEvent event)
	{
		if(event.getSource().getEntity() != null)
		{
			if (event.getSource().getEntity() instanceof ServerPlayer)
			{
				ServerPlayer player = (ServerPlayer) event.getSource().getEntity();
				if (event.getEntityLiving() instanceof UnderlingEntity)
				{    //Increase damage to underling
					double modifier = PlayerSavedData.getData(player).getEcheladder().getUnderlingDamageModifier();
					event.setAmount((float) (event.getAmount() * modifier));
				}
			}
			else if (event.getEntityLiving() instanceof ServerPlayer && event.getSource().getEntity() instanceof UnderlingEntity)
			{    //Decrease damage to player
				ServerPlayer player = (ServerPlayer) event.getEntityLiving();
					double modifier = PlayerSavedData.getData(player).getEcheladder().getUnderlingProtectionModifier();
					event.setAmount((float) (event.getAmount() * modifier));
			}
		}
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = false)
	public static void onEntityDamage(LivingHurtEvent event)
	{
		if(event.getEntityLiving() instanceof UnderlingEntity)
		{
			((UnderlingEntity) event.getEntityLiving()).onEntityDamaged(event.getSource(), event.getAmount());
		}
	}
	
	@SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = false)
	public static void onPlayerInjured(LivingHurtEvent event)
	{
		if(event.getEntityLiving() instanceof Player)
		{
			Player injuredPlayer = ((Player) event.getEntity());
			Title title = PlayerSavedData.getData((ServerPlayer) injuredPlayer).getTitle();
			boolean isDoom = title != null && title.getHeroAspect() == EnumAspect.DOOM;
			ItemStack handItem = injuredPlayer.getMainHandItem();
			float activateThreshold = ((injuredPlayer.getMaxHealth() / (injuredPlayer.getHealth() + 1)) / injuredPlayer.getMaxHealth()); //fraction of players health that rises dramatically the more injured they are
			
			if(handItem.getItem() == MSItems.LUCERNE_HAMMER_OF_UNDYING.get())
			{
				if(isDoom)
					activateThreshold = activateThreshold * 1.5F;
				
				activateThreshold = activateThreshold + injuredPlayer.getRandom().nextFloat() * .9F;
				
				if(activateThreshold >= 1.0F && injuredPlayer.getRandom().nextFloat() >= .75)
				{
					injuredPlayer.level.playSound(null, injuredPlayer.getX(), injuredPlayer.getY(), injuredPlayer.getZ(), SoundEvents.TOTEM_USE, SoundSource.PLAYERS, 1.0F, 1.4F);
					injuredPlayer.setHealth(injuredPlayer.getHealth() + 3);
					injuredPlayer.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 450, 0));
					if(isDoom)
					{
						injuredPlayer.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 100, 0));
						handItem.hurtAndBreak(100, injuredPlayer, playerEntity -> playerEntity.broadcastBreakEvent(InteractionHand.MAIN_HAND));
					} else
					{
						handItem.hurtAndBreak(250, injuredPlayer, playerEntity -> playerEntity.broadcastBreakEvent(InteractionHand.MAIN_HAND));
					}
				}
			}
			
			if(handItem.getItem() == MSItems.CRUEL_FATE_CRUCIBLE.get())
			{
				activateThreshold = activateThreshold * 8 + injuredPlayer.getRandom().nextFloat() * .9F;
				
				if((isDoom && activateThreshold >= 1.0F && injuredPlayer.getRandom().nextFloat() <= .2) || (!isDoom && activateThreshold >= 1.0F && injuredPlayer.getRandom().nextFloat() <= .05))
				{
					AABB axisalignedbb = injuredPlayer.getBoundingBox().inflate(4.0D, 2.0D, 4.0D);
					List<LivingEntity> list = injuredPlayer.level.getEntitiesOfClass(LivingEntity.class, axisalignedbb);
					list.remove(injuredPlayer);
					if(!list.isEmpty())
					{
						injuredPlayer.level.playSound(null, injuredPlayer.getX(), injuredPlayer.getY(), injuredPlayer.getZ(), SoundEvents.WITHER_HURT, SoundSource.PLAYERS, 0.5F, 1.6F);
						if(isDoom)
							handItem.hurtAndBreak(2, injuredPlayer, playerEntity -> playerEntity.broadcastBreakEvent(InteractionHand.MAIN_HAND));
						else
							handItem.hurtAndBreak(10, injuredPlayer, playerEntity -> playerEntity.broadcastBreakEvent(InteractionHand.MAIN_HAND));
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
		TitleSelectionHook.cancelSelection((ServerPlayer) event.getPlayer());
	}
	
	@SubscribeEvent(priority=EventPriority.LOW, receiveCanceled=false)
	public static void onServerChat(ServerChatEvent event)
	{
		Modus modus = PlayerSavedData.getData(event.getPlayer()).getModus();
		if(modus instanceof HashMapModus)
			((HashMapModus) modus).onChatMessage(event.getPlayer(), event.getMessage());
	}
	
	@SubscribeEvent
	public static void onGetItemBurnTime(FurnaceFuelBurnTimeEvent event)
	{
		if(event.getItemStack().getItem() == MSBlocks.TREATED_PLANKS.get().asItem())
			event.setBurnTime(50);	//Do not set this number to 0.
	}
	
	@SubscribeEvent
	public static void onPlayerTickEvent(TickEvent.PlayerTickEvent event)
	{
		if(!event.player.level.isClientSide)
		{
			PlayerData data = PlayerSavedData.getData((ServerPlayer) event.player);
			if(data.getTitle() != null)
				data.getTitle().handleAspectEffects((ServerPlayer) event.player);
		}
	}
	
	@SubscribeEvent
	public static void onEffectRemove(PotionEvent.PotionRemoveEvent event)
	{
		onEffectEnd(event.getEntityLiving(), event.getPotion());
	}
	
	@SubscribeEvent
	public static void onEffectExpire(PotionEvent.PotionExpiryEvent expiryEvent)
	{
		onEffectEnd(expiryEvent.getEntityLiving(), expiryEvent.getPotionEffect().getEffect());
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
		ItemEntity e = event.getEntityItem();
		if(e.getItem().getCount() == 1 && (e.getItem().getItem() == Items.BREAD)) {
			ItemEntity stalebread = new ItemEntity(e.level, e.getX(), e.getY(), e.getZ(), new ItemStack(MSItems.STALE_BAGUETTE.get()));
			e.level.addFreshEntity(stalebread);
		}
	}
}