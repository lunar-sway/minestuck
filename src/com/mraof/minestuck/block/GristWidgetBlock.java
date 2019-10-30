package com.mraof.minestuck.block;

import com.mraof.minestuck.tileentity.GristWidgetTileEntity;
import com.mraof.minestuck.util.IdentifierHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.Map;

public class GristWidgetBlock extends MachineProcessBlock
{
	public static final Map<Direction, VoxelShape> SHAPE = MSBlockShapes.GRIST_WIDGET.createRotatedShapes();
	
	public static final BooleanProperty HAS_CARD = MSProperties.HAS_CARD;
	
	public GristWidgetBlock(Properties properties)
	{
		super(properties);
	}
	
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
	{
		return SHAPE.get(state.get(FACING));
	}
	
	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
	{
		super.fillStateContainer(builder);
		builder.add(HAS_CARD);
	}
	
	@Override
	public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit)
	{
		TileEntity tileEntity = worldIn.getTileEntity(pos);
		
		if (!(tileEntity instanceof GristWidgetTileEntity) || player.isSneaking())
			return false;
		
		if(!worldIn.isRemote)
		{
			GristWidgetTileEntity widget = (GristWidgetTileEntity) tileEntity;
			widget.owner = IdentifierHandler.encode(player);
			NetworkHooks.openGui((ServerPlayerEntity) player, widget, pos);
		}
		return true;
	}
	
	@Override
	public boolean hasTileEntity(BlockState state)
	{
		return true;
	}
	
	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world)
	{
		return new GristWidgetTileEntity();
	}
	
	public static void updateItem(boolean b, World world, BlockPos pos)
	{
		BlockState oldState = world.getBlockState(pos);
		if(oldState.getBlock() instanceof GristWidgetBlock)
			world.setBlockState(pos, oldState.with(HAS_CARD, b), 2);
	}
}