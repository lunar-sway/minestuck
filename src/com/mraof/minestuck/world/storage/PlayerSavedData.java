package com.mraof.minestuck.world.storage;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.alchemy.GristSet;
import com.mraof.minestuck.alchemy.GristType;
import com.mraof.minestuck.editmode.ClientEditHandler;
import com.mraof.minestuck.inventory.captchalogue.CaptchaDeckHandler;
import com.mraof.minestuck.inventory.captchalogue.Modus;
import com.mraof.minestuck.network.GristCachePacket;
import com.mraof.minestuck.network.MinestuckPacketHandler;
import com.mraof.minestuck.network.PlayerDataPacket;
import com.mraof.minestuck.util.*;
import com.mraof.minestuck.util.IdentifierHandler.PlayerIdentifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraft.world.storage.WorldSavedDataStorage;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.HashMap;
import java.util.Map;

public class PlayerSavedData extends WorldSavedData	//TODO This class need a thorough look through to make sure that markDirty() is called when it should (otherwise there may be hard to notice data-loss bugs)
{
	private static final String DATA_NAME = Minestuck.MOD_ID+"_player_data";
	//Client sided

	@OnlyIn(Dist.CLIENT)
	public static Title title;
	@OnlyIn(Dist.CLIENT)
	public static int rung;
	@OnlyIn(Dist.CLIENT)
	public static float rungProgress;
	@OnlyIn(Dist.CLIENT)
	public static long boondollars;
	@OnlyIn(Dist.CLIENT)
	static GristSet playerGrist;
	@OnlyIn(Dist.CLIENT)
	static GristSet targetGrist;
	
	Map<PlayerIdentifier, PlayerData> dataMap = new HashMap<>();
	private final MinecraftServer mcServer;
	
	private PlayerSavedData(MinecraftServer server)
	{
		super(DATA_NAME);
		mcServer = server;
	}
	
	private PlayerSavedData(String name, MinecraftServer server)
	{
		super(name);
		mcServer = server;
	}
	
	public static PlayerSavedData get(MinecraftServer mcServer)
	{
		return get(mcServer.getWorld(DimensionType.OVERWORLD));
	}
	
