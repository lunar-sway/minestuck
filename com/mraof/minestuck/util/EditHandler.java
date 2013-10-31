package com.mraof.minestuck.util;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.entity.EntityDecoy;
import com.mraof.minestuck.grist.GristSet;
import com.mraof.minestuck.network.MinestuckPacket;
import com.mraof.minestuck.network.MinestuckPacket.Type;
import com.mraof.minestuck.network.skaianet.SburbConnection;
import com.mraof.minestuck.network.skaianet.SkaianetHandler;
import com.mraof.minestuck.tileentity.TileEntityComputer;
import com.mraof.minestuck.tracker.MinestuckPlayerTracker;
import com.mraof.minestuck.world.storage.MinestuckSaveHandler;

import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.FoodStats;
import net.minecraft.world.World;

public class EditHandler implements ITickHandler{
	
	//Client sided stuff
	static NBTTagCompound capabilities;
	
	static PlayerControllerMP controller;
	
	static int centerX, centerZ;
	
	/**
	 * Used to tell if the client is in edit mode or not.
	 */
	public static boolean isActive() {
		return capabilities != null && controller != null;
	}
	
	public static void activate(String username, String target) {
		Minecraft mc = Minecraft.getMinecraft();
		Packet250CustomPayload packet = new Packet250CustomPayload();
		packet.channel = "Minestuck";
		packet.data = MinestuckPacket.makePacket(Type.CLIENT_EDIT, username, target);
		packet.length = packet.data.length;
		PacketDispatcher.sendPacketToServer(packet);
	}
	
	public static void onKeyPressed() {
		Packet250CustomPayload packet = new Packet250CustomPayload();
		packet.channel = "Minestuck";
		packet.data = MinestuckPacket.makePacket(Type.CLIENT_EDIT);
		packet.length = packet.data.length;
		PacketDispatcher.sendPacketToServer(packet);
	}
	
	public static void onClientPackage(String target, int posX, int posZ) {
		Minecraft mc = Minecraft.getMinecraft();
		EntityClientPlayerMP player = mc.thePlayer;
		if(target != null) {	//Enable edit mode
			if(controller == null) {
				controller = mc.playerController;
				mc.playerController = new SburbServerController(mc, mc.getNetHandler());
				((SburbServerController)mc.playerController).client = target;
			}
			if(capabilities == null) {
				capabilities = new NBTTagCompound();
				player.capabilities.writeCapabilitiesToNBT(capabilities);
			}
			centerX = posX;
			centerZ = posZ;
		} else {	//Disable edit mode
			if(controller != null) {
				mc.playerController = controller;
				controller = null;
			}
			if(capabilities != null) {
				player.capabilities.readCapabilitiesFromNBT(capabilities);
				player.capabilities.allowFlying = mc.playerController.isInCreativeMode();
				player.capabilities.isFlying = player.capabilities.isFlying && player.capabilities.allowFlying;
				capabilities = null;
			}
		}
	}
	
	//Server sided stuff
	
	static List<EditData> list = new ArrayList();
	
	/**
	 * Called both when any player logged out and when a player pressed the exit button.
	 * @param player
	 */
	public static void onPlayerExit(EntityPlayer player) {
		if(!player.worldObj.isRemote) {
			reset(null, 0, getData(player.username));
		}
	}
	
	/**
	 * Called when the server stops editing the clients house.
	 * @param damageSource If the process was cancelled by the decoy taking damage, this parameter will be the damage source. Else null.
	 * @param damage If the damageSource isn't null, this is the damage taken, else this parameter is ignored.
	 * @param decoy The decoy entity used.
	 * @param player The player.
	 */
	public static void reset(DamageSource damageSource, float damage, EditData data) {
		if(data == null) {
			return;
		}
		EntityPlayerMP player = data.player;
		EntityDecoy decoy = data.decoy;
		if(player.dimension != decoy.dimension)
			player.travelToDimension(decoy.dimension);
		player.playerNetServerHandler.setPlayerLocation(decoy.posX, decoy.posY, decoy.posZ, decoy.rotationYaw, decoy.rotationPitch);
		if(!player.theItemInWorldManager.getGameType().equals(decoy.gameType))
			player.setGameType(decoy.gameType);
		player.capabilities.readCapabilitiesFromNBT(decoy.capabilities);
		player.sendPlayerAbilities();
		player.setHealth(decoy.getHealth());
		NBTTagCompound nbt = new NBTTagCompound();
		decoy.foodStats.writeNBT(nbt);
		player.getFoodStats().readNBT(nbt);
		player.theItemInWorldManager = data.manager;
		
		decoy.setDead();
		list.remove(data);
		
		Packet250CustomPayload packet = new Packet250CustomPayload();
		packet.channel = "Minestuck";
		packet.data = MinestuckPacket.makePacket(Type.SERVER_EDIT);
		packet.length = packet.data.length;
		player.playerNetServerHandler.sendPacketToPlayer(packet);
		
		if(damageSource != null && damageSource.getSourceOfDamage() != player)
			player.attackEntityFrom(damageSource, damage);
	}
	
