package com.mraof.minestuck.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mraof.minestuck.world.lands.LandTypePair;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

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
		LandTypePair.Named landTypes = LandTypePair.getNamed(source.getLevel()).orElseThrow(FAIL_EXCEPTION::create);
		
		Component toSend = new TranslatableComponent(CHECK, landTypes.asComponent());
		source.sendSuccess(toSend, false);
		return 1;
	}
}