package com.mraof.minestuck.tileentity;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.util.AlchemyRecipeHandler;
import com.mraof.minestuck.util.CombinationRegistry;
import com.mraof.minestuck.util.GristRegistry;
import com.mraof.minestuck.util.GristSet;
import com.mraof.minestuck.util.GristType;

public class TileEntityMachine extends TileEntity implements IInventory {

    public ItemStack[] inv;
    public int progress = 0;
    public int maxProgress = 100;
    public byte rotation = 0;
    //true if and, false if or
    public boolean mode = true;
    public EntityPlayer owner;
	public boolean ready = false;

    public TileEntityMachine(){
            this.inv = new ItemStack[4];
            
    }
    
    @Override
    public int getSizeInventory() {
    		switch (getMetadata()) {
    		case (0):
    			return 2;
    		case (1):
    			return 4;
    		case (2):
    			return 3;
    		case (3):
    			return 2;
    		}
            return this.inv.length;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
            return this.inv[slot];
    }
    
    @Override
    public void setInventorySlotContents(int slot, ItemStack stack) {
            this.inv[slot] = stack;
            if (stack != null && stack.stackSize > getInventoryStackLimit()) {
                    stack.stackSize = getInventoryStackLimit();
            }               
    }

    @Override
    public ItemStack decrStackSize(int slot, int amt) {
            ItemStack stack = getStackInSlot(slot);
            if (stack != null) {
                    if (stack.stackSize <= amt) {
                            setInventorySlotContents(slot, null);
                    } else {
                            stack = stack.splitStack(amt);
                            if (stack.stackSize == 0) {
                                    setInventorySlotContents(slot, null);
                            }
                    }
            }
            return stack;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slot) {
            ItemStack stack = getStackInSlot(slot);
            if (stack != null) {
                    setInventorySlotContents(slot, null);
            }
            return stack;
    }
    
