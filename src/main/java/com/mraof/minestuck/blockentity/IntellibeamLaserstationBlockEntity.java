package com.mraof.minestuck.blockentity;

import com.mraof.minestuck.alchemy.AlchemyHelper;
import com.mraof.minestuck.block.machine.IntellibeamLaserstationBlock;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.util.MSSoundEvents;
import com.mraof.minestuck.util.MSTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
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

public class IntellibeamLaserstationBlockEntity extends BlockEntity
{
	//TODO with decodeAndEjectCard(), create a Set of registry names for items that have been successfully read
	private static final int EXP_LEVEL_CAPACITY = 10;
	
	private ItemStack analyzedCard = ItemStack.EMPTY;
	private int storedExperience = 0;
	private int waitTimer = 0;
	
	public static final String DECODING_PROGRESS = "block.minestuck.intellibeam_laserstation.decoding_progress";
	public static final String CAPTCHA_DECODED = "block.minestuck.intellibeam_laserstation.captcha_decoded";
	
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
			} else if(storedExperience >= EXP_LEVEL_CAPACITY)
			{
				decodeAndEjectCard(player);
				player.displayClientMessage(Component.translatable(DECODING_PROGRESS, processExperienceGuage()), true);
			} else if(AlchemyHelper.isReadableCard(analyzedCard))
			{
				this.level.playSound(null, this.worldPosition, MSSoundEvents.INTELLIBEAM_LASERSTATION_REMOVE_CARD.get(), SoundSource.BLOCKS, 0.5F, 0.1F);
				player.displayClientMessage(Component.translatable(CAPTCHA_DECODED), true);
				waitTimer = 10;
			} else if(analyzedCard.isEmpty() && !AlchemyHelper.isReadableCard(analyzedCard) && cardWithItem.is(MSTags.Items.UNREADABLE))
			{
				setCard(heldItem.split(1));
				waitTimer = 10;
			} else
			{
				addExperience(player);
				player.displayClientMessage(Component.translatable(DECODING_PROGRESS, processExperienceGuage()), true);
				waitTimer = 10;
			}
		}
	}
	
	public void decodeAndEjectCard(Player player)
	{
		applyDecodedTag(analyzedCard);
		takeCard(player);
		
		storedExperience = 0;
		waitTimer = 10;
	}
	
	public String processExperienceGuage()
	{
		int numberOfBars = "▯▯▯▯▯▯▯▯▯▯".length(); //used to visualize how many experience bars there are
		
		char[] stringChars = new char[numberOfBars];
		
		for(int i = 0; i < numberOfBars; i++)
		{
			if(i < storedExperience)
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
			player.giveExperienceLevels(-1);
			storedExperience += 1;
			float soundScale = storedExperience / 10F;
			
			this.level.playSound(null, this.worldPosition, MSSoundEvents.INTELLIBEAM_LASERSTATION_EXP_GATHER.get(), SoundSource.BLOCKS, 0.5F, 1F + soundScale);
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
		storedExperience = nbt.getInt("experience_level");
	}
	
	@Override
	public void saveAdditional(CompoundTag compound)
	{
		super.saveAdditional(compound);
		compound.put("card", analyzedCard.save(new CompoundTag()));
		compound.putInt("experience_level", storedExperience);
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