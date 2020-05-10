package com.mraof.minestuck.computer.editmode;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.entity.DecoyEntity;
import com.mraof.minestuck.event.ConnectionClosedEvent;
import com.mraof.minestuck.item.crafting.alchemy.GristCostRecipe;
import com.mraof.minestuck.item.crafting.alchemy.GristHelper;
import com.mraof.minestuck.item.crafting.alchemy.GristSet;
import com.mraof.minestuck.item.crafting.alchemy.GristTypes;
import com.mraof.minestuck.network.MSPacketHandler;
import com.mraof.minestuck.network.ServerEditPacket;
import com.mraof.minestuck.player.PlayerIdentifier;
import com.mraof.minestuck.skaianet.SburbConnection;
import com.mraof.minestuck.skaianet.SkaianetHandler;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.util.Teleport;
import com.mraof.minestuck.world.MSDimensions;
import com.mraof.minestuck.world.storage.MSExtraData;
import com.mraof.minestuck.world.storage.PlayerSavedData;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityTravelToDimensionEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppingEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Main class to handle the server side of edit mode.
 * Also contains some methods used on both sides.
 * @author kirderf1
 */
@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class ServerEditHandler
{
	
	public static final ArrayList<String> commands = new ArrayList<>(Arrays.asList("effect", "gamemode", "defaultgamemode", "enchant", "xp", "tp", "spreadplayers", "kill", "clear", "spawnpoint", "setworldspawn", "give"));
	
	/**
	 * Called both when any player logged out and when a player pressed the exit button.
	 */
	public static void onPlayerExit(PlayerEntity player)
	{
		if(!player.world.isRemote)
			reset(getData(player));
	}
	
	@SubscribeEvent
	public static void onDisconnect(ConnectionClosedEvent event)
	{
		reset(getData(event.getMinecraftServer(), event.getConnection()));
		event.getConnection().useCoordinates = false;
	}
	
	public static void reset(EditData data)
	{
		reset(null, 0, data);
	}
	
	/**
	 * Called when the server stops editing the clients house.
	 * @param damageSource If the process was cancelled by the decoy taking damage, this parameter will be the damage source. Else null.
	 * @param damage If the damageSource isn't null, this is the damage taken, else this parameter is ignored.
	 * @param editData editdata to identify the editmode session
	 */
	public static void reset(DamageSource damageSource, float damage, EditData editData)
	{
		if(editData == null)
			return;
		
		ServerPlayerEntity player = editData.getEditor();
		
		editData.recover();	//TODO handle exception from failed recovery
		
		MSExtraData data = MSExtraData.get(player.world);
		data.removeEditData(editData);
		
		ServerEditPacket packet = ServerEditPacket.exit();
		MSPacketHandler.sendToPlayer(packet, player);
		
		editData.getDecoy().markedForDespawn = true;
		
		if(damageSource != null && damageSource.getImmediateSource() != player)
			player.attackEntityFrom(damageSource, damage);
	}
	
	public static void newServerEditor(ServerPlayerEntity player, PlayerIdentifier computerOwner, PlayerIdentifier computerTarget)
	{
		if(player.isPassenger())
		{
			player.sendMessage(new StringTextComponent("You may not activate editmode while riding something"));
			return;    //Don't want to bother making the decoy able to ride anything right now.
		}
		SburbConnection c = SkaianetHandler.get(player.getServer()).getActiveConnection(computerTarget);
		if(c != null && c.getServerIdentifier().equals(computerOwner) && getData(player.server, c) == null && getData(player) == null)
		{
			Debug.info("Activating edit mode on player \""+player.getName()+"\", target player: \""+computerTarget+"\".");
			DecoyEntity decoy = new DecoyEntity((ServerWorld) player.world, player);
			EditData data = new EditData(decoy, player, c);
			if(!c.hasEntered())
			{
				c.centerX = c.getClientComputer().getPos().getX();
				c.centerZ = c.getClientComputer().getPos().getZ();
			}
			if(!setPlayerStats(player, c))
			{
				player.sendMessage(new StringTextComponent(TextFormatting.RED+"Failed to activate edit mode."));
				return;
			}
			if(c.inventory != null)
				player.inventory.read(c.inventory);
			decoy.world.addEntity(decoy);
			MSExtraData.get(player.world).addEditData(data);
			
			ServerEditPacket packet = ServerEditPacket.activate(computerTarget.getUsername(), c.centerX, c.centerZ, DeployList.getDeployListTag(player.getServer(), c));
			MSPacketHandler.sendToPlayer(packet, player);
			data.sendGristCacheToEditor();
		}
	}
	
	static boolean setPlayerStats(ServerPlayerEntity player, SburbConnection c)
	{
		
		double posX, posY = 0, posZ;
		ServerWorld world = player.getServer().getWorld(c.hasEntered() ? c.getClientDimension() : c.getClientComputer().getDimension());
		
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
		
		if(Teleport.teleportEntity(player, world, posX, posY, posZ) == null)
			return false;
		
		player.closeScreen();
		player.inventory.clear();
		
		player.setGameType(GameType.CREATIVE);
		player.sendPlayerAbilities();
		
		return true;
	}
	
	public static EditData getData(PlayerEntity editor)
	{
		return MSExtraData.get(editor.world).findEditData(editData -> editData.getEditor() == editor);
	}
	
	public static EditData getData(MinecraftServer server, SburbConnection c)
	{
		return MSExtraData.get(server).findEditData(editData -> editData.connection.getClientIdentifier().equals(c.getClientIdentifier()) && editData.connection.getServerIdentifier().equals(c.getServerIdentifier()));
	}
	
	public static EditData getData(DecoyEntity decoy) {
		return MSExtraData.get(decoy.getEntityWorld()).findEditData(editData -> editData.getDecoy() == decoy);
	}
	
	@SubscribeEvent
	public static void tickEnd(TickEvent.PlayerTickEvent event) {
		if(event.phase != TickEvent.Phase.END || event.side == LogicalSide.CLIENT)
			return;
		ServerPlayerEntity player = (ServerPlayerEntity) event.player;
		
		EditData data = getData(player);
		if(data == null)
			return;
		
		SburbConnection c = data.connection;
		int range = MSDimensions.isLandDimension(player.dimension) ? MinestuckConfig.landEditRange.get() : MinestuckConfig.overworldEditRange.get();
		
		updateInventory(player, c);
		updatePosition(player, range, c.centerX, c.centerZ);
		
		player.timeUntilPortal = player.getPortalCooldown();
	}
	
	@SubscribeEvent
	public static void onTossEvent(ItemTossEvent event)
	{
		PlayerInventory inventory = event.getPlayer().inventory;
		if(!event.getEntity().world.isRemote && getData(event.getPlayer()) != null)
		{
			EditData data = getData(event.getPlayer());
			ItemStack stack = event.getEntityItem().getItem();
			DeployEntry entry = DeployList.getEntryForItem(stack, data.connection, event.getEntity().world);
			if(entry != null && !isBlockItem(stack.getItem()))
			{
				GristSet cost = entry.getCurrentCost(data.connection);
				if(GristHelper.canAfford(PlayerSavedData.getData(data.connection.getClientIdentifier(), event.getPlayer().world).getGristCache(), cost))
				{
					GristHelper.decrease(event.getPlayer().world, data.connection.getClientIdentifier(), cost);
					GristHelper.notifyEditPlayer(event.getPlayer().world.getServer(), data.connection.getClientIdentifier(), cost, false);
					data.connection.setHasGivenItem(entry);
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
	public static void onItemPickupEvent(EntityItemPickupEvent event)
	{
		if(!event.getEntity().world.isRemote && getData(event.getPlayer()) != null)
			event.setCanceled(true);
	}
	
	//TODO Slightly unsafe with this approach to check, and then execute in a different event listener. It is probably better to try first, and then reset if we tried and the event got cancelled.
	@SubscribeEvent(priority=EventPriority.NORMAL)
	public static void onRightClickBlockControl(PlayerInteractEvent.RightClickBlock event)
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
			
			DeployEntry entry = DeployList.getEntryForItem(stack, data.connection, event.getEntity().world);
			if(entry != null)
			{
				GristSet cost = entry.getCurrentCost(data.connection);
				if(!GristHelper.canAfford(event.getWorld(), data.connection.getClientIdentifier(), cost))
				{
					if(cost != null)
						event.getPlayer().sendMessage(cost.createMissingMessage());
					event.setCanceled(true);
				}
			}
			else if(!isBlockItem(stack.getItem()) || !GristHelper.canAfford(event.getWorld(), data.connection.getClientIdentifier(), GristCostRecipe.findCostForItem(stack, null, false, event.getWorld())))
			{
				event.setCanceled(true);
			}
			if(event.getUseItem() == Event.Result.DEFAULT)
				event.setUseItem(Event.Result.ALLOW);
		}
	}
	
	@SubscribeEvent(priority=EventPriority.NORMAL)
	public static void onLeftClickBlockControl(PlayerInteractEvent.LeftClickBlock event)
	{
		if(!event.getWorld().isRemote && getData(event.getPlayer()) != null)
		{
			EditData data = getData(event.getPlayer());
			BlockState block = event.getWorld().getBlockState(event.getPos());
			if(block.getBlockHardness(event.getWorld(), event.getPos()) < 0 || block.getMaterial() == Material.PORTAL
					|| (GristHelper.getGrist(event.getEntity().world, data.connection.getClientIdentifier(), GristTypes.BUILD) <= 0 && !MinestuckConfig.gristRefund.get()))
				event.setCanceled(true);
		}
	}
	
	@SubscribeEvent(priority=EventPriority.NORMAL)
	public static void onItemUseControl(PlayerInteractEvent.RightClickItem event)
	{
		if(!event.getWorld().isRemote && getData(event.getPlayer()) != null)
		{
			event.setCanceled(true);
		}
	}
	
	@SubscribeEvent(priority=EventPriority.LOWEST)
	public static void onBlockBreak(PlayerInteractEvent.LeftClickBlock event)
	{
		if(!event.getEntity().world.isRemote && getData(event.getPlayer()) != null)
		{
			EditData data = getData(event.getPlayer());
			if(!MinestuckConfig.gristRefund.get())
			{
				GristHelper.decrease(event.getWorld(), data.connection.getClientIdentifier(), new GristSet(GristTypes.BUILD,1));
				GristHelper.notifyEditPlayer(event.getWorld().getServer(), data.connection.getClientIdentifier(), new GristSet(GristTypes.BUILD, 1), false);
			}
			else
			{
				BlockState block = event.getWorld().getBlockState(event.getPos());
				ItemStack stack = block.getBlock().getPickBlock(block, null, event.getWorld(), event.getPos(), event.getPlayer());
				GristSet set = GristCostRecipe.findCostForItem(stack, null, false, event.getWorld());
				if(set != null && !set.isEmpty())
					GristHelper.increase(event.getWorld(), data.connection.getClientIdentifier(), set);
					GristHelper.notifyEditPlayer(event.getWorld().getServer(), data.connection.getClientIdentifier(), new GristSet(GristTypes.BUILD, 1), true);
			}
		}
	}
	
	@SubscribeEvent(priority=EventPriority.LOW)
	public static void onBlockPlaced(BlockEvent.EntityPlaceEvent event)
	{
		if(event.getEntity() instanceof ServerPlayerEntity)
		{
			ServerPlayerEntity player = (ServerPlayerEntity) event.getEntity();
			if(getData(player) != null)
			{
				EditData data = getData(player);
				if(event.isCanceled())    //If the event was cancelled server side and not client side, notify the client.
				{
					data.sendGivenItemsToEditor();
					return;
				}
				
				ItemStack stack = player.getHeldItemMainhand();	//TODO Make sure offhand isn't used in editmode?
				SburbConnection c = data.connection;
				DeployEntry entry = DeployList.getEntryForItem(stack, c, player.world);
				if(entry != null)
				{
					GristSet cost = entry.getCurrentCost(c);
					c.setHasGivenItem(entry);
					if(!c.isMain())
						SkaianetHandler.get(player.server).giveItems(c.getClientIdentifier());
					if(!cost.isEmpty())
					{
						GristHelper.decrease(player.world, c.getClientIdentifier(), cost);
						GristHelper.notifyEditPlayer(player.world.getServer(), c.getClientIdentifier(), cost, false);
					}
					player.inventory.mainInventory.set(player.inventory.currentItem, ItemStack.EMPTY);
				} else
				{
					GristHelper.decrease(player.world, data.connection.getClientIdentifier(), GristCostRecipe.findCostForItem(stack, null, false, player.getEntityWorld()));
					GristHelper.notifyEditPlayer(player.world.getServer(), data.connection.getClientIdentifier(), GristCostRecipe.findCostForItem(stack, null, false, player.getEntityWorld()), false);
				}
			}
		}
	}
	
	@SubscribeEvent(priority=EventPriority.NORMAL)
	public static void onAttackEvent(AttackEntityEvent event)
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
	
	public static void updateInventory(ServerPlayerEntity player, SburbConnection connection)
	{
		List<DeployEntry> deployList = DeployList.getItemList(player.getServer(), connection);
		deployList.removeIf(entry -> entry.getCurrentCost(connection) == null);
		List<ItemStack> itemList = new ArrayList<>();
		deployList.forEach(deployEntry -> itemList.add(deployEntry.getItemStack(connection, player.world)));
		
		boolean inventoryChanged = false;
		for(int i = 0; i < player.inventory.mainInventory.size(); i++)
		{
			ItemStack stack = player.inventory.mainInventory.get(i);
			if(stack.isEmpty())
				continue;
			if(GristCostRecipe.findCostForItem(stack, null, false, player.getEntityWorld()) == null || !isBlockItem(stack.getItem()))
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
	
	/*@SubscribeEvent(priority=EventPriority.LOWEST, receiveCanceled=false) TODO Do something about command security
	public static void onCommandEvent(CommandEvent event)
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
	public static void onEntityTeleport(EntityTravelToDimensionEvent event)
	{
		if(event.getEntity() instanceof ServerPlayerEntity)
		{
			EditData data = getData((ServerPlayerEntity) event.getEntity());
			// Prevent edit player teleportation unless it is recovering to its initial state
			if(data != null && !data.isRecovering())
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
	
	@SubscribeEvent
	public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event)
	{
		ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
		UUID id = player.getGameProfile().getId();
		EditData.PlayerRecovery recovery = MSExtraData.get(player.world).removePlayerRecovery(id);
		if(recovery != null)
			recovery.recover(player, false);
	}
	
	@SubscribeEvent(priority = EventPriority.HIGHEST)	//Editmode players need to be reset before nei handles the event
	public static void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event)
	{
		ServerEditHandler.onPlayerExit(event.getPlayer());
	}
	
	@SubscribeEvent
	public static void onServerStopping(FMLServerStoppingEvent event)
	{
		MSExtraData.get(event.getServer()).forEachAndClear(ServerEditHandler::reset);
	}
	
	@SubscribeEvent
	public static void serverStarted(FMLServerStartedEvent event)
	{
		SkaianetHandler skaianet = SkaianetHandler.get(event.getServer());
		MSExtraData.get(event.getServer()).recoverConnections(recovery -> recovery.recover(skaianet.getActiveConnection(recovery.getClientPlayer())));
	}
}