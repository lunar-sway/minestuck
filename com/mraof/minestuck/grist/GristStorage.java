package com.mraof.minestuck.grist;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class GristStorage {
	
	static Map<String, GristSet> gristMap = new HashMap();
	
	public static GristSet getGrist(String player) {
		return gristMap.get(player);
	}
	
	public static void setGrist(String player, GristSet set) {
		gristMap.put(player, set);
	}
	
	public static void writeToNBT(NBTTagCompound nbt) {
		NBTTagList list = new NBTTagList();
		for(Map.Entry<String, GristSet> entry : gristMap.entrySet()) {
			NBTTagCompound gristCompound = new NBTTagCompound();
			gristCompound.setString("username", entry.getKey());
			for(GristType type : GristType.values())
				gristCompound.setInteger(type.getName(), entry.getValue().getGrist(type));
			list.appendTag(gristCompound);
		}
		nbt.setTag("grist", list);
	}
	
	public static void readFromNBT(NBTTagCompound nbt) {
		gristMap.clear();
		if(nbt == null)
			return;
		NBTTagList list = nbt.getTagList("grist");
		for(int i = 0; i < list.tagCount(); i++) {
			NBTTagCompound gristCompound = (NBTTagCompound)list.tagAt(i);
			GristSet set = new GristSet();
			for(GristType type : GristType.values())
				set.addGrist(type, gristCompound.getInteger(type.getName()));
			gristMap.put(gristCompound.getString("username"), set);
		}
	}
	
}
