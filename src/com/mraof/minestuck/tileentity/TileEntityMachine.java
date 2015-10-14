package com.mraof.minestuck.tileentity;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.entity.item.EntityGrist;
import com.mraof.minestuck.item.MinestuckItems;
import com.mraof.minestuck.item.block.ItemMachine;
import com.mraof.minestuck.tracker.MinestuckPlayerTracker;
import com.mraof.minestuck.util.AlchemyRecipeHandler;
import com.mraof.minestuck.util.CombinationRegistry;
import com.mraof.minestuck.util.GristAmount;
import com.mraof.minestuck.util.GristHelper;
import com.mraof.minestuck.util.GristRegistry;
import com.mraof.minestuck.util.GristSet;
import com.mraof.minestuck.util.GristType;
import com.mraof.minestuck.util.MinestuckAchievementHandler;
import com.mraof.minestuck.util.MinestuckPlayerData;
import com.mraof.minestuck.util.UsernameHandler;

public class TileEntityMachine extends TileEntity implements IInventory, IUpdatePlayerListBox
{

    public ItemStack[] inv;
    public int progress = 0;
    public int maxProgress = 100;
    public EntityPlayer owner;
	public boolean ready = false;
	public boolean overrideStop = false;
	public GristType selectedGrist = GristType.Build;
	public byte rotation;
	public int color = -1;
	
    public TileEntityMachine(){
            this.inv = new ItemStack[4];
            
    }
    
    @Override
	public int getSizeInventory()
    {
		switch (getMachineType())
		{
    		case (0):
    			return 2;
    		case (1):
    			return 3;
    		case (2):
    			return 4;
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
	public boolean isUseableByPlayer(EntityPlayer player)
	{
		return this.worldObj.getTileEntity(pos) == this &&
				player.getDistanceSq(this.pos.getX() + 0.5, this.pos.getY() + 0.5, this.pos.getZ() + 0.5) < 64;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tagCompound)
	{
		super.readFromNBT(tagCompound);
		
		this.rotation = tagCompound.getByte("rotation");
		
		this.progress = tagCompound.getInteger("progress");
		this.overrideStop = tagCompound.getBoolean("overrideStop");
		
		for (int i = 0; i < inv.length; i++)
		{
			NBTTagCompound tag = tagCompound.getCompoundTag("slot"+i);
			this.inv[i] = ItemStack.loadItemStackFromNBT(tag);
		}
		if(tagCompound.hasKey("gristType"))
			this.selectedGrist = GristType.values()[tagCompound.getInteger("gristType")];
		
		this.color = tagCompound.getInteger("color");
	}
	
	@Override
	public void writeToNBT(NBTTagCompound tagCompound) {
		super.writeToNBT(tagCompound);
		
		tagCompound.setByte("rotation", this.rotation);
		
		tagCompound.setInteger("progress", this.progress);
		tagCompound.setBoolean("overrideStop", this.overrideStop);
		
		for (int i = 0; i < this.inv.length; i++)
		{
			ItemStack stack = this.inv[i];
			NBTTagCompound tag = new NBTTagCompound();
			if (stack != null)
				stack.writeToNBT(tag);
			tagCompound.setTag("slot"+i, tag);
		}
		tagCompound.setInteger("gristType", selectedGrist.ordinal());
		
		tagCompound.setInteger("color", color);
	}
	
    @Override
    public Packet getDescriptionPacket() 
    {
    	NBTTagCompound tagCompound = new NBTTagCompound();
    	this.writeToNBT(tagCompound);
    	return new S35PacketUpdateTileEntity(this.pos, 2, tagCompound);
    }
    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) 
    {
    	this.readFromNBT(pkt.getNbtCompound());
    }
	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack) {
		return true;
	}
	
