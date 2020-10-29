package com.mraof.minestuck.advancements;

import net.minecraft.advancements.CriteriaTriggers;

public class MSCriteriaTriggers
{
	public static final EventTrigger SBURB_CONNECTION = new EventTrigger(EventTrigger.SBURB_CONNECTION_ID);
	public static final EventTrigger CRUXITE_ARTIFACT = new EventTrigger(EventTrigger.CRUXITE_ARTIFACT_ID);
	public static final EventTrigger MELON_OVERLOAD = new EventTrigger(EventTrigger.MELON_OVERLOAD_ID);
	public static final PunchDesignixTrigger PUNCH_DESIGNIX = new PunchDesignixTrigger();
	public static final CaptchalogueTrigger CAPTCHALOGUE = new CaptchalogueTrigger();
	public static final ChangeModusTrigger CHANGE_MODUS = new ChangeModusTrigger();
	public static final TreeModusRootTrigger TREE_MODUS_ROOT = new TreeModusRootTrigger();
	public static final ConsortItemTrigger CONSORT_ITEM = new ConsortItemTrigger();
	public static final ConsortTalkTrigger CONSORT_TALK = new ConsortTalkTrigger();
	
	/**
	 * Currently (1.15), this is not thread safe and need to be deferred
	 */
	public static void register()
	{
		CriteriaTriggers.register(SBURB_CONNECTION);
		CriteriaTriggers.register(CRUXITE_ARTIFACT);
		CriteriaTriggers.register(MELON_OVERLOAD);
		CriteriaTriggers.register(PUNCH_DESIGNIX);
		CriteriaTriggers.register(CAPTCHALOGUE);
		CriteriaTriggers.register(CHANGE_MODUS);
		CriteriaTriggers.register(TREE_MODUS_ROOT);
		CriteriaTriggers.register(CONSORT_ITEM);
		CriteriaTriggers.register(CONSORT_TALK);
	}
}