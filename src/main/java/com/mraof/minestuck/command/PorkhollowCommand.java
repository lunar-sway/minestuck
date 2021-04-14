package com.mraof.minestuck.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.LongArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mraof.minestuck.item.BoondollarsItem;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.world.storage.PlayerSavedData;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Util;
import net.minecraft.util.text.TranslationTextComponent;

public class PorkhollowCommand    //Much like /gristSend and /land, is a temporary command until a proper feature is in place
{
	public static final String SEND = "commands.minestuck.porkhollow.send";
	public static final String RECEIVE = "commands.minestuck.porkhollow.receive";
	public static final String TAKE = "commands.minestuck.porkhollow.take";
	public static final String INSUFFICIENT = "commands.minestuck.porkhollow.insufficient";
	private static final SimpleCommandExceptionType NOT_ENOUGH = new SimpleCommandExceptionType(new TranslationTextComponent(INSUFFICIENT));
	
	public static void register(CommandDispatcher<CommandSource> dispatcher)
	{
		dispatcher.register(Commands.literal("porkhollow").then(createSend()).then(createTake()));
	}
	
	private static ArgumentBuilder<CommandSource, ?> createSend()
	{
		return Commands.literal("send").then(Commands.argument("target", EntityArgument.player())
				.then(Commands.argument("amount", LongArgumentType.longArg(0)).executes(context -> send(context.getSource(), EntityArgument.getPlayer(context, "target"), LongArgumentType.getLong(context, "amount")))));
	}
	
	private static ArgumentBuilder<CommandSource, ?> createTake()
	{
		return Commands.literal("take").then(Commands.argument("amount", IntegerArgumentType.integer(0)).executes(context -> take(context.getSource(), IntegerArgumentType.getInteger(context, "amount"))));
	}
	
	private static int send(CommandSource source, ServerPlayerEntity target, long amount) throws CommandSyntaxException
	{
		ServerPlayerEntity player = source.asPlayer();
		
		if(PlayerSavedData.getData(player).tryTakeBoondollars(amount))
		{
			PlayerSavedData.getData(target).addBoondollars(amount);
			source.sendFeedback(new TranslationTextComponent(SEND, amount, target.getDisplayName()), true);
			target.sendMessage(new TranslationTextComponent(RECEIVE, amount, player.getDisplayName()), Util.DUMMY_UUID);
			return 1;
		} else throw NOT_ENOUGH.create();
	}
	
	private static int take(CommandSource source, int amount) throws CommandSyntaxException
	{
		ServerPlayerEntity player = source.asPlayer();
		
		if(PlayerSavedData.getData(player).tryTakeBoondollars(amount))
		{
			ItemStack stack = BoondollarsItem.setCount(new ItemStack(MSItems.BOONDOLLARS), amount);
			if(!player.addItemStackToInventory(stack))
			{
				ItemEntity entity = player.dropItem(stack, false);
				if (entity != null)
					entity.setNoPickupDelay();
			}
			
			source.sendFeedback(new TranslationTextComponent(TAKE, amount), true);
			return 1;
		} else throw NOT_ENOUGH.create();
	}
}