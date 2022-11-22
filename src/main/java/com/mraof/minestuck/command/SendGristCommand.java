package com.mraof.minestuck.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mraof.minestuck.command.argument.GristSetArgument;
import com.mraof.minestuck.alchemy.GristHelper;
import com.mraof.minestuck.alchemy.NonNegativeGristSet;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.player.PlayerIdentifier;
import com.mraof.minestuck.skaianet.SburbConnection;
import com.mraof.minestuck.skaianet.SessionHandler;
import com.mraof.minestuck.skaianet.SkaianetHandler;
import net.minecraft.Util;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;

import java.util.Optional;

public class SendGristCommand
{
	public static final String SUCCESS = "commands.minestuck.send_grist.success";
	public static final String RECEIVE = "commands.minestuck.send_grist.receive";
	public static final String NOT_PERMITTED = "commands.minestuck.send_grist.not_permitted";
	public static final String CANT_AFFORD = "commands.minestuck.send_grist.cant_afford";
	private static final DynamicCommandExceptionType PERMISSION_EXCEPTION = new DynamicCommandExceptionType(o -> new TranslatableComponent(NOT_PERMITTED, o));
	private static final DynamicCommandExceptionType CANT_AFFORD_EXCEPTION = new DynamicCommandExceptionType(o -> new TranslatableComponent(CANT_AFFORD, o));
	
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
			if(GristHelper.canAfford(player, grist))
			{
				GristHelper.decrease(player.level, IdentifierHandler.encode(player), grist);
				GristHelper.increase(player.level, IdentifierHandler.encode(target), grist);
				source.sendSuccess(new TranslatableComponent(SUCCESS, target.getDisplayName(), grist.asTextComponent()), true);
				target.sendMessage(new TranslatableComponent(RECEIVE, player.getDisplayName(), grist.asTextComponent()), Util.NIL_UUID);
				return 1;
			} else throw CANT_AFFORD_EXCEPTION.create(grist);
		} else throw PERMISSION_EXCEPTION.create(target.getDisplayName());
	}
	
	private static boolean isPermittedFor(ServerPlayer player, ServerPlayer player2)
	{
		PlayerIdentifier name1 = IdentifierHandler.encode(player), name2 = IdentifierHandler.encode(player2);
		Optional<SburbConnection> c1 = SkaianetHandler.get(player.server).getPrimaryConnection(name1, true);
		Optional<SburbConnection> c2 = SkaianetHandler.get(player.server).getPrimaryConnection(name2, true);
		if(!c1.isPresent() || !c2.isPresent() || !c1.get().hasEntered() || !c2.get().hasEntered())
			return false;
		else return SessionHandler.get(player.server).getPlayerSession(name1) == SessionHandler.get(player.server).getPlayerSession(name2);
	}
}