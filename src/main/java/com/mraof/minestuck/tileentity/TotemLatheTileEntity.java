package com.mraof.minestuck.tileentity;


import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.block.TotemLatheBlock;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.item.crafting.alchemy.AlchemyRecipes;
import com.mraof.minestuck.item.crafting.alchemy.CombinationRegistry;
import com.mraof.minestuck.util.ColorCollector;
import com.mraof.minestuck.util.Debug;
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
	protected ItemStack card1 = ItemStack.EMPTY;
	protected ItemStack card2 = ItemStack.EMPTY;
	
	public TotemLatheTileEntity()
	{
		super(MSTileEntityTypes.TOTEM_LATHE);
	}
	
	//data checking
	public void setCard1(ItemStack stack)
	{
		if(!card2.isEmpty())
			throw new IllegalStateException("Cannot set first card with the second card!");
		
		if(stack.getItem() == MSItems.CAPTCHA_CARD || stack.isEmpty())
		{
			card1 = stack;
			if(world != null)
			{
				BlockState state = world.getBlockState(pos);
				if(!card1.isEmpty())
					state = state.with(TotemLatheBlock.Slot.COUNT, 1);
				else state = state.with(TotemLatheBlock.Slot.COUNT, 0);
				world.setBlockState(pos, state, 2);
			}
		}
	}
	
	@Nonnull
	public ItemStack getCard1()
	{
		return card1;
	}
	
	public void setCard2(ItemStack stack)
	{
		if(card1.isEmpty())
			throw new IllegalStateException("Cannot set second card without the first card!");
		
		if(stack.getItem() == MSItems.CAPTCHA_CARD || stack.isEmpty())
		{
			card2 = stack;
			if(world != null)
			{
				BlockState state = world.getBlockState(pos);
				if(!card2.isEmpty())
					state = state.with(TotemLatheBlock.Slot.COUNT, 2);
				else state = state.with(TotemLatheBlock.Slot.COUNT, 1);
				world.setBlockState(pos, state, 2);
			}
		}
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
		BlockPos pos = getPos().up().offset(facing.rotateYCCW(), 2);
		BlockState state = world.getBlockState(pos);
		if(stack.isEmpty())
		{
			if(state.equals(MSBlocks.TOTEM_LATHE.DOWEL_ROD.getDefaultState().with(TotemLatheBlock.FACING, facing)))
				world.removeBlock(pos, false);
			return true;
		} else if (stack.getItem() == MSBlocks.CRUXITE_DOWEL.asItem())
		{
			if(state.equals(MSBlocks.TOTEM_LATHE.DOWEL_ROD.getDefaultState().with(TotemLatheBlock.FACING, facing)))
			{
				TileEntity te = world.getTileEntity(pos);
				if(!(te instanceof ItemStackTileEntity))
				{
					te = new ItemStackTileEntity();
					world.setTileEntity(pos, te);
				}
				ItemStackTileEntity teItem = (ItemStackTileEntity) te;
				teItem.setStack(stack);
				world.notifyBlockUpdate(pos, state, state, 2);
				return true;
			} else if(state.isAir(world, pos))
			{
				world.setBlockState(pos, MSBlocks.TOTEM_LATHE.DOWEL_ROD.getDefaultState().with(TotemLatheBlock.FACING, facing));
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
		if(world.getBlockState(pos).equals(MSBlocks.TOTEM_LATHE.DOWEL_ROD.getDefaultState().with(TotemLatheBlock.FACING, getFacing())))
		{
			TileEntity te = world.getTileEntity(pos);
			if(te instanceof ItemStackTileEntity)
			{
				return ((ItemStackTileEntity) te).getStack();
			}
		}
		return ItemStack.EMPTY;
		
	}
	
	public Direction getFacing()
	{
		return getBlockState().get(TotemLatheBlock.FACING);
	}
	
	public void onRightClick(PlayerEntity player, BlockState clickedState)
	{
		boolean working = isUseable(clickedState);
		
		ItemStack heldStack = player.getHeldItemMainhand();
		//if they have clicked on the part that holds the chapta cards.
		if(clickedState.getBlock() instanceof TotemLatheBlock.Slot)
		{
			if(!card1.isEmpty())
			{
				if(!card2.isEmpty())
				{
					if(player.getHeldItemMainhand().isEmpty())
						player.setHeldItem(Hand.MAIN_HAND, card2);
					else if(!player.inventory.addItemStackToInventory(card2))
						dropItem(false, getPos(), card2);
					else player.container.detectAndSendChanges();
					setCard2(ItemStack.EMPTY);
				} else if(working && heldStack.getItem() == MSItems.CAPTCHA_CARD)
				{
					setCard2(heldStack.split(1));
				} else
				{
					if(player.getHeldItemMainhand().isEmpty())
						player.setHeldItem(Hand.MAIN_HAND, card1);
					else if(!player.inventory.addItemStackToInventory(card1))
						dropItem(false, getPos(), card1);
					else player.container.detectAndSendChanges();
					setCard1(ItemStack.EMPTY);
				}
			} else if(working && heldStack.getItem() == MSItems.CAPTCHA_CARD)
			{
				setCard1(heldStack.split(1));
			}
		}
		
		//if they have clicked the dowel block
		if(clickedState.getBlock() == MSBlocks.TOTEM_LATHE.ROD || clickedState.getBlock() == MSBlocks.TOTEM_LATHE.DOWEL_ROD)
		{
			ItemStack dowel = getDowel();
			if (dowel.isEmpty())
			{
				if(working && heldStack.getItem() == MSBlocks.CRUXITE_DOWEL.asItem())
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
		
		//if they have clicked on the lever
		if(working && clickedState.getBlock() == MSBlocks.TOTEM_LATHE.CARVER)
		{
			//carve the dowel.
			processContents();
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
		Direction facing = getFacing();
		
		if(	//!world.getBlockState(getPos()).equals(MinestuckBlocks.TOTEM_LATHE.CARD_SLOT.getDefaultState().with(BlockTotemLathe.FACING, facing)) ||
			!world.getBlockState(getPos().offset(facing.rotateYCCW(),1)).equals(MSBlocks.TOTEM_LATHE.BOTTOM_LEFT.getDefaultState().with(TotemLatheBlock.FACING, facing)) ||
			!world.getBlockState(getPos().offset(facing.rotateYCCW(),2)).equals(MSBlocks.TOTEM_LATHE.BOTTOM_RIGHT.getDefaultState().with(TotemLatheBlock.FACING, facing)) ||
			!world.getBlockState(getPos().offset(facing.rotateYCCW(),3)).equals(MSBlocks.TOTEM_LATHE.BOTTOM_CORNER.getDefaultState().with(TotemLatheBlock.FACING, facing)) ||
			
			!world.getBlockState(getPos().up()).equals(MSBlocks.TOTEM_LATHE.MIDDLE.getDefaultState().with(TotemLatheBlock.FACING, facing)) ||
			!world.getBlockState(getPos().up().offset(facing.rotateYCCW(),1)).equals(MSBlocks.TOTEM_LATHE.ROD.getDefaultState().with(TotemLatheBlock.FACING, facing)) ||
			!world.getBlockState(getPos().up().offset(facing.rotateYCCW(),3)).equals(MSBlocks.TOTEM_LATHE.WHEEL.getDefaultState().with(TotemLatheBlock.FACING, facing)) ||
			
			!world.getBlockState(getPos().up(2)).equals(MSBlocks.TOTEM_LATHE.TOP_CORNER.getDefaultState().with(TotemLatheBlock.FACING, facing)) ||
			!world.getBlockState(getPos().up(2).offset(facing.rotateYCCW(),1)).equals(MSBlocks.TOTEM_LATHE.TOP.getDefaultState().with(TotemLatheBlock.FACING, facing)) ||
			!world.getBlockState(getPos().up(2).offset(facing.rotateYCCW(),2)).equals(MSBlocks.TOTEM_LATHE.CARVER.getDefaultState().with(TotemLatheBlock.FACING, facing)))
		{
			setBroken();
		}
		
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
		if(!dowel.isEmpty() && !AlchemyRecipes.hasDecodedItem(dowel) &&  (!card1.isEmpty() || !card2.isEmpty()))
		{
			if(!card1.isEmpty() && !card2.isEmpty())
				if(!card1.hasTag() || !card1.getTag().getBoolean("punched") || !card2.hasTag() || !card2.getTag().getBoolean("punched"))
					output = new ItemStack(MSBlocks.GENERIC_OBJECT);
				else output = CombinationRegistry.getCombination(AlchemyRecipes.getDecodedItem(card1), AlchemyRecipes.getDecodedItem(card2), CombinationRegistry.Mode.MODE_AND);
			else
			{
				ItemStack input = card1.isEmpty() ? card2 : card1;
				if(!input.hasTag() || !input.getTag().getBoolean("punched"))
					output = new ItemStack(MSBlocks.GENERIC_OBJECT);
				else output = AlchemyRecipes.getDecodedItem(input);
			}
			
			if(!output.isEmpty())
			{
				ItemStack outputDowel = output.getItem().equals(MSBlocks.GENERIC_OBJECT.asItem()) ? new ItemStack(MSBlocks.CRUXITE_DOWEL) : AlchemyRecipes.createEncodedItem(output, false);
				ColorCollector.setColor(outputDowel, ColorCollector.getColorFromStack(dowel));
				
				setDowel(outputDowel);
				success = true;
			}
		}
		
		effects(success);
	}
	
	private void effects(boolean success)
	{
		BlockPos pos = getPos().up().offset(getFacing().rotateYCCW(), 2);
		world.playEvent(success ? 1000 : 1001, pos, 0);
		if (success)
		{
			Direction direction = getFacing();
			int i = direction.getXOffset() + 1 + (direction.getZOffset() + 1) * 3;
			world.playEvent(2000, pos, i);
		}
	}
}