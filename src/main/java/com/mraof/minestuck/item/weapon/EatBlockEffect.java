package com.mraof.minestuck.item.weapon;

import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.util.Debug;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;
import net.minecraft.potion.Effects;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.*;

import java.util.*;

public class EatBlockEffect implements RightClickBlockEffect
{
	/*static List<Blocks> consumableBlocks = new List<Blocks>()
	{
	
	};
	
	public static Map<Blocks, int> consumableBlocks()
	{
		return Maps.newEnumMap(ImmutableMap.of());
	}*/
	
	public static Block viableBlock(int i)
	{
		Debug.debugf("viableBlock is checked");
		Block[] blocks = new Block[]{Blocks.MELON.getBlock(), Blocks.PUMPKIN.getBlock(), Blocks.HAY_BLOCK.getBlock(),
				Blocks.CARVED_PUMPKIN.getBlock(), Blocks.DRIED_KELP_BLOCK.getBlock(), Blocks.HONEY_BLOCK.getBlock(),
				Blocks.WHEAT.getBlock(), Blocks.BEETROOTS.getBlock(), Blocks.CARROTS.getBlock(), Blocks.POTATOES.getBlock()};
		return blocks[i];
	}
	
	public static Block largeFoodSource(int i)
	{
		Block[] blocks = new Block[]{Blocks.PUMPKIN.getBlock(), Blocks.HAY_BLOCK.getBlock(), Blocks.CARVED_PUMPKIN.getBlock()};
		return blocks[i];
	}
	
	public static Block smallFoodSource(int i)
	{
		Block[] blocks = new Block[]{Blocks.MELON.getBlock(), Blocks.DRIED_KELP_BLOCK.getBlock(), Blocks.HONEY_BLOCK.getBlock(),
				Blocks.WHEAT.getBlock(), Blocks.BEETROOTS.getBlock(), Blocks.CARROTS.getBlock(), Blocks.POTATOES.getBlock()};
		return blocks[i];
	}
	
	public EatBlockEffect()
	{
	
	}
	
	@Override
	public ActionResultType onClick(ItemUseContext context)
	{
		return onItemUse(context);
	}
	
	EatBlockEffect EAT = (context) -> {
		World worldIn = context.getWorld();
		BlockPos pos = context.getPos();
		PlayerEntity player = context.getPlayer();
		ItemStack itemStack = context.getItem();
		Direction facing = context.getFace();
		boolean inside = context.isInside();
		
		if(!worldIn.isRemote && player != null)
		{
			BlockState state = worldIn.getBlockState(pos);
			
			if(inside && worldIn.getBlockState(player.getPosition()).getBlock() == Blocks.WATER)
			{
				state = Blocks.WATER.getDefaultState();
				pos = player.getPosition();
			}
			
			if(worldIn.getBlockState(pos.offset(facing)).getBlock() == Blocks.WATER)
			{
				state = Blocks.WATER.getDefaultState();
				pos = pos.offset(facing);
			}
			
			if(worldIn.getBlockState(pos.offset(facing)).getBlock() == Blocks.AIR)
			{
				state = Blocks.AIR.getDefaultState();
				pos = pos.offset(facing);
			}
			
			if(state.getBlock() == Blocks.WATER && itemStack.getItem() == MSItems.SWONGE)
			{
				worldIn.setBlockState(pos, Blocks.AIR.getDefaultState());
				ItemStack newItem = new ItemStack(MSItems.WET_SWONGE, itemStack.getCount());
				newItem.setTag(itemStack.getTag());
				player.setHeldItem(context.getHand(), newItem);
				worldIn.playSound(null, pos, SoundEvents.BLOCK_WATER_AMBIENT, SoundCategory.BLOCKS, 1F, 1F);
				//worldIn.playEvent(2001, pos, Block.getStateId(Blocks.WATER.getDefaultState()));
				return ActionResultType.SUCCESS;
			} else if(state.getBlock() == Blocks.AIR && itemStack.getItem() == MSItems.WET_SWONGE)
			{
				worldIn.setBlockState(pos, Blocks.WATER.getDefaultState());
				ItemStack newItem = new ItemStack(MSItems.SWONGE, itemStack.getCount());
				newItem.setTag(itemStack.getTag());
				player.setHeldItem(context.getHand(), newItem);
				worldIn.playSound(null, pos, SoundEvents.BLOCK_WATER_AMBIENT, SoundCategory.BLOCKS, 1F, 1F);
				//worldIn.playEvent(2001, pos, Block.getStateId(Blocks.WATER.getDefaultState()));
				return ActionResultType.SUCCESS;
			}
		}
		return ActionResultType.PASS;
	};
	
