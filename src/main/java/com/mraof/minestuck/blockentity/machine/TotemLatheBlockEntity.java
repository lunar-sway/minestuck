package com.mraof.minestuck.blockentity.machine;


import com.mraof.minestuck.block.EnumDowelType;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.block.machine.TotemLatheBlock;
import com.mraof.minestuck.blockentity.MSBlockEntityTypes;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.alchemy.AlchemyHelper;
import com.mraof.minestuck.alchemy.CombinationMode;
import com.mraof.minestuck.alchemy.CombinationRecipe;
import com.mraof.minestuck.alchemy.CombinerWrapper;
import com.mraof.minestuck.blockentity.ItemStackBlockEntity;
import com.mraof.minestuck.util.ColorHandler;
import com.mraof.minestuck.util.WorldEventUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import java.util.Objects;

public class TotemLatheBlockEntity extends BlockEntity
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	private boolean broken = false;
	//two cards so that we can preform the && alchemy operation
	private ItemStack card1 = ItemStack.EMPTY;
	private ItemStack card2 = ItemStack.EMPTY;
	
	public TotemLatheBlockEntity(BlockPos pos, BlockState state)
	{
		super(MSBlockEntityTypes.TOTEM_LATHE.get(), pos, state);
	}
	
	private boolean tryAddCard(ItemStack stack)
	{
		if(!isBroken() && stack.is(MSItems.CAPTCHA_CARD.get()))
		{
			if(card1.isEmpty())
				card1 = stack;
			else if(card2.isEmpty())
				card2 = stack;
			else return false;
			
			updateState();
			return true;
		}
		return false;
	}
	
	private ItemStack tryTakeCard()
	{
		ItemStack card = ItemStack.EMPTY;
		if(!card2.isEmpty())
		{
			card = card2;
			card2 = ItemStack.EMPTY;
		} else if(!card1.isEmpty())
		{
			card = card1;
			card1 = ItemStack.EMPTY;
		}
		if(!card.isEmpty())
			updateState();
		return card;
	}
	
	private void updateState()
	{
		int worldCount = getBlockState().getValue(TotemLatheBlock.Slot.COUNT);
		int actualCount = getActualCardCount();
		if(worldCount != actualCount)
		{
			level.setBlockAndUpdate(worldPosition, getBlockState().setValue(TotemLatheBlock.Slot.COUNT, actualCount));
		}
	}
	
	private int getActualCardCount()
	{
		if(!card2.isEmpty())
			return 2;
		else if(!card1.isEmpty())
			return 1;
		else return 0;
	}
	
	@Nonnull
	public ItemStack getCard1()
	{
		return card1;
	}
	
	public ItemStack getCard2()
	{
		return card2;
	}
	
	public boolean isBroken()
	{
		return broken;
	}
	
	public void setBroken()
	{
		broken = true;
	}
	
	public void removeDowel()
	{
		Objects.requireNonNull(this.level);
		
		Direction facing = getFacing();
		BlockPos pos = MSBlocks.TOTEM_LATHE.getDowelPos(getBlockPos(), getBlockState());
		BlockState state = level.getBlockState(pos);
		
		if(isValidDowelRod(state, facing))
		{
			level.removeBlock(pos, false);
			setActiveRod(false);
		}
	}
	
	public boolean setDowel(ItemStack dowelStack)
	{
		Objects.requireNonNull(this.level);
		if(!dowelStack.is(MSItems.CRUXITE_DOWEL.get()))
			return false;
		
		Direction facing = getFacing();
		BlockPos pos = MSBlocks.TOTEM_LATHE.getDowelPos(getBlockPos(), getBlockState());
		BlockState oldState = level.getBlockState(pos);
		
		if(!oldState.isAir())
			return false;	// Something is in the way that we shouldn't replace
		
		BlockState newState = MSBlocks.TOTEM_LATHE.DOWEL_ROD.get()
				.defaultBlockState().setValue(TotemLatheBlock.FACING, facing)
				.setValue(TotemLatheBlock.DowelRod.DOWEL, EnumDowelType.getForDowel(dowelStack));
		
		level.setBlockAndUpdate(pos, newState);
		setActiveRod(true);
		
		BlockEntity be = level.getBlockEntity(pos);
		if(be instanceof ItemStackBlockEntity beItem)
		{
			beItem.setStack(dowelStack);
			return true;
		} else
			return false;
	}
	
	private boolean setCarvedItem(ItemStack output)
	{
		Objects.requireNonNull(this.level);
		
		Direction facing = getFacing();
		BlockPos pos = MSBlocks.TOTEM_LATHE.getDowelPos(getBlockPos(), getBlockState());
		BlockState oldState = level.getBlockState(pos);
		
		if(!isValidDowelRod(oldState, facing))
			return false;	// This is not our dowel rod block!
		
		BlockEntity be = level.getBlockEntity(pos);
		if(be instanceof ItemStackBlockEntity beItem)
		{
			ItemStack oldDowel = beItem.getStack();
			ItemStack newDowel = output.is(MSItems.GENERIC_OBJECT.get()) ? new ItemStack(MSItems.CRUXITE_DOWEL.get()) : AlchemyHelper.createEncodedItem(output, false);
			ColorHandler.setColor(newDowel, ColorHandler.getColorFromStack(oldDowel));
			beItem.setStack(newDowel);
			
			BlockState newState = MSBlocks.TOTEM_LATHE.DOWEL_ROD.get()
					.defaultBlockState().setValue(TotemLatheBlock.FACING, facing)
					.setValue(TotemLatheBlock.DowelRod.DOWEL, EnumDowelType.getForDowel(newDowel));
			
			if(!oldState.equals(newState))
				level.setBlockAndUpdate(pos, newState);
			else
				level.sendBlockUpdated(pos, oldState, newState, 2);	// Make sure the new dowel item is synced to client
			
			return true;
		} else
			return false;
	}
	
	private void setActiveRod(boolean active)
	{
		Objects.requireNonNull(this.level);
		Direction facing = this.getFacing();
		
		BlockPos rodPos = MSBlocks.TOTEM_LATHE.getRodPos(getBlockPos(), getBlockState());
		BlockState rodState = this.level.getBlockState(rodPos);
		if(rodState.is(MSBlocks.TOTEM_LATHE.ROD.get()) && rodState.getValue(TotemLatheBlock.FACING) == facing)
			this.level.setBlockAndUpdate(rodPos, rodState.setValue(TotemLatheBlock.Rod.ACTIVE, active));
		
		BlockPos wheelPos = MSBlocks.TOTEM_LATHE.getWheelPos(getBlockPos(), getBlockState());
		BlockState wheelState = this.level.getBlockState(wheelPos);
		if(wheelState.is(MSBlocks.TOTEM_LATHE.WHEEL.get()) && wheelState.getValue(TotemLatheBlock.FACING) == facing)
			this.level.setBlockAndUpdate(wheelPos, wheelState.setValue(TotemLatheBlock.Rod.ACTIVE, active));
	}
	
	public ItemStack getDowel()
	{
		BlockPos pos = MSBlocks.TOTEM_LATHE.getDowelPos(getBlockPos(), getBlockState());
		if(isValidDowelRod(level.getBlockState(pos), getFacing()))
		{
			if(level.getBlockEntity(pos) instanceof ItemStackBlockEntity blockEntity)
				return blockEntity.getStack();
		}
		return ItemStack.EMPTY;
		
	}
	
	private boolean isValidDowelRod(BlockState state, Direction facing)
	{
		return state.is(MSBlocks.TOTEM_LATHE.DOWEL_ROD.get()) && state.getValue(TotemLatheBlock.FACING) == facing;
	}
	
	public Direction getFacing()
	{
		return getBlockState().getValue(TotemLatheBlock.FACING);
	}
	
	public void onRightClick(Player player, BlockState clickedState)
	{
		boolean working = isUseable(clickedState);
		
		//if they have clicked on the part that holds the captcha cards
		if(clickedState.getBlock() instanceof TotemLatheBlock.Slot)
			handleSlotClick(player, working);
		
		//if they have clicked the dowel block
		if(clickedState.is(MSBlocks.TOTEM_LATHE.ROD.get()) || clickedState.is(MSBlocks.TOTEM_LATHE.DOWEL_ROD.get()))
			handleDowelClick(player, working);
		
		//if they have clicked on the lever
		if(working && clickedState.is(MSBlocks.TOTEM_LATHE.CARVER.get()))
		{
			//carve the dowel.
			processContents();
		}
	}
	
	private void handleSlotClick(Player player, boolean isWorking)
	{
		ItemStack heldStack = player.getMainHandItem();
		ItemStack card = heldStack.copy().split(1);
		if(tryAddCard(card))
		{
			heldStack.shrink(1);
		} else
		{
			card = tryTakeCard();
			if(!card.isEmpty())
			{
				if(player.getMainHandItem().isEmpty())
					player.setItemInHand(InteractionHand.MAIN_HAND, card);
				else if(!player.getInventory().add(card))
					dropItem(false, getBlockPos(), card);
				else player.inventoryMenu.broadcastChanges();
			}
		}
	}
	
	private void handleDowelClick(Player player, boolean isWorking)
	{
		ItemStack heldStack = player.getMainHandItem();
		ItemStack dowel = getDowel();
		if (dowel.isEmpty())
		{
			if(isWorking && heldStack.is(MSItems.CRUXITE_DOWEL.get()))
			{
				ItemStack copy = heldStack.copy();
				copy.setCount(1);
				if(setDowel(copy))
				{
					heldStack.shrink(1);
				}
			}
		} else
		{
			if(player.getMainHandItem().isEmpty())
				player.setItemInHand(InteractionHand.MAIN_HAND, dowel);
			else if(!player.getInventory().add(dowel))
				dropItem(true, getBlockPos().above().relative(getFacing().getCounterClockWise(), 2), dowel);
			else player.inventoryMenu.broadcastChanges();
			removeDowel();
		}
	}
	
	private boolean isUseable(BlockState state)
	{
		BlockState currentState = getLevel().getBlockState(getBlockPos());
		if(!isBroken())
		{
			checkStates();
			if(isBroken())
				LOGGER.warn("Failed to notice a block being broken or misplaced at the totem lathe at {}", getBlockPos());
		}
		
		if(!state.getValue(TotemLatheBlock.FACING).equals(currentState.getValue(TotemLatheBlock.FACING)))
			return false;
		return !isBroken();
	}
	
	public void checkStates()
	{
		if(isBroken())
			return;
		
		if(MSBlocks.TOTEM_LATHE.isInvalidFromSlot(level, getBlockPos()))
			setBroken();
	}
	
	private void dropItem(boolean inBlock, BlockPos pos, ItemStack stack)
	{
		Direction direction = getFacing();
		BlockPos dropPos;
		if(inBlock)
			dropPos = pos;
		else if(!Block.canSupportCenter(level, pos.relative(direction), direction.getOpposite()))
			dropPos = pos.relative(direction);
		else dropPos = pos;
		
		Containers.dropItemStack(level, dropPos.getX(), dropPos.getY(), dropPos.getZ(), stack);
	}
	
	@Override
	public void load(CompoundTag nbt)
	{
		super.load(nbt);
		broken = nbt.getBoolean("broken");
		card1 = ItemStack.of(nbt.getCompound("card1"));
		card2 = ItemStack.of(nbt.getCompound("card2"));
		if(card1.isEmpty() && !card2.isEmpty())
		{
			card1 = card2;
			card2 = ItemStack.EMPTY;
		}
	}
	
	@Override
	public void saveAdditional(CompoundTag compound)
	{
		super.saveAdditional(compound);
		compound.putBoolean("broken",broken);
		compound.put("card1", card1.save(new CompoundTag()));
		compound.put("card2", card2.save(new CompoundTag()));
	}
	
	public void processContents()
	{
		ItemStack dowel = getDowel();
		ItemStack output;
		boolean success = false;
		if(!dowel.isEmpty() && !AlchemyHelper.hasDecodedItem(dowel) && (!card1.isEmpty() || !card2.isEmpty()))
		{
			if(!card1.isEmpty() && !card2.isEmpty())
				if(!AlchemyHelper.isPunchedCard(card1) || !AlchemyHelper.isPunchedCard(card2))
					output = new ItemStack(MSItems.GENERIC_OBJECT.get());
				else output = CombinationRecipe.findResult(new CombinerWrapper(card1, card2, CombinationMode.AND), level);
			else
			{
				ItemStack input = card1.isEmpty() ? card2 : card1;
				if(!AlchemyHelper.isPunchedCard(input))
					output = new ItemStack(MSItems.GENERIC_OBJECT.get());
				else output = AlchemyHelper.getDecodedItem(input);
			}
			
			if(!output.isEmpty())
				success = setCarvedItem(output);
		}
		
		effects(success);
	}
	
	private void effects(boolean success)
	{
		BlockPos pos = getBlockPos().above().relative(getFacing().getCounterClockWise(), 2);
		WorldEventUtil.dispenserEffect(getLevel(), pos, getFacing(), success);
	}
}