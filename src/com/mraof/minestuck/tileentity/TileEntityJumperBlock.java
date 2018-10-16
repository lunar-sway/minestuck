package com.mraof.minestuck.tileentity;


import com.mraof.minestuck.block.BlockAlchemiter;
import com.mraof.minestuck.block.BlockAlchemiterUpgrades;
import com.mraof.minestuck.block.BlockJumperBlock;
import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.block.BlockAlchemiter.EnumParts;
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



public class TileEntityJumperBlock extends TileEntity
{
	private boolean broken = false;
	private ItemStack upgrade[] = {ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY,ItemStack.EMPTY,ItemStack.EMPTY};
	private int color = -1;
	
	public int getColor()
	{
		return color;
	}
	
	public void setColor(int Color)
	{
		color = Color;
	}
	
	//constructor
	public TileEntityJumperBlock() {}
	//data checking

	public void setUpgrade(ItemStack stack, int id)
	{
		if((stack.getItem() == MinestuckItems.shunt && stack.hasTagCompound() && stack.getTagCompound().hasKey("contentID")) || stack.isEmpty())
		{
			upgrade[id] = stack;
			if(world != null)
			{
				IBlockState state = world.getBlockState(pos);
				world.notifyBlockUpdate(pos, state, state, 2);
			}
		}
	}
	
	@Nonnull
	public ItemStack getUpgrade(int id)
	{
		return upgrade[id];
	}
	
	
	public boolean isBroken()
	{
		return broken;
	}
	
	public void setBroken()
	{
		broken = true;
	}
	
	/*public boolean setDowel(ItemStack stack)
	{
		if(world == null)
			return false;
		EnumFacing facing = getFacing();
		BlockPos pos = getPos().up().offset(facing.rotateYCCW(), 2);
		IBlockState state = world.getBlockState(pos);
		if(stack.isEmpty())
		{
			if(state.equals(BlockJumperBlock.getState(BlockJumperBlock.EnumParts.ROD_RIGHT, facing)))
				world.setBlockToAir(pos);
			return true;
		} else if (stack.getItem() == MinestuckItems.cruxiteDowel)
		{
			if(state.equals(BlockJumperBlock.getState(BlockJumperBlock.EnumParts.ROD_RIGHT, facing)))
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
				world.setBlockState(pos, BlockJumperBlock.getState(BlockJumperBlock.EnumParts.ROD_RIGHT, facing));
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
		if(world.getBlockState(pos).equals(BlockJumperBlock.getState(BlockJumperBlock.EnumParts.ROD_RIGHT, getFacing())))
		{
			TileEntity te = world.getTileEntity(pos);
			if(te instanceof TileEntityItemStack)
			{
				return ((TileEntityItemStack) te).getStack();
			}
		}
		return ItemStack.EMPTY;
		
	}*/
	
	//TODO
	public BlockPos idToPos(int id)
	{
		EnumFacing facing = getFacing();
		BlockPos mainPos = getPos();
		BlockPos pos;
		
		switch(id)
		{
		default: pos = null; break;
		case 0: pos = mainPos.offset(facing.rotateYCCW(), 2); break;
		case 1: pos = mainPos.offset(facing.rotateYCCW(), 1); break;
		case 2: pos = mainPos.offset(facing.getOpposite(), 1).offset(facing.rotateYCCW(), 2);
		case 3: pos = mainPos.offset(facing.getOpposite(), 2).offset(facing.rotateYCCW(), 2);
		case 4: pos = mainPos.offset(facing.getOpposite(), 3).offset(facing.rotateYCCW(), 2);
		case 5: pos = mainPos.offset(facing.getOpposite(), 1).offset(facing.rotateYCCW(), 1);
		case 6: pos = mainPos.offset(facing.getOpposite(), 2).offset(facing.rotateYCCW(), 1);
		case 7: pos = mainPos.offset(facing.getOpposite(), 3).offset(facing.rotateYCCW(), 1);
		
		}
		
		
		return pos;
	}
	
	public EnumFacing getFacing()
	{
		return EnumFacing.getHorizontal(getBlockMetadata()%4);
	}
	
