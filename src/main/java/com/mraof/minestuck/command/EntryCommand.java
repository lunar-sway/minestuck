package com.mraof.minestuck.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mraof.minestuck.entry.EntryProcess;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;

public class EntryCommand
{
	public static void register(CommandDispatcher<CommandSourceStack> dispatcher)
	{
		dispatcher.register(Commands.literal("enter").requires(c -> c.hasPermission(Commands.LEVEL_GAMEMASTERS))
				.then(Commands.argument("player", EntityArgument.player())
								.then(Commands.argument("pos", BlockPosArgument.blockPos())
										.executes(ctx -> enter(EntityArgument.getPlayer(ctx, "player"), BlockPosArgument.getLoadedBlockPos(ctx, "pos"))))
								.executes(ctx -> enter(EntityArgument.getPlayer(ctx, "player"))))
				.then(Commands.argument("pos", BlockPosArgument.blockPos())
						.executes(ctx -> enter(ctx.getSource().getPlayerOrException(), BlockPosArgument.getLoadedBlockPos(ctx, "pos"))))
				.executes(ctx -> enter(ctx.getSource().getPlayerOrException()))
		);
	}
	
	private static int enter(ServerPlayer player)
	{
		EntryProcess.onArtifactActivated(player);
		return 0;
	}
	
	private static int enter(ServerPlayer player, BlockPos pos)
	{
		EntryProcess.onArtifactActivated(player, pos);
		return 0;
	}
}
