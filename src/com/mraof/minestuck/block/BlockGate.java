package com.mraof.minestuck.block;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.tileentity.TileEntityGate;
import net.minecraft.block.Block;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class BlockGate extends Block
{
	
	protected static final VoxelShape SHAPE = Block.makeCuboidShape(0.0D, 7.0D, 0.0D, 16.0D, 9.0D, 16.0D);
	public static BooleanProperty MAIN = MinestuckProperties.MAIN;
	
	public BlockGate(Properties properties)
	{
		super(properties);
		setDefaultState(getDefaultState().with(MAIN, false));
	}
	
	@Override
	public VoxelShape getShape(IBlockState state, IBlockReader worldIn, BlockPos pos)
	{
		return SHAPE;
	}
	
	@Override
	public boolean addDestroyEffects(IBlockState state, World world, BlockPos pos, ParticleManager manager)
	{
		return true;
	}
	
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state)
	{
		return EnumBlockRenderType.INVISIBLE;
	}
	
	@Override
	public boolean isFullCube(IBlockState state)
	{
		return false;
	}
	
	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, IBlockState> builder)
	{
		builder.add(MAIN);
	}
	
	@Nullable
	@Override
	public TileEntity createTileEntity(IBlockState state, IBlockReader world)
	{
		return new TileEntityGate();
	}
	
	@Override
	public boolean hasTileEntity(IBlockState state)
	{
		return state.get(MAIN);
	}
	
	@Override
	public void onEntityCollision(IBlockState state, World worldIn, BlockPos pos, Entity entityIn)
	{
		if(entityIn instanceof EntityPlayerMP && !entityIn.isPassenger() && !entityIn.isBeingRidden())
		{
			if(entityIn.timeUntilPortal != 0)
			{
				entityIn.timeUntilPortal = entityIn.getPortalCooldown();
				return;
			}
			
			BlockPos mainPos = pos;
			if(!state.get(MAIN))
				if(this != MinestuckBlocks.GATE)
					mainPos = this.findMainComponent(pos, worldIn);
				else return;
			
			TileEntity te = worldIn.getTileEntity(mainPos);
			if(te instanceof TileEntityGate)
				((TileEntityGate) te).teleportEntity(worldIn, (EntityPlayerMP) entityIn, this);
		}
	}
	
	protected boolean isValid(BlockPos pos, World world, IBlockState state)
	{
		if(state.get(MAIN))
			return isValid(pos, world);
		else
		{
			BlockPos mainPos = findMainComponent(pos, world);
			if(mainPos != null)
				return isValid(mainPos, world);
			else return false;
		}
	}
	
	protected boolean isValid(BlockPos pos, World world)
	{
		for(int x = -1; x <= 1; x++)
			for(int z = -1; z <= 1; z++)
				if(x != 0 || z != 0)
				{
					IBlockState block = world.getBlockState(pos.add(x, 0, z));
					if(block.getBlock() != this || (Boolean) block.get(MAIN))
						return false;
				}
		
		return true;
	}
	
	protected void removePortal(BlockPos pos, World world)
	{
		for(int x = -1; x <= 1; x++)
			for(int z = -1; z <= 1; z++)
				if(world.getBlockState(pos.add(x, 0, z)).getBlock() == this)
					world.removeBlock(pos.add(x, 0, z));
	}
	
	protected BlockPos findMainComponent(BlockPos pos, World world)
	{
		for(int x = -1; x <= 1; x++)
			for(int z = -1; z <= 1; z++)
				if(x != 0 || z != 0)
				{
					IBlockState block = world.getBlockState(pos.add(x, 0, z));
					if(block.getBlock() == this && block.get(MAIN))
						return pos.add(x, 0, z);
				}
		
		return null;
	}
	
	@Override
	public void onReplaced(IBlockState state, World worldIn, BlockPos pos, IBlockState newState, boolean isMoving)
	{
		super.onReplaced(state, worldIn, pos, newState, isMoving);
		if(state.get(MAIN))
			removePortal(pos, worldIn);
		else
		{
			BlockPos mainPos = findMainComponent(pos, worldIn);
			if(mainPos != null)
				removePortal(mainPos, worldIn);
		}
	}
	
	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
	{
		if(!this.isValid(pos, worldIn, state))
		{
			BlockPos mainPos = pos;
			if(!state.get(MAIN))
				mainPos = findMainComponent(pos, worldIn);
			
			if(mainPos == null)
				worldIn.removeBlock(pos);
			else removePortal(mainPos, worldIn);
		}
	}
	
	@Override
	public float getExplosionResistance(IBlockState state, IWorldReader world, BlockPos pos, @Nullable Entity exploder, Explosion explosion)
	{
		if(this instanceof BlockReturnNode || MinestuckConfig.canBreakGates)
			return super.getExplosionResistance(state, world, pos, exploder, explosion);
		else return 3600000.0F;
	}
	
	@Override
	public BlockFaceShape getBlockFaceShape(IBlockReader worldIn, IBlockState state, BlockPos pos, EnumFacing face)
	{
		return BlockFaceShape.UNDEFINED;
	}
}