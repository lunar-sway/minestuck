package com.mraof.minestuck.item.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CauldronBlock;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class FrozenFrogBlockItem extends FrogBlockItem
{
	public FrozenFrogBlockItem(Block blockIn, Properties builder)
	{
		super(blockIn, builder);
	}
	
	@Override
	public ActionResultType onItemUse(ItemUseContext context)
	{
		World worldIn = context.getWorld();
		BlockPos blockpos = context.getPos();
		BlockPos belowBlock = blockpos.down(1);
		BlockState belowBlockState = worldIn.getBlockState(belowBlock);
		BlockState blockstate = worldIn.getBlockState(blockpos);
		
		if(blockstate.getBlock() == Blocks.CAULDRON && blockstate.get(CauldronBlock.LEVEL) >= 1 && belowBlockState.getBlock() == Blocks.FIRE)
		{
			ItemStack itemstack = context.getItem();
			if(!worldIn.isRemote)
			{
				Entity entity = createFrog(worldIn, (double)blockpos.getX() + 0.5D, (double)blockpos.getY() + 0.8D, (double)blockpos.getZ() + 0.5D, 0);
				worldIn.addEntity(entity);
				worldIn.playSound(null, blockpos.getX(), blockpos.getY(), blockpos.getZ(), SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.AMBIENT, 1.5F, 1.0F);
				worldIn.addParticle(ParticleTypes.BARRIER, (float)blockpos.getX() + random.nextFloat(), (float)blockpos.getY() + 1.1F, (float)blockpos.getZ() + random.nextFloat(), 0.0D, 0.0D, 0.0D);
				itemstack.shrink(1);
			}
			
			return ActionResultType.SUCCESS;
		} else
		{
			return ActionResultType.PASS;
		}
	}
}
