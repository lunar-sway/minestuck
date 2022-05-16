package com.mraof.minestuck.tileentity.machine;

import com.mraof.minestuck.inventory.SendificatorContainer;
import com.mraof.minestuck.tileentity.MSTileEntityTypes;
import com.mraof.minestuck.util.ExtraForgeTags;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RangedWrapper;
import net.minecraftforge.items.wrapper.RecipeWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SendificatorTileEntity extends MachineProcessTileEntity implements INamedContainerProvider, INameable
{
	private BlockPos destBlockPos;
	
	public static final RunType TYPE = RunType.BUTTON_OVERRIDE;
	public static final int DEFAULT_MAX_PROGRESS = 0;
	private final IInventory recipeInventory = new RecipeWrapper(itemHandler);
	
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
	
	String id = "";
	
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
	public ITextComponent getName()
	{
		return new StringTextComponent("Sendificator");
	}
	
	@Override
	public ITextComponent getDisplayName()
	{
		//TODO seems to always default to getName even when named, check custom name functionality in Transport
		return hasCustomName() ? getCustomName() : getName(); //using INameable so that people can use the name to describe what the destination is
	}
	
	@Nullable
	@Override
	public ITextComponent getCustomName()
	{
		return id.isEmpty() ? null : new StringTextComponent(id);
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
		
		this.id = compound.getString("id");
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
		
		compound.putString("id", id);
		
		return compound;
	}
	
	@Override
	protected ItemStackHandler createItemHandler()
	{
		return new MachineProcessTileEntity.CustomHandler(3, (index, stack) -> index == 1 ? ExtraForgeTags.Items.URANIUM_CHUNKS.contains(stack.getItem()) : index != 2);
	}
	
	/*@Override
	public CompoundNBT save(CompoundNBT compound)
	{
		super.save(compound);
		compound.putShort("fuel", fuel);
		return compound;
	}
	
	@Override
	public void load(BlockState state, CompoundNBT nbt)
	{
		super.load(state, nbt);
		fuel = nbt.getShort("fuel");
	}*/
	
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
		//ItemStack output = irradiate();
		return getFuel() <= getMaxFuel() - 32 && ExtraForgeTags.Items.URANIUM_CHUNKS.contains(fuel.getItem()) || !input.isEmpty()/* && !output.isEmpty()*/;
	}
	
	/*private ItemStack irradiate()	//TODO Handle the recipe and make sure to use its exp/cooking time
	{
		if(level == null)
			return ItemStack.EMPTY;
		
		//List of all recipes that match to the current input
		Stream<IrradiatingRecipe> stream = level.getRecipeManager().getRecipesFor(MSRecipeTypes.IRRADIATING_TYPE, recipeInventory, level).stream();
		//Sort the stream to get non-fallback recipes first, and fallback recipes second
		stream = stream.sorted(Comparator.comparingInt(o -> (o.isFallback() ? 1 : 0)));
		//Let the recipe return the recipe actually used (for fallbacks), to clear out all that are not present, and then get the first
		Optional<? extends AbstractCookingRecipe> cookingRecipe = stream.flatMap(recipe -> Util.toStream(recipe.getCookingRecipe(recipeInventory, level))).findFirst();
		
		return cookingRecipe.map(abstractCookingRecipe -> abstractCookingRecipe.assemble(recipeInventory)).orElse(ItemStack.EMPTY);
	}*/
	
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
					ItemEntity itemEntity = new ItemEntity(level, destinationPos.getX(), destinationPos.getY() + 0.5, destinationPos.getZ(), mimicStack); //use if needed: Teleport.teleportEntity
					level.addFreshEntity(itemEntity);
					itemHandler.extractItem(0, 1, false);
					
					fuel = (short) (fuel - 8);
				}
			}
		}
		/*if(canIrradiate())
		{
			ItemStack output = irradiate();
			if(itemHandler.getStackInSlot(2).isEmpty() && fuel > 0)
			{
				itemHandler.setStackInSlot(2, output);
			} else
			{
				itemHandler.extractItem(2, -output.getCount(), false);
			}
			if(itemHandler.getStackInSlot(0).hasContainerItem())
			{
				itemHandler.setStackInSlot(0, itemHandler.getStackInSlot(0).getContainerItem());
			} else
			{
				itemHandler.extractItem(0, 1, false);
			}
			fuel--;
		}*/
	}
	
	private boolean canSend()
	{
		return fuel > 0 && !itemHandler.getStackInSlot(0).isEmpty();
	}
	
	/*private boolean canIrradiate()
	{
		ItemStack output = irradiate();
		if(fuel > 0 && !itemHandler.getStackInSlot(0).isEmpty() && !output.isEmpty())
		{
			ItemStack out = itemHandler.getStackInSlot(2);
			if(out.isEmpty())
			{
				return true;
			}
			else if(out.getMaxStackSize() >= output.getCount() + out.getCount() && out.sameItem(output))
			{
				return true;
			}
		}
		return false;
	}*/
	
	private final LazyOptional<IItemHandler> upHandler = LazyOptional.of(() -> new RangedWrapper(itemHandler, 0, 1));
	private final LazyOptional<IItemHandler> downHandler = LazyOptional.of(() -> new RangedWrapper(itemHandler, 2, 3));
	//private final LazyOptional<IItemHandler> sideHandler = LazyOptional.of(() -> new RangedWrapper(itemHandler, 1, 2));
	
	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side)
	{
		if(cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && side != null)
		{
			return side == Direction.DOWN ? downHandler.cast() : upHandler.cast();
			//return side == Direction.DOWN ? downHandler.cast() : side == Direction.UP ? upHandler.cast() : sideHandler.cast();
		}
		return super.getCapability(cap, side);
	}
	
	@Nullable
	@Override
	public Container createMenu(int windowId, PlayerInventory playerInventory, PlayerEntity player)
	{
		return new SendificatorContainer(windowId, playerInventory, itemHandler, parameters, fuelHolder, IWorldPosCallable.create(level, worldPosition), worldPosition, getLevel());
	}
	
	/*@Override
	public ITextComponent getName()
	{
		return null;
	}
	
	@Override
	public ITextComponent getDisplayName()
	{
		return new TranslationTextComponent(TITLE);
	}*/
	
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