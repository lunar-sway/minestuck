package com.mraof.minestuck.event;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.entity.consort.ConsortDialogue;
import com.mraof.minestuck.entity.underling.UnderlingEntity;
import com.mraof.minestuck.inventory.captchalogue.HashMapModus;
import com.mraof.minestuck.inventory.captchalogue.Modus;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.network.MSPacketHandler;
import com.mraof.minestuck.network.StopCreativeShockEffectPacket;
import com.mraof.minestuck.player.Echeladder;
import com.mraof.minestuck.player.EnumAspect;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.player.Title;
import com.mraof.minestuck.effects.MSEffects;
import com.mraof.minestuck.skaianet.SburbHandler;
import com.mraof.minestuck.skaianet.SkaianetHandler;
import com.mraof.minestuck.skaianet.TitleSelectionHook;
import com.mraof.minestuck.tileentity.redstone.StatStorerTileEntity;
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
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityStruckByLightningEvent;
import net.minecraftforge.event.entity.item.ItemExpireEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.*;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.minecraftforge.event.world.SaplingGrowTreeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppedEvent;

import java.util.List;

@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
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
	
	@SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = false)
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
		
		statStorer(1, StatStorerTileEntity.ActiveType.DEATHS, event.getEntity().getPosition(), event.getEntity().world);
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
	
	public static void statStorer(float eventAmount, StatStorerTileEntity.ActiveType activeType, BlockPos eventPos, World world)
	{
		for(BlockPos blockPos : BlockPos.getAllInBoxMutable(eventPos.add(10, 10, 10), eventPos.add(-10, -10, -10)))
		{
			TileEntity tileEntity = world.getTileEntity(blockPos);
			if(tileEntity instanceof StatStorerTileEntity)
			{
				StatStorerTileEntity storerTileEntity = (StatStorerTileEntity) tileEntity;
				
				if(activeType == storerTileEntity.getActiveType())
					storerTileEntity.setActiveStoredStatValue(storerTileEntity.getActiveStoredStatValue() + eventAmount, blockPos.up(), true);
			}
		}
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = false)
	public static void onEntityHeal(LivingHealEvent event)
	{
		statStorer(event.getAmount(), StatStorerTileEntity.ActiveType.HEALTH_RECOVERED, event.getEntity().getPosition(), event.getEntity().world);
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = false)
	public static void onSaplingGrow(SaplingGrowTreeEvent event)
	{
		statStorer(1, StatStorerTileEntity.ActiveType.SAPLING_GROWN, event.getPos(), event.getWorld().getWorld());
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = false)
	public static void onEntityStruck(EntityStruckByLightningEvent event)
	{
		statStorer(1, StatStorerTileEntity.ActiveType.LIGHTNING_STRUCK, event.getEntity().getPosition(), event.getEntity().world);
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = false)
	public static void onBlockRightClick(PlayerInteractEvent.RightClickBlock event)
	{
		//conditions check does not work
		if(event.getUseBlock() == Event.Result.ALLOW)
			statStorer(1, StatStorerTileEntity.ActiveType.BLOCK_RIGHT_CLICK, event.getEntity().getPosition(), event.getEntity().world);
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = false)
	public static void onEntitySetTarget(LivingSetAttackTargetEvent event)
	{
		//adds value every tick
		//statStorer(1, StatStorerTileEntity.ActiveType.ENTITY_SET_TARGET, event.getEntity().getPosition(), event.getEntity().world);
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = false)
	public static void onAlchemy(AlchemyEvent event)
	{
		statStorer(1, StatStorerTileEntity.ActiveType.ALCHEMY_ACTIVATED, event.getAlchemiter().getPos(), event.getWorld());
	}
	
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public static void onEntityAttack(LivingHurtEvent event)
	{
		if(event.getSource().getTrueSource() != null)
		{
			if(event.getSource().getTrueSource() instanceof ServerPlayerEntity)
			{
				ServerPlayerEntity player = (ServerPlayerEntity) event.getSource().getTrueSource();
				if(event.getEntityLiving() instanceof UnderlingEntity)
				{    //Increase damage to underling
					double modifier = PlayerSavedData.getData(player).getEcheladder().getUnderlingDamageModifier();
					event.setAmount((float) (event.getAmount() * modifier));
				}
			} else if(event.getEntityLiving() instanceof ServerPlayerEntity && event.getSource().getTrueSource() instanceof UnderlingEntity)
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
		
		statStorer(event.getAmount(), StatStorerTileEntity.ActiveType.DAMAGE, event.getEntity().getPosition(), event.getEntity().world);
	}
	
	@SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = false)
	public static void onPlayerInjured(LivingHurtEvent event)
	{
		if(event.getEntityLiving() instanceof PlayerEntity)
		{
			PlayerEntity injuredPlayer = ((PlayerEntity) event.getEntity());
			Title title = PlayerSavedData.getData((ServerPlayerEntity) injuredPlayer).getTitle();
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
		}
	}
	
	@SubscribeEvent
	public static void playerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event)
	{
		TitleSelectionHook.cancelSelection((ServerPlayerEntity) event.getPlayer());
		
		PlayerSavedData.getData((ServerPlayerEntity) event.getPlayer()).getEcheladder().resendAttributes(event.getPlayer());
	}
	
	@SubscribeEvent(priority = EventPriority.LOW, receiveCanceled = false)
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
			event.getContext().getWorld().playSound(null, event.getContext().getPos(), SoundEvents.ITEM_HOE_TILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
			event.setResult(Event.Result.ALLOW);
		}
	}
	
	@SubscribeEvent
	public static void onGetItemBurnTime(FurnaceFuelBurnTimeEvent event)
	{
		if(event.getItemStack().getItem() == MSBlocks.TREATED_PLANKS.asItem())
			event.setBurnTime(50);    //Do not set this number to 0.
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
		
		if(!event.player.isCreative() && event.player.isPotionActive(MSEffects.CREATIVE_SHOCK.get()))
		{
			int duration = event.player.getActivePotionEffect(MSEffects.CREATIVE_SHOCK.get()).getDuration();
			if(duration >= 5)
				event.player.abilities.allowEdit = false;
			else
			{
				if(!event.player.world.isRemote)
					event.player.abilities.allowEdit = ((ServerPlayerEntity) event.player).interactionManager.getGameType().hasLimitedInteractions();
			}
		}
	}
	
	@SubscribeEvent
	public static void onEffectRemove(PotionEvent.PotionRemoveEvent event)
	{
		onEffectEnd(event.getEntityLiving(), event.getPotionEffect().getPotion());
	}
	
	@SubscribeEvent
	public static void onEffectExpire(PotionEvent.PotionExpiryEvent expiryEvent)
	{
		onEffectEnd(expiryEvent.getEntityLiving(), expiryEvent.getPotionEffect().getPotion());
	}
	
	private static void onEffectEnd(LivingEntity entityLiving, Effect effect) //TODO MSPacketHandler.sendToPlayer recieves an invalid message
	{
		if(entityLiving instanceof PlayerEntity)
		{
			PlayerEntity player = (PlayerEntity) entityLiving;
			
			if(player instanceof ServerPlayerEntity && !player.isCreative() && effect == MSEffects.CREATIVE_SHOCK.get())
			{
				ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) player;
				player.abilities.allowEdit = !serverPlayerEntity.interactionManager.getGameType().hasLimitedInteractions();
				
				StopCreativeShockEffectPacket packet = new StopCreativeShockEffectPacket(serverPlayerEntity.interactionManager.getGameType());
				MSPacketHandler.sendToPlayer(packet, serverPlayerEntity);
			}
		}
	}
	
	@SubscribeEvent
	public static void onBreakSpeed(PlayerEvent.BreakSpeed event)
	{
		if(event.getPlayer().isPotionActive(MSEffects.CREATIVE_SHOCK.get()))
			event.setNewSpeed(0);
	}
	
	@SubscribeEvent
	public static void onHarvestCheck(PlayerEvent.HarvestCheck event)
	{
		if(event.getPlayer().isPotionActive(MSEffects.CREATIVE_SHOCK.get()))
			event.setCanHarvest(false);
	}
	
	
	@SubscribeEvent
	public static void breadStaling(ItemExpireEvent event)
	{
		ItemEntity e = event.getEntityItem();
		if(e.getItem().getCount() == 1 && (e.getItem().getItem() == Items.BREAD))
		{
			ItemEntity stalebread = new ItemEntity(e.world, e.getPosX(), e.getPosY(), e.getPosZ(), new ItemStack(MSItems.STALE_BAGUETTE));
			e.world.addEntity(stalebread);
		}
	}
}