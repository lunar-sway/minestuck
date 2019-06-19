package com.mraof.minestuck.util;

import com.mraof.minestuck.alchemy.GristSet;
import com.mraof.minestuck.alchemy.GristType;
import com.mraof.minestuck.editmode.ClientEditHandler;
import com.mraof.minestuck.inventory.captchalogue.CaptchaDeckHandler;
import com.mraof.minestuck.inventory.captchalogue.Modus;
import com.mraof.minestuck.network.GristCachePacket;
import com.mraof.minestuck.network.MinestuckPacketHandler;
import com.mraof.minestuck.network.PlayerDataPacket;
import com.mraof.minestuck.util.IdentifierHandler.PlayerIdentifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.HashMap;
import java.util.Map;

public class MinestuckPlayerData
{

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
	static Map<PlayerIdentifier, PlayerData> dataMap = new HashMap<>();

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
			list.add(data.writeToNBT());

		nbt.setTag("playerData", list);
	}

	public static void readFromNBT(NBTTagCompound nbt)
	{
		dataMap.clear();
		if (nbt == null)
			return;

		NBTTagList list = nbt.getList("playerData", 10);
		for (int i = 0; i < list.size(); i++)
		{
			NBTTagCompound dataCompound = list.getCompound(i);
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
	
	public static boolean addBoondollars(EntityPlayerMP player, long boons)
	{
		PlayerData data = MinestuckPlayerData.getData(player);
		if(data.boondollars + boons < 0)
			return false;
		data.boondollars += boons;
		
		MinestuckPacketHandler.sendToPlayer(PlayerDataPacket.boondollars(data.boondollars), player);
		return true;
	}
	
	public static boolean addBoondollars(MinecraftServer server, PlayerIdentifier id, long boons)
	{
		PlayerData data = MinestuckPlayerData.getData(id);
		if(data.boondollars + boons < 0)
			return false;
		data.boondollars += boons;
		
		EntityPlayerMP player = id.getPlayer(server);
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
		
		private void readFromNBT(NBTTagCompound nbt)
		{
			if (nbt.hasKey("username"))
				this.player = IdentifierHandler.load(nbt, "username");    //For compability with saves from older minestuck versions
			else this.player = IdentifierHandler.load(nbt, "player");
			if (nbt.hasKey("grist"))
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
			if (nbt.hasKey("titleClass"))
				this.title = new Title(EnumClass.getClassFromInt(nbt.getByte("titleClass")), EnumAspect.getAspectFromInt(nbt.getByte("titleAspect")));
			if (nbt.hasKey("modus"))
			{
				this.modus = CaptchaDeckHandler.readFromNBT(nbt.getCompound("modus"), false);
				givenModus = true;
			}
			else givenModus = nbt.getBoolean("givenModus");
			if (nbt.hasKey("color"))
				this.color = nbt.getInt("color");
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
					gristTag.setInt("amount", this.gristCache.getGrist(type));
					list.add(gristTag);
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
			nbt.setInt("color", this.color);
			nbt.setLong("boondollars", boondollars);
			nbt.setBoolean("effectToggle", effectToggle);
			
			echeladder.saveEcheladder(nbt);
			return nbt;
		}

	}

}
