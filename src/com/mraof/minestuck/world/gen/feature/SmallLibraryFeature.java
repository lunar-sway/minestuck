package com.mraof.minestuck.world.gen.feature;

import com.mojang.datafixers.Dynamic;
import com.mraof.minestuck.Minestuck;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;
import java.util.function.Function;

public class SmallLibraryFeature extends Feature<NoFeatureConfig>
{
	private static final ResourceLocation STRUCTURE_SMALL_LIBRARY = new ResourceLocation(Minestuck.MOD_ID, "small_library");
	
	public SmallLibraryFeature(Function<Dynamic<?>, ? extends NoFeatureConfig> deserialize)
	{
		super(deserialize);
	}
	
	@Override
	public boolean place(IWorld worldIn, ChunkGenerator<? extends GenerationSettings> generator, Random rand, BlockPos pos, NoFeatureConfig config)
	{
		Rotation rotation = Rotation.randomRotation(rand);
		TemplateManager templates = ((ServerWorld) worldIn.getWorld()).getSaveHandler().getStructureTemplateManager();
		Template template = templates.getTemplateDefaulted(STRUCTURE_SMALL_LIBRARY);
		
		PlacementSettings settings = new PlacementSettings().setRotation(rotation).setChunk(new ChunkPos(pos)).setRandom(rand).addProcessor(StructureBlockRegistryProcessor.INSTANCE);
		
		BlockPos size = template.transformedSize(rotation);
		int xOffset = rand.nextInt(16 - size.getX()), zOffset = rand.nextInt(16 - size.getX());
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
		for(BlockPos doorPos : BlockPos.getAllInBoxMutable(door1.minX, 0, door1.minZ, door1.maxX, 0, door1.maxZ))
		{
			y1 = Math.max(y1, worldIn.getHeight(Heightmap.Type.OCEAN_FLOOR_WG, pos.getX() + doorPos.getX() + xOffset, pos.getZ() + doorPos.getZ() + zOffset));
		}
		int y2 = 0;
		for(BlockPos doorPos : BlockPos.getAllInBoxMutable(door2.minX, 0, door2.minZ, door2.maxX, 0, door2.maxZ))
		{
			y2 = Math.max(y2, worldIn.getHeight(Heightmap.Type.OCEAN_FLOOR_WG, pos.getX() + doorPos.getX() + xOffset, pos.getZ() + doorPos.getZ() + zOffset));
		}
		int y = Math.min(y1, y2) - 1;
		
		BlockPos structurePos = template.getZeroPositionWithTransform(new BlockPos(pos.getX() + xOffset, y, pos.getZ() + zOffset), Mirror.NONE, rotation);
		template.addBlocksToWorld(worldIn, structurePos, settings);
		
		return true;
	}
}
