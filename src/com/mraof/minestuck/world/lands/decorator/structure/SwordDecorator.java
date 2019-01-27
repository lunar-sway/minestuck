package com.mraof.minestuck.world.lands.decorator.structure;

import java.util.Random;

import com.mraof.minestuck.world.lands.structure.blocks.StructureBlockUtil;
import com.mraof.minestuck.world.storage.loot.MinestuckLoot;

import net.minecraft.block.BlockTorch;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;

import static net.minecraft.block.BlockColored.COLOR;
import static net.minecraft.item.EnumDyeColor.*;

import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;

public class SwordDecorator extends SimpleStructureDecorator
{
	@Override
	public BlockPos generateStructure(World world, Random random, BlockPos pos, ChunkProviderLands provider)
	{
		IBlockState[] materials = pickMaterials(random);
		if(materials==null)
		{
			return null;
		}
		
		rotation = random.nextBoolean();
		BlockPos out = generateHilt(world, random, pos, provider, materials);
		
		if(out != null)
		{
			//Randomize the location and orientation of the blade.
			rotation = random.nextBoolean();
			pos = pos.add((4 + random.nextInt(6)) * Math.pow(-1, random.nextInt(2)), 0, (4 + random.nextInt(6)) * Math.pow(-1, random.nextInt(2)));
			generateBlade(world, random, pos, provider, materials);
		}
		
		return out;
	}
	
	/**
	 * Finds a set of blocks to make up the composition of the sword.
	 * @param random A random used in determining which material set should be used.
	 * @return An array of 6 block states, with the following associations:
	 * 0: Outline for guard and pommel
	 * 1: Body for guard and pommel, and outline for blade
	 * 2: Outline for handle
	 * 3: Body for handle
	 * 4: Body for blade
	 * 5: Core of blade
	 */
	private IBlockState[] pickMaterials(Random random)
	{
		IBlockState[] out = null;
		//One in 600 swords will be made of the valuable materials
		switch(random.nextInt(random.nextDouble() < 0.005 ? 3 : 2))
		{
		case 0:
			out = new IBlockState[]
			{
				Blocks.CONCRETE.getDefaultState().withProperty(COLOR, GRAY),
				Blocks.STAINED_HARDENED_CLAY.getDefaultState().withProperty(COLOR, CYAN),
				Blocks.STAINED_HARDENED_CLAY.getDefaultState().withProperty(COLOR, BROWN),
				Blocks.CONCRETE.getDefaultState().withProperty(COLOR, BROWN),
				Blocks.CONCRETE.getDefaultState().withProperty(COLOR, SILVER),
				Blocks.CONCRETE.getDefaultState().withProperty(COLOR, WHITE)
			};
			break;
		case 1:
			out = new IBlockState[]
			{
				Blocks.STAINED_HARDENED_CLAY.getDefaultState().withProperty(COLOR, PURPLE),
				Blocks.STAINED_HARDENED_CLAY.getDefaultState().withProperty(COLOR, PINK),
				Blocks.CONCRETE.getDefaultState().withProperty(COLOR, SILVER),
				Blocks.STAINED_HARDENED_CLAY.getDefaultState().withProperty(COLOR, CYAN),
				Blocks.STAINED_HARDENED_CLAY.getDefaultState().withProperty(COLOR, MAGENTA),
				Blocks.CONCRETE.getDefaultState().withProperty(COLOR, PINK)
			};
			break;
		case 2:
			out = new IBlockState[]
			{
				Blocks.CONCRETE.getDefaultState().withProperty(COLOR, WHITE),
				Blocks.QUARTZ_BLOCK.getDefaultState(),
				Blocks.WOOL.getDefaultState(),
				Blocks.BONE_BLOCK.getDefaultState(),
				Blocks.STAINED_GLASS.getDefaultState().withProperty(COLOR, WHITE),
				Blocks.SEA_LANTERN.getDefaultState()
			};
			break;
		}
		return out;
	}
	
