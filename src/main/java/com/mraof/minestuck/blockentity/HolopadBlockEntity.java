package com.mraof.minestuck.blockentity;

import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.block.machine.HolopadBlock;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.alchemy.AlchemyHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class HolopadBlockEntity extends BlockEntity
{
	
	public int innerRotation = 0;
	protected ItemStack card = ItemStack.EMPTY;
	
	public HolopadBlockEntity(BlockPos pos, BlockState state)
	{
		super(MSBlockEntityTypes.HOLOPAD.get(), pos, state);
	}
	
	public void onRightClick(Player player)
	{
		if(!card.isEmpty())
		{
			if (player.getMainHandItem().isEmpty())
				player.setItemInHand(InteractionHand.MAIN_HAND, card);
			else if (!player.getInventory().add(card))
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
				if (!heldStack.isEmpty() && heldStack.getItem() == MSItems.CAPTCHA_CARD.get())
				{
					setCard(heldStack.split(1));    //Insert card into the card slot
					ItemStack in = getCard();
					ItemStack item = new ItemStack(MSBlocks.GENERIC_OBJECT.get());
					
					if (in.hasTag() && in.getTag().contains("contentID"))
						item = AlchemyHelper.getDecodedItem(in);
				}
					
				
			}
		}
	}
	
	public void dropItem(boolean inBlock, Level level, BlockPos pos, ItemStack item)
	{
		BlockPos dropPos;
		if(inBlock)
			dropPos = pos;
		else if(!Block.canSupportCenter(level, pos.above(), Direction.DOWN))
			dropPos = pos.above();
		else dropPos = pos;
		
		Containers.dropItemStack(level, dropPos.getX(), dropPos.getY(), dropPos.getZ(), item);
		
	}
	
	public boolean hasCard()
	{
		return !this.getCard().isEmpty();
	}
	
	public void setCard(ItemStack card)
	{
		if (card.getItem() == MSItems.CAPTCHA_CARD.get() || card.isEmpty())
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
		ItemStack item = new ItemStack(MSBlocks.GENERIC_OBJECT.get());
		
		if (in.hasTag() && in.getTag().contains("contentID"))
			item = AlchemyHelper.getDecodedItem(in);
		
		return item;
	}
	
	@Override
	public void load(CompoundTag nbt)
	{
		super.load(nbt);
		//broken = tagCompound.getBoolean("broken");
		setCard(ItemStack.of(nbt.getCompound("card")));
	}
	
	@Override
	public void saveAdditional(CompoundTag compound)
	{
		super.saveAdditional(compound);
		//tagCompound.setBoolean("broken", this.broken);
		compound.put("card", card.save(new CompoundTag()));
	}
	
	@Override
	public CompoundTag getUpdateTag()
	{
		CompoundTag nbt = super.getUpdateTag();
		nbt.put("card", card.save(new CompoundTag()));
		return nbt;
	}
	
	@Override
	public Packet<ClientGamePacketListener> getUpdatePacket()
	{
		return ClientboundBlockEntityDataPacket.create(this);
	}
	
	public static void clientTick(Level level, BlockPos pos, BlockState state, HolopadBlockEntity blockEntity)
	{
		blockEntity.innerRotation++;
	}
	
	private void updateState()
	{
		if(level != null && !level.isClientSide)
		{
			BlockState state = level.getBlockState(worldPosition);
			boolean hasCard = !card.isEmpty();
			if(state.hasProperty(HolopadBlock.HAS_CARD) && hasCard != state.getValue(HolopadBlock.HAS_CARD))
				level.setBlock(worldPosition, state.setValue(HolopadBlock.HAS_CARD, hasCard), Block.UPDATE_CLIENTS);
		}
	}
}