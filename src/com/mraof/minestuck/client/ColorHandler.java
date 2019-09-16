package com.mraof.minestuck.client;

import com.mraof.minestuck.item.FrogItem;
import com.mraof.minestuck.item.MSItems;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@OnlyIn(Dist.CLIENT)
public class ColorHandler
{
    @SubscribeEvent
    public static void onItemColors(ColorHandlerEvent.Item event)
    {
        System.out.println("onItemColors is doing something");
        ItemColors itemColors = event.getItemColors();

        itemColors.register(new FrogItemColor(), MSItems.FROG);
        System.out.println("onItemColors is still doing something");
    }

    protected static class FrogItemColor implements IItemColor
    {
        public int getColor(ItemStack stack, int tintIndex)
        {
            FrogItem item = ((FrogItem)stack.getItem());
            int color = -1;
            int type = stack.hasTag() ? 0 : stack.getTag().getInt("Type");
            if(type == 0)
            {
                switch(tintIndex)
                {
                    case 0: color = item.getSkinColor(stack); break;
                    case 1: color = item.getEyeColor(stack); break;
                    case 2: color = item.getBellyColor(stack); break;
                }
            }
            return color;
        }
    }
}
