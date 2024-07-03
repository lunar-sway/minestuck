package com.mraof.minestuck.blockentity.machine;

import com.mraof.minestuck.alchemy.AlchemyHelper;
import com.mraof.minestuck.alchemy.GristHelper;
import com.mraof.minestuck.api.alchemy.GristSet;
import com.mraof.minestuck.api.alchemy.GristType;
import com.mraof.minestuck.api.alchemy.GristTypes;
import com.mraof.minestuck.api.alchemy.recipe.GristCostRecipe;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.blockentity.MSBlockEntityTypes;
import com.mraof.minestuck.event.AlchemyEvent;
import com.mraof.minestuck.inventory.MiniAlchemiterMenu;
import com.mraof.minestuck.player.GristCache;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.player.PlayerIdentifier;
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
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.wrapper.RangedWrapper;

import javax.annotation.Nullable;
import java.util.Objects;

public class MiniAlchemiterBlockEntity extends MachineProcessBlockEntity implements MenuProvider, IOwnable, GristWildcardHolder
{
	public static final String TITLE = "container.minestuck.mini_alchemiter";
	public static final int INPUT = 0, OUTPUT = 1;
	public static final int MAX_PROGRESS = 100;
	
	private final ProgressTracker progressTracker = new ProgressTracker(ProgressTracker.RunType.ONCE_OR_LOOPING, MAX_PROGRESS, this::setChanged, this::contentsValid);
	private final DataSlot wildcardGristHolder = new DataSlot()
	{
		@Override
		public int get()
		{
			return GristTypes.REGISTRY.getId(getWildcardGrist());
		}
		
		@Override
		public void set(int id)
		{
			GristType type = GristTypes.REGISTRY.byId(id);
			if(type == null)
				type = GristTypes.BUILD.get();
			setWildcardGrist(type);
		}
	};
	
	private int ticks_since_update = 0;
	@Nullable
	private PlayerIdentifier owner;
	private GristType wildcardGrist = GristTypes.BUILD.get();
	
	public MiniAlchemiterBlockEntity(BlockPos pos, BlockState state)
	{
		super(MSBlockEntityTypes.MINI_ALCHEMITER.get(), pos, state);
	}
	
	@Override
	protected ItemStackHandler createItemHandler()
	{
		return new CustomHandler(2, (slot, stack) ->  slot == INPUT && stack.getItem() == MSBlocks.CRUXITE_DOWEL.get().asItem());
	}
	
	private boolean contentsValid()
	{
		if(!level.hasNeighborSignal(this.getBlockPos()) && !itemHandler.getStackInSlot(INPUT).isEmpty() && this.owner != null)
		{
			//Check owner's cache: Do they have everything they need?
			ItemStack newItem = AlchemyHelper.getDecodedItem(itemHandler.getStackInSlot(INPUT));
			if(newItem.isEmpty())
				if(!itemHandler.getStackInSlot(INPUT).hasTag() || !itemHandler.getStackInSlot(INPUT).getTag().contains("contentID"))
					newItem = new ItemStack(MSBlocks.GENERIC_OBJECT.get());
				else return false;
			if(!itemHandler.getStackInSlot(OUTPUT).isEmpty() && (itemHandler.getStackInSlot(OUTPUT).getItem() != newItem.getItem() || itemHandler.getStackInSlot(OUTPUT).getMaxStackSize() <= itemHandler.getStackInSlot(OUTPUT).getCount()))
			{
				return false;
			}
			GristSet cost = GristCostRecipe.findCostForItem(newItem, wildcardGrist, false, level);
			
			return GristCache.get(level, owner).canAfford(cost);
		}
		else
		{
			return false;
		}
	}
	
	private void processContents()
	{
		ItemStack newItem = AlchemyHelper.getDecodedItem(itemHandler.getStackInSlot(INPUT));
		
		if (newItem.isEmpty())
			newItem = new ItemStack(MSBlocks.GENERIC_OBJECT.get());
		
		GristSet cost = GristCostRecipe.findCostForItem(newItem, wildcardGrist, false, level);
		Objects.requireNonNull(cost);
		
		if(GristCache.get(level, owner).tryTake(cost, GristHelper.EnumSource.CLIENT))
		{
			AlchemyEvent event = new AlchemyEvent(owner, this, itemHandler.getStackInSlot(INPUT), newItem, cost);
			NeoForge.EVENT_BUS.post(event);
			newItem = event.getItemResult();
			ItemStack existing = itemHandler.getStackInSlot(OUTPUT);
			if(!existing.isEmpty())
				newItem.grow(existing.getCount());
			
			itemHandler.setStackInSlot(OUTPUT, newItem);
		}
	}
	
	// We're going to want to trigger a block update every 20 ticks to have comparators pull data from the Alchemiter.
	@Override
	protected void tick()
	{
		this.progressTracker.tick(this::processContents);
		
		if (this.ticks_since_update == 20)
		{
			level.updateNeighbourForOutputSignal(this.getBlockPos(), this.getBlockState().getBlock());
			this.ticks_since_update = 0;
		}
		else
		{
			this.ticks_since_update++;
		}
	}
	
	@Override
	public void load(CompoundTag nbt)
	{
		super.load(nbt);
		
		this.progressTracker.load(nbt);
		
		this.wildcardGrist = GristHelper.parseGristType(nbt.get("gristType")).orElseGet(GristTypes.BUILD);
		
		owner = IdentifierHandler.load(nbt, "owner").result().orElse(null);
	}
	
	@Override
	public void saveAdditional(CompoundTag compound)
	{
		super.saveAdditional(compound);
		
		this.progressTracker.save(compound);
		
		compound.put("gristType", GristHelper.encodeGristType(wildcardGrist));
		
		if(owner != null)
			owner.saveToNBT(compound, "owner");
	}
	
	@Override
	public Component getDisplayName()
	{
		return Component.translatable(TITLE);
	}
	
	public IItemHandler getItemHandler(@Nullable Direction side)
	{
		if(side == null)
			return this.itemHandler;
		
		if(side == Direction.DOWN)
			return new RangedWrapper(this.itemHandler, OUTPUT, OUTPUT + 1);
		return new RangedWrapper(this.itemHandler, INPUT, INPUT + 1);
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
					newItem = new ItemStack(MSBlocks.GENERIC_OBJECT.get());
				else return 0;
			}
			ItemStack output = itemHandler.getStackInSlot(OUTPUT);
			if (!output.isEmpty() && (output.getItem() != newItem.getItem() || output.getMaxStackSize() <= output.getCount()))
			{
				return 0;
			}
			GristSet cost = GristCostRecipe.findCostForItem(newItem, wildcardGrist, false, level);
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
					scale_cost = cost.mutableCopy().scale(lvl);
					if (!GristCache.get(level, owner).canAfford(scale_cost))
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
	public AbstractContainerMenu createMenu(int windowId, Inventory playerInventory, Player player)
	{
		return new MiniAlchemiterMenu(windowId, playerInventory, itemHandler, this.progressTracker, wildcardGristHolder, ContainerLevelAccess.create(level, worldPosition), worldPosition);
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
			setChanged();
		}
	}
	
	@Override
	public void setOwner(PlayerIdentifier identifier)
	{
		this.owner = identifier;
	}
	
	@Nullable
	@Override
	public PlayerIdentifier getOwner()
	{
		return owner;
	}
}
