package com.mraof.minestuck.world;

import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.skaianet.SburbConnection;
import com.mraof.minestuck.skaianet.SkaianetHandler;
import com.mraof.minestuck.world.biome.MSBiomes;
import com.mraof.minestuck.world.gen.MSWorldGenTypes;
import com.mraof.minestuck.world.gen.SkaiaGenSettings;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.biome.provider.BiomeProviderType;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraftforge.common.ModDimension;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.BiFunction;

public class SkaiaDimension extends Dimension
{
	public SkaiaDimension(World worldIn, DimensionType typeIn)
	{
		super(worldIn, typeIn, 0.0F);
	}
	
	@Override
	public ChunkGenerator<?> createChunkGenerator()
	{
		SkaiaGenSettings settings = MSWorldGenTypes.SKAIA.createSettings();
		settings.setDefaultBlock(Blocks.STONE.getDefaultState());
		settings.setDefaultFluid(Blocks.AIR.getDefaultState());
		return MSWorldGenTypes.SKAIA.create(this.world, BiomeProviderType.FIXED.create(BiomeProviderType.FIXED.createSettings(this.world.getWorldInfo()).setBiome(MSBiomes.SKAIA)), settings);
	}
	
	@Override
	public boolean isDaytime()
	{
		return true;
	}
	
	@Override
	public float calculateCelestialAngle(long par1, float par3)
	{
		return 1.0F;
	}
	
	@Override
	public boolean canRespawnHere()
	{
		return false;
	}
	
	@Nullable
	@Override
	public BlockPos findSpawn(ChunkPos p_206920_1_, boolean checkValid)
	{
		return null;
	}
	
	@Nullable
	@Override
	public BlockPos findSpawn(int p_206921_1_, int p_206921_2_, boolean checkValid)
	{
		return null;
	}
	
	@Override
	public boolean isSurfaceWorld()
	{
		return false;
	}
	
	@Override
	public Vec3d getFogColor(float p_76562_1_, float p_76562_2_)
	{
		return new Vec3d(0.8, 0.8, 1.0);
	}
	
	@Override
	public boolean doesXZShowFog(int x, int z)
	{
		return false;
	}
	
	
	@Override
	public DimensionType getRespawnDimension(ServerPlayerEntity player)
	{
		DimensionType dimOut;
		Optional<SburbConnection> c = SkaianetHandler.get(world.getServer()).getMainConnection(IdentifierHandler.encode(player), true);
		if(c.isPresent() && c.get().hasEntered())
			dimOut = c.get().getClientDimension();
		else
		{
			dimOut = player.getSpawnDimension();	//Method outputs 0 when no spawn dimension is set, sending players to the overworld.
		}
		
		return dimOut;
	}
	
	
	@Override
	public SleepResult canSleepAt(PlayerEntity player, BlockPos pos)
	{
		return SleepResult.ALLOW;
	}
	
	public static class Type extends ModDimension
	{
		@Override
		public BiFunction<World, DimensionType, ? extends Dimension> getFactory()
		{
			return SkaiaDimension::new;
		}
	}
}