package com.mraof.minestuck.block;

import com.mraof.minestuck.item.TabMinestuck;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSnowLayer;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockLayered extends BlockSnowLayer
{
	public Block sourceBlock;
	
	public BlockLayered(Block block)
	{
		super(Properties.from(block));
		this.sourceBlock = block;
	}
	
	@Override
	public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack stack)
	{
		Integer integer = state.get(LAYERS);
		net.minecraft.util.NonNullList<ItemStack> items = net.minecraft.util.NonNullList.create();
		int fortune = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, stack);
		float chance;
		
		if (this.canSilkHarvest(state, worldIn, pos, player) && EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, stack) > 0) {
			if (integer == 8) {
				items.add(new ItemStack(sourceBlock));
			} else {
				for(int i = 0; i < integer; ++i) {
					items.add(this.getSilkTouchDrop(state));
				}
			}
			chance = net.minecraftforge.event.ForgeEventFactory.fireBlockHarvesting(items, worldIn, pos, state, 0, 1.0f, true, player);
		} else {
			getDrops(state, items, worldIn, pos, fortune);
			chance = net.minecraftforge.event.ForgeEventFactory.fireBlockHarvesting(items, worldIn, pos, state, fortune, 1.0f, false, player);
		}
		
		for (ItemStack item : items) {
			if (worldIn.rand.nextFloat() <= chance)
				spawnAsEntity(worldIn, pos, item);
		}
		
		worldIn.removeBlock(pos);
		player.addStat(StatList.BLOCK_MINED.get(this));
		player.addExhaustion(0.005F);
	}
	
	@Override
	public IItemProvider getItemDropped(IBlockState state, World worldIn, BlockPos pos, int fortune)
	{
		return this;
	}
	
	@Override
	public void tick(IBlockState state, World worldIn, BlockPos pos, Random random)
	{}
	
}
