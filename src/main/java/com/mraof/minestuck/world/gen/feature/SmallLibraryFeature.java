package com.mraof.minestuck.world.gen.feature;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.world.gen.structure.blocks.StructureBlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.templatesystem.*;

import java.util.Random;

public class SmallLibraryFeature extends Feature<NoneFeatureConfiguration>
{
	private static final ResourceLocation STRUCTURE_SMALL_LIBRARY = new ResourceLocation(Minestuck.MOD_ID, "small_library");
	
	public SmallLibraryFeature(Codec<NoneFeatureConfiguration> codec)
	{
		super(codec);
	}
	
	@Override
	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context)
	{
		WorldGenLevel level = context.level();
		BlockPos pos = context.origin();
		Random rand = context.random();
		Rotation rotation = Rotation.getRandom(rand);
		StructureManager templates = level.getLevel().getStructureManager();
		StructureTemplate template = templates.getOrCreate(STRUCTURE_SMALL_LIBRARY);
		
		ChunkPos chunkPos = new ChunkPos(pos);
		BoundingBox boundingBox = new BoundingBox(chunkPos.getMinBlockX() - 16, level.getMinBuildHeight(), chunkPos.getMinBlockZ() - 16, chunkPos.getMaxBlockX() + 16, level.getMaxBuildHeight(), chunkPos.getMaxBlockZ() + 16);
		StructurePlaceSettings settings = new StructurePlaceSettings().setRotation(rotation).setBoundingBox(boundingBox).setRandom(rand)
				.addProcessor(new StructureBlockRegistryProcessor(StructureBlockRegistry.getOrDefault(context.chunkGenerator())));
		
		if(rand.nextBoolean())
		{	//Replace 20% of bookcases with air
			settings.addProcessor(new RuleProcessor(ImmutableList.of(new ProcessorRule(new RandomBlockMatchTest(Blocks.BOOKSHELF, 0.2F), AlwaysTrueTest.INSTANCE, Blocks.AIR.defaultBlockState()))));
		}
		
		Vec3i size = template.getSize(rotation);
		pos = pos.offset(-size.getX()/2, 0, -size.getZ()/2);
		
		int minX = template.getSize().getX()/2 - 1, maxX = template.getSize().getX()/2 + 1, z1 = 0, z2 = template.getSize().getZ();
		BoundingBox door1, door2;
		if(rotation == Rotation.NONE || rotation == Rotation.CLOCKWISE_180)
		{
			door1 = new BoundingBox(minX, 0, z1, maxX, 0, z1);
			door2 = new BoundingBox(minX, 0, z2, maxX, 0, z2);
		} else
		{
			door1 = new BoundingBox(z1, 0, minX, z1, 0, maxX);
			door2 = new BoundingBox(z2, 0, minX, z2, 0, maxX);
		}
		
		int y1 = level.getMinBuildHeight();
		for(BlockPos doorPos : BlockPos.betweenClosed(door1.minX(), 0, door1.minZ(), door1.maxX(), 0, door1.maxZ()))
		{
			y1 = Math.max(y1, level.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, pos.getX() + doorPos.getX(), pos.getZ() + doorPos.getZ()));
		}
		int y2 = level.getMinBuildHeight();
		for(BlockPos doorPos : BlockPos.betweenClosed(door2.minX(), 0, door2.minZ(), door2.maxX(), 0, door2.maxZ()))
		{
			y2 = Math.max(y2, level.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, pos.getX() + doorPos.getX(), pos.getZ() + doorPos.getZ()));
		}
		int y = Math.min(y1, y2) - 1;
		
		BlockPos structurePos = template.getZeroPositionWithTransform(pos.atY(y), Mirror.NONE, rotation);
		template.placeInWorld(level, structurePos, structurePos, settings, rand, Block.UPDATE_INVISIBLE);
		
		return true;
	}
}
