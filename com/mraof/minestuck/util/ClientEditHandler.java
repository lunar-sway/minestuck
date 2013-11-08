package com.mraof.minestuck.util;

import java.util.EnumSet;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraftforge.event.Event;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.item.ItemCardPunched;
import com.mraof.minestuck.item.ItemCruxiteArtifact;
import com.mraof.minestuck.item.ItemMachine;
import com.mraof.minestuck.network.MinestuckPacket;
import com.mraof.minestuck.network.MinestuckPacket.Type;
import com.mraof.minestuck.network.skaianet.SburbConnection;
import com.mraof.minestuck.network.skaianet.SkaiaClient;
import com.mraof.minestuck.network.skaianet.SkaianetHandler;
import com.mraof.minestuck.world.storage.MinestuckSaveHandler;

import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.network.PacketDispatcher;

public class ClientEditHandler implements ITickHandler{
	
	public final static ClientEditHandler instance = new ClientEditHandler();
	
	static NBTTagCompound capabilities;
	
	static PlayerControllerMP controller;
	
	static int centerX, centerZ;
	
	public static String client;
	
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
			}
			if(capabilities == null) {
				capabilities = new NBTTagCompound();
				player.capabilities.writeCapabilitiesToNBT(capabilities);
			}
			centerX = posX;
			centerZ = posZ;
			client = target;
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

	@Override
	
	public void tickStart(EnumSet<TickType> type, Object... tickData) {}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) {
		EntityPlayer player = (EntityPlayer)tickData[0];
		if(!(player instanceof EntityClientPlayerMP) || !isActive())
			return;
		
		double range = (MinestuckSaveHandler.lands.contains((byte)player.dimension)?Minestuck.clientLandEditRange:Minestuck.clientOverworldEditRange)/2;
		
		SburbConnection c = SkaiaClient.getClientConnection(client);
		
		ServerEditHandler.updateInventory(player, c, Minestuck.clientHardMode);
		ServerEditHandler.updatePosition(player, range, centerX, centerZ);
	}

	@Override
	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.PLAYER);
	}

	@Override
	public String getLabel() {
		return "ClientEditTick";
	}

	@ForgeSubscribe
	public void onTossEvent(ItemTossEvent event) {
		if(event.entity.worldObj.isRemote && event.player instanceof EntityClientPlayerMP && isActive()) {
			InventoryPlayer inventory = event.player.inventory;
			ItemStack stack = event.entityItem.getEntityItem();
			if((stack.getItem() instanceof ItemMachine && stack.getItemDamage() < 4)) {
				event.setCanceled(true);
				if(inventory.getItemStack() != null)
					inventory.inventoryChanged = true;
			}
			else if(stack.getItem() instanceof ItemCardPunched && AlchemyRecipeHandler.getDecodedItem(stack).getItem() instanceof ItemCruxiteArtifact) {
				SburbConnection c = SkaiaClient.getClientConnection(client);
				c.givenItems()[4] = true;
				if(!Minestuck.clientHardMode)
					inventory.inventoryChanged = true;
			} else {
				event.setCanceled(true);
				if(inventory.getItemStack() != null)
					inventory.setItemStack(null);
				else inventory.setInventorySlotContents(inventory.currentItem, null);
			}
		}
	}
	
	@ForgeSubscribe
	public void onItemPickupEvent(EntityItemPickupEvent event) {
		if(event.entity.worldObj.isRemote && event.entityPlayer instanceof EntityClientPlayerMP && isActive())
			event.setCanceled(true);
	}
	
	@ForgeSubscribe
	public void onInteractEvent(PlayerInteractEvent event) {
		
		if(event.entity.worldObj.isRemote && event.entityPlayer instanceof EntityClientPlayerMP && isActive() && event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) {
			event.useBlock = Event.Result.DENY;
			event.useItem = Event.Result.ALLOW;
		}
	}
	
	@ForgeSubscribe
	public void onAttackEvent(AttackEntityEvent event) {
		if(event.entity.worldObj.isRemote && event.entityPlayer instanceof EntityClientPlayerMP && isActive())
			event.setCanceled(true);
	}
	
}
