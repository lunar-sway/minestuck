package com.mraof.minestuck.world.gen.feature;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mraof.minestuck.Minestuck;
import net.minecraft.block.Blocks;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.template.*;
import net.minecraftforge.common.util.Constants;

import java.util.Random;

public class SmallLibraryFeature extends Feature<NoFeatureConfig>
{
	private static final ResourceLocation STRUCTURE_SMALL_LIBRARY = new ResourceLocation(Minestuck.MOD_ID, "small_library");
	
	public SmallLibraryFeature(Codec<NoFeatureConfig> codec)
	{
		super(codec);
	}
	
	@Override
	public boolean place(ISeedReader worldIn, ChunkGenerator generator, Random rand, BlockPos pos, NoFeatureConfig config)
	{
		Rotation rotation = Rotation.getRandom(rand);
		TemplateManager templates = worldIn.getLevel().getStructureManager();
		Template template = templates.getOrCreate(STRUCTURE_SMALL_LIBRARY);
		
		PlacementSettings settings = new PlacementSettings().setRotation(rotation).setChunkPos(new ChunkPos(pos)).setRandom(rand).addProcessor(StructureBlockRegistryProcessor.INSTANCE);
		
		if(rand.nextBoolean())
		{	//Replace 20% of bookcases with air
			settings.addProcessor(new RuleStructureProcessor(ImmutableList.of(new RuleEntry(new RandomBlockMatchRuleTest(Blocks.BOOKSHELF, 0.2F), AlwaysTrueRuleTest.INSTANCE, Blocks.AIR.defaultBlockState()))));
		}
		
		BlockPos size = template.getSize(rotation);
		int xOffset = rand.nextInt(16 - size.getX()), zOffset = rand.nextInt(16 - size.getZ());
		int minX = template.getSize().getX()/2 - 1, maxX = template.getSize().getX()/2 + 1, z1 = 0, z2 = template.getSize().getZ();
		MutableBoundingBox door1, door2;
		if(rotation == Rotation.NONE || rotation == Rotation.CLOCKWISE_180)
		{
			door1 = new MutableBoundingBox(minX, 0, z1, maxX, 0, z1);
			door2 = new MutableBoundingBox(minX, 0, z2, maxX, 0, z2);
		} else
		{
			door1 = new MutableBoundingBox(z1, 0, minX, z1, 0, maxX);
			door2 = new MutableBoundingBox(z2, 0, minX, z2, 0, maxX);
		}
		
		int y1 = 0;
		for(BlockPos doorPos : BlockPos.betweenClosed(door1.x0, 0, door1.z0, door1.x1, 0, door1.z1))
		{
			y1 = Math.max(y1, worldIn.getHeight(Heightmap.Type.OCEAN_FLOOR_WG, pos.getX() + doorPos.getX() + xOffset, pos.getZ() + doorPos.getZ() + zOffset));
		}
		int y2 = 0;
		for(BlockPos doorPos : BlockPos.betweenClosed(door2.x0, 0, door2.z0, door2.x1, 0, door2.z1))
		{
			y2 = Math.max(y2, worldIn.getHeight(Heightmap.Type.OCEAN_FLOOR_WG, pos.getX() + doorPos.getX() + xOffset, pos.getZ() + doorPos.getZ() + zOffset));
		}
		int y = Math.min(y1, y2) - 1;
		
		BlockPos structurePos = template.getZeroPositionWithTransform(new BlockPos(pos.getX() + xOffset, y, pos.getZ() + zOffset), Mirror.NONE, rotation);
		template.placeInWorld(worldIn, structurePos, structurePos, settings, rand, Constants.BlockFlags.NO_RERENDER);
		
		return true;
	}
}
