package com.mraof.minestuck.editmode;

import com.mraof.minestuck.entity.EntityDecoy;
import com.mraof.minestuck.network.skaianet.SburbConnection;
import com.mraof.minestuck.util.IdentifierHandler.PlayerIdentifier;
import net.minecraft.entity.player.EntityPlayerMP;

/**
 * Data structure used by the server sided EditHandler
 * Contains the player, player decoy, and connection
 * involved in the editmode session that this class represents.
 * @author Kirderf1
 */
public class EditData
{
	
	EditData(EntityDecoy decoy, EntityPlayerMP player, SburbConnection c)
	{
		this.decoy = decoy;
		this.player = player;
		this.connection = c;
	}
	
	EntityDecoy decoy;
	
	SburbConnection connection;
	
	EntityPlayerMP player;
	
	/**
	 * @return a player identifier for the player at the receiving end of the connection
	 */
	public PlayerIdentifier getTarget()
	{
		return connection.getClientIdentifier();
	}
	
	/**
	 * @return the player that activated and is in editmode (not necessarily the server player of the connection)
	 */
	public EntityPlayerMP getEditor()
	{
		return player;
	}
	
	/**
	 * @return the decoy entity that took the editors place
	 */
	public EntityDecoy getDecoy()
	{
		return decoy;
	}
}