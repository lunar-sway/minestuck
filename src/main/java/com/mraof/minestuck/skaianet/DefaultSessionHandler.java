package com.mraof.minestuck.skaianet;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.player.PlayerIdentifier;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

/**
 * The standard implementation of SessionHandler which allows multiple sessions
 */
public final class DefaultSessionHandler extends SessionHandler
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	/**
	 * An array list of the current worlds sessions.
	 */
	private final Set<Session> sessions = new HashSet<>();
	
	DefaultSessionHandler(SkaianetHandler skaianetHandler)
	{
		super(skaianetHandler);
	}
	
	DefaultSessionHandler(SkaianetHandler skaianetHandler, Session globalSession)
	{
		this(skaianetHandler);
		addNewSession(globalSession);
		split(globalSession);
	}
	
	DefaultSessionHandler(SkaianetHandler skaianetHandler, ListTag list)
	{
		super(skaianetHandler);
		for(int i = 0; i < list.size(); i++)
			correctAndAddSession(Session.read(list.getCompound(i), skaianetHandler));
	}
	
	@Override
	void write(CompoundTag compound)
	{
		ListTag list = new ListTag();
		
		for(Session s : sessions)
			list.add(s.write());
		
		compound.put("sessions", list);
	}
	
	@Override
	SessionHandler getActual()
	{
		if(MinestuckConfig.SERVER.globalSession.get())
		{
			try
			{
				Session session = SessionMerger.mergedSessionFromAll(sessions);
				return new GlobalSessionHandler(skaianetHandler, session);
				
			} catch(MergeResult.SessionMergeException e)
			{
				LOGGER.warn("Not able to merge all sessions together! Reason: {}. Global session temporarily disabled for this time.", e.getMessage());
			}
		}
		return this;
	}
	
	@Override
	public Session getPlayerSession(PlayerIdentifier player)
	{
		for(Session s : sessions)
			if(s.containsPlayer(player))
				return s;
		return null;
	}
	
	@Override
	public Set<Session> getSessions()
	{
		return Collections.unmodifiableSet(sessions);
	}
	
	@Override
	Session prepareSessionFor(PlayerIdentifier... players) throws MergeResult.SessionMergeException
	{
		return SessionMerger.getValidMergedSession(this, players);
	}
	
	@Override
	void findOrCreateAndCall(PlayerIdentifier player, SkaianetException.SkaianetConsumer<Session> consumer) throws SkaianetException
	{
		Session session = getPlayerSession(player);
		if(session != null)
			consumer.consume(session);
		else
		{
			//When no previous session exists, add the session after the consumer call,
			// such that the session isn't added if predefine call fails
			session = new Session();
			consumer.consume(session);
			addNewSession(session);
		}
	}
	
	@Override
	void onConnectionChainBroken(Session session)
	{
		split(session);
		sessions.forEach(existingSession -> existingSession.checkIfCompleted(skaianetHandler));
	}
	
	private void correctAndAddSession(Session session)
	{
		try
		{
			addNewSession(session);
		} catch(RuntimeException e)
		{
			LOGGER.error("Failed to add session. This might lead to loss of data, so I hope you've got a backup! Reason \"{}\"", e.getMessage());
		}
	}
	
	void addNewSession(Session session)
	{
		if(sessions.contains(session))
			throw new IllegalStateException("Session has already been added");
		else if(getConnectionStream().anyMatch(session.connections::contains))
			throw new IllegalStateException("Session contained connections that have already been added");
		
		sessions.add(session);
	}
	
	void handleSuccessfulMerge(Set<Session> sessions, Session result)
	{
		for(Session session : sessions)
			this.sessions.remove(session);
		
		this.sessions.add(result);
	}
	
	private void split(Session session)
	{
		List<Session> sessions = SessionMerger.splitSession(session, skaianetHandler.activeConnections);
		this.sessions.addAll(sessions);
		removeIfEmpty(session);
	}
	
	private void removeIfEmpty(Session session)
	{
		if(session.isEmpty())
			sessions.remove(session);
	}
}
