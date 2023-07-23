package com.mraof.minestuck.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mraof.minestuck.blockentity.TransportalizerBlockEntity;
import com.mraof.minestuck.util.Teleport;
import com.mraof.minestuck.world.storage.TransportalizerSavedData;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;

import java.util.Collection;
import java.util.Collections;
import java.util.Locale;

public class TransportalizerCommand
{
	public static final String NOT_FOUND = "commands.minestuck.tpz.not_found";
	public static final String FAILURE = "commands.minestuck.tpz.failure";
	public static final String RESULT = "commands.minestuck.tpz.result";
	public static final String FAILURE_RESULT = "commands.minestuck.tpz.failure_result";
	private static final DynamicCommandExceptionType NOT_FOUND_EXCEPTION = new DynamicCommandExceptionType(o -> Component.translatable(NOT_FOUND, o));
	private static final SimpleCommandExceptionType BLOCKED_EXCEPTION = new SimpleCommandExceptionType(Component.translatable(TransportalizerBlockEntity.BLOCKED_DESTINATION));
	private static final SimpleCommandExceptionType RESULT_EXCEPTION = new SimpleCommandExceptionType(Component.translatable(FAILURE_RESULT));
	
	public static void register(CommandDispatcher<CommandSourceStack> dispatcher)
	{
		dispatcher.register(Commands.literal("tpz").requires(commandSource -> commandSource.hasPermission(2))
				.then(Commands.argument("code", StringArgumentType.word()).executes(context -> teleport(context.getSource(), Collections.singleton(context.getSource().getEntityOrException()), StringArgumentType.getString(context, "code")))
				.then(Commands.argument("targets", EntityArgument.entities()).executes(context -> teleport(context.getSource(), EntityArgument.getEntities(context, "targets"), StringArgumentType.getString(context, "code"))))));
	}
	
	private static int teleport(CommandSourceStack source, Collection<? extends Entity> entities, String code) throws CommandSyntaxException
	{
		//TODO allow the command to accept special characters like "♥" which can be used as valid transportalizer codes
		code = code.toUpperCase(Locale.ROOT); //prevents case sensitivity issues since actual transportalizer codes are caps only
		
		GlobalPos destination = TransportalizerSavedData.get(source.getServer()).get(code);
		if(destination == null)
			throw NOT_FOUND_EXCEPTION.create(code);
		
		ServerLevel level = source.getServer().getLevel(destination.dimension());
		
		if(level == null || !(level.getBlockEntity(destination.pos()) instanceof TransportalizerBlockEntity))
			throw NOT_FOUND_EXCEPTION.create(code);
		
		if(TransportalizerBlockEntity.isBlocked(level, destination.pos()))
			throw BLOCKED_EXCEPTION.create();
		
		int count = 0;
		for(Entity entity : entities)
		{
			Entity newEntity = Teleport.teleportEntity(entity, level, destination.pos().getX() + 0.5, destination.pos().getY() + 0.6, destination.pos().getZ() + 0.5, entity.getYRot(), entity.getXRot());
			if(newEntity != null)
			{
				newEntity.setPortalCooldown();
				count++;
			} else source.sendFailure(Component.translatable(FAILURE, entity.getDisplayName()));
		}
		
		if(count == 0)
			throw RESULT_EXCEPTION.create();
		else
		{
			int finalCount = count;
			source.sendSuccess(() -> Component.translatable(RESULT, finalCount), true);
		}
		
		return count;
	}
}