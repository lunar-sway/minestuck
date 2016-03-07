package com.mraof.minestuck.util;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.mraof.minestuck.editmode.ClientEditHandler;
import com.mraof.minestuck.inventory.captchalouge.CaptchaDeckHandler;
import com.mraof.minestuck.inventory.captchalouge.Modus;
import com.mraof.minestuck.network.GristCachePacket;

public class MinestuckPlayerData {
	
	//Client sided
	
	@SideOnly(Side.CLIENT)
	static GristSet playerGrist;
	@SideOnly(Side.CLIENT)
	static GristSet targetGrist;
	@SideOnly(Side.CLIENT)
	public static Title title;
	@SideOnly(Side.CLIENT)
	public static int rung;
	@SideOnly(Side.CLIENT)
	public static float rungProgress;
	@SideOnly(Side.CLIENT)
	public static int boondollars;
	
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
	
	public static PlayerData getData(EntityPlayer player)
	{
		return getData(UsernameHandler.encode(player.getCommandSenderName()));
	}
	
	public static PlayerData getData(String player)
	{
		if(!dataMap.containsKey(player))
		{
			PlayerData data = new PlayerData();
			data.player = player;
			data.echeladder = new Echeladder(player);
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
		public boolean givenModus;
		public int color = -1;
		public int boondollars;
		public Echeladder echeladder;
		
		private void readFromNBT(NBTTagCompound nbt)
		{
			this.player = nbt.getString("username");
			if(nbt.hasKey("grist"))
				this.gristCache = new GristSet(GristType.values(), nbt.getIntArray("grist"));
			if(nbt.hasKey("titleClass"))
				this.title = new Title(EnumClass.getClassFromInt(nbt.getByte("titleClass")), EnumAspect.getAspectFromInt(nbt.getByte("titleAspect")));
			if(nbt.hasKey("modus"))
			{
				this.modus = CaptchaDeckHandler.readFromNBT(nbt.getCompoundTag("modus"), false);
				givenModus = true;
			}
			else givenModus = nbt.getBoolean("givenModus");
			if(nbt.hasKey("color"))
				this.color = nbt.getInteger("color");
			boondollars = nbt.getInteger("boondollars");
			
			echeladder = new Echeladder(player);
			echeladder.loadEcheladder(nbt);
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
			else nbt.setBoolean("givenModus", givenModus);
			nbt.setInteger("color", this.color);
			nbt.setInteger("boondollars", boondollars);
			
			echeladder.saveEcheladder(nbt);
			return nbt;
		}
		
	}
	
	public static GristSet getGristSet(EntityPlayer player)
	{
		if(player.worldObj.isRemote)
			return getClientGrist();
		else return getGristSet(UsernameHandler.encode(player.getCommandSenderName()));
	}
	
}
