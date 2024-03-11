package com.mraof.minestuck.entity.dialogue;

import com.mojang.serialization.Codec;
import com.mraof.minestuck.Minestuck;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

import java.util.function.Supplier;

public final class Conditions
{
	public static final DeferredRegister<Codec<? extends Condition>> REGISTER = DeferredRegister.create(new ResourceLocation(Minestuck.MOD_ID, "dialogue_condition"), Minestuck.MOD_ID);
	public static final Supplier<IForgeRegistry<Codec<? extends Condition>>> REGISTRY = REGISTER.makeRegistry(() -> new RegistryBuilder<Codec<? extends Condition>>().disableSaving().disableSync());
	
	static {
		REGISTER.register("always_true", () -> Condition.AlwaysTrue.CODEC);
		REGISTER.register("list", () -> ListCondition.CODEC);
		REGISTER.register("consort", () -> Condition.IsConsort.CODEC);
		REGISTER.register("carapacian", () -> Condition.IsCarapacian.CODEC);
		REGISTER.register("entity_type", () -> Condition.IsEntityType.CODEC);
		REGISTER.register("one_of_entity_type", () -> Condition.IsOneOfEntityType.CODEC);
		REGISTER.register("in_land", () -> Condition.InAnyLand.CODEC);
		REGISTER.register("terrain_land_type", () -> Condition.InTerrainLandType.CODEC);
		REGISTER.register("terrain_land_type_tag", () -> Condition.InTerrainLandTypeTag.CODEC);
		REGISTER.register("title_land_type", () -> Condition.InTitleLandType.CODEC);
		REGISTER.register("title_land_type_tag", () -> Condition.InTitleLandTypeTag.CODEC);
		REGISTER.register("player_item", () -> Condition.PlayerHasItem.CODEC);
		REGISTER.register("player_class", () -> Condition.PlayerIsClass.CODEC);
		REGISTER.register("player_aspect", () -> Condition.PlayerIsAspect.CODEC);
		REGISTER.register("player_reputation", () -> Condition.PlayerHasReputation.CODEC);
		REGISTER.register("player_boondollars", () -> Condition.PlayerHasBoondollars.CODEC);
	}
}