	public BlockPos generateHilt(World world, Random random, BlockPos pos, ChunkProviderLands provider, IBlockState[] materials)
	{
		xCoord = pos.getX();
		zCoord = pos.getZ();
		yCoord = getAverageHeight(world);
		if(yCoord == -1)
			return null;
		
		IBlockState ground = world.getBlockState(new BlockPos(xCoord, yCoord - 1, zCoord));
		if(ground.getMaterial().isLiquid())
			return null;
		
		BlockPos curr = new BlockPos(xCoord, yCoord, zCoord);
		if(rotation)
		{
			if(provider.isBBInSpawn(new StructureBoundingBox(curr.add(-8, 0, 0), curr.add(8, 25, 0))))
				return null;
		} else
		{
			if(provider.isBBInSpawn(new StructureBoundingBox(curr.add(0, 0, -8), curr.add(0, 25, 8))))
				return null;
		}
			
		
		
		int yOffset = 2 + random.nextInt(3);
		if(yCoord + 26 - yOffset >= 256)
			return null;
		
		curr = new BlockPos(0, -yOffset, 0);
		
		//First, put down the actual blade of the sword.
		boolean isFlipped = random.nextBoolean();
		
		for(int i=0; i<5; i++)
		{
			int material = Math.abs(2 - i);
			switch(material)
			{
			case 2: material = 1; break;
			case 1: material = 4; break;
			case 0: material = 5; break;
			}
			this.placeBlocks(world, materials[material], curr.add(2-i, isFlipped ? 4-i : i, 0), curr.add(2-i, 14, 0));
		}
		
		//Second, put down the guard of the sword.
		curr = curr.add(0, 14, 0);
		this.placeBlock(world, materials[1], curr.getX(), curr.getY(), curr.getZ());
		{
			this.placeHalfGuard(world, curr, materials, true);
			this.placeHalfGuard(world, curr, materials, false);
		}
		curr = curr.up();
		this.placeBlocks(world, materials[1], curr.add(-1, 0, 0), curr.add(1, 0, 0));
		
		//Third, put down the handle of the sword.
		this.placeBlocks(world, materials[2], curr.add(-1, 1, 0), curr.add(1, 6, 0));
		this.placeBlocks(world, materials[3], curr, curr.up(7));
		
		//Fourth, put down the pommel
		curr = curr.up(9);
		this.placeBlocks(world, materials[0], curr.add(-1, -1, 0), curr.add(1, 1, 0));
		this.placeBlock(world, materials[1], curr.getX(), curr.getY(), curr.getZ());
		
		return new BlockPos(xCoord, yCoord, zCoord);
	}
	
	private void placeHalfGuard(World world, BlockPos origin, IBlockState[] materials, boolean isFlipped)
	{
		int x = origin.getX(), y = origin.getY(), z = origin.getZ();
		int s = isFlipped ? 1 : -1;
		for(int i=0; i<3; i++)
		{
			BlockPos a = new BlockPos(x + s*(2+2*i), y + 1-i, z);
			BlockPos b = new BlockPos(x + s*(3+2*i), y + 3-i, z);
			this.placeBlocks(world, materials[0], a, b);
			this.placeBlocks(world, materials[1], a.up(), b.down());
		}
		this.placeBlock(world, materials[0], x + s*8, y, z);
	}
	
	public BlockPos generateBlade(World world, Random random, BlockPos pos, ChunkProviderLands provider, IBlockState[] materials)
	{
		xCoord = pos.getX();
		zCoord = pos.getZ();
		yCoord = getAverageHeight(world);
		
		pos = new BlockPos(xCoord, yCoord, zCoord);
		
		IBlockState ground = world.getBlockState(pos);
		if(ground.getBlock().canBeReplacedByLeaves(ground, world, pos))
		{
			yCoord++;
		}
		
		pos = new BlockPos(0, 0, 0);
		
		boolean isFlipped = random.nextBoolean();
		boolean secondFlip = random.nextBoolean();
		int s = isFlipped ? -1 : 1;		//The point and the break of the blade swap places
		int t = secondFlip? -1 : 1;		//The break of the blade goes the other direction
		
		//Main body of the sword
		if(isFlipped)
		{
			placeBlocks(world, materials[1], pos.add(-5, 0, -2), pos.add(6, 0, 2));
			placeBlocks(world, materials[4], pos.add(-6, 0, -1), pos.add(6, 0, 1));
			placeBlocks(world, materials[5], pos.add(-7, 0, 0),  pos.add(6, 0, 0));
		} else
		{
			placeBlocks(world, materials[1], pos.add(-6, 0, -2), pos.add(5, 0, 2));
			placeBlocks(world, materials[4], pos.add(-6, 0, -1), pos.add(6, 0, 1));
			placeBlocks(world, materials[5], pos.add(-6, 0, 0),  pos.add(7, 0, 0));
		}
		
		//Broken corner
		BlockPos a = new BlockPos(7*s, 0, 1*t);
		BlockPos b = new BlockPos(8*s, 0, 1*t);
		placeBlocks(world, materials[4], a, b);
		
		a = new BlockPos(6*s, 0, 2*t);
		b = new BlockPos(9*s, 0, 2*t);
		placeBlocks(world, materials[1], a, b);
		
		//Blade tip
		placeBlock (world, materials[1], -8*s, 0, 0);
		placeBlocks(world, materials[1], -7*s, 0, -1, -7*s, 0, 1);
		placeBlock (world, materials[5], -7*s, 0, 0);
		return pos;
	}
	
	private void placeBlocks(World world, IBlockState block, BlockPos start, BlockPos end)
	{
		int fromX = start.getX(), fromY = start.getY(), fromZ = start.getZ();
		int toX = end.getX(), toY = end.getY(), toZ = end.getZ();
		placeBlocks(world, block, Math.min(fromX, toX), Math.min(fromY, toY), Math.min(fromZ, toZ),
			Math.max(fromX, toX), Math.max(fromY, toY), Math.max(fromZ, toZ));
	}
	
	@Override
	public int getCount(Random random)
	{
		return random.nextFloat() < 0.05 ? 1 : 0;
	}
	
	@Override
	public float getPriority()
	{
		return Float.MAX_VALUE;	//Note: never do this.
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
}