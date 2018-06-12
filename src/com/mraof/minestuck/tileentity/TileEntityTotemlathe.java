package com.mraof.minestuck.tileentity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.block.BlockSburbMachine.MachineType;
import com.mraof.minestuck.block.BlockTotemlathe;
import com.mraof.minestuck.block.BlockTotemlathe.enumParts;
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

public class TileEntityTotemlathe extends TileEntityMachine
{
	private PlayerIdentifier owner;
	private GristType selectedGrist = GristType.Build;
	private int color = -1;
	private boolean destroyed=false;
	public com.mraof.minestuck.block.BlockTotemlathe.enumParts part;
	//constructor

	public boolean isDestroyed() {
		return destroyed;
	}
	
	public void destroy() {
		destroyed=true;
	}
	
	
	@Override
	public boolean isAutomatic()
	{
		return false;
	}
	
	@Override
	public boolean allowOverrideStop()
	{
		return false;
	}
	
	@Override
	public int getSizeInventory()
	{

		return 4;
		
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
			setOwner(IdentifierHandler.load(tagCompound, "owner"));
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tagCompound)
	{
		super.writeToNBT(tagCompound);
		return tagCompound;
	}
	
	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack)
	{
	
			return i == 0 || i == 1 ? itemstack.getItem() == MinestuckItems.captchaCard : i == 2 ? itemstack.getItem() == MinestuckItems.cruxiteDowel : false;
	}
	
	@Override
	public boolean contentsValid()
	{
			if((!inv.get(0).isEmpty() || !inv.get(1).isEmpty()) && !inv.get(2).isEmpty() && !(inv.get(2).hasTagCompound() && inv.get(2).getTagCompound().hasKey("contentID"))
					&& (inv.get(3).isEmpty() || inv.get(3).getCount() < inv.get(3).getMaxStackSize() && inv.get(3).getItemDamage() == inv.get(2).getItemDamage()))
			{
				if(!inv.get(0).isEmpty() && !inv.get(1).isEmpty())
				{
					if(!inv.get(0).hasTagCompound() || !inv.get(0).getTagCompound().getBoolean("punched") || !inv.get(1).hasTagCompound() || !inv.get(1).getTagCompound().getBoolean("punched"))
						return inv.get(3).isEmpty() || !(inv.get(3).hasTagCompound() && inv.get(3).getTagCompound().hasKey("contentID"));
					else
					{
						ItemStack output = CombinationRegistry.getCombination(AlchemyRecipeHandler.getDecodedItem(inv.get(0)), AlchemyRecipeHandler.getDecodedItem(inv.get(1)), CombinationRegistry.MODE_AND);
						return !output.isEmpty() && (inv.get(3).isEmpty() || AlchemyRecipeHandler.getDecodedItem(inv.get(3)).isItemEqual(output));
					}
				}
				else
				{
					ItemStack input = inv.get(0).isEmpty() ? inv.get(1) : inv.get(0);
					return (inv.get(3).isEmpty() || (AlchemyRecipeHandler.getDecodedItem(inv.get(3)).isItemEqual(AlchemyRecipeHandler.getDecodedItem(input))
							|| !(input.hasTagCompound() && input.getTagCompound().getBoolean("punched")) && !(inv.get(3).hasTagCompound() && inv.get(3).getTagCompound().hasKey("contentID"))));
				}
			}
			else return false;
		
	}
	
	public int comparatorValue()
	{
		
		return 0;
	}
	
	// We're going to want to trigger a block update every 20 ticks to have comparators pull data from the Alchemeter.
	@Override
	public void update()
	{
		if(world.isRemote)
			return;

		
		super.update();
	}

	@Override
	public void processContents()
	{

			if(!inv.get(3).isEmpty())
			{
				this.inv.get(3).grow(1);
				decrStackSize(2, 1);
				return;
			}
			
			ItemStack output;
			if(!inv.get(0).isEmpty() && !inv.get(1).isEmpty())
				if(!inv.get(0).hasTagCompound() || !inv.get(0).getTagCompound().getBoolean("punched") || !inv.get(1).hasTagCompound() || !inv.get(1).getTagCompound().getBoolean("punched"))
					output = new ItemStack(MinestuckBlocks.genericObject);
				else output = CombinationRegistry.getCombination(AlchemyRecipeHandler.getDecodedItem(inv.get(0)), AlchemyRecipeHandler.getDecodedItem(inv.get(1)), CombinationRegistry.MODE_AND);
			else
			{
				ItemStack input = inv.get(0).isEmpty() ? inv.get(1) : inv.get(0);
				if(!input.hasTagCompound() || !input.getTagCompound().getBoolean("punched"))
					output = new ItemStack(MinestuckBlocks.genericObject);
				else output = AlchemyRecipeHandler.getDecodedItem(input);
			}
			
			ItemStack outputDowel = output.getItem().equals(Item.getItemFromBlock(MinestuckBlocks.genericObject))
					? new ItemStack(MinestuckItems.cruxiteDowel) : AlchemyRecipeHandler.createEncodedItem(output, false);
			outputDowel.setItemDamage(inv.get(2).getItemDamage());
			
			setInventorySlotContents(3, outputDowel);
			decrStackSize(2, 1);

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
		return "tile.sburbMachine.Totemlathe.name";
	}
	
	public MachineType getMachineType()
	{
		return MachineType.values()[getBlockMetadata()%4];
	}

	public PlayerIdentifier getOwner() {
		return owner;
	}

	public void setOwner(PlayerIdentifier owner) {
		this.owner = owner;
	}

	public GristType getSelectedGrist() {
		return selectedGrist;
	}

	public void setSelectedGrist(GristType selectedGrist) {
		this.selectedGrist = selectedGrist;
	}
	
}
