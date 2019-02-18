package com.mraof.minestuck.block;

import com.mraof.minestuck.item.MinestuckItems;
import com.mraof.minestuck.item.TabMinestuck;
import com.mraof.minestuck.tileentity.TileEntityItemStack;
import com.mraof.minestuck.alchemy.AlchemyRecipes;
import net.minecraft.block.Block;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class BlockCruxtiteDowel extends Block
{
	protected static final AxisAlignedBB CRUXTRUDER_AABB = new AxisAlignedBB(5/16D, 0.0D, 5/16D, 11/16D, 5/16D, 11/16D);
	protected static final AxisAlignedBB DOWEL_AABB = new AxisAlignedBB(5/16D, 0.0D, 5/16D, 11/16D, 8/16D, 11/16D);
	public static final PropertyEnum<Type> TYPE = PropertyEnum.create("type", Type.class);
	
	public BlockCruxtiteDowel()
	{
		super(Material.GLASS);
		this.setCreativeTab(TabMinestuck.instance);
		this.setUnlocalizedName("dowelCruxite");
		setDefaultState(getDefaultState().withProperty(TYPE, Type.DOWEL));
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		return state.getValue(TYPE) == Type.CRUXTRUDER ? CRUXTRUDER_AABB : DOWEL_AABB;
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
	public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
	{
		TileEntity te = world.getTileEntity(pos);
		if(te instanceof TileEntityItemStack)
		{
			ItemStack stack = ((TileEntityItemStack) te).getStack();
			drops.add(stack);
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
	
	@Override
	public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side)
	{
		return side == EnumFacing.UP;
	}
	
	@Override
	public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
	{
		return getDefaultState();
	}
	
	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player)
	{
		TileEntity te = world.getTileEntity(pos);
		if(te instanceof TileEntityItemStack)
		{
			ItemStack dowel = ((TileEntityItemStack) te).getStack();
			if(!dowel.isEmpty())
				return dowel.copy();
		}
		return super.getPickBlock(state, target, world, pos, player);
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
	
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
	{
		TileEntity te = worldIn.getTileEntity(pos);
		if(te instanceof TileEntityItemStack)
		{
			ItemStack dowel = ((TileEntityItemStack) te).getStack();
			if(state.getValue(TYPE) == Type.DOWEL && AlchemyRecipes.hasDecodedItem(dowel))
				return state.withProperty(TYPE, Type.TOTEM);
		}
		
		return state;
	}
	
	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, TYPE);
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return getDefaultState().withProperty(TYPE, Type.values()[meta % 2]);
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return state.getValue(TYPE) == Type.CRUXTRUDER ? 0 : 1;
	}
	
	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
	{
		return BlockFaceShape.UNDEFINED;
	}
	
	@Override
	public EnumPushReaction getMobilityFlag(IBlockState state)
	{
		return EnumPushReaction.DESTROY;
	}
	
	public enum Type implements IStringSerializable
	{
		CRUXTRUDER,
		DOWEL,
		TOTEM;
		
		
		@Override
		public String getName()
		{
			return this.name().toLowerCase();
		}
	}
}
