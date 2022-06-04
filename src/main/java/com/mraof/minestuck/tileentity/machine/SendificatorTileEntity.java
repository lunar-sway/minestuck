package com.mraof.minestuck.tileentity.machine;

import com.mraof.minestuck.inventory.SendificatorContainer;
import com.mraof.minestuck.tileentity.MSTileEntityTypes;
import com.mraof.minestuck.util.ExtraForgeTags;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RangedWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SendificatorTileEntity extends MachineProcessTileEntity implements INamedContainerProvider
{
	public static final RunType TYPE = RunType.BUTTON_OVERRIDE;
	public static final String TITLE = "container.minestuck.sendificator";
	public static final int DEFAULT_MAX_PROGRESS = 0;
	public static final int FUEL_COST = 32;
	
	private BlockPos destBlockPos;
	
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
	
	public SendificatorTileEntity()
	{
		super(MSTileEntityTypes.SENDIFICATOR.get());
		maxProgress = DEFAULT_MAX_PROGRESS;
	}
	
	public BlockPos getDestinationBlockPos()
	{
		return destBlockPos;
	}
	
	public void setDestinationBlockPos(BlockPos destinationPosIn)
	{
		this.destBlockPos = destinationPosIn;
	}
	
	@Override
	public ITextComponent getDisplayName()
	{
		return new TranslationTextComponent(TITLE);
	}
	
	@Override
	public void load(BlockState state, CompoundNBT compound)
	{
		super.load(state, compound);

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
	public CompoundNBT save(CompoundNBT compound)
	{
		super.save(compound);
		
		if(destBlockPos != null)
		{
			compound.putInt("destX", destBlockPos.getX());
			compound.putInt("destY", destBlockPos.getY());
			compound.putInt("destZ", destBlockPos.getZ());
		}
		
		compound.putShort("fuel", fuel);
		
		return compound;
	}
	
	@Override
	protected ItemStackHandler createItemHandler()
	{
		return new MachineProcessTileEntity.CustomHandler(2, (index, stack) -> index == 1 ? ExtraForgeTags.Items.URANIUM_CHUNKS.contains(stack.getItem()) : index != 2);
	}
	
	@Override
	public MachineProcessTileEntity.RunType getRunType()
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
		return getFuel() <= getMaxFuel() - FUEL_COST && ExtraForgeTags.Items.URANIUM_CHUNKS.contains(fuel.getItem()) || !input.isEmpty();
	}
	
	/**
	 * With the given container possessing tile entity system our mod uses, this is the function that connects to the GoButton found in it's screen(SendificatorScreen in this example)
	 */
	@Override
	public void processContents()
	{
		if(getFuel() <= getMaxFuel() - FUEL_COST)
		{
			//checks for a uranium itemstack in the lower(fuel) item slot, increases the fuel value if some is found and then removes one count from the fuel stack
			if(ExtraForgeTags.Items.URANIUM_CHUNKS.contains(itemHandler.getStackInSlot(1).getItem()))
			{
				//Refill fuel
				fuel += FUEL_COST;
				itemHandler.extractItem(1, 1, false);
			}
		}
		
		if(canSend())
		{
			if(itemHandler.getStackInSlot(0).hasContainerItem())
			{
				itemHandler.setStackInSlot(0, itemHandler.getStackInSlot(0).getContainerItem());
			} else
			{
				if(level != null)
				{
					BlockPos destinationPos = getDestinationBlockPos();
					if(destinationPos != null)
					{
						ItemStack mimicStack = itemHandler.getStackInSlot(0).copy();
						ItemEntity itemEntity = new ItemEntity(level, destinationPos.getX(), destinationPos.getY(), destinationPos.getZ(), mimicStack);
						level.addFreshEntity(itemEntity);
						itemHandler.extractItem(0, itemHandler.getStackInSlot(0).getCount(), false); //sends the whole stack at once

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
	
	private final LazyOptional<IItemHandler> upHandler = LazyOptional.of(() -> new RangedWrapper(itemHandler, 0, 1)); //sendificated item slot
	private final LazyOptional<IItemHandler> downHandler = LazyOptional.of(() -> new RangedWrapper(itemHandler, 1, 3)); //uranium fuel slot
	
	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side)
	{
		if(cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && side != null)
		{
			return side == Direction.DOWN ? downHandler.cast() : upHandler.cast();
		}
		return super.getCapability(cap, side);
	}
	
	@Nullable
	@Override
	public Container createMenu(int windowId, PlayerInventory playerInventory, PlayerEntity player)
	{
		return new SendificatorContainer(windowId, playerInventory, itemHandler, parameters, fuelHolder, IWorldPosCallable.create(level, worldPosition), worldPosition);
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
	
	@Override
	public CompoundNBT getUpdateTag()
	{
		return this.save(new CompoundNBT());
	}
	
	@Override
	public SUpdateTileEntityPacket getUpdatePacket()
	{
		return new SUpdateTileEntityPacket(getBlockPos(), 2, getUpdateTag());
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt)
	{
		this.load(getBlockState(), pkt.getTag());
	}
}