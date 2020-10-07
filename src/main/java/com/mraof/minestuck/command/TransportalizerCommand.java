package com.mraof.minestuck.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.tileentity.TransportalizerTileEntity;
import com.mraof.minestuck.util.Teleport;
import com.mraof.minestuck.world.storage.TransportalizerSavedData;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.server.ServerWorld;

import java.util.Collection;
import java.util.Collections;

public class TransportalizerCommand
{
	public static final String NOT_FOUND = "commands.minestuck.tpz.not_found";
	public static final String FAILURE = "commands.minestuck.tpz.failure";
	public static final String RESULT = "commands.minestuck.tpz.result";
	public static final String FAILURE_RESULT = "commands.minestuck.tpz.failure_result";
	private static final DynamicCommandExceptionType NOT_FOUND_EXCEPTION = new DynamicCommandExceptionType(o -> new TranslationTextComponent(NOT_FOUND, o));
	private static final SimpleCommandExceptionType BLOCKED_EXCEPTION = new SimpleCommandExceptionType(new TranslationTextComponent(TransportalizerTileEntity.BLOCKED_DESTINATION));
	private static final SimpleCommandExceptionType RESULT_EXCEPTION = new SimpleCommandExceptionType(new TranslationTextComponent(FAILURE_RESULT));
	
	public static void register(CommandDispatcher<CommandSource> dispatcher)
	{
		dispatcher.register(Commands.literal("tpz").requires(commandSource -> commandSource.hasPermissionLevel(2))
				.then(Commands.argument("code", StringArgumentType.word()).executes(context -> teleport(context.getSource(), Collections.singleton(context.getSource().assertIsEntity()), StringArgumentType.getString(context, "code")))
				.then(Commands.argument("targets", EntityArgument.entities()).executes(context -> teleport(context.getSource(), EntityArgument.getEntities(context, "targets"), StringArgumentType.getString(context, "code"))))));
	}
	
	private static int teleport(CommandSource source, Collection<? extends Entity> entities, String code) throws CommandSyntaxException
	{
		GlobalPos destination = TransportalizerSavedData.get(source.getServer()).get(code);
		if(destination == null)
			throw NOT_FOUND_EXCEPTION.create(code);
		
		ServerWorld world = source.getServer().getWorld(destination.getDimension());
		
		if(world == null || world.getBlockState(destination.getPos()).getBlock() != MSBlocks.TRANSPORTALIZER)
			throw NOT_FOUND_EXCEPTION.create(code);
		
		if(TransportalizerTileEntity.isBlocked(world, destination.getPos()))
			throw BLOCKED_EXCEPTION.create();
		
		int count = 0;
		for(Entity entity : entities)
		{
			Entity newEntity = Teleport.teleportEntity(entity, world, destination.getPos().getX() + 0.5, destination.getPos().getY() + 0.6, destination.getPos().getZ() + 0.5, entity.rotationYaw, entity.rotationPitch);
			if(newEntity != null)
			{
				newEntity.func_242279_ag(); //setPortalCooldown
				count++;
			} else source.sendErrorMessage(new TranslationTextComponent(FAILURE, entity.getDisplayName()));
		}
		
		if(count == 0)
			throw RESULT_EXCEPTION.create();
		else source.sendFeedback(new TranslationTextComponent(RESULT, count), true);
		
		return count;
	}
}