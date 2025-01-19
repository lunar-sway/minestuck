package com.mraof.minestuck.world.gen.structure.wfc;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Streams;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.world.gen.structure.SimpleTemplatePiece;
import net.minecraft.core.*;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
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
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Contains types for describing pieces and how they connect, as well as builders for defining data of these types.
 */
@EventBusSubscriber(modid = Minestuck.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
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
		public static final Codec<ConnectorType> CODEC = ResourceLocation.CODEC.xmap(ConnectorType::new, ConnectorType::id);
		
		public static final ConnectorType
				TOP_BORDER = new ConnectorType(Minestuck.id("top_border")),
				BOTTOM_BORDER = new ConnectorType(Minestuck.id("bottom_border"));
	}
	
	public record PieceEntry(Function<BlockPos, StructurePiece> constructor,
							 Map<Direction, ConnectorType> connections)
	{
	}
	
	public enum PrototypeType implements StringRepresentable
	{
		EMPTY(EmptyEntryPrototype.CODEC),
		TEMPLATE(TemplateEntryPrototype.CODEC);
		
		public static final Codec<PrototypeType> CODEC = StringRepresentable.fromEnum(PrototypeType::values);
		
		private final MapCodec<? extends EntryPrototype> prototypeCodec;
		
		PrototypeType(MapCodec<? extends EntryPrototype> prototypeCodec)
		{
			this.prototypeCodec = prototypeCodec;
		}
		
		@Override
		public String getSerializedName()
		{
			return this.name().toLowerCase(Locale.ROOT);
		}
	}
	
	public sealed interface EntryPrototype
	{
		static final ResourceKey<Registry<EntryPrototype>> REGISTRY_KEY = ResourceKey.createRegistryKey(Minestuck.id("wfc_entry_prototype"));
		
		PrototypeType type();
		
		void build(EntryBuilderContext context);
	}
	
	@SubscribeEvent
	private static void onDatapackNewRegistryEvent(DataPackRegistryEvent.NewRegistry event)
	{
		event.dataPackRegistry(EntryPrototype.REGISTRY_KEY, PrototypeType.CODEC.dispatch(EntryPrototype::type, type -> type.prototypeCodec));
	}
	
	public record EmptyEntryPrototype(ConnectorType connector) implements EntryPrototype
	{
		public static final MapCodec<EmptyEntryPrototype> CODEC = ConnectorType.CODEC.fieldOf("connector")
				.xmap(EmptyEntryPrototype::new, EmptyEntryPrototype::connector);
		
		@Override
		public PrototypeType type()
		{
			return PrototypeType.EMPTY;
		}
		
		@Override
		public void build(EntryBuilderContext context)
		{
			context.entriesBuilder().add(new PieceEntry(pos -> null, Map.of(
					Direction.DOWN, this.connector,
					Direction.UP, this.connector,
					Direction.NORTH, this.connector,
					Direction.EAST, this.connector,
					Direction.SOUTH, this.connector,
					Direction.WEST, this.connector
			)));
		}
	}
	
	public enum Symmetry implements StringRepresentable
	{
		SYMMETRIC(List.of(Rotation.NONE)),
		AXIS_SYMMETRIC(List.of(Rotation.NONE, Rotation.CLOCKWISE_90)),
		ROTATABLE(List.of(Rotation.values()));
		
		public static final Codec<Symmetry> CODEC = StringRepresentable.fromEnum(Symmetry::values);
		
		private final List<Rotation> rotations;
		
		Symmetry(List<Rotation> rotations)
		{
			this.rotations = rotations;
		}
		
		@Override
		public String getSerializedName()
		{
			return this.name().toLowerCase(Locale.ROOT);
		}
	}
	
	public record TemplateEntryPrototype(ResourceLocation templateId, Symmetry symmetry) implements EntryPrototype
	{
		public static final MapCodec<TemplateEntryPrototype> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
				ResourceLocation.CODEC.fieldOf("template").forGetter(TemplateEntryPrototype::templateId),
				Symmetry.CODEC.fieldOf("symmetry").forGetter(TemplateEntryPrototype::symmetry)
		).apply(instance, TemplateEntryPrototype::new));
		
		@Override
		public PrototypeType type()
		{
			return PrototypeType.TEMPLATE;
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
						for(Rotation rotation : this.symmetry.rotations)
							loadedEntry.build(rotation, context);
					});
		}
		
		private static void loadConnectorsFromJigsaws(ResourceLocation templateId, StructureTemplate template, WFCUtil.CellSize cellSize, TemplateEntryBuilder builder)
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
				WFCUtil.CellPos pos = new WFCUtil.CellPos(blockInfo.pos().getX() / cellSize.width(),
						blockInfo.pos().getY() / cellSize.height(), blockInfo.pos().getZ() / cellSize.width());
				Direction direction = blockInfo.state().getValue(JigsawBlock.ORIENTATION).front();
				
				builder.put(pos, direction, connectorType);
			}
		}
	}
	
	private static final class TemplateEntryBuilder
	{
		private final ResourceLocation templateId;
		private final WFCUtil.Dimensions templateDimensions;
		
		final Map<WFCUtil.CellPos, Map<Direction, ConnectorType>> connectors = new HashMap<>();
		boolean failed = false;
		
		private TemplateEntryBuilder(ResourceLocation templateId, WFCUtil.Dimensions templateDimensions)
		{
			this.templateId = templateId;
			this.templateDimensions = templateDimensions;
		}
		
		static Optional<TemplateEntryBuilder> init(ResourceLocation templateId, Vec3i templateSize, WFCUtil.CellSize cellSize)
		{
			if(templateSize.getX() % cellSize.width() != 0 || templateSize.getY() % cellSize.height() != 0 || templateSize.getZ() % cellSize.width() != 0)
			{
				LOGGER.error("Template {} of size {} is not a multiple of {}", templateId, templateSize, cellSize);
				return Optional.empty();
			}
			WFCUtil.Dimensions dimensions = new WFCUtil.Dimensions(templateSize.getX() / cellSize.width(),
					templateSize.getY() / cellSize.height(), templateSize.getZ() / cellSize.width());
			return Optional.of(new TemplateEntryBuilder(templateId, dimensions));
		}
		
		void put(WFCUtil.CellPos pos, Direction direction, ConnectorType connectorType)
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
	
	private record LoadedTemplateEntry(ResourceLocation templateId, WFCUtil.Dimensions templateDimensions,
									   Map<WFCUtil.CellPos, Map<Direction, ConnectorType>> edgeConnectors)
	{
		void build(Rotation rotation, EntryBuilderContext context)
		{
			Map<Direction.Axis, Map<WFCUtil.CellPos, ConnectorType>> connectors = new EnumMap<>(Direction.Axis.class);
			
			BiFunction<Direction.Axis, WFCUtil.CellPos, ConnectorType> innerConnectorGetter = (axis, cellPos) ->
					connectors.computeIfAbsent(axis, ignored -> new HashMap<>())
							.computeIfAbsent(cellPos, ignored -> addNewConnector(cellPos, axis, rotation, context.connectionsBuilder()));
			
			for(WFCUtil.CellPos cellPos : this.templateDimensions.iterateAll())
			{
				Function<BlockPos, StructurePiece> constructor;
				if(cellPos.x() == 0 && cellPos.y() == 0 && cellPos.z() == 0)
					constructor = templateConstructor(context.cellSize(), rotation, context.templateManager());
				else
					constructor = pos -> null;
				
				Map<Direction, ConnectorType> entryConnectors = Map.of(
						Direction.DOWN, templateDimensions.isOnEdge(cellPos, Direction.DOWN)
								? edgeConnectors().get(cellPos).get(Direction.DOWN)
								: innerConnectorGetter.apply(Direction.Axis.Y, new WFCUtil.CellPos(cellPos.x(), cellPos.y() - 1, cellPos.z())),
						Direction.UP, templateDimensions.isOnEdge(cellPos, Direction.UP)
								? edgeConnectors().get(cellPos).get(Direction.UP)
								: innerConnectorGetter.apply(Direction.Axis.Y, cellPos),
						Direction.NORTH, templateDimensions.isOnEdge(cellPos, Direction.NORTH)
								? edgeConnectors().get(cellPos).get(Direction.NORTH)
								: innerConnectorGetter.apply(Direction.Axis.Z, new WFCUtil.CellPos(cellPos.x(), cellPos.y(), cellPos.z() - 1)),
						Direction.EAST, templateDimensions.isOnEdge(cellPos, Direction.EAST)
								? edgeConnectors().get(cellPos).get(Direction.EAST)
								: innerConnectorGetter.apply(Direction.Axis.X, cellPos),
						Direction.SOUTH, templateDimensions.isOnEdge(cellPos, Direction.SOUTH)
								? edgeConnectors().get(cellPos).get(Direction.SOUTH)
								: innerConnectorGetter.apply(Direction.Axis.Z, cellPos),
						Direction.WEST, templateDimensions.isOnEdge(cellPos, Direction.WEST)
								? edgeConnectors().get(cellPos).get(Direction.WEST)
								: innerConnectorGetter.apply(Direction.Axis.X, new WFCUtil.CellPos(cellPos.x() - 1, cellPos.y(), cellPos.z()))
				);
				
				PieceEntry entry = new PieceEntry(constructor, rotateConnectors(entryConnectors, rotation));
				context.entriesBuilder().add(entry);
			}
		}
		
		private ConnectorType addNewConnector(WFCUtil.CellPos pos, Direction.Axis axis, Rotation rotation, ConnectionsBuilder builder)
		{
			ConnectorType newConnector = new ConnectorType(templateId.withSuffix("/" + pos.x() + "_" + pos.y() + "_" + pos.z()
					+ "_" + axis.getSerializedName() + "_" + rotation.getSerializedName()));
			builder.connectSelf(newConnector);
			return newConnector;
		}
		
		private Function<BlockPos, StructurePiece> templateConstructor(WFCUtil.CellSize cellSize, Rotation rotation, StructureTemplateManager templateManager)
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
									  WFCUtil.CellSize cellSize, StructureTemplateManager templateManager)
	{
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
		private final WFCUtil.CellSize cellSize;
		private final StructureTemplateManager templateManager;
		
		private final ConnectionsBuilder connectionsBuilder = new ConnectionsBuilder();
		private final ImmutableList.Builder<WeightedEntry.Wrapper<PieceEntry>> pieceEntries = ImmutableList.builder();
		
		public EntriesBuilder(WFCUtil.CellSize cellSize, StructureTemplateManager templateManager)
		{
			this.cellSize = cellSize;
			this.templateManager = templateManager;
		}
		
		public ConnectionsBuilder connections()
		{
			return this.connectionsBuilder;
		}
		
		public void add(Holder<ConnectionSet> connectionSet)
		{
			connectionSet.value().connections().forEach(pair -> this.connectionsBuilder.connect(pair.getFirst(), pair.getSecond()));
		}
		
		public void add(Holder<EntryPrototype> provider, int weight)
		{
			provider.value().build(new EntryBuilderContext(this.connectionsBuilder, pieceEntry -> this.pieceEntries.add(WeightedEntry.wrap(pieceEntry, weight)), this.cellSize, this.templateManager));
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
	
	@EventBusSubscriber(modid = Minestuck.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
	public static record ConnectionSet(List<Pair<ConnectorType, ConnectorType>> connections)
	{
		private static final Codec<Pair<ConnectorType, ConnectorType>> CONNECTION_CODEC = ConnectorType.CODEC.listOf(2, 2)
				.xmap(list -> Pair.of(list.getFirst(), list.get(1)), pair -> List.of(pair.getFirst(), pair.getSecond()));
		private static final Codec<ConnectionSet> DIRECT_CODEC = CONNECTION_CODEC.listOf().xmap(ConnectionSet::new, ConnectionSet::connections);
		
		public static final ResourceKey<Registry<ConnectionSet>> REGISTRY_KEY = ResourceKey.createRegistryKey(Minestuck.id("wfc_connection_set"));
		
		@SubscribeEvent
		private static void onDatapackNewRegistryEvent(DataPackRegistryEvent.NewRegistry event)
		{
			event.dataPackRegistry(REGISTRY_KEY, DIRECT_CODEC);
		}
	}
}
