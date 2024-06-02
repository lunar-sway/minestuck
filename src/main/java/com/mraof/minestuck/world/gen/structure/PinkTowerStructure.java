package com.mraof.minestuck.world.gen.structure;

import com.mojang.serialization.Codec;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.world.gen.feature.TemplatePlacement;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
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
		return MSStructures.PINK_TOWER_TYPE.get();
	}
	
	private static void generatePieces(StructurePiecesBuilder builder, GenerationContext context)
	{
		RandomSource random = context.random();
		StructureTemplateManager templateManager = context.structureTemplateManager();
		ResourceLocation templateId = Minestuck.id("pink_tower");
		
		// todo uses fixed positions as a workaround to the "button on chunk border" issue.
		int x = context.chunkPos().getBlockX(1), z = context.chunkPos().getBlockZ(1);
		var placement = TemplatePlacement.centeredWithRandomRotation(templateManager.getOrCreate(templateId), new BlockPos(x, 0, z), random);
		
		int y = context.chunkGenerator().getBaseHeight(x, z, Heightmap.Types.WORLD_SURFACE_WG, context.heightAccessor(), context.randomState());
		
		builder.addPiece(new PinkTowerPiece(templateManager, templateId, placement.zeroPos().atY(y - 3), placement.rotation()));
	}
}
