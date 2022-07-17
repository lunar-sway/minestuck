package com.mraof.minestuck.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mraof.minestuck.world.MSDimensions;
import com.mraof.minestuck.world.lands.LandInfo;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;

public class CheckLandCommand
{
	public static final String CHECK = "commands.minestuck.check_land";
	public static final String FAIL = "commands.minestuck.check_land.fail";
	private static final SimpleCommandExceptionType FAIL_EXCEPTION = new SimpleCommandExceptionType(new TranslatableComponent(FAIL));
	
	public static void register(CommandDispatcher<CommandSourceStack> dispatcher)
	{
		dispatcher.register(Commands.literal("checkland").executes(context -> execute(context.getSource())));
	}
	
	private static int execute(CommandSourceStack source) throws CommandSyntaxException
	{
		ServerPlayer player = source.getPlayerOrException();
		
		if(MSDimensions.isLandDimension(player.server, player.level.dimension()))
		{
			LandInfo info = MSDimensions.getLandInfo(player.level);
			Component toSend = new TranslatableComponent(CHECK, info.landAsTextComponent());
			source.sendSuccess(toSend, false);
			return 1;
		}
		else
		{
			throw FAIL_EXCEPTION.create();
		}
	}
}