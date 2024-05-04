package com.mraof.minestuck.item.loot;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mraof.minestuck.api.alchemy.GristSet;
import com.mraof.minestuck.api.alchemy.recipe.GristCostRecipe;
import com.mraof.minestuck.entity.item.GristEntity;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.loot.LootModifier;

import javax.annotation.Nonnull;

/**
 * A global loot modifier that when applied to loot,
 * will turn any items with a grist cost into grist entities,
 * which will be spawned at the loot location.
 * It is also possible set a multiplier for the grist cost produced,
 * which could be used to for example lower grist gains.
 */
public class GristLootModifier extends LootModifier
{
	public static final Codec<GristLootModifier> CODEC = RecordCodecBuilder.create(instance ->
			codecStart(instance)
			.and(Codec.FLOAT.fieldOf("multiplier").forGetter(modifier -> modifier.multiplier)
			).apply(instance, GristLootModifier::new));
	
	private final float multiplier;
	public GristLootModifier(LootItemCondition[] conditionsIn, float multiplier)
	{
		super(conditionsIn);
		this.multiplier = multiplier;
	}
	
	@Nonnull
	@Override
	protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context)
	{
		if(context.hasParam(LootContextParams.ORIGIN))
		{
			Vec3 pos = context.getParam(LootContextParams.ORIGIN);
			ObjectArrayList<ItemStack> remainingLoot = new ObjectArrayList<>();
			
			for(ItemStack stack : generatedLoot)
			{
				GristSet cost = GristCostRecipe.findCostForItem(stack, null, true, context.getLevel());
				if(cost != null && multiplier != 1)
					cost = cost.mutableCopy().scale(multiplier, false);
				
				if(cost != null && !cost.isEmpty())
					GristEntity.spawnGristEntities(cost, context.getLevel(), pos.x, pos.y, pos.z,
							context.getRandom(), entity -> entity.setDeltaMovement(entity.getDeltaMovement().multiply(0.5, 0.5, 0.5)));
				else
					remainingLoot.add(stack);
			}
			return remainingLoot;
		}
		return generatedLoot;
	}
	
	@Override
	public Codec<? extends GristLootModifier> codec()
	{
		return CODEC;
	}
}
