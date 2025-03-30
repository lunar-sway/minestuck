package com.mraof.minestuck.blockentity.machine;

import com.mraof.minestuck.api.alchemy.recipe.combination.CombinationInput;
import com.mraof.minestuck.api.alchemy.recipe.combination.CombinationMode;
import com.mraof.minestuck.api.alchemy.recipe.combination.CombinationRecipe;
import com.mraof.minestuck.blockentity.MSBlockEntityTypes;
import com.mraof.minestuck.inventory.MiniTotemLatheMenu;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.item.components.EncodedItemComponent;
import com.mraof.minestuck.item.components.MSItemComponents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
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
	protected void loadAdditional(CompoundTag nbt, HolderLookup.Provider pRegistries)
	{
		super.loadAdditional(nbt, pRegistries);
		this.progressTracker.load(nbt);
	}
	
	@Override
	protected void saveAdditional(CompoundTag compound, HolderLookup.Provider provider)
	{
		super.saveAdditional(compound, provider);
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
			return currentOutput.isEmpty() || ItemStack.isSameItemSameComponents(output, currentOutput);
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
		ItemStack cardInput1 = itemHandler.getStackInSlot(0), cardInput2 = itemHandler.getStackInSlot(1), dowelInput = itemHandler.getStackInSlot(2);
		if(cardInput1.isEmpty() && cardInput2.isEmpty() || dowelInput.isEmpty() || dowelInput.has(MSItemComponents.ENCODED_ITEM))
			return ItemStack.EMPTY;
		
		ItemStack output;
		if(!cardInput1.isEmpty() && !cardInput2.isEmpty())
		{
			ItemStack input1 = EncodedItemComponent.getEncodedOrBlank(cardInput1),
					input2 = EncodedItemComponent.getEncodedOrBlank(cardInput2);
			if(input1.is(MSItems.GENERIC_OBJECT.get()) || input2.is(MSItems.GENERIC_OBJECT.get()))
				output = new ItemStack(MSItems.GENERIC_OBJECT.get());
			else
				output = CombinationRecipe.findResult(new CombinationInput(input1, input2, CombinationMode.AND), level);
		} else
		{
			ItemStack input = cardInput1.isEmpty() ? cardInput2 : cardInput1;
			output = EncodedItemComponent.getEncodedOrBlank(input);
		}
		
		if(output.isEmpty())
			return ItemStack.EMPTY;
		
		return EncodedItemComponent.setEncodedUnlessBlank(dowelInput.copy().split(1), output.getItem());
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
