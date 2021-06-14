package com.mraof.minestuck.tileentity.machine;

import com.mraof.minestuck.advancements.MSCriteriaTriggers;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.block.machine.PunchDesignixBlock;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.item.crafting.alchemy.AlchemyHelper;
import com.mraof.minestuck.item.crafting.alchemy.CombinationMode;
import com.mraof.minestuck.item.crafting.alchemy.CombinationRecipe;
import com.mraof.minestuck.item.crafting.alchemy.CombinerWrapper;
import com.mraof.minestuck.tileentity.MSTileEntityTypes;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.util.WorldEventUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nonnull;

import static com.mraof.minestuck.block.machine.MachineBlock.FACING;

public class PunchDesignixTileEntity extends TileEntity
{
	private boolean broken = false;
	private ItemStack card = ItemStack.EMPTY;
	
	public PunchDesignixTileEntity()
	{
		super(MSTileEntityTypes.PUNCH_DESIGNIX.get());
	}
	
	public void setCard(ItemStack card)
	{
		if (card.getItem() == MSItems.CAPTCHA_CARD || card.isEmpty())
		{
			this.card = card;
			updateState();
		}
	}
	
	public void breakMachine()
	{
		broken = true;
		setChanged();
	}
	
	private void updateState()
	{
		if(level != null && !level.isClientSide)
		{
			BlockState state = level.getBlockState(worldPosition);
			boolean hasCard = !card.isEmpty();
			if(state.hasProperty(PunchDesignixBlock.Slot.HAS_CARD) && hasCard != state.getValue(PunchDesignixBlock.Slot.HAS_CARD))
				level.setBlock(worldPosition, state.setValue(PunchDesignixBlock.Slot.HAS_CARD, hasCard), Constants.BlockFlags.BLOCK_UPDATE);
		}
	}
	
	@Nonnull
	public ItemStack getCard()
	{
		return card;
	}
	
	public void onRightClick(ServerPlayerEntity player, BlockState clickedState)
	{
		validateMachine();
		
		Block part = clickedState.getBlock();
		if (part instanceof PunchDesignixBlock.Slot)
		{
			handleSlotClick(player);
		} else if(isUsable(clickedState) && (part == MSBlocks.PUNCH_DESIGNIX.KEYBOARD.get() || part == MSBlocks.PUNCH_DESIGNIX.RIGHT_LEG.get()))
		{
			handleKeyboardClick(player);
		}
	}
	
	private void handleSlotClick(ServerPlayerEntity player)
	{
		if(!getCard().isEmpty())
		{
			if (player.getMainHandItem().isEmpty())
				player.setItemInHand(Hand.MAIN_HAND, getCard());
			else if (!player.inventory.add(getCard()))
				dropItem(false);
			else player.inventoryMenu.broadcastChanges();
			
			setCard(ItemStack.EMPTY);
		} else if(!broken)
		{
			ItemStack heldStack = player.getMainHandItem();
			if(!heldStack.isEmpty() && heldStack.getItem() == MSItems.CAPTCHA_CARD)
				setCard(heldStack.split(1));    //Insert card into the punch slot
		}
	}
	
	private void handleKeyboardClick(ServerPlayerEntity player)
	{
		ItemStack heldStack = player.getMainHandItem();
		if(heldStack.getItem() != MSItems.CAPTCHA_CARD)
			return;    //Not a valid item in hand
		
		if(getCard().getItem() == MSItems.CAPTCHA_CARD)
		{
			if(AlchemyHelper.hasDecodedItem(heldStack))
			{
				ItemStack output;
				if(AlchemyHelper.isPunchedCard(getCard()))	//|| combination
				{
					output = CombinationRecipe.findResult(new CombinerWrapper(heldStack, getCard(), CombinationMode.OR), level);
				} else output = AlchemyHelper.getDecodedItem(heldStack);
				
				if(!output.isEmpty())
				{
					MSCriteriaTriggers.PUNCH_DESIGNIX.trigger(player, AlchemyHelper.getDecodedItem(heldStack), AlchemyHelper.getDecodedItem(getCard()), output);
					setCard(AlchemyHelper.createCard(output, true));
					effects(true);
					return;
				}
			}
		}
		effects(false);
	}
	
	private void effects(boolean success)
	{
		WorldEventUtil.dispenserEffect(getLevel(), getBlockPos(), getBlockState().getValue(FACING), success);
	}
	
	private boolean isUsable(BlockState state)
	{
		return !broken && state.getValue(FACING).equals(getBlockState().getValue(FACING));
	}
	
	private void validateMachine()
	{
		if (broken || level == null)
			return;
		
		if(MSBlocks.PUNCH_DESIGNIX.isInvalidFromSlot(level, getBlockPos()))
			broken = true;
	}
	
	public void dropItem(boolean inBlock)
	{
		if(level == null) {
			Debug.warn("Tried to drop punch designix card before the world had been set!");
			return;
		}
		
		Direction direction = inBlock ? null : level.getBlockState(this.worldPosition).getValue(FACING);
		BlockPos dropPos;
		if (inBlock)
			dropPos = this.worldPosition;
		else if (!Block.canSupportCenter(level, this.worldPosition.relative(direction), direction.getOpposite()))
			dropPos = this.worldPosition.relative(direction);
		else if (!Block.canSupportCenter(level, this.worldPosition.above(), Direction.DOWN))
			dropPos = this.worldPosition.above();
		else dropPos = this.worldPosition;
		
		InventoryHelper.dropItemStack(level, dropPos.getX(), dropPos.getY(), dropPos.getZ(), getCard());
		setCard(ItemStack.EMPTY);
	}
	
	@Override
	public void load(BlockState state, CompoundNBT nbt)
	{
		super.load(state, nbt);
		broken = nbt.getBoolean("broken");
		setCard(ItemStack.of(nbt.getCompound("card")));
	}
	
	@Override
	public CompoundNBT save(CompoundNBT compound)
	{
		super.save(compound);
		compound.putBoolean("broken", this.broken);
		compound.put("card", getCard().save(new CompoundNBT()));
		return compound;
	}
}