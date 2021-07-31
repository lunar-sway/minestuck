package com.mraof.minestuck.event;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.entity.consort.ConsortDialogue;
import com.mraof.minestuck.entity.underling.UnderlingEntity;
import com.mraof.minestuck.inventory.captchalogue.HashMapModus;
import com.mraof.minestuck.inventory.captchalogue.Modus;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.player.*;
import com.mraof.minestuck.skaianet.SburbHandler;
import com.mraof.minestuck.skaianet.SkaianetHandler;
import com.mraof.minestuck.skaianet.TitleSelectionHook;
import com.mraof.minestuck.util.RandomLocalTeleport;
import com.mraof.minestuck.world.MSDimensions;
import com.mraof.minestuck.world.gen.feature.MSFeatures;
import com.mraof.minestuck.world.storage.MSExtraData;
import com.mraof.minestuck.world.storage.PlayerData;
import com.mraof.minestuck.world.storage.PlayerSavedData;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.item.ItemExpireEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.UseHoeEvent;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppedEvent;
import org.apache.logging.log4j.LogManager;

import java.util.List;

import static com.mraof.minestuck.player.EnumAspect.*;

@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus=Mod.EventBusSubscriber.Bus.FORGE)
public class ServerEventHandler
{
	public static long lastDay;
	
	@SubscribeEvent
	public static void serverStarting(FMLServerStartingEvent event)
	{
		ConsortDialogue.serverStarting();
		//if(!event.getServer().isDedicatedServer() && Minestuck.class.getAnnotation(Mod.class).version().startsWith("@")) TODO Find an alternative to detect dev environment
		//event.getServer().setOnlineMode(false);	//Makes it possible to use LAN in a development environment
		
		lastDay = event.getServer().getWorld(DimensionType.OVERWORLD).getGameTime() / 24000L;
	}
	
	@SubscribeEvent
	public static void serverStopped(FMLServerStoppedEvent event)
	{
		IdentifierHandler.clear();
		SkaianetHandler.clear();
		MSDimensions.clear();
		MSFeatures.LAND_GATE.clearCache();
	}
	
	@SubscribeEvent
	public static void onWorldTick(TickEvent.WorldTickEvent event)
	{
		if(event.phase == TickEvent.Phase.END)
		{
			
			if(!MinestuckConfig.SERVER.hardMode && event.world.getDimension().getType() == DimensionType.OVERWORLD)
			{
				long time = event.world.getGameTime() / 24000L;
				if(time != lastDay)
				{
					lastDay = time;
					SburbHandler.resetGivenItems(event.world.getServer());
				}
			}
			
			MinecraftServer server = event.world.getServer();
			if(server != null)
				MSExtraData.get(server).executeEntryTasks(server);
		}
	}
	
	@SubscribeEvent(priority=EventPriority.LOWEST, receiveCanceled=false)
	public static void onEntityDeath(LivingDeathEvent event)
	{
		if(event.getEntity() instanceof IMob && event.getSource().getTrueSource() instanceof ServerPlayerEntity)
		{
			ServerPlayerEntity player = (ServerPlayerEntity) event.getSource().getTrueSource();
			int exp = 0;
			if(event.getEntity() instanceof ZombieEntity || event.getEntity() instanceof SkeletonEntity)
				exp = 6;
			else if(event.getEntity() instanceof CreeperEntity || event.getEntity() instanceof SpiderEntity || event.getEntity() instanceof SilverfishEntity)
				exp = 5;
			else if(event.getEntity() instanceof EndermanEntity || event.getEntity() instanceof BlazeEntity || event.getEntity() instanceof WitchEntity || event.getEntity() instanceof GuardianEntity)
				exp = 12;
			else if(event.getEntity() instanceof SlimeEntity)
				exp = ((SlimeEntity) event.getEntity()).getSlimeSize() - 1;
			
			if(exp > 0)
				Echeladder.increaseProgress(player, exp);
		}
		if(event.getEntity() instanceof ServerPlayerEntity)
		{
			TitleSelectionHook.cancelSelection((ServerPlayerEntity) event.getEntity());
		}
	}

