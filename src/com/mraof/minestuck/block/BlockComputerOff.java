package com.mraof.minestuck.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.tileentity.TileEntityComputer;
import com.mraof.minestuck.util.ComputerProgram;
import com.mraof.minestuck.util.UsernameHandler;

public class BlockComputerOff extends Block
{
	
	public static final PropertyDirection DIRECTION = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	
	public BlockComputerOff()
	{
		super(Material.rock);
		setUnlocalizedName("sburbComputer");
		setHardness(4.0F);
		this.setCreativeTab(Minestuck.tabMinestuck);
		
	}
	
	@Override
	protected BlockState createBlockState()
	{
		return new BlockState(this, DIRECTION);
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
			Block block = world.getBlockState(new BlockPos(x, y, z - 1)).getBlock();
			Block block1 = world.getBlockState(new BlockPos(x, y, z + 1)).getBlock();
			Block block2 = world.getBlockState(new BlockPos(x - 1, y, z)).getBlock();
			Block block3 = world.getBlockState(new BlockPos(x + 1, y, z)).getBlock();
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
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		if(player.isSneaking() || !state.getValue(DIRECTION).equals(side) || player.getHeldItem() != null && ComputerProgram.getProgramID(player.getHeldItem()) == -2)
			return false;
		
		if(!world.isRemote)
		{
			world.setBlockState(pos, Minestuck.blockComputerOn.getDefaultState().withProperty(DIRECTION, side), 2);
			
			TileEntityComputer te = (TileEntityComputer) world.getTileEntity(pos);
			te.owner = UsernameHandler.encode(player.getCommandSenderName());
			Minestuck.blockComputerOn.onBlockActivated(world, pos, world.getBlockState(pos), player, side, hitX, hitY, hitZ);
		}
		
		return true;
	}
	
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
	{
		int l = MathHelper.floor_double((double)(placer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
		
		EnumFacing facing = new EnumFacing[]{EnumFacing.NORTH, EnumFacing.EAST, EnumFacing.SOUTH, EnumFacing.WEST}[l];
		
		worldIn.setBlockState(pos, state.withProperty(DIRECTION, facing), 2);
	}
	
}
