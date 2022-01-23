package com.mraof.minestuck.event;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.effects.CreativeShockEffect;
import com.mraof.minestuck.effects.MSEffects;
import com.mraof.minestuck.entity.consort.ConsortDialogue;
import com.mraof.minestuck.entity.underling.UnderlingEntity;
import com.mraof.minestuck.inventory.captchalogue.HashMapModus;
import com.mraof.minestuck.inventory.captchalogue.Modus;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.item.artifact.CruxiteArtifactItem;
import com.mraof.minestuck.network.MSPacketHandler;
import com.mraof.minestuck.network.StopCreativeShockEffectPacket;
import com.mraof.minestuck.player.Echeladder;
import com.mraof.minestuck.player.EnumAspect;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.player.Title;
import com.mraof.minestuck.skaianet.SburbHandler;
import com.mraof.minestuck.skaianet.SkaianetHandler;
import com.mraof.minestuck.skaianet.TitleSelectionHook;
import com.mraof.minestuck.tileentity.redstone.StatStorerTileEntity;
import com.mraof.minestuck.world.MSDimensions;
import com.mraof.minestuck.world.storage.MSExtraData;
import com.mraof.minestuck.world.storage.PlayerData;
import com.mraof.minestuck.world.storage.PlayerSavedData;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ChorusFruitItem;
import net.minecraft.item.EnderPearlItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityStruckByLightningEvent;
import net.minecraftforge.event.entity.item.ItemExpireEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.UseHoeEvent;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.event.world.SaplingGrowTreeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppedEvent;

