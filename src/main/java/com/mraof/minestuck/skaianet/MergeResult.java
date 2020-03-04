package com.mraof.minestuck.skaianet;

public enum MergeResult	//TODO This isn't used for much else than exceptions. Replace usage with SkaianetException and when relevant factory functions
{
	ABLE("able", "Is able to merge"),
	LOCKED("locked", "Session is locked"),
	GLOBAL_SESSION_FULL("global_session_full", "Global session is full"),
	CLIENT_SESSION_FULL("client_session_full", "Client session is full"),
	SERVER_SESSION_FULL("server_session_full", "Server session is full"),
	MERGED_SESSION_FULL("merged_session_full", "Merged session is full"),
	BOTH_CUSTOM("both_custom", "Both sessions are custom sessions"),
	GENERIC_FAIL("fail", "Merge failed");
	
	private final String key, message;
	
	MergeResult(String key, String message)
	{
		this.key = key;
		this.message = message;
	}
	
	SessionMergeException exception()
	{
		return new SessionMergeException(this, message);
	}
	
	public String translationKey()
	{
		return "minestuck.session.merge."+key;
	}
	
	public static class SessionMergeException extends Exception
	{
		private final MergeResult result;
		
		public SessionMergeException(MergeResult result, String message)
		{
			super(message);
			this.result = result;
		}
		
		public MergeResult getResult()
		{
			return result;
		}
	}
}