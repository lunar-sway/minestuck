package com.mraof.minestuck.item;

import com.mraof.minestuck.block.CassettePlayerBlock;
import com.mraof.minestuck.block.EnumCassetteType;
import com.mraof.minestuck.block.MSBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.function.Supplier;

public class CassetteItem extends ModMusicDiscItem
{
	public final EnumCassetteType cassetteID;
	
	public CassetteItem(int comparatorValueIn, Supplier<SoundEvent> soundIn, EnumCassetteType cassetteName, Properties builder)
	{
		super(comparatorValueIn, soundIn, builder);
		this.cassetteID = cassetteName;
	}
	
	@Override
	public ActionResultType onItemUse(ItemUseContext context)
	{
		World world = context.getWorld();
		BlockPos blockpos = context.getPos();
		BlockState blockstate = world.getBlockState(blockpos);
		if(blockstate.getBlock() == MSBlocks.CASSETTE_PLAYER && blockstate.get(CassettePlayerBlock.CASSETTE) == EnumCassetteType.NONE && blockstate.get(CassettePlayerBlock.OPEN))
		{
			ItemStack itemstack = context.getItem();
			if(!world.isRemote)
			{
				(MSBlocks.CASSETTE_PLAYER).insertCassette(world, blockpos, blockstate, itemstack);
				itemstack.shrink(1);
			}
			
			return ActionResultType.SUCCESS;
		} else
		{
			return ActionResultType.PASS;
		}
	}
}
