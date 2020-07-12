package com.mraof.minestuck.tileentity;

import com.mraof.minestuck.client.gui.ComputerScreen;
import com.mraof.minestuck.computer.ProgramData;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.player.PlayerIdentifier;
import com.mraof.minestuck.skaianet.SburbConnection;
import com.mraof.minestuck.skaianet.SkaianetHandler;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;

import javax.annotation.Nullable;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class ComputerTileEntity extends TileEntity
{
	public ComputerTileEntity()
	{
		super(MSTileEntityTypes.COMPUTER);
	}
	
	/**
	 * 0 = client, 1 = server
	 */
	public Hashtable<Integer, Boolean> installedPrograms = new Hashtable<Integer, Boolean>();
	public ComputerScreen gui;
	public PlayerIdentifier owner;
	//client side only
	public int ownerId;
	public Hashtable<Integer, String> latestmessage = new Hashtable<Integer, String>();
	public CompoundNBT programData = new CompoundNBT();
	public int programSelected = -1;
	
	@Override
	public void read(CompoundNBT compound)
	{
		super.read(compound);
		if (compound.contains("programs"))
		{
			CompoundNBT programs = compound.getCompound("programs");
			for (Object name : programs.keySet())
			{
				installedPrograms.put(programs.getInt((String)name), true);
			}
		}

		latestmessage.clear();
		for(Entry<Integer,Boolean> e : installedPrograms.entrySet())
			if(e.getValue())
				latestmessage.put(e.getKey(), compound.getString("text" + e.getKey()));

		programData = compound.getCompound("programData");

		if(!compound.contains("programData"))
		{
			CompoundNBT nbt = new CompoundNBT();
			nbt.putBoolean("connectedToServer", compound.getBoolean("connectServer"));
			nbt.putBoolean("isResuming", compound.getBoolean("resumeClient"));
			programData.put("program_0", nbt);
			nbt = new CompoundNBT();
			nbt.putString("connectedClient", compound.getString("connectClient"));
			nbt.putBoolean("isOpen", compound.getBoolean("serverOpen"));
			programData.put("program_1", nbt);
		}
		if(compound.contains("ownerId"))
			ownerId = compound.getInt("ownerId");
		else this.owner = IdentifierHandler.load(compound, "owner");
		if(gui != null)
			gui.updateGui();
	}
	
	@Override
	public CompoundNBT write(CompoundNBT compound)
	{
		super.write(compound);
		CompoundNBT programs = new CompoundNBT();
		Iterator<Entry<Integer, Boolean>> it = this.installedPrograms.entrySet().iterator();
		//int place = 0;
		while (it.hasNext()) 
		{
			Map.Entry<Integer, Boolean> pairs = it.next();
			int program = pairs.getKey();
			programs.putInt("program" + program,program);
			//place++;
		}
		for(Entry<Integer, String> e : latestmessage.entrySet())
			compound.putString("text" + e.getKey(), e.getValue());
		compound.put("programs",programs);
		compound.put("programData", programData.copy());
		if (owner != null) 
			owner.saveToNBT(compound, "owner");
		return compound;
	}
	
	@Override
	public CompoundNBT getUpdateTag()
	{
		CompoundNBT tagCompound = this.write(new CompoundNBT());
		tagCompound.remove("owner");
		tagCompound.remove("ownerMost");
		tagCompound.remove("ownerLeast");
		if(owner != null)
			tagCompound.putInt("ownerId", owner.getId());
		if(hasProgram(1))
		{
			SburbConnection c = SkaianetHandler.get(getWorld()).getServerConnection(this);
			if(c != null)
				tagCompound.getCompound("programData").getCompound("program_1").putInt("connectedClient", c.getClientIdentifier().getId());
		}
		return tagCompound;
	}
	
	@Nullable
	@Override
	public SUpdateTileEntityPacket getUpdatePacket()
	{
		return new SUpdateTileEntityPacket(this.pos, 2, getUpdateTag());
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt)
	{
		this.read(pkt.getNbtCompound());
	}

	public boolean hasProgram(int id) 
	{
		return installedPrograms.get(id) == null ? false:installedPrograms.get(id);
	}

	public CompoundNBT getData(int id)
	{
		if(!programData.contains("program_"+id))
			programData.put("program_" + id, new CompoundNBT());
		return programData.getCompound("program_" + id);
	}

	public void closeAll() 
	{
		for(Entry<Integer, Boolean> entry : installedPrograms.entrySet())
			if(entry.getValue() && entry.getKey() != -1)
				ProgramData.closeProgram(entry.getKey(), this);
	}

	public void connected(PlayerIdentifier player, boolean isClient)
	{
		if(isClient)
		{
			getData(0).putBoolean("isResuming", false);
			getData(0).putBoolean("connectedToServer", true);
		}
		else
		{
			this.getData(1).putBoolean("isOpen", false);
		}
	}
	
	public void markBlockForUpdate()
	{
		BlockState state = world.getBlockState(pos);
		this.world.notifyBlockUpdate(pos, state, state, 3);
	}
	
}
