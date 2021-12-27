package com.mraof.minestuck.world;
/*
public class LandDimension extends Dimension
{
	private static final long GENERIC_BIG_PRIME = 661231563202688713L;
	
	@Override
	public long getSeed()
	{
		return super.getSeed() + getType().getId()*GENERIC_BIG_PRIME;
	}
	
	@Override
	public float calculateCelestialAngle(long worldTime, float partialTicks)
	{
		//Reverses the algorithm used to calculate the skylight float. Needed as the skylight is currently hardcoded to use celestial angle
		return (float) (Math.acos((properties.skylightBase - 0.5F) / 2) / (Math.PI * 2F));
	}
	
	@Nullable
	@Override
	public MusicTicker.MusicType getMusicType()
	{
		//A hack to make the vanilla music ticker behave as if the music type changes when entering/exiting lands
		// (matters for some timing-related behavior, even if we stop any vanilla music from playing in lands)
		return MusicTicker.MusicType.MENU;
	}
}*/