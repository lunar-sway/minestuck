package com.mraof.minestuck.world.gen.feature;

import com.mojang.serialization.Codec;
import com.mraof.minestuck.Minestuck;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

public class BrokenSwordFeature extends Feature<NoneFeatureConfiguration>
{
	private static final ResourceLocation STRUCTURE_SWORD_HILT_0 = new ResourceLocation(Minestuck.MOD_ID, "sword_hilt_0");
	private static final ResourceLocation STRUCTURE_SWORD_HILT_1 = new ResourceLocation(Minestuck.MOD_ID, "sword_hilt_1");
	private static final ResourceLocation STRUCTURE_VALUABLE_SWORD_HILT = new ResourceLocation(Minestuck.MOD_ID, "valuable_sword_hilt");
	private static final ResourceLocation STRUCTURE_SWORD_BLADE_0 = new ResourceLocation(Minestuck.MOD_ID, "sword_blade_0");
	private static final ResourceLocation STRUCTURE_SWORD_BLADE_1 = new ResourceLocation(Minestuck.MOD_ID, "sword_blade_1");
	private static final ResourceLocation STRUCTURE_VALUABLE_SWORD_BLADE = new ResourceLocation(Minestuck.MOD_ID, "valuable_sword_blade");
	
	public BrokenSwordFeature(Codec<NoneFeatureConfiguration> codec)
	{
		super(codec);
	}
	
	@Override
	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context)
	{
		WorldGenLevel level = context.level();
		BlockPos pos = context.origin();
		RandomSource rand = context.random();
		
		ResourceLocation hilt, blade;
		if(rand.nextInt(600) == 0)
		{
			hilt = STRUCTURE_VALUABLE_SWORD_HILT;
			blade = STRUCTURE_VALUABLE_SWORD_BLADE;
		} else if(rand.nextBoolean())
		{
			hilt = STRUCTURE_SWORD_HILT_0;
			blade = STRUCTURE_SWORD_BLADE_0;
		} else
		{
			hilt = STRUCTURE_SWORD_HILT_1;
			blade = STRUCTURE_SWORD_BLADE_1;
		}
		
		Rotation hiltRotation = Rotation.getRandom(rand), bladeRotation = Rotation.getRandom(rand);
		Mirror bladeMirror = rand.nextBoolean() ? Mirror.NONE : Mirror.LEFT_RIGHT;
		StructureTemplateManager templates = level.getLevel().getStructureManager();
		StructureTemplate hiltTemplate = templates.getOrCreate(hilt), bladeTemplate = templates.getOrCreate(blade);
		
		StructurePlaceSettings settings = new StructurePlaceSettings().setBoundingBox(new BoundingBox(pos.getX() - 8, 0, pos.getZ() - 8, pos.getX() + 24 - 1, 255, pos.getZ() + 24 - 1)).setRandom(rand);
		
		Vec3i hiltSize = hiltTemplate.getSize(hiltRotation);
		BlockPos hiltPos = level.getHeightmapPos(Heightmap.Types.WORLD_SURFACE_WG, pos).below(2 + rand.nextInt(3));
		hiltPos = hiltTemplate.getZeroPositionWithTransform(hiltPos.offset(-hiltSize.getX()/2, 0, -hiltSize.getZ()/2), Mirror.NONE, hiltRotation);
		
		Vec3i bladeSize = bladeTemplate.getSize(bladeRotation);
		BlockPos bladePos = pos.offset(rand.nextInt(8), 0, rand.nextInt(8));
		bladePos = level.getHeightmapPos(Heightmap.Types.WORLD_SURFACE_WG, bladePos).below(rand.nextInt(3));
		bladePos = bladeTemplate.getZeroPositionWithTransform(bladePos.offset(-bladeSize.getX()/2, 0, -bladeSize.getZ()/2), bladeMirror, bladeRotation);
		
		settings.setRotation(hiltRotation);
		hiltTemplate.placeInWorld(level, hiltPos, hiltPos, settings, rand, Block.UPDATE_INVISIBLE);
		settings.setRotation(bladeRotation).setMirror(bladeMirror);
		bladeTemplate.placeInWorld(level, bladePos, bladePos, settings, rand, Block.UPDATE_INVISIBLE);
		
		return true;
	}
}