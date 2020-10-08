package com.mraof.minestuck.world.gen.feature;

import com.mojang.serialization.Codec;
import com.mraof.minestuck.Minestuck;
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
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.minecraftforge.common.util.Constants;

import java.util.Random;

public class BrokenSwordFeature extends Feature<NoFeatureConfig>
{
	private static final ResourceLocation STRUCTURE_SWORD_HILT_0 = new ResourceLocation(Minestuck.MOD_ID, "sword_hilt_0");
	private static final ResourceLocation STRUCTURE_SWORD_HILT_1 = new ResourceLocation(Minestuck.MOD_ID, "sword_hilt_1");
	private static final ResourceLocation STRUCTURE_VALUABLE_SWORD_HILT = new ResourceLocation(Minestuck.MOD_ID, "valuable_sword_hilt");
	private static final ResourceLocation STRUCTURE_SWORD_BLADE_0 = new ResourceLocation(Minestuck.MOD_ID, "sword_blade_0");
	private static final ResourceLocation STRUCTURE_SWORD_BLADE_1 = new ResourceLocation(Minestuck.MOD_ID, "sword_blade_1");
	private static final ResourceLocation STRUCTURE_VALUABLE_SWORD_BLADE = new ResourceLocation(Minestuck.MOD_ID, "valuable_sword_blade");
	
	public BrokenSwordFeature(Codec<NoFeatureConfig> codec)
	{
		super(codec);
	}
	
	@Override
	public boolean func_241855_a(ISeedReader world, ChunkGenerator generator, Random rand, BlockPos pos, NoFeatureConfig config)
	{
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
		
		Rotation hiltRotation = Rotation.randomRotation(rand), bladeRotation = Rotation.randomRotation(rand);
		Mirror bladeMirror = rand.nextBoolean() ? Mirror.NONE : Mirror.LEFT_RIGHT;
		TemplateManager templates = world.getWorld().getStructureTemplateManager();
		Template hiltTemplate = templates.getTemplateDefaulted(hilt), bladeTemplate = templates.getTemplateDefaulted(blade);
		
		PlacementSettings settings = new PlacementSettings().setChunk(new ChunkPos(pos)).setBoundingBox(new MutableBoundingBox(pos.getX() - 8, 0, pos.getZ() - 8, pos.getX() + 24 - 1, 255, pos.getZ() + 24 - 1)).setRandom(rand);
		
		BlockPos hiltSize = hiltTemplate.transformedSize(hiltRotation);
		int xOffset = rand.nextInt(32 - hiltSize.getX()) - 8;
		int zOffset = rand.nextInt(32 - hiltSize.getZ()) - 8;
		BlockPos hiltPos = world.getHeight(Heightmap.Type.WORLD_SURFACE_WG, pos.add(xOffset + hiltSize.getX()/2, 0, zOffset + hiltSize.getZ()/2));
		hiltPos = hiltTemplate.getZeroPositionWithTransform(hiltPos.add(-hiltSize.getX()/2, -(2 + rand.nextInt(3)), -hiltSize.getZ()/2), Mirror.NONE, hiltRotation);
		
		BlockPos bladeSize = bladeTemplate.transformedSize(bladeRotation);
		xOffset = rand.nextInt(32 - bladeSize.getX()) - 8;
		zOffset = rand.nextInt(32 - bladeSize.getZ()) - 8;
		BlockPos bladePos = world.getHeight(Heightmap.Type.WORLD_SURFACE_WG, pos.add(xOffset + bladeSize.getX()/2, 0, zOffset + bladeSize.getZ()/2));
		bladePos = bladeTemplate.getZeroPositionWithTransform(bladePos.add(-bladeSize.getX()/2, -rand.nextInt(3), -bladeSize.getZ()/2), bladeMirror, bladeRotation);
		
		settings.setRotation(hiltRotation);
		hiltTemplate.func_237146_a_(world, hiltPos, hiltPos, settings, rand, Constants.BlockFlags.NO_RERENDER);
		settings.setRotation(bladeRotation).setMirror(bladeMirror);
		bladeTemplate.func_237146_a_(world, bladePos, bladePos, settings, rand, Constants.BlockFlags.NO_RERENDER);
		
		return true;
	}
}