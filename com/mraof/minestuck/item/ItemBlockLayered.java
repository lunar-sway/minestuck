package com.mraof.minestuck.item;

import com.mraof.minestuck.block.BlockLayered;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlockWithMetadata;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemBlockLayered extends ItemBlockWithMetadata {

	public Block theBlock;
	public ItemBlockLayered(int par1, Block par2Block)
	{
		super(par1, par2Block);
		theBlock = par2Block;
	}

    /**
     * Callback for item usage. If the item does something special on right clicking, he will have one of those. Return
     * True if something happen and false if it don't. This is for ITEMS, not BLOCKS
     */
	@Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int par7, float par8, float par9, float par10)
    {
        if (itemStack.stackSize == 0)
        {
            return false;
        }
        else if (!player.canPlayerEdit(x, y, z, par7, itemStack))
        {
            return false;
        }
        else
        {
            int id = world.getBlockId(x, y, z);

            if (id == theBlock.blockID)
            {
                Block block = Block.blocksList[this.getBlockID()];
                int metadata = world.getBlockMetadata(x, y, z);

                if (/*depth <= 6 && */world.checkNoEntityCollision(block.getCollisionBoundingBoxFromPool(world, x, y, z)) && ((BlockLayered)theBlock).changeHeight(world, x, y, z, metadata + 1)) //changes full BlockLayered into full b
                {
                    world.playSoundEffect((double)x + 0.5, (double)y + 0.5, (double)z + 0.5, block.stepSound.getPlaceSound(), (block.stepSound.getVolume() + 1.0F) / 2.0F, block.stepSound.getPitch() * 0.8F);
                    --itemStack.stackSize;
                    return true;
                }
            }

            return super.onItemUse(itemStack, player, world, x, y, z, par7, par8, par9, par10);
        }
    }

}
