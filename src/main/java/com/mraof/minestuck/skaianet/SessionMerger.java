package com.mraof.minestuck.skaianet;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.player.PlayerIdentifier;

import java.util.Set;

class SessionMerger
{
	
	static Session getValidMergedSession(SessionHandler handler, PlayerIdentifier client, PlayerIdentifier server) throws MergeResult.SessionMergeException
	{
		if(handler.singleSession)
		{
			verifyCanAdd(handler.sessions.get(0), client, server, MergeResult.GLOBAL_SESSION_FULL);
			return handler.sessions.get(0);
		} else
		{
			Session cs = handler.getPlayerSession(client), ss = handler.getPlayerSession(server);
			if(cs != null && ss != null)
			{
				Session target = createMergedSession(cs, ss);
				verifyCanAdd(target, client, server, MergeResult.MERGED_SESSION_FULL);
				handler.handleSuccessfulMerge(cs, ss, target);
				return target;
			} else if(cs != null)
			{
				verifyCanAdd(cs, client, server, MergeResult.CLIENT_SESSION_FULL);
				return cs;
			} else if(ss != null)
			{
				verifyCanAdd(ss, client, server, MergeResult.SERVER_SESSION_FULL);
				return ss;
			} else
			{
				Session session = new Session();
				verifyCanAdd(session, client, server, MergeResult.GENERIC_FAIL);
				handler.sessions.add(session);
				return session;
			}
		}
	}
	
	static Session mergedSessionFromAll(SessionHandler handler) throws MergeResult.SessionMergeException
	{
		Session session = new Session();
		for(Session other : handler.sessions)
			session.inheritFrom(other);
		
		return session;
	}
	
	private static void verifyCanAdd(Session target, PlayerIdentifier client, PlayerIdentifier server, MergeResult fullSessionResult) throws MergeResult.SessionMergeException
	{
		Set<PlayerIdentifier> players = target.getPlayerList();
		int size = players.size();
		if(!players.contains(client))
			size++;
		if(!players.contains(server))
			size++;
		
		if(target.locked && size != players.size())	//If the session is locked and we're trying to add a new player to it
			throw MergeResult.LOCKED.exception();
		
		if(MinestuckConfig.forceMaxSize && size > SessionHandler.maxSize)
			throw fullSessionResult.exception();
		
	}
	
	private static Session createMergedSession(Session s1, Session s2) throws MergeResult.SessionMergeException
	
	{
		Session mergedSession = new Session();
		mergedSession.inheritFrom(s1);
		mergedSession.inheritFrom(s2);
		return mergedSession;
	}
}