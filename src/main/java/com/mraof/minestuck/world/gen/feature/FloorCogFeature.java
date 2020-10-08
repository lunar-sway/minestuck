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

public class FloorCogFeature extends Feature<NoFeatureConfig>
{
	private static final ResourceLocation STRUCTURE_LARGE_FLOOR_COG_1 = new ResourceLocation(Minestuck.MOD_ID, "large_floor_cog_1");
	private static final ResourceLocation STRUCTURE_LARGE_FLOOR_COG_2 = new ResourceLocation(Minestuck.MOD_ID, "large_floor_cog_2");
	
	public FloorCogFeature(Codec<NoFeatureConfig> codec)
	{
		super(codec);
	}
	
	@Override
	public boolean func_241855_a(ISeedReader world, ChunkGenerator generator, Random rand, BlockPos pos, NoFeatureConfig config)
	{
		TemplateManager templates = world.getWorld().getStructureTemplateManager();
		Template template = templates.getTemplateDefaulted(rand.nextBoolean() ? STRUCTURE_LARGE_FLOOR_COG_1 : STRUCTURE_LARGE_FLOOR_COG_2);
		
		PlacementSettings settings = new PlacementSettings().setChunk(new ChunkPos(pos)).setRandom(rand).addProcessor(StructureBlockRegistryProcessor.INSTANCE);
		
		BlockPos size = template.getSize();
		int xOffset = rand.nextInt(16 - size.getX()), zOffset = rand.nextInt(16 - size.getZ());
		
		int yMin = Integer.MAX_VALUE, yMax = 0;
		for(BlockPos floorPos : BlockPos.getAllInBoxMutable(0, 0, 0, size.getX(), 0, size.getZ()))
		{
			int y = world.getHeight(Heightmap.Type.WORLD_SURFACE_WG, pos.getX() + floorPos.getX() + xOffset, pos.getZ() + floorPos.getZ() + zOffset);
			yMax = Math.max(yMax, y);
			yMin = Math.min(yMin, y);
		}
		
		int y;
		if(yMin == yMax)
			y = yMin - size.getY() + 1;
		else y = yMin - size.getY() + 2;
		
		BlockPos structurePos = template.getZeroPositionWithTransform(new BlockPos(pos.getX() + xOffset, y, pos.getZ() + zOffset), Mirror.NONE, Rotation.NONE);
		template.func_237146_a_(world, structurePos, structurePos, settings, rand, Constants.BlockFlags.NO_RERENDER);
		
		return true;
	}
}