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
		return AlchemyRecipes.getDecodedItem(upgrade[id]);
	}
	
	
	public boolean isBroken()
	{
		return broken;
	}
	
	public void setBroken()
	{
		//System.out.println("thing!");
		TileEntity te = world.getTileEntity(alchemiterMainPos());
		if(te instanceof TileEntityAlchemiter)
		{
			TileEntityAlchemiter alchemTe = (TileEntityAlchemiter) te;
			//System.out.println("thing broke");
			alchemTe.setUpgraded(false, getPos());
			System.out.println("alchemiter upgrade now set to " + alchemTe.isUpgraded());
		}
		else Debug.warnf("Couldn't find TileEntityAlchemiter at %s, found %s instead.", alchemiterMainPos(), te);
		broken = true;
	}
	
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
		EnumFacing facing = getFacing();
		BlockPos alchemMainPos = alchemiterMainPos(facing, pos);
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
		
		TileEntity te = world.getTileEntity(alchemMainPos);
		if(te instanceof TileEntityAlchemiter)
		{
			TileEntityAlchemiter alchemTe = (TileEntityAlchemiter) te;
			alchemTe.setUpgraded(true, pos);
		}
		else Debug.warnf("Couldn't find TileEntityAlchemiter at %s, found %s instead.", alchemMainPos, te);
		
		
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
	
	public BlockPos alchemiterMainPos()
	{
		return alchemiterMainPos(getFacing(), pos);
	}
	
	public static BlockPos staticAlchemiterMainPos(EnumFacing facing, BlockPos pos, World world)
	{
		BlockPos alchemPos = pos.offset(facing.rotateY()).offset(facing);
		
		
		for(int i = 0; i <= 5; i++)
		{
			if(world.getBlockState(alchemPos).equals(BlockAlchemiter.getBlockState(EnumParts.TOTEM_CORNER, facing)))
			{	
				pos = alchemPos.up();
				break;
			}
			else
			alchemPos = alchemPos.offset(facing.rotateYCCW(), 3);
			if(world.getBlockState(pos.offset(facing.rotateYCCW(), 3)).equals(BlockAlchemiter.getBlockState(EnumParts.TOTEM_CORNER, facing)))
			{
				pos = alchemPos.up();
				break;
			}
			if(world.getBlockState(alchemPos).getBlock() instanceof BlockAlchemiter || world.getBlockState(alchemPos).getBlock() instanceof BlockAlchemiterUpgrades)
				facing = world.getBlockState(alchemPos).getValue(BlockAlchemiter.DIRECTION);
			else
				facing = facing.rotateY();
		}
		
		return pos;
	}
	
	public BlockPos alchemiterMainPos(EnumFacing facing, BlockPos pos)
	{
		BlockPos alchemPos = getPos().offset(facing.rotateY()).offset(facing);
		
		
		for(int i = 0; i <= 5; i++)
		{
			if(world.getBlockState(alchemPos).equals(BlockAlchemiter.getBlockState(EnumParts.TOTEM_CORNER, facing)))
			{	
				pos = alchemPos.up();
				break;
			}
			else
			alchemPos = alchemPos.offset(facing.rotateYCCW(), 3);
			if(world.getBlockState(pos.offset(facing.rotateYCCW(), 3)).equals(BlockAlchemiter.getBlockState(EnumParts.TOTEM_CORNER, facing)))
			{
				pos = alchemPos.up();
				break;
			}
			if(world.getBlockState(alchemPos).getBlock() instanceof BlockAlchemiter || world.getBlockState(alchemPos).getBlock() instanceof BlockAlchemiterUpgrades)
				facing = world.getBlockState(alchemPos).getValue(BlockAlchemiter.DIRECTION);
			else
				facing = facing.rotateY();
		}
		
		return pos;
	}
	
	public void checkStates()
	{
		if(isBroken())
			return;
		EnumFacing facing = getFacing();
		BlockPos alchemMainPos = alchemiterMainPos(facing, pos);
		
		if(alchemMainPos == pos)
		{
			setBroken();
			return;
		}
		
		TileEntity alchemTe = world.getTileEntity(alchemMainPos);
		
		
		if(!(alchemTe instanceof TileEntityAlchemiter))
		{
			setBroken();
			return;
		} else
		{
			if(((TileEntityAlchemiter) alchemTe).isBroken())
			{
				setBroken();
				return;
			}
		}
		facing = getFacing();
		if(		!world.getBlockState(getPos()).equals(BlockJumperBlock.getState(BlockJumperBlock.EnumParts.CABLE, facing)) ||
				!world.getBlockState(getPos().offset(facing.getOpposite(),1)).equals(BlockJumperBlock.getState(BlockJumperBlock.EnumParts.CENTER, facing)) ||
				!world.getBlockState(getPos().offset(facing.getOpposite(),2)).equals(BlockJumperBlock.getState(BlockJumperBlock.EnumParts.CENTER, facing)) ||
				!world.getBlockState(getPos().offset(facing.getOpposite(),3)).equals(BlockJumperBlock.getState(BlockJumperBlock.EnumParts.CENTER, facing)) ||
				!world.getBlockState(getPos().offset(facing.getOpposite(),4)).equals(BlockJumperBlock.getState(BlockJumperBlock.EnumParts.BORDER_LEFT, facing)) ||				
				
				!(world.getBlockState(getPos().offset(facing.rotateYCCW(),1)).equals(BlockJumperBlock.getState(BlockJumperBlock.EnumParts.BOTTOM_CORNER_PLUG, facing)) || world.getBlockState(getPos().offset(facing.rotateYCCW(),1)).equals(BlockJumperBlock.getState(BlockJumperBlock.EnumParts.BOTTOM_CORNER_SHUNT, facing))) ||
				!(world.getBlockState(getPos().offset(facing.rotateYCCW(),1).offset(facing.getOpposite(),1)).equals(BlockJumperBlock.getState(BlockJumperBlock.EnumParts.BOTTOM_PLUG, facing)) || world.getBlockState(getPos().offset(facing.rotateYCCW(),1)).equals(BlockJumperBlock.getState(BlockJumperBlock.EnumParts.BOTTOM_SHUNT, facing))) ||
				!(world.getBlockState(getPos().offset(facing.rotateYCCW(),1).offset(facing.getOpposite(),2)).equals(BlockJumperBlock.getState(BlockJumperBlock.EnumParts.BOTTOM_PLUG, facing)) || world.getBlockState(getPos().offset(facing.rotateYCCW(),1)).equals(BlockJumperBlock.getState(BlockJumperBlock.EnumParts.BOTTOM_SHUNT, facing))) ||
				!(world.getBlockState(getPos().offset(facing.rotateYCCW(),1).offset(facing.getOpposite(),3)).equals(BlockJumperBlock.getState(BlockJumperBlock.EnumParts.BOTTOM_PLUG, facing)) || world.getBlockState(getPos().offset(facing.rotateYCCW(),1)).equals(BlockJumperBlock.getState(BlockJumperBlock.EnumParts.BOTTOM_SHUNT, facing))) ||
				!world.getBlockState(getPos().offset(facing.rotateYCCW(),1).offset(facing.getOpposite(),4)).equals(BlockJumperBlock.getState(BlockJumperBlock.EnumParts.BORDER_SIDE, facing)) ||
				
				!(world.getBlockState(getPos().offset(facing.rotateYCCW(),2)).equals(BlockJumperBlock.getState(BlockJumperBlock.EnumParts.TOP_CORNER_PLUG, facing)) || world.getBlockState(getPos().offset(facing.rotateYCCW(),1)).equals(BlockJumperBlock.getState(BlockJumperBlock.EnumParts.TOP_CORNER_SHUNT, facing))) ||
				!(world.getBlockState(getPos().offset(facing.rotateYCCW(),2).offset(facing.getOpposite(),1)).equals(BlockJumperBlock.getState(BlockJumperBlock.EnumParts.TOP_PLUG, facing)) || world.getBlockState(getPos().offset(facing.rotateYCCW(),1)).equals(BlockJumperBlock.getState(BlockJumperBlock.EnumParts.TOP_SHUNT, facing))) ||
				!(world.getBlockState(getPos().offset(facing.rotateYCCW(),2).offset(facing.getOpposite(),2)).equals(BlockJumperBlock.getState(BlockJumperBlock.EnumParts.TOP_PLUG, facing)) || world.getBlockState(getPos().offset(facing.rotateYCCW(),1)).equals(BlockJumperBlock.getState(BlockJumperBlock.EnumParts.TOP_SHUNT, facing))) ||
				!(world.getBlockState(getPos().offset(facing.rotateYCCW(),2).offset(facing.getOpposite(),3)).equals(BlockJumperBlock.getState(BlockJumperBlock.EnumParts.TOP_PLUG, facing)) || world.getBlockState(getPos().offset(facing.rotateYCCW(),1)).equals(BlockJumperBlock.getState(BlockJumperBlock.EnumParts.TOP_SHUNT, facing))) ||
				!world.getBlockState(getPos().offset(facing.rotateYCCW(),2).offset(facing.getOpposite(),4)).equals(BlockJumperBlock.getState(BlockJumperBlock.EnumParts.BORDER_RIGHT, facing)) ||
				
				!world.getBlockState(getPos().offset(facing.rotateY(),1)).equals(BlockJumperBlock.getState(BlockJumperBlock.EnumParts.BASE_CORNER, facing.rotateYCCW())) ||
				!world.getBlockState(getPos().offset(facing.rotateY(),1).offset(facing.getOpposite(),1)).equals(BlockJumperBlock.getState(BlockJumperBlock.EnumParts.BASE_SIDE, facing.rotateYCCW())) ||
				!world.getBlockState(getPos().offset(facing.rotateY(),1).offset(facing.getOpposite(),2)).equals(BlockJumperBlock.getState(BlockJumperBlock.EnumParts.BASE_SIDE, facing.rotateYCCW())) ||
				!world.getBlockState(getPos().offset(facing.rotateY(),1).offset(facing.getOpposite(),3)).equals(BlockJumperBlock.getState(BlockJumperBlock.EnumParts.BASE_CORNER, facing)) ||
				!world.getBlockState(getPos().offset(facing.rotateY(),1).offset(facing.getOpposite(),4)).equals(BlockJumperBlock.getState(BlockJumperBlock.EnumParts.SMALL_CORNER, facing))  

				)

		{
			setBroken();
		}
		
		if(isBroken())((TileEntityAlchemiter) alchemTe).setUpgraded(false, pos);
		else ((TileEntityAlchemiter) alchemTe).setUpgraded(true, pos);
		
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