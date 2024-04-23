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

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
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
			PositionalRandomFactory randomFactory = RandomSource.create(context.seed()).forkPositional().fromHashOf(Minestuck.id("prospit")).forkPositional();
			
			BlockPos cornerPos = context.chunkPos().getWorldPosition().offset(-(WIDTH_IN_CHUNKS * 8), 0, -(WIDTH_IN_CHUNKS * 8));
			
			WFC.Template template = new WFC.Template(ProspitStructure.WFC_DIMENSIONS, ProspitStructure.ENTRIES_DATA);
			template.setupFixedEdgeBounds(Direction.UP, Set.of(WFCData.ConnectorType.AIR));
			
			BlockPos northWestPos = cornerPos;
			WFC.Generator northWestGenerator = template.cornerGenerator();
			northWestGenerator.collapse(randomFactory.at(northWestPos), (piecePos, pieceConstructor) -> {
				StructurePiece piece = pieceConstructor.apply(piecePos.toBlockPos(northWestPos, PIECE_SIZE, PIECE_SIZE));
				if(piece != null)
					piecesBuilder.addPiece(piece);
			});
			
			BlockPos northEast = cornerPos.offset(WIDTH_IN_PIECES * PIECE_SIZE, 0, 0);
			WFC.Generator northEastGenerator = template.cornerGenerator();
			northEastGenerator.collapse(randomFactory.at(northEast), (piecePos, pieceConstructor) -> {});
			
			BlockPos southWest = cornerPos.offset(0, 0, WIDTH_IN_PIECES * PIECE_SIZE);
			WFC.Generator southWestGenerator = template.cornerGenerator();
			southWestGenerator.collapse(randomFactory.at(southWest), (piecePos, pieceConstructor) -> {});
			
			BlockPos southEastPos = cornerPos.offset(WIDTH_IN_PIECES * PIECE_SIZE, 0, WIDTH_IN_PIECES * PIECE_SIZE);
			WFC.Generator southEastGenerator = template.cornerGenerator();
			southEastGenerator.collapse(randomFactory.at(southEastPos), (piecePos, pieceConstructor) -> {});
			
			
			BlockPos northPos = cornerPos.offset(PIECE_SIZE, 0, 0);
			WFC.Generator northGenerator = template.zEdgeGenerator();
			northGenerator.setupEdgeBounds(Direction.WEST, northWestGenerator);
			northGenerator.setupEdgeBounds(Direction.EAST, northEastGenerator);
			northGenerator.collapse(randomFactory.at(northPos), (piecePos, pieceConstructor) -> {
				StructurePiece piece = pieceConstructor.apply(piecePos.toBlockPos(northPos, PIECE_SIZE, PIECE_SIZE));
				if(piece != null)
					piecesBuilder.addPiece(piece);
			});
			
			BlockPos westPos = cornerPos.offset(0, 0, PIECE_SIZE);
			WFC.Generator westGenerator = template.xEdgeGenerator();
			westGenerator.setupEdgeBounds(Direction.NORTH, northWestGenerator);
			westGenerator.setupEdgeBounds(Direction.SOUTH, southWestGenerator);
			westGenerator.collapse(randomFactory.at(westPos), (piecePos, pieceConstructor) -> {
				StructurePiece piece = pieceConstructor.apply(piecePos.toBlockPos(westPos, PIECE_SIZE, PIECE_SIZE));
				if(piece != null)
					piecesBuilder.addPiece(piece);
			});
			
			BlockPos southPos = cornerPos.offset(PIECE_SIZE, 0, WIDTH_IN_PIECES * PIECE_SIZE);
			WFC.Generator southGenerator = template.zEdgeGenerator();
			southGenerator.setupEdgeBounds(Direction.WEST, southWestGenerator);
			southGenerator.setupEdgeBounds(Direction.EAST, southEastGenerator);
			southGenerator.collapse(randomFactory.at(southPos), (piecePos, pieceConstructor) -> {});
			
			BlockPos eastPos = cornerPos.offset(WIDTH_IN_PIECES * PIECE_SIZE, 0, PIECE_SIZE);
			WFC.Generator eastGenerator = template.xEdgeGenerator();
			eastGenerator.setupEdgeBounds(Direction.NORTH, northEastGenerator);
			eastGenerator.setupEdgeBounds(Direction.SOUTH, southEastGenerator);
			eastGenerator.collapse(randomFactory.at(eastPos), (piecePos, pieceConstructor) -> {});
			
			
			BlockPos centerPos = cornerPos.offset(PIECE_SIZE, 0, PIECE_SIZE);
			WFC.Generator centerGenerator = template.centerGenerator();
			centerGenerator.setupEdgeBounds(Direction.NORTH, northGenerator);
			centerGenerator.setupEdgeBounds(Direction.WEST, westGenerator);
			centerGenerator.setupEdgeBounds(Direction.SOUTH, southGenerator);
			centerGenerator.setupEdgeBounds(Direction.EAST, eastGenerator);
			centerGenerator.collapse(randomFactory.at(centerPos), (piecePos, pieceConstructor) -> {
				StructurePiece piece = pieceConstructor.apply(piecePos.toBlockPos(centerPos, PIECE_SIZE, PIECE_SIZE));
				if(piece != null)
					piecesBuilder.addPiece(piece);
			});
		}
	}
	
	private static final WFCData.EntriesData ENTRIES_DATA = Util.make(() -> {
		WFCData.EntriesBuilder builder = new WFCData.EntriesBuilder();
		
		WFCData.ConnectorType.addCoreConnections(builder);
		
		builder.addSymmetric(pos -> null, WFCData.ConnectorType.AIR, WFCData.ConnectorType.AIR, WFCData.ConnectorType.AIR, 10);
		builder.addSymmetric(SolidPiece::new, WFCData.ConnectorType.SOLID, WFCData.ConnectorType.SOLID, WFCData.ConnectorType.WALL, 10);
		builder.addSymmetric(PyramidPiece::new, WFCData.ConnectorType.SOLID, WFCData.ConnectorType.AIR, WFCData.ConnectorType.ROOF_SIDE, 2);
		builder.addAxisSymmetric(BridgePiece::new, WFCData.ConnectorType.AIR, WFCData.ConnectorType.AIR, WFCData.ConnectorType.BRIDGE, WFCData.ConnectorType.AIR, 4);
		builder.addRotating(LedgePiece::new, Map.of(
				Direction.DOWN, WFCData.ConnectorType.SOLID,
				Direction.UP, WFCData.ConnectorType.AIR,
				Direction.NORTH, WFCData.ConnectorType.LEDGE_FRONT,
				Direction.EAST, WFCData.ConnectorType.LEDGE_RIGHT,
				Direction.SOUTH, WFCData.ConnectorType.LEDGE_BACK,
				Direction.WEST, WFCData.ConnectorType.LEDGE_LEFT
		), 3);
		builder.addRotating(LedgeCornerPiece::new, Map.of(
				Direction.DOWN, WFCData.ConnectorType.SOLID,
				Direction.UP, WFCData.ConnectorType.AIR,
				Direction.NORTH, WFCData.ConnectorType.LEDGE_FRONT,
				Direction.EAST, WFCData.ConnectorType.LEDGE_RIGHT,
				Direction.SOUTH, WFCData.ConnectorType.LEDGE_LEFT,
				Direction.WEST, WFCData.ConnectorType.LEDGE_FRONT
		), 3);
		
		return builder.build();
	});
	
	
	public static final Supplier<StructurePieceType.ContextlessType> SOLID_PIECE_TYPE = MSStructurePieces.REGISTER.register("prospit_solid",
			() -> SolidPiece::new);
	public static final Supplier<StructurePieceType.ContextlessType> PYRAMID_PIECE_TYPE = MSStructurePieces.REGISTER.register("prospit_pyramid",
			() -> PyramidPiece::new);
	public static final Supplier<StructurePieceType.ContextlessType> BRIDGE_PIECE_TYPE = MSStructurePieces.REGISTER.register("prospit_bridge",
			() -> BridgePiece::new);
	public static final Supplier<StructurePieceType.ContextlessType> LEDGE_PIECE_TYPE = MSStructurePieces.REGISTER.register("prospit_ledge",
			() -> LedgePiece::new);
	public static final Supplier<StructurePieceType.ContextlessType> LEDGE_CORNER_PIECE_TYPE = MSStructurePieces.REGISTER.register("prospit_ledge_corner",
			() -> LedgeCornerPiece::new);
	
	public static final class SolidPiece extends ImprovedStructurePiece
	{
		public SolidPiece(BlockPos bottomCornerPos)
		{
			super(SOLID_PIECE_TYPE.get(), 0, BoundingBox.fromCorners(bottomCornerPos,
					bottomCornerPos.offset(PIECE_SIZE - 1, PIECE_SIZE - 1, PIECE_SIZE - 1)));
			setOrientation(Direction.SOUTH);
		}
		
		public SolidPiece(CompoundTag tag)
		{
			super(SOLID_PIECE_TYPE.get(), tag);
		}
		
		@Override
		protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag)
		{
		}
		
		@Override
		public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox box, ChunkPos chunkPos, BlockPos pos)
		{
			generateBox(level, box, 0, 0, 0, PIECE_SIZE - 1, PIECE_SIZE - 1, PIECE_SIZE - 1,
					Blocks.GOLD_BLOCK.defaultBlockState(), Blocks.GOLD_BLOCK.defaultBlockState(), false);
		}
	}
	
	public static final class PyramidPiece extends ImprovedStructurePiece
	{
		public PyramidPiece(BlockPos bottomCornerPos)
		{
			super(PYRAMID_PIECE_TYPE.get(), 0, BoundingBox.fromCorners(bottomCornerPos,
					bottomCornerPos.offset(PIECE_SIZE - 1, PIECE_SIZE - 1, PIECE_SIZE - 1)));
			setOrientation(Direction.SOUTH);
		}
		
		public PyramidPiece(CompoundTag tag)
		{
			super(PYRAMID_PIECE_TYPE.get(), tag);
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
			generateBox(level, box, 1, 1, 1, 6, 1, 6,
					Blocks.GOLD_BLOCK.defaultBlockState(), Blocks.GOLD_BLOCK.defaultBlockState(), false);
			generateBox(level, box, 2, 2, 2, 5, 2, 5,
					Blocks.GOLD_BLOCK.defaultBlockState(), Blocks.GOLD_BLOCK.defaultBlockState(), false);
			generateBox(level, box, 3, 3, 3, 4, 3, 4,
					Blocks.GOLD_BLOCK.defaultBlockState(), Blocks.GOLD_BLOCK.defaultBlockState(), false);
		}
	}
	
	public static final class BridgePiece extends ImprovedStructurePiece
	{
		public BridgePiece(BlockPos bottomCornerPos, Direction.Axis axis)
		{
			super(BRIDGE_PIECE_TYPE.get(), 0, BoundingBox.fromCorners(bottomCornerPos,
					bottomCornerPos.offset(PIECE_SIZE - 1, PIECE_SIZE - 1, PIECE_SIZE - 1)));
			setOrientation(axis == Direction.Axis.X ? Direction.EAST : Direction.SOUTH);
		}
		
		public BridgePiece(CompoundTag tag)
		{
			super(BRIDGE_PIECE_TYPE.get(), tag);
		}
		
		@Override
		protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag)
		{
		}
		
		@Override
		public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox box, ChunkPos chunkPos, BlockPos pos)
		{
			generateBox(level, box, 2, 1, 0, 5, 1, 7,
					Blocks.GOLD_BLOCK.defaultBlockState(), Blocks.GOLD_BLOCK.defaultBlockState(), false);
			generateBox(level, box, 3, 0, 0, 4, 0, 7,
					Blocks.GOLD_BLOCK.defaultBlockState(), Blocks.GOLD_BLOCK.defaultBlockState(), false);
			generateBox(level, box, 2, 2, 0, 2, 2, 7,
					Blocks.GOLD_BLOCK.defaultBlockState(), Blocks.GOLD_BLOCK.defaultBlockState(), false);
			generateBox(level, box, 5, 2, 0, 5, 2, 7,
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
