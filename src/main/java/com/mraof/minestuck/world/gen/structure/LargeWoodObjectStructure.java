package com.mraof.minestuck.world.gen.structure;

import com.mojang.serialization.Codec;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.world.gen.feature.TemplatePlacement;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

import java.util.List;
import java.util.Optional;

public class LargeWoodObjectStructure extends Structure
{
	public static final Codec<LargeWoodObjectStructure> CODEC = simpleCodec(LargeWoodObjectStructure::new);
	
	private static final List<ResourceLocation> TEMPLATES = List.of(Minestuck.id("large_unfinished_table"), Minestuck.id("massive_framing"), Minestuck.id("massive_table"));
	
	public LargeWoodObjectStructure(StructureSettings settings)
	{
		super(settings);
	}
	
	@Override
	public Optional<GenerationStub> findGenerationPoint(GenerationContext context)
	{
		return onTopOfChunkCenter(context, Heightmap.Types.WORLD_SURFACE_WG, builder -> generatePieces(builder, context));
	}
	
	@Override
	public StructureType<?> type()
	{
		return MSStructures.LARGE_WOOD_OBJECT_TYPE.get();
	}
	
	private static void generatePieces(StructurePiecesBuilder builder, GenerationContext context)
	{
		RandomSource random = context.random();
		StructureTemplateManager templateManager = context.structureTemplateManager();
		ResourceLocation templateId = Util.getRandom(TEMPLATES, random);
		
		int x = context.chunkPos().getBlockX(random.nextInt(16)), z = context.chunkPos().getBlockZ(random.nextInt(16));
		var placement = TemplatePlacement.centeredWithRandomRotation(templateManager.getOrCreate(templateId), new BlockPos(x, 0, z), random);
		
		int minY = placement.xzCorners()
				.mapToInt(corner -> context.chunkGenerator().getBaseHeight(corner.getX(), corner.getZ(), Heightmap.Types.OCEAN_FLOOR_WG, context.heightAccessor(), context.randomState()))
				.min().orElseThrow();
		
		builder.addPiece(new SimpleTemplatePiece(templateManager, templateId, placement.zeroPos().atY(minY), placement.rotation()));
	}
}
