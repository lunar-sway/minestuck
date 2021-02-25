package com.mraof.minestuck.network;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.player.EnumAspect;
import com.mraof.minestuck.player.EnumClass;
import com.mraof.minestuck.world.storage.PlayerData;
import com.mraof.minestuck.world.storage.PlayerSavedData;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.*;
import net.minecraft.util.text.StringTextComponent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.mraof.minestuck.player.EnumAspect.*;

public class PositiveOtherEffectPacket implements PlayToServerPacket
{
	private static final Logger LOGGER = LogManager.getLogger();
	private static final EntityPredicate visiblePredicate = (new EntityPredicate()).setLineOfSiteRequired();
	
	private static final Effect[] positiveAspectEffects = {Effects.ABSORPTION, Effects.SLOW_FALLING, Effects.RESISTANCE, Effects.ABSORPTION, Effects.FIRE_RESISTANCE, Effects.REGENERATION, Effects.LUCK, Effects.NIGHT_VISION, Effects.STRENGTH, Effects.SPEED, Effects.HASTE, Effects.INVISIBILITY}; //Blood, Breath, Doom, Heart, Hope, Life, Light, Mind, Rage, Space, Time, Void
	
	@Override
	public void encode(PacketBuffer buffer)
	{
	}
	
	public static PositiveOtherEffectPacket decode(PacketBuffer buffer)
	{
		return new PositiveOtherEffectPacket();
	}
	
	@Override
	public void execute(ServerPlayerEntity player)
	{
		if(!MinestuckConfig.SERVER.aspectEffects.get())
			return;
		if(player.isSpectator()){
			player.sendMessage(new StringTextComponent("Aspect powers cannot be used in spectator mode"));
			return;
		}
		PlayerData data = PlayerSavedData.getData(player);
		int rung = data.getEcheladder().getRung();
		int cooldown = data.getAspectPowerCooldown();
		if(data.getTitle().getHeroAspect() != null)
		{
			EnumAspect aspect = data.getTitle().getHeroAspect();
			EnumClass heroClass = data.getTitle().getHeroClass();
			if(!player.isCreative())
				LogManager.getLogger().debug("{}'s power cooldown is {}", player.getName().getFormattedText(), cooldown);
			if(cooldown <= 0)
			{
				int potionLevel = rung / 12;
				EffectInstance effectInstance = new EffectInstance(positiveAspectEffects[aspect.ordinal()], 300, potionLevel);
				
				LivingEntity closestTarget = player.world.getClosestEntityWithinAABB(LivingEntity.class, visiblePredicate, player, player.getPosX(), player.getPosY(), player.getPosZ(), player.getBoundingBox().expand(player.getLookVec().scale(5.0D + (double) rung / 5)).grow(0.05D));
				
				if(closestTarget != null)
				{
					if(positiveAspectEffects[aspect.ordinal()] != null)
					{
						if(rung > 20 && aspect == SPACE)
						{
							closestTarget.addPotionEffect(new EffectInstance(Effects.HASTE, 300, 0));
						}
						
						if(rung > 20 && aspect == TIME)
						{
							closestTarget.addPotionEffect(new EffectInstance(Effects.SPEED, 300, 0));
						}
						
						if(rung > 20 && aspect == HOPE)
						{
							closestTarget.addPotionEffect(new EffectInstance(Effects.WATER_BREATHING, 300, 0));
						}
						
						if(rung > 20 && aspect == LIFE)
						{
							player.addPotionEffect(new EffectInstance(Effects.SATURATION, 1, 0));
						}
						
						player.world.playSound(null, closestTarget.getPosition(), SoundEvents.ENTITY_ILLUSIONER_CAST_SPELL, SoundCategory.PLAYERS, 0.8F, 1.6F);
						closestTarget.addPotionEffect(effectInstance);
						LOGGER.debug("Applied aspect potion effect to {}, level {}", closestTarget.getName().getFormattedText(), potionLevel);
						heroClassModifiers(data, heroClass, rung, effectInstance, (LivingEntity) player);
					}
				}
			}
		}
	}
	
	public void heroClassModifiers(PlayerData data, EnumClass heroClass, int rung, EffectInstance effectInstance, LivingEntity playerEntity)
	{
		data.setAspectPowerCooldown(4500);
		//heir unmodified here
		if(heroClass == EnumClass.SEER)
		{
			data.setAspectPowerCooldown(data.getAspectPowerCooldown() - (rung * 75)); //increases as player moves away from helping self
		}
		//witch unmodified here
		if(heroClass == EnumClass.KNIGHT)
		{
			data.setAspectPowerCooldown(data.getAspectPowerCooldown() - (rung * 50 + 1250)); //high value
		}
		if(heroClass == EnumClass.MAID)
		{
			data.setAspectPowerCooldown(data.getAspectPowerCooldown() - (50 * (50 / (rung + 1)))); //inverts as player switches towards commanding others
		}
		//rogue unmodified here
		//page unmodified here
		//prince unmodified here
		if(heroClass == EnumClass.SYLPH)
		{
			data.setAspectPowerCooldown(data.getAspectPowerCooldown() - (rung * 50 + 1000)); //high value
			playerEntity.addPotionEffect(effectInstance);
			LOGGER.debug("Applied class bonus aspect potion effect to {}", playerEntity.getName().getFormattedText());
		}
		if(heroClass == EnumClass.MAGE)
		{
			data.setAspectPowerCooldown(data.getAspectPowerCooldown() - (rung * 50 + 250)); //moderate reduction
		}
		if(heroClass == EnumClass.THIEF)
		{
			data.setAspectPowerCooldown(data.getAspectPowerCooldown() + 1500); //detrimental
		}
		//bard unmodified here
	}
}