    @Override
    public int getInventoryStackLimit() {
            return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
            return this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord) == this &&
            player.getDistanceSq(this.xCoord + 0.5, this.yCoord + 0.5, this.zCoord + 0.5) < 64;
    }

    @Override
    public void openChest() {}

    @Override
    public void closeChest() {}
    
    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
            super.readFromNBT(tagCompound);
            
            this.rotation = tagCompound.getByte("rotation");
            this.progress = tagCompound.getInteger("progress");
            this.mode =  tagCompound.getBoolean("mode");
            
            //String ownerName = tagCompound.getString("owner");
            
            NBTTagList tagList = tagCompound.getTagList("Inventory");
            for (int i = 0; i < tagList.tagCount(); i++) {
                    NBTTagCompound tag = (NBTTagCompound) tagList.tagAt(i);
                    byte slot = tag.getByte("Slot");
                    if (slot >= 0 && slot < this.inv.length) {
                            this.inv[slot] = ItemStack.loadItemStackFromNBT(tag);
                    }
            }
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
            super.writeToNBT(tagCompound);
                            
            tagCompound.setByte("rotation", this.rotation);
            tagCompound.setInteger("progress", this.progress);
            tagCompound.setBoolean("mode", this.mode);
            //tagCompound.setString("owner", owner.username);
            
            NBTTagList itemList = new NBTTagList();
            for (int i = 0; i < this.inv.length; i++) {
                    ItemStack stack = this.inv[i];
                    if (stack != null) {
                            NBTTagCompound tag = new NBTTagCompound();
                            tag.setByte("Slot", (byte) i);
                            stack.writeToNBT(tag);
                            itemList.appendTag(tag);
                    }
            }
            tagCompound.setTag("Inventory", itemList);
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
    	this.readFromNBT(pkt.customParam1);
    }

    @Override
    public String getInvName() {
            return "Alchemy Machine";
    }

	@Override
	public boolean isInvNameLocalized() {
		return false;
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack) {
		return true;
	}

	public int getMetadata() {
		return this.worldObj.getBlockMetadata(this.xCoord,this.yCoord,this.zCoord);
	}
	
	@Override
	public void updateEntity() {
		
		if (!contentsValid()) {
			this.progress = 0;
			this.ready = false;
			return;
		}
		
		this.progress++;
		
		if (this.progress == this.maxProgress) {
			this.progress = 0;
			this.ready = false;
			processContents();
		}
	}
	
	public boolean contentsValid() {
		
		if (getMetadata() != 0 && !this.ready) {
			return false;
		}
		switch (getMetadata()) {
		case (0):
			return (this.inv[1] != null && (this.inv[0] == null || this.inv[0].stackSize < 64));
		case (1):
		 if (this.inv[1] != null && this.inv[2] != null) {
			return (this.inv[3] != null && this.inv[0] == null && CombinationRegistry.getCombination(AlchemyRecipeHandler.getDecodedItem(this.inv[1]), AlchemyRecipeHandler.getDecodedItem(this.inv[2]),this.mode) != null);
		 } else if (this.inv[1] != null || this.inv[2] != null) {
			return (this.inv[3] != null && this.inv[0] == null);
		} else {
			return false;
		}
		case (2):
			return (this.inv[1] != null && this.inv[2] != null && this.inv[0] == null);
		case (3):
			if (this.inv[1] != null && this.inv[0] == null && this.owner != null) {
				//Check owner's cache: Do they have everything they need?
				ItemStack newItem = AlchemyRecipeHandler.getDecodedItem(this.inv[1]);
				if (newItem == null) {return false;}
		    	GristSet set = GristRegistry.getGristConversion(newItem);
		    	if (set == null) {return false;}
			    	Hashtable reqs = set.getHashtable();
			    	//Debug.print("reqs: " + reqs.size());
			    	if (reqs != null) {
			    	   	Iterator it = reqs.entrySet().iterator();
			            while (it.hasNext()) {
			                Map.Entry pairs = (Map.Entry)it.next();
			                int type = (Integer) pairs.getKey();
			                int need = (Integer) pairs.getValue();
			                int have =  this.owner.getEntityData().getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG).getCompoundTag("Grist").getInteger(GristType.values()[type].getName());
			                
			                if (need > have) {return false;}
			        }
		    	} else {return false;}
		    	return true;
			} else {
				return false;
			}
		}
		return false;
	}
	
	public void processContents() {
		switch (getMetadata()) {
		case (0):
			// Process the Raw Cruxite
			
			if (this.inv[0] == null) {
				setInventorySlotContents(0, new ItemStack(Minestuck.cruxiteDowel,1));
			} else {
				decrStackSize(0, -1);
			}
			decrStackSize(1, 1);
			
			this.progress++;
			break;
		case (1):
			//Create a new card, using CombinationRegistry
			ItemStack outputItem = CombinationRegistry.getCombination(AlchemyRecipeHandler.getDecodedItem(this.inv[1]),AlchemyRecipeHandler.getDecodedItem(this.inv[2]),this.mode);
			ItemStack outputCard = new ItemStack(Minestuck.punchedCard);

			NBTTagCompound nbttagcompound = new NBTTagCompound();
			outputCard.setTagCompound(nbttagcompound);
			if (this.inv[1] == null) {
		        nbttagcompound.setInteger("contentID", this.inv[2].itemID);
		        nbttagcompound.setInteger("contentMeta", this.inv[2].getItemDamage());
			} else if (this.inv[2]==null) {
		        nbttagcompound.setInteger("contentID", this.inv[1].itemID);
		        nbttagcompound.setInteger("contentMeta", this.inv[1].getItemDamage());
			} else {
		        nbttagcompound.setInteger("contentID", outputItem.itemID);
		        nbttagcompound.setInteger("contentMeta", outputItem.getItemDamage());
			}

	        
			setInventorySlotContents(0,outputCard);
			//decrStackSize(1, 1);
			//decrStackSize(2, 1);
			decrStackSize(3, 1);
			break;
		case (2):
			ItemStack outputDowel = new ItemStack(Minestuck.cruxiteDowelCarved);
			
			NBTTagCompound cardtag = this.inv[1].getTagCompound();
			if (cardtag == null) {
				break;
			}
			NBTTagCompound doweltag = new NBTTagCompound();
			doweltag.setInteger("contentID", cardtag.getInteger("contentID"));
			doweltag.setInteger("contentMeta", cardtag.getInteger("contentMeta"));
			outputDowel.setTagCompound(doweltag);
			setInventorySlotContents(0,outputDowel);
			//decrStackSize(1, 1);
			decrStackSize(2, 1);
			break;
		case (3):
			ItemStack newItem = AlchemyRecipeHandler.getDecodedItem(this.inv[1]);
			setInventorySlotContents(0,newItem);
			//decrStackSize(1, 1);
	    	GristSet set = GristRegistry.getGristConversion(newItem);
		    Hashtable reqs = set.getHashtable();
	    	if (reqs != null) {
	    	   	Iterator it = reqs.entrySet().iterator();
	            while (it.hasNext()) {
	                Map.Entry pairs = (Map.Entry)it.next();
	                this.owner.getEntityData().getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG).getCompoundTag("Grist").setInteger(GristType.values()[(Integer) pairs.getKey()].getName(),this.owner.getEntityData().getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG).getCompoundTag("Grist").getInteger(GristType.values()[(Integer)pairs.getKey()].getName()) - (Integer)pairs.getValue());
	            }
			break;
	    	}
		}
	}
}
