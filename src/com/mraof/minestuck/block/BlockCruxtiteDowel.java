package com.mraof.minestuck.block;

import com.mraof.minestuck.item.MinestuckItems;
import com.mraof.minestuck.tileentity.TileEntityItemStack;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class BlockCruxtiteDowel extends Block
{
	protected static final AxisAlignedBB TRANSPORTALIZER_AABB = new AxisAlignedBB(5/16D, 0.0D, 5/16D, 11/16D, 5/16D, 11/16D);
	
	public BlockCruxtiteDowel()
	{
		super(Material.GLASS);
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		return TRANSPORTALIZER_AABB;
	}
	
	@Override
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.CUTOUT_MIPPED;
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}
	
	@Override
	public boolean isFullCube(IBlockState state)
	{
		return false;
	}
	
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		super.breakBlock(worldIn, pos, state);
		worldIn.removeTileEntity(pos);
	}
	
	@Override
	public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack stack)
	{
		player.addStat(StatList.getBlockStats(this));
		player.addExhaustion(0.005F);
		
		if(te instanceof TileEntityItemStack)
		{
			int fortune = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, stack);
			List<ItemStack> items = new ArrayList<>();
			items.add(((TileEntityItemStack) te).getStack());
			net.minecraftforge.event.ForgeEventFactory.fireBlockHarvesting(items, worldIn, pos, state, fortune, 1.0f, false, harvesters.get());
			
			for (ItemStack item : items)
			{
				spawnAsEntity(worldIn, pos, item);
			}
		}
	}
	
	@Override
	public boolean hasTileEntity(IBlockState state)
	{
		return true;
	}
	
	@Nullable
	@Override
	public TileEntity createTileEntity(World world, IBlockState state)
	{
		TileEntityItemStack te = new TileEntityItemStack();
		te.setStack(new ItemStack(MinestuckItems.cruxiteDowel));
		return te;
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if(!worldIn.isRemote)
			dropDowel(worldIn, pos);
		return true;
	}
	
	public static void dropDowel(World world, BlockPos pos)
	{
		TileEntity te = world.getTileEntity(pos);
		if(te instanceof TileEntityItemStack)
		{
			ItemStack stack = ((TileEntityItemStack) te).getStack();
			spawnAsEntity(world, pos, stack);
		}
		world.setBlockToAir(pos);
	}
}
