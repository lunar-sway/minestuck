package com.mraof.minestuck.tileentity.machine;

import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.event.AlchemyEvent;
import com.mraof.minestuck.inventory.MiniAlchemiterContainer;
import com.mraof.minestuck.item.crafting.alchemy.*;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.player.PlayerIdentifier;
import com.mraof.minestuck.tileentity.MSTileEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.IntReferenceHolder;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RangedWrapper;
import net.minecraftforge.registries.ForgeRegistry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class MiniAlchemiterTileEntity extends MachineProcessTileEntity implements INamedContainerProvider, IOwnable, GristWildcardHolder
{
	public static final String TITLE = "container.minestuck.mini_alchemiter";
	public static final RunType TYPE = RunType.BUTTON_OVERRIDE;
	public static final int INPUT = 0, OUTPUT = 1;
	
	private final IntReferenceHolder wildcardGristHolder = new IntReferenceHolder()
	{
		@Override
		public int get()
		{
			return ((ForgeRegistry<GristType>) GristTypes.getRegistry()).getID(getWildcardGrist());
		}
		
		@Override
		public void set(int id)
		{
			GristType type = ((ForgeRegistry<GristType>) GristTypes.getRegistry()).getValue(id);
			if(type == null)
				type = GristTypes.BUILD.get();
			setWildcardGrist(type);
		}
	};
	
	private int ticks_since_update = 0;
	private PlayerIdentifier owner;
	private GristType wildcardGrist = GristTypes.BUILD.get();
	
	public MiniAlchemiterTileEntity()
	{
		super(MSTileEntityTypes.MINI_ALCHEMITER.get());
	}
	
	@Override
	protected ItemStackHandler createItemHandler()
	{
		return new CustomHandler(2, (slot, stack) ->  slot == INPUT && stack.getItem() == MSBlocks.CRUXITE_DOWEL.asItem());
	}
	
	@Override
	public RunType getRunType()
	{
		return TYPE;
	}
	
	@Override
	public boolean contentsValid()
	{
		if(!world.isBlockPowered(this.getPos()) && !itemHandler.getStackInSlot(INPUT).isEmpty() && this.owner != null)
		{
			//Check owner's cache: Do they have everything they need?
			ItemStack newItem = AlchemyHelper.getDecodedItem(itemHandler.getStackInSlot(INPUT));
			if(newItem.isEmpty())
				if(!itemHandler.getStackInSlot(INPUT).hasTag() || !itemHandler.getStackInSlot(INPUT).getTag().contains("contentID"))
					newItem = new ItemStack(MSBlocks.GENERIC_OBJECT);
				else return false;
			if(!itemHandler.getStackInSlot(OUTPUT).isEmpty() && (itemHandler.getStackInSlot(OUTPUT).getItem() != newItem.getItem() || itemHandler.getStackInSlot(OUTPUT).getMaxStackSize() <= itemHandler.getStackInSlot(OUTPUT).getCount()))
			{
				return false;
			}
			GristSet cost = GristCostRecipe.findCostForItem(newItem, wildcardGrist, false, world);
			
			return GristHelper.canAfford(world, owner, cost);
		}
		else
		{
			return false;
		}
	}
	
	@Override
	public void processContents()
	{
		ItemStack newItem = AlchemyHelper.getDecodedItem(itemHandler.getStackInSlot(INPUT));
		
		if (newItem.isEmpty())
			newItem = new ItemStack(MSBlocks.GENERIC_OBJECT);
		
		GristSet cost = GristCostRecipe.findCostForItem(newItem, wildcardGrist, false, world);
		
		GristHelper.decrease(world, owner, cost);
		
		AlchemyEvent event = new AlchemyEvent(owner, this, itemHandler.getStackInSlot(INPUT), newItem, cost);
		MinecraftForge.EVENT_BUS.post(event);
		newItem = event.getItemResult();
		ItemStack existing = itemHandler.getStackInSlot(OUTPUT);
		if(!existing.isEmpty())
			newItem.grow(existing.getCount());
		
		itemHandler.setStackInSlot(OUTPUT, newItem);
	}
	
	// We're going to want to trigger a block update every 20 ticks to have comparators pull data from the Alchemiter.
	@Override
	public void tick()
	{
		super.tick();
		if (this.ticks_since_update == 20)
		{
			world.updateComparatorOutputLevel(this.getPos(), this.getBlockState().getBlock());
			this.ticks_since_update = 0;
		}
		else
		{
			this.ticks_since_update++;
		}
	}
	
	@Override
	public void read(BlockState state, CompoundNBT nbt)
	{
		super.read(state, nbt);
		
		this.wildcardGrist = GristType.read(nbt, "gristType");
		
		if(IdentifierHandler.hasIdentifier(nbt, "owner"))
			owner = IdentifierHandler.load(nbt, "owner");
	}
	
	@Override
	public CompoundNBT write(CompoundNBT compound)
	{
		compound.putString("gristType", wildcardGrist.getRegistryName().toString());
		
		if(owner != null)
			owner.saveToNBT(compound, "owner");
		
		return super.write(compound);
	}
	
	@Override
	public ITextComponent getDisplayName()
	{
		return new TranslationTextComponent(TITLE);
	}
	
	private final LazyOptional<IItemHandler> sideHandler = LazyOptional.of(() -> new RangedWrapper(itemHandler, INPUT, INPUT + 1));
	private final LazyOptional<IItemHandler> downHandler = LazyOptional.of(() -> new RangedWrapper(itemHandler, OUTPUT, OUTPUT + 1));
	
	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side)
	{
		if(cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && side != null)
		{
			return side == Direction.DOWN ? downHandler.cast() : sideHandler.cast();
		}
		return super.getCapability(cap, side);
	}
	
	public int comparatorValue()
	{
		ItemStack input = itemHandler.getStackInSlot(INPUT);
		if(!input.isEmpty() && owner != null)
		{
			ItemStack newItem = AlchemyHelper.getDecodedItem(input);
			if (newItem.isEmpty())
			{
				if(!AlchemyHelper.hasDecodedItem(input))
					newItem = new ItemStack(MSBlocks.GENERIC_OBJECT);
				else return 0;
			}
			ItemStack output = itemHandler.getStackInSlot(OUTPUT);
			if (!output.isEmpty() && (output.getItem() != newItem.getItem() || output.getMaxStackSize() <= output.getCount()))
			{
				return 0;
			}
			GristSet cost = GristCostRecipe.findCostForItem(newItem, wildcardGrist, false, world);
			// We need to run the check 16 times. Don't want to hammer the game with too many of these, so the comparators are only told to update every 20 ticks.
			// Additionally, we need to check if the item in the slot is empty. Otherwise, it will attempt to check the cost for air, which cannot be alchemized anyway.
			if (cost != null && !input.isEmpty())
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
					scale_cost = cost.copy().scale(lvl);
					if (!GristHelper.canAfford(world, owner, scale_cost))
					{
						return lvl - 1;
					}
				}
			}
		}
		return 0;
	}
	
	@Nullable
	@Override
	public Container createMenu(int windowId, PlayerInventory playerInventory, PlayerEntity player)
	{
		return new MiniAlchemiterContainer(windowId, playerInventory, itemHandler, parameters, wildcardGristHolder, IWorldPosCallable.of(world, pos), pos);
	}
	
	public GristType getWildcardGrist()
	{
		return wildcardGrist;
	}
	
	@Override
	public void setWildcardGrist(GristType wildcardGrist)
	{
		if(this.wildcardGrist != wildcardGrist)
		{
			this.wildcardGrist = wildcardGrist;
			markDirty();
		}
	}
	
	@Override
	public void setOwner(PlayerIdentifier identifier)
	{
		this.owner = identifier;
	}
	
	@Override
	public PlayerIdentifier getOwner()
	{
		return owner;
	}
}