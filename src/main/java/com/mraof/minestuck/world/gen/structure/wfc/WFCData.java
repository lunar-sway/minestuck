package com.mraof.minestuck.world.gen.structure.wfc;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Streams;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.world.gen.structure.SimpleTemplatePiece;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.JigsawBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.JigsawBlockEntity;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Contains types for describing pieces and how they connect, as well as builders for defining data of these types.
 */
public final class WFCData
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	private static Map<Direction, ConnectorType> rotateConnectors(Map<Direction, ConnectorType> connections, Rotation rotation)
	{
		if(rotation == Rotation.NONE)
			return Map.copyOf(connections);
		
		return Map.of(
				Direction.DOWN, connections.get(Direction.DOWN),
				Direction.UP, connections.get(Direction.UP),
				rotation.rotate(Direction.NORTH), connections.get(Direction.NORTH),
				rotation.rotate(Direction.EAST), connections.get(Direction.EAST),
				rotation.rotate(Direction.SOUTH), connections.get(Direction.SOUTH),
				rotation.rotate(Direction.WEST), connections.get(Direction.WEST)
		);
	}
	
	public record ConnectorType(ResourceLocation id)
	{
		public static final ConnectorType
				TOP_BORDER = new ConnectorType(Minestuck.id("top_border")),
				BOTTOM_BORDER = new ConnectorType(Minestuck.id("bottom_border"));
	}
	
	public record PieceEntry(Function<BlockPos, StructurePiece> constructor,
							 Map<Direction, ConnectorType> connections) implements EntryProvider
	{
		@Override
		public void build(EntryBuilderContext context)
		{
			context.entriesBuilder().add(this);
		}
	}
	
	public record TemplateEntry(ResourceLocation templateId, List<Rotation> rotations) implements EntryProvider
	{
		public static EntryProvider symmetric(ResourceLocation templateId)
		{
			return new TemplateEntry(templateId, List.of(Rotation.NONE));
		}
		
		public static EntryProvider axisSymmetric(ResourceLocation templateId)
		{
			return new TemplateEntry(templateId, List.of(Rotation.NONE, Rotation.CLOCKWISE_90));
		}
		
		public static EntryProvider rotatable(ResourceLocation templateId)
		{
			return new TemplateEntry(templateId, List.of(Rotation.values()));
		}
		
		@Override
		public void build(EntryBuilderContext context)
		{
			StructureTemplate template = context.templateManager().getOrCreate(this.templateId);
			
			TemplateEntryBuilder
					.init(this.templateId, template.getSize(), context.cellSize())
					.flatMap(builder -> {
						loadConnectorsFromJigsaws(this.templateId, template, context.cellSize(), builder);
						return builder.verify();
					})
					.ifPresent(loadedEntry -> {
						for(Rotation rotation : this.rotations)
							loadedEntry.build(rotation, context);
					});
		}
		
		private static void loadConnectorsFromJigsaws(ResourceLocation templateId, StructureTemplate template, WFC.CellSize cellSize, TemplateEntryBuilder builder)
		{
			for(StructureTemplate.StructureBlockInfo blockInfo : template.filterBlocks(BlockPos.ZERO, new StructurePlaceSettings(), Blocks.JIGSAW, false))
			{
				if(blockInfo.nbt() == null)
				{
					LOGGER.warn("Jigsaw block is missing data in template {} at {}", templateId, blockInfo.pos());
					continue;
				}
				String connectorName = blockInfo.nbt().getString(JigsawBlockEntity.NAME);
				ResourceLocation connectorId = ResourceLocation.tryParse(connectorName);
				if(connectorId == null)
				{
					LOGGER.error("Connector name \"{}\" in template {} is not a valid id", connectorName, templateId);
					continue;
				}
				
				ConnectorType connectorType = new ConnectorType(connectorId);
				WFC.CellPos pos = new WFC.CellPos(blockInfo.pos().getX() / cellSize.width(),
						blockInfo.pos().getY() / cellSize.height(), blockInfo.pos().getZ() / cellSize.width());
				Direction direction = blockInfo.state().getValue(JigsawBlock.ORIENTATION).front();
				
				builder.put(pos, direction, connectorType);
			}
		}
	}
	
	private static final class TemplateEntryBuilder
	{
		private final ResourceLocation templateId;
		private final WFC.Dimensions templateDimensions;
		
		final Map<WFC.CellPos, Map<Direction, ConnectorType>> connectors = new HashMap<>();
		boolean failed = false;
		
		private TemplateEntryBuilder(ResourceLocation templateId, WFC.Dimensions templateDimensions)
		{
			this.templateId = templateId;
			this.templateDimensions = templateDimensions;
		}
		
		static Optional<TemplateEntryBuilder> init(ResourceLocation templateId, Vec3i templateSize, WFC.CellSize cellSize)
		{
			if(templateSize.getX() % cellSize.width() != 0 || templateSize.getY() % cellSize.height() != 0 || templateSize.getZ() % cellSize.width() != 0)
			{
				LOGGER.error("Template {} of size {} is not a multiple of {}", templateId, templateSize, cellSize);
				return Optional.empty();
			}
			WFC.Dimensions dimensions = new WFC.Dimensions(templateSize.getX() / cellSize.width(),
					templateSize.getY() / cellSize.height(), templateSize.getZ() / cellSize.width());
			return Optional.of(new TemplateEntryBuilder(templateId, dimensions));
		}
		
		void put(WFC.CellPos pos, Direction direction, ConnectorType connectorType)
		{
			if(!this.templateDimensions.isOnEdge(pos, direction))
			{
				LOGGER.warn("Connector {} in template {} is not at the edge", connectorType, this.templateId);
				return;
			}
			
			if(this.connectors.computeIfAbsent(pos, ignored -> new HashMap<>()).put(direction, connectorType) != null)
			{
				LOGGER.error("Template {} has multiple jigsaws for the same side ({})", this.templateId, direction);
				this.failed = false;
			}
		}
		
		Optional<LoadedTemplateEntry> verify()
		{
			List<Direction> missingDirections = Arrays.stream(Direction.values()).filter(this::isConnectorMissingOnSide).toList();
			if(!missingDirections.isEmpty())
			{
				LOGGER.error("Template {} is missing connector type for the following sides: {}", this.templateId, missingDirections);
				return Optional.empty();
			}
			
			if(this.failed)
				return Optional.empty();
			
			return Optional.of(new LoadedTemplateEntry(this.templateId, this.templateDimensions,
					this.connectors.entrySet().stream().map(entry -> Map.entry(entry.getKey(), Map.copyOf(entry.getValue())))
							.collect(ImmutableMap.toImmutableMap(Map.Entry::getKey, Map.Entry::getValue))));
		}
		
		private boolean isConnectorMissingOnSide(Direction direction)
		{
			return Streams.stream(this.templateDimensions.iterateEdge(direction))
					.anyMatch(pos -> !this.connectors.getOrDefault(pos, Collections.emptyMap()).containsKey(direction));
		}
	}
	
	private record LoadedTemplateEntry(ResourceLocation templateId, WFC.Dimensions templateDimensions, Map<WFC.CellPos, Map<Direction, ConnectorType>> edgeConnectors)
	{
		void build(Rotation rotation, EntryBuilderContext context)
		{
			Map<Direction.Axis, Map<WFC.CellPos, ConnectorType>> connectors = new EnumMap<>(Direction.Axis.class);
			
			BiFunction<Direction.Axis, WFC.CellPos, ConnectorType> innerConnectorGetter = (axis, cellPos) ->
					connectors.computeIfAbsent(axis, ignored -> new HashMap<>())
							.computeIfAbsent(cellPos, ignored -> addNewConnector(cellPos, axis, rotation, context.connectionsBuilder()));
			
			for(WFC.CellPos cellPos : this.templateDimensions.iterateAll())
			{
				Function<BlockPos, StructurePiece> constructor;
				if(cellPos.x() == 0 && cellPos.y() == 0 && cellPos.z() == 0)
					constructor = templateConstructor(context.cellSize(), rotation, context.templateManager());
				else
					constructor = pos -> null;
				
				Map<Direction, ConnectorType> entryConnectors = Map.of(
						Direction.DOWN, templateDimensions.isOnEdge(cellPos, Direction.DOWN)
								? edgeConnectors().get(cellPos).get(Direction.DOWN)
								: innerConnectorGetter.apply(Direction.Axis.Y, new WFC.CellPos(cellPos.x(), cellPos.y() - 1, cellPos.z())),
						Direction.UP, templateDimensions.isOnEdge(cellPos, Direction.UP)
								? edgeConnectors().get(cellPos).get(Direction.UP)
								: innerConnectorGetter.apply(Direction.Axis.Y, cellPos),
						Direction.NORTH, templateDimensions.isOnEdge(cellPos, Direction.NORTH)
								? edgeConnectors().get(cellPos).get(Direction.NORTH)
								: innerConnectorGetter.apply(Direction.Axis.Z, new WFC.CellPos(cellPos.x(), cellPos.y(), cellPos.z() - 1)),
						Direction.EAST, templateDimensions.isOnEdge(cellPos, Direction.EAST)
								? edgeConnectors().get(cellPos).get(Direction.EAST)
								: innerConnectorGetter.apply(Direction.Axis.X, cellPos),
						Direction.SOUTH, templateDimensions.isOnEdge(cellPos, Direction.SOUTH)
								? edgeConnectors().get(cellPos).get(Direction.SOUTH)
								: innerConnectorGetter.apply(Direction.Axis.Z, cellPos),
						Direction.WEST, templateDimensions.isOnEdge(cellPos, Direction.WEST)
								? edgeConnectors().get(cellPos).get(Direction.WEST)
								: innerConnectorGetter.apply(Direction.Axis.X, new WFC.CellPos(cellPos.x() - 1, cellPos.y(), cellPos.z()))
				);
				
				PieceEntry entry = new PieceEntry(constructor, rotateConnectors(entryConnectors, rotation));
				context.entriesBuilder().add(entry);
			}
		}
		
		private ConnectorType addNewConnector(WFC.CellPos pos, Direction.Axis axis, Rotation rotation, ConnectionsBuilder builder)
		{
			ConnectorType newConnector = new ConnectorType(templateId.withSuffix("/" + pos.x() + "_" + pos.y() + "_" + pos.z()
					+ "_" + axis.getSerializedName() + "_" + rotation.getSerializedName()));
			builder.connectSelf(newConnector);
			return newConnector;
		}
		
		private Function<BlockPos, StructurePiece> templateConstructor(WFC.CellSize cellSize, Rotation rotation, StructureTemplateManager templateManager)
		{
			BlockPos zeroPos = StructureTemplate.getZeroPositionWithTransform(BlockPos.ZERO, Mirror.NONE, rotation, cellSize.width(), cellSize.width());
			return pos -> new SimpleTemplatePiece(templateManager, this.templateId, pos.offset(zeroPos), rotation);
		}
	}
	
	public interface WeightedEntriesBuilder
	{
		void add(PieceEntry entry);
		
		default void add(PieceEntry... entries)
		{
			for(PieceEntry entry : entries)
				this.add(entry);
		}
		
		default void add(Iterable<PieceEntry> entries)
		{
			entries.forEach(this::add);
		}
	}
	
	public record EntryBuilderContext(ConnectionsBuilder connectionsBuilder, WeightedEntriesBuilder entriesBuilder,
									  WFC.CellSize cellSize, StructureTemplateManager templateManager)
	{
	}
	
	public interface EntryProvider
	{
		void build(EntryBuilderContext context);
	}
	
	public static final class ConnectionTester
	{
		private final Map<ConnectorType, Set<ConnectorType>> connectionMap;
		
		private ConnectionTester(Map<ConnectorType, Set<ConnectorType>> connectionMap)
		{
			this.connectionMap = connectionMap;
		}
		
		public boolean canConnect(ConnectorType type, Set<ConnectorType> otherTypes)
		{
			Set<ConnectorType> supportedTypes = this.connectionMap.get(type);
			if(supportedTypes == null)
				return false;
			return otherTypes.stream().anyMatch(supportedTypes::contains);
		}
		
		public boolean isKnown(ConnectorType connectorType)
		{
			return this.connectionMap.containsKey(connectorType);
		}
	}
	
	public record EntryPalette(Collection<WeightedEntry.Wrapper<PieceEntry>> entries, ConnectionTester connectionTester)
	{
	}
	
	public static final class ConnectionsBuilder
	{
		private final Map<ConnectorType, ImmutableSet.Builder<ConnectorType>> connections = new HashMap<>();
		
		public void connectSelf(ConnectorType type)
		{
			this.connect(type, type);
		}
		
		public void connect(ConnectorType type1, ConnectorType type2)
		{
			connections.computeIfAbsent(type1, ignored -> ImmutableSet.builder()).add(type2);
			connections.computeIfAbsent(type2, ignored -> ImmutableSet.builder()).add(type1);
		}
		
		private ConnectionTester buildConnectionTester()
		{
			Map<ConnectorType, Set<ConnectorType>> map = this.connections.entrySet().stream()
					.collect(ImmutableMap.toImmutableMap(Map.Entry::getKey, entry -> entry.getValue().build()));
			return new ConnectionTester(map);
		}
	}
	
	public static final class EntriesBuilder
	{
		private final WFC.CellSize cellSize;
		private final StructureTemplateManager templateManager;
		
		private final ConnectionsBuilder connectionsBuilder = new ConnectionsBuilder();
		private final ImmutableList.Builder<WeightedEntry.Wrapper<PieceEntry>> pieceEntries = ImmutableList.builder();
		
		public EntriesBuilder(WFC.CellSize cellSize, StructureTemplateManager templateManager)
		{
			this.cellSize = cellSize;
			this.templateManager = templateManager;
		}
		
		public ConnectionsBuilder connections()
		{
			return this.connectionsBuilder;
		}
		
		public void add(EntryProvider provider, int weight)
		{
			provider.build(new EntryBuilderContext(this.connectionsBuilder, pieceEntry -> this.pieceEntries.add(WeightedEntry.wrap(pieceEntry, weight)), this.cellSize, this.templateManager));
		}
		
		public EntryPalette build()
		{
			ImmutableList<WeightedEntry.Wrapper<PieceEntry>> entriesList = this.pieceEntries.build();
			ConnectionTester connectionTester = this.connectionsBuilder.buildConnectionTester();
			
			entriesList.stream().flatMap(entry -> entry.data().connections.values().stream()).forEach(connector -> {
				if(!connectionTester.isKnown(connector))
					LOGGER.warn("Found unknown connector: {}", connector.id());
			});
			
			return new EntryPalette(entriesList, connectionTester);
		}
	}
}
