package com.mraof.minestuck.item.loot.functions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mraof.minestuck.item.IncompleteSburbCodeItem;
import com.mraof.minestuck.item.loot.MSLootTables;
import com.mraof.minestuck.util.MSTags;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;

/**
 * Adds random filled in code segments(blocks from the hieroglyphs block tag) to a sburb code item that is being created in the world.
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class SetSburbCodeFragments extends LootItemConditionalFunction
{
	public static final Codec<SetSburbCodeFragments> CODEC = RecordCodecBuilder.create(instance ->
			commonFields(instance).apply(instance, SetSburbCodeFragments::new));
	public SetSburbCodeFragments(List<LootItemCondition> conditionsIn)
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
	
	private Set<Block> pickRandomHieroglyphs(RandomSource random)
	{
		Optional<HolderSet.Named<Block>> hieroglyphs = BuiltInRegistries.BLOCK.getTag(MSTags.Blocks.GREEN_HIEROGLYPHS);
		
		if(hieroglyphs.isEmpty() || hieroglyphs.get().size() == 0)
			return Collections.emptySet();
		
		Set<Block> pickedBlocks = new HashSet<>();
		int numberOfIterations = Math.max(1, random.nextInt(Math.min(3, hieroglyphs.get().size())));
		
		for(int iterate = 0; iterate < numberOfIterations; iterate++) //up to two runs at filling the book with another fragment of code
		{
			hieroglyphs.get().getRandomElement(random)
					.ifPresent(holder -> pickedBlocks.add(holder.value()));
		}
		
		return pickedBlocks;
	}
	
	public static net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction.Builder<?> builder()
	{
		return simpleBuilder(SetSburbCodeFragments::new);
	}
}