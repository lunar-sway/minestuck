package com.mraof.minestuck.tileentity;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.alchemy.*;
import com.mraof.minestuck.block.BlockCrockerMachine;
import com.mraof.minestuck.block.BlockCrockerMachine.MachineType;
import com.mraof.minestuck.entity.item.EntityGrist;
import com.mraof.minestuck.item.MinestuckItems;
import com.mraof.minestuck.util.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Map.Entry;

public class TileEntityCrockerMachine extends TileEntityMachine
{
	public IdentifierHandler.PlayerIdentifier owner;
	boolean hasItem;
	
	public GristSet getGristWidgetResult()
	{
		ItemStack item = AlchemyRecipes.getDecodedItem(inv.get(0), true);
		GristSet gristSet = GristRegistry.getGristConversion(item);
		if(inv.get(0).getItem() != MinestuckItems.captchaCard || AlchemyRecipes.isPunchedCard(inv.get(0))
				|| item.getItem() == MinestuckItems.captchaCard || gristSet == null)
			return null;
		
		if (item.getCount() != 1)
			gristSet.scaleGrist(item.getCount());
		
		if (item.isItemDamaged())
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
	public boolean isAutomatic()
	{
		return false;
	}

	@Override
	public boolean allowOverrideStop()
	{
		return getMachineType() == MachineType.GRIST_WIDGET;
	}

	@Override
	public int getSizeInventory()
	{
		switch (getMachineType())
		{
			case GRIST_WIDGET:
				return 1;
			default:
				return 0;
		}
	}
	
	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack)
	{
		switch (getMachineType())
		{
			case GRIST_WIDGET:
				if(i!=0 || itemstack.getItem() != MinestuckItems.captchaCard)
				{
					return false;
				}
				else
				{
					return (!itemstack.getTagCompound().getBoolean("punched") && itemstack.getTagCompound().getInteger("contentSize") > 0
					&& AlchemyRecipes.getDecodedItem(itemstack).getItem() != MinestuckItems.captchaCard);
				}
			default:
				return true;
		}
	}
	
	@Override
	public void update()
	{
		super.update();
		switch(getMachineType())
		{
			case GRIST_WIDGET:
				boolean item = this.getStackInSlot(0).getCount() == 0;
				if(item != hasItem)
				{
					hasItem = item;
					resendState();
				}
				break;
		}
	}
	
	@Override
	public boolean contentsValid()
	{
		switch (getMachineType())
		{
			case GRIST_WIDGET:
				if (MinestuckConfig.disableGristWidget)
					return false;
				if (world.isBlockPowered(this.getPos()))
					return false;
				int i = getGristWidgetBoondollarValue();
				return owner != null && i != 0 && i <= MinestuckPlayerData.getData(owner).boondollars;
		}
		return false;
	}

	@Override
	public void processContents()
	{
		switch (getMachineType())
		{
			case GRIST_WIDGET:
				GristSet gristSet = getGristWidgetResult();
				
				if(!MinestuckPlayerData.addBoondollars(owner, -getGristWidgetBoondollarValue()))
				{
					Debug.warnf("Failed to remove boondollars for a grist widget from %s's porkhollow", owner.getUsername());
					return;
				}
				
				for (Entry<GristType, Integer> entry : gristSet.getMap().entrySet())
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
				break;
		}
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tagCompound)
	{
		super.readFromNBT(tagCompound);
		
		if(IdentifierHandler.hasIdentifier(tagCompound, "owner"))
			owner = IdentifierHandler.load(tagCompound, "owner");
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tagCompound)
	{
		super.writeToNBT(tagCompound);
		
		if(getMachineType() == MachineType.GRIST_WIDGET && owner != null)
			owner.saveToNBT(tagCompound, "owner");
		
		return tagCompound;
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
	public String getName()
	{
		return "tile.crockerMachine." + getMachineType().getUnlocalizedName() + ".name";
	}
	
	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate)
	{
		return oldState.getBlock() != newSate.getBlock() || oldState.getValue(BlockCrockerMachine.MACHINE_TYPE) != newSate.getValue(BlockCrockerMachine.MACHINE_TYPE);
	}
	
	public MachineType getMachineType()
	{
		return MachineType.values()[getBlockMetadata() % 4];
	}
	
	public void resendState()
	{
		if(hasItem)
		{
			BlockCrockerMachine.updateItem(false, world, this.getPos());
		} else
		{
			BlockCrockerMachine.updateItem(true, world, this.getPos());
		}
	}
}
