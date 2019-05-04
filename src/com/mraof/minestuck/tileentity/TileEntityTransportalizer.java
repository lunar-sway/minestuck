package com.mraof.minestuck.tileentity;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.util.Location;
import com.mraof.minestuck.util.Teleport;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.WorldServer;
import net.minecraft.world.dimension.DimensionType;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

public class TileEntityTransportalizer extends TileEntity implements ITickable
{
	public static HashMap<String, Location> transportalizers = new HashMap<String, Location>();
	private static Random rand = new Random();
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
				id = getUnusedId();
			put(id, new Location(this.pos, world.getDimension().getType()));
		}
	}
	
	@Override
	public void remove()
	{
		super.remove();
		if(!world.isRemote && active)
		{
			Location location = transportalizers.get(id);
			if(location.equals(new Location(this.pos, this.world.getDimension().getType())))
				transportalizers.remove(id);
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

	public String getUnusedId()
	{
		String unusedId = "";
		do
		{
			for(int i = 0; i < 4; i++)
			{
				unusedId += (char) (rand.nextInt(26) + 'A');
			}
		} 
		while(transportalizers.containsKey(unusedId));

		return unusedId;
	}

	public static void put(String key, Location location)
	{
		transportalizers.put(key, location);
	}
	
	public void teleport(Entity entity)
	{
		Location location = transportalizers.get(this.destId);
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
				transportalizers.remove(this.destId);
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
			
			Teleport.teleportEntity(entity, location.dim, null, destTransportalizer.pos.getX() + 0.5, destTransportalizer.pos.getY() + 0.6, destTransportalizer.pos.getZ() + 0.5);
			entity.timeUntilPortal = entity.getPortalCooldown();
		}
	}
	
	public static void saveTransportalizers(NBTTagCompound compound)
	{
		NBTTagCompound transportalizerTagCompound = new NBTTagCompound();
		Iterator<Map.Entry<String, Location>> it = transportalizers.entrySet().iterator();
		while(it.hasNext())
		{
			Map.Entry<String, Location> entry = it.next();
			Location location = entry.getValue();
			NBTTagCompound locationTag = new NBTTagCompound();
			locationTag.setInt("x", location.pos.getX());
			locationTag.setInt("y", location.pos.getY());
			locationTag.setInt("z", location.pos.getZ());
			ResourceLocation dimName = DimensionType.func_212678_a(location.dim);
			if(dimName != null)
				locationTag.setString("dim", dimName.toString());
			else
			{
				Debug.warnf("Could not save transportalizer %s with dimension %s. Could not get dimension name!", entry.getKey(), location.dim);
				continue;
			}
			transportalizerTagCompound.setTag(entry.getKey(), locationTag);
		}
		compound.setTag("transportalizers", transportalizerTagCompound);
	}
	
	public static void loadTransportalizers(NBTTagCompound compound)
	{
		for(String id : compound.keySet())
		{
			NBTTagCompound locationTag = compound.getCompound((String)id);
			DimensionType type = DimensionType.byName(ResourceLocation.makeResourceLocation(compound.getString("dim")));
			if(type != null)
				put(id, new Location(locationTag.getInt("x"), locationTag.getInt("y"), locationTag.getInt("z"), type));
			else Debug.warnf("Found transportalizer %s with invalid dimension type %s. This transportalizer will be ignored.", id, compound.getString("dim"));
		}
	}
	
	public String getId()
	{
		return id;
	}
	
	public void setId(String id)
	{
		if(active && !this.id.isEmpty())
			transportalizers.remove(this.id);
		Location location = transportalizers.get(id);
		this.id = id;
		if(location == null || this.hasWorld() && location.dim == getWorld().getDimension().getType() && location.pos.equals(this.getPos()))
		{
			transportalizers.put(id, new Location(getPos(), getWorld().getDimension().getType()));
		} else
		{
			active = false;
		}
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
		if(compound.hasKey("active"))
			this.active = compound.getBoolean("active");
	}

	@Override
	public NBTTagCompound write(NBTTagCompound compound)
	{
		super.write(compound);
		
		compound.setString("idString", id);
		compound.setString("destId", destId);
		compound.setBoolean("active", active);
		
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