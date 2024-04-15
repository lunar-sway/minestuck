package com.mraof.minestuck.advancements;

import com.mraof.minestuck.Minestuck;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class MSCriteriaTriggers
{
	private static final Map<ResourceLocation, CriterionTrigger<?>> triggers = new HashMap<>();
	
	public static final EventTrigger SBURB_CONNECTION = registerLater("sburb_connection", new EventTrigger());
	public static final EventTrigger CRUXITE_ARTIFACT = registerLater("cruxite_artifact", new EventTrigger());
	public static final EventTrigger RETURN_NODE = registerLater("return_node", new EventTrigger());
	public static final EventTrigger MELON_OVERLOAD = registerLater("melon_overload", new EventTrigger());
	public static final EventTrigger BUY_OUT_SHOP = registerLater("buy_out_shop", new EventTrigger());
	
	public static final PunchDesignixTrigger PUNCH_DESIGNIX = registerLater("punch_designix", new PunchDesignixTrigger());
	public static final IntellibeamLaserstationTrigger INTELLIBEAM_LASERSTATION = registerLater("intellibeam_laserstation", new IntellibeamLaserstationTrigger());
	public static final CaptchalogueTrigger CAPTCHALOGUE = registerLater("captchalogue", new CaptchalogueTrigger());
	public static final ChangeModusTrigger CHANGE_MODUS = registerLater("change_modus", new ChangeModusTrigger());
	public static final TreeModusRootTrigger TREE_MODUS_ROOT = registerLater("tree_modus_root", new TreeModusRootTrigger());
	public static final EcheladderTrigger ECHELADDER = registerLater("echeladder", new EcheladderTrigger());
	public static final ConsortItemTrigger CONSORT_ITEM = registerLater("consort_item", new ConsortItemTrigger());
	public static final ConsortTalkTrigger CONSORT_TALK = registerLater("consort_talk", new ConsortTalkTrigger());
	
	private static <T extends CriterionTrigger<?>> T registerLater(String name, T trigger)
	{
		triggers.put(Minestuck.id(name), trigger);
		return trigger;
	}
	
	/**
	 * Currently (1.15), this is not thread safe and need to be deferred
	 */
	public static void register()
	{
		for(Map.Entry<ResourceLocation, CriterionTrigger<?>> entry : triggers.entrySet())
			CriteriaTriggers.register(entry.getKey().toString(), entry.getValue());
	}
}