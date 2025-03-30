package com.mraof.minestuck.advancements;

import com.mraof.minestuck.Minestuck;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class MSCriteriaTriggers
{
	public static final DeferredRegister<CriterionTrigger<?>> REGISTER = DeferredRegister.create(BuiltInRegistries.TRIGGER_TYPES, Minestuck.MOD_ID);
	
	public static final Supplier<EventTrigger> SBURB_CONNECTION = REGISTER.register("sburb_connection", EventTrigger::new);
	public static final Supplier<EventTrigger> CRUXITE_ARTIFACT = REGISTER.register("cruxite_artifact", EventTrigger::new);
	public static final Supplier<EventTrigger> RETURN_NODE = REGISTER.register("return_node", EventTrigger::new);
	public static final Supplier<EventTrigger> MELON_OVERLOAD = REGISTER.register("melon_overload", EventTrigger::new);
	public static final Supplier<EventTrigger> BUY_OUT_SHOP = REGISTER.register("buy_out_shop", EventTrigger::new);
	
	public static final Supplier<PunchDesignixTrigger> PUNCH_DESIGNIX = REGISTER.register("punch_designix", PunchDesignixTrigger::new);
	public static final Supplier<IntellibeamLaserstationTrigger> INTELLIBEAM_LASERSTATION = REGISTER.register("intellibeam_laserstation", IntellibeamLaserstationTrigger::new);
	public static final Supplier<CaptchalogueTrigger> CAPTCHALOGUE = REGISTER.register("captchalogue", CaptchalogueTrigger::new);
	public static final Supplier<ChangeModusTrigger> CHANGE_MODUS = REGISTER.register("change_modus", ChangeModusTrigger::new);
	public static final Supplier<TreeModusRootTrigger> TREE_MODUS_ROOT = REGISTER.register("tree_modus_root", TreeModusRootTrigger::new);
	public static final Supplier<EcheladderTrigger> ECHELADDER = REGISTER.register("echeladder", EcheladderTrigger::new);
	public static final Supplier<ConsortItemTrigger> CONSORT_ITEM = REGISTER.register("consort_item", ConsortItemTrigger::new);
	public static final Supplier<ConsortTalkTrigger> CONSORT_TALK = REGISTER.register("consort_talk", ConsortTalkTrigger::new);
}