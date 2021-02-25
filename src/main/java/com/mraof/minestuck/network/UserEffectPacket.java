package com.mraof.minestuck.network;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.player.EnumAspect;
import com.mraof.minestuck.player.EnumClass;
import com.mraof.minestuck.world.storage.PlayerData;
import com.mraof.minestuck.world.storage.PlayerSavedData;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.network.PacketBuffer;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

import static com.mraof.minestuck.player.EnumAspect.*;

public class UserEffectPacket implements PlayToServerPacket
{
	private static final Logger LOGGER = LogManager.getLogger();
	protected RayTraceResult rayTraceBlock;
	
	private static final Effect[] positiveAspectEffects = {null, Effects.SPEED, Effects.RESISTANCE, Effects.ABSORPTION, Effects.FIRE_RESISTANCE, Effects.REGENERATION, Effects.LUCK, Effects.NIGHT_VISION, Effects.STRENGTH, null, null, Effects.INVISIBILITY}; //Blood, Breath, Doom, Heart, Hope, Life, Light, Mind, Rage, Space, Time, Void
	
	@Override
	public void encode(PacketBuffer buffer)
	{
	}
	
	public static UserEffectPacket decode(PacketBuffer buffer)
	{
		return new UserEffectPacket();
	}
	
	@Override
	public void execute(ServerPlayerEntity player)
	{
		if(!MinestuckConfig.SERVER.aspectEffects.get())
			return;
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
				
				if(positiveAspectEffects[aspect.ordinal()] != null)
				{
					player.world.playSound(null, player.getPosition(), SoundEvents.ENTITY_ILLUSIONER_CAST_SPELL, SoundCategory.PLAYERS, 0.8F, 1.6F);
					player.addPotionEffect(effectInstance);
					LOGGER.debug("Applied aspect potion effect to {}, level {}", player.getName().getFormattedText(), potionLevel);
					heroClassModifiers(data, heroClass, aspect, rung, effectInstance);
				}
				
				if(aspect == SPACE)
				{
					this.rayTraceBlock = player.pick(5.0D + (double) rung * 1.5, 0.0F, false);
					
					if(this.rayTraceBlock.getType() == RayTraceResult.Type.BLOCK)
					{
						BlockRayTraceResult blockResult = (BlockRayTraceResult) this.rayTraceBlock;
						Direction blockFace = blockResult.getFace();
						BlockPos blockPos = blockResult.getPos().offset(blockFace);
						
						player.teleport(player.server.getWorld(player.dimension), blockPos.getX(), blockPos.getY(), blockPos.getZ(), player.rotationYaw, player.rotationPitch);
						player.world.playSound(null, player.getPosition(), SoundEvents.ENTITY_ILLUSIONER_CAST_SPELL, SoundCategory.PLAYERS, 0.8F, 1.6F);
						heroClassModifiers(data, heroClass, aspect, rung, effectInstance);
					}
				}
				
				if(aspect == TIME)
				{
					if(player.getHeldItemMainhand().getItem() == Items.CLOCK || player.getHeldItemOffhand().getItem() == Items.CLOCK)
					{
						data.setTimePlayerAnchor(player.getPosition(), player.server.getWorld(player.dimension), player.rotationYaw, player.rotationPitch);
						player.sendMessage(new StringTextComponent("Time anchor set!"));
						data.setAspectPowerCooldown(500);
					} else
					{
						if(data.getTimePlayerAnchorDimension() != null)
						{
							player.getServerWorld();
							player.teleport(data.getTimePlayerAnchorDimension(), data.getTimePlayerAnchorPos().getX(), data.getTimePlayerAnchorPos().getY(), data.getTimePlayerAnchorPos().getZ(), data.getTimePlayerAnchorYaw(), data.getTimePlayerAnchorPitch());
							player.world.playSound(null, player.getPosition(), SoundEvents.ENTITY_ILLUSIONER_CAST_SPELL, SoundCategory.PLAYERS, 0.8F, 1.6F);
							heroClassModifiers(data, heroClass, aspect, rung, effectInstance);
						} else
						{
							player.sendMessage(new StringTextComponent("Time anchor was not set or the dimension transfer was invalid! Hold a clock to store a location to return to."));
						}
					}
				}
				
				if(rung > 20 && aspect == HOPE)
				{
					player.addPotionEffect(new EffectInstance(Effects.WATER_BREATHING, 300, 0));
				}
				
				if(aspect == BLOOD)
				{
					List<Entity> playerEventList = player.world.getEntitiesWithinAABBExcludingEntity(player, player.getBoundingBox().grow(10.0D));
					
					if(!playerEventList.isEmpty())
					{
						for(Entity eventEntity : playerEventList)
						{
							if(eventEntity instanceof LivingEntity)
							{
								LivingEntity livingEventEntity = (LivingEntity) eventEntity;
								livingEventEntity.getCombatTracker().reset();
							}
						}
					}
				}
				
				if(rung > 20 && aspect == LIFE)
				{
					player.addPotionEffect(new EffectInstance(Effects.SATURATION, 1, 1));
				}
				
			}
		}
	}
	
	public void heroClassModifiers(PlayerData data, EnumClass heroClass, EnumAspect heroAspect, int rung, EffectInstance effectInstance)
	{
		
		data.setAspectPowerCooldown(4500);
		//heir unmodified here
		if(heroClass == EnumClass.SEER)
		{
			data.setAspectPowerCooldown(data.getAspectPowerCooldown() - (50 * (50 / (rung + 1)))); //inverts as player switches towards commanding others
		}
		if(heroClass == EnumClass.WITCH)
		{
			data.setAspectPowerCooldown(data.getAspectPowerCooldown() - (rung * 50 + 1250)); //high value
		}
		//knight unmodified here
		if(heroClass == EnumClass.MAID)
		{
			data.setAspectPowerCooldown(data.getAspectPowerCooldown() - (rung * 75)); //increases as player moves away from helping self
		}
		//rogue unmodified here
		if(heroClass == EnumClass.PAGE)
		{
			data.setAspectPowerCooldown(data.getAspectPowerCooldown() - ((int) Math.pow(1.2, rung / 1.1))); //exponential with slow start and higher than average finish
		}
		if(heroClass == EnumClass.PRINCE)
		{
			data.setAspectPowerCooldown(data.getAspectPowerCooldown() + 1500); //detrimental
		}
		//sylph unmodified here
		if(heroClass == EnumClass.MAGE)
		{
			data.setAspectPowerCooldown(data.getAspectPowerCooldown() - (rung * 50 + 250)); //moderate reduction
		}
		//thief unmodified here
		//bard unmodified here
		
		if(heroAspect == SPACE)
			data.setAspectPowerCooldown(data.getAspectPowerCooldown() - (rung * 50 + 250));
		if(heroAspect == TIME)
			data.setAspectPowerCooldown(data.getAspectPowerCooldown() - (rung * 50 + 500));
	}
}