package com.mraof.minestuck.block;

import com.mraof.minestuck.tileentity.TileEntityMiniPunchDesignix;
import com.mraof.minestuck.tileentity.TileEntityPunchDesignix;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.Map;

public class BlockMiniPunchDesignix extends BlockMachineProcess
{
	public static final Map<EnumFacing, VoxelShape> SHAPE = createRotatedShapes(0, 0, 0, 16, 16, 10);
	
	public BlockMiniPunchDesignix(Properties properties)
	{
		super(properties);
	}
	
	@Override
	public VoxelShape getShape(IBlockState state, IBlockReader worldIn, BlockPos pos)
	{
		return SHAPE.get(state.get(FACING));
	}
	
	@Override
	public boolean onBlockActivated(IBlockState state, World worldIn, BlockPos pos, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		TileEntity tileEntity = worldIn.getTileEntity(pos);
		if(!(tileEntity instanceof TileEntityMiniPunchDesignix) || player.isSneaking())
			return false;
		
		if(!worldIn.isRemote)
		{
			NetworkHooks.openGui((EntityPlayerMP) player, (TileEntityMiniPunchDesignix) tileEntity, pos);
		}
		return true;
	}
	
	@Override
	public boolean hasTileEntity(IBlockState state)
	{
		return true;
	}
	
	@Nullable
	@Override
	public TileEntity createTileEntity(IBlockState state, IBlockReader world)
	{
		return new TileEntityPunchDesignix();
	}
}