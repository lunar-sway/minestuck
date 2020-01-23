package com.mraof.minestuck.block;

import com.mraof.minestuck.tileentity.IOwnable;
import com.mraof.minestuck.util.IdentifierHandler;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
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
import java.util.function.Supplier;

public class SmallMachineBlock extends MachineProcessBlock
{
	private final Map<Direction, VoxelShape> shape;
	private final Supplier<TileEntityType<?>> tileType;
	
	public SmallMachineBlock(Map<Direction, VoxelShape> shape, Supplier<TileEntityType<?>> tileType, Properties properties)
	{
		super(properties);
		this.shape = shape;
		this.tileType = tileType;
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
	{
		return shape.get(state.get(FACING));
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit)
	{
		if(!player.isSneaking())
		{
			if(!worldIn.isRemote)
			{
				TileEntity tileEntity = worldIn.getTileEntity(pos);
				if(tileEntity != null && tileEntity.getType() == this.tileType.get())
				{
					if(tileEntity instanceof IOwnable)
						((IOwnable) tileEntity).setOwner(IdentifierHandler.encode(player));
					if(tileEntity instanceof INamedContainerProvider)
						NetworkHooks.openGui((ServerPlayerEntity) player, (INamedContainerProvider) tileEntity, pos);
				}
			}
			return true;
		} else return false;
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
		return tileType.get().create();
	}
}