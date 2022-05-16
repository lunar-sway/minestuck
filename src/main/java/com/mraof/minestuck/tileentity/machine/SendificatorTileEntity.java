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
		if(destBlockPos == null)
			destBlockPos = new BlockPos(0, 0, 0);
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
		
		int destX = compound.getInt("destX");
		int destY = compound.getInt("destY");
		int destZ = compound.getInt("destZ");
		this.destBlockPos = new BlockPos(destX, destY, destZ);
		
		fuel = compound.getShort("fuel");
	}
	
	@Override
	public CompoundNBT save(CompoundNBT compound)
	{
		super.save(compound);
		
		getDestinationBlockPos();
		compound.putInt("destX", destBlockPos.getX());
		compound.putInt("destY", destBlockPos.getY());
		compound.putInt("destZ", destBlockPos.getZ());
		
		compound.putShort("fuel", fuel);
		
		return compound;
	}
	
	@Override
	protected ItemStackHandler createItemHandler()
	{
		return new MachineProcessTileEntity.CustomHandler(3, (index, stack) -> index == 1 ? ExtraForgeTags.Items.URANIUM_CHUNKS.contains(stack.getItem()) : index != 2);
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
		return getFuel() <= getMaxFuel() - 32 && ExtraForgeTags.Items.URANIUM_CHUNKS.contains(fuel.getItem()) || !input.isEmpty();
	}
	
	@Override
	public void processContents()
	{
		if(getFuel() <= getMaxFuel() - 32)
		{
			if(ExtraForgeTags.Items.URANIUM_CHUNKS.contains(itemHandler.getStackInSlot(1).getItem()))
			{
				//Refill fuel
				fuel += 32;
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
					ItemStack mimicStack = itemHandler.getStackInSlot(0).copy();
					mimicStack.setCount(1);
					ItemEntity itemEntity = new ItemEntity(level, destinationPos.getX(), destinationPos.getY(), destinationPos.getZ(), mimicStack); //use if needed: Teleport.teleportEntity
					level.addFreshEntity(itemEntity);
					itemHandler.extractItem(0, 1, false);
					
					fuel = (short) (fuel - 8);
				}
			}
		}
	}
	
	private boolean canSend()
	{
		return fuel > 0 && !itemHandler.getStackInSlot(0).isEmpty();
	}
	
	private final LazyOptional<IItemHandler> upHandler = LazyOptional.of(() -> new RangedWrapper(itemHandler, 0, 1));
	private final LazyOptional<IItemHandler> downHandler = LazyOptional.of(() -> new RangedWrapper(itemHandler, 2, 3));
	
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