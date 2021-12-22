package com.mraof.minestuck.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mraof.minestuck.player.Echeladder;
import com.mraof.minestuck.world.storage.PlayerSavedData;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Collection;

public class SetRungCommand
{
	public static final String SUCCESS = "commands.minestuck.set_rung";
	
	public static void register(CommandDispatcher<CommandSource> dispatcher)
	{
		dispatcher.register(Commands.literal("setrung").requires(source -> source.hasPermission(2)).then(Commands.argument("target", EntityArgument.players())
				.then(Commands.argument("rung", IntegerArgumentType.integer(0, Echeladder.RUNG_COUNT - 1)).executes(context -> setRung(context.getSource(), EntityArgument.getPlayers(context, "target"), IntegerArgumentType.getInteger(context, "rung"), 0))
				.then(Commands.argument("progress", DoubleArgumentType.doubleArg(0, 1)).executes(context -> setRung(context.getSource(), EntityArgument.getPlayers(context, "target"), IntegerArgumentType.getInteger(context, "rung"), DoubleArgumentType.getDouble(context, "progress")))))));
	}
	
	private static int setRung(CommandSource source, Collection<ServerPlayerEntity> players, int rung, double progress)
	{
		for(ServerPlayerEntity player : players)
		{
			PlayerSavedData.getData(player).getEcheladder().setByCommand(rung, progress);
		}
		
		source.sendSuccess(new TranslationTextComponent(SUCCESS, players.size(), rung, progress), true);
		return players.size();
	}
}