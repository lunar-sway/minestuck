package com.mraof.minestuck.tileentity.machine;

import com.mraof.minestuck.inventory.MiniPunchDesignixContainer;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.item.crafting.MSRecipeTypes;
import com.mraof.minestuck.item.crafting.alchemy.AlchemyHelper;
import com.mraof.minestuck.item.crafting.alchemy.CombinationMode;
import com.mraof.minestuck.item.crafting.alchemy.ItemCombiner;
import com.mraof.minestuck.tileentity.MSTileEntityTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;

public class MiniPunchDesignixTileEntity extends MachineProcessTileEntity implements INamedContainerProvider, ItemCombiner
{
	public static final String TITLE = "container.minestuck.mini_punch_designix";
	public static final RunType TYPE = RunType.BUTTON;
	
	public MiniPunchDesignixTileEntity()
	{
		super(MSTileEntityTypes.MINI_PUNCH_DESIGNIX.get());
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
	public CombinationMode getMode()
	{
		return CombinationMode.OR;
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
			ItemStack output = createResult();
			if(output.isEmpty())
				return false;
			
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
		if(!inv.get(2).isEmpty())
		{
			decrStackSize(1, 1);
			if(!AlchemyHelper.hasDecodedItem(inv.get(0)))
				decrStackSize(0, 1);
			this.inv.get(2).grow(1);
			return;
		}
		
		ItemStack outputItem = createResult();
		
		setInventorySlotContents(2, outputItem);
		if(!AlchemyHelper.hasDecodedItem(inv.get(0)))
			decrStackSize(0, 1);
		decrStackSize(1, 1);
	}
	
	private ItemStack createResult()
	{
		ItemStack output = AlchemyHelper.getDecodedItemDesignix(inv.get(0));
		if(!output.isEmpty() && AlchemyHelper.isPunchedCard(inv.get(1)))
		{
			output = world.getRecipeManager().getRecipe(MSRecipeTypes.COMBINATION_TYPE, this, world).map(IRecipe::getRecipeOutput).orElse(ItemStack.EMPTY);
		} else return output;
		
		if(!output.isEmpty())
			return AlchemyHelper.createCard(output, true);
		else return ItemStack.EMPTY;
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
		return new TranslationTextComponent(TITLE);
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
		return new MiniPunchDesignixContainer(windowId, playerInventory, this, parameters, pos);
	}
}