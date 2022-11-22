package com.mraof.minestuck.blockentity.machine;

import com.mraof.minestuck.alchemy.*;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.blockentity.MSBlockEntityTypes;
import com.mraof.minestuck.inventory.MiniTotemLatheMenu;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.util.ColorHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
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

public class MiniTotemLatheBlockEntity extends MachineProcessBlockEntity implements MenuProvider
{
	public static final String TITLE = "container.minestuck.mini_totem_lathe";
	public static final RunType TYPE = RunType.BUTTON;
	
	private final ItemCombiner combinerInventory = new ItemCombinerWrapper(itemHandler, CombinationMode.AND);
	
	public MiniTotemLatheBlockEntity(BlockPos pos, BlockState state)
	{
		super(MSBlockEntityTypes.MINI_TOTEM_LATHE.get(), pos, state);
	}
	
	@Override
	protected ItemStackHandler createItemHandler()
	{
		return new CustomHandler(4, (index, stack) -> (index == 0 || index == 1) && stack.getItem() == MSItems.CAPTCHA_CARD.get() || index == 2 && stack.getItem() == MSItems.CRUXITE_DOWEL.get());
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
			return currentOutput.isEmpty() || ItemStack.isSame(output, currentOutput) && ItemStack.tagMatches(output, currentOutput);
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
				output = new ItemStack(MSBlocks.GENERIC_OBJECT.get());
			else
				output = CombinationRecipe.findResult(combinerInventory, level);
		else
		{
			ItemStack input = input1.isEmpty() ? input2 : input1;
			if (!AlchemyHelper.isPunchedCard(input))
				output = new ItemStack(MSBlocks.GENERIC_OBJECT.get());
			else output = AlchemyHelper.getDecodedItem(input);
		}
		
		if(output.isEmpty())
			return ItemStack.EMPTY;
		
		ItemStack outputDowel = output.getItem().equals(MSBlocks.GENERIC_OBJECT.get().asItem())
				? new ItemStack(MSBlocks.CRUXITE_DOWEL.get()) : AlchemyHelper.createEncodedItem(output, false);
		ColorHandler.setColor(outputDowel, ColorHandler.getColorFromStack(dowelInput));	//Setting color
		return outputDowel;
	}
	
	@Override
	public void setChanged()
	{
		this.progress = 0;
		this.ready = false;
		super.setChanged();
	}
	
	@Override
	public Component getDisplayName()
	{
		return new TranslatableComponent(TITLE);
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
	public AbstractContainerMenu createMenu(int windowId, Inventory playerInventory, Player playerIn)
	{
		return new MiniTotemLatheMenu(windowId, playerInventory, itemHandler, parameters, ContainerLevelAccess.create(level, worldPosition), worldPosition);
	}
}