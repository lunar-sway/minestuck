package com.mraof.minestuck.util;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.computer.editmode.EditToolsCapabilityProvider;
import com.mraof.minestuck.computer.editmode.IEditTools;
import com.mraof.minestuck.inventory.musicplayer.IMusicPlaying;
import com.mraof.minestuck.inventory.musicplayer.MusicPlayingCapabilityProvider;
import com.mraof.minestuck.network.MusicPlayerPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID)
public class MSCapabilities
{
	public static final Capability<IMusicPlaying> MUSIC_PLAYING_CAPABILITY = CapabilityManager.get(new CapabilityToken<>()
	{
	});
	
	public static final Capability<IEditTools> EDIT_TOOLS_CAPABILITY = CapabilityManager.get(new CapabilityToken<>()
	{
	});
	
	public static void register(RegisterCapabilitiesEvent event)
	{
		event.register(IMusicPlaying.class);
		event.register(IEditTools.class);
	}
	
	/**
	 * Attaches a provider of the music playing and edit tools capability to any player
	 *
	 * @see MusicPlayingCapabilityProvider
	 * @see EditToolsCapabilityProvider
	 */
	@SubscribeEvent
	public static void entityAttachCapabilitiesEvent(AttachCapabilitiesEvent<Entity> attachCapabilitiesEvent)
	{
		if(attachCapabilitiesEvent.getObject() instanceof Player)
		{
			attachCapabilitiesEvent.addCapability(new ResourceLocation(Minestuck.MOD_ID, "musicplaying"),
					new MusicPlayingCapabilityProvider());
			attachCapabilitiesEvent.addCapability(new ResourceLocation(Minestuck.MOD_ID, "edittools"),
					new EditToolsCapabilityProvider());
		}
	}
}
