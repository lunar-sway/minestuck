package com.mraof.minestuck.tileentity;

import java.util.List;

import com.mraof.minestuck.alchemy.AlchemyRecipes;
import com.mraof.minestuck.block.BlockHolopad;
import com.mraof.minestuck.block.BlockPunchDesignix;
import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.entity.EntityFrog;
import com.mraof.minestuck.entity.item.EntityHologram;
import com.mraof.minestuck.item.MinestuckItems;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityHolopad extends TileEntity
{

	protected ItemStack card = ItemStack.EMPTY;
	
	public void onRightClick(EntityPlayer player)
	{
		if(!world.isRemote)
		{
			AxisAlignedBB bb = new AxisAlignedBB(pos);
		}
		if(!card.isEmpty())
		{
			if (player.getHeldItemMainhand().isEmpty())
				player.setHeldItem(EnumHand.MAIN_HAND, card);
			else if (!player.inventory.addItemStackToInventory(card))
				dropItem(false, world, pos, card);
			else player.inventoryContainer.detectAndSendChanges();
			
			setCard(ItemStack.EMPTY);
			
			destroyHologram(pos);
			return;
		}
		else
		{
			ItemStack heldStack = player.getHeldItemMainhand();
			if (card.isEmpty())
			{
				if (!heldStack.isEmpty() && heldStack.getItem() == MinestuckItems.captchaCard)
				{
					setCard(heldStack.splitStack(1));    //Insert card into the card slot
					ItemStack in = getCard();
					ItemStack item = new ItemStack(MinestuckBlocks.genericObject);
					
					if (in.hasTagCompound() && in.getTagCompound().hasKey("contentID"))
						item = AlchemyRecipes.getDecodedItem(in);
					
					spawnHologram(pos, item);
				}
					
				
			}
		}
	}
	
	public void spawnHologram(BlockPos pos, ItemStack item)
	{
		if(!world.isRemote)
		{
			AxisAlignedBB bb = new AxisAlignedBB(pos);
			
			boolean bool = world.getEntitiesWithinAABB(EntityHologram.class, bb).isEmpty();
			
			if(bool)
			{
				EntityHologram holo = new EntityHologram(world, item);
				holo.setPosition(pos.getX()+0.5, pos.getY()+0.5, pos.getZ()+0.5);
				world.spawnEntity(holo);
			}
		}
	}
	
	public void destroyHologram(BlockPos pos)
	{
		if(!world.isRemote)
		{
			AxisAlignedBB bb = new AxisAlignedBB(pos);
			List<EntityHologram> list = world.getEntitiesWithinAABB(EntityHologram.class, bb);
			
			for(EntityHologram holo : list)
			{
				world.removeEntity(holo);
			}
		}
	}
	
	public void dropItem(boolean inBlock, World worldIn, BlockPos pos, ItemStack item)
	{
		BlockPos dropPos;
		if(inBlock)
			dropPos = pos;
		else if(!worldIn.getBlockState(pos).isBlockNormalCube())
			dropPos = pos;
		else if(!worldIn.getBlockState(pos.up()).isBlockNormalCube())
			dropPos = pos.up();
		else dropPos = pos;
		
		InventoryHelper.spawnItemStack(worldIn, dropPos.getX(), dropPos.getY(), dropPos.getZ(), item);
		
	}
	
	public boolean hasCard()
	{
		return !this.getCard().isEmpty();
	}
	
	public void setCard(ItemStack card)
	{
		if (card.getItem() == MinestuckItems.captchaCard || card.isEmpty())
		{
			this.card = card;
			if(world != null)
			{
				IBlockState state = world.getBlockState(pos);
				world.notifyBlockUpdate(pos, state, state, 2);
			}
		}
	}
	
	public ItemStack getCard()
	{
		return this.card;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tagCompound)
	{
		super.readFromNBT(tagCompound);
		//broken = tagCompound.getBoolean("broken");
		setCard(new ItemStack(tagCompound.getCompoundTag("card")));
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tagCompound)
	{
		super.writeToNBT(tagCompound);
		//tagCompound.setBoolean("broken", this.broken);
		tagCompound.setTag("card", card.writeToNBT(new NBTTagCompound()));
		return tagCompound;
	}
	
	@Override
	public NBTTagCompound getUpdateTag()
	{
		NBTTagCompound nbt;
		nbt = super.getUpdateTag();
		nbt.setTag("card", card.writeToNBT(new NBTTagCompound()));
		return nbt;
	}
	
	@Override
	public SPacketUpdateTileEntity getUpdatePacket()
	{
		SPacketUpdateTileEntity packet;
		packet = new SPacketUpdateTileEntity(this.pos, 0, getUpdateTag());
		return packet;
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
	{
		handleUpdateTag(pkt.getNbtCompound());
	}
	
	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate)
	{
		return oldState.getBlock() != newSate.getBlock() || oldState.getValue(BlockHolopad.CARD) != newSate.getValue(BlockHolopad.CARD);
	}
}
