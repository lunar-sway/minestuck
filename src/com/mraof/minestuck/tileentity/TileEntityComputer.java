package com.mraof.minestuck.tileentity;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

import com.mraof.minestuck.client.gui.GuiComputer;
import com.mraof.minestuck.util.ComputerProgram;
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
	public NBTTagCompound programData = new NBTTagCompound();
	public int programSelected = -1;
	
	@SideOnly(Side.CLIENT)
	public ComputerProgram program;
	
	@Override
	public void readFromNBT(NBTTagCompound par1NBTTagCompound) {
		super.readFromNBT(par1NBTTagCompound);	
		if (par1NBTTagCompound.getCompoundTag("programs") != null) {
			NBTTagCompound programs = par1NBTTagCompound.getCompoundTag("programs");
			for (Object name : programs.func_150296_c()) {
				installedPrograms.put(programs.getInteger((String)name),true);
			}
		}
		
		latestmessage.clear();
		for(Entry<Integer,Boolean> e : installedPrograms.entrySet())
			if(e.getValue())
				latestmessage.put(e.getKey(), par1NBTTagCompound.getString("text"+e.getKey()));
		
		programData = par1NBTTagCompound.getCompoundTag("programData");
		
		if(!par1NBTTagCompound.hasKey("programData")) {
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setBoolean("connectedToServer", par1NBTTagCompound.getBoolean("connectServer"));
			nbt.setBoolean("isResuming", par1NBTTagCompound.getBoolean("resumeClient"));
			programData.setTag("program_0", nbt);
			nbt = new NBTTagCompound();
			nbt.setString("connectedClient", par1NBTTagCompound.getString("connectClient"));
			nbt.setBoolean("isOpen", par1NBTTagCompound.getBoolean("serverOpen"));
			programData.setTag("program_1", nbt);
		}
		this.owner = par1NBTTagCompound.getString("owner");
    	 if(gui != null)
    		 gui.updateGui();
    }
    
    @Override
    public void writeToNBT(NBTTagCompound par1NBTTagCompound) {
    	super.writeToNBT(par1NBTTagCompound);
    	NBTTagCompound programs = new NBTTagCompound();
		Iterator<Entry<Integer, Boolean>> it = this.installedPrograms.entrySet().iterator();
	   	//int place = 0;
	    while (it.hasNext()) {
			Map.Entry<Integer, Boolean> pairs = (Entry<Integer, Boolean>) it
					.next();
	        int program = (Integer) pairs.getKey();
	        programs.setInteger("program"+program,program);
	        //place++;
         }
	    for(Entry<Integer, String> e : latestmessage.entrySet())
	    	par1NBTTagCompound.setString("text"+e.getKey(), e.getValue());
		par1NBTTagCompound.setTag("programs",programs);
			par1NBTTagCompound.setTag("programData", (NBTTagCompound) programData.copy());
		if (!this.owner.isEmpty()) {
    		par1NBTTagCompound.setString("owner",this.owner);
    	}

    }
    
    @Override
    public Packet getDescriptionPacket() 
    {
    	NBTTagCompound tagCompound = new NBTTagCompound();
    	this.writeToNBT(tagCompound);
    	return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 2, tagCompound);
    }
    
    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) 
    {
		//Debug.print("Data packet gotten "+net.getClass());
    	this.readFromNBT(pkt.func_148857_g());
    }
	
	public boolean hasProgram(int id) {
		return installedPrograms.get(id)==null?false:installedPrograms.get(id);
	}
	
	public NBTTagCompound getData(int id) {
		if(!programData.hasKey("program_"+id))
			programData.setTag("program_"+id, new NBTTagCompound());
		return programData.getCompoundTag("program_"+id);
	}
	
	public void closeAll() {
		for(Entry<Integer, Boolean> entry : installedPrograms.entrySet())
			if(entry.getValue() && entry.getKey() != -1 && ComputerProgram.getProgram(entry.getKey()) != null)
				ComputerProgram.getProgram(entry.getKey()).onClosed(this);
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
	
}
