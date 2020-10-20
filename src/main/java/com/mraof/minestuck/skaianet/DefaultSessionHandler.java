package com.mraof.minestuck.skaianet;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.event.ConnectionCreatedEvent;
import com.mraof.minestuck.player.PlayerIdentifier;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import org.apache.commons.lang3.tuple.Pair;
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
	private final Map<String, Session> sessionsByName = new HashMap<>();
	
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
	
	DefaultSessionHandler(SkaianetHandler skaianetHandler, ListNBT list)
	{
		super(skaianetHandler);
		for(int i = 0; i < list.size(); i++)
			correctAndAddSession(Session.read(list.getCompound(i), skaianetHandler));
	}
	
	@Override
	void write(CompoundNBT compound)
	{
		ListNBT list = new ListNBT();
		
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
	Set<Session> getSessions()
	{
		return Collections.unmodifiableSet(sessions);
	}
	
	@Override
	Pair<Session, ConnectionCreatedEvent.SessionJoinType> tryGetSessionToAdd(PlayerIdentifier client, PlayerIdentifier server) throws MergeResult.SessionMergeException
	{
		return SessionMerger.getValidMergedSession(this, client, server);
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
	}
	
	private void correctAndAddSession(Session session)
	{
		if(session.isCustom() && sessionsByName.containsKey(session.name))
		{
			LOGGER.error("Found session with name that is already being used. Removing session name before adding.");
			session.name = null;
		}
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
			throw new IllegalStateException("Session has already been added: " + session.name);
		else if(session.isCustom() && sessionsByName.containsKey(session.name))
			throw new IllegalStateException("Session name is already in use: " + session.name);
		else if(getConnectionStream().anyMatch(session.connections::contains))
			throw new IllegalStateException("Session contained connections that have already been added: " + session.name);
		else
		{
			sessions.add(session);
			if(session.isCustom())
				sessionsByName.put(session.name, session);
		}
	}
	
	void handleSuccessfulMerge(Session s1, Session s2, Session result)
	{
		sessions.remove(s1);
		sessionsByName.remove(s1.name);
		sessions.remove(s2);
		sessionsByName.remove(s2.name);
		sessions.add(result);
		if(result.isCustom())
			sessionsByName.put(result.name, result);
		result.finishMergeOrSplit();
	}
	
	private void split(Session session)
	{
		List<Session> sessions = SessionMerger.splitSession(session);
		sessions.forEach(Session::checkIfCompleted);
		this.sessions.addAll(sessions);
		sessions.forEach(Session::finishMergeOrSplit);
		removeIfEmpty(session);
	}
	
	private void removeIfEmpty(Session session)
	{
		if(session.isEmpty())
		{
			sessions.remove(session);
			sessionsByName.remove(session.name);
		}
	}
}