	@Override
	public void update()
	{
		
		if(worldObj.getBlockState(pos).getBlock() != MinestuckBlocks.blockMachine || worldObj.isRemote)	//Processing is easier done on the server side only
			return;
		
		if (!contentsValid())
		{
			boolean b = progress == 0;
			this.progress = 0;
			this.ready = overrideStop;
			if(!b)
				worldObj.markBlockForUpdate(pos);
			return;
		}
		
		this.progress++;
		
		if (this.progress == this.maxProgress)
		{
			this.progress = 0;
			this.ready = overrideStop;
			processContents();
			worldObj.markBlockForUpdate(pos);
		}
	}
	
	public boolean contentsValid()
	{
		
		if (getMachineType() != 0 && !this.ready)
		{
			return false;
		}
		switch (getMachineType())
		{
		case (0):
			return (this.inv[1] != null && (this.inv[0] == null || this.inv[0].stackSize < this.inv[0].getMaxStackSize() && inv[0].getItemDamage() == this.color + 1));
		case (1):
		if (this.inv[1] != null && inv[2] != null)
		{
			ItemStack output = AlchemyRecipeHandler.getDecodedItemDesignix(inv[1]);
			if(inv[2].hasTagCompound() && inv[2].getTagCompound().getBoolean("punched"))
			{
				output = CombinationRegistry.getCombination(output,
						AlchemyRecipeHandler.getDecodedItem(inv[2]), CombinationRegistry.MODE_OR);
			}
			if(output == null)
				return false;
			if(output.getItem().isDamageable())
				output.setItemDamage(0);
			output = AlchemyRecipeHandler.createCard(output, true);
			return (inv[0] == null || inv[0].stackSize < 16 && ItemStack.areItemStackTagsEqual(inv[0], output));
		}
		else
		{
			return false;
		}
		case (2):
			if((inv[1] != null || inv[2] != null) && inv[3] != null && !(inv[3].hasTagCompound() && inv[3].getTagCompound().hasKey("contentID")))
			{
				if(inv[1] != null && inv[2] != null)
				{
					if(!inv[1].hasTagCompound() || !inv[1].getTagCompound().getBoolean("punched") || !inv[2].hasTagCompound() || !inv[2].getTagCompound().getBoolean("punched"))
						return inv[0] == null || !(inv[0].hasTagCompound() && inv[0].getTagCompound().hasKey("contentID") && inv[0].stackSize < inv[0].getMaxStackSize() && inv[0].getItemDamage() == inv[3].getItemDamage());
					else
					{
						ItemStack output = CombinationRegistry.getCombination(AlchemyRecipeHandler.getDecodedItem(inv[1]), AlchemyRecipeHandler.getDecodedItem(inv[2]), CombinationRegistry.MODE_AND);
						return output != null && (inv[0] == null || AlchemyRecipeHandler.getDecodedItem(inv[0]).isItemEqual(output) && inv[0].stackSize < inv[0].getMaxStackSize() && inv[0].getItemDamage() == inv[3].getItemDamage());
					}
				}
				else
				{
					ItemStack input = inv[1] != null ? inv[1] : inv[2];
					return (inv[0] == null || inv[0].stackSize < inv[0].getMaxStackSize() && (AlchemyRecipeHandler.getDecodedItem(inv[0]).isItemEqual(AlchemyRecipeHandler.getDecodedItem(input))
							|| !(inv[2].hasTagCompound() && inv[2].getTagCompound().getBoolean("punched")) && !(inv[0].hasTagCompound() && inv[0].getTagCompound().hasKey("contentID"))));
				}
			}
			else return false;
		case (3):
			if (this.inv[1] != null && this.owner != null) {
				//Check owner's cache: Do they have everything they need?
				ItemStack newItem = AlchemyRecipeHandler.getDecodedItem(this.inv[1]);
				if (newItem == null)
					if(!inv[1].hasTagCompound() || !inv[1].getTagCompound().hasKey("contentID"))
						newItem = new ItemStack(MinestuckBlocks.blockStorage, 1, 1);
					else return false;
				if (inv[0] != null && (inv[0].getItem() != newItem.getItem() || inv[0].getItemDamage() != newItem.getItemDamage() || inv[0].getMaxStackSize() <= inv[0].stackSize))
				{return false;}
				GristSet cost = GristRegistry.getGristConversion(newItem);
				if(newItem.getItem() == MinestuckItems.captchaCard)
					cost = new GristSet(selectedGrist, MinestuckConfig.cardCost);
				if(cost != null && newItem.isItemDamaged())
				{
					float multiplier = 1 - newItem.getItem().getDamage(newItem)/((float) newItem.getMaxDamage());
					for(int i = 0; i < cost.gristTypes.length; i++)
						cost.gristTypes[i] = (int) Math.ceil(cost.gristTypes[i]*multiplier);
				}
				return GristHelper.canAfford(MinestuckPlayerData.getGristSet(this.owner), cost);
			}
			else
			{
				return false;
			}
		case (4):
			if(MinestuckConfig.disableGristWidget) return false;
			return (this.inv[1] != null && inv[1].getItem() == MinestuckItems.captchaCard && GristRegistry.getGristConversion(AlchemyRecipeHandler.getDecodedItem(inv[1])) != null
			&& !inv[1].getTagCompound().getBoolean("punched") && AlchemyRecipeHandler.getDecodedItem(inv[1]).getItem() != MinestuckItems.captchaCard);
		}
		return false;
	}
	