	public static PlayerSavedData get(World world)
	{
		if(world.isRemote)
			throw new IllegalStateException("Should not attempt to get saved data on the client side!");
		
		WorldSavedDataStorage storage = world.getSavedDataStorage();
		PlayerSavedData instance = storage.get(DimensionType.OVERWORLD, s -> new PlayerSavedData(s, world.getServer()), DATA_NAME);
		
		if(instance == null)	//There is no save data
		{
			instance = new PlayerSavedData(world.getServer());
			storage.set(DimensionType.OVERWORLD, DATA_NAME, instance);
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

	public void setGrist(PlayerIdentifier player, GristSet set)
	{
		getData(player).gristCache = set;
		markDirty();
	}

	public Title getTitle(PlayerIdentifier player)
	{
		return getData(player).title;
	}
	
	public boolean getEffectToggle(PlayerIdentifier player)
	{
		return getData(player).effectToggle;
	}
	
	public void setEffectToggle(PlayerIdentifier player, boolean toggle)
	{
		getData(player).effectToggle = toggle;
		markDirty();
	}
	
	
	@Override
	public NBTTagCompound write(NBTTagCompound compound)
	{
		NBTTagList list = new NBTTagList();
		for (PlayerData data : dataMap.values())
			list.add(data.writeToNBT());
		
		compound.put("playerData", list);
		return compound;
	}
	
	@Override
	public void read(NBTTagCompound nbt)
	{
		NBTTagList list = nbt.getList("playerData", 10);
		for (int i = 0; i < list.size(); i++)
		{
			NBTTagCompound dataCompound = list.getCompound(i);
			PlayerData data = new PlayerData();
			data.readFromNBT(dataCompound, mcServer);
			dataMap.put(data.player, data);
		}
	}

	public void setTitle(PlayerIdentifier player, Title newTitle)
	{
		if (getData(player).title == null)
			getData(player).title = newTitle;
		this.markDirty();
	}

	public static PlayerData getData(ServerPlayerEntity player)
	{
		return get(player.world).getData(IdentifierHandler.encode(player));
	}

	public PlayerData getData(PlayerIdentifier player)
	{
		if (!dataMap.containsKey(player))
		{
			PlayerData data = new PlayerData();
			data.player = player;
			data.echeladder = new Echeladder(player, mcServer);
			dataMap.put(player, data);
		}
		return dataMap.get(player);
	}

	public static GristSet getGristSet(PlayerEntity player)
	{
		if (player.world.isRemote)
			return getClientGrist();
		else return get(player.world).getGristSet(IdentifierHandler.encode(player));
	}
	
	public static boolean addBoondollars(ServerPlayerEntity player, long boons)
	{
		PlayerData data = getData(player);
		if(data.boondollars + boons < 0)
			return false;
		data.boondollars += boons;
		get(player.world).markDirty();
		
		MinestuckPacketHandler.sendToPlayer(PlayerDataPacket.boondollars(data.boondollars), player);
		return true;
	}
	
	public boolean addBoondollars(PlayerIdentifier id, long boons)
	{
		PlayerData data = getData(id);
		if(data.boondollars + boons < 0)
			return false;
		data.boondollars += boons;
		markDirty();
		
		EntityPlayerMP player = id.getPlayer(mcServer);
		if(player != null)
			MinestuckPacketHandler.sendToPlayer(PlayerDataPacket.boondollars(data.boondollars), player);
		return true;
	}
	
	public static class PlayerData
	{
		public PlayerIdentifier player;
		public Title title;
		public GristSet gristCache;
		public Modus modus;
		public boolean givenModus;
		public int color = -1;
		public long boondollars;
		public Echeladder echeladder;
		public boolean effectToggle = true;
		
		private void readFromNBT(NBTTagCompound nbt, MinecraftServer mcServer)
		{
			if (nbt.contains("username"))
				this.player = IdentifierHandler.load(nbt, "username");    //For compability with saves from older minestuck versions
			else this.player = IdentifierHandler.load(nbt, "player");
			if (nbt.contains("grist"))
			{
				this.gristCache = new GristSet();
				NBTTagList gristTags = nbt.getList("grist", 10);
				for(int i = 0; i <  gristTags.size(); i++)
				{
					NBTTagCompound gristTag = gristTags.getCompound(i);
					GristType type = GristType.getTypeFromString(gristTag.getString("id"));
					if(type != null)
						this.gristCache.setGrist(type, gristTag.getInt("amount"));
				}
			}
			if (nbt.contains("titleClass"))
				this.title = new Title(EnumClass.getClassFromInt(nbt.getByte("titleClass")), EnumAspect.getAspectFromInt(nbt.getByte("titleAspect")));
			if (nbt.contains("modus"))
			{
				this.modus = CaptchaDeckHandler.readFromNBT(nbt.getCompound("modus"), false);
				givenModus = true;
			}
			else givenModus = nbt.getBoolean("givenModus");
			if (nbt.contains("color"))
				this.color = nbt.getInt("color");
			boondollars = nbt.getLong("boondollars");
			effectToggle = nbt.getBoolean("effectToggle");
			
			echeladder = new Echeladder(player, mcServer);
			echeladder.loadEcheladder(nbt);
		}

		private NBTTagCompound writeToNBT()
		{
			NBTTagCompound nbt = new NBTTagCompound();
			player.saveToNBT(nbt, "player");
			if (this.gristCache != null)
			{
				NBTTagList list = new NBTTagList();
				for (GristType type : GristType.values())
				{
					NBTTagCompound gristTag = new NBTTagCompound();
					gristTag.putString("id", String.valueOf(type.getRegistryName()));
					gristTag.putInt("amount", this.gristCache.getGrist(type));
					list.add(gristTag);
				}
				nbt.put("grist", list);
			}
			if (this.title != null)
			{
				nbt.putByte("titleClass", (byte) this.title.getHeroClass().ordinal());
				nbt.putByte("titleAspect", (byte) this.title.getHeroAspect().ordinal());
			}
			if (this.modus != null)
				nbt.put("modus", CaptchaDeckHandler.writeToNBT(modus));
			else nbt.putBoolean("givenModus", givenModus);
			nbt.putInt("color", this.color);
			nbt.putLong("boondollars", boondollars);
			nbt.putBoolean("effectToggle", effectToggle);
			
			echeladder.saveEcheladder(nbt);
			return nbt;
		}

	}
	
}
