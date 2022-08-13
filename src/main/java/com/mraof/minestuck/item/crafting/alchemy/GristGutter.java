package com.mraof.minestuck.item.crafting.alchemy;

import com.mraof.minestuck.entity.item.GristEntity;
import com.mraof.minestuck.player.PlayerIdentifier;
import net.minecraft.entity.player.PlayerEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.minecraft.world.World;
import org.apache.logging.log4j.spi.LoggerRegistry;
import com.mraof.minestuck.item.crafting.alchemy.GristType;
import com.mraof.minestuck.item.crafting.alchemy.GristHelper;


public class GristGutter extends GristSet
{
	public int gutterTotal = -1;
	
	public static final int GUTTER_CAPACITY = 1000;

	private static final Logger LOGGER = LogManager.getLogger();
	
	/**
	 * the original intent of this code was to acquire the gutter total for logging purposes
	 * however for some reason we're not exactly sure to, the part of the gutter this was designed to work with
	 * just kind of ended up working anyway
	 * i'm not entirely sure at all how this works honestly
	 * */
	 public long getGutterTotal()
	{
		if(gutterTotal < 0)
		{
			//TODO: Recalculate total
			// Then assign the newly-calculated total to gutterTotal.
		}
		return gutterTotal;
	}
	//todo: what happens to grist gutter super overflow
	

	public GristSet gristToSpill = new GristSet();
	
	public void spillGrist(World world, PlayerEntity player)
	{
		gristToSpill.spawnGristEntities(
				world,
				player.getX(), player.getY(), player.getZ(),
				world.random,
				entity -> entity.setDeltaMovement(entity.getDeltaMovement().multiply(1.5, 0.5, 1.5)),
				90,
				world.random.nextInt(6) > 0 ? 1 : 2
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
			long maximumAllowed = gutterTotal - GUTTER_CAPACITY + originalAmount;
			
			
			this.gristTypes.compute(type, (key, value) ->
					value == null ? amount : value + amount);
			gutterTotal += amount;
			
			//this is essentially saying "if the gutter goes higher than the set capacity
			//then print "gutter has capped out
			//find the remainder of guttertotal and gutter capacity and set that to a super overflow
			//and then add the type and amount to that super overflow
			if(gutterTotal > GUTTER_CAPACITY)
			{
				System.out.println("gutter has capped out");
				long sOverflowAmount = gutterTotal - GUTTER_CAPACITY;
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
		 we're using a simple logger that'll tell us how much grist is going into the gutter by updating us on
		 the current gutter total, followed by the "value" (which just means the type of grist getting added)
		 */
		
	LOGGER.debug("Gutter before adding " + amount + " "
			+ type.getDisplayName().getString() + " grist:");
	LOGGER.debug("Total: " + getGutterTotal());
	LOGGER.debug("Value: " + getValue());
	
	
	for(GristType t : this.gristTypes.keySet())
	{
		LOGGER.debug(t.getDisplayName().toString() + ": " + gristTypes.get(t));
	}
		
		LOGGER.debug("Gutter AFTER adding " + amount + " "
				+ type.getDisplayName().toString() + " grist:");
		LOGGER.debug("Total: " + getGutterTotal());
		LOGGER.debug("Value: " + getValue());
		
		for(GristType t : this.gristTypes.keySet())
		{
			LOGGER.debug(t.getDisplayName().toString() + ": "
					+ gristTypes.get(t));
			
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