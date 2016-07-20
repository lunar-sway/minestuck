package com.mraof.minestuck.world.lands.decorator.structure;

import java.util.Random;

import net.minecraft.block.BlockTorch;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;

import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;

public class BasicTowerDecorator extends SimpleStructureDecorator
{
	
	@Override
	public BlockPos generateStructure(World world, Random random, BlockPos pos, ChunkProviderLands provider)
	{
		xCoord = pos.getX();
		zCoord = pos.getZ();
		yCoord = getAverageHeight(world);
		if(yCoord == -1)
			return null;
		
		IBlockState ground = world.getBlockState(new BlockPos(xCoord, yCoord - 1, zCoord));
		if((ground.getMaterial().isLiquid() || ground.getMaterial() == Material.ICE) && random.nextFloat() < 0.6)	//Make it uncommon, but not impossible for it to be placed in the sea.
			return null;
		if(provider.isBBInSpawn(new StructureBoundingBox(xCoord - 4, zCoord - 4, xCoord + 4, zCoord + 4)))
			return null;
		
		int height = random.nextInt(7) + 12;
		if(height + yCoord + 3 >= 256)
			return null;
		
		IBlockState wall = provider.blockRegistry.getBlockState("structure_primary");
		IBlockState wallDec = provider.blockRegistry.getBlockState("structure_primary_decorative");
		IBlockState floor = provider.blockRegistry.getBlockState("structure_secondary");
		
		boolean torches = random.nextFloat() < (provider.dayCycle == 0 ? 0.4F : provider.dayCycle == 1 ? 0F : 0.9F);
		
		for(int x = -3; x < 4; x++)
			for(int z = Math.abs(x) == 3 ? -2 : -3; z < (Math.abs(x) == 3 ? 3 : 4); z++)
			{
				BlockPos floorPos = new BlockPos(xCoord + x, yCoord, zCoord + z);
				do
				{
					world.setBlockState(floorPos, (Math.abs(x) == 3 || Math.abs(z) == 3) ? wall : floor, 2);
					floorPos = pos.down();
				} while(!world.getBlockState(floorPos).getMaterial().isSolid());
			}
		
		
		this.placeBlocks(world, wall, -2, 1, -3, -1, height, -3);
		this.placeBlocks(world, wall, 1, 1, -3, 2, height, -3);
		
		this.placeBlocks(world, wall, -2, 1, 3, -1, height, 3);
		this.placeBlocks(world, wall, 1, 1, 3, 2, height, 3);
		
		this.placeBlocks(world, wall, -3, 1, -2, -3, height, -1);
		this.placeBlocks(world, wall, -3, 1, 1, -3, height, 2);
		
		this.placeBlocks(world, wall, 3, 1, -2, 3, height, -1);
		this.placeBlocks(world, wall, 3, 1, 1, 3, height, 2);
		
		for(EnumFacing facing : EnumFacing.HORIZONTALS)
		{
			BlockPos doorPos = new BlockPos(xCoord, yCoord + 1, zCoord).offset(facing, 4);
			if(world.getBlockState(doorPos).getMaterial().isSolid())
			{
				this.placeBlock(world, wall, 3*facing.getFrontOffsetX(), 1, 3*facing.getFrontOffsetZ());
				if(world.getBlockState(doorPos.up()).getMaterial().isSolid())
				{
					this.placeBlocks(world, wall, 3*facing.getFrontOffsetX(), 2, 3*facing.getFrontOffsetZ(), 3*facing.getFrontOffsetX(), height, 3*facing.getFrontOffsetZ());
					continue;
				}
			} else
			{
				this.placeBlock(world, Blocks.AIR.getDefaultState(), 3*facing.getFrontOffsetX(), 1, 3*facing.getFrontOffsetZ());
				
				if(!world.getBlockState(doorPos.down(2)).getMaterial().isSolid())
				{
					this.placeBlocks(world, floor, Math.min(3*facing.getFrontOffsetX(), 4*facing.getFrontOffsetX()), 0, Math.min(3*facing.getFrontOffsetZ(), 4*facing.getFrontOffsetZ()),
							Math.max(3*facing.getFrontOffsetX(), 4*facing.getFrontOffsetX()), 0, Math.max(3*facing.getFrontOffsetZ(), 4*facing.getFrontOffsetZ()));
					if(facing.getAxis() == EnumFacing.Axis.X)
					{
						this.placeBlocks(world, wall, 4*facing.getFrontOffsetX(), -1, -1, 4*facing.getFrontOffsetX(), -1, 1);
						this.placeBlocks(world, wall, 5*facing.getFrontOffsetX(), 0, -1, 5*facing.getFrontOffsetX(), 0, 1);
						this.placeBlock(world, wall, 4*facing.getFrontOffsetX(), 0, -1);
						this.placeBlock(world, wall, 4*facing.getFrontOffsetX(), 0, 1);
					} else
					{
						this.placeBlocks(world, wall, -1, -1, 4*facing.getFrontOffsetZ(), 1, -1, 4*facing.getFrontOffsetZ());
						this.placeBlocks(world, wall, -1, 0, 5*facing.getFrontOffsetZ(), 1, 0, 5*facing.getFrontOffsetZ());
						this.placeBlock(world, wall, -1, 0, 4*facing.getFrontOffsetZ());
						this.placeBlock(world, wall, 1, 0, 4*facing.getFrontOffsetZ());
					}
				}
			}
			
			this.placeBlocks(world, Blocks.AIR.getDefaultState(), 3*facing.getFrontOffsetX(), 2, 3*facing.getFrontOffsetZ(), 3*facing.getFrontOffsetX(), 3, 3*facing.getFrontOffsetZ());
			this.placeBlock(world, wallDec, 3*facing.getFrontOffsetX(), 4, 3*facing.getFrontOffsetZ());
			this.placeBlocks(world, wall, 3*facing.getFrontOffsetX(), 5, 3*facing.getFrontOffsetZ(), 3*facing.getFrontOffsetX(), height, 3*facing.getFrontOffsetZ());
		}
		
		this.placeBlocks(world, floor, -3, height + 1, -1, -2, height + 1, 1);
		this.placeBlocks(world, floor, 2, height + 1, -1, 3, height + 1, 1);
		this.placeBlocks(world, floor, -1, height + 1, -3, 1, height + 1, -2);
		this.placeBlocks(world, floor, -1, height + 1, 2, 1, height + 1, 3);
		this.placeBlock(world, floor, -2, height + 1, -2);
		this.placeBlock(world, floor, 2, height + 1, -2);
		this.placeBlock(world, floor, 2, height + 1, 2);
		this.placeBlock(world, floor, -2, height + 1, 2);
		if(torches)
		{
			this.placeBlock(world, Blocks.TORCH.getDefaultState(), -2, height + 2, -2);
			this.placeBlock(world, Blocks.TORCH.getDefaultState(), 2, height + 2, -2);
			this.placeBlock(world, Blocks.TORCH.getDefaultState(), 2, height + 2, 2);
			this.placeBlock(world, Blocks.TORCH.getDefaultState(), -2, height + 2, 2);
		}
		
		
		this.placeBlocks(world, wall, -4, height + 1, -1, -4, height + 2, 1);
		this.placeBlock(world, wall, -4, height + 3, -1);
		this.placeBlock(world, wall, -4, height + 3, 1);
		this.placeBlocks(world, wall, -3, height + 1, -2, -3, height + 2, -2);
		this.placeBlocks(world, wall, -3, height + 1, 2, -3, height + 2, 2);
		
		this.placeBlocks(world, wall, 4, height + 1, -1, 4, height + 2, 1);
		this.placeBlock(world, wall, 4, height + 3, -1);
		this.placeBlock(world, wall, 4, height + 3, 1);
		this.placeBlocks(world, wall, 3, height + 1, -2, 3, height + 2, -2);
		this.placeBlocks(world, wall, 3, height + 1, 2, 3, height + 2, 2);
		
		this.placeBlocks(world, wall, -1, height + 1, -4, 1, height + 2, -4);
		this.placeBlock(world, wall, -1, height + 3, -4);
		this.placeBlock(world, wall, 1, height + 3, -4);
		this.placeBlocks(world, wall, -2, height + 1, -3, -2, height + 2, -3);
		this.placeBlocks(world, wall, 2, height + 1, -3, 2, height + 2, -3);
		
		this.placeBlocks(world, wall, -1, height + 1, 4, 1, height + 2, 4);
		this.placeBlock(world, wall, -1, height + 3, 4);
		this.placeBlock(world, wall, 1, height + 3, 4);
		this.placeBlocks(world, wall, -2, height + 1, 3, -2, height + 2, 3);
		this.placeBlocks(world, wall, 2, height + 1, 3, 2, height + 2, 3);
		
		
		this.placeBlocks(world, Blocks.AIR.getDefaultState(), -2, 1, -2, 2, height, 2);
		this.placeBlocks(world, Blocks.AIR.getDefaultState(), -1, height + 1, -1, 1, height + 1, 1);
		this.placeBlock(world, floor, 0, height + 1, 0);
		
		rotation = random.nextBoolean();
		int stairOffset = random.nextInt(8);
		BlockPos offset = null;
		for(int y = 0; y <= height + 3; y++)
		{
			offset = getStairOffset(y + stairOffset);
			this.placeBlock(world, floor, offset.getX(), Math.min(height, y) + 1, offset.getZ());
		}
		if(rotation)
			offset = new BlockPos(offset.getZ(), 0, offset.getX());
		
		rotation = false;
		if(torches)
		{
			for(int y = 5; y < height; y += 5)
			{
				this.placeBlock(world, Blocks.TORCH.getDefaultState().withProperty(BlockTorch.FACING, EnumFacing.EAST), -2, y, 0);
				this.placeBlock(world, Blocks.TORCH.getDefaultState().withProperty(BlockTorch.FACING, EnumFacing.WEST), 2, y, 0);
				this.placeBlock(world, Blocks.TORCH.getDefaultState().withProperty(BlockTorch.FACING, EnumFacing.SOUTH), 0, y, -2);
				this.placeBlock(world, Blocks.TORCH.getDefaultState().withProperty(BlockTorch.FACING, EnumFacing.NORTH), 0, y, 2);
			}
		}
		
		if(offset.getZ() == -1 && offset.getX() != 1)
			offset = offset.north(2);
		else if(offset.getX() == -1)
			offset = offset.west(2).north();
		else if(offset.getZ() == 1)
			offset = offset.south().west();
		else offset = offset.east();
		
		return new BlockPos(xCoord + offset.getX(), yCoord + height + 2, zCoord + offset.getZ());
	}
	
