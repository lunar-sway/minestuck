package com.mraof.minestuck.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mraof.minestuck.Minestuck;
import net.minecraft.commands.CommandSourceStack;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus=Mod.EventBusSubscriber.Bus.FORGE)
public class MSCommands
{
	@SubscribeEvent
	public static void serverStarting(RegisterCommandsEvent event)
	{
		CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
		
		CheckLandCommand.register(dispatcher);
		GristLayerCommand.register(dispatcher);
		GristCommand.register(dispatcher);
		SendGristCommand.register(dispatcher);
		GutterCommand.register(dispatcher);
		TransportalizerCommand.register(dispatcher);
		SburbPredefineCommand.register(dispatcher);
		SburbConnectionCommand.register(dispatcher);
		SetRungCommand.register(dispatcher);
		ConsortReplyCommand.register(dispatcher);
		PorkhollowCommand.register(dispatcher);
		DebugLandsCommand.register(dispatcher);
		EntryCommand.register(dispatcher);
	}
}