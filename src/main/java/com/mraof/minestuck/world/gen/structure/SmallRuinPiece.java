package com.mraof.minestuck.world.gen.structure;

import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.entity.underling.OgreEntity;
import com.mraof.minestuck.item.loot.MSLootTables;
import com.mraof.minestuck.world.gen.structure.blocks.StructureBlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.WallTorchBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.ScatteredFeaturePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;

public class SmallRuinPiece extends ScatteredFeaturePiece
{
	private final boolean[] torches = new boolean[4];
	private final boolean[] placedOgres = new boolean[4];
	private boolean placedChest;
	
	public SmallRuinPiece(RandomSource random, int minX, int minZ, float skyLight)
	{
		super(MSStructures.SMALL_RUIN_PIECE.get(), minX, 64, minZ, 7, 4, 10, getRandomHorizontalDirection(random));
		
		float torchChance = 1 - skyLight;
		
		torches[0] = random.nextFloat() < 0.5F * torchChance;
		torches[1] = random.nextFloat() < 0.5F * torchChance;
		torches[2] = random.nextFloat() < torchChance;
		torches[3] = random.nextFloat() < torchChance;
	}
	
	public SmallRuinPiece(CompoundTag nbt)
	{
		super(MSStructures.SMALL_RUIN_PIECE.get(), nbt);
		
		for(int i = 0; i < 4; i++)
			torches[i] = nbt.getBoolean("torch" + i);
		for(int i = 0; i < 4; i++)
			placedOgres[i] = nbt.getBoolean("placed_ogres" + i);
		placedChest = nbt.getBoolean("placed_chest");
	}
	
