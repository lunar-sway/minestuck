package com.mraof.minestuck.client;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.client.renderer.BlockColorCruxite;
import com.mraof.minestuck.item.FrogItem;
import com.mraof.minestuck.item.MSItems;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.StemBlock;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ColorHandler
{
    @SubscribeEvent
    public static void onItemColors(ColorHandlerEvent.Item event)
    {
        ItemColors itemColors = event.getItemColors();
    
        itemColors.register((stack, tintIndex) -> BlockColorCruxite.handleColorTint(com.mraof.minestuck.util.ColorHandler.getColorFromStack(stack), tintIndex),
                MSBlocks.CRUXITE_DOWEL.get(), MSItems.CRUXITE_APPLE.get(), MSItems.CRUXITE_POTION.get());
        itemColors.register(new FrogItemColor(), MSItems.FROG.get());
    }
    
    @SubscribeEvent
    public static void initBlockColors(ColorHandlerEvent.Block event)
    {
        BlockColors colors = event.getBlockColors();
        colors.register(new BlockColorCruxite(), MSBlocks.ALCHEMITER.TOTEM_PAD.get(), MSBlocks.TOTEM_LATHE.DOWEL_ROD.get(), MSBlocks.CRUXITE_DOWEL.get());
        colors.register((state, worldIn, pos, tintIndex) ->
        {
            int age = state.getValue(StemBlock.AGE);
            int red = age * 32;
            int green = 255 - age * 8;
            int blue = age * 4;
            return red << 16 | green << 8 | blue;
        }, MSBlocks.STRAWBERRY_STEM.get());
    }

    protected static class FrogItemColor implements ItemColor
    {
        public int getColor(ItemStack stack, int tintIndex)
        {
            int color = -1;
            int type = !stack.hasTag() ? 0 : stack.getTag().getInt("Type");
            if(type == 0)
            {
                switch(tintIndex)
                {
                    case 0: color = FrogItem.getSkinColor(stack); break;
                    case 1: color = FrogItem.getEyeColor(stack); break;
                    case 2: color = FrogItem.getBellyColor(stack); break;
                }
            }
            return color;
        }
    }
}