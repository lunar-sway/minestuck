package com.mraof.minestuck.tileentity;


import com.mraof.minestuck.block.BlockTotemLathe;
import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.item.MinestuckItems;
import com.mraof.minestuck.alchemy.AlchemyRecipes;
import com.mraof.minestuck.alchemy.CombinationRegistry;

import com.mraof.minestuck.util.Debug;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class TileEntityTotemLathe extends TileEntity
{
	private boolean broken = false;
	//two cards so that we can preform the && alchemy operation
	protected ItemStack card1 = ItemStack.EMPTY;
	protected ItemStack card2 = ItemStack.EMPTY;
	//constructor
	public TileEntityTotemLathe() {}
	//data checking

	public void setCard1(ItemStack stack)
	{
		if(stack.getItem() == MinestuckItems.captchaCard || stack.isEmpty())
		{
			card1 = stack;
			if(world != null)
			{
				IBlockState state = world.getBlockState(pos);
				world.notifyBlockUpdate(pos, state, state, 2);
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
		if(stack.getItem() == MinestuckItems.captchaCard || stack.isEmpty())
		{
			card2 = stack;
			if(world != null)
			{
				IBlockState state = world.getBlockState(pos);
				world.notifyBlockUpdate(pos, state, state, 2);
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
			if(state.equals(BlockTotemLathe.getState(BlockTotemLathe.EnumParts.ROD_RIGHT, facing)))
				world.setBlockToAir(pos);
			return true;
		} else if (stack.getItem() == MinestuckItems.cruxiteDowel)
		{
			if(state.equals(BlockTotemLathe.getState(BlockTotemLathe.EnumParts.ROD_RIGHT, facing)))
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
			} else if(state.getBlock().isReplaceable(world, pos))
			{
				world.setBlockState(pos, BlockTotemLathe.getState(BlockTotemLathe.EnumParts.ROD_RIGHT, facing));
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
		if(world.getBlockState(pos).equals(BlockTotemLathe.getState(BlockTotemLathe.EnumParts.ROD_RIGHT, getFacing())))
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
		return EnumFacing.getHorizontal(getBlockMetadata()%4);
	}
	
	public void onRightClick(EntityPlayer player, IBlockState clickedState)
	{
		boolean working = isUseable(clickedState);
		
		ItemStack heldStack = player.getHeldItemMainhand();
		BlockTotemLathe.EnumParts part = BlockTotemLathe.getPart(clickedState);
		if(part == null)
			return;
		//if they have clicked on the part that holds the chapta cards.
		if(part.isBottomLeft())
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
				} else if(working && heldStack.getItem() == MinestuckItems.captchaCard)
				{
					setCard2(heldStack.splitStack(1));
				} else
				{
					if(player.getHeldItemMainhand().isEmpty())
						player.setHeldItem(EnumHand.MAIN_HAND, card1);
					else if(!player.inventory.addItemStackToInventory(card1))
						dropItem(false, getPos(), card1);
					else player.inventoryContainer.detectAndSendChanges();
					setCard1(ItemStack.EMPTY);
				}
			} else if(working && heldStack.getItem() == MinestuckItems.captchaCard)
			{
				setCard1(heldStack.splitStack(1));
			}
		}
		
		//if they have clicked the dowel block
		if (part == BlockTotemLathe.EnumParts.ROD_LEFT || part == BlockTotemLathe.EnumParts.ROD_RIGHT)
		{
			ItemStack dowel = getDowel();
			if (dowel.isEmpty())
			{
				if(working && heldStack.getItem() == MinestuckItems.cruxiteDowel)
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
		if(working && part == BlockTotemLathe.EnumParts.TOP_MIDRIGHT)
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
		
		if(!state.getValue(BlockTotemLathe.DIRECTION).equals(currentState.getValue(BlockTotemLathe.DIRECTION)))
			return false;
		return !isBroken();
	}
	
	public void checkStates()
	{
		if(isBroken())
			return;
		EnumFacing facing = getFacing();
		
		if(	!world.getBlockState(getPos()).equals(BlockTotemLathe.getState(BlockTotemLathe.EnumParts.BOTTOM_LEFT, facing)) ||
			!world.getBlockState(getPos().offset(facing.rotateYCCW(),1)).equals(BlockTotemLathe.getState(BlockTotemLathe.EnumParts.BOTTOM_MIDLEFT, facing)) ||
			!world.getBlockState(getPos().offset(facing.rotateYCCW(),2)).equals(BlockTotemLathe.getState(BlockTotemLathe.EnumParts.BOTTOM_MIDRIGHT, facing)) ||
			!world.getBlockState(getPos().offset(facing.rotateYCCW(),3)).equals(BlockTotemLathe.getState(BlockTotemLathe.EnumParts.BOTTOM_RIGHT, facing)) ||
			
			!world.getBlockState(getPos().up()).equals(BlockTotemLathe.getState(BlockTotemLathe.EnumParts.MID_LEFT, facing)) ||
			!world.getBlockState(getPos().up().offset(facing.rotateYCCW(),1)).equals(BlockTotemLathe.getState(BlockTotemLathe.EnumParts.ROD_LEFT, facing)) ||
			!world.getBlockState(getPos().up().offset(facing.rotateYCCW(),3)).equals(BlockTotemLathe.getState(BlockTotemLathe.EnumParts.MID_RIGHT, facing)) ||
			
			!world.getBlockState(getPos().up(2)).equals(BlockTotemLathe.getState(BlockTotemLathe.EnumParts.TOP_LEFT, facing)) ||
			!world.getBlockState(getPos().up(2).offset(facing.rotateYCCW(),1)).equals(BlockTotemLathe.getState(BlockTotemLathe.EnumParts.TOP_MIDLEFT, facing)) ||
			!world.getBlockState(getPos().up(2).offset(facing.rotateYCCW(),2)).equals(BlockTotemLathe.getState(BlockTotemLathe.EnumParts.TOP_MIDRIGHT, facing)))
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
	public void readFromNBT(NBTTagCompound tagCompound)
	{
		super.readFromNBT(tagCompound);
		broken = tagCompound.getBoolean("broken");
		setCard1(new ItemStack(tagCompound.getCompoundTag("card1")));
		setCard2(new ItemStack(tagCompound.getCompoundTag("card2")));
		
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tagCompound)
	{
		super.writeToNBT(tagCompound);
		tagCompound.setBoolean("broken",broken);
		tagCompound.setTag("card1", card1.writeToNBT(new NBTTagCompound()));
		tagCompound.setTag("card2", card2.writeToNBT(new NBTTagCompound()));
		return tagCompound;
	}
	@Override
	public NBTTagCompound getUpdateTag()
	{
		return writeToNBT(new NBTTagCompound());
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
	
	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate)
	{
		return oldState.getBlock() != newSate.getBlock() || oldState.getValue(BlockTotemLathe.PART1) != newSate.getValue(BlockTotemLathe.PART1);
	}
	
	
	public void processContents()
	{
		ItemStack dowel = getDowel();
		ItemStack output;
		boolean success = false;
		if(!dowel.isEmpty() && !AlchemyRecipes.hasDecodedItem(dowel) &&  (!card1.isEmpty() || !card2.isEmpty()))
		{
			if(!card1.isEmpty() && !card2.isEmpty())
				if(!card1.hasTagCompound() || !card1.getTagCompound().getBoolean("punched") || !card2.hasTagCompound() || !card2.getTagCompound().getBoolean("punched"))
					output = new ItemStack(MinestuckBlocks.genericObject);
				else output = CombinationRegistry.getCombination(AlchemyRecipes.getDecodedItem(card1), AlchemyRecipes.getDecodedItem(card2), CombinationRegistry.Mode.MODE_AND);
			else
			{
				ItemStack input = card1.isEmpty() ? card2 : card1;
				if(!input.hasTagCompound() || !input.getTagCompound().getBoolean("punched"))
					output = new ItemStack(MinestuckBlocks.genericObject);
				else output = AlchemyRecipes.getDecodedItem(input);
			}
			
			if(!output.isEmpty())
			{
				ItemStack outputDowel = output.getItem().equals(Item.getItemFromBlock(MinestuckBlocks.genericObject)) ? new ItemStack(MinestuckItems.cruxiteDowel) : AlchemyRecipes.createEncodedItem(output, false);
				outputDowel.setItemDamage(dowel.getItemDamage());
				
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
			int i = direction.getFrontOffsetX() + 1 + (direction.getFrontOffsetZ() + 1) * 3;
			world.playEvent(2000, pos, i);
		}
	}
}