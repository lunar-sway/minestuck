package com.mraof.minestuck.world.gen;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ColorResolver;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkSource;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.lighting.LevelLightEngine;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.storage.LevelData;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.ticks.LevelTickAccess;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

public abstract class WorldGenLevelWrapper implements WorldGenLevel	//TODO go through and override anything that needs overriding
{
	protected final WorldGenLevel level;
	
	public WorldGenLevelWrapper(WorldGenLevel level)
	{
		this.level = level;
	}
	
	// From WorldGenLevel
	
	@Override
	public long getSeed()
	{
		return this.level.getSeed();
	}
	
	@Override
	public boolean ensureCanWrite(BlockPos pos)
	{
		return this.level.ensureCanWrite(pos);
	}
	
	@Override
	public void setCurrentlyGenerating(@Nullable Supplier<String> currentlyGenerating)
	{
		this.level.setCurrentlyGenerating(currentlyGenerating);
	}
	
	// From ServerLevelAccessor
	
	@Override
	public ServerLevel getLevel()
	{
		return this.level.getLevel();
	}
	
	// From LevelAccessor
	
	@Override
	public long nextSubTickCount()
	{
		return this.level.nextSubTickCount();
	}
	
	@Override
	public LevelTickAccess<Block> getBlockTicks()
	{
		return this.level.getBlockTicks();
	}
	
	@Override
	public LevelTickAccess<Fluid> getFluidTicks()
	{
		return this.level.getFluidTicks();
	}
	
	@Override
	public LevelData getLevelData()
	{
		return this.level.getLevelData();
	}
	
	@Override
	public DifficultyInstance getCurrentDifficultyAt(BlockPos pos)
	{
		return this.level.getCurrentDifficultyAt(pos);
	}
	
	@Nullable
	@Override
	public MinecraftServer getServer()
	{
		return this.level.getServer();
	}
	
	@Override
	public ChunkSource getChunkSource()
	{
		return this.level.getChunkSource();
	}
	
	@Override
	public RandomSource getRandom()
	{
		return this.level.getRandom();
	}
	
	@Override
	public void blockUpdated(BlockPos pos, Block block)
	{
		this.level.blockUpdated(pos, block);
	}
	
	@Override
	public void playSound(@Nullable Player player, BlockPos pos, SoundEvent sound, SoundSource category, float volume, float pitch)
	{
		this.level.playSound(player, pos, sound, category, volume, pitch);
	}
	
	@Override
	public void addParticle(ParticleOptions particleData, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed)
	{
		this.level.addParticle(particleData, x, y, z, xSpeed, ySpeed, zSpeed);
	}
	
	@Override
	public void levelEvent(@Nullable Player player, int type, BlockPos pos, int data)
	{
		this.level.levelEvent(player, type, pos, data);
	}
	
	@Override
	public void gameEvent(@Nullable Entity entity, GameEvent event, BlockPos pos)
	{
		this.level.gameEvent(entity, event, pos);
	}
	
	// From CommonLevelAccessor
	
	@Override
	public RegistryAccess registryAccess()
	{
		return this.level.registryAccess();
	}
	
	// From EntityGetter
	
	@Override
	public List<Entity> getEntities(@Nullable Entity entity, AABB area, Predicate<? super Entity> predicate)
	{
		return this.level.getEntities(entity, area, predicate);
	}
	
	@Override
	public <T extends Entity> List<T> getEntities(EntityTypeTest<Entity, T> entityTypeTest, AABB area, Predicate<? super T> predicate)
	{
		return this.level.getEntities(entityTypeTest, area, predicate);
	}
	
	@Override
	public List<? extends Player> players()
	{
		return this.level.players();
	}
	
	@Nullable
	@Override
	public Player getNearestPlayer(double x, double y, double z, double distance, @Nullable Predicate<Entity> predicate)
	{
		return this.level.getNearestPlayer(x, y, z, distance, predicate);
	}
	
	// From LevelReader
	
	@Nullable
	@Override
	public ChunkAccess getChunk(int x, int z, ChunkStatus requiredStatus, boolean nonnull)
	{
		return this.level.getChunk(x, z, requiredStatus, nonnull);
	}
	
