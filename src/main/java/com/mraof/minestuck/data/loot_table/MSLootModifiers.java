package com.mraof.minestuck.data.loot_table;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.item.MSItemTypes;
import com.mraof.minestuck.item.loot.GristLootModifier;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.data.GlobalLootModifierProvider;
import net.neoforged.neoforge.common.loot.CanItemPerformAbility;

import java.util.concurrent.CompletableFuture;

public class MSLootModifiers extends GlobalLootModifierProvider
{
	public MSLootModifiers(PackOutput output, CompletableFuture<HolderLookup.Provider> registries)
	{
		super(output, registries, Minestuck.MOD_ID);
	}
	
	@Override
	protected void start()
	{
		add("grist_mining", new GristLootModifier(
				new LootItemCondition[]{new CanItemPerformAbility(MSItemTypes.GRIST_HARVEST)}, 0.5F));
	}
}
