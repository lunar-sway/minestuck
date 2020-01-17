package com.mraof.minestuck.block;

import com.mraof.minestuck.tileentity.CruxtruderTileEntity;
import com.mraof.minestuck.tileentity.MiniCruxtruderTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
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

public class MiniCruxtruderBlock extends MachineProcessBlock
{
	public static final Map<Direction, VoxelShape> SHAPE = MSBlockShapes.SMALL_CRUXTRUDER.createRotatedShapes();
	
	public MiniCruxtruderBlock(Properties properties)
	{
		super(properties);
	}
	
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
	{
		return SHAPE.get(state.get(FACING));
	}
	
	@Override
	public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit)
	{
		TileEntity tileEntity = worldIn.getTileEntity(pos);
		if(!(tileEntity instanceof MiniCruxtruderTileEntity) || player.isSneaking())
			return false;
		
		if(!worldIn.isRemote)
		{
			NetworkHooks.openGui((ServerPlayerEntity) player, (MiniCruxtruderTileEntity) tileEntity, pos);
		}
		return true;
	}
	
	/*TODO
	@Override
	public void getDrops(IBlockState state, NonNullList<ItemStack> drops, World world, BlockPos pos, int fortune)
	{
		TileEntity tileEntity = world.getTileEntity(pos);
		int color = -1;
		if(tileEntity instanceof TileEntityMiniCruxtruder)
			color = ((TileEntityMiniCruxtruder) tileEntity).color;
		
		ItemStack stack = new ItemStack(this);
		stack.getOrCreateTag().putInt("dowel_color", color);
		drops.add(stack);
	}*/
	
	@Override
	public boolean hasTileEntity(BlockState state)
	{
		return true;
	}
	
	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world)
	{
		return new CruxtruderTileEntity();
	}
}