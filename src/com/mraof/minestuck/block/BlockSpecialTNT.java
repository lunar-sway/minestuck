package com.mraof.minestuck.block;

import java.util.Random;

import com.mraof.minestuck.Minestuck;

import net.minecraft.block.BlockTNT;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class BlockSpecialTNT extends BlockTNT
{
	
	final boolean primed, unstable, instant;
	
	public BlockSpecialTNT(boolean primed, boolean unstable, boolean instant)
	{
		super();
		setCreativeTab(Minestuck.tabMinestuck);
		setHardness(0.0F);
		setStepSound(soundTypeGrass);
		this.primed = primed;
		this.unstable = unstable;
		this.instant = instant;
		if(unstable)
			setTickRandomly(true);
	}
	
	@Override
	public void onBlockClicked(World worldIn, BlockPos pos, EntityPlayer playerIn)
	{
		if(primed)
		{
			this.explode(worldIn, pos, worldIn.getBlockState(pos).withProperty(EXPLODE, Boolean.valueOf(true)), playerIn);
			worldIn.setBlockToAir(pos);
		}
	}
	
	@Override
	public void explode(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase igniter)
	{
		if (!worldIn.isRemote)
		{
			if (((Boolean)state.getValue(EXPLODE)).booleanValue())
			{
				EntityTNTPrimed entitytntprimed = new EntityTNTPrimed(worldIn, (double)((float)pos.getX() + 0.5F), (double)((float)pos.getY() + 0.5F), (double)((float)pos.getZ() + 0.5F), igniter);
				if(instant)
					entitytntprimed.fuse = 0;
				worldIn.spawnEntityInWorld(entitytntprimed);
				worldIn.playSoundAtEntity(entitytntprimed, "game.tnt.primed", 1.0F, 1.0F);
			}
		}
	}
	
	@Override
	public void onBlockDestroyedByExplosion(World worldIn, BlockPos pos, Explosion explosionIn)
	{
		if (!worldIn.isRemote)
		{
			EntityTNTPrimed entitytntprimed = new EntityTNTPrimed(worldIn, (double)((float)pos.getX() + 0.5F), (double)((float)pos.getY() + 0.5F), (double)((float)pos.getZ() + 0.5F), explosionIn.getExplosivePlacedBy());
			entitytntprimed.fuse = worldIn.rand.nextInt(entitytntprimed.fuse / 4) + entitytntprimed.fuse / 8;
			if(instant)
				entitytntprimed.fuse /= 2;
			worldIn.spawnEntityInWorld(entitytntprimed);
		}
	}
	
	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
	{
		if(unstable && rand.nextDouble() < 0.1)
		{
			this.explode(worldIn, pos, worldIn.getBlockState(pos).withProperty(EXPLODE, Boolean.valueOf(true)), null);
			worldIn.setBlockToAir(pos);
		}
	}
	
}