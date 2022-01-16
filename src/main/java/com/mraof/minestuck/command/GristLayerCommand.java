package com.mraof.minestuck.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mraof.minestuck.world.lands.GristLayerInfo;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Optional;

public class GristLayerCommand
{
	public static final String FAIL = CheckLandCommand.FAIL;
	private static final SimpleCommandExceptionType FAIL_EXCEPTION = new SimpleCommandExceptionType(new TranslationTextComponent(FAIL));
	
	public static void register(CommandDispatcher<CommandSource> dispatcher)
	{
		dispatcher.register(Commands.literal("gristlayers").requires(source -> source.hasPermission(2)).executes(context -> execute(context.getSource())));
	}
	
	private static int execute(CommandSource source) throws CommandSyntaxException
	{
		ServerPlayerEntity player = source.getPlayerOrException();
		
		Optional<GristLayerInfo> optionalInfo = GristLayerInfo.get(player.level.dimension());
		if(optionalInfo.isPresent())
		{
			ITextComponent layerInfo = optionalInfo.get().getGristLayerInfo(player.blockPosition().getX(), player.blockPosition().getZ());
			source.sendSuccess(layerInfo, false);
			return 1;
		} else
		{
			throw FAIL_EXCEPTION.create();
		}
	}
}