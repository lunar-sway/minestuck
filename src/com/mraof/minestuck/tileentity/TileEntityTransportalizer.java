package com.mraof.minestuck.tileentity;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.util.Location;
import com.mraof.minestuck.world.storage.TransportalizerSavedData;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.util.ITeleporter;

import java.util.Iterator;
import java.util.Map;
import java.util.Random;

public class TileEntityTransportalizer extends TileEntity implements ITickable, ITeleporter
{
	private boolean enabled = true;
	private boolean active = true;
	String id = "";
	private String destId = "";
	
	public TileEntityTransportalizer()
	{
		super(MinestuckTiles.TRANSPORTALIZER);
	}
	
	@Override
	public void validate()
	{
		super.validate();
		if(!world.isRemote && active)
		{
			if(id.isEmpty())
				id = TransportalizerSavedData.get(world).findNewId(world.rand, new Location(this));
			else active = TransportalizerSavedData.get(world).set(id, new Location(this));
		}
	}
	
	@Override
	public void remove()
	{
		super.remove();
		if(!world.isRemote && active)
		{
			TransportalizerSavedData.get(world).remove(id, new Location(this));
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
	}
	
	public void teleport(Entity entity)
	{
		Location location = TransportalizerSavedData.get(world).get(this.destId);
		if(!enabled)
		{
			entity.timeUntilPortal = entity.getPortalCooldown();
			if(entity instanceof EntityPlayerMP)
				entity.sendMessage(new TextComponentTranslation("message.transportalizer.transportalizerDisabled"));
			return;
		}
		if(location != null && location.pos.getY() != -1)
		{
			WorldServer world = entity.getServer().getWorld(location.dim);
			TileEntityTransportalizer destTransportalizer = (TileEntityTransportalizer) world.getTileEntity(location.pos);
			if(destTransportalizer == null)
			{
				Debug.warn("Invalid transportalizer in map: " + this.destId + " at " + location);
				TransportalizerSavedData.get(world).remove(this.destId, location);
				this.destId = "";
				return;
			}
			
			if(!destTransportalizer.getEnabled()) { return; } // Fail silently to make it look as though the player entered an ID that doesn't map to a transportalizer.
			
			for(DimensionType id : MinestuckConfig.forbiddenDimensionsTpz)
				if(this.world.getDimension().getType() == id || location.dim == id)
				{
					entity.timeUntilPortal = entity.getPortalCooldown();
					if(entity instanceof EntityPlayerMP)
						entity.sendMessage(new TextComponentTranslation(this.world.getDimension().getType() == id ?"message.transportalizer.forbidden":"message.transportalizer.forbiddenDest"));
					return;
				}
			
			IBlockState block0 = this.world.getBlockState(this.pos.up());
			IBlockState block1 = this.world.getBlockState(this.pos.up(2));
			if(block0.getMaterial().blocksMovement() || block1.getMaterial().blocksMovement())
			{
				entity.timeUntilPortal = entity.getPortalCooldown();
				if(entity instanceof EntityPlayerMP)
					entity.sendMessage(new TextComponentTranslation("message.transportalizer.blocked"));
				return;
			}
			block0 = world.getBlockState(location.pos.up());
			block1 = world.getBlockState(location.pos.up(2));
			if(block0.getMaterial().blocksMovement() || block1.getMaterial().blocksMovement())
			{
				entity.timeUntilPortal = entity.getPortalCooldown();
				if(entity instanceof EntityPlayerMP)
					entity.sendMessage(new TextComponentTranslation("message.transportalizer.destinationBlocked"));
				return;
			}
			
			entity = entity.changeDimension(location.dim, destTransportalizer);
			if(entity != null)
				entity.timeUntilPortal = entity.getPortalCooldown();
		}
	}
	
	@Override
	public void placeEntity(World world, Entity entity, float yaw)
	{
		entity.setPosition(this.getPos().getX() + 0.5, this.getPos().getY() + 0.6, this.getPos().getZ() + 0.5);
	}
	
	public static void saveTransportalizers(NBTTagCompound compound)
	{
	
	}
	
	public static void loadTransportalizers(NBTTagCompound compound)
	{
	
	}
	
	public String getId()
	{
		return id;
	}
	
	public void setId(String id)
	{
		Location location = new Location(this);
		if(active && !this.id.isEmpty())
			TransportalizerSavedData.get(world).remove(this.id, location);
		
		this.id = id;
		active = TransportalizerSavedData.get(world).set(id, location);
	}
	
	public String getDestId()
	{
		return destId;
	}

	public void setDestId(String destId)
	{
		this.destId = destId;
		IBlockState state = world.getBlockState(pos);
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
		IBlockState state = world.getBlockState(pos);
		this.markDirty();
		world.notifyBlockUpdate(pos, state, state, 0);
	}

	@Override
	public void read(NBTTagCompound compound)
	{
		super.read(compound);
		this.destId = compound.getString("destId");
		this.id = compound.getString("idString");
		if(compound.contains("active"))
			this.active = compound.getBoolean("active");
	}

	@Override
	public NBTTagCompound write(NBTTagCompound compound)
	{
		super.write(compound);
		
		compound.putString("idString", id);
		compound.putString("destId", destId);
		compound.putBoolean("active", active);
		
		return compound;
	}
	
	@Override
	public NBTTagCompound getUpdateTag()
	{
		return this.write(new NBTTagCompound());
	}
	
	@Override
	public SPacketUpdateTileEntity getUpdatePacket()
	{
		return new SPacketUpdateTileEntity(this.pos, 2, this.write(new NBTTagCompound()));
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) 
	{
		this.read(pkt.getNbtCompound());
	}
	
}