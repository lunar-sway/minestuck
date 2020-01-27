package com.mraof.minestuck.client;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.client.renderer.BlockColorCruxite;
import com.mraof.minestuck.item.FrogItem;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.util.ColorCollector;
import net.minecraft.block.StemBlock;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.item.ItemStack;
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
    
        itemColors.register((stack, tintIndex) -> BlockColorCruxite.handleColorTint(ColorCollector.getColorFromStack(stack), tintIndex),
                MSBlocks.CRUXITE_DOWEL, MSItems.CRUXITE_APPLE, MSItems.CRUXITE_POTION);
        itemColors.register(new FrogItemColor(), MSItems.FROG);
    }
    
    @SubscribeEvent
    public static void initBlockColors(ColorHandlerEvent.Block event)
    {
        BlockColors colors = event.getBlockColors();
        colors.register(new BlockColorCruxite(), MSBlocks.ALCHEMITER.TOTEM_PAD.get(), MSBlocks.TOTEM_LATHE.DOWEL_ROD.get(), MSBlocks.CRUXITE_DOWEL);
        colors.register((state, worldIn, pos, tintIndex) ->
        {
            int age = state.get(StemBlock.AGE);
            int red = age * 32;
            int green = 255 - age * 8;
            int blue = age * 4;
            return red << 16 | green << 8 | blue;
        }, MSBlocks.STRAWBERRY_STEM);
    }

    protected static class FrogItemColor implements IItemColor
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