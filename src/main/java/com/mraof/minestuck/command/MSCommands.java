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
		GristCommand.register(dispatcher);
		SendGristCommand.register(dispatcher);
		CommandTransportalizer.register(dispatcher);
		CommandSburbSession.register(dispatcher);
		CommandSburbServer.register(dispatcher);
		SetRungCommand.register(dispatcher);
		ConsortReplyCommand.register(dispatcher);
		CommandToStructure.register(dispatcher);
		PorkhollowCommand.register(dispatcher);
		CommandLandDebug.register(dispatcher);
	}
}