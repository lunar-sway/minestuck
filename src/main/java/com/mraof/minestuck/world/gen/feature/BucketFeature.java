package com.mraof.minestuck.world.gen.feature;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.world.gen.feature.structure.blocks.StructureBlockRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Rarity;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.WeightedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.template.*;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Random;

public class BucketFeature extends Feature<NoFeatureConfig>
{
	private static final ResourceLocation STRUCTURE_BUCKET_0 = new ResourceLocation(Minestuck.MOD_ID, "bucket_0");
	private static final ResourceLocation STRUCTURE_BUCKET_1 = new ResourceLocation(Minestuck.MOD_ID, "bucket_1");
	private static final ResourceLocation STRUCTURE_BUCKET_WITH_HANDLE_0 = new ResourceLocation(Minestuck.MOD_ID, "bucket_with_handle_0");
	private static final ResourceLocation STRUCTURE_BUCKET_WITH_HANDLE_1 = new ResourceLocation(Minestuck.MOD_ID, "bucket_with_handle_1");
	
	public BucketFeature(Codec<NoFeatureConfig> codec)
	{
		super(codec);
	}
	
	@Override
	public boolean generate(ISeedReader world, ChunkGenerator generator, Random rand, BlockPos pos, NoFeatureConfig config)
	{
		ResourceLocation structure;
		if(rand.nextFloat() < 0.6F)
		{
			structure = rand.nextFloat() < 0.7F ? STRUCTURE_BUCKET_0 : STRUCTURE_BUCKET_1;
		} else
		{
			structure = rand.nextFloat() < 0.7F ? STRUCTURE_BUCKET_WITH_HANDLE_0 : STRUCTURE_BUCKET_WITH_HANDLE_1;
		}
		
		Rotation rotation = Rotation.randomRotation(rand);
		TemplateManager templates = world.getWorld().getStructureTemplateManager();
		Template template = templates.getTemplateDefaulted(structure);
		
		PlacementSettings settings = new PlacementSettings().setRotation(rotation).setChunk(new ChunkPos(pos)).setRandom(rand).addProcessor(StructureBlockRegistryProcessor.INSTANCE);
		
		BlockState bucketFluid;
		if(rand.nextBoolean())
		{
			WeightedList<BlockState> list = new WeightedList<>();
			list.addWeighted(Blocks.AIR.getDefaultState(), 50);
			for(Fluid fluid : ForgeRegistries.FLUIDS)
			{
				Rarity rarity = fluid.getAttributes().getRarity();
				if(rarity == Rarity.COMMON)
					list.addWeighted(fluid.getDefaultState().getBlockState(), 50);
				else if(rarity == Rarity.UNCOMMON)
					list.addWeighted(fluid.getDefaultState().getBlockState(), 10);
				else if(rarity == Rarity.RARE)
					list.addWeighted(fluid.getDefaultState().getBlockState(), 1);
			}
			
			bucketFluid = list.getRandomValue(rand);
		} else
		{
			StructureBlockRegistry blocks = StructureBlockRegistry.getOrDefault(generator);
			bucketFluid = blocks.getBlockState(rand.nextBoolean() ? "ocean" : "river");
		}
		
		settings.addProcessor(new RuleStructureProcessor(ImmutableList.of(new RuleEntry(new BlockMatchRuleTest(Blocks.BLUE_STAINED_GLASS), AlwaysTrueRuleTest.INSTANCE, bucketFluid))));
		
		BlockPos size = template.transformedSize(rotation);
		int xOffset = rand.nextInt(16 - size.getX()), zOffset = rand.nextInt(16 - size.getZ());
		pos = world.getHeight(Heightmap.Type.WORLD_SURFACE_WG, pos.add(xOffset + size.getX()/2, 0, zOffset + size.getZ()/2));
		if(!world.getFluidState(pos.down()).isEmpty())
			return false;
		
		BlockPos structurePos = template.getZeroPositionWithTransform(pos.add(-size.getX()/2, -rand.nextInt(3), -size.getZ()/2), Mirror.NONE, rotation);
		template.func_237146_a_(world, structurePos, structurePos, settings, rand, Constants.BlockFlags.NO_RERENDER);
		
		return true;
	}
}