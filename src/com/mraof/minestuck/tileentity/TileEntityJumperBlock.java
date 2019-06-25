package com.mraof.minestuck.tileentity;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.mraof.minestuck.alchemy.AlchemyRecipes;
import com.mraof.minestuck.block.BlockAlchemiter.EnumParts;
import com.mraof.minestuck.block.BlockAlchemiter;
import com.mraof.minestuck.block.BlockJumperBlock;
import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.item.MinestuckItems;
import com.mraof.minestuck.upgrades.AlchemiterUpgrade;
import com.mraof.minestuck.upgrades.placement.HorizontalPlacement;
import com.mraof.minestuck.upgrades.placement.UpgradePlacementType;
import com.mraof.minestuck.util.Debug;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

public class TileEntityJumperBlock extends TileEntity
{
	private boolean broken = false;
	private AlchemiterUpgrade upgrade[] = new AlchemiterUpgrade[8];
	private int color = -1;
	private int latheUpgradeSlot = -1;
	private TileEntityAlchemiter alchemiter;
	
	
	public TileEntityJumperBlock()
	{
		BlockPos pos = this.pos.offset(getFacing());
		this.alchemiter = (TileEntityAlchemiter) world.getTileEntity(MinestuckBlocks.alchemiter[0].getMainPos(world.getBlockState(pos), pos, world));
	}
	
	
	//Key Methods
	public void onRightClick(EntityPlayer playerIn, IBlockState state, int slot)
	{
		boolean working = isUseable(state);
		ItemStack heldStack = playerIn.getHeldItemMainhand();
		BlockJumperBlock.EnumParts part = BlockJumperBlock.getPart(state);
		if(part == null) return;
		
		//On plug right click
		if(!upgrade[slot].isEmpty())
		{
			//shunt retrieving
			if(playerIn.getHeldItemMainhand().isEmpty())
				playerIn.setHeldItem(EnumHand.MAIN_HAND, upgrade[slot].getShunt());
			else if(!playerIn.inventory.addItemStackToInventory(upgrade[slot].getShunt()))
				dropItem(false, getPos(), upgrade[slot].getShunt());
			else playerIn.inventoryContainer.detectAndSendChanges();
			setUpgrade(AlchemiterUpgrade.EMPTY, slot);
		} else if(working && heldStack.getItem() == MinestuckItems.shunt)
		{
			//shunt adding
			AlchemiterUpgrade upg = AlchemiterUpgrade.getUpgradeFromItem(AlchemyRecipes.getDecodedItem(heldStack));
			setUpgrade(upg, slot);
			heldStack.splitStack(1);
		}
		
		//upgrade checking
		if(alchemiter == null)
			Debug.warnf("Couldn't find TileEntityAlchemiter, found %s instead.", alchemiter);
		else updateUpgrades(playerIn);
	}
	
	public void updateUpgrades(EntityPlayer playerIn)
	{
		BlockAlchemiter.EnumParts[] totemParts = {EnumParts.TOTEM_CORNER, EnumParts.TOTEM_PAD, EnumParts.LOWER_ROD, EnumParts.UPPER_ROD};
		AlchemiterUpgrade[] upgradeList = upgrade;
		int blockCount = 12;
		//class based upgrade block placement
		for(AlchemiterUpgrade i : upgradeList)
		{
			Class<? extends UpgradePlacementType> placeClass = i.getPlacementType();
			if(placeClass != null)
				try {
					Constructor<?> placeCtor = placeClass.getConstructor(BlockPos.class, int.class);
					UpgradePlacementType placeObj = (UpgradePlacementType) placeCtor.newInstance(new Object[] {this, i});
					blockCount = placeObj.placeBlocks(blockCount, playerIn, world);
					
				} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException
						| InvocationTargetException e) {e.printStackTrace();}
				
		}
		
	}
	
	public BlockPos posWrap(BlockPos pos, int i)
	{
		
		return pos.offset(EnumFacing.NORTH, i);
	}
	
	public void checkStates()
	{
		if(alchemiter == null || alchemiter.isBroken())
		{
			setBroken();
			return;
		}
		EnumFacing facing = getFacing();
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
			setBroken();
		
		if(getBroken()) alchemiter.setUpgraded(false, this);
		else			alchemiter.setUpgraded(true, this);
		
	}
	
	public boolean isUseable(IBlockState state)
	{
		IBlockState currentState = getWorld().getBlockState(getPos());
		if(!broken)
		{
			checkStates();
			if(broken)
				Debug.warnf("Failed to notice a block being broken or misplaced at the jumper block at %s", getPos());
		}
		
		if(!state.getValue(BlockJumperBlock.DIRECTION).equals(currentState.getValue(BlockJumperBlock.DIRECTION)))
			return false;
		return !broken;
	}
	
	//Misc.
	public void dropItem(boolean inBlock, BlockPos pos, ItemStack stack)
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
	
	public BlockPos slotToPos(int slot)
	{
		EnumFacing facing = getFacing();
		BlockPos mainPos = getPos();
		BlockPos pos;
		
		switch(slot)
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
	
	//Setters
	public void setBroken(boolean in)
	{
		broken = in;
		if(alchemiter != null)alchemiter.setUpgraded(!in, this);
	}
	
	public void setBroken()								  {setBroken(true);}
	public void setUpgrade(AlchemiterUpgrade in, int slot){upgrade[slot] = in;}
	public void setColor(int in) 					      {color = in;}
	public void setLatheUpgradeSlot(int in) 		      {latheUpgradeSlot = in;}
	public void setAlchemiter(TileEntityAlchemiter in)    {alchemiter = in;}
	
	//Getters
	public boolean getBroken() 						{return broken;}
	public AlchemiterUpgrade getUpgrade(int slot)  {return upgrade[slot];}
	public AlchemiterUpgrade[] getUpgrades()  {return upgrade;}
	public int getColor()							{return color;}
	public int getLatheUpgradeSlot()				{return latheUpgradeSlot;}
	public TileEntityAlchemiter getAlchemiter()		{return alchemiter;}
	
	public EnumFacing getFacing(){return EnumFacing.getHorizontal(getBlockMetadata()%4);}
	
	//NBT and Packets
	@Override
	public void readFromNBT(NBTTagCompound tagCompound)
	{
		super.readFromNBT(tagCompound);
		broken = tagCompound.getBoolean("broken");
		color = tagCompound.getInteger("color");
		latheUpgradeSlot = tagCompound.getInteger("latheUpgradeSlot");
		
		int[] pos = tagCompound.getIntArray("alchemiterPos");
		alchemiter = (TileEntityAlchemiter) world.getTileEntity(new BlockPos(pos[0],pos[1],pos[2]));
		
		for(int i = 0; i < upgrade.length; i++)
		{
			setUpgrade(AlchemiterUpgrade.getUpgradeFromName(tagCompound.getString("upgrade" + i)), i);
		}
		
		
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tagCompound)
	{
		super.writeToNBT(tagCompound);
		tagCompound.setBoolean("broken",broken);
		tagCompound.setInteger("color", color);
		tagCompound.setInteger("latheUpgradeSlot", latheUpgradeSlot);
		
		BlockPos pos = alchemiter.getPos();
		tagCompound.setIntArray("alchemiterPos", new int[] {pos.getX(), pos.getY(), pos.getZ()});
		
		for(int i = 0; i < upgrade.length; i++)
		{
			tagCompound.setString("upgrade" + i, upgrade[i].getName());
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
	
}
