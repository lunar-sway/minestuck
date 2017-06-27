package com.mraof.minestuck.tileentity;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.block.BlockSburbMachine.MachineType;
import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.item.MinestuckItems;
import com.mraof.minestuck.tracker.MinestuckPlayerTracker;
import com.mraof.minestuck.util.AlchemyRecipeHandler;
import com.mraof.minestuck.util.CombinationRegistry;
import com.mraof.minestuck.util.GristHelper;
import com.mraof.minestuck.util.GristRegistry;
import com.mraof.minestuck.util.GristSet;
import com.mraof.minestuck.util.GristType;
import com.mraof.minestuck.util.MinestuckAchievementHandler;
import com.mraof.minestuck.util.MinestuckPlayerData;
import com.mraof.minestuck.util.IdentifierHandler;
import com.mraof.minestuck.util.IdentifierHandler.PlayerIdentifier;

public class TileEntitySburbMachine extends TileEntityMachine
{
	public PlayerIdentifier owner;
	public GristType selectedGrist = GristType.Build;
	public int color = -1;
	private int ticks_since_update = 0;
	
	@Override
	public boolean isAutomatic()
	{
		return getMachineType() == MachineType.CRUXTRUDER;
	}
	
	@Override
	public boolean allowOverrideStop()
	{
		return getMachineType() == MachineType.ALCHEMITER;
	}
	
