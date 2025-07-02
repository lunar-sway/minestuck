package com.mraof.minestuck.item.weapon;

import com.mraof.minestuck.block.MSBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import static com.mraof.minestuck.util.MSTags.Blocks.FARMINE_BREAK_BLACKLIST;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

/**
 * A "Farmine" harvestTool is a harvestTool that mines more blocks that just the one originally broken.
 * Other mods may refer to this as vein-mining or similar, but here it is called far-mining.
 * A farmining harvestTool works mostly like a normal WeaponItem: it has durability, attack damage, attack speed, and enchantability.
 * In addition, however, it has a Radius and Terminus.
 * The radius is the farthest distance between the initially-broken block and a block broken in the same mining operation. A value of -1 means no radius will come into play.
 * The terminus is the largest number of blocks that can be destroyed in a single farmining operation. Terminus is clamped to a minimum of 1.
 * <p>
 * Farmining tools will only break more of the same block broken originally.
 * Two blocks are considered the same if they have the same Block ID and drop the same item with the same damage.
 * Two blocks that are not normally considered the same will be considered the same if they have an association.
 *
 * @author BenjaminK
 */
public class FarmineEffect implements DestroyBlockEffect
{
	/**
	 * Maximum mining distance from the block mined
	 */
	private int radius;
	/**
	 * Maximum amount of blocks mined
	 */
	private int terminus;
	private final HashMap<Block, HashSet<Block>> farMineEquivalencies = new HashMap<>();
	
	public FarmineEffect(int radius, int terminus)
	{
		redefineLimiters(radius, terminus);
		initializeFarMineLists();
	}
	
	private void initializeFarMineLists()
	{
		addAssociation(Blocks.DIRT, Blocks.GRASS_BLOCK);
		addAssociation(Blocks.DIRT, Blocks.MYCELIUM);
		addAssociation(Blocks.DIRT, Blocks.DIRT_PATH);
		addAssociation(Blocks.END_STONE, MSBlocks.END_GRASS.get());
		
		addOneWayAssociation(Blocks.COBBLESTONE, Blocks.INFESTED_COBBLESTONE);
		addOneWayAssociation(Blocks.STONE, Blocks.INFESTED_STONE);
		addOneWayAssociation(Blocks.STONE_BRICKS, Blocks.INFESTED_STONE_BRICKS);
		addOneWayAssociation(Blocks.CHISELED_STONE_BRICKS, Blocks.INFESTED_CHISELED_STONE_BRICKS);
		addOneWayAssociation(Blocks.CRACKED_STONE_BRICKS, Blocks.INFESTED_CRACKED_STONE_BRICKS);
		addOneWayAssociation(Blocks.MOSSY_STONE_BRICKS, Blocks.INFESTED_MOSSY_STONE_BRICKS);
	}
	
	/**
	 * Called when a Block is destroyed using this Item. Returns true to trigger the "Use Item" statistic.
	 * This is the method that performs farmining calculations and destruction.
	 *
	 * @param stack      The ItemStack being used to destroy the block. This should always be an instance of FarmineItem.
	 *                   This method also respects the presence of Silk Touch or Fortune on this ItemStack.
	 * @param level      The world where the blocks being destroyed can be found.
	 * @param blockState The state of the initial block being destroyed.
	 * @param pos        The position of the initial block being destroyed.
	 * @param playerIn   The player doing the actual destroying. This MUST be an instance of EntityPlayer or no farmining will occur!
	 * @return Returns false if and only if the world is remote.
	 */
	
	@Override
	public void onDestroyBlock(ItemStack stack, Level level, BlockState blockState, BlockPos pos, LivingEntity playerIn)
	{
		if(level.isClientSide)
		{
			return;
		}
		
		int rad;
		
		//If the harvestTool can't harvest the block, or the player isn't actually a player, or the player is sneaking,
		//or the harvestTool doesn't farmine, or it's one of those blocks that breaks instantly, don't farmine.
		if(!stack.isCorrectToolForDrops(blockState) || !(playerIn instanceof Player) || playerIn.isShiftKeyDown()
				|| terminus == 1 || radius == 0 || Math.abs(blockState.getDestroySpeed(level, pos)) < 0.000000001)
		{
			return;
		} else
		{
			//If the block is unacceptable or there's a harvestTool mismatch, cap out at a basic 3x3 area
			if(blockState.is(FARMINE_BREAK_BLACKLIST)
					|| blockState.getBlock().defaultDestroyTime() > ((WeaponItem) stack.getItem()).getEfficiency())
			{
				rad = 1;
			} else    //Otherwise, farmine normally
			{
				rad = radius;
			}
		}
		
		//This set will contain all the blocks you'll want to break in the end.
		HashSet<BlockPos> blocksToBreak = getConnectedBlocks(pos, blockState.getBlock(), rad,
				Math.min(terminus, stack.getMaxDamage() - stack.getDamageValue()), level);
		
		//Now, break ALL of the blocks!
		for(BlockPos blockToBreak : blocksToBreak)
		{
			BlockState state = level.getBlockState(blockToBreak);
			harvestBlock(level, state.getBlock(), blockToBreak, state, playerIn, stack);
		}
		
		stack.hurtAndBreak(blocksToBreak.size(), playerIn, EquipmentSlot.MAINHAND);
	}
	
