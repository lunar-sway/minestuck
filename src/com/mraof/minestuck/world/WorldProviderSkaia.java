package com.mraof.minestuck.world;

import com.mraof.minestuck.network.skaianet.SburbConnection;
import com.mraof.minestuck.network.skaianet.SkaianetHandler;
import com.mraof.minestuck.util.IdentifierHandler;
import com.mraof.minestuck.world.gen.ChunkProviderSkaia;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Biomes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldProvider.WorldSleepResult;
import net.minecraft.world.biome.BiomeProviderSingle;
import net.minecraft.world.gen.IChunkGenerator;

public class WorldProviderSkaia extends WorldProvider 
{
	
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
	public int getRespawnDimension(EntityPlayerMP player)
	{
		int dimOut = 0;
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
	public WorldSleepResult canSleepAt(EntityPlayer player, BlockPos pos)
	{
		return WorldSleepResult.ALLOW;
	}
	
	@Override
	public DimensionType getDimensionType()
	{
		return MinestuckDimensionHandler.skaiaDimensionType;
	}
	
	@Override
	public void onPlayerAdded(EntityPlayerMP player)
	{
		int centerX = ((int)player.posX) >> 4;
		int centerZ = ((int)player.posZ) >> 4;
		for(int x = centerX - 1; x <= centerX + 1; x++)
			for(int z = centerZ - 1; z <= centerZ + 1; z++)
				this.world.getChunkProvider().provideChunk(x, z);
	}
}