package com.mraof.minestuck.block;

import com.mraof.minestuck.tileentity.TileEntityCruxtruder;
import com.mraof.minestuck.tileentity.TileEntityMiniCruxtruder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

public class MiniCruxtruderBlock extends MachineProcessBlock
{
	public static final VoxelShape SHAPE = Block.makeCuboidShape(0, 0, 0, 16, 15, 16);
	
	public MiniCruxtruderBlock(Properties properties)
	{
		super(properties);
	}
	
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
	{
		return SHAPE;
	}
	
	@Override
	public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit)
	{
		TileEntity tileEntity = worldIn.getTileEntity(pos);
		if(!(tileEntity instanceof TileEntityMiniCruxtruder) || player.isSneaking())
			return false;
		
		if(!worldIn.isRemote)
		{
			NetworkHooks.openGui((ServerPlayerEntity) player, (TileEntityMiniCruxtruder) tileEntity, pos);
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
		return new TileEntityCruxtruder();
	}
}