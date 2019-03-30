package com.mraof.minestuck.block;

import net.minecraft.block.BlockLeaves;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class BlockMinestuckLeaves extends BlockLeaves
{
	protected static final Item[] RAINBOW_DROPS = new Item[]{Items.ORANGE_DYE, Items.MAGENTA_DYE, Items.LIGHT_BLUE_DYE,
			Items.LIME_DYE, Items.PINK_DYE, Items.GRAY_DYE, Items.LIGHT_GRAY_DYE, Items.CYAN_DYE, Items.PURPLE_DYE};
	
	protected IItemProvider sapling, apple;
	protected boolean reducedSaplingDrop;
	
	public BlockMinestuckLeaves(IItemProvider sapling, Properties properties)
	{
		this(sapling, false, null, properties);
	}
	
	/**
	 * Constructor for leaves
	 * @param properties general properties of the block
	 * @param sapling the sapling that will be dropped when the leaves break.
	 * @param reducedSaplingDrop halves sapling drop rate if true, in the same way as jungle leaves
	 * @param apple bonus item that uses same probabilities as the vanilla apples, or null if no such item should drop.
	 *              Beware that blocks are registered before items.
	 */
	public BlockMinestuckLeaves(IItemProvider sapling, boolean reducedSaplingDrop, IItemProvider apple, Properties properties)
	{
		super(properties);
		this.sapling = sapling;
		this.reducedSaplingDrop = reducedSaplingDrop;
		this.apple = apple;
	}
	
	@Override
	public IItemProvider getItemDropped(IBlockState state, World worldIn, BlockPos pos, int fortune)
	{
		return sapling;
	}
	
	@Override
	protected void dropApple(World worldIn, BlockPos pos, IBlockState state, int chance)
	{
		if(apple != null && worldIn.rand.nextInt(chance) == 0)
		{
			spawnAsEntity(worldIn, pos, new ItemStack(apple));
		} else if(this == MinestuckBlocks.RAINBOW_LEAVES)
		{
			int i = worldIn.rand.nextInt(RAINBOW_DROPS.length);
			spawnAsEntity(worldIn, pos, new ItemStack(RAINBOW_DROPS[i]));
		}
	}
	
	@Override
	public int getFlammability(IBlockState state, IBlockReader world, BlockPos pos, EnumFacing face)
	{
		return 5;
	}
	
	@Override
	public int getFireSpreadSpeed(IBlockState state, IBlockReader world, BlockPos pos, EnumFacing face)
	{
		return 5;
	}
	
	@Override
	protected int getSaplingDropChance(IBlockState state)
	{
		return reducedSaplingDrop ? 40 : 20;
	}
}