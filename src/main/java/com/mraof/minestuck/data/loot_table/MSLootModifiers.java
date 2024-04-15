package com.mraof.minestuck.data.loot_table;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.item.MSItemTypes;
import com.mraof.minestuck.item.loot.GristLootModifier;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.data.GlobalLootModifierProvider;
import net.neoforged.neoforge.common.loot.CanToolPerformAction;

public class MSLootModifiers extends GlobalLootModifierProvider
{
	public MSLootModifiers(PackOutput output)
	{
		super(output, Minestuck.MOD_ID);
	}
	
	@Override
	protected void start()
	{
		add("grist_mining", new GristLootModifier(
				new LootItemCondition[]{new CanToolPerformAction(MSItemTypes.GRIST_HARVEST)}, 0.5F));
	}
}
