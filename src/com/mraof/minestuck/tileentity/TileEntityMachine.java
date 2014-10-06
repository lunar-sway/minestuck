package com.mraof.minestuck.tileentity;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.entity.item.EntityGrist;
import com.mraof.minestuck.tracker.MinestuckPlayerTracker;
import com.mraof.minestuck.util.AlchemyRecipeHandler;
import com.mraof.minestuck.util.CombinationRegistry;
import com.mraof.minestuck.util.GristAmount;
import com.mraof.minestuck.util.GristHelper;
import com.mraof.minestuck.util.GristRegistry;
import com.mraof.minestuck.util.GristSet;
import com.mraof.minestuck.util.GristType;
import com.mraof.minestuck.util.MinestuckAchievementHandler;
import com.mraof.minestuck.util.UsernameHandler;

import cpw.mods.fml.common.registry.GameRegistry;

public class TileEntityMachine extends TileEntity implements IInventory {

    public ItemStack[] inv;
    public int progress = 0;
    public int maxProgress = 100;
    public byte rotation = 0;
    //true if and, false if or
    public boolean mode = true;
    public EntityPlayer owner;
	public boolean ready = false;
	public boolean overrideStop = false;

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
    		case (4):
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
            return this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) == this &&
            player.getDistanceSq(this.xCoord + 0.5, this.yCoord + 0.5, this.zCoord + 0.5) < 64;
    }

