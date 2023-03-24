package com.mraof.minestuck.alchemy;

import com.mraof.minestuck.skaianet.Session;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

/**
 * A class that handles Grist overflow whenever you aqcuire too much grist.
 * @author Doro
 */
public class GristGutter
{
	private static final Logger LOGGER = LogManager.getLogger();
	public static final int GUTTER_CAPACITY = 10000;
	
	private final GristSet gristSet = new GristSet();
	public int gutterTotal = -1;
	
	public static double getGutterCapacity(Session session)
	{
		return (GUTTER_CAPACITY * session.getGutterMultiplier());
	}
	public long getGutterTotal()
	{
		if(gutterTotal < 0)
		{
			gutterTotal = 0;
			for(Map.Entry<GristType, Long> pair : gristSet.getMap().entrySet())
			{
				gutterTotal += pair.getValue();
			}
		}
		return gutterTotal;
	}
	public GristSet gristToSpill = new GristSet();
	public void spillGrist(Level level, Player player)
	{
	//isn't being used by anything, but when i delete it everything breaks
		gristToSpill.spawnGristEntities(level, player.getX(), player.getY(), player.getZ(), level.random, entity -> entity.setDeltaMovement(entity.getDeltaMovement().multiply(1.5, 0.5, 1.5)), 90, level.random.nextInt(6) > 0 ? 1 : 2);
	}
	
	public void addGrist(GristSet set, Session session)
	{
		for(GristAmount amount : set.getAmounts())
			this.addGrist(amount.getType(), amount.getAmount(), session);
	}
	
	public void addGrist(GristType type, long amount, Session session)
	{
		GristSet sOverflowGrist = new GristSet();//creates a new gristset called Super overflow
		long originalAmount = gristSet.getMap().getOrDefault(type, 0L);
		long maximumAllowed = (long) (gutterTotal - getGutterCapacity(session) + originalAmount);
		
		gristSet.getMap().compute(type, (key, value) -> value == null ? amount : value + amount);
		gutterTotal += amount;//adds grist to gutter
		
		//logger
		LOGGER.debug("Gutter after adding " + amount + " " + type.getDisplayName().toString() + " grist:");
		LOGGER.debug("Total: " + getGutterTotal());
		for(GristType t : gristSet.getMap().keySet())
		{
			LOGGER.debug(t.getDisplayName().toString() + ": " + gristSet.getMap().get(t));
		}
		logGutter(type, amount);
		
		//not used by anything currently
		if(gutterTotal > GUTTER_CAPACITY)
		{
			System.out.println("gutter has capped out");
			long sOverflowAmount = (long) (gutterTotal - getGutterCapacity(session));
			gristSet.getMap().put(type, maximumAllowed);
			sOverflowGrist.addGrist(type, sOverflowAmount);
			gristToSpill.addGrist(sOverflowGrist);
			gutterTotal -= sOverflowAmount;
		}
	}
	public GristGutter logGutter(GristType type, long amount)
	{
		LOGGER.debug("Gutter after adding " + amount + " " + type.getDisplayName().toString() + " grist:");
		LOGGER.debug("Total: " + getGutterTotal());
		
		for(GristType t : gristSet.getMap().keySet())
		{
			LOGGER.debug(t.getDisplayName().toString() + ": " + gristSet.getMap().get(t));
		}
		
		return this;
	}
	
	/**
	 * this is how we take grist from the gutter and throw it into the player's cache
	 */
	public GristSet splice(int i)
	{
		GristSet spliceSet = new GristSet();
		
		for(GristType t : gristSet.getMap().keySet())
		{
			long xMover = Math.max(Math.min(gristSet.getMap().get(t), i), 0);
			spliceSet.addGrist(t, xMover);//spliceSet calls addgrist with the amount and type specified
			
			gutterTotal -= xMover;//takes grist from the gutter
		}
		return spliceSet;//returns splice set to be used to distribute itself to the player's caches
	}
	
}