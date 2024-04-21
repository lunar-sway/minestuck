package com.mraof.minestuck.world.gen.structure;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
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
import net.minecraft.util.random.Weight;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.util.random.WeightedRandom;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.ChunkGeneratorStructureState;
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
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static com.mraof.minestuck.world.gen.structure.MSStructureTypes.asType;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class ProspitStructure
{
	public static final int PIECE_SIZE = 8;
	public static final int WIDTH_IN_PIECES = 16, HEIGHT_IN_PIECES = 16;
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
		
		private void generatePieces(StructurePiecesBuilder builder, GenerationContext context)
		{
			List<PieceEntry> pieces = List.of(
					new PieceEntry(pos -> null, ConnectionType.AIR, ConnectionType.AIR, Weight.of(10)),
					new PieceEntry(SolidPiece::new, ConnectionType.TOP_SOLID, ConnectionType.SOLID, Weight.of(10))
			);
			
			BlockPos cornerPos = context.chunkPos().getWorldPosition().offset(-(WIDTH_IN_CHUNKS * 8), 0, -(WIDTH_IN_CHUNKS * 8));
			
			Map<PiecePos, List<PieceEntry>> availablePiecesMap = new HashMap<>();
			List<PiecePos> piecesToGenerate = new ArrayList<>();
			
			for(int xIndex = 0; xIndex < WIDTH_IN_PIECES; xIndex++)
			{
				for(int zIndex = 0; zIndex < WIDTH_IN_PIECES; zIndex++)
				{
					for(int yIndex = 0; yIndex < HEIGHT_IN_PIECES; yIndex++)
					{
						PiecePos pos = new PiecePos(xIndex, yIndex, zIndex);
						availablePiecesMap.put(pos, new ArrayList<>(pieces));
						piecesToGenerate.add(pos);
					}
				}
			}
			
			while(!piecesToGenerate.isEmpty())
			{
				PiecePos pos = piecesToGenerate.remove(context.random().nextInt(piecesToGenerate.size()));
				
				List<PieceEntry> availablePieces = availablePiecesMap.get(pos);
				var chosenEntry = WeightedRandom.getRandomItem(context.random(), availablePieces);
				if(chosenEntry.isEmpty())
					continue;
				
				BlockPos piecePos = pos.toBlockPos(cornerPos);
				StructurePiece piece = chosenEntry.get().constructor().apply(piecePos);
				if(piece != null)
					builder.addPiece(piece);
				
				if(availablePieces.removeIf(entry -> entry != chosenEntry.get()))
					removeConflictingPieces(pos, availablePiecesMap);
			}
		}
	}
	
	private static void removeConflictingPieces(PiecePos pos, Map<PiecePos, List<PieceEntry>> availablePiecesMap)
	{
		if(pos.y > 0)
		{
			Set<ConnectionType> connections = availablePiecesMap.get(pos).stream().map(PieceEntry::belowConnection).collect(Collectors.toSet());
			PiecePos belowPos = new PiecePos(pos.x, pos.y - 1, pos.z);
			if(availablePiecesMap.get(belowPos).removeIf(belowEntry -> cannotConnect(belowEntry.aboveConnection(), connections)))
				removeConflictingPieces(belowPos, availablePiecesMap);
		}
		
		if(pos.y < HEIGHT_IN_PIECES - 1)
		{
			Set<ConnectionType> connections = availablePiecesMap.get(pos).stream().map(PieceEntry::aboveConnection).collect(Collectors.toSet());
			PiecePos abovePos = new PiecePos(pos.x, pos.y + 1, pos.z);
			if(availablePiecesMap.get(abovePos).removeIf(aboveEntry -> cannotConnect(aboveEntry.belowConnection(), connections)))
				removeConflictingPieces(abovePos, availablePiecesMap);
		}
	}
	
	private static boolean cannotConnect(ConnectionType type, Set<ConnectionType> otherTypes)
	{
		Set<ConnectionType> supportedTypes = SUPPORTED_CONNECTIONS.get(type);
		return otherTypes.stream().noneMatch(supportedTypes::contains);
	}
	
	private record PiecePos(int x, int y, int z)
	{
		public BlockPos toBlockPos(BlockPos cornerPos)
		{
			return cornerPos.offset(this.x * PIECE_SIZE, this.y * PIECE_SIZE, this.z * PIECE_SIZE);
		}
	}
	
	private record PieceEntry(Function<BlockPos, StructurePiece> constructor, ConnectionType aboveConnection, ConnectionType belowConnection, Weight weight) implements WeightedEntry
	{
		@Override
		public Weight getWeight()
		{
			return weight;
		}
	}
	
	private enum ConnectionType
	{
		AIR,
		TOP_SOLID,
		SOLID,
	}
	
	private static final Map<ConnectionType, Set<ConnectionType>> SUPPORTED_CONNECTIONS = Util.make(() -> {
		List<Pair<ConnectionType, ConnectionType>> allowedConnections = List.of(
				Pair.of(ConnectionType.AIR, ConnectionType.AIR),
				Pair.of(ConnectionType.AIR, ConnectionType.TOP_SOLID),
				Pair.of(ConnectionType.TOP_SOLID, ConnectionType.SOLID)
		);
		
		ImmutableMap.Builder<ConnectionType, Set<ConnectionType>> mapBuilder = ImmutableMap.builder();
		for(ConnectionType type : ConnectionType.values())
		{
			ImmutableSet.Builder<ConnectionType> setBuilder = ImmutableSet.builder();
			for(Pair<ConnectionType, ConnectionType> pair : allowedConnections)
			{
				if(pair.getFirst() == type)
					setBuilder.add(pair.getSecond());
				if(pair.getSecond() == type)
					setBuilder.add(pair.getFirst());
			}
			mapBuilder.put(type, setBuilder.build());
		}
		return mapBuilder.build();
	});
	
	public static final Supplier<StructurePieceType.ContextlessType> SOLID_PIECE_TYPE = MSStructurePieces.REGISTER.register("prospit_solid_piece",
			() -> SolidPiece::new);
	
	public static final class SolidPiece extends ImprovedStructurePiece
	{
		public SolidPiece(BlockPos bottomCornerPos)
		{
			super(SOLID_PIECE_TYPE.get(), 0, BoundingBox.fromCorners(bottomCornerPos,
					bottomCornerPos.offset(PIECE_SIZE - 1, PIECE_SIZE - 1, PIECE_SIZE - 1)));
			setOrientation(Direction.NORTH);
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
}