	public void processContents()
	{
		switch (getMachineType())
		{
		case (0):
			// Process the Raw Cruxite
			
			if (this.inv[0] == null)
				setInventorySlotContents(0, new ItemStack(MinestuckItems.cruxiteDowel, 1, color + 1));
			else decrStackSize(0, -1);
			decrStackSize(1, 1);
			
			this.progress++;
			break;
		case (1):
			//Create a new card, using CombinationRegistry
			if(inv[0] != null)
			{
				decrStackSize(2, 1);
				if(!(inv[1].hasTagCompound() && inv[1].getTagCompound().hasKey("contentID")))
					decrStackSize(1, 1);
				decrStackSize(0, -1);
				break;
			}
			
			ItemStack outputItem = AlchemyRecipeHandler.getDecodedItemDesignix(inv[1]);
			
			if(inv[2].hasTagCompound() && inv[2].getTagCompound().getBoolean("punched"))
				outputItem = CombinationRegistry.getCombination(outputItem, AlchemyRecipeHandler.getDecodedItem(inv[2]), CombinationRegistry.MODE_OR);
			if(outputItem.getItem().isDamageable())
				outputItem.setItemDamage(0);
			
			//Create card
			outputItem = AlchemyRecipeHandler.createCard(outputItem, true);
			
			setInventorySlotContents(0,outputItem);
			if(!(inv[1].hasTagCompound() && inv[1].getTagCompound().hasKey("contentID")))
				decrStackSize(1, 1);
			decrStackSize(2, 1);
			break;
		case (2):
			if(inv[0] != null)
			{
				decrStackSize(0, -1);
				decrStackSize(3, 1);
				return;
			}
			
			ItemStack output;
			if(inv[1] != null && inv[2] != null)
				if(!inv[1].hasTagCompound() || !inv[1].getTagCompound().getBoolean("punched") || !inv[2].hasTagCompound() || !inv[2].getTagCompound().getBoolean("punched"))
					output = new ItemStack(MinestuckBlocks.blockStorage, 1, 1);
				else output = CombinationRegistry.getCombination(AlchemyRecipeHandler.getDecodedItem(inv[1]), AlchemyRecipeHandler.getDecodedItem(inv[2]), CombinationRegistry.MODE_AND);
			else
			{
				ItemStack input = inv[1] != null ? inv[1] : inv[2];
				if(!input.hasTagCompound() || !input.getTagCompound().getBoolean("punched"))
					output = new ItemStack(MinestuckBlocks.blockStorage, 1, 1);
				else output = AlchemyRecipeHandler.getDecodedItem(input);
			}
			
			ItemStack outputDowel = (output.getItem().equals(Item.getItemFromBlock(MinestuckBlocks.blockStorage)) && output.getItemDamage() == 1)
					? new ItemStack(MinestuckItems.cruxiteDowel) : AlchemyRecipeHandler.createEncodedItem(output, false);
			outputDowel.setItemDamage(inv[3].getItemDamage());
			
			setInventorySlotContents(0,outputDowel);
			decrStackSize(3, 1);
			break;
		case (3):
			ItemStack newItem = AlchemyRecipeHandler.getDecodedItem(this.inv[1]);
			
			if(newItem == null)
				newItem = new ItemStack(MinestuckBlocks.blockStorage, 1, 1);
			
			if (inv[0] == null)
			{
				setInventorySlotContents(0,newItem);
			}
			else
			{
				decrStackSize(0, -1);
			}
			
			MinestuckAchievementHandler.onAlchemizedItem(newItem, owner);
			
			GristSet cost = GristRegistry.getGristConversion(newItem);
			if(newItem.getItem() == MinestuckItems.captchaCard)
				cost = new GristSet(selectedGrist, MinestuckConfig.cardCost);
			if(newItem.isItemDamaged())
			{
				float multiplier = 1 - newItem.getItem().getDamage(newItem)/((float) newItem.getMaxDamage());
				for(int i = 0; i < cost.gristTypes.length; i++)
					cost.gristTypes[i] = (int) Math.ceil(cost.gristTypes[i]*multiplier);
			}
			GristHelper.decrease(UsernameHandler.encode(owner.getCommandSenderName()), cost);
			MinestuckPlayerTracker.updateGristCache(UsernameHandler.encode(owner.getCommandSenderName()));
			break;
		case (4):
			if(!worldObj.isRemote) 
			{
				ItemStack item = AlchemyRecipeHandler.getDecodedItem(inv[1]);
				GristSet gristSet = GristRegistry.getGristConversion(item);
				if(item.stackSize != 1)
					gristSet.scaleGrist(item.stackSize);
				
				gristSet.scaleGrist(0.9F);
				
				if(item.isItemDamaged())
				{
					float multiplier = 1 - item.getItem().getDamage(item)/((float) item.getMaxDamage());
					for(int i = 0; i < gristSet.gristTypes.length; i++)
						gristSet.gristTypes[i] = (int) (gristSet.gristTypes[i]*multiplier);
				}
				
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
						EntityGrist entity = new EntityGrist(worldObj, this.pos.getX() + 0.5 /* this.width - this.width / 2*/, this.pos.getY() + 1, this.pos.getZ() + 0.5 /* this.width - this.width / 2*/, gristAmount);
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
	public void markDirty()
	{
		if(getMachineType() == 1 || getMachineType() == 2)
		{
			this.progress = 0;
			this.ready = false;
		}
		super.markDirty();
	}

	@Override
	public String getCommandSenderName()
	{
		return "tile.blockMachine."+ItemMachine.subNames[getMachineType()]+".name";
	}
	
	@Override
	public boolean hasCustomName()
	{
		return false;
	}

	@Override
	public IChatComponent getDisplayName()
	{
		return new ChatComponentTranslation(getCommandSenderName());
	}

	@Override
	public void openInventory(EntityPlayer playerIn) {}

	@Override
	public void closeInventory(EntityPlayer playerIn) {}

	@Override
	public int getField(int id)
	{
		return 0;
	}

	@Override
	public void setField(int id, int value) {}
	
	@Override
	public int getFieldCount()
	{
		return 0;
	}

	@Override
	public void clear()
	{
		inv = new ItemStack[4];
	}
	
	public int getMachineType()
	{
		return getBlockMetadata();
	}
	
}
