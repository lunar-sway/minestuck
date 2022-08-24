package com.mraof.minestuck.item.crafting.alchemy;

import com.mraof.minestuck.skaianet.Session;
import com.mraof.minestuck.skaianet.SessionHandler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.mraof.minestuck.tileentity.machine.GristWidgetTileEntity;

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
		
		gristToSpill.spawnGristEntities(
				level,
				player.getX(), player.getY(), player.getZ(),
				level.random,
				entity -> entity.setDeltaMovement(entity.getDeltaMovement().multiply(1.5, 0.5, 1.5)),
				90,
				level.random.nextInt(6) > 0 ? 1 : 2
		);
	}
	
	/**
	 * this is our main function that adds grist to the gutter.
	 * it does this by checking the type (which is represented by value)
	 * and checking the amount (which is represented by amount
	 */
	@Override
	public GristGutter addGrist(GristType type, long amount)
	{
		if(type != null)
		{
			//this is a full gristset(which is essentially a grist inventory for things like the cache)
			//For super Overflow
			GristSet sOverflowGrist = new GristSet();
			long originalAmount = this.gristTypes.getOrDefault(type, 0L);
			long maximumAllowed = (long) (gutterTotal - getGutterCapacity() + originalAmount);
			
			
			this.gristTypes.compute(type, (key, value) ->
					value == null ? amount : value + amount);
			gutterTotal += amount;
			
			LOGGER.debug("Gutter after adding " + amount + " "
					+ type.getDisplayName().toString() + " grist:");
			LOGGER.debug("Total: " + getGutterTotal());
			
			for(GristType t : this.gristTypes.keySet())
			{
				LOGGER.debug(t.getDisplayName().toString() + ": " + gristTypes.get(t));
			}
			
			logGutter(type, amount);
			
			//this is essentially saying "if the gutter goes higher than the set capacity
			//then print "gutter has capped out
			//find the remainder of guttertotal and gutter capacity and set that to a super overflow
			//and then add the type and amount to that super overflow
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
	
	
	
	public GristGutter logGutter(GristType type, long amount){
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
this is how we take grist from the gutter and throw it into the player's cache
	 we use the xMover variable (comprised of the minimum of something or other)...
	 */
	public GristSet splice(int i)
	{
		//this is our splice-set inventory
		GristSet spliceSet = new GristSet();
		
		//we use I here instead of a value so that it's easier to work with in the onPlayerTickEvent class
		//which is where this function will be used primarily
		for(GristType t : this.gristTypes.keySet())
		{
			int xMover = (int) Math.min(gristTypes.get(t), i);
			spliceSet.addGrist(t, xMover);
			
			gutterTotal -= xMover;
		}
		return spliceSet;
	}
	
}