package com.mraof.minestuck.tileentity;

import com.mraof.minestuck.alchemy.AlchemyRecipes;
import com.mraof.minestuck.alchemy.CombinationRegistry;
import com.mraof.minestuck.client.gui.GuiHandler;
import com.mraof.minestuck.inventory.ContainerMiniPunchDesignix;
import com.mraof.minestuck.item.MinestuckItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IInteractionObject;

public class TileEntityMiniPunchDesignix extends TileEntityMachineProcess implements IInteractionObject
{
	public TileEntityMiniPunchDesignix()
	{
		super(MinestuckTiles.MINI_PUNCH_DESIGNIX);
	}
	
	@Override
	public RunType getRunType()
	{
		return RunType.BUTTON;
	}
	
	@Override
	public int getSizeInventory()
	{
		return 3;
	}
	
	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack)
	{
		return index == 0 || index == 1 && stack.getItem() == MinestuckItems.CAPTCHA_CARD;
	}
	
	@Override
	public boolean contentsValid()
	{
		if (!this.inv.get(0).isEmpty() && !inv.get(1).isEmpty())
		{
			ItemStack output = AlchemyRecipes.getDecodedItemDesignix(inv.get(0));
			if (inv.get(1).hasTag() && inv.get(1).getTag().getBoolean("punched"))
			{
				output = CombinationRegistry.getCombination(output,
						AlchemyRecipes.getDecodedItem(inv.get(1)), CombinationRegistry.Mode.MODE_OR);
			}
			if (output.isEmpty())
				return false;
			output = AlchemyRecipes.createCard(output, true);
			return (inv.get(2).isEmpty() || inv.get(2).getCount() < 16 && ItemStack.areItemStackTagsEqual(inv.get(2), output));
		}
		else
		{
			return false;
		}
	}
	
	@Override
	public void processContents()
	{
		//Create a new card, using CombinationRegistry
		if(!inv.get(2).isEmpty())
		{
			decrStackSize(1, 1);
			if(!(inv.get(0).hasTag() && inv.get(0).getTag().contains("contentID")))
				decrStackSize(0, 1);
			this.inv.get(2).grow(1);
			return;
		}
		
		ItemStack outputItem = AlchemyRecipes.getDecodedItemDesignix(inv.get(0));
		
		if(inv.get(1).hasTag() && inv.get(1).getTag().getBoolean("punched"))
			outputItem = CombinationRegistry.getCombination(outputItem, AlchemyRecipes.getDecodedItem(inv.get(1)), CombinationRegistry.Mode.MODE_OR);
		
		//Create card
		outputItem = AlchemyRecipes.createCard(outputItem, true);
		
		setInventorySlotContents(2, outputItem);
		if(!(inv.get(0).hasTag() && inv.get(0).getTag().contains("contentID")))
			decrStackSize(0, 1);
		decrStackSize(1, 1);
	}
	
	@Override
	public void markDirty()
	{
		this.progress = 0;
		this.ready = false;
		super.markDirty();
	}
	
	@Override
	public ITextComponent getName()
	{
		return new TextComponentTranslation("container.mini_punch_designix");
	}
	
	@Override
	public int[] getSlotsForFace(EnumFacing side)
	{
		if(side == EnumFacing.UP)
			return new int[] {1};
		if(side == EnumFacing.DOWN)
			return new int[] {0, 2};
		else return new int[] {0};
	}
	
	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction)
	{
		if(index == 0)
			return !inv.get(2).isEmpty();
		else return true;
	}
	
	@Override
	public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn)
	{
		return new ContainerMiniPunchDesignix(playerInventory, this);
	}
	
	@Override
	public String getGuiID()
	{
		return GuiHandler.MINI_PUNCH_DESIGNIX_ID.toString();
	}
}