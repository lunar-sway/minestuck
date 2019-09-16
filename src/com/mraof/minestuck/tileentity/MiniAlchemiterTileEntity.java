package com.mraof.minestuck.tileentity;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.alchemy.*;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.inventory.MiniAlchemiterContainer;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.tracker.PlayerTracker;
import com.mraof.minestuck.util.IdentifierHandler;
import com.mraof.minestuck.world.storage.PlayerSavedData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.IntReferenceHolder;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;

public class MiniAlchemiterTileEntity extends MachineProcessTileEntity implements INamedContainerProvider
{
	public static final RunType TYPE = RunType.BUTTON_OVERRIDE;
	public static final int INPUT = 0, OUTPUT = 1;
	
	private final IntReferenceHolder wildcardGristHolder = new IntReferenceHolder()
	{
		@Override
		public int get()
		{
			return getWildcardGrist().getId();
		}
		
		@Override
		public void set(int id)
		{
			GristType type = GristType.REGISTRY.getValue(id);
			if(type == null)
				type = GristType.BUILD;
			setWildcardGrist(type);
		}
	};
	
	private int ticks_since_update = 0;
	public IdentifierHandler.PlayerIdentifier owner;
	private GristType wildcardGrist = GristType.BUILD;
	
	public MiniAlchemiterTileEntity()
	{
		super(MSTileEntityTypes.MINI_ALCHEMITER);
	}
	
	@Override
	public RunType getRunType()
	{
		return TYPE;
	}
	
	@Override
	public int getSizeInventory()
	{
		return 2;
	}
	
	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack)
	{
		return index == 0 && stack.getItem() == MSBlocks.CRUXITE_DOWEL.asItem();
	}
	
	@Override
	public boolean contentsValid()
	{
		if(!world.isBlockPowered(this.getPos()) && !this.inv.get(INPUT).isEmpty() && this.owner != null)
		{
			//Check owner's cache: Do they have everything they need?
			ItemStack newItem = AlchemyRecipes.getDecodedItem(this.inv.get(INPUT));
			if(newItem.isEmpty())
				if(!inv.get(INPUT).hasTag() || !inv.get(INPUT).getTag().contains("contentID"))
					newItem = new ItemStack(MSBlocks.GENERIC_OBJECT);
				else return false;
			if(!inv.get(OUTPUT).isEmpty() && (inv.get(OUTPUT).getItem() != newItem.getItem() || inv.get(OUTPUT).getMaxStackSize() <= inv.get(OUTPUT).getCount()))
			{
				return false;
			}
			GristSet cost = AlchemyCostRegistry.getGristConversion(newItem);
			if(newItem.getItem() == MSItems.CAPTCHA_CARD)
				cost = new GristSet(wildcardGrist, MinestuckConfig.cardCost.get());
			
			return GristHelper.canAfford(PlayerSavedData.get(world).getGristSet(this.owner), cost);
		}
		else
		{
			return false;
		}
	}
	
	@Override
	public void processContents()
	{
		ItemStack newItem = AlchemyRecipes.getDecodedItem(this.inv.get(INPUT));
		
		if (newItem.isEmpty())
			newItem = new ItemStack(MSBlocks.GENERIC_OBJECT);
		
		if (inv.get(OUTPUT).isEmpty())
		{
			setInventorySlotContents(1, newItem);
		}
		else
		{
			this.inv.get(OUTPUT).grow(1);
		}
		
		ServerPlayerEntity player = owner.getPlayer(world.getServer());
		if (player != null)
			AlchemyRecipes.onAlchemizedItem(newItem, player);
		
		GristSet cost = AlchemyCostRegistry.getGristConversion(newItem);
		if (newItem.getItem() == MSItems.CAPTCHA_CARD)
			cost = new GristSet(wildcardGrist, MinestuckConfig.cardCost.get());
		GristHelper.decrease(world, owner, cost);
		PlayerTracker.updateGristCache(world.getServer(), owner);
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
	public void read(CompoundNBT compound)
	{
		super.read(compound);
		
		this.wildcardGrist = GristType.getTypeFromString(compound.getString("gristType"));
		if(this.wildcardGrist == null)
		{
			this.wildcardGrist = GristType.BUILD;
		}
		
		if(IdentifierHandler.hasIdentifier(compound, "owner"))
			owner = IdentifierHandler.load(compound, "owner");
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
		return new TranslationTextComponent("container.mini_alchemiter");
	}
	
	@Override
	public int[] getSlotsForFace(Direction side)
	{
		if(side == Direction.DOWN)
			return new int[] {1};
		else return new int[] {0};
	}
	
	public int comparatorValue()
	{
		if (getStackInSlot(INPUT) != null && owner != null)
		{
			ItemStack newItem = AlchemyRecipes.getDecodedItem(getStackInSlot(INPUT));
			if (newItem.isEmpty())
				if (!getStackInSlot(INPUT).hasTag() || !getStackInSlot(INPUT).getTag().contains("contentID"))
					newItem = new ItemStack(MSBlocks.GENERIC_OBJECT);
				else return 0;
			if (!getStackInSlot(OUTPUT).isEmpty() && (getStackInSlot(OUTPUT).getItem() != newItem.getItem() || getStackInSlot(OUTPUT).getMaxStackSize() <= getStackInSlot(OUTPUT).getCount()))
			{
				return 0;
			}
			GristSet cost = AlchemyCostRegistry.getGristConversion(newItem);
			if (newItem.getItem() == MSItems.CAPTCHA_CARD)
				cost = new GristSet(wildcardGrist, MinestuckConfig.cardCost.get());
			// We need to run the check 16 times. Don't want to hammer the game with too many of these, so the comparators are only told to update every 20 ticks.
			// Additionally, we need to check if the item in the slot is empty. Otherwise, it will attempt to check the cost for air, which cannot be alchemized anyway.
			if (cost != null && !getStackInSlot(0).isEmpty())
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
					scale_cost = cost.copy().scaleGrist(lvl);
					if (!GristHelper.canAfford(PlayerSavedData.get(world).getGristSet(owner), scale_cost))
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
		return new MiniAlchemiterContainer(windowId, playerInventory, this, parameters, wildcardGristHolder);
	}
	
	public GristType getWildcardGrist()
	{
		return wildcardGrist;
	}
	
	public void setWildcardGrist(GristType wildcardGrist)
	{
		if(this.wildcardGrist != wildcardGrist)
		{
			this.wildcardGrist = wildcardGrist;
			markDirty();
		}
	}
}