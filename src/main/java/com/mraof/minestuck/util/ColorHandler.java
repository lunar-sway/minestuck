package com.mraof.minestuck.util;

import com.mojang.datafixers.util.Pair;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.event.AlchemyEvent;
import com.mraof.minestuck.item.AlchemizedColored;
import com.mraof.minestuck.network.PlayerColorPackets;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.player.PlayerData;
import com.mraof.minestuck.player.PlayerIdentifier;
import com.mraof.minestuck.skaianet.SburbHandler;
import com.mraof.minestuck.skaianet.SburbPlayerData;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

import java.util.List;
import java.util.Objects;

/**
 * Stores the array with colors that the player picks from, and provides utility function to handle colors.
 */
@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class ColorHandler
{
	public static final class BuiltinColors
	{
		public static final int DEFAULT_COLOR = 0xA0DCFF;
		
		private static final List<Pair<Integer, String>> COLORS = List.of(
				Pair.of(0x0715cd, "blue"),
				Pair.of(0xb536da, "orchid"),
				Pair.of(0xe00707, "red"),
				Pair.of(0x4ac925, "green"),
				
				Pair.of(0x00d5f2, "cyan"),
				Pair.of(0xff6ff2, "pink"),
				Pair.of(0xf2a400, "orange"),
				Pair.of(0x1f9400, "emerald"),
				
				Pair.of(0xa10000, "rust"),
				Pair.of(0xa15000, "bronze"),
				Pair.of(0xa1a100, "gold"),
				Pair.of(0x626262, "iron"),
				Pair.of(0x416600, "olive"),
				Pair.of(0x008141, "jade"),
				Pair.of(0x008282, "teal"),
				Pair.of(0x005682, "cobalt"),
				Pair.of(0x000056, "indigo"),
				Pair.of(0x2b0057, "purple"),
				Pair.of(0x6a006a, "violet"),
				Pair.of(0x77003c, "fuchsia")
		);
		
		public static int getColor(int index)
		{
			if(index < 0 || index >= COLORS.size())
				return DEFAULT_COLOR;
			return COLORS.get(index).getFirst();
		}
		
		public static Component getName(int index)
		{
			if(index < 0 || index >= COLORS.size())
				return Component.literal("INVALID");
			return Component.translatable("minestuck.color." + COLORS.get(index).getSecond());
		}
	}
	
	@SubscribeEvent
	private static void onAlchemy(AlchemyEvent event)
	{
		ItemStack stack = event.getItemResult();
		if(stack.getItem() instanceof AlchemizedColored colored)
		{
			int color = getColorForPlayer(event.getPlayer(), event.getLevel());
			event.setItemResult(colored.setColor(stack, color));
		}
	}
	
	public static ItemStack setDefaultColor(ItemStack stack)
	{
		return setColor(stack, BuiltinColors.DEFAULT_COLOR);
	}
	
	public static ItemStack setColor(ItemStack stack, int color)
	{
		stack.getOrCreateTag().putInt("color", color);
		return stack;
	}
	
	public static int getColorFromStack(ItemStack stack)
	{
		if(stack.hasTag() && stack.getTag().contains("color", Tag.TAG_ANY_NUMERIC))
			return stack.getTag().getInt("color");
		else return BuiltinColors.DEFAULT_COLOR;
	}
	
	public static int getColorForDimension(ServerLevel level)
	{
		return SburbPlayerData.getForLand(level)
				.map(data -> getColorForPlayer(data.playerId(), level))
				.orElse(BuiltinColors.DEFAULT_COLOR);
	}
	
	public static int getColorForPlayer(ServerPlayer player)
	{
		return getColorForPlayer(IdentifierHandler.encode(player), player.level());
	}
	
	public static int getColorForPlayer(PlayerIdentifier identifier, Level level)
	{
		return PlayerData.get(identifier, level).getData(MSAttachments.PLAYER_COLOR);
	}
	
	public static void trySetPlayerColor(ServerPlayer player, int color)
	{
		PlayerIdentifier playerId = IdentifierHandler.encode(player);
		if(playerId == null || !SburbHandler.canSelectColor(playerId, player.server))
			return;
		
		PlayerData playerData = PlayerData.get(playerId, player.server);
		Integer prevColor = playerData.setData(MSAttachments.PLAYER_COLOR, color);
		
		if(!Objects.equals(prevColor, color))
			player.connection.send(new PlayerColorPackets.Data(color));
	}
	
	@SubscribeEvent
	private static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event)
	{
		ServerPlayer player = (ServerPlayer) event.getEntity();
		PlayerData playerData = PlayerData.get(player).orElseThrow();
		
		boolean firstTime = playerData.getExistingData(MSAttachments.PLAYER_COLOR).isEmpty();
		
		if(firstTime && !player.isSpectator())
		{
			playerData.setData(MSAttachments.PLAYER_COLOR, BuiltinColors.DEFAULT_COLOR);
			player.connection.send(new PlayerColorPackets.OpenSelection());
		} else
			player.connection.send(new PlayerColorPackets.Data(playerData.getData(MSAttachments.PLAYER_COLOR)));
	}
}
