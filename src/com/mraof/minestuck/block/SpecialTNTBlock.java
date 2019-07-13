package com.mraof.minestuck.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.TNTBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.TNTEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

import java.util.Random;

public class SpecialTNTBlock extends Block//TNTBlock
{
	
	final boolean primed, unstable, instant;
	
	public SpecialTNTBlock(Properties builder, boolean primed, boolean unstable, boolean instant)
	{
		super(builder);
		this.primed = primed;
		this.unstable = unstable;
		this.instant = instant;
	}
	/*
	@Override
	public void onBlockClicked(BlockState state, World worldIn, BlockPos pos, PlayerEntity player)
	{
		if(primed)
		{
			this.explode(worldIn, pos, player);
			worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 11);
		}
	}
	
	@Override
	public void explode(World worldIn, BlockPos pos)
	{
		this.explode(worldIn, pos, null);
	}
	
	public void explode(World worldIn, BlockPos pos, EntityLivingBase igniter)
	{
		if(!worldIn.isRemote)
		{
			TNTEntity entitytntprimed = new TNTEntity(worldIn, (double) ((float) pos.getX() + 0.5F), (double) ((float) pos.getY() + 0.5F), (double) ((float) pos.getZ() + 0.5F), igniter);
			if(instant)
				entitytntprimed.setFuse(0);
			worldIn.spawnEntity(entitytntprimed);
			worldIn.playSound(null, entitytntprimed.posX, entitytntprimed.posY, entitytntprimed.posZ, SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1.0F, 1.0F);
		}
	}
	
	@Override
	public boolean onBlockActivated(IBlockState state, World worldIn, BlockPos pos, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		ItemStack stack = player.getHeldItem(hand);
		Item item = stack.getItem();
		if(item != Items.FLINT_AND_STEEL && item != Items.FIRE_CHARGE)
			return super.onBlockActivated(state, worldIn, pos, player, hand, side, hitX, hitY, hitZ);
		else
		{
			this.explode(worldIn, pos, player);
			worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 11);
			if(item == Items.FLINT_AND_STEEL)
				stack.damageItem(1, player);
			else stack.shrink(1);
			
			return true;
		}
	}
	
	@Override
	public void onEntityCollision(IBlockState state, World worldIn, BlockPos pos, Entity entityIn)
	{
		if(!worldIn.isRemote && entityIn instanceof EntityArrow)
		{
			EntityArrow entityarrow = (EntityArrow) entityIn;
			Entity entity = entityarrow.getShooter();
			if(entityarrow.isBurning())
			{
				this.explode(worldIn, pos, entity instanceof EntityLivingBase ? (EntityLivingBase) entity : null);
				worldIn.removeBlock(pos);
			}
		}
		
	}
	
	@Override
	public void onExplosionDestroy(World worldIn, BlockPos pos, Explosion explosionIn)
	{
		if(!worldIn.isRemote)
		{
			EntityTNTPrimed entitytntprimed = new EntityTNTPrimed(worldIn, (double) ((float) pos.getX() + 0.5F), (double) ((float) pos.getY() + 0.5F), (double) ((float) pos.getZ() + 0.5F), explosionIn.getExplosivePlacedBy());
			entitytntprimed.setFuse(worldIn.rand.nextInt(entitytntprimed.getFuse() / 4) + entitytntprimed.getFuse() / 8);
			if(instant)
				entitytntprimed.setFuse(entitytntprimed.getFuse() / 2);
			worldIn.spawnEntity(entitytntprimed);
		}
	}
	
	@Override
	public void randomTick(IBlockState state, World worldIn, BlockPos pos, Random random)
	{
		if(unstable && random.nextDouble() < 0.1)
		{
			this.explode(worldIn, pos, null);
			worldIn.removeBlock(pos);
		}
	}*/
}