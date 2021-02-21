package com.mraof.minestuck.network;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.player.EnumAspect;
import com.mraof.minestuck.world.storage.PlayerData;
import com.mraof.minestuck.world.storage.PlayerSavedData;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.mraof.minestuck.player.EnumAspect.HOPE;

public class PositiveEffectPacket implements PlayToServerPacket
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	private static final Effect[] positiveAspectEffects = {Effects.ABSORPTION, Effects.SPEED, Effects.RESISTANCE, Effects.ABSORPTION, Effects.FIRE_RESISTANCE, Effects.REGENERATION, Effects.LUCK, Effects.NIGHT_VISION, Effects.STRENGTH, Effects.JUMP_BOOST, Effects.HASTE, Effects.INVISIBILITY }; //Blood, Breath, Doom, Heart, Hope, Life, Light, Mind, Rage, Space, Time, Void
	//private boolean isUser;
	
	//private static final Effect[] negativeAspectEffects = {Effects.INSTANT_DAMAGE, Effects.SPEED, Effects.WEAKNESS, Effects.INSTANT_DAMAGE, Effects.WEAKNESS, Effects.WITHER, Effects.UNLUCK, Effects.BLINDNESS, Effects.WEAKNESS, Effects.SLOWNESS, Effects.SLOWNESS, Effects.GLOWING }; //Blood, Breath, Doom, Heart, Hope, Life, Light, Mind, Rage, Space, Time, Void
	
	// Increase the starting rungs
	//private static final float[] aspectStrength = new float[] {1.0F/14, 1.0F/15, 1.0F/28, 1.0F/14, 1.0F/18, 1.0F/20, 1.0F/10, 1.0F/12, 1.0F/25, 1.0F/10, 1.0F/13, 1.0F/12}; //Absorption, Speed, Resistance, Saturation, Fire Resistance, Regeneration, Luck, Night Vision, Strength, Jump Boost, Haste, Invisibility
	
	@Override
	public void encode(PacketBuffer buffer)
	{
	}
	
	public static PositiveEffectPacket decode(PacketBuffer buffer)
	{
		return new PositiveEffectPacket();
		//this.isUser = isUser;
	}
	
	@Override
	public void execute(ServerPlayerEntity player)
	{
		if(!MinestuckConfig.SERVER.aspectEffects.get())
			return;
		PlayerData data = PlayerSavedData.getData(player);
		int rung = data.getEcheladder().getRung();
		if(data.getTitle().getHeroAspect() != null){
			EnumAspect aspect = data.getTitle().getHeroAspect();
			int potionLevel = rung/15;
			
			if(rung > 20 && aspect == HOPE)
			{
				player.addPotionEffect(new EffectInstance(Effects.WATER_BREATHING, 600, 0));
			}
			
			player.addPotionEffect(new EffectInstance(positiveAspectEffects[aspect.ordinal()], 600, potionLevel));
			LOGGER.debug("Applied aspect potion effect to {}, level {}", player.getName().getFormattedText(), potionLevel);
		}
	}
}