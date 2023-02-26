package com.mraof.minestuck.computer.editmode;

import com.mraof.minestuck.entity.DecoyEntity;
import com.mraof.minestuck.alchemy.GristSet;
import com.mraof.minestuck.network.MSPacketHandler;
import com.mraof.minestuck.network.ServerEditPacket;
import com.mraof.minestuck.network.data.GristCachePacket;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.player.PlayerIdentifier;
import com.mraof.minestuck.skaianet.SburbConnection;
import com.mraof.minestuck.util.Teleport;
import com.mraof.minestuck.player.PlayerSavedData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Data structure used by the server sided EditHandler
 * Contains the player, player decoy, and connection
 * involved in the editmode session that this class represents.
 * @author Kirderf1
 */
public class EditData
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	EditData(DecoyEntity decoy, ServerPlayer player, SburbConnection c)
	{
		this.decoy = decoy;
		this.player = player;
		this.connection = c;
	}
	
	private final DecoyEntity decoy;
	
	final SburbConnection connection;
	
	private final ServerPlayer player;
	
	private boolean isRecovering;
	
	public SburbConnection getConnection()
	{
		return connection;
	}
	
	/**
	 * @return the player that activated and is in editmode (not necessarily the server player of the connection)
	 */
	public ServerPlayer getEditor()
	{
		return player;
	}
	
	/**
	 * @return the decoy entity that took the editors place
	 */
	public DecoyEntity getDecoy()
	{
		return decoy;
	}
	
	public void sendGristCacheToEditor()
	{
		GristSet cache = PlayerSavedData.getData(connection.getClientIdentifier(), getEditor().server).getGristCache();
		ServerPlayer editor = getEditor();
		GristCachePacket packet = new GristCachePacket(cache, true);
		MSPacketHandler.sendToPlayer(packet, editor);
	}
	
	public void sendGivenItemsToEditor()
	{
		
		ServerEditPacket packet = ServerEditPacket.givenItems(DeployList.getDeployListTag(player.server, connection));
		MSPacketHandler.sendToPlayer(packet, getEditor());
	}
	
	public CompoundTag writeRecoveryData()
	{
		CompoundTag nbt = new CompoundTag();
		
		new PlayerRecovery(decoy).write(nbt);
		new ConnectionRecovery(this).write(nbt);
		
		return nbt;
	}
	
	public static PlayerRecovery readRecovery(CompoundTag nbt)
	{
		return new PlayerRecovery(nbt);
	}
	
	public static ConnectionRecovery readExtraRecovery(CompoundTag nbt)
	{
		if(nbt.contains("edit_inv"))
			return new ConnectionRecovery(nbt);
		else return null;
	}
	
	void recover()
	{
		isRecovering = true;
		try
		{
			new ConnectionRecovery(this).recover(connection, player);
			new PlayerRecovery(decoy).recover(player, true);
		} finally {
			isRecovering = false;
		}
	}
	
	boolean isRecovering()
	{
		return isRecovering;
	}
	
	public static class PlayerRecovery
	{
		private final ResourceKey<Level> dimension;
		private final double posX, posY, posZ;
		private final float rotationYaw, rotationPitch;
		private final GameType gameType;
		private final CompoundTag capabilities;
		private final float health;
		private final CompoundTag foodStats;
		private final ListTag inventory;
		
		private PlayerRecovery(DecoyEntity decoy)
		{
			dimension = decoy.level.dimension();
			posX = decoy.getX();
			posY = decoy.getY();
			posZ = decoy.getZ();
			rotationYaw = decoy.getYRot();
			rotationPitch = decoy.getXRot();
			gameType = decoy.gameType;
			capabilities = decoy.capabilities.copy();
			health = decoy.getHealth();
			foodStats = decoy.getFoodStatsNBT();
			inventory = decoy.inventory.save(new ListTag());
		}
		
		private PlayerRecovery(CompoundTag nbt)
		{
			dimension = Level.RESOURCE_KEY_CODEC.parse(NbtOps.INSTANCE, nbt.get("dim")).resultOrPartial(LOGGER::error).orElse(null);
			posX = nbt.getDouble("x");
			posY = nbt.getDouble("y");
			posZ = nbt.getDouble("z");
			rotationYaw = nbt.getFloat("rot_yaw");
			rotationPitch = nbt.getFloat("rot_pitch");
			
			gameType = GameType.byId(nbt.getInt("game_type"));
			capabilities = nbt.getCompound("capabilities");
			health = nbt.getFloat("health");
			foodStats = nbt.getCompound("food");
			inventory = nbt.getList("inv", Tag.TAG_COMPOUND);
		}
		
		public CompoundTag write(CompoundTag nbt)
		{
			if(dimension != null)
				ResourceLocation.CODEC.encodeStart(NbtOps.INSTANCE, dimension.location()).resultOrPartial(LOGGER::error)
						.ifPresent(tag -> nbt.put("dim", tag));
			nbt.putDouble("x", posX);
			nbt.putDouble("y", posY);
			nbt.putDouble("z", posZ);
			nbt.putFloat("rot_yaw", rotationYaw);
			nbt.putFloat("rot_pitch", rotationPitch);
			
			nbt.putInt("game_type", gameType.getId());
			nbt.put("capabilities", capabilities);
			nbt.putFloat("health", health);
			nbt.put("food", foodStats);
			nbt.put("inv", inventory);
			
			return nbt;
		}
		
		void recover(ServerPlayer player, boolean throwException)
		{
			player.closeContainer();
			ResourceKey<Level> dim = dimension;
			if(dim == null)
			{
				LOGGER.warn("Couldn't load original dimension for player {}. Defaulting to overworld.", player.getGameProfile().getName());
				dim = Level.OVERWORLD;
			}
			ServerLevel world = player.server.getLevel(dim);
			if(player.level.dimension() != dim && (world == null || Teleport.teleportEntity(player, world) == null))
			{
				if(throwException)
					throw new IllegalStateException("Unable to teleport editmode player "+player.getGameProfile().getName()+" to their original dimension with world: " + world);
				else LOGGER.warn("Unable to teleport editmode player {} to their original dimension with world: {}", player.getGameProfile().getName(), world);
			}
			player.connection.teleport(posX, posY, posZ, rotationYaw, rotationPitch);
			player.setGameMode(gameType);
			player.getAbilities().loadSaveData(capabilities);
			player.onUpdateAbilities();
			player.fallDistance = 0;
			
			player.setHealth(health);
			player.getFoodData().readAdditionalSaveData(foodStats);
			player.getInventory().load(inventory);
		}
	}
	
	public static class ConnectionRecovery
	{
		private final PlayerIdentifier clientPlayer;
		private final ListTag inventory;
		
		private ConnectionRecovery(EditData data)
		{
			clientPlayer = data.connection.getClientIdentifier();
			inventory = data.player.getInventory().save(new ListTag());
		}
		
		private ConnectionRecovery(CompoundTag nbt)
		{
			clientPlayer = IdentifierHandler.load(nbt, "client");
			inventory = nbt.getList("edit_inv", Tag.TAG_COMPOUND);
		}
		
		private void write(CompoundTag nbt)
		{
			clientPlayer.saveToNBT(nbt, "client");
			nbt.put("edit_inv", inventory);
		}
		
		public PlayerIdentifier getClientPlayer()
		{
			return clientPlayer;
		}
		
		public void recover(SburbConnection connection)
		{
			recover(connection, null);
		}
		
		void recover(SburbConnection connection, ServerPlayer editPlayer)
		{
			if(connection != null)
			{
				connection.putEditmodeInventory(this.inventory);
				if(editPlayer != null)
				{
					ServerEditHandler.lastEditmodePos.put(connection, new Vec3(editPlayer.getX(), editPlayer.getY(), editPlayer.getZ()));
				}
			} else LOGGER.warn("Unable to perform editmode recovery for the connection for client player {}. Got null connection.", clientPlayer.getUsername());
		}
	}
}