package com.mraof.minestuck.item.loot.functions;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.mraof.minestuck.item.SburbCodeItem;
import com.mraof.minestuck.item.loot.MSLootTables;
import com.mraof.minestuck.util.MSTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
		return SburbCodeItem.setRecordedInfo(stack, getRandomList());
	}
	
	private List<Block> getRandomList()
	{
		List<Block> completeList = MSTags.getBlocksFromTag(MSTags.Blocks.GREEN_HIEROGLYPHS);
		List<Block> pickedList = new ArrayList<>();
		if(!completeList.isEmpty())
		{
			Random random = new Random();
			int numberOfIterations = Math.max(1, random.nextInt(Math.min(3, completeList.size())));
			
			for(int iterate = 0; iterate < numberOfIterations; iterate++) //up to two runs at filling the book with another fragment of code
			{
				pickedList.add(completeList.get(random.nextInt(completeList.size()))); //random element from the full list of blocks
			}
		}
		
		return pickedList;
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