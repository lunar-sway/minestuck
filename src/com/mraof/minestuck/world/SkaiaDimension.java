package com.mraof.minestuck.world;

import com.mraof.minestuck.network.skaianet.SburbConnection;
import com.mraof.minestuck.network.skaianet.SkaianetHandler;
import com.mraof.minestuck.util.IdentifierHandler;
import com.mraof.minestuck.world.gen.ChunkProviderSkaia;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Biomes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.common.ModDimension;

import java.util.function.Function;

public class SkaiaDimension extends Dimension
{
	private final DimensionType type;
	
	public SkaiaDimension(DimensionType type)
	{
		this.type = type;
	}
	
	@Override
	public IChunkGenerator createChunkGenerator()
	{
		return new ChunkProviderSkaia(this.world, this.world.getSeed(), true);
	}
	
	@Override
	public boolean isDaytime()
	{
		return true;
	}
	
	@Override
	protected void init()
	{
		this.hasSkyLight = true;
		this.biomeProvider = new BiomeProviderSingle(Biomes.PLAINS);
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