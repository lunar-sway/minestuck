package com.mraof.minestuck.tileentity;

import com.mraof.minestuck.alchemy.AlchemyRecipes;
import com.mraof.minestuck.alchemy.CombinationRegistry;
import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.inventory.MiniTotemLatheContainer;
import com.mraof.minestuck.item.MinestuckItems;
import com.mraof.minestuck.util.ColorCollector;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;

public class MiniTotemLatheTileEntity extends MachineProcessTileEntity implements INamedContainerProvider
{
	public static final RunType TYPE = RunType.BUTTON;
	
	public MiniTotemLatheTileEntity()
	{
		super(ModTileEntityTypes.MINI_TOTEM_LATHE);
	}
	
	@Override
	public RunType getRunType()
	{
		return TYPE;
	}
	
	@Override
	public int getSizeInventory()
	{
		return 4;
	}
	
	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack)
	{
		return (index == 0 || index == 1) && stack.getItem() == MinestuckItems.CAPTCHA_CARD || index == 2 && stack.getItem() == MinestuckBlocks.CRUXITE_DOWEL.asItem();
	}
	
	@Override
	public boolean contentsValid()
	{
		if ((!inv.get(0).isEmpty() || !inv.get(1).isEmpty()) && !inv.get(2).isEmpty() && !(inv.get(2).hasTag() && inv.get(2).getTag().contains("contentID"))
				&& (inv.get(3).isEmpty() || inv.get(3).getCount() < inv.get(3).getMaxStackSize() && ColorCollector.getColorFromStack(inv.get(3), -1) == ColorCollector.getColorFromStack(inv.get(2), -1)))
		{
			if (!inv.get(0).isEmpty() && !inv.get(1).isEmpty())
			{
				if (!inv.get(0).hasTag() || !inv.get(0).getTag().getBoolean("punched") || !inv.get(1).hasTag() || !inv.get(1).getTag().getBoolean("punched"))
					return inv.get(3).isEmpty() || !(inv.get(3).hasTag() && inv.get(3).getTag().contains("contentID"));
				else
				{
					ItemStack output = CombinationRegistry.getCombination(AlchemyRecipes.getDecodedItem(inv.get(0)), AlchemyRecipes.getDecodedItem(inv.get(1)), CombinationRegistry.Mode.MODE_AND);
					return !output.isEmpty() && (inv.get(3).isEmpty() || AlchemyRecipes.getDecodedItem(inv.get(3)).isItemEqual(output));
				}
			}
			else
			{
				ItemStack input = inv.get(0).isEmpty() ? inv.get(1) : inv.get(0);
				return (inv.get(3).isEmpty() || (AlchemyRecipes.getDecodedItem(inv.get(3)).isItemEqual(AlchemyRecipes.getDecodedItem(input))
						|| !(input.hasTag() && input.getTag().getBoolean("punched")) && !(inv.get(3).hasTag() && inv.get(3).getTag().contains("contentID"))));
			}
		}
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
		
		ItemStack output;
		if (!inv.get(0).isEmpty() && !inv.get(1).isEmpty())
			if (!inv.get(0).hasTag() || !inv.get(0).getTag().getBoolean("punched") || !inv.get(1).hasTag() || !inv.get(1).getTag().getBoolean("punched"))
				output = new ItemStack(MinestuckBlocks.GENERIC_OBJECT);
			else
				output = CombinationRegistry.getCombination(AlchemyRecipes.getDecodedItem(inv.get(0)), AlchemyRecipes.getDecodedItem(inv.get(1)), CombinationRegistry.Mode.MODE_AND);
		else
		{
			ItemStack input = inv.get(0).isEmpty() ? inv.get(1) : inv.get(0);
			if (!input.hasTag() || !input.getTag().getBoolean("punched"))
				output = new ItemStack(MinestuckBlocks.GENERIC_OBJECT);
			else output = AlchemyRecipes.getDecodedItem(input);
		}
		
		ItemStack outputDowel = output.getItem().equals(MinestuckBlocks.GENERIC_OBJECT.asItem())
				? new ItemStack(MinestuckBlocks.CRUXITE_DOWEL) : AlchemyRecipes.createEncodedItem(output, false);
		ColorCollector.setColor(outputDowel, ColorCollector.getColorFromStack(inv.get(2), -1));	//Setting color
		
		setInventorySlotContents(3, outputDowel);
		decrStackSize(2, 1);
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
		return new TranslationTextComponent("container.mini_totem_lathe");
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
		return new MiniTotemLatheContainer(windowId, playerInventory, this, parameters);
	}
}