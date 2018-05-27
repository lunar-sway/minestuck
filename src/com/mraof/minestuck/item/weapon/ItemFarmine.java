package com.mraof.minestuck.item.weapon;

import com.mraof.minestuck.util.Pair;
import com.mraof.minestuck.util.MinestuckRandom;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Random;

public class ItemFarmine extends ItemWeapon
{
	private int radius;
	private int terminus;
	private HashSet<Block> farMineBaseAcceptables = new HashSet<Block>();
	private HashSet<Block> farMineForbiddenBlocks = new HashSet<Block>();
	private HashSet<Block> farMineForceAcceptable = new HashSet<Block>();
	
	public ItemFarmine(int maxUses, double damageVsEntity, double weaponSpeed, int enchantability, String name, int r, int t)
	{
		super(maxUses, damageVsEntity, weaponSpeed, enchantability, name);
		radius = r;
		terminus = t;
		if (radius != 0)
		{
			reinitializeFarMineLists();
		} else
		{
			terminus = Math.max(terminus, 1);
		}
	}
	
	public int getRadius()
	{
		return radius;
	}
	
	public ItemFarmine setTerminus(int r, int t)
	{
		radius = r;
		terminus = t;
		if (farMineBaseAcceptables.isEmpty())
		{
			reinitializeFarMineLists();
		}
		return this;
	}
	
