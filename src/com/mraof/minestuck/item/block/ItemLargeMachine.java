package com.mraof.minestuck.item.block;

import com.mraof.minestuck.item.MinestuckItems;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBed;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class ItemLargeMachine extends ItemBlock
{
	
	



	private int Xlength;
	private int Zwidth;
	private int Yheight;

	
	public ItemLargeMachine(Block block, int XLength, int YHeight,int ZWidth) {
		super(block);
		Xlength=XLength;
		Yheight=YHeight;
		Zwidth=ZWidth;
	}
    
    
    
    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {

        if (worldIn.isRemote)
        {
            return EnumActionResult.SUCCESS;
        }
        else
        {
            IBlockState iblockstate = worldIn.getBlockState(pos);
            Block block = iblockstate.getBlock();
            boolean flag = block.isReplaceable(worldIn, pos);

            if (!flag)
            {
                pos = pos.up();
            }

            int i = MathHelper.floor((double)(player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
            EnumFacing enumfacing = EnumFacing.getHorizontal(i);
            ItemStack itemstack = player.getHeldItem(hand);
            
            for(int x = 0; x<Xlength; x++){
            	for (int y=0;y<Yheight;y++){
            		for (int z=0; z<Zwidth;z++){
            			if (!worldIn.mayPlace(getBlock(), pos.offset( enumfacing.rotateY() , x ).offset(enumfacing,z).up(y), false, EnumFacing.UP, player)){
            					
            				return EnumActionResult.FAIL;
            			}
        	            worldIn.notifyNeighborsRespectDebug(pos, block, false);
            		}
            	}
            }
            return super.onItemUse(player, worldIn, pos, hand, enumfacing, hitX, hitY, hitZ);
        }
    }
}	
	