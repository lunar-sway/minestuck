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
		dispatcher.register(
				Commands.literal("enter")
						.requires(c -> c.hasPermission(Commands.LEVEL_GAMEMASTERS))
						.then(
								Commands.argument("player", EntityArgument.player())
										.then(
												Commands.argument("pos", BlockPosArgument.blockPos())
														.executes(ctx -> {
															// both player and position specified
															EntryProcess.onArtifactActivated(
																	EntityArgument.getPlayer(ctx, "player"),
																	BlockPosArgument.getLoadedBlockPos(ctx, "pos")
															);
															return 0;
														})
										)
										.executes(ctx -> {
											// some other player at their position
											EntryProcess.onArtifactActivated(EntityArgument.getPlayer(ctx, "player"));
											return 0;
										})
						)
						.then(
								Commands.argument("pos", BlockPosArgument.blockPos())
										.executes(ctx -> {
											// position and self
											EntryProcess.onArtifactActivated(
													ctx.getSource().getPlayerOrException(),
													BlockPosArgument.getLoadedBlockPos(ctx, "pos"));
											return 0;
										})
						)
						.executes(ctx -> {
							// no arguments, just the self
							EntryProcess.onArtifactActivated(ctx.getSource().getPlayerOrException());
							return 0;
						})
		);
	}
}
