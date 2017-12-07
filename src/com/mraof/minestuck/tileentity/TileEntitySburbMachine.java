package com.mraof.minestuck.tileentity;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.block.BlockSburbMachine.MachineType;
import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.item.MinestuckItems;
import com.mraof.minestuck.tracker.MinestuckPlayerTracker;
import com.mraof.minestuck.util.*;
import com.mraof.minestuck.util.IdentifierHandler.PlayerIdentifier;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class TileEntitySburbMachine extends TileEntityMachine
{
	public PlayerIdentifier owner;
	public GristType selectedGrist = GristType.Build;
	public int color = -1;
	private int ticks_since_update = 0;

	@Override
	public boolean isAutomatic()
	{
		return getMachineType() == MachineType.CRUXTRUDER;
	}

	@Override
	public boolean allowOverrideStop()
	{
		return getMachineType() == MachineType.ALCHEMITER;
	}

	@Override
	public int getSizeInventory()
	{
		switch (getMachineType())
		{
			case CRUXTRUDER:
				return 2;
			case PUNCH_DESIGNIX:
				return 3;
			case TOTEM_LATHE:
				return 4;
			case ALCHEMITER:
				return 2;
		}
		return 0;
	}

	@Override
	public void readFromNBT(NBTTagCompound tagCompound)
	{
		super.readFromNBT(tagCompound);

		if (tagCompound.hasKey("gristType"))
		{
			this.selectedGrist = GristType.getTypeFromString(tagCompound.getString("gristType"));
			if (this.selectedGrist == null)
			{
				this.selectedGrist = GristType.Build;
			}
		}

		if (tagCompound.hasKey("color"))
			this.color = tagCompound.getInteger("color");

		if (tagCompound.hasKey("owner") || tagCompound.hasKey("ownerMost"))
			owner = IdentifierHandler.load(tagCompound, "owner");
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tagCompound)
	{
		super.writeToNBT(tagCompound);

		if (getMachineType() == MachineType.ALCHEMITER)
			tagCompound.setInteger("gristType", selectedGrist.getId());

		if (getMachineType() == MachineType.CRUXTRUDER)
			tagCompound.setInteger("color", color);

		if (getMachineType() == MachineType.ALCHEMITER && owner != null)
			owner.saveToNBT(tagCompound, "owner");
		return tagCompound;
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack)
	{
		switch (getMachineType())
		{
			case CRUXTRUDER:
				return i == 0 ? itemstack.getItem() == MinestuckItems.rawCruxite : false;
			case PUNCH_DESIGNIX:
				return i == 1 ? itemstack.getItem() == MinestuckItems.captchaCard : i == 0;
			case TOTEM_LATHE:
				return i == 0 || i == 1 ? itemstack.getItem() == MinestuckItems.captchaCard : i == 2 ? itemstack.getItem() == MinestuckItems.cruxiteDowel : false;
			case ALCHEMITER:
				return i == 0 ? itemstack.getItem() == MinestuckItems.cruxiteDowel : false;
		}
		return true;
	}

	@Override
	public boolean contentsValid()
	{
		switch (getMachineType())
		{
			case CRUXTRUDER:
				ItemStack stack1 = this.inv.get(1);
				return (!world.isBlockPowered(this.getPos()) && !this.inv.get(0).isEmpty() && (stack1.isEmpty() || stack1.getCount() < stack1.getMaxStackSize() && stack1.getItemDamage() == this.color + 1));
			case PUNCH_DESIGNIX:
				if (!this.inv.get(0).isEmpty() && !inv.get(1).isEmpty())
				{
					ItemStack output = AlchemyRecipeHandler.getDecodedItemDesignix(inv.get(0));
					if (inv.get(1).hasTagCompound() && inv.get(1).getTagCompound().getBoolean("punched"))
					{
						output = CombinationRegistry.getCombination(output,
								AlchemyRecipeHandler.getDecodedItem(inv.get(1)), CombinationRegistry.MODE_OR);
					}
					if (output.isEmpty())
						return false;
					if (output.getItem().isDamageable())
						output.setItemDamage(0);
					output = AlchemyRecipeHandler.createCard(output, true);
					return (inv.get(2).isEmpty() || inv.get(2).getCount() < 16 && ItemStack.areItemStackTagsEqual(inv.get(2), output));
				}
				else
				{
					return false;
				}
			case TOTEM_LATHE:
				if ((!inv.get(0).isEmpty() || !inv.get(1).isEmpty()) && !inv.get(2).isEmpty() && !(inv.get(2).hasTagCompound() && inv.get(2).getTagCompound().hasKey("contentID"))
						&& (inv.get(3).isEmpty() || inv.get(3).getCount() < inv.get(3).getMaxStackSize() && inv.get(3).getItemDamage() == inv.get(2).getItemDamage()))
				{
					if (!inv.get(0).isEmpty() && !inv.get(1).isEmpty())
					{
						if (!inv.get(0).hasTagCompound() || !inv.get(0).getTagCompound().getBoolean("punched") || !inv.get(1).hasTagCompound() || !inv.get(1).getTagCompound().getBoolean("punched"))
							return inv.get(3).isEmpty() || !(inv.get(3).hasTagCompound() && inv.get(3).getTagCompound().hasKey("contentID"));
						else
						{
							ItemStack output = CombinationRegistry.getCombination(AlchemyRecipeHandler.getDecodedItem(inv.get(0)), AlchemyRecipeHandler.getDecodedItem(inv.get(1)), CombinationRegistry.MODE_AND);
							return !output.isEmpty() && (inv.get(3).isEmpty() || AlchemyRecipeHandler.getDecodedItem(inv.get(3)).isItemEqual(output));
						}
					}
					else
					{
						ItemStack input = inv.get(0).isEmpty() ? inv.get(1) : inv.get(0);
						return (inv.get(3).isEmpty() || (AlchemyRecipeHandler.getDecodedItem(inv.get(3)).isItemEqual(AlchemyRecipeHandler.getDecodedItem(input))
								|| !(input.hasTagCompound() && input.getTagCompound().getBoolean("punched")) && !(inv.get(3).hasTagCompound() && inv.get(3).getTagCompound().hasKey("contentID"))));
					}
				}
				else return false;
			case ALCHEMITER:
				if (!world.isBlockPowered(this.getPos()) && !this.inv.get(0).isEmpty() && this.owner != null)
				{
					//Check owner's cache: Do they have everything they need?
					ItemStack newItem = AlchemyRecipeHandler.getDecodedItem(this.inv.get(0));
					if (newItem.isEmpty())
						if (!inv.get(0).hasTagCompound() || !inv.get(0).getTagCompound().hasKey("contentID"))
							newItem = new ItemStack(MinestuckBlocks.genericObject);
						else return false;
					if (!inv.get(1).isEmpty() && (inv.get(1).getItem() != newItem.getItem() || inv.get(1).getItemDamage() != newItem.getItemDamage() || inv.get(1).getMaxStackSize() <= inv.get(1).getCount()))
					{
						return false;
					}
					GristSet cost = GristRegistry.getGristConversion(newItem);
					if (newItem.getItem() == MinestuckItems.captchaCard)
						cost = new GristSet(selectedGrist, MinestuckConfig.cardCost);
					if (cost != null && newItem.isItemDamaged())
					{
						float multiplier = 1 - newItem.getItem().getDamage(newItem) / ((float) newItem.getMaxDamage());
						for (GristAmount amount : cost.getArray())
						{
							cost.setGrist(amount.getType(), (int) Math.ceil(amount.getAmount() * multiplier));
						}
					}
					return GristHelper.canAfford(MinestuckPlayerData.getGristSet(this.owner), cost);
				}
				else
				{
					return false;
				}
		}
		return false;
	}

	public int comparatorValue()
	{
		switch (getMachineType())
		{
			case CRUXTRUDER:
				break;
			case PUNCH_DESIGNIX:
				break;
			case TOTEM_LATHE:
				break;
			case ALCHEMITER:
				if (getStackInSlot(0) != null && owner != null)
				{
					ItemStack newItem = AlchemyRecipeHandler.getDecodedItem(getStackInSlot(0));
					if (newItem.isEmpty())
						if (!getStackInSlot(0).hasTagCompound() || !getStackInSlot(0).getTagCompound().hasKey("contentID"))
							newItem = new ItemStack(MinestuckBlocks.genericObject);
						else return 0;
					if (!getStackInSlot(1).isEmpty() && (getStackInSlot(1).getItem() != newItem.getItem() || getStackInSlot(1).getItemDamage() != newItem.getItemDamage() || getStackInSlot(1).getMaxStackSize() <= getStackInSlot(1).getCount()))
					{
						return 0;
					}
					GristSet cost = GristRegistry.getGristConversion(newItem);
					if (newItem.getItem() == MinestuckItems.captchaCard)
						cost = new GristSet(selectedGrist, MinestuckConfig.cardCost);
					if (cost != null && newItem.isItemDamaged())
					{
						float multiplier = 1 - newItem.getItem().getDamage(newItem) / ((float) newItem.getMaxDamage());
						for (GristAmount amount : cost.getArray())
						{
							cost.setGrist(amount.getType(), (int) Math.ceil(amount.getAmount() * multiplier));
						}
					}
					// We need to run the check 16 times. Don't want to hammer the game with too many of these, so the comparators are only told to update every 20 ticks.
					// Additionally, we need to check if the item in the slot is empty. Otherwise, it will attempt to check the cost for air, which cannot be alchemized anyway.
					if (cost != null && !getStackInSlot(0).isEmpty())
					{
						GristSet scale_cost;
						for (int lvl = 1; lvl <= 17; lvl++)
						{
							// We went through fifteen item cost checks and could still afford it. No sense in checking more than this.
							if (lvl == 17)
							{
								return 15;
							}
							// We need to make a copy to preserve the original grist amounts and avoid scaling values that have already been scaled. Keeps scaling linear as opposed to exponential.
							scale_cost = cost.copy().scaleGrist(lvl);
							if (!GristHelper.canAfford(MinestuckPlayerData.getGristSet(owner), scale_cost))
							{
								return lvl - 1;
							}
						}
						return 0;
					}
				}
		}
		return 0;
	}

	// We're going to want to trigger a block update every 20 ticks to have comparators pull data from the Alchemeter.
	@Override
	public void update()
	{
		if (world.isRemote)
			return;
		switch (getMachineType())
		{
			case CRUXTRUDER:
				break;
			case PUNCH_DESIGNIX:
				break;
			case TOTEM_LATHE:
				break;
			case ALCHEMITER:
				if (this.ticks_since_update == 20)
				{
					world.updateComparatorOutputLevel(this.getPos(), this.blockType);
					this.ticks_since_update = 0;
				}
				else
				{
					this.ticks_since_update++;
				}
		}
		super.update();
	}

	@Override
	public void processContents()
	{
		switch (getMachineType())
		{
			case CRUXTRUDER:
				// Process the Raw Cruxite

				if (this.inv.get(1).isEmpty())
					setInventorySlotContents(1, new ItemStack(MinestuckItems.cruxiteDowel, 1, color + 1));
				else this.inv.get(1).grow(1);
				decrStackSize(0, 1);

				this.progress++;
				break;
			case PUNCH_DESIGNIX:
				//Create a new card, using CombinationRegistry
				if (!inv.get(2).isEmpty())
				{
					decrStackSize(1, 1);
					if (!(inv.get(0).hasTagCompound() && inv.get(0).getTagCompound().hasKey("contentID")))
						decrStackSize(0, 1);
					this.inv.get(2).grow(1);
					break;
				}

				ItemStack outputItem = AlchemyRecipeHandler.getDecodedItemDesignix(inv.get(0));

				if (inv.get(1).hasTagCompound() && inv.get(1).getTagCompound().getBoolean("punched"))
					outputItem = CombinationRegistry.getCombination(outputItem, AlchemyRecipeHandler.getDecodedItem(inv.get(1)), CombinationRegistry.MODE_OR);
				if (outputItem.getItem().isDamageable())
					outputItem.setItemDamage(0);

				//Create card
				outputItem = AlchemyRecipeHandler.createCard(outputItem, true);

				setInventorySlotContents(2, outputItem);
				if (!(inv.get(0).hasTagCompound() && inv.get(0).getTagCompound().hasKey("contentID")))
					decrStackSize(0, 1);
				decrStackSize(1, 1);
				break;
			case TOTEM_LATHE:
				if (!inv.get(3).isEmpty())
				{
					this.inv.get(3).grow(1);
					decrStackSize(2, 1);
					return;
				}

				ItemStack output;
				if (!inv.get(0).isEmpty() && !inv.get(1).isEmpty())
					if (!inv.get(0).hasTagCompound() || !inv.get(0).getTagCompound().getBoolean("punched") || !inv.get(1).hasTagCompound() || !inv.get(1).getTagCompound().getBoolean("punched"))
						output = new ItemStack(MinestuckBlocks.genericObject);
					else
						output = CombinationRegistry.getCombination(AlchemyRecipeHandler.getDecodedItem(inv.get(0)), AlchemyRecipeHandler.getDecodedItem(inv.get(1)), CombinationRegistry.MODE_AND);
				else
				{
					ItemStack input = inv.get(0).isEmpty() ? inv.get(1) : inv.get(0);
					if (!input.hasTagCompound() || !input.getTagCompound().getBoolean("punched"))
						output = new ItemStack(MinestuckBlocks.genericObject);
					else output = AlchemyRecipeHandler.getDecodedItem(input);
				}

				ItemStack outputDowel = output.getItem().equals(Item.getItemFromBlock(MinestuckBlocks.genericObject))
						? new ItemStack(MinestuckItems.cruxiteDowel) : AlchemyRecipeHandler.createEncodedItem(output, false);
				outputDowel.setItemDamage(inv.get(2).getItemDamage());

				setInventorySlotContents(3, outputDowel);
				decrStackSize(2, 1);
				break;
			case ALCHEMITER:
				ItemStack newItem = AlchemyRecipeHandler.getDecodedItem(this.inv.get(0));

				if (newItem.isEmpty())
					newItem = new ItemStack(MinestuckBlocks.genericObject);

				if (inv.get(1).isEmpty())
				{
					setInventorySlotContents(1, newItem);
				}
				else
				{
					this.inv.get(1).grow(1);
				}

				EntityPlayerMP player = owner.getPlayer();
				if (player != null)
					MinestuckAchievementHandler.onAlchemizedItem(newItem, player);

				GristSet cost = GristRegistry.getGristConversion(newItem);
				if (newItem.getItem() == MinestuckItems.captchaCard)
					cost = new GristSet(selectedGrist, MinestuckConfig.cardCost);
				if (newItem.isItemDamaged())
				{
					float multiplier = 1 - newItem.getItem().getDamage(newItem) / ((float) newItem.getMaxDamage());
					for (GristAmount amount : cost.getArray())
					{
						cost.setGrist(amount.getType(), (int) Math.ceil(amount.getAmount() * multiplier));
					}
				}
				GristHelper.decrease(owner, cost);
				MinestuckPlayerTracker.updateGristCache(owner);
				break;
		}
	}

	@Override
	public void markDirty()
	{
		if (getMachineType() == MachineType.PUNCH_DESIGNIX || getMachineType() == MachineType.TOTEM_LATHE)
		{
			this.progress = 0;
			this.ready = false;
		}
		super.markDirty();
	}

	@Override
	public String getName()
	{
		return "tile.sburbMachine." + getMachineType().getUnlocalizedName() + ".name";
	}

	public MachineType getMachineType()
	{
		return MachineType.values()[getBlockMetadata() % 4];
	}

}
