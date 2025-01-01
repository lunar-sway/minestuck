package com.mraof.minestuck.blockentity.machine;

import com.mraof.minestuck.advancements.MSCriteriaTriggers;
import com.mraof.minestuck.block.machine.IntellibeamLaserstationBlock;
import com.mraof.minestuck.blockentity.MSBlockEntityTypes;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.item.components.CaptchaCodeComponent;
import com.mraof.minestuck.item.components.CardStoredItemComponent;
import com.mraof.minestuck.item.components.MSItemComponents;
import com.mraof.minestuck.util.MSSoundEvents;
import com.mraof.minestuck.util.MSTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Takes a captcha card and expends player experience to "decode" the captcha on the card if the item stored in said card is in the item tag UNREADABLE.
 * Editmode deployable
 */
@ParametersAreNonnullByDefault
public class IntellibeamLaserstationBlockEntity extends BlockEntity
{
	public static final String DECODING_PROGRESS = "block.minestuck.intellibeam_laserstation.decoding_progress";
	public static final String CAPTCHA_DECODED = "block.minestuck.intellibeam_laserstation.captcha_decoded";
	
	private static final int EXP_LEVEL_CAPACITY = 10;
	
	private final Map<Item, Integer> decodingProgress = new HashMap<>();
	
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
		if(!(level instanceof ServerLevel serverLevel))
			return;
		if(waitTimer > 0)
			return;
		waitTimer = 10;
		
		if(analyzedCard.isEmpty())
		{
			ItemStack heldCard = player.getMainHandItem();
			ItemStack itemInHeldCard = heldCard.getOrDefault(MSItemComponents.CARD_STORED_ITEM, CardStoredItemComponent.EMPTY).storedStack();
			if(itemInHeldCard.is(MSTags.Items.UNREADABLE))
				setCard(heldCard.split(1));
			return;
		}
		
		if(player.isShiftKeyDown())
		{
			takeCard(player);
			return;
		}
		
		if(this.analyzedCard.has(MSItemComponents.CAPTCHA_CODE))
		{
			this.level.playSound(null, this.worldPosition, MSSoundEvents.INTELLIBEAM_LASERSTATION_REMOVE_CARD.get(), SoundSource.BLOCKS, 0.5F, 0.1F);
			player.displayClientMessage(Component.translatable(CAPTCHA_DECODED), true);
			return;
		}
		if(getCardItemExperience() < EXP_LEVEL_CAPACITY)
		{
			addExperience(player);
			return;
		}
		
		CardStoredItemComponent analyzedCardComponent = this.analyzedCard.getOrDefault(MSItemComponents.CARD_STORED_ITEM, CardStoredItemComponent.EMPTY);
		MSCriteriaTriggers.INTELLIBEAM_LASERSTATION.get().trigger((ServerPlayer) player, analyzedCardComponent.storedStack());
		setReadable(analyzedCard, serverLevel.getServer());
		takeCard(player);
	}
	
	private Integer getCardItemExperience()
	{
		return decodingProgress.getOrDefault(analyzedCard.getOrDefault(MSItemComponents.CARD_STORED_ITEM, CardStoredItemComponent.EMPTY).storedStack().getItem(), 0);
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
	
	public void setReadable(ItemStack taggedCard, MinecraftServer mcServer)
	{
		CardStoredItemComponent storedItem = taggedCard.get(MSItemComponents.CARD_STORED_ITEM);
		if(storedItem != null)
			taggedCard.set(MSItemComponents.CAPTCHA_CODE, CaptchaCodeComponent.createFor(storedItem.storedStack(), mcServer));
	}
	
	public void addExperience(Player player)
	{
		if(player.experienceLevel <= 0 && !player.isCreative())
		{
			return;
		}
		
		if(player.getMainHandItem().isEmpty() && !analyzedCard.isEmpty())
		{
			if(!player.isCreative())
				player.giveExperienceLevels(-1);
			
			Item analyzedItem = analyzedCard.getOrDefault(MSItemComponents.CARD_STORED_ITEM, CardStoredItemComponent.EMPTY).storedStack().getItem();
			int storedExperience = decodingProgress.getOrDefault(analyzedItem, 0);
			decodingProgress.put(analyzedItem, storedExperience + 1);
			
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
	protected void loadAdditional(CompoundTag nbt, HolderLookup.Provider pRegistries)
	{
		super.loadAdditional(nbt, pRegistries);
		setCard(ItemStack.parseOptional(pRegistries, nbt.getCompound("card")));
		
		CompoundTag progressData = nbt.getCompound("decoding_progress");
		for(String itemName : progressData.getAllKeys())
		{
			ResourceLocation itemId = ResourceLocation.tryParse(itemName);
			if(itemId == null)
				continue;
			Item item = BuiltInRegistries.ITEM.get(itemId);
			if(item == null)
				continue;
			decodingProgress.put(item, progressData.getInt(itemName));
		}
	}
	
	@Override
	public void saveAdditional(CompoundTag compound, HolderLookup.Provider provider)
	{
		super.saveAdditional(compound, provider);
		compound.put("card", analyzedCard.saveOptional(provider));
		
		CompoundTag progressData = new CompoundTag();
		for(Map.Entry<Item, Integer> entry : decodingProgress.entrySet())
		{
			String itemName = Objects.requireNonNull(BuiltInRegistries.ITEM.getKey(entry.getKey())).toString();
			progressData.putInt(itemName, entry.getValue());
		}
		compound.put("decoding_progress", progressData);
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
