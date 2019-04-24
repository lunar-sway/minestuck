package com.mraof.minestuck.block;

import com.mraof.minestuck.client.gui.GuiTransportalizer;
import com.mraof.minestuck.tileentity.TileEntityTransportalizer;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockTransportalizer extends BlockMachine
{
	public static final VoxelShape SHAPE = Block.makeCuboidShape(0, 0, 0, 16, 8, 16);
	
	public BlockTransportalizer(Properties properties)
	{
		super(properties);
	}
	
	@Override
	public VoxelShape getShape(IBlockState state, IBlockReader worldIn, BlockPos pos)
	{
		return SHAPE;
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
		return new TileEntityTransportalizer();
	}
	
	@Override
	public void onEntityCollision(IBlockState state, World worldIn, BlockPos pos, Entity entityIn)
	{
		if (!worldIn.isRemote && entityIn.getRidingEntity() == null && entityIn.getPassengers().isEmpty() && !worldIn.isRemote)
		{
			if(entityIn.timeUntilPortal == 0)
				((TileEntityTransportalizer) worldIn.getTileEntity(pos)).teleport(entityIn);
			else entityIn.timeUntilPortal = entityIn.getPortalCooldown();
		}
	}
	
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state)
	{
		return EnumBlockRenderType.MODEL;
	}
	
	@Override
	public boolean onBlockActivated(IBlockState state, World worldIn, BlockPos pos, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		TileEntityTransportalizer tileEntity = (TileEntityTransportalizer) worldIn.getTileEntity(pos);

		if (tileEntity == null || player.isSneaking())
		{
			return false;
		}

		if(worldIn.isRemote)
			Minecraft.getInstance().displayGuiScreen(new GuiTransportalizer(Minecraft.getInstance(), tileEntity));	//TODO Check if this causes complications

		return true;
	}
	
	@Override
	public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack stack)
	{
		player.addStat(StatList.BLOCK_MINED.get(this));
		player.addExhaustion(0.005F);
		
		if (EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, stack) > 0)
		{
			NonNullList<ItemStack> items = NonNullList.create();
			ItemStack itemstack = this.getSilkTouchDrop(state);
			
			if (!itemstack.isEmpty())
			{
				if(te instanceof TileEntityTransportalizer)
					itemstack.setDisplayName(new TextComponentString(((TileEntityTransportalizer) te).getId()));
				
				items.add(itemstack);
			}
			
			net.minecraftforge.event.ForgeEventFactory.fireBlockHarvesting(items, worldIn, pos, state, 0, 1.0f, true, player);
			for (ItemStack item : items)
			{
				spawnAsEntity(worldIn, pos, item);
			}
		}
		else
		{
			harvesters.set(player);
			int i = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, stack);
			state.dropBlockAsItem(worldIn, pos, i);
			harvesters.set(null);
		}
	}
}