package com.mraof.minestuck.entity.dialogue;

import com.mojang.serialization.Codec;
import com.mraof.minestuck.Minestuck;
import net.minecraft.core.Registry;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class Triggers
{
	public static final DeferredRegister<Codec<? extends Trigger>> REGISTER = DeferredRegister.create(Minestuck.id("dialogue_trigger"), Minestuck.MOD_ID);
	public static final Registry<Codec<? extends Trigger>> REGISTRY = REGISTER.makeRegistry(builder -> {});
	
	static {
		REGISTER.register("set_dialogue", () -> Trigger.SetDialogue.CODEC);
		REGISTER.register("set_random_dialogue", () -> Trigger.SetDialogueFromList.CODEC);
		REGISTER.register("set_player_dialogue", () -> Trigger.SetPlayerDialogue.CODEC);
		REGISTER.register("open_consort_merchant_gui", () -> Trigger.OpenConsortMerchantGui.CODEC);
		REGISTER.register("command", () -> Trigger.Command.CODEC);
		REGISTER.register("take_item", () -> Trigger.TakeItem.CODEC);
		REGISTER.register("take_matched_item", () -> Trigger.TakeMatchedItem.CODEC);
		REGISTER.register("set_npc_item", () -> Trigger.SetNPCItem.CODEC);
		REGISTER.register("set_npc_matched_item", () -> Trigger.SetNPCMatchedItem.CODEC);
		REGISTER.register("give_item", () -> Trigger.GiveItem.CODEC);
		REGISTER.register("give_from_loot_table", () -> Trigger.GiveFromLootTable.CODEC);
		REGISTER.register("add_consort_reputation", () -> Trigger.AddConsortReputation.CODEC);
		REGISTER.register("add_boondollars", () -> Trigger.AddBoondollars.CODEC);
		REGISTER.register("explode", () -> Trigger.Explode.CODEC);
		REGISTER.register("set_flag", () -> Trigger.SetFlag.CODEC);
		REGISTER.register("set_random_flag", () -> Trigger.SetRandomFlag.CODEC);
	}
}
