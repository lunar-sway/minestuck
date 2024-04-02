package com.mraof.minestuck.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mraof.minestuck.alchemy.GristHelper;
import com.mraof.minestuck.api.alchemy.NonNegativeGristSet;
import com.mraof.minestuck.command.argument.GristSetArgument;
import com.mraof.minestuck.player.GristCache;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.skaianet.SburbPlayerData;
import com.mraof.minestuck.skaianet.SessionHandler;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class SendGristCommand
{
	public static final String SUCCESS = "commands.minestuck.send_grist.success";
	public static final String RECEIVE = "commands.minestuck.send_grist.receive";
	public static final String NOT_PERMITTED = "commands.minestuck.send_grist.not_permitted";
	public static final String CANT_AFFORD = "commands.minestuck.send_grist.cant_afford";
	private static final DynamicCommandExceptionType PERMISSION_EXCEPTION = new DynamicCommandExceptionType(o -> Component.translatable(NOT_PERMITTED, o));
	private static final DynamicCommandExceptionType CANT_AFFORD_EXCEPTION = new DynamicCommandExceptionType(o -> Component.translatable(CANT_AFFORD, o));
	
	public static void register(CommandDispatcher<CommandSourceStack> dispatcher)
	{
		dispatcher.register(Commands.literal("sendgrist").then(Commands.argument("target", EntityArgument.player()).then(Commands.argument("grist", GristSetArgument.nonNegativeSet())
				.executes(context -> execute(context.getSource(), EntityArgument.getPlayer(context, "target"), GristSetArgument.getNonNegativeGristArgument(context, "grist"))))));
	}
	
	private static int execute(CommandSourceStack source, ServerPlayer target, NonNegativeGristSet grist) throws CommandSyntaxException
	{
		ServerPlayer player = source.getPlayerOrException();
		if(isPermittedFor(player, target))
		{
			if(GristCache.get(player).tryTake(grist, GristHelper.EnumSource.SENDGRIST))
			{
				GristCache.get(target).addWithGutter(grist, GristHelper.EnumSource.SENDGRIST);
				source.sendSuccess(() -> Component.translatable(SUCCESS, target.getDisplayName(), grist.asTextComponent()), true);
				target.sendSystemMessage(Component.translatable(RECEIVE, player.getDisplayName(), grist.asTextComponent()));
				return 1;
			} else throw CANT_AFFORD_EXCEPTION.create(grist);
		} else throw PERMISSION_EXCEPTION.create(target.getDisplayName());
	}
	
	private static boolean isPermittedFor(ServerPlayer player, ServerPlayer player2)
	{
		
		if(!SburbPlayerData.get(player).hasEntered() || !SburbPlayerData.get(player2).hasEntered())
			return false;
		
		SessionHandler sessionHandler = SessionHandler.get(player.server);
		return sessionHandler.isInSameSession(IdentifierHandler.encode(player), IdentifierHandler.encode(player2));
	}
}