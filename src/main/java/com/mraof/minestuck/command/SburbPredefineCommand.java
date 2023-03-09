package com.mraof.minestuck.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mraof.minestuck.command.argument.TerrainLandTypeArgument;
import com.mraof.minestuck.command.argument.TitleArgument;
import com.mraof.minestuck.command.argument.TitleLandTypeArgument;
import com.mraof.minestuck.player.Title;
import com.mraof.minestuck.skaianet.SburbHandler;
import com.mraof.minestuck.skaianet.SkaianetException;
import com.mraof.minestuck.world.lands.terrain.TerrainLandType;
import com.mraof.minestuck.world.lands.title.TitleLandType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class SburbPredefineCommand
{
	public static final String SET_TITLE = "commands.minestuck.sburbpredefine.set_title";
	public static final String SET_TERRAIN_LAND = "commands.minestuck.sburbpredefine.set_terrain_land";
	public static final String SET_TITLE_LAND = "commands.minestuck.sburbpredefine.set_title_land";
	public static final String DEFINE = "commands.minestuck.sburbpredefine.define";
	private static final DynamicCommandExceptionType ANY_FAILURE = new DynamicCommandExceptionType(o -> (Component) o);
	
	public static void register(CommandDispatcher<CommandSourceStack> dispatcher)
	{
		dispatcher.register(Commands.literal("sburbpredefine").requires(source -> source.hasPermission(2)).then(subCommandTitle()).then(subCommandTerrainLand()).then(subCommandTitleLand()).then(subCommandDefine()));
	}
	
	/**
	 * Subcommand that sets the name of the session that a specified player is in
	 */
	private static ArgumentBuilder<CommandSourceStack, ?> subCommandName()
	{
		return null;
	}
	
	/**
	 * Subcommand that "adds" specified players to a session
	 */
	private static ArgumentBuilder<CommandSourceStack, ?> subCommandAdd()
	{
		return null;
	}
	
	/**
	 * Subcommand that makes appropriate predefines for all players in the session,
	 * and then locks the session to the players currently in the session
	 */
	private static ArgumentBuilder<CommandSourceStack, ?> subCommandFinish()
	{
		return null;
	}
	
	private static ArgumentBuilder<CommandSourceStack, ?> subCommandTitle()
	{
		return Commands.literal("title").then(Commands.argument("player", EntityArgument.player()).then(Commands.argument("title", TitleArgument.title())
				.executes(context -> setTitle(context.getSource(), EntityArgument.getPlayer(context, "player"), TitleArgument.get(context, "title")))));
	}
	
	private static ArgumentBuilder<CommandSourceStack, ?> subCommandTerrainLand()
	{
		return Commands.literal("terrain_land").then(Commands.argument("player", EntityArgument.player()).then(Commands.argument("land", TerrainLandTypeArgument.terrainLandType())
				.executes(context -> setTerrainLand(context.getSource(), EntityArgument.getPlayer(context, "player"), TerrainLandTypeArgument.get(context, "land")))));
	}
	
	private static ArgumentBuilder<CommandSourceStack, ?> subCommandTitleLand()
	{
		return Commands.literal("title_land").then(Commands.argument("player", EntityArgument.player()).then(Commands.argument("land", TitleLandTypeArgument.titleLandType())
				.executes(context -> setTitleLand(context.getSource(), EntityArgument.getPlayer(context, "player"), TitleLandTypeArgument.get(context, "land")))));
	}
	
	private static ArgumentBuilder<CommandSourceStack, ?> subCommandDefine()
	{
		return Commands.literal("define").then(Commands.argument("player", EntityArgument.player()).then(Commands.argument("title", TitleArgument.title())
				.then(Commands.argument("title_land", TitleLandTypeArgument.titleLandType()).then(Commands.argument("terrain_land", TerrainLandTypeArgument.terrainLandType())
				.executes(context -> define(context.getSource(), EntityArgument.getPlayer(context, "player"), TitleArgument.get(context, "title"), TerrainLandTypeArgument.get(context, "terrain_land"), TitleLandTypeArgument.get(context, "title_land")))))));
	}
	
	private static int setTitle(CommandSourceStack source, ServerPlayer player, Title title) throws CommandSyntaxException
	{
		try
		{
			SburbHandler.handlePredefineData(player, data -> data.predefineTitle(title, source));
			source.sendSuccess(Component.translatable(SET_TITLE, player.getDisplayName(), title.asTextComponent()), true);
			return 1;
		} catch(SkaianetException e)
		{
			throw ANY_FAILURE.create(e.getTextComponent());
		}
	}
	
	private static int setTerrainLand(CommandSourceStack source, ServerPlayer player, TerrainLandType landType) throws CommandSyntaxException
	{
		try
		{
			SburbHandler.handlePredefineData(player, data -> data.predefineTerrainLand(landType, source));
			source.sendSuccess(Component.translatable(SET_TERRAIN_LAND, player.getDisplayName()), true);
			return 1;
		} catch(SkaianetException e)
		{
			throw ANY_FAILURE.create(e.getTextComponent());
		}
	}
	
	private static int setTitleLand(CommandSourceStack source, ServerPlayer player, TitleLandType landType) throws CommandSyntaxException
	{
		try
		{
			SburbHandler.handlePredefineData(player, data -> data.predefineTitleLand(landType, source));
			source.sendSuccess(Component.translatable(SET_TITLE_LAND, player.getDisplayName()), true);
			return 1;
		} catch(SkaianetException e)
		{
			throw ANY_FAILURE.create(e.getTextComponent());
		}
	}
	
	private static int define(CommandSourceStack source, ServerPlayer player, Title title, TerrainLandType terrainLand, TitleLandType titleLand) throws CommandSyntaxException
	{
		CommandSourceStack silentSource = source.withSuppressedOutput();
		setTitle(silentSource, player, title);
		setTitleLand(silentSource, player, titleLand);
		setTerrainLand(silentSource, player, terrainLand);
		source.sendSuccess(Component.translatable(DEFINE, player.getDisplayName()), true);
		return 1;
	}
}