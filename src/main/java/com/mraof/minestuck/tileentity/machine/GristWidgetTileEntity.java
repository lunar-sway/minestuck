package com.mraof.minestuck.tileentity.machine;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.block.machine.GristWidgetBlock;
import com.mraof.minestuck.inventory.GristWidgetContainer;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.item.crafting.alchemy.AlchemyHelper;
import com.mraof.minestuck.item.crafting.alchemy.GristCostRecipe;
import com.mraof.minestuck.item.crafting.alchemy.GristSet;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.player.PlayerIdentifier;
import com.mraof.minestuck.tileentity.MSTileEntityTypes;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.world.storage.PlayerSavedData;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

public class GristWidgetTileEntity extends MachineProcessTileEntity implements INamedContainerProvider, IOwnable
{
	public static final String TITLE = "container.minestuck.grist_widget";
	public static final RunType TYPE = RunType.BUTTON_OVERRIDE;
	
	private PlayerIdentifier owner;
	private boolean hasItem;
	
	public GristWidgetTileEntity()
	{
		super(MSTileEntityTypes.GRIST_WIDGET.get());
	}
	
	@Override
	protected ItemStackHandler createItemHandler()
	{
		return new CustomHandler(1, this::isItemValid);
	}
	
	private boolean isItemValid(int slot, ItemStack stack)
	{
		if(stack.getItem() != MSItems.CAPTCHA_CARD)
		{
			return false;
		} else
		{
			return (!AlchemyHelper.isPunchedCard(stack) && !AlchemyHelper.isGhostCard(stack)
					&& AlchemyHelper.getDecodedItem(stack).getItem() != MSItems.CAPTCHA_CARD);
		}
	}
	
	public GristSet getGristWidgetResult()
	{
		return getGristWidgetResult(itemHandler.getStackInSlot(0), level);
	}
	
	public static GristSet getGristWidgetResult(ItemStack stack, World world)
	{
		if(world == null)
			return null;
		ItemStack heldItem = AlchemyHelper.getDecodedItem(stack, true);
		GristSet gristSet = GristCostRecipe.findCostForItem(heldItem, null, true, world);
		if(stack.getItem() != MSItems.CAPTCHA_CARD || AlchemyHelper.isPunchedCard(stack) || gristSet == null)
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
	public void tick()
	{
		super.tick();
		
		boolean item = !itemHandler.getStackInSlot(0).isEmpty();
		if(item != hasItem)
		{
			hasItem = item;
			resendState();
		}
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
			Debug.warnf("Failed to remove boondollars for a grist widget from %s's porkhollow", owner.getUsername());
			return;
		}
		
		gristSet.spawnGristEntities(level, worldPosition.getX() + 0.5, worldPosition.getY() + 1, worldPosition.getZ() + 0.5, level.random, entity -> entity.setDeltaMovement(entity.getDeltaMovement().multiply(0.5, 0.5, 0.5)));
		
		itemHandler.extractItem(0, 1, false);
	}
	
	@Override
	public void load(BlockState state, CompoundNBT nbt)
	{
		super.load(state, nbt);
		
		if(IdentifierHandler.hasIdentifier(nbt, "owner"))
			owner = IdentifierHandler.load(nbt, "owner");
	}
	
	@Override
	public CompoundNBT save(CompoundNBT compound)
	{
		super.save(compound);
		
		if(owner != null)
			owner.saveToNBT(compound, "owner");
		
		return compound;
	}
	
	@Override
	public ITextComponent getDisplayName()
	{
		return new TranslationTextComponent(TITLE);
	}
	
	@Nullable
	@Override
	public Container createMenu(int windowId, PlayerInventory playerInventory, PlayerEntity player)
	{
		return new GristWidgetContainer(windowId, playerInventory, itemHandler, parameters, IWorldPosCallable.create(level, worldPosition), worldPosition);
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
	
	public void resendState()
	{
		GristWidgetBlock.updateItem(hasItem, level, this.getBlockPos());
	}
}