	@Override
	public int getSizeInventory()
	{
		switch(getMachineType())
		{
		case CRUXTRUDER: return 2;
		case PUNCH_DESIGNIX: return 3;
		case TOTEM_LATHE: return 4;
		case ALCHEMITER: return 2;
		}
		return 0;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tagCompound)
	{
		super.readFromNBT(tagCompound);
		
		if(tagCompound.hasKey("gristType"))
			this.selectedGrist = GristType.values()[tagCompound.getInteger("gristType")];
		
		if(tagCompound.hasKey("color"))
			this.color = tagCompound.getInteger("color");
		
		if(tagCompound.hasKey("owner") || tagCompound.hasKey("ownerMost"))
			owner = IdentifierHandler.load(tagCompound, "owner");
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tagCompound)
	{
		super.writeToNBT(tagCompound);
		
		if(getMachineType() == MachineType.ALCHEMITER)
			tagCompound.setInteger("gristType", selectedGrist.ordinal());
		
		if(getMachineType() == MachineType.CRUXTRUDER)
			tagCompound.setInteger("color", color);
		
		if(getMachineType() == MachineType.ALCHEMITER && owner != null)
			owner.saveToNBT(tagCompound, "owner");
		return tagCompound;
	}
	
	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack)
	{
		switch(getMachineType())
		{
		case CRUXTRUDER: return i == 0 ? itemstack.getItem() == MinestuckItems.rawCruxite : false;
		case PUNCH_DESIGNIX: return i == 1 ? itemstack.getItem() == MinestuckItems.captchaCard : i == 0;
		case TOTEM_LATHE: return i == 0 || i == 1 ? itemstack.getItem() == MinestuckItems.captchaCard : i == 2 ? itemstack.getItem() == MinestuckItems.cruxiteDowel : false;
		case ALCHEMITER: return i == 0 ? itemstack.getItem() == MinestuckItems.cruxiteDowel : false;
		}
		return true;
	}
	
	@Override
	public boolean contentsValid()
	{
		switch (getMachineType())
		{
		case CRUXTRUDER:
			return (!world.isBlockPowered(this.getPos()) && this.inv[0] != null && (this.inv[1] == null || this.inv[1].stackSize < this.inv[1].getMaxStackSize() && inv[1].getItemDamage() == this.color + 1));
		case PUNCH_DESIGNIX:
		if (this.inv[0] != null && inv[1] != null)
		{
			ItemStack output = AlchemyRecipeHandler.getDecodedItemDesignix(inv[0]);
			if(inv[1].hasTagCompound() && inv[1].getTagCompound().getBoolean("punched"))
			{
				output = CombinationRegistry.getCombination(output,
						AlchemyRecipeHandler.getDecodedItem(inv[1]), CombinationRegistry.MODE_OR);
			}
			if(output == null)
				return false;
			if(output.getItem().isDamageable())
				output.setItemDamage(0);
			output = AlchemyRecipeHandler.createCard(output, true);
			return (inv[2] == null || inv[2].stackSize < 16 && ItemStack.areItemStackTagsEqual(inv[2], output));
		}
		else
		{
			return false;
		}
		case TOTEM_LATHE:
			if((inv[0] != null || inv[1] != null) && inv[2] != null && !(inv[2].hasTagCompound() && inv[2].getTagCompound().hasKey("contentID"))
					&& (inv[3] == null || inv[3].stackSize < inv[3].getMaxStackSize() && inv[3].getItemDamage() == inv[2].getItemDamage()))
			{
				if(inv[0] != null && inv[1] != null)
				{
					if(!inv[0].hasTagCompound() || !inv[0].getTagCompound().getBoolean("punched") || !inv[1].hasTagCompound() || !inv[1].getTagCompound().getBoolean("punched"))
						return inv[3] == null || !(inv[3].hasTagCompound() && inv[3].getTagCompound().hasKey("contentID"));
					else
					{
						ItemStack output = CombinationRegistry.getCombination(AlchemyRecipeHandler.getDecodedItem(inv[0]), AlchemyRecipeHandler.getDecodedItem(inv[1]), CombinationRegistry.MODE_AND);
						return output != null && (inv[3] == null || AlchemyRecipeHandler.getDecodedItem(inv[3]).isItemEqual(output));
					}
				}
				else
				{
					ItemStack input = inv[0] != null ? inv[0] : inv[1];
					return (inv[3] == null || (AlchemyRecipeHandler.getDecodedItem(inv[3]).isItemEqual(AlchemyRecipeHandler.getDecodedItem(input))
							|| !(input.hasTagCompound() && input.getTagCompound().getBoolean("punched")) && !(inv[3].hasTagCompound() && inv[3].getTagCompound().hasKey("contentID"))));
				}
			}
			else return false;
		case ALCHEMITER:
			if (!world.isBlockPowered(this.getPos()) && this.inv[0] != null && this.owner != null)
			{
				//Check owner's cache: Do they have everything they need?
				ItemStack newItem = AlchemyRecipeHandler.getDecodedItem(this.inv[0]);
				if (newItem == null)
					if(!inv[0].hasTagCompound() || !inv[0].getTagCompound().hasKey("contentID"))
						newItem = new ItemStack(MinestuckBlocks.genericObject);
					else return false;
				if (inv[1] != null && (inv[1].getItem() != newItem.getItem() || inv[1].getItemDamage() != newItem.getItemDamage() || inv[1].getMaxStackSize() <= inv[1].stackSize))
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
		}
		return false;
	}

	public int comparatorValue()
 	{
		switch (getMachineType()) {
			case CRUXTRUDER:
				break;
			case PUNCH_DESIGNIX:
				break;
			case TOTEM_LATHE:
				break;
			case ALCHEMITER:
				if (this.inv[0] != null && owner != null) {
					ItemStack newItem = AlchemyRecipeHandler.getDecodedItem(this.inv[0]);
					if (newItem == null)
						if (!this.inv[0].hasTagCompound() || !this.inv[0].getTagCompound().hasKey("contentID"))
						newItem = new ItemStack(MinestuckBlocks.genericObject);
					else return 0;
					if (!(this.inv[1] == null) && (this.inv[1].getItem() != newItem.getItem() || this.inv[1].getItemDamage() != newItem.getItemDamage() || this.inv[1].getMaxStackSize() <= this.inv[1].stackSize)) {
						return 0;
					}
					GristSet cost = GristRegistry.getGristConversion(newItem);
					if (newItem.getItem() == MinestuckItems.captchaCard)
						cost = new GristSet(selectedGrist, MinestuckConfig.cardCost);
					if (cost != null && newItem.isItemDamaged()) {
						float multiplier = 1 - newItem.getItem().getDamage(newItem) / ((float) newItem.getMaxDamage());
						for (int i = 0; i < cost.gristTypes.length; i++)
							cost.gristTypes[i] = (int) Math.ceil(cost.gristTypes[i] * multiplier);
					}
					// We need to run the check 16 times. Don't want to hammer the game with too many of these, so the comparators are only told to update every 20 ticks.
					// Additionally, we need to check if the item in the slot is empty. Otherwise, it will attempt to check the cost for air, which cannot be alchemized anyway.
					if (cost != null && !(this.inv[0] == null)) {
						GristSet scale_cost;
						for (int lvl = 1; lvl <= 17; lvl++) {
							// We went through fifteen item cost checks and could still afford it. No sense in checking more than this.
							if (lvl == 17) {
								return 15;
							}
							// We need to make a copy to preserve the original grist amounts and avoid scaling values that have already been scaled. Keeps scaling linear as opposed to exponential.
							scale_cost = cost.copy().scaleGrist(lvl);
							if (!GristHelper.canAfford(MinestuckPlayerData.getGristSet(owner), scale_cost)) {
								return lvl - 1;
							}
						}
						return 0;
					}
				}
		}
		return 0;
	}

	// We're going to want to trigger a block update every 20 ticks to have comparators pull data from the Alchemeter.
	@Override
	public void update()
 	{
		if(world.isRemote)
			return;
		switch (getMachineType()) {
			case CRUXTRUDER:
				break;
			case PUNCH_DESIGNIX:
				break;
			case TOTEM_LATHE:
				break;
			case ALCHEMITER:
				if(this.ticks_since_update == 20)
				{
					world.updateComparatorOutputLevel(this.getPos(), this.blockType);
					this.ticks_since_update = 0;
				} else {
					this.ticks_since_update++;
				}
		}
		super.update();
	}
	
	@Override
	public void processContents()
	{
		switch (getMachineType())
		{
		case CRUXTRUDER:
			// Process the Raw Cruxite
			
			if (this.inv[1] == null)
				setInventorySlotContents(1, new ItemStack(MinestuckItems.cruxiteDowel, 1, color + 1));
			else decrStackSize(1, -1);
			decrStackSize(0, 1);
			
			this.progress++;
			break;
		case PUNCH_DESIGNIX:
			//Create a new card, using CombinationRegistry
			if(inv[2] != null)
			{
				decrStackSize(1, 1);
				if(!(inv[0].hasTagCompound() && inv[0].getTagCompound().hasKey("contentID")))
					decrStackSize(0, 1);
				decrStackSize(2, -1);
				break;
			}
			
			ItemStack outputItem = AlchemyRecipeHandler.getDecodedItemDesignix(inv[0]);
			
			if(inv[1].hasTagCompound() && inv[1].getTagCompound().getBoolean("punched"))
				outputItem = CombinationRegistry.getCombination(outputItem, AlchemyRecipeHandler.getDecodedItem(inv[1]), CombinationRegistry.MODE_OR);
			if(outputItem.getItem().isDamageable())
				outputItem.setItemDamage(0);
			
			//Create card
			outputItem = AlchemyRecipeHandler.createCard(outputItem, true);
			
			setInventorySlotContents(2, outputItem);
			if(!(inv[0].hasTagCompound() && inv[0].getTagCompound().hasKey("contentID")))
				decrStackSize(0, 1);
			decrStackSize(1, 1);
			break;
		case TOTEM_LATHE:
			if(inv[3] != null)
			{
				decrStackSize(3, -1);
				decrStackSize(2, 1);
				return;
			}
			
			ItemStack output;
			if(inv[0] != null && inv[1] != null)
				if(!inv[0].hasTagCompound() || !inv[0].getTagCompound().getBoolean("punched") || !inv[1].hasTagCompound() || !inv[1].getTagCompound().getBoolean("punched"))
					output = new ItemStack(MinestuckBlocks.genericObject);
				else output = CombinationRegistry.getCombination(AlchemyRecipeHandler.getDecodedItem(inv[0]), AlchemyRecipeHandler.getDecodedItem(inv[1]), CombinationRegistry.MODE_AND);
			else
			{
				ItemStack input = inv[0] != null ? inv[0] : inv[1];
				if(!input.hasTagCompound() || !input.getTagCompound().getBoolean("punched"))
					output = new ItemStack(MinestuckBlocks.genericObject);
				else output = AlchemyRecipeHandler.getDecodedItem(input);
			}
			
			ItemStack outputDowel = output.getItem().equals(Item.getItemFromBlock(MinestuckBlocks.genericObject))
					? new ItemStack(MinestuckItems.cruxiteDowel) : AlchemyRecipeHandler.createEncodedItem(output, false);
			outputDowel.setItemDamage(inv[2].getItemDamage());
			
			setInventorySlotContents(3, outputDowel);
			decrStackSize(2, 1);
			break;
		case ALCHEMITER:
			ItemStack newItem = AlchemyRecipeHandler.getDecodedItem(this.inv[0]);
			
			if(newItem == null)
				newItem = new ItemStack(MinestuckBlocks.genericObject);
			
			if (inv[1] == null)
			{
				setInventorySlotContents(1,newItem);
			}
			else
			{
				decrStackSize(1, -1);
			}
			
			EntityPlayerMP player = owner.getPlayer();
			if(player != null)
				MinestuckAchievementHandler.onAlchemizedItem(newItem, player);
			
			GristSet cost = GristRegistry.getGristConversion(newItem);
			if(newItem.getItem() == MinestuckItems.captchaCard)
				cost = new GristSet(selectedGrist, MinestuckConfig.cardCost);
			if(newItem.isItemDamaged())
			{
				float multiplier = 1 - newItem.getItem().getDamage(newItem)/((float) newItem.getMaxDamage());
				for(int i = 0; i < cost.gristTypes.length; i++)
					cost.gristTypes[i] = (int) Math.ceil(cost.gristTypes[i]*multiplier);
			}
			GristHelper.decrease(owner, cost);
			MinestuckPlayerTracker.updateGristCache(owner);
			break;
		}
	}
	
	@Override
	public void markDirty()
	{
		if(getMachineType() == MachineType.PUNCH_DESIGNIX || getMachineType() == MachineType.TOTEM_LATHE)
		{
			this.progress = 0;
			this.ready = false;
		}
		super.markDirty();
	}

	@Override
	public String getName()
	{
		return "tile.sburbMachine."+getMachineType().getUnlocalizedName()+".name";
	}
	
	public MachineType getMachineType()
	{
		return MachineType.values()[getBlockMetadata()%4];
	}
	
}
