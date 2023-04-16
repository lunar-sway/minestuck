package com.mraof.minestuck.computer.editmode;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.alchemy.*;
import com.mraof.minestuck.block.machine.EditmodeDestroyable;
import com.mraof.minestuck.entity.DecoyEntity;
import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.entity.ServerCursorEntity;
import com.mraof.minestuck.event.ConnectionClosedEvent;
import com.mraof.minestuck.event.SburbEvent;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.network.MSPacketHandler;
import com.mraof.minestuck.network.ServerEditPacket;
import com.mraof.minestuck.player.GristCache;
import com.mraof.minestuck.player.PlayerIdentifier;
import com.mraof.minestuck.skaianet.SburbConnection;
import com.mraof.minestuck.skaianet.SburbHandler;
import com.mraof.minestuck.skaianet.SkaianetHandler;
import com.mraof.minestuck.util.MSCapabilities;
import com.mraof.minestuck.util.Teleport;
import com.mraof.minestuck.world.MSDimensions;
import com.mraof.minestuck.world.storage.MSExtraData;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.SectionPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityTravelToDimensionEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.event.server.ServerStoppedEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

/**
 * Main class to handle the server side of edit mode.
 * Also contains some methods used on both sides.
 * @author kirderf1
 */
@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class ServerEditHandler	//TODO Consider splitting this class into two
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	public static final ArrayList<String> commands = new ArrayList<>(Arrays.asList("effect", "gamemode", "defaultgamemode", "enchant", "xp", "tp", "spreadplayers", "kill", "clear", "spawnpoint", "setworldspawn", "give"));
	
	static final Map<SburbConnection, Vec3> lastEditmodePos = new HashMap<>();
	
	/**
	 * Called both when any player logged out and when a player pressed the exit button.
	 */
	public static void onPlayerExit(Player player)
	{
		if(!player.level.isClientSide)
			reset(getData(player));
	}
	
	@SubscribeEvent
	public static void serverStopped(ServerStoppedEvent event)
	{
		lastEditmodePos.clear();
	}
	
	@SubscribeEvent
	public static void onDisconnect(ConnectionClosedEvent event)
	{
		reset(getData(event.getMinecraftServer(), event.getConnection()));
		lastEditmodePos.remove(event.getConnection());
	}
	
	@SubscribeEvent
	public static void onEntry(SburbEvent.OnEntry event)
	{
		lastEditmodePos.remove(event.getConnection());
	}
	
	@SubscribeEvent
	public static void onPlayerCloneEvent(PlayerEvent.Clone event)
	{
		EditData prevData = getData(event.getOriginal());
		if(prevData != null && event.getEntity() instanceof ServerPlayer player)
		{
			//take measures to prevent editmode data from ending up with an invalid player entity
			LOGGER.error("Minestuck failed to prevent death or different cloning event for player {}. Applying measure to reduce problems", event.getEntity().getName().getString());
			
			MSExtraData data = MSExtraData.get(event.getEntity().level);
			data.removeEditData(prevData);
			data.addEditData(new EditData(prevData.getDecoy(), player, prevData.connection));
		}
	}
	
	@SubscribeEvent
	public static void onLivingDamage(LivingDamageEvent event)
	{
		if(event.getEntity() instanceof ServerPlayer player && getData(player) != null)
			event.setCanceled(true);
	}
	
	@SubscribeEvent
	public static void onLivingDeath(LivingDeathEvent event)
	{
		if(event.getEntity() instanceof ServerPlayer player && getData(player) != null)
			event.setCanceled(true);
	}
	
	public static void reset(EditData data)
	{
		reset(null, 0, data);
	}
	
	/**
	 * Called when the server stops editing the clients house.
	 * @param damageSource If the process was cancelled by the decoy taking damage, this parameter will be the damage source. Else null.
	 * @param damage If the damageSource isn't null, this is the damage taken, else this parameter is ignored.
	 * @param editData edit-data that identifies the editmode session
	 */
	public static void reset(DamageSource damageSource, float damage, EditData editData)
	{
		partialReset(damageSource, damage, editData);
		
		if(editData == null)
			return;
		
		MSExtraData data = MSExtraData.get(editData.getEditor().level);
		data.removeEditData(editData);
	}
	
	private static void partialReset(EditData data)
	{
		partialReset(null, 0, data);
	}
	private static void partialReset(DamageSource damageSource, float damage, EditData editData)
	{
		if(editData == null)
			return;
		
		ServerPlayer player = editData.getEditor();
		
		editData.recover();	//TODO handle exception from failed recovery
		
		ServerEditPacket packet = ServerEditPacket.exit();
		MSPacketHandler.sendToPlayer(packet, player);
		
		editData.getDecoy().markedForDespawn = true;
		
		if(damageSource != null && damageSource.getDirectEntity() != player)
			player.hurt(damageSource, damage);
	}
	
	public static void newServerEditor(ServerPlayer player, PlayerIdentifier computerOwner, PlayerIdentifier computerTarget)
	{
		if(player.isPassenger())
		{
			player.sendSystemMessage(Component.literal("You may not activate editmode while riding something"));
			return;    //Don't want to bother making the decoy able to ride anything right now.
		}
		SburbConnection c = SkaianetHandler.get(player.getServer()).getActiveConnection(computerTarget);
		if(c != null && c.getServerIdentifier().equals(computerOwner) && getData(player.server, c) == null && getData(player) == null)
		{
			LOGGER.info("Activating edit mode on player \"{}\", target player: \"{}\".", player.getName().getString(), computerTarget);
			DecoyEntity decoy = new DecoyEntity((ServerLevel) player.level, player);
			EditData data = new EditData(decoy, player, c);

			if(!setPlayerStats(player, c))
			{
				player.sendSystemMessage(Component.literal("Failed to activate edit mode.").withStyle(ChatFormatting.RED));
				return;
			}
			if(c.getEditmodeInventory() != null)
				player.getInventory().load(c.getEditmodeInventory());
			decoy.level.addFreshEntity(decoy);
			MSExtraData.get(player.level).addEditData(data);

			BlockPos center = getEditmodeCenter(c);
			ServerEditPacket packet = ServerEditPacket.activate(computerTarget.getUsername(), center.getX(), center.getZ(), DeployList.getDeployListTag(player.getServer(), c));
			MSPacketHandler.sendToPlayer(packet, player);
			data.sendGristCacheToEditor();
			data.sendCacheLimitToEditor();
		}
	}
	
	static boolean setPlayerStats(ServerPlayer player, SburbConnection c)
	{
		
		double posX, posY, posZ;
		ServerLevel level = player.getServer().getLevel(c.hasEntered() ? c.getClientDimension() : c.getClientComputer().getPosForEditmode().dimension());
		
		if(lastEditmodePos.containsKey(c))
		{
			Vec3 lastPos = lastEditmodePos.get(c);
			posX = lastPos.x;
			posZ = lastPos.z;
		} else
		{
			BlockPos center = getEditmodeCenter(c);
			posX = center.getX() + 0.5;
			posZ = center.getZ() + 0.5;
		}
		posY = getMotionBlockingY(level, Mth.floor(posX), Mth.floor(posZ));
		
		if(Teleport.teleportEntity(player, level, posX, posY, posZ) == null)
			return false;
		
		player.closeContainer();
		player.getInventory().clearContent();
		
		player.setGameMode(GameType.CREATIVE);
		player.onUpdateAbilities();
		
		return true;
	}
	
	//Helper function to force a chunk to load, to then get the top block
	private static int getMotionBlockingY(ServerLevel level, int x, int z)
	{
		return level.getChunk(SectionPos.blockToSectionCoord(x), SectionPos.blockToSectionCoord(z))
				.getHeight(Heightmap.Types.MOTION_BLOCKING, x, z) + 1;
	}
	
	public static void resendEditmodeStatus(ServerPlayer editor)
	{
		EditData data = getData(editor);
		if(data != null)
		{
			BlockPos center = getEditmodeCenter(data.connection);
			ServerEditPacket packet = ServerEditPacket.activate(data.connection.getClientIdentifier().getUsername(), center.getX(), center.getZ(), DeployList.getDeployListTag(editor.getServer(), data.connection));
			MSPacketHandler.sendToPlayer(packet, editor);
			data.sendGristCacheToEditor();
			data.sendCacheLimitToEditor();
		} else
		{
			ServerEditPacket packet = ServerEditPacket.exit();
			MSPacketHandler.sendToPlayer(packet, editor);
		}
	}
	
	public static EditData getData(Player editor)
	{
		return MSExtraData.get(editor.level).findEditData(editData -> editData.getEditor() == editor);
	}
	
	public static EditData getData(MinecraftServer server, SburbConnection c)
	{
		return MSExtraData.get(server).findEditData(editData -> editData.connection.getClientIdentifier().equals(c.getClientIdentifier()) && editData.connection.getServerIdentifier().equals(c.getServerIdentifier()));
	}
	
	public static EditData getData(MinecraftServer server, PlayerIdentifier client)
	{
		return MSExtraData.get(server).findEditData(editData -> editData.connection.getClientIdentifier().equals(client));
	}
	
	public static EditData getData(DecoyEntity decoy) {
		return MSExtraData.get(decoy.getCommandSenderWorld()).findEditData(editData -> editData.getDecoy() == decoy);
	}

	private static BlockPos getEditmodeCenter(SburbConnection connection)
	{
		GlobalPos computerPos = connection.getClientComputer().getPosForEditmode();
		if(computerPos == null)
			throw new IllegalStateException("Connection has to be active with a computer position to be used here");
		if(connection.hasEntered())
			return new BlockPos(0, 0, 0);
		else return computerPos.pos();
	}
	
	@SubscribeEvent
	public static void tickEnd(TickEvent.PlayerTickEvent event) {
		if(event.phase != TickEvent.Phase.END || event.side == LogicalSide.CLIENT)
			return;
		ServerPlayer player = (ServerPlayer) event.player;
		
		EditData data = getData(player);
		if(data == null)
			return;
		
		SburbConnection c = data.connection;
		int range = MSDimensions.isLandDimension(player.server, player.level.dimension()) ? MinestuckConfig.SERVER.landEditRange.get() : MinestuckConfig.SERVER.overworldEditRange.get();
		BlockPos center = getEditmodeCenter(c);

		updateInventory(player, c);
		updatePosition(player, range, center.getX(), center.getZ());
		
		player.setPortalCooldown();
	}
	
	@SubscribeEvent
	public static void onTossEvent(ItemTossEvent event)
	{
		Inventory inventory = event.getPlayer().getInventory();
		if(!event.getEntity().level.isClientSide && getData(event.getPlayer()) != null)
		{
			EditData data = getData(event.getPlayer());
			ItemStack stack = event.getEntity().getItem();
			DeployEntry entry = DeployList.getEntryForItem(stack, data.connection, event.getEntity().level, DeployList.EntryLists.DEPLOY);
			if(entry != null && !isBlockItem(stack.getItem()))
			{
				GristSet cost = entry.getCurrentCost(data.connection);
				if(data.getGristCache().tryTake(cost, GristHelper.EnumSource.SERVER))
				{
					data.connection.setHasGivenItem(entry);
					if(!data.connection.isMain())
						SburbHandler.giveItems(event.getPlayer().getServer(), data.connection.getClientIdentifier());
				}
				else event.setCanceled(true);
			}
			else if(AlchemyHelper.isPunchedCard(stack) && DeployList.containsItemStack(AlchemyHelper.getDecodedItem(stack), data.connection, event.getEntity().level, DeployList.EntryLists.ATHENEUM))
			{
				GristSet cost = GristCostRecipe.findCostForItem(MSItems.CAPTCHA_CARD.get().getDefaultInstance(), GristTypes.BUILD.get(), false, event.getPlayer().getLevel());
				if(cost == null || !data.getGristCache().tryTake(cost, GristHelper.EnumSource.SERVER))
					event.setCanceled(true);
			}
			else
			{
				event.setCanceled(true);
			}
			if(event.isCanceled())
			{
				event.getEntity().discard();
				AbstractContainerMenu menu = event.getPlayer().containerMenu;
				if(!menu.getCarried().isEmpty())
					menu.setCarried(ItemStack.EMPTY);
				else inventory.setItem(inventory.selected, ItemStack.EMPTY);
			}
		}
	}
	
	@SubscribeEvent
	public static void onItemPickupEvent(EntityItemPickupEvent event)
	{
		if(!event.getEntity().level.isClientSide && getData(event.getEntity()) != null)
			event.setCanceled(true);
	}
	
	//TODO Slightly unsafe with this approach to check, and then execute in a different event listener. It is probably better to try first, and then reset if we tried and the event got cancelled.
	@SubscribeEvent(priority=EventPriority.NORMAL)
	public static void onRightClickBlockControl(PlayerInteractEvent.RightClickBlock event)
	{
		if(event.getEntity() instanceof ServerPlayer player && getData(event.getEntity()) != null)
		{
			IEditTools cap = player.getCapability(MSCapabilities.EDIT_TOOLS_CAPABILITY).orElseThrow(() -> new IllegalStateException("EditTools Capability is empty in RightClickBlock event on the server!"));
			if(!event.getEntity().canInteractWith(event.getPos(), 0.0) || cap.getEditPos1() != null)
			{
				event.setCanceled(true);
				return;
			}
			
			EditData data = getData(event.getEntity());
			Block block = event.getLevel().getBlockState(event.getPos()).getBlock();
			ItemStack stack = event.getEntity().getMainHandItem();
			event.setUseBlock(stack.isEmpty() && (block instanceof DoorBlock || block instanceof TrapDoorBlock || block instanceof FenceGateBlock) ? Event.Result.ALLOW : Event.Result.DENY);
			if(event.getUseBlock() == Event.Result.ALLOW)
				return;
			if(stack.isEmpty() || !isBlockItem(stack.getItem()) || event.getHand().equals(InteractionHand.OFF_HAND))
			{
				event.setCanceled(true);
				return;
			}
			
			cleanStackNBT(stack, data.connection, event.getLevel());
			
			DeployEntry entry = DeployList.getEntryForItem(stack, data.connection, event.getEntity().level);
			GristCache gristCache = data.getGristCache();
			if(entry != null)
			{
				GristSet cost = entry.getCurrentCost(data.connection);
				if(!gristCache.canAfford(cost))
				{
					if(cost != null)
						event.getEntity().sendSystemMessage(cost.createMissingMessage());
					event.setCanceled(true);
				}
			}
			else if(!isBlockItem(stack.getItem()) ||
					!gristCache.canAfford(GristCostRecipe.findCostForItem(stack, null, false, event.getLevel())))
			{
				event.setCanceled(true);
			}
			if(event.getUseItem() == Event.Result.DEFAULT)
				event.setUseItem(Event.Result.ALLOW);
		}
	}
	
	static GristSet blockBreakCost()
	{
		return new GristSet(GristTypes.BUILD, 1);
	}
	
	@SubscribeEvent(priority=EventPriority.NORMAL)
	public static void onLeftClickBlockControl(PlayerInteractEvent.LeftClickBlock event)
	{
		if(event.getEntity() instanceof ServerPlayer player && getData(event.getEntity()) != null)
		{
			IEditTools cap = player.getCapability(MSCapabilities.EDIT_TOOLS_CAPABILITY).orElseThrow(() -> new IllegalStateException("EditTools Capability is empty in LeftClickBlock event on the server!"));
			if(!event.getEntity().canInteractWith(event.getPos(), 0.0) || cap.getEditPos1() != null)
			{
				event.setCanceled(true);
				return;
			}
			
			EditData data = getData(event.getEntity());
			BlockState block = event.getLevel().getBlockState(event.getPos());
			ItemStack stack = block.getCloneItemStack(null, event.getLevel(), event.getPos(), event.getEntity());
			DeployEntry entry = DeployList.getEntryForItem(stack, data.connection, event.getLevel());
			if(block.getDestroySpeed(event.getLevel(), event.getPos()) < 0 || block.getMaterial() == Material.PORTAL
				|| (!data.getGristCache().canAfford(blockBreakCost()) && !MinestuckConfig.SERVER.gristRefund.get()
				|| entry == null || entry.getCategory() == DeployList.EntryLists.ATHENEUM))
			{
				event.setCanceled(true);
				return;
			}
			
			if(block.getBlock() instanceof EditmodeDestroyable destroyable)
				destroyable.destroyFull(block, event.getLevel(), event.getPos());
		}
	}
	
	@SubscribeEvent(priority=EventPriority.NORMAL)
	public static void onItemUseControl(PlayerInteractEvent.RightClickItem event)
	{
		if(!event.getLevel().isClientSide && getData(event.getEntity()) != null)
		{
			event.setCanceled(true);
		}
	}
	
	@SubscribeEvent(priority=EventPriority.LOWEST)
	public static void onBlockBreak(BlockEvent.BreakEvent event)
	{
		//BlockEvent.BreakEvent for some reason still uses the old naming scheme of getPlayer(), instead of the new scheme of getEntity()
		if(!event.getPlayer().level.isClientSide && getData(event.getPlayer()) != null)
		{
			
			EditData data = getData(event.getPlayer());
			BlockState block = event.getLevel().getBlockState(event.getPos());
			ItemStack stack = block.getCloneItemStack(null, event.getLevel(), event.getPos(), event.getPlayer());
			DeployEntry entry = DeployList.getEntryForItem(stack, data.connection, event.getPlayer().getLevel(), DeployList.EntryLists.ATHENEUM);
			if(block.getDestroySpeed(event.getLevel(), event.getPos()) < 0 || block.getMaterial() == Material.PORTAL)
			{
				event.setCanceled(true);
				return;
			}
			if(!MinestuckConfig.SERVER.gristRefund.get())
			{
				if(entry != null)
					data.getGristCache().addWithGutter(entry.getCurrentCost(data.connection), GristHelper.EnumSource.SERVER);
				else //Assumes that this will succeed because of the check in onLeftClickBlockControl()
					data.getGristCache().tryTake(blockBreakCost(), GristHelper.EnumSource.SERVER);
			}
			else
			{
				GristSet set = entry != null ? entry.getCurrentCost(data.connection) : GristCostRecipe.findCostForItem(stack, null, false, event.getPlayer().getLevel());
				if(set != null && !set.isEmpty())
				{
					data.getGristCache().addWithGutter(set, GristHelper.EnumSource.SERVER);
				}
			}
		}
	}
	
	@SubscribeEvent(priority=EventPriority.LOW)
	public static void onBlockPlaced(BlockEvent.EntityPlaceEvent event)
	{
		//Probably only need the ServerPlayer instanceof check, but we want to be ABSOLUTELY SURE that this isn't run client-side, so we check the level too.
		if(!event.getEntity().level.isClientSide() && event.getEntity() instanceof ServerPlayer player && getData(player) != null)
		{
		
			EditData data = getData(player);
			if(event.isCanceled())    //If the event was cancelled server side and not client side, notify the client.
			{
				data.sendGivenItemsToEditor();
				return;
			}
			
			ItemStack stack = player.getMainHandItem();	//TODO Make sure offhand isn't used in editmode?
			SburbConnection c = data.connection;
			DeployEntry entry = DeployList.getEntryForItem(stack, c, player.level);
			if(entry != null)
			{
				GristSet cost = entry.getCurrentCost(c);
				if(entry.getCategory() == DeployList.EntryLists.DEPLOY)
				{
					c.setHasGivenItem(entry);
					if(!c.isMain())
						SburbHandler.giveItems(player.server, c.getClientIdentifier());
				}
				if(!cost.isEmpty())
				{
					//Assumes that this will succeed because of the check in onRightClickBlockControl()
						data.getGristCache().tryTake(cost, GristHelper.EnumSource.SERVER);
					}if(entry.getCategory() != DeployList.EntryLists.ATHENEUM)
					player.getInventory().items.set(player.getInventory().selected, ItemStack.EMPTY);
			
			} else
			{
				GristSet set = GristCostRecipe.findCostForItem(stack, null, false, player.getCommandSenderWorld());
					//Assumes that this will succeed because of the check in onRightClickBlockControl()
					data.getGristCache().tryTake(set, GristHelper.EnumSource.SERVER);
			}
		}
	}
	
	@SubscribeEvent(priority=EventPriority.NORMAL)
	public static void onAttackEvent(AttackEntityEvent event)
	{
		if(!event.getEntity().level.isClientSide && getData(event.getEntity()) != null)
			event.setCanceled(true);
	}
	
	@SubscribeEvent(priority=EventPriority.NORMAL)
	public static void onInteractEvent(PlayerInteractEvent.EntityInteract event)
	{
		if(!event.getEntity().level.isClientSide && getData(event.getEntity()) != null)
			event.setCanceled(true);
	}
	
	@SubscribeEvent(priority=EventPriority.NORMAL)
	public static void onInteractEvent(PlayerInteractEvent.EntityInteractSpecific event)
	{
		if(!event.getEntity().level.isClientSide && getData(event.getEntity()) != null)
			event.setCanceled(true);
	}
	
	public static void updateEditToolsServer(ServerPlayer player, boolean isDragging, BlockPos pos1, BlockPos pos2)
	{
		if (player == null)
			throw LOGGER.throwing(new NullPointerException("Server Player is NULL in updateEditToolsServer()!"));
		else if (player.getLevel().isClientSide())
			throw LOGGER.throwing(new IllegalStateException("Server Level is clientside in updateEditToolsServer()!"));
		
		
		IEditTools cap = player.getCapability(MSCapabilities.EDIT_TOOLS_CAPABILITY, null).orElseThrow(() -> LOGGER.throwing(new IllegalStateException("EditTools Capability is empty in updateEditToolsServer()!")));
		
		//Gets whether the end of the selection-box (pos2) is lesser or greater than the origin-point (pos1)
		boolean signX = pos1.getX() < pos2.getX();
		boolean signY = pos1.getY() > pos2.getY();
		boolean signZ = pos1.getZ() < pos2.getZ();
		
		//uses each sign to offset the cursor to the correct corner.
		double posX = pos2.getX() + (signX ? 1 : 0);
		double posY = pos2.getY() + (signY ? -0.1 : 1); //When it is flipped, the cursor should be lowered a bit more, so that the hitbox does not intersect with the block.
		double posZ = pos2.getZ() + (signZ ? 1 : 0);
		boolean flipCursor = signY; //uses the sign to determine whether the cursor should be upside down or not.
		
		//some math to find out which way the cursor should point relative to the selection-origin.
		float cursorLean = 0f;
		if (signX && !signZ)
			cursorLean = 360.0f; //+X -Z = 0/360
		if (signX && signZ)
			cursorLean = 90.0f; //+X +Z = 90
		if (!signX && signZ)
			cursorLean = 180.0f; //-X +Z = 180
		if (!signX && !signZ)
			cursorLean = 270.0f; //-X -Z = 270
		
		if(cap.getEditCursorID() == null)
		{
			//creates the cursor and stores its UUID if one does not currently exist.
			cap.setEditCursorID(createCursorEntity(player, new Vec3(posX,posY,posZ), cursorLean, flipCursor));
		}
		else
		{
			//if it does exist already, update its position, rotation, and animation
			updateCursorEntity(player, new Vec3(posX,posY,posZ), cursorLean, flipCursor, cap.getEditCursorID());
		}
	}
	
	public static UUID createCursorEntity(ServerPlayer player, Vec3 startPosition, float cursorLean, boolean flip)
	{
		ServerCursorEntity cursor = MSEntityTypes.SERVER_CURSOR.get().create(player.getLevel());
		if(cursor == null)
			throw LOGGER.throwing(new NullPointerException("Server Cursor is null after creation! Something is wrong!"));
		cursor.noPhysics = true;
		cursor.setNoGravity(true);
		cursor.setInvulnerable(true);
		
		cursor.moveTo(startPosition.x, startPosition.y, startPosition.z, cursorLean - 45.0f, flip ? 135f : 45f);
		cursor.setYBodyRot(cursorLean - 45.0f);
		cursor.setYHeadRot(cursorLean - 45.0f);
		cursor.setAnimation(ServerCursorEntity.Animation.CLICK);
		player.getLevel().addWithUUID(cursor);
		if(player.getLevel().getEntity(cursor.getUUID()) == null)
			throw new NullPointerException("Server Cursor is null after added to level! Something is wrong!");
		return cursor.getUUID();
	}
	
	public static void updateCursorEntity(ServerPlayer player, Vec3 newPosition, float cursorLean, boolean flip, UUID uuid)
	{
		ServerCursorEntity cursor = (ServerCursorEntity) player.getLevel().getEntity(uuid);
		
		cursor.moveTo(newPosition.x, newPosition.y, newPosition.z, cursorLean - 45.0f, flip ? 135f : 45f);
		cursor.setYBodyRot(cursorLean - 45.0f);
		cursor.setYHeadRot(cursorLean - 45.0f);
		cursor.setAnimation(ServerCursorEntity.Animation.IDLE);
	}
	
	/**
	 * only called server-side, when an edit tool has finished being used (I.E when you release the right mouse button while using revise)
	 */
	public static void removeCursorEntity(ServerPlayer player, boolean rejected)
	{
		IEditTools cap = player.getCapability(MSCapabilities.EDIT_TOOLS_CAPABILITY, null).orElseThrow(() -> LOGGER.throwing(new IllegalStateException("EditTools Capability is empty in removeCursorEntity()!")));
		
		if(cap.getEditCursorID() != null)
		{
			ServerCursorEntity cursor = (ServerCursorEntity) player.getLevel().getEntity(cap.getEditCursorID());
			cursor.queueRemoval(rejected ? ServerCursorEntity.Animation.REJECTED : ServerCursorEntity.Animation.CLICK);
		}
		
		cap.resetDragTools();
	}
	
	/**
	 * Used on both server and client side.
	 */
	public static void updatePosition(Player player, double range, int centerX, int centerZ) {
		double y = player.getY();
		if(y < player.level.getMinBuildHeight()) {
			y = player.level.getMinBuildHeight();
			player.setDeltaMovement(player.getDeltaMovement().multiply(1, 0, 1));
			player.getAbilities().flying = true;
		}
		
		double newX = player.getX();
		double newZ = player.getZ();
		double offset = player.getBoundingBox().maxX-player.getX();
		
		if(range >= 1) {
			if(player.getX() > centerX+range-offset)
				newX = centerX+range-offset;
			else if(player.getX() < centerX-range+offset)
				newX = centerX-range+offset;
			if(player.getZ() > centerZ+range-offset)
				newZ = centerZ+range-offset;
			else if(player.getZ() < centerZ-range+offset)
				newZ = centerZ-range+offset;
		}
		
		if(newX != player.getX())
			player.setDeltaMovement(player.getDeltaMovement().multiply(0, 1, 1));
		
		if(newZ != player.getZ())
			player.setDeltaMovement(player.getDeltaMovement().multiply(1, 1, 0));
		
		if(newX != player.getX() || newZ != player.getZ() || y != player.getY())
		{
			if(player.level.isClientSide)
				player.setPos(newX, y, newZ);
			else player.teleportTo(newX, y, newZ);
		}
	}
	
	public static void updateInventory(ServerPlayer player, SburbConnection connection)
	{
		List<DeployEntry> deployList = DeployList.getItemList(player.getServer(), connection);
		deployList.removeIf(entry -> entry.getCurrentCost(connection) == null);
		List<ItemStack> itemList = new ArrayList<>();
		deployList.forEach(deployEntry -> itemList.add(deployEntry.getItemStack(connection, player.level)));
		
		boolean inventoryChanged = false;
		for(int i = 0; i < player.getInventory().items.size(); i++)
		{
			ItemStack stack = player.getInventory().items.get(i);
			if(stack.isEmpty())
				continue;
			if(GristCostRecipe.findCostForItem(stack, null, false, player.getCommandSenderWorld()) == null || !isBlockItem(stack.getItem()))
			{
				listSearch :
				{
					for(ItemStack deployStack : itemList)
						if(ItemStack.matches(deployStack, stack) || (AlchemyHelper.isPunchedCard(stack) && ItemStack.matches(deployStack, AlchemyHelper.getDecodedItem(stack)) && DeployList.containsItemStack(AlchemyHelper.getDecodedItem(stack), connection, player.getLevel(), DeployList.EntryLists.ATHENEUM)))
							break listSearch;
					player.getInventory().items.set(i, ItemStack.EMPTY);
					inventoryChanged = true;
				}
			} else if(stack.hasTag())
			{
				listSearch :
				{
					for(ItemStack deployStack : itemList)
						if(ItemStack.matches(deployStack, stack) || (AlchemyHelper.isPunchedCard(stack) && ItemStack.matches(deployStack, AlchemyHelper.getDecodedItem(stack)) && DeployList.containsItemStack(AlchemyHelper.getDecodedItem(stack), connection, player.getLevel(), DeployList.EntryLists.ATHENEUM)))
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
			player.getServer().getPlayerList().sendAllPlayerInfo(player);
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
		if(event.getEntity() instanceof ServerPlayer player)
		{
			EditData data = getData(player);
			// Prevent edit player teleportation unless it is recovering to its initial state
			if(data != null && !data.isRecovering())
				event.setCanceled(true);
		}
	}
	
	public static boolean isBlockItem(Item item)
	{
		return item instanceof BlockItem;
	}
	
	public static void cleanStackNBT(ItemStack stack, SburbConnection c, Level level)
	{
		if(!DeployList.containsItemStack(stack, c, level, DeployList.EntryLists.DEPLOY) || !(AlchemyHelper.isPunchedCard(stack) && DeployList.containsItemStack(AlchemyHelper.getDecodedItem(stack), c, level, DeployList.EntryLists.ATHENEUM)))
			stack.setTag(null);
	}
	
	@SubscribeEvent
	public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event)
	{
		ServerPlayer player = (ServerPlayer) event.getEntity();
		UUID id = player.getGameProfile().getId();
		EditData.PlayerRecovery recovery = MSExtraData.get(player.level).removePlayerRecovery(id);
		if(recovery != null)
			recovery.recover(player, false);
	}
	
	@SubscribeEvent(priority = EventPriority.HIGHEST)	//Editmode players need to be reset before nei handles the event
	public static void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event)
	{
		ServerEditHandler.onPlayerExit(event.getEntity());
	}
	
	@SubscribeEvent
	public static void onServerStopping(ServerStoppingEvent event)
	{
		MSExtraData.get(event.getServer()).forEachAndClear(ServerEditHandler::partialReset);
	}
	
	@SubscribeEvent
	public static void serverStarted(ServerStartedEvent event)
	{
		SkaianetHandler skaianet = SkaianetHandler.get(event.getServer());
		MSExtraData.get(event.getServer()).recoverConnections(recovery -> recovery.recover(skaianet.getActiveConnection(recovery.getClientPlayer())));
	}
}