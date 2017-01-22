package com.mraof.minestuck.item.weapon;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Created by mraof on 2017 January 18 at 6:17 PM.
 */
public class ItemPogoWeapon extends ItemWeapon
{
    private double pogoMotion;

    public ItemPogoWeapon(int maxUses, double damageVsEntity, double weaponSpeed, int enchantability, String name, double pogoMotion)
    {
        super(maxUses, damageVsEntity, weaponSpeed, enchantability, name);
        this.pogoMotion = pogoMotion;
    }

    @Override
    public boolean hitEntity(ItemStack itemStack, EntityLivingBase target, EntityLivingBase player)
    {
        itemStack.damageItem(1, player);
        if (player.fallDistance > 0.0F && !player.onGround && !player.isOnLadder() && !player.isInWater() && !player.isRiding())
        {
            double knockbackModifier = 1D - target.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).getAttributeValue();
            target.motionY = Math.max(target.motionY, knockbackModifier * Math.min(getPogoMotion(itemStack) * 2, Math.abs(player.motionY) + target.motionY + getPogoMotion(itemStack)));
            player.motionY = 0;
            player.fallDistance = 0;
        }
        return true;
    }

    private double getPogoMotion(ItemStack stack)
    {
//		return 0.5 + EnchantmentHelper.getEnchantmentLevel(Enchantment.efficiency.effectId, stack)*0.1;
        return pogoMotion;
    }

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if(worldIn.getBlockState(pos).getBlock() != Blocks.AIR)
        {
            double velocity = Math.max(playerIn.motionY, Math.min(getPogoMotion(stack) * 2, Math.abs(playerIn.motionY) + getPogoMotion(stack)));
            final float HORIZONTAL_Y = 6f;
            switch (facing.getAxis()) {
                case X:
                    velocity += Math.abs(playerIn.motionX) / 2;
                    playerIn.motionX = velocity * facing.getDirectionVec().getX();
                    playerIn.motionY = velocity / HORIZONTAL_Y;
                    break;
                case Y:
                    playerIn.motionY = velocity * facing.getDirectionVec().getY();
                    break;
                case Z:
                    velocity += Math.abs(playerIn.motionZ) / 2;
                    playerIn.motionZ = velocity * facing.getDirectionVec().getZ();
                    playerIn.motionY = velocity / HORIZONTAL_Y;
                    break;
            }
            playerIn.fallDistance = 0;
            stack.damageItem(1, playerIn);
            return EnumActionResult.SUCCESS;
        }
        return EnumActionResult.PASS;
    }

}
