package com.mraof.minestuck.world.lands.structure;

import java.util.Random;

import com.mraof.minestuck.entity.underling.EntityOgre;
import com.mraof.minestuck.world.GateHandler;
import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;
import com.mraof.minestuck.world.lands.structure.blocks.StructureBlockUtil;

import com.mraof.minestuck.world.storage.loot.MinestuckLoot;
import net.minecraft.block.BlockTorch;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureStart;
import net.minecraft.world.gen.structure.template.TemplateManager;

public class SmallRuinStart extends StructureStart
{
	
	public SmallRuinStart()
	{}
	
	public SmallRuinStart(ChunkProviderLands provider, World world, Random rand, int chunkX, int chunkZ)
	{
		super(chunkX, chunkZ);
		
		components.add(new SmallRuin(provider, chunkX, chunkZ, rand));
		updateBoundingBox();
		
		BlockPos pos = GateHandler.getGatePos(-1, world.provider.getDimension());
		if(pos != null && this.getBoundingBox().intersectsWith(pos.getX() - 16, pos.getZ() - 16, pos.getX() + 16, pos.getZ() + 16))
			components.clear();
	}
	
	public static class SmallRuin extends StructureComponent
	{
		
		protected boolean[] torches = new boolean[4];
		protected boolean definedHeight  = false;
		
		public SmallRuin()
		{}
		
		protected SmallRuin(ChunkProviderLands provider, int chunkX, int chunkZ, Random rand)
		{
			int x = chunkX*16 + rand.nextInt(16);
			int z = chunkZ*16 + rand.nextInt(16);
			setCoordBaseMode(EnumFacing.Plane.HORIZONTAL.random(rand));
			int xWidth = getCoordBaseMode().getAxis().equals(EnumFacing.Axis.X) ? 10 : 7;
			int zWidth = getCoordBaseMode().getAxis().equals(EnumFacing.Axis.Z) ? 10 : 7;
			this.boundingBox = new StructureBoundingBox(x, 64, z, x + xWidth - 1, 67, z + zWidth - 1);
			
			float torchChance = 1 -  provider.worldProvider.skylightBase;
			
			torches[0] = rand.nextFloat() < 0.5F*torchChance;
			torches[1] = rand.nextFloat() < 0.5F*torchChance;
			torches[2] = rand.nextFloat() < torchChance;
			torches[3] = rand.nextFloat() < torchChance;
			
		}
		
		@Override
		public boolean addComponentParts(World worldIn, Random rand, StructureBoundingBox boundingBox)
		{
			if(!checkHeight(worldIn, boundingBox) || this.isLiquidInStructureBoundingBox(worldIn, boundingBox))
				return false;
			
			ChunkProviderLands provider = (ChunkProviderLands) worldIn.provider.createChunkGenerator();
			IBlockState wallBlock = provider.blockRegistry.getBlockState("structure_primary");
			IBlockState wallDecor = provider.blockRegistry.getBlockState("structure_primary_decorative");
			IBlockState floorBlock = provider.blockRegistry.getBlockState("structure_secondary");
			IBlockState torchBlock = provider.blockRegistry.getBlockState("torch");
			
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
					if(this.getBlockStateFromPos(worldIn, x, 2, 8, boundingBox) == wallBlock)
						this.setBlockState(worldIn, wallDecor, x, 2, 8, boundingBox);
				} else buildFloorTile(floorBlock, x, 8, worldIn, rand, boundingBox);
			
			for(int x = 2; x < 5; x++)
			{
				buildFloorTile(wallBlock, x, 9, worldIn, rand, boundingBox);
				buildWall(wallBlock, x, 9, worldIn, rand, boundingBox, 0);
			}
			
			this.fillWithAir(worldIn, boundingBox, 1, 1, 0, 5, 3, 7);
			this.fillWithAir(worldIn, boundingBox, 2, 1, 8, 4, 3, 8);
			BlockPos chestPos = new BlockPos(this.getXWithOffset(3, 6), this.getYWithOffset(1), this.getZWithOffset(3, 6));
			StructureBlockUtil.placeLootChest(chestPos, worldIn, boundingBox, this.getCoordBaseMode().getOpposite(), MinestuckLoot.BASIC_MEDIUM_CHEST, rand);
			
