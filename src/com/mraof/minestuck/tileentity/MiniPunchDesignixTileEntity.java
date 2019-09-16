package com.mraof.minestuck.tileentity;

import com.mraof.minestuck.alchemy.AlchemyRecipes;
import com.mraof.minestuck.alchemy.CombinationRegistry;
import com.mraof.minestuck.inventory.MiniPunchDesignixContainer;
import com.mraof.minestuck.item.MSItems;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;

public class MiniPunchDesignixTileEntity extends MachineProcessTileEntity implements INamedContainerProvider
{
	public static final RunType TYPE = RunType.BUTTON;
	
	public MiniPunchDesignixTileEntity()
	{
		super(MSTileEntityTypes.MINI_PUNCH_DESIGNIX);
	}
	
	@Override
	public RunType getRunType()
	{
		return TYPE;
	}
	
	@Override
	public int getSizeInventory()
	{
		return 3;
	}
	
	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack)
	{
		return index == 0 || index == 1 && stack.getItem() == MSItems.CAPTCHA_CARD;
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
	public ITextComponent getDisplayName()
	{
		return new TranslationTextComponent("container.mini_punch_designix");
	}
	
	@Override
	public int[] getSlotsForFace(Direction side)
	{
		if(side == Direction.UP)
			return new int[] {1};
		if(side == Direction.DOWN)
			return new int[] {0, 2};
		else return new int[] {0};
	}
	
	@Override
	public boolean canExtractItem(int index, ItemStack stack, Direction direction)
	{
		if(index == 0)
			return !inv.get(2).isEmpty();
		else return true;
	}
	
	@Nullable
	@Override
	public Container createMenu(int windowId, PlayerInventory playerInventory, PlayerEntity player)
	{
		return new MiniPunchDesignixContainer(windowId, playerInventory, this, parameters);
	}
}