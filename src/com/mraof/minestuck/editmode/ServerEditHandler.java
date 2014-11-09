package com.mraof.minestuck.editmode;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldSettings.GameType;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.entity.EntityDecoy;
import com.mraof.minestuck.network.MinestuckChannelHandler;
import com.mraof.minestuck.network.MinestuckPacket;
import com.mraof.minestuck.network.MinestuckPacket.Type;
import com.mraof.minestuck.network.skaianet.SburbConnection;
import com.mraof.minestuck.network.skaianet.SessionHandler;
import com.mraof.minestuck.network.skaianet.SkaianetHandler;
import com.mraof.minestuck.tracker.MinestuckPlayerTracker;
import com.mraof.minestuck.util.AlchemyRecipeHandler;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.util.GristHelper;
import com.mraof.minestuck.util.GristRegistry;
import com.mraof.minestuck.util.GristSet;
import com.mraof.minestuck.util.MinestuckPlayerData;
import com.mraof.minestuck.util.GristType;
import com.mraof.minestuck.world.storage.MinestuckSaveHandler;

import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.PlayerTickEvent;

/**
 * Main class to handle the server side of edit mode.
 * Also contains some methods used on both sides.
 * @author kirderf1
 */
public class ServerEditHandler
{
	
	public static final ServerEditHandler instance = new ServerEditHandler();
	
	static List<EditData> list = new ArrayList<EditData>();
	
