package com.mraof.minestuck.world.gen.feature.structure;

import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.entity.underling.OgreEntity;
import com.mraof.minestuck.world.gen.feature.MSStructurePieces;
import com.mraof.minestuck.world.gen.feature.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.storage.loot.MSLootTables;
import net.minecraft.block.*;
import net.minecraft.entity.SpawnReason;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.structure.ScatteredStructurePiece;
import net.minecraft.world.gen.feature.template.TemplateManager;

import java.util.Random;

public class SmallRuinPiece extends ScatteredStructurePiece
{
	private final boolean[] torches = new boolean[4];
	private boolean placedChest;
	
	public SmallRuinPiece(Random random, int minX, int minZ, float skyLight)
	{
		super(MSStructurePieces.SMALL_RUIN, random, minX, 64, minZ, 7, 4, 10);
		
		float torchChance = 1 - skyLight;
		
		torches[0] = random.nextFloat() < 0.5F * torchChance;
		torches[1] = random.nextFloat() < 0.5F * torchChance;
		torches[2] = random.nextFloat() < torchChance;
		torches[3] = random.nextFloat() < torchChance;
	}
	
	public SmallRuinPiece(TemplateManager templates, CompoundNBT nbt)
	{
		super(MSStructurePieces.SMALL_RUIN, nbt);
		
		for(int i = 0; i < 4; i++)
			torches[i] = nbt.getBoolean("torch" + i);
		placedChest = nbt.getBoolean("placed_chest");
	}
	
	@Override
	protected void readAdditional(CompoundNBT nbt)    //Note: incorrectly mapped. Should be writeAdditional
	{
		super.readAdditional(nbt);
		
		for(int i = 0; i < 4; i++)
			nbt.putBoolean("torch" + i, torches[i]);
		nbt.putBoolean("placed_chest", placedChest);
	}
	
	@Override
	public boolean addComponentParts(IWorld worldIn, Random randomIn, MutableBoundingBox boundingBoxIn, ChunkPos chunkPosIn)
	{
		if(!isInsideBounds(worldIn, boundingBoxIn, 0))
			return false;
		
		StructureBlockRegistry blocks = StructureBlockRegistry.getOrDefault(worldIn.getChunkProvider().getChunkGenerator().getSettings());
		BlockState wallBlock = blocks.getBlockState("structure_primary");
		BlockState wallDecor = blocks.getBlockState("structure_primary_decorative");
		BlockState floorBlock = blocks.getBlockState("structure_secondary");
		BlockState wallTorch = blocks.getBlockState("wall_torch");
		
		for(int z = 0; z < 8; z++)
			for(int x = 0; x < 7; x++)
				if(x == 0 || x == 6)
				{
					boolean torch = false;
					if(z == 3 || z == 6)
						torch = torches[(x == 0 ? 0 : 1) + (z == 3 ? 0 : 2)];
					if(buildFloorTile(wallBlock, x, z, worldIn, randomIn, boundingBoxIn))
						buildWall(wallBlock, x, z, worldIn, randomIn, boundingBoxIn, torch ? 2 : 0);
				} else buildFloorTile(floorBlock, x, z, worldIn, randomIn, boundingBoxIn);
		
		for(int x = 1; x < 6; x++)
			if(x == 1 || x == 5)
			{
				buildFloorTile(wallBlock, x, 8, worldIn, randomIn, boundingBoxIn);
				buildWall(wallBlock, x, 8, worldIn, randomIn, boundingBoxIn, 0);
				if(this.getBlockStateFromPos(worldIn, x, 2, 8, boundingBoxIn) == wallBlock)
					this.setBlockState(worldIn, wallDecor, x, 2, 8, boundingBoxIn);
			} else buildFloorTile(floorBlock, x, 8, worldIn, randomIn, boundingBoxIn);
		
		for(int x = 2; x < 5; x++)
		{
			buildFloorTile(wallBlock, x, 9, worldIn, randomIn, boundingBoxIn);
			buildWall(wallBlock, x, 9, worldIn, randomIn, boundingBoxIn, 0);
		}
		
		this.fillWithAir(worldIn, boundingBoxIn, 1, 1, 0, 5, 3, 7);
		this.fillWithAir(worldIn, boundingBoxIn, 2, 1, 8, 4, 3, 8);
		
		if(!placedChest)
			placedChest = generateChest(worldIn, boundingBoxIn, randomIn, 3, 1, 6, this.getCoordBaseMode().getOpposite(), MSLootTables.BASIC_MEDIUM_CHEST);
		
		if(torches[0])
			this.setBlockState(worldIn, wallTorch.with(WallTorchBlock.HORIZONTAL_FACING, Direction.EAST), 1, 2, 3, boundingBoxIn);
		if(torches[1])
			this.setBlockState(worldIn, wallTorch.with(WallTorchBlock.HORIZONTAL_FACING, Direction.WEST), 5, 2, 3, boundingBoxIn);
		if(torches[2])
			this.setBlockState(worldIn, wallTorch.with(WallTorchBlock.HORIZONTAL_FACING, Direction.EAST), 1, 2, 6, boundingBoxIn);
		if(torches[3])
			this.setBlockState(worldIn, wallTorch.with(WallTorchBlock.HORIZONTAL_FACING, Direction.WEST), 5, 2, 6, boundingBoxIn);
		
		if(boundingBoxIn.intersectsWith(this.boundingBox.minX, this.boundingBox.minZ, this.boundingBox.minX, this.boundingBox.minZ))
			placeUnderling(this.boundingBox.minX - 6, this.boundingBox.minZ - 6, worldIn, randomIn);
		if(boundingBoxIn.intersectsWith(this.boundingBox.maxX, this.boundingBox.minZ, this.boundingBox.maxX, this.boundingBox.minZ))
			placeUnderling(this.boundingBox.maxX - 6, this.boundingBox.minZ - 6, worldIn, randomIn);
		if(boundingBoxIn.intersectsWith(this.boundingBox.minX, this.boundingBox.maxZ, this.boundingBox.minX, this.boundingBox.maxZ))
			placeUnderling(this.boundingBox.minX - 6, this.boundingBox.maxZ - 6, worldIn, randomIn);
		if(boundingBoxIn.intersectsWith(this.boundingBox.maxX, this.boundingBox.maxZ, this.boundingBox.maxX, this.boundingBox.maxZ))
			placeUnderling(this.boundingBox.maxX - 6, this.boundingBox.maxZ - 6, worldIn, randomIn);
		
		return true;
	}
	
