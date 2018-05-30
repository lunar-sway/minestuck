package com.mraof.minestuck.item.weapon;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;

import com.mraof.minestuck.util.Pair;
import com.mraof.minestuck.util.MinestuckRandom;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * A "Farmine" tool is a tool that mines more blocks that just the one originally broken.
 * Other mods may refer to this as vein-mining or similar, but here it is called far-mining.
 * A farmining tool works mostly like a normal ItemWeapon: it has durability, attack damage, attack speed, and enchantability.
 * In addition, however, it has a Radius and Terminus.
 * The radius is the farthest distance between the initially-broken block and a block broken in the same mining operation. A value of -1 means no radius will come into play.
 * The terminus is the largest number of blocks that can be destroyed in a single farmining operation. Terminus is clamped to a minimum of 1.
 * 
 * Farmining tools will only break more of the same block broken originally.
 * Two blocks are considered the same if they have the same Block ID and drop the same item with the same damage.
 * Two blocks that are not normally considered the same will be considered the same if they have an association.
 * @author BenjaminK
 *
 */
public class ItemFarmine extends ItemWeapon
{
	private int radius;
	private int terminus;
	private HashSet<Block> farMineForbiddenBlocks = new HashSet<Block>();
	private HashMap<Block, HashSet<Block>> farMineEquivalencies = new HashMap<Block, HashSet<Block>>();
	
	public ItemFarmine(ToolMaterial material, int maxUses, double damageVsEntity, double weaponSpeed, int enchantability, String name, int r, int t)
	{
		super(material, maxUses, damageVsEntity, weaponSpeed, enchantability, name);
		redefineLimiters(r, t);
		initializeFarMineLists();
	}
	
	public ItemFarmine(int maxUses, double damageVsEntity, double weaponSpeed, int enchantability, String name, int r, int t)
	{
		this(ToolMaterial.IRON, maxUses, damageVsEntity, weaponSpeed, enchantability, name, r, t);
	}

	private void initializeFarMineLists()
	{
		farMineForbiddenBlocks.add(Blocks.OBSIDIAN);
		
		HashSet set = new HashSet<Block>();
		set.add(Blocks.LIT_REDSTONE_ORE);
		farMineEquivalencies.put(Blocks.REDSTONE_ORE, (HashSet<Block>) set.clone());
		
		set.clear();
		set.add(Blocks.REDSTONE_ORE);
		farMineEquivalencies.put(Blocks.LIT_REDSTONE_ORE, (HashSet<Block>) set.clone());
		
		set.clear();
		set.add(Blocks.MONSTER_EGG);
		farMineEquivalencies.put(Blocks.COBBLESTONE, (HashSet<Block>) set.clone());
		farMineEquivalencies.put(Blocks.STONE, (HashSet<Block>) set.clone());
		farMineEquivalencies.put(Blocks.STONEBRICK, (HashSet<Block>) set.clone());

		set.clear();
		set.add(Blocks.DIRT);
		farMineEquivalencies.put(Blocks.GRASS, (HashSet<Block>) set.clone());
		farMineEquivalencies.put(Blocks.MYCELIUM, (HashSet<Block>) set.clone());
		farMineEquivalencies.put(Blocks.GRASS_PATH, (HashSet<Block>) set.clone());
		
		set.clear();
		set.add(Blocks.GRASS);
		set.add(Blocks.MYCELIUM);
		set.add(Blocks.GRASS_PATH);
		farMineEquivalencies.put(Blocks.DIRT, (HashSet<Block>) set.clone());
	}
	
