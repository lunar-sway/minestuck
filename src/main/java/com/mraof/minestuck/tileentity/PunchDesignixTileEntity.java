package com.mraof.minestuck.tileentity;

import com.mraof.minestuck.advancements.MSCriteriaTriggers;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.block.PunchDesignixBlock;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.item.crafting.alchemy.AlchemyRecipes;
import com.mraof.minestuck.item.crafting.alchemy.CombinationRegistry;
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

import javax.annotation.Nonnull;

import static com.mraof.minestuck.block.MachineBlock.FACING;

public class PunchDesignixTileEntity extends TileEntity
{
	private boolean broken = false;
	private ItemStack card = ItemStack.EMPTY;
	
	public PunchDesignixTileEntity()
	{
		super(MSTileEntityTypes.PUNCH_DESIGNIX);
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
		markDirty();
	}
	
	private void updateState()
	{
		if(world != null && !world.isRemote)
		{
			BlockState state = world.getBlockState(pos);
			boolean hasCard = !card.isEmpty();
			if(state.has(PunchDesignixBlock.Slot.HAS_CARD) && hasCard != state.get(PunchDesignixBlock.Slot.HAS_CARD))
				world.setBlockState(pos, state.with(PunchDesignixBlock.Slot.HAS_CARD, hasCard), 2);
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
			if (player.getHeldItemMainhand().isEmpty())
				player.setHeldItem(Hand.MAIN_HAND, getCard());
			else if (!player.inventory.addItemStackToInventory(getCard()))
				dropItem(false);
			else player.container.detectAndSendChanges();
			
			setCard(ItemStack.EMPTY);
		} else if(!broken)
		{
			ItemStack heldStack = player.getHeldItemMainhand();
			if(!heldStack.isEmpty() && heldStack.getItem() == MSItems.CAPTCHA_CARD)
				setCard(heldStack.split(1));    //Insert card into the punch slot
		}
	}
	
	private void handleKeyboardClick(ServerPlayerEntity player)
	{
		ItemStack heldStack = player.getHeldItemMainhand();
		if(heldStack.getItem() != MSItems.CAPTCHA_CARD)
			return;    //Not a valid item in hand
		
		if(getCard().getItem() == MSItems.CAPTCHA_CARD)
		{
			ItemStack input1 = AlchemyRecipes.getDecodedItem(heldStack);
			if(!input1.isEmpty())
			{
				ItemStack output;
				ItemStack input2 = AlchemyRecipes.isPunchedCard(getCard()) ? AlchemyRecipes.getDecodedItem(getCard()) : ItemStack.EMPTY;
				if(!input2.isEmpty())	//|| combination
				{
					output = CombinationRegistry.getCombination(input1, input2, CombinationRegistry.Mode.MODE_OR);
				} else output = input1;
				
				if(!output.isEmpty())
				{
					MSCriteriaTriggers.PUNCH_DESIGNIX.trigger(player, input1, input2, output);
					setCard(AlchemyRecipes.createCard(output, true));
					effects(true);
					return;
				}
			}
		}
		effects(false);
	}
	
	private void effects(boolean success)
	{
		WorldEventUtil.dispenserEffect(getWorld(), getPos(), getBlockState().get(FACING), success);
	}
	
	private boolean isUsable(BlockState state)
	{
		return !broken && state.get(FACING).equals(getBlockState().get(FACING));
	}
	
	private void validateMachine()
	{
		if (broken || world == null)
			return;
		
		if(MSBlocks.PUNCH_DESIGNIX.isInvalidFromSlot(world, getPos()))
			broken = true;
	}
	
	public void dropItem(boolean inBlock)
	{
		if(world == null) {
			Debug.warn("Tried to drop punch designix card before the world had been set!");
			return;
		}
		
		Direction direction = inBlock ? null : world.getBlockState(this.pos).get(FACING);
		BlockPos dropPos;
		if (inBlock)
			dropPos = this.pos;
		else if (!Block.hasSolidSide(world.getBlockState(this.pos.offset(direction)), world, this.pos.offset(direction), direction.getOpposite()))
			dropPos = this.pos.offset(direction);
		else if (!Block.hasSolidSide(world.getBlockState(this.pos.up()), world, this.pos.up(), Direction.DOWN))
			dropPos = this.pos.up();
		else dropPos = this.pos;
		
		InventoryHelper.spawnItemStack(world, dropPos.getX(), dropPos.getY(), dropPos.getZ(), getCard());
		setCard(ItemStack.EMPTY);
	}
	
	@Override
	public void read(CompoundNBT compound)
	{
		super.read(compound);
		broken = compound.getBoolean("broken");
		setCard(ItemStack.read(compound.getCompound("card")));
	}
	
	@Override
	public CompoundNBT write(CompoundNBT compound)
	{
		super.write(compound);
		compound.putBoolean("broken", this.broken);
		compound.put("card", getCard().write(new CompoundNBT()));
		return compound;
	}
}