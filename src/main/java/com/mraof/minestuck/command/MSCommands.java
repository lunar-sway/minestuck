package com.mraof.minestuck.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mraof.minestuck.Minestuck;
import net.minecraft.command.CommandSource;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;

@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus=Mod.EventBusSubscriber.Bus.FORGE)
public class MSCommands
{
	@SubscribeEvent
	public static void serverStarting(FMLServerStartingEvent event)
	{
		CommandDispatcher<CommandSource> dispatcher = event.getCommandDispatcher();
		
		CheckLandCommand.register(dispatcher);
		GristLayerCommand.register(dispatcher);
		GristCommand.register(dispatcher);
		SendGristCommand.register(dispatcher);
		TransportalizerCommand.register(dispatcher);
		SburbPredefineCommand.register(dispatcher);
		SburbConnectionCommand.register(dispatcher);
		SetRungCommand.register(dispatcher);
		ConsortReplyCommand.register(dispatcher);
		PorkhollowCommand.register(dispatcher);
		DebugLandsCommand.register(dispatcher);
	}
}