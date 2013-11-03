package com.mraof.minestuck.util;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.entity.EntityDecoy;
import com.mraof.minestuck.grist.GristRegistry;
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

import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.FoodStats;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;

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
	
	public static void onDisconnect(SburbConnection c) {
		reset(null, 0, getData(c));
		c.useCoordinates = false;
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
		
		data.connection.useCoordinates = true;
		data.connection.posX = player.posX;
		data.connection.posZ = player.posZ;
		
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
		data.connection.inventory = player.inventory.writeToNBT(new NBTTagList());
		player.inventory.copyInventory(decoy.inventory);
		
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
			if(c.inventory != null)
				player.inventory.readFromNBT(c.inventory);
			decoy.worldObj.spawnEntityInWorld(decoy);
			list.add(data);
			Packet250CustomPayload packet = new Packet250CustomPayload();
			packet.channel = "Minestuck";
			packet.data = MinestuckPacket.makePacket(Type.SERVER_EDIT, computerTarget, c.centerX, c.centerZ);
			packet.length = packet.data.length;
			player.playerNetServerHandler.sendPacketToPlayer(packet);
			MinestuckPlayerTracker.updateGristCache(c.getClientName());
		}
	}
	
	static boolean setPlayerStats(EntityPlayerMP player, SburbConnection c) {
		
		double playerOffset = player.posX-player.boundingBox.maxX;
		
		SburbServerManager manager = new SburbServerManager(player.worldObj, player);
		manager.client = c.getClientName();
		player.theItemInWorldManager = manager;
		double posX, posY, posZ;
		World world = MinecraftServer.getServer().worldServerForDimension(c.enteredGame()?c.getClientDimension():c.getClientData().getDimension());
		
		if(world.provider.dimensionId != player.worldObj.provider.dimensionId)
			player.travelToDimension(world.provider.dimensionId);
		
		if(c.useCoordinates) {
			posX = c.posX;
			posZ = c.posZ;
			posY = world.getTopSolidOrLiquidBlock((int)posX, (int)posZ);
		} else if(c.enteredGame()) {
				posX = c.centerX + 0.5;
				posY = world.getTopSolidOrLiquidBlock(c.centerX, c.centerZ);
				posZ = c.centerZ + 0.5;
			} else {
				TileEntityComputer te = SkaianetHandler.getComputer(c.getClientData());
				posX = te.xCoord + 0.5;
				posY = world.getTopSolidOrLiquidBlock(te.xCoord, te.zCoord);
				posZ = te.zCoord + 0.5;
			}
		
		player.closeScreen();
		player.inventory.clearInventory(-1, -1);
		
		player.setPositionAndUpdate(posX, posY, posZ);
		
		return true;
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
			
			centerX = data.connection.centerX;
			centerZ = data.connection.centerZ;
			
		}	//From here, the player must be in edit mode.
		
		for(int i = 0; i < player.inventory.mainInventory.length; i++) {
			ItemStack stack = player.inventory.mainInventory[i];
			if(stack != null && (GristRegistry.getGristConversion(stack) == null || !(stack.getItem() instanceof ItemBlock)))
				player.inventory.mainInventory[i] = null;
		}
		double y = player.posY-player.yOffset;
		if(y < 0) {
			y = 0;
			player.motionY = 0;
			player.capabilities.isFlying = true;
		}
		
		double newX = player.posX;
		double newZ = player.posZ;
		double offset = player.boundingBox.maxX-player.posX;
		
		if(range >= 1) {
			if(player.posX > centerX+range-offset)
				newX = centerX+range-offset;
			else if(player.posX < centerX-range+offset)
				newX = centerX-range+offset;
			if(player.posZ > centerZ+range-offset)
				newZ = centerZ+range-offset;
			else if(player.posZ < centerZ-range+offset)
				newZ = centerZ-range+offset;
		}
		
		if(newX != player.posX)
			player.motionX = 0;
		
		if(newZ != player.posZ)
			player.motionZ = 0;
		
		if(newX != player.posX || newZ != player.posZ || y != player.posY-player.yOffset) {
			player.setPositionAndUpdate(newX, y, newZ);
			//Update gravity if the player is on ground
			
		}
		
	}

	@Override
	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.PLAYER);
	}

	@Override
	public String getLabel() {
		return "TickEditHandler";
	}
	
	@ForgeSubscribe
	public void onTossEvent(ItemTossEvent event) {
		if(event.player.worldObj.isRemote) {
			if(event.player == Minecraft.getMinecraft().thePlayer && isActive())
				event.entityItem.setDead();
		} else {
			if(getData(event.player.username) != null)
				event.entityItem.setDead();
		}
	}
	
	@ForgeSubscribe
	public void onItemPickupEvent(EntityItemPickupEvent event) {
		if(event.entityPlayer.worldObj.isRemote) {
			if(event.entityPlayer == Minecraft.getMinecraft().thePlayer && isActive())
				event.setCanceled(true);
		} else {
			if(getData(event.entityPlayer.username) != null)
				event.setCanceled(true);
		}
	}
	
}
