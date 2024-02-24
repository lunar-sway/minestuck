package com.mraof.minestuck.util;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.computer.editmode.EditToolsCapabilityProvider;
import com.mraof.minestuck.computer.editmode.IEditTools;
import com.mraof.minestuck.fluid.MSFluidType;
import com.mraof.minestuck.inventory.musicplayer.IMusicPlaying;
import com.mraof.minestuck.inventory.musicplayer.MusicPlayingCapabilityProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID)
public class MSCapabilities
{
	public static final Capability<IMusicPlaying> MUSIC_PLAYING_CAPABILITY = CapabilityManager.get(new CapabilityToken<>()
	{
	});
	
	public static final Capability<IEditTools> EDIT_TOOLS_CAPABILITY = CapabilityManager.get(new CapabilityToken<>()
	{
	});
	
	public static final Capability<MSFluidType.LastFluidTickData> LAST_FLUID_TICK = CapabilityManager.get(new CapabilityToken<>(){});
	
	public static void register(RegisterCapabilitiesEvent event)
	{
		event.register(IMusicPlaying.class);
		event.register(IEditTools.class);
		event.register(MSFluidType.LastFluidTickData.class);
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
			attachCapabilitiesEvent.addCapability(Minestuck.id("musicplaying"),
					new MusicPlayingCapabilityProvider());
			attachCapabilitiesEvent.addCapability(Minestuck.id("edit_tools"),
					new EditToolsCapabilityProvider());
		}
		
		attachCapabilitiesEvent.addCapability(Minestuck.id("last_fluid_tick"),
				new ICapabilityProvider()
				{
					private final LazyOptional<MSFluidType.LastFluidTickData> lazyOptional = LazyOptional.of(() -> this.data);
					private final MSFluidType.LastFluidTickData data = new MSFluidType.LastFluidTickData();
					
					@Override
					public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side)
					{
						return LAST_FLUID_TICK.orEmpty(cap, lazyOptional);
					}
				});
	}
}
