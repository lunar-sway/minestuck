package com.mraof.minestuck.tileentity;

import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.entity.item.HologramEntity;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.item.crafting.alchemy.AlchemyRecipes;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class HolopadTileEntity extends TileEntity
{

	protected ItemStack card = ItemStack.EMPTY;
	
	public HolopadTileEntity()
	{
		super(MSTileEntityTypes.HOLOPAD);
	}
	
	public void onRightClick(PlayerEntity player)
	{
		if(!world.isRemote)
		{
			AxisAlignedBB bb = new AxisAlignedBB(pos);
		}
		if(!card.isEmpty())
		{
			if (player.getHeldItemMainhand().isEmpty())
				player.setHeldItem(Hand.MAIN_HAND, card);
			else if (!player.inventory.addItemStackToInventory(card))
				dropItem(false, world, pos, card);
			else player.container.detectAndSendChanges();
			
			setCard(ItemStack.EMPTY);
			
			destroyHologram(pos);
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
			
			boolean bool = world.getEntitiesWithinAABB(HologramEntity.class, bb).isEmpty();
			
			if(bool)
			{
				HologramEntity holo = new HologramEntity(world, item);
				holo.setPosition(pos.getX()+0.5, pos.getY()+0.5, pos.getZ()+0.5);
				world.addEntity(holo);
			}
		}
	}
	
	public void destroyHologram(BlockPos pos)
	{
		if(!world.isRemote)
		{
			AxisAlignedBB bb = new AxisAlignedBB(pos);
			List<HologramEntity> list = world.getEntitiesWithinAABB(HologramEntity.class, bb);
			
			for(HologramEntity holo : list)
			{
				holo.remove();
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
				BlockState state = world.getBlockState(pos);
				world.notifyBlockUpdate(pos, state, state, 2);
			}
		}
	}
	
	public ItemStack getCard()
	{
		return this.card;
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
		CompoundNBT nbt;
		nbt = super.getUpdateTag();
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
}