package com.mraof.minestuck.editmode;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.alchemy.*;
import com.mraof.minestuck.entity.DecoyEntity;
import com.mraof.minestuck.network.MinestuckPacketHandler;
import com.mraof.minestuck.network.ServerEditPacket;
import com.mraof.minestuck.network.skaianet.SburbConnection;
import com.mraof.minestuck.network.skaianet.SkaianetHandler;
import com.mraof.minestuck.tracker.MinestuckPlayerTracker;
import com.mraof.minestuck.util.*;
import com.mraof.minestuck.util.IdentifierHandler.PlayerIdentifier;
import com.mraof.minestuck.world.MinestuckDimensions;
import com.mraof.minestuck.world.storage.PlayerSavedData;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityTravelToDimensionEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;

import java.util.*;

/**
 * Main class to handle the server side of edit mode.
 * Also contains some methods used on both sides.
 * @author kirderf1
 */
public class ServerEditHandler
{
	
	public static final ArrayList<String> commands = new ArrayList<>(Arrays.asList(new String[]{"effect", "gamemode", "defaultgamemode", "enchant", "xp", "tp", "spreadplayers", "kill", "clear", "spawnpoint", "setworldspawn", "give"}));
	public static final ServerEditHandler instance = new ServerEditHandler();
	
	static List<EditData> list = new ArrayList<>();
	
	/**
	 * Called both when any player logged out and when a player pressed the exit button.
	 * @param player
	 */
	public static void onPlayerExit(PlayerEntity player)
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
		
		ServerPlayerEntity player = data.player;
		player.closeScreen();
		DecoyEntity decoy = data.decoy;
		if(player.dimension != decoy.dimension)
			//if(!Teleport.teleportEntity(player, decoy.dimension, null))	TODO
			{
				list.add(data);
				throw new IllegalStateException("Was not able to reset editmode player for "+player.getName()+"! Likely caused by mod collision.");
			}
		
		data.connection.useCoordinates = true;
		data.connection.posX = player.posX;
		data.connection.posZ = player.posZ;
		
		player.setGameType(decoy.gameType);
		
		player.connection.setPlayerLocation(decoy.posX, decoy.posY, decoy.posZ, decoy.rotationYaw, decoy.rotationPitch);
		player.abilities.read(decoy.capabilities);
		player.sendPlayerAbilities();
		player.fallDistance = 0;
		player.setHealth(decoy.getHealth());
		player.getFoodStats().read(decoy.getFoodStatsNBT());
		data.connection.inventory = player.inventory.write(new ListNBT());
		player.inventory.copyInventory(decoy.inventory);
		
		decoy.markedForDespawn = true;
		
		ServerEditPacket packet = ServerEditPacket.exit();
		MinestuckPacketHandler.sendToPlayer(packet, player);
		