	// Stores the crit result from the CriticalHitEvent, to be used during LivingHurtEvent to trigger special effects of any weapons.
	// This method is reliable only as long as LivingHurtEvent is posted only on the main thread and after a matching CriticalHitEvent
	private static boolean cachedCrit;

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onCrit(CriticalHitEvent event)
	{
		if(!event.getEntity().world.isRemote)
			cachedCrit = event.getResult() == Event.Result.ALLOW || event.getResult() == Event.Result.DEFAULT && event.isVanillaCritical();
	}
	
	public static boolean wasLastHitCrit(LivingEntity entity)
	{
		return entity instanceof ServerPlayerEntity && cachedCrit;
	}
	
	@SubscribeEvent(priority=EventPriority.NORMAL)
	public static void onEntityAttack(LivingHurtEvent event)
	{
		if(event.getSource().getTrueSource() != null)
		{
			if (event.getSource().getTrueSource() instanceof ServerPlayerEntity)
			{
				ServerPlayerEntity player = (ServerPlayerEntity) event.getSource().getTrueSource();
				if (event.getEntityLiving() instanceof UnderlingEntity)
				{    //Increase damage to underling
					double modifier = PlayerSavedData.getData(player).getEcheladder().getUnderlingDamageModifier();
					event.setAmount((float) (event.getAmount() * modifier));
				}
			}
			else if (event.getEntityLiving() instanceof ServerPlayerEntity && event.getSource().getTrueSource() instanceof UnderlingEntity)
			{    //Decrease damage to player
				ServerPlayerEntity player = (ServerPlayerEntity) event.getEntityLiving();
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
		if(event.getEntityLiving() instanceof PlayerEntity)
		{
			PlayerEntity injuredPlayer = ((PlayerEntity) event.getEntity());
			PlayerData data = PlayerSavedData.getData((ServerPlayerEntity) injuredPlayer);
			Title title = data.getTitle();
			int rung = data.getEcheladder().getRung();
			
			boolean isDoom = title != null && title.getHeroAspect() == EnumAspect.DOOM;
			ItemStack handItem = injuredPlayer.getHeldItemMainhand();
			float activateThreshold = ((injuredPlayer.getMaxHealth() / (injuredPlayer.getHealth() + 1)) / injuredPlayer.getMaxHealth()); //fraction of players health that rises dramatically the more injured they are
			
			if(handItem.getItem() == MSItems.LUCERNE_HAMMER_OF_UNDYING)
			{
				if(isDoom)
					activateThreshold = activateThreshold * 1.5F;
				
				activateThreshold = activateThreshold + injuredPlayer.getRNG().nextFloat() * .9F;
				
				if(activateThreshold >= 1.0F && injuredPlayer.getRNG().nextFloat() >= .75)
				{
					injuredPlayer.world.playSound(null, injuredPlayer.getPosX(), injuredPlayer.getPosY(), injuredPlayer.getPosZ(), SoundEvents.ITEM_TOTEM_USE, SoundCategory.PLAYERS, 1.0F, 1.4F);
					injuredPlayer.setHealth(injuredPlayer.getHealth() + 3);
					injuredPlayer.addPotionEffect(new EffectInstance(Effects.REGENERATION, 450, 0));
					if(isDoom)
					{
						injuredPlayer.addPotionEffect(new EffectInstance(Effects.ABSORPTION, 100, 0));
						handItem.damageItem(100, injuredPlayer, playerEntity -> playerEntity.sendBreakAnimation(Hand.MAIN_HAND));
					} else
					{
						handItem.damageItem(250, injuredPlayer, playerEntity -> playerEntity.sendBreakAnimation(Hand.MAIN_HAND));
					}
				}
			}
			
			if(handItem.getItem() == MSItems.CRUEL_FATE_CRUCIBLE)
			{
				activateThreshold = activateThreshold * 8 + injuredPlayer.getRNG().nextFloat() * .9F;
				
				if((isDoom && activateThreshold >= 1.0F && injuredPlayer.getRNG().nextFloat() <= .2) || (!isDoom && activateThreshold >= 1.0F && injuredPlayer.getRNG().nextFloat() <= .05))
				{
					AxisAlignedBB axisalignedbb = injuredPlayer.getBoundingBox().grow(4.0D, 2.0D, 4.0D);
					List<LivingEntity> list = injuredPlayer.world.getEntitiesWithinAABB(LivingEntity.class, axisalignedbb);
					list.remove(injuredPlayer);
					if(!list.isEmpty())
					{
						injuredPlayer.world.playSound(null, injuredPlayer.getPosX(), injuredPlayer.getPosY(), injuredPlayer.getPosZ(), SoundEvents.ENTITY_WITHER_HURT, SoundCategory.PLAYERS, 0.5F, 1.6F);
						if(isDoom)
							handItem.damageItem(2, injuredPlayer, playerEntity -> playerEntity.sendBreakAnimation(Hand.MAIN_HAND));
						else
							handItem.damageItem(10, injuredPlayer, playerEntity -> playerEntity.sendBreakAnimation(Hand.MAIN_HAND));
						for(LivingEntity livingentity : list)
						{
							livingentity.addPotionEffect(new EffectInstance(Effects.INSTANT_DAMAGE, 1, 1));
						}
					}
				}
			}
			if(title != null && title.getHeroClass() == EnumClass.BARD && injuredPlayer.getRNG().nextFloat() <= (.01 + rung/500) && data.passiveEffectToggle()){
				LivingEntity attackingEntity = (LivingEntity) event.getSource().getTrueSource();
				if(attackingEntity != null){
					//EntityPredicate visiblePredicate = (new EntityPredicate()).setLineOfSiteRequired();
					Effect[] negativeAspectEffects = {null, Effects.SLOWNESS, null, Effects.WITHER, Effects.WEAKNESS, Effects.WITHER, Effects.GLOWING, null, Effects.WEAKNESS, null, Effects.SLOWNESS, Effects.BLINDNESS}; //Blood, Breath, Doom, Heart, Hope, Life, Light, Mind, Rage, Space, Time, Void
					
					if(title.getHeroAspect() == SPACE){
						RandomLocalTeleport.teleportEntity((LivingEntity) attackingEntity, attackingEntity.world);
					}
					
					if(title.getHeroAspect() == MIND || title.getHeroAspect() == BLOOD){
						List<Entity> nearbyEntities = attackingEntity.world.getEntitiesWithinAABBExcludingEntity(attackingEntity, attackingEntity.getBoundingBox().grow(6.0D));
						
						if(!nearbyEntities.isEmpty())
						{
							for(Entity eventEntity : nearbyEntities)
							{
								if(eventEntity instanceof LivingEntity)
								{
									LivingEntity livingEventEntity = (LivingEntity) eventEntity;
									livingEventEntity.getCombatTracker().reset();
									livingEventEntity.setRevengeTarget(attackingEntity);
								}
							}
						}
					}
					
					if(title.getHeroAspect() == DOOM){
						Effect[] positivePotionEffects = {Effects.ABSORPTION, Effects.SLOW_FALLING, Effects.RESISTANCE, Effects.FIRE_RESISTANCE, Effects.REGENERATION, Effects.LUCK, Effects.NIGHT_VISION, Effects.STRENGTH, Effects.SPEED, Effects.HASTE, Effects.INVISIBILITY, Effects.WATER_BREATHING}; //TODO EffectType.BENEFICIAL.ordinal(); <- maybe make it a list and check for Beneficial as it iterates?
						for(int i = 0; i < positivePotionEffects.length; i++){
							attackingEntity.removePotionEffect(positivePotionEffects[i]);
						}
					}
					
					if(negativeAspectEffects[title.getHeroAspect().ordinal()] != null){
						attackingEntity.addPotionEffect(new EffectInstance(negativeAspectEffects[data.getTitle().getHeroAspect().ordinal()], 300, rung / 12));
					}
					LogManager.getLogger().debug("Applied passive class aspect effect to {}", attackingEntity.getName().getFormattedText());
				}
			}
		}
	}
	
	@SubscribeEvent
	public static void playerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event)
	{
		TitleSelectionHook.cancelSelection((ServerPlayerEntity) event.getPlayer());
		
		PlayerSavedData.getData((ServerPlayerEntity) event.getPlayer()).getEcheladder().resendAttributes(event.getPlayer());
	}
	
	@SubscribeEvent(priority=EventPriority.LOW, receiveCanceled=false)
	public static void onServerChat(ServerChatEvent event)
	{
		Modus modus = PlayerSavedData.getData(event.getPlayer()).getModus();
		if(modus instanceof HashMapModus)
			((HashMapModus) modus).onChatMessage(event.getPlayer(), event.getMessage());
	}
	
	//This functionality uses an event to maintain compatibility with mod items having hoe functionality but not extending ItemHoe, like TiCon mattocks.
	@SubscribeEvent
	public static void onPlayerUseHoe(UseHoeEvent event)
	{
		if(event.getContext().getWorld().getBlockState(event.getContext().getPos()).getBlock() == MSBlocks.COARSE_END_STONE)
		{
			event.getContext().getWorld().setBlockState(event.getContext().getPos(), Blocks.END_STONE.getDefaultState());
			event.getContext().getWorld().playSound(null, event.getContext().getPos(), SoundEvents.ITEM_HOE_TILL, SoundCategory.BLOCKS, 1.0F, 	1.0F);
			event.setResult(Event.Result.ALLOW);
		}
	}
	
	@SubscribeEvent
	public static void onGetItemBurnTime(FurnaceFuelBurnTimeEvent event)
	{
		if(event.getItemStack().getItem() == MSBlocks.TREATED_PLANKS.asItem())
			event.setBurnTime(50);	//Do not set this number to 0.
	}
	
	@SubscribeEvent
	public static void onPlayerTickEvent(TickEvent.PlayerTickEvent event)
	{
		if(!event.player.world.isRemote)
		{
			PlayerEntity player = event.player;
			PlayerData data = PlayerSavedData.getData((ServerPlayerEntity) player);
			Title title = data.getTitle();
			
			if(data.getTitle() != null){
				if(data.getAspectPowerCooldown() > 0){
					data.setAspectPowerCooldown(data.getAspectPowerCooldown()-(1 + data.getEcheladder().getRung()/10));
				}
				
				int rung = data.getEcheladder().getRung();
				
				if(player.getEntityWorld().getGameTime() % (380 * (1 + 50/(rung+5))) == 0 && title.getHeroClass() == EnumClass.HEIR && data.passiveEffectToggle() && !player.isSpectator()){
					
					Effect[] positiveAspectEffects = {Effects.ABSORPTION, Effects.SLOW_FALLING, Effects.RESISTANCE, Effects.ABSORPTION, Effects.FIRE_RESISTANCE, Effects.REGENERATION, Effects.LUCK, Effects.NIGHT_VISION, Effects.STRENGTH, Effects.SPEED, Effects.HASTE, Effects.INVISIBILITY}; //Blood, Breath, Doom, Heart, Hope, Life, Light, Mind, Rage, Space, Time, Void
					if(rung > 20 && title.getHeroAspect() == SPACE)
					{
						player.addPotionEffect(new EffectInstance(Effects.HASTE, 300, 0));
					}
					
					if(rung > 20 && title.getHeroAspect() == TIME)
					{
						player.addPotionEffect(new EffectInstance(Effects.SPEED, 300, 0));
					}
					
					if(rung > 20 && title.getHeroAspect() == HOPE)
					{
						player.addPotionEffect(new EffectInstance(Effects.WATER_BREATHING, 300, 0));
					}
					if(rung > 20 && title.getHeroAspect() == LIFE)
					{
						player.addPotionEffect(new EffectInstance(Effects.SATURATION, 1, 0));
					}
					if(positiveAspectEffects[title.getHeroAspect().ordinal()] != null){
						player.world.playSound(null, player.getPosition(), SoundEvents.ENTITY_ILLUSIONER_CAST_SPELL, SoundCategory.PLAYERS, 0.5F, 1.4F);
						player.addPotionEffect(new EffectInstance(positiveAspectEffects[title.getHeroAspect().ordinal()], 300, rung / 20));
						LogManager.getLogger().debug("Applied passive class aspect potion effect to {}", player.getName().getFormattedText());
					}
				}
			}
			if(player.isCreative() && data.getAspectPowerCooldown() > 20)
				data.setAspectPowerCooldown(20);
			
			if(data.getAspectPowerCooldown() < 0)
				data.setAspectPowerCooldown(0);
			
		}
	}
	
	@SubscribeEvent
	public static void breadStaling(ItemExpireEvent event)
	{
		ItemEntity e = event.getEntityItem();
		if(e.getItem().getCount() == 1 && (e.getItem().getItem() == Items.BREAD)) {
			ItemEntity stalebread = new ItemEntity(e.world, e.getPosX(), e.getPosY(), e.getPosZ(), new ItemStack(MSItems.STALE_BAGUETTE));
			e.world.addEntity(stalebread);
		}
	}
}