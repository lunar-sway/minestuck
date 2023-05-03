package com.mraof.minestuck.blockentity;

import com.mraof.minestuck.alchemy.AlchemyHelper;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.block.machine.IntellibeamLaserstationBlock;
import com.mraof.minestuck.item.MSItems;
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

public class IntellibeamLaserstationBlockEntity extends BlockEntity
{
	
	protected ItemStack card = ItemStack.EMPTY;
	protected int EXP_LEVEL_CAPACITY = 15;
	protected int EXPERIENCE_LEVEL = 0;
	
	public IntellibeamLaserstationBlockEntity(BlockPos pos, BlockState state)
	{
		super(MSBlockEntityTypes.INTELLIBEAM_LASERSTATION.get(), pos, state);
	}
	
	public int remainingExpCapacity()
	{
		return Math.max(0, EXP_LEVEL_CAPACITY - EXPERIENCE_LEVEL);
	}
	
	public void attemptAddExperience(Player player)
	{
		if (remainingExpCapacity() >= 0)
		{
			return;
		}
		if (player.experienceLevel <= 0)
		{
			return;
		}
		if (player.getMainHandItem().isEmpty() && !this.hasCard())
		{
			player.giveExperienceLevels(-1);
			EXPERIENCE_LEVEL += 1;
		}
	}
	
	public ItemStack getTaggedItem()
	{
		ItemStack taggedCard = getCard();
		ItemStack taggedItem = new ItemStack(MSBlocks.GENERIC_OBJECT.get());
		
		if (taggedCard.hasTag() && taggedCard.getTag().contains("unreadable"))
			taggedItem = AlchemyHelper.getDecodedItem(taggedCard);
		
		return taggedItem;
	}
	
	public void attemptRemoveUnreadableTag()
	{
		if(remainingExpCapacity() >= 0)
		{
			getTaggedItem().removeTagKey("unreadable");
			EXPERIENCE_LEVEL = 0;
		}
	}
	
	public void attemptTakeCard(Player player)
	{
		if (player.getMainHandItem().isEmpty())
			player.setItemInHand(InteractionHand.MAIN_HAND, card);
		else if (!player.getInventory().add(card))
			dropCard(false, level, worldPosition, card);
		else player.inventoryMenu.broadcastChanges();
		
		insertCard(ItemStack.EMPTY);
	}
	
	public void tryInsertCard(ItemStack heldStack)
	{
		if (!heldStack.isEmpty() && heldStack.getItem() == MSItems.CAPTCHA_CARD.get())
		{
			insertCard(heldStack.split(1));
			ItemStack cardStack = getCard();
			ItemStack item = new ItemStack(MSBlocks.GENERIC_OBJECT.get());
			
			if (cardStack.hasTag() && cardStack.getTag().contains("contentID"))
				item = AlchemyHelper.getDecodedItem(cardStack);
		}
	}
	
	public void onRightClick(Player player)
	{
		if(card.isEmpty())
		{
			ItemStack heldItem = player.getMainHandItem();
			if (card.isEmpty())
			{
				tryInsertCard(heldItem);
			}
			return;
		}
		attemptAddExperience(player);
		attemptRemoveUnreadableTag();
		attemptTakeCard(player);
	}
	
	public void dropCard(boolean inBlock, Level level, BlockPos pos, ItemStack item)
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
	
	public void insertCard(ItemStack card)
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
	
	@Override
	public void load(CompoundTag nbt)
	{
		super.load(nbt);
		insertCard(ItemStack.of(nbt.getCompound("card")));
		
	}
	
	@Override
	public void saveAdditional(CompoundTag compound)
	{
		super.saveAdditional(compound);
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
	
	private void updateState()
	{
		if(level != null && !level.isClientSide)
		{
			BlockState state = level.getBlockState(worldPosition);
			boolean hasCard = !card.isEmpty();
			if(state.hasProperty(IntellibeamLaserstationBlock.HAS_CARD) && hasCard != state.getValue(IntellibeamLaserstationBlock.HAS_CARD)) level.setBlock(worldPosition, state.setValue(IntellibeamLaserstationBlock.HAS_CARD, hasCard), Block.UPDATE_CLIENTS);
		}
	}
}