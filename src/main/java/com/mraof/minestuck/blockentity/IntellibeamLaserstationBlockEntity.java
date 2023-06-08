package com.mraof.minestuck.blockentity;

import com.mraof.minestuck.alchemy.AlchemyHelper;
import com.mraof.minestuck.alchemy.CardCaptchas;
import com.mraof.minestuck.block.machine.IntellibeamLaserstationBlock;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.util.MSSoundEvents;
import com.mraof.minestuck.util.MSTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.Map;

public class IntellibeamLaserstationBlockEntity extends BlockEntity
{
	public static final String DECODING_PROGRESS = "block.minestuck.intellibeam_laserstation.decoding_progress";
	public static final String CAPTCHA_DECODED = "block.minestuck.intellibeam_laserstation.captcha_decoded";
	
	private static final int EXP_LEVEL_CAPACITY = 10;
	
	private final Map<String, Integer> storedDecodings = new HashMap<>();
	
	private ItemStack analyzedCard = ItemStack.EMPTY;
	private int waitTimer = 0;
	
	public IntellibeamLaserstationBlockEntity(BlockPos pos, BlockState state)
	{
		super(MSBlockEntityTypes.INTELLIBEAM_LASERSTATION.get(), pos, state);
	}
	
	public static void serverTick(Level level, BlockPos pos, BlockState state, IntellibeamLaserstationBlockEntity intellibeam)
	{
		if(intellibeam.waitTimer > 0)
		{
			intellibeam.waitTimer--;
		}
	}
	
	public void onRightClick(Player player)
	{
		if(level != null && !level.isClientSide)
		{
			ItemStack heldItem = player.getMainHandItem();
			ItemStack cardWithItem = AlchemyHelper.getDecodedItem(heldItem);
			
			if(waitTimer > 0)
			{
				return;
			}
			
			if(!analyzedCard.isEmpty() && player.isShiftKeyDown())
			{
				takeCard(player);
				waitTimer = 10;
			} else if(AlchemyHelper.isReadableCard(analyzedCard))
			{
				this.level.playSound(null, this.worldPosition, MSSoundEvents.INTELLIBEAM_LASERSTATION_REMOVE_CARD.get(), SoundSource.BLOCKS, 0.5F, 0.1F);
				player.displayClientMessage(Component.translatable(CAPTCHA_DECODED), true);
				waitTimer = 10;
			} else if(analyzedCard.isEmpty() && !AlchemyHelper.isReadableCard(analyzedCard) && cardWithItem.is(MSTags.Items.UNREADABLE))
			{
				setCard(heldItem.split(1));
				waitTimer = 10;
			} else if(getCardItemExperience() >= EXP_LEVEL_CAPACITY)
			{
				decodeAndEjectCard(player);
			} else
			{
				addExperience(player);
				waitTimer = 10;
			}
		}
	}
	
	private Integer getCardItemExperience()
	{
		return storedDecodings.getOrDefault(getCardItemName(), 0);
	}
	
	private String getCardItemName()
	{
		return CardCaptchas.getRegistryNameFromItem(AlchemyHelper.getDecodedItem(analyzedCard).getItem());
	}
	
	public void decodeAndEjectCard(Player player)
	{
		applyDecodedTag(analyzedCard);
		takeCard(player);
		
		waitTimer = 10;
	}
	
	public String processExperienceGuage()
	{
		int numberOfBars = "▯▯▯▯▯▯▯▯▯▯".length(); //used to visualize how many experience bars there are
		
		char[] stringChars = new char[numberOfBars];
		
		for(int i = 0; i < numberOfBars; i++)
		{
			if(i < getCardItemExperience())
				stringChars[i] = '▮';
			else
				stringChars[i] = '▯';
		}
		
		return '<' + new String(stringChars) + '>'; //if two levels of experience have been invested, it will look like this: "<▮▮▯▯▯▯▯▯▯▯>"
	}
	
	public void takeCard(Player player)
	{
		if(player.getMainHandItem().isEmpty())
			player.setItemInHand(InteractionHand.MAIN_HAND, analyzedCard);
		else if(!player.getInventory().add(analyzedCard))
			dropCard(false, level, worldPosition, analyzedCard);
		else player.inventoryMenu.broadcastChanges();
		
		this.level.playSound(null, this.worldPosition, MSSoundEvents.INTELLIBEAM_LASERSTATION_REMOVE_CARD.get(), SoundSource.BLOCKS, 0.5F, 1F);
		
		setCard(ItemStack.EMPTY);
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
	
	public void setCard(ItemStack card)
	{
		if(card.is(MSItems.CAPTCHA_CARD.get()) || card.isEmpty())
		{
			this.analyzedCard = card;
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
		if(player.experienceLevel <= 0)
		{
			return;
		}
		if(player.getMainHandItem().isEmpty() && !analyzedCard.isEmpty())
		{
			if(!player.isCreative())
				player.giveExperienceLevels(-1);

			String analyzedCardName = getCardItemName();
			int storedExperience = storedDecodings.getOrDefault(analyzedCardName, 0);
			storedDecodings.put(analyzedCardName, storedExperience + 1);
			
			float soundScale = storedExperience / 10F;
			
			this.level.playSound(null, this.worldPosition, MSSoundEvents.INTELLIBEAM_LASERSTATION_EXP_GATHER.get(), SoundSource.BLOCKS, 0.5F, 1F + soundScale);
			
			player.displayClientMessage(Component.translatable(DECODING_PROGRESS, processExperienceGuage()), true);
		}
	}
	
	public ItemStack getAnalyzedCard()
	{
		return this.analyzedCard;
	}
	
	@Override
	public void load(CompoundTag nbt)
	{
		super.load(nbt);
		setCard(ItemStack.of(nbt.getCompound("card")));
		
		ListTag list = nbt.getList("storedDecodings", Tag.TAG_COMPOUND);
		for(int i = 0; i < list.size(); i++)
		{
			CompoundTag itemProgress = list.getCompound(i);
			storedDecodings.put(itemProgress.getString("registryName"), itemProgress.getInt("storedExperience"));
		}
	}
	
	@Override
	public void saveAdditional(CompoundTag compound)
	{
		super.saveAdditional(compound);
		compound.put("card", analyzedCard.save(new CompoundTag()));
		
		ListTag listTag = new ListTag();
		for(Map.Entry<String, Integer> entry : storedDecodings.entrySet())
		{
			CompoundTag itemProgress = new CompoundTag();
			itemProgress.putString("registryName", entry.getKey());
			itemProgress.putInt("storedExperience", entry.getValue());
			listTag.add(itemProgress);
		}
		compound.put("storedDecodings", listTag);
	}
	
	private void updateState()
	{
		if(level != null && !level.isClientSide)
		{
			BlockState state = level.getBlockState(worldPosition);
			boolean hasCard = !analyzedCard.isEmpty();
			if(state.hasProperty(IntellibeamLaserstationBlock.HAS_CARD) && hasCard != state.getValue(IntellibeamLaserstationBlock.HAS_CARD))
				level.setBlock(worldPosition, state.setValue(IntellibeamLaserstationBlock.HAS_CARD, hasCard), Block.UPDATE_CLIENTS);
		}
	}
}