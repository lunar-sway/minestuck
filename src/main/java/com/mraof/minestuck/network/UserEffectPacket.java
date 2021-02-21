package com.mraof.minestuck.network;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.player.EnumAspect;
import com.mraof.minestuck.world.storage.PlayerData;
import com.mraof.minestuck.world.storage.PlayerSavedData;
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

import static com.mraof.minestuck.player.EnumAspect.*;

public class UserEffectPacket implements PlayToServerPacket
{
	private static final Logger LOGGER = LogManager.getLogger();
	protected RayTraceResult rayTraceBlock;
	
	private static final Effect[] positiveAspectEffects = {Effects.ABSORPTION, Effects.SPEED, Effects.RESISTANCE, Effects.ABSORPTION, Effects.FIRE_RESISTANCE, null, Effects.LUCK, Effects.NIGHT_VISION, Effects.STRENGTH, null, null, Effects.INVISIBILITY}; //Blood, Breath, Doom, Heart, Hope, Life, Light, Mind, Rage, Space, Time, Void
	
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
			if(!player.isCreative())
				LogManager.getLogger().debug("{}'s power cooldown is {}", player.getName().getFormattedText(), cooldown);
			if(cooldown <= 0)
			{
				EnumAspect aspect = data.getTitle().getHeroAspect();
				int potionLevel = rung / 12;
				
				if(rung > 20 && aspect == HOPE)
				{
					player.addPotionEffect(new EffectInstance(Effects.WATER_BREATHING, 300, 0));
				}
				
				if(positiveAspectEffects[aspect.ordinal()] != null)
				{
					player.world.playSound(null, player.getPosition(), SoundEvents.ENTITY_ILLUSIONER_CAST_SPELL, SoundCategory.PLAYERS, 0.8F, 1.6F);
					player.addPotionEffect(new EffectInstance(positiveAspectEffects[aspect.ordinal()], 300, potionLevel));
					LOGGER.debug("Applied aspect potion effect to {}, level {}", player.getName().getFormattedText(), potionLevel);
					data.setAspectPowerCooldown(4500);
				}
				
				if(aspect == SPACE)
				{
					this.rayTraceBlock = player.pick(5.0D + (double) rung * 1.1, 0.0F, false);
					
					if(this.rayTraceBlock.getType() == RayTraceResult.Type.BLOCK)
					{
						BlockRayTraceResult blockResult = (BlockRayTraceResult) this.rayTraceBlock;
						Direction blockFace = blockResult.getFace();
						BlockPos blockPos = blockResult.getPos().offset(blockFace);
						
						player.teleport(player.server.getWorld(player.dimension), blockPos.getX(), blockPos.getY(), blockPos.getZ(), player.rotationYaw, player.rotationPitch);
						player.world.playSound(null, player.getPosition(), SoundEvents.ENTITY_ILLUSIONER_CAST_SPELL, SoundCategory.PLAYERS, 0.8F, 1.6F);
					}
				}
				
				if(aspect == LIFE)
				{
					if(player.getHeldItemMainhand().getItem() == Items.CLOCK || player.getHeldItemOffhand().getItem() == Items.CLOCK)
					{
						data.setTimePlayerAnchor(player.getPosition(), player.server.getWorld(player.dimension), player.rotationYaw, player.rotationPitch);
						player.sendMessage(new StringTextComponent("Time anchor set!"));
					} else
					{
						if(data.getTimePlayerAnchorDimension() != null)
						{
							player.teleport(data.getTimePlayerAnchorDimension(), data.getTimePlayerAnchorPos().getX(), data.getTimePlayerAnchorPos().getY(), data.getTimePlayerAnchorPos().getZ(), data.getTimePlayerAnchorYaw(), data.getTimePlayerAnchorPitch());
							player.world.playSound(null, player.getPosition(), SoundEvents.ENTITY_ILLUSIONER_CAST_SPELL, SoundCategory.PLAYERS, 0.8F, 1.6F);
						} else
						{
							player.sendMessage(new StringTextComponent("Time anchor was not set or the dimension transfer was invalid! Hold a clock to store a location to return to."));
						}
					}
				}
			}
		}
		
	}
}