	private static ActionResultType onItemUse(ItemUseContext context)
	{
		
		World worldIn = context.getWorld();
		BlockPos pos = context.getPos();
		PlayerEntity player = context.getPlayer();
		ItemStack itemStack = context.getItem();
		Direction facing = context.getFace();
		boolean inside = context.isInside();
		
		if(!worldIn.isRemote && player != null)
		{
			Debug.debugf("world is not remote");
			//List<Blocks> consumableBlocks = {new Blocks.MELON};
			//Block[] consumableBlocks = new Block[]{new Block(Blocks.MELON.)};
			//HashMap<Block, HashSet<Block>> out = new HashMap<Block, HashSet<Block>>();
			
			//int a = 0;
			//int blockArrayLength = viableBlock(a).length;
			
			BlockState state = worldIn.getBlockState(pos);
			
			for(int i = 0; viableBlock(i) != null; i++)
			{
				Block viableBlock = viableBlock(i);
				
				if(inside && worldIn.getBlockState(player.getPosition()).getBlock() == viableBlock)
				{
					state = viableBlock.getDefaultState();
					pos = player.getPosition();
				}
				
				if(worldIn.getBlockState(pos.offset(facing)).getBlock() == viableBlock)
				{
					state = viableBlock.getDefaultState();
					pos = pos.offset(facing);
				}
				
				if(state.getBlock() == viableBlock)
				{
					Debug.debugf("inner loop");
					for(int a = 0; smallFoodSource(a) != null; a++)
					{
						if(smallFoodSource(a) == state.getBlock())
						{
							worldIn.setBlockState(pos, Blocks.AIR.getDefaultState());
							worldIn.playSound(null, pos, SoundEvents.ENTITY_GENERIC_EAT, SoundCategory.PLAYERS, 1F, 1F);
							context.getItem().damageItem(1, player, (playerEntity) -> playerEntity.sendBreakAnimation(Hand.MAIN_HAND));
							player.addPotionEffect(new EffectInstance(Effects.SATURATION, 1, 2));
							return ActionResultType.SUCCESS;
						}
					}
					
					for(int a = 0; largeFoodSource(a) != null; a++)
					{
						if(largeFoodSource(a) == state.getBlock())
						{
							worldIn.setBlockState(pos, Blocks.AIR.getDefaultState());
							worldIn.playSound(null, pos, SoundEvents.ENTITY_GENERIC_EAT, SoundCategory.PLAYERS, 1F, 1F);
							context.getItem().damageItem(1, player, (playerEntity) -> playerEntity.sendBreakAnimation(Hand.MAIN_HAND));
							player.addPotionEffect(new EffectInstance(Effects.SATURATION, 1, 5));
							return ActionResultType.SUCCESS;
						}
					}
					//LootContext lootContext =;
					
					//LootContext lootcontext = builder.withParameter(LootParameters.BLOCK_STATE, state).build(LootParameterSets.BLOCK);
					//state.getDrops();
					//worldIn.addBlockEvent();
					
					/*RayTraceResult rayTraceResult = player.pick(context.getHitVec().distanceTo(player.getPositionVec()), 1F, false);
					viableBlock.getPickBlock(state, rayTraceResult, worldIn, pos, player);
					
					ResourceLocation lootTable = viableBlock.getLootTable();
					if (lootTable != LootTables.EMPTY) {
						LootTable.Builder loottable$builder = this.lootTables.remove(resourcelocation);
						if (loottable$builder == null) {
							throw new IllegalStateException(String.format("Missing loottable '%s' for '%s'", resourcelocation, Registry.BLOCK.getKey(block)));
						}
						
						p_accept_1_.accept(resourcelocation, loottable$builder);
					}*/
					
					
				}
			}
		}
		return ActionResultType.PASS;
	}
}
