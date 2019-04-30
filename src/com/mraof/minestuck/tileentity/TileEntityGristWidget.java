package com.mraof.minestuck.tileentity;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.alchemy.*;
import com.mraof.minestuck.block.BlockGristWidget;
import com.mraof.minestuck.entity.item.EntityGrist;
import com.mraof.minestuck.item.MinestuckItems;
import com.mraof.minestuck.util.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IInteractionObject;

import java.util.Map.Entry;

public class TileEntityGristWidget extends TileEntityMachineProcess implements IInteractionObject
{
	
	
	public IdentifierHandler.PlayerIdentifier owner;
	boolean hasItem;
	
	public TileEntityGristWidget()
	{
		super(MinestuckTiles.GRIST_WIDGET);
	}
	
	public GristSet getGristWidgetResult()
	{
		ItemStack item = AlchemyRecipes.getDecodedItem(inv.get(0), true);
		GristSet gristSet = AlchemyCostRegistry.getGristConversion(item);
		if(inv.get(0).getItem() != MinestuckItems.captchaCard || AlchemyRecipes.isPunchedCard(inv.get(0))
				|| item.getItem() == MinestuckItems.captchaCard || gristSet == null)
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
		if(i != 0 || itemstack.getItem() != MinestuckItems.captchaCard)
		{
			return false;
		} else
		{
			return (!itemstack.getTag().getBoolean("punched") && itemstack.getTag().getInt("contentSize") > 0
					&& AlchemyRecipes.getDecodedItem(itemstack).getItem() != MinestuckItems.captchaCard);
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
		return owner != null && i != 0 && i <= MinestuckPlayerData.getData(owner).boondollars;
	}

	@Override
	public void processContents()
	{
		GristSet gristSet = getGristWidgetResult();
		
		if(!MinestuckPlayerData.addBoondollars(owner, -getGristWidgetBoondollarValue()))
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
				EntityGrist entity = new EntityGrist(world,
						this.pos.getX()
								+ 0.5 /* this.width - this.width / 2 */,
						this.pos.getY() + 1, this.pos.getZ()
						+ 0.5 /* this.width - this.width / 2 */,
						gristAmount);
				entity.motionX /= 2;
				entity.motionY /= 2;
				entity.motionZ /= 2;
				world.spawnEntity(entity);
				//Create grist entity of gristAmount
				grist -= gristAmount.getAmount();
			}
		}
		this.decrStackSize(0, 1);
	}
	
	@Override
	public void read(NBTTagCompound compound)
	{
		super.read(compound);
		
		if(IdentifierHandler.hasIdentifier(compound, "owner"))
			owner = IdentifierHandler.load(compound, "owner");
	}
	
	@Override
	public NBTTagCompound write(NBTTagCompound compound)
	{
		super.write(compound);
		
		if(owner != null)
			owner.saveToNBT(compound, "owner");
		
		return compound;
	}
	
	@Override
	public int[] getSlotsForFace(EnumFacing side)
	{
		return new int[0];
	}
	
	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction)
	{
		return true;
	}
	
	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction)
	{
		return true;
	}
	
	@Override
	public ITextComponent getName()
	{
		return new TextComponentTranslation("container.grist_widget");
	}
	
	@Override
	public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn)
	{
		return null;
	}
	
	@Override
	public String getGuiID()
	{
		return null;
	}
	
	public void resendState()
	{
		if(hasItem)
		{
			BlockGristWidget.updateItem(false, world, this.getPos());
		} else
		{
			BlockGristWidget.updateItem(true, world, this.getPos());
		}
	}
}
