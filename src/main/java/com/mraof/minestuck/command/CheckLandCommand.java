package com.mraof.minestuck.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mraof.minestuck.world.lands.LandTypePair;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public class CheckLandCommand
{
	public static final String CHECK = "commands.minestuck.check_land";
	public static final String FAIL = "commands.minestuck.check_land.fail";
	private static final SimpleCommandExceptionType FAIL_EXCEPTION = new SimpleCommandExceptionType(Component.translatable(FAIL));
	
	public static void register(CommandDispatcher<CommandSourceStack> dispatcher)
	{
		dispatcher.register(Commands.literal("checkland").executes(context -> execute(context.getSource())));
	}
	
	private static int execute(CommandSourceStack source) throws CommandSyntaxException
	{
		LandTypePair.Named landTypes = LandTypePair.getNamed(source.getLevel()).orElseThrow(FAIL_EXCEPTION::create);
		
		source.sendSuccess(() -> Component.translatable(CHECK, landTypes.asComponent()), false);
		return 1;
	}
}