package com.mraof.minestuck.skaianet;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.player.PlayerIdentifier;
import net.minecraft.nbt.CompoundTag;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;

/**
 * An implementation that only allows for a single, global session, which all players are assumed to be part of
 */
public final class GlobalSessionHandler extends SessionHandler
{
	@Nonnull
	private final Session globalSession;
	
	GlobalSessionHandler(SkaianetHandler skaianetHandler, Session session)
	{
		super(skaianetHandler);
		globalSession = Objects.requireNonNull(session);
	}
	
	GlobalSessionHandler(SkaianetHandler skaianetHandler, CompoundTag nbt)
	{
		this(skaianetHandler, Session.read(nbt, skaianetHandler));
	}
	
	@Override
	void write(CompoundTag compound)
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
	public Set<Session> getSessions()
	{
		return Collections.singleton(globalSession);
	}
	
	@Override
	Session prepareSessionFor(PlayerIdentifier... players) throws MergeResult.SessionMergeException
	{
		return SessionMerger.verifyCanAddToGlobal(globalSession, players);
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
