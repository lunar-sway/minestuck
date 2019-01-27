package com.mraof.minestuck.block;

import java.util.Random;

import com.mraof.minestuck.world.biome.BiomeMinestuck;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockFluidEnder extends BlockFluidClassic
{
	public BlockFluidEnder(Fluid fluid, Material material)
	{
		super(fluid, material);
		setTickRandomly(true);
	}
	
	@SideOnly (Side.CLIENT)
	@Override
	public Vec3d getFogColor(World world, BlockPos pos, IBlockState state, Entity entity, Vec3d originalColor, float partialTicks)
	{
		return new Vec3d(0.012, 0.112, 0.096);
	}
	
	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random rand)
	{
		//0 is the full moon, 4 is the new moon. Possible values range from 0 to 7.
		int moonPhase = world.provider.getMoonPhase(world.getWorldTime());
		
		//Causes Ender not to flow during the full moon
		if(moonPhase==0)
		{
			return;
		}
		
		//Checks if the fluid should replicate like water. Defaults to replication in oceans during the new moon: NOT the full moon.
		if (!isSourceBlock(world, pos)
				&& ForgeEventFactory.canCreateFluidSource(world, pos, state, moonPhase==4))
		{
			int adjacentSourceBlocks =
					(isSourceBlock(world, pos.north()) ? 1 : 0) +
					(isSourceBlock(world, pos.south()) ? 1 : 0) +
					(isSourceBlock(world, pos.east()) ? 1 : 0) +
					(isSourceBlock(world, pos.west()) ? 1 : 0);
			if (adjacentSourceBlocks >= 2 && (world.getBlockState(pos.up(densityDir)).getMaterial().isSolid() || isSourceBlock(world, pos.up(densityDir))))
				world.setBlockState(pos, state.withProperty(LEVEL, 0));
		}
		
		int quantaRemaining = quantaPerBlock - state.getValue(LEVEL);
		int expQuanta = -101;
		
		// check adjacent block levels, if this one is non-source
		if (quantaRemaining < quantaPerBlock)
		{
			if (world.getBlockState(pos.add( 0, -densityDir,  0)).getBlock() == this ||
				world.getBlockState(pos.add( 0, -densityDir, -1)).getBlock() == this ||
				world.getBlockState(pos.add( 0, -densityDir,  1)).getBlock() == this)
			{
				expQuanta = quantaPerBlock - 1;
			}
			else if(world.getBlockState(pos.add(-1, -densityDir,  0)).getBlock() == this ||
					world.getBlockState(pos.add( 1, -densityDir,  0)).getBlock() == this)
			{
				expQuanta = quantaPerBlock - 2;
			}
			else
			{
				int maxQuanta = -100;
				maxQuanta = getLargerQuanta(world, pos.add(-1, 0,  0), maxQuanta) - 1;
				maxQuanta = Math.max(maxQuanta, getLargerQuanta(world, pos.add( 1, 0,  0), maxQuanta) - 1);		//Has to respect the already-present decrease if appropriate.
				maxQuanta = getLargerQuanta(world, pos.add( 0, 0, -1), maxQuanta);
				maxQuanta = getLargerQuanta(world, pos.add( 0, 0,  1), maxQuanta);
				
				expQuanta = maxQuanta - 1;
			}
			
			// decay calculation
			if (expQuanta != quantaRemaining)
			{
				quantaRemaining = expQuanta;
				
				if (expQuanta <= 0)
				{
					world.setBlockToAir(pos);
				}
				else
				{
					world.setBlockState(pos, state.withProperty(LEVEL, quantaPerBlock - expQuanta), 2);
					world.scheduleUpdate(pos, this, tickRate);
					world.notifyNeighborsOfStateChange(pos, this, false);
				}
			}
		}
		// This is a "source" block: set meta to zero, and send a server only update
		else if (quantaRemaining >= quantaPerBlock)
		{
			world.setBlockState(pos, this.getDefaultState(), 2);
		}
		
		// Flow vertically if possible
		if (canDisplace(world, pos.up(densityDir)))
		{
			flowIntoBlock(world, pos.up(densityDir), 1);
			return;
		}
		
		// Flow outward if possible
		int flowMeta = quantaPerBlock - quantaRemaining + 1;
		if (flowMeta >= quantaPerBlock)
		{
			return;
		}
		
		if (isSourceBlock(world, pos) || !isFlowingVertically(world, pos))
		{
			if (world.getBlockState(pos.down(densityDir)).getBlock() == this)
			{
				flowMeta = 1;
			}
			boolean flowTo[] = getOptimalFlowDirections(world, pos);

			if(flowMeta+1<quantaPerBlock)
			{
				if (flowTo[0]) flowIntoBlock(world, pos.add(-1, 0,  0), flowMeta+1);
				if (flowTo[1]) flowIntoBlock(world, pos.add( 1, 0,  0), flowMeta+1);
			}
			if (flowTo[2]) flowIntoBlock(world, pos.add( 0, 0, -1), flowMeta);
			if (flowTo[3]) flowIntoBlock(world, pos.add( 0, 0,  1), flowMeta);
		}
	}
	
	@Override
	protected int calculateFlowCost(World world, BlockPos pos, int recurseDepth, int side)
	{
		int cost = 1000;
		for (int adjSide = 0; adjSide < 4; adjSide++)
		{
			if ((adjSide == 0 && side == 1) ||
				(adjSide == 1 && side == 0) ||
				(adjSide == 2 && side == 3) ||
				(adjSide == 3 && side == 2))
			{
				continue;
			}
			
			BlockPos pos2 = pos;
			
			switch (adjSide)
			{
				case 0: pos2 = pos2.add(-1, 0,  0); break;
				case 1: pos2 = pos2.add( 1, 0,  0); break;
				case 2: pos2 = pos2.add( 0, 0, -1); break;
				case 3: pos2 = pos2.add( 0, 0,  1); break;
			}
			
			if (!canFlowInto(world, pos2) || isSourceBlock(world, pos2))
			{
				continue;
			}
			
			if (canFlowInto(world, pos2.add(0, densityDir, 0)))
			{
				return recurseDepth;
			}
			
			if (recurseDepth >= 4)
			{
				continue;
			}
			
			int min = calculateFlowCost(world, pos2, recurseDepth + (adjSide/2==0? 2:1), adjSide);
			if (min < cost)
			{
				cost = min;
			}
		}
		return cost;
	}
}
