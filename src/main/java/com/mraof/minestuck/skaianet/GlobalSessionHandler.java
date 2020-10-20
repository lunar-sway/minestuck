package com.mraof.minestuck.skaianet;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.event.ConnectionCreatedEvent;
import com.mraof.minestuck.player.PlayerIdentifier;
import net.minecraft.nbt.CompoundNBT;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;

/**
 * An implementation that only allows for a single, global session, which all players are assumed to be part of
 */
public final class GlobalSessionHandler extends SessionHandler
{
	private static final String GLOBAL_SESSION_NAME = "global";
	
	@Nonnull
	private final Session globalSession;
	
	GlobalSessionHandler(SkaianetHandler skaianetHandler, Session session)
	{
		super(skaianetHandler);
		session.name = GLOBAL_SESSION_NAME;
		globalSession = Objects.requireNonNull(session);
	}
	
	GlobalSessionHandler(SkaianetHandler skaianetHandler, CompoundNBT nbt)
	{
		this(skaianetHandler, Session.read(nbt, skaianetHandler));
	}
	
	@Override
	void write(CompoundNBT compound)
	{
		compound.put("session", globalSession.write());
	}
	
	@Override
	SessionHandler getActual()
	{
		if(!MinestuckConfig.SERVER.globalSession.get())
			return new DefaultSessionHandler(skaianetHandler, globalSession);
		else return this;
	}
	
	@Override
	public Session getPlayerSession(PlayerIdentifier player)
	{
		return globalSession;
	}
	
	@Override
	Set<Session> getSessions()
	{
		return Collections.singleton(globalSession);
	}
	
	@Override
	Pair<Session, ConnectionCreatedEvent.SessionJoinType> tryGetSessionToAdd(PlayerIdentifier client, PlayerIdentifier server) throws MergeResult.SessionMergeException
	{
		return Pair.of(SessionMerger.verifyCanAddToGlobal(globalSession, client, server), ConnectionCreatedEvent.SessionJoinType.JOIN);
	}
	
	@Override
	void findOrCreateAndCall(PlayerIdentifier player, SkaianetException.SkaianetConsumer<Session> consumer) throws SkaianetException
	{
		consumer.consume(globalSession);
	}
	
	@Override
	boolean doesSessionHaveMaxTier(Session session)
	{
		return false;
	}
}
