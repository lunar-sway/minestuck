package com.mraof.minestuck.item.loot;

import com.google.gson.JsonObject;
import com.mraof.minestuck.alchemy.GristCostRecipe;
import com.mraof.minestuck.alchemy.GristSet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import org.apache.commons.compress.utils.Lists;

import javax.annotation.Nonnull;
import java.util.List;

public class GristLootModifier extends LootModifier
{
	private final float multiplier;
	public GristLootModifier(LootItemCondition[] conditionsIn, float multiplier)
	{
		super(conditionsIn);
		this.multiplier = multiplier;
	}
	
	@Nonnull
	@Override
	protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context)
	{
		if(context.hasParam(LootContextParams.ORIGIN))
		{
			Vec3 pos = context.getParam(LootContextParams.ORIGIN);
			List<ItemStack> remainingLoot = Lists.newArrayList();
			
			for(ItemStack stack : generatedLoot)
			{
				GristSet cost = GristCostRecipe.findCostForItem(stack, null, true, context.getLevel());
				if(cost != null && multiplier != 1)
					cost = new GristSet(cost).scale(multiplier, false);
				
				if(cost != null && !cost.isEmpty())
					cost.spawnGristEntities(context.getLevel(), pos.x, pos.y, pos.z,
							context.getRandom(), entity -> entity.setDeltaMovement(entity.getDeltaMovement().multiply(0.5, 0.5, 0.5)));
				else
					remainingLoot.add(stack);
			}
			return remainingLoot;
		}
		return generatedLoot;
	}
	
	public static class Serializer extends GlobalLootModifierSerializer<GristLootModifier>
	{
		@Override
		public GristLootModifier read(ResourceLocation location, JsonObject object, LootItemCondition[] lootConditions)
		{
			return new GristLootModifier(lootConditions, GsonHelper.getAsFloat(object, "multiplier"));
		}
		
		@Override
		public JsonObject write(GristLootModifier instance)
		{
			JsonObject json = makeConditions(instance.conditions);
			json.addProperty("multiplier", instance.multiplier);
			return json;
		}
	}
}
