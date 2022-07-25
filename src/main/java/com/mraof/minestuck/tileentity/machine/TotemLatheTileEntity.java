package com.mraof.minestuck.tileentity.machine;


import com.mraof.minestuck.block.EnumDowelType;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.block.machine.TotemLatheBlock;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.item.crafting.alchemy.AlchemyHelper;
import com.mraof.minestuck.item.crafting.alchemy.CombinationMode;
import com.mraof.minestuck.item.crafting.alchemy.CombinationRecipe;
import com.mraof.minestuck.item.crafting.alchemy.CombinerWrapper;
import com.mraof.minestuck.tileentity.ItemStackTileEntity;
import com.mraof.minestuck.tileentity.MSTileEntityTypes;
import com.mraof.minestuck.util.ColorHandler;
import com.mraof.minestuck.util.Debug;
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

import javax.annotation.Nonnull;

public class TotemLatheTileEntity extends BlockEntity
{
	private boolean broken = false;
	//two cards so that we can preform the && alchemy operation
	private ItemStack card1 = ItemStack.EMPTY;
	private ItemStack card2 = ItemStack.EMPTY;
	
	public TotemLatheTileEntity(BlockPos pos, BlockState state)
	{
		super(MSTileEntityTypes.TOTEM_LATHE.get(), pos, state);
	}
	
	private boolean tryAddCard(ItemStack stack)
	{
		if(!isBroken() && stack.getItem() == MSItems.CAPTCHA_CARD)
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
	
	public boolean setDowel(ItemStack stack)
	{
		if(level == null)
			return false;
		Direction facing = getFacing();
		BlockPos pos = MSBlocks.TOTEM_LATHE.get().getDowelPos(getBlockPos(), getBlockState());
		BlockState state = level.getBlockState(pos);
		if(stack.isEmpty())
		{
			if(isValidDowelRod(state, facing))
				level.removeBlock(pos, false);
			return true;
		} else if(stack.getItem() == MSBlocks.CRUXITE_DOWEL.get().asItem())
		{
			BlockState newState = MSBlocks.TOTEM_LATHE.get().DOWEL_ROD.get()
					.defaultBlockState().setValue(TotemLatheBlock.FACING, facing)
					.setValue(TotemLatheBlock.DowelRod.DOWEL, EnumDowelType.getForDowel(stack));
			//TODO clean up the following code block
			if(isValidDowelRod(state, facing))
			{
				BlockEntity te = level.getBlockEntity(pos);
				if(!(te instanceof ItemStackTileEntity teItem))
					return false;
				
				teItem.setStack(stack);
				if(!state.equals(newState))
					level.setBlockAndUpdate(pos, newState);
				else level.sendBlockUpdated(pos, state, state, 2);
				return true;
			} else if(state.isAir())
			{
				level.setBlockAndUpdate(pos, newState);
				BlockEntity te = level.getBlockEntity(pos);
				if(!(te instanceof ItemStackTileEntity teItem))
					return false;
				
				teItem.setStack(stack);
				
				return true;
			}
		}
		return false;
	}
	
	public ItemStack getDowel()
	{
		BlockPos pos = getBlockPos().above().relative(getFacing().getCounterClockWise(), 2);
		if(isValidDowelRod(level.getBlockState(pos), getFacing()))
		{
			if(level.getBlockEntity(pos) instanceof ItemStackTileEntity blockEntity)
				return blockEntity.getStack();
		}
		return ItemStack.EMPTY;
		
	}
	
	private boolean isValidDowelRod(BlockState state, Direction facing)
	{
		return state.getBlock() == MSBlocks.TOTEM_LATHE.get().DOWEL_ROD.get() && state.getValue(TotemLatheBlock.FACING) == facing;
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
		if(clickedState.getBlock() == MSBlocks.TOTEM_LATHE.get().ROD.get() || clickedState.getBlock() == MSBlocks.TOTEM_LATHE.get().DOWEL_ROD.get())
			handleDowelClick(player, working);
		
		//if they have clicked on the lever
		if(working && clickedState.getBlock() == MSBlocks.TOTEM_LATHE.get().CARVER.get())
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
			if(isWorking && heldStack.getItem() == MSBlocks.CRUXITE_DOWEL.get().asItem())
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
			setDowel(ItemStack.EMPTY);
		}
	}
	
	private boolean isUseable(BlockState state)
	{
		BlockState currentState = getLevel().getBlockState(getBlockPos());
		if(!isBroken())
		{
			checkStates();
			if(isBroken())
				Debug.warnf("Failed to notice a block being broken or misplaced at the totem lathe at %s", getBlockPos());
		}
		
		if(!state.getValue(TotemLatheBlock.FACING).equals(currentState.getValue(TotemLatheBlock.FACING)))
			return false;
		return !isBroken();
	}
	
	public void checkStates()
	{
		if(isBroken())
			return;
		
		if(MSBlocks.TOTEM_LATHE.get().isInvalidFromSlot(level, getBlockPos()))
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
					output = new ItemStack(MSBlocks.GENERIC_OBJECT.get());
				else output = CombinationRecipe.findResult(new CombinerWrapper(card1, card2, CombinationMode.AND), level);
			else
			{
				ItemStack input = card1.isEmpty() ? card2 : card1;
				if(!AlchemyHelper.isPunchedCard(input))
					output = new ItemStack(MSBlocks.GENERIC_OBJECT.get());
				else output = AlchemyHelper.getDecodedItem(input);
			}
			
			if(!output.isEmpty())
			{
				ItemStack outputDowel = output.getItem().equals(MSBlocks.GENERIC_OBJECT.get().asItem()) ? new ItemStack(MSBlocks.CRUXITE_DOWEL.get()) : AlchemyHelper.createEncodedItem(output, false);
				ColorHandler.setColor(outputDowel, ColorHandler.getColorFromStack(dowel));
				
				setDowel(outputDowel);
				success = true;
			}
		}
		
		effects(success);
	}
	
	private void effects(boolean success)
	{
		BlockPos pos = getBlockPos().above().relative(getFacing().getCounterClockWise(), 2);
		WorldEventUtil.dispenserEffect(getLevel(), pos, getFacing(), success);
	}
}