package com.mraof.minestuck.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mraof.minestuck.world.lands.GristLayerInfo;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

import java.util.Optional;

public class GristLayerCommand
{
	public static final String FAIL = CheckLandCommand.FAIL;
	private static final SimpleCommandExceptionType FAIL_EXCEPTION = new SimpleCommandExceptionType(Component.translatable(FAIL));
	
	public static void register(CommandDispatcher<CommandSourceStack> dispatcher)
	{
		dispatcher.register(Commands.literal("gristlayers").requires(source -> source.hasPermission(2)).executes(context -> execute(context.getSource())));
	}
	
	private static int execute(CommandSourceStack source) throws CommandSyntaxException
	{
		ServerPlayer player = source.getPlayerOrException();
		
		Optional<GristLayerInfo> optionalInfo = GristLayerInfo.get((ServerLevel) player.level());
		if(optionalInfo.isPresent())
		{
			source.sendSuccess(() -> optionalInfo.get().getGristLayerInfo(player.blockPosition().getX(), player.blockPosition().getZ()), false);
			return 1;
		} else
		{
			throw FAIL_EXCEPTION.create();
		}
	}
}