		if(damageSource != null && damageSource.getImmediateSource() != player)
			player.attackEntityFrom(damageSource, damage);
	}
	
	public static void newServerEditor(ServerPlayerEntity player, PlayerIdentifier computerOwner, PlayerIdentifier computerTarget)
	{
		if(player.getRidingEntity() == null)
			return;	//Don't want to bother making the decoy able to ride anything right now.
		SburbConnection c = SkaianetHandler.get(player.getServer()).getActiveConnection(computerTarget);
		if(c != null && c.getServerIdentifier().equals(computerOwner) && getData(c) == null && getData(player) == null)
		{
			Debug.info("Activating edit mode on player \""+player.getName()+"\", target player: \""+computerTarget+"\".");
			DecoyEntity decoy = new DecoyEntity((ServerWorld) player.world, player);
			EditData data = new EditData(decoy, player, c);
			if(!c.hasEntered())
			{
				c.centerX = c.getClientData().getPos().getX();
				c.centerZ = c.getClientData().getPos().getZ();
			}
			if(!setPlayerStats(player, c))
			{
				player.sendMessage(new StringTextComponent(TextFormatting.RED+"Failed to activate edit mode."));
				return;
			}
			if(c.inventory != null)
				player.inventory.read(c.inventory);
			decoy.world.addEntity(decoy);
			list.add(data);
			ServerEditPacket packet = ServerEditPacket.activate(computerTarget.getUsername(), c.centerX, c.centerZ, c.givenItems(), DeployList.getDeployListTag(player.getServer(), c));
			MinestuckPacketHandler.sendToPlayer(packet, player);
			MinestuckPlayerTracker.updateGristCache(player.getServer(), c.getClientIdentifier());
		}
	}
	
	static boolean setPlayerStats(ServerPlayerEntity player, SburbConnection c)
	{
		
		double posX, posY = 0, posZ;
		ServerWorld world = player.getServer().getWorld(c.hasEntered() ? c.getClientDimension() : c.getClientData().getDimension());
		
		if(c.useCoordinates)
		{
			posX = c.posX;
			posZ = c.posZ;
			posY = world.getHeight(Heightmap.Type.MOTION_BLOCKING, new BlockPos(posX, 0, posZ)).getY();
		} else
		{
			posX = c.centerX + 0.5;
			posY = world.getHeight(Heightmap.Type.MOTION_BLOCKING, new BlockPos(c.centerX, 0, c.centerZ)).getY();
			posZ = c.centerZ + 0.5;
		}
		
		//if(player.changeDimension(world.getDimension().getType(), new PositionTeleporter(posX, posY, posZ)) == null) TODO
		//	return false;
		
		player.closeScreen();
		player.inventory.clear();
		
		player.setGameType(GameType.CREATIVE);
		player.sendPlayerAbilities();
		
		return true;
	}
	
	public static EditData getData(PlayerEntity editor)
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
	
	public static EditData getData(DecoyEntity decoy) {
		for(EditData data : list)
			if(data.decoy == decoy)
				return data;
		return null;
	}
	
	@SubscribeEvent
	public void tickEnd(TickEvent.PlayerTickEvent event) {
		if(event.phase != TickEvent.Phase.END || event.side == LogicalSide.CLIENT)
			return;
		ServerPlayerEntity player = (ServerPlayerEntity) event.player;
		
		EditData data = getData(player);
		if(data == null)
			return;
		
		SburbConnection c = data.connection;
		int range = MinestuckDimensions.isLandDimension(player.dimension) ? MinestuckConfig.landEditRange.get() : MinestuckConfig.overworldEditRange.get();
		
		updateInventory(player, c.givenItems(), c);
		updatePosition(player, range, c.centerX, c.centerZ);
		
		player.timeUntilPortal = player.getPortalCooldown();
	}
	
	@SubscribeEvent
	public void onTossEvent(ItemTossEvent event)
	{
		PlayerInventory inventory = event.getPlayer().inventory;
		if(!event.getEntity().world.isRemote && getData(event.getPlayer()) != null)
		{
			EditData data = getData(event.getPlayer());
			ItemStack stack = event.getEntityItem().getItem();
			DeployList.DeployEntry entry = DeployList.getEntryForItem(stack, data.connection, event.getEntity().world);
			if(entry != null && !isBlockItem(stack.getItem()))
			{
				int i = DeployList.getOrdinal(entry.getName());
				GristSet cost = data.connection.givenItems()[i]
						? entry.getSecondaryGristCost(data.connection) : entry.getPrimaryGristCost(data.connection);
				if(GristHelper.canAfford(PlayerSavedData.get(event.getEntity().getServer()).getGristSet(data.connection.getClientIdentifier()), cost))
				{
					GristHelper.decrease(event.getPlayer().world, data.connection.getClientIdentifier(), cost);
					MinestuckPlayerTracker.updateGristCache(event.getPlayer().getServer(), data.connection.getClientIdentifier());
					data.connection.givenItems()[i] = true;
					if(!data.connection.isMain())
						SkaianetHandler.get(event.getPlayer().getServer()).giveItems(data.connection.getClientIdentifier());
				}
				else event.setCanceled(true);
			}
			else
			{
				event.setCanceled(true);
			}
			if(event.isCanceled())
			{
				event.getEntityItem().remove();
				if(!inventory.getItemStack().isEmpty())
					inventory.setItemStack(ItemStack.EMPTY);
				else inventory.setInventorySlotContents(inventory.currentItem, ItemStack.EMPTY);
			}
		}
	}
	
	@SubscribeEvent
	public void onItemPickupEvent(EntityItemPickupEvent event)
	{
		if(!event.getEntity().world.isRemote && getData(event.getPlayer()) != null)
			event.setCanceled(true);
	}
	
	@SubscribeEvent(priority=EventPriority.NORMAL)
	public void onRightClickBlockControl(PlayerInteractEvent.RightClickBlock event)
	{
		if(!event.getWorld().isRemote && getData(event.getPlayer()) != null)
		{
			EditData data = getData(event.getPlayer());
			Block block = event.getWorld().getBlockState(event.getPos()).getBlock();
			ItemStack stack = event.getPlayer().getHeldItemMainhand();
			event.setUseBlock(stack.isEmpty() && (block instanceof DoorBlock || block instanceof TrapDoorBlock || block instanceof FenceGateBlock) ? Event.Result.ALLOW : Event.Result.DENY);
			if(event.getUseBlock() == Event.Result.ALLOW)
				return;
			if(stack.isEmpty() || !isBlockItem(stack.getItem()) || event.getHand().equals(Hand.OFF_HAND))
			{
				event.setCanceled(true);
				return;
			}
			
			cleanStackNBT(stack, data.connection, event.getWorld());
			
			DeployList.DeployEntry entry = DeployList.getEntryForItem(stack, data.connection, event.getEntity().world);
			if(entry != null)
			{
				GristSet cost = data.connection.givenItems()[DeployList.getOrdinal(entry.getName())]
						? entry.getSecondaryGristCost(data.connection) : entry.getPrimaryGristCost(data.connection);
				if(!GristHelper.canAfford(PlayerSavedData.get(event.getEntity().getServer()).getGristSet(data.connection.getClientIdentifier()), cost))
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
						event.getPlayer().sendMessage(new TranslationTextComponent("grist.missing",str.toString()));
					}
					event.setCanceled(true);
				}
			}
			else if(!isBlockItem(stack.getItem()) || !GristHelper.canAfford(event.getEntity().world, data.connection.getClientIdentifier(), stack))
			{
				event.setCanceled(true);
			}
			if(event.getUseItem() == Event.Result.DEFAULT)
				event.setUseItem(Event.Result.ALLOW);
		}
	}
	
	@SubscribeEvent(priority=EventPriority.NORMAL)
	public void onLeftClickBlockControl(PlayerInteractEvent.LeftClickBlock event)
	{
		if(!event.getWorld().isRemote && getData(event.getPlayer()) != null)
		{
			EditData data = getData(event.getPlayer());
			BlockState block = event.getWorld().getBlockState(event.getPos());
			if(block.getBlockHardness(event.getWorld(), event.getPos()) < 0 || block.getMaterial() == Material.PORTAL
					|| (GristHelper.getGrist(event.getEntity().world, data.connection.getClientIdentifier(), GristType.BUILD) <= 0 && !MinestuckConfig.gristRefund.get()))
				event.setCanceled(true);
		}
	}
	
	@SubscribeEvent(priority=EventPriority.NORMAL)
	public void onItemUseControl(PlayerInteractEvent.RightClickItem event)
	{
		if(!event.getWorld().isRemote && getData(event.getPlayer()) != null)
		{
			event.setCanceled(true);
		}
	}
	
	@SubscribeEvent(priority=EventPriority.LOWEST)
	public void onBlockBreak(PlayerInteractEvent.LeftClickBlock event)
	{
		if(!event.getEntity().world.isRemote && getData(event.getPlayer()) != null)
		{
			EditData data = getData(event.getPlayer());
			if(!MinestuckConfig.gristRefund.get())
				GristHelper.decrease(event.getWorld(), data.connection.getClientIdentifier(), new GristSet(GristType.BUILD, 1));
			else
			{
				BlockState block = event.getWorld().getBlockState(event.getPos());
				ItemStack stack = block.getBlock().getPickBlock(block, null, event.getWorld(), event.getPos(), event.getPlayer());
				GristSet set = AlchemyCostRegistry.getGristConversion(stack);
				if(set != null && !set.isEmpty())
					GristHelper.increase(event.getWorld(), data.connection.getClientIdentifier(), set);
			}
			MinestuckPlayerTracker.updateGristCache(event.getEntity().getServer(), data.connection.getClientIdentifier());
		}
	}
	
	@SubscribeEvent(priority=EventPriority.LOW)
	public void onBlockPlaced(BlockEvent.EntityPlaceEvent event)
	{
		if(event.getEntity() instanceof ServerPlayerEntity)
		{
			ServerPlayerEntity player = (ServerPlayerEntity) event.getEntity();
			if(getData(player) != null)
			{
				EditData data = getData(player);
				if(event.isCanceled())    //If the event was cancelled server side and not client side, notify the client.
				{
					ServerEditPacket packet = ServerEditPacket.givenItems(data.connection.givenItems());
					MinestuckPacketHandler.sendToPlayer(packet, player);
					return;
				}
				
				ItemStack stack = player.getHeldItemMainhand();	//TODO Make sure offhand isn't used in editmode?
				SburbConnection c = data.connection;
				DeployList.DeployEntry entry = DeployList.getEntryForItem(stack, c, player.world);
				if(entry != null)
				{
					int index = DeployList.getOrdinal(entry.getName());
					GristSet cost = c.givenItems()[index]
							? entry.getSecondaryGristCost(c) : entry.getPrimaryGristCost(c);
					c.givenItems()[index] = true;
					if(!c.isMain())
						SkaianetHandler.get(player.server).giveItems(c.getClientIdentifier());
					if(!cost.isEmpty())
					{
						GristHelper.decrease(player.world, c.getClientIdentifier(), cost);
						MinestuckPlayerTracker.updateGristCache(player.server, data.connection.getClientIdentifier());
					}
					player.inventory.mainInventory.set(player.inventory.currentItem, ItemStack.EMPTY);
				} else
				{
					GristHelper.decrease(player.world, data.connection.getClientIdentifier(), AlchemyCostRegistry.getGristConversion(stack));
					MinestuckPlayerTracker.updateGristCache(player.server, data.connection.getClientIdentifier());
				}
			}
		}
	}
	
	@SubscribeEvent(priority=EventPriority.NORMAL)
	public void onAttackEvent(AttackEntityEvent event)
	{
		if(!event.getEntity().world.isRemote && getData(event.getPlayer()) != null)
			event.setCanceled(true);
	}
	
	/**
	 * Used on both server and client side.
	 */
	public static void updatePosition(PlayerEntity player, double range, int centerX, int centerZ) {
		double y = player.posY;
		if(y < 0) {
			y = 0;
			player.setMotion(player.getMotion().mul(1, 0, 1));
			player.abilities.isFlying = true;
		}
		
		double newX = player.posX;
		double newZ = player.posZ;
		double offset = player.getBoundingBox().maxX-player.posX;
		
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
			player.setMotion(player.getMotion().mul(0, 1, 1));
		
		if(newZ != player.posZ)
			player.setMotion(player.getMotion().mul(1, 1, 0));
		
		if(newX != player.posX || newZ != player.posZ || y != player.posY)
		{
			player.setPositionAndUpdate(newX, y, newZ);
		}
	}
	
	public static void updateInventory(ServerPlayerEntity player, boolean[] givenItems, SburbConnection connection)
	{
		List<DeployList.DeployEntry> deployList = DeployList.getItemList(player.getServer(), connection);
		deployList.removeIf(entry -> givenItems[DeployList.getOrdinal(entry.getName())] && entry.getSecondaryGristCost(connection) == null);
		List<ItemStack> itemList = new ArrayList<>();
		deployList.forEach(deployEntry -> itemList.add(deployEntry.getItemStack(connection, player.world)));
		
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
			} else if(stack.hasTag())
			{
				listSearch :
				{
					for(ItemStack deployStack : itemList)
						if(ItemStack.areItemStacksEqual(deployStack, stack))
							break listSearch;
					stack.setTag(null);
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
			player.getServer().getPlayerList().sendInventory(player);
	}
	
	public static void onServerStopping()
	{	
		for(EditData data : new ArrayList<>(list))
			reset(data);
	}
	
	/*@SubscribeEvent(priority=EventPriority.LOWEST, receiveCanceled=false) TODO Do something about command security
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
	}*/
	
	@SubscribeEvent
	public void onEntityTeleport(EntityTravelToDimensionEvent event)
	{
		if(event.getEntity() instanceof ServerPlayerEntity && getData((ServerPlayerEntity) event.getEntity()) != null)
		{
			event.setCanceled(true);
		}
	}
	
	public static boolean isBlockItem(Item item)
	{
		return item instanceof BlockItem;
	}
	
	public static void cleanStackNBT(ItemStack stack, SburbConnection c, World world)
	{
		if(!DeployList.containsItemStack(stack, c, world))
			stack.setTag(null);
	}
	
	private static List<CompoundNBT> recoverData = new ArrayList<CompoundNBT>();
	
	public static void saveData(CompoundNBT nbt)
	{
		ListNBT nbtList = new ListNBT();
		for(CompoundNBT recoverEntry : recoverData)
			nbtList.add(recoverEntry);
		
		for(EditData data : list)
		{
			CompoundNBT nbtTag = new CompoundNBT();
			UUID id = data.player.getGameProfile().getId();
			nbtTag.putLong("UUID1", id.getLeastSignificantBits());
			nbtTag.putLong("UUID2", id.getMostSignificantBits());
			
			nbtTag.putString("dim", data.decoy.dimension.getRegistryName().toString());
			nbtTag.putDouble("x", data.decoy.posX);
			nbtTag.putDouble("y", data.decoy.posY);
			nbtTag.putDouble("z", data.decoy.posZ);
			nbtTag.putFloat("rotYaw", data.decoy.rotationYaw);
			nbtTag.putFloat("rotPitch", data.decoy.rotationPitch);
			
			nbtTag.putInt("gamemode", data.decoy.gameType.getID());
			nbtTag.put("capabilities", data.decoy.capabilities);
			nbtTag.putFloat("health", data.decoy.getHealth());
			nbtTag.put("food", data.decoy.getFoodStatsNBT());
			nbtTag.put("inv", data.decoy.inventory.write(new ListNBT()));
			
			data.connection.inventory = data.player.inventory.write(new ListNBT());
			
			nbtList.add(nbtTag);
		}
		
		nbt.put("editmodeRecover", nbtList);
	}
	
	public static void loadData(CompoundNBT nbt)
	{
		recoverData.clear();
		if(nbt != null && nbt.contains("editmodeRecover", 9))
		{
			ListNBT nbtList = nbt.getList("editmodeRecover", 10);
			for(int i = 0; i < nbtList.size(); i++)
				recoverData.add(nbtList.getCompound(i));
		}
	}
	
	public static void onPlayerLoggedIn(ServerPlayerEntity player)
	{
		UUID id = player.getGameProfile().getId();
		Iterator<CompoundNBT> iter = recoverData.iterator();
		while(iter.hasNext())
		{
			CompoundNBT nbt = iter.next();
			if(id.getLeastSignificantBits() == nbt.getLong("UUID1") && id.getMostSignificantBits() == nbt.getLong("UUID2"))
			{	//Recover player
				DimensionType type = DimensionType.byName(new ResourceLocation(nbt.getString("dim")));
				if(type == null)
					throw new IllegalStateException("Unable to restore editmode player for "+player.getName()+"! Could not read dimension "+nbt.getString("dim")+".");
				//else if(player.dimension != type && player.changeDimension(type, (world, entity, yaw) -> {}) == null) //TODO
				//	throw new IllegalStateException("Was not able to restore editmode player for "+player.getName()+"! Likely caused by mod collision.");
				
				player.connection.setPlayerLocation(nbt.getDouble("x"), nbt.getDouble("y"), nbt.getDouble("z"), nbt.getFloat("rotYaw"), nbt.getFloat("rotPitch"));
				player.setGameType(GameType.getByID(nbt.getInt("gamemode")));
				player.abilities.read(nbt.getCompound("capabilities"));
				player.sendPlayerAbilities();
				player.fallDistance = 0;
				
				player.setHealth(nbt.getFloat("health"));
				player.getFoodStats().read(nbt.getCompound("food"));
				player.inventory.read(nbt.getList("inv", 10));
				
				iter.remove();
				
				return;
			}
		}
	}
}