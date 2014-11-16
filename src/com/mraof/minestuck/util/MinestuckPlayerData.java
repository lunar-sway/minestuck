package com.mraof.minestuck.util;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import com.mraof.minestuck.editmode.ClientEditHandler;
import com.mraof.minestuck.inventory.captchalouge.CaptchaDeckHandler;
import com.mraof.minestuck.inventory.captchalouge.Modus;
import com.mraof.minestuck.network.GristCachePacket;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class MinestuckPlayerData {
	
	//Client sided
	
	@SideOnly(Side.CLIENT)
	static GristSet playerGrist;
	@SideOnly(Side.CLIENT)
	static GristSet targetGrist;
	@SideOnly(Side.CLIENT)
	public static Title title;
	
	
	public static void onPacketRecived(GristCachePacket packet) {
		if(packet.targetGrist)
			targetGrist = new GristSet(GristType.values(), packet.values);
		else playerGrist = new GristSet(GristType.values(), packet.values);
	}
	
	public static GristSet getClientGrist() {
		return ClientEditHandler.isActive()?targetGrist:playerGrist;
	}
	
	//Server sided
	
	static Map<String, PlayerData> dataMap = new HashMap<String, PlayerData>();
	
	public static GristSet getGristSet(String player)
	{
		return getData(player).gristCache;
	}
	
	public static void setGrist(String player, GristSet set)
	{
		getData(player).gristCache = set;
	}
	
	public static Title getTitle(String player)
	{
		return getData(player).title;
	}
	
	public static void writeToNBT(NBTTagCompound nbt)
	{
		NBTTagList list = new NBTTagList();
		for(PlayerData data : dataMap.values())
			list.appendTag(data.writeToNBT());
		
		nbt.setTag("playerData", list);
	}
	
	public static void readFromNBT(NBTTagCompound nbt)
	{
		dataMap.clear();
		if(nbt == null)
			return;
		
		NBTTagList list = nbt.getTagList("playerData", 10);
		for(int i = 0; i < list.tagCount(); i++)
		{
			NBTTagCompound dataCompound = list.getCompoundTagAt(i);
			PlayerData data = new PlayerData();
			data.readFromNBT(dataCompound);
			dataMap.put(data.player, data);
		}
	}
	
	public static void setTitle(String player, Title newTitle)
	{
		if(getData(player).title == null)
			getData(player).title = newTitle;
	}
	
	public static PlayerData getData(String player)
	{
		if(!dataMap.containsKey(player))
		{
			PlayerData data = new PlayerData();
			data.player = player;
			dataMap.put(player, data);
		}
		return dataMap.get(player);
	}
	
	public static class PlayerData
	{
		
		public String player;
		public Title title;
		public GristSet gristCache;
		public Modus modus;
		
		private void readFromNBT(NBTTagCompound nbt)
		{
			this.player = nbt.getString("username");
			if(nbt.hasKey("grist"))
				this.gristCache = new GristSet(GristType.values(), nbt.getIntArray("grist"));
			if(nbt.hasKey("titleClass"))
				this.title = new Title(TitleHelper.getClassFromInt(nbt.getByte("titleClass")), TitleHelper.getAspectFromInt(nbt.getByte("titleAspect")));
			if(nbt.hasKey("modus"))
				this.modus = CaptchaDeckHandler.readFromNBT(nbt.getCompoundTag("modus"), false);
		}
		
		private NBTTagCompound writeToNBT()
		{
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setString("username", this.player);
			if(this.gristCache != null)
			{
				int[] grist = new int[GristType.allGrists];
				for(GristType type : GristType.values())
					grist[type.ordinal()] = this.gristCache.getGrist(type);
				nbt.setIntArray("grist", grist);
			}
			if(this.title != null)
			{
				nbt.setByte("titleClass", (byte) this.title.getHeroClass().ordinal());
				nbt.setByte("titleAspect", (byte) this.title.getHeroAspect().ordinal());
			}
			if(this.modus != null)
				nbt.setTag("modus", CaptchaDeckHandler.writeToNBT(modus));
			return nbt;
		}
		
	}
	
}
