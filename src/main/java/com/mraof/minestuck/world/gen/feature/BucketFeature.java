package com.mraof.minestuck.world.gen.feature;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.world.gen.feature.structure.blocks.StructureBlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.templatesystem.*;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Random;

public class BucketFeature extends Feature<NoneFeatureConfiguration>
{
	private static final ResourceLocation STRUCTURE_BUCKET_0 = new ResourceLocation(Minestuck.MOD_ID, "bucket_0");
	private static final ResourceLocation STRUCTURE_BUCKET_1 = new ResourceLocation(Minestuck.MOD_ID, "bucket_1");
	private static final ResourceLocation STRUCTURE_BUCKET_WITH_HANDLE_0 = new ResourceLocation(Minestuck.MOD_ID, "bucket_with_handle_0");
	private static final ResourceLocation STRUCTURE_BUCKET_WITH_HANDLE_1 = new ResourceLocation(Minestuck.MOD_ID, "bucket_with_handle_1");
	
	public BucketFeature(Codec<NoneFeatureConfiguration> codec)
	{
		super(codec);
	}
	
	
	@Override
	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context)
	{
		BlockPos pos = context.origin();
		WorldGenLevel level = context.level();
		Random rand = context.random();
		ResourceLocation structure;
		if(rand.nextFloat() < 0.6F)
		{
			structure = rand.nextFloat() < 0.7F ? STRUCTURE_BUCKET_0 : STRUCTURE_BUCKET_1;
		} else
		{
			structure = rand.nextFloat() < 0.7F ? STRUCTURE_BUCKET_WITH_HANDLE_0 : STRUCTURE_BUCKET_WITH_HANDLE_1;
		}
		
		Rotation rotation = Rotation.getRandom(rand);
		StructureManager templates = context.level().getLevel().getStructureManager();
		StructureTemplate template = templates.getOrCreate(structure);
		
		ChunkPos chunkPos = new ChunkPos(pos);
		BoundingBox boundingBox = new BoundingBox(chunkPos.getMinBlockX() - 16, level.getMinBuildHeight(), chunkPos.getMinBlockZ() - 16, chunkPos.getMaxBlockX() + 16, level.getMaxBuildHeight(), chunkPos.getMaxBlockZ() + 16);
		StructurePlaceSettings settings = new StructurePlaceSettings().setRotation(rotation).setBoundingBox(boundingBox).setRandom(rand)
				.addProcessor(new StructureBlockRegistryProcessor(StructureBlockRegistry.getOrDefault(context.chunkGenerator())));
		
		BlockState bucketFluid;
		if(rand.nextBoolean())
		{
			SimpleWeightedRandomList.Builder<BlockState> list = SimpleWeightedRandomList.builder();
			list.add(Blocks.AIR.defaultBlockState(), 50);
			for(Fluid fluid : ForgeRegistries.FLUIDS)
			{
				Rarity rarity = fluid.getAttributes().getRarity();
				if(rarity == Rarity.COMMON)
					list.add(fluid.defaultFluidState().createLegacyBlock(), 50);
				else if(rarity == Rarity.UNCOMMON)
					list.add(fluid.defaultFluidState().createLegacyBlock(), 10);
				else if(rarity == Rarity.RARE)
					list.add(fluid.defaultFluidState().createLegacyBlock(), 1);
			}
			
			bucketFluid = list.build().getRandomValue(rand).orElseThrow();
		} else
		{
			StructureBlockRegistry blocks = StructureBlockRegistry.getOrDefault(context.chunkGenerator());
			bucketFluid = blocks.getBlockState(rand.nextBoolean() ? "ocean" : "river");
		}
		
		settings.addProcessor(new RuleProcessor(ImmutableList.of(new ProcessorRule(new BlockMatchTest(Blocks.BLUE_STAINED_GLASS), AlwaysTrueTest.INSTANCE, bucketFluid))));
		
		Vec3i size = template.getSize(rotation);
		int xOffset = rand.nextInt(16 - size.getX()), zOffset = rand.nextInt(16 - size.getZ());
		pos = level.getHeightmapPos(Heightmap.Types.WORLD_SURFACE_WG, pos.offset(xOffset + size.getX()/2, 0, zOffset + size.getZ()/2));
		if(!level.getFluidState(pos.below()).isEmpty())
			return false;
		
		BlockPos structurePos = template.getZeroPositionWithTransform(pos.offset(-size.getX()/2, -rand.nextInt(3), -size.getZ()/2), Mirror.NONE, rotation);
		template.placeInWorld(level, structurePos, structurePos, settings, rand, Block.UPDATE_INVISIBLE);
		
		return true;
	}
}