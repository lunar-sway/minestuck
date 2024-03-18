package com.mraof.minestuck.world.gen.structure;

import com.mojang.serialization.Codec;
import com.mraof.minestuck.Minestuck;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;

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
		return MSStructureTypes.LARGE_WOOD_OBJECT.get();
	}
	
	private static void generatePieces(StructurePiecesBuilder builder, GenerationContext context)
	{
		RandomSource random = context.random();
		int x = context.chunkPos().getBlockX(random.nextInt(16)), z = context.chunkPos().getBlockZ(random.nextInt(16));
		int y = context.chunkGenerator().getBaseHeight(x, z, Heightmap.Types.OCEAN_FLOOR_WG, context.heightAccessor(), context.randomState());
		BlockPos blockpos = new BlockPos(x, y, z);
		SimpleTemplatePiece piece = new SimpleTemplatePiece(context.structureTemplateManager(), Util.getRandom(TEMPLATES, random), blockpos, Rotation.getRandom(random));
		builder.addPiece(piece);
	}
}