	@Override
	protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag nbt)
	{
		super.addAdditionalSaveData(context, nbt);
		
		for(int i = 0; i < 4; i++)
			nbt.putBoolean("torch" + i, torches[i]);
		for(int i = 0; i < 4; i++)
			nbt.putBoolean("placed_ogres" + i, placedOgres[i]);
		nbt.putBoolean("placed_chest", placedChest);
	}

	@Override
	public void postProcess(WorldGenLevel level, StructureManager manager, ChunkGenerator chunkGeneratorIn, RandomSource randomIn, BoundingBox boundingBoxIn, ChunkPos chunkPosIn, BlockPos pos)
	{
		if(!updateAverageGroundHeight(level, boundingBoxIn, 0)) //where the height is determined, uses ScatteredStructurePiece "Heightmap.Type.MOTION_BLOCKING_NO_LEAVES"
			return;

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
					if(buildFloorTile(wallBlock, x, z, level, randomIn, boundingBoxIn))
						buildWall(wallBlock, x, z, level, randomIn, boundingBoxIn, torch ? 2 : 0);
				} else buildFloorTile(floorBlock, x, z, level, randomIn, boundingBoxIn);

		for(int x = 1; x < 6; x++)
			if(x == 1 || x == 5)
			{
				buildFloorTile(wallBlock, x, 8, level, randomIn, boundingBoxIn);
				buildWall(wallBlock, x, 8, level, randomIn, boundingBoxIn, 0);
				if(this.getBlock(level, x, 2, 8, boundingBoxIn) == wallBlock)
					this.placeBlock(level, wallDecor, x, 2, 8, boundingBoxIn);
			} else buildFloorTile(floorBlock, x, 8, level, randomIn, boundingBoxIn);

		for(int x = 2; x < 5; x++)
		{
			buildFloorTile(wallBlock, x, 9, level, randomIn, boundingBoxIn);
			buildWall(wallBlock, x, 9, level, randomIn, boundingBoxIn, 0);
		}

		this.generateAirBox(level, boundingBoxIn, 1, 1, 0, 5, 3, 7);
		this.generateAirBox(level, boundingBoxIn, 2, 1, 8, 4, 3, 8);

		if(!placedChest)
			placedChest = generateChest(level, boundingBoxIn, randomIn, 3, 1, 6, this.getOrientation().getOpposite(), MSLootTables.BASIC_MEDIUM_CHEST);

		if(torches[0])
			this.placeBlock(level, wallTorch.setValue(WallTorchBlock.FACING, Direction.EAST), 1, 2, 3, boundingBoxIn);
		if(torches[1])
			this.placeBlock(level, wallTorch.setValue(WallTorchBlock.FACING, Direction.WEST), 5, 2, 3, boundingBoxIn);
		if(torches[2])
			this.placeBlock(level, wallTorch.setValue(WallTorchBlock.FACING, Direction.EAST), 1, 2, 6, boundingBoxIn);
		if(torches[3])
			this.placeBlock(level, wallTorch.setValue(WallTorchBlock.FACING, Direction.WEST), 5, 2, 6, boundingBoxIn);

		if(!placedOgres[0])
			placedOgres[0] = placeUnderling(this.boundingBox.minX() - 3, this.boundingBox.minZ() - 3, boundingBoxIn, level, randomIn);
		if(!placedOgres[1])
			placedOgres[1] = placeUnderling(this.boundingBox.maxX()  + 3, this.boundingBox.minZ() - 3, boundingBoxIn, level, randomIn);
		if(!placedOgres[2])
			placedOgres[2] = placeUnderling(this.boundingBox.minX() - 3, this.boundingBox.maxZ() + 3, boundingBoxIn, level, randomIn);
		if(!placedOgres[3])
			placedOgres[3] = placeUnderling(this.boundingBox.maxX() + 3, this.boundingBox.maxZ() + 3, boundingBoxIn, level, randomIn);
	}
	
	private boolean generateChest(WorldGenLevel level, BoundingBox boundingBoxIn, RandomSource randomIn, int x, int y, int z, Direction direction, ResourceLocation lootTable)
	{
		BlockPos blockpos = new BlockPos(this.getWorldX(x, z), this.getWorldY(y), this.getWorldZ(x, z));
		return createChest(level, boundingBoxIn, randomIn, blockpos, lootTable, Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, direction));
	}
	
	private void buildWall(BlockState block, int x, int z, WorldGenLevel level, RandomSource rand, BoundingBox boundingBox, int minY)
	{
		
		float f = z * 0.2F;
		for(int y = 1; y < 4; y++)
		{
			if(y > minY && rand.nextFloat() >= f)
				return;
			
			this.placeBlock(level, block, x, y, z, boundingBox);
			
			f -= 0.5F;
		}
	}
	
	private boolean buildFloorTile(BlockState block, int x, int z, WorldGenLevel level, RandomSource rand, BoundingBox boundingBox)
	{
		int y = 0;
		
		float f = (3 - z) * 0.25F;
		if(this.getBlock(level, x, y, z, boundingBox).isSolid())
			f -= 0.25F;
		boolean b = true;
		do
		{
			if(rand.nextFloat() >= f)
			{
				this.placeBlock(level, block, x, y, z, boundingBox);
				f = 0F;
			} else
			{
				b = false;
				f -= 0.25F;
			}
			
			y--;
		} while(this.boundingBox.minY() + y >= 0 && !this.getBlock(level, x, y, z, boundingBox).isSolid());
		
		return b;
	}
	
	private boolean placeUnderling(int xPos, int zPos, BoundingBox boundingBox, WorldGenLevel level, RandomSource rand)
	{
		if(!boundingBox.isInside(new BlockPos(xPos, 64, zPos)))
			return false;
		
		int minY = level.getMaxBuildHeight(), maxY = level.getMinBuildHeight();
		for(int x = xPos - 1; x <= xPos + 1; x++)
			for(int z = zPos - 1; z <= zPos + 1; z++)
			{
				int y = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, new BlockPos(x, 0, z)).getY();
				if(y < minY)
					minY = y;
				if(y > maxY)
					maxY = y;
			}
		
		if(maxY - minY < 3)
		{
			OgreEntity ogre = MSEntityTypes.OGRE.get().create(level.getLevel());
			if(ogre == null)
				throw new IllegalStateException("Unable to create a new ogre. Entity factory returned null!");
			ogre.setPersistenceRequired();
			ogre.moveTo(xPos + 0.5, maxY, zPos + 0.5, rand.nextFloat() * 360F, 0);
			ogre.finalizeSpawn(level, level.getCurrentDifficultyAt(new BlockPos(xPos, maxY, zPos)), MobSpawnType.STRUCTURE, null, null);
			ogre.restrictTo(new BlockPos(xPos, this.boundingBox.minY(), zPos), 10);
			level.addFreshEntity(ogre);
		}
		return true;
	}
}
