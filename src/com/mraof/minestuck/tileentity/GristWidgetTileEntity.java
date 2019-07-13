package com.mraof.minestuck.tileentity;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.alchemy.*;
import com.mraof.minestuck.block.GristWidgetBlock;
import com.mraof.minestuck.entity.item.GristEntity;
import com.mraof.minestuck.inventory.ContainerGristWidget;
import com.mraof.minestuck.item.MinestuckItems;
import com.mraof.minestuck.util.*;
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

import javax.annotation.Nullable;
import java.util.Map.Entry;

public class GristWidgetTileEntity extends MachineProcessTileEntity implements INamedContainerProvider
{
	
	
	public IdentifierHandler.PlayerIdentifier owner;
	boolean hasItem;
	
	public GristWidgetTileEntity()
	{
		super(MinestuckTiles.GRIST_WIDGET);
	}
	
	public GristSet getGristWidgetResult()
	{
		ItemStack item = AlchemyRecipes.getDecodedItem(inv.get(0), true);
		GristSet gristSet = AlchemyCostRegistry.getGristConversion(item);
		if(inv.get(0).getItem() != MinestuckItems.CAPTCHA_CARD || AlchemyRecipes.isPunchedCard(inv.get(0))
				|| item.getItem() == MinestuckItems.CAPTCHA_CARD || gristSet == null)
			return null;
		
		if (item.getCount() != 1)
			gristSet.scaleGrist(item.getCount());
		
		if (item.isDamaged())
		{
			float multiplier = 1 - item.getItem().getDamage(item) / ((float) item.getMaxDamage());
			for (GristAmount amount : gristSet.getArray())
			{
				gristSet.setGrist(amount.getType(), (int) (amount.getAmount() * multiplier));
			}
		}
		return gristSet;
	}
	
	public int getGristWidgetBoondollarValue()
	{
		GristSet set = getGristWidgetResult();
		return set == null ? 0 : Math.max(1, (int) Math.pow(set.getValue(), 1/1.5));
	}
	
	@Override
	public RunType getRunType()
	{
		return RunType.BUTTON_OVERRIDE;
	}
	
	@Override
	public int getSizeInventory()
	{
		return 1;
	}
	
	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack)
	{
		if(i != 0 || itemstack.getItem() != MinestuckItems.CAPTCHA_CARD)
		{
			return false;
		} else
		{
			return (!itemstack.getTag().getBoolean("punched") && itemstack.getTag().getInt("contentSize") > 0
					&& AlchemyRecipes.getDecodedItem(itemstack).getItem() != MinestuckItems.CAPTCHA_CARD);
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
		if(MinestuckConfig.disableGristWidget)
			return false;
		if(world.isBlockPowered(this.getPos()))
			return false;
		int i = getGristWidgetBoondollarValue();
		return owner != null && i != 0 && i <= PlayerSavedData.get(world).getData(owner).boondollars;
	}

	@Override
	public void processContents()
	{
		GristSet gristSet = getGristWidgetResult();
		
		if(!PlayerSavedData.get(world).addBoondollars(owner, -getGristWidgetBoondollarValue()))
		{
			Debug.warnf("Failed to remove boondollars for a grist widget from %s's porkhollow", owner.getUsername());
			return;
		}
		
		for(Entry<GristType, Integer> entry : gristSet.getMap().entrySet())
		{
			int grist = entry.getValue();
			while(true)
			{
				if(grist == 0)
					break;
				GristAmount gristAmount = new GristAmount(entry.getKey(),
						grist <= 3 ? grist : (world.rand.nextInt(grist) + 1));
				GristEntity entity = new GristEntity(world,
						this.pos.getX()
								+ 0.5 /* this.width - this.width / 2 */,
						this.pos.getY() + 1, this.pos.getZ()
						+ 0.5 /* this.width - this.width / 2 */,
						gristAmount);
				entity.setMotion(entity.getMotion().mul(0.5, 0.5, 0.5));
				world.addEntity(entity);
				//Create grist entity of gristAmount
				grist -= gristAmount.getAmount();
			}
		}
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
		return new TranslationTextComponent("container.grist_widget");
	}
	
	@Nullable
	@Override
	public Container createMenu(int containerId, PlayerInventory playerInventory, PlayerEntity player)
	{
		return new ContainerGristWidget(playerInventory, this);
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