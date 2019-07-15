package com.mraof.minestuck.tileentity;

import com.mraof.minestuck.inventory.UraniumCookerContainer;
import com.mraof.minestuck.item.MinestuckItems;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.IntReferenceHolder;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class UraniumCookerTileEntity extends MachineProcessTileEntity implements INamedContainerProvider
{
	private static HashMap<Item, ItemStack> radiations = new HashMap<>();
	
	public static final RunType TYPE = RunType.BUTTON_OVERRIDE;
	public static final int DEFAULT_MAX_PROGRESS = 0;
	
	private final IntReferenceHolder fuelHolder = new IntReferenceHolder()
	{
		@Override
		public int get()
		{
			return fuel;
		}
		
		@Override
		public void set(int value)
		{
			fuel = (short) value;
		}
	};
	
	private short fuel = 0;
	private static final short maxFuel = 128;
	
	public UraniumCookerTileEntity()
	{
		super(ModTileEntityTypes.URANIUM_COOKER);
		maxProgress = DEFAULT_MAX_PROGRESS;
	}
	
	@Override
	public CompoundNBT write(CompoundNBT compound)
	{
		super.write(compound);
		compound.putShort("fuel", fuel);
		return compound;
	}
	
	@Override
	public void read(CompoundNBT compound)
	{
		super.read(compound);
		fuel = compound.getShort("fuel");
	}
	
	@Override
	public RunType getRunType()
	{
		return TYPE;
	}
	
	@Override
	public int getSizeInventory()
	{
		return 3;
	}
	
	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack)
	{
		if(i == 0 && itemstack.getItem() != MinestuckItems.RAW_URANIUM)
		{
			return false;
		}
		
		return true;
	}
	
	@Override
	public boolean contentsValid()
	{
		if(world.isBlockPowered(this.getPos()))
		{
			return false;
		}
		
		ItemStack inputA = this.inv.get(0);
		ItemStack inputB = this.inv.get(1);
		ItemStack output = irradiate(inputB);
		return (inputA.getItem() == MinestuckItems.RAW_URANIUM && !inputB.isEmpty());
	}
	
	private ItemStack irradiate(ItemStack input)
	{
		if(radiations.containsKey(input.getItem()))
		{
			input = radiations.get(input.getItem());
		} else
		{
			//input = this.world.getRecipeManager().getRecipe(this, this.world, net.minecraftforge.common.crafting.VanillaRecipeTypes.SMELTING).getRecipeOutput();
			//TODO Check the above
		}
		
		return input.copy();
	}
	
	//This is called mostly from AlchemyRecipeHandler
	public static void setRadiation(Item in, ItemStack out)
	{
		radiations.put(in, out);
	}
	
	public static void removeRadiation(Item in)
	{
		radiations.remove(in);
	}

	public static Map getRadiations()
	{
		return radiations;
	}
	
	@Override
	public void processContents()
	{
		ItemStack item = inv.get(1);
		if(getFuel() <= getMaxFuel() - 32 && inv.get(0).getItem() == MinestuckItems.RAW_URANIUM)
		{    //Refill fuel
			fuel += 32;
			this.decrStackSize(0, 1);
		}
		if(canIrradiate())
		{
			ItemStack output = irradiate(this.getStackInSlot(1));
			if(inv.get(2).isEmpty() && fuel > 0)
			{
				this.setInventorySlotContents(2, output);
			} else
			{
				this.getStackInSlot(2).grow(output.getCount());
			}
			if(this.getStackInSlot(1).getItem() == Items.MUSHROOM_STEW)
			{
				this.setInventorySlotContents(1, new ItemStack(Items.BOWL));
			} else
			{
				this.decrStackSize(1, 1);
			}
			fuel--;
		}
	}
	
	private boolean canIrradiate()
	{
		ItemStack output = irradiate(inv.get(1));
		if(fuel > 0 && !inv.get(1).isEmpty() && !output.isEmpty())
		{
			ItemStack out = inv.get(2);
			if(out.isEmpty())
			{
				return true;
			}
			else if(out.getMaxStackSize() >= output.getCount() + out.getCount() && out.isItemEqual(output))
			{
				return true;
			}
		}
		return false;
	}
	
	@Override
	public int[] getSlotsForFace(Direction side)
	{
		if(side == Direction.UP)
			return new int[] {1};
		if(side == Direction.DOWN)
			return new int[] {2};
		else return new int[] {0};
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
	
	@Nullable
	@Override
	public Container createMenu(int windowId, PlayerInventory playerInventory, PlayerEntity player)
	{
		return new UraniumCookerContainer(windowId, playerInventory, this, parameters, fuelHolder);
	}
	
	@Override
	public ITextComponent getDisplayName()
	{
		return new TranslationTextComponent("container.uranium_cooker");
	}
	
	public short getFuel()
	{
		return fuel;
	}

	public void setFuel(short fuel)
	{
		this.fuel = fuel;
	}

	public static short getMaxFuel()
	{
		return maxFuel;
	}
}
