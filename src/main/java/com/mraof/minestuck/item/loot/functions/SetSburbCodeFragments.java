package com.mraof.minestuck.item.loot.functions;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.mraof.minestuck.item.IncompleteSburbCodeItem;
import com.mraof.minestuck.item.loot.MSLootTables;
import com.mraof.minestuck.util.MSTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.tags.ITag;

import java.util.*;

/**
 * Adds random filled in code segments(blocks from the hieroglyphs block tag) to a sburb code item that is being created in the world.
 */
public class SetSburbCodeFragments extends LootItemConditionalFunction
{
	public SetSburbCodeFragments(LootItemCondition[] conditionsIn)
	{
		super(conditionsIn);
	}
	
	@Override
	public LootItemFunctionType getType()
	{
		return MSLootTables.SET_SBURB_CODE_FRAGMENT_FUNCTION.get();
	}
	
	@Override
	protected ItemStack run(ItemStack stack, LootContext context)
	{
		return IncompleteSburbCodeItem.setRecordedInfo(stack, pickRandomHieroglyphs(context.getRandom()));
	}
	
	private Set<Block> pickRandomHieroglyphs(Random random)
	{
		ITag<Block> hieroglyphs = Objects.requireNonNull(ForgeRegistries.BLOCKS.tags()).getTag(MSTags.Blocks.GREEN_HIEROGLYPHS);
		
		if(hieroglyphs.isEmpty())
			return Collections.emptySet();
		
		Set<Block> pickedBlocks = new HashSet<>();
		int numberOfIterations = Math.max(1, random.nextInt(Math.min(3, hieroglyphs.size())));
		
		for(int iterate = 0; iterate < numberOfIterations; iterate++) //up to two runs at filling the book with another fragment of code
		{
			hieroglyphs.getRandomElement(random)
					.ifPresent(pickedBlocks::add);
		}
		
		return pickedBlocks;
	}
	
	public static Builder<?> builder()
	{
		return simpleBuilder(SetSburbCodeFragments::new);
	}
	
	public static class Serializer extends LootItemConditionalFunction.Serializer<SetSburbCodeFragments>
	{
		public void serialize(JsonObject object, SetSburbCodeFragments function, JsonSerializationContext context)
		{
		}
		
		public SetSburbCodeFragments deserialize(JsonObject object, JsonDeserializationContext context, LootItemCondition[] conditionsIn)
		{
			return new SetSburbCodeFragments(conditionsIn);
		}
	}
}