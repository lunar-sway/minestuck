package com.mraof.minestuck.blockentity;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.util.MSParticleType;
import com.mraof.minestuck.util.MSSoundEvents;
import com.mraof.minestuck.util.Teleport;
import com.mraof.minestuck.world.storage.TransportalizerSavedData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Nameable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.phys.AABB;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class TransportalizerBlockEntity extends OnCollisionTeleporterBlockEntity<Entity> implements Nameable
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	public static final String DISABLED = "minestuck.transportalizer.disabled";
	public static final String BLOCKED = "minestuck.transportalizer.blocked";
	public static final String BLOCKED_DESTINATION = "minestuck.transportalizer.blocked_destination";
	public static final String FORBIDDEN = "minestuck.transportalizer.forbidden";
	public static final String FORBIDDEN_DESTINATION = "minestuck.transportalizer.forbidden_destination";
	
	private boolean enabled = true;
	private boolean active = true;
	String id = "";
	private String destId = "";
	
	public TransportalizerBlockEntity(BlockPos pos, BlockState state)
	{
		super(MSBlockEntityTypes.TRANSPORTALIZER.get(), pos, state, Entity.class);
	}
	
	@Override
	public void clearRemoved()
	{
		super.clearRemoved();
		if(!level.isClientSide && active)
		{
			if(id.isEmpty())
				id = TransportalizerSavedData.get(level).findNewId(level.random, GlobalPos.of(level.dimension(), worldPosition));
			else active = TransportalizerSavedData.get(level).set(id, GlobalPos.of(level.dimension(), worldPosition));
		}
	}
	
	private boolean unloaded = false;
	
	@Override
	public void onChunkUnloaded()
	{
		super.onChunkUnloaded();
		this.unloaded = true;
	}
	
	@Override
	public void setRemoved()
	{
		super.setRemoved();
		if(!level.isClientSide && active && !unloaded)
		{
			TransportalizerSavedData.get(level).remove(id, GlobalPos.of(level.dimension(), worldPosition));
		}
	}
	
	public static <E extends Entity> void transportalizerTick(Level level, BlockPos pos, BlockState state, TransportalizerBlockEntity blockEntity)
	{
		if(level.isClientSide)
			return;
		// Disable the transportalizer if it's being powered by a redstone signal.
		// Disabling a transportalizer prevents it from receiving or sending.
		// Recieving will fail silently. Sending will warn the player.
		if(level.hasNeighborSignal(blockEntity.getBlockPos()))
		{
			if(blockEntity.enabled)
			{
				blockEntity.setEnabled(false);
			}
		} else
		{
			if(!blockEntity.enabled)
			{
				blockEntity.setEnabled(true);
			}
		}
		
		serverTick(level, pos, state, blockEntity);
	}
	
	@Override
	protected AABB getTeleportField()
	{
		return new AABB(worldPosition.getX() + 1D / 16, worldPosition.getY() + 8D / 16, worldPosition.getZ() + 1D / 16, worldPosition.getX() + 15D / 16, worldPosition.getY() + 1, worldPosition.getZ() + 15D / 16);
	}
	
	@Override
	protected void teleport(Entity entity)
	{
		if(this.level == null)
			return;
		
		GlobalPos destination = TransportalizerSavedData.get(level).get(this.destId);
		if(!enabled)
		{
			entity.setPortalCooldown();
			if(entity instanceof ServerPlayer)
				entity.sendSystemMessage(Component.translatable(DISABLED));
			return;
		}
		if(destination != null && entity.getServer() != null)
		{
			ServerLevel destinationLevel = entity.getServer().getLevel(destination.dimension());
			if(destinationLevel == null)
			{
				LOGGER.warn("Transportalizer at invalid dimension in map: {} at {}", this.destId, destination);
				TransportalizerSavedData.get(level).remove(this.destId, destination);
				this.destId = "";
				return;
			}
			if(!(destinationLevel.getBlockEntity(destination.pos()) instanceof TransportalizerBlockEntity destTransportalizer))
			{
				LOGGER.warn("Invalid transportalizer in map: {} at {}", this.destId, destination);
				TransportalizerSavedData.get(level).remove(this.destId, destination);
				this.destId = "";
				return;
			}
			
			if(!destTransportalizer.getEnabled())
			{
				return; // Fail silently to make it look as though the player entered an ID that doesn't map to a transportalizer.
			}
			
			if(isDimensionForbidden(this.level))
			{
				entity.setPortalCooldown();
				if(entity instanceof ServerPlayer)
					entity.sendSystemMessage(Component.translatable(FORBIDDEN));
				return;
			}
			if(isDimensionForbidden(destinationLevel))
			{
				entity.setPortalCooldown();
				if(entity instanceof ServerPlayer)
					entity.sendSystemMessage(Component.translatable(FORBIDDEN_DESTINATION));
				return;
			}
			
			if(isBlocked(this.level, this.worldPosition))
			{
				entity.setPortalCooldown();
				if(entity instanceof ServerPlayer)
					entity.sendSystemMessage(Component.translatable(BLOCKED));
				return;
			}
			
			if(isBlocked(destinationLevel, destination.pos()))
			{
				entity.setPortalCooldown();
				if(entity instanceof ServerPlayer)
					entity.sendSystemMessage(Component.translatable(BLOCKED_DESTINATION));
				return;
			}
			
			ServerLevel originLevel = (ServerLevel) this.level;
			
			entity = Teleport.teleportEntity(entity, (ServerLevel) destTransportalizer.level, destination.pos().getX() + 0.5, destination.pos().getY() + 0.6, destination.pos().getZ() + 0.5, entity.getYRot(), entity.getXRot());
			if(entity != null)
			{
				entity.setPortalCooldown();
				
				if(originLevel != null)
					originLevel.sendParticles(MSParticleType.TRANSPORTALIZER.get(), getBlockPos().getX() + 0.5, getBlockPos().getY() + 1, getBlockPos().getZ() + 0.5, 1, 0, 0, 0, 0);
				this.level.playSound(null, this.getBlockPos(), MSSoundEvents.TRANSPORTALIZER_TELEPORT.get(), SoundSource.BLOCKS, 1F, 1F);
				destinationLevel.sendParticles(MSParticleType.TRANSPORTALIZER.get(), destination.pos().getX() + 0.5, destination.pos().getY() + 1, destination.pos().getZ() + 0.5, 1, 0, 0, 0, 0);
			}
		}
	}
	
	public static boolean isBlocked(Level level, BlockPos pos)
	{
		BlockState block0 = level.getBlockState(pos.above());
		BlockState block1 = level.getBlockState(pos.above(2));
		return block0.getMaterial().blocksMotion() || block1.getMaterial().blocksMotion();
	}
	
	private static boolean isDimensionForbidden(Level level)
	{
		List<String> forbiddenWorlds = MinestuckConfig.SERVER.forbiddenWorldsTpz.get();
		List<String> forbiddenDimTypes = MinestuckConfig.SERVER.forbiddenDimensionTypesTpz.get();
		
		Optional<ResourceKey<DimensionType>> typeKey = level.registryAccess().registryOrThrow(Registry.DIMENSION_TYPE_REGISTRY).getResourceKey(level.dimensionType());
		
		return forbiddenWorlds.contains(String.valueOf(level.dimension().location()))
				|| typeKey.isPresent() && forbiddenDimTypes.contains(String.valueOf(typeKey.get().location()));
	}
	
	public String getId()
	{
		return id;
	}
	
	public void setId(String id)
	{
		if(level != null && !level.isClientSide)
		{
			GlobalPos location = GlobalPos.of(level.dimension(), worldPosition);
			if(active && !this.id.isEmpty())
				TransportalizerSavedData.get(level).remove(this.id, location);
			
			this.id = id;
			active = TransportalizerSavedData.get(level).set(id, location);
		}
	}
	
	public String getDestId()
	{
		return destId;
	}
	
	public void setDestId(String destId)
	{
		this.destId = destId;
		BlockState state = level.getBlockState(worldPosition);
		this.setChanged();
		level.sendBlockUpdated(worldPosition, state, state, 0);
	}
	
	public boolean getEnabled()
	{
		return enabled;
	}
	
	public boolean isActive()
	{
		return active;
	}
	
	public void tryReactivate()
	{
		active = TransportalizerSavedData.get(level).set(id, GlobalPos.of(level.dimension(), worldPosition));
	}
	
	public void setEnabled(boolean enabled)
	{
		this.enabled = enabled;
		BlockState state = level.getBlockState(worldPosition);
		this.setChanged();
		level.sendBlockUpdated(worldPosition, state, state, 0);
	}
	
	@Override
	public Component getName()
	{
		return Component.literal("Transportalizer");
	}
	
	@Override
	public Component getDisplayName()
	{
		return hasCustomName() ? getCustomName() : getName();
	}
	
	@Nullable
	@Override
	public Component getCustomName()
	{
		return id.isEmpty() ? null : Component.literal(id);
	}
	
	@Override
	public void load(CompoundTag nbt)
	{
		super.load(nbt);
		this.destId = nbt.getString("destId");
		this.id = nbt.getString("idString");
		if(nbt.contains("active"))
			this.active = nbt.getBoolean("active");
	}
	
	@Override
	public void saveAdditional(CompoundTag compound)
	{
		super.saveAdditional(compound);
		
		compound.putString("idString", id);
		compound.putString("destId", destId);
		compound.putBoolean("active", active);
	}
	
	@Override
	public CompoundTag getUpdateTag()
	{
		return this.saveWithoutMetadata();
	}
	
	@Override
	public Packet<ClientGamePacketListener> getUpdatePacket()
	{
		return ClientboundBlockEntityDataPacket.create(this);
	}
	
}