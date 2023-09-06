package com.mraof.minestuck.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mraof.minestuck.entry.EntryProcess;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class EntryCommand
{
	public static final String OTHER_PLAYER_ENTERING = "minestuck.entry.other_player_entering";
	public static final String ENTERING = "minestuck.entry.entering";
	
	public static void register(CommandDispatcher<CommandSourceStack> dispatcher)
	{
		dispatcher.register(Commands.literal("enter").requires(c -> c.hasPermission(Commands.LEVEL_GAMEMASTERS))
						.then(Commands.argument("player", EntityArgument.player())
										.then(Commands.argument("pos", BlockPosArgument.blockPos())
												// other player at pos
												.executes(ctx -> enter(
														ctx.getSource(),
														EntityArgument.getPlayer(ctx, "player"),
														BlockPosArgument.getLoadedBlockPos(ctx, "pos")))
										)
								// other player
								.executes(ctx -> enter(ctx.getSource(), EntityArgument.getPlayer(ctx, "player")))
						)
						.then(Commands.argument("pos", BlockPosArgument.blockPos())
								// self at pos
								.executes(ctx -> enter(
										ctx.getSource(),
										ctx.getSource().getPlayerOrException(),
										BlockPosArgument.getLoadedBlockPos(ctx, "pos")))
						)
				// self
				.executes(ctx -> enter(ctx.getSource(), ctx.getSource().getPlayerOrException()))
		);
	}
	
	private static int enter(CommandSourceStack src, ServerPlayer player) throws CommandSyntaxException
	{
		if (src.getEntity() == null || !player.equals(src.getPlayerOrException()))
		{
			src.sendSuccess(() -> Component.translatable(OTHER_PLAYER_ENTERING, player.getDisplayName().getString()), false);
		} else {
			src.sendSuccess(() -> Component.translatable(ENTERING), false);
		}
		EntryProcess.enter(player);
		return 1;
	}
	
	private static int enter(CommandSourceStack src, ServerPlayer player, BlockPos pos) throws CommandSyntaxException
	{
		if (src.getEntity() == null || !player.equals(src.getPlayerOrException()))
		{
			src.sendSuccess(() -> Component.translatable(OTHER_PLAYER_ENTERING, player.getDisplayName().getString()), false);
		} else {
			src.sendSuccess(() -> Component.translatable(ENTERING), false);
		}
		EntryProcess.enter(player, pos);
		return 1;
	}
}