	public void onRightClick(EntityPlayer player, IBlockState clickedState, int id)
	{
		boolean working = isUseable(clickedState);
		
		ItemStack heldStack = player.getHeldItemMainhand();
		BlockJumperBlock.EnumParts part = BlockJumperBlock.getPart(clickedState);
		if(part == null)
			return;
		//if they have clicked on one of the jbe plugs.
		if(part.isPlug() || part.isShunt())
		{
			
				if(!upgrade[id].isEmpty())
				{
					if(player.getHeldItemMainhand().isEmpty())
						player.setHeldItem(EnumHand.MAIN_HAND, upgrade[id]);
					else if(!player.inventory.addItemStackToInventory(upgrade[id]))
						dropItem(false, getPos(), upgrade[id]);
					else player.inventoryContainer.detectAndSendChanges();
					setUpgrade(ItemStack.EMPTY, id);
				} else if(working && heldStack.getItem() == MinestuckItems.shunt)
				{
					setUpgrade(heldStack.splitStack(1), id);
				} 
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
		
		if(!state.getValue(BlockJumperBlock.DIRECTION).equals(currentState.getValue(BlockJumperBlock.DIRECTION)))
			return false;
		return !isBroken();
	}
	
	
	public void checkStates()
	{
		if(isBroken())
			return;
		EnumFacing facing = getFacing();
		BlockPos alchemMainPos = pos;
		BlockPos alchemPos = getPos().offset(facing.rotateY()).offset(facing);
		
		
		for(int i = 0; i <= 5; i++)
		{
			if(world.getBlockState(alchemPos).equals(BlockAlchemiter.getBlockState(EnumParts.TOTEM_CORNER, facing)))
			{	
				System.out.println("found pad base at " + alchemPos);
				alchemMainPos = alchemPos;
				break;
			}
			else
				System.out.println("failed to find pad base at current location: " + alchemPos);
			alchemPos = alchemPos.offset(facing.rotateYCCW(), 3);
			if(world.getBlockState(alchemMainPos.offset(facing.rotateYCCW(), 3)).equals(BlockAlchemiter.getBlockState(EnumParts.TOTEM_CORNER, facing)))
			{
				System.out.println("found pad base at " + alchemPos);
				alchemMainPos = alchemPos;
				break;
			}
			if(world.getBlockState(alchemPos).getBlock() instanceof BlockAlchemiter || world.getBlockState(alchemPos).getBlock() instanceof BlockAlchemiterUpgrades)
				facing = world.getBlockState(alchemPos).getValue(BlockAlchemiter.DIRECTION);
			else
				facing = facing.rotateY();
			System.out.println("failed to find pad base at " + alchemMainPos);
		}
		
		if(alchemMainPos == pos)
		{
			System.out.println("no pad base was found");
			setBroken();
			return;
		}
		else
			alchemMainPos = alchemMainPos.up();
		
		TileEntity alchemTe = world.getTileEntity(alchemMainPos);
		
		
		if(!(alchemTe instanceof TileEntityAlchemiter))
		{
			System.out.println("no alchemiter te found, found " + alchemTe + " instead");
			setBroken();
			return;
		} else
		{
			if(((TileEntityAlchemiter) alchemTe).isBroken())
			{
				System.out.println("alchemiter broken");
				setBroken();
				return;
			}
		}
		if(		!world.getBlockState(getPos()).equals(BlockJumperBlock.getState(BlockJumperBlock.EnumParts.CABLE, facing)) ||
				!world.getBlockState(getPos().offset(facing.getOpposite(),1)).equals(BlockJumperBlock.getState(BlockJumperBlock.EnumParts.CENTER, facing)) ||
				!world.getBlockState(getPos().offset(facing.getOpposite(),2)).equals(BlockJumperBlock.getState(BlockJumperBlock.EnumParts.CENTER, facing)) ||
				!world.getBlockState(getPos().offset(facing.getOpposite(),3)).equals(BlockJumperBlock.getState(BlockJumperBlock.EnumParts.CENTER, facing)) ||
				!world.getBlockState(getPos().offset(facing.getOpposite(),4)).equals(BlockJumperBlock.getState(BlockJumperBlock.EnumParts.BORDER_LEFT, facing)) ||
				
				!world.getBlockState(getPos().offset(facing.rotateYCCW(),1)).equals(BlockJumperBlock.getState(BlockJumperBlock.EnumParts.BOTTOM_CORNER_PLUG, facing)) ||
				!world.getBlockState(getPos().offset(facing.rotateYCCW(),1).offset(facing.getOpposite(),1)).equals(BlockJumperBlock.getState(BlockJumperBlock.EnumParts.BOTTOM_PLUG, facing)) ||
				!world.getBlockState(getPos().offset(facing.rotateYCCW(),1).offset(facing.getOpposite(),2)).equals(BlockJumperBlock.getState(BlockJumperBlock.EnumParts.BOTTOM_PLUG, facing)) ||
				!world.getBlockState(getPos().offset(facing.rotateYCCW(),1).offset(facing.getOpposite(),3)).equals(BlockJumperBlock.getState(BlockJumperBlock.EnumParts.BOTTOM_PLUG, facing)) ||
				!world.getBlockState(getPos().offset(facing.rotateYCCW(),1).offset(facing.getOpposite(),4)).equals(BlockJumperBlock.getState(BlockJumperBlock.EnumParts.BORDER_SIDE, facing)) ||
				
				!world.getBlockState(getPos().offset(facing.rotateYCCW(),2)).equals(BlockJumperBlock.getState(BlockJumperBlock.EnumParts.TOP_CORNER_PLUG, facing)) ||
				!world.getBlockState(getPos().offset(facing.rotateYCCW(),2).offset(facing.getOpposite(),1)).equals(BlockJumperBlock.getState(BlockJumperBlock.EnumParts.TOP_PLUG, facing)) ||
				!world.getBlockState(getPos().offset(facing.rotateYCCW(),2).offset(facing.getOpposite(),2)).equals(BlockJumperBlock.getState(BlockJumperBlock.EnumParts.TOP_PLUG, facing)) ||
				!world.getBlockState(getPos().offset(facing.rotateYCCW(),2).offset(facing.getOpposite(),3)).equals(BlockJumperBlock.getState(BlockJumperBlock.EnumParts.TOP_PLUG, facing)) ||
				!world.getBlockState(getPos().offset(facing.rotateYCCW(),2).offset(facing.getOpposite(),4)).equals(BlockJumperBlock.getState(BlockJumperBlock.EnumParts.BORDER_RIGHT, facing)) ||
				
				!world.getBlockState(getPos().offset(facing.rotateY(),1)).equals(BlockJumperBlock.getState(BlockJumperBlock.EnumParts.BASE_CORNER, facing.rotateY())) ||
				!world.getBlockState(getPos().offset(facing.rotateY(),1).offset(facing.getOpposite(),1)).equals(BlockJumperBlock.getState(BlockJumperBlock.EnumParts.BASE_SIDE, facing.rotateY())) ||
				!world.getBlockState(getPos().offset(facing.rotateY(),1).offset(facing.getOpposite(),2)).equals(BlockJumperBlock.getState(BlockJumperBlock.EnumParts.BASE_SIDE, facing.rotateY())) ||
				!world.getBlockState(getPos().offset(facing.rotateY(),1).offset(facing.getOpposite(),3)).equals(BlockJumperBlock.getState(BlockJumperBlock.EnumParts.BASE_CORNER, facing)) ||
				!world.getBlockState(getPos().offset(facing.rotateY(),1).offset(facing.getOpposite(),4)).equals(BlockJumperBlock.getState(BlockJumperBlock.EnumParts.SMALL_CORNER, facing))  

				)

		{
			System.out.println("JBE broke");
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
		
		for(int i = 0; i < upgrade.length; i++)
		{
			setUpgrade(new ItemStack(tagCompound.getCompoundTag("upgrade" + i)), i);
		}
		
		
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tagCompound)
	{
		super.writeToNBT(tagCompound);
		tagCompound.setBoolean("broken",broken);
		for(int i = 0; i < upgrade.length; i++)
		{
			tagCompound.setTag("upgrade" + i, upgrade[i].writeToNBT(new NBTTagCompound()));
		}
		
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
		return oldState.getBlock() != newSate.getBlock() || oldState.getValue(BlockJumperBlock.PART1) != newSate.getValue(BlockJumperBlock.PART1);
	}
	
	
	/*public void processContents()
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
	}*/
	
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