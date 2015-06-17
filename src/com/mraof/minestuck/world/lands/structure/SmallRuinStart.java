package com.mraof.minestuck.world.lands.structure;

import java.util.Random;

import com.mraof.minestuck.entity.underling.EntityOgre;
import com.mraof.minestuck.util.AlchemyRecipeHandler;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;

import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockTorch;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureStart;

public class SmallRuinStart extends StructureStart
{
	
	public SmallRuinStart()
	{}
	
	public SmallRuinStart(ChunkProviderLands provider, World world, Random rand, int chunkX, int chunkZ)
	{
		super(chunkX, chunkZ);
		
		components.add(new SmallRuin(provider, chunkX, chunkZ, rand));
		updateBoundingBox();
	}
	
	public static class SmallRuin extends StructureComponent
	{
		
		protected boolean[] torches = new boolean[4];
		protected int floorIndex, wallIndex;
		protected boolean definedHeight  = false;
		
		public SmallRuin()
		{}
		
		protected SmallRuin(ChunkProviderLands provider, int chunkX, int chunkZ, Random rand)
		{
			int x = chunkX*16 + rand.nextInt(16);
			int z = chunkZ*16 + rand.nextInt(16);
			this.coordBaseMode = EnumFacing.Plane.HORIZONTAL.random(rand);
			int xWidth = coordBaseMode.getAxis().equals(EnumFacing.Axis.X) ? 10 : 7;
			int zWidth = coordBaseMode.getAxis().equals(EnumFacing.Axis.Z) ? 10 : 7;
			this.boundingBox = new StructureBoundingBox(x, 64, z, x + xWidth - 1, 67, z + zWidth - 1);
			
			IBlockState[] structureBlocks = provider.aspect1.getStructureBlocks();
			floorIndex = rand.nextInt(structureBlocks.length);
			wallIndex = rand.nextInt(Math.max(1, structureBlocks.length - 1));
			if(wallIndex >= floorIndex && structureBlocks.length > 1)
				wallIndex++;
			
			float torchChance = provider.dayCycle == 0 ? 0.4F : provider.dayCycle == 1 ? 0F : 0.9F;
			
			torches[0] = rand.nextFloat() < 0.5F*torchChance;
			torches[1] = rand.nextFloat() < 0.5F*torchChance;
			torches[2] = rand.nextFloat() < torchChance;
			torches[3] = rand.nextFloat() < torchChance;
			
		}
		
