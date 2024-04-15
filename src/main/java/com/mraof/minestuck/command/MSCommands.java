package com.mraof.minestuck.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mraof.minestuck.Minestuck;
import net.minecraft.commands.CommandSourceStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

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
		RungCommand.register(dispatcher);
		PorkhollowCommand.register(dispatcher);
		DebugLandsCommand.register(dispatcher);
		EntryCommand.register(dispatcher);
		ReviewDialogueCommand.register(dispatcher, event.getBuildContext());
		SetDialogueCommand.register(dispatcher);
	}
}