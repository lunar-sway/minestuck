package com.mraof.minestuck.tileentity.machine;

import com.mraof.minestuck.inventory.MiniPunchDesignixContainer;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.item.crafting.alchemy.*;
import com.mraof.minestuck.tileentity.MSTileEntityTypes;
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

public class MiniPunchDesignixTileEntity extends MachineProcessTileEntity implements INamedContainerProvider
{
	public static final String TITLE = "container.minestuck.mini_punch_designix";
	public static final RunType TYPE = RunType.BUTTON;
	
	private final ItemCombiner combinerInventory = new ItemCombinerWrapper(itemHandler, CombinationMode.OR);
	
	public MiniPunchDesignixTileEntity()
	{
		super(MSTileEntityTypes.MINI_PUNCH_DESIGNIX.get());
	}
	
	@Override
	protected ItemStackHandler createItemHandler()
	{
		return new CustomHandler(3, (index, stack) -> index == 0 || index == 1 && stack.getItem() == MSItems.CAPTCHA_CARD);
	}
	
	@Override
	public RunType getRunType()
	{
		return TYPE;
	}
	
	@Override
	public boolean contentsValid()
	{
		if (!itemHandler.getStackInSlot(0).isEmpty() && !itemHandler.getStackInSlot(1).isEmpty())
		{
			ItemStack output = createResult();
			if(output.isEmpty())
				return false;
			
			ItemStack currentOutput = itemHandler.getStackInSlot(2);
			return (currentOutput.isEmpty() || currentOutput.getCount() < 16 && ItemStack.areItemStackTagsEqual(currentOutput, output));
		}
		else
		{
			return false;
		}
	}
	
	@Override
	public void processContents()
	{
		if(!itemHandler.getStackInSlot(2).isEmpty())
		{
			itemHandler.extractItem(1, 1, false);
			if(!AlchemyHelper.hasDecodedItem(itemHandler.getStackInSlot(0)))
				itemHandler.extractItem(0, 1, false);
			itemHandler.extractItem(2, -1, false);
			return;
		}
		
		ItemStack outputItem = createResult();
		
		itemHandler.setStackInSlot(2, outputItem);
		if(!AlchemyHelper.hasDecodedItem(itemHandler.getStackInSlot(0)))
			itemHandler.extractItem(0, 1, false);
		itemHandler.extractItem(1, 1, false);
	}
	
	private ItemStack createResult()
	{
		ItemStack output = AlchemyHelper.getDecodedItemDesignix(itemHandler.getStackInSlot(0));
		if(!output.isEmpty() && AlchemyHelper.isPunchedCard(itemHandler.getStackInSlot(1)))
		{
			output = CombinationRecipe.findResult(combinerInventory, world);
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
	
	private final LazyOptional<IItemHandler> sideHandler = LazyOptional.of(this::createInputSlotHandler);
	private final LazyOptional<IItemHandler> upHandler = LazyOptional.of(() -> new RangedWrapper(itemHandler, 1, 2));
	private final LazyOptional<IItemHandler> downHandler = LazyOptional.of(() -> new CombinedInvWrapper(createInputSlotHandler(), new RangedWrapper(itemHandler, 2, 3)));
	
	private IItemHandlerModifiable createInputSlotHandler()
	{
		return new RangedWrapper(itemHandler, 0, 1)
		{
			@Nonnull
			@Override
			public ItemStack extractItem(int slot, int amount, boolean simulate)
			{
				if(itemHandler.getStackInSlot(2).isEmpty())
					return ItemStack.EMPTY;	//Only allow extraction from slot 0 from below when slot 2 isn't empty
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
	public Container createMenu(int windowId, PlayerInventory playerInventory, PlayerEntity player)
	{
		return new MiniPunchDesignixContainer(windowId, playerInventory, itemHandler, parameters, IWorldPosCallable.of(world, pos), pos);
	}
}