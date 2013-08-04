package com.mraof.minestuck.tileentity;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.entity.item.EntityGrist;
import com.mraof.minestuck.util.CombinationMode;
import com.mraof.minestuck.util.CombinationRegistry;
import com.mraof.minestuck.util.GristRegistry;
import com.mraof.minestuck.util.GristSet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;

public class TileEntityMachine extends TileEntity implements IInventory {

    public ItemStack[] inv;
    public int progress = 0;
    public int maxProgress = 100;
    public CombinationMode mode = CombinationMode.AND;
    public EntityPlayer owner;
	public boolean alcReady = false;

    public TileEntityMachine(){
            inv = new ItemStack[4];
            
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
            return inv.length;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
            return inv[slot];
    }
    
    @Override
    public void setInventorySlotContents(int slot, ItemStack stack) {
            inv[slot] = stack;
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
            return worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) == this &&
            player.getDistanceSq(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5) < 64;
    }

    @Override
    public void openChest() {}

    @Override
    public void closeChest() {}
    
    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
            super.readFromNBT(tagCompound);
            
            progress = tagCompound.getInteger("progress");
            mode =  tagCompound.getBoolean("mode") ? CombinationMode.AND : CombinationMode.OR;
            
            //String ownerName = tagCompound.getString("owner");
            
            NBTTagList tagList = tagCompound.getTagList("Inventory");
            for (int i = 0; i < tagList.tagCount(); i++) {
                    NBTTagCompound tag = (NBTTagCompound) tagList.tagAt(i);
                    byte slot = tag.getByte("Slot");
                    if (slot >= 0 && slot < inv.length) {
                            inv[slot] = ItemStack.loadItemStackFromNBT(tag);
                    }
            }
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
            super.writeToNBT(tagCompound);
                            
            tagCompound.setInteger("progress", progress);
            tagCompound.setBoolean("mode", mode == CombinationMode.AND);
            //tagCompound.setString("owner", owner.username);
            
            NBTTagList itemList = new NBTTagList();
            for (int i = 0; i < inv.length; i++) {
                    ItemStack stack = inv[i];
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
		return worldObj.getBlockMetadata(xCoord,yCoord,zCoord);
	}
	
	@Override
	public void updateEntity() {
		
		if (!contentsValid()) {
			progress = 0;
			alcReady = false;
			return;
		}
		
		progress++;
		
		if (progress == maxProgress) {
			progress = 0;
			processContents();
		}
	}
	
	public boolean contentsValid() {
		switch (getMetadata()) {
		case (0):
			return (inv[1] != null && (inv[0] == null || inv[0].stackSize < 64));
		case (1):
			return (inv[1] != null && inv[2] != null && inv[3] != null && inv[0] == null && CombinationRegistry.getCombination(inv[1], inv[2],mode) != null);
		case (2):
			return (inv[1] != null && inv[2] != null && inv[0] == null);
		case (3):
		if (alcReady && inv[1] != null && inv[0] == null && owner != null) {
			//Check owner's cache: Do they have everything they need?
		  	NBTTagCompound nbttagcompound = inv[1].getTagCompound();
	    	GristSet set = GristRegistry.getGristConversion(new ItemStack(nbttagcompound.getInteger("contentID"),1,nbttagcompound.getInteger("contentMeta")));
	    	if (set == null) {return false;}
		    	Hashtable reqs = set.getTable();
		    	//System.out.println("reqs: " + reqs.size());
		    	if (reqs != null) {
		    	   	Iterator it = reqs.entrySet().iterator();
		            while (it.hasNext()) {
		                Map.Entry pairs = (Map.Entry)it.next();
		                int type = (Integer) pairs.getKey();
		                int need = (Integer) pairs.getValue();
		                int have =  owner.getEntityData().getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG).getCompoundTag("Grist").getInteger(EntityGrist.gristTypes[type]);
		                
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
			
			if (inv[0] == null) {
				setInventorySlotContents(0, new ItemStack(Minestuck.cruxiteDowel,1));
			} else {
				decrStackSize(0, -1);
			}
			decrStackSize(1, 1);
			
			progress++;
			break;
		case (1):
			//Create a new card, using CombinationRegistry
			ItemStack outputItem = CombinationRegistry.getCombination(inv[1], inv[2],mode);
			ItemStack outputCard = new ItemStack(Minestuck.punchedCard);

			NBTTagCompound nbttagcompound = new NBTTagCompound();
			outputCard.setTagCompound(nbttagcompound);
	        nbttagcompound.setInteger("contentID", outputItem.itemID);
	        nbttagcompound.setInteger("contentMeta", outputItem.getItemDamage());
	        
			setInventorySlotContents(0,outputCard);
			decrStackSize(1, 1);
			decrStackSize(2, 1);
			decrStackSize(3, 1);
			break;
		case (2):
			ItemStack outputDowel = new ItemStack(Minestuck.cruxiteDowelCarved);
			
			NBTTagCompound cardtag = inv[1].getTagCompound();
			if (cardtag == null) {
				break;
			}
			NBTTagCompound doweltag = new NBTTagCompound();
			doweltag.setInteger("contentID", cardtag.getInteger("contentID"));
			doweltag.setInteger("contentMeta", cardtag.getInteger("contentMeta"));
			outputDowel.setTagCompound(doweltag);
			setInventorySlotContents(0,outputDowel);
			decrStackSize(1, 1);
			decrStackSize(2, 1);
			break;
		case (3):
			nbttagcompound = inv[1].getTagCompound();
			if (nbttagcompound == null) { break;}
			setInventorySlotContents(0,new ItemStack(nbttagcompound.getInteger("contentID"),1,nbttagcompound.getInteger("contentMeta")));
			decrStackSize(1, 1);
	    	GristSet set = GristRegistry.getGristConversion(new ItemStack(nbttagcompound.getInteger("contentID"),1,nbttagcompound.getInteger("contentMeta")));
		    Hashtable reqs = set.getTable();
	    	if (reqs != null) {
	    	   	Iterator it = reqs.entrySet().iterator();
	            while (it.hasNext()) {
	                Map.Entry pairs = (Map.Entry)it.next();
	                owner.getEntityData().getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG).getCompoundTag("Grist").setInteger(EntityGrist.gristTypes[(Integer) pairs.getKey()],owner.getEntityData().getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG).getCompoundTag("Grist").getInteger(EntityGrist.gristTypes[(Integer)pairs.getKey()]) - (Integer)pairs.getValue());
	            }
	        alcReady = false;
			break;
	    	}
		}
	}
}
