package com.mraof.minestuck.blockentity;

import com.mraof.minestuck.alchemy.AlchemyHelper;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.block.machine.IntellibeamLaserstationBlock;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.util.MSSoundEvents;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundSource;
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
	protected int EXP_LEVEL_CAPACITY = 10;
	protected int EXPERIENCE_LEVEL = 0;
	protected float SOUND_SCALER = 0F;
	protected static int WAIT_TIMER = 0;
	
	public IntellibeamLaserstationBlockEntity(BlockPos pos, BlockState state)
	{
		super(MSBlockEntityTypes.INTELLIBEAM_LASERSTATION.get(), pos, state);
	}
	
	public void onRightClick(Player player)
	{
		ItemStack heldItem = player.getMainHandItem();
		ItemStack itemInsideCard = AlchemyHelper.getDecodedItem(heldItem);
		
		if(WAIT_TIMER <= 0)
		{
			return;
		}
		if(!card.isEmpty() && player.isShiftKeyDown())
		{
			takeCard(player);
			WAIT_TIMER = 20;
			return;
		}
		if(AlchemyHelper.isReadableCard(card))
		{
			this.level.playSound(null, this.worldPosition, MSSoundEvents.INTELLIBEAM_LAZERSTATION_REMOVE_CARD.get(), SoundSource.BLOCKS, 0.5F, 0.1F);
			WAIT_TIMER = 20;
			return;
		}
		if(card.isEmpty() && !AlchemyHelper.isReadableCard(itemInsideCard) && WAIT_TIMER <= 0)
		{
			tryInsertCard(heldItem);
			WAIT_TIMER = 20;
			return;
		}
		if(EXPERIENCE_LEVEL >= EXP_LEVEL_CAPACITY)
		{
			applyDecodedTag(card);
			takeCard(player);
			
			EXPERIENCE_LEVEL = 0;
			SOUND_SCALER = 0F;
			WAIT_TIMER = 20;
			return;
		}
		addExperience(player);
		WAIT_TIMER = 20;
	}
	
	public void takeCard(Player player)
	{
		if (player.getMainHandItem().isEmpty())
			player.setItemInHand(InteractionHand.MAIN_HAND, card);
		else if (!player.getInventory().add(card))
			dropCard(false, level, worldPosition, card);
		else player.inventoryMenu.broadcastChanges();
		
		this.level.playSound(null, this.worldPosition, MSSoundEvents.INTELLIBEAM_LAZERSTATION_REMOVE_CARD.get(), SoundSource.BLOCKS, 0.5F, 1F);
		
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
		if (card.is(MSItems.CAPTCHA_CARD.get()) || card.isEmpty())
		{
			this.card = card;
			if(level != null)
			{
				updateState();
			}
		}
	}
	
	public ItemStack applyDecodedTag(ItemStack taggedCard)
	{
		taggedCard.getOrCreateTag().putBoolean("decoded", true);
		
		return taggedCard;
	}
	
	public void addExperience(Player player)
	{
		if (player.experienceLevel <= 0)
		{
			return;
		}
		if (player.getMainHandItem().isEmpty() && this.hasCard())
		{
			player.giveExperienceLevels(-1);
			EXPERIENCE_LEVEL += 1;
			SOUND_SCALER += 0.1;
			
			this.level.playSound(null, this.worldPosition, MSSoundEvents.INTELLIBEAM_LAZERSTATION_EXP_GATHER.get(), SoundSource.BLOCKS, 0.5F, 1F + SOUND_SCALER);
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
		EXPERIENCE_LEVEL = nbt.getInt("experience_level");
	}
	
	@Override
	public void saveAdditional(CompoundTag compound)
	{
		super.saveAdditional(compound);
		compound.put("card", card.save(new CompoundTag()));
		compound.putInt("experience_level", EXPERIENCE_LEVEL);
	}
	
	public static void serverTick(Level level, BlockPos pos, BlockState state, IntellibeamLaserstationBlockEntity intellibeam)
	{
		if(WAIT_TIMER > 0)
		{
			intellibeam.WAIT_TIMER--;
		}
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