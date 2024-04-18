package com.mraof.minestuck.blockentity.machine;

import com.mraof.minestuck.alchemy.AlchemyHelper;
import com.mraof.minestuck.api.alchemy.recipe.combination.CombinationMode;
import com.mraof.minestuck.api.alchemy.recipe.combination.CombinationRecipe;
import com.mraof.minestuck.api.alchemy.recipe.combination.CombinerContainer;
import com.mraof.minestuck.blockentity.MSBlockEntityTypes;
import com.mraof.minestuck.inventory.MiniPunchDesignixMenu;
import com.mraof.minestuck.item.MSItems;
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

public class MiniPunchDesignixBlockEntity extends MachineProcessBlockEntity implements MenuProvider
{
	public static final String TITLE = "container.minestuck.mini_punch_designix";
	public static final int MAX_PROGRESS = 100;
	
	private final ProgressTracker progressTracker = new ProgressTracker(ProgressTracker.RunType.ONCE, MAX_PROGRESS, this::setChanged, this::contentsValid);
	private final CombinerContainer combinerInventory = new CombinerContainer.ItemHandlerWrapper(itemHandler, CombinationMode.OR);
	
	public MiniPunchDesignixBlockEntity(BlockPos pos, BlockState state)
	{
		super(MSBlockEntityTypes.MINI_PUNCH_DESIGNIX.get(), pos, state);
	}
	
	@Override
	protected ItemStackHandler createItemHandler()
	{
		return new CustomHandler(3, (index, stack) -> index == 0 || index == 1 && stack.is(MSItems.CAPTCHA_CARD.get()))
		{
			@Override
			protected void onContentsChanged(int slot)
			{
				MiniPunchDesignixBlockEntity.this.progressTracker.resetProgress();
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
	
	private boolean contentsValid()
	{
		boolean bothHaveItems = !itemHandler.getStackInSlot(0).isEmpty() && !itemHandler.getStackInSlot(1).isEmpty();
		boolean bothAreReadable = AlchemyHelper.isReadableCard(itemHandler.getStackInSlot(0)) && (AlchemyHelper.isReadableCard(itemHandler.getStackInSlot(1)) || AlchemyHelper.getDecodedItem(itemHandler.getStackInSlot(1)).isEmpty());
		
		if(bothHaveItems && bothAreReadable)
		{
			ItemStack output = createResult();
			if(output.isEmpty())
				return false;
			
			ItemStack currentOutput = itemHandler.getStackInSlot(2);
			return (currentOutput.isEmpty() || currentOutput.getCount() < 16 && ItemStack.isSameItemSameTags(currentOutput, output));
		} else
		{
			return false;
		}
	}
	
	@Override
	protected void tick()
	{
		this.progressTracker.tick(this::processContents);
	}
	
	private void processContents()
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
			output = CombinationRecipe.findResult(combinerInventory, level);
		}
		
		if(!output.isEmpty())
			return AlchemyHelper.createPunchedCard(output);
		else return ItemStack.EMPTY;
	}
	
	@Override
	public Component getDisplayName()
	{
		return Component.translatable(TITLE);
	}
	
	private IItemHandlerModifiable createInputSlotHandler()
	{
		return new RangedWrapper(itemHandler, 0, 1)
		{
			@Nonnull
			@Override
			public ItemStack extractItem(int slot, int amount, boolean simulate)
			{
				if(itemHandler.getStackInSlot(2).isEmpty())
					return ItemStack.EMPTY;    //Only allow extraction from slot 0 from below when slot 2 isn't empty
				return super.extractItem(slot, amount, simulate);
			}
		};
	}
	
	public IItemHandler getItemHandler(@Nullable Direction side)
	{
		if(side == null)
			return this.itemHandler;
		
		if(side == Direction.DOWN)
			return new CombinedInvWrapper(this.createInputSlotHandler(), new RangedWrapper(this.itemHandler, 2, 3));
		if(side == Direction.UP)
			return new RangedWrapper(this.itemHandler, 1, 2);
		return this.createInputSlotHandler();
	}
	
	@Nullable
	@Override
	public AbstractContainerMenu createMenu(int windowId, Inventory playerInventory, Player player)
	{
		return new MiniPunchDesignixMenu(windowId, playerInventory, itemHandler, this.progressTracker, ContainerLevelAccess.create(level, worldPosition), worldPosition);
	}
}