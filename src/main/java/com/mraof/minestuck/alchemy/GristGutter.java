package com.mraof.minestuck.alchemy;

import com.mraof.minestuck.skaianet.Session;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;


public class GristGutter extends GristSet
{
	private static Session session;
	
	private static MinecraftServer mcServer;
	
	public int gutterTotal = -1;
	
	public static final int GUTTER_CAPACITY = 10000;
	
	private static final Logger LOGGER = LogManager.getLogger();
	
	public void setSession(Session session)
	{
		this.session = session;
	}
	
	public void setMcServer(MinecraftServer mcServer)
	{
		this.mcServer = mcServer;
	}
	
	public double getGutterCapacity()
	{
		double sesPL = session.getSessionPowerlevel(mcServer);
		double gutcap = (GUTTER_CAPACITY * sesPL);
		return gutcap;
	}
	
	public long getGutterTotal()
	{
		if(gutterTotal < 0)
		{
			gutterTotal = 0;
			for(Map.Entry<GristType, Long> pair : gristTypes.entrySet())
			{
				gutterTotal += pair.getValue();
			}
		}
		return gutterTotal;
	}
	
	public GristSet gristToSpill = new GristSet();
	
	public void spillGrist(Level level, Player player)
	{
	//isn't being used by anything to my knowledge
		gristToSpill.spawnGristEntities(
				level,
				player.getX(), player.getY(), player.getZ(),
				level.random,
				entity -> entity.setDeltaMovement(entity.getDeltaMovement().multiply(1.5, 0.5, 1.5)),
				90,
				level.random.nextInt(6) > 0 ? 1 : 2
		);
	}
	

	@Override
	public GristGutter addGrist(GristType type, long amount)
	{
		if(type != null)
		{
			GristSet sOverflowGrist = new GristSet();//creates a new gristset called Super overflow
			long originalAmount = this.gristTypes.getOrDefault(type, 0L);
			long maximumAllowed = (long) (gutterTotal - getGutterCapacity() + originalAmount);
			
			
			this.gristTypes.compute(type, (key, value) ->
					value == null ? amount : value + amount);
			gutterTotal += amount;//adds grist to gutter
			
			
			LOGGER.debug("Gutter after adding " + amount + " "
					+ type.getDisplayName().toString() + " grist:");
			LOGGER.debug("Total: " + getGutterTotal());
			
			for(GristType t : this.gristTypes.keySet())
			{
				LOGGER.debug(t.getDisplayName().toString() + ": " + gristTypes.get(t));
			}
			
			logGutter(type, amount);
			
			//not used by anything currently
			if(gutterTotal > GUTTER_CAPACITY)
			{
				System.out.println("gutter has capped out");
				long sOverflowAmount = (long) (gutterTotal - getGutterCapacity());
				this.gristTypes.put(type, maximumAllowed);
				sOverflowGrist.addGrist(type, sOverflowAmount);
				gristToSpill.addGrist(sOverflowGrist);
				gutterTotal -= sOverflowAmount;
				
			}
		}
		return this;
	}
	
	
	public GristGutter logGutter(GristType type, long amount)
	{
		/**
		 * we're using a simple logger that'll tell us how much grist is in the gutter by updating us on
		 * the current gutter total
		 */
		
		LOGGER.debug("Gutter after adding " + amount + " "
				+ type.getDisplayName().toString() + " grist:");
		LOGGER.debug("Total: " + getGutterTotal());
		
		for(GristType t : this.gristTypes.keySet())
		{
			LOGGER.debug(t.getDisplayName().toString() + ": " + gristTypes.get(t));
		}
		
		return this;
	}
	
	
	/**
	 * this is how we take grist from the gutter and throw it into the player's cache
	 * we use the xMover variable (comprised of the minimum of something or other)...
	 */
	public GristSet splice(int i)
	{
		GristSet spliceSet = new GristSet();
		
		for(GristType t : this.gristTypes.keySet())
		{
			int xMover = (int) Math.min(gristTypes.get(t), i);
			spliceSet.addGrist(t, xMover);//spliceSet calls addgrist with the amount and type specified
			
			gutterTotal -= xMover;//takes grist from the gutter
		}
		return spliceSet;//returns splice set to be used to distribute itself to the player's caches
	}
	
}