package com.mraof.minestuck.world.storage;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.editmode.ClientEditHandler;
import com.mraof.minestuck.item.crafting.alchemy.GristSet;
import com.mraof.minestuck.network.GristCachePacket;
import com.mraof.minestuck.util.IdentifierHandler;
import com.mraof.minestuck.util.IdentifierHandler.PlayerIdentifier;
import com.mraof.minestuck.util.Title;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.DimensionSavedDataManager;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.Constants;

import java.util.HashMap;
import java.util.Map;

public class PlayerSavedData extends WorldSavedData	//TODO This class need a thorough look through to make sure that markDirty() is called when it should (otherwise there may be hard to notice data-loss bugs)
{
	private static final String DATA_NAME = Minestuck.MOD_ID+"_player_data";
	//TODO Put client data in its own class
	//Client sided
	public static Title title;
	public static int rung;
	public static float rungProgress;
	public static long boondollars;
	static GristSet playerGrist;
	static GristSet targetGrist;
	
	private final Map<PlayerIdentifier, PlayerData> dataMap = new HashMap<>();
	public final MinecraftServer mcServer;
	
	private PlayerSavedData(MinecraftServer server)
	{
		super(DATA_NAME);
		mcServer = server;
	}
	
	public static PlayerSavedData get(World world)
	{
		MinecraftServer server = world.getServer();
		if(server == null)
			throw new IllegalArgumentException("Can't get player data instance on client side! (Got null server from world)");
		return get(server);
	}
	
	public static PlayerSavedData get(MinecraftServer mcServer)
	{
		ServerWorld world = mcServer.getWorld(DimensionType.OVERWORLD);
		
		DimensionSavedDataManager storage = world.getSavedData();
		PlayerSavedData instance = storage.get(() -> new PlayerSavedData(world.getServer()), DATA_NAME);
		
		if(instance == null)	//There is no save data
		{
			instance = new PlayerSavedData(world.getServer());
			storage.set(instance);
		}
		
		return instance;
	}
	
	public static void onPacketRecived(GristCachePacket packet)
	{
		if (packet.isEditmode)
		{
			targetGrist = packet.gristCache;
		}
		else
		{
			playerGrist = packet.gristCache;
		}
	}

	//Server sided

	public static GristSet getClientGrist()
	{
		return ClientEditHandler.isActive() ? targetGrist : playerGrist;
	}

	public GristSet getGristSet(PlayerIdentifier player)
	{
		return getData(player).gristCache;
	}
	
	@Override
	public CompoundNBT write(CompoundNBT compound)
	{
		ListNBT list = new ListNBT();
		for (PlayerData data : dataMap.values())
			list.add(data.writeToNBT());
		
		compound.put("playerData", list);
		return compound;
	}
	
	@Override
	public void read(CompoundNBT nbt)
	{
		ListNBT list = nbt.getList("playerData", Constants.NBT.TAG_COMPOUND);
		for (int i = 0; i < list.size(); i++)
		{
			CompoundNBT dataCompound = list.getCompound(i);
			PlayerData data = new PlayerData(this, dataCompound);
			dataMap.put(data.identifier, data);
		}
	}

	public static PlayerData getData(ServerPlayerEntity player)
	{
		return get(player.server).getData(IdentifierHandler.encode(player));
	}
	
	public static PlayerData getData(PlayerIdentifier player, World world)
	{
		return get(world).getData(player);
	}
	
	public static PlayerData getData(PlayerIdentifier player, MinecraftServer server)
	{
		return get(server).getData(player);
	}

	public PlayerData getData(PlayerIdentifier player)
	{
		if (!dataMap.containsKey(player))
		{
			PlayerData data = new PlayerData(this, player);
			dataMap.put(player, data);
			markDirty();
		}
		return dataMap.get(player);
	}

	public static GristSet getGristSet(PlayerEntity player)
	{
		if (player.world.isRemote)
			return getClientGrist();
		else return get(player.getServer()).getGristSet(IdentifierHandler.encode(player));
	}
}