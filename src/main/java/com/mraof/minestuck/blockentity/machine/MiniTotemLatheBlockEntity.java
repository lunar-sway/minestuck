package com.mraof.minestuck.blockentity.machine;

import com.mraof.minestuck.alchemy.AlchemyHelper;
import com.mraof.minestuck.api.alchemy.recipe.combination.CombinationMode;
import com.mraof.minestuck.api.alchemy.recipe.combination.CombinationRecipe;
import com.mraof.minestuck.api.alchemy.recipe.combination.CombinerContainer;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.blockentity.MSBlockEntityTypes;
import com.mraof.minestuck.inventory.MiniTotemLatheMenu;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.util.ColorHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.wrapper.CombinedInvWrapper;
import net.neoforged.neoforge.items.wrapper.RangedWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class MiniTotemLatheBlockEntity extends MachineProcessBlockEntity implements MenuProvider
{
	public static final String TITLE = "container.minestuck.mini_totem_lathe";
	public static final int MAX_PROGRESS = 100;
	
	private final ProgressTracker progressTracker = new ProgressTracker(ProgressTracker.RunType.ONCE, MAX_PROGRESS, this::setChanged, this::contentsValid);
	private final CombinerContainer combinerInventory = new CombinerContainer.ItemHandlerWrapper(itemHandler, CombinationMode.AND);
	
	public MiniTotemLatheBlockEntity(BlockPos pos, BlockState state)
	{
		super(MSBlockEntityTypes.MINI_TOTEM_LATHE.get(), pos, state);
	}
	
	@Override
	protected ItemStackHandler createItemHandler()
	{
		return new CustomHandler(4, (index, stack) ->
				(index == 0 || index == 1) && stack.is(MSItems.CAPTCHA_CARD.get())
				|| index == 2 && stack.is(MSItems.CRUXITE_DOWEL.get()))
		{
			@Override
			protected void onContentsChanged(int slot)
			{
				MiniTotemLatheBlockEntity.this.progressTracker.resetProgress();
				super.onContentsChanged(slot);
			}
		};
	}
	
	@Override
	public void load(CompoundTag nbt)
	{
		super.load(nbt);
		this.progressTracker.load(nbt);
	}
	
	@Override
	protected void saveAdditional(CompoundTag compound)
	{
		super.saveAdditional(compound);
		this.progressTracker.save(compound);
	}
	
	@Override
	protected void tick()
	{
		this.progressTracker.tick(this::processContents);
	}
	
	private boolean contentsValid()
	{
		ItemStack output = createResult();
		
		ItemStack currentOutput = itemHandler.getStackInSlot(3);
		if(!output.isEmpty())
			return currentOutput.isEmpty() || ItemStack.isSameItemSameTags(output, currentOutput);
		else return false;
	}
	
	private void processContents()
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
	public Component getDisplayName()
	{
		return Component.translatable(TITLE);
	}
	
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
	
	public IItemHandler getItemHandler(@Nullable Direction side)
	{
		if(side == null)
			return this.itemHandler;
		
		if(side == Direction.DOWN)
			return new CombinedInvWrapper(this.createSlotInputsHandler(), new RangedWrapper(this.itemHandler, 3, 4));
		if(side == Direction.UP)
			return new RangedWrapper(this.itemHandler, 2, 3);
		return this.createSlotInputsHandler();
	}
	
	@Nullable
	@Override
	public AbstractContainerMenu createMenu(int windowId, Inventory playerInventory, Player playerIn)
	{
		return new MiniTotemLatheMenu(windowId, playerInventory, itemHandler, this.progressTracker, ContainerLevelAccess.create(level, worldPosition), worldPosition);
	}
}