package com.mraof.minestuck.util;

import com.mraof.minestuck.alchemy.GristSet;
import com.mraof.minestuck.alchemy.GristType;
import com.mraof.minestuck.editmode.ClientEditHandler;
import com.mraof.minestuck.inventory.captchalouge.CaptchaDeckHandler;
import com.mraof.minestuck.inventory.captchalouge.Modus;
import com.mraof.minestuck.network.GristCachePacket;
import com.mraof.minestuck.network.MinestuckChannelHandler;
import com.mraof.minestuck.network.MinestuckPacket;
import com.mraof.minestuck.network.PlayerDataPacket;
import com.mraof.minestuck.util.IdentifierHandler.PlayerIdentifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashMap;
import java.util.Map;

public class MinestuckPlayerData
{

	//Client sided

	@SideOnly(Side.CLIENT)
	public static Title title;
	@SideOnly(Side.CLIENT)
	public static int rung;
	@SideOnly(Side.CLIENT)
	public static float rungProgress;
	@SideOnly(Side.CLIENT)
	public static long boondollars;
	@SideOnly(Side.CLIENT)
	static GristSet playerGrist;
	@SideOnly(Side.CLIENT)
	static GristSet targetGrist;
	static Map<PlayerIdentifier, PlayerData> dataMap = new HashMap<>();

	public static void onPacketRecived(GristCachePacket packet)
	{
		if (packet.targetGrist)
		{
			targetGrist = packet.values;
		}
		else
		{
			playerGrist = packet.values;
		}
	}

	//Server sided

	public static GristSet getClientGrist()
	{
		return ClientEditHandler.isActive() ? targetGrist : playerGrist;
	}

	public static GristSet getGristSet(PlayerIdentifier player)
	{
		return getData(player).gristCache;
	}

	public static void setGrist(PlayerIdentifier player, GristSet set)
	{
		getData(player).gristCache = set;
	}

	public static Title getTitle(PlayerIdentifier player)
	{
		return getData(player).title;
	}
	
	public static boolean getEffectToggle(PlayerIdentifier player)
	{
		return getData(player).effectToggle;
	}
	
	public static void setEffectToggle(PlayerIdentifier player, boolean toggle)
	{
		getData(player).effectToggle = toggle;
	}
	
	public static void writeToNBT(NBTTagCompound nbt)
	{
		NBTTagList list = new NBTTagList();
		for (PlayerData data : dataMap.values())
			list.appendTag(data.writeToNBT());

		nbt.setTag("playerData", list);
	}

	public static void readFromNBT(NBTTagCompound nbt)
	{
		dataMap.clear();
		if (nbt == null)
			return;

		NBTTagList list = nbt.getTagList("playerData", 10);
		for (int i = 0; i < list.tagCount(); i++)
		{
			NBTTagCompound dataCompound = list.getCompoundTagAt(i);
			PlayerData data = new PlayerData();
			data.readFromNBT(dataCompound);
			dataMap.put(data.player, data);
		}
	}

	public static void setTitle(PlayerIdentifier player, Title newTitle)
	{
		if (getData(player).title == null)
			getData(player).title = newTitle;
	}

	public static PlayerData getData(EntityPlayer player)
	{
		return getData(IdentifierHandler.encode(player));
	}

	public static PlayerData getData(PlayerIdentifier player)
	{
		if (!dataMap.containsKey(player))
		{
			PlayerData data = new PlayerData();
			data.player = player;
			data.echeladder = new Echeladder(player);
			dataMap.put(player, data);
		}
		return dataMap.get(player);
	}

	public static GristSet getGristSet(EntityPlayer player)
	{
		if (player.world.isRemote)
			return getClientGrist();
		else return getGristSet(IdentifierHandler.encode(player));
	}
	
	public static boolean addBoondollars(EntityPlayer player, long boons)
	{
		return addBoondollars(IdentifierHandler.encode(player), boons);
	}
	
