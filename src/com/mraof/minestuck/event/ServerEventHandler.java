package com.mraof.minestuck.event;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.entity.underling.EntityUnderling;
import com.mraof.minestuck.inventory.captchalouge.HashmapModus;
import com.mraof.minestuck.inventory.captchalouge.Modus;
import com.mraof.minestuck.item.MinestuckItems;
import com.mraof.minestuck.item.weapon.ItemModularWeapon;
import com.mraof.minestuck.item.weapon.ItemPotionWeapon;
import com.mraof.minestuck.network.skaianet.SburbConnection;
import com.mraof.minestuck.network.skaianet.SburbHandler;
import com.mraof.minestuck.network.skaianet.SkaianetHandler;
import com.mraof.minestuck.util.*;
import net.java.games.input.Component;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.UseHoeEvent;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerChangedDimensionEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.mraof.minestuck.util.EnumAspect.HOPE;
import static com.mraof.minestuck.util.MinestuckPlayerData.getTitle;

public class ServerEventHandler
{
	
	public static final ServerEventHandler instance = new ServerEventHandler();
	
	public static long lastDay;
	
	public static List<PostEntryTask> tickTasks = new ArrayList<PostEntryTask>();
	
	static Potion[] aspectEffects = { MobEffects.ABSORPTION, MobEffects.SPEED, MobEffects.RESISTANCE, MobEffects.SATURATION, MobEffects.FIRE_RESISTANCE, MobEffects.REGENERATION, MobEffects.LUCK, MobEffects.NIGHT_VISION, MobEffects.STRENGTH, MobEffects.JUMP_BOOST, MobEffects.HASTE, MobEffects.INVISIBILITY }; //Blood, Breath, Doom, Heart, Hope, Life, Light, Mind, Rage, Space, Time, Void
	// Increase the starting rungs
	static float[] aspectStrength = new float[] {1.0F/14, 1.0F/15, 1.0F/28, 1.0F/25, 1.0F/18, 1.0F/20, 1.0F/10, 1.0F/12, 1.0F/25, 1.0F/10, 1.0F/13, 1.0F/12}; //Absorption, Speed, Resistance, Saturation, Fire Resistance, Regeneration, Luck, Night Vision, Strength, Jump Boost, Haste, Invisibility
	
	@SubscribeEvent
	public void onWorldTick(TickEvent.WorldTickEvent event)
	{
		if(event.phase == TickEvent.Phase.END)
		{
			
			if(!MinestuckConfig.hardMode && event.world.provider.getDimension() == 0)
			{
				long time = event.world.getWorldTime() / 24000L;
				if(time != lastDay)
				{
					lastDay = time;
					SkaianetHandler.resetGivenItems();
				}
			}
			
			Iterator<PostEntryTask> iter = tickTasks.iterator();
			while(iter.hasNext())
				if(iter.next().onTick(event.world.getMinecraftServer()))
					iter.remove();
		}
	}
	
	@SubscribeEvent(priority=EventPriority.LOWEST, receiveCanceled=false)
	public void onEntityDeath(LivingDeathEvent event)
	{
		if(event.getEntity() instanceof IMob && event.getSource().getTrueSource() instanceof EntityPlayerMP)
		{
			EntityPlayerMP player = (EntityPlayerMP) event.getSource().getTrueSource();
			int exp = 0;
			if(event.getEntity() instanceof EntityZombie || event.getEntity() instanceof EntitySkeleton)
				exp = 6;
			else if(event.getEntity() instanceof EntityCreeper || event.getEntity() instanceof EntitySpider || event.getEntity() instanceof EntitySilverfish)
				exp = 5;
			else if(event.getEntity() instanceof EntityEnderman || event.getEntity() instanceof EntityBlaze || event.getEntity() instanceof EntityWitch || event.getEntity() instanceof EntityGuardian)
				exp = 12;
			else if(event.getEntity() instanceof EntitySlime)
				exp = ((EntitySlime) event.getEntity()).getSlimeSize() - 1;
			
			if(exp > 0)
				Echeladder.increaseProgress(player, exp);
		}
		if(event.getEntity() instanceof EntityPlayerMP)
			SburbHandler.stopEntry((EntityPlayerMP) event.getEntity());
	}

	//Gets reset after AttackEntityEvent but before LivingHurtEvent, but is used in determining if it's a critical hit
	private float cachedCooledAttackStrength = 0;

	@SubscribeEvent
	public void onPlayerAttack(AttackEntityEvent event)
	{
		cachedCooledAttackStrength = event.getEntityPlayer().getCooledAttackStrength(0.5F);
	}

