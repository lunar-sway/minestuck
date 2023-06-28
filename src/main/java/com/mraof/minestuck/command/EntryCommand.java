package com.mraof.minestuck.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mraof.minestuck.entry.EntryProcess;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;

public class EntryCommand
{
	public static void register(CommandDispatcher<CommandSourceStack> dispatcher)
	{
		dispatcher.register(
				Commands.literal("enter")
						.requires(c -> c.hasPermission(Commands.LEVEL_GAMEMASTERS))
						.then(
								Commands.argument("player", EntityArgument.player())
										.executes(ctx -> {
											EntryProcess.onArtifactActivated(EntityArgument.getPlayer(ctx, "player"));
											return 0;
										})
						)
						.executes(ctx -> {
							EntryProcess.onArtifactActivated(ctx.getSource().getPlayerOrException());
							return 0;
						})
		);
	}
}
