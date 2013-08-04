package com.mraof.minestuck.tracker;

import com.mraof.minestuck.entity.item.EntityGrist;
import com.mraof.minestuck.network.MinestuckPacket;
import com.mraof.minestuck.network.MinestuckPacket.Type;
import com.mraof.minestuck.util.Title;
import com.mraof.minestuck.util.TitleHelper;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.packet.Packet250CustomPayload;
import cpw.mods.fml.common.IPlayerTracker;

public class MinestuckPlayerTracker implements IPlayerTracker 
{

	@Override
	public void onPlayerLogin(EntityPlayer player) 
	{
		updateGristCache(player);
		updateTitle(player);
	}

	@Override
	public void onPlayerLogout(EntityPlayer player) 
	{

	}

	@Override
	public void onPlayerChangedDimension(EntityPlayer player) 
	{
		updateGristCache(player);
		updateTitle(player);
	}

	@Override
	public void onPlayerRespawn(EntityPlayer player) 
	{
		updateGristCache(player);
		updateTitle(player);
	}
	
	public void updateGristCache(EntityPlayer player)
	{
		//set all the grist values to the correct amount
        Packet250CustomPayload packet = new Packet250CustomPayload();
        int[] gristValues = new int[EntityGrist.gristTypes.length];
        for(int typeInt = 0; typeInt < gristValues.length; typeInt++)
        	gristValues[typeInt] = ((EntityPlayer)player).getEntityData().getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG).getCompoundTag("Grist").getInteger(EntityGrist.gristTypes[typeInt]);
        packet.channel = "Minestuck";
        packet.data = MinestuckPacket.makePacket(Type.GRISTCACHE, gristValues);
        packet.length = packet.data.length;
        ((EntityPlayerMP)player).playerNetServerHandler.sendPacketToPlayer(packet);
	}
	
	public void updateTitle(EntityPlayer player) {
			Title newTitle;
			if (((EntityPlayer)player).getEntityData().getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG).getInteger("Class") == 0 || ((EntityPlayer)player).getEntityData().getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG).getInteger("Aspect") == 0) {
				newTitle = TitleHelper.randomTitle();
			} else {
				newTitle = new Title(((EntityPlayer)player).getEntityData().getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG).getInteger("Class"),((EntityPlayer)player).getEntityData().getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG).getInteger("Aspect"));
			}
	        Packet250CustomPayload packet = new Packet250CustomPayload();
	        packet.channel = "Minestuck";
	        packet.data = MinestuckPacket.makePacket(Type.TITLE, newTitle.getHeroClass(),newTitle.getHeroAspect());
	        packet.length = packet.data.length;
	        ((EntityPlayerMP)player).playerNetServerHandler.sendPacketToPlayer(packet);
	}
}
