package com.mraof.minestuck.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.LongArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mraof.minestuck.item.BoondollarsItem;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.player.PlayerBoondollars;
import com.mraof.minestuck.player.PlayerData;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;

public class PorkhollowCommand    //Much like /gristSend and /land, is a temporary command until a proper feature is in place
{
	public static final String SEND = "commands.minestuck.porkhollow.send";
	public static final String RECEIVE = "commands.minestuck.porkhollow.receive";
	public static final String TAKE = "commands.minestuck.porkhollow.take";
	public static final String INSUFFICIENT = "commands.minestuck.porkhollow.insufficient";
	private static final SimpleCommandExceptionType NOT_ENOUGH = new SimpleCommandExceptionType(Component.translatable(INSUFFICIENT));
	
	public static void register(CommandDispatcher<CommandSourceStack> dispatcher)
	{
		dispatcher.register(Commands.literal("porkhollow").then(createSend()).then(createTake()));
	}
	
	private static ArgumentBuilder<CommandSourceStack, ?> createSend()
	{
		return Commands.literal("send").then(Commands.argument("target", EntityArgument.player())
				.then(Commands.argument("amount", LongArgumentType.longArg(0)).executes(context -> send(context.getSource(), EntityArgument.getPlayer(context, "target"), LongArgumentType.getLong(context, "amount")))));
	}
	
	private static ArgumentBuilder<CommandSourceStack, ?> createTake()
	{
		return Commands.literal("take").then(Commands.argument("amount", IntegerArgumentType.integer(0)).executes(context -> take(context.getSource(), IntegerArgumentType.getInteger(context, "amount"))));
	}
	
	private static int send(CommandSourceStack source, ServerPlayer target, long amount) throws CommandSyntaxException
	{
		ServerPlayer player = source.getPlayerOrException();
		
		if(PlayerBoondollars.tryTakeBoondollars(PlayerData.get(player).orElseThrow(), amount))
		{
			PlayerBoondollars.addBoondollars(PlayerData.get(target).orElseThrow(), amount);
			source.sendSuccess(() -> Component.translatable(SEND, amount, target.getDisplayName()), true);
			target.sendSystemMessage(Component.translatable(RECEIVE, amount, player.getDisplayName()));
			return 1;
		} else throw NOT_ENOUGH.create();
	}
	
	private static int take(CommandSourceStack source, int amount) throws CommandSyntaxException
	{
		ServerPlayer player = source.getPlayerOrException();
		
		if(PlayerBoondollars.tryTakeBoondollars(PlayerData.get(player).orElseThrow(), amount))
		{
			ItemStack stack = BoondollarsItem.setCount(new ItemStack(MSItems.BOONDOLLARS.get()), amount);
			if(!player.addItem(stack))
			{
				ItemEntity entity = player.drop(stack, false);
				if (entity != null)
					entity.setNoPickUpDelay();
			}
			
			source.sendSuccess(() -> Component.translatable(TAKE, amount), true);
			return 1;
		} else throw NOT_ENOUGH.create();
	}
}