	private boolean generateChest(IWorld worldIn, MutableBoundingBox boundingBoxIn, Random randomIn, int x, int y, int z, Direction direction, ResourceLocation lootTable)
	{
		BlockPos blockpos = new BlockPos(this.getXWithOffset(x, z), this.getYWithOffset(y), this.getZWithOffset(x, z));
		return generateChest(worldIn, boundingBoxIn, randomIn, blockpos, lootTable, Blocks.CHEST.getDefaultState().with(ChestBlock.FACING, direction));
	}
	
	private void buildWall(BlockState block, int x, int z, IWorld world, Random rand, MutableBoundingBox boundingBox, int minY)
	{
		
		float f = z * 0.2F;
		for(int y = 1; y < 4; y++)
		{
			if(y > minY && rand.nextFloat() >= f)
				return;
			
			this.setBlockState(world, block, x, y, z, boundingBox);
			
			f -= 0.5F;
		}
	}
	
	private boolean buildFloorTile(BlockState block, int x, int z, IWorld world, Random rand, MutableBoundingBox boundingBox)
	{
		int y = 0;
		
		float f = (3 - z) * 0.25F;
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
	
	private void placeUnderling(int minX, int minZ, IWorld world, Random rand)
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
					int y = world.getHeight(Heightmap.Type.MOTION_BLOCKING, new BlockPos(x, 0, z)).getY();
					if(y < minY)
						minY = y;
					if(y > maxY)
						maxY = y;
				}
			
			if(maxY - minY < 3)
			{
				OgreEntity ogre = MSEntityTypes.OGRE.create(world.getWorld());
				if(ogre == null)
					throw new IllegalStateException("Unable to create a new ogre. Entity factory returned null!");
				ogre.enablePersistence();
				ogre.setPositionAndRotation(xPos + 0.5, maxY, zPos + 0.5, rand.nextFloat() * 360F, 0);
				ogre.onInitialSpawn(world, world.getDifficultyForLocation(new BlockPos(xPos, maxY, zPos)), SpawnReason.STRUCTURE, null, null);
				ogre.setHomePosAndDistance(new BlockPos(minX + 8, this.boundingBox.minY, minZ + 8), 10);
				world.addEntity(ogre);
				return;
			}
			
			i++;
		}
	}
}