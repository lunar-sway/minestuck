package com.mraof.minestuck.blockentity.machine;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.block.machine.GristWidgetBlock;
import com.mraof.minestuck.blockentity.MSBlockEntityTypes;
import com.mraof.minestuck.inventory.GristWidgetMenu;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.alchemy.AlchemyHelper;
import com.mraof.minestuck.alchemy.GristCostRecipe;
import com.mraof.minestuck.alchemy.GristSet;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.player.PlayerIdentifier;
import com.mraof.minestuck.player.PlayerSavedData;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.Objects;

public class GristWidgetBlockEntity extends MachineProcessBlockEntity implements MenuProvider, IOwnable
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	public static final String TITLE = "container.minestuck.grist_widget";
	public static final RunType TYPE = RunType.BUTTON_OVERRIDE;
	
	private PlayerIdentifier owner;
	private boolean hasItem;
	
	public GristWidgetBlockEntity(BlockPos pos, BlockState state)
	{
		super(MSBlockEntityTypes.GRIST_WIDGET.get(), pos, state);
	}
	
	@Override
	protected ItemStackHandler createItemHandler()
	{
		return new CustomHandler(1, this::isItemValid)
		{
			@Override
			protected void onContentsChanged(int slot)
			{
				super.onContentsChanged(slot);
				checkAndUpdateState();
			}
		};
	}
	
	private void checkAndUpdateState()
	{
		Objects.requireNonNull(this.level);
		boolean hasCard = !this.itemHandler.getStackInSlot(0).isEmpty();
		BlockState newState = this.getBlockState().setValue(GristWidgetBlock.HAS_CARD, hasCard);
		
		if(newState != this.getBlockState())
			this.level.setBlock(this.getBlockPos(), newState, Block.UPDATE_CLIENTS);
			
	}
	
	private boolean isItemValid(int slot, ItemStack stack)
	{
		if(stack.getItem() != MSItems.CAPTCHA_CARD.get())
		{
			return false;
		} else
		{
			return (!AlchemyHelper.isPunchedCard(stack) && !AlchemyHelper.isGhostCard(stack)
					&& AlchemyHelper.getDecodedItem(stack).getItem() != MSItems.CAPTCHA_CARD.get());
		}
	}
	
	public GristSet getGristWidgetResult()
	{
		return getGristWidgetResult(itemHandler.getStackInSlot(0), level);
	}
	
	public static GristSet getGristWidgetResult(ItemStack stack, Level level)
	{
		if(level == null)
			return null;
		ItemStack heldItem = AlchemyHelper.getDecodedItem(stack, true);
		GristSet gristSet = GristCostRecipe.findCostForItem(heldItem, null, true, level);
		if(stack.getItem() != MSItems.CAPTCHA_CARD.get() || AlchemyHelper.isPunchedCard(stack) || gristSet == null)
			return null;
		
		return gristSet;
	}
	
	public int getGristWidgetBoondollarValue()
	{
		GristSet set = getGristWidgetResult();
		return getGristWidgetBoondollarValue(set);
	}
	
	public static int getGristWidgetBoondollarValue(GristSet set)
	{
		return set == null ? 0 : Math.max(1, (int) Math.pow(set.getValue(), 1/1.5));
	}
	
	@Override
	public RunType getRunType()
	{
		return TYPE;
	}
	
	@Override
	public boolean contentsValid()
	{
		if(MinestuckConfig.SERVER.disableGristWidget.get())
			return false;
		if(level.hasNeighborSignal(this.getBlockPos()))
			return false;
		int i = getGristWidgetBoondollarValue();
		return owner != null && i != 0 && i <= PlayerSavedData.getData(owner, level).getBoondollars();
	}

	@Override
	public void processContents()
	{
		GristSet gristSet = getGristWidgetResult();
		
		if(!PlayerSavedData.getData(owner, level).tryTakeBoondollars(getGristWidgetBoondollarValue()))
		{
			LOGGER.warn("Failed to remove boondollars for a grist widget from {}'s porkhollow", owner.getUsername());
			return;
		}
		
		gristSet.spawnGristEntities(level, worldPosition.getX() + 0.5, worldPosition.getY() + 1, worldPosition.getZ() + 0.5, level.random, entity -> entity.setDeltaMovement(entity.getDeltaMovement().multiply(0.5, 0.5, 0.5)));
		
		itemHandler.extractItem(0, 1, false);
	}
	
	@Override
	public void load(CompoundTag nbt)
	{
		super.load(nbt);
		
		if(IdentifierHandler.hasIdentifier(nbt, "owner"))
			owner = IdentifierHandler.load(nbt, "owner");
	}
	
	@Override
	public void saveAdditional(CompoundTag compound)
	{
		super.saveAdditional(compound);
		
		if(owner != null)
			owner.saveToNBT(compound, "owner");
	}
	
	@Override
	public Component getDisplayName()
	{
		return new TranslatableComponent(TITLE);
	}
	
	@Nullable
	@Override
	public AbstractContainerMenu createMenu(int windowId, Inventory playerInventory, Player player)
	{
		return new GristWidgetMenu(windowId, playerInventory, itemHandler, parameters, ContainerLevelAccess.create(level, worldPosition), worldPosition);
	}
	
	@Override
	public void setOwner(PlayerIdentifier identifier)
	{
		this.owner = identifier;
	}
	
	@Override
	public PlayerIdentifier getOwner()
	{
		return owner;
	}
}