import java.util.List;

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
		
		lastDay = event.getServer().overworld().getGameTime() / 24000L;
	}
	
	@SubscribeEvent
	public static void serverStopped(FMLServerStoppedEvent event)
	{
		IdentifierHandler.clear();
		SkaianetHandler.clear();
		MSDimensions.clear();
	}
	
	@SubscribeEvent
	public static void onWorldTick(TickEvent.WorldTickEvent event)
	{
		if(event.phase == TickEvent.Phase.END)
		{
			
			if(!MinestuckConfig.SERVER.hardMode && event.world.dimension() == World.OVERWORLD)
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
		if(event.getEntity() instanceof IMob && event.getSource().getEntity() instanceof ServerPlayerEntity)
		{
			ServerPlayerEntity player = (ServerPlayerEntity) event.getSource().getEntity();
			int exp = 0;
			if(event.getEntity() instanceof ZombieEntity || event.getEntity() instanceof SkeletonEntity)
				exp = 1;
			else if(event.getEntity() instanceof CreeperEntity || event.getEntity() instanceof SpiderEntity || event.getEntity() instanceof SilverfishEntity)
				exp = 2;
			else if(event.getEntity() instanceof EndermanEntity || event.getEntity() instanceof BlazeEntity || event.getEntity() instanceof WitchEntity || event.getEntity() instanceof GuardianEntity)
				exp = 3;
			else if(event.getEntity() instanceof SlimeEntity)
				exp = Math.min(((SlimeEntity) event.getEntity()).getSize() - 1, 9);
			
			if(exp > 0)
				Echeladder.increaseProgress(player, exp);
		}
		if(event.getEntity() instanceof ServerPlayerEntity)
		{
			TitleSelectionHook.cancelSelection((ServerPlayerEntity) event.getEntity());
		}
		
		statStorer(1, StatStorerTileEntity.ActiveType.DEATHS, event.getEntity().blockPosition(), event.getEntity().level);
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
		return entity instanceof ServerPlayerEntity && cachedCrit;
	}
	
	public static void statStorer(float eventAmount, StatStorerTileEntity.ActiveType activeType, BlockPos eventPos, World world)
	{
		for(BlockPos blockPos : BlockPos.betweenClosed(eventPos.offset(16, 16, 16), eventPos.offset(-16, -16, -16)))
		{
			if(world == null || !world.isAreaLoaded(blockPos, 0))
				return;
			
			TileEntity tileEntity = world.getBlockEntity(blockPos);
			if(tileEntity instanceof StatStorerTileEntity)
			{
				StatStorerTileEntity storerTileEntity = (StatStorerTileEntity) tileEntity;
				
				if(activeType == storerTileEntity.getActiveType())
					storerTileEntity.setActiveStoredStatValue(storerTileEntity.getActiveStoredStatValue() + eventAmount, blockPos.above(), true);
			}
		}
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = false)
	public static void onEntityHeal(LivingHealEvent event)
	{
		statStorer(event.getAmount(), StatStorerTileEntity.ActiveType.HEALTH_RECOVERED, event.getEntity().blockPosition(), event.getEntity().level);
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = false)
	public static void onSaplingGrow(SaplingGrowTreeEvent event)
	{
		statStorer(1, StatStorerTileEntity.ActiveType.SAPLING_GROWN, event.getPos(), (World) event.getWorld());
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = false)
	public static void onEntityStruck(EntityStruckByLightningEvent event)
	{
		if(event.getLightning().tickCount == 1)
			statStorer(1, StatStorerTileEntity.ActiveType.LIGHTNING_STRUCK_ENTITY, event.getEntity().blockPosition(), event.getEntity().level);
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = false)
	public static void onEntityBred(BabyEntitySpawnEvent event)
	{
		if(!event.isCanceled())
			statStorer(1, StatStorerTileEntity.ActiveType.ENTITIES_BRED, event.getParentA().blockPosition(), event.getParentA().level);
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = false)
	public static void onExplosion(ExplosionEvent event)
	{
		PlayerEntity playerEntity = (PlayerEntity) event.getExplosion().getSourceMob();
		if(playerEntity != null && CreativeShockEffect.doesCreativeShockLimit(playerEntity, 0, 3))
			event.setCanceled(true); //intended to prevent blocks from being destroyed by a player attempting to circumvent creative shock
		else
		{
			statStorer(1, StatStorerTileEntity.ActiveType.EXPLOSIONS, new BlockPos(event.getExplosion().getPosition()), event.getWorld()); //TODO seems to be doubled
		}
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = false)
	public static void onAlchemy(AlchemyEvent event)
	{
		statStorer(1, StatStorerTileEntity.ActiveType.ALCHEMY_ACTIVATED, event.getAlchemiter().getBlockPos(), event.getWorld());
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = false)
	public static void onGristDrop(GristDropsEvent event)
	{
		statStorer(1, StatStorerTileEntity.ActiveType.GRIST_DROPS, event.getUnderling().getEntity().blockPosition(), event.getUnderling().level);
	}
	
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public static void onEntityAttack(LivingHurtEvent event)
	{
		if(event.getSource().getEntity() != null)
		{
			if (event.getSource().getEntity() instanceof ServerPlayerEntity)
			{
				ServerPlayerEntity player = (ServerPlayerEntity) event.getSource().getEntity();
				if (event.getEntityLiving() instanceof UnderlingEntity)
				{    //Increase damage to underling
					double modifier = PlayerSavedData.getData(player).getEcheladder().getUnderlingDamageModifier();
					event.setAmount((float) (event.getAmount() * modifier));
				}
			}
			else if (event.getEntityLiving() instanceof ServerPlayerEntity && event.getSource().getEntity() instanceof UnderlingEntity)
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
		
		statStorer(event.getAmount(), StatStorerTileEntity.ActiveType.DAMAGE, event.getEntity().blockPosition(), event.getEntity().level);
	}
	
	@SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = false)
	public static void onPlayerInjured(LivingHurtEvent event)
	{
		if(event.getEntityLiving() instanceof PlayerEntity)
		{
			PlayerEntity injuredPlayer = ((PlayerEntity) event.getEntity());
			Title title = PlayerSavedData.getData((ServerPlayerEntity) injuredPlayer).getTitle();
			boolean isDoom = title != null && title.getHeroAspect() == EnumAspect.DOOM;
			ItemStack handItem = injuredPlayer.getMainHandItem();
			float activateThreshold = ((injuredPlayer.getMaxHealth() / (injuredPlayer.getHealth() + 1)) / injuredPlayer.getMaxHealth()); //fraction of players health that rises dramatically the more injured they are
			
			if(handItem.getItem() == MSItems.LUCERNE_HAMMER_OF_UNDYING)
			{
				if(isDoom)
					activateThreshold = activateThreshold * 1.5F;
				
				activateThreshold = activateThreshold + injuredPlayer.getRandom().nextFloat() * .9F;
				
				if(activateThreshold >= 1.0F && injuredPlayer.getRandom().nextFloat() >= .75)
				{
					injuredPlayer.level.playSound(null, injuredPlayer.getX(), injuredPlayer.getY(), injuredPlayer.getZ(), SoundEvents.TOTEM_USE, SoundCategory.PLAYERS, 1.0F, 1.4F);
					injuredPlayer.setHealth(injuredPlayer.getHealth() + 3);
					injuredPlayer.addEffect(new EffectInstance(Effects.REGENERATION, 450, 0));
					if(isDoom)
					{
						injuredPlayer.addEffect(new EffectInstance(Effects.ABSORPTION, 100, 0));
						handItem.hurtAndBreak(100, injuredPlayer, playerEntity -> playerEntity.broadcastBreakEvent(Hand.MAIN_HAND));
					} else
					{
						handItem.hurtAndBreak(250, injuredPlayer, playerEntity -> playerEntity.broadcastBreakEvent(Hand.MAIN_HAND));
					}
				}
			}
			
			if(handItem.getItem() == MSItems.CRUEL_FATE_CRUCIBLE)
			{
				activateThreshold = activateThreshold * 8 + injuredPlayer.getRandom().nextFloat() * .9F;
				
				if((isDoom && activateThreshold >= 1.0F && injuredPlayer.getRandom().nextFloat() <= .2) || (!isDoom && activateThreshold >= 1.0F && injuredPlayer.getRandom().nextFloat() <= .05))
				{
					AxisAlignedBB axisalignedbb = injuredPlayer.getBoundingBox().inflate(4.0D, 2.0D, 4.0D);
					List<LivingEntity> list = injuredPlayer.level.getEntitiesOfClass(LivingEntity.class, axisalignedbb);
					list.remove(injuredPlayer);
					if(!list.isEmpty())
					{
						injuredPlayer.level.playSound(null, injuredPlayer.getX(), injuredPlayer.getY(), injuredPlayer.getZ(), SoundEvents.WITHER_HURT, SoundCategory.PLAYERS, 0.5F, 1.6F);
						if(isDoom)
							handItem.hurtAndBreak(2, injuredPlayer, playerEntity -> playerEntity.broadcastBreakEvent(Hand.MAIN_HAND));
						else
							handItem.hurtAndBreak(10, injuredPlayer, playerEntity -> playerEntity.broadcastBreakEvent(Hand.MAIN_HAND));
						for(LivingEntity livingentity : list)
						{
							livingentity.addEffect(new EffectInstance(Effects.HARM, 1, 1));
						}
					}
				}
			}
		}
	}
	
	@SubscribeEvent(priority=EventPriority.NORMAL)
	public static void onLeftClickBlockEvent(PlayerInteractEvent.LeftClickBlock event)
	{
		if(event.getEntity() instanceof PlayerEntity)
		{
			PlayerEntity playerEntity = (PlayerEntity) event.getEntity();
			if(CreativeShockEffect.doesCreativeShockLimit(playerEntity, 0, 3))
				event.setCanceled(true);
		}
	}
	
	@SubscribeEvent
	public static void playerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event)
	{
		TitleSelectionHook.cancelSelection((ServerPlayerEntity) event.getPlayer());
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
	public static void onPlayerUseHoe(UseHoeEvent event)	//TODO replace by an extension to block.getToolModifiedState()
	{
		if(event.getContext().getLevel().getBlockState(event.getContext().getClickedPos()).getBlock() == MSBlocks.COARSE_END_STONE)
		{
			event.getContext().getLevel().setBlockAndUpdate(event.getContext().getClickedPos(), Blocks.END_STONE.defaultBlockState());
			event.getContext().getLevel().playSound(null, event.getContext().getClickedPos(), SoundEvents.HOE_TILL, SoundCategory.BLOCKS, 1.0F, 	1.0F);
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
		if(!event.player.level.isClientSide)
		{
			PlayerData data = PlayerSavedData.getData((ServerPlayerEntity) event.player);
			if(data.getTitle() != null)
				data.getTitle().handleAspectEffects((ServerPlayerEntity) event.player);
		}
		
		if(event.player.hasEffect(MSEffects.CREATIVE_SHOCK.get()))
		{
			int duration = event.player.getEffect(MSEffects.CREATIVE_SHOCK.get()).getDuration();
			if(duration >= 5)
			{
				if(CreativeShockEffect.doesCreativeShockLimit(event.player, 0, 3))
					event.player.abilities.mayBuild = false;
				CreativeShockEffect.stopElytraFlying(event.player, 2, 5);
			}
			else
			{
				if(!event.player.level.isClientSide)
				{
					event.player.abilities.mayBuild = ((ServerPlayerEntity) event.player).gameMode.getGameModeForPlayer().isBlockPlacingRestricted();
				}
			}
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
	
	private static void onEffectEnd(LivingEntity entityLiving, Effect effect)
	{
		if(entityLiving instanceof PlayerEntity)
		{
			PlayerEntity player = (PlayerEntity) entityLiving;
			
			if(player instanceof ServerPlayerEntity && effect == MSEffects.CREATIVE_SHOCK.get())
			{
				ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) player;
				player.abilities.mayBuild = !serverPlayerEntity.gameMode.getGameModeForPlayer().isBlockPlacingRestricted(); //block placing restricted was hasLimitedInteractions(), mayBuild was allowEdit
				
				StopCreativeShockEffectPacket packet = new StopCreativeShockEffectPacket(serverPlayerEntity.gameMode.getGameModeForPlayer().isBlockPlacingRestricted());
				MSPacketHandler.sendToPlayer(packet, serverPlayerEntity);
			}
		}
	}
	
	@SubscribeEvent
	public static void onBreakSpeed(PlayerEvent.BreakSpeed event)
	{
		if(CreativeShockEffect.doesCreativeShockLimit(event.getPlayer(), 0, 3))
			event.setNewSpeed(0);
	}
	
	@SubscribeEvent
	public static void onHarvestCheck(PlayerEvent.HarvestCheck event)
	{
		if(CreativeShockEffect.doesCreativeShockLimit(event.getPlayer(), 0, 3))
			event.setCanHarvest(false);
	}
	
	@SubscribeEvent
	public static void onRightClickItem(PlayerInteractEvent.RightClickItem event)
	{
		if(CreativeShockEffect.doesCreativeShockLimit(event.getPlayer(), 0, 3))
		{
			if(event.getItemStack().getItem() instanceof CruxiteArtifactItem //Cruxite check prevents players from using an artifact to enter while under effects of Creative Shock
					|| event.getItemStack().getItem() instanceof EnderPearlItem
					|| event.getItemStack().getItem() instanceof ChorusFruitItem)
				event.setCanceled(true);
		}
	}
	
	@SubscribeEvent
	public static void breadStaling(ItemExpireEvent event)
	{
		ItemEntity e = event.getEntityItem();
		if(e.getItem().getCount() == 1 && (e.getItem().getItem() == Items.BREAD)) {
			ItemEntity stalebread = new ItemEntity(e.level, e.getX(), e.getY(), e.getZ(), new ItemStack(MSItems.STALE_BAGUETTE));
			e.level.addFreshEntity(stalebread);
		}
	}
}