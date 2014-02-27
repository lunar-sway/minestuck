package com.mraof.minestuck.tileentity;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;

import com.mraof.minestuck.client.gui.GuiComputer;
import com.mraof.minestuck.network.skaianet.SkaianetHandler;
import com.mraof.minestuck.util.ComputerProgram;
import com.mraof.minestuck.util.Debug;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TileEntityComputer extends TileEntity {
	
	/**
	 * 0 = client, 1 = server, -1 = secret easter egg
	 */
	public Hashtable<Integer, Boolean> installedPrograms = new Hashtable();
	public GuiComputer gui;
	public String owner = "";
	public Hashtable<Integer,String> latestmessage = new Hashtable();
	public NBTTagCompound programData;
	public int programSelected = -1;
	
	@SideOnly(Side.CLIENT)
	public ComputerProgram program;
	
	@Override
	public void readFromNBT(NBTTagCompound par1NBTTagCompound) {
		super.readFromNBT(par1NBTTagCompound);	
		if (par1NBTTagCompound.getCompoundTag("programs") != null) {
			for (Object tag : par1NBTTagCompound.getCompoundTag("programs").getTags()) {
				installedPrograms.put(((NBTTagInt)tag).data,true);
			}
		}
		
		latestmessage.clear();
		for(Entry<Integer,Boolean> e : installedPrograms.entrySet())
			if(e.getValue())
				latestmessage.put(e.getKey(), par1NBTTagCompound.getString("text"+e.getKey()));
		
		programData = par1NBTTagCompound.getCompoundTag("programData");
		
		if(!par1NBTTagCompound.hasKey("programData")) {
			programData = new NBTTagCompound();
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setBoolean("connectedToServer", par1NBTTagCompound.getBoolean("connectServer"));
			nbt.setBoolean("isResuming", par1NBTTagCompound.getBoolean("resumeClient"));
			programData.setCompoundTag("program_0", nbt);
			nbt = new NBTTagCompound();
			nbt.setString("connectedClient", par1NBTTagCompound.getString("connectClient"));
			nbt.setBoolean("isOpen", par1NBTTagCompound.getBoolean("serverOpen"));
			programData.setCompoundTag("program_1", nbt);
		}
		this.owner = par1NBTTagCompound.getString("owner");
    	 if(gui != null)
    		 gui.updateGui();
    }
    
    @Override
    public void writeToNBT(NBTTagCompound par1NBTTagCompound) {
    	super.writeToNBT(par1NBTTagCompound);
    	NBTTagCompound programs = new NBTTagCompound();
   	   	Iterator it = this.installedPrograms.entrySet().iterator();
	   	int place = 0;
	    while (it.hasNext()) {
	        Map.Entry pairs = (Map.Entry)it.next();
	        int program = (Integer) pairs.getKey();
	        programs.setInteger("program"+program,program);
	        place++;
         }
	    for(Entry<Integer, String> e : latestmessage.entrySet())
	    	par1NBTTagCompound.setString("text"+e.getKey(), e.getValue());
    	par1NBTTagCompound.setCompoundTag("programs",programs);
		if(programData != null)
			par1NBTTagCompound.setCompoundTag("programData", (NBTTagCompound) programData.copy());
    	if (!this.owner.isEmpty()) {
    		par1NBTTagCompound.setString("owner",this.owner);
    	}

    }
    
    @Override
    public Packet getDescriptionPacket() 
    {
    	NBTTagCompound tagCompound = new NBTTagCompound();
    	this.writeToNBT(tagCompound);
    	return new Packet132TileEntityData(this.xCoord, this.yCoord, this.zCoord, 2, tagCompound);
    }
    
    @Override
    public void onDataPacket(INetworkManager net, Packet132TileEntityData pkt) 
    {
		//Debug.print("Data packet gotten "+net.getClass());
    	this.readFromNBT(pkt.data);
    }
	
	public boolean hasProgram(int id) {
		return installedPrograms.get(id)==null?false:installedPrograms.get(id);
	}
	
	public NBTTagCompound getData(int id) {
		if(!programData.hasKey("program_"+id))
			programData.setCompoundTag("program_"+id, new NBTTagCompound());
		return programData.getCompoundTag("program_"+id);
	}
	
	public void connected(String player, boolean isClient){
		if(isClient){
			getData(0).setBoolean("isResuming", false);
			getData(0).setBoolean("connectedToServer", true);
		}
		else{
			this.getData(1).setBoolean("isOpen", false);
			this.getData(1).setString("connectedClient", player);
		}
	}

	public void closeConnections() {
		if(getData(0).getBoolean("connectedToServer") && SkaianetHandler.getClientConnection(owner) != null)
			SkaianetHandler.closeConnection(owner, SkaianetHandler.getClientConnection(owner).getServerName(), true);
		else if(getData(0).getBoolean("isResuming"))
			SkaianetHandler.closeConnection(owner, "", true);
		if(!getData(1).getString("connectedClient").isEmpty())
			SkaianetHandler.closeConnection(owner, getData(1).getString("connectedClient"), false);
		else if(getData(1).getBoolean("isOpen"))
			SkaianetHandler.closeConnection(owner, "", false);
	}
	
}
