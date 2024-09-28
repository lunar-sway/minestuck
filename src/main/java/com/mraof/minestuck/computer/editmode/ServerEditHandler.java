package com.mraof.minestuck.computer.editmode;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.alchemy.AlchemyHelper;
import com.mraof.minestuck.alchemy.GristHelper;
import com.mraof.minestuck.api.alchemy.GristSet;
import com.mraof.minestuck.api.alchemy.GristTypes;
import com.mraof.minestuck.api.alchemy.recipe.GristCostRecipe;
import com.mraof.minestuck.block.machine.EditmodeDestroyable;
import com.mraof.minestuck.entity.DecoyEntity;
import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.entity.ServerCursorEntity;
import com.mraof.minestuck.event.OnEntryEvent;
import com.mraof.minestuck.event.SburbEvent;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.network.editmode.EditmodeLocationsPacket;
import com.mraof.minestuck.network.editmode.ServerEditPackets;
import com.mraof.minestuck.player.GristCache;
import com.mraof.minestuck.player.PlayerIdentifier;
import com.mraof.minestuck.skaianet.ActiveConnection;
import com.mraof.minestuck.skaianet.SburbConnections;
import com.mraof.minestuck.skaianet.SburbHandler;
import com.mraof.minestuck.skaianet.SburbPlayerData;
import com.mraof.minestuck.util.MSAttachments;
import com.mraof.minestuck.util.MSTags;
import com.mraof.minestuck.util.Teleport;
import com.mraof.minestuck.world.storage.MSExtraData;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.SectionPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
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
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.LogicalSide;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.TickEvent;
import net.neoforged.neoforge.event.entity.EntityTravelToDimensionEvent;
import net.neoforged.neoforge.event.entity.item.ItemTossEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.entity.player.EntityItemPickupEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.*;

/**
 * Main class to handle the server side of edit mode.
 * Also contains some methods used on both sides.
 *
 * @author kirderf1
 */
