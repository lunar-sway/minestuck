package com.mraof.minestuck.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mraof.minestuck.entry.EntryProcess;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;

public class EntryCommand
{
	public static void register(CommandDispatcher<CommandSourceStack> dispatcher)
	{
		dispatcher.register(Commands.literal("enter").requires(c -> c.hasPermission(Commands.LEVEL_GAMEMASTERS))
				.then(Commands.argument("player", EntityArgument.player())
								.then(Commands.argument("pos", BlockPosArgument.blockPos())
										.executes(ctx -> {
											EntryProcess.enter(EntityArgument.getPlayer(ctx, "player"), BlockPosArgument.getLoadedBlockPos(ctx, "pos"));
											return 1;
										}))
								.executes(ctx -> {
									EntryProcess.enter(EntityArgument.getPlayer(ctx, "player"));
									return 1;
								}))
				.then(Commands.argument("pos", BlockPosArgument.blockPos())
						.executes(ctx -> {
							EntryProcess.enter(ctx.getSource().getPlayerOrException(), BlockPosArgument.getLoadedBlockPos(ctx, "pos"));
							return 1;
						}))
				.executes(ctx -> {
					EntryProcess.enter(ctx.getSource().getPlayerOrException());
					return 1;
				})
		);
	}
}
