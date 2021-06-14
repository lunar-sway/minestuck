package com.mraof.minestuck.world.gen.feature;

import com.mojang.serialization.Codec;
import com.mraof.minestuck.Minestuck;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.minecraftforge.common.util.Constants;

import java.util.Random;

public class CogFeature extends Feature<NoFeatureConfig>
{
	private static final ResourceLocation STRUCTURE_SMALL_COG = new ResourceLocation(Minestuck.MOD_ID, "small_cog");
	private static final ResourceLocation STRUCTURE_LARGE_COG_1 = new ResourceLocation(Minestuck.MOD_ID, "large_cog_1");
	private static final ResourceLocation STRUCTURE_LARGE_COG_2 = new ResourceLocation(Minestuck.MOD_ID, "large_cog_2");
	
	public CogFeature(Codec<NoFeatureConfig> codec)
	{
		super(codec);
	}
	
	@Override
	public boolean place(ISeedReader world, ChunkGenerator generator, Random rand, BlockPos pos, NoFeatureConfig config)
	{
		Rotation rotation = Rotation.getRandom(rand);
		TemplateManager templates = world.getLevel().getStructureManager();
		Template template;
		boolean big = rand.nextInt(10) == 0;
		if(big)
			template = templates.getOrCreate(rand.nextBoolean() ? STRUCTURE_LARGE_COG_1 : STRUCTURE_LARGE_COG_2);
		else template = templates.getOrCreate(STRUCTURE_SMALL_COG);
		
		PlacementSettings settings = new PlacementSettings().setRotation(rotation).setChunkPos(new ChunkPos(pos)).setRandom(rand).addProcessor(StructureBlockRegistryProcessor.INSTANCE);
		
		BlockPos size = template.getSize(rotation);
		int xOffset = rand.nextInt(16 - size.getX()), zOffset = rand.nextInt(16 - size.getZ());
		
		int yMin = Integer.MAX_VALUE;
		for(BlockPos floorPos : BlockPos.betweenClosed(0, 0, 0, size.getX(), 0, size.getZ()))
		{
			int y = world.getHeight(Heightmap.Type.WORLD_SURFACE_WG, pos.getX() + floorPos.getX() + xOffset, pos.getZ() + floorPos.getZ() + zOffset);
			yMin = Math.min(yMin, y);
		}
		
		int y = Math.max(0, yMin - rand.nextInt(big ? 4 : 3));
		
		BlockPos structurePos = template.getZeroPositionWithTransform(new BlockPos(pos.getX() + xOffset, y, pos.getZ() + zOffset), Mirror.NONE, rotation);
		template.placeInWorld(world, structurePos, structurePos, settings, rand, Constants.BlockFlags.NO_RERENDER);
		
		return true;
	}
}