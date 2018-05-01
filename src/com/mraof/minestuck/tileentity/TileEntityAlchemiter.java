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
import net.minecraft.world.World;

public class TileEntityAlchemiter extends TileEntity
{
	private GristType selectedGrist = GristType.Build;
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
	
	/**
	 * @return true if the machine is marked as broken
	 */
	public boolean isBroken()
	{
		return broken;
	}
	
	//tells the tile entity to stop working
	public void breakMachine()
	{
		broken = true;
		if(world != null)
		{
			IBlockState state = world.getBlockState(pos);
			world.notifyBlockUpdate(pos, state, state, 2);
		}
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
		
		EnumFacing facing = getWorld().getBlockState(this.getPos()).getValue(BlockAlchemiter.DIRECTION);
		BlockPos pos = getPos().down();
		if(!world.getBlockState(pos.up(3)).equals(BlockAlchemiter.getBlockState(EnumParts.UPPER_ROD, facing)) ||
				!world.getBlockState(pos.up(2)).equals(BlockAlchemiter.getBlockState(EnumParts.LOWER_ROD, facing)) ||
				!world.getBlockState(pos.up()).equals(BlockAlchemiter.getBlockState(EnumParts.TOTEM_PAD, facing)) ||
				!world.getBlockState(pos).equals(BlockAlchemiter.getBlockState(EnumParts.TOTEM_CORNER, facing)) ||
				!world.getBlockState(pos.offset(facing.rotateY())).equals(BlockAlchemiter.getBlockState(EnumParts.SIDE_LEFT, facing)) ||
				!world.getBlockState(pos.offset(facing.rotateY(), 2)).equals(BlockAlchemiter.getBlockState(EnumParts.SIDE_RIGHT, facing)) ||
				!world.getBlockState(pos.offset(facing).offset(facing.rotateY())).equals(BlockAlchemiter.getBlockState(EnumParts.CENTER_PAD, facing)) ||
				!world.getBlockState(pos.offset(facing.rotateY(), 3)).equals(BlockAlchemiter.getBlockState(EnumParts.CORNER, facing)) ||
				!world.getBlockState(pos.offset(facing).offset(facing.rotateY(), 3)).equals(BlockAlchemiter.getBlockState(EnumParts.SIDE_LEFT, facing.rotateYCCW())) ||
				!world.getBlockState(pos.offset(facing, 2).offset(facing.rotateY(), 3)).equals(BlockAlchemiter.getBlockState(EnumParts.SIDE_RIGHT, facing.rotateYCCW())) ||
				!world.getBlockState(pos.offset(facing).offset(facing.rotateY(), 2)).equals(BlockAlchemiter.getBlockState(EnumParts.CENTER_PAD, facing.rotateYCCW())) ||
				!world.getBlockState(pos.offset(facing, 3).offset(facing.rotateY(), 3)).equals(BlockAlchemiter.getBlockState(EnumParts.CORNER, facing.rotateYCCW())) ||
				!world.getBlockState(pos.offset(facing, 3).offset(facing.rotateY(), 2)).equals(BlockAlchemiter.getBlockState(EnumParts.SIDE_LEFT, facing.getOpposite())) ||
				!world.getBlockState(pos.offset(facing, 3).offset(facing.rotateY(), 1)).equals(BlockAlchemiter.getBlockState(EnumParts.SIDE_RIGHT, facing.getOpposite())) ||
				!world.getBlockState(pos.offset(facing, 2).offset(facing.rotateY(), 2)).equals(BlockAlchemiter.getBlockState(EnumParts.CENTER_PAD, facing.getOpposite())) ||
				!world.getBlockState(pos.offset(facing, 3)).equals(BlockAlchemiter.getBlockState(EnumParts.CORNER, facing.getOpposite())) ||
				!world.getBlockState(pos.offset(facing, 2)).equals(BlockAlchemiter.getBlockState(EnumParts.SIDE_LEFT, facing.rotateY())) ||
				!world.getBlockState(pos.offset(facing)).equals(BlockAlchemiter.getBlockState(EnumParts.SIDE_RIGHT, facing.rotateY())) ||
				!world.getBlockState(pos.offset(facing, 2).offset(facing.rotateY(), 1)).equals(BlockAlchemiter.getBlockState(EnumParts.CENTER_PAD, facing.rotateY())))
		{
			breakMachine();
			Debug.warnf("Failed to notice a block being broken or misplaced at the alchemiter at %s", getPos());
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
		
		broken = tagCompound.getBoolean("broken");
		
		if(tagCompound.hasKey("dowel")) 
			setDowel(new ItemStack(tagCompound.getCompoundTag("dowel")));
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tagCompound)
	{
		super.writeToNBT(tagCompound);
		
		tagCompound.setString("gristType", selectedGrist.getRegistryName().toString());
		
		tagCompound.setBoolean("broken", isBroken());
		
		if(dowel!= null)
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
					else player.inventoryContainer.detectAndSendChanges();
					
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
}