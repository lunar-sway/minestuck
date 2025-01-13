package com.mraof.minestuck.world.gen.structure.wfc;

import com.mojang.serialization.MapCodec;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.world.gen.structure.MSStructures;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.chunk.ChunkGeneratorStructureState;
import net.minecraft.world.level.levelgen.PositionalRandomFactory;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacementType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import static com.mraof.minestuck.world.gen.structure.MSStructures.asType;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class ProspitStructure
{
	public static final WFCUtil.CellSize PIECE_SIZE = new WFCUtil.CellSize(8, 8);
	public static final int WIDTH_IN_PIECES = 16, HEIGHT_IN_PIECES = 14;
	public static final WFCUtil.Dimensions WFC_DIMENSIONS = new WFCUtil.Dimensions(WIDTH_IN_PIECES, HEIGHT_IN_PIECES, WIDTH_IN_PIECES);
	public static final int WIDTH_IN_CHUNKS = (PIECE_SIZE.width() * WIDTH_IN_PIECES) / 16;
	public static final int BOTTOM_Y = 1;
	
	public static void init()
	{
	}
	
	public static final ResourceKey<Structure> STRUCTURE = ResourceKey.create(Registries.STRUCTURE, Minestuck.id("prospit_terrain"));
	
	public static final Supplier<StructurePlacementType<FixedPlacement>> FIXED_PLACEMENT_TYPE = MSStructures.PLACEMENT_REGISTER.register("fixed",
			() -> () -> FixedPlacement.CODEC);
	
	public static final class FixedPlacement extends StructurePlacement
	{
		public static final MapCodec<FixedPlacement> CODEC = MapCodec.unit(FixedPlacement::new);
		
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
	
	public static final Supplier<StructureType<TerrainStructure>> STRUCTURE_TYPE = MSStructures.TYPE_REGISTER.register("prospit_terrain",
			() -> asType(TerrainStructure.CODEC));
	
	public static final class TerrainStructure extends Structure
	{
		public static final MapCodec<TerrainStructure> CODEC = simpleCodec(TerrainStructure::new);
		
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
			
			WFCUtil.PositionTransform middleTransform = new WFCUtil.PositionTransform(context.chunkPos().getMiddleBlockPosition(BOTTOM_Y), PIECE_SIZE);
			
			Holder<WFCData.ConnectionSet> connectionSet = context.registryAccess().lookupOrThrow(WFCData.ConnectionSet.REGISTRY_KEY).getOrThrow(PROSPIT_CONNECTIONS);
			WFCData.EntryPalette centerPalette = context.random().nextBoolean() ? buildCenterPalette(connectionSet, templateManager) : buildOpenZonePalette(connectionSet, templateManager);
			WFCData.EntryPalette borderPalette = buildBorderPalette(connectionSet, templateManager);
			
			WFC.InfiniteModularGeneration.generateModule(middleTransform, ProspitStructure.WFC_DIMENSIONS,
					centerPalette, borderPalette, randomFactory, piecesBuilder, null);
		}
	}
	
	public static final ResourceKey<WFCData.ConnectionSet> PROSPIT_CONNECTIONS = ResourceKey.create(WFCData.ConnectionSet.REGISTRY_KEY, Minestuck.id("prospit"));
	
	private static final WFCData.ConnectorType AIR_CONNECTOR = new WFCData.ConnectorType(Minestuck.id("air"));
	
	public static final class Entries
	{
		public static final WFCData.EntryProvider EMPTY = new WFCData.PieceEntry(pos -> null, Map.of(
				Direction.DOWN, AIR_CONNECTOR,
				Direction.UP, AIR_CONNECTOR,
				Direction.NORTH, AIR_CONNECTOR,
				Direction.EAST, AIR_CONNECTOR,
				Direction.SOUTH, AIR_CONNECTOR,
				Direction.WEST, AIR_CONNECTOR
		));
		public static final WFCData.EntryProvider SOLID = WFCData.TemplateEntry.symmetric(Minestuck.id("prospit/solid"));
		public static final WFCData.EntryProvider PYRAMID_ROOF = WFCData.TemplateEntry.symmetric(Minestuck.id("prospit/pyramid_roof"));
		public static final WFCData.EntryProvider SPIKE = WFCData.TemplateEntry.symmetric(Minestuck.id("prospit/spike"));
		public static final WFCData.EntryProvider BRIDGE = WFCData.TemplateEntry.axisSymmetric(Minestuck.id("prospit/bridge"));
		public static final WFCData.EntryProvider LEDGE = WFCData.TemplateEntry.rotatable(Minestuck.id("prospit/ledge"));
		public static final WFCData.EntryProvider LEDGE_CORNER = WFCData.TemplateEntry.rotatable(Minestuck.id("prospit/ledge_corner"));
		public static final WFCData.EntryProvider SUPPORT = WFCData.TemplateEntry.rotatable(Minestuck.id("prospit/support"));
		public static final WFCData.EntryProvider CORRIDOR = WFCData.TemplateEntry.axisSymmetric(Minestuck.id("prospit/corridor"));
		public static final WFCData.EntryProvider CORRIDOR_WINDOW = WFCData.TemplateEntry.rotatable(Minestuck.id("prospit/corridor_window"));
		public static final WFCData.EntryProvider TURN_CORRIDOR = WFCData.TemplateEntry.rotatable(Minestuck.id("prospit/turn_corridor"));
		public static final WFCData.EntryProvider T_CORRIDOR = WFCData.TemplateEntry.rotatable(Minestuck.id("prospit/t_corridor"));
		public static final WFCData.EntryProvider T_CORRIDOR_WINDOW = WFCData.TemplateEntry.rotatable(Minestuck.id("prospit/t_corridor_window"));
		public static final WFCData.EntryProvider ROOM = WFCData.TemplateEntry.rotatable(Minestuck.id("prospit/room"));
		public static final WFCData.EntryProvider INTERIOR_STAIRS = WFCData.TemplateEntry.rotatable(Minestuck.id("prospit/interior_stairs"));
		public static final WFCData.EntryProvider OUTDOOR_STAIRS = WFCData.TemplateEntry.rotatable(Minestuck.id("prospit/outdoor_stairs"));
	}
	
	public static WFCData.EntryPalette buildCenterPalette(Holder<WFCData.ConnectionSet> connectionSet, StructureTemplateManager templateManager)
	{
		WFCData.EntriesBuilder builder = new WFCData.EntriesBuilder(PIECE_SIZE, templateManager);
		
		builder.add(connectionSet);
		
		builder.add(Entries.EMPTY, 30);
		builder.add(Entries.SOLID, 10);
		builder.add(Entries.SUPPORT, 4);
		builder.add(Entries.PYRAMID_ROOF, 2);
		builder.add(Entries.SPIKE, 1);
		builder.add(Entries.BRIDGE, 3);
		builder.add(Entries.LEDGE, 2);
		builder.add(Entries.LEDGE_CORNER, 2);
		builder.add(Entries.CORRIDOR, 5);
		builder.add(Entries.CORRIDOR_WINDOW, 3);
		builder.add(Entries.TURN_CORRIDOR, 3);
		builder.add(Entries.T_CORRIDOR, 4);
		builder.add(Entries.T_CORRIDOR_WINDOW, 5);
		builder.add(Entries.ROOM, 1);
		builder.add(Entries.INTERIOR_STAIRS, 4);
		builder.add(Entries.OUTDOOR_STAIRS, 4);
		
		return builder.build();
	}
	
	private static WFCData.EntryPalette buildOpenZonePalette(Holder<WFCData.ConnectionSet> connectionSet, StructureTemplateManager templateManager)
	{
		WFCData.EntriesBuilder builder = new WFCData.EntriesBuilder(PIECE_SIZE, templateManager);
		
		builder.add(connectionSet);
		
		builder.add(Entries.EMPTY, 200);
		builder.add(Entries.SOLID, 1);
		builder.add(Entries.SUPPORT, 8);
		builder.add(Entries.PYRAMID_ROOF, 2);
		builder.add(Entries.SPIKE, 1);
		builder.add(Entries.BRIDGE, 10);
		builder.add(Entries.LEDGE, 3);
		builder.add(Entries.LEDGE_CORNER, 3);
		builder.add(Entries.CORRIDOR, 1);
		builder.add(Entries.CORRIDOR_WINDOW, 5);
		builder.add(Entries.TURN_CORRIDOR, 3);
		builder.add(Entries.T_CORRIDOR, 1);
		builder.add(Entries.T_CORRIDOR_WINDOW, 5);
		builder.add(Entries.INTERIOR_STAIRS, 2);
		builder.add(Entries.OUTDOOR_STAIRS, 7);
		
		return builder.build();
	}
	
	public static WFCData.EntryPalette buildBorderPalette(Holder<WFCData.ConnectionSet> connectionSet, StructureTemplateManager templateManager)
	{
		WFCData.EntriesBuilder builder = new WFCData.EntriesBuilder(PIECE_SIZE, templateManager);
		
		builder.add(connectionSet);
		
		builder.add(Entries.EMPTY, 10);
		builder.add(Entries.SOLID, 10);
		builder.add(Entries.SUPPORT, 1);
		builder.add(Entries.PYRAMID_ROOF, 3);
		builder.add(Entries.SPIKE, 1);
		builder.add(Entries.CORRIDOR, 3);
		builder.add(Entries.T_CORRIDOR, 5);
		builder.add(Entries.INTERIOR_STAIRS, 4);
		
		return builder.build();
	}
}
