package com.mraof.minestuck.tileentity;

import com.mraof.minestuck.block.BlockComputerOn;
import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.client.gui.GuiComputer;
import com.mraof.minestuck.network.skaianet.ComputerData;
import com.mraof.minestuck.network.skaianet.SburbConnection;
import com.mraof.minestuck.network.skaianet.SkaianetHandler;
import com.mraof.minestuck.util.ComputerProgram;
import com.mraof.minestuck.util.IdentifierHandler;
import com.mraof.minestuck.util.IdentifierHandler.PlayerIdentifier;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class TileEntityComputer extends TileEntity
{
	public TileEntityComputer()
	{
		super(MinestuckTiles.computer);
	}
	
	/**
	 * 0 = client, 1 = server
	 */
	public Hashtable<Integer, Boolean> installedPrograms = new Hashtable<Integer, Boolean>();
	public GuiComputer gui;
	public PlayerIdentifier owner;
	@OnlyIn(Dist.CLIENT)
	public int ownerId;
	public Hashtable<Integer, String> latestmessage = new Hashtable<Integer, String>();
	public NBTTagCompound programData = new NBTTagCompound();
	public int programSelected = -1;
	
	@OnlyIn(Dist.CLIENT)
	public ComputerProgram program;
	
	@Override
	public void read(NBTTagCompound compound)
	{
		super.read(compound);
		if (compound.getCompound("programs") != null)
		{
			NBTTagCompound programs = compound.getCompound("programs");
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

		if(!compound.hasKey("programData"))
		{
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setBoolean("connectedToServer", compound.getBoolean("connectServer"));
			nbt.setBoolean("isResuming", compound.getBoolean("resumeClient"));
			programData.setTag("program_0", nbt);
			nbt = new NBTTagCompound();
			nbt.setString("connectedClient", compound.getString("connectClient"));
			nbt.setBoolean("isOpen", compound.getBoolean("serverOpen"));
			programData.setTag("program_1", nbt);
		}
		if(compound.hasKey("ownerId"))
			ownerId = compound.getInt("ownerId");
		else this.owner = IdentifierHandler.load(compound, "owner");
		if(gui != null)
			gui.updateGui();
	}
	
	@Override
	public NBTTagCompound write(NBTTagCompound compound)
	{
		super.write(compound);
		NBTTagCompound programs = new NBTTagCompound();
		Iterator<Entry<Integer, Boolean>> it = this.installedPrograms.entrySet().iterator();
		//int place = 0;
		while (it.hasNext()) 
		{
			Map.Entry<Integer, Boolean> pairs = it.next();
			int program = pairs.getKey();
			programs.setInt("program" + program,program);
			//place++;
		}
		for(Entry<Integer, String> e : latestmessage.entrySet())
			compound.setString("text" + e.getKey(), e.getValue());
		compound.setTag("programs",programs);
		compound.setTag("programData", (NBTTagCompound) programData.copy());
		if (owner != null) 
			owner.saveToNBT(compound, "owner");
		return compound;
	}
	
	@Override
	public NBTTagCompound getUpdateTag()
	{
		NBTTagCompound tagCompound = new NBTTagCompound();
		this.write(tagCompound);
		tagCompound.removeTag("owner");
		tagCompound.removeTag("ownerMost");
		tagCompound.removeTag("ownerLeast");
		if(owner != null)
			tagCompound.setInt("ownerId", owner.getId());
		if(hasProgram(1))
		{
			SburbConnection c = SkaianetHandler.getServerConnection(ComputerData.createData(this));
			if(c != null)
				tagCompound.getCompound("programData").getCompound("program_1").setInt("connectedClient", c.getClientIdentifier().getId());
		}
		return tagCompound;
	}
	
	@Override
	public SPacketUpdateTileEntity getUpdatePacket()
	{
		return new SPacketUpdateTileEntity(this.pos, 2, getUpdateTag());
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) 
	{
		this.read(pkt.getNbtCompound());
	}

	public boolean hasProgram(int id) 
	{
		return installedPrograms.get(id) == null ? false:installedPrograms.get(id);
	}

	public NBTTagCompound getData(int id) 
	{
		if(!programData.hasKey("program_"+id))
			programData.setTag("program_" + id, new NBTTagCompound());
		return programData.getCompound("program_" + id);
	}

	public void closeAll() 
	{
		for(Entry<Integer, Boolean> entry : installedPrograms.entrySet())
			if(entry.getValue() && entry.getKey() != -1 && ComputerProgram.getProgram(entry.getKey()) != null)
				ComputerProgram.getProgram(entry.getKey()).onClosed(this);
	}

	public void connected(PlayerIdentifier player, boolean isClient)
	{
		if(isClient)
		{
			getData(0).setBoolean("isResuming", false);
			getData(0).setBoolean("connectedToServer", true);
		}
		else
		{
			this.getData(1).setBoolean("isOpen", false);
		}
	}
	
	public void markBlockForUpdate()
	{
		IBlockState state = world.getBlockState(pos);
		this.world.notifyBlockUpdate(pos, state, state, 3);
	}
	
}
