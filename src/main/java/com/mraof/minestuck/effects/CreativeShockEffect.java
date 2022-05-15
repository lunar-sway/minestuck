package com.mraof.minestuck.effects;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.item.artifact.CruxiteArtifactItem;
import com.mraof.minestuck.network.MSPacketHandler;
import com.mraof.minestuck.network.StopCreativeShockEffectPacket;
import com.mraof.minestuck.util.MSTags;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.EnderPearlItem;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * This is an adapted version of Cibernet's code in Minestuck Universe, credit goes to him!
 * The different levels of amplification increase the range of the effect, allowing the selective limitation of both creative and non creative players.
 * 0 = cant directly cause block breakage/placement outside of creative, 1 = cant access redstone machinery gui outside of creative, 2 = cant use mobility items outside of creative,
 * 3 = cant directly cause block breakage/placement even in creative, 4 = cant access redstone machinery gui even in creative, 5 = cant use mobility items even in creative
 */
@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CreativeShockEffect extends Effect
{
	public static final int LIMIT_BLOCK_PLACEMENT_AND_BREAKING = 0;
	public static final int LIMIT_MACHINE_INTERACTIONS = 1; //TODO consider moving this to an amplifier of 0 so that it is uniform with player accessibility to vanilla redstone component right click functionality requiring "mayBuild"
	public static final int LIMIT_MOBILITY_ITEMS = 2;
	
	protected CreativeShockEffect()
	{
		super(EffectType.NEUTRAL, 0x47453d);
	}
	
	/**
	 * Checks whether player has creative shock effect and whether the amplifier is strong enough to limit.
	 * Will return true if amplifier is equal to or greater than relevant threshold.
	 */
	public static boolean doesCreativeShockLimit(PlayerEntity player, int survivalAmplifierThreshold) //TODO consider an inverted version of this function for the many "!doesCreativeShockLimit" use cases
	{
		return doesCreativeShockLimit(player, survivalAmplifierThreshold, survivalAmplifierThreshold + 3);
	}
	
	public static boolean doesCreativeShockLimit(PlayerEntity player, int survivalAmplifierThreshold, int creativeAmplifierThreshold)
	{
		if(player.hasEffect(MSEffects.CREATIVE_SHOCK.get()))
		{
			return player.getEffect(MSEffects.CREATIVE_SHOCK.get()).getAmplifier() >= (player.isCreative() ? creativeAmplifierThreshold : survivalAmplifierThreshold);
		}
		
		return false;
	}
	
	public static void stopElytraFlying(PlayerEntity player, int survivalAmplifierThreshold)
	{
		if(CreativeShockEffect.doesCreativeShockLimit(player, survivalAmplifierThreshold))
			player.stopFallFlying();
	}
	
	@Override
	public void applyEffectTick(LivingEntity entityLivingBaseIn, int amplifier)
	{
		super.applyEffectTick(entityLivingBaseIn, amplifier);
		
		if(!(entityLivingBaseIn instanceof PlayerEntity))
			return;
		
		PlayerEntity player = (PlayerEntity) entityLivingBaseIn;
		
		if(doesCreativeShockLimit(player, LIMIT_BLOCK_PLACEMENT_AND_BREAKING))
		{
			player.abilities.mayBuild = false; //this property is restored when the effect ends, in StopCreativeShockEffectPacket
		}
	}
	
	@Override
	public boolean isDurationEffectTick(int duration, int amplifier)
	{
		return (duration % 5) == 0;
	}
	
	@SubscribeEvent
	public static void onPlayerTickEvent(TickEvent.PlayerTickEvent event)
	{
		if(event.player.hasEffect(MSEffects.CREATIVE_SHOCK.get()))
		{
			int duration = event.player.getEffect(MSEffects.CREATIVE_SHOCK.get()).getDuration();
			if(duration >= 5)
			{
				if(CreativeShockEffect.doesCreativeShockLimit(event.player, LIMIT_BLOCK_PLACEMENT_AND_BREAKING))
					event.player.abilities.mayBuild = false;
				CreativeShockEffect.stopElytraFlying(event.player, LIMIT_MOBILITY_ITEMS);
			} else
			{
				if(!event.player.level.isClientSide)
				{
					event.player.abilities.mayBuild = ((ServerPlayerEntity) event.player).gameMode.getGameModeForPlayer().isBlockPlacingRestricted();
				}
			}
		}
	}
	
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public static void onLeftClickBlockEvent(PlayerInteractEvent.LeftClickBlock event)
	{
		if(event.getEntity() instanceof PlayerEntity)
		{
			PlayerEntity playerEntity = (PlayerEntity) event.getEntity();
			if(doesCreativeShockLimit(playerEntity, LIMIT_BLOCK_PLACEMENT_AND_BREAKING))
				event.setCanceled(true);
		}
	}
	
	@SubscribeEvent
	public static void onBreakSpeed(PlayerEvent.BreakSpeed event)
	{
		if(doesCreativeShockLimit(event.getPlayer(), LIMIT_BLOCK_PLACEMENT_AND_BREAKING))
			event.setNewSpeed(0);
	}
	
	@SubscribeEvent
	public static void onHarvestCheck(PlayerEvent.HarvestCheck event)
	{
		if(doesCreativeShockLimit(event.getPlayer(), LIMIT_BLOCK_PLACEMENT_AND_BREAKING))
			event.setCanHarvest(false);
	}
	
	@SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = false)
	public static void onExplosionCreativeShock(ExplosionEvent.Start event)
	{
		LivingEntity sourceEntity = event.getExplosion().getSourceMob();
		if(sourceEntity instanceof PlayerEntity)
		{
			if(CreativeShockEffect.doesCreativeShockLimit((PlayerEntity) sourceEntity, LIMIT_BLOCK_PLACEMENT_AND_BREAKING))
				event.setCanceled(true); //intended to prevent blocks from being destroyed by a player attempting to circumvent creative shock
		}
	}
	
	@SubscribeEvent
	public static void onRightClickItem(PlayerInteractEvent.RightClickItem event)
	{
		if(doesCreativeShockLimit(event.getPlayer(), LIMIT_BLOCK_PLACEMENT_AND_BREAKING))
		{
			if(event.getItemStack().getItem() instanceof CruxiteArtifactItem //Cruxite check prevents players from using an artifact to enter while under effects of Creative Shock
					|| event.getItemStack().getItem() instanceof EnderPearlItem
					|| MSTags.Items.CREATIVE_SHOCK_RIGHT_CLICK_LIMIT.contains(event.getItemStack().getItem()))
				event.setCanceled(true);
		}
	}
	
	public static void onEffectEnd(ServerPlayerEntity serverPlayerEntity)
	{
		serverPlayerEntity.abilities.mayBuild = !serverPlayerEntity.gameMode.getGameModeForPlayer().isBlockPlacingRestricted(); //block placing restricted was hasLimitedInteractions(), mayBuild was allowEdit
		
		StopCreativeShockEffectPacket packet = new StopCreativeShockEffectPacket(serverPlayerEntity.gameMode.getGameModeForPlayer().isBlockPlacingRestricted());
		MSPacketHandler.sendToPlayer(packet, serverPlayerEntity);
	}
}