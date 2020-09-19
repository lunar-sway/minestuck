package com.mraof.minestuck.tileentity;

import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.inventory.MiniTotemLatheContainer;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.item.crafting.MSRecipeTypes;
import com.mraof.minestuck.item.crafting.alchemy.AlchemyHelper;
import com.mraof.minestuck.item.crafting.alchemy.CombinationMode;
import com.mraof.minestuck.item.crafting.alchemy.ItemCombiner;
import com.mraof.minestuck.util.ColorHandler;
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

public class MiniTotemLatheTileEntity extends MachineProcessTileEntity implements INamedContainerProvider, ItemCombiner
{
	public static final String TITLE = "container.minestuck.mini_totem_lathe";
	public static final RunType TYPE = RunType.BUTTON;
	
	public MiniTotemLatheTileEntity()
	{
		super(MSTileEntityTypes.MINI_TOTEM_LATHE.get());
	}
	
	@Override
	public RunType getRunType()
	{
		return TYPE;
	}
	
	@Override
	public CombinationMode getMode()
	{
		return CombinationMode.AND;
	}
	
	@Override
	public int getSizeInventory()
	{
		return 4;
	}
	
	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack)
	{
		return (index == 0 || index == 1) && stack.getItem() == MSItems.CAPTCHA_CARD || index == 2 && stack.getItem() == MSBlocks.CRUXITE_DOWEL.asItem();
	}
	
	@Override
	public boolean contentsValid()
	{
		ItemStack output = createResult();
		
		if(!output.isEmpty())
			return inv.get(3).isEmpty() || ItemStack.areItemsEqual(output, inv.get(3)) && ItemStack.areItemStackTagsEqual(output, inv.get(3));
		else return false;
	}
	
	@Override
	public void processContents()
	{
		if (!inv.get(3).isEmpty())
		{
			this.inv.get(3).grow(1);
			decrStackSize(2, 1);
			return;
		}
		
		ItemStack outputDowel = createResult();
		
		setInventorySlotContents(3, outputDowel);
		decrStackSize(2, 1);
	}
	
	private ItemStack createResult()
	{
		if(inv.get(0).isEmpty() && inv.get(1).isEmpty() || inv.get(2).isEmpty() || AlchemyHelper.hasDecodedItem(inv.get(2)))
			return ItemStack.EMPTY;
		
		ItemStack output;
		if (!inv.get(0).isEmpty() && !inv.get(1).isEmpty())
			if (!AlchemyHelper.isPunchedCard(inv.get(0)) || !AlchemyHelper.isPunchedCard(inv.get(1)))
				output = new ItemStack(MSBlocks.GENERIC_OBJECT);
			else
				output = world.getRecipeManager().getRecipe(MSRecipeTypes.COMBINATION_TYPE, this, world).map(IRecipe::getRecipeOutput).orElse(ItemStack.EMPTY);
		else
		{
			ItemStack input = inv.get(0).isEmpty() ? inv.get(1) : inv.get(0);
			if (!AlchemyHelper.isPunchedCard(input))
				output = new ItemStack(MSBlocks.GENERIC_OBJECT);
			else output = AlchemyHelper.getDecodedItem(input);
		}
		
		if(output.isEmpty())
			return ItemStack.EMPTY;
		
		ItemStack outputDowel = output.getItem().equals(MSBlocks.GENERIC_OBJECT.asItem())
				? new ItemStack(MSBlocks.CRUXITE_DOWEL) : AlchemyHelper.createEncodedItem(output, false);
		ColorHandler.setColor(outputDowel, ColorHandler.getColorFromStack(inv.get(2)));	//Setting color
		return outputDowel;
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
			return new int[] {2};
		if(side == Direction.DOWN)
			return new int[] {0, 1, 3};
		else return new int[] {0, 1};
	}
	
	@Override
	public boolean canExtractItem(int index, ItemStack stack, Direction direction)
	{
		if(index == 0 || index == 1)
			return !inv.get(3).isEmpty();
		else return true;
	}
	
	@Nullable
	@Override
	public Container createMenu(int windowId, PlayerInventory playerInventory, PlayerEntity playerIn)
	{
		return new MiniTotemLatheContainer(windowId, playerInventory, this, parameters, pos);
	}
}