package com.mraof.minestuck.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.command.argument.LandTypePairArgument;
import com.mraof.minestuck.command.argument.ListArgument;
import com.mraof.minestuck.entry.EntryProcess;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.player.PlayerIdentifier;
import com.mraof.minestuck.skaianet.SburbConnections;
import com.mraof.minestuck.skaianet.SburbPlayerData;
import com.mraof.minestuck.world.DynamicDimensions;
import com.mraof.minestuck.world.MSDimensions;
import com.mraof.minestuck.world.lands.LandTypePair;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Objects;

/**
 * A command that is useful for testing the land skybox as it creates debug lands that is connected to the user
 */
public class DebugLandsCommand
{
	public static final String SUCCESS = "commands.minestuck.debuglands.success";
	public static final String MUST_ENTER = "commands.minestuck.debuglands.must_enter";
	public static final SimpleCommandExceptionType MUST_ENTER_EXCEPTION = new SimpleCommandExceptionType(Component.translatable(MUST_ENTER));
	public static final String INVALID_CHAIN = "commands.minestuck.debuglands.invalid_chain";
	public static final SimpleCommandExceptionType INVALID_CHAIN_EXCEPTION = new SimpleCommandExceptionType(Component.translatable(INVALID_CHAIN));
	
	public static void register(CommandDispatcher<CommandSourceStack> dispatcher)
	{
		dispatcher.register(Commands.literal("debuglands").requires(source -> source.hasPermission(2))
				.then(Commands.argument("lands", ListArgument.list(LandTypePairArgument.nullablePairs()))
						.executes(context -> createDebugLands(context.getSource(), ListArgument.getListArgument(context, "lands")))));
	}
	
	private static int createDebugLands(CommandSourceStack source, List<LandTypePair> landTypes) throws CommandSyntaxException
	{
		ServerPlayer player = source.getPlayerOrException();
		PlayerIdentifier playerId = Objects.requireNonNull(IdentifierHandler.encode(player), "Command was executed on a fake player");
		int createdLands = createDebugLandsChain(playerId, landTypes, source.getServer());
		
		source.sendSuccess(() -> Component.translatable(SUCCESS, createdLands, player.getDisplayName()), true);
		return createdLands;
	}
	
	private static int createDebugLandsChain(PlayerIdentifier playerId, List<LandTypePair> landTypes, MinecraftServer mcServer) throws CommandSyntaxException
	{
		if(!SburbPlayerData.get(playerId, mcServer).hasEntered())
			throw MUST_ENTER_EXCEPTION.create();
		
		var connections = SburbConnections.get(mcServer);
		
		connections.unlinkClientPlayer(playerId);
		connections.unlinkServerPlayer(playerId);
		
		int openChainIndex = landTypes.indexOf(null);
		
		if(openChainIndex == -1)
		{
			List<LandEntry> landEntries = withFakePlayers(landTypes);
			
			if(!landEntries.isEmpty())
			{
				connections.setPrimaryConnection(playerId, landEntries.get(0).playerId);
				connections.setPrimaryConnection(landEntries.get(landEntries.size() - 1).playerId, playerId);
			}
			
			connectAndCreateLands(landEntries, connections, mcServer);
			
			MSDimensions.sendLandTypesToAll(mcServer);
			return landEntries.size();
		} else
		{
			if(landTypes.lastIndexOf(null) != openChainIndex)
				throw INVALID_CHAIN_EXCEPTION.create();
			
			List<LandEntry> landEntries1 = withFakePlayers(landTypes.subList(0, openChainIndex));
			List<LandEntry> landEntries2 = withFakePlayers(landTypes.subList(openChainIndex + 1, landTypes.size()));
			
			if(!landEntries1.isEmpty())
				connections.setPrimaryConnection(playerId, landEntries1.get(0).playerId);
			if(!landEntries2.isEmpty())
				connections.setPrimaryConnection(landEntries2.get(landEntries2.size() - 1).playerId, playerId);
			
			connectAndCreateLands(landEntries1, connections, mcServer);
			connectAndCreateLands(landEntries2, connections, mcServer);
			
			MSDimensions.sendLandTypesToAll(mcServer);
			return landEntries1.size() + landEntries2.size();
		}
	}
	
	private static void connectAndCreateLands(List<LandEntry> landEntries2, SburbConnections connections, MinecraftServer mcServer)
	{
		for(int i = 0; i < landEntries2.size() - 1; i++)
			connections.setPrimaryConnection(landEntries2.get(i).playerId, landEntries2.get(i + 1).playerId);
		for(LandEntry entry : landEntries2)
			createAndSetLand(entry.landTypes, entry.playerId, mcServer);
	}
	
	private record LandEntry(PlayerIdentifier playerId, LandTypePair landTypes)
	{
	}
	
	private static List<LandEntry> withFakePlayers(List<LandTypePair> landTypes)
	{
		return landTypes.stream().map(landType -> new LandEntry(IdentifierHandler.createNewFakeIdentifier(), landType)).toList();
	}
	
	private static final ResourceLocation DEBUG_LAND_BASE_ID = new ResourceLocation(Minestuck.MOD_ID, "debug_land");
	
	private static void createAndSetLand(LandTypePair landTypes, PlayerIdentifier player, MinecraftServer mcServer)
	{
		SburbConnections.get(mcServer).setPrimaryConnectionForEntry(player);
		SburbPlayerData playerData = SburbPlayerData.get(player, mcServer);
		ResourceKey<Level> dimensionName = DynamicDimensions.createLand(mcServer, DEBUG_LAND_BASE_ID, landTypes);
		playerData.setLand(dimensionName);
		playerData.setHasEntered();
		
		EntryProcess.placeGates(mcServer.getLevel(dimensionName));
	}
}