package com.mraof.minestuck.network;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.entity.underling.UnderlingEntity;
import com.mraof.minestuck.player.EnumAspect;
import com.mraof.minestuck.player.EnumClass;
import com.mraof.minestuck.world.storage.PlayerData;
import com.mraof.minestuck.world.storage.PlayerSavedData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.StringTextComponent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

import static com.mraof.minestuck.player.EnumAspect.*;

public class NegativeOtherEffectPacket implements PlayToServerPacket
{
	private static final Logger LOGGER = LogManager.getLogger();
	private static final EntityPredicate visiblePredicate = (new EntityPredicate()).setLineOfSiteRequired();
	
	private PlayerEntity playerCasting;
	
	private static final Effect[] negativeAspectEffects = {null, Effects.SLOWNESS, Effects.WITHER, null, Effects.WEAKNESS, Effects.WITHER, null, null, Effects.WEAKNESS, null, null, Effects.BLINDNESS}; //Blood, Breath, Doom, Heart, Hope, Life, Light, Mind, Rage, Space, Time, Void
	private static final Effect[] positiveAspectEffects = {Effects.ABSORPTION, Effects.SLOW_FALLING, Effects.RESISTANCE, Effects.ABSORPTION, Effects.FIRE_RESISTANCE, Effects.REGENERATION, Effects.LUCK, Effects.NIGHT_VISION, Effects.STRENGTH, Effects.SPEED, Effects.HASTE, Effects.INVISIBILITY}; //Blood, Breath, Doom, Heart, Hope, Life, Light, Mind, Rage, Space, Time, Void
	
	@Override
	public void encode(PacketBuffer buffer)
	{
	}
	
	public static NegativeOtherEffectPacket decode(PacketBuffer buffer)
	{
		return new NegativeOtherEffectPacket();
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
		playerCasting = (PlayerEntity) player;
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
				int potionLevel = rung / 20;
				EffectInstance effectInstance = new EffectInstance(negativeAspectEffects[aspect.ordinal()], 200, potionLevel);
				
				LivingEntity closestTarget = player.world.getClosestEntityWithinAABB(LivingEntity.class, visiblePredicate, player, player.getPosX(), player.getPosY(), player.getPosZ(), player.getBoundingBox().expand(player.getLookVec().scale(5.0D + (double) rung / 5)).grow(0.05D));
				
				if(closestTarget != null)
				{
					if(negativeAspectEffects[aspect.ordinal()] != null)
					{
						player.world.playSound(null, closestTarget.getPosition(), SoundEvents.ENTITY_ILLUSIONER_CAST_SPELL, SoundCategory.PLAYERS, 0.8F, 1.6F);
						closestTarget.addPotionEffect(effectInstance);
						LOGGER.debug("Applied aspect potion effect to {}, level {}", closestTarget.getName().getFormattedText(), potionLevel);
						damageTarget(closestTarget, rung, 5);
					} else
					{
						if(aspect == SPACE)
						{
							damageTarget(closestTarget, rung, 5);
						} else if(aspect == TIME)
						{
							damageTarget(closestTarget, rung, 5);
						} else if(aspect == MIND)
						{
							closestTarget.getCombatTracker().reset();
							damageTarget(closestTarget, rung, 5);
						} else if(aspect == HEART)
						{
							closestTarget.getCombatTracker().reset();
							damageTarget(closestTarget, rung, 5);
						} else if(aspect == BLOOD)
						{
							List<Entity> targetEventList = closestTarget.world.getEntitiesWithinAABBExcludingEntity(closestTarget, closestTarget.getBoundingBox().grow(20.0D));
							targetEventList.remove(player);
							if(!targetEventList.isEmpty())
							{
								for(Entity eventEntity : targetEventList)
								{
									if(eventEntity instanceof LivingEntity)
									{
										LivingEntity livingEventEntity = (LivingEntity) eventEntity;
										livingEventEntity.setRevengeTarget(closestTarget);
									}
								}
							} else
							{
								closestTarget.getCombatTracker().reset();
							}
							damageTarget(closestTarget, rung, 5);
						} else if(aspect == LIGHT)
						{
							List<Entity> targetEventList = closestTarget.world.getEntitiesWithinAABBExcludingEntity(closestTarget, closestTarget.getBoundingBox().grow(20.0D));
							targetEventList.remove(player);
							if(!targetEventList.isEmpty())
							{
								for(Entity eventEntity : targetEventList)
								{
									if(eventEntity instanceof LivingEntity)
									{
										LivingEntity livingEventEntity = (LivingEntity) eventEntity;
										livingEventEntity.setGlowing(true);
									}
								}
							}
							damageTarget(closestTarget, rung, 5);
						} else
						{
							damageTarget(closestTarget, rung, 3);
						}
					}
					heroClassModifiers(data, heroClass, rung, effectInstance);
				}
			}
		}
	}
	
	public void heroClassModifiers(PlayerData data, EnumClass heroClass, int rung, EffectInstance effectInstance)
	{
		data.setAspectPowerCooldown(4500);
		//heir unmodified here
		//seer unmodified here
		//witch unmodified here
		//knight unmodified here
		//maid unmodified here
		if(heroClass == EnumClass.ROGUE)
		{
			data.setAspectPowerCooldown(4500 - (rung * 50 + 1000)); //high value
			PlayerEntity nearbyPeacefulPlayer = playerCasting.world.getClosestEntityWithinAABB(PlayerEntity.class, visiblePredicate, playerCasting, playerCasting.getPosX(), playerCasting.getPosY(), playerCasting.getPosZ(), playerCasting.getBoundingBox().grow(15.0D));
			if(playerCasting.getCombatTracker().getFighter() != nearbyPeacefulPlayer && nearbyPeacefulPlayer != null)
			{
				nearbyPeacefulPlayer.addPotionEffect(new EffectInstance(positiveAspectEffects[data.getTitle().getHeroAspect().ordinal()], 300, rung / 12));
				LOGGER.debug("Applied class bonus aspect potion effect to {}", nearbyPeacefulPlayer.getName().getFormattedText());
			}
			
		}
		//page unmodified here
		if(heroClass == EnumClass.PRINCE)
		{
			data.setAspectPowerCooldown(data.getAspectPowerCooldown() - (rung * 50 + 1250)); //high value
		}
		if(heroClass == EnumClass.SYLPH)
		{
			data.setAspectPowerCooldown(data.getAspectPowerCooldown() + 1500); //detrimental
		}
		if(heroClass == EnumClass.MAGE)
		{
			data.setAspectPowerCooldown(data.getAspectPowerCooldown() - (rung * 50 + 250)); //moderate reduction
		}
		if(heroClass == EnumClass.THIEF)
		{
			data.setAspectPowerCooldown(data.getAspectPowerCooldown() - (rung * 50 + 1000)); //high value
			playerCasting.addPotionEffect(effectInstance);
			LOGGER.debug("Applied class bonus aspect potion effect to {}", playerCasting.getName().getFormattedText());
		}
		//bard unmodified here
	}
	
	public void damageTarget(LivingEntity closestTarget, int rung, int mod)
	{
		LOGGER.debug("Health going in {}", closestTarget.getHealth());
		closestTarget.performHurtAnimation();
		closestTarget.setRevengeTarget(playerCasting);
		if(closestTarget instanceof UnderlingEntity)
			closestTarget.setHealth(closestTarget.getHealth() - 5 + rung);
		else
			closestTarget.setHealth(closestTarget.getHealth() - (int) (2 + (double) rung / mod));
		LOGGER.debug("Health coming out {}", closestTarget.getHealth());
	}
}