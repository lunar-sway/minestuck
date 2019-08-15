package com.mraof.minestuck.event;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.entity.underling.UnderlingEntity;
import com.mraof.minestuck.inventory.captchalogue.HashMapModus;
import com.mraof.minestuck.inventory.captchalogue.Modus;
import com.mraof.minestuck.item.weapon.PotionWeaponItem;
import com.mraof.minestuck.network.skaianet.SburbConnection;
import com.mraof.minestuck.network.skaianet.SburbHandler;
import com.mraof.minestuck.network.skaianet.SkaianetHandler;
import com.mraof.minestuck.util.*;

import com.mraof.minestuck.world.storage.PlayerSavedData;
import net.minecraft.block.Blocks;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.mraof.minestuck.util.EnumAspect.HOPE;

public class ServerEventHandler
{
	
	public static final ServerEventHandler instance = new ServerEventHandler();
	
	public static long lastDay;
	
	public static List<PostEntryTask> tickTasks = new ArrayList<>();
	
	static Effect[] aspectEffects = {Effects.ABSORPTION, Effects.SPEED, Effects.RESISTANCE, Effects.ABSORPTION, Effects.FIRE_RESISTANCE, Effects.REGENERATION, Effects.LUCK, Effects.NIGHT_VISION, Effects.STRENGTH, Effects.JUMP_BOOST, Effects.HASTE, Effects.INVISIBILITY }; //Blood, Breath, Doom, Heart, Hope, Life, Light, Mind, Rage, Space, Time, Void
	// Increase the starting rungs
	static float[] aspectStrength = new float[] {1.0F/14, 1.0F/15, 1.0F/28, 1.0F/14, 1.0F/18, 1.0F/20, 1.0F/10, 1.0F/12, 1.0F/25, 1.0F/10, 1.0F/13, 1.0F/12}; //Absorption, Speed, Resistance, Saturation, Fire Resistance, Regeneration, Luck, Night Vision, Strength, Jump Boost, Haste, Invisibility
	
	@SubscribeEvent
	public void onWorldTick(TickEvent.WorldTickEvent event)
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
			
			Iterator<PostEntryTask> iter = tickTasks.iterator();
			while(iter.hasNext())
				if(iter.next().onTick(event.world.getServer()))
					iter.remove();
		}
	}
	
	@SubscribeEvent(priority=EventPriority.LOWEST, receiveCanceled=false)
	public void onEntityDeath(LivingDeathEvent event)
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
	private float cachedCooledAttackStrength = 0;

	@SubscribeEvent
	public void onPlayerAttack(AttackEntityEvent event)
	{
		cachedCooledAttackStrength = event.getPlayer().getCooledAttackStrength(0.5F);
	}

	@SubscribeEvent(priority=EventPriority.NORMAL)
	public void onEntityAttack(LivingHurtEvent event)
	{
		if(event.getSource().getTrueSource() != null)
		{
			if (event.getSource().getTrueSource() instanceof ServerPlayerEntity)
			{
				ServerPlayerEntity player = (ServerPlayerEntity) event.getSource().getTrueSource();
				if (event.getEntityLiving() instanceof UnderlingEntity)
				{    //Increase damage to underling
					double modifier = PlayerSavedData.getData(player).echeladder.getUnderlingDamageModifier();
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
					double modifier = PlayerSavedData.getData(player).echeladder.getUnderlingProtectionModifier();
					event.setAmount((float) (event.getAmount() * modifier));
			}
		}
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = false)
	public void onEntityDamage(LivingHurtEvent event)
	{
		if(event.getEntityLiving() instanceof UnderlingEntity)
		{
			((UnderlingEntity) event.getEntityLiving()).onEntityDamaged(event.getSource(), event.getAmount());
		}
	}
	
	@SubscribeEvent
	public void playerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event)
	{
		SburbHandler.stopEntry((ServerPlayerEntity) event.getPlayer());
		
		PlayerSavedData.getData((ServerPlayerEntity) event.getPlayer()).echeladder.resendAttributes(event.getPlayer());
	}
	
	@SubscribeEvent(priority=EventPriority.LOW, receiveCanceled=false)
	public void onServerChat(ServerChatEvent event)
	{
		Modus modus = PlayerSavedData.getData(event.getPlayer()).modus;
		if(modus instanceof HashMapModus)
			((HashMapModus) modus).onChatMessage(event.getPlayer(), event.getMessage());
	}
	
	//This functionality uses an event to maintain compatibility with mod items having hoe functionality but not extending ItemHoe, like TiCon mattocks.
	@SubscribeEvent
	public void onPlayerUseHoe(UseHoeEvent event)
	{
		if(event.getContext().getWorld().getBlockState(event.getContext().getPos()).getBlock() == MinestuckBlocks.COARSE_END_STONE)
		{
			event.getContext().getWorld().setBlockState(event.getContext().getPos(), Blocks.END_STONE.getDefaultState());
			event.getContext().getWorld().playSound(null, event.getContext().getPos(), SoundEvents.ITEM_HOE_TILL, SoundCategory.BLOCKS, 1.0F, 	1.0F);
			event.setResult(Event.Result.ALLOW);
		}
	}
	
	@SubscribeEvent
	public void onGetItemBurnTime(FurnaceFuelBurnTimeEvent event)
	{
		if(event.getItemStack().getItem() == MinestuckBlocks.TREATED_PLANKS.asItem())
			event.setBurnTime(50);	//Do not set this number to 0.
	}
	
	@SubscribeEvent
	public void aspectPotionEffect(TickEvent.PlayerTickEvent event)
	{
		if(!event.player.world.isRemote)
		{
			IdentifierHandler.PlayerIdentifier identifier = IdentifierHandler.encode(event.player);
			SburbConnection c = SkaianetHandler.get(event.player.getServer()).getMainConnection(identifier, true);
			if(c == null || !c.hasEntered() || !MinestuckConfig.aspectEffects || !PlayerSavedData.get(event.player.getServer()).getEffectToggle(identifier))
				return;
			int rung = PlayerSavedData.getData((ServerPlayerEntity) event.player).echeladder.getRung();
			EnumAspect aspect = PlayerSavedData.get(event.player.getServer()).getTitle(identifier).getHeroAspect();
			int potionLevel = (int) (aspectStrength[aspect.ordinal()] * rung); //Blood, Breath, Doom, Heart, Hope, Life, Light, Mind, Rage, Space, Time, Void
			
			if(event.player.getEntityWorld().getGameTime() % 380 == identifier.hashCode() % 380)
			{
				if(rung > 18 && aspect == HOPE)
				{
					event.player.addPotionEffect(new EffectInstance(Effects.WATER_BREATHING, 600, 0));
				}
				
				if(potionLevel > 0)
				{
					event.player.addPotionEffect(new EffectInstance(aspectEffects[aspect.ordinal()], 600, potionLevel - 1));
				}
			}
		}
	}
}