package com.mraof.minestuck.blockentity.machine;

import com.mraof.minestuck.api.uranium.IUraniumHandler;
import com.mraof.minestuck.api.uranium.SimpleUraniumHandler;
import com.mraof.minestuck.api.uranium.UraniumPower;
import com.mraof.minestuck.blockentity.MSBlockEntityTypes;
import com.mraof.minestuck.inventory.OptionalPosHolder;
import com.mraof.minestuck.inventory.SendificatorMenu;
import com.mraof.minestuck.util.ExtraModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.wrapper.RangedWrapper;

import javax.annotation.Nullable;
import java.util.Optional;

public class SendificatorBlockEntity extends MachineProcessBlockEntity implements MenuProvider
{
	public static final String TITLE = "container.minestuck.sendificator";
	public static final int MAX_FUEL = 128;
	
	private final ProgressTracker progressTracker = new ProgressTracker(ProgressTracker.RunType.ONCE_OR_LOOPING, 0, this::setChanged, this::contentsValid);
	
	private int fuel;
	private final IUraniumHandler uraniumHandler = new SimpleUraniumHandler(() -> MAX_FUEL, () -> this.fuel, fuel -> this.fuel = fuel)
	{
		public boolean canExtractUranium()
		{
			return false;
		}
	};
	
	@Nullable
	private BlockPos destBlockPos;
	
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
			fuel = value;
		}
	};
	private final OptionalPosHolder destinationHolder = OptionalPosHolder.forPos(() -> Optional.ofNullable(this.getDestinationBlockPos()));
	
	public SendificatorBlockEntity(BlockPos pos, BlockState state)
	{
		super(MSBlockEntityTypes.SENDIFICATOR.get(), pos, state);
	}
	
	@Nullable
	public BlockPos getDestinationBlockPos()
	{
		return destBlockPos;
	}
	
	public void setDestinationBlockPos(BlockPos destinationPosIn)
	{
		this.destBlockPos = destinationPosIn;
	}
	
	@Override
	public Component getDisplayName()
	{
		return Component.translatable(TITLE);
	}
	
	@Override
	public void loadAdditional(CompoundTag compound, HolderLookup.Provider provider)
	{
		super.loadAdditional(compound, provider);
		
		this.progressTracker.load(compound);
		
		if(compound.contains("destX") && compound.contains("destY") && compound.contains("destZ"))
		{
			int destX = compound.getInt("destX");
			int destY = compound.getInt("destY");
			int destZ = compound.getInt("destZ");
			this.destBlockPos = new BlockPos(destX, destY, destZ);
		}
		
		if (compound.contains("fuel", 2))
			fuel = compound.getShort("fuel");
		else
			fuel = compound.getInt("fuel");
	}
	
	@Override
	public void saveAdditional(CompoundTag compound, HolderLookup.Provider provider)
	{
		super.saveAdditional(compound, provider);
		
		this.progressTracker.save(compound);
		
		if(destBlockPos != null)
		{
			compound.putInt("destX", destBlockPos.getX());
			compound.putInt("destY", destBlockPos.getY());
			compound.putInt("destZ", destBlockPos.getZ());
		}
		
		compound.putInt("fuel", fuel);
	}
	
	@Override
	protected ItemStackHandler createItemHandler()
	{
		return new MachineProcessBlockEntity.CustomHandler(2, (index, stack) -> index == 0 || stack.is(ExtraModTags.Items.URANIUM_CHUNKS));
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
		
		ItemStack fuel = itemHandler.getStackInSlot(1);
		ItemStack input = itemHandler.getStackInSlot(0);
		return canBeRefueled(fuel) || !input.isEmpty();
	}
	
	/**
	 * With the given container possessing block entity system our mod uses, this is the function that connects to the GoButton found in it's screen({@link com.mraof.minestuck.client.gui.SendificatorScreen} in this example)
	 */
	private void processContents()
	{
		ItemStack fuel = itemHandler.getStackInSlot(1);
		if(canBeRefueled(fuel))
		{
			//Refill fuel
			addFuel(fuel);
			ItemStack taken = itemHandler.extractItem(1, 1, false);
			ItemStack remainder = taken.getCraftingRemainingItem();
			if(!remainder.isEmpty() && level != null)
			{
				ItemEntity remainderEntity = new ItemEntity(level, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), remainder);
				level.addFreshEntity(remainderEntity);
			}
		}
		
		if(canSend())
		{
			if(itemHandler.getStackInSlot(0).hasCraftingRemainingItem())
			{
				itemHandler.setStackInSlot(0, itemHandler.getStackInSlot(0).getCraftingRemainingItem());
			} else
			{
				if(level != null)
				{
					BlockPos destinationPos = getDestinationBlockPos();
					if(destinationPos != null)
					{
						ItemStack sentStack = itemHandler.extractItem(0, 64, false);
						ItemEntity itemEntity = new ItemEntity(level, destinationPos.getX(), destinationPos.getY(), destinationPos.getZ(), sentStack);
						level.addFreshEntity(itemEntity);
						
						this.fuel -= 8;
					}
				}
			}
		}
	}
	
	/**
	 * Checks that there is enough fuel energy for the machine to work and that there is something to sendificate
	 */
	private boolean canSend()
	{
		return fuel > 0 && !itemHandler.getStackInSlot(0).isEmpty();
	}
	
	/**
	 * Checks that fuel can be added without any excess/wasted points being attributed
	 */
	public boolean canBeRefueled(ItemStack fuelStack)
	{
		int amount = UraniumPower.getUraniumPower(fuelStack);
		return fuel + amount <= MAX_FUEL;
	}
	
	public void addFuel(ItemStack fuelStack)
	{
		int amount = UraniumPower.getUraniumPower(fuelStack);
		fuel += amount;
	}
	
	@Nullable
	public IItemHandler getItemHandler(@Nullable Direction side)
	{
		if(side == null)
			return this.itemHandler;
		
		if(side == Direction.UP)
			return new RangedWrapper(this.itemHandler, 0, 1);
		if(side == Direction.DOWN)
			return null;
		return new RangedWrapper(this.itemHandler, 1, 2);
	}
	
	@Nullable
	public IUraniumHandler getUraniumHandler(@Nullable Direction side)
	{
		return uraniumHandler;
	}
	
	public void openMenu(ServerPlayer player)
	{
		player.openMenu(this, SendificatorMenu.makeExtraDataWriter(this.worldPosition, this.destBlockPos));
	}
	
	@Nullable
	@Override
	public AbstractContainerMenu createMenu(int windowId, Inventory playerInventory, Player player)
	{
		return new SendificatorMenu(windowId, playerInventory, itemHandler,
				this.progressTracker, fuelHolder, destinationHolder,
				ContainerLevelAccess.create(level, worldPosition), worldPosition);
	}
}
