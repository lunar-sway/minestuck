package com.mraof.minestuck.world.gen.structure;

import com.mojang.serialization.Codec;
import com.mraof.minestuck.Minestuck;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.Direction;
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
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

import static com.mraof.minestuck.world.gen.structure.MSStructureTypes.asType;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class ProspitStructure
{
	public static final WFC.PieceSize PIECE_SIZE = new WFC.PieceSize(8, 8);
	public static final int WIDTH_IN_PIECES = 16, HEIGHT_IN_PIECES = 16;
	public static final WFC.Dimensions WFC_DIMENSIONS = new WFC.Dimensions(WIDTH_IN_PIECES, HEIGHT_IN_PIECES, WIDTH_IN_PIECES);
	public static final int WIDTH_IN_CHUNKS = (PIECE_SIZE.width() * WIDTH_IN_PIECES) / 16;
	public static final int BOTTOM_Y = 0;
	
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
			
			WFC.Template borderTemplate = new WFC.Template(ProspitStructure.WFC_DIMENSIONS, buildBorderEntries(templateManager));
			WFC.Template centerTemplate = new WFC.Template(ProspitStructure.WFC_DIMENSIONS, buildCenterEntries(templateManager));
			borderTemplate.setupFixedEdgeBounds(Direction.UP, Set.of(WFCData.ConnectorType.AIR));
			centerTemplate.setupFixedEdgeBounds(Direction.UP, Set.of(WFCData.ConnectorType.AIR));
			
			WFC.PositionTransform middleTransform = new WFC.PositionTransform(context.chunkPos().getMiddleBlockPosition(BOTTOM_Y), PIECE_SIZE);
			
			WFC.InfiniteModularGeneration.generateModule(middleTransform, ProspitStructure.WFC_DIMENSIONS, centerTemplate, borderTemplate, randomFactory, piecesBuilder);
		}
	}
	
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
	
	private static WFCData.EntriesData buildCenterEntries(StructureTemplateManager templateManager)
	{
		WFCData.EntriesBuilder builder = new WFCData.EntriesBuilder(PIECE_SIZE, templateManager);
		
		WFCData.ConnectorType.addCoreConnections(builder.connections());
		
		builder.add(WFCData.PieceEntry.EMPTY, 30);
		builder.add(SOLID, 10);
		builder.add(SUPPORT, 4);
		builder.add(PYRAMID_ROOF, 2);
		builder.add(SPIKE, 1);
		builder.add(BRIDGE, 3);
		builder.add(LEDGE, 2);
		builder.add(LEDGE_CORNER, 2);
		builder.add(CORRIDOR, 5);
		builder.add(CORRIDOR_WINDOW, 3);
		builder.add(TURN_CORRIDOR, 3);
		builder.add(T_CORRIDOR, 4);
		builder.add(T_CORRIDOR_WINDOW, 5);
		builder.add(ROOM, 1);
		builder.add(INTERIOR_STAIRS, 4);
		
		return builder.build();
	}
	
	private static WFCData.EntriesData buildBorderEntries(StructureTemplateManager templateManager)
	{
		WFCData.EntriesBuilder builder = new WFCData.EntriesBuilder(PIECE_SIZE, templateManager);
		
		WFCData.ConnectorType.addCoreConnections(builder.connections());
		
		builder.add(WFCData.PieceEntry.EMPTY, 10);
		builder.add(SOLID, 10);
		builder.add(SUPPORT, 1);
		builder.add(PYRAMID_ROOF, 3);
		builder.add(SPIKE, 1);
		builder.add(CORRIDOR, 3);
		builder.add(T_CORRIDOR, 5);
		builder.add(INTERIOR_STAIRS, 4);
		
		return builder.build();
	}
}
