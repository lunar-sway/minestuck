package com.mraof.minestuck.player;

import com.mojang.serialization.MapCodec;
import com.mraof.minestuck.Minestuck;
import net.minecraft.core.Registry;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class EcheladderExpSources
{
	public static final DeferredRegister<MapCodec<? extends EcheladderExpSource>> REGISTER = DeferredRegister.create(Minestuck.id("exp_source"), Minestuck.MOD_ID);
	public static final Registry<MapCodec<? extends EcheladderExpSource>> REGISTRY = REGISTER.makeRegistry(builder -> {});
	
	static
	{
		REGISTER.register("kill_entity", () -> EcheladderExpSource.KillEntity.CODEC);
		REGISTER.register("kill_entity_tag", () -> EcheladderExpSource.KillEntityTag.CODEC);
		REGISTER.register("advancement_earned", () -> EcheladderExpSource.AdvancementEarned.CODEC);
	}
}
