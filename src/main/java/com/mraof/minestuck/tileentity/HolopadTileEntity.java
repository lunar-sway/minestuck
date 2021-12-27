package com.mraof.minestuck.tileentity;

import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.block.machine.HolopadBlock;
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
import net.minecraftforge.common.util.Constants;

public class HolopadTileEntity extends TileEntity implements ITickableTileEntity
{
	
	public int innerRotation = 0;
	protected ItemStack card = ItemStack.EMPTY;
	
	public HolopadTileEntity()
	{
		super(MSTileEntityTypes.HOLOPAD.get());
	}
	
	public void onRightClick(PlayerEntity player)
	{
		if(!card.isEmpty())
		{
			if (player.getMainHandItem().isEmpty())
				player.setItemInHand(Hand.MAIN_HAND, card);
			else if (!player.inventory.add(card))
				dropItem(false, level, worldPosition, card);
			else player.inventoryMenu.broadcastChanges();
			
			setCard(ItemStack.EMPTY);
			return;
		}
		else
		{
			ItemStack heldStack = player.getMainHandItem();
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
		else if(!Block.canSupportCenter(worldIn, pos.above(), Direction.DOWN))
			dropPos = pos.above();
		else dropPos = pos;
		
		InventoryHelper.dropItemStack(worldIn, dropPos.getX(), dropPos.getY(), dropPos.getZ(), item);
		
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
			if(level != null)
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
	public void load(BlockState state, CompoundNBT nbt)
	{
		super.load(state, nbt);
		//broken = tagCompound.getBoolean("broken");
		setCard(ItemStack.of(nbt.getCompound("card")));
	}
	
	@Override
	public CompoundNBT save(CompoundNBT compound)
	{
		super.save(compound);
		//tagCompound.setBoolean("broken", this.broken);
		compound.put("card", card.save(new CompoundNBT()));
		return compound;
	}
	
	@Override
	public CompoundNBT getUpdateTag()
	{
		CompoundNBT nbt = super.getUpdateTag();
		nbt.put("card", card.save(new CompoundNBT()));
		return nbt;
	}
	
	@Override
	public SUpdateTileEntityPacket getUpdatePacket()
	{
		SUpdateTileEntityPacket packet;
		packet = new SUpdateTileEntityPacket(this.worldPosition, 0, getUpdateTag());
		return packet;
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt)
	{
		handleUpdateTag(getBlockState(), pkt.getTag());
	}
	
	
	@Override
	public void tick()
	{
		++innerRotation;
	}
	
	private void updateState()
	{
		if(level != null && !level.isClientSide)
		{
			BlockState state = level.getBlockState(worldPosition);
			boolean hasCard = !card.isEmpty();
			if(state.hasProperty(HolopadBlock.HAS_CARD) && hasCard != state.getValue(HolopadBlock.HAS_CARD))
				level.setBlock(worldPosition, state.setValue(HolopadBlock.HAS_CARD, hasCard), Constants.BlockFlags.BLOCK_UPDATE);
		}
	}
}