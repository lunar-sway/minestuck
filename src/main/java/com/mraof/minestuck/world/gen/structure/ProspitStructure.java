package com.mraof.minestuck.world.gen.structure;

import com.mojang.serialization.Codec;
import com.mraof.minestuck.Minestuck;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.ChunkGeneratorStructureState;
import net.minecraft.world.level.levelgen.PositionalRandomFactory;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacementType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;
import java.util.function.Supplier;

import static com.mraof.minestuck.world.gen.structure.MSStructureTypes.asType;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class ProspitStructure
{
	public static final int PIECE_SIZE = 8;
	public static final int WIDTH_IN_PIECES = 16, HEIGHT_IN_PIECES = 16;
	public static final WFC.Dimensions WFC_DIMENSIONS = new WFC.Dimensions(WIDTH_IN_PIECES, HEIGHT_IN_PIECES, WIDTH_IN_PIECES);
	public static final int WIDTH_IN_CHUNKS = (PIECE_SIZE * WIDTH_IN_PIECES) / 16;
	
	public static void init()
	{
	}
	
	public static final ResourceKey<Structure> STRUCTURE = ResourceKey.create(Registries.STRUCTURE, Minestuck.id("prospit_terrain"));
	
	public static final Supplier<StructurePlacementType<FixedPlacement>> FIXED_PLACEMENT_TYPE = MSStructurePlacements.REGISTER.register("fixed",
			() -> () -> FixedPlacement.CODEC);
	
	public static final class FixedPlacement extends StructurePlacement
	{
		public static final Codec<FixedPlacement> CODEC = Codec.unit(FixedPlacement::new);
		
		public FixedPlacement()
		{
			super(Vec3i.ZERO, FrequencyReductionMethod.DEFAULT, 1, 0, Optional.empty());
		}
		
		@Override
		public StructurePlacementType<FixedPlacement> type()
		{
			return FIXED_PLACEMENT_TYPE.get();
		}
		
		@Override
		protected boolean isPlacementChunk(ChunkGeneratorStructureState structureState, int x, int z)
		{
			return Math.floorMod(x, WIDTH_IN_CHUNKS) == 0 && Math.floorMod(z, WIDTH_IN_CHUNKS) == 0;
		}
	}
	
	public static final Supplier<StructureType<TerrainStructure>> STRUCTURE_TYPE = MSStructureTypes.REGISTER.register("prospit_terrain",
			() -> asType(TerrainStructure.CODEC));
	
	public static final class TerrainStructure extends Structure
	{
		public static final Codec<TerrainStructure> CODEC = simpleCodec(TerrainStructure::new);
		
		public TerrainStructure(StructureSettings settings)
		{
			super(settings);
		}
		
		@Override
		public StructureType<TerrainStructure> type()
		{
			return STRUCTURE_TYPE.get();
		}
		
		@Override
		protected Optional<GenerationStub> findGenerationPoint(GenerationContext context)
		{
			return Optional.of(new GenerationStub(context.chunkPos().getWorldPosition(), builder -> generatePieces(builder, context)));
		}
		
		private void generatePieces(StructurePiecesBuilder piecesBuilder, GenerationContext context)
		{
			StructureTemplateManager templateManager = context.structureTemplateManager();
			PositionalRandomFactory randomFactory = RandomSource.create(context.seed()).forkPositional().fromHashOf(Minestuck.id("prospit")).forkPositional();
			
			BlockPos cornerPos = context.chunkPos().getWorldPosition().offset(-(WIDTH_IN_CHUNKS * 8), 0, -(WIDTH_IN_CHUNKS * 8));
			
			WFC.Template borderTemplate = new WFC.Template(ProspitStructure.WFC_DIMENSIONS, ProspitStructure.BORDER_ENTRIES);
			WFC.Template centerTemplate = new WFC.Template(ProspitStructure.WFC_DIMENSIONS, ProspitStructure.CENTER_ENTRIES);
			borderTemplate.setupFixedEdgeBounds(Direction.UP, Set.of(WFCData.ConnectorType.AIR));
			centerTemplate.setupFixedEdgeBounds(Direction.UP, Set.of(WFCData.ConnectorType.AIR));
			
			BlockPos northWestPos = cornerPos;
			WFC.Generator northWestGenerator = borderTemplate.cornerGenerator();
			northWestGenerator.collapse(randomFactory.at(northWestPos), (piecePos, entry) -> {
				StructurePiece piece = entry.constructor().apply(templateManager, piecePos.toBlockPos(northWestPos, PIECE_SIZE, PIECE_SIZE));
				if(piece != null)
					piecesBuilder.addPiece(piece);
			});
			
			BlockPos northEast = cornerPos.offset(WIDTH_IN_PIECES * PIECE_SIZE, 0, 0);
			WFC.Generator northEastGenerator = borderTemplate.cornerGenerator();
			northEastGenerator.collapse(randomFactory.at(northEast), (piecePos, pieceConstructor) -> {});
			
			BlockPos southWest = cornerPos.offset(0, 0, WIDTH_IN_PIECES * PIECE_SIZE);
			WFC.Generator southWestGenerator = borderTemplate.cornerGenerator();
			southWestGenerator.collapse(randomFactory.at(southWest), (piecePos, pieceConstructor) -> {});
			
			BlockPos southEastPos = cornerPos.offset(WIDTH_IN_PIECES * PIECE_SIZE, 0, WIDTH_IN_PIECES * PIECE_SIZE);
			WFC.Generator southEastGenerator = borderTemplate.cornerGenerator();
			southEastGenerator.collapse(randomFactory.at(southEastPos), (piecePos, pieceConstructor) -> {});
			
			
			BlockPos northPos = cornerPos.offset(PIECE_SIZE, 0, 0);
			WFC.Generator northGenerator = borderTemplate.zEdgeGenerator(northWestGenerator, northEastGenerator);
			northGenerator.collapse(randomFactory.at(northPos), (piecePos, entry) -> {
				StructurePiece piece = entry.constructor().apply(templateManager, piecePos.toBlockPos(northPos, PIECE_SIZE, PIECE_SIZE));
				if(piece != null)
					piecesBuilder.addPiece(piece);
			});
			
			BlockPos westPos = cornerPos.offset(0, 0, PIECE_SIZE);
			WFC.Generator westGenerator = borderTemplate.xEdgeGenerator(northWestGenerator, southWestGenerator);
			westGenerator.collapse(randomFactory.at(westPos), (piecePos, entry) -> {
				StructurePiece piece = entry.constructor().apply(templateManager, piecePos.toBlockPos(westPos, PIECE_SIZE, PIECE_SIZE));
				if(piece != null)
					piecesBuilder.addPiece(piece);
			});
			
			BlockPos southPos = cornerPos.offset(PIECE_SIZE, 0, WIDTH_IN_PIECES * PIECE_SIZE);
			WFC.Generator southGenerator = borderTemplate.zEdgeGenerator(southWestGenerator, southEastGenerator);
			southGenerator.collapse(randomFactory.at(southPos), (piecePos, pieceConstructor) -> {});
			
			BlockPos eastPos = cornerPos.offset(WIDTH_IN_PIECES * PIECE_SIZE, 0, PIECE_SIZE);
			WFC.Generator eastGenerator = borderTemplate.xEdgeGenerator(northEastGenerator, southEastGenerator);
			eastGenerator.collapse(randomFactory.at(eastPos), (piecePos, pieceConstructor) -> {});
			
			
			BlockPos centerPos = cornerPos.offset(PIECE_SIZE, 0, PIECE_SIZE);
			WFC.Generator centerGenerator = centerTemplate.centerGenerator(northGenerator, westGenerator, southGenerator, eastGenerator);
			centerGenerator.collapse(randomFactory.at(centerPos), (piecePos, entry) -> {
				StructurePiece piece = entry.constructor().apply(templateManager, piecePos.toBlockPos(centerPos, PIECE_SIZE, PIECE_SIZE));
				if(piece != null)
					piecesBuilder.addPiece(piece);
			});
		}
	}
	
	public static final WFCData.PieceEntry SOLID = WFCData.symmetricTemplate(Minestuck.id("prospit/solid"),
			WFCData.ConnectorType.SOLID, WFCData.ConnectorType.SOLID, WFCData.ConnectorType.WALL);
	public static final WFCData.PieceEntry PYRAMID_ROOF = WFCData.symmetricTemplate(Minestuck.id("prospit/pyramid_roof"),
			WFCData.ConnectorType.SOLID, WFCData.ConnectorType.AIR, WFCData.ConnectorType.ROOF_SIDE);
	public static final WFCData.MultiPieceEntry SPIKE = WFCData.symmetricPillarPieces(SpikePiece::new, "spike", 2,
			WFCData.ConnectorType.SOLID, WFCData.ConnectorType.AIR, List.of(WFCData.ConnectorType.ROOF_SIDE, WFCData.ConnectorType.AIR));
	public static final Collection<WFCData.PieceEntry> BRIDGE = WFCData.axisSymmetricTemplate(Minestuck.id("prospit/bridge"),
			WFCData.ConnectorType.AIR, WFCData.ConnectorType.AIR, WFCData.ConnectorType.BRIDGE, WFCData.ConnectorType.AIR);
	public static final Collection<WFCData.PieceEntry> LEDGE = WFCData.rotatablePiece(LedgePiece::new, Map.of(
			Direction.DOWN, WFCData.ConnectorType.SOLID,
			Direction.UP, WFCData.ConnectorType.AIR,
			Direction.NORTH, WFCData.ConnectorType.LEDGE_FRONT,
			Direction.EAST, WFCData.ConnectorType.LEDGE_RIGHT,
			Direction.SOUTH, WFCData.ConnectorType.LEDGE_BACK,
			Direction.WEST, WFCData.ConnectorType.LEDGE_LEFT
	));
	public static final Collection<WFCData.PieceEntry> LEDGE_CORNER = WFCData.rotatablePiece(LedgeCornerPiece::new, Map.of(
			Direction.DOWN, WFCData.ConnectorType.SOLID,
			Direction.UP, WFCData.ConnectorType.AIR,
			Direction.NORTH, WFCData.ConnectorType.LEDGE_FRONT,
			Direction.EAST, WFCData.ConnectorType.LEDGE_RIGHT,
			Direction.SOUTH, WFCData.ConnectorType.LEDGE_LEFT,
			Direction.WEST, WFCData.ConnectorType.LEDGE_FRONT
	));
	
	private static final WFCData.EntriesData CENTER_ENTRIES = Util.make(() -> {
		WFCData.EntriesBuilder builder = new WFCData.EntriesBuilder();
		
		WFCData.ConnectorType.addCoreConnections(builder.connections());
		
		builder.add(WFCData.PieceEntry.EMPTY, 30);
		builder.add(SOLID, 10);
		builder.add(PYRAMID_ROOF, 2);
		builder.add(SPIKE, 1);
		builder.add(BRIDGE, 3);
		builder.add(LEDGE, 2);
		builder.add(LEDGE_CORNER, 2);
		
		return builder.build();
	});
	private static final WFCData.EntriesData BORDER_ENTRIES = Util.make(() -> {
		WFCData.EntriesBuilder builder = new WFCData.EntriesBuilder();
		
		WFCData.ConnectorType.addCoreConnections(builder.connections());
		
		builder.add(WFCData.PieceEntry.EMPTY, 10);
		builder.add(SOLID, 10);
		builder.add(PYRAMID_ROOF, 3);
		builder.add(SPIKE, 1);
		
		return builder.build();
	});
	
	
	public static final Supplier<StructurePieceType.ContextlessType> SPIKE_PIECE_TYPE = MSStructurePieces.REGISTER.register("prospit_spike",
			() -> SpikePiece::new);
	public static final Supplier<StructurePieceType.ContextlessType> LEDGE_PIECE_TYPE = MSStructurePieces.REGISTER.register("prospit_ledge",
			() -> LedgePiece::new);
	public static final Supplier<StructurePieceType.ContextlessType> LEDGE_CORNER_PIECE_TYPE = MSStructurePieces.REGISTER.register("prospit_ledge_corner",
			() -> LedgeCornerPiece::new);
	
	public static final class SpikePiece extends ImprovedStructurePiece
	{
		public SpikePiece(BlockPos bottomCornerPos)
		{
			super(SPIKE_PIECE_TYPE.get(), 0, BoundingBox.fromCorners(bottomCornerPos,
					bottomCornerPos.offset(PIECE_SIZE - 1, 2 * PIECE_SIZE - 1, PIECE_SIZE - 1)));
			setOrientation(Direction.SOUTH);
		}
		
		public SpikePiece(CompoundTag tag)
		{
			super(SPIKE_PIECE_TYPE.get(), tag);
		}
		
		@Override
		protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag)
		{
		}
		
		@Override
		public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox box, ChunkPos chunkPos, BlockPos pos)
		{
			generateBox(level, box, 0, 0, 0, 7, 1, 7,
					Blocks.GOLD_BLOCK.defaultBlockState(), Blocks.GOLD_BLOCK.defaultBlockState(), false);
			generateBox(level, box, 1, 2, 1, 6, 4, 6,
					Blocks.GOLD_BLOCK.defaultBlockState(), Blocks.GOLD_BLOCK.defaultBlockState(), false);
			generateBox(level, box, 2, 5, 2, 5, 8, 5,
					Blocks.GOLD_BLOCK.defaultBlockState(), Blocks.GOLD_BLOCK.defaultBlockState(), false);
			generateBox(level, box, 3, 9, 3, 4, 13, 4,
					Blocks.GOLD_BLOCK.defaultBlockState(), Blocks.GOLD_BLOCK.defaultBlockState(), false);
		}
	}
	
	public static final class LedgePiece extends ImprovedStructurePiece
	{
		public LedgePiece(BlockPos bottomCornerPos, Rotation rotation)
		{
			super(LEDGE_PIECE_TYPE.get(), 0, BoundingBox.fromCorners(bottomCornerPos,
					bottomCornerPos.offset(PIECE_SIZE - 1, PIECE_SIZE - 1, PIECE_SIZE - 1)));
			setOrientation(rotation.rotate(Direction.SOUTH));
		}
		
		public LedgePiece(CompoundTag tag)
		{
			super(LEDGE_PIECE_TYPE.get(), tag);
		}
		
		@Override
		protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag)
		{
		}
		
		@Override
		public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox box, ChunkPos chunkPos, BlockPos pos)
		{
			generateBox(level, box, 0, 0, 0, 7, 0, 7,
					Blocks.GOLD_BLOCK.defaultBlockState(), Blocks.GOLD_BLOCK.defaultBlockState(), false);
			placeBlock(level, Blocks.GOLD_BLOCK.defaultBlockState(), 0, 1, 0, box);
			placeBlock(level, Blocks.GOLD_BLOCK.defaultBlockState(), 3, 1, 0, box);
			placeBlock(level, Blocks.GOLD_BLOCK.defaultBlockState(), 4, 1, 0, box);
			placeBlock(level, Blocks.GOLD_BLOCK.defaultBlockState(), 7, 1, 0, box);
		}
	}
	
	public static final class LedgeCornerPiece extends ImprovedStructurePiece
	{
		public LedgeCornerPiece(BlockPos bottomCornerPos, Rotation rotation)
		{
			super(LEDGE_CORNER_PIECE_TYPE.get(), 0, BoundingBox.fromCorners(bottomCornerPos,
					bottomCornerPos.offset(PIECE_SIZE - 1, PIECE_SIZE - 1, PIECE_SIZE - 1)));
			setOrientation(rotation.rotate(Direction.SOUTH));
		}
		
		public LedgeCornerPiece(CompoundTag tag)
		{
			super(LEDGE_CORNER_PIECE_TYPE.get(), tag);
		}
		
		@Override
		protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag)
		{
		}
		
		@Override
		public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox box, ChunkPos chunkPos, BlockPos pos)
		{
			generateBox(level, box, 0, 0, 0, 7, 0, 7,
					Blocks.GOLD_BLOCK.defaultBlockState(), Blocks.GOLD_BLOCK.defaultBlockState(), false);
			placeBlock(level, Blocks.GOLD_BLOCK.defaultBlockState(), 0, 1, 7, box);
			placeBlock(level, Blocks.GOLD_BLOCK.defaultBlockState(), 0, 1, 4, box);
			placeBlock(level, Blocks.GOLD_BLOCK.defaultBlockState(), 0, 1, 3, box);
			placeBlock(level, Blocks.GOLD_BLOCK.defaultBlockState(), 0, 1, 0, box);
			placeBlock(level, Blocks.GOLD_BLOCK.defaultBlockState(), 0, 2, 0, box);
			placeBlock(level, Blocks.GOLD_BLOCK.defaultBlockState(), 3, 1, 0, box);
			placeBlock(level, Blocks.GOLD_BLOCK.defaultBlockState(), 4, 1, 0, box);
			placeBlock(level, Blocks.GOLD_BLOCK.defaultBlockState(), 7, 1, 0, box);
		}
	}
}
