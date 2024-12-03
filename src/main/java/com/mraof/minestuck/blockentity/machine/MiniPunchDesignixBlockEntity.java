package com.mraof.minestuck.blockentity.machine;

import com.mraof.minestuck.api.alchemy.recipe.combination.CombinationInput;
import com.mraof.minestuck.api.alchemy.recipe.combination.CombinationMode;
import com.mraof.minestuck.api.alchemy.recipe.combination.CombinationRecipe;
import com.mraof.minestuck.blockentity.MSBlockEntityTypes;
import com.mraof.minestuck.inventory.MiniPunchDesignixMenu;
import com.mraof.minestuck.item.CaptchaCardItem;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.item.components.CardStoredItemComponent;
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

public class MiniPunchDesignixBlockEntity extends MachineProcessBlockEntity implements MenuProvider
{
	public static final String TITLE = "container.minestuck.mini_punch_designix";
	public static final int MAX_PROGRESS = 100;
	
	private final ProgressTracker progressTracker = new ProgressTracker(ProgressTracker.RunType.ONCE, MAX_PROGRESS, this::setChanged, this::contentsValid);
	
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
	
	private boolean contentsValid()
	{
		boolean bothHaveItems = !itemHandler.getStackInSlot(0).isEmpty() && !itemHandler.getStackInSlot(1).isEmpty();
		
		if(bothHaveItems)
		{
			ItemStack output = createResult();
			if(output.isEmpty())
				return false;
			
			ItemStack currentOutput = itemHandler.getStackInSlot(2);
			return (currentOutput.isEmpty() || currentOutput.getCount() < 16 && ItemStack.isSameItemSameComponents(currentOutput, output));
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
		ItemStack captchaInput = itemHandler.getStackInSlot(0);
		boolean shouldConsumeCaptchaInput = !(captchaInput.is(MSItems.CAPTCHA_CARD)
				&& (captchaInput.has(MSItemComponents.ENCODED_ITEM) || captchaInput.has(MSItemComponents.CARD_STORED_ITEM)));
		
		if(!itemHandler.getStackInSlot(2).isEmpty())
		{
			itemHandler.extractItem(1, 1, false);
			if(shouldConsumeCaptchaInput)
				itemHandler.extractItem(0, 1, false);
			itemHandler.extractItem(2, -1, false);
			return;
		}
		
		ItemStack outputItem = createResult();
		
		itemHandler.setStackInSlot(2, outputItem);
		if(shouldConsumeCaptchaInput)
			itemHandler.extractItem(0, 1, false);
		itemHandler.extractItem(1, 1, false);
	}
	
	private ItemStack createResult()
	{
		ItemStack output;
		
		ItemStack captchaInput = itemHandler.getStackInSlot(0);
		EncodedItemComponent captchaPunchedInput = captchaInput.get(MSItemComponents.ENCODED_ITEM);
		CardStoredItemComponent captchaStoredInput = captchaInput.get(MSItemComponents.CARD_STORED_ITEM);
		if (captchaInput.is(MSItems.CAPTCHA_CARD) && captchaPunchedInput != null)
			output = captchaPunchedInput.asItemStack();
		else if (captchaInput.is(MSItems.CAPTCHA_CARD) && captchaStoredInput != null)
		{
			if(captchaStoredInput.code() == null)
				return ItemStack.EMPTY;
			output = captchaStoredInput.storedStack().copy();
		} else
			output = captchaInput.copy();
		
		EncodedItemComponent punchedInput = itemHandler.getStackInSlot(1).get(MSItemComponents.ENCODED_ITEM);
		if(punchedInput != null)
			output = CombinationRecipe.findResult(new CombinationInput(output, punchedInput.asItemStack(), CombinationMode.OR), level);
		
		if(output.isEmpty())
			return ItemStack.EMPTY;
		
		return CaptchaCardItem.createPunchedCard(output.getItem());
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
