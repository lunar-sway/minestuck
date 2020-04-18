package com.mraof.minestuck.tileentity;

import com.mraof.minestuck.block.HolopadBlock;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.item.crafting.alchemy.AlchemyHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class HolopadTileEntity extends TileEntity implements ITickableTileEntity
{
	
	public int innerRotation = 0;
	protected ItemStack card = ItemStack.EMPTY;
	
	public HolopadTileEntity()
	{
		super(MSTileEntityTypes.HOLOPAD);
	}
	
	public void onRightClick(PlayerEntity player)
	{
		if(!card.isEmpty())
		{
			if (player.getHeldItemMainhand().isEmpty())
				player.setHeldItem(Hand.MAIN_HAND, card);
			else if (!player.inventory.addItemStackToInventory(card))
				dropItem(false, world, pos, card);
			else player.container.detectAndSendChanges();
			
			setCard(ItemStack.EMPTY);
			return;
		}
		else
		{
			ItemStack heldStack = player.getHeldItemMainhand();
			if (card.isEmpty())
			{
				if (!heldStack.isEmpty() && heldStack.getItem() == MSItems.CAPTCHA_CARD)
				{
					setCard(heldStack.split(1));    //Insert card into the card slot
					ItemStack in = getCard();
					ItemStack item = new ItemStack(MSBlocks.GENERIC_OBJECT);
					
					if (in.hasTag() && in.getTag().contains("contentID"))
						item = AlchemyHelper.getDecodedItem(in);
				}
					
				
			}
		}
	}
	
	public void dropItem(boolean inBlock, World worldIn, BlockPos pos, ItemStack item)
	{
		BlockPos dropPos;
		if(inBlock)
			dropPos = pos;
		else if(!Block.hasSolidSide(worldIn.getBlockState(pos.up()), worldIn, pos.up(), Direction.DOWN))
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
		if (card.getItem() == MSItems.CAPTCHA_CARD || card.isEmpty())
		{
			this.card = card;
			if(world != null)
			{
				updateState();
			}
		}
	}
	
	public ItemStack getCard()
	{
		return this.card;
	}
	
	public ItemStack getHoloItem()
	{
		ItemStack in = getCard();
		ItemStack item = new ItemStack(MSBlocks.GENERIC_OBJECT);
		
		if (in.hasTag() && in.getTag().contains("contentID"))
			item = AlchemyHelper.getDecodedItem(in);
		
		return item;
	}
	
	@Override
	public void read(CompoundNBT compound)
	{
		super.read(compound);
		//broken = tagCompound.getBoolean("broken");
		setCard(ItemStack.read(compound.getCompound("card")));
	}
	
	@Override
	public CompoundNBT write(CompoundNBT compound)
	{
		super.write(compound);
		//tagCompound.setBoolean("broken", this.broken);
		compound.put("card", card.write(new CompoundNBT()));
		return compound;
	}
	
	@Override
	public CompoundNBT getUpdateTag()
	{
		CompoundNBT nbt = super.getUpdateTag();
		nbt.put("card", card.write(new CompoundNBT()));
		return nbt;
	}
	
	@Override
	public SUpdateTileEntityPacket getUpdatePacket()
	{
		SUpdateTileEntityPacket packet;
		packet = new SUpdateTileEntityPacket(this.pos, 0, getUpdateTag());
		return packet;
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt)
	{
		handleUpdateTag(pkt.getNbtCompound());
	}
	
	
	@Override
	public void tick()
	{
		++innerRotation;
	}
	
	private void updateState()
	{
		if(world != null && !world.isRemote)
		{
			BlockState state = world.getBlockState(pos);
			boolean hasCard = !card.isEmpty();
			if(state.has(HolopadBlock.HAS_CARD) && hasCard != state.get(HolopadBlock.HAS_CARD))
				world.setBlockState(pos, state.with(HolopadBlock.HAS_CARD, hasCard), 2);
		}
	}
}