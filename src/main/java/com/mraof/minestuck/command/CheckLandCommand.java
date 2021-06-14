package com.mraof.minestuck.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mraof.minestuck.world.MSDimensions;
import com.mraof.minestuck.world.lands.LandInfo;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class CheckLandCommand
{
	public static final String CHECK = "commands.minestuck.check_land";
	public static final String FAIL = "commands.minestuck.check_land.fail";
	private static final SimpleCommandExceptionType FAIL_EXCEPTION = new SimpleCommandExceptionType(new TranslationTextComponent(FAIL));
	
	public static void register(CommandDispatcher<CommandSource> dispatcher)
	{
		dispatcher.register(Commands.literal("checkland").executes(context -> execute(context.getSource())));
	}
	
	private static int execute(CommandSource source) throws CommandSyntaxException
	{
		ServerPlayerEntity player = source.getPlayerOrException();
		
		if(MSDimensions.isLandDimension(player.level.dimension()))
		{
			LandInfo info = MSDimensions.getLandInfo(player.level);
			ITextComponent toSend = new TranslationTextComponent(CHECK, info.landAsTextComponent());
			source.sendSuccess(toSend, false);
			return 1;
		}
		else
		{
			throw FAIL_EXCEPTION.create();
		}
	}
}