		@Override
		public boolean addComponentParts(World worldIn, Random rand, StructureBoundingBox boundingBox)
		{
			if(!checkHeight(worldIn) || this.isLiquidInStructureBoundingBox(worldIn, boundingBox))
				return false;
			
			ChunkProviderLands provider = (ChunkProviderLands) worldIn.provider.createChunkGenerator();
			IBlockState[] structureBlocks = provider.aspect1.getStructureBlocks();
			floorIndex = Math.min(floorIndex, structureBlocks.length - 1);
			wallIndex = Math.min(wallIndex, structureBlocks.length - 1);	//In case the structure blocks have decreased during an update
			IBlockState floorBlock = structureBlocks[floorIndex], wallBlock = structureBlocks[wallIndex];
			
			for(int z = 0; z < 8; z++)
				for(int x = 0; x < 7; x++)
					if(x == 0 || x == 6)
					{
						boolean torch = false;
						if(z == 3 || z == 6)
							torch = torches[(x == 0 ? 0 : 1) + (z == 3 ? 0 : 2)];
						if(buildFloorTile(wallBlock, x, z, worldIn, rand, boundingBox))
							buildWall(wallBlock, x, z, worldIn, rand, boundingBox, torch ? 2 : 0);
					} else buildFloorTile(floorBlock, x, z, worldIn, rand, boundingBox);
			
			for(int x = 1; x < 6; x++)
				if(x == 1 || x == 5)
				{
					buildFloorTile(wallBlock, x, 8, worldIn, rand, boundingBox);
					buildWall(wallBlock, x, 8, worldIn, rand, boundingBox, 0);
					if(this.func_175807_a(worldIn, x, 2, 8, boundingBox) == wallBlock)
						this.func_175811_a(worldIn, provider.aspect1.getDecorativeBlockFor(wallBlock), x, 2, 8, boundingBox);
				} else buildFloorTile(floorBlock, x, 8, worldIn, rand, boundingBox);
			
			for(int x = 2; x < 5; x++)
			{
				buildFloorTile(wallBlock, x, 9, worldIn, rand, boundingBox);
				buildWall(wallBlock, x, 9, worldIn, rand, boundingBox, 0);
			}
			
			this.fillWithAir(worldIn, boundingBox, 1, 1, 0, 5, 3, 7);
			this.fillWithAir(worldIn, boundingBox, 2, 1, 8, 4, 3, 8);
			this.func_180778_a(worldIn, boundingBox, rand, 3, 1, 6, provider.lootMap.get(AlchemyRecipeHandler.BASIC_MEDIUM_CHEST).getItems(rand), rand.nextInt(3) + 5);
			if(boundingBox.func_175898_b(new BlockPos(this.getXWithOffset(3, 6), this.getYWithOffset(1), this.getZWithOffset(3, 6))))
				this.func_175811_a(worldIn, this.func_175807_a(worldIn, 3, 1, 6, boundingBox).withProperty(BlockChest.FACING, this.coordBaseMode.getOpposite()), 3, 1, 6, boundingBox);
			
			EnumFacing torchFacing = coordBaseMode == EnumFacing.EAST || coordBaseMode == EnumFacing.NORTH ? this.coordBaseMode.rotateY() : this.coordBaseMode.rotateYCCW();
			if(torches[0])
				this.func_175811_a(worldIn, Blocks.torch.getDefaultState().withProperty(BlockTorch.FACING, torchFacing), 1, 2, 3, boundingBox);
			if(torches[1])
				this.func_175811_a(worldIn, Blocks.torch.getDefaultState().withProperty(BlockTorch.FACING, torchFacing.getOpposite()), 5, 2, 3, boundingBox);
			if(torches[2])
				this.func_175811_a(worldIn, Blocks.torch.getDefaultState().withProperty(BlockTorch.FACING, torchFacing), 1, 2, 6, boundingBox);
			if(torches[3])
				this.func_175811_a(worldIn, Blocks.torch.getDefaultState().withProperty(BlockTorch.FACING, torchFacing.getOpposite()), 5, 2, 6, boundingBox);
			
			if(boundingBox.intersectsWith(this.boundingBox.minX, this.boundingBox.minZ, this.boundingBox.minX, this.boundingBox.minZ))
				placeUnderling(this.boundingBox.minX - 6, this.boundingBox.minZ - 6, worldIn, rand);
			if(boundingBox.intersectsWith(this.boundingBox.maxX, this.boundingBox.minZ, this.boundingBox.maxX, this.boundingBox.minZ))
				placeUnderling(this.boundingBox.maxX - 6, this.boundingBox.minZ - 6, worldIn, rand);
			if(boundingBox.intersectsWith(this.boundingBox.minX, this.boundingBox.maxZ, this.boundingBox.minX, this.boundingBox.maxZ))
				placeUnderling(this.boundingBox.minX - 6, this.boundingBox.maxZ - 6, worldIn, rand);
			if(boundingBox.intersectsWith(this.boundingBox.maxX, this.boundingBox.maxZ, this.boundingBox.maxX, this.boundingBox.maxZ))
				placeUnderling(this.boundingBox.maxX - 6, this.boundingBox.maxZ - 6, worldIn, rand);
			
			return true;
		}
		
		protected boolean checkHeight(World worldIn)
		{
			if(definedHeight)
				return true;
			int minY = 256, maxY = 0;
			int height = 0;
			boolean onLand = false;
			if(coordBaseMode.getAxis().equals(EnumFacing.Axis.X))
			{
				int x = coordBaseMode.getAxisDirection().equals(EnumFacing.AxisDirection.POSITIVE) ? boundingBox.minX : boundingBox.maxX;
				
				for(int z = boundingBox.minZ; z <= boundingBox.maxZ; z++)
				{
					int y = worldIn.getTopSolidOrLiquidBlock(new BlockPos(x, 0, z)).getY() - 1;
					if(y < minY)
						minY = y;
					if(y > maxY)
						maxY = y;
					height += y;
					if(!worldIn.getBlockState(new BlockPos(x, y, z)).getBlock().equals(Blocks.ice))
						onLand = true;	//used to prevent the structure from spawning in an ice-covered sea without any land nearby
				}
				height /= (boundingBox.maxZ - boundingBox.minZ + 1);
			} else
			{
				int z = coordBaseMode.getAxisDirection().equals(EnumFacing.AxisDirection.POSITIVE) ? boundingBox.minZ : boundingBox.maxZ;
				
				for(int x = boundingBox.minX; x <= boundingBox.maxX; x++)
				{
					int y = worldIn.getTopSolidOrLiquidBlock(new BlockPos(x, 0, z)).getY();
					if(y < minY)
						minY = y;
					if(y > maxY)
						maxY = y;
					height += y;
					if(!worldIn.getBlockState(new BlockPos(x, y, z)).getBlock().equals(Blocks.ice))
						onLand = true;
				}
				height /= (boundingBox.maxX - boundingBox.minX + 1);
				
			}
			if(!onLand || maxY - minY > 5)
				return false;
			
			boundingBox.offset(0, height - boundingBox.minY, 0);
			definedHeight = true;
			return true;
		}
		
