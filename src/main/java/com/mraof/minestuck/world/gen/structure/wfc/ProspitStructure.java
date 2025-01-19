package com.mraof.minestuck.world.gen.structure.wfc;

import com.mojang.serialization.MapCodec;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.world.gen.structure.MSStructures;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
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
			HolderGetter<WFCData.EntryPrototype> prototypeLookup = context.registryAccess().lookupOrThrow(WFCData.EntryPrototype.REGISTRY_KEY);
			PositionalRandomFactory randomFactory = RandomSource.create(context.seed()).forkPositional().fromHashOf(Minestuck.id("prospit")).forkPositional();
			
			WFCUtil.PositionTransform middleTransform = new WFCUtil.PositionTransform(context.chunkPos().getMiddleBlockPosition(BOTTOM_Y), PIECE_SIZE);
			
			Holder<WFCData.ConnectionSet> connectionSet = context.registryAccess().lookupOrThrow(WFCData.ConnectionSet.REGISTRY_KEY).getOrThrow(PROSPIT_CONNECTIONS);
			WFCData.EntryPalette centerPalette = context.random().nextBoolean()
					? buildCenterPalette(connectionSet, prototypeLookup, templateManager)
					: buildOpenZonePalette(connectionSet, prototypeLookup, templateManager);
			WFCData.EntryPalette borderPalette = buildBorderPalette(connectionSet, prototypeLookup, templateManager);
			
			WFC.InfiniteModularGeneration.generateModule(middleTransform, ProspitStructure.WFC_DIMENSIONS,
					centerPalette, borderPalette, randomFactory, piecesBuilder, null);
		}
	}
	
	public static final ResourceKey<WFCData.ConnectionSet> PROSPIT_CONNECTIONS = ResourceKey.create(WFCData.ConnectionSet.REGISTRY_KEY, Minestuck.id("prospit"));
	
	private static final WFCData.ConnectorType AIR_CONNECTOR = new WFCData.ConnectorType(Minestuck.id("air"));
	
	public static final class Entries
	{
		public static final ResourceKey<WFCData.EntryPrototype> EMPTY = key("empty");
		public static final ResourceKey<WFCData.EntryPrototype> SOLID = key("solid");
		public static final ResourceKey<WFCData.EntryPrototype> PYRAMID_ROOF = key("pyramid_roof");
		public static final ResourceKey<WFCData.EntryPrototype> SPIKE = key("spike");
		public static final ResourceKey<WFCData.EntryPrototype> BRIDGE = key("bridge");
		public static final ResourceKey<WFCData.EntryPrototype> LEDGE = key("ledge");
		public static final ResourceKey<WFCData.EntryPrototype> LEDGE_CORNER = key("ledge_corner");
		public static final ResourceKey<WFCData.EntryPrototype> SUPPORT = key("support");
		public static final ResourceKey<WFCData.EntryPrototype> CORRIDOR = key("corridor");
		public static final ResourceKey<WFCData.EntryPrototype> CORRIDOR_WINDOW = key("corridor_window");
		public static final ResourceKey<WFCData.EntryPrototype> TURN_CORRIDOR = key("turn_corridor");
		public static final ResourceKey<WFCData.EntryPrototype> T_CORRIDOR = key("t_corridor");
		public static final ResourceKey<WFCData.EntryPrototype> T_CORRIDOR_WINDOW = key("t_corridor_window");
		public static final ResourceKey<WFCData.EntryPrototype> INTERIOR_STAIRS = key("interior_stairs");
		public static final ResourceKey<WFCData.EntryPrototype> OUTDOOR_STAIRS = key("outdoor_stairs");
		
		private static ResourceKey<WFCData.EntryPrototype> key(String name)
		{
			return ResourceKey.create(WFCData.EntryPrototype.REGISTRY_KEY, Minestuck.id("prospit/" + name));
		}
	}
	
	public static WFCData.EntryPalette buildCenterPalette(Holder<WFCData.ConnectionSet> connectionSet, HolderGetter<WFCData.EntryPrototype> prototypeLookup,
														  StructureTemplateManager templateManager)
	{
		WFCData.EntriesBuilder builder = new WFCData.EntriesBuilder(PIECE_SIZE, templateManager);
		
		builder.add(connectionSet);
		
		builder.add(prototypeLookup.getOrThrow(Entries.EMPTY), 30);
		builder.add(prototypeLookup.getOrThrow(Entries.SOLID), 10);
		builder.add(prototypeLookup.getOrThrow(Entries.SUPPORT), 4);
		builder.add(prototypeLookup.getOrThrow(Entries.PYRAMID_ROOF), 2);
		builder.add(prototypeLookup.getOrThrow(Entries.SPIKE), 1);
		builder.add(prototypeLookup.getOrThrow(Entries.BRIDGE), 3);
		builder.add(prototypeLookup.getOrThrow(Entries.LEDGE), 2);
		builder.add(prototypeLookup.getOrThrow(Entries.LEDGE_CORNER), 2);
		builder.add(prototypeLookup.getOrThrow(Entries.CORRIDOR), 5);
		builder.add(prototypeLookup.getOrThrow(Entries.CORRIDOR_WINDOW), 3);
		builder.add(prototypeLookup.getOrThrow(Entries.TURN_CORRIDOR), 3);
		builder.add(prototypeLookup.getOrThrow(Entries.T_CORRIDOR), 4);
		builder.add(prototypeLookup.getOrThrow(Entries.T_CORRIDOR_WINDOW), 5);
		builder.add(Holder.direct(new WFCData.TemplateEntryPrototype(Minestuck.id("prospit/room"), WFCData.Symmetry.ROTATABLE)), 1);
		builder.add(prototypeLookup.getOrThrow(Entries.INTERIOR_STAIRS), 4);
		builder.add(prototypeLookup.getOrThrow(Entries.OUTDOOR_STAIRS), 4);
		
		return builder.build();
	}
	
	private static WFCData.EntryPalette buildOpenZonePalette(Holder<WFCData.ConnectionSet> connectionSet, HolderGetter<WFCData.EntryPrototype> prototypeLookup,
															 StructureTemplateManager templateManager)
	{
		WFCData.EntriesBuilder builder = new WFCData.EntriesBuilder(PIECE_SIZE, templateManager);
		
		builder.add(connectionSet);
		
		builder.add(prototypeLookup.getOrThrow(Entries.EMPTY), 200);
		builder.add(prototypeLookup.getOrThrow(Entries.SOLID), 1);
		builder.add(prototypeLookup.getOrThrow(Entries.SUPPORT), 8);
		builder.add(prototypeLookup.getOrThrow(Entries.PYRAMID_ROOF), 2);
		builder.add(prototypeLookup.getOrThrow(Entries.SPIKE), 1);
		builder.add(prototypeLookup.getOrThrow(Entries.BRIDGE), 10);
		builder.add(prototypeLookup.getOrThrow(Entries.LEDGE), 3);
		builder.add(prototypeLookup.getOrThrow(Entries.LEDGE_CORNER), 3);
		builder.add(prototypeLookup.getOrThrow(Entries.CORRIDOR), 1);
		builder.add(prototypeLookup.getOrThrow(Entries.CORRIDOR_WINDOW), 5);
		builder.add(prototypeLookup.getOrThrow(Entries.TURN_CORRIDOR), 3);
		builder.add(prototypeLookup.getOrThrow(Entries.T_CORRIDOR), 1);
		builder.add(prototypeLookup.getOrThrow(Entries.T_CORRIDOR_WINDOW), 5);
		builder.add(prototypeLookup.getOrThrow(Entries.INTERIOR_STAIRS), 2);
		builder.add(prototypeLookup.getOrThrow(Entries.OUTDOOR_STAIRS), 7);
		
		return builder.build();
	}
	
	public static WFCData.EntryPalette buildBorderPalette(Holder<WFCData.ConnectionSet> connectionSet, HolderGetter<WFCData.EntryPrototype> prototypeLookup,
														  StructureTemplateManager templateManager)
	{
		WFCData.EntriesBuilder builder = new WFCData.EntriesBuilder(PIECE_SIZE, templateManager);
		
		builder.add(connectionSet);
		
		builder.add(prototypeLookup.getOrThrow(Entries.EMPTY), 10);
		builder.add(prototypeLookup.getOrThrow(Entries.SOLID), 10);
		builder.add(prototypeLookup.getOrThrow(Entries.SUPPORT), 1);
		builder.add(prototypeLookup.getOrThrow(Entries.PYRAMID_ROOF), 3);
		builder.add(prototypeLookup.getOrThrow(Entries.SPIKE), 1);
		builder.add(prototypeLookup.getOrThrow(Entries.CORRIDOR), 3);
		builder.add(prototypeLookup.getOrThrow(Entries.T_CORRIDOR), 5);
		builder.add(prototypeLookup.getOrThrow(Entries.INTERIOR_STAIRS), 4);
		
		return builder.build();
	}
}
