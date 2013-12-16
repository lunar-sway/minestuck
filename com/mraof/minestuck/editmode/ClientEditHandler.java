package com.mraof.minestuck.editmode;

import java.util.EnumSet;
import java.util.Set;
import java.util.Map.Entry;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.event.Event;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.WorldEvent;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.item.ItemCardPunched;
import com.mraof.minestuck.item.ItemCruxiteArtifact;
import com.mraof.minestuck.item.ItemMachine;
import com.mraof.minestuck.network.MinestuckPacket;
import com.mraof.minestuck.network.MinestuckPacket.Type;
import com.mraof.minestuck.network.skaianet.SburbConnection;
import com.mraof.minestuck.network.skaianet.SkaiaClient;
import com.mraof.minestuck.util.AlchemyRecipeHandler;
import com.mraof.minestuck.util.GristRegistry;
import com.mraof.minestuck.util.GristSet;
import com.mraof.minestuck.util.GristStorage;
import com.mraof.minestuck.util.GristType;
import com.mraof.minestuck.world.storage.MinestuckSaveHandler;

import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.network.PacketDispatcher;

public class ClientEditHandler implements ITickHandler{
	
	public final static ClientEditHandler instance = new ClientEditHandler();
	
	static boolean[] givenItems;
	
	static NBTTagCompound capabilities;
	
	static PlayerControllerMP controller;
	
	static int centerX, centerZ;
	
	public static String client;
	
	/**
	 * Used to tell if the client is in edit mode or not.
	 */
	public static boolean isActive() {
		return controller != null;
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
	
	public static void onClientPackage(String target, int posX, int posZ, boolean[] items) {
		Minecraft mc = Minecraft.getMinecraft();
		EntityClientPlayerMP player = mc.thePlayer;
		if(target != null) {	//Enable edit mode
			if(controller == null) {	//Prevent the server from screwing up the client too hard if called the wrong time.
				controller = mc.playerController;
				mc.playerController = new SburbEditController(mc, mc.getNetHandler());
				capabilities = new NBTTagCompound();
				player.capabilities.writeCapabilitiesToNBT(capabilities);
			}
			givenItems = items;
			centerX = posX;
			centerZ = posZ;
			client = target;
		} else {	//Disable edit mode
			if(controller != null) {
				mc.playerController = controller;
				controller = null;
				player.capabilities.readCapabilitiesFromNBT(capabilities);
				player.capabilities.allowFlying = mc.playerController.isInCreativeMode();
				player.fallDistance = 0;
				player.capabilities.isFlying = player.capabilities.isFlying && player.capabilities.allowFlying;
				capabilities = null;
			}
		}
	}
	
	public void addToolTip(EntityPlayer player, boolean[] givenItems) {
		GristSet have = GristStorage.getClientGrist();
		for(int i = 0; i < player.inventory.mainInventory.length; i++) {
			ItemStack stack = player.inventory.mainInventory[i];
			if(stack == null)
				continue;
			if(stack.stackTagCompound == null)
				stack.stackTagCompound = new NBTTagCompound();
			if(!stack.stackTagCompound.hasKey("display"))
				stack.stackTagCompound.setCompoundTag("display", new NBTTagCompound());
			NBTTagList list = new NBTTagList();
			stack.stackTagCompound.getCompoundTag("display").setTag("Lore", list);
			
			GristSet cost;
			if(stack.getItem() instanceof ItemMachine && stack.getItemDamage() < 4) {
				if(stack.getItemDamage() == 1)
					cost = new GristSet(GristType.Shale, 4);
				else cost = Minestuck.clientHardMode&&givenItems[stack.getItemDamage()]?new GristSet(GristType.Build, 100):new GristSet();
			}
			else if(stack.getItem().equals(Minestuck.punchedCard))
				cost = new GristSet();
			else cost = GristRegistry.getGristConversion(stack);
			
			for(Entry entry : (Set<Entry>)cost.getHashtable().entrySet()) {
				GristType grist = GristType.values()[(Integer)entry.getKey()];
				String s = EnumChatFormatting.RESET+""+EnumChatFormatting.RESET;
				s += (Integer)entry.getValue() <= have.getGrist(grist)?EnumChatFormatting.GREEN:EnumChatFormatting.RED;
				list.appendTag(new NBTTagString("",s+entry.getValue()+" "+grist.getDisplayName()+" ("+have.getGrist(grist)+")"));
			}
			if(cost.isEmpty())
				list.appendTag(new NBTTagString("",""+EnumChatFormatting.RESET+EnumChatFormatting.RESET+EnumChatFormatting.GREEN+
						StatCollector.translateToLocal("gui.free")));
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
		
		ServerEditHandler.updateInventory(player, givenItems, MinestuckSaveHandler.lands.contains((byte)player.dimension), Minestuck.clientHardMode);
		ServerEditHandler.updatePosition(player, range, centerX, centerZ);
		if(Minestuck.toolTipEnabled)
			addToolTip(player, givenItems);
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
				givenItems[4] = true;
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
	
	@ForgeSubscribe
	public void onWorldUnloadEvent(WorldEvent.Unload event) {
		if(controller != null) {
			Minecraft.getMinecraft().playerController = controller;
			controller = null;
			capabilities = null;
		}
	}
	
}
