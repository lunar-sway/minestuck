package com.mraof.minestuck.event;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.entity.consort.ConsortDialogue;
import com.mraof.minestuck.entity.underling.UnderlingEntity;
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
import com.mraof.minestuck.world.MSDimensions;
import com.mraof.minestuck.world.gen.feature.MSFeatures;
import com.mraof.minestuck.world.storage.MSExtraData;
import com.mraof.minestuck.world.storage.PlayerData;
import com.mraof.minestuck.world.storage.PlayerSavedData;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
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
		MSFeatures.LAND_GATE.clearCache();
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
				exp = 6;
			else if(event.getEntity() instanceof CreeperEntity || event.getEntity() instanceof SpiderEntity || event.getEntity() instanceof SilverfishEntity)
				exp = 5;
			else if(event.getEntity() instanceof EndermanEntity || event.getEntity() instanceof BlazeEntity || event.getEntity() instanceof WitchEntity || event.getEntity() instanceof GuardianEntity)
				exp = 12;
			else if(event.getEntity() instanceof SlimeEntity)
				exp = ((SlimeEntity) event.getEntity()).getSize() - 1;
			
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
		if(!event.getEntity().level.isClientSide)
			cachedCrit = event.getResult() == Event.Result.ALLOW || event.getResult() == Event.Result.DEFAULT && event.isVanillaCritical();
	}
	
	public static boolean wasLastHitCrit(LivingEntity entity)
	{
		return entity instanceof ServerPlayerEntity && cachedCrit;
	}
	
	@SubscribeEvent(priority=EventPriority.NORMAL)
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
			if(handItem.getItem() == MSItems.LUCERNE_HAMMER_OF_UNDYING){
				if((isDoom && injuredPlayer.getHealth() <= 3.0F && injuredPlayer.getRandom().nextFloat() <= .08) || (!isDoom && injuredPlayer.getHealth() <= 2.0F && injuredPlayer.getRandom().nextFloat() <= .02))
				{
					injuredPlayer.level.playSound(null, injuredPlayer.getX(), injuredPlayer.getY(), injuredPlayer.getZ(), SoundEvents.TOTEM_USE, SoundCategory.PLAYERS, 1.0F, 1.4F);
					injuredPlayer.setHealth(injuredPlayer.getHealth() + 3);
					injuredPlayer.addEffect(new EffectInstance(Effects.REGENERATION, 450, 0));
					if(isDoom){
						injuredPlayer.addEffect(new EffectInstance(Effects.ABSORPTION, 100, 0));
						handItem.hurtAndBreak(400, injuredPlayer, playerEntity -> playerEntity.broadcastBreakEvent(Hand.MAIN_HAND));
					} else {
						handItem.hurtAndBreak(1000, injuredPlayer, playerEntity -> playerEntity.broadcastBreakEvent(Hand.MAIN_HAND));
					}
				}
			}
			if(handItem.getItem() == MSItems.CRUEL_FATE_CRUCIBLE){
				if((isDoom && injuredPlayer.getHealth() <= 12.0F && injuredPlayer.getRandom().nextFloat() <= .25) || (!isDoom && injuredPlayer.getHealth() <= 8.0F && injuredPlayer.getRandom().nextFloat() <= .10))
				{
					AxisAlignedBB axisalignedbb = injuredPlayer.getBoundingBox().inflate(4.0D, 2.0D, 4.0D);
					List<LivingEntity> list = injuredPlayer.level.getEntitiesOfClass(LivingEntity.class, axisalignedbb);
					list.remove(injuredPlayer);
					if (!list.isEmpty()) {
						injuredPlayer.level.playSound(null, injuredPlayer.getX(), injuredPlayer.getY(), injuredPlayer.getZ(), SoundEvents.WITHER_HURT, SoundCategory.PLAYERS, 0.5F, 1.6F);
						if(isDoom)
							handItem.hurtAndBreak(1, injuredPlayer, playerEntity -> playerEntity.broadcastBreakEvent(Hand.MAIN_HAND));
						else
							handItem.hurtAndBreak(4, injuredPlayer, playerEntity -> playerEntity.broadcastBreakEvent(Hand.MAIN_HAND));
						for(LivingEntity livingentity : list) {
							livingentity.addEffect(new EffectInstance(Effects.HARM, 1, 1));
						}
					}
				}
			}
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