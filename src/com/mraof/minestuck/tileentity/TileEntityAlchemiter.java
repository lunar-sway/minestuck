package com.mraof.minestuck.tileentity;


import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.block.BlockAlchemiter;
import com.mraof.minestuck.block.BlockAlchemiter.EnumParts;
import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.item.MinestuckItems;
import com.mraof.minestuck.tracker.MinestuckPlayerTracker;
import com.mraof.minestuck.util.*;
import com.mraof.minestuck.util.IdentifierHandler.PlayerIdentifier;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
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

public class TileEntityAlchemiter extends TileEntity
{
	//still private because programming teacher and data protection
	public GristType selectedGrist = GristType.Build;
	private boolean broken = false;
	private ItemStack dowel = ItemStack.EMPTY;
	
	public void setDowel(ItemStack newDowel)
	{
		if (newDowel.getItem() == MinestuckItems.cruxiteDowel || newDowel.isEmpty())
		{
			dowel = newDowel;
			if(world != null)
			{
				IBlockState state = world.getBlockState(pos);
				world.notifyBlockUpdate(pos, state, state, 2);
			}
		}
	}
	
	public ItemStack getDowel()
	{
		return dowel;
		
	}
	
	//checks if the tile enity should work
	public boolean isBroken()
	{
		return broken;
	}
	
	//tells the tile entity to stop working
	public void breakMachine()
	{
		broken = true;		
	}

	
	public void dropItem(boolean inBlock)
	{
		BlockPos dropPos;
		if(inBlock)
			dropPos = this.pos;
		else if(!world.getBlockState(this.pos).isBlockNormalCube())
			dropPos = this.pos;
		else if(!world.getBlockState(this.pos.up()).isBlockNormalCube())
			dropPos = this.pos.up();
		else dropPos = this.pos;
		
		InventoryHelper.spawnItemStack(world, dropPos.getX(), dropPos.getY(), dropPos.getZ(), dowel);
		setDowel(ItemStack.EMPTY);
	}
	
	private boolean checkStates(IBlockState state)
	{
		if(this.broken)
			return false;
		Block[] block=MinestuckBlocks.alchemiter;
		EnumFacing hOffset = getWorld().getBlockState(this.getPos()).getValue(BlockAlchemiter.DIRECTION).rotateY();
		
		if(
			! world.getBlockState(getPos()).getBlock().equals(block[0])||
			! world.getBlockState(getPos().up()).getBlock().equals(block[0])||
			! world.getBlockState(getPos().up(2)).getBlock().equals(block[0])||
			! world.getBlockState(getPos().up(3)).getBlock().equals(block[0])||
			
			! world.getBlockState(getPos().offset(hOffset)).getBlock().equals(block[1])||
			! world.getBlockState(getPos().offset(hOffset,2)).getBlock().equals(block[1])||
			! world.getBlockState(getPos().offset(hOffset,3)).getBlock().equals(block[1])||

			! world.getBlockState(getPos().offset(hOffset.rotateYCCW())).getBlock().equals(block[1])||
			! world.getBlockState(getPos().offset(hOffset.rotateYCCW()).offset(hOffset)).getBlock().equals(block[1])||
			! world.getBlockState(getPos().offset(hOffset.rotateYCCW()).offset(hOffset,2)).getBlock().equals(block[1])||
			! world.getBlockState(getPos().offset(hOffset.rotateYCCW()).offset(hOffset,3)).getBlock().equals(block[1])||
			
			! world.getBlockState(getPos().offset(hOffset.rotateYCCW(),2)).getBlock().equals(block[1])||
			! world.getBlockState(getPos().offset(hOffset.rotateYCCW(),2).offset(hOffset)).getBlock().equals(block[1])||
			! world.getBlockState(getPos().offset(hOffset.rotateYCCW(),2).offset(hOffset,2)).getBlock().equals(block[1])||
			! world.getBlockState(getPos().offset(hOffset.rotateYCCW(),2).offset(hOffset,3)).getBlock().equals(block[1])||
			
			! world.getBlockState(getPos().offset(hOffset.rotateYCCW(),3)).getBlock().equals(block[1])||
			! world.getBlockState(getPos().offset(hOffset.rotateYCCW(),3).offset(hOffset)).getBlock().equals(block[1])||
			! world.getBlockState(getPos().offset(hOffset.rotateYCCW(),3).offset(hOffset,2)).getBlock().equals(block[1])||
			! world.getBlockState(getPos().offset(hOffset.rotateYCCW(),3).offset(hOffset,3)).getBlock().equals(block[1])
	
					) {
			Debug.info(world.getBlockState(getPos().offset(hOffset))+","+world.getBlockState(getPos().down())+","+world.getBlockState(getPos().down().offset(hOffset)));
			return false;
		}
		
		return true;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tagCompound)
	{
		super.readFromNBT(tagCompound);
		
		if(tagCompound.hasKey("gristType"))
			this.selectedGrist = GristType.getTypeFromString(tagCompound.getString("gristType"));
		if(this.selectedGrist == null)
		{
			this.selectedGrist = GristType.Build;
		}
		
		if(tagCompound.hasKey("dowel")) 
			setDowel(new ItemStack(tagCompound.getCompoundTag("dowel")));
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tagCompound)
	{
		super.writeToNBT(tagCompound);
		
		tagCompound.setString("gristType", selectedGrist.getRegistryName().toString());
		
		if(dowel!= null)
			tagCompound.setTag("dowel", dowel.writeToNBT(new NBTTagCompound()));
		
		return tagCompound;
	}
	
