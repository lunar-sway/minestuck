package com.mraof.minestuck.editmode;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.Teleporter;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.Event;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.entity.EntityDecoy;
import com.mraof.minestuck.item.ItemCardPunched;
import com.mraof.minestuck.item.ItemCruxiteArtifact;
import com.mraof.minestuck.item.ItemMachine;
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
import com.mraof.minestuck.util.GristStorage;
import com.mraof.minestuck.util.UsernameHandler;
import com.mraof.minestuck.world.storage.MinestuckSaveHandler;

import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;

/**
 * Main class to handle the server side of edit mode.
 * Also contains some methods used on both sides.
 * @author kirderf1
 */
public class ServerEditHandler implements ITickHandler{
	
	public static final ServerEditHandler instance = new ServerEditHandler();
	
	static List<EditData> list = new ArrayList();
	
	/**
	 * Called both when any player logged out and when a player pressed the exit button.
	 * @param player
	 */
	public static void onPlayerExit(EntityPlayer player) {
		if(!player.worldObj.isRemote) {
			reset(null, 0, getData(player.username));
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
		EntityDecoy decoy = data.decoy;
		if(player.dimension != decoy.dimension)
			player.mcServer.getConfigurationManager().transferPlayerToDimension(player, decoy.worldObj.provider.dimensionId, new Teleporter((WorldServer)decoy.worldObj) {
				public void placeInPortal(Entity par1Entity, double par2, double par4, double par6, float par8) {}});
		
		data.connection.useCoordinates = true;
		data.connection.posX = player.posX;
		data.connection.posZ = player.posZ;
		
		player.playerNetServerHandler.setPlayerLocation(decoy.posX, decoy.posY, decoy.posZ, decoy.rotationYaw, decoy.rotationPitch);
		if(!player.theItemInWorldManager.getGameType().equals(decoy.gameType))
			player.setGameType(decoy.gameType);
		player.capabilities.readCapabilitiesFromNBT(decoy.capabilities);
		player.sendPlayerAbilities();
		player.fallDistance = 0;
		player.setHealth(decoy.getHealth());
		NBTTagCompound nbt = new NBTTagCompound();
		decoy.foodStats.writeNBT(nbt);
		player.getFoodStats().readNBT(nbt);
		player.theItemInWorldManager = data.manager;
		data.connection.inventory = player.inventory.writeToNBT(new NBTTagList());
		player.inventory.copyInventory(decoy.inventory);
		
		decoy.markedForDespawn = true;
		list.remove(data);
		
		Packet250CustomPayload packet = new Packet250CustomPayload();
		packet.channel = "Minestuck";
		packet.data = MinestuckPacket.makePacket(Type.SERVER_EDIT);
		packet.length = packet.data.length;
		player.playerNetServerHandler.sendPacketToPlayer(packet);
		
		if(damageSource != null && damageSource.getSourceOfDamage() != player)
			player.attackEntityFrom(damageSource, damage);
	}
	
	public static void newServerEditor(EntityPlayerMP player, String computerOwner, String computerTarget) {
		if(player.isRiding())
			return;	//Don't want to bother making the decoy able to ride anything right now.
		SburbConnection c = SkaianetHandler.getClientConnection(computerTarget);
		if(c != null && c.getServerName().equals(computerOwner) && getData(c) == null && getData(player.username) == null) {
			Debug.print("Activating edit mode on player \""+player.username+"\", target player: \""+computerTarget+"\".");
			EntityDecoy decoy = new EntityDecoy(player.worldObj, player);
			EditData data = new EditData(decoy, player, c);
			if(!c.enteredGame()) {
				c.centerX = c.getClientData().getX();
				c.centerZ = c.getClientData().getZ();
			}
			if(!setPlayerStats(player, c)) {
				player.theItemInWorldManager = data.manager;
				ChatMessageComponent message = new ChatMessageComponent();
				message.addText("Failed to activate edit mode.");
				message.setColor(EnumChatFormatting.RED);
				player.sendChatToPlayer(message);
				return;
			}
			if(c.inventory != null)
				player.inventory.readFromNBT(c.inventory);
			decoy.worldObj.spawnEntityInWorld(decoy);
			list.add(data);
			Packet250CustomPayload packet = new Packet250CustomPayload();
			packet.channel = "Minestuck";
			packet.data = MinestuckPacket.makePacket(Type.SERVER_EDIT, computerTarget, c.centerX, c.centerZ, c.givenItems());
			packet.length = packet.data.length;
			player.playerNetServerHandler.sendPacketToPlayer(packet);
			MinestuckPlayerTracker.updateGristCache(c.getClientName());
		}
	}
	
	static boolean setPlayerStats(EntityPlayerMP player, SburbConnection c) {
		
		double playerOffset = player.posX-player.boundingBox.maxX;
		
		SburbEditManager manager = new SburbEditManager(player.worldObj, player);
		manager.client = c.getClientName();
		player.theItemInWorldManager = manager;
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
		player.inventory.clearInventory(-1, -1);
		
		player.setPositionAndUpdate(posX, posY, posZ);
		
		return true;
	}
	
	public static EditData getData(String editor) {
		for(EditData data : list)
			if(data.player.username == editor)
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
	
	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) {
		EntityPlayerMP player = (EntityPlayerMP)tickData[0];
		
		EditData data = getData(player.username);
		if(data == null)
			return;
		
		SburbConnection c = data.connection;
		int range = MinestuckSaveHandler.lands.contains((byte)player.dimension)?Minestuck.landEditRange:Minestuck.overworldEditRange;
		
		updateInventory(player, c.givenItems(), c.enteredGame(), c.getClientName());
		updatePosition(player, range, c.centerX, c.centerZ);
	}

	@Override
	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.PLAYER);
	}

	@Override
	public String getLabel() {
		return "TickEditHandler";
	}
	
	@ForgeSubscribe
	public void onTossEvent(ItemTossEvent event) {
		if(!event.entity.worldObj.isRemote && getData(event.player.username) != null) {
			EditData data = getData(event.player.username);
			InventoryPlayer inventory = event.player.inventory;
			ItemStack stack = event.entityItem.getEntityItem();
			if(DeployList.containsItemStack(stack))
					if(stack.getItem() instanceof ItemBlock)
						event.setCanceled(true);
					else {
						GristSet cost = Minestuck.hardMode && data.connection.givenItems()[DeployList.getOrdinal(stack)+1]
								?DeployList.getSecondaryCost(stack):DeployList.getPrimaryCost(stack);
						if(GristHelper.canAfford(GristStorage.getGristSet(data.connection.getClientName()), cost)) {
							GristHelper.decrease(data.connection.getClientName(), cost);
							MinestuckPlayerTracker.updateGristCache(data.connection.getClientName());
							data.connection.givenItems()[DeployList.getOrdinal(stack)+1] = true;
							if(!data.connection.isMain())
								SkaianetHandler.giveItems(data.connection.getClientName());
						} else event.setCanceled(true);
					}
			
			else if(stack.getItem() instanceof ItemCardPunched && AlchemyRecipeHandler.getDecodedItem(stack).getItem() instanceof ItemCruxiteArtifact) {
				SburbConnection c = data.connection;
				c.givenItems()[0] = true;
				if(!c.isMain())
					SkaianetHandler.giveItems(c.getClientName());
			} else {
				event.setCanceled(true);
				if(inventory.getItemStack() != null)
					inventory.setItemStack(null);
				else inventory.setInventorySlotContents(inventory.currentItem, null);
			}
		}
	}
	
	@ForgeSubscribe
	public void onItemPickupEvent(EntityItemPickupEvent event) {
		if(!event.entity.worldObj.isRemote && getData(event.entityPlayer.username) != null)
			event.setCanceled(true);
	}
	
	@ForgeSubscribe
	public void onInteractEvent(PlayerInteractEvent event) {
		
		if(!event.entity.worldObj.isRemote && getData(event.entityPlayer.username) != null && event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) {
			event.useBlock = Event.Result.DENY;
			event.useItem = Event.Result.ALLOW;
		}
	}
	
	@ForgeSubscribe
	public void onAttackEvent(AttackEntityEvent event) {
		if(!event.entity.worldObj.isRemote && getData(event.entityPlayer.username) != null)
			event.setCanceled(true);
	}
	
	//Both sided.
	
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
	
	/**
	 * Server sided
	 */
	public static void updateInventory(EntityPlayerMP player, boolean[] givenItems, boolean enteredGame, String client) {	//Could need to have better effiency
		boolean inventoryChanged = false;
		for(int i = 0; i < player.inventory.mainInventory.length; i++) {
			ItemStack stack = player.inventory.mainInventory[i];
			if(stack != null && (GristRegistry.getGristConversion(stack) == null || !(stack.getItem() instanceof ItemBlock)) && !(DeployList.containsItemStack(stack) ||
					stack.getItem() instanceof ItemCardPunched && AlchemyRecipeHandler.getDecodedItem(stack).getItem() instanceof ItemCruxiteArtifact && (!Minestuck.hardMode || !givenItems[0]) && !enteredGame)) {
				player.inventory.mainInventory[i] = null;
				inventoryChanged = true;
			}
			if(stack != null && stack.stackSize > 1) {
				stack.stackSize = 1;
				inventoryChanged = true;
			}
		}
		
		ArrayList<ItemStack> itemsToRemove = new ArrayList();
		for(ItemStack stack : DeployList.getItemList()) {
			boolean shouldHave = !(Minestuck.hardMode && givenItems[DeployList.getOrdinal(stack)+1] && DeployList.getSecondaryCost(stack) == null
					|| DeployList.getTier(stack) > SessionHandler.availableTier(client));
			if(shouldHave && !player.inventory.hasItemStack(stack) && !(player.inventory.getItemStack() != null && player.inventory.getItemStack().isItemEqual(stack))) {
				if(player.inventory.addItemStackToInventory(stack))
					inventoryChanged = true;
			} else if(!shouldHave)
				itemsToRemove.add(stack);
		}
		
		
		if(player.inventory.getItemStack() != null)
			for(ItemStack stack : itemsToRemove)
				if(player.inventory.getItemStack().equals(stack)) {
					player.inventory.setItemStack(null);
					inventoryChanged = true;
					break;
				}
		
		for(int i = 0; i < player.inventory.mainInventory.length; i++) {
			ItemStack stack = player.inventory.mainInventory[i];
			if(stack == null)
				continue;
			for(ItemStack stack1 : itemsToRemove)
				if(stack.isItemEqual(stack1)) {
					player.inventory.mainInventory[i] = null;
					inventoryChanged = true;
					break;
				}
		}
		
		ItemStack stack = new ItemStack(Minestuck.punchedCard);
		NBTTagCompound nbt = new NBTTagCompound();
		stack.setTagCompound(nbt);
		nbt.setInteger("contentID", Minestuck.cruxiteArtifact.itemID);
		nbt.setInteger("contentMeta", SessionHandler.getEntryItem(client));
		if(!enteredGame) {
			if(!player.inventory.hasItemStack(stack) && !(player.inventory.getItemStack() != null && player.inventory.getItemStack().isItemEqual(stack))) {
				player.inventory.addItemStackToInventory(stack);
				inventoryChanged = true;
			}
		} else inventoryChanged = inventoryChanged || player.inventory.clearInventory(Minestuck.cruxiteArtifact.itemID, SessionHandler.getEntryItem(player.username)) != 0;
		
		if(inventoryChanged)
			MinecraftServer.getServer().getConfigurationManager().syncPlayerInventory(player);
	}
	
}