//    @Override
//    public void openChest() {}
//
//    @Override
//    public void closeChest() {}
    
    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
            super.readFromNBT(tagCompound);
            
            this.rotation = tagCompound.getByte("rotation");
            this.progress = tagCompound.getInteger("progress");
            this.mode =  tagCompound.getBoolean("mode");
            this.overrideStop = tagCompound.getBoolean("overrideStop");
            
            NBTTagList tagList = tagCompound.getTagList("Inventory", 10);
            for (int i = 0; i < tagList.tagCount(); i++) {
                    NBTTagCompound tag = tagList.getCompoundTagAt(i);
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
            tagCompound.setBoolean("overrideStop", this.overrideStop);
            
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
    	return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 2, tagCompound);
    }
    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) 
    {
    	this.readFromNBT(pkt.func_148857_g());
    }

    @Override
    public String getInventoryName() {
            return "Alchemy Machine";
    }

	@Override
	public boolean hasCustomInventoryName() {
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
			this.ready = overrideStop;
			return;
		}
		
		this.progress++;
		
		if (this.progress == this.maxProgress) {
			this.progress = 0;
			this.ready = overrideStop;
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
		if (this.inv[1] != null && this.inv[2] != null) {	//&& or || alchemy
			if(inv[3] != null) {
				ItemStack outputItem = CombinationRegistry.getCombination(AlchemyRecipeHandler.getDecodedItem(this.inv[1],true), AlchemyRecipeHandler.getDecodedItem(this.inv[2],true),this.mode);
				if(inv[3].hasTagCompound() && inv[3].getTagCompound().getBoolean("punched"))
					outputItem = CombinationRegistry.getCombination(outputItem, AlchemyRecipeHandler.getDecodedItem(inv[3]), CombinationRegistry.MODE_OR);
				if(inv[3].hasTagCompound() && inv[3].getTagCompound().hasKey("displayID")) {
					outputItem.getTagCompound().setString("displayID", inv[3].getTagCompound().getString("displayID"));
					outputItem.getTagCompound().setInteger("displayMeta", inv[3].getTagCompound().getInteger("displayMeta"));
				}
				if(outputItem != null)
					return (this.inv[0] == null || inv[0].stackSize < 16 && outputItem.isItemEqual(AlchemyRecipeHandler.getDecodedItem(inv[0])));
			}
			return false;
		} else if (this.inv[1] != null || this.inv[2] != null) {	//Register item to card
			if(inv[3] != null) {
				ItemStack input = (inv[1] == null?inv[2]:inv[1]);
				ItemStack output = (input.getItem().equals(Minestuck.captchaCard)&&input.hasTagCompound()&&input.getTagCompound().getBoolean("punched")
						?input.copy():AlchemyRecipeHandler.createCard(input, true));
				if(inv[3].hasTagCompound() && inv[3].getTagCompound().getBoolean("punched")) {
					output = CombinationRegistry.getCombination(output, AlchemyRecipeHandler.getDecodedItem(inv[3]), CombinationRegistry.MODE_OR);
					if(inv[3].getTagCompound().hasKey("displayID")) {
						output.getTagCompound().setString("displayID", inv[3].getTagCompound().getString("displayID"));
						output.getTagCompound().setInteger("displayMeta", inv[3].getTagCompound().getInteger("displayMeta"));
					} else if(input.getItem().equals(Minestuck.captchaCard)&&input.hasTagCompound()&&input.getTagCompound().getBoolean("punched")) {
						output.getTagCompound().removeTag("displayID");
						output.getTagCompound().removeTag("displayMeta");
					}
				} else 
				return (output != null && (inv[0] == null || inv[0].stackSize < 16 && inv[0].isItemEqual(output)));
			}
		} else {
			return false;
		}
		case (2):
			return (this.inv[1] != null && this.inv[2] != null && !(inv[2].hasTagCompound() && inv[2].getTagCompound().hasKey("contentID")) && (this.inv[0] == null ||
			inv[0].stackSize < 16 && AlchemyRecipeHandler.getDecodedItem(inv[0]).isItemEqual(AlchemyRecipeHandler.getDecodedItem(inv[1]))));
		case (3):
			if (this.inv[1] != null && this.owner != null) {
				//Check owner's cache: Do they have everything they need?
				ItemStack newItem = AlchemyRecipeHandler.getDecodedItem(this.inv[1]);
				if (newItem == null) {return false;}
				if (inv[0] != null && (inv[0].getItem() != newItem.getItem() || inv[0].getItemDamage() != newItem.getItemDamage() || inv[0].getMaxStackSize() <= inv[0].stackSize)) {return false;}
				return GristHelper.canAfford(UsernameHandler.encode(owner.getCommandSenderName()), newItem);
			} else {
				return false;
			}
		case (4):
			return (this.inv[1] != null && this.owner != null && GristRegistry.getGristConversion(inv[1]) != null);
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
			if(inv[0] != null) {
				decrStackSize(3, 1);
				if(inv[1] != null && !(inv[1].getItem().equals(Minestuck.captchaCard) && inv[1].hasTagCompound() && inv[1].getTagCompound().getBoolean("punched")))
					//decrStackSize(1, 1);
				if(inv[2] != null && !(inv[2].getItem().equals(Minestuck.captchaCard) && inv[2].hasTagCompound() && inv[2].getTagCompound().getBoolean("punched")))
					//decrStackSize(2, 1);
				decrStackSize(0, -1);
				break;
			}
			ItemStack outputItem = CombinationRegistry.getCombination(AlchemyRecipeHandler.getDecodedItem(this.inv[1],true),AlchemyRecipeHandler.getDecodedItem(this.inv[2],true),this.mode);
			//boolean consumeItem = inv[1] == null || inv[2] == null;
			
			if (this.inv[1] == null) {
				outputItem = inv[2];
			} else if (this.inv[2]==null) {
				outputItem = inv[1];
			}
			
			
			if(inv[3].hasTagCompound() && inv[3].getTagCompound().getBoolean("punched")) {	//If you push the data onto a punched card, perform an OR alchemy
				outputItem = CombinationRegistry.getCombination(outputItem, AlchemyRecipeHandler.getDecodedItem(inv[3]), CombinationRegistry.MODE_OR);
			}
			
			//Create card
			outputItem = AlchemyRecipeHandler.createCard(outputItem, true);
			
			if(inv[3].hasTagCompound() && inv[3].getTagCompound().hasKey("displayID")) {
				outputItem.getTagCompound().setString("displayID", inv[3].getTagCompound().getString("displayID"));
				outputItem.getTagCompound().setInteger("displayMeta", inv[3].getTagCompound().getInteger("displayMeta"));
			} else if(inv[1] == null || inv[2] == null) {
				ItemStack input = inv[1] == null?inv[2]:inv[1];
				if(!(input.getItem().equals(Minestuck.captchaCard) && input.hasTagCompound() && input.getTagCompound().getBoolean("punched"))) {
					outputItem.getTagCompound().setString("displayID", Item.itemRegistry.getNameForObject(input.getItem()));
					outputItem.getTagCompound().setInteger("displayMeta", input.getItemDamage());
				}
			}
			
			setInventorySlotContents(0,outputItem);
			if (inv[1] == null || inv[2] == null) {
				decrStackSize(1, 1);
				decrStackSize(2, 1);
			}
			decrStackSize(3, 1);
			break;
		case (2):
			if(inv[0] != null) {
				decrStackSize(0, -1);
				decrStackSize(2, 1);
			}
			ItemStack outputDowel = new ItemStack(Minestuck.cruxiteDowel);
			
			NBTTagCompound cardtag = this.inv[1].getTagCompound();
			if(cardtag == null)
				break;
			
			if(cardtag.getString("contentID") == GameRegistry.findUniqueIdentifierFor(Minestuck.blockStorage).name &&
					cardtag.getInteger("contentMeta") == 1)
				outputDowel = new ItemStack(Minestuck.cruxiteDowel);
			if(cardtag != null && !(cardtag.getString("contentID") == GameRegistry.findUniqueIdentifierFor(Minestuck.blockStorage).name &&
					cardtag.getInteger("contentMeta") == 1)) {
				NBTTagCompound doweltag = new NBTTagCompound();
				doweltag.setString("contentID", cardtag.getString("contentID"));
				doweltag.setInteger("contentMeta", cardtag.getInteger("contentMeta"));
				outputDowel.setTagCompound(doweltag);
			}
			setInventorySlotContents(0,outputDowel);
			//decrStackSize(1, 1);
			decrStackSize(2, 1);
			break;
		case (3):
			ItemStack newItem = AlchemyRecipeHandler.getDecodedItem(this.inv[1]);
			if (inv[0] == null) {
				setInventorySlotContents(0,newItem);
			} else {
				decrStackSize(0, -1);
			}
			
			MinestuckAchievementHandler.onAlchemizedItem(newItem, owner);
			
			if(!worldObj.isRemote) {
				GristHelper.decrease(UsernameHandler.encode(owner.getCommandSenderName()), GristRegistry.getGristConversion(newItem));
				MinestuckPlayerTracker.updateGristCache(UsernameHandler.encode(owner.getCommandSenderName()));
			}
			break;
		case (4):
			if(!worldObj.isRemote) 
			{
				GristSet gristSet = GristRegistry.getGristConversion(inv[1]);
				
				Iterator<Entry<Integer, Integer>> iter = gristSet.getHashtable().entrySet().iterator();
				while(iter.hasNext()) 
				{
					Map.Entry<Integer, Integer> entry = (Entry<Integer, Integer>) iter.next();
					int grist = entry.getValue();
					while(true) 
					{
						if(grist == 0)
							break;
						GristAmount gristAmount = new GristAmount(GristType.values()[entry.getKey()],grist<=3?grist:(worldObj.rand.nextInt(grist)+1));
						EntityGrist entity = new EntityGrist(worldObj, this.xCoord + 0.5 /* this.width - this.width / 2*/, this.yCoord+1, this.zCoord + 0.5 /* this.width - this.width / 2*/, gristAmount);
						entity.motionX /= 2;
						entity.motionY /= 2;
						entity.motionZ /= 2;
						worldObj.spawnEntityInWorld(entity);
						//Create grist entity of gristAmount
						grist -= gristAmount.getAmount();
					}
				}
				
			}
			this.decrStackSize(1, 1);
			break;
		}
	}

	@Override
	public void closeInventory() {}

	@Override
	public void openInventory() {}
}