	@Override
	public NBTTagCompound getUpdateTag()
	{
		NBTTagCompound nbt;
		nbt = super.getUpdateTag();
		nbt.setBoolean("broken", isBroken());
		nbt.setTag("dowel", dowel.writeToNBT(new NBTTagCompound()));
		return nbt;
	}
	
	@Override
	public SPacketUpdateTileEntity getUpdatePacket()
	{
		SPacketUpdateTileEntity packet = new SPacketUpdateTileEntity(this.pos, 0, getUpdateTag());
		return packet;
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
	{
		handleUpdateTag(pkt.getNbtCompound());
	}
	
	public void onRightClick(EntityPlayer player, IBlockState clickedState)
	{
		if (checkStates(clickedState))
		{
			BlockAlchemiter alchemiter = (BlockAlchemiter) clickedState.getBlock();
			EnumParts part = clickedState.getValue(alchemiter.PART);
			if (part.equals(EnumParts.TOTEM_PAD))
			{
				if (!dowel.isEmpty())
				{    //Remove dowel from pad
					if (player.getHeldItemMainhand().isEmpty())
						player.setHeldItem(EnumHand.MAIN_HAND, dowel);
					else if (!player.inventory.addItemStackToInventory(dowel))
						dropItem(false);
					
					setDowel(ItemStack.EMPTY);
				} else
				{
					ItemStack heldStack = player.getHeldItemMainhand();
					if (!heldStack.isEmpty() && heldStack.getItem() == MinestuckItems.cruxiteDowel)
						setDowel(heldStack.splitStack(1));    //Put a dowel on the pad
				}
			}
		}
	}
	
	public void processContents(int quantity, EntityPlayer player)
	{
		if (quantity == 0)
		{
			return;
		}
		EnumFacing facing = world.getBlockState(pos).getValue(BlockAlchemiter.DIRECTION);
		ItemStack newItem = AlchemyRecipeHandler.getDecodedItem(dowel);
		if (!(dowel.hasTagCompound() && dowel.getTagCompound().hasKey("contentID")))
			newItem = new ItemStack(MinestuckBlocks.genericObject);
		BlockPos pos = this.getPos().offset(facing).offset(facing.rotateY()).up();
		newItem.setCount(quantity);
		GristSet cost = GristRegistry.getGristConversion(newItem);
		
		for (GristAmount amount : cost.getArray())
		{
			cost.setGrist(amount.getType(), amount.getAmount() * quantity);
		}
		
		boolean canAfford = true;
		
		for (GristAmount amount : cost.getArray())
		{
			GristType type = amount.getType();
			//if they dont have enough of said grist type
			if (!(cost.getGrist(type) <= MinestuckPlayerData.getClientGrist().getGrist(type)))
				canAfford = false;
		}
		
		if (canAfford)
		{
			EntityItem item = new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), newItem);
			world.spawnEntity(item);
			if (player != null)
				MinestuckAchievementHandler.onAlchemizedItem(newItem, player);
			
			if (newItem.getItem() == MinestuckItems.captchaCard)
				cost = new GristSet(getSelectedGrist(), MinestuckConfig.cardCost);
			if (newItem.isItemDamaged())
			{
				float multiplier = 1 - newItem.getItem().getDamage(newItem) / ((float) newItem.getMaxDamage());
				for (GristAmount amount : cost.getArray())
				{
					cost.setGrist(amount.getType(), (int) Math.ceil(amount.getAmount() * multiplier));
				}
			}
			PlayerIdentifier pid = IdentifierHandler.encode(player);
			GristHelper.decrease(pid, cost);
			MinestuckPlayerTracker.updateGristCache(pid);
		}
	}

	public GristType getSelectedGrist()
	{
		return selectedGrist;
	}
	
	public void setSelectedGrist(GristType selectedGrist)
	{
		this.selectedGrist = selectedGrist;
	}
	
	public void resendState()
	{
		if(dowel.isEmpty())
		{
			BlockAlchemiter.updateItem(false, world, getPos());
		} else
		{
			BlockAlchemiter.updateItem(true, world, this.getPos());
		}
	}
}