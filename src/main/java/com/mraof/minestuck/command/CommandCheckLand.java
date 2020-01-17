package com.mraof.minestuck.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mraof.minestuck.world.MSDimensions;
import com.mraof.minestuck.world.lands.LandInfo;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class CommandCheckLand
{
	public static final String CHECK = "commands.minestuck.check_land";
	public static final String FAIL = "commands.minestuck.check_land.fail";
	
	public static void register(CommandDispatcher<CommandSource> dispatcher)
	{
		dispatcher.register(Commands.literal("checkland").executes(context -> execute(context.getSource())));
	}
	
	private static int execute(CommandSource source) throws CommandSyntaxException
	{
		ServerPlayerEntity player = source.asPlayer();
		
		if(MSDimensions.isLandDimension(player.dimension))
		{
			LandInfo info = MSDimensions.getLandInfo(player.world);
			ITextComponent toSend = new TranslationTextComponent(CHECK, info.landAsTextComponent());
			source.sendFeedback(toSend, false);
		}
		else
		{
			source.sendFeedback(new TranslationTextComponent(FAIL), false);
		}
		return 1;
	}
}