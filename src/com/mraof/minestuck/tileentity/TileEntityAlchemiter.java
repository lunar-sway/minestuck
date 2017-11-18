package com.mraof.minestuck.tileentity;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.item.MinestuckItems;
import com.mraof.minestuck.tracker.MinestuckPlayerTracker;
import com.mraof.minestuck.util.AlchemyRecipeHandler;
import com.mraof.minestuck.util.GristHelper;
import com.mraof.minestuck.util.GristRegistry;
import com.mraof.minestuck.util.GristSet;
import com.mraof.minestuck.util.GristType;
import com.mraof.minestuck.util.IdentifierHandler;
import com.mraof.minestuck.util.IdentifierHandler.PlayerIdentifier;
import com.mraof.minestuck.util.MinestuckAchievementHandler;
import com.mraof.minestuck.util.MinestuckPlayerData;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class TileEntityAlchemiter extends TileEntityMachine
{
	//still private because programming teacher and data protection
	private PlayerIdentifier owner;
	private GristType selectedGrist = GristType.Build;
	private int color = -1;
	private int ticks_since_update = 0;
	private boolean destroyed=false;
	public PlayerIdentifier getOwner(){
		return owner;
	}
	public void setOwner(PlayerIdentifier Owner){
		owner= Owner;
	}
	
	
	
	//checks if the tile enity should work
	public boolean isDestroyed(){
		return destroyed;
	}
	//tells the tile entity to stop working
	public void destroy(){
		destroyed = true;		
	}

	
	@Override
	public boolean isAutomatic()
	{
		return false;
	}
	
	@Override
	public boolean allowOverrideStop()
	{
		return true;
	}
	
	@Override
	public int getSizeInventory()
	{
return 2;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tagCompound)
	{
		super.readFromNBT(tagCompound);
		
		if(tagCompound.hasKey("gristType"))
			this.setSelectedGrist(GristType.values()[tagCompound.getInteger("gristType")]);
		
		if(tagCompound.hasKey("color"))
			this.color = tagCompound.getInteger("color");
		
		if(tagCompound.hasKey("owner") || tagCompound.hasKey("ownerMost"))
			owner = IdentifierHandler.load(tagCompound, "owner");
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tagCompound)
	{
		super.writeToNBT(tagCompound);
		
		if(true)
			tagCompound.setInteger("gristType", getSelectedGrist().ordinal());
		
		if(true && owner != null)
			owner.saveToNBT(tagCompound, "owner");
		return tagCompound;
	}
	
	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack)
	{
	 return i == 0 ? itemstack.getItem() == MinestuckItems.cruxiteDowel : false;

	}
	
	@Override
	public boolean contentsValid()
	{

			if(!world.isBlockPowered(this.getPos()) && !this.inv.get(0).isEmpty() && this.owner != null)
			{
				//Check owner's cache: Do they have everything they need?
				ItemStack newItem = AlchemyRecipeHandler.getDecodedItem(this.inv.get(0));
				if (newItem.isEmpty())
					if(!inv.get(0).hasTagCompound() || !inv.get(0).getTagCompound().hasKey("contentID"))
						newItem = new ItemStack(MinestuckBlocks.genericObject);
					else return false;
				if (!inv.get(1).isEmpty() && (inv.get(1).getItem() != newItem.getItem() || inv.get(1).getItemDamage() != newItem.getItemDamage() || inv.get(1).getMaxStackSize() <= inv.get(1).getCount()))
				{return false;}
				GristSet cost = GristRegistry.getGristConversion(newItem);
				if(newItem.getItem() == MinestuckItems.captchaCard)
					cost = new GristSet(getSelectedGrist(), MinestuckConfig.cardCost);
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
	
	public int comparatorValue()
	{
				if (getStackInSlot(0) != null && owner != null) {
					ItemStack newItem = AlchemyRecipeHandler.getDecodedItem(getStackInSlot(0));
					if (newItem.isEmpty())
						if (!getStackInSlot(0).hasTagCompound() || !getStackInSlot(0).getTagCompound().hasKey("contentID"))
							newItem = new ItemStack(MinestuckBlocks.genericObject);
						else return 0;
					if (!getStackInSlot(1).isEmpty() && (getStackInSlot(1).getItem() != newItem.getItem() || getStackInSlot(1).getItemDamage() != newItem.getItemDamage() || getStackInSlot(1).getMaxStackSize() <= getStackInSlot(1).getCount())) {
						return 0;
					}
					GristSet cost = GristRegistry.getGristConversion(newItem);
					if (newItem.getItem() == MinestuckItems.captchaCard)
						cost = new GristSet(getSelectedGrist(), MinestuckConfig.cardCost);
					if (cost != null && newItem.isItemDamaged()) {
						float multiplier = 1 - newItem.getItem().getDamage(newItem) / ((float) newItem.getMaxDamage());
						for (int i = 0; i < cost.gristTypes.length; i++)
							cost.gristTypes[i] = (int) Math.ceil(cost.gristTypes[i] * multiplier);
					}
					// We need to run the check 16 times. Don't want to hammer the game with too many of these, so the comparators are only told to update every 20 ticks.
					// Additionally, we need to check if the item in the slot is empty. Otherwise, it will attempt to check the cost for air, which cannot be alchemized anyway.
					if (cost != null && !getStackInSlot(0).isEmpty()) {
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
		
		return 0;
	}
	
	// We're going to want to trigger a block update every 20 ticks to have comparators pull data from the Alchemeter.
	@Override
	public void update()
	{
		if(world.isRemote)
			return;

				if(this.ticks_since_update == 20)
				{
					world.updateComparatorOutputLevel(this.getPos(), this.blockType);
					this.ticks_since_update = 0;
				} else {
					this.ticks_since_update++;
				}
	
		super.update();
	}

	@Override
	public void processContents()
	{

			ItemStack newItem = AlchemyRecipeHandler.getDecodedItem(this.inv.get(0));
			
			if(newItem.isEmpty())
				newItem = new ItemStack(MinestuckBlocks.genericObject);
			
			if (inv.get(1).isEmpty())
			{
				setInventorySlotContents(1,newItem);
			}
			else
			{
				this.inv.get(1).grow(1);
			}
			
			EntityPlayerMP player = owner.getPlayer();
			if(player != null)
				MinestuckAchievementHandler.onAlchemizedItem(newItem, player);
			
			GristSet cost = GristRegistry.getGristConversion(newItem);
			if(newItem.getItem() == MinestuckItems.captchaCard)
				cost = new GristSet(getSelectedGrist(), MinestuckConfig.cardCost);
			if(newItem.isItemDamaged())
			{
				float multiplier = 1 - newItem.getItem().getDamage(newItem)/((float) newItem.getMaxDamage());
				for(int i = 0; i < cost.gristTypes.length; i++)
					cost.gristTypes[i] = (int) Math.ceil(cost.gristTypes[i]*multiplier);
			}
			GristHelper.decrease(owner, cost);
			MinestuckPlayerTracker.updateGristCache(owner);
		}
	
	
	@Override
	public void markDirty()
	{
		super.markDirty();
	}

	@Override
	public String getName()
	{
		return "tile.sburbMachine.alchemiter.name";
	}
	public GristType getSelectedGrist() {
		return selectedGrist;
	}
	public void setSelectedGrist(GristType selectedGrist) {
		this.selectedGrist = selectedGrist;
	}
	

	
}
