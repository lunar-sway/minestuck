package com.mraof.minestuck.world;

import com.mraof.minestuck.network.skaianet.SburbConnection;
import com.mraof.minestuck.network.skaianet.SkaianetHandler;
import com.mraof.minestuck.util.IdentifierHandler;
import com.mraof.minestuck.world.gen.ChunkProviderSkaia;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Biomes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.common.ModDimension;

import javax.annotation.Nullable;
import java.util.function.Function;

public class SkaiaDimension extends Dimension
{
	private final DimensionType type;
	
	public SkaiaDimension(DimensionType type)
	{
		this.type = type;
	}
	
	@Override
	protected void init()
	{
		this.hasSkyLight = true;
		//this.biomeProvider = new BiomeProviderSingle(Biomes.PLAINS);
	}
	
	@Override
	public IChunkGenerator createChunkGenerator()
	{
		return null;//new ChunkProviderSkaia(this.world, this.world.getSeed(), true);
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
		return true;
	}
	
	@Override
	public DimensionType getRespawnDimension(EntityPlayerMP player)
	{
		DimensionType dimOut;
		SburbConnection c = SkaianetHandler.getMainConnection(IdentifierHandler.encode(player), true);
		if(c == null || !c.enteredGame())
			dimOut = player.getSpawnDimension();	//Method outputs 0 when no spawn dimension is set, sending players to the overworld.
		else
		{
			dimOut = c.getClientDimension();
		}
		
		return dimOut;
	}
	
	@Override
	public SleepResult canSleepAt(EntityPlayer player, BlockPos pos)
	{
		return SleepResult.ALLOW;
	}
	
	@Override
	public DimensionType getType()
	{
		return type;
	}
	
	public static class Type extends ModDimension
	{
		@Override
		public Function<DimensionType, ? extends Dimension> getFactory()
		{
			return SkaiaDimension::new;
		}
	}
}