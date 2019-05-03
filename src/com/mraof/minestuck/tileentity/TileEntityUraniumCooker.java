package com.mraof.minestuck.tileentity;

import com.mraof.minestuck.client.gui.GuiHandler;
import com.mraof.minestuck.item.MinestuckItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IInteractionObject;

import java.util.HashMap;
import java.util.Map;

public class TileEntityUraniumCooker extends TileEntityMachineProcess implements IInteractionObject
{
	private static HashMap<Item, ItemStack> radiations = new HashMap<>();
	private short fuel = 0;
	private short maxFuel = 128;
	
	public TileEntityUraniumCooker()
	{
		super(MinestuckTiles.URANIUM_COOKER);
		maxProgress = 0;
	}
	
	@Override
	public NBTTagCompound write(NBTTagCompound compound)
	{
		super.write(compound);
		compound.setShort("fuel", fuel);
		return compound;
	}
	
	@Override
	public void read(NBTTagCompound compound)
	{
		super.read(compound);
		fuel = compound.getShort("fuel");
	}
	
	@Override
	public RunType getRunType()
	{
		return RunType.BUTTON_OVERRIDE;
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
			input = this.world.getRecipeManager().getRecipe(this, this.world, net.minecraftforge.common.crafting.VanillaRecipeTypes.SMELTING).getRecipeOutput();
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
	public int[] getSlotsForFace(EnumFacing side)
	{
		if(side == EnumFacing.UP)
			return new int[] {1};
		if(side == EnumFacing.DOWN)
			return new int[] {2};
		else return new int[] {0};
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
		return new TextComponentTranslation("container.uranium_cooker");
	}
	
	@Override
	public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn)
	{
		return null;
	}
	
	@Override
	public String getGuiID()
	{
		return GuiHandler.URANIUM_COOKER_ID.toString();
	}
	
	public short getFuel()
	{
		return fuel;
	}

	public void setFuel(short fuel)
	{
		this.fuel = fuel;
	}

	public short getMaxFuel()
	{
		return maxFuel;
	}

	public void setMaxFuel(short maxFuel)
	{
		this.maxFuel = maxFuel;
	}
}
