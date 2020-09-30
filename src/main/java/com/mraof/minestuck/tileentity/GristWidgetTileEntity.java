package com.mraof.minestuck.tileentity;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.block.machine.GristWidgetBlock;
import com.mraof.minestuck.inventory.GristWidgetContainer;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.item.crafting.alchemy.AlchemyHelper;
import com.mraof.minestuck.item.crafting.alchemy.GristCostRecipe;
import com.mraof.minestuck.item.crafting.alchemy.GristSet;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.player.PlayerIdentifier;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.world.storage.PlayerSavedData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class GristWidgetTileEntity extends MachineProcessTileEntity implements INamedContainerProvider, IOwnable
{
	public static final String TITLE = "container.minestuck.grist_widget";
	public static final RunType TYPE = RunType.BUTTON_OVERRIDE;
	
	private PlayerIdentifier owner;
	boolean hasItem;
	
	public GristWidgetTileEntity()
	{
		super(MSTileEntityTypes.GRIST_WIDGET.get());
	}
	
	public GristSet getGristWidgetResult()
	{
		return getGristWidgetResult(inv.get(0), world);
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
	public int getSizeInventory()
	{
		return 1;
	}
	
	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack)
	{
		if(i != 0 || itemstack.getItem() != MSItems.CAPTCHA_CARD)
		{
			return false;
		} else
		{
			return (!itemstack.getTag().getBoolean("punched") && itemstack.getTag().getInt("contentSize") > 0
					&& AlchemyHelper.getDecodedItem(itemstack).getItem() != MSItems.CAPTCHA_CARD);
		}
	}
	
	@Override
	public void tick()
	{
		super.tick();
		
		boolean item = this.getStackInSlot(0).getCount() == 0;
		if(item != hasItem)
		{
			hasItem = item;
			resendState();
		}
	}
	
	@Override
	public boolean contentsValid()
	{
		if(MinestuckConfig.disableGristWidget.get())
			return false;
		if(world.isBlockPowered(this.getPos()))
			return false;
		int i = getGristWidgetBoondollarValue();
		return owner != null && i != 0 && i <= PlayerSavedData.getData(owner, world).getBoondollars();
	}

	@Override
	public void processContents()
	{
		GristSet gristSet = getGristWidgetResult();
		
		if(!PlayerSavedData.getData(owner, world).tryTakeBoondollars(getGristWidgetBoondollarValue()))
		{
			Debug.warnf("Failed to remove boondollars for a grist widget from %s's porkhollow", owner.getUsername());
			return;
		}
		
		gristSet.spawnGristEntities(world, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, world.rand, entity -> entity.setMotion(entity.getMotion().mul(0.5, 0.5, 0.5)));
		
		this.decrStackSize(0, 1);
	}
	
	@Override
	public void read(CompoundNBT compound)
	{
		super.read(compound);
		
		if(IdentifierHandler.hasIdentifier(compound, "owner"))
			owner = IdentifierHandler.load(compound, "owner");
	}
	
	@Override
	public CompoundNBT write(CompoundNBT compound)
	{
		super.write(compound);
		
		if(owner != null)
			owner.saveToNBT(compound, "owner");
		
		return compound;
	}
	
	@Override
	public int[] getSlotsForFace(Direction side)
	{
		return new int[0];
	}
	
	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, Direction direction)
	{
		return true;
	}
	
	@Override
	public boolean canExtractItem(int index, ItemStack stack, Direction direction)
	{
		return true;
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
		return new GristWidgetContainer(windowId, playerInventory, this, parameters, pos);
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
		if(hasItem)
		{
			GristWidgetBlock.updateItem(false, world, this.getPos());
		} else
		{
			GristWidgetBlock.updateItem(true, world, this.getPos());
		}
	}
}