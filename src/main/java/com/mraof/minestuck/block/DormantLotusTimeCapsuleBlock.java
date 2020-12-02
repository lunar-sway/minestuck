package com.mraof.minestuck.block;

import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.util.MSSoundEvents;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootParameterSets;
import net.minecraft.world.storage.loot.LootParameters;

import java.util.List;
import java.util.Random;

public class DormantLotusTimeCapsuleBlock extends Block
{
	protected DormantLotusTimeCapsuleBlock(Properties builder) {
		super(builder);
	}
	
	public void tick(BlockState state, World worldIn, BlockPos pos, Random random) {
		super.tick(state, worldIn, pos, random);
		if (random.nextInt(7) == 0) {
			//worldIn.setBlockState(pos, MSBlocks.LOTUS_TIME_CAPSULE_BLOCK.getDefaultState().with(LotusTimeCapsuleBlock.UNACTIVATED, true), 11);
			worldIn.setBlockState(pos, MSBlocks.MINI_FROG_STATUE.getDefaultState());
		}
	}
}