@SuppressWarnings("resource")
@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class ServerEditHandler    //TODO Consider splitting this class into two
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	public static final ArrayList<String> commands = new ArrayList<>(Arrays.asList("effect", "gamemode", "defaultgamemode", "enchant", "xp", "tp", "spreadplayers", "kill", "clear", "spawnpoint", "setworldspawn", "give"));
	
	/**
	 * Called both when any player logged out and when a player pressed the exit button.
	 */
	public static void onPlayerExit(Player player)
	{
		if(!player.level().isClientSide)
			reset(getData(player));
	}
	
	@SubscribeEvent
	public static void onDisconnect(SburbEvent.ConnectionClosed event)
	{
		reset(getData(event.getMinecraftServer(), event.getConnection()));
	}
	
	@SubscribeEvent
	public static void onEntry(OnEntryEvent event)
	{
		SburbConnections.get(event.getMcServer()).getActiveConnection(event.getPlayer())
				.ifPresent(connection -> connection.lastEditmodePosition = null);
	}
	
	@SubscribeEvent
	public static void onPlayerCloneEvent(PlayerEvent.Clone event)
	{
		EditData prevData = getData(event.getOriginal());
		if(prevData != null && event.getEntity() instanceof ServerPlayer player)
		{
			//take measures to prevent editmode data from ending up with an invalid player entity
			LOGGER.error("Minestuck failed to prevent death or different cloning event for player {}. Applying measure to reduce problems", event.getEntity().getName().getString());
			
			MSExtraData data = MSExtraData.get(event.getEntity().level());
			data.removeEditData(prevData);
			data.addEditData(new EditData(prevData.getDecoy(), player, prevData.activeConnection));
		}
	}
	
	@SubscribeEvent
	public static void onLivingDamage(LivingDamageEvent event)
	{
		if(event.getEntity() instanceof ServerPlayer player && isInEditmode(player))
			event.setCanceled(true);
	}
	
	@SubscribeEvent
	public static void onLivingDeath(LivingDeathEvent event)
	{
		if(event.getEntity() instanceof ServerPlayer player && isInEditmode(player))
			event.setCanceled(true);
	}
	
	public static void reset(EditData data)
	{
		reset(null, 0, data);
	}
	
	/**
	 * Called when the server stops editing the clients house.
	 *
	 * @param damageSource If the process was cancelled by the decoy taking damage, this parameter will be the damage source. Else null.
	 * @param damage       If the damageSource isn't null, this is the damage taken, else this parameter is ignored.
	 * @param editData     edit-data that identifies the editmode session
	 */
	public static void reset(DamageSource damageSource, float damage, EditData editData)
	{
		partialReset(damageSource, damage, editData);
		
		if(editData == null)
			return;
		
		MSExtraData data = MSExtraData.get(editData.getEditor().level());
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
		
		editData.recover();    //TODO handle exception from failed recovery
		
		PacketDistributor.PLAYER.with(player).send(new ServerEditPackets.Exit());
		
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
		Optional<ActiveConnection> connectionOptional = SburbConnections.get(player.getServer()).getCheckedActiveConnection(computerTarget);
		if(connectionOptional.isEmpty())
			return;
		ActiveConnection connection = connectionOptional.get();
		
		if(connection.server().equals(computerOwner) && getData(player.server, connection) == null && !isInEditmode(player))
		{
			LOGGER.info("Activating edit mode on player \"{}\", target player: \"{}\".", player.getName().getString(), computerTarget);
			
			SburbPlayerData targetData = SburbPlayerData.get(computerTarget, player.server);
			DecoyEntity decoy = new DecoyEntity((ServerLevel) player.level(), player);
			EditData data = new EditData(decoy, player, connection);
			
			if(!setPlayerStats(player, targetData, connection))
			{
				player.sendSystemMessage(Component.literal("Failed to activate edit mode.").withStyle(ChatFormatting.RED));
				return;
			}
			if(targetData.getEditmodeInventory() != null)
				player.getInventory().load(targetData.getEditmodeInventory());
			decoy.level().addFreshEntity(decoy);
			MSExtraData.get(player.level()).addEditData(data);
			
			data.locations().validateClosestSource(player, targetData);
			
			PacketDistributor.PLAYER.with(player).send(new ServerEditPackets.Activate());
			data.sendGivenItemsToEditor();
			EditmodeLocationsPacket.send(data);
			
			data.sendGristCacheToEditor();
			data.sendCacheLimitToEditor();
		}
	}
	
	static boolean setPlayerStats(ServerPlayer player, SburbPlayerData clientData, ActiveConnection activeConnection)
	{
		GlobalPos computerPos = activeConnection.clientComputer().getPosForEditmode();
		double posX, posY, posZ;
		ResourceKey<Level> landDimension = Objects.requireNonNullElse(clientData.getLandDimensionIfEntered(), computerPos.dimension());
		ServerLevel level = player.server.getLevel(landDimension);
		
		if(activeConnection.lastEditmodePosition != null)
		{
			posX = activeConnection.lastEditmodePosition.x;
			posZ = activeConnection.lastEditmodePosition.z;
		} else
		{
			BlockPos center;
			if(clientData.hasEntered())
				center = new BlockPos(0, 0, 0);
			else center = computerPos.pos();
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
			PacketDistributor.PLAYER.with(editor).send(new ServerEditPackets.Activate());
			data.sendGivenItemsToEditor();
			EditmodeLocationsPacket.send(data);
			
			data.sendGristCacheToEditor();
			data.sendCacheLimitToEditor();
		} else
		{
			PacketDistributor.PLAYER.with(editor).send(new ServerEditPackets.Exit());
		}
	}
	
	public static boolean isInEditmode(ServerPlayer player)
	{
		return getData(player) != null;
	}
	
	@Nullable
	public static EditData getData(Player editor)
	{
		return MSExtraData.get(editor.level()).findEditData(editData -> editData.getEditor() == editor);
	}
	
	@Nullable
	public static EditData getData(MinecraftServer server, ActiveConnection connection)
	{
		return getData(server, connection.client());
	}
	
	@Nullable
	public static EditData getData(MinecraftServer server, PlayerIdentifier client)
	{
		return MSExtraData.get(server).findEditData(editData -> editData.getTarget().equals(client));
	}
	
	@Nullable
	public static EditData getData(DecoyEntity decoy)
	{
		return MSExtraData.get(decoy.getCommandSenderWorld()).findEditData(editData -> editData.getDecoy() == decoy);
	}
	
	@SubscribeEvent
	public static void tickEnd(TickEvent.PlayerTickEvent event)
	{
		if(event.phase != TickEvent.Phase.END || event.side == LogicalSide.CLIENT)
			return;
		ServerPlayer player = (ServerPlayer) event.player;
		
		EditData data = getData(player);
		if(data == null)
			return;
		
		SburbPlayerData targetData = data.sburbData();
		EditmodeLocations editmodeLocations = data.locations();
		
		//every 10 seconds, revalidate locations
		if(player.level().getGameTime() % 200 == 0)
			editmodeLocations.validateClosestSource(player, targetData);
		
		editmodeLocations.limitMovement(player, targetData.getLandDimensionIfEntered());
		
		updateInventory(player, targetData);
		
		player.setPortalCooldown();
	}
	
	@SubscribeEvent
	public static void onTossEvent(ItemTossEvent event)
	{
		Inventory inventory = event.getPlayer().getInventory();
		if(event.getPlayer() instanceof ServerPlayer player)
		{
			EditData data = getData(player);
			if(data == null)
				return;
			ItemStack stack = event.getEntity().getItem();
			DeployEntry entry = DeployList.getEntryForItem(stack, data.sburbData(), event.getEntity().level(), DeployList.EntryLists.DEPLOY);
			if(entry != null && !isBlockItem(stack.getItem()))
			{
				GristSet cost = entry.getCurrentCost(data.sburbData());
				if(data.getGristCache().tryTake(cost, GristHelper.EnumSource.SERVER))
				{
					data.sburbData().setHasGivenItem(entry);
					SburbHandler.onEntryItemsDeployed(player.getServer(), data.getTarget());
				} else event.setCanceled(true);
			} else if(AlchemyHelper.isPunchedCard(stack) && DeployList.containsItemStack(AlchemyHelper.getDecodedItem(stack), data.sburbData(), event.getEntity().level(), DeployList.EntryLists.ATHENEUM))
			{
				GristSet cost = GristCostRecipe.findCostForItem(MSItems.CAPTCHA_CARD.get().getDefaultInstance(), GristTypes.BUILD.get(), false, player.level());
				if(cost == null || !data.getGristCache().tryTake(cost, GristHelper.EnumSource.SERVER))
					event.setCanceled(true);
			} else
			{
				event.setCanceled(true);
			}
			if(event.isCanceled())
			{
				event.getEntity().discard();
				AbstractContainerMenu menu = player.containerMenu;
				if(!menu.getCarried().isEmpty())
					menu.setCarried(ItemStack.EMPTY);
				else inventory.setItem(inventory.selected, ItemStack.EMPTY);
			}
		}
	}
	
	@SubscribeEvent
	public static void onItemPickupEvent(EntityItemPickupEvent event)
	{
		if(event.getEntity() instanceof ServerPlayer player && isInEditmode(player))
			event.setCanceled(true);
	}
	
	//TODO Slightly unsafe with this approach to check, and then execute in a different event listener. It is probably better to try first, and then reset if we tried and the event got cancelled.
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public static void onRightClickBlockControl(PlayerInteractEvent.RightClickBlock event)
	{
		if(event.getEntity() instanceof ServerPlayer player)
		{
			EditData data = getData(event.getEntity());
			if (data == null)
				return;
			
			EditTools cap = player.getData(MSAttachments.EDIT_TOOLS);
			if(!event.getEntity().canReach(event.getPos(), 0.0) || cap.getEditPos1() != null)
			{
				event.setCanceled(true);
				return;
			}
			
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
			
			SburbPlayerData targetData = data.sburbData();
			cleanStackNBT(stack, targetData, event.getLevel());
			
			DeployEntry entry = DeployList.getEntryForItem(stack, targetData, event.getEntity().level());
			GristCache gristCache = data.getGristCache();
			if(entry != null)
			{
				GristSet cost = entry.getCurrentCost(targetData);
				if(!gristCache.canAfford(cost))
				{
					if(cost != null)
						event.getEntity().sendSystemMessage(GristCache.createMissingMessage(cost));
					event.setCanceled(true);
				}
			} else if(!isBlockItem(stack.getItem()) ||
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
		return GristTypes.BUILD.get().amount(1);
	}
	
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public static void onLeftClickBlockControl(PlayerInteractEvent.LeftClickBlock event)
	{
		if(event.getEntity() instanceof ServerPlayer player)
		{
			EditData data = getData(event.getEntity());
			if(data == null)
				return;
			
			EditTools cap = player.getData(MSAttachments.EDIT_TOOLS);
			if(!event.getEntity().canReach(event.getPos(), 0.0) || cap.getEditPos1() != null)
			{
				event.setCanceled(true);
				return;
			}
			
			BlockState block = event.getLevel().getBlockState(event.getPos());
			ItemStack stack = block.getCloneItemStack(null, event.getLevel(), event.getPos(), event.getEntity());
			DeployEntry entry = DeployList.getEntryForItem(stack, data.sburbData(), event.getLevel());
			if(block.getDestroySpeed(event.getLevel(), event.getPos()) < 0 || block.is(MSTags.Blocks.EDITMODE_BREAK_BLACKLIST)
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
	
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public static void onItemUseControl(PlayerInteractEvent.RightClickItem event)
	{
		if(event.getEntity() instanceof ServerPlayer player && isInEditmode(player))
		{
			event.setCanceled(true);
		}
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onBlockBreak(BlockEvent.BreakEvent event)
	{
		//BlockEvent.BreakEvent for some reason still uses the old naming scheme of getPlayer(), instead of the new scheme of getEntity()
		if(!event.getPlayer().level().isClientSide)
		{
			EditData data = getData(event.getPlayer());
			if(data == null)
				return;
			
			BlockState block = event.getLevel().getBlockState(event.getPos());
			ItemStack stack = block.getCloneItemStack(null, event.getLevel(), event.getPos(), event.getPlayer());
			DeployEntry entry = DeployList.getEntryForItem(stack, data.sburbData(), event.getPlayer().level(), DeployList.EntryLists.ATHENEUM);
			if(block.getDestroySpeed(event.getLevel(), event.getPos()) < 0 || block.is(MSTags.Blocks.EDITMODE_BREAK_BLACKLIST))
			{
				event.setCanceled(true);
				return;
			}
			if(!MinestuckConfig.SERVER.gristRefund.get())
			{
				if(entry != null)
					data.getGristCache().addWithGutter(entry.getCurrentCost(data.sburbData()), GristHelper.EnumSource.SERVER);
				else //Assumes that this will succeed because of the check in onLeftClickBlockControl()
					data.getGristCache().tryTake(blockBreakCost(), GristHelper.EnumSource.SERVER);
			} else
			{
				GristSet set = entry != null ? entry.getCurrentCost(data.sburbData()) : GristCostRecipe.findCostForItem(stack, null, false, event.getPlayer().level());
				if(set != null && !set.isEmpty())
				{
					data.getGristCache().addWithGutter(set, GristHelper.EnumSource.SERVER);
				}
			}
		}
	}
	
	@SubscribeEvent(priority = EventPriority.LOW)
	public static void onBlockPlaced(BlockEvent.EntityPlaceEvent event)
	{
		if(event.getEntity() instanceof ServerPlayer player)
		{
			EditData data = getData(player);
			if(data == null)
				return;
			
			if(event.isCanceled())    //If the event was cancelled server side and not client side, notify the client.
			{
				data.sendGivenItemsToEditor();
				return;
			}
			
			ItemStack stack = player.getMainHandItem();    //TODO Make sure offhand isn't used in editmode?
			SburbPlayerData targetData = data.sburbData();
			DeployEntry entry = DeployList.getEntryForItem(stack, targetData, player.level());
			if(entry != null)
			{
				GristSet cost = entry.getCurrentCost(targetData);
				if(entry.getCategory() == DeployList.EntryLists.DEPLOY)
				{
					targetData.setHasGivenItem(entry);
					SburbHandler.onEntryItemsDeployed(player.server, data.getTarget());
				}
				if(!cost.isEmpty())
				{
					//Assumes that this will succeed because of the check in onRightClickBlockControl()
					data.getGristCache().tryTake(cost, GristHelper.EnumSource.SERVER);
				}
				if(entry.getCategory() != DeployList.EntryLists.ATHENEUM)
					player.getInventory().items.set(player.getInventory().selected, ItemStack.EMPTY);
				
			} else
			{
				GristSet set = GristCostRecipe.findCostForItem(stack, null, false, player.getCommandSenderWorld());
				//Assumes that this will succeed because of the check in onRightClickBlockControl()
				data.getGristCache().tryTake(set, GristHelper.EnumSource.SERVER);
			}
		}
	}
	
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public static void onAttackEvent(AttackEntityEvent event)
	{
		if(event.getEntity() instanceof ServerPlayer player && isInEditmode(player))
			event.setCanceled(true);
	}
	
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public static void onInteractEvent(PlayerInteractEvent.EntityInteract event)
	{
		if(event.getEntity() instanceof ServerPlayer player && isInEditmode(player))
			event.setCanceled(true);
	}
	
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public static void onInteractEvent(PlayerInteractEvent.EntityInteractSpecific event)
	{
		if(event.getEntity() instanceof ServerPlayer player && isInEditmode(player))
			event.setCanceled(true);
	}
	
	public static void updateEditToolsServer(ServerPlayer player, boolean isDragging, BlockPos pos1, BlockPos pos2)
	{
		if(player == null)
			throw LOGGER.throwing(new NullPointerException("Server Player is NULL in updateEditToolsServer()!"));
		else if(player.level().isClientSide())
			throw LOGGER.throwing(new IllegalStateException("Server Level is clientside in updateEditToolsServer()!"));
		
		
		IEditTools cap = player.getData(MSAttachments.EDIT_TOOLS);
		
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
		if(signX && !signZ)
			cursorLean = 360.0f; //+X -Z = 0/360
		if(signX && signZ)
			cursorLean = 90.0f; //+X +Z = 90
		if(!signX && signZ)
			cursorLean = 180.0f; //-X +Z = 180
		if(!signX && !signZ)
			cursorLean = 270.0f; //-X -Z = 270
		
		if(cap.getEditCursorID() == null)
		{
			//creates the cursor and stores its UUID if one does not currently exist.
			cap.setEditCursorID(createCursorEntity(player, new Vec3(posX, posY, posZ), cursorLean, flipCursor));
		} else
		{
			//if it does exist already, update its position, rotation, and animation
			updateCursorEntity(player, new Vec3(posX, posY, posZ), cursorLean, flipCursor, cap.getEditCursorID());
		}
	}
	
	public static UUID createCursorEntity(ServerPlayer player, Vec3 startPosition, float cursorLean, boolean flip)
	{
		ServerCursorEntity cursor = MSEntityTypes.SERVER_CURSOR.get().create(player.level());
		if(cursor == null)
			throw LOGGER.throwing(new NullPointerException("Server Cursor is null after creation! Something is wrong!"));
		cursor.noPhysics = true;
		cursor.setNoGravity(true);
		cursor.setInvulnerable(true);
		
		cursor.moveTo(startPosition.x, startPosition.y, startPosition.z, cursorLean - 45.0f, flip ? 135f : 45f);
		cursor.setYBodyRot(cursorLean - 45.0f);
		cursor.setYHeadRot(cursorLean - 45.0f);
		cursor.setAnimation(ServerCursorEntity.AnimationType.CLICK);
		player.serverLevel().addWithUUID(cursor);
		if(player.serverLevel().getEntity(cursor.getUUID()) == null)
			throw new NullPointerException("Server Cursor is null after added to level! Something is wrong!");
		return cursor.getUUID();
	}
	
	public static void updateCursorEntity(ServerPlayer player, Vec3 newPosition, float cursorLean, boolean flip, UUID uuid)
	{
		ServerCursorEntity cursor = (ServerCursorEntity) player.serverLevel().getEntity(uuid);
		
		cursor.moveTo(newPosition.x, newPosition.y, newPosition.z, cursorLean - 45.0f, flip ? 135f : 45f);
		cursor.setYBodyRot(cursorLean - 45.0f);
		cursor.setYHeadRot(cursorLean - 45.0f);
		cursor.setAnimation(ServerCursorEntity.AnimationType.IDLE);
	}
	
	/**
	 * only called server-side, when an edit tool has finished being used (I.E when you release the right mouse button while using revise)
	 */
	public static void removeCursorEntity(ServerPlayer player, boolean rejected)
	{
		IEditTools cap = player.getData(MSAttachments.EDIT_TOOLS);
		
		if(cap.getEditCursorID() != null)
		{
			ServerCursorEntity cursor = (ServerCursorEntity) player.serverLevel().getEntity(cap.getEditCursorID());
			cursor.queueRemoval(rejected ? ServerCursorEntity.AnimationType.REJECTED : ServerCursorEntity.AnimationType.CLICK);
		}
		
		cap.resetDragTools();
	}
	
	public static void updateInventory(ServerPlayer player, SburbPlayerData playerData)
	{
		List<DeployEntry> deployList = DeployList.getItemList(player.getServer(), playerData);
		deployList.removeIf(entry -> entry.getCurrentCost(playerData) == null);
		List<ItemStack> itemList = new ArrayList<>();
		deployList.forEach(deployEntry -> itemList.add(deployEntry.getItemStack(playerData, player.level())));
		
		boolean inventoryChanged = false;
		for(int i = 0; i < player.getInventory().items.size(); i++)
		{
			ItemStack stack = player.getInventory().items.get(i);
			if(stack.isEmpty())
				continue;
			if(GristCostRecipe.findCostForItem(stack, null, false, player.getCommandSenderWorld()) == null || !isBlockItem(stack.getItem()))
			{
				listSearch:
				{
					for(ItemStack deployStack : itemList)
						if(ItemStack.matches(deployStack, stack)
								|| (AlchemyHelper.isPunchedCard(stack) && ItemStack.matches(deployStack, AlchemyHelper.getDecodedItem(stack))
								&& DeployList.containsItemStack(AlchemyHelper.getDecodedItem(stack), playerData, player.level(), DeployList.EntryLists.ATHENEUM)))
							break listSearch;
					player.getInventory().items.set(i, ItemStack.EMPTY);
					inventoryChanged = true;
				}
			} else if(stack.hasTag())
			{
				listSearch:
				{
					for(ItemStack deployStack : itemList)
						if(ItemStack.matches(deployStack, stack)
								|| (AlchemyHelper.isPunchedCard(stack) && ItemStack.matches(deployStack, AlchemyHelper.getDecodedItem(stack))
								&& DeployList.containsItemStack(AlchemyHelper.getDecodedItem(stack), playerData, player.level(), DeployList.EntryLists.ATHENEUM)))
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
	
	public static void cleanStackNBT(ItemStack stack, SburbPlayerData playerData, Level level)
	{
		if(!DeployList.containsItemStack(stack, playerData, level, DeployList.EntryLists.DEPLOY)
				|| !(AlchemyHelper.isPunchedCard(stack) && DeployList.containsItemStack(AlchemyHelper.getDecodedItem(stack), playerData, level, DeployList.EntryLists.ATHENEUM)))
			stack.setTag(null);
	}
	
	@SubscribeEvent
	public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event)
	{
		ServerPlayer player = (ServerPlayer) event.getEntity();
		UUID id = player.getGameProfile().getId();
		EditData.PlayerRecovery recovery = MSExtraData.get(player.level()).removePlayerRecovery(id);
		if(recovery != null)
			recovery.recover(player, false);
	}
	
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	//Editmode players need to be reset before nei handles the event
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
		MSExtraData.get(event.getServer()).recoverConnections(event.getServer());
	}
}
