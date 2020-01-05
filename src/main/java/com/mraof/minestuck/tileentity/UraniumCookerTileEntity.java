package com.mraof.minestuck.tileentity;

import com.mraof.minestuck.inventory.UraniumCookerContainer;
import com.mraof.minestuck.item.crafting.IrradiatingRecipe;
import com.mraof.minestuck.item.crafting.MSRecipeTypes;
import com.mraof.minestuck.util.ExtraForgeTags;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.AbstractCookingRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.IntReferenceHolder;
import net.minecraft.util.Util;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Stream;

public class UraniumCookerTileEntity extends MachineProcessTileEntity implements INamedContainerProvider
{
	public static final String TITLE = "container.minestuck.uranium_cooker";
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
		super(MSTileEntityTypes.URANIUM_COOKER);
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
		if(i == 0)
		{
			return ExtraForgeTags.Items.URANIUM_CHUNKS.contains(itemstack.getItem());
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
		
		ItemStack fuel = this.inv.get(1);
		ItemStack input = this.inv.get(0);
		ItemStack output = irradiate();
		return ExtraForgeTags.Items.URANIUM_CHUNKS.contains(fuel.getItem()) && !input.isEmpty() && !output.isEmpty();
	}
	
	private ItemStack irradiate()	//TODO Handle the recipe and make sure to use its exp/cooking time
	{
		if(world == null)
			return ItemStack.EMPTY;
		
		//List of all recipes that match to the current input
		Stream<IrradiatingRecipe> stream = world.getRecipeManager().getRecipes(MSRecipeTypes.IRRADIATING_TYPE, this, world).stream();
		//Sort the stream to get non-fallback recipes first, and fallback recipes second
		stream = stream.sorted(Comparator.comparingInt(o -> (o.isFallback() ? 1 : 0)));
		//Let the recipe return the recipe actually used (for fallbacks), to clear out all that are not present, and then get the first
		Optional<? extends AbstractCookingRecipe> cookingRecipe = stream.flatMap(recipe -> Util.streamOptional(recipe.getCookingRecipe(this, world))).findFirst();
		
		return cookingRecipe.map(abstractCookingRecipe -> abstractCookingRecipe.getCraftingResult(this)).orElse(ItemStack.EMPTY);
	}
	
	@Override
	public void processContents()
	{
		if(getFuel() <= getMaxFuel() - 32 && ExtraForgeTags.Items.URANIUM_CHUNKS.contains(inv.get(0).getItem()))
		{    //Refill fuel
			fuel += 32;
			this.decrStackSize(1, 1);
		}
		if(canIrradiate())
		{
			ItemStack output = irradiate();
			if(inv.get(2).isEmpty() && fuel > 0)
			{
				this.setInventorySlotContents(2, output);
			} else
			{
				this.getStackInSlot(2).grow(output.getCount());
			}
			if(this.getStackInSlot(0).hasContainerItem())
			{
				this.setInventorySlotContents(0, getStackInSlot(0).getContainerItem());
			} else
			{
				this.decrStackSize(0, 1);
			}
			fuel--;
		}
	}
	
	private boolean canIrradiate()
	{
		ItemStack output = irradiate();
		if(fuel > 0 && !inv.get(0).isEmpty() && !output.isEmpty())
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
			return new int[] {0};
		if(side == Direction.DOWN)
			return new int[] {2};
		else return new int[] {1};
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
		return new UraniumCookerContainer(windowId, playerInventory, this, parameters, fuelHolder, pos);
	}
	
	@Override
	public ITextComponent getDisplayName()
	{
		return new TranslationTextComponent(TITLE);
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