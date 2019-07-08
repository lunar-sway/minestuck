package com.mraof.minestuck.tileentity;


import com.mraof.minestuck.block.BlockTotemLathe;
import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.item.MinestuckItems;
import com.mraof.minestuck.alchemy.AlchemyRecipes;
import com.mraof.minestuck.alchemy.CombinationRegistry;

import com.mraof.minestuck.util.ColorCollector;
import com.mraof.minestuck.util.Debug;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nonnull;

public class TileEntityTotemLathe extends TileEntity
{
	private boolean broken = false;
	//two cards so that we can preform the && alchemy operation
	protected ItemStack card1 = ItemStack.EMPTY;
	protected ItemStack card2 = ItemStack.EMPTY;
	
	public TileEntityTotemLathe()
	{
		super(MinestuckTiles.TOTEM_LATHE);
	}
	
	//data checking
	public void setCard1(ItemStack stack)
	{
		if(!card2.isEmpty())
			throw new IllegalStateException("Cannot set first card with the second card!");
		
		if(stack.getItem() == MinestuckItems.CAPTCHA_CARD || stack.isEmpty())
		{
			card1 = stack;
			if(world != null)
			{
				IBlockState state = world.getBlockState(pos);
				if(!card1.isEmpty())
					state = state.with(BlockTotemLathe.Slot.COUNT, 1);
				else state = state.with(BlockTotemLathe.Slot.COUNT, 0);
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
		
		if(stack.getItem() == MinestuckItems.CAPTCHA_CARD || stack.isEmpty())
		{
			card2 = stack;
			if(world != null)
			{
				IBlockState state = world.getBlockState(pos);
				if(!card2.isEmpty())
					state = state.with(BlockTotemLathe.Slot.COUNT, 2);
				else state = state.with(BlockTotemLathe.Slot.COUNT, 1);
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
		EnumFacing facing = getFacing();
		BlockPos pos = getPos().up().offset(facing.rotateYCCW(), 2);
		IBlockState state = world.getBlockState(pos);
		if(stack.isEmpty())
		{
			if(state.equals(MinestuckBlocks.TOTEM_LATHE.DOWEL_ROD.getDefaultState().with(BlockTotemLathe.FACING, facing)))
				world.removeBlock(pos);
			return true;
		} else if (stack.getItem() == MinestuckBlocks.CRUXITE_DOWEL.asItem())
		{
			if(state.equals(MinestuckBlocks.TOTEM_LATHE.DOWEL_ROD.getDefaultState().with(BlockTotemLathe.FACING, facing)))
			{
				TileEntity te = world.getTileEntity(pos);
				if(!(te instanceof TileEntityItemStack))
				{
					te = new TileEntityItemStack();
					world.setTileEntity(pos, te);
				}
				TileEntityItemStack teItem = (TileEntityItemStack) te;
				teItem.setStack(stack);
				world.notifyBlockUpdate(pos, state, state, 2);
				return true;
			} else if(state.isAir(world, pos))
			{
				world.setBlockState(pos, MinestuckBlocks.TOTEM_LATHE.DOWEL_ROD.getDefaultState().with(BlockTotemLathe.FACING, facing));
				TileEntity te = world.getTileEntity(pos);
				if(!(te instanceof TileEntityItemStack))
				{
					te = new TileEntityItemStack();
					world.setTileEntity(pos, te);
				}
				TileEntityItemStack teItem = (TileEntityItemStack) te;
				teItem.setStack(stack);
				
				return true;
			}
		}
		return false;
	}
	public ItemStack getDowel()
	{
		BlockPos pos = getPos().up().offset(getFacing().rotateYCCW(), 2);
		if(world.getBlockState(pos).equals(MinestuckBlocks.TOTEM_LATHE.DOWEL_ROD.getDefaultState().with(BlockTotemLathe.FACING, getFacing())))
		{
			TileEntity te = world.getTileEntity(pos);
			if(te instanceof TileEntityItemStack)
			{
				return ((TileEntityItemStack) te).getStack();
			}
		}
		return ItemStack.EMPTY;
		
	}
	
	public EnumFacing getFacing()
	{
		return getBlockState().get(BlockTotemLathe.FACING);
	}
	
	public void onRightClick(EntityPlayer player, IBlockState clickedState)
	{
		boolean working = isUseable(clickedState);
		
		ItemStack heldStack = player.getHeldItemMainhand();
		//if they have clicked on the part that holds the chapta cards.
		if(clickedState.getBlock() instanceof BlockTotemLathe.Slot)
		{
			if(!card1.isEmpty())
			{
				if(!card2.isEmpty())
				{
					if(player.getHeldItemMainhand().isEmpty())
						player.setHeldItem(EnumHand.MAIN_HAND, card2);
					else if(!player.inventory.addItemStackToInventory(card2))
						dropItem(false, getPos(), card2);
					else player.inventoryContainer.detectAndSendChanges();
					setCard2(ItemStack.EMPTY);
				} else if(working && heldStack.getItem() == MinestuckItems.CAPTCHA_CARD)
				{
					setCard2(heldStack.split(1));
				} else
				{
					if(player.getHeldItemMainhand().isEmpty())
						player.setHeldItem(EnumHand.MAIN_HAND, card1);
					else if(!player.inventory.addItemStackToInventory(card1))
						dropItem(false, getPos(), card1);
					else player.inventoryContainer.detectAndSendChanges();
					setCard1(ItemStack.EMPTY);
				}
			} else if(working && heldStack.getItem() == MinestuckItems.CAPTCHA_CARD)
			{
				setCard1(heldStack.split(1));
			}
		}
		
		//if they have clicked the dowel block
		if(clickedState.getBlock() == MinestuckBlocks.TOTEM_LATHE.ROD || clickedState.getBlock() == MinestuckBlocks.TOTEM_LATHE.DOWEL_ROD)
		{
			ItemStack dowel = getDowel();
			if (dowel.isEmpty())
			{
				if(working && heldStack.getItem() == MinestuckBlocks.CRUXITE_DOWEL.asItem())
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
					player.setHeldItem(EnumHand.MAIN_HAND, dowel);
				else if(!player.inventory.addItemStackToInventory(dowel))
					dropItem(true, getPos().up().offset(getFacing().rotateYCCW(), 2), dowel);
				else player.inventoryContainer.detectAndSendChanges();
				setDowel(ItemStack.EMPTY);
			}
		}
		
		//if they have clicked on the lever
		if(working && clickedState.getBlock() == MinestuckBlocks.TOTEM_LATHE.CARVER)
		{
			//carve the dowel.
			processContents();
		}
	}
	
	private boolean isUseable(IBlockState state)
	{
		IBlockState currentState = getWorld().getBlockState(getPos());
		if(!isBroken())
		{
			checkStates();
			if(isBroken())
				Debug.warnf("Failed to notice a block being broken or misplaced at the totem lathe at %s", getPos());
		}
		
		if(!state.get(BlockTotemLathe.FACING).equals(currentState.get(BlockTotemLathe.FACING)))
			return false;
		return !isBroken();
	}
	
	public void checkStates()
	{
		if(isBroken())
			return;
		EnumFacing facing = getFacing();
		
		if(	//!world.getBlockState(getPos()).equals(MinestuckBlocks.TOTEM_LATHE.CARD_SLOT.getDefaultState().with(BlockTotemLathe.FACING, facing)) ||
			!world.getBlockState(getPos().offset(facing.rotateYCCW(),1)).equals(MinestuckBlocks.TOTEM_LATHE.BOTTOM_LEFT.getDefaultState().with(BlockTotemLathe.FACING, facing)) ||
			!world.getBlockState(getPos().offset(facing.rotateYCCW(),2)).equals(MinestuckBlocks.TOTEM_LATHE.BOTTOM_RIGHT.getDefaultState().with(BlockTotemLathe.FACING, facing)) ||
			!world.getBlockState(getPos().offset(facing.rotateYCCW(),3)).equals(MinestuckBlocks.TOTEM_LATHE.BOTTOM_CORNER.getDefaultState().with(BlockTotemLathe.FACING, facing)) ||
			
			!world.getBlockState(getPos().up()).equals(MinestuckBlocks.TOTEM_LATHE.MIDDLE.getDefaultState().with(BlockTotemLathe.FACING, facing)) ||
			!world.getBlockState(getPos().up().offset(facing.rotateYCCW(),1)).equals(MinestuckBlocks.TOTEM_LATHE.ROD.getDefaultState().with(BlockTotemLathe.FACING, facing)) ||
			!world.getBlockState(getPos().up().offset(facing.rotateYCCW(),3)).equals(MinestuckBlocks.TOTEM_LATHE.WHEEL.getDefaultState().with(BlockTotemLathe.FACING, facing)) ||
			
			!world.getBlockState(getPos().up(2)).equals(MinestuckBlocks.TOTEM_LATHE.TOP_CORNER.getDefaultState().with(BlockTotemLathe.FACING, facing)) ||
			!world.getBlockState(getPos().up(2).offset(facing.rotateYCCW(),1)).equals(MinestuckBlocks.TOTEM_LATHE.TOP.getDefaultState().with(BlockTotemLathe.FACING, facing)) ||
			!world.getBlockState(getPos().up(2).offset(facing.rotateYCCW(),2)).equals(MinestuckBlocks.TOTEM_LATHE.CARVER.getDefaultState().with(BlockTotemLathe.FACING, facing)))
		{
			setBroken();
		}
		
	}
	
	private void dropItem(boolean inBlock, BlockPos pos, ItemStack stack)
	{
		EnumFacing direction = getFacing();
		BlockPos dropPos;
		if(inBlock)
			dropPos = pos;
		else if(!world.getBlockState(pos.offset(direction)).isBlockNormalCube())
			dropPos = pos.offset(direction);
		else dropPos = pos;
		
		InventoryHelper.spawnItemStack(world, dropPos.getX(), dropPos.getY(), dropPos.getZ(), stack);
	}
	
	@Override
	public void read(NBTTagCompound compound)
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
	public NBTTagCompound write(NBTTagCompound compound)
	{
		super.write(compound);
		compound.putBoolean("broken",broken);
		compound.put("card1", card1.write(new NBTTagCompound()));
		compound.put("card2", card2.write(new NBTTagCompound()));
		return compound;
	}
	@Override
	public NBTTagCompound getUpdateTag()
	{
		return write(new NBTTagCompound());
	}
	@Override
	public SPacketUpdateTileEntity getUpdatePacket()
	{
		return new SPacketUpdateTileEntity(this.pos, 0, getUpdateTag());
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
	{
		handleUpdateTag(pkt.getNbtCompound());
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
					output = new ItemStack(MinestuckBlocks.GENERIC_OBJECT);
				else output = CombinationRegistry.getCombination(AlchemyRecipes.getDecodedItem(card1), AlchemyRecipes.getDecodedItem(card2), CombinationRegistry.Mode.MODE_AND);
			else
			{
				ItemStack input = card1.isEmpty() ? card2 : card1;
				if(!input.hasTag() || !input.getTag().getBoolean("punched"))
					output = new ItemStack(MinestuckBlocks.GENERIC_OBJECT);
				else output = AlchemyRecipes.getDecodedItem(input);
			}
			
			if(!output.isEmpty())
			{
				ItemStack outputDowel = output.getItem().equals(MinestuckBlocks.GENERIC_OBJECT.asItem()) ? new ItemStack(MinestuckBlocks.CRUXITE_DOWEL) : AlchemyRecipes.createEncodedItem(output, false);
				ColorCollector.setColor(outputDowel, ColorCollector.getColorFromStack(dowel, -1));
				
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
			EnumFacing direction = getFacing();
			int i = direction.getXOffset() + 1 + (direction.getZOffset() + 1) * 3;
			world.playEvent(2000, pos, i);
		}
	}
}