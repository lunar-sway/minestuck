package com.mraof.minestuck.editmode;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.alchemy.*;
import com.mraof.minestuck.client.gui.playerStats.GuiPlayerStats;
import com.mraof.minestuck.item.MinestuckItems;
import com.mraof.minestuck.network.MinestuckChannelHandler;
import com.mraof.minestuck.network.MinestuckPacket;
import com.mraof.minestuck.network.MinestuckPacket.Type;
import com.mraof.minestuck.util.*;
import com.mraof.minestuck.world.MinestuckDimensionHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockTrapDoor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

import java.util.List;
import java.util.Map.Entry;

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
	
	public static void onClientPackage(String target, int posX, int posZ, boolean[] items, NBTTagCompound deployList)
	{
		Minecraft mc = Minecraft.getMinecraft();
		EntityPlayerSP player = mc.player;
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
		if(deployList != null)
		{
			DeployList.loadClientDeployList(deployList);
		}
	}
	
	@SubscribeEvent
	public void addToolTip(ItemTooltipEvent event)
	{
		if(!isActive())
			return;
		
		GristSet have = MinestuckPlayerData.getClientGrist();
		
		addToolTip(event.getItemStack(), event.getToolTip(), have, givenItems);
		
	}
	
	static void addToolTip(ItemStack stack, List<String> toolTip, GristSet have, boolean[] givenItems)
	{
		
		GristSet cost;
		DeployList.ClientDeployEntry deployEntry = DeployList.getEntryClient(stack);
		if(deployEntry != null)
			cost = givenItems[deployEntry.getIndex()]
					? deployEntry.getSecondaryCost() : deployEntry.getPrimaryCost();
		else if(stack.getItem().equals(MinestuckItems.captchaCard))
			cost = new GristSet();
		else cost = AlchemyCostRegistry.getGristConversion(stack);
		
		if(cost == null)
		{
			toolTip.add(TextFormatting.RED + I18n.format("gui.notAvailable"));
			return;
		}
		
		for(Entry<GristType, Integer> entry : cost.getMap().entrySet())
		{
			GristType grist = entry.getKey();
			String s = "" + (entry.getValue() <= have.getGrist(grist) ? TextFormatting.GREEN : TextFormatting.RED);
			toolTip.add(s + entry.getValue() + " " + grist.getDisplayName() + " (" + have.getGrist(grist) + ")");
		}
		if(cost.isEmpty())
			toolTip.add(TextFormatting.GREEN + I18n.format("gui.free"));
	}
	
	@SubscribeEvent
	public void tickEnd(PlayerTickEvent event) {
		if(event.phase != TickEvent.Phase.END || event.player != Minecraft.getMinecraft().player || !isActive())
			return;
		EntityPlayer player = event.player;
		
		double range = MinestuckDimensionHandler.isLandDimension(player.dimension) ? MinestuckConfig.clientLandEditRange : MinestuckConfig.clientOverworldEditRange;
		
		ServerEditHandler.updatePosition(player, range, centerX, centerZ);
		
	}
	
	@SubscribeEvent
	public void onTossEvent(ItemTossEvent event)
	{
		if(event.getEntity().world.isRemote && event.getPlayer().isUser() && isActive())
		{
			InventoryPlayer inventory = event.getPlayer().inventory;
			ItemStack stack = event.getEntityItem().getItem();
			DeployList.ClientDeployEntry entry = DeployList.getEntryClient(stack);
			if(entry != null)
			{
				if(!ServerEditHandler.isBlockItem(stack.getItem()) && GristHelper.canAfford(MinestuckPlayerData.getClientGrist(), givenItems[entry.getIndex()]
						? entry.getSecondaryCost() : entry.getPrimaryCost()))
					givenItems[entry.getIndex()] = true;
				else event.setCanceled(true);
				
			}
			if(event.isCanceled())
			{
				if(!inventory.getItemStack().isEmpty())
					inventory.setItemStack(ItemStack.EMPTY);
				else inventory.setInventorySlotContents(inventory.currentItem, ItemStack.EMPTY);
				event.getEntityItem().setDead();
			}
		}
	}
	
	@SubscribeEvent
	public void onItemPickupEvent(EntityItemPickupEvent event) {
		if(event.getEntity().world.isRemote && isActive() && event.getEntityPlayer().equals(Minecraft.getMinecraft().player))
			event.setCanceled(true);
	}
	
	@SubscribeEvent(priority=EventPriority.NORMAL)
	public void onRightClickEvent(PlayerInteractEvent.RightClickBlock event)
	{
		if(event.getWorld().isRemote && event.getEntityPlayer().isUser() && isActive())
		{
			Block block = event.getWorld().getBlockState(event.getPos()).getBlock();
			ItemStack stack = event.getEntityPlayer().getHeldItemMainhand();
			event.setUseBlock((block instanceof BlockDoor || block instanceof BlockTrapDoor || block instanceof BlockFenceGate) ? Result.ALLOW : Result.DENY);
			if(event.getUseBlock() == Result.ALLOW)
				return;
			if(event.getHand().equals(EnumHand.OFF_HAND) || !ServerEditHandler.isBlockItem(stack.getItem()))
			{
				event.setCanceled(true);
				return;
			}
			
			GristSet cost;
			DeployList.ClientDeployEntry entry = DeployList.getEntryClient(stack);
			if(entry != null)
				if(givenItems[entry.getIndex()])
					cost = entry.getSecondaryCost();
				else cost = entry.getPrimaryCost();
			else cost = AlchemyCostRegistry.getGristConversion(stack);
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
					event.getEntityPlayer().sendMessage(new TextComponentTranslation("grist.missing",str.toString()));
				}
				event.setCanceled(true);
			}
			if(event.getUseItem() == Result.DEFAULT)
				event.setUseItem(Result.ALLOW);
		}
	}
	
	@SubscribeEvent(priority=EventPriority.NORMAL)
	public void onLeftClickEvent(PlayerInteractEvent.LeftClickBlock event)
	{
		if(event.getWorld().isRemote && event.getEntityPlayer().isUser() && isActive())
		{
			IBlockState block = event.getWorld().getBlockState(event.getPos());
			if(block.getBlockHardness(event.getWorld(), event.getPos()) < 0 || block.getMaterial() == Material.PORTAL
					|| MinestuckPlayerData.getClientGrist().getGrist(GristType.Build) <= 0)
				event.setCanceled(true);
		}
	}
	
	@SubscribeEvent(priority=EventPriority.NORMAL)
	public void onRightClickAir(PlayerInteractEvent.RightClickItem event)
	{
		if(event.getWorld().isRemote && event.getEntityPlayer().isUser() && isActive())
		{
			event.setCanceled(true);
		}
	}
	
	@SubscribeEvent(priority=EventPriority.LOWEST,receiveCanceled=false)
	public void onBlockPlaced(PlayerInteractEvent.RightClickBlock event)
	{
		if(event.getWorld().isRemote && isActive() && event.getEntityPlayer().equals(Minecraft.getMinecraft().player)
				&& event.getUseItem() == Result.ALLOW) {
			ItemStack stack = event.getEntityPlayer().getHeldItemMainhand();
			DeployList.ClientDeployEntry entry = DeployList.getEntryClient(stack);
			if(entry != null)
				givenItems[entry.getIndex()] = true;
		}
	}
	
	@SubscribeEvent
	public void onAttackEvent(AttackEntityEvent event)
	{
		if(event.getEntity().world.isRemote && event.getEntityPlayer().isUser() && isActive())
			event.setCanceled(true);
	}
	
	@SubscribeEvent
	public void onWorldUnload(WorldEvent.Unload event)
	{
		if(event.getWorld().isRemote)
			activated = false;
	}
	
	@SubscribeEvent(priority=EventPriority.HIGH)
	public void onGuiOpened(GuiOpenEvent event)
	{
		if(isActive() && event.getGui() instanceof InventoryEffectRenderer)
		{
				event.setCanceled(true);
				GuiPlayerStats.editmodeTab = GuiPlayerStats.EditmodeGuiType.DEPLOY_LIST;
				GuiPlayerStats.openGui(true);
		}
	}
	
}
