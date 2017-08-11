package com.mraof.minestuck.block;

import com.mraof.minestuck.item.MinestuckItems;
import com.mraof.minestuck.tileentity.TileEntityComputer;
import com.mraof.minestuck.util.ComputerProgram;
import com.mraof.minestuck.util.IdentifierHandler;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class BlockComputerOff extends Block
{
	
	public static final PropertyDirection DIRECTION = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	
	public BlockComputerOff()
	{
		super(Material.ROCK);
		setUnlocalizedName("sburbComputer");
		setHardness(4.0F);
		setHarvestLevel("pickaxe", 0);
		this.setCreativeTab(MinestuckItems.tabMinestuck);
		
	}
	
	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, DIRECTION);
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return ((EnumFacing)state.getValue(DIRECTION)).ordinal() - 2;
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return getDefaultState().withProperty(DIRECTION, EnumFacing.values()[meta + 2]);
	}
	
	public static void setDefaultDirection(World world, int x, int y, int z)
	{
		if (!world.isRemote)
		{
			IBlockState block = world.getBlockState(new BlockPos(x, y, z - 1));
			IBlockState block1 = world.getBlockState(new BlockPos(x, y, z + 1));
			IBlockState block2 = world.getBlockState(new BlockPos(x - 1, y, z));
			IBlockState block3 = world.getBlockState(new BlockPos(x + 1, y, z));
			byte b0 = 0;

			if (block.isFullBlock() && !block1.isFullBlock())
			{
				b0 = 3;
			}

			if (block1.isFullBlock() && !block.isFullBlock())
			{
				b0 = 2;
			}

			if (block2.isFullBlock() && !block3.isFullBlock())
			{
				b0 = 5;
			}

			if (block3.isFullBlock() && !block2.isFullBlock())
			{
				b0 = 4;
			}
			
			if(b0 == 0)
				b0 = (byte) (block3.isFullBlock() ? 2 : 5);
			
			world.setBlockState(new BlockPos(x, y, z), world.getBlockState(new BlockPos(x, y, z)).withProperty(DIRECTION, EnumFacing.values()[b0]), 2);
		}
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		ItemStack heldItem = playerIn.getHeldItem(hand);
		if(playerIn.isSneaking() || !state.getValue(DIRECTION).equals(facing) || !heldItem.isEmpty() && ComputerProgram.getProgramID(heldItem) == -2)
			return false;
		
		if(!worldIn.isRemote)
		{
			worldIn.setBlockState(pos, MinestuckBlocks.blockComputerOn.getDefaultState().withProperty(DIRECTION, facing), 2);
			
			TileEntityComputer te = (TileEntityComputer) worldIn.getTileEntity(pos);
			te.owner = IdentifierHandler.encode(playerIn);
			MinestuckBlocks.blockComputerOn.onBlockActivated(worldIn, pos, worldIn.getBlockState(pos), playerIn, hand, facing, hitX, hitY, hitZ);
		}
		
		return true;
	}
	
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
	{
		int l = MathHelper.floor((double)(placer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
		
		EnumFacing facing = new EnumFacing[]{EnumFacing.NORTH, EnumFacing.EAST, EnumFacing.SOUTH, EnumFacing.WEST}[l];
		
		worldIn.setBlockState(pos, state.withProperty(DIRECTION, facing), 2);
	}
	
}
