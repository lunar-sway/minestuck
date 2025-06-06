package com.mraof.minestuck.item;

import com.mraof.minestuck.blockentity.ComputerBlockEntity;
import com.mraof.minestuck.client.gui.MSScreenFactories;
import com.mraof.minestuck.computer.DiskBurnerData;
import com.mraof.minestuck.computer.ProgramTypes;
import com.mraof.minestuck.util.MSSoundEvents;
import com.mraof.minestuck.util.MSTags;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * The class for the items Sburb Code and Completed Sburb Code which primarily allows them to be read, with Sburb Code extending its usage.
 * Some checks for the use of sburb code exists in ComputerBlock, and functions from here are used in both ComputerBlock and ComputerTileEntity
 */
public abstract class ReadableSburbCodeItem extends Item
{
	public ReadableSburbCodeItem(Item.Properties properties)
	{
		super(properties);
	}
	
	public abstract boolean getParadoxInfo(ItemStack stack);
	
	/**
	 * Loads the set of hieroglyph blocks that have been recorded into this item.
	 */
	public abstract Set<Block> getRecordedBlocks(ItemStack stack);
	
	@Override
	public InteractionResultHolder<ItemStack> use(Level levelIn, Player playerIn, InteractionHand handIn)
	{
		ItemStack itemStackIn = playerIn.getItemInHand(handIn);
		
		if(levelIn.isClientSide())
		{
			MSScreenFactories.displayReadableSburbCodeScreen(getRecordedBlocks(itemStackIn), getParadoxInfo(itemStackIn));
		}
		
		return InteractionResultHolder.sidedSuccess(itemStackIn, levelIn.isClientSide());
	}
	
	@Override
	public InteractionResult onItemUseFirst(ItemStack heldStack, UseOnContext context)
	{
		Level level = context.getLevel();
		Player player = context.getPlayer();
		InteractionHand hand = context.getHand();
		BlockPos pos = context.getClickedPos();
		
		if(player != null && level.getBlockEntity(pos) instanceof ComputerBlockEntity computer)
		{
			Optional<DiskBurnerData> diskBurnerData = computer.getProgramData(ProgramTypes.DISK_BURNER);
			if(diskBurnerData.isPresent() && useOnComputer(heldStack, player, hand, pos, diskBurnerData.get()))
				return InteractionResult.sidedSuccess(level.isClientSide);
			else
				return InteractionResult.FAIL;
		}
		
		return InteractionResult.PASS;
	}
	
	protected boolean useOnComputer(ItemStack heldStack, Player player, InteractionHand hand, BlockPos pos, DiskBurnerData diskBurnerData)
	{
		boolean newInfo = diskBurnerData.recordNewInfo(getParadoxInfo(heldStack), getRecordedBlocks(heldStack));
		
		if(newInfo)
			player.level().playSound(null, pos, MSSoundEvents.COMPUTER_KEYBOARD.get(), SoundSource.BLOCKS);
		
		return newInfo;
	}
	
	public static class Completed extends ReadableSburbCodeItem
	{
		public Completed(Properties properties)
		{
			super(properties);
		}
		
		@Override
		public boolean getParadoxInfo(ItemStack stack)
		{
			return true;
		}
		
		@Override
		public Set<Block> getRecordedBlocks(ItemStack stack)
		{
			return MSTags.getBlocksFromTag(MSTags.Blocks.GREEN_HIEROGLYPHS);
		}
		
		@Override
		public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag)
		{
			if(Screen.hasShiftDown())
				tooltip.add(Component.translatable("item.minestuck.completed_sburb_code.additional_info"));
			else
				tooltip.add(Component.translatable("message.shift_for_more_info"));
		}
		
	}
}
