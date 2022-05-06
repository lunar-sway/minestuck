package com.mraof.minestuck.world.gen.feature.structure;

import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.entity.underling.OgreEntity;
import com.mraof.minestuck.world.gen.feature.MSStructurePieces;
import com.mraof.minestuck.world.gen.feature.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.storage.loot.MSLootTables;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.WallTorchBlock;
import net.minecraft.entity.SpawnReason;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.structure.ScatteredStructurePiece;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.template.TemplateManager;

import java.util.Random;

public class SmallRuinPiece extends ScatteredStructurePiece
{
	private final boolean[] torches = new boolean[4];
	private final boolean[] placedOgres = new boolean[4];
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
		for(int i = 0; i < 4; i++)
			placedOgres[i] = nbt.getBoolean("placed_ogres" + i);
		placedChest = nbt.getBoolean("placed_chest");
	}
	
	@Override
	protected void addAdditionalSaveData(CompoundNBT nbt)
	{
		super.addAdditionalSaveData(nbt);
		
		for(int i = 0; i < 4; i++)
			nbt.putBoolean("torch" + i, torches[i]);
		for(int i = 0; i < 4; i++)
			nbt.putBoolean("placed_ogres" + i, placedOgres[i]);
		nbt.putBoolean("placed_chest", placedChest);
	}

	@Override
	public boolean postProcess(ISeedReader worldIn, StructureManager manager, ChunkGenerator chunkGeneratorIn, Random randomIn, MutableBoundingBox boundingBoxIn, ChunkPos chunkPosIn, BlockPos pos)
	{
		if(!updateAverageGroundHeight(worldIn, boundingBoxIn, 0)) //where the height is determined, uses ScatteredStructurePiece "Heightmap.Type.MOTION_BLOCKING_NO_LEAVES"
			return false;

		StructureBlockRegistry blocks = StructureBlockRegistry.getOrDefault(chunkGeneratorIn);
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
				if(this.getBlock(worldIn, x, 2, 8, boundingBoxIn) == wallBlock)
					this.placeBlock(worldIn, wallDecor, x, 2, 8, boundingBoxIn);
			} else buildFloorTile(floorBlock, x, 8, worldIn, randomIn, boundingBoxIn);

		for(int x = 2; x < 5; x++)
		{
			buildFloorTile(wallBlock, x, 9, worldIn, randomIn, boundingBoxIn);
			buildWall(wallBlock, x, 9, worldIn, randomIn, boundingBoxIn, 0);
		}

		this.generateAirBox(worldIn, boundingBoxIn, 1, 1, 0, 5, 3, 7);
		this.generateAirBox(worldIn, boundingBoxIn, 2, 1, 8, 4, 3, 8);

		if(!placedChest)
			placedChest = generateChest(worldIn, boundingBoxIn, randomIn, 3, 1, 6, this.getOrientation().getOpposite(), MSLootTables.BASIC_MEDIUM_CHEST);

		if(torches[0])
			this.placeBlock(worldIn, wallTorch.setValue(WallTorchBlock.FACING, Direction.EAST), 1, 2, 3, boundingBoxIn);
		if(torches[1])
			this.placeBlock(worldIn, wallTorch.setValue(WallTorchBlock.FACING, Direction.WEST), 5, 2, 3, boundingBoxIn);
		if(torches[2])
			this.placeBlock(worldIn, wallTorch.setValue(WallTorchBlock.FACING, Direction.EAST), 1, 2, 6, boundingBoxIn);
		if(torches[3])
			this.placeBlock(worldIn, wallTorch.setValue(WallTorchBlock.FACING, Direction.WEST), 5, 2, 6, boundingBoxIn);

		if(!placedOgres[0])
			placedOgres[0] = placeUnderling(this.boundingBox.x0 - 3, this.boundingBox.z0 - 3, boundingBoxIn, worldIn, randomIn);
		if(!placedOgres[1])
			placedOgres[1] = placeUnderling(this.boundingBox.x1  + 3, this.boundingBox.z0 - 3, boundingBoxIn, worldIn, randomIn);
		if(!placedOgres[2])
			placedOgres[2] = placeUnderling(this.boundingBox.x0 - 3, this.boundingBox.z1 + 3, boundingBoxIn, worldIn, randomIn);
		if(!placedOgres[3])
			placedOgres[3] = placeUnderling(this.boundingBox.x1 + 3, this.boundingBox.z1 + 3, boundingBoxIn, worldIn, randomIn);

		return true;
	}
	
	private boolean generateChest(ISeedReader worldIn, MutableBoundingBox boundingBoxIn, Random randomIn, int x, int y, int z, Direction direction, ResourceLocation lootTable)
	{
		BlockPos blockpos = new BlockPos(this.getWorldX(x, z), this.getWorldY(y), this.getWorldZ(x, z));
		return createChest(worldIn, boundingBoxIn, randomIn, blockpos, lootTable, Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, direction));
	}
	
	private void buildWall(BlockState block, int x, int z, ISeedReader world, Random rand, MutableBoundingBox boundingBox, int minY)
	{
		
		float f = z * 0.2F;
		for(int y = 1; y < 4; y++)
		{
			if(y > minY && rand.nextFloat() >= f)
				return;
			
			this.placeBlock(world, block, x, y, z, boundingBox);
			
			f -= 0.5F;
		}
	}
	
	private boolean buildFloorTile(BlockState block, int x, int z, ISeedReader world, Random rand, MutableBoundingBox boundingBox)
	{
		int y = 0;
		
		float f = (3 - z) * 0.25F;
		if(this.getBlock(world, x, y, z, boundingBox).getMaterial().isSolid())
			f -= 0.25F;
		boolean b = true;
		do
		{
			if(rand.nextFloat() >= f)
			{
				this.placeBlock(world, block, x, y, z, boundingBox);
				f = 0F;
			} else
			{
				b = false;
				f -= 0.25F;
			}
			
			y--;
		} while(this.boundingBox.y0 + y >= 0 && !this.getBlock(world, x, y, z, boundingBox).getMaterial().isSolid());
		
		return b;
	}
	
	private boolean placeUnderling(int xPos, int zPos, MutableBoundingBox boundingBox, ISeedReader world, Random rand)
	{
		if(!boundingBox.isInside(new BlockPos(xPos, 64, zPos)))
			return false;
		
		int minY = 256, maxY = 0;
		for(int x = xPos - 1; x <= xPos + 1; x++)
			for(int z = zPos - 1; z <= zPos + 1; z++)
			{
				int y = world.getHeightmapPos(Heightmap.Type.MOTION_BLOCKING, new BlockPos(x, 0, z)).getY();
				if(y < minY)
					minY = y;
				if(y > maxY)
					maxY = y;
			}
		
		if(maxY - minY < 3)
		{
			OgreEntity ogre = MSEntityTypes.OGRE.create(world.getLevel());
			if(ogre == null)
				throw new IllegalStateException("Unable to create a new ogre. Entity factory returned null!");
			ogre.setPersistenceRequired();
			ogre.moveTo(xPos + 0.5, maxY, zPos + 0.5, rand.nextFloat() * 360F, 0);
			ogre.finalizeSpawn(world, world.getCurrentDifficultyAt(new BlockPos(xPos, maxY, zPos)), SpawnReason.STRUCTURE, null, null);
			ogre.restrictTo(new BlockPos(xPos, this.boundingBox.y0, zPos), 10);
			world.addFreshEntity(ogre);
		}
		return true;
	}
}