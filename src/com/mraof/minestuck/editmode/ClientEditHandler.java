package com.mraof.minestuck.editmode;

import java.util.Map.Entry;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import net.minecraftforge.event.world.WorldEvent;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.client.gui.GuiInventoryReplacer;
import com.mraof.minestuck.network.MinestuckChannelHandler;
import com.mraof.minestuck.network.MinestuckPacket;
import com.mraof.minestuck.network.MinestuckPacket.Type;
import com.mraof.minestuck.util.AlchemyRecipeHandler;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.util.GristAmount;
import com.mraof.minestuck.util.GristHelper;
import com.mraof.minestuck.util.GristRegistry;
import com.mraof.minestuck.util.GristSet;
import com.mraof.minestuck.util.MinestuckPlayerData;
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
		GristSet have = MinestuckPlayerData.getClientGrist();
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
			if(DeployList.containsItemStack(stack))
				cost = Minestuck.clientHardMode&&givenItems[DeployList.getOrdinal(stack)+1]
						?DeployList.getSecondaryCost(stack):DeployList.getPrimaryCost(stack);
			else if(stack.getItem().equals(Minestuck.captchaCard))
				cost = new GristSet();
			else cost = GristRegistry.getGristConversion(stack);
			
			if(cost == null) {
				list.appendTag(new NBTTagString(""+EnumChatFormatting.RESET+EnumChatFormatting.RESET
						+EnumChatFormatting.RED+StatCollector.translateToLocal("gui.notAvailable")));
				continue;
			}
			
			for(Entry<Integer, Integer> entry : cost.getHashtable().entrySet()) {
				GristType grist = GristType.values()[entry.getKey()];
				String s = EnumChatFormatting.RESET + "" + EnumChatFormatting.RESET;
				s += entry.getValue() <= have.getGrist(grist) ? EnumChatFormatting.GREEN : EnumChatFormatting.RED;
				list.appendTag(new NBTTagString(s + entry.getValue() + " " + grist.getDisplayName() + " (" + have.getGrist(grist) + ")"));
			}
			if(cost.isEmpty())
				list.appendTag(new NBTTagString(""+EnumChatFormatting.RESET+EnumChatFormatting.RESET+EnumChatFormatting.GREEN+
						StatCollector.translateToLocal("gui.free")));
		}
	}
	
	@SubscribeEvent
	public void tickEnd(PlayerTickEvent event) {
		if(event.phase != TickEvent.Phase.END || event.player != Minecraft.getMinecraft().thePlayer || !isActive())
			return;
		EntityPlayer player = event.player;
		
		double range = MinestuckSaveHandler.lands.contains((byte)player.dimension)?Minestuck.clientLandEditRange:Minestuck.clientOverworldEditRange;
		
		ServerEditHandler.updatePosition(player, range, centerX, centerZ);
		if(Minestuck.toolTipEnabled)
			addToolTip(player, givenItems);
		
	}
	
	@SubscribeEvent
	public void onTossEvent(ItemTossEvent event) {
		if(event.entity.worldObj.isRemote && event.player instanceof EntityClientPlayerMP && isActive()) {
			InventoryPlayer inventory = event.player.inventory;
			ItemStack stack = event.entityItem.getEntityItem();
			int ordinal = DeployList.getOrdinal(stack)+1;
			if(ordinal > 0)
				if(Block.getBlockFromItem(stack.getItem()) != null)
					event.setCanceled(true);
				else if(GristHelper.canAfford(MinestuckPlayerData.getClientGrist(), Minestuck.clientHardMode&&givenItems[ordinal]
						?DeployList.getSecondaryCost(stack):DeployList.getPrimaryCost(stack)))
					givenItems[ordinal] = true;
			else if(stack.getItem() == Minestuck.captchaCard && AlchemyRecipeHandler.getDecodedItem(stack, false).getItem() == Minestuck.cruxiteArtifact
					&& stack.getTagCompound().getBoolean("punched")) {
				//SburbConnection c = SkaiaClient.getClientConnection(client); //unused
				givenItems[0] = true;
			} else {
				event.setCanceled(true);
				if(inventory.getItemStack() != null)
					inventory.setItemStack(null);
				else inventory.setInventorySlotContents(inventory.currentItem, null);
			}
			if(event.isCanceled())
				event.entityItem.setDead();
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
				if(stack == null)
					return;
				GristSet cost;
				if(DeployList.containsItemStack(stack))
					if(Minestuck.clientHardMode && givenItems[DeployList.getOrdinal(stack)+1])
						cost = DeployList.getSecondaryCost(stack);
					else cost = DeployList.getPrimaryCost(stack);
				else cost = GristRegistry.getGristConversion(stack);
				if(!GristHelper.canAfford(MinestuckPlayerData.getClientGrist(), cost)) {
					StringBuilder str = new StringBuilder();
					if(cost != null)
					{
						for(GristAmount grist : cost.getArray())
						{
							if(cost.getArray().indexOf(grist) != 0)
								str.append(", ");
							str.append(grist.getAmount()+" "+grist.getType().getDisplayName());
						}
						event.entityPlayer.addChatMessage(new ChatComponentTranslation("grist.missing",str.toString()));
					}
					event.setCanceled(true);
				}
				if(event.useItem == Result.DEFAULT)
					event.useItem = Result.ALLOW;
			} else if(event.action == PlayerInteractEvent.Action.LEFT_CLICK_BLOCK) {
				Block block = event.entity.worldObj.getBlock(event.x, event.y, event.z);
				if(block.getBlockHardness(event.entity.worldObj, event.x, event.y, event.z) < 0
						|| MinestuckPlayerData.getClientGrist().getGrist(GristType.Build) <= 0)
					event.setCanceled(true);
			} else if(event.action == PlayerInteractEvent.Action.RIGHT_CLICK_AIR)
				event.setCanceled(true);
		}
	}
	
	@SubscribeEvent(priority=EventPriority.LOWEST,receiveCanceled=false)
	public void onBlockPlaced(PlayerInteractEvent event) {
		if(event.entity.worldObj.isRemote && isActive() && event.entityPlayer.equals(Minecraft.getMinecraft().thePlayer)
				&& event.action == Action.LEFT_CLICK_BLOCK && event.useItem == Result.ALLOW) {
			ItemStack stack = event.entityPlayer.getCurrentEquippedItem();
			if(DeployList.containsItemStack(stack))
				givenItems[DeployList.getOrdinal(stack)+1] = true;
		}
	}
	
	@SubscribeEvent
	public void onAttackEvent(AttackEntityEvent event) {
		if(event.entity.worldObj.isRemote && event.entityPlayer instanceof EntityClientPlayerMP && isActive())
			event.setCanceled(true);
	}
	
	@SubscribeEvent
	public void onWorldUnload(WorldEvent.Unload event) {
		if(event.world.isRemote)
			activated = false;
	}
	
	@SubscribeEvent(priority=EventPriority.HIGH)
	public void onGuiOpened(GuiOpenEvent event) {
		if(isActive() && event.gui instanceof InventoryEffectRenderer && !(event.gui instanceof GuiInventoryReplacer))
				event.gui = new GuiInventoryReplacer(Minecraft.getMinecraft().thePlayer);
	}
	
}
