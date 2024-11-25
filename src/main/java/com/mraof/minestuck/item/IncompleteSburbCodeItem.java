package com.mraof.minestuck.item;

import com.mraof.minestuck.blockentity.ComputerBlockEntity;
import com.mraof.minestuck.item.components.MSItemComponents;
import com.mraof.minestuck.item.components.SburbCodeComponent;
import com.mraof.minestuck.util.MSTags;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Used for the Sburb Code item, extends ReadableSburbCodeItem which is used by Completed Sburb Code.
 * Allows players to store a list of blocks from the GREEN_HIEROGLYPH block tag that in game reflects the collection of genetic code for the creation of sburb
 */
public class IncompleteSburbCodeItem extends ReadableSburbCodeItem
{
	public IncompleteSburbCodeItem(Properties properties)
	{
		super(properties);
	}
	
	@Override
	public boolean getParadoxInfo(ItemStack stack)
	{
		return stack != null && stack.has(MSItemComponents.SBURB_CODE) && stack.get(MSItemComponents.SBURB_CODE).paradoxCode();
	}
	
	public static void setParadoxInfo(ItemStack stack, boolean hasInfo)
	{
		List<Block> recordedHieroglyphs = new ArrayList<>();
		
		if(stack.has(MSItemComponents.SBURB_CODE))
			recordedHieroglyphs = stack.get(MSItemComponents.SBURB_CODE).hieroglyphs();
		
		stack.set(MSItemComponents.SBURB_CODE, new SburbCodeComponent(hasInfo, recordedHieroglyphs));
	}
	
	/**
	 * Loads the set of hieroglyph blocks that have been recorded into this item.
	 * This set is stored in the item as an nbt tag list under the name "recordedHieroglyphs".
	 */
	@Override
	public Set<Block> getRecordedBlocks(ItemStack stack)
	{
		if(stack.has(MSItemComponents.SBURB_CODE))
			return new HashSet<>(stack.get(MSItemComponents.SBURB_CODE).hieroglyphs());
		else
			return Collections.emptySet();
	}
	
	/**
	 * Takes a block that is in the GREEN_HIEROGLYPHS block tag and adds its registry name(as a string) to the item's nbt if it did not already have it stored.
	 * @return true if the item stack was changed.
	 */
	public static boolean addRecordedInfo(ItemStack stack, Block block)
	{
		return tryAddBlockToSet(stack, block);
	}
	
	public static ItemStack setRecordedInfo(ItemStack stack, Set<Block> blockList)
	{
		for(Block block : blockList)
			addRecordedInfo(stack, block);
		
		return stack;
	}
	
	@Override
	protected boolean useOnComputer(ItemStack heldStack, Player player, InteractionHand hand, ComputerBlockEntity blockEntity)
	{
		boolean success = super.useOnComputer(heldStack, player, hand, blockEntity);
		boolean changedItem = false;
		
		// adds any new hieroglyph and paradox info from the computer to the item
		
		if(blockEntity.hasParadoxInfoStored && !getParadoxInfo(heldStack))
		{
			IncompleteSburbCodeItem.setParadoxInfo(heldStack, true);
			changedItem = true;
		}
		
		for(Block iterateBlock : blockEntity.hieroglyphsStored)
		{
			if(iterateBlock.defaultBlockState().is(MSTags.Blocks.GREEN_HIEROGLYPHS))
				changedItem |= IncompleteSburbCodeItem.addRecordedInfo(heldStack, iterateBlock);
		}
		
		if(changedItem)
			attemptConversionToCompleted(player, hand);
		
		return success || changedItem;
	}
	
