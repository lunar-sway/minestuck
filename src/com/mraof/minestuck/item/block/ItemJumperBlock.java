package com.mraof.minestuck.item.block;

import com.mraof.minestuck.block.BlockAlchemiter;
import com.mraof.minestuck.block.BlockAlchemiterUpgrades;
import com.mraof.minestuck.block.BlockJumperBlock.EnumParts;
import com.mraof.minestuck.editmode.EditData;
import com.mraof.minestuck.editmode.ServerEditHandler;
import com.mraof.minestuck.item.MinestuckItems;
import com.mraof.minestuck.tileentity.TileEntityAlchemiter;
import com.mraof.minestuck.tileentity.TileEntityCruxtruder;
import com.mraof.minestuck.tileentity.TileEntityJumperBlock;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.util.MinestuckPlayerData;
import com.mraof.minestuck.block.BlockJumperBlock;
import com.mraof.minestuck.block.MinestuckBlocks;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class ItemJumperBlock extends ItemBlock
{
	static boolean flip = false;
	
	public ItemJumperBlock(Block block)
	{
		super(block);
	}
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if (worldIn.isRemote)
		{
			return EnumActionResult.SUCCESS;
		} else if (facing != EnumFacing.UP)
		{
			return EnumActionResult.FAIL;
		} else
		{
			Block block = worldIn.getBlockState(pos).getBlock();
			boolean flag = block.isReplaceable(worldIn, pos);
			
			if (!flag)
			{
				pos = pos.up();
			}
			
			EnumFacing placedFacing = player.getHorizontalFacing().getOpposite();
			ItemStack itemstack = player.getHeldItem(hand);
			
			pos = pos.offset(placedFacing.rotateY());
			
			if(placedFacing.getFrontOffsetX() > 0 && hitZ >= 0.5F || placedFacing.getFrontOffsetX() < 0 && hitZ < 0.5F
					|| placedFacing.getFrontOffsetZ() > 0 && hitX < 0.5F || placedFacing.getFrontOffsetZ() < 0 && hitX >= 0.5F)
				pos = pos.offset(placedFacing.rotateY());
			
			if (!itemstack.isEmpty())
			{
				if(!canPlaceAt(itemstack, player, worldIn, pos, placedFacing))
					return EnumActionResult.FAIL;
				
				//if(!true) TODO alchemiter detection
				//	return EnumActionResult.FAIL;
				
				IBlockState state = this.block.getDefaultState().withProperty(BlockJumperBlock.DIRECTION, placedFacing);
				this.placeBlockAt(itemstack, player, worldIn, pos, facing, hitX, hitY, hitZ, state);
				return EnumActionResult.SUCCESS;
			}
			return EnumActionResult.FAIL;
		}
	}
	
	public static boolean checkOutline(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing facing)
	{
		boolean outlineFlip = flip;
		for (int x = 0; x < 5; x++)
		{
			if (!player.canPlayerEdit(pos.offset(facing.rotateYCCW(), x), EnumFacing.UP, stack))
				return false;
			for (int y = 0; y < 1; y++)
			{
				for(int z=0;z<4;z++) {
					if (!world.mayPlace(MinestuckBlocks.jumperBlockExtension[0], pos.offset(facing.getOpposite(),z).offset(facing.rotateYCCW(), x).up(y), false, EnumFacing.UP, null))
						return false;
				}
			}
		}
		for(int z = 0; z < 4; z++)
		{
			EnumFacing lookupFacing = facing.rotateY();
			EnumFacing flipFacing = facing;
			int flipOffset = 1;
			int lookupOffset = 4;
			if(!outlineFlip)
				{
					lookupFacing = lookupFacing.getOpposite();
					flipFacing = flipFacing.getOpposite();
					lookupOffset = 5;
					flipOffset = 5;
				}
			
			
			
			BlockPos lookupPos = (pos.offset(lookupFacing, lookupOffset));
			BlockPos alchemPos = TileEntityJumperBlock.staticAlchemiterMainPos(lookupPos, world);
			
				for(int z2 = 0; z2 < 4; z2++)
				{
					
					if(!(world.getBlockState(pos.offset(facing.getOpposite(), z2).offset(flipFacing.rotateY(), flipOffset)).getBlock() instanceof BlockAlchemiter))
						{
							
						outlineFlip = !outlineFlip;
							return false;
						}
				}
				
			}
		
		return true;
	}
	
	public static boolean canPlaceAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing facing)
	{
		for (int x = 0; x < 5; x++)
		{
			if (!player.canPlayerEdit(pos.offset(facing.rotateYCCW(), x), EnumFacing.UP, stack))
				return false;
			for (int y = 0; y < 1; y++)
			{
				for(int z=0;z<4;z++) {
					if (!world.mayPlace(MinestuckBlocks.jumperBlockExtension[0], pos.offset(facing.getOpposite(),z).offset(facing.rotateYCCW(), x).up(y), false, EnumFacing.UP, null))
						return false;
				}
			}
		}
		for(int z = 0; z < 4; z++)
		{
			EnumFacing lookupFacing = facing.rotateY();
			EnumFacing flipFacing = facing;
			int flipOffset = 1;
			int lookupOffset = 4;
			if(!flip)
				{
					lookupFacing = lookupFacing.getOpposite();
					flipFacing = flipFacing.getOpposite();
					lookupOffset = 5;
					flipOffset = 5;
				}
			
			
			
			BlockPos lookupPos = (pos.offset(lookupFacing, lookupOffset));
			BlockPos alchemPos = TileEntityJumperBlock.staticAlchemiterMainPos(lookupPos, world);
			
				for(int z2 = 0; z2 < 4; z2++)
				{
					
					if(!(world.getBlockState(pos.offset(facing.getOpposite(), z2).offset(flipFacing.rotateY(), flipOffset)).getBlock() instanceof BlockAlchemiter))
						{
							
							flip = !flip;
							return false;
						}
					else
					{
						TileEntity alchemTe = world.getTileEntity(alchemPos);
						
						
						if(alchemTe instanceof TileEntityAlchemiter)
						{

							TileEntityAlchemiter alchemiterTe = (TileEntityAlchemiter) alchemTe;
							
							if(alchemiterTe.isUpgraded() || alchemiterTe.isBroken()) 
								return false;
							
							
						}
					}
				}
				
			}
		
		return true;
	}
	@Override
	public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, IBlockState newState)
	{
		EnumFacing facing = player.getHorizontalFacing();
		
		if(!flip)
		{
			pos = pos.offset(facing,3).offset(facing.rotateY(),4);
			facing = facing.getOpposite();
		}
		
		if(!world.isRemote)
		{
			BlockPos cablePos = pos.offset(facing,2).offset(facing.rotateY(),0);
			
			world.setBlockState(pos.up(0), BlockJumperBlock.getState(EnumParts.TOP_CORNER_PLUG, facing.rotateYCCW()));
			world.setBlockState(pos.offset(facing,0).offset(facing.rotateY(),1), BlockJumperBlock.getState(EnumParts.TOP_PLUG, facing.rotateYCCW()));
			world.setBlockState(pos.offset(facing,0).offset(facing.rotateY(),2), BlockJumperBlock.getState(EnumParts.TOP_PLUG, facing.rotateYCCW()));
			world.setBlockState(pos.offset(facing,0).offset(facing.rotateY(),3), BlockJumperBlock.getState(EnumParts.TOP_PLUG, facing.rotateYCCW()));
			world.setBlockState(pos.offset(facing,0).offset(facing.rotateY(),4), BlockJumperBlock.getState(EnumParts.BORDER_RIGHT, facing.rotateYCCW()));
			world.setBlockState(pos.offset(facing,1).offset(facing.rotateY(),0), BlockJumperBlock.getState(EnumParts.BOTTOM_CORNER_PLUG, facing.rotateYCCW()));
			world.setBlockState(pos.offset(facing,1).offset(facing.rotateY(),1), BlockJumperBlock.getState(EnumParts.BOTTOM_PLUG, facing.rotateYCCW()));
			world.setBlockState(pos.offset(facing,1).offset(facing.rotateY(),2), BlockJumperBlock.getState(EnumParts.BOTTOM_PLUG, facing.rotateYCCW()));
			world.setBlockState(pos.offset(facing,1).offset(facing.rotateY(),3), BlockJumperBlock.getState(EnumParts.BOTTOM_PLUG, facing.rotateYCCW()));
			world.setBlockState(pos.offset(facing,1).offset(facing.rotateY(),4), BlockJumperBlock.getState(EnumParts.BORDER_SIDE, facing.rotateYCCW()));
			world.setBlockState(cablePos, BlockJumperBlock.getState(EnumParts.CABLE, facing.rotateYCCW()));
			world.setBlockState(pos.offset(facing,2).offset(facing.rotateY(),1), BlockJumperBlock.getState(EnumParts.CENTER, facing.rotateYCCW()));
			world.setBlockState(pos.offset(facing,2).offset(facing.rotateY(),2), BlockJumperBlock.getState(EnumParts.CENTER, facing.rotateYCCW()));
			world.setBlockState(pos.offset(facing,2).offset(facing.rotateY(),3), BlockJumperBlock.getState(EnumParts.CENTER, facing.rotateYCCW()));
			world.setBlockState(pos.offset(facing,2).offset(facing.rotateY(),4), BlockJumperBlock.getState(EnumParts.BORDER_LEFT, facing.rotateYCCW()));
			world.setBlockState(pos.offset(facing,3).offset(facing.rotateY(),0), BlockJumperBlock.getState(EnumParts.BASE_CORNER, facing.getOpposite()));
			world.setBlockState(pos.offset(facing,3).offset(facing.rotateY(),1), BlockJumperBlock.getState(EnumParts.BASE_SIDE, facing.getOpposite()));
			world.setBlockState(pos.offset(facing,3).offset(facing.rotateY(),2), BlockJumperBlock.getState(EnumParts.BASE_SIDE, facing.getOpposite()));
			world.setBlockState(pos.offset(facing,3).offset(facing.rotateY(),3), BlockJumperBlock.getState(EnumParts.BASE_CORNER, facing.rotateYCCW()));
			world.setBlockState(pos.offset(facing,3).offset(facing.rotateY(),4), BlockJumperBlock.getState(EnumParts.SMALL_CORNER, facing.rotateYCCW()));
			
			TileEntity te = world.getTileEntity(cablePos);
			if(te instanceof TileEntityJumperBlock)
			{
				
				int color;
				EditData editData = ServerEditHandler.getData(player);
				if(editData != null)
					color = MinestuckPlayerData.getData(editData.getTarget()).color;
				else color = MinestuckPlayerData.getData(player).color;
				
				((TileEntityJumperBlock) te).setColor(color);
				
				EnumFacing flipFacing = facing.rotateY();
				if(!flip) flipFacing = flipFacing.getOpposite();
				TileEntity alchemTe = world.getTileEntity(((TileEntityJumperBlock) te).staticAlchemiterMainPos(cablePos.offset(flipFacing), world));
				if(alchemTe instanceof TileEntityAlchemiter)
				{
					TileEntityAlchemiter alchemiter = (TileEntityAlchemiter) alchemTe;
					
					//TODO
					alchemiter.setUpgraded(true, cablePos);
				}
				else Debug.warnf("Couldn't find Alchemiter at %s. Instead found %s.", ((TileEntityJumperBlock) te).alchemiterMainPos(), alchemTe);
				
			} else Debug.warnf("Placed JBE, but can't find tile entity. Instead found %s.", te);
			
			if(player instanceof EntityPlayerMP)
				CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP) player, pos, stack);
		}
		return true;
	}
}


