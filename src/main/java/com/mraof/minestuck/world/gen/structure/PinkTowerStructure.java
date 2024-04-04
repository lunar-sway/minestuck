package com.mraof.minestuck.world.gen.structure;

import com.mojang.serialization.Codec;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.world.gen.feature.TemplatePlacement;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

import java.util.Optional;

public class PinkTowerStructure extends Structure
{
	public static final Codec<PinkTowerStructure> CODEC = simpleCodec(PinkTowerStructure::new);
	
	public PinkTowerStructure(StructureSettings settings)
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
		return MSStructureTypes.PINK_TOWER.get();
	}
	
	private static void generatePieces(StructurePiecesBuilder builder, GenerationContext context)
	{
		RandomSource random = context.random();
		StructureTemplateManager templateManager = context.structureTemplateManager();
		ResourceLocation templateId = Minestuck.id("pink_tower");
		
		int x = context.chunkPos().getBlockX(random.nextInt(16)), z = context.chunkPos().getBlockZ(random.nextInt(16));
		var placement = TemplatePlacement.centeredWithRandomRotation(templateManager.getOrCreate(templateId), new BlockPos(x, 0, z), random);
		
		int minY = placement.xzCorners()
				.mapToInt(corner -> context.chunkGenerator().getBaseHeight(corner.getX(), corner.getZ(), Heightmap.Types.WORLD_SURFACE_WG, context.heightAccessor(), context.randomState()))
				.min().orElseThrow();
		
		builder.addPiece(new SimpleTemplatePiece(templateManager, templateId, placement.zeroPos().atY(minY), placement.rotation()));
	}
}