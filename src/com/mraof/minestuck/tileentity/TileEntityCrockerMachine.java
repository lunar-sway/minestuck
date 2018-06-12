package com.mraof.minestuck.tileentity;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.item.ItemStack;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.block.BlockCrockerMachine.MachineType;
import com.mraof.minestuck.entity.item.EntityGrist;
import com.mraof.minestuck.item.MinestuckItems;
import com.mraof.minestuck.util.AlchemyRecipeHandler;
import com.mraof.minestuck.util.GristAmount;
import com.mraof.minestuck.util.GristRegistry;
import com.mraof.minestuck.util.GristSet;
import com.mraof.minestuck.util.GristType;

public class TileEntityCrockerMachine extends TileEntityMachine
{
	
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
		switch (getMachineType()) {
		case GRIST_WIDGET:
			return 1;
		default:
			return 0;
		}
	}
	
	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack)
	{
		return true;
	}
	
	@Override
	public boolean contentsValid()
	{
		switch (getMachineType()) {
		case GRIST_WIDGET:
			if(MinestuckConfig.disableGristWidget)
				return false;
			if(world.isBlockPowered(this.getPos()))
				return false;
			ItemStack input = this.inv.get(0);
			return (input.getItem() == MinestuckItems.captchaCard
					&& GristRegistry.getGristConversion(AlchemyRecipeHandler.getDecodedItem(input)) != null
					&& !input.getTagCompound().getBoolean("punched")
					&& AlchemyRecipeHandler.getDecodedItem(input).getItem() != MinestuckItems.captchaCard);
		}
		return false;
	}
	
	@Override
	public void processContents()
	{
		switch (getMachineType()) {
		case GRIST_WIDGET:
			if(!world.isRemote)
			{
				ItemStack item = AlchemyRecipeHandler.getDecodedItem(inv.get(0));
				GristSet gristSet = GristRegistry.getGristConversion(item);
				if(item.getCount() != 1)
					gristSet.scaleGrist(item.getCount());
				
				gristSet.scaleGrist(0.9F);
				
				if(item.isItemDamaged())
				{
					float multiplier = 1 - item.getItem().getDamage(item) / ((float) item.getMaxDamage());
					for(int i = 0; i < gristSet.gristTypes.length; i++)
						gristSet.gristTypes[i] = (int) (gristSet.gristTypes[i] * multiplier);
				}
				
				Iterator<Entry<Integer, Integer>> iter = gristSet.getHashtable().entrySet().iterator();
				while (iter.hasNext())
				{
					Map.Entry<Integer, Integer> entry = (Entry<Integer, Integer>) iter.next();
					int grist = entry.getValue();
					while (true)
					{
						if(grist == 0)
							break;
						GristAmount gristAmount = new GristAmount(GristType.values()[entry.getKey()],
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
				
			}
			this.decrStackSize(0, 1);
			break;
		}
	}
	
	@Override
	public String getName()
	{
		return "tile.crockerMachine." + getMachineType().getUnlocalizedName() + ".name";
	}
	
	public MachineType getMachineType()
	{
		return MachineType.values()[getBlockMetadata() % 4];
	}
}
