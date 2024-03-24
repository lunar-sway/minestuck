package com.mraof.minestuck.entry;

import com.mraof.minestuck.skaianet.ActiveConnection;
import com.mraof.minestuck.skaianet.SburbConnections;
import com.mraof.minestuck.skaianet.SburbPlayerData;
import net.minecraft.core.GlobalPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;

import java.util.Objects;

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
		{
			SburbConnections connections = SburbConnections.get(server);
			connections.activeConnections().filter(connection ->
					connections.hasPrimaryConnectionForClient(connection.client())
							&& !SburbPlayerData.get(connection.client(), server).hasEntered()).forEach(connection -> handleConnection(connection, server));
		}
	}
	
	private static void handleConnection(ActiveConnection connection, MinecraftServer server)
	{
		GlobalPos pos = connection.clientComputer().getPosForEditmode();
		ServerLevel level = server.getLevel(pos.dimension());
		if (level != null && level.isLoaded(pos.pos()))
		{
			// Spawn some kind of fireball in the sky near the computer
			RandomSource rand = level.getRandom();
			
			double x = pos.pos().getX() + 0.5 + RADIUS * invertedPyramidDist(rand);
			double y = 256;
			double z = pos.pos().getZ() + 0.5 + RADIUS * invertedPyramidDist(rand);
			
			AbstractHurtingProjectile entity;
			if (rand.nextFloat() < 0.95)
				entity = EntityType.FIREBALL.create(level);
			else
				entity = EntityType.DRAGON_FIREBALL.create(level);
			Objects.requireNonNull(entity);
			entity.moveTo(x, y, z, entity.getYRot(), entity.getXRot());
			entity.xPower = 0;
			entity.yPower = -0.1D;
			entity.zPower = 0;
			
			level.addFreshEntity(entity);
		}
	}
	
	/**
	 * A random distribution in the range -1 to 1 that is less likely to be near 0 and more likely to be near -1 or 1.
	 */
	private static double invertedPyramidDist(RandomSource rand)
	{
		double value = rand.nextDouble() - rand.nextDouble();
		if (value > 0)
			return 1 - value;
		else
			return -1 - value;
	}
}