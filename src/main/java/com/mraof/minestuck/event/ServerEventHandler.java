package com.mraof.minestuck.event;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.entity.underling.UnderlingEntity;
import com.mraof.minestuck.inventory.captchalogue.HashMapModus;
import com.mraof.minestuck.inventory.captchalogue.Modus;
import com.mraof.minestuck.item.weapon.PotionWeaponItem;
import com.mraof.minestuck.skaianet.SburbHandler;
import com.mraof.minestuck.skaianet.SkaianetHandler;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.player.Echeladder;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.world.gen.feature.MSFeatures;
import com.mraof.minestuck.world.storage.MSExtraData;
import com.mraof.minestuck.world.storage.PlayerData;
import com.mraof.minestuck.world.storage.PlayerSavedData;
import net.minecraft.block.Blocks;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.potion.Effects;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.SharedConstants;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.UseHoeEvent;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppedEvent;

@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus=Mod.EventBusSubscriber.Bus.FORGE)
public class ServerEventHandler
{
	public static long lastDay;
	
	@SubscribeEvent
	public void serverStarting(FMLServerStartingEvent event)
	{
		Debug.info(SharedConstants.developmentMode);
		//if(!event.getServer().isDedicatedServer() && Minestuck.class.getAnnotation(Mod.class).version().startsWith("@")) TODO Find an alternative to detect dev environment
		//event.getServer().setOnlineMode(false);	//Makes it possible to use LAN in a development environment
		
		lastDay = event.getServer().getWorld(DimensionType.OVERWORLD).getGameTime() / 24000L;
	}
	
	@SubscribeEvent
	public void serverStopped(FMLServerStoppedEvent event)
	{
		IdentifierHandler.clear();
		SkaianetHandler.clear();
		MSFeatures.LAND_GATE.clearCache();
	}
	
	@SubscribeEvent
	public static void onWorldTick(TickEvent.WorldTickEvent event)
	{
		if(event.phase == TickEvent.Phase.END)
		{
			
			if(!MinestuckConfig.hardMode && event.world.getDimension().getType() == DimensionType.OVERWORLD)
			{
				long time = event.world.getGameTime() / 24000L;
				if(time != lastDay)
				{
					lastDay = time;
					SkaianetHandler.get(event.world.getServer()).resetGivenItems();
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
			SburbHandler.stopEntry((ServerPlayerEntity) event.getEntity());
	}

	//Gets reset after AttackEntityEvent but before LivingHurtEvent, but is used in determining if it's a critical hit
	private static float cachedCooledAttackStrength = 0;

	@SubscribeEvent
	public static void onPlayerAttack(AttackEntityEvent event)
	{
		cachedCooledAttackStrength = event.getPlayer().getCooledAttackStrength(0.5F);
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
				boolean critical = cachedCooledAttackStrength > 0.9 && player.fallDistance > 0.0F && !player.onGround && !player.isOnLadder() && !player.isInWater() && !player.isPotionActive(Effects.BLINDNESS) && !player.isPassenger() && !player.isBeingRidden();
				if(!player.getHeldItemMainhand().isEmpty() && player.getHeldItemMainhand().getItem() instanceof PotionWeaponItem)
				{
					if(((PotionWeaponItem) player.getHeldItemMainhand().getItem()).potionOnCrit())
					{
						if(critical)
						event.getEntityLiving().addPotionEffect(((PotionWeaponItem) player.getHeldItemMainhand().getItem()).getEffect(player));
					}
					else event.getEntityLiving().addPotionEffect(((PotionWeaponItem) player.getHeldItemMainhand().getItem()).getEffect(player));
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
	
	@SubscribeEvent
	public static void playerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event)
	{
		SburbHandler.stopEntry((ServerPlayerEntity) event.getPlayer());
		
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
			PlayerData data = PlayerSavedData.getData((ServerPlayerEntity) event.player);
			if(data.getTitle() != null)
				data.getTitle().handleAspectEffects((ServerPlayerEntity) event.player);
		}
	}
}