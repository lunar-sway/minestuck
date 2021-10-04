package com.mraof.minestuck.tileentity;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.util.Teleport;
import com.mraof.minestuck.world.storage.TransportalizerSavedData;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.util.INameable;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class TransportalizerTileEntity extends OnCollisionTeleporterTileEntity<Entity> implements INameable
{
	public static final String DISABLED = "minestuck.transportalizer.disabled";
	public static final String BLOCKED = "minestuck.transportalizer.blocked";
	public static final String BLOCKED_DESTINATION = "minestuck.transportalizer.blocked_destination";
	public static final String FORBIDDEN = "minestuck.transportalizer.forbidden";
	public static final String FORBIDDEN_DESTINATION = "minestuck.transportalizer.forbidden_destination";
	
	private boolean enabled = true;
	private boolean active = true;
	String id = "";
	private String destId = "";
	
	public TransportalizerTileEntity()
	{
		super(MSTileEntityTypes.TRANSPORTALIZER.get(), Entity.class);
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
	
	@Override
	public void setRemoved()
	{
		super.setRemoved();
		if(!level.isClientSide && active)
		{
			TransportalizerSavedData.get(level).remove(id, GlobalPos.of(level.dimension(), worldPosition));
		}
	}
	
	@Override
	public void tick()
	{
		if(level.isClientSide)
			return;
		// Disable the transportalizer if it's being powered by a redstone signal.
		// Disabling a transportalizer prevents it from receiving or sending.
		// Recieving will fail silently. Sending will warn the player.
		if(level.hasNeighborSignal(this.getBlockPos()))
		{
			if(enabled) { setEnabled(false); }
		}
		else {
			if(!enabled) { setEnabled(true); }
		}
		
		super.tick();
	}
	
	@Override
	protected AxisAlignedBB getTeleportField()
	{
		return new AxisAlignedBB(worldPosition.getX() + 1D/16, worldPosition.getY() + 8D/16, worldPosition.getZ() + 1D/16, worldPosition.getX() + 15D/16, worldPosition.getY() + 1, worldPosition.getZ() + 15D/16);
	}
	
	@Override
	protected void teleport(Entity entity)
	{
		GlobalPos location = TransportalizerSavedData.get(level).get(this.destId);
		if(!enabled)
		{
			entity.setPortalCooldown();
			if(entity instanceof ServerPlayerEntity)
				entity.sendMessage(new TranslationTextComponent(DISABLED), Util.NIL_UUID);
			return;
		}
		if(location != null && location.pos().getY() != -1)
		{
			ServerWorld world = entity.getServer().getLevel(location.dimension());
			TransportalizerTileEntity destTransportalizer = (TransportalizerTileEntity) world.getBlockEntity(location.pos());
			if(destTransportalizer == null)
			{
				Debug.warn("Invalid transportalizer in map: " + this.destId + " at " + location);
				TransportalizerSavedData.get(world).remove(this.destId, location);
				this.destId = "";
				return;
			}
			
			if(!destTransportalizer.getEnabled()) { return; } // Fail silently to make it look as though the player entered an ID that doesn't map to a transportalizer.
			
			if(isDimensionForbidden(level))
			{
				entity.setPortalCooldown();
				if(entity instanceof ServerPlayerEntity)
					entity.sendMessage(new TranslationTextComponent(FORBIDDEN), Util.NIL_UUID);
				return;
			}
			if(isDimensionForbidden(world))
			{
				entity.setPortalCooldown();
				if(entity instanceof ServerPlayerEntity)
					entity.sendMessage(new TranslationTextComponent(FORBIDDEN_DESTINATION), Util.NIL_UUID);
				return;
			}
			
			if(isBlocked(this.level, this.worldPosition))
			{
				entity.setPortalCooldown();
				if(entity instanceof ServerPlayerEntity)
					entity.sendMessage(new TranslationTextComponent(BLOCKED), Util.NIL_UUID);
				return;
			}
			
			if(isBlocked(world, location.pos()))
			{
				entity.setPortalCooldown();
				if(entity instanceof ServerPlayerEntity)
					entity.sendMessage(new TranslationTextComponent(BLOCKED_DESTINATION), Util.NIL_UUID);
				return;
			}
			
			entity = Teleport.teleportEntity(entity, (ServerWorld) destTransportalizer.level, location.pos().getX() + 0.5, location.pos().getY() + 0.6, location.pos().getZ() + 0.5, entity.yRot, entity.xRot);
			if(entity != null)
				entity.setPortalCooldown();
		}
	}
	
	public static boolean isBlocked(World world, BlockPos pos)
	{
		BlockState block0 = world.getBlockState(pos.above());
		BlockState block1 = world.getBlockState(pos.above(2));
		return block0.getMaterial().blocksMotion() || block1.getMaterial().blocksMotion();
	}
	
	private static boolean isDimensionForbidden(World world)
	{
		List<String> forbiddenWorlds = MinestuckConfig.SERVER.forbiddenWorldsTpz.get();
		List<String> forbiddenDimTypes = MinestuckConfig.SERVER.forbiddenDimensionTypesTpz.get();
		
		Optional<RegistryKey<DimensionType>> typeKey = world.registryAccess().dimensionTypes().getResourceKey(world.dimensionType());
		
		return forbiddenWorlds.contains(String.valueOf(world.dimension().location()))
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

	public boolean getEnabled() { return enabled; }
	
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
	public ITextComponent getName()
	{
		return new StringTextComponent("Transportalizer");
	}
	
	@Override
	public ITextComponent getDisplayName()
	{
		return hasCustomName() ? getCustomName() : getName();
	}
	
	@Nullable
	@Override
	public ITextComponent getCustomName()
	{
		return id.isEmpty() ? null : new StringTextComponent(id);
	}
	
	@Override
	public void load(BlockState state, CompoundNBT nbt)
	{
		super.load(state, nbt);
		this.destId = nbt.getString("destId");
		this.id = nbt.getString("idString");
		if(nbt.contains("active"))
			this.active = nbt.getBoolean("active");
	}

	@Override
	public CompoundNBT save(CompoundNBT compound)
	{
		super.save(compound);
		
		compound.putString("idString", id);
		compound.putString("destId", destId);
		compound.putBoolean("active", active);
		
		return compound;
	}
	
	@Override
	public CompoundNBT getUpdateTag()
	{
		return this.save(new CompoundNBT());
	}
	
	@Override
	public SUpdateTileEntityPacket getUpdatePacket()
	{
		return new SUpdateTileEntityPacket(this.worldPosition, 2, this.save(new CompoundNBT()));
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt)
	{
		this.load(getBlockState(), pkt.getTag());
	}
	
}