package com.mraof.minestuck.entity.consort;

public enum EnumConsort
{
	SALAMANDER(EntitySalamander.class),
	TURTLE(EntityTurtle.class),
	NAKAGATOR(EntityNakagator.class),
	IGUANA(EntityIguana.class);
	
	private Class<? extends EntityConsort> consortClass;
	
	private EnumConsort(Class<? extends EntityConsort> consort)
	{
		consortClass = consort;
	}
	
	public boolean isConsort(EntityConsort consort)
	{
		return consortClass.isInstance(consort);
	}
}