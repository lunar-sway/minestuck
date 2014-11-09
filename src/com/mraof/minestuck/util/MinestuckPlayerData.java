package com.mraof.minestuck.util;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import com.mraof.minestuck.editmode.ClientEditHandler;
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
	
	static Map<String, GristSet> gristMap = new HashMap<String, GristSet>();
	
	static Map<String, Title> titles = new HashMap();
	
	public static GristSet getGristSet(String player) {
		if(!gristMap.containsKey(player))
			Debug.print("Failed to get grist cache for "+player);
		return gristMap.get(player);
	}
	
	public static void setGrist(String player, GristSet set) {
		gristMap.put(player, set);
	}
	
	public static Title getTitle(String player) {
		return titles.get(player);
	}
	
	public static void writeToNBT(NBTTagCompound nbt)
	{
		NBTTagList list = new NBTTagList();
		for(Map.Entry<String, GristSet> entry : gristMap.entrySet())
		{
			NBTTagCompound dataCompound = new NBTTagCompound();
			dataCompound.setString("username", entry.getKey());
			
			int[] grist = new int[GristType.allGrists];
			for(GristType type : GristType.values())
				grist[type.ordinal()] = entry.getValue().getGrist(type);
			dataCompound.setIntArray("grist", grist);
			list.appendTag(dataCompound);
			
			if(titles.containsKey(entry.getKey()))
			{
				Title title = titles.get(entry.getKey());
				dataCompound.setByte("titleClass", (byte) title.getHeroClass().ordinal());
				dataCompound.setByte("titleAspect", (byte) title.getHeroAspect().ordinal());
			}
		}
		nbt.setTag("playerData", list);
	}
	
	public static void readFromNBT(NBTTagCompound nbt)
	{
		if(nbt == null)
			return;
		NBTTagList list;
		if(nbt.hasKey("playerData"))
			list = nbt.getTagList("playerData", 10);
		else list = nbt.getTagList("grist", 10);
		for(int i = 0; i < list.tagCount(); i++)
		{
			NBTTagCompound dataCompound = list.getCompoundTagAt(i);
			String username = dataCompound.getString("username");
			if(dataCompound.hasKey("grist")) {
				GristSet set = new GristSet(GristType.values(), dataCompound.getIntArray("grist"));
				gristMap.put(username, set);
				Title title = new Title(TitleHelper.getClassFromInt(dataCompound.getByte("titleClass")), TitleHelper.getAspectFromInt(dataCompound.getByte("titleAspect")));
				titles.put(username, title);
			} else
			{
				GristSet set = new GristSet();
				for(GristType type : GristType.values())
					set.addGrist(type, dataCompound.getInteger(type.getName()));
				gristMap.put(username, set);
			}
		}
	}

	public static void onServerStarting()
	{
		gristMap.clear();
		titles.clear();
	}
	
	public static void setTitle(String player, Title newTitle)
	{
		if(titles.get(player) == null)
			titles.put(player, newTitle);
	}
	
}