	/**
	 * Called both when any player logged out and when a player pressed the exit button.
	 * @param player
	 */
	public static void onPlayerExit(EntityPlayer player) {
		if(!player.worldObj.isRemote) {
			reset(null, 0, getData(player.getCommandSenderName()));
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
		player.closeScreen();
		EntityDecoy decoy = data.decoy;
		if(player.dimension != decoy.dimension)
			player.mcServer.getConfigurationManager().transferPlayerToDimension(player, decoy.worldObj.provider.dimensionId, new Teleporter((WorldServer)decoy.worldObj) {
				public void placeInPortal(Entity par1Entity, double par2, double par4, double par6, float par8) {}});
		
		data.connection.useCoordinates = true;
		data.connection.posX = player.posX;
		data.connection.posZ = player.posZ;
		
		player.setGameType(decoy.gameType);
		
		player.playerNetServerHandler.setPlayerLocation(decoy.posX, decoy.posY, decoy.posZ, decoy.rotationYaw, decoy.rotationPitch);
		player.capabilities.readCapabilitiesFromNBT(decoy.capabilities);
		player.sendPlayerAbilities();
		player.fallDistance = 0;
		player.setHealth(decoy.getHealth());
		NBTTagCompound nbt = new NBTTagCompound();
		decoy.foodStats.writeNBT(nbt);
		player.getFoodStats().readNBT(nbt);
		data.connection.inventory = player.inventory.writeToNBT(new NBTTagList());
		player.inventory.copyInventory(decoy.inventory);
		
		decoy.markedForDespawn = true;
		list.remove(data);
		
		MinestuckPacket packet = MinestuckPacket.makePacket(Type.SERVER_EDIT);
		MinestuckChannelHandler.sendToPlayer(packet, player);
		
		if(damageSource != null && damageSource.getSourceOfDamage() != player)
			player.attackEntityFrom(damageSource, damage);
	}
	
	public static void newServerEditor(EntityPlayerMP player, String computerOwner, String computerTarget) {
		if(player.isRiding())
			return;	//Don't want to bother making the decoy able to ride anything right now.
		SburbConnection c = SkaianetHandler.getClientConnection(computerTarget);
		if(c != null && c.getServerName().equals(computerOwner) && getData(c) == null && getData(player.getCommandSenderName()) == null) {
			Debug.print("Activating edit mode on player \""+player.getCommandSenderName()+"\", target player: \""+computerTarget+"\".");
			EntityDecoy decoy = new EntityDecoy(player.worldObj, player);
			EditData data = new EditData(decoy, player, c);
			if(!c.enteredGame()) {
				c.centerX = c.getClientData().getX();
				c.centerZ = c.getClientData().getZ();
			}
			if(!setPlayerStats(player, c)) {
				player.addChatMessage(new ChatComponentText(EnumChatFormatting.RED+"Failed to activate edit mode."));
				return;
			}
			if(c.inventory != null)
				player.inventory.readFromNBT(c.inventory);
			decoy.worldObj.spawnEntityInWorld(decoy);
			list.add(data);
			MinestuckPacket packet = MinestuckPacket.makePacket(Type.SERVER_EDIT, computerTarget, c.centerX, c.centerZ, c.givenItems());
			MinestuckChannelHandler.sendToPlayer(packet, player);
			MinestuckPlayerTracker.updateGristCache(c.getClientName());
		}
	}
	
	static boolean setPlayerStats(EntityPlayerMP player, SburbConnection c) {
		
		//double playerOffset = player.posX - player.boundingBox.maxX; //unused
		
		double posX, posY, posZ;
		WorldServer world = MinecraftServer.getServer().worldServerForDimension(c.enteredGame()?c.getClientDimension():c.getClientData().getDimension());
		
		if(world.provider.dimensionId != player.worldObj.provider.dimensionId)
			player.mcServer.getConfigurationManager().transferPlayerToDimension(player, world.provider.dimensionId, new Teleporter(world) {
				public void placeInPortal(Entity par1Entity, double par2, double par4, double par6, float par8) {}	//To make sure no portal is placed
			});
		
		if(c.useCoordinates) {
			posX = c.posX;
			posZ = c.posZ;
			posY = world.getTopSolidOrLiquidBlock((int)posX, (int)posZ);
		} else {
				posX = c.centerX + 0.5;
				posY = world.getTopSolidOrLiquidBlock(c.centerX, c.centerZ);
				posZ = c.centerZ + 0.5;
			}
		
		player.closeScreen();
		player.inventory.clearInventory(null, -1);
		
		player.setGameType(GameType.CREATIVE);
		player.sendPlayerAbilities();
		
		player.setPositionAndUpdate(posX, posY, posZ);
		
		return true;
	}
	
	public static EditData getData(String editor) {
		for(EditData data : list)
			if(data.player.getCommandSenderName() == editor)
				return data;
		return null;
	}
	
	public static EditData getData(SburbConnection c) {
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
	
	@SubscribeEvent
	public void tickEnd(PlayerTickEvent event) {
		if(event.phase != Phase.END || event.side.isClient())
			return;
		EntityPlayerMP player = (EntityPlayerMP)event.player;
		
		EditData data = getData(player.getCommandSenderName());
		if(data == null)
			return;
		
		SburbConnection c = data.connection;
		int range = MinestuckSaveHandler.lands.contains((byte)player.dimension)?Minestuck.landEditRange:Minestuck.overworldEditRange;
		
		updateInventory(player, c.givenItems(), c.enteredGame(), c.getClientName());
		updatePosition(player, range, c.centerX, c.centerZ);
	}
	
	@SubscribeEvent
	public void onTossEvent(ItemTossEvent event)
	{
		InventoryPlayer inventory = event.player.inventory;
		if(!event.entity.worldObj.isRemote && getData(event.player.getCommandSenderName()) != null)
		{
			EditData data = getData(event.player.getCommandSenderName());
			ItemStack stack = event.entityItem.getEntityItem();
			if(DeployList.containsItemStack(stack) && !(stack.getItem() instanceof ItemBlock))
			{
				GristSet cost = Minestuck.hardMode && data.connection.givenItems()[DeployList.getOrdinal(stack)]
						?DeployList.getSecondaryCost(stack):DeployList.getPrimaryCost(stack);
				if(GristHelper.canAfford(MinestuckPlayerData.getGristSet(data.connection.getClientName()), cost))
				{
					GristHelper.decrease(data.connection.getClientName(), cost);
					MinestuckPlayerTracker.updateGristCache(data.connection.getClientName());
					data.connection.givenItems()[DeployList.getOrdinal(stack)] = true;
					if(!data.connection.isMain())
						SkaianetHandler.giveItems(data.connection.getClientName());
				}
				else event.setCanceled(true);
			}
			else 
			{
				event.setCanceled(true);
			}
		}
		if(event.isCanceled())
		{
			event.entityItem.setDead();
			if(inventory.getItemStack() != null)
				inventory.setItemStack(null);
			else inventory.setInventorySlotContents(inventory.currentItem, null);
		}
	}
	
	@SubscribeEvent
	public void onItemPickupEvent(EntityItemPickupEvent event) {
		if(!event.entity.worldObj.isRemote && getData(event.entityPlayer.getCommandSenderName()) != null)
			event.setCanceled(true);
	}
	
	/**
	 * Checks if the event should be canceled.
	 */
	@SubscribeEvent(priority=EventPriority.NORMAL)
	public void onItemUseControl(PlayerInteractEvent event) {
		
		if(!event.entity.worldObj.isRemote && getData(event.entityPlayer.getCommandSenderName()) != null) {
			EditData data = getData(event.entityPlayer.getCommandSenderName());
			if(event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) {
				event.useBlock = Result.DENY;
				ItemStack stack = event.entityPlayer.getCurrentEquippedItem();
				if(stack == null || !(stack.getItem() instanceof ItemBlock))	//Is there a better way of checking if an item is a block item?
					return;
				if(DeployList.containsItemStack(stack)) {
					GristSet cost = Minestuck.hardMode && data.connection.givenItems()[DeployList.getOrdinal(stack)]
							? DeployList.getSecondaryCost(stack) : DeployList.getPrimaryCost(stack);
					if(!GristHelper.canAfford(MinestuckPlayerData.getGristSet(data.connection.getClientName()), cost)) {
						event.setCanceled(true);
					}
				} else if(!(stack.getItem() instanceof ItemBlock) || !GristHelper.canAfford(data.connection.getClientName(), stack)) {
					event.setCanceled(true);
				}
				if(event.useItem == Result.DEFAULT)
					event.useItem = Result.ALLOW;
			} else if(event.action == PlayerInteractEvent.Action.LEFT_CLICK_BLOCK) {
				Block block = event.entity.worldObj.getBlock(event.x, event.y, event.z);
				if(block.getBlockHardness(event.entity.worldObj, event.x, event.y, event.z) < 0
						|| GristHelper.getGrist(data.connection.getClientName(), GristType.Build) <= 0)
					event.setCanceled(true);
			} else if(event.action == PlayerInteractEvent.Action.RIGHT_CLICK_AIR)
				event.setCanceled(true);
		}
	}
	
	@SubscribeEvent(priority=EventPriority.LOWEST)
	public void onItemUseRead(PlayerInteractEvent event) {
		
		if(!event.entity.worldObj.isRemote && getData(event.entityPlayer.getCommandSenderName()) != null) {
			EditData data = getData(event.entityPlayer.getCommandSenderName());
			if(event.isCanceled()) {	//If the event was cancelled server side and not client side, notify the client.
				MinestuckPacket packet = MinestuckPacket.makePacket(Type.SERVER_EDIT, data.connection.givenItems());
				MinestuckChannelHandler.sendToPlayer(packet, event.entityPlayer);
				return;
			}
			if(event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK && event.useItem == Result.ALLOW) {
				ItemStack stack = event.entityPlayer.getCurrentEquippedItem();
				if(DeployList.containsItemStack(stack)) {
					SburbConnection c = data.connection;
					GristSet cost = Minestuck.hardMode && c.givenItems()[DeployList.getOrdinal(stack)]
							?DeployList.getSecondaryCost(stack):DeployList.getPrimaryCost(stack);
					c.givenItems()[DeployList.getOrdinal(stack)] = true;
					if(!c.isMain())
						SkaianetHandler.giveItems(c.getClientName());
					if(!cost.isEmpty())
						GristHelper.decrease(c.getClientName(), cost);
				} else {
					GristHelper.decrease(data.connection.getClientName(), GristRegistry.getGristConversion(stack));
					MinestuckPlayerTracker.updateGristCache(data.connection.getClientName());
				}
			} else if(event.action == PlayerInteractEvent.Action.LEFT_CLICK_BLOCK) {
				GristHelper.decrease(data.connection.getClientName(), new GristSet(GristType.Build,1));
			}
		}
	}
	
	@SubscribeEvent(priority=EventPriority.NORMAL)
	public void onAttackEvent(AttackEntityEvent event) {
		if(!event.entity.worldObj.isRemote && getData(event.entityPlayer.getCommandSenderName()) != null)
			event.setCanceled(true);
	}
	
	/**
	 * Used on both server and client side.
	 */
	public static void updatePosition(EntityPlayer player, double range, int centerX, int centerZ) {
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
	
	public static void updateInventory(EntityPlayerMP player, boolean[] givenItems, boolean enteredGame, String client)
	{
		boolean inventoryChanged = false;
		for(int i = 0; i < player.inventory.mainInventory.length; i++)
		{
			ItemStack stack = player.inventory.mainInventory[i];
			if(stack != null && (GristRegistry.getGristConversion(stack) == null || !(stack.getItem() instanceof ItemBlock)) && !(DeployList.containsItemStack(stack) ||
					stack.getItem() == Minestuck.captchaCard && AlchemyRecipeHandler.getDecodedItem(stack).getItem() == Minestuck.cruxiteArtifact && (!Minestuck.hardMode || !givenItems[0]) && !enteredGame))
			{	//removes blocks without a grist value and all items from the inventory.
				player.inventory.mainInventory[i] = null;
				inventoryChanged = true;
			}
			if(stack != null && stack.stackSize > 1)
			{
				stack.stackSize = 1;
				inventoryChanged = true;
			}
		}
		
		ArrayList<ItemStack> itemsToRemove = new ArrayList<ItemStack>();
		for(ItemStack stack : DeployList.getItemList())
		{	//Find deploy list items that isn't available.
			boolean shouldHave = !(Minestuck.hardMode && givenItems[DeployList.getOrdinal(stack)] && DeployList.getSecondaryCost(stack) == null
					|| DeployList.getTier(stack) > SessionHandler.availableTier(client));
			if(!shouldHave)
				itemsToRemove.add(stack);
		}
		
		if(player.inventory.getItemStack() != null)
			for(ItemStack stack : itemsToRemove)
				if(player.inventory.getItemStack().equals(stack))
				{
					player.inventory.setItemStack(null);
					inventoryChanged = true;
					break;
				}
		
		for(int i = 0; i < player.inventory.mainInventory.length; i++)
		{
			ItemStack stack = player.inventory.mainInventory[i];
			if(stack == null)
				continue;
			for(ItemStack stack1 : itemsToRemove)
				if(stack.isItemEqual(stack1))
				{
					player.inventory.mainInventory[i] = null;
					inventoryChanged = true;
					break;
				}
		}
		
		if(inventoryChanged)
			MinecraftServer.getServer().getConfigurationManager().syncPlayerInventory(player);
	}
	
	public static void onServerStopping()
	{	
		for(EditData data : new ArrayList<EditData>(list))
			reset(null, 0, data);
	}
	
}
