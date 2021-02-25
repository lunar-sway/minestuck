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
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nonnull;

public class TotemLatheTileEntity extends TileEntity
{
	private boolean broken = false;
	//two cards so that we can preform the && alchemy operation
	private ItemStack card1 = ItemStack.EMPTY;
	private ItemStack card2 = ItemStack.EMPTY;
	
	public TotemLatheTileEntity()
	{
		super(MSTileEntityTypes.TOTEM_LATHE.get());
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
		int worldCount = getBlockState().get(TotemLatheBlock.Slot.COUNT);
		int actualCount = getActualCardCount();
		if(worldCount != actualCount)
		{
			world.setBlockState(pos, getBlockState().with(TotemLatheBlock.Slot.COUNT, actualCount));
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
		if(world == null)
			return false;
		Direction facing = getFacing();
		BlockPos pos = MSBlocks.TOTEM_LATHE.getDowelPos(getPos(), getBlockState());
		BlockState state = world.getBlockState(pos);
		if(stack.isEmpty())
		{
			if(isValidDowelRod(state, facing))
				world.removeBlock(pos, false);
			return true;
		} else if(stack.getItem() == MSBlocks.CRUXITE_DOWEL.asItem())
		{
			BlockState newState = MSBlocks.TOTEM_LATHE.DOWEL_ROD.get().getDefaultState().with(TotemLatheBlock.FACING, facing).with(TotemLatheBlock.DowelRod.DOWEL, EnumDowelType.getForDowel(stack));
			if(isValidDowelRod(state, facing))
			{
				TileEntity te = world.getTileEntity(pos);
				if(!(te instanceof ItemStackTileEntity))
				{
					te = new ItemStackTileEntity();
					world.setTileEntity(pos, te);
				}
				ItemStackTileEntity teItem = (ItemStackTileEntity) te;
				teItem.setStack(stack);
				if(!state.equals(newState))
					world.setBlockState(pos, newState);
				else world.notifyBlockUpdate(pos, state, state, 2);
				return true;
			} else if(state.isAir(world, pos))
			{
				world.setBlockState(pos, newState);
				TileEntity te = world.getTileEntity(pos);
				if(!(te instanceof ItemStackTileEntity))
				{
					te = new ItemStackTileEntity();
					world.setTileEntity(pos, te);
				}
				ItemStackTileEntity teItem = (ItemStackTileEntity) te;
				teItem.setStack(stack);
				
				return true;
			}
		}
		return false;
	}
	
	public ItemStack getDowel()
	{
		BlockPos pos = getPos().up().offset(getFacing().rotateYCCW(), 2);
		if(isValidDowelRod(world.getBlockState(pos), getFacing()))
		{
			TileEntity te = world.getTileEntity(pos);
			if(te instanceof ItemStackTileEntity)
			{
				return ((ItemStackTileEntity) te).getStack();
			}
		}
		return ItemStack.EMPTY;
		
	}
	
	private boolean isValidDowelRod(BlockState state, Direction facing)
	{
		return state.getBlock() == MSBlocks.TOTEM_LATHE.DOWEL_ROD.get() && state.get(TotemLatheBlock.FACING) == facing;
	}
	
	public Direction getFacing()
	{
		return getBlockState().get(TotemLatheBlock.FACING);
	}
	
	public void onRightClick(PlayerEntity player, BlockState clickedState)
	{
		boolean working = isUseable(clickedState);
		
		//if they have clicked on the part that holds the captcha cards
		if(clickedState.getBlock() instanceof TotemLatheBlock.Slot)
			handleSlotClick(player, working);
		
		//if they have clicked the dowel block
		if(clickedState.getBlock() == MSBlocks.TOTEM_LATHE.ROD.get() || clickedState.getBlock() == MSBlocks.TOTEM_LATHE.DOWEL_ROD.get())
			handleDowelClick(player, working);
		
		//if they have clicked on the lever
		if(working && clickedState.getBlock() == MSBlocks.TOTEM_LATHE.CARVER.get())
		{
			//carve the dowel.
			processContents();
		}
	}
	
	private void handleSlotClick(PlayerEntity player, boolean isWorking)
	{
		ItemStack heldStack = player.getHeldItemMainhand();
		ItemStack card = heldStack.copy().split(1);
		if(tryAddCard(card))
		{
			heldStack.shrink(1);
		} else
		{
			card = tryTakeCard();
			if(!card.isEmpty())
			{
				if(player.getHeldItemMainhand().isEmpty())
					player.setHeldItem(Hand.MAIN_HAND, card);
				else if(!player.inventory.addItemStackToInventory(card))
					dropItem(false, getPos(), card);
				else player.container.detectAndSendChanges();
			}
		}
	}
	
	private void handleDowelClick(PlayerEntity player, boolean isWorking)
	{
		ItemStack heldStack = player.getHeldItemMainhand();
		ItemStack dowel = getDowel();
		if (dowel.isEmpty())
		{
			if(isWorking && heldStack.getItem() == MSBlocks.CRUXITE_DOWEL.asItem())
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
			if(player.getHeldItemMainhand().isEmpty())
				player.setHeldItem(Hand.MAIN_HAND, dowel);
			else if(!player.inventory.addItemStackToInventory(dowel))
				dropItem(true, getPos().up().offset(getFacing().rotateYCCW(), 2), dowel);
			else player.container.detectAndSendChanges();
			setDowel(ItemStack.EMPTY);
		}
	}
	
	private boolean isUseable(BlockState state)
	{
		BlockState currentState = getWorld().getBlockState(getPos());
		if(!isBroken())
		{
			checkStates();
			if(isBroken())
				Debug.warnf("Failed to notice a block being broken or misplaced at the totem lathe at %s", getPos());
		}
		
		if(!state.get(TotemLatheBlock.FACING).equals(currentState.get(TotemLatheBlock.FACING)))
			return false;
		return !isBroken();
	}
	
	public void checkStates()
	{
		if(isBroken())
			return;
		
		if(MSBlocks.TOTEM_LATHE.isInvalidFromSlot(world, getPos()))
			setBroken();
	}
	
	private void dropItem(boolean inBlock, BlockPos pos, ItemStack stack)
	{
		Direction direction = getFacing();
		BlockPos dropPos;
		if(inBlock)
			dropPos = pos;
		else if(!Block.hasSolidSide(world.getBlockState(pos.offset(direction)), world, pos.offset(direction), direction.getOpposite()))
			dropPos = pos.offset(direction);
		else dropPos = pos;
		
		InventoryHelper.spawnItemStack(world, dropPos.getX(), dropPos.getY(), dropPos.getZ(), stack);
	}
	
	@Override
	public void read(CompoundNBT compound)
	{
		super.read(compound);
		broken = compound.getBoolean("broken");
		card1 = ItemStack.read(compound.getCompound("card1"));
		card2 = ItemStack.read(compound.getCompound("card2"));
		if(card1.isEmpty() && !card2.isEmpty())
		{
			card1 = card2;
			card2 = ItemStack.EMPTY;
		}
	}
	
	@Override
	public CompoundNBT write(CompoundNBT compound)
	{
		super.write(compound);
		compound.putBoolean("broken",broken);
		compound.put("card1", card1.write(new CompoundNBT()));
		compound.put("card2", card2.write(new CompoundNBT()));
		return compound;
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
					output = new ItemStack(MSBlocks.GENERIC_OBJECT);
				else output = CombinationRecipe.findResult(new CombinerWrapper(card1, card2, CombinationMode.AND), world);
			else
			{
				ItemStack input = card1.isEmpty() ? card2 : card1;
				if(!AlchemyHelper.isPunchedCard(input))
					output = new ItemStack(MSBlocks.GENERIC_OBJECT);
				else output = AlchemyHelper.getDecodedItem(input);
			}
			
			if(!output.isEmpty())
			{
				ItemStack outputDowel = output.getItem().equals(MSBlocks.GENERIC_OBJECT.asItem()) ? new ItemStack(MSBlocks.CRUXITE_DOWEL) : AlchemyHelper.createEncodedItem(output, false);
				ColorHandler.setColor(outputDowel, ColorHandler.getColorFromStack(dowel));
				
				setDowel(outputDowel);
				success = true;
			}
		}
		
		effects(success);
	}
	
	private void effects(boolean success)
	{
		BlockPos pos = getPos().up().offset(getFacing().rotateYCCW(), 2);
		WorldEventUtil.dispenserEffect(getWorld(), pos, getFacing(), success);
	}
}