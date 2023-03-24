package com.mraof.minestuck.alchemy;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.player.PlayerData;
import com.mraof.minestuck.player.PlayerIdentifier;
import com.mraof.minestuck.player.PlayerSavedData;
import com.mraof.minestuck.skaianet.Session;
import com.mraof.minestuck.skaianet.SessionHandler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * A class that handles Grist overflow whenever you aqcuire too much grist.
 * @author Doro
 */
@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus=Mod.EventBusSubscriber.Bus.FORGE)
public class GristGutter
{
	public static final int GUTTER_CAPACITY = 10000;
	
	private final GristSet gristSet = new GristSet();
	private long gutterTotal = 0;
	private double gutterMultiplier = 1;
	
	public long getGutterCapacity()
	{
		return (long) (GUTTER_CAPACITY * gutterMultiplier);
	}
	public long getGutterTotal()
	{
		return gutterTotal;
	}
	
	public void increaseGutterMultiplier(double amount)
	{
		this.gutterMultiplier += amount;
	}
	public double getGutterMultiplier()
	{
		return gutterMultiplier;
	}
	
	public void addGristFrom(GristSet set)
	{
		for(GristAmount amount : set.getAmounts())
		{
			GristType type = amount.getType();
			long maximumAllowed = getGutterCapacity() - gutterTotal;
			
			if(maximumAllowed <= 0)
				return;
			
			long amountToAdd = Math.min(maximumAllowed, amount.getAmount());
			set.addGrist(type, -amountToAdd);
			this.addGristInternal(type, amountToAdd);
		}
	}
	
	/**
	 * The grist set is currently only modified here,
	 * which lets us be certain that gutterTotal is accurate.
	 */
	private void addGristInternal(GristType type, long amount)
	{
		this.gristSet.addGrist(type, amount);
		this.gutterTotal += amount;
	}
	
	/**
	 * this is how we take grist from the gutter and throw it into the player's cache
	 */
	public GristSet splice(int i)
	{
		GristSet spliceSet = new GristSet();
		
		for(GristAmount gristAmount : gristSet.getAmounts())
		{
			long xMover = Math.max(Math.min(gristAmount.getAmount(), i), 0);
			spliceSet.addGrist(gristAmount.getType(), xMover);
			
			this.addGristInternal(gristAmount.getType(), -xMover);
		}
		return spliceSet;
	}
	
	@SubscribeEvent
	public static void onPlayerTickEvent(TickEvent.PlayerTickEvent event)
	{
		if(event.player instanceof ServerPlayer player && player.level.getGameTime() % 200 == 0)
		{
			PlayerIdentifier playerId = IdentifierHandler.encode(player);
			if(playerId != null)
				tickDistributionToPlayer(playerId, player.server);
		}
	}
	
	private static void tickDistributionToPlayer(PlayerIdentifier player, MinecraftServer server)
	{
		PlayerData data = PlayerSavedData.getData(player, server);
		
		Session session = SessionHandler.get(server).getPlayerSession(player);
		if(session == null)
			return;
		GristGutter gutter = session.getGristGutter();
		int gutterMultiplier = (int) gutter.getGutterMultiplier();
		int capacity = data.getEcheladder().getGristCapacity();
		int spliceAmount = (int) (capacity * Math.min((gutterMultiplier + 1.0), 1.0) / 20.0);
		
		GristSet rungGrist = GristHelper.increaseAndReturnExcess(data, gutter.splice(spliceAmount));
		gutter.addGristFrom(rungGrist);
		if(!rungGrist.isEmpty())
			throw new IllegalStateException("Failed to add back grist that couldn't be given to a player");
	}
}