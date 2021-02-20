package com.mraof.minestuck.network;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.player.EnumAspect;
import com.mraof.minestuck.world.storage.PlayerData;
import com.mraof.minestuck.world.storage.PlayerSavedData;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.text.TranslationTextComponent;

import static com.mraof.minestuck.player.EnumAspect.HOPE;

public class UserEffectPacket implements PlayToServerPacket
{
	//public static final String ON = "minestuck.aspect_effects.on";
	//public static final String OFF = "minestuck.aspect_effects.off";
	
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
		if(data.effectToggle())
		{
			int rung = data.getEcheladder().getRung();
			EnumAspect aspect = data.getTitle().getHeroAspect();
			int potionLevel = (int) (aspectStrength[aspect.ordinal()] * rung); //Blood, Breath, Doom, Heart, Hope, Life, Light, Mind, Rage, Space, Time, Void
			
			if(player.getEntityWorld().getGameTime() % 380 == 0)
			{
				if(rung > 18 && aspect == HOPE)
				{
					player.addPotionEffect(new EffectInstance(Effects.WATER_BREATHING, 600, 0));
				}
				
				if(potionLevel > 0)
				{
					player.addPotionEffect(new EffectInstance(aspectEffects[aspect.ordinal()], 600, potionLevel - 1));
					LOGG.debug("Applied aspect potion effect to {}", player.getDisplayName().getFormattedText());
				}
			}
		}
		
		PlayerData data = PlayerSavedData.getData(player);
		data.effectToggle(!data.effectToggle());
		if(data.effectToggle())
		{
			player.sendStatusMessage(new TranslationTextComponent(ON), true);
		} else
		{
			player.sendStatusMessage(new TranslationTextComponent(OFF), true);
		}
	}
}