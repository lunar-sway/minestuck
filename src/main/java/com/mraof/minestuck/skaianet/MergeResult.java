package com.mraof.minestuck.skaianet;

public enum MergeResult	//TODO This isn't used for much else than exceptions. Replace usage with SkaianetException and when relevant factory functions
{
	ABLE("able", "Is able to merge"),
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