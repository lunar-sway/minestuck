package com.mraof.minestuck.editmode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockTrapDoor;
import net.minecraft.block.material.Material;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.PlayerSelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemDoor;
import net.minecraft.item.ItemReed;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldSettings.GameType;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.entity.EntityDecoy;
import com.mraof.minestuck.item.ItemCruxiteArtifact;
import com.mraof.minestuck.item.MinestuckItems;
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
import com.mraof.minestuck.util.Teleport;
import com.mraof.minestuck.world.MinestuckDimensionHandler;

/**
 * Main class to handle the server side of edit mode.
 * Also contains some methods used on both sides.
 * @author kirderf1
 */
public class ServerEditHandler
{
	
	public static final ArrayList<String> commands = new ArrayList<String>(Arrays.asList(new String[]{"effect", "gamemode", "defaultgamemode", "enchant", "xp", "tp", "spreadplayers", "kill", "clear", "spawnpoint", "setworldspawn", "give"}));
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
			Teleport.teleportEntity(player, decoy.dimension, null, false);
		
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
			EntityDecoy decoy = new EntityDecoy((WorldServer) player.worldObj, player);
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
		
		double posX, posY = 0, posZ;
		WorldServer world = MinecraftServer.getServer().worldServerForDimension(c.enteredGame()?c.getClientDimension():c.getClientData().getDimension());
		
		if(world.provider.getDimensionId() != player.worldObj.provider.getDimensionId())
			Teleport.teleportEntity(player, world.provider.getDimensionId(), null, false);
		
		if(c.useCoordinates) {
			posX = c.posX;
			posZ = c.posZ;
			posY = world.getTopSolidOrLiquidBlock(new BlockPos(posX, 0, posZ)).getY();
		} else {
				posX = c.centerX + 0.5;
				posY = world.getTopSolidOrLiquidBlock(new BlockPos(c.centerX, 0, c.centerZ)).getY();
				posZ = c.centerZ + 0.5;
			}
		
		player.closeScreen();
		player.inventory.clear();
		
		player.setGameType(GameType.CREATIVE);
		player.sendPlayerAbilities();
		
		player.setPositionAndUpdate(posX, posY, posZ);
		