	private void reinitializeFarMineLists()
	{
		//farMineForbiddenBlocks.add(Blocks.STONE);
		//farMineForbiddenBlocks.add(Blocks.GRASS);
		//farMineForbiddenBlocks.add(Blocks.NETHERRACK);
		//farMineForbiddenBlocks.add(Blocks.END_STONE);
		//farMineForbiddenBlocks.add(MinestuckBlocks.coloredDirt);
		
		//farMineBaseAcceptables.add(Blocks.STONE);
		
		//farMineBaseAcceptables.add(Blocks.GOLD_ORE);
		//farMineBaseAcceptables.add(Blocks.IRON_ORE);
		//farMineBaseAcceptables.add(Blocks.COAL_ORE);
		//farMineBaseAcceptables.add(Blocks.LAPIS_ORE);
		//farMineBaseAcceptables.add(Blocks.DIAMOND_ORE);
		//farMineBaseAcceptables.add(Blocks.REDSTONE_ORE);
		//farMineBaseAcceptables.add(Blocks.EMERALD_ORE);
		//farMineBaseAcceptables.add(Blocks.QUARTZ_ORE);
		//farMineBaseAcceptables.add(Blocks.SANDSTONE);
		//farMineBaseAcceptables.add(Blocks.QUARTZ_BLOCK);
		//farMineBaseAcceptables.add(Blocks.PRISMARINE);
		//farMineBaseAcceptables.add(Blocks.RED_SANDSTONE);
		//farMineBaseAcceptables.add(MinestuckBlocks.oreCruxite);
		
		farMineForceAcceptable.add(Blocks.LOG);
		farMineForceAcceptable.add(Blocks.LOG2);
	}
	
	
	@Override
	public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState blockState, BlockPos pos, EntityLivingBase playerIn)
	{
		Random r = new Random();
		Comparator<Pair> comparator = new PairedIntComparator();
		PriorityQueue<Pair> candidates = new PriorityQueue<Pair>(comparator);
		Block block = blockState.getBlock();
		Item drop = block.getItemDropped(blockState, MinestuckRandom.getRandom(), 0);
		int damageDrop = block.damageDropped(blockState);
		
		//If the tool can't harvest the block, or the player is sneaking,
		//or the tool doesn't farmine, or it's one of those blocks that breaks instantly, don't farmine.
		if (!canHarvestBlock(blockState, stack) || playerIn.isSneaking()
				|| radius == 0 || Math.abs(blockState.getBlockHardness(worldIn, pos)) < 0.000000001)
		{
			if (!isDamageable())
				return true;
			else
				return super.onBlockDestroyed(stack, worldIn, blockState, pos, playerIn);
		}
		//If the block is acceptable and there's no tool mismatch, farmine normally
		else if (getToolClasses(stack).contains(blockState.getBlock().getHarvestTool(blockState))
				&& (farMineForceAcceptable.contains(block) || !farMineForbiddenBlocks.contains(block)))
		{
			candidates.add(new Pair(pos, radius));
		}
		//Otherwise, cap out at a basic 3x3 area.
		else
		{
			candidates.add(new Pair(pos, Math.min(1, radius)));
		}
		
		//This set will contain all the blocks you'll want to break in the end.
		//This is used to determine if the number of blocks broken is too high.
		//If it is, it mines a 3x3 area instead.
		HashSet<BlockPos> blocksToBreak = new HashSet<BlockPos>();
		
		boolean flag = false;
		while (!candidates.isEmpty() && flag == false)
		{
			BlockPos curr = (BlockPos) candidates.peek().object1;
			int rad = (Integer) candidates.poll().object2;
			if (!blocksToBreak.contains(curr))
			{
				blocksToBreak.add(curr);
				
				if (rad != 0)
				{
					//Iterates across all blocks in a 3x3 cube centered on this block.
					for (int i = -1; i < 2; i++)
					{
						for (int j = -1; j < 2; j++)
						{
							for (int k = -1; k < 2; k++)
							{
								BlockPos newBlock = new BlockPos(curr.getX() + i, curr.getY() + j, curr.getZ() + k);
								IBlockState newstate = worldIn.getBlockState(newBlock);
								if (newstate.getBlock().equals(block)
										&& newstate.getBlock().getItemDropped(newstate, MinestuckRandom.getRandom(), 0) == drop
										&& newstate.getBlock().damageDropped(newstate) == damageDrop)
								{
									candidates.add(new Pair(newBlock, rad - 1));
								}
							}
						}
					}
				}
				
			}
			
			if (blocksToBreak.size() + 1 > stack.getMaxDamage() - stack.getItemDamage()
					|| blocksToBreak.size() + 1 > terminus)
			{
				flag = true;
			}
		}
		
		//If you passed the break limit, only harvest a 3x3 area.
		if (flag)
		{
			int damage = 1;
			for (int i = -1; i < 2; i++)
			{
				for (int j = -1; j < 2; j++)
				{
					for (int k = -1; k < 2; k++)
					{
						BlockPos newBlock = new BlockPos(pos.getX() + i, pos.getY() + j, pos.getZ() + k);
						IBlockState newstate = worldIn.getBlockState(newBlock);
						if (newstate.getBlock().equals(block)
								&& newstate.getBlock().getItemDropped(newstate, MinestuckRandom.getRandom(), 0) == drop
								&& newstate.getBlock().damageDropped(newstate) == damageDrop
								&& damage < stack.getMaxDamage() - stack.getItemDamage())
						{
							block.dropBlockAsItem(worldIn, pos, newstate, 0);
							worldIn.setBlockToAir(newBlock);
							damage++;
						}
					}
				}
			}
			if (isDamageable())
				stack.damageItem(damage, playerIn);
			
		} else    //Otherwise, break ALL the blocks!
		{
			for (BlockPos blockToBreak : blocksToBreak)
			{
				block.dropBlockAsItem(worldIn, pos, worldIn.getBlockState(blockToBreak), 0);
				worldIn.setBlockToAir(blockToBreak);
			}
			
			//We add 1 because that means the tool will always take at least 2 damage.
			//This is important because all ItemWeapons take at least 2 damage whenever it breaks a block.
			//This is because ItemWeapon extends ItemSword.
			if (isDamageable())
				stack.damageItem(blocksToBreak.size() + 1, playerIn);
		}
		
		return true;
	}
	
	//This returns a larger-to-smaller comparison of the paired objects, assuming they are integers.
	private class PairedIntComparator implements Comparator<Pair>
	{
		@Override
		public int compare(Pair x, Pair y)
		{
			if (x == null || y == null || x.object2 == null || y.object2 == null)
				return 0;
			
			return (Integer) y.object2 - (Integer) x.object2;
		}
	}
}
