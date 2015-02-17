package com.mraof.minestuck.editmode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
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
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.client.ClientProxy;
import com.mraof.minestuck.client.gui.playerStats.GuiPlayerStats;
import com.mraof.minestuck.inventory.ContainerEditmode;
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
	
	public static void onKeyPressed()
	{
		MinestuckPacket packet = MinestuckPacket.makePacket(Type.CLIENT_EDIT);
		MinestuckChannelHandler.sendToServer(packet);
	}
	
	public static void onClientPackage(String target, int posX, int posZ, boolean[] items) {
		Minecraft mc = Minecraft.getMinecraft();
		EntityPlayerSP player = mc.thePlayer;
		if(target != null) {	//Enable edit mode
			activated = true;
			givenItems = items;
			centerX = posX;
			centerZ = posZ;
			client = target;
		} else if(items != null) {
			givenItems = items;
		}
		else	//Disable edit mode
		{
			player.fallDistance = 0;
			activated = false;
		}
	}
	
	@SubscribeEvent
	public void addToolTip(ItemTooltipEvent event)
	{
		EntityPlayer player = event.entityPlayer;
		if(player != ClientProxy.getPlayer()/*Probably unnecessary*/ || !isActive())
			return;
		
		GristSet have = MinestuckPlayerData.getClientGrist();
		
		addToolTip(event.itemStack, event.toolTip, have, givenItems);
		
	}
	
	static void addToolTip(ItemStack stack, List<String> toolTip, GristSet have, boolean[] givenItems)
	{
		
		GristSet cost;
		if(DeployList.containsItemStack(stack))
			cost = MinestuckConfig.clientHardMode && givenItems[DeployList.getOrdinal(stack)]
					? DeployList.getSecondaryCost(stack) : DeployList.getPrimaryCost(stack);
		else if(stack.getItem().equals(Minestuck.captchaCard))
			cost = new GristSet();
		else cost = GristRegistry.getGristConversion(stack);
		
		if(cost == null)
		{
			toolTip.add(EnumChatFormatting.RED + StatCollector.translateToLocal("gui.notAvailable"));
			return;
		}
		
		for(Entry<Integer, Integer> entry : cost.getHashtable().entrySet())
		{
			GristType grist = GristType.values()[entry.getKey()];
			String s = "" + (entry.getValue() <= have.getGrist(grist) ? EnumChatFormatting.GREEN : EnumChatFormatting.RED);
			toolTip.add(s + entry.getValue() + " " + grist.getDisplayName() + " (" + have.getGrist(grist) + ")");
		}
		if(cost.isEmpty())
			toolTip.add(EnumChatFormatting.GREEN + StatCollector.translateToLocal("gui.free"));
	}
	
	@SubscribeEvent
	public void tickEnd(PlayerTickEvent event) {
		if(event.phase != TickEvent.Phase.END || event.player != Minecraft.getMinecraft().thePlayer || !isActive())
			return;
		EntityPlayer player = event.player;
		
		double range = MinestuckSaveHandler.lands.contains((byte)player.dimension) ? MinestuckConfig.clientLandEditRange : MinestuckConfig.clientOverworldEditRange;
		
		ServerEditHandler.updatePosition(player, range, centerX, centerZ);
		
	}
	
	@SubscribeEvent
	public void onTossEvent(ItemTossEvent event) {
		if(event.entity.worldObj.isRemote && event.player == ClientProxy.getPlayer() && isActive()) {
			InventoryPlayer inventory = event.player.inventory;
			ItemStack stack = event.entityItem.getEntityItem();
			int ordinal = DeployList.getOrdinal(stack);
			if(ordinal >= 0)
			{
				if(!ServerEditHandler.isBlockItem(stack.getItem()) && GristHelper.canAfford(MinestuckPlayerData.getClientGrist(), MinestuckConfig.clientHardMode && givenItems[ordinal]
						? DeployList.getSecondaryCost(stack) : DeployList.getPrimaryCost(stack)))
					givenItems[ordinal] = true;
				else event.setCanceled(true);
				
			}
			if(event.isCanceled())
			{
				if(inventory.getItemStack() != null)
					inventory.setItemStack(null);
				else inventory.setInventorySlotContents(inventory.currentItem, null);
				event.entityItem.setDead();
			}
		}
	}
	
	@SubscribeEvent
	public void onItemPickupEvent(EntityItemPickupEvent event) {
		if(event.entity.worldObj.isRemote && isActive() && event.entityPlayer.equals(Minecraft.getMinecraft().thePlayer))
			event.setCanceled(true);
	}
	
	@SubscribeEvent(priority=EventPriority.NORMAL)
	public void onInteractEvent(PlayerInteractEvent event)
	{
		
		if(event.entity.worldObj.isRemote && event.entityPlayer == ClientProxy.getPlayer() && isActive())
		{
			if(event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK)
			{
				event.useBlock = Result.DENY;
				ItemStack stack = event.entityPlayer.getCurrentEquippedItem();
				if(stack == null || !ServerEditHandler.isBlockItem(stack.getItem()))
				{
					event.setCanceled(true);
					return;
				}
				
				GristSet cost;
				if(DeployList.containsItemStack(stack))
					if(MinestuckConfig.clientHardMode && givenItems[DeployList.getOrdinal(stack)])
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
				Block block = event.entity.worldObj.getBlockState(event.pos).getBlock();
				if(block.getBlockHardness(event.entity.worldObj, event.pos) < 0
						|| MinestuckPlayerData.getClientGrist().getGrist(GristType.Build) <= 0)
					event.setCanceled(true);
			} else if(event.action == PlayerInteractEvent.Action.RIGHT_CLICK_AIR)
				event.setCanceled(true);
		}
	}
	
	@SubscribeEvent(priority=EventPriority.LOWEST,receiveCanceled=false)
	public void onBlockPlaced(PlayerInteractEvent event) {
		if(event.entity.worldObj.isRemote && isActive() && event.entityPlayer.equals(Minecraft.getMinecraft().thePlayer)
				&& event.action == Action.RIGHT_CLICK_BLOCK && event.useItem == Result.ALLOW) {
			ItemStack stack = event.entityPlayer.getCurrentEquippedItem();
			if(DeployList.containsItemStack(stack))
				givenItems[DeployList.getOrdinal(stack)] = true;
		}
	}
	
	@SubscribeEvent
	public void onAttackEvent(AttackEntityEvent event) {
		if(event.entity.worldObj.isRemote && event.entityPlayer == ClientProxy.getPlayer() && isActive())
			event.setCanceled(true);
	}
	
	@SubscribeEvent
	public void onWorldUnload(WorldEvent.Unload event) {
		if(event.world.isRemote)
			activated = false;
	}
	
	@SubscribeEvent(priority=EventPriority.HIGH)
	public void onGuiOpened(GuiOpenEvent event)
	{
		if(isActive() && event.gui instanceof InventoryEffectRenderer)
		{
				event.setCanceled(true);
				GuiPlayerStats.editmodeTab = GuiPlayerStats.EditmodeGuiType.DEPLOY_LIST;
				GuiPlayerStats.openGui(true);
		}
	}
	
}
