package com.mraof.minestuck.item.weapon;

import com.mraof.minestuck.advancements.MSCriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class MelonWeaponItem extends WeaponItem
{
    private final Supplier<Item> itemDropped;
    private final Supplier<Block> harvestedBlock;
    private final int maxBonusItems;
    private final float percentage;
    private final boolean melonOverload;
    
    public MelonWeaponItem(IItemTier tier, int attackDamageIn, float attackSpeedIn, float efficiency, boolean melonOverload, float percentage, int maxBonusItems, Supplier<Item> itemDropped, Supplier<Block> harvestedBlock, @Nullable MSToolType toolType, Properties builder)
    {
        super(tier, attackDamageIn, attackSpeedIn, efficiency, toolType, builder);
        this.itemDropped = itemDropped;
        this.harvestedBlock = harvestedBlock;
        this.maxBonusItems = maxBonusItems;
        this.percentage = percentage;
        this.melonOverload = melonOverload;
    }
    
    @Override
    public boolean onBlockDestroyed(ItemStack stack, World worldIn, BlockState state, BlockPos pos, LivingEntity entityLiving)
    {
        if(state == harvestedBlock.get().getDefaultState() && !worldIn.isRemote)
        {
            int harvestCounter = 0;
            for(Float i = entityLiving.getRNG().nextFloat(); i <= percentage && harvestCounter < maxBonusItems; i = entityLiving.getRNG().nextFloat())
            {
                ItemEntity item = new ItemEntity(worldIn, pos.getX(), pos.getY(), pos.getZ(), itemDropped.get().getDefaultInstance());
                worldIn.addEntity(item);
                
                harvestCounter++;
            }
            
            if(melonOverload && harvestCounter >= 9 && entityLiving instanceof PlayerEntity)
            {
                IFormattableTextComponent message = new TranslationTextComponent(getTranslationKey() + ".message");
                message.mergeStyle(TextFormatting.GREEN);
                message.setStyle(message.getStyle().setBold(true));
                entityLiving.sendMessage(message, Util.DUMMY_UUID);
                
                entityLiving.addPotionEffect(new EffectInstance(Effects.STRENGTH, 1800, 3));
                entityLiving.addPotionEffect(new EffectInstance(Effects.RESISTANCE, 1800, 3));
                entityLiving.addPotionEffect(new EffectInstance(Effects.HASTE, 1800, 3));
                
                MSCriteriaTriggers.MELON_OVERLOAD.trigger((ServerPlayerEntity) entityLiving);
            }
        }
        return super.onBlockDestroyed(stack, worldIn, state, pos, entityLiving);
    }
}
