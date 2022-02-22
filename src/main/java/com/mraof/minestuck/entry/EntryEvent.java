package com.mraof.minestuck.entry;

import com.mraof.minestuck.skaianet.SburbConnection;
import com.mraof.minestuck.skaianet.SkaianetHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.DragonFireballEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

/**
 * Handles special events leading up to the player entering, that may motivate the player to hurry up with entry.
 */
public class EntryEvent
{
	private static final int FREQUENCY = 8;
	private static final int RADIUS = 30;
	
	public static void tick(MinecraftServer server)
	{
		if (server.overworld().getGameTime() % FREQUENCY == 0)
			SkaianetHandler.get(server).getConnectionsInEntry().forEach(connection -> handleConnection(connection, server));
	}
	
	private static void handleConnection(SburbConnection connection, MinecraftServer server)
	{
		GlobalPos pos = connection.getClientComputer().getPosForEditmode();
		ServerWorld level = server.getLevel(pos.dimension());
		if (level != null && level.isLoaded(pos.pos()))
		{
			// Spawn some kind of fireball in the sky near the computer
			Random rand = level.getRandom();
			
			double x = pos.pos().getX() + 0.5 + RADIUS * invertedPyramidDist(rand);
			double y = 256;
			double z = pos.pos().getZ() + 0.5 + RADIUS * invertedPyramidDist(rand);
			
			Entity entity;
			if (rand.nextFloat() < 0.95)
				entity = new FireballEntity(level, x, y, z, 0, -1, 0);
			else
				entity = new DragonFireballEntity(level, x, y, z, 0, -1, 0);
			level.addFreshEntity(entity);
		}
	}
	
	/**
	 * A random distribution in the range -1 to 1 that is less likely to be near 0 and more likely to be near -1 or 1.
	 */
	private static double invertedPyramidDist(Random rand)
	{
		double value = rand.nextDouble() - rand.nextDouble();
		if (value > 0)
			return 1 - value;
		else
			return -1 - value;
	}
}