	@Override
	public boolean hasChunk(int chunkX, int chunkZ)
	{
		return this.level.hasChunk(chunkX, chunkZ);
	}
	
	@Override
	public int getHeight(Heightmap.Types heightmapType, int x, int z)
	{
		return this.level.getHeight(heightmapType, x, z);
	}
	
	@Override
	public int getSkyDarken()
	{
		return this.level.getSkyDarken();
	}
	
	@Override
	public BiomeManager getBiomeManager()
	{
		return this.level.getBiomeManager();
	}
	
	@Override
	public Holder<Biome> getUncachedNoiseBiome(int x, int y, int z)
	{
		return this.level.getUncachedNoiseBiome(x, y, z);
	}
	
	@Override
	public boolean isClientSide()
	{
		return this.level.isClientSide();
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public int getSeaLevel()
	{
		return this.level.getSeaLevel();
	}
	
	@Override
	public DimensionType dimensionType()
	{
		return this.level.dimensionType();
	}
	
	@Override
	public ChunkAccess getChunk(int chunkX, int chunkZ)
	{
		return this.level.getChunk(chunkX, chunkZ);
	}
	
	// From BlockAndTintGetter
	
	@Override
	public float getShade(Direction direction, boolean shade)
	{
		return this.level.getShade(direction, shade);
	}
	
	@Override
	public LevelLightEngine getLightEngine()
	{
		return this.level.getLightEngine();
	}
	
	@Override
	public int getBlockTint(BlockPos blockPos, ColorResolver colorResolver)
	{
		return this.level.getBlockTint(blockPos, colorResolver);
	}
	
	// From BlockGetter
	
	@Nullable
	@Override
	public BlockEntity getBlockEntity(BlockPos pos)
	{
		return this.level.getBlockEntity(pos);
	}
	
	@Override
	public BlockState getBlockState(BlockPos pos)
	{
		return this.level.getBlockState(pos);
	}
	
	@Override
	public FluidState getFluidState(BlockPos pos)
	{
		return this.level.getFluidState(pos);
	}
	
	@Override
	public int getLightEmission(BlockPos pos)
	{
		return this.level.getLightEmission(pos);
	}
	
	@Override
	public int getMaxLightLevel()
	{
		return this.level.getMaxLightLevel();
	}
	
	// From LevelHeightAccessor
	
	@Override
	public int getHeight()
	{
		return this.level.getHeight();
	}
	
	@Override
	public int getMinBuildHeight()
	{
		return this.level.getMinBuildHeight();
	}
	
	// From CollisionGetter
	
	@Override
	public WorldBorder getWorldBorder()
	{
		return this.level.getWorldBorder();
	}
	
	@Nullable
	@Override
	public BlockGetter getChunkForCollisions(int chunkX, int chunkZ)
	{
		return this.level.getChunkForCollisions(chunkX, chunkZ);
	}
	
	// From LevelSimulatedReader
	
	@Override
	public boolean isStateAtPosition(BlockPos pos, Predicate<BlockState> state)
	{
		return this.level.isStateAtPosition(pos, state);
	}
	
	@Override
	public boolean isFluidAtPosition(BlockPos pos, Predicate<FluidState> predicate)
	{
		return this.level.isFluidAtPosition(pos, predicate);
	}
	
	// From LevelWriter
	
	@Override
	public boolean setBlock(BlockPos pos, BlockState state, int flags, int recursionLeft)
	{
		return this.level.setBlock(pos, state, flags, recursionLeft);
	}
	
	@Override
	public boolean removeBlock(BlockPos pos, boolean isMoving)
	{
		return this.level.removeBlock(pos, isMoving);
	}
	
	@Override
	public boolean destroyBlock(BlockPos pos, boolean dropBlock, @Nullable Entity entity, int recursionLeft)
	{
		return this.level.destroyBlock(pos, dropBlock, entity, recursionLeft);
	}
	
	@Override
	public boolean addFreshEntity(Entity entity)
	{
		return this.level.addFreshEntity(entity);
	}
}