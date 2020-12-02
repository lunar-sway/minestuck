package com.mraof.minestuck.block;

import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.util.MSSoundEvents;
import net.minecraft.block.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.BrainUtil;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootParameterSets;
import net.minecraft.world.storage.loot.LootParameters;
import net.minecraft.world.storage.loot.LootTable;

import java.util.List;
import java.util.Random;

public class LotusTimeCapsuleBlock extends Block
{
	public static final BooleanProperty UNACTIVATED = MSProperties.UNACTIVATED;
	
	protected LotusTimeCapsuleBlock(Block.Properties builder) {
		super(builder);
	}
	
	public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit, Random random) {
		ItemStack itemstack = player.getHeldItem(handIn);
		if(itemstack.isEmpty())
		{
			if(!worldIn.isRemote)
			{
				Direction direction = hit.getFace();
				Direction direction1 = direction.getAxis() == Direction.Axis.Y ? player.getHorizontalFacing().getOpposite() : direction;
				worldIn.playSound((PlayerEntity) null, pos, MSSoundEvents.EVENT_ECHELADDER_INCREASE, SoundCategory.BLOCKS, 1.0F, 1.0F);
				worldIn.setBlockState(pos, MSBlocks.DORMANT_LOTUS_TIME_CAPSULE_BLOCK.getDefaultState(), 11);
				ItemEntity itementity = new ItemEntity(worldIn, (double) pos.getX() + 0.5D + (double) direction1.getXOffset() * 0.65D, (double) pos.getY() + 0.1D, (double) pos.getZ() + 0.5D + (double) direction1.getZOffset() * 0.65D, new ItemStack(MSItems.SERVER_DISK, 1));
				itementity.setMotion(0.05D * (double) direction1.getXOffset() + worldIn.rand.nextDouble() * 0.02D, 0.05D, 0.05D * (double) direction1.getZOffset() + worldIn.rand.nextDouble() * 0.02D);
				worldIn.addEntity(itementity);
				
				
				//List<ItemStack> itemStack = this.generateLoot(lootTable, player);
				
				//ItemEntity itementity1 = new ItemEntity(worldIn, (double) pos.getX() + 0.5D + (double) direction1.getXOffset() * 0.65D, (double) pos.getY() + 0.1D, (double) pos.getZ() + 0.5D + (double) direction1.getZOffset() * 0.65D, new ItemStack(MSItems.CLIENT_DISK, 1));
				//itementity1.setMotion(0.05D * (double) direction1.getXOffset() + worldIn.rand.nextDouble() * 0.02D, 0.05D, 0.05D * (double) direction1.getZOffset() + worldIn.rand.nextDouble() * 0.02D);
				//worldIn.addEntity(itementity1);
				//worldIn.setBlockState(pos, MSBlocks.LOTUS_TIME_CAPSULE_BLOCK.getDefaultState(), 11);
				//worldIn.setBlockState(pos, MSBlocks.LOTUS_TIME_CAPSULE_BLOCK.getDefaultState().with(LotusTimeCapsuleBlock.UNACTIVATED, false), 11);
			}
			return true;
		} else {
			return super.onBlockActivated(state, worldIn, pos, player, handIn, hit);
		}
	}
	
	public List<ItemStack>  generateLoot(ResourceLocation lootTable, PlayerEntity playerEntity) {
		//LootTable loottable = playerEntity.world.getServer().getLootTableManager().getLootTableFromLocation(playerEntity.getLootTableResourceLocation());
		//LootContext.Builder lootcontext$builder = (new LootContext.Builder((ServerWorld)playerEntity.world)).withParameter(LootParameters.POSITION, new BlockPos(playerEntity)).withParameter(LootParameters.THIS_ENTITY, playerEntity).withRandom(playerEntity.getRNG());
		//return loottable.generate(lootcontext$builder.build(LootParameterSets.CHEST));
		
		LootContext.Builder contextBuilder = new LootContext.Builder((ServerWorld) playerEntity.world).withParameter(LootParameters.THIS_ENTITY, playerEntity).withParameter(LootParameters.POSITION, new BlockPos(playerEntity));
		List<ItemStack> itemStacks = playerEntity.getServer().getLootTableManager().getLootTableFromLocation(lootTable).generate(contextBuilder.build(LootParameterSets.GIFT));
		return itemStacks;
	}
	
	
}