	@Override
	public int getCount(Random random)
	{
		return random.nextFloat() < 0.05 ? 1 : 0;
	}
	
	@Override
	public float getPriority()
	{
		return 0.4F;
	}
	
	protected int getAverageHeight(World world)
	{
		int value = 0;
		int minVal = Integer.MAX_VALUE, maxVal = Integer.MIN_VALUE;
		int minDepth = Integer.MAX_VALUE;
		
		for(int x = -3; x < 4; x++)
			for(int z = Math.abs(x) == 3 ? -2 : -3; z < (Math.abs(x) == 3 ? 3 : 4); z++)
			{
				int height = world.getPrecipitationHeight(new BlockPos(xCoord + x, 0, zCoord + z)).getY();
				value += height;
				minVal = Math.min(minVal, height);
				maxVal = Math.max(maxVal, height);
				minDepth = Math.min(minDepth, world.getTopSolidOrLiquidBlock(new BlockPos(xCoord + x, 0, zCoord + z)).getY());
			}
		
		if(maxVal - minVal > 6 || minVal - minDepth > 12)
			return -1;
		value /= 45;
		value -= 1;
		return value;
	}
	
	protected BlockPos getStairOffset(int offset)
	{
		int x = 0;
		if((offset & 4) == 0 && (offset & 3) != 0)
			x = 1;
		else if((offset & 4) != 0 && (offset & 3) != 0)
			x = -1;
		
		offset += 2;
		
		int z = 0;
		if((offset & 4) == 0 && (offset & 3) != 0)
			z = 1;
		else if((offset & 4) != 0 && (offset & 3) != 0)
			z = -1;
		
		return new BlockPos(x, 0, z);
	}
	
}