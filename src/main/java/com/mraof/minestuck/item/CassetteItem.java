package com.mraof.minestuck.item;

import com.mraof.minestuck.block.CassettePlayerBlock;
import com.mraof.minestuck.block.EnumCassetteType;
import com.mraof.minestuck.block.MSBlocks;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.JukeboxSong;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

/**
 * @deprecated Use the <code>minestuck:cassette_code</code> components
 */
@Deprecated
@ParametersAreNonnullByDefault
public class CassetteItem extends Item
{
	public final EnumCassetteType cassetteType;
	private final int comparatorValue;
	
	public CassetteItem(int comparatorValue, EnumCassetteType cassetteType, Properties builder, int lengthInTicks)
	{
		super(builder);
		this.comparatorValue = comparatorValue;
		this.cassetteType = cassetteType;
	}
	
	@Override
	public InteractionResult useOn(UseOnContext context)
	{
		Level level = context.getLevel();
		BlockPos blockpos = context.getClickedPos();
		BlockState blockstate = level.getBlockState(blockpos);
		if(blockstate.getBlock() == MSBlocks.CASSETTE_PLAYER.get() && blockstate.getValue(CassettePlayerBlock.CASSETTE) && blockstate.getValue(CassettePlayerBlock.OPEN))
		{
			ItemStack itemstack = context.getItemInHand();
			if(!level.isClientSide)
			{
				(MSBlocks.CASSETTE_PLAYER.get()).insertCassette(level, blockpos, blockstate, itemstack);
				itemstack.shrink(1);
			}
			
			return InteractionResult.SUCCESS;
		} else
		{
			return InteractionResult.PASS;
		}
	}
	
	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag)
	{
		ResourceKey<JukeboxSong> songKey = this.cassetteType.getJukeboxSong();
		HolderLookup.Provider registries = context.registries();
		if (songKey != null && registries != null)
		{
			registries.lookup(songKey.registryKey())
					.flatMap(registry -> registry.get(songKey))
					.ifPresent(song -> {
				MutableComponent songDescription = song.value().description().copy();
				ComponentUtils.mergeStyles(songDescription, Style.EMPTY.withColor(ChatFormatting.GRAY));
				tooltipComponents.add(songDescription);
			});
		}
	}
	
	public int getComparatorValue()
	{
		return this.comparatorValue;
	}
}
