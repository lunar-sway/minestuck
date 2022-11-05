package com.mraof.minestuck.client.sounds;

import com.mraof.minestuck.block.EnumCassetteType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.EntityBoundSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

import java.util.HashMap;
import java.util.Map;

public class PlayerMusicClientHandler
{
	private static final Map<Integer, EntityBoundSoundInstance> entitiesMap = new HashMap<>();
	public static void sendPacket(int entityID, EnumCassetteType cassetteType)
	{
		Entity entity = Minecraft.getInstance().level.getEntity(entityID);
		if(entity instanceof Player)
		{
			SoundManager soundManager = Minecraft.getInstance().getSoundManager();
			
			if(getEntitiesMap().containsKey(entityID))
			{
				soundManager.stop(getEntitiesMap().remove(entityID));
			}
			if(cassetteType != EnumCassetteType.NONE)
			{
				EntityBoundSoundInstance soundInstance = new EntityBoundSoundInstance(
						cassetteType.getSoundEvent(), SoundSource.PLAYERS, 10.0F, 0.0F, entity);
				
				getEntitiesMap().put(entityID, soundInstance);
				soundManager.play(soundInstance);
			}
		}
	}
	
	public static Map<Integer, EntityBoundSoundInstance> getEntitiesMap()
	{
		return entitiesMap;
	}
}