			if(torches[0])
				this.setBlockState(worldIn, torchBlock.withProperty(BlockTorch.FACING, EnumFacing.EAST), 1, 2, 3, boundingBox);
			if(torches[1])
				this.setBlockState(worldIn, torchBlock.withProperty(BlockTorch.FACING, EnumFacing.WEST), 5, 2, 3, boundingBox);
			if(torches[2])
				this.setBlockState(worldIn, torchBlock.withProperty(BlockTorch.FACING, EnumFacing.EAST), 1, 2, 6, boundingBox);
			if(torches[3])
				this.setBlockState(worldIn, torchBlock.withProperty(BlockTorch.FACING, EnumFacing.WEST), 5, 2, 6, boundingBox);
			
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
		
		protected boolean checkHeight(World worldIn, StructureBoundingBox bb)
		{
			if(definedHeight)
				return true;
//			int minY = 256, maxY = 0;
			int height = 0;
			boolean onLand = false;
			int i = 0;
			
			for(int x = boundingBox.minX; x <= boundingBox.maxX; x++)
				for(int z = boundingBox.minZ; z <= boundingBox.maxZ; z++)
				{
					if(!bb.isVecInside(new BlockPos(x, 64, z)))
						continue;
					
					int y = worldIn.getTopSolidOrLiquidBlock(new BlockPos(x, 0, z)).getY();
					/*if(y < minY)
						minY = y;
					if(y > maxY)
						maxY = y;*/
					height += y;
					i++;
					if(!worldIn.getBlockState(new BlockPos(x, y, z)).getBlock().equals(Blocks.ICE))
						onLand = true;	//used to prevent the structure from spawning in an ice-covered sea without any land nearby
				}
				
			if(!onLand || i == 0/* || maxY - minY > 5*/)
				return false;
			
			height /= i;
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
				
				this.setBlockState(world, block, x, y, z, boundingBox);
				
				f -= 0.5F;
			}
		}
		
		protected boolean buildFloorTile(IBlockState block, int x, int z, World world, Random rand, StructureBoundingBox boundingBox)
		{
			int y = 0;
			
			float f = (3 - z)*0.25F;
			if(this.getBlockStateFromPos(world, x, y, z, boundingBox).getMaterial().isSolid())
				f -= 0.25F;
			boolean b = true;
			do
			{
				if(rand.nextFloat() >= f)
				{
					this.setBlockState(world, block, x, y, z, boundingBox);
					f = 0F;
				} else
				{
					b = false;
					f -= 0.25F;
				}
				
				y--;
			} while(this.boundingBox.minY + y >= 0 && !this.getBlockStateFromPos(world, x, y, z, boundingBox).getMaterial().isSolid());
			
			return b;
		}
		
		protected void placeUnderling(int minX, int minZ, World world, Random rand)
		{
			int i = 0;
			while(i < 10)
			{
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
					EntityOgre ogre = new EntityOgre(world);
					ogre.setPositionAndRotation(xPos + 0.5, maxY, zPos + 0.5, rand.nextFloat()*360F, 0);
					ogre.onInitialSpawn(null, null);
					ogre.setHomePosAndDistance(new BlockPos(minX + 8, this.boundingBox.minY, minZ + 8), 10);
					world.spawnEntity(ogre);
					return;
				}
				
				i++;
			}
		}
		
		@Override
		protected void writeStructureToNBT(NBTTagCompound nbt)
		{
			nbt.setBoolean("definedHeight", definedHeight);
			for(int i = 0; i < 4; i++)
				nbt.setBoolean("torch"+i, torches[i]);
		}
		
		@Override
		protected void readStructureFromNBT(NBTTagCompound nbt, TemplateManager templates)
		{
			definedHeight = nbt.getBoolean("definedHeight");
			for(int i = 0; i < 4; i++)
				torches[i] = nbt.getBoolean("torch"+i);
		}
		
	}
}