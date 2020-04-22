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
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.List;

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
		super(MSTileEntityTypes.TRANSPORTALIZER, Entity.class);
	}
	
	@Override
	public void validate()
	{
		super.validate();
		if(!world.isRemote && active)
		{
			if(id.isEmpty())
				id = TransportalizerSavedData.get(world).findNewId(world.rand, GlobalPos.of(world.dimension.getType(), pos));
			else active = TransportalizerSavedData.get(world).set(id, GlobalPos.of(world.dimension.getType(), pos));
		}
	}
	
	@Override
	public void remove()
	{
		super.remove();
		if(!world.isRemote && active)
		{
			TransportalizerSavedData.get(world).remove(id, GlobalPos.of(world.dimension.getType(), pos));
		}
	}
	
	@Override
	public void tick()
	{
		if(world.isRemote)
			return;
		// Disable the transportalizer if it's being powered by a redstone signal.
		// Disabling a transportalizer prevents it from receiving or sending.
		// Recieving will fail silently. Sending will warn the player.
		if(world.isBlockPowered(this.getPos()))
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
		return new AxisAlignedBB(pos.getX() + 1D/16, pos.getY() + 8D/16, pos.getZ() + 1D/16, pos.getX() + 15D/16, pos.getY() + 1, pos.getZ() + 15D/16);
	}
	
	@Override
	protected void teleport(Entity entity)
	{
		GlobalPos location = TransportalizerSavedData.get(world).get(this.destId);
		if(!enabled)
		{
			entity.timeUntilPortal = entity.getPortalCooldown();
			if(entity instanceof ServerPlayerEntity)
				entity.sendMessage(new TranslationTextComponent(DISABLED));
			return;
		}
		if(location != null && location.getPos().getY() != -1)
		{
			ServerWorld world = entity.getServer().getWorld(location.getDimension());
			TransportalizerTileEntity destTransportalizer = (TransportalizerTileEntity) world.getTileEntity(location.getPos());
			if(destTransportalizer == null)
			{
				Debug.warn("Invalid transportalizer in map: " + this.destId + " at " + location);
				TransportalizerSavedData.get(world).remove(this.destId, location);
				this.destId = "";
				return;
			}
			
			if(!destTransportalizer.getEnabled()) { return; } // Fail silently to make it look as though the player entered an ID that doesn't map to a transportalizer.
			
			if(isDimensionForbidden(world.getDimension().getType()))
			{
				entity.timeUntilPortal = entity.getPortalCooldown();
				if(entity instanceof ServerPlayerEntity)
					entity.sendMessage(new TranslationTextComponent(FORBIDDEN));
				return;
			}
			if(isDimensionForbidden(location.getDimension()))
			{
				entity.timeUntilPortal = entity.getPortalCooldown();
				if(entity instanceof ServerPlayerEntity)
					entity.sendMessage(new TranslationTextComponent(FORBIDDEN_DESTINATION));
				return;
			}
			
			if(isBlocked(this.world, this.pos))
			{
				entity.timeUntilPortal = entity.getPortalCooldown();
				if(entity instanceof ServerPlayerEntity)
					entity.sendMessage(new TranslationTextComponent(BLOCKED));
				return;
			}
			
			if(isBlocked(world, location.getPos()))
			{
				entity.timeUntilPortal = entity.getPortalCooldown();
				if(entity instanceof ServerPlayerEntity)
					entity.sendMessage(new TranslationTextComponent(BLOCKED_DESTINATION));
				return;
			}
			
			entity = Teleport.teleportEntity(entity, (ServerWorld) destTransportalizer.world, location.getPos().getX() + 0.5, location.getPos().getY() + 0.6, location.getPos().getZ() + 0.5, entity.rotationYaw, entity.rotationPitch);
			if(entity != null)
				entity.timeUntilPortal = entity.getPortalCooldown();
		}
	}
	
	public static boolean isBlocked(World world, BlockPos pos)
	{
		BlockState block0 = world.getBlockState(pos.up());
		BlockState block1 = world.getBlockState(pos.up(2));
		return block0.getMaterial().blocksMovement() || block1.getMaterial().blocksMovement();
	}
	
	private static boolean isDimensionForbidden(DimensionType dim)
	{
		List<String> forbiddenTypes = MinestuckConfig.forbiddenDimensionTypesTpz.get();
		List<String> forbiddenDims = MinestuckConfig.forbiddenModDimensionsTpz.get();
		ResourceLocation modDim = dim.getModType() != null ? dim.getModType().getRegistryName() : null;
		return forbiddenTypes.contains(String.valueOf(dim.getRegistryName())) || forbiddenDims.contains(String.valueOf(modDim));
	}
	
	public String getId()
	{
		return id;
	}
	
	public void setId(String id)
	{
		if(world != null && !world.isRemote)
		{
			GlobalPos location = GlobalPos.of(world.dimension.getType(), pos);
			if(active && !this.id.isEmpty())
				TransportalizerSavedData.get(world).remove(this.id, location);
			
			this.id = id;
			active = TransportalizerSavedData.get(world).set(id, location);
		}
	}
	
	public String getDestId()
	{
		return destId;
	}

	public void setDestId(String destId)
	{
		this.destId = destId;
		BlockState state = world.getBlockState(pos);
		this.markDirty();
		world.notifyBlockUpdate(pos, state, state, 0);
	}

	public boolean getEnabled() { return enabled; }
	
	public boolean getActive()
	{
		return active;
	}
	
	public void setEnabled(boolean enabled)
	{
		this.enabled = enabled;
		BlockState state = world.getBlockState(pos);
		this.markDirty();
		world.notifyBlockUpdate(pos, state, state, 0);
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
	public void read(CompoundNBT compound)
	{
		super.read(compound);
		this.destId = compound.getString("destId");
		this.id = compound.getString("idString");
		if(compound.contains("active"))
			this.active = compound.getBoolean("active");
	}

	@Override
	public CompoundNBT write(CompoundNBT compound)
	{
		super.write(compound);
		
		compound.putString("idString", id);
		compound.putString("destId", destId);
		compound.putBoolean("active", active);
		
		return compound;
	}
	
	@Override
	public CompoundNBT getUpdateTag()
	{
		return this.write(new CompoundNBT());
	}
	
	@Override
	public SUpdateTileEntityPacket getUpdatePacket()
	{
		return new SUpdateTileEntityPacket(this.pos, 2, this.write(new CompoundNBT()));
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt)
	{
		this.read(pkt.getNbtCompound());
	}
	
}