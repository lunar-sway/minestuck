package com.mraof.minestuck.network;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.player.EnumAspect;
import com.mraof.minestuck.player.EnumClass;
import com.mraof.minestuck.world.storage.PlayerData;
import com.mraof.minestuck.world.storage.PlayerSavedData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.network.PacketBuffer;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.text.StringTextComponent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.function.Predicate;

import static com.mraof.minestuck.player.EnumAspect.*;

public class PositiveOtherEffectPacket implements PlayToServerPacket
{
	private static final Logger LOGGER = LogManager.getLogger();
	//protected RayTraceResult rayTraceBlock;
	//protected RayTraceResult rayTraceEntity;
	private static final EntityPredicate visiblePredicate = (new EntityPredicate()).setLineOfSiteRequired();
	//private static final Predicate<Entity> CAN_COLLIDE_PREDICATE = EntityPredicates.NOT_SPECTATING.and(Entity::canBeCollidedWith);
	//private static final Predicate CAN_COLLIDE_PREDICATE = (EntityPredicates.NOT_SPECTATING.and(Entity::canBeCollidedWith));
	//private static final EntityPredicate MOMMY_TARGETING = (new EntityPredicate()).setDistance(16.0D).allowInvulnerable().allowFriendlyFire().setLineOfSiteRequired().setCustomPredicate(IS_HORSE_BREEDING);
	