	public static void newServerEditor(EntityPlayerMP player, String computerOwner, String computerTarget) {
		if(player.isRiding())
			return;	//Don't want to bother making the decoy able to ride anything right now.
		SburbConnection c = SkaianetHandler.getClientConnection(computerTarget);
		if(c != null && c.getServerName().equals(computerOwner) && getData(c) == null && getData(player.username) == null) {
			Debug.print("Activating edit mode on player \""+player.username+"\", target player: \""+computerTarget+"\".");
			EntityDecoy decoy = new EntityDecoy(player.worldObj, player);
			EditData data = new EditData(decoy, player, c);
			if(!setPlayerStats(player, c)) {
				player.theItemInWorldManager = data.manager;
				ChatMessageComponent message = new ChatMessageComponent();
				message.addText("Failed to activate edit mode.");
				message.setColor(EnumChatFormatting.RED);
				player.sendChatToPlayer(message);
				return;
			}
			decoy.worldObj.spawnEntityInWorld(decoy);
			data.centerX = (int)player.posX;
			data.centerZ = (int)player.posZ;
			list.add(data);
			Packet250CustomPayload packet = new Packet250CustomPayload();
			packet.channel = "Minestuck";
			packet.data = MinestuckPacket.makePacket(Type.SERVER_EDIT, computerTarget, data.centerX, data.centerZ);
			packet.length = packet.data.length;
			player.playerNetServerHandler.sendPacketToPlayer(packet);
			MinestuckPlayerTracker.updateGristCache(c.getClientName());
		}
	}
	
	static boolean setPlayerStats(EntityPlayerMP player, SburbConnection c) {
		SburbServerManager manager = new SburbServerManager(player.worldObj, player);
		manager.client = c.getClientName();
		player.theItemInWorldManager = manager;
		ChunkCoordinates coord;
		World world = MinecraftServer.getServer().worldServerForDimension(c.enteredGame()?c.getClientDimension():c.getClientData().getDimension());
		if(c.enteredGame()) {
			coord = world.getSpawnPoint();
		} else {
			TileEntityComputer te = SkaianetHandler.getComputer(c.getClientData());
			coord = new ChunkCoordinates(te.xCoord, te.yCoord, te.zCoord);
		}
		coord.posY = world.getTopSolidOrLiquidBlock(coord.posX, coord.posZ);
		
		player.closeScreen();
		
		if(world.provider.dimensionId != player.worldObj.provider.dimensionId)
			player.travelToDimension(world.provider.dimensionId);
		
		player.setPositionAndUpdate(coord.posX+0.5, coord.posY, coord.posZ+0.5);
		
		return true;
	}
	
	public static void onDisconnect(SburbConnection c) {
		reset(null, 0, getData(c));
	}
	
	public static EditData getData(String editor) {
		for(EditData data : list)
			if(data.player.username == editor)
				return data;
		return null;
	}
	
	public static EditData getData(SburbConnection c) {
		Debug.print(list.size());
		for(EditData data : list) {
			if(data.connection.getClientName().equals(c.getClientName()) && data.connection.getServerName().equals(c.getServerName()))
				return data;}
		return null;
	}
	
	public static EditData getData(EntityDecoy decoy) {
		for(EditData data : list)
			if(data.decoy == decoy)
				return data;
		return null;
	}
	
	//Both sided
	
	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) {
		EntityPlayer player = (EntityPlayer)tickData[0];
		double range;
		int centerX, centerZ;
		
		if(player.worldObj.isRemote) {
			if(!(tickData[0] == Minecraft.getMinecraft().thePlayer) || !this.isActive())
				return;
			
			range = (MinestuckSaveHandler.lands.contains((byte)player.dimension)?Minestuck.clientLandEditRange:Minestuck.clientOverworldEditRange)/2;
			
			centerX = EditHandler.centerX;
			centerZ = EditHandler.centerZ;
			
		} else {
			
			EditData data = getData(player.username);
			if(data == null)
				return;
			
			range = (MinestuckSaveHandler.lands.contains((byte)player.dimension)?Minestuck.landEditRange:Minestuck.overworldEditRange)/2;
			
			centerX = data.centerX;
			centerZ = data.centerZ;
			
		}
		if(range < 1)
			return;
		double newX = player.posX;
		double newZ = player.posZ;
		double offset = player.boundingBox.maxX-player.posX;
		if(player.posX > centerX+range-offset)
			newX = centerX+range-offset;
		else if(player.posX < centerX-range+offset)
			newX = centerX-range+offset;
		if(player.posZ > centerZ+range-offset)
			newZ = centerZ+range-offset;
		else if(player.posZ < centerZ-range+offset)
			newZ = centerZ-range+offset;
		
		if(newX != player.posX || newZ != player.posZ)
			player.setPositionAndUpdate(newX, player.posY-(double)player.yOffset, newZ);
		
		
	}

	@Override
	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.PLAYER);
	}

	@Override
	public String getLabel() {
		return "TickEditHandler";
	}
	
}
