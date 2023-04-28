package com.mraof.minestuck.blockentity.machine;

import com.mraof.minestuck.blockentity.MSBlockEntityTypes;
import com.mraof.minestuck.inventory.*;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.util.ExtraForgeTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RangedWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class AnthvilBlockEntity extends MachineProcessBlockEntity implements MenuProvider
{
	public static final String TITLE = "container.minestuck.anthvil";
	public static final short MAX_FUEL = 128;
	public static final short MAX_CRUXITE = 128;
	
	private final ProgressTracker progressTracker = new ProgressTracker(ProgressTracker.RunType.ONCE, 0, this::setChanged, this::contentsValid);
	private short fuel = 0;
	private short cruxite = 0;
	
	private final DataSlot fuelHolder = new DataSlot()
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
	
	private final DataSlot cruxiteHolder = new DataSlot()
	{
		@Override
		public int get()
		{
			return cruxite;
		}
		
		@Override
		public void set(int value)
		{
			cruxite = (short) value;
		}
	};
	
	public AnthvilBlockEntity(BlockPos pos, BlockState state)
	{
		super(MSBlockEntityTypes.ANTHVIL.get(), pos, state);
	}
	
	@Override
	public Component getDisplayName()
	{
		return Component.translatable(TITLE);
	}
	
	@Override
	public void load(CompoundTag compound)
	{
		super.load(compound);
		
		this.progressTracker.load(compound);
		
		fuel = compound.getShort("fuel");
		cruxite = compound.getShort("cruxite");
	}
	
	@Override
	public void saveAdditional(CompoundTag compound)
	{
		super.saveAdditional(compound);
		
		this.progressTracker.save(compound);
		
		compound.putShort("fuel", fuel);
		compound.putShort("cruxite", cruxite);
	}
	
	@Override
	protected ItemStackHandler createItemHandler()
	{
		return new MachineProcessBlockEntity.CustomHandler(3, (index, stack) -> index == 0 || stack.is(ExtraForgeTags.Items.URANIUM_CHUNKS) || stack.is(MSItems.RAW_CRUXITE.get()));
	}
	
	@Override
	protected void tick()
	{
		this.progressTracker.tick(this::processContents);
	}
	
	private boolean contentsValid()
	{
		if(level.hasNeighborSignal(this.getBlockPos()))
		{
			return false;
		}
		
		ItemStack cruxite = itemHandler.getStackInSlot(2);
		ItemStack fuel = itemHandler.getStackInSlot(1);
		ItemStack input = itemHandler.getStackInSlot(0);
		
		boolean goodOnFuel = canBeRefueled() && fuel.is(ExtraForgeTags.Items.URANIUM_CHUNKS);
		boolean goodOnCruxite = cruxiteCanBeRestocked() && cruxite.is(MSItems.RAW_CRUXITE.get());
		return (goodOnFuel && goodOnCruxite) || !input.isEmpty();
	}
	
	/**
	 * With the given container possessing block entity system our mod uses, this is the function that connects to the GoButton found in it's screen({@link com.mraof.minestuck.client.gui.AnthvilScreen} in this example)
	 */
	private void processContents()
	{
		if(canBeRefueled())
		{
			//checks for a uranium itemstack in the right item slot, increases the fuel value if some is found and then removes one count from the fuel stack
			if(itemHandler.getStackInSlot(1).is(ExtraForgeTags.Items.URANIUM_CHUNKS))
			{
				//Refill fuel
				fuel += FUEL_INCREASE;
				itemHandler.extractItem(1, 1, false);
			}
		}
		
		if(cruxiteCanBeRestocked())
		{
			//checks for a cruxite itemstack in the middle item slot, increases the cruxite value if some is found and then removes one count from the cruxite stack
			if(itemHandler.getStackInSlot(2).is(MSItems.RAW_CRUXITE.get()))
			{
				//Refill cruxite
				cruxite += 1;
				itemHandler.extractItem(2, 1, false);
			}
		}
		
		if(canMend())
		{
			ItemStack slotStack = itemHandler.getStackInSlot(0);
			if(slotStack.hasCraftingRemainingItem())
			{
				itemHandler.setStackInSlot(0, slotStack.getCraftingRemainingItem());
			} else if(!slotStack.isEmpty() && slotStack.isDamageableItem() && slotStack.isDamaged())
			{
				slotStack.setDamageValue(slotStack.getDamageValue() - 10);
				
				fuel = (short) (fuel - 1);
				cruxite = (short) (cruxite - 1);
			}
		}
	}
	
	/**
	 * Checks that there is enough fuel energy/cruxite for the machine to work and that there is something to mend
	 */
	private boolean canMend()
	{
		return fuel > 0 && cruxite > 0 && !itemHandler.getStackInSlot(0).isEmpty();
	}
	
	/**
	 * Checks that fuel can be added without any excess/wasted points being attributed
	 */
	public boolean canBeRefueled()
	{
		return fuel <= MAX_FUEL - FUEL_INCREASE;
	}
	
	/**
	 * Checks that cruxite can be added without any excess/wasted points being attributed
	 */
	public boolean cruxiteCanBeRestocked()
	{
		return cruxite <= MAX_CRUXITE - 1;
	}
	
	private final LazyOptional<IItemHandler> inputHandler = LazyOptional.of(() -> new RangedWrapper(itemHandler, 0, 1)); //regenerating item slot
	private final LazyOptional<IItemHandler> fuelHandler = LazyOptional.of(() -> new RangedWrapper(itemHandler, 1, 2)); //uranium fuel slot
	private final LazyOptional<IItemHandler> cruxiteHandler = LazyOptional.of(() -> new RangedWrapper(itemHandler, 2, 3)); //cruxite slot
	
	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side)
	{
		if(cap == ForgeCapabilities.ITEM_HANDLER && side != null)
		{
			if(side == Direction.UP)
				return inputHandler.cast();
			else if(side == Direction.DOWN)
				return LazyOptional.empty();
			else if(side == Direction.SOUTH)
				return cruxiteHandler.cast(); //will fill the anthvil with cruxite if fed from behind
			else
				return fuelHandler.cast(); //will fill the anthvil with fuel if fed from the sides (just not the back)
		}
		return super.getCapability(cap, side);
	}
	
	@Nullable
	@Override
	public AbstractContainerMenu createMenu(int windowId, Inventory playerInventory, Player player)
	{
		return new AnthvilMenu(windowId, playerInventory, itemHandler,
				this.progressTracker, fuelHolder, cruxiteHolder,
				ContainerLevelAccess.create(level, worldPosition), worldPosition);
	}
}