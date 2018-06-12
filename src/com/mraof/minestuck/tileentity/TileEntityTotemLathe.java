package com.mraof.minestuck.tileentity;


import com.mraof.minestuck.block.BlockTotemLathe;
import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.item.MinestuckItems;
import com.mraof.minestuck.util.AlchemyRecipeHandler;
import com.mraof.minestuck.util.CombinationRegistry;
import com.mraof.minestuck.util.Debug;

import net.minecraft.block.Block;
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
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nonnull;

public class TileEntityTotemLathe extends TileEntity
{
	private boolean broken = false;
	//two cards so that we can preform the && alchemy operation
	protected ItemStack card1 = ItemStack.EMPTY;
	protected ItemStack card2 = ItemStack.EMPTY;
	protected ItemStack dowel = ItemStack.EMPTY;
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
	
	public void setDowel(ItemStack stack)
	{
		if (stack.getItem() == MinestuckItems.cruxiteDowel || stack.isEmpty())
		{
			dowel = stack;
			if(world != null)
			{
			    IBlockState state = world.getBlockState(pos);
			    this.world.notifyBlockUpdate(pos, state, state, 2);
			}
		}
	}
	public ItemStack getDowel()
	{
		return dowel;
		
	}
	
	public void onRightClick(EntityPlayer player, IBlockState clickedState)
	{
		boolean working = checkStates(clickedState);
		
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
					player.inventory.addItemStackToInventory(card2);
					setCard2(ItemStack.EMPTY);
				} else if(working && heldStack.getItem() == MinestuckItems.captchaCard)
				{
					setCard2(heldStack.splitStack(1));
				} else
				{
					player.inventory.addItemStackToInventory(card1);
					setCard1(ItemStack.EMPTY);
				}
			} else if(working && heldStack.getItem() == MinestuckItems.captchaCard)
			{
				setCard1(heldStack.splitStack(1));
			}
		}
		
		//if they have clicked the dowel block
		if (part == BlockTotemLathe.EnumParts.MID_MIDLEFT || part == BlockTotemLathe.EnumParts.MID_MIDRIGHT)
		{
			if (dowel.isEmpty())
			{
				if(working && heldStack.getItem() == MinestuckItems.cruxiteDowel)
				{
					setDowel(heldStack.splitStack(1));
				}
			} else
			{
				player.inventory.addItemStackToInventory(dowel);
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
	
	private boolean checkStates(IBlockState state)
	{
		if(isBroken())
			return false;
		EnumFacing facing = this.getWorld().getBlockState(this.getPos()).getValue(BlockTotemLathe.DIRECTION);
		
		if(	!world.getBlockState(getPos()).equals(BlockTotemLathe.getState(BlockTotemLathe.EnumParts.BOTTOM_LEFT, facing)) ||
			!world.getBlockState(getPos().offset(facing.rotateYCCW(),1)).equals(BlockTotemLathe.getState(BlockTotemLathe.EnumParts.BOTTOM_MIDLEFT, facing)) ||
			!world.getBlockState(getPos().offset(facing.rotateYCCW(),2)).equals(BlockTotemLathe.getState(BlockTotemLathe.EnumParts.BOTTOM_MIDRIGHT, facing)) ||
			!world.getBlockState(getPos().offset(facing.rotateYCCW(),3)).equals(BlockTotemLathe.getState(BlockTotemLathe.EnumParts.BOTTOM_RIGHT, facing)) ||
			
			!world.getBlockState(getPos().up()).equals(BlockTotemLathe.getState(BlockTotemLathe.EnumParts.MID_LEFT, facing)) ||
			!world.getBlockState(getPos().up().offset(facing.rotateYCCW(),1)).equals(BlockTotemLathe.getState(BlockTotemLathe.EnumParts.MID_MIDLEFT, facing)) ||
			!world.getBlockState(getPos().up().offset(facing.rotateYCCW(),2)).equals(BlockTotemLathe.getState(BlockTotemLathe.EnumParts.MID_MIDRIGHT, facing)) ||
			!world.getBlockState(getPos().up().offset(facing.rotateYCCW(),3)).equals(BlockTotemLathe.getState(BlockTotemLathe.EnumParts.MID_RIGHT, facing)) ||
			
			!world.getBlockState(getPos().up(2)).equals(BlockTotemLathe.getState(BlockTotemLathe.EnumParts.TOP_LEFT, facing)) ||
			!world.getBlockState(getPos().up(2).offset(facing.rotateYCCW(),1)).equals(BlockTotemLathe.getState(BlockTotemLathe.EnumParts.TOP_MIDLEFT, facing)) ||
			!world.getBlockState(getPos().up(2).offset(facing.rotateYCCW(),2)).equals(BlockTotemLathe.getState(BlockTotemLathe.EnumParts.TOP_MIDRIGHT, facing)))
		{
			return false;
		}
		
		return true;
	}
	
	
	public void dropCard1(boolean inBlock,BlockPos pos)
	{
		dropItem(inBlock, pos, getCard1());
		setCard1(ItemStack.EMPTY);
	}
	
	public void dropCard2(boolean inBlock,BlockPos pos)
	{
		dropItem(inBlock, pos, getCard2());
		setCard2(ItemStack.EMPTY);
	}
	public void dropDowel(boolean inBlock, BlockPos pos)
	{
		dropItem(inBlock, pos, getDowel());
		setDowel(ItemStack.EMPTY);
	}
	
	private void dropItem(boolean inBlock, BlockPos pos, ItemStack stack)
	{
		EnumFacing direction = inBlock ? null : world.getBlockState(this.pos).getValue(BlockTotemLathe.DIRECTION);
		BlockPos dropPos;
		if(inBlock)
			dropPos = pos;
		else if(!world.getBlockState(pos.offset(direction)).isBlockNormalCube())
			dropPos = pos.offset(direction);
		else if(!world.getBlockState(pos.up()).isBlockNormalCube())
			dropPos = pos.up();
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
		setDowel(new ItemStack(tagCompound.getCompoundTag("dowel")));
		
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tagCompound)
	{
		super.writeToNBT(tagCompound);
		tagCompound.setBoolean("broken",broken);
		tagCompound.setTag("card1", card1.writeToNBT(new NBTTagCompound()));
		tagCompound.setTag("card2", card2.writeToNBT(new NBTTagCompound()));
		tagCompound.setTag("dowel", dowel.writeToNBT(new NBTTagCompound()));
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
	
	public void processContents()
	{
	
		ItemStack output;
		if(!dowel.isEmpty())
		{
			if(!card1.isEmpty() && !card2.isEmpty())
				if(!card1.hasTagCompound() || !card1.getTagCompound().getBoolean("punched") || !card2.hasTagCompound() || !card2.getTagCompound().getBoolean("punched"))
					output = new ItemStack(MinestuckBlocks.genericObject);
				else output = CombinationRegistry.getCombination(AlchemyRecipeHandler.getDecodedItem(card1), AlchemyRecipeHandler.getDecodedItem(card2), CombinationRegistry.Mode.MODE_AND);
			else
			{
				ItemStack input = card1.isEmpty() ? card2 : card1;
				if(!input.hasTagCompound() || !input.getTagCompound().getBoolean("punched"))
					output = new ItemStack(MinestuckBlocks.genericObject);
				else output = AlchemyRecipeHandler.getDecodedItem(input);
			}
			
			ItemStack outputDowel = output.getItem().equals(Item.getItemFromBlock(MinestuckBlocks.genericObject)) ? new ItemStack(MinestuckItems.cruxiteDowel) : AlchemyRecipeHandler.createEncodedItem(output, false);
			outputDowel.setItemDamage(dowel.getItemDamage());
		
			setDowel(outputDowel);
		}
	}
}