		return true;
	}
	
	public static EditData getData(String editor)
	{
		for(EditData data : list)
			if(data.player.getCommandSenderName().equals(editor))
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
		
		player.timeUntilPortal = 60;
		SburbConnection c = data.connection;
		int range = MinestuckDimensionHandler.isLandDimension(player.dimension) ? MinestuckConfig.landEditRange : MinestuckConfig.overworldEditRange;
		
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
			if(DeployList.containsItemStack(stack) && !isBlockItem(stack.getItem()))
			{
				GristSet cost = data.connection.givenItems()[DeployList.getOrdinal(stack)]
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
			if(event.isCanceled())
			{
				event.entityItem.setDead();
				if(inventory.getItemStack() != null)
					inventory.setItemStack(null);
				else inventory.setInventorySlotContents(inventory.currentItem, null);
			}
		}
	}
	
	@SubscribeEvent
	public void onItemPickupEvent(EntityItemPickupEvent event)
	{
		if(!event.entity.worldObj.isRemote && getData(event.entityPlayer.getCommandSenderName()) != null)
			event.setCanceled(true);
	}
	
	/**
	 * Checks if the event should be canceled.
	 */
	@SubscribeEvent(priority=EventPriority.NORMAL)
	public void onItemUseControl(PlayerInteractEvent event)
	{
		
		if(!event.world.isRemote && getData(event.entityPlayer.getCommandSenderName()) != null)
		{
			EditData data = getData(event.entityPlayer.getCommandSenderName());
			if(event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK)
			{
				Block block = event.world.getBlockState(event.pos).getBlock();
				ItemStack stack = event.entityPlayer.getCurrentEquippedItem();
				event.useBlock = stack == null && (block instanceof BlockDoor || block instanceof BlockTrapDoor || block instanceof BlockFenceGate) ? Result.ALLOW : Result.DENY;
				if(event.useBlock == Result.ALLOW)
					return;
				if(stack == null || !isBlockItem(stack.getItem()))
				{
					event.setCanceled(true);
					return;
				}
				
				cleanStackNBT(stack);
				
				if(DeployList.containsItemStack(stack))
				{
					GristSet cost = data.connection.givenItems()[DeployList.getOrdinal(stack)]
							? DeployList.getSecondaryCost(stack) : DeployList.getPrimaryCost(stack);
					if(!GristHelper.canAfford(MinestuckPlayerData.getGristSet(data.connection.getClientName()), cost))
					{
						event.setCanceled(true);
					}
				}
				else if(!isBlockItem(stack.getItem()) || !GristHelper.canAfford(data.connection.getClientName(), stack, false))
				{
					event.setCanceled(true);
				}
				if(event.useItem == Result.DEFAULT)
					event.useItem = Result.ALLOW;
			}
			else if(event.action == PlayerInteractEvent.Action.LEFT_CLICK_BLOCK)
			{
				Block block = event.world.getBlockState(event.pos).getBlock();
				if(block.getBlockHardness(event.world, event.pos) < 0 || block.getMaterial() == Material.portal
						|| GristHelper.getGrist(data.connection.getClientName(), GristType.Build) <= 0)
					event.setCanceled(true);
			}
			else if(event.action == PlayerInteractEvent.Action.RIGHT_CLICK_AIR)
				event.setCanceled(true);
		}
	}
	
	@SubscribeEvent(priority=EventPriority.LOWEST)
	public void onBlockBreak(PlayerInteractEvent event)
	{
		if(!event.entity.worldObj.isRemote && getData(event.entityPlayer.getCommandSenderName()) != null
				&& event.action == PlayerInteractEvent.Action.LEFT_CLICK_BLOCK)
		{
			EditData data = getData(event.entityPlayer.getCommandSenderName());
			GristHelper.decrease(data.connection.getClientName(), new GristSet(GristType.Build,1));
			MinestuckPlayerTracker.updateGristCache(data.connection.getClientName());
		}
	}
	
	@SubscribeEvent(priority=EventPriority.LOWEST)
	public void onBlockPlaced(BlockEvent.PlaceEvent event)
	{
		if(getData(event.player.getCommandSenderName()) != null)
		{
			EditData data = getData(event.player.getCommandSenderName());
			if(event.isCanceled())	//If the event was cancelled server side and not client side, notify the client.
			{
				MinestuckPacket packet = MinestuckPacket.makePacket(Type.SERVER_EDIT, data.connection.givenItems());
				MinestuckChannelHandler.sendToPlayer(packet, event.player);
				return;
			}
			
			ItemStack stack = event.itemInHand;
			if(DeployList.containsItemStack(stack))
			{
				SburbConnection c = data.connection;
				GristSet cost = c.givenItems()[DeployList.getOrdinal(stack)]
						? DeployList.getSecondaryCost(stack) : DeployList.getPrimaryCost(stack);
				c.givenItems()[DeployList.getOrdinal(stack)] = true;
				if(!c.isMain())
					SkaianetHandler.giveItems(c.getClientName());
				if(!cost.isEmpty())
					GristHelper.decrease(c.getClientName(), cost);
			} else
			{
				GristHelper.decrease(data.connection.getClientName(), GristRegistry.getGristConversion(stack));
				MinestuckPlayerTracker.updateGristCache(data.connection.getClientName());
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
		double y = player.posY;
		if(y < 0) {
			y = 0;
			player.motionY = 0;
			player.capabilities.isFlying = true;
		}
		
		double newX = player.posX;
		double newZ = player.posZ;
		double offset = player.getEntityBoundingBox().maxX-player.posX;
		
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
		
		if(newX != player.posX || newZ != player.posZ || y != player.posY)
		{
			player.setPositionAndUpdate(newX, y, newZ);
		}
	}
	
	public static void updateInventory(EntityPlayerMP player, boolean[] givenItems, boolean enteredGame, String client)
	{
		boolean inventoryChanged = false;
		for(int i = 0; i < player.inventory.mainInventory.length; i++)
		{
			ItemStack stack = player.inventory.mainInventory[i];
			if(stack != null &&
					(stack.getItem() == MinestuckItems.captchaCard && AlchemyRecipeHandler.getDecodedItem(stack).getItem() instanceof ItemCruxiteArtifact && enteredGame
					|| !DeployList.containsItemStack(stack) && (GristRegistry.getGristConversion(stack) == null || !isBlockItem(stack.getItem()))))
			{
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
		int availableTier = SessionHandler.availableTier(client);
		for(ItemStack stack : DeployList.getItemList())
			if(givenItems[DeployList.getOrdinal(stack)] && DeployList.getSecondaryCost(stack) == null
					|| DeployList.getTier(stack) > availableTier)
				itemsToRemove.add(stack);
		
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
			cleanStackNBT(stack);
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
	
	@SubscribeEvent(priority=EventPriority.LOWEST, receiveCanceled=false)
	public void onCommandEvent(CommandEvent event)
	{
		if(list.isEmpty())
			return;
		try
		{
			if(commands.contains(event.command.getCommandName()))
			{
				String c = event.command.getCommandName();
				EntityPlayer target;
				Debug.print(c);
				if(c.equals("kill") || (c.equals("clear") || c.equals("spawnpoint")) && event.parameters.length == 0
						|| c.equals("tp") && event.parameters.length != 2 && event.parameters.length != 4
						|| c.equals("setworldspawn") && (event.parameters.length == 0 || event.parameters.length == 3))
					target = CommandBase.getCommandSenderAsPlayer(event.sender);
				else if(c.equals("defaultgamemode") && MinecraftServer.getServer().getForceGamemode())
				{
					for(EditData data : (EditData[]) list.toArray())
						reset(null, 0, data);
					return;
				}
				else if(c.equals("spreadplayers"))
				{
					ArrayList<EntityPlayer> targets = new ArrayList<EntityPlayer>();
					for(int i = 5; i < event.parameters.length; i++)
					{
						String s = event.parameters[i];
						if(PlayerSelector.hasArguments(s))
						{
							Entity[] list = (Entity[]) PlayerSelector.matchEntities(event.sender, s, Entity.class).toArray();
							if(list.length == 0)
								return;
							for(Entity e : list)
								if(e instanceof EntityPlayer)
									targets.add((EntityPlayer) e);
						}
						else
						{
							EntityPlayer player = MinecraftServer.getServer().getConfigurationManager().getPlayerByUsername(s);
							if(player == null)
								return;
							targets.add(player);
						}
					}
					
					for(EntityPlayer player : targets)
						if(getData(player.getCommandSenderName()) != null)
						{
							reset(null, 0, getData(player.getCommandSenderName()));
						}
					return;
				}
				else if(c.equals("gamemode") || c.equals("xp"))
					target = event.parameters.length >= 2 ? CommandBase.getPlayer(event.sender, event.parameters[1]) : CommandBase.getCommandSenderAsPlayer(event.sender);
				else target = CommandBase.getPlayer(event.sender, event.parameters[0]);
				
				if(target != null && getData(target.getCommandSenderName()) != null)
					reset(null, 0, getData(target.getCommandSenderName()));
			}
		}
		catch(CommandException e)
		{}
	}
	
	public static boolean isBlockItem(Item item)
	{
		return item instanceof ItemBlock || item instanceof ItemDoor || item instanceof ItemReed;
	}
	
	public static void cleanStackNBT(ItemStack stack)
	{
		NBTTagCompound nbt = stack.getTagCompound();
		if(nbt != null)
		{
			if(nbt.hasKey("BlockEntityTag"))
				if(stack.getItem().equals(Item.getItemFromBlock(MinestuckBlocks.blockMachine)) && stack.getMetadata() == 0)
				{
					for(Object key : nbt.getCompoundTag("BlockEntityTag").getKeySet())
						if(!key.equals("color"))
							nbt.getCompoundTag("BlockEntityTag").removeTag((String) key);
				} else nbt.removeTag("BlockEntityTag");
			
			nbt.removeTag("ench");
			nbt.removeTag("display");
			
			if(nbt.hasNoTags())
				stack.setTagCompound(null);
		}
	}
	
}
