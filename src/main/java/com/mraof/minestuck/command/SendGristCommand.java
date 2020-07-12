package com.mraof.minestuck.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mraof.minestuck.command.argument.GristSetArgument;
import com.mraof.minestuck.item.crafting.alchemy.GristHelper;
import com.mraof.minestuck.item.crafting.alchemy.NonNegativeGristSet;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.player.PlayerIdentifier;
import com.mraof.minestuck.skaianet.SburbConnection;
import com.mraof.minestuck.skaianet.SessionHandler;
import com.mraof.minestuck.skaianet.SkaianetHandler;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.TranslationTextComponent;

public class SendGristCommand
{
	public static final String SUCCESS = "commands.minestuck.send_grist.success";
	public static final String NOT_PERMITTED = "commands.minestuck.send_grist.not_permitted";
	public static final String CANT_AFFORD = "commands.minestuck.send_grist.cant_afford";
	private static final DynamicCommandExceptionType PERMISSION_EXCEPTION = new DynamicCommandExceptionType(o -> new TranslationTextComponent(NOT_PERMITTED, o));
	private static final DynamicCommandExceptionType CANT_AFFORD_EXCEPTION = new DynamicCommandExceptionType(o -> new TranslationTextComponent(CANT_AFFORD, o));
	
	public static void register(CommandDispatcher<CommandSource> dispatcher)
	{
		dispatcher.register(Commands.literal("sendgrist").then(Commands.argument("target", EntityArgument.player()).then(Commands.argument("grist", GristSetArgument.nonNegativeSet())
				.executes(context -> execute(context.getSource(), EntityArgument.getPlayer(context, "target"), GristSetArgument.getNonNegativeGristArgument(context, "grist"))))));
	}
	
	private static int execute(CommandSource source, ServerPlayerEntity target, NonNegativeGristSet grist) throws CommandSyntaxException
	{
		ServerPlayerEntity player = source.asPlayer();
		if(isPermittedFor(player, target))
		{
			if(GristHelper.canAfford(player, grist))
			{
				GristHelper.decrease(player.world, IdentifierHandler.encode(player), grist);
				GristHelper.increase(player.world, IdentifierHandler.encode(target), grist);
				source.sendFeedback(new TranslationTextComponent(SUCCESS, target.getDisplayName(), grist.asTextComponent()), true);
				return 1;
			} else throw CANT_AFFORD_EXCEPTION.create(grist);
		} else throw PERMISSION_EXCEPTION.create(target.getDisplayName());
	}
	
	private static boolean isPermittedFor(ServerPlayerEntity player, ServerPlayerEntity player2)
	{
		PlayerIdentifier name1 = IdentifierHandler.encode(player), name2 = IdentifierHandler.encode(player2);
		SburbConnection c1 = SkaianetHandler.get(player.server).getMainConnection(name1, true);
		SburbConnection c2 = SkaianetHandler.get(player.server).getMainConnection(name2, true);
		if(c1 == null || c2 == null || !c1.hasEntered() || !c2.hasEntered())
			return false;
		else return SessionHandler.get(player.server).getPlayerSession(name1) == SessionHandler.get(player.server).getPlayerSession(name2);
	}
}