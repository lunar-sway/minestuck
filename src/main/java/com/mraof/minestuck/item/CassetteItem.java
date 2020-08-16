package com.mraof.minestuck.item;

import com.mraof.minestuck.block.CassettePlayerBlock;
import com.mraof.minestuck.block.MSBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.function.Supplier;

public class CassetteItem extends ModMusicDiscItem {

    public CassetteItem(int comparatorValueIn, Supplier<SoundEvent>soundIn, Properties builder)
    {
        super(comparatorValueIn, soundIn, builder);
    }


    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        World world = context.getWorld();
        BlockPos blockpos = context.getPos();
        BlockState blockstate = world.getBlockState(blockpos);
        if(blockstate.getBlock() == MSBlocks.CASSETTE_PLAYER_DEFAULT && !blockstate.get(CassettePlayerBlock.HAS_CASSETTE) && blockstate.get(CassettePlayerBlock.OPEN))
		{
			ItemStack itemstack = context.getItem();
			if(!world.isRemote)
			{
				((CassettePlayerBlock) MSBlocks.CASSETTE_PLAYER_DEFAULT).insertCassette(world, blockpos, blockstate, itemstack);
				world.playEvent((PlayerEntity) null, 1010, blockpos, Item.getIdFromItem(this));
				itemstack.shrink(1);
				PlayerEntity playerentity = context.getPlayer();
				if(playerentity != null)
				{
					playerentity.addStat(Stats.PLAY_RECORD);
				}
			}

            return ActionResultType.SUCCESS;
        } else {
            return ActionResultType.PASS;
        }
    }
}