	@Override
	public InteractionResult useOn(UseOnContext context)
	{
		ItemStack stackInUse = context.getItemInHand();
		Level level = context.getLevel();
		Player player = context.getPlayer();
		InteractionHand handIn = context.getHand();
		
		if(player == null)
			return InteractionResult.PASS;
		
		BlockState state = level.getBlockState(context.getClickedPos());
		
		if(!state.is(MSTags.Blocks.GREEN_HIEROGLYPHS))
			return InteractionResult.FAIL;
		
		if(!addRecordedInfo(stackInUse, state.getBlock()))
			return InteractionResult.FAIL;
		
		level.playSound(null, player.blockPosition(), SoundEvents.VILLAGER_WORK_CARTOGRAPHER, SoundSource.BLOCKS, 1.0F, 1.0F);
		
		//if after addRecordedInfo the item now possesses all hieroglyphs, convert it to a completed sburb code item
		attemptConversionToCompleted(player, handIn);
		
		return InteractionResult.sidedSuccess(level.isClientSide);
	}
	
	private void attemptConversionToCompleted(Player player, InteractionHand hand)
	{
		ItemStack stackInHand = player.getItemInHand(hand);
		Set<Block> recordedSet = getRecordedBlocks(stackInHand);
		
		if(hasAllBlocks(recordedSet) && getParadoxInfo(stackInHand))
			player.setItemInHand(hand, MSItems.COMPLETED_SBURB_CODE.get().getDefaultInstance());
	}
	
	public static boolean hasAllBlocks(Set<Block> hieroglyphs)
	{
		return hieroglyphs.containsAll(MSTags.getBlocksFromTag(MSTags.Blocks.GREEN_HIEROGLYPHS));
	}
	
	private float percentCompletion(ItemStack stack)
	{
		int mod = getParadoxInfo(stack) ? 1 : 0; //the mod of 1 is to give the illusion that part of it has already been filled in before it was sent through the lotus flower, if it has the paradox code
		float sizeOfList = getRecordedBlocks(stack).size();
		return ((sizeOfList + mod) / (MSTags.getBlocksFromTag(MSTags.Blocks.GREEN_HIEROGLYPHS).size() + 1)) * 100;
	}
	
	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag)
	{
		tooltip.add(Component.translatable("item.minestuck.sburb_code.completion", (byte) percentCompletion(stack)));
		if(hasAllBlocks(getRecordedBlocks(stack)))
			tooltip.add(Component.translatable("item.minestuck.sburb_code.paradox_hint"));
		
		if(Screen.hasShiftDown())
		{
			tooltip.add(Component.translatable("item.minestuck.sburb_code.additional_info"));
		} else
			tooltip.add(Component.translatable("message.shift_for_more_info"));
	}
	
	public static Set<Block> readBlockSet(CompoundTag nbt, String key)
	{
		return nbt.getList(key, Tag.TAG_STRING).stream().map(Tag::getAsString)
				//Turn the Strings into ResourceLocations
				.flatMap(blockName -> Stream.ofNullable(ResourceLocation.tryParse(blockName)))
				//Turn the ResourceLocations into Blocks
				.flatMap(blockId -> Stream.ofNullable(BuiltInRegistries.BLOCK.get(blockId)))
				//Gather the blocks into a set
				.collect(Collectors.toSet());
	}
	
	public static void writeBlockSet(CompoundTag nbt, String key, @Nonnull Set<Block> blocks)
	{
		ListTag listTag = new ListTag();
		for(Block blockIterate : blocks)
		{
			String blockName = String.valueOf(BuiltInRegistries.BLOCK.getKey(blockIterate));
			listTag.add(StringTag.valueOf(blockName));
		}
		
		nbt.put(key, listTag);
	}
	
	public static boolean tryAddBlockToSet(ItemStack stack, Block block)
	{
		if(!stack.has(MSItemComponents.SBURB_CODE))
			return false;
		
		List<Block> recordedHieroglyphs = stack.get(MSItemComponents.SBURB_CODE).hieroglyphs();
		
		recordedHieroglyphs.add(block);
		stack.set(MSItemComponents.SBURB_CODE, new SburbCodeComponent(stack.get(MSItemComponents.SBURB_CODE).paradoxCode(), recordedHieroglyphs));
		
		return true;
	}
}