	public static boolean addBoondollars(PlayerIdentifier id, long boons)
	{
		PlayerData data = MinestuckPlayerData.getData(id);
		if(data.boondollars + boons < 0)
			return false;
		data.boondollars += boons;
		
		EntityPlayer player = id.getPlayer();
		if(player != null)
			MinestuckChannelHandler.sendToPlayer(MinestuckPacket.makePacket(MinestuckPacket.Type.PLAYER_DATA, PlayerDataPacket.BOONDOLLAR, data.boondollars), player);
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
		
		private void readFromNBT(NBTTagCompound nbt)
		{
			if (nbt.hasKey("username"))
				this.player = IdentifierHandler.load(nbt, "username");    //For compability with saves from older minestuck versions
			else this.player = IdentifierHandler.load(nbt, "player");
			if (nbt.hasKey("grist"))
			{
				//old format
				if (nbt.getTagId("grist") == 11)
				{
					int[] array = nbt.getIntArray("grist");
					this.gristCache = new GristSet()
							.addGrist(GristType.Amber, array[0])
							.addGrist(GristType.Amethyst, array[1])
							.addGrist(GristType.Artifact, array[2])
							.addGrist(GristType.Build, array[3])
							.addGrist(GristType.Caulk, array[4])
							.addGrist(GristType.Chalk, array[5])
							.addGrist(GristType.Cobalt, array[6])
							.addGrist(GristType.Diamond, array[7])
							.addGrist(GristType.Garnet, array[8])
							.addGrist(GristType.Gold, array[9])
							.addGrist(GristType.Iodine, array[10])
							.addGrist(GristType.Marble, array[11])
							.addGrist(GristType.Mercury, array[12])
							.addGrist(GristType.Quartz, array[13])
							.addGrist(GristType.Ruby, array[14])
							.addGrist(GristType.Rust, array[15])
							.addGrist(GristType.Shale, array[16])
							.addGrist(GristType.Sulfur, array[17])
							.addGrist(GristType.Tar, array[18])
							.addGrist(GristType.Uranium, array[19])
							.addGrist(GristType.Zillium, array[20]);
				}
				else
				{
					this.gristCache = new GristSet();
					for (NBTBase nbtBase : nbt.getTagList("grist", 10))
					{
						NBTTagCompound gristTag = (NBTTagCompound) nbtBase;
						GristType type = GristType.getTypeFromString(gristTag.getString("id"));
						if(type != null)
							this.gristCache.setGrist(type, gristTag.getInteger("amount"));
					}
				}
			}
			if (nbt.hasKey("titleClass"))
				this.title = new Title(EnumClass.getClassFromInt(nbt.getByte("titleClass")), EnumAspect.getAspectFromInt(nbt.getByte("titleAspect")));
			if (nbt.hasKey("modus"))
			{
				this.modus = CaptchaDeckHandler.readFromNBT(nbt.getCompoundTag("modus"), false);
				givenModus = true;
			}
			else givenModus = nbt.getBoolean("givenModus");
			if (nbt.hasKey("color"))
				this.color = nbt.getInteger("color");
			boondollars = nbt.getLong("boondollars");
			effectToggle = nbt.getBoolean("effectToggle");
			
			echeladder = new Echeladder(player);
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
					gristTag.setString("id", String.valueOf(type.getRegistryName()));
					gristTag.setInteger("amount", this.gristCache.getGrist(type));
					list.appendTag(gristTag);
				}
				nbt.setTag("grist", list);
			}
			if (this.title != null)
			{
				nbt.setByte("titleClass", (byte) this.title.getHeroClass().ordinal());
				nbt.setByte("titleAspect", (byte) this.title.getHeroAspect().ordinal());
			}
			if (this.modus != null)
				nbt.setTag("modus", CaptchaDeckHandler.writeToNBT(modus));
			else nbt.setBoolean("givenModus", givenModus);
			nbt.setInteger("color", this.color);
			nbt.setLong("boondollars", boondollars);
			nbt.setBoolean("effectToggle", effectToggle);
			
			echeladder.saveEcheladder(nbt);
			return nbt;
		}

	}

}