	private static final Effect[] positiveAspectEffects = {Effects.ABSORPTION, Effects.SLOW_FALLING, Effects.RESISTANCE, Effects.ABSORPTION, Effects.FIRE_RESISTANCE, Effects.REGENERATION, Effects.LUCK, Effects.NIGHT_VISION, Effects.STRENGTH, Effects.HASTE, Effects.SPEED, Effects.INVISIBILITY}; //Blood, Breath, Doom, Heart, Hope, Life, Light, Mind, Rage, Space, Time, Void
	
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
		PlayerData data = PlayerSavedData.getData(player);
		int rung = data.getEcheladder().getRung();
		int cooldown = data.getAspectPowerCooldown();
		if(data.getTitle().getHeroAspect() != null/* && CAN_COLLIDE_PREDICATE instanceof EntityPredicate*/)
		{
			EnumAspect aspect = data.getTitle().getHeroAspect();
			EnumClass heroClass = data.getTitle().getHeroClass();
			if(!player.isCreative())
				LogManager.getLogger().debug("{}'s power cooldown is {}", player.getName().getFormattedText(), cooldown);
			if(cooldown <= 0)
			{
				int potionLevel = rung / 12;
				EffectInstance effectInstance = new EffectInstance(positiveAspectEffects[aspect.ordinal()], 300, potionLevel);
				
				double modifiedDistance = 5.0D + (double) rung / 5;
				
				//this.rayTraceBlock = player.pick(5.0D + (double) rung / 5, 0.0F, false);
				//player.interactOn()
				//this.rayTraceEntity = player.interactOn(en);
				//RayTraceResult rayTraceResult = (RayTraceResult) new EntityRayTraceResult ;
				//List<Entity> list = player.world.getEntitiesInAABBexcluding(player, player.getBoundingBox().expand(player.getLookVec().scale(5.0D)).grow(0.3D), CAN_COLLIDE_PREDICATE);
				LivingEntity closestTarget = player.world.getClosestEntityWithinAABB(LivingEntity.class, visiblePredicate, player, player.getPosX(), player.getPosY(), player.getPosZ(), player.getBoundingBox().expand(player.getLookVec().scale(5.0D)).grow(0.3D));
				
				if(closestTarget != null)
				{
					LOGGER.debug("Entities targeted = {}", closestTarget);
					Vec3d eyePos = player.getEyePosition(1.0F)/*.scale(5.0D + (double) rung / 5)*/;
					
					Vec3d visionEdgeVec = new Vec3d(eyePos.x * (modifiedDistance*player.rotationYaw), eyePos.y * (modifiedDistance*(player.rotationPitch)), eyePos.z * (modifiedDistance*player.rotationYaw));
					LOGGER.debug("eyePos = {}, visionEdgeVec = {}, rotationYaw = {}, rotationPitch = {}", eyePos, visionEdgeVec, player.rotationYaw, player.rotationPitch);
					
					//LivingEntity livingTarget = (LivingEntity) targetEntity;
					//LOGGER.debug("Ray traced entity {}", livingTarget);
					if(positiveAspectEffects[aspect.ordinal()] != null)
					{
						if(rung > 20 && aspect == HOPE)
						{
							closestTarget.addPotionEffect(new EffectInstance(Effects.WATER_BREATHING, 300, 0));
						}
						player.world.playSound(null, closestTarget.getPosition(), SoundEvents.ENTITY_ILLUSIONER_CAST_SPELL, SoundCategory.PLAYERS, 0.8F, 1.6F);
						closestTarget.addPotionEffect(effectInstance);
						LOGGER.debug("Applied aspect potion effect to {}, level {}", closestTarget.getName().getFormattedText(), potionLevel);
						heroClassModifiers(data, heroClass, rung, effectInstance, (LivingEntity) player);
					}
					/*for(Entity targetEntity : list)
					{
						player.getDistanceSq(targetEntity).;
						AxisAlignedBB axisalignedbb = targetEntity.getBoundingBox().grow(targetEntity.getCollisionBorderSize());
						/*if(axisalignedbb.contains(eyePos))
						{
							if(targetEntity instanceof LivingEntity)
							{
								LivingEntity livingTarget = (LivingEntity) targetEntity;
								LOGGER.debug("Ray traced entity {}", livingTarget);
								if(positiveAspectEffects[aspect.ordinal()] != null)
								{
									if(rung > 20 && aspect == HOPE)
									{
										livingTarget.addPotionEffect(new EffectInstance(Effects.WATER_BREATHING, 300, 0));
									}
									player.world.playSound(null, livingTarget.getPosition(), SoundEvents.ENTITY_ILLUSIONER_CAST_SPELL, SoundCategory.PLAYERS, 0.8F, 1.6F);
									livingTarget.addPotionEffect(effectInstance);
									LOGGER.debug("Applied aspect potion effect to {}, level {}", livingTarget.getName().getFormattedText(), potionLevel);
									heroClassModifiers(data, heroClass, rung, effectInstance, player);
								}
							}
						}*/
						//(player.getPosX(), player.getPosY(), player.getPosZ(), eyePos.x, eyePos.y, eyePos.z)
						//if(axisalignedbb.intersects(eyePos, visionEdgeVec)){
						/*if(axisalignedbb.intersects(eyePos, visionEdgeVec)){
							if(targetEntity instanceof LivingEntity)
							{
								LivingEntity livingTarget = (LivingEntity) targetEntity;
								LOGGER.debug("Ray traced entity {}", livingTarget);
								if(positiveAspectEffects[aspect.ordinal()] != null)
								{
									if(rung > 20 && aspect == HOPE)
									{
										livingTarget.addPotionEffect(new EffectInstance(Effects.WATER_BREATHING, 300, 0));
									}
									player.world.playSound(null, livingTarget.getPosition(), SoundEvents.ENTITY_ILLUSIONER_CAST_SPELL, SoundCategory.PLAYERS, 0.8F, 1.6F);
									livingTarget.addPotionEffect(effectInstance);
									LOGGER.debug("Applied aspect potion effect to {}, level {}", livingTarget.getName().getFormattedText(), potionLevel);
									heroClassModifiers(data, heroClass, rung, effectInstance, (LivingEntity) player);
								}
							}
						}*/
					//}
				}
				/*if(this.rayTraceBlock.getType() == RayTraceResult.Type.ENTITY)
				{
					RayTraceResult rayTarget = this.rayTraceBlock;
					Entity target = ((EntityRayTraceResult)rayTarget).getEntity();
					if(target instanceof LivingEntity){
						LivingEntity livingTarget = (LivingEntity) target;
						LOGGER.debug("Ray traced entity {}", livingTarget);
						if(positiveAspectEffects[aspect.ordinal()] != null){
							if(rung > 20 && aspect == HOPE)
							{
								livingTarget.addPotionEffect(new EffectInstance(Effects.WATER_BREATHING, 300, 0));
							}
							player.world.playSound(null, livingTarget.getPosition(), SoundEvents.ENTITY_ILLUSIONER_CAST_SPELL, SoundCategory.PLAYERS, 0.8F, 1.6F);
							livingTarget.addPotionEffect(effectInstance);
							LOGGER.debug("Applied aspect potion effect to {}, level {}", livingTarget.getName().getFormattedText(), potionLevel);
							heroClassModifiers(data, heroClass, rung, effectInstance, player);
						}
					}
				}*/
			}
		}
	}
	
	public void heroClassModifiers(PlayerData data, EnumClass heroClass, int rung, EffectInstance effectInstance, LivingEntity playerEntity)
	{
		//heir unmodified here
		if(heroClass == EnumClass.SEER)
		{
			data.setAspectPowerCooldown(4500 - (rung * 60)); //increases as player moves away from helping self
		}
		//witch unmodified here
		if(heroClass == EnumClass.KNIGHT)
		{
			data.setAspectPowerCooldown(4500 - (rung * 50) + 1000); //high value
		}
		if(heroClass == EnumClass.MAID)
		{
			data.setAspectPowerCooldown(4500 - (50 * (50 / (rung + 1)))); //inverts as player switches towards commanding others
		}
		//rogue unmodified here
		//page unmodified here
		//prince unmodified here
		if(heroClass == EnumClass.SYLPH)
		{
			data.setAspectPowerCooldown(4500 - (rung * 50) + 1000); //high value
			playerEntity.addPotionEffect(effectInstance);
			LOGGER.debug("Applied class bonus aspect potion effect to {}", playerEntity.getName().getFormattedText());
		}
		if(heroClass == EnumClass.MAGE)
		{
			data.setAspectPowerCooldown(4500 - (rung * 50) + 250); //moderate reduction
		}
		if(heroClass == EnumClass.THIEF)
		{
			data.setAspectPowerCooldown(4500 + 1500); //detrimental
		}
		//bard unmodified here
		else
		{
			data.setAspectPowerCooldown(4500);
		}
	}
}