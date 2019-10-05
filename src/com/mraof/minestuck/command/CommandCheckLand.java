package com.mraof.minestuck.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mraof.minestuck.world.MSDimensions;
import com.mraof.minestuck.world.lands.LandAspects;
import com.mraof.minestuck.world.lands.LandInfoContainer;
import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class CommandCheckLand
{
	public static void register(CommandDispatcher<CommandSource> dispatcher)
	{
		dispatcher.register(Commands.literal("checkland").executes(context -> execute(context.getSource())));
	}
	
	private static int execute(CommandSource source) throws CommandSyntaxException
	{
		ServerPlayerEntity player = source.asPlayer();
		
		if(MSDimensions.isLandDimension(player.dimension))
		{
			LandInfoContainer info = MSDimensions.getLandInfo(player.server, player.dimension);
			ITextComponent toSend = new TranslationTextComponent("land.message.check", info.landAsTextComponent());
			source.sendFeedback(toSend, false);
		}
		else
		{
			source.sendFeedback(new TranslationTextComponent("land.message.checkFail"), false);
		}
		return 1;
	}
}