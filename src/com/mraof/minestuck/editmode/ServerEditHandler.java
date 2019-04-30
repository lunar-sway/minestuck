package com.mraof.minestuck.editmode;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.alchemy.*;
import com.mraof.minestuck.entity.EntityDecoy;
import com.mraof.minestuck.network.MinestuckChannelHandler;
import com.mraof.minestuck.network.MinestuckPacket;
import com.mraof.minestuck.network.MinestuckPacket.Type;
import com.mraof.minestuck.network.skaianet.SburbConnection;
import com.mraof.minestuck.network.skaianet.SkaianetHandler;
import com.mraof.minestuck.tracker.MinestuckPlayerTracker;
import com.mraof.minestuck.util.*;
import com.mraof.minestuck.util.IdentifierHandler.PlayerIdentifier;
import com.mraof.minestuck.world.MinestuckDimensionHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockTrapDoor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.EntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.GameType;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.entity.EntityTravelToDimensionEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

import java.util.*;

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
	public static void onPlayerExit(EntityPlayer player)
	{
		if(!player.world.isRemote)
			reset(getData(player));
	}
	
	public static void onDisconnect(SburbConnection c)
	{
		reset(getData(c));
		c.useCoordinates = false;
	}
	
	public static void reset(EditData data)
	{
		reset(null, 0, data);
	}
	
	/**
	 * Called when the server stops editing the clients house.
	 * @param damageSource If the process was cancelled by the decoy taking damage, this parameter will be the damage source. Else null.
	 * @param damage If the damageSource isn't null, this is the damage taken, else this parameter is ignored.
	 * @param data editdata to identify the editmode session
	 */
	public static void reset(DamageSource damageSource, float damage, EditData data)
	{
		if(data == null)
			return;
		
		list.remove(data);
		
		EntityPlayerMP player = data.player;
		player.closeScreen();
		EntityDecoy decoy = data.decoy;
		if(player.dimension != decoy.dimension)
			if(!Teleport.teleportEntity(player, decoy.dimension, null))
			{
				list.add(data);
				throw new IllegalStateException("Was not able to reset editmode player for "+player.getName()+"! Likely caused by mod collision.");
			}
		
		data.connection.useCoordinates = true;
		data.connection.posX = player.posX;
		data.connection.posZ = player.posZ;
		
		player.setGameType(decoy.gameType);
		
		player.connection.setPlayerLocation(decoy.posX, decoy.posY, decoy.posZ, decoy.rotationYaw, decoy.rotationPitch);
		player.capabilities.readCapabilitiesFromNBT(decoy.capabilities);
		player.sendPlayerAbilities();
		player.fallDistance = 0;
		player.setHealth(decoy.getHealth());
		player.getFoodStats().readNBT(decoy.getFoodStatsNBT());
		data.connection.inventory = player.inventory.writeToNBT(new NBTTagList());
		player.inventory.copyInventory(decoy.inventory);
		
		decoy.markedForDespawn = true;
		
		MinestuckPacket packet = MinestuckPacket.makePacket(Type.SERVER_EDIT);
		MinestuckChannelHandler.sendToPlayer(packet, player);
		
		if(damageSource != null && damageSource.getImmediateSource() != player)
			player.attackEntityFrom(damageSource, damage);
	}
	
	public static void newServerEditor(EntityPlayerMP player, PlayerIdentifier computerOwner, PlayerIdentifier computerTarget)
	{
		if(player.isRiding())
			return;	//Don't want to bother making the decoy able to ride anything right now.
		SburbConnection c = SkaianetHandler.getClientConnection(computerTarget);
		if(c != null && c.getServerIdentifier().equals(computerOwner) && getData(c) == null && getData(player) == null)
		{
			Debug.info("Activating edit mode on player \""+player.getName()+"\", target player: \""+computerTarget+"\".");
			EntityDecoy decoy = new EntityDecoy((WorldServer) player.world, player);
			EditData data = new EditData(decoy, player, c);
			if(!c.enteredGame())
			{
				c.centerX = c.getClientData().getX();
				c.centerZ = c.getClientData().getZ();
			}
			if(!setPlayerStats(player, c))
			{
				player.sendMessage(new TextComponentString(TextFormatting.RED+"Failed to activate edit mode."));
				return;
			}
			if(c.inventory != null)
				player.inventory.readFromNBT(c.inventory);
			decoy.world.spawnEntity(decoy);
			list.add(data);
			MinestuckPacket packet = MinestuckPacket.makePacket(Type.SERVER_EDIT, computerTarget, c.centerX, c.centerZ, c.givenItems(), DeployList.getDeployListTag(c));
			MinestuckChannelHandler.sendToPlayer(packet, player);
			MinestuckPlayerTracker.updateGristCache(c.getClientIdentifier());
		}
	}
	
	static boolean setPlayerStats(EntityPlayerMP player, SburbConnection c)
	{
		
		double posX, posY = 0, posZ;
		WorldServer world = player.getServer().getWorld(c.enteredGame()?c.getClientDimension():c.getClientData().getDimension());
		
		if(c.useCoordinates)
		{
			posX = c.posX;
			posZ = c.posZ;
			posY = world.getTopSolidOrLiquidBlock(new BlockPos(posX, 0, posZ)).getY();
		} else
		{
			posX = c.centerX + 0.5;
			posY = world.getTopSolidOrLiquidBlock(new BlockPos(c.centerX, 0, c.centerZ)).getY();
			posZ = c.centerZ + 0.5;
		}
		
		if(!Teleport.teleportEntity(player, world.provider.getDimension(), null, posX, posY, posZ))
			return false;
		
		player.closeScreen();
		player.inventory.clear();
		
		player.setGameType(GameType.CREATIVE);
		player.sendPlayerAbilities();
		
		return true;
	}
	
	public static EditData getData(EntityPlayer editor)
	{
		for(EditData data : list)
			if(data.player == editor)
				return data;
		return null;
	}
	
	public static EditData getData(SburbConnection c)
	{
		for(EditData data : list)
			if(data.connection.getClientIdentifier().equals(c.getClientIdentifier()) && data.connection.getServerIdentifier().equals(c.getServerIdentifier()))
				return data;
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
		
		EditData data = getData(player);
		if(data == null)
			return;
		
		SburbConnection c = data.connection;
		int range = MinestuckDimensionHandler.isLandDimension(player.dimension) ? MinestuckConfig.landEditRange : MinestuckConfig.overworldEditRange;
		
		updateInventory(player, c.givenItems(), c);
		updatePosition(player, range, c.centerX, c.centerZ);
		
		player.timeUntilPortal = player.getPortalCooldown();
	}
	
	@SubscribeEvent
	public void onTossEvent(ItemTossEvent event)
	{
		InventoryPlayer inventory = event.getPlayer().inventory;
		if(!event.getEntity().world.isRemote && getData(event.getPlayer()) != null)
		{
			EditData data = getData(event.getPlayer());
			ItemStack stack = event.getEntityItem().getItem();
			DeployList.DeployEntry entry = DeployList.getEntryForItem(stack, data.connection);
			if(entry != null && !isBlockItem(stack.getItem()))
			{
				int i = DeployList.getOrdinal(entry.getName());
				GristSet cost = data.connection.givenItems()[i]
						? entry.getSecondaryGristCost(data.connection) : entry.getPrimaryGristCost(data.connection);
				if(GristHelper.canAfford(MinestuckPlayerData.getGristSet(data.connection.getClientIdentifier()), cost))
				{
					GristHelper.decrease(data.connection.getClientIdentifier(), cost);
					MinestuckPlayerTracker.updateGristCache(data.connection.getClientIdentifier());
					data.connection.givenItems()[i] = true;
					if(!data.connection.isMain())
						SkaianetHandler.giveItems(data.connection.getClientIdentifier());
				}
				else event.setCanceled(true);
			}
			else
			{
				event.setCanceled(true);
			}
			if(event.isCanceled())
			{
				event.getEntityItem().setDead();
				if(!inventory.getItemStack().isEmpty())
					inventory.setItemStack(ItemStack.EMPTY);
				else inventory.setInventorySlotContents(inventory.currentItem, ItemStack.EMPTY);
			}
		}
	}
	
	@SubscribeEvent
	public void onItemPickupEvent(EntityItemPickupEvent event)
	{
		if(!event.getEntity().world.isRemote && getData(event.getEntityPlayer()) != null)
			event.setCanceled(true);
	}
	
	@SubscribeEvent(priority=EventPriority.NORMAL)
	public void onRightClickBlockControl(PlayerInteractEvent.RightClickBlock event)
	{
		if(!event.getWorld().isRemote && getData(event.getEntityPlayer()) != null)
		{
			EditData data = getData(event.getEntityPlayer());
			Block block = event.getWorld().getBlockState(event.getPos()).getBlock();
			ItemStack stack = event.getEntityPlayer().getHeldItemMainhand();
			event.setUseBlock(stack.isEmpty() && (block instanceof BlockDoor || block instanceof BlockTrapDoor || block instanceof BlockFenceGate) ? Result.ALLOW : Result.DENY);
			if(event.getUseBlock() == Result.ALLOW)
				return;
			if(stack.isEmpty() || !isBlockItem(stack.getItem()) || event.getHand().equals(EnumHand.OFF_HAND))
			{
				event.setCanceled(true);
				return;
			}
			
			cleanStackNBT(stack, data.connection);
			
			DeployList.DeployEntry entry = DeployList.getEntryForItem(stack, data.connection);
			if(entry != null)
			{
				GristSet cost = data.connection.givenItems()[DeployList.getOrdinal(entry.getName())]
						? entry.getSecondaryGristCost(data.connection) : entry.getPrimaryGristCost(data.connection);
				if(!GristHelper.canAfford(MinestuckPlayerData.getGristSet(data.connection.getClientIdentifier()), cost))
				{
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
			}
			else if(!isBlockItem(stack.getItem()) || !GristHelper.canAfford(data.connection.getClientIdentifier(), stack, false))
			{
				event.setCanceled(true);
			}
			if(event.getUseItem() == Result.DEFAULT)
				event.setUseItem(Result.ALLOW);
		}
	}
	
	@SubscribeEvent(priority=EventPriority.NORMAL)
	public void onLeftClickBlockControl(PlayerInteractEvent.LeftClickBlock event)
	{
		if(!event.getWorld().isRemote && getData(event.getEntityPlayer()) != null)
		{
			EditData data = getData(event.getEntityPlayer());
			IBlockState block = event.getWorld().getBlockState(event.getPos());
			if(block.getBlockHardness(event.getWorld(), event.getPos()) < 0 || block.getMaterial() == Material.PORTAL
					|| (GristHelper.getGrist(data.connection.getClientIdentifier(), GristType.Build) <= 0 && !MinestuckConfig.gristRefund))
				event.setCanceled(true);
		}
	}
	
	@SubscribeEvent(priority=EventPriority.NORMAL)
	public void onItemUseControl(PlayerInteractEvent.RightClickItem event)
	{
		if(!event.getWorld().isRemote && getData(event.getEntityPlayer()) != null)
		{
			event.setCanceled(true);
		}
	}
	
	@SubscribeEvent(priority=EventPriority.LOWEST)
	public void onBlockBreak(PlayerInteractEvent.LeftClickBlock event)
	{
		if(!event.getEntity().world.isRemote && getData(event.getEntityPlayer()) != null)
		{
			EditData data = getData(event.getEntityPlayer());
			if(!MinestuckConfig.gristRefund)
				GristHelper.decrease(data.connection.getClientIdentifier(), new GristSet(GristType.Build,1));
			else
			{
				IBlockState block = event.getWorld().getBlockState(event.getPos());
				ItemStack stack = block.getBlock().getPickBlock(block, null, event.getWorld(), event.getPos(), event.getEntityPlayer());
				GristSet set = AlchemyCostRegistry.getGristConversion(stack);
				if(set != null && !set.isEmpty())
					GristHelper.increase(data.connection.getClientIdentifier(), set);
			}
			MinestuckPlayerTracker.updateGristCache(data.connection.getClientIdentifier());
		}
	}
	
	@SubscribeEvent(priority=EventPriority.LOW)
	public void onBlockPlaced(BlockEvent.PlaceEvent event)
	{
		if(getData(event.getPlayer()) != null)
		{
			EditData data = getData(event.getPlayer());
			if(event.isCanceled())	//If the event was cancelled server side and not client side, notify the client.
			{
				MinestuckPacket packet = MinestuckPacket.makePacket(Type.SERVER_EDIT, data.connection.givenItems());
				MinestuckChannelHandler.sendToPlayer(packet, event.getPlayer());
				return;
			}
			
			ItemStack stack = event.getItemInHand();
			SburbConnection c = data.connection;
			DeployList.DeployEntry entry = DeployList.getEntryForItem(stack, c);
			if(entry != null)
			{
				int index = DeployList.getOrdinal(entry.getName());
				GristSet cost = c.givenItems()[index]
						? entry.getSecondaryGristCost(c) : entry.getPrimaryGristCost(c);
				c.givenItems()[index] = true;
				if(!c.isMain())
					SkaianetHandler.giveItems(c.getClientIdentifier());
				if(!cost.isEmpty())
				{
					GristHelper.decrease(c.getClientIdentifier(), cost);
					MinestuckPlayerTracker.updateGristCache(data.connection.getClientIdentifier());
				}
				event.getPlayer().inventory.mainInventory.set(event.getPlayer().inventory.currentItem, ItemStack.EMPTY);
			} else
			{
				GristHelper.decrease(data.connection.getClientIdentifier(), AlchemyCostRegistry.getGristConversion(stack));
				MinestuckPlayerTracker.updateGristCache(data.connection.getClientIdentifier());
			}
		}
	}
	
	@SubscribeEvent(priority=EventPriority.NORMAL)
	public void onAttackEvent(AttackEntityEvent event)
	{
		if(!event.getEntity().world.isRemote && getData(event.getEntityPlayer()) != null)
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
	
	public static void updateInventory(EntityPlayerMP player, boolean[] givenItems, SburbConnection connection)
	{
		List<DeployList.DeployEntry> deployList = DeployList.getItemList(connection);
		deployList.removeIf(entry -> givenItems[DeployList.getOrdinal(entry.getName())] && entry.getSecondaryGristCost(connection) == null);
		List<ItemStack> itemList = new ArrayList<>();
		deployList.forEach(deployEntry -> itemList.add(deployEntry.getItemStack(connection)));
		
		boolean inventoryChanged = false;
		for(int i = 0; i < player.inventory.mainInventory.size(); i++)
		{
			ItemStack stack = player.inventory.mainInventory.get(i);
			if(stack.isEmpty())
				continue;
			if(AlchemyCostRegistry.getGristConversion(stack) == null || !isBlockItem(stack.getItem()))
			{
				listSearch :
				{
					for(ItemStack deployStack : itemList)
						if(ItemStack.areItemStacksEqual(deployStack, stack))
							break listSearch;
					player.inventory.mainInventory.set(i, ItemStack.EMPTY);
					inventoryChanged = true;
				}
			} else if(stack.hasTagCompound())
			{
				listSearch :
				{
					for(ItemStack deployStack : itemList)
						if(ItemStack.areItemStacksEqual(deployStack, stack))
							break listSearch;
					stack.setTagCompound(null);
					inventoryChanged = true;
				}
			}
			if(stack.getCount() > 1)
			{
				stack.setCount(1);
				inventoryChanged = true;
			}
		}
		
		if(inventoryChanged)
			player.getServer().getPlayerList().syncPlayerInventory(player);
	}
	
	public static void onServerStopping()
	{	
		for(EditData data : new ArrayList<>(list))
			reset(data);
	}
	
	@SubscribeEvent(priority=EventPriority.LOWEST, receiveCanceled=false)
	public void onCommandEvent(CommandEvent event)
	{
		if(list.isEmpty())
			return;
		try
		{
			MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
			if(commands.contains(event.getCommand().getName()))
			{
				String c = event.getCommand().getName();
				EntityPlayer target;
				if(c.equals("kill") || (c.equals("clear") || c.equals("spawnpoint")) && event.getParameters().length == 0
						|| c.equals("tp") && event.getParameters().length != 2 && event.getParameters().length != 4
						|| c.equals("setworldspawn") && (event.getParameters().length == 0 || event.getParameters().length == 3))
					target = CommandBase.getCommandSenderAsPlayer(event.getSender());
				else if(c.equals("defaultgamemode") && server.getForceGamemode())
				{
					for(EditData data : (EditData[]) list.toArray())
						reset(data);
					return;
				}
				else if(c.equals("spreadplayers"))
				{
					ArrayList<EntityPlayer> targets = new ArrayList<EntityPlayer>();
					for(int i = 5; i < event.getParameters().length; i++)
					{
						String s = event.getParameters()[i];
						if(EntitySelector.isSelector(s))
						{
							Entity[] list = (Entity[]) EntitySelector.matchEntities(event.getSender(), s, Entity.class).toArray();
							if(list.length == 0)
								return;
							for(Entity e : list)
								if(e instanceof EntityPlayer)
									targets.add((EntityPlayer) e);
						}
						else
						{
							EntityPlayer player = server.getPlayerList().getPlayerByUsername(s);
							if(player == null)
								return;
							targets.add(player);
						}
					}
					
					for(EntityPlayer player : targets)
						if(getData(player) != null)
						{
							reset(getData(player));
						}
					return;
				}
				else if(c.equals("gamemode") || c.equals("xp"))
					target = event.getParameters().length >= 2 ? CommandBase.getPlayer(server, event.getSender(), event.getParameters()[1]) : CommandBase.getCommandSenderAsPlayer(event.getSender());
				else target = CommandBase.getPlayer(server, event.getSender(), event.getParameters()[0]);
				
				if(target != null && getData(target) != null)
					reset(getData(target));
			}
		}
		catch(CommandException e)
		{}
	}
	
	@SubscribeEvent
	public void onEntityTeleport(EntityTravelToDimensionEvent event)
	{
		if(event.getEntity() instanceof EntityPlayerMP && getData((EntityPlayerMP) event.getEntity()) != null)
		{
			event.setCanceled(true);
		}
	}
	
	public static boolean isBlockItem(Item item)
	{
		return item instanceof ItemBlock || item instanceof ItemDoor || item instanceof ItemBlockSpecial;
	}
	
	public static void cleanStackNBT(ItemStack stack, SburbConnection c)
	{
		if(!DeployList.containsItemStack(stack, c))
			stack.setTagCompound(null);
	}
	
	private static List<NBTTagCompound> recoverData = new ArrayList<NBTTagCompound>();
	
	public static void saveData(NBTTagCompound nbt)
	{
		NBTTagList nbtList = new NBTTagList();
		for(NBTTagCompound recoverEntry : recoverData)
			nbtList.appendTag(recoverEntry);
		
		for(EditData data : list)
		{
			NBTTagCompound nbtTag = new NBTTagCompound();
			UUID id = data.player.getGameProfile().getId();
			nbtTag.setLong("UUID1", id.getLeastSignificantBits());
			nbtTag.setLong("UUID2", id.getMostSignificantBits());
			
			nbtTag.setInteger("dim", data.decoy.dimension);
			nbtTag.setDouble("x", data.decoy.posX);
			nbtTag.setDouble("y", data.decoy.posY);
			nbtTag.setDouble("z", data.decoy.posZ);
			nbtTag.setFloat("rotYaw", data.decoy.rotationYaw);
			nbtTag.setFloat("rotPitch", data.decoy.rotationPitch);
			
			nbtTag.setInteger("gamemode", data.decoy.gameType.getID());
			nbtTag.setTag("capabilities", data.decoy.capabilities);
			nbtTag.setFloat("health", data.decoy.getHealth());
			nbtTag.setTag("food", data.decoy.getFoodStatsNBT());
			nbtTag.setTag("inv", data.decoy.inventory.writeToNBT(new NBTTagList()));
			
			data.connection.inventory = data.player.inventory.writeToNBT(new NBTTagList());
			
			nbtList.appendTag(nbtTag);
		}
		
		nbt.setTag("editmodeRecover", nbtList);
	}
	
	public static void loadData(NBTTagCompound nbt)
	{
		recoverData.clear();
		if(nbt != null && nbt.hasKey("editmodeRecover", 9))
		{
			NBTTagList nbtList = nbt.getTagList("editmodeRecover", 10);
			for(int i = 0; i < nbtList.tagCount(); i++)
				recoverData.add(nbtList.getCompoundTagAt(i));
		}
	}
	
	public static void onPlayerLoggedIn(EntityPlayerMP player)
	{
		UUID id = player.getGameProfile().getId();
		Iterator<NBTTagCompound> iter = recoverData.iterator();
		while(iter.hasNext())
		{
			NBTTagCompound nbt = iter.next();
			if(id.getLeastSignificantBits() == nbt.getLong("UUID1") && id.getMostSignificantBits() == nbt.getLong("UUID2"))
			{	//Recover player
				if(player.dimension != nbt.getInteger("dim"))
					if(!Teleport.teleportEntity(player, nbt.getInteger("dim"), null))
						throw new IllegalStateException("Was not able to restore editmode player for "+player.getName()+"! Likely caused by mod collision.");
				
				player.connection.setPlayerLocation(nbt.getDouble("x"), nbt.getDouble("y"), nbt.getDouble("z"), nbt.getFloat("rotYaw"), nbt.getFloat("rotPitch"));
				player.setGameType(GameType.getByID(nbt.getInteger("gamemode")));
				player.capabilities.readCapabilitiesFromNBT(nbt.getCompoundTag("capabilities"));
				player.sendPlayerAbilities();
				player.fallDistance = 0;
				
				player.setHealth(nbt.getFloat("health"));
				player.getFoodStats().readNBT(nbt.getCompoundTag("food"));
				player.inventory.readFromNBT(nbt.getTagList("inv", 10));
				
				iter.remove();
				
				return;
			}
		}
	}
}