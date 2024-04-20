package com.mraof.minestuck.util;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.event.AlchemyEvent;
import com.mraof.minestuck.item.AlchemizedColored;
import com.mraof.minestuck.network.data.PlayerColorPacket;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.player.PlayerData;
import com.mraof.minestuck.player.PlayerIdentifier;
import com.mraof.minestuck.player.PlayerSavedData;
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
import net.neoforged.neoforge.network.PacketDistributor;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Stores the array with colors that the player picks from, and provides utility function to handle colors.
 */
@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ColorHandler
{
	public static final int DEFAULT_COLOR = 0xA0DCFF;
	private static final List<Pair<Integer, String>> colors;
	
	static
	{
		colors = new ArrayList<>();
		
		colors.add(Pair.of(0x0715cd, "blue"));
		colors.add(Pair.of(0xb536da, "orchid"));
		colors.add(Pair.of(0xe00707, "red"));
		colors.add(Pair.of(0x4ac925, "green"));
		
		colors.add(Pair.of(0x00d5f2, "cyan"));
		colors.add(Pair.of(0xff6ff2, "pink"));
		colors.add(Pair.of(0xf2a400, "orange"));
		colors.add(Pair.of(0x1f9400, "emerald"));
		
		colors.add(Pair.of(0xa10000, "rust"));
		colors.add(Pair.of(0xa15000, "bronze"));
		colors.add(Pair.of(0xa1a100, "gold"));
		colors.add(Pair.of(0x626262, "iron"));
		colors.add(Pair.of(0x416600, "olive"));
		colors.add(Pair.of(0x008141, "jade"));
		colors.add(Pair.of(0x008282, "teal"));
		colors.add(Pair.of(0x005682, "cobalt"));
		colors.add(Pair.of(0x000056, "indigo"));
		colors.add(Pair.of(0x2b0057, "purple"));
		colors.add(Pair.of(0x6a006a, "violet"));
		colors.add(Pair.of(0x77003c, "fuchsia"));
	}
	
	@SubscribeEvent
	public static void onAlchemy(AlchemyEvent event)
	{
		ItemStack stack = event.getItemResult();
		if(stack.getItem() instanceof AlchemizedColored)
		{
			int color = getColorForPlayer(event.getPlayer(), event.getLevel());
			event.setItemResult(((AlchemizedColored) stack.getItem()).setColor(stack, color));
		}
	}
	
	public static int getColor(int index)
	{
		if(index < 0 || index >= colors.size())
			return DEFAULT_COLOR;
		return colors.get(index).getLeft();
	}
	
	public static Component getName(int index)
	{
		if(index < 0 || index >= colors.size())
			return Component.literal("INVALID");
		return Component.translatable("minestuck.color." + colors.get(index).getRight());
	}
	
	public static int getColorSize()
	{
		return colors.size();
	}
	
	public static ItemStack setDefaultColor(ItemStack stack)
	{
		return setColor(stack, DEFAULT_COLOR);
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
		else return DEFAULT_COLOR;
	}
	
	public static int getColorForDimension(ServerLevel level)
	{
		return SburbPlayerData.getForLand(level)
				.map(data -> getColorForPlayer(data.playerId(), level))
				.orElse(ColorHandler.DEFAULT_COLOR);
	}
	
	public static int getColorForPlayer(ServerPlayer player)
	{
		return getColorForPlayer(IdentifierHandler.encode(player), player.level());
	}
	
	public static int getColorForPlayer(PlayerIdentifier identifier, Level level)
	{
		return getColor(PlayerSavedData.getData(identifier, level));
	}
	
	public static int getColor(PlayerData playerData)
	{
		return playerData.getData(MSCapabilities.PLAYER_COLOR);
	}
	
	public static void trySetColor(ServerPlayer player, int color)
	{
		PlayerIdentifier playerId = IdentifierHandler.encode(player);
		if(playerId == null || !SburbHandler.canSelectColor(playerId, player.server))
			return;
		
		PlayerData playerData = PlayerSavedData.getData(playerId, player.server);
		Integer prevColor = playerData.setData(MSCapabilities.PLAYER_COLOR, color);
		
		if(!Objects.equals(prevColor, color))
			sendColor(player, playerData);
	}
	
	@SubscribeEvent
	private static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event)
	{
		ServerPlayer player = (ServerPlayer) event.getEntity();
		PlayerData playerData = PlayerSavedData.getData(player);
		ColorHandler.sendColor(player, playerData);
	}
	
	private static void sendColor(ServerPlayer player, PlayerData playerData)
	{
		if(player == null)
			return;
		
		boolean firstTime = playerData.getExistingData(MSCapabilities.PLAYER_COLOR).isEmpty();
		
		if(firstTime && !player.isSpectator())
		{
			playerData.setData(MSCapabilities.PLAYER_COLOR, DEFAULT_COLOR);
			PacketDistributor.PLAYER.with(player).send(new PlayerColorPacket.OpenSelection());
		} else
			PacketDistributor.PLAYER.with(player).send(new PlayerColorPacket.Data(getColor(playerData)));
	}
}