		protected void buildWall(IBlockState block, int x, int z, World world, Random rand, StructureBoundingBox boundingBox, int minY)
		{
			
			float f = z*0.2F;
			for(int y = 1; y < 4; y++)
			{
				if(y > minY && rand.nextFloat() >= f)
					return;
				
				this.func_175811_a(world, block, x, y, z, boundingBox);
				
				f -= 0.5F;
			}
		}
		
		protected boolean buildFloorTile(IBlockState block, int x, int z, World world, Random rand, StructureBoundingBox boundingBox)
		{
			int y = 0;
			
			float f = (3 - z)*0.25F;
			if(this.func_175807_a(world, x, y, z, boundingBox).getBlock().getMaterial().isSolid())	//func_175807_a: get block state
				f -= 0.25F;
			boolean b = true;
			do
			{
				if(rand.nextFloat() >= f)
				{
					this.func_175811_a(world, block, x, y, z, boundingBox);	//func_175811_a: place block
					f = 0F;
				} else
				{
					b = false;
					f -= 0.25F;
				}
				
				y--;
			} while(this.boundingBox.minY + y >= 0 && !this.func_175807_a(world, x, y, z, boundingBox).getBlock().getMaterial().isSolid());
			
			return b;
		}
		
		protected void placeUnderling(int minX, int minZ, World world, Random rand)
		{
			Debug.print("Looking for spawning an ogre...");
			int i = 0;
			while(i < 10)
			{
				Debug.print("Try no. "+i);
				int xPos = rand.nextInt(12) + minX;
				int zPos = rand.nextInt(12) + minZ;
				if(this.boundingBox.intersectsWith(xPos - 1, zPos - 1, xPos + 1, zPos + 1))
					continue;
				
				int minY = 256, maxY = 0;
				for(int x = xPos - 1; x <= xPos + 1; x++)
					for(int z = zPos - 1; z <= zPos + 1; z++)
					{
						int y = world.getTopSolidOrLiquidBlock(new BlockPos(x, 0, z)).getY();
						if(y < minY)
							minY = y;
						if(y > maxY)
							maxY = y;
					}
				
				if(maxY - minY < 3)
				{
					Debug.print("Spawning ogre at "+xPos+", "+maxY+", "+zPos);
					EntityOgre ogre = new EntityOgre(world);
					ogre.setPositionAndRotation(xPos + 0.5, maxY, zPos + 0.5, rand.nextFloat()*360F, 0);
					ogre.func_180482_a(null, null);
					ogre.func_175449_a(new BlockPos(minX + 8, this.boundingBox.minY, minZ + 8), 10);
					world.spawnEntityInWorld(ogre);
					return;
				}
				Debug.print("Spawning failed. Height difference is "+(maxY - minY));
				
				i++;
			}
		}
		
		@Override
		protected void writeStructureToNBT(NBTTagCompound nbt)
		{
			nbt.setInteger("wallIndex", wallIndex);
			nbt.setInteger("floorIndex", floorIndex);
			nbt.setBoolean("definedHeight", definedHeight);
			for(int i = 0; i < 4; i++)
				nbt.setBoolean("torch"+i, torches[i]);
		}
		
		@Override
		protected void readStructureFromNBT(NBTTagCompound nbt)
		{
			wallIndex = nbt.getInteger("wallIndex");
			floorIndex = nbt.getInteger("floorIndex");
			definedHeight = nbt.getBoolean("definedHeight");
			for(int i = 0; i < 4; i++)
				torches[i] = nbt.getBoolean("torch"+i);
		}
		
	}
}