	/*
	 * Utility
	 */
	
	private boolean harvestBlock(Level level, Block block, BlockPos pos, BlockState state, LivingEntity playerIn, ItemStack stack)
	{
		Player player = (Player) playerIn;
		
		BlockEntity te = level.getBlockEntity(pos);
		if(block.onDestroyedByPlayer(state, level, pos, player, true, level.getFluidState(pos)))
		{
			block.destroy(level, pos, state);
			block.playerDestroy(level, player, pos, state, te, stack);
			return true;
		}
		return false;
	}
	
	/**
	 * Determines the greatest distance in all 3 axis between 2 positions
	 */
	private int distBetween(BlockPos a, BlockPos b)
	{
		return Math.max(Math.max(Math.abs(a.getX() - b.getX()),
						Math.abs(a.getY() - b.getY())),
				Math.abs(a.getZ() - b.getZ()));
	}
	
	/**
	 * Returns all connected blocks positions that can be farmined
	 *
	 * @param start Starting position
	 * @param block Starting block
	 */
	private HashSet<BlockPos> getConnectedBlocks(BlockPos start, Block block, int radius, int max, Level level)
	{
		HashSet<BlockPos> blocksToBreak = new HashSet<BlockPos>();
		HashSet<Block> equals = farMineEquivalencies.get(block);
		if(equals == null) equals = new HashSet<>();
		
		if(radius != 0)
		{
			LinkedList<BlockPos> edges = new LinkedList<>();
			edges.add(start);
			
			do
			{
				BlockPos edge = edges.poll();
				blocksToBreak.add(edge);
				
				//Iterates across all blocks in a 3x3 cube centered on this block.
				//This ends up with a bias towards downward north-west
				for(int i = -1; i < 2; i++)
				{
					for(int j = -1; j < 2; j++)
					{
						for(int k = -1; k < 2; k++)
						{
							if(i == 0 && j == 0 && k == 0)
								continue;
							
							BlockPos newBlockPos = new BlockPos(edge.getX() + i, edge.getY() + j, edge.getZ() + k);
							BlockState newState = level.getBlockState(newBlockPos);
							Block newBlock = newState.getBlock();
							
							if(!blocksToBreak.contains(newBlockPos) && !edges.contains(newBlockPos) &&
									distBetween(start, newBlockPos) <= radius &&
									(equals.contains(newBlock) || newBlock.equals(block)))
							{
								edges.add(newBlockPos);
							}
						}
					}
				}
			} while(blocksToBreak.size() <= max && edges.size() > 0);
		} else
		{
			blocksToBreak.add(start);
		}
		
		return blocksToBreak;
	}
	
	/*
	 * Getters, setters, and other public access methods
	 */
	
	/**
	 * Redefines the limiting values of a farmining harvestTool: radius and terminus.
	 *
	 * @param r R is the new value for the harvestTool's radius. Clamped between 0 and t. Setting this to 0 means the item can't farmine at all.
	 * @param t T is the new value for the harvestTool's terminus. This will limit how many blocks can be mined. Values are clamped to a minimum of 1.
	 * @return Returns the same farmining harvestTool, after the change has been applied. Useful for chaining same-line modifications.
	 */
	public FarmineEffect redefineLimiters(int r, int t)
	{
		//A terminus of less than 1 means that the harvestTool cannot mine. A terminus of 1 means it cannot farmine.
		terminus = Math.max(1, t);
		//A harvestTool that can only mine X blocks can't mine more than X blocks away.
		radius = Math.max(0, Math.min(r, terminus));
		return this;
	}
	
	/**
	 * Normally, a block is considered equal to the originally-mined one by having the same Block ID and dropping the same item, with the same damage value.
	 * In cases where this violates intuition and diminishes the gameplay experience (lit redstone ore is not redstone ore), we use associations.
	 * Two blocks that have an association are considered equal to the farmining harvestTool; when one is mined, the other will be, too.
	 * This method lets you add an association between two blocks so that this harvestTool will treat them as the same.
	 *
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
	 *
	 * @param a A is a block that will not be made equal to B. When a B block is mined, blocks of type A are ignored like before.
	 * @param b B is a block that will be made equal to A. When an A block is mined, blocks of type B are included as mining candidates.
	 */
	public void addOneWayAssociation(Block a, Block b)
	{
		HashSet<Block> equalToA = farMineEquivalencies.get(a);
		if(equalToA == null)
		{
			equalToA = new HashSet<Block>();
			farMineEquivalencies.put(a, equalToA);
		}
		equalToA.add(b);
	}
}
