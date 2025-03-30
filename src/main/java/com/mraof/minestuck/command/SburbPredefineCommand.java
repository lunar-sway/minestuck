package com.mraof.minestuck.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mraof.minestuck.command.argument.TerrainLandTypeArgument;
import com.mraof.minestuck.command.argument.TitleArgument;
import com.mraof.minestuck.command.argument.TitleLandTypeArgument;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.player.PlayerIdentifier;
import com.mraof.minestuck.player.Title;
import com.mraof.minestuck.skaianet.PredefineData;
import com.mraof.minestuck.skaianet.SkaianetData;
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
	public static final String TOO_LATE = "commands.minestuck.sburbpredefine.too_late";
	
	private static final SimpleCommandExceptionType TOO_LATE_EXCEPTION = new SimpleCommandExceptionType(Component.translatable(TOO_LATE));
	
	public static void register(CommandDispatcher<CommandSourceStack> dispatcher)
	{
		dispatcher.register(Commands.literal("sburbpredefine").requires(source -> source.hasPermission(2)).then(subCommandTitle()).then(subCommandTerrainLand()).then(subCommandTitleLand()).then(subCommandDefine()));
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
		getPredefineData(player).predefineTitle(title);
		source.sendSuccess(() -> Component.translatable(SET_TITLE, player.getDisplayName(), title.asTextComponent()), true);
		return 1;
	}
	
	private static int setTerrainLand(CommandSourceStack source, ServerPlayer player, TerrainLandType landType) throws CommandSyntaxException
	{
		getPredefineData(player).predefineTerrainLand(landType, source);
		source.sendSuccess(() -> Component.translatable(SET_TERRAIN_LAND, player.getDisplayName()), true);
		return 1;
	}
	
	private static int setTitleLand(CommandSourceStack source, ServerPlayer player, TitleLandType landType) throws CommandSyntaxException
	{
		getPredefineData(player).predefineTitleLand(landType, source);
		source.sendSuccess(() -> Component.translatable(SET_TITLE_LAND, player.getDisplayName()), true);
		return 1;
	}
	
	private static int define(CommandSourceStack source, ServerPlayer player, Title title, TerrainLandType terrainLand, TitleLandType titleLand) throws CommandSyntaxException
	{
		CommandSourceStack silentSource = source.withSuppressedOutput();
		PredefineData predefineData = getPredefineData(player);
		predefineData.predefineTitle(title);
		predefineData.predefineTitleLand(titleLand, silentSource);
		predefineData.predefineTerrainLand(terrainLand, silentSource);
		source.sendSuccess(() -> Component.translatable(DEFINE, player.getDisplayName()), true);
		return 1;
	}
	
	private static PredefineData getPredefineData(ServerPlayer player) throws CommandSyntaxException
	{
		PlayerIdentifier playerId = IdentifierHandler.encode(player);
		return SkaianetData.get(player.server).getOrCreatePredefineData(playerId).orElseThrow(TOO_LATE_EXCEPTION::create);
	}
}