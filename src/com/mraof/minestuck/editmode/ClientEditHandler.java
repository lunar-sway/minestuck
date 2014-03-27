package com.mraof.minestuck.editmode;

import java.util.Map.Entry;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import net.minecraftforge.event.world.WorldEvent;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.item.ItemCruxiteArtifact;
import com.mraof.minestuck.item.block.ItemMachine;
import com.mraof.minestuck.network.MinestuckChannelHandler;
import com.mraof.minestuck.network.MinestuckPacket;
import com.mraof.minestuck.network.MinestuckPacket.Type;
import com.mraof.minestuck.network.skaianet.SburbConnection;
import com.mraof.minestuck.network.skaianet.SkaiaClient;
import com.mraof.minestuck.util.AlchemyRecipeHandler;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.util.GristHelper;
import com.mraof.minestuck.util.GristRegistry;
import com.mraof.minestuck.util.GristSet;
import com.mraof.minestuck.util.GristStorage;
import com.mraof.minestuck.util.GristType;
import com.mraof.minestuck.world.storage.MinestuckSaveHandler;

import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class ClientEditHandler {
	
	public final static ClientEditHandler instance = new ClientEditHandler();
	
	static boolean[] givenItems;
	
	static boolean activated;
	
	static int centerX, centerZ;
	
	public static String client;
	
	/**
	 * Used to tell if the client is in edit mode or not.
	 */
	public static boolean isActive() {
		return activated;
	}
	
	public static void onKeyPressed() {
		MinestuckPacket packet = MinestuckPacket.makePacket(Type.CLIENT_EDIT);
		MinestuckChannelHandler.sendToServer(packet);
	}
	
	public static void onClientPackage(String target, int posX, int posZ, boolean[] items) {
		Minecraft mc = Minecraft.getMinecraft();
		EntityClientPlayerMP player = mc.thePlayer;
		if(target != null) {	//Enable edit mode
			activated = true;
			givenItems = items;
			centerX = posX;
			centerZ = posZ;
			client = target;
		} else if(items != null) {
			givenItems = items;
		} else {	//Disable edit mode
			player.fallDistance = 0;
			activated = false;
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
				stack.stackTagCompound.setTag("display", new NBTTagCompound());
			NBTTagList list = new NBTTagList();
			stack.stackTagCompound.getCompoundTag("display").setTag("Lore", list);
			
			GristSet cost;
			if(stack.getItem() instanceof ItemMachine && stack.getItemDamage() < 4) {
				if(stack.getItemDamage() == 1)
					cost = new GristSet(GristType.Shale, 4);
				else cost = Minestuck.clientHardMode&&givenItems[stack.getItemDamage()]?new GristSet(GristType.Build, 100):new GristSet();
			}
			else if(stack.getItem().equals(Minestuck.captchaCard))
				cost = new GristSet();
			else cost = GristRegistry.getGristConversion(stack);
			
			if(cost == null)
				continue;
			for(Entry entry : cost.getHashtable().entrySet()) {
				GristType grist = GristType.values()[(Integer)entry.getKey()];
				String s = EnumChatFormatting.RESET+""+EnumChatFormatting.RESET;
				s += (Integer)entry.getValue() <= have.getGrist(grist)?EnumChatFormatting.GREEN:EnumChatFormatting.RED;
				list.appendTag(new NBTTagString(s+entry.getValue()+" "+grist.getDisplayName()+" ("+have.getGrist(grist)+")"));
			}
			if(cost.isEmpty())
				list.appendTag(new NBTTagString(""+EnumChatFormatting.RESET+EnumChatFormatting.RESET+EnumChatFormatting.GREEN+
						StatCollector.translateToLocal("gui.free")));
		}
	}
	

	@SubscribeEvent
	public void tickEnd(PlayerTickEvent event) {
		if(event.phase != TickEvent.Phase.END || event.player != Minecraft.getMinecraft().thePlayer)
			return;
		EntityPlayer player = event.player;
		if(!(player instanceof EntityClientPlayerMP) || !isActive())
			return;
		
		double range = (MinestuckSaveHandler.lands.contains((byte)player.dimension)?Minestuck.clientLandEditRange:Minestuck.clientOverworldEditRange)/2;
		
//		ServerEditHandler.updatePosition(player, range, centerX, centerZ);
		if(Minestuck.toolTipEnabled)
			addToolTip(player, givenItems);
	}
	
	@SubscribeEvent
	public void onTossEvent(ItemTossEvent event) {
		if(event.entity.worldObj.isRemote && event.player instanceof EntityClientPlayerMP && isActive()) {
			InventoryPlayer inventory = event.player.inventory;
			ItemStack stack = event.entityItem.getEntityItem();
			if((stack.getItem() instanceof ItemMachine && stack.getItemDamage() < 4)) {
				event.setCanceled(true);
//				if(inventory.getItemStack() != null)
//					inventory.inventoryChanged = true;
			}
			else if(stack.getItem().equals(Minestuck.captchaCard) && AlchemyRecipeHandler.getDecodedItem(stack).getItem() instanceof ItemCruxiteArtifact) {
				SburbConnection c = SkaiaClient.getClientConnection(client);
				givenItems[4] = true;
//				if(!Minestuck.clientHardMode)
//					inventory.inventoryChanged = true;
			} else {
				event.setCanceled(true);
				if(inventory.getItemStack() != null)
					inventory.setItemStack(null);
				else inventory.setInventorySlotContents(inventory.currentItem, null);
			}
		}
	}
	
	@SubscribeEvent
	public void onItemPickupEvent(EntityItemPickupEvent event) {
		if(event.entity.worldObj.isRemote && isActive() && event.entityPlayer.equals(Minecraft.getMinecraft().thePlayer))
			event.setCanceled(true);
	}
	
	@SubscribeEvent(priority=EventPriority.NORMAL)
	public void onInteractEvent(PlayerInteractEvent event) {
		
		if(event.entity.worldObj.isRemote && event.entityPlayer instanceof EntityClientPlayerMP && isActive()) {
			if(event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) {
				event.useBlock = Result.DENY;
				ItemStack stack = event.entityPlayer.getCurrentEquippedItem();
				if(stack.getItem().equals(Item.getItemFromBlock(Minestuck.blockMachine)) && stack.getItemDamage() < 4) {
					GristSet cost;
					if(stack.getItemDamage() == 1)
						cost = new GristSet(GristType.Shale, 4);
					else cost = Minestuck.clientHardMode && givenItems[stack.getItemDamage()]?new GristSet(GristType.Build, 100):new GristSet();
					if(!GristHelper.canAfford(GristStorage.getClientGrist(), cost))
						event.setCanceled(true);
				} else if(!(stack.getItem() instanceof ItemBlock) || !GristHelper.canAfford(GristStorage.getClientGrist(), GristRegistry.getGristConversion(stack)))
					event.setCanceled(true);
			} else if(event.action == PlayerInteractEvent.Action.LEFT_CLICK_BLOCK) {
				Block block = event.entity.worldObj.getBlock(event.x, event.y, event.z);
				if(block.getBlockHardness(event.entity.worldObj, event.x, event.y, event.z) < 0
						|| GristStorage.getClientGrist().getGrist(GristType.Build) <= 0)
					event.setCanceled(true);
			} else if(event.action == PlayerInteractEvent.Action.RIGHT_CLICK_AIR)
				event.setCanceled(true);
		}
	}
	
	@SubscribeEvent(priority=EventPriority.LOWEST,receiveCanceled=false)
	public void onBlockPlaced(PlayerInteractEvent event) {
		if(event.entity.worldObj.isRemote && isActive() && event.entityPlayer.equals(Minecraft.getMinecraft().thePlayer)
				&& event.action == Action.LEFT_CLICK_BLOCK && event.useItem != Result.DENY) {
			ItemStack stack = event.entityPlayer.getCurrentEquippedItem();
			if(stack.getItem().equals(Item.getItemFromBlock(Minestuck.blockMachine)) && stack.getItemDamage() < 4)
				givenItems[stack.getItemDamage()+1] = true;
		}
	}
	
	@SubscribeEvent
	public void onAttackEvent(AttackEntityEvent event) {
		if(event.entity.worldObj.isRemote && event.entityPlayer instanceof EntityClientPlayerMP && isActive())
			event.setCanceled(true);
	}
	
	@SubscribeEvent
	public void onWorldUnload(WorldEvent.Unload event) {
		activated = false;
	}
	
}
