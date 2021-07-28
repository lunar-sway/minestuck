package com.mraof.minestuck.tileentity.machine;

import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.inventory.MiniTotemLatheContainer;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.item.crafting.alchemy.*;
import com.mraof.minestuck.tileentity.MSTileEntityTypes;
import com.mraof.minestuck.util.ColorHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import net.minecraftforge.items.wrapper.RangedWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class MiniTotemLatheTileEntity extends MachineProcessTileEntity implements INamedContainerProvider
{
	public static final String TITLE = "container.minestuck.mini_totem_lathe";
	public static final RunType TYPE = RunType.BUTTON;
	
	private final ItemCombiner combinerInventory = new ItemCombinerWrapper(itemHandler, CombinationMode.AND);
	
	public MiniTotemLatheTileEntity()
	{
		super(MSTileEntityTypes.MINI_TOTEM_LATHE.get());
	}
	
	@Override
	protected ItemStackHandler createItemHandler()
	{
		return new CustomHandler(4, (index, stack) -> (index == 0 || index == 1) && stack.getItem() == MSItems.CAPTCHA_CARD || index == 2 && stack.getItem() == MSBlocks.CRUXITE_DOWEL.asItem());
	}
	
	@Override
	public RunType getRunType()
	{
		return TYPE;
	}
	
	@Override
	public boolean contentsValid()
	{
		ItemStack output = createResult();
		
		ItemStack currentOutput = itemHandler.getStackInSlot(3);
		if(!output.isEmpty())
			return currentOutput.isEmpty() || ItemStack.areItemsEqual(output, currentOutput) && ItemStack.areItemStackTagsEqual(output, currentOutput);
		else return false;
	}
	
	@Override
	public void processContents()
	{
		if (!itemHandler.getStackInSlot(3).isEmpty())
		{
			itemHandler.extractItem(3, -1, false);
			itemHandler.extractItem(2, 1, false);
			return;
		}
		
		ItemStack outputDowel = createResult();
		
		itemHandler.setStackInSlot(3, outputDowel);
		itemHandler.extractItem(2, 1, false);
	}
	
	private ItemStack createResult()
	{
		ItemStack input1 = itemHandler.getStackInSlot(0), input2 = itemHandler.getStackInSlot(1), dowelInput = itemHandler.getStackInSlot(2);
		if(input1.isEmpty() && input2.isEmpty() || dowelInput.isEmpty() || AlchemyHelper.hasDecodedItem(dowelInput))
			return ItemStack.EMPTY;
		
		ItemStack output;
		if (!input1.isEmpty() && !input2.isEmpty())
			if (!AlchemyHelper.isPunchedCard(input1) || !AlchemyHelper.isPunchedCard(input2))
				output = new ItemStack(MSBlocks.GENERIC_OBJECT);
			else
				output = CombinationRecipe.findResult(combinerInventory, world);
		else
		{
			ItemStack input = input1.isEmpty() ? input2 : input1;
			if (!AlchemyHelper.isPunchedCard(input))
				output = new ItemStack(MSBlocks.GENERIC_OBJECT);
			else output = AlchemyHelper.getDecodedItem(input);
		}
		
		if(output.isEmpty())
			return ItemStack.EMPTY;
		
		ItemStack outputDowel = output.getItem().equals(MSBlocks.GENERIC_OBJECT.asItem())
				? new ItemStack(MSBlocks.CRUXITE_DOWEL) : AlchemyHelper.createEncodedItem(output, false);
		ColorHandler.setStackColor(outputDowel, ColorHandler.getColorFromStack(dowelInput));	//Setting color
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
	
	private final LazyOptional<IItemHandler> upHandler = LazyOptional.of(() -> new RangedWrapper(itemHandler, 2, 3));
	private final LazyOptional<IItemHandler> downHandler = LazyOptional.of(() -> new CombinedInvWrapper(createSlotInputsHandler(), new RangedWrapper(itemHandler, 3, 4)));
	private final LazyOptional<IItemHandler> sideHandler = LazyOptional.of(this::createSlotInputsHandler);
	
	private IItemHandlerModifiable createSlotInputsHandler()
	{
		return new RangedWrapper(itemHandler, 0, 2)
		{
			@Nonnull
			@Override
			public ItemStack extractItem(int slot, int amount, boolean simulate)
			{
				if(itemHandler.getStackInSlot(3).isEmpty())
					return ItemStack.EMPTY;
				return super.extractItem(slot, amount, simulate);
			}
		};
	}
	
	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side)
	{
		if(cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && side != null)
		{
			return side == Direction.DOWN ? downHandler.cast() :
					side == Direction.UP ? upHandler.cast() : sideHandler.cast();
		}
		return super.getCapability(cap, side);
	}
	
	@Nullable
	@Override
	public Container createMenu(int windowId, PlayerInventory playerInventory, PlayerEntity playerIn)
	{
		return new MiniTotemLatheContainer(windowId, playerInventory, itemHandler, parameters, IWorldPosCallable.of(world, pos), pos);
	}
}