	@Override
	public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState blockState, BlockPos pos, EntityLivingBase playerIn)
	{
		if(worldIn.isRemote)
		{
			return false;
		}
		
		Comparator<Pair> comparator = new PairedIntComparator();
		PriorityQueue<Pair> candidates = new PriorityQueue<Pair>(comparator);
		Block block = blockState.getBlock();
		Item drop = block.getItemDropped(blockState, MinestuckRandom.getRandom(), 0);
		int damageDrop = block.damageDropped(blockState);
		HashSet<Block> equals = farMineEquivalencies.get(block);
		if(equals==null) equals = new HashSet<Block>();	
		
		//If the tool can't harvest the block, or the player is sneaking,
		//or the tool doesn't farmine, or it's one of those blocks that breaks instantly, don't farmine.
		if (!canHarvestBlock(blockState, stack) || playerIn.isSneaking()
				|| terminus == 1 || radius==0 || Math.abs(blockState.getBlockHardness(worldIn, pos)) < 0.000000001)
		{
			if (!isDamageable())
				return true;
			else
				return super.onBlockDestroyed(stack, worldIn, blockState, pos, playerIn);
		}
		else
		{
			//If the block is unacceptable or there's a tool mismatch, cap out at a basic 3x3 area
			if (farMineForbiddenBlocks.contains(block)
					|| getDestroySpeed(stack, blockState) < getEfficiency())
			{
				candidates.add(new Pair(pos, 1));
			}
			else	//Otherwise, farmine normally
			{
				candidates.add(new Pair(pos, radius));
			}
		}
		
		//This set will contain all the blocks you'll want to break in the end.
		//This is used to determine if the number of blocks broken is too high.
		//If it is, it mines a 3x3 area instead.
		HashSet<BlockPos> blocksToBreak = new HashSet<BlockPos>();
		boolean passedBreakLimit = false;

		while (!candidates.isEmpty() && passedBreakLimit == false)
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
								if (	equals.contains(newstate.getBlock()) || newstate.getBlock().equals(block)
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
				passedBreakLimit = true;
			}
		}
		
		//If you passed the break limit, only harvest a 3x3 area.
		if (passedBreakLimit)
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
						if ( equals.contains(newstate.getBlock()) || newstate.getBlock().equals(block)
								&& newstate.getBlock().getItemDropped(newstate, MinestuckRandom.getRandom(), 0) == drop
								&& newstate.getBlock().damageDropped(newstate) == damageDrop
								&& damage < stack.getMaxDamage() - stack.getItemDamage())
						{
							if(playerIn instanceof EntityPlayer)
								block.removedByPlayer(newstate, worldIn, newBlock, (EntityPlayer) playerIn, true);
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
				if(playerIn instanceof EntityPlayer)
					block.removedByPlayer(worldIn.getBlockState(blockToBreak), worldIn, blockToBreak, (EntityPlayer) playerIn, true);
				worldIn.getBlockState(blockToBreak).getBlock().dropBlockAsItem(worldIn, pos, worldIn.getBlockState(blockToBreak), 0);
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
	
	/*
	 * Utility
	 */
	
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
	
	/**
	 * Determines if the farmining tool will consider one block equivalent to another.
	 * @param a The block whose nature is the test of equivalence
	 * @param b The block whose equivalency is being tested
	 * @return Returns true if <code>b</code> is equivalent to <code>a</code>
	 */
	private boolean isEquivalent(Block a, Block b)
	{
		HashSet e = farMineEquivalencies.get(a);
		return e != null && e.contains(b);
	}
	
	/*
	 * Getters, setters, and other public access methods
	 */
	
	/**
	 * Gets the tool's radius. This limits how far out blocks can be mined.
	 * @return Returns the tool's maximum mining range. Will never be less than 0.
	 */
	public int getRadius()
	{
		return radius;
	}
	
	/**
	 * For some blocks, like obsidian, it might not be appropriate to farmine, even with a farmining tool equipped.
	 * Farmining can be disabled for these blocks by adding them to a special list.
	 * This method tests a block to see if it is on this list.
	 * @param block The block being tested
	 * @return Returns true if the block is on the forbidden list, and false if the block is not on the list.
	 */
	public boolean isBlockForbidden(Block block)
	{
		return farMineForbiddenBlocks.contains(block);
	}
	
	/**
	 * Gets the tool's terminus. This limits how many blocks can be mined in one operation.
	 * @return Returns the tool's maximum number of blocks broken in a single operation. Will never be less than 1.
	 */
	public int getTerminus()
	{
		return terminus;
	}
	
	/**
	 * Redefines the limiting values of a farmining tool: radius and terminus.
	 * @param r R is the new value for the tool's radius. Clamped between 0 and t. Setting this to 0 means the item can't farmine at all.
	 * @param t T is the new value for the tool's terminus. This will limit how many blocks can be mined. Values are clamped to a minimum of 1.
	 * @return Returns the same farmining tool, after the change has been applied. Useful for chaining same-line modifications.
	 */
	public ItemFarmine redefineLimiters(int r, int t)	
	{
		//A terminus of less than 1 means that the tool cannot mine. A terminus of 1 means it cannot farmine.
		terminus = Math.max(1, t);
		//A tool that can only mine X blocks can't mine more than X blocks away.
		radius = Math.max(0, Math.min(r, terminus));
		return this;
	}
	
	/**
	 * Each tool maps blocks to sets of blocks, in a system of association.
	 * This implementation was chosen because it provides cheap lookup.
	 * This is good, because associations are checked in large batches within a single server tick.
	 * A block is mapped to a set of all the blocks associated with it.
	 * @return Returns (or is supposed to return) a deep clone of this tool's private association map. 
	 */
	public HashMap<Block, HashSet<Block>> getAssociations()
	{
		HashMap<Block, HashSet<Block>> out = new HashMap<Block, HashSet<Block>>();
		//TODO: Add security code so that this doesn't return the ACTUAL association storage, but just a copy of it.
		//Using .clone() is not sufficient.
		return out;
	}
	
	/**
	 * Normally, a block is considered equal to the originally-mined one by having the same Block ID and dropping the same item, with the same damage value.
	 * In cases where this violates intuition and diminishes the gameplay experience (lit redstone ore is not redstone ore), we use associations.
	 * Two blocks that have an association are considered equal to the farmining tool; when one is mined, the other will be, too. 
	 * This method lets you add an association between two blocks so that this tool will treat them as the same. 
	 * @param a A is a block that will be made equivalent to B.
	 * @param b B is a block that will be made equivalent to A.
	 */
	public void addAssociation(Block a, Block b)
	{
		addOneWayAssociation(a, b);
		addOneWayAssociation(b, a);
	}
	
	/**
	 * Adds a one-way association between two blocks, stating that B is equal to A, but A is not equal to B.
	 * One-way associations should be reserved for special cases; please use <code>addAssociation</code> for normal equalities.
	 * For example: by default, monster eggs are the only blocks to use one-way associations;
	 * monster eggs are treated as stone, cobblestone, and stone bricks, but those blocks are not treated as monster eggs.
	 * @param a A is a block that will not be made equal to B. When a B block is mined, blocks of type A are ignored like before.
	 * @param b B is a block that will be made equal to A. When an A block is mined, blocks of type B are included as mining candidates.
	 */
	public void addOneWayAssociation(Block a, Block b)
	{
		HashSet<Block> equalToA = farMineEquivalencies.get(a);
		if(equalToA==null)
		{
			equalToA = new HashSet<Block>();
			farMineEquivalencies.put(a, equalToA);
		}
		equalToA.add(b);
	}

	/**
	 * Removes an association between two blocks. These two blocks will no longer be considered equivalent for this farmining tool.
	 * Blocks will still be equivalent if they have the same block ID and drop items with the same ID and damage values.
	 * This removal is a two-way removal. If you want to remove only one, use this method alongside <code>addOneWayAssociation</code>. 
	 * @param a A is a block that may or may not be associated with B. This method ensures it will not be, when complete.
	 * @param b B is a block that may or may not be associated with A. This method ensures it will not be, when complete.
	 */
	public void removeAssociation(Block a, Block b)
	{
		HashSet<Block> equalToA = farMineEquivalencies.get(a);
		HashSet<Block> equalToB = farMineEquivalencies.get(b);
		if(equalToA!=null)
		{
			equalToA.remove(b);
		}
		if(equalToB!=null)
		{
			equalToB.remove(a);
		}
	}
}
