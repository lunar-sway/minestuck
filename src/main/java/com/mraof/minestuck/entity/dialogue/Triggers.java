package com.mraof.minestuck.entity.dialogue;

import com.mojang.serialization.MapCodec;
import com.mraof.minestuck.Minestuck;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredRegister;

import static com.mraof.minestuck.entity.dialogue.Trigger.*;

public final class Triggers
{
	public static final DeferredRegister<MapCodec<? extends Trigger>> REGISTER = DeferredRegister.create(Minestuck.id("dialogue_trigger"), Minestuck.MOD_ID);
	public static final Registry<MapCodec<? extends Trigger>> REGISTRY = REGISTER.makeRegistry(builder -> {});
	
	static
	{
		REGISTER.register("set_dialogue", () -> SetDialogue.CODEC);
		REGISTER.register("set_random_dialogue", () -> SetDialogueFromList.CODEC);
		REGISTER.register("set_player_dialogue", () -> SetPlayerDialogue.CODEC);
		REGISTER.register("open_consort_merchant_gui", () -> OpenConsortMerchantGui.CODEC);
		REGISTER.register("command", () -> Command.CODEC);
		REGISTER.register("take_item", () -> TakeItem.CODEC);
		REGISTER.register("take_matched_item", () -> TakeMatchedItem.CODEC);
		REGISTER.register("set_npc_item", () -> SetNPCItem.CODEC);
		REGISTER.register("set_npc_matched_item", () -> SetNPCMatchedItem.CODEC);
		REGISTER.register("give_item", () -> GiveItem.CODEC);
		REGISTER.register("give_from_loot_table", () -> GiveFromLootTable.CODEC);
		REGISTER.register("add_consort_reputation", () -> AddConsortReputation.CODEC);
		REGISTER.register("add_boondollars", () -> AddBoondollars.CODEC);
		REGISTER.register("add_echeladder", () -> AddEcheladderExperience.CODEC);
		REGISTER.register("go_to_block", () -> GoToBlock.CODEC);
		REGISTER.register("explode", () -> Explode.CODEC);
		REGISTER.register("set_flag", () -> SetFlag.CODEC);
		REGISTER.register("set_random_flag", () -> SetRandomFlag.CODEC);
	}
	
	public static Trigger takeItem(Item item)
	{
		return new TakeItem(item);
	}
	
	public static Trigger takeItem(Item item, int count)
	{
		return new TakeItem(item, count);
	}
	
	public static Trigger giveItem(Item item)
	{
		return new GiveItem(item);
	}
	
	public static Trigger giveItem(Item item, int count)
	{
		return new GiveItem(item, count);
	}
	
	public static Trigger setDialogue(ResourceLocation dialogue)
	{
		return new SetDialogue(dialogue);
	}
	
	public static Trigger setFlag(String flag, boolean isPlayerSpecific)
	{
		return new SetFlag(flag, isPlayerSpecific);
	}
}
