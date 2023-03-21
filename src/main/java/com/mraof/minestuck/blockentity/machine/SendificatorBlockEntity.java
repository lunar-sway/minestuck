package com.mraof.minestuck.blockentity.machine;

import com.mraof.minestuck.inventory.OptionalPosHolder;
import com.mraof.minestuck.inventory.SendificatorMenu;
import com.mraof.minestuck.blockentity.MSBlockEntityTypes;
import com.mraof.minestuck.util.ExtraForgeTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RangedWrapper;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

public class SendificatorBlockEntity extends MachineProcessBlockEntity implements MenuProvider
{
	public static final ProgressTracker.RunType TYPE = ProgressTracker.RunType.BUTTON_OVERRIDE;
	public static final String TITLE = "container.minestuck.sendificator";
	public static final short MAX_FUEL = 128;
	private short fuel = 0;
	
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
			fuel = (short) value;
		}
	};
	private final OptionalPosHolder destinationHolder = OptionalPosHolder.forPos(() -> Optional.ofNullable(this.getDestinationBlockPos()));
	
	public SendificatorBlockEntity(BlockPos pos, BlockState state)
	{
		super(MSBlockEntityTypes.SENDIFICATOR.get(), pos, state);
	}
	
	@Override
	protected int getMaxProgress()
	{
		return 0;
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
	public void load(CompoundTag compound)
	{
		super.load(compound);
		
		if(compound.contains("destX") && compound.contains("destY") && compound.contains("destZ"))
		{
			int destX = compound.getInt("destX");
			int destY = compound.getInt("destY");
			int destZ = compound.getInt("destZ");
			this.destBlockPos = new BlockPos(destX, destY, destZ);
		}
		
		fuel = compound.getShort("fuel");
	}
	
	@Override
	public void saveAdditional(CompoundTag compound)
	{
		super.saveAdditional(compound);
		
		if(destBlockPos != null)
		{
			compound.putInt("destX", destBlockPos.getX());
			compound.putInt("destY", destBlockPos.getY());
			compound.putInt("destZ", destBlockPos.getZ());
		}
		
		compound.putShort("fuel", fuel);
	}
	
	@Override
	protected ItemStackHandler createItemHandler()
	{
		return new MachineProcessBlockEntity.CustomHandler(2, (index, stack) -> index == 0 || stack.is(ExtraForgeTags.Items.URANIUM_CHUNKS));
	}
	
	@Override
	public ProgressTracker.RunType getRunType()
	{
		return TYPE;
	}
	
	@Override
	public boolean contentsValid()
	{
		if(level.hasNeighborSignal(this.getBlockPos()))
		{
			return false;
		}
		
		ItemStack fuel = itemHandler.getStackInSlot(1);
		ItemStack input = itemHandler.getStackInSlot(0);
		return canBeRefueled() && fuel.is(ExtraForgeTags.Items.URANIUM_CHUNKS) || !input.isEmpty();
	}
	
	/**
	 * With the given container possessing block entity system our mod uses, this is the function that connects to the GoButton found in it's screen({@link com.mraof.minestuck.client.gui.SendificatorScreen} in this example)
	 */
	@Override
	public void processContents()
	{
		if(canBeRefueled())
		{
			//checks for a uranium itemstack in the lower(fuel) item slot, increases the fuel value if some is found and then removes one count from the fuel stack
			if(itemHandler.getStackInSlot(1).is(ExtraForgeTags.Items.URANIUM_CHUNKS))
			{
				//Refill fuel
				fuel += FUEL_INCREASE;
				itemHandler.extractItem(1, 1, false);
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
						
						fuel = (short) (fuel - 8);
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
	public boolean canBeRefueled()
	{
		return fuel <= MAX_FUEL - FUEL_INCREASE;
	}
	
	private final LazyOptional<IItemHandler> inputHandler = LazyOptional.of(() -> new RangedWrapper(itemHandler, 0, 1)); //sendificated item slot
	private final LazyOptional<IItemHandler> fuelHandler = LazyOptional.of(() -> new RangedWrapper(itemHandler, 1, 2)); //uranium fuel slot
	
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
			else
				return fuelHandler.cast(); //will fill the sendificator with fuel if fed from the sides
		}
		return super.getCapability(cap, side);
	}
	
	public void openMenu(ServerPlayer player)
	{
		NetworkHooks.openScreen(player, this, SendificatorMenu.makeExtraDataWriter(this.worldPosition, this.destBlockPos));
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