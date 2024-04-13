package com.mraof.minestuck.world.gen.feature;

import com.mojang.serialization.Codec;
import com.mraof.minestuck.Minestuck;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

public class BrokenSwordFeature extends Feature<NoneFeatureConfiguration>
{
	
	public BrokenSwordFeature(Codec<NoneFeatureConfiguration> codec)
	{
		super(codec);
	}
	
	@Override
	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context)
	{
		WorldGenLevel level = context.level();
		RandomSource rand = context.random();
		StructureTemplateManager templates = level.getLevel().getStructureManager();
		
		Type type = pickType(rand);
		
		BlockPos hiltPos = context.origin();
		TemplatePlacement hiltPlacement = TemplatePlacement.centeredWithRandomRotation(templates.getOrCreate(type.hiltId), hiltPos, rand);
		int hiltY = level.getHeight(Heightmap.Types.WORLD_SURFACE_WG, hiltPos.getX(), hiltPos.getZ()) - 2 - rand.nextInt(3);
		
		BlockPos bladePos = context.origin().offset(rand.nextInt(8), 0, rand.nextInt(8));
		Mirror bladeMirror = rand.nextBoolean() ? Mirror.NONE : Mirror.LEFT_RIGHT;
		TemplatePlacement bladePlacement = TemplatePlacement.centeredWithRandomRotation(templates.getOrCreate(type.bladeId), bladePos, rand, bladeMirror);
		int bladeY = level.getHeight(Heightmap.Types.WORLD_SURFACE_WG, bladePos.getX(), bladePos.getZ()) - rand.nextInt(3);
		
		hiltPlacement.placeAt(hiltY, context);
		bladePlacement.placeAt(bladeY, context);
		
		return true;
	}
	
	private static Type pickType(RandomSource rand)
	{
		if(rand.nextInt(600) == 0)
			return Type.VALUABLE;
		else
			return rand.nextBoolean() ? Type.REGULAR_0 : Type.REGULAR_1;
	}
	
	private enum Type
	{
		REGULAR_0(new ResourceLocation(Minestuck.MOD_ID, "sword_hilt_0"), new ResourceLocation(Minestuck.MOD_ID, "sword_blade_0")),
		REGULAR_1(new ResourceLocation(Minestuck.MOD_ID, "sword_hilt_1"), new ResourceLocation(Minestuck.MOD_ID, "sword_blade_1")),
		VALUABLE(new ResourceLocation(Minestuck.MOD_ID, "valuable_sword_hilt"), new ResourceLocation(Minestuck.MOD_ID, "valuable_sword_blade")),
		;
		
		private final ResourceLocation hiltId, bladeId;
		
		Type(ResourceLocation hiltId, ResourceLocation bladeId)
		{
			this.hiltId = hiltId;
			this.bladeId = bladeId;
		}
	}
}
