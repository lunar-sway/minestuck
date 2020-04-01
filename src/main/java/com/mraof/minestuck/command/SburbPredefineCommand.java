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
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class SburbPredefineCommand
{
	public static final String SET_TITLE = "commands.minestuck.sburbpredefine.set_title";
	public static final String SET_TERRAIN_LAND = "commands.minestuck.sburbpredefine.set_terrain_land";
	public static final String SET_TITLE_LAND = "commands.minestuck.sburbpredefine.set_title_land";
	public static final String DEFINE = "commands.minestuck.sburbpredefine.define";
	private static final DynamicCommandExceptionType ANY_FAILURE = new DynamicCommandExceptionType(o -> (ITextComponent) o);
	
	public static void register(CommandDispatcher<CommandSource> dispatcher)
	{
		dispatcher.register(Commands.literal("sburbpredefine").requires(source -> source.hasPermissionLevel(2)).then(subCommandTitle()).then(subCommandTerrainLand()).then(subCommandTitleLand()).then(subCommandDefine()));
	}
	
	private static ArgumentBuilder<CommandSource, ?> subCommandName()
	{
		return null;
	}
	
	private static ArgumentBuilder<CommandSource, ?> subCommandAdd()
	{
		return null;
	}
	
	private static ArgumentBuilder<CommandSource, ?> subCommandFinish()
	{
		return null;
	}
	
	private static ArgumentBuilder<CommandSource, ?> subCommandTitle()
	{
		return Commands.literal("title").then(Commands.argument("player", EntityArgument.player()).then(Commands.argument("title", TitleArgument.title())
				.executes(context -> setTitle(context.getSource(), EntityArgument.getPlayer(context, "player"), TitleArgument.get(context, "title")))));
	}
	
	private static ArgumentBuilder<CommandSource, ?> subCommandTerrainLand()
	{
		return Commands.literal("terrain_land").then(Commands.argument("player", EntityArgument.player()).then(Commands.argument("land", TerrainLandTypeArgument.terrainLandType())
				.executes(context -> setTerrainLand(context.getSource(), EntityArgument.getPlayer(context, "player"), TerrainLandTypeArgument.get(context, "land")))));
	}
	
	private static ArgumentBuilder<CommandSource, ?> subCommandTitleLand()
	{
		return Commands.literal("title_land").then(Commands.argument("player", EntityArgument.player()).then(Commands.argument("land", TitleLandTypeArgument.titleLandType())
				.executes(context -> setTitleLand(context.getSource(), EntityArgument.getPlayer(context, "player"), TitleLandTypeArgument.get(context, "land")))));
	}
	
	private static ArgumentBuilder<CommandSource, ?> subCommandDefine()
	{
		return Commands.literal("define").then(Commands.argument("player", EntityArgument.player()).then(Commands.argument("title", TitleArgument.title())
				.then(Commands.argument("title_land", TitleLandTypeArgument.titleLandType()).then(Commands.argument("terrain_land", TerrainLandTypeArgument.terrainLandType())
				.executes(context -> define(context.getSource(), EntityArgument.getPlayer(context, "player"), TitleArgument.get(context, "title"), TerrainLandTypeArgument.get(context, "terrain_land"), TitleLandTypeArgument.get(context, "title_land")))))));
	}
	
	private static int setTitle(CommandSource source, ServerPlayerEntity player, Title title) throws CommandSyntaxException
	{
		try
		{
			SburbHandler.handlePredefineData(player, data -> data.predefineTitle(title, source));
			source.sendFeedback(new TranslationTextComponent(SET_TITLE, player.getDisplayName(), title.asTextComponent()), true);
			return 1;
		} catch(SkaianetException e)
		{
			throw ANY_FAILURE.create(e.getTextComponent());
		}
	}
	
	private static int setTerrainLand(CommandSource source, ServerPlayerEntity player, TerrainLandType landType) throws CommandSyntaxException
	{
		try
		{
			SburbHandler.handlePredefineData(player, data -> data.predefineTerrainLand(landType, source));
			source.sendFeedback(new TranslationTextComponent(SET_TERRAIN_LAND, player.getDisplayName()), true);
			return 1;
		} catch(SkaianetException e)
		{
			throw ANY_FAILURE.create(e.getTextComponent());
		}
	}
	
	private static int setTitleLand(CommandSource source, ServerPlayerEntity player, TitleLandType landType) throws CommandSyntaxException
	{
		try
		{
			SburbHandler.handlePredefineData(player, data -> data.predefineTitleLand(landType, source));
			source.sendFeedback(new TranslationTextComponent(SET_TITLE_LAND, player.getDisplayName()), true);
			return 1;
		} catch(SkaianetException e)
		{
			throw ANY_FAILURE.create(e.getTextComponent());
		}
	}
	
	private static int define(CommandSource source, ServerPlayerEntity player, Title title, TerrainLandType terrainLand, TitleLandType titleLand) throws CommandSyntaxException
	{
		CommandSource silentSource = source.withFeedbackDisabled();
		setTitle(silentSource, player, title);
		setTitleLand(silentSource, player, titleLand);
		setTerrainLand(silentSource, player, terrainLand);
		source.sendFeedback(new TranslationTextComponent(DEFINE, player.getDisplayName()), true);
		return 1;
	}
}