package com.mraof.minestuck.editmode;

import java.util.List;
import java.util.Map.Entry;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockTrapDoor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
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

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.client.ClientProxy;
import com.mraof.minestuck.client.gui.playerStats.GuiPlayerStats;
import com.mraof.minestuck.item.MinestuckItems;
import com.mraof.minestuck.network.MinestuckChannelHandler;
import com.mraof.minestuck.network.MinestuckPacket;
import com.mraof.minestuck.network.MinestuckPacket.Type;
import com.mraof.minestuck.util.GristAmount;
import com.mraof.minestuck.util.GristHelper;
import com.mraof.minestuck.util.GristRegistry;
import com.mraof.minestuck.util.GristSet;
import com.mraof.minestuck.util.MinestuckPlayerData;
import com.mraof.minestuck.util.GristType;
import com.mraof.minestuck.world.MinestuckDimensionHandler;

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
		if(!isActive())
			return;
		
		GristSet have = MinestuckPlayerData.getClientGrist();
		
		addToolTip(event.getItemStack(), event.getToolTip(), have, givenItems);
		
	}
	
	static void addToolTip(ItemStack stack, List<String> toolTip, GristSet have, boolean[] givenItems)
	{
		
		GristSet cost;
		if(DeployList.containsItemStack(stack))
			cost = givenItems[DeployList.getOrdinal(stack)]
					? DeployList.getSecondaryCost(stack) : DeployList.getPrimaryCost(stack);
		else if(stack.getItem().equals(MinestuckItems.captchaCard))
			cost = new GristSet();
		else cost = GristRegistry.getGristConversion(stack);
		
		if(cost == null)
		{
			toolTip.add(TextFormatting.RED + I18n.translateToLocal("gui.notAvailable"));
			return;
		}
		
		for(Entry<Integer, Integer> entry : cost.getHashtable().entrySet())
		{
			GristType grist = GristType.values()[entry.getKey()];
			String s = "" + (entry.getValue() <= have.getGrist(grist) ? TextFormatting.GREEN : TextFormatting.RED);
			toolTip.add(s + entry.getValue() + " " + grist.getDisplayName() + " (" + have.getGrist(grist) + ")");
		}
		if(cost.isEmpty())
			toolTip.add(TextFormatting.GREEN + I18n.translateToLocal("gui.free"));
	}
	
	@SubscribeEvent
	public void tickEnd(PlayerTickEvent event) {
		if(event.phase != TickEvent.Phase.END || event.player != Minecraft.getMinecraft().thePlayer || !isActive())
			return;
		EntityPlayer player = event.player;
		
		double range = MinestuckDimensionHandler.isLandDimension(player.dimension) ? MinestuckConfig.clientLandEditRange : MinestuckConfig.clientOverworldEditRange;
		
		ServerEditHandler.updatePosition(player, range, centerX, centerZ);
		
	}
	
	@SubscribeEvent
	public void onTossEvent(ItemTossEvent event)
	{
		if(event.getEntity().worldObj.isRemote && event.getPlayer() == ClientProxy.getClientPlayer() && isActive())
		{
			InventoryPlayer inventory = event.getPlayer().inventory;
			ItemStack stack = event.getEntityItem().getEntityItem();
			int ordinal = DeployList.getOrdinal(stack);
			if(ordinal >= 0)
			{
				if(!ServerEditHandler.isBlockItem(stack.getItem()) && GristHelper.canAfford(MinestuckPlayerData.getClientGrist(), givenItems[ordinal]
						? DeployList.getSecondaryCost(stack) : DeployList.getPrimaryCost(stack)))
					givenItems[ordinal] = true;
				else event.setCanceled(true);
				
			}
			if(event.isCanceled())
			{
				if(inventory.getItemStack() != null)
					inventory.setItemStack(null);
				else inventory.setInventorySlotContents(inventory.currentItem, null);
				event.getEntityItem().setDead();
			}
		}
	}
	
	@SubscribeEvent
	public void onItemPickupEvent(EntityItemPickupEvent event) {
		if(event.getEntity().worldObj.isRemote && isActive() && event.getEntityPlayer().equals(Minecraft.getMinecraft().thePlayer))
			event.setCanceled(true);
	}
	
	@SubscribeEvent(priority=EventPriority.NORMAL)
	public void onRightClickEvent(PlayerInteractEvent.RightClickBlock event)
	{
		if(event.getWorld().isRemote && event.getEntityPlayer() == ClientProxy.getClientPlayer() && isActive())
		{
			Block block = event.getWorld().getBlockState(event.getPos()).getBlock();
			ItemStack stack = event.getEntityPlayer().getHeldItemMainhand();
			event.setUseBlock(stack == null && (block instanceof BlockDoor || block instanceof BlockTrapDoor || block instanceof BlockFenceGate) ? Result.ALLOW : Result.DENY);
			if(event.getUseBlock() == Result.ALLOW)
				return;
			if(stack == null || !ServerEditHandler.isBlockItem(stack.getItem()))
			{
				event.setCanceled(true);
				return;
			}
			
			GristSet cost;
			if(DeployList.containsItemStack(stack))
				if(givenItems[DeployList.getOrdinal(stack)])
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
					event.getEntityPlayer().addChatMessage(new TextComponentTranslation("grist.missing",str.toString()));
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
		if(event.getWorld().isRemote && event.getEntityPlayer() == ClientProxy.getClientPlayer() && isActive())
		{
			IBlockState block = event.getWorld().getBlockState(event.getPos());
			if(block.getBlockHardness(event.getWorld(), event.getPos()) < 0 || block.getMaterial() == Material.portal
					|| MinestuckPlayerData.getClientGrist().getGrist(GristType.Build) <= 0)
				event.setCanceled(true);
		}
	}
	
	@SubscribeEvent(priority=EventPriority.NORMAL)
	public void onRightClickAir(PlayerInteractEvent.RightClickItem event)
	{
		if(event.getWorld().isRemote && event.getEntityPlayer() == ClientProxy.getClientPlayer() && isActive())
		{
			event.setCanceled(true);
		}
	}
	
	@SubscribeEvent(priority=EventPriority.LOWEST,receiveCanceled=false)
	public void onBlockPlaced(PlayerInteractEvent.RightClickBlock event)
	{
		if(event.getWorld().isRemote && isActive() && event.getEntityPlayer().equals(Minecraft.getMinecraft().thePlayer)
				&& event.getUseItem() == Result.ALLOW) {
			ItemStack stack = event.getEntityPlayer().getHeldItemMainhand();
			if(DeployList.containsItemStack(stack))
				givenItems[DeployList.getOrdinal(stack)] = true;
		}
	}
	
	@SubscribeEvent
	public void onAttackEvent(AttackEntityEvent event)
	{
		if(event.getEntity().worldObj.isRemote && event.getEntityPlayer() == ClientProxy.getClientPlayer() && isActive())
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
