package com.mraof.minestuck.client.sounds;

import com.mraof.minestuck.block.EnumCassetteType;
import com.mraof.minestuck.network.MusicPlayerPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.EntityBoundSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * Keeps track of sound instances for music being played by nearby players.
 * These sound instances are created client-side upon receiving a MusicPlayerPacket.
 *
 * @see MusicPlayerPacket
 */

public class PlayerMusicClientHandler
{
	private static final Map<Integer, EntityBoundSoundInstance> entitiesMap = new HashMap<>();
	
	public static void handlePacket(int entityID, EnumCassetteType cassetteType, float volume, float pitch)
	{
		checkEntitiesInMap();
		
		Entity entity = Minecraft.getInstance().level.getEntity(entityID);
		if(entity instanceof Player)
		{
			SoundManager soundManager = Minecraft.getInstance().getSoundManager();
			
			if(entitiesMap.containsKey(entityID))
			{
				//TODO: if the sound instance was started the same tick, it fails to stop properly
				soundManager.stop(entitiesMap.remove(entityID));
			}
			if(cassetteType != EnumCassetteType.NONE)
			{
				EntityBoundSoundInstance soundInstance = new EntityBoundSoundInstance(
						cassetteType.getSoundEvent(), SoundSource.PLAYERS, volume, pitch, entity, 0);//TODO
				
				entitiesMap.put(entityID, soundInstance);
				soundManager.play(soundInstance);
			}
		}
	}
	
	private static void checkEntitiesInMap()
	{
		for(Integer key : entitiesMap.keySet())
		{
			Entity entity = Minecraft.getInstance().level.getEntity(key);
			if(entity == null)
			{
				entitiesMap.remove(key);
			}
		}
	}
}