	@SubscribeEvent(priority=EventPriority.NORMAL)
	public void onEntityAttack(LivingHurtEvent event)
	{
		if(event.getSource().getTrueSource() != null)
		{
			if (event.getSource().getTrueSource() instanceof EntityPlayerMP)
			{
				EntityPlayerMP player = (EntityPlayerMP) event.getSource().getTrueSource();
				if (event.getEntityLiving() instanceof EntityUnderling)
				{    //Increase damage to underling
					double modifier = MinestuckPlayerData.getData(player).echeladder.getUnderlingDamageModifier();
					event.setAmount((float) (event.getAmount() * modifier));
				}
				boolean critical = cachedCooledAttackStrength > 0.9 && player.fallDistance > 0.0F && !player.onGround && !player.isOnLadder() && !player.isInWater() && !player.isPotionActive(MobEffects.BLINDNESS) && !player.isRiding();
				if(!player.getHeldItemMainhand().isEmpty())
				{
					ItemStack weapon = player.getHeldItemMainhand();
					if(weapon.getItem() instanceof ItemPotionWeapon)
					{
						//If the attack was a critical, or if the PotionWeapon applies potions on all hits
						if(critical || !((ItemPotionWeapon) weapon.getItem()).potionOnCrit())
							event.getEntityLiving().addPotionEffect(((ItemPotionWeapon) weapon.getItem()).getEffect(player));
					}
					else if (weapon.getItem() instanceof ItemModularWeapon)
					{
						((ItemModularWeapon) weapon.getItem()).onCriticalHit(weapon, event.getEntityLiving(), player);
					}
				}
			}
			else if (event.getEntityLiving() instanceof EntityPlayerMP && event.getSource().getTrueSource() instanceof EntityUnderling)
			{    //Decrease damage to player
					EntityPlayerMP player = (EntityPlayerMP) event.getEntityLiving();
					double modifier = MinestuckPlayerData.getData(player).echeladder.getUnderlingProtectionModifier();
					event.setAmount((float) (event.getAmount() * modifier));
			}
		}
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = false)
	public void onEntityDamage(LivingHurtEvent event)
	{
		if(event.getEntityLiving() instanceof EntityUnderling)
		{
			((EntityUnderling) event.getEntityLiving()).onEntityDamaged(event.getSource(), event.getAmount());
		}
	}
	
	@SubscribeEvent
	public void playerChangedDimension(PlayerChangedDimensionEvent event)
	{
		SburbHandler.stopEntry(event.player);
		
		MinestuckPlayerData.getData(event.player).echeladder.resendAttributes(event.player);
	}
	
	@SubscribeEvent(priority=EventPriority.LOW, receiveCanceled=false)
	public void onServerChat(ServerChatEvent event)
	{
		Modus modus = MinestuckPlayerData.getData(event.getPlayer()).modus;
		if(modus instanceof HashmapModus)
			((HashmapModus) modus).onChatMessage(event.getMessage());
	}
	
	//This functionality uses an event to maintain compatibility with mod items having hoe functionality but not extending ItemHoe, like TiCon mattocks.
	@SubscribeEvent
	public void onPlayerUseHoe(UseHoeEvent event)
	{
		if(event.getWorld().getBlockState(event.getPos()).getBlock()==MinestuckBlocks.coarseEndStone)
		{
			event.getWorld().setBlockState(event.getPos(), Blocks.END_STONE.getDefaultState());
			event.getWorld().playSound(null, event.getPos(), SoundEvents.ITEM_HOE_TILL, SoundCategory.BLOCKS, 1.0F, 	1.0F);
			event.setResult(Result.ALLOW);
		}
	}
	
	@SubscribeEvent
	public void onGetItemBurnTime(FurnaceFuelBurnTimeEvent event)
	{
		if(event.getItemStack().getItem() == Item.getItemFromBlock(MinestuckBlocks.treatedPlanks))
			event.setBurnTime(50);	//Do not set this number to 0.
	}
	
	@SubscribeEvent
	public void aspectPotionEffect(TickEvent.PlayerTickEvent event)
	{
		IdentifierHandler.PlayerIdentifier identifier = IdentifierHandler.encode(event.player);
		SburbConnection c = SkaianetHandler.getMainConnection(identifier, true);
		if(c == null || !c.enteredGame() || MinestuckConfig.aspectEffects == false)
			return;
		int rung = MinestuckPlayerData.getData(identifier).echeladder.getRung();
		EnumAspect aspect = MinestuckPlayerData.getTitle(identifier).getHeroAspect();
		int potionLevel = (int) (aspectStrength[aspect.ordinal()] * rung); //Blood, Breath, Doom, Heart, Hope, Life, Light, Mind, Rage, Space, Time, Void
		
		if(event.player.getEntityWorld().getTotalWorldTime() % 380 == identifier.hashCode() % 380) {
			if(rung > 18 && aspect == HOPE) {
				event.player.addPotionEffect(new PotionEffect(MobEffects.WATER_BREATHING, 600, 0));
			}
			
			if(potionLevel > 0) {
				event.player.addPotionEffect(new PotionEffect(aspectEffects[aspect.ordinal()], 600, potionLevel-1));
			}
		}
	}
}