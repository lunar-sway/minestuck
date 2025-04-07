package com.mraof.minestuck.world.gen.structure.wfc;

import com.mojang.serialization.MapCodec;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.world.gen.structure.MSStructures;
import net.minecraft.MethodsReturnNonnullByDefault;
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
public final class ProspitWFCDemoStructure
{
	public static final WFCUtil.CellSize CELL_SIZE = new WFCUtil.CellSize(8, 8);
	public static final int WIDTH_IN_PIECES = 16, HEIGHT_IN_PIECES = 14;
	public static final WFCUtil.Dimensions WFC_DIMENSIONS = new WFCUtil.Dimensions(WIDTH_IN_PIECES, HEIGHT_IN_PIECES, WIDTH_IN_PIECES);
	public static final int WIDTH_IN_CHUNKS = (CELL_SIZE.width() * WIDTH_IN_PIECES) / 16;
	public static final int BOTTOM_Y = 1;
	
	public static void init()
	{
	}
	
	public static final ResourceKey<Structure> STRUCTURE = ResourceKey.create(Registries.STRUCTURE, Minestuck.id("prospit_wfc_demo"));
	
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
	
	public static final Supplier<StructureType<TerrainStructure>> STRUCTURE_TYPE = MSStructures.TYPE_REGISTER.register("prospit_wfc_demo",
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
			HolderGetter<WFCData.PaletteData> paletteLookup = context.registryAccess().lookupOrThrow(WFCData.PaletteData.REGISTRY_KEY);
			PositionalRandomFactory randomFactory = RandomSource.create(context.seed()).forkPositional().fromHashOf(Minestuck.id("prospit")).forkPositional();
			
			WFCUtil.PositionTransform middleTransform = new WFCUtil.PositionTransform(context.chunkPos().getMiddleBlockPosition(BOTTOM_Y), CELL_SIZE);
			
			ResourceKey<WFCData.PaletteData> centerPaletteKey = context.random().nextBoolean() ? Palettes.NORMAL : Palettes.OPEN_ZONE;
			WFCData.EntryPalette centerPalette = paletteLookup.getOrThrow(centerPaletteKey).value().build(CELL_SIZE, templateManager);
			WFCData.EntryPalette borderPalette = paletteLookup.getOrThrow(Palettes.BORDER).value().build(CELL_SIZE, templateManager);
			
			WFC.InfiniteModularGeneration.generateModule(middleTransform, ProspitWFCDemoStructure.WFC_DIMENSIONS,
					centerPalette, borderPalette, randomFactory, piecesBuilder, null);
		}
	}
	
	public static final class Palettes
	{
		public static final ResourceKey<WFCData.PaletteData> BORDER = key("border");
		public static final ResourceKey<WFCData.PaletteData> NORMAL = key("normal");
		public static final ResourceKey<WFCData.PaletteData> OPEN_ZONE = key("open_zone");
		
		private static ResourceKey<WFCData.PaletteData> key(String name)
		{
			return ResourceKey.create(WFCData.PaletteData.REGISTRY_KEY, Minestuck.id("prospit/" + name));
		}
	}
}
