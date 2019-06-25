package com.mraof.minestuck.block;

import com.mraof.minestuck.tileentity.TileEntityGristWidget;
import com.mraof.minestuck.util.IdentifierHandler;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.Map;

public class BlockGristWidget extends BlockMachineProcess
{
	public static final Map<EnumFacing, VoxelShape> SHAPE = createRotatedShapes(2, 0, 5, 14, 2, 12);
	
	public static final BooleanProperty HAS_CARD = MinestuckProperties.HAS_CARD;
	
	public BlockGristWidget(Properties properties)
	{
		super(properties);
	}
	
	@Override
	public VoxelShape getShape(IBlockState state, IBlockReader worldIn, BlockPos pos)
	{
		return SHAPE.get(state.get(FACING));
	}
	
	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, IBlockState> builder)
	{
		super.fillStateContainer(builder);
		builder.add(HAS_CARD);
	}
	
	@Override
	public boolean onBlockActivated(IBlockState state, World worldIn, BlockPos pos, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		TileEntity tileEntity = worldIn.getTileEntity(pos);
		
		if (!(tileEntity instanceof TileEntityGristWidget) || player.isSneaking())
			return false;
		
		if(!worldIn.isRemote)
		{
			TileEntityGristWidget widget = (TileEntityGristWidget) tileEntity;
			widget.owner = IdentifierHandler.encode(player);
			NetworkHooks.openGui((EntityPlayerMP) player, widget, pos);
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
		return new TileEntityGristWidget();
	}
	
	public static void updateItem(boolean b, World world, BlockPos pos)
	{
		IBlockState oldState = world.getBlockState(pos);
		if(oldState.getBlock() instanceof BlockGristWidget)
			world.setBlockState(pos, oldState.with(HAS_CARD, b), 2);
	}
}