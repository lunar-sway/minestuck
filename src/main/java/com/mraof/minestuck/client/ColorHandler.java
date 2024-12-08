package com.mraof.minestuck.client;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.client.renderer.BlockColorCruxite;
import com.mraof.minestuck.entity.FrogEntity;
import com.mraof.minestuck.item.FrogItem;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.item.components.MSItemComponents;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.StemBlock;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;

@EventBusSubscriber(modid = Minestuck.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class ColorHandler
{
    @SubscribeEvent
    public static void onItemColors(RegisterColorHandlersEvent.Item event)
    {
        event.register((stack, tintIndex) -> BlockColorCruxite.handleColorTint(com.mraof.minestuck.util.ColorHandler.getColorFromStack(stack), tintIndex),
                MSBlocks.CRUXITE_DOWEL.get(), MSItems.CRUXITE_APPLE.get(), MSItems.CRUXITE_POTION.get());
        event.register(new FrogItemColor(), MSItems.FROG.get());
    }
    
    @SubscribeEvent
    public static void initBlockColors(RegisterColorHandlersEvent.Block event)
    {
        event.register(new BlockColorCruxite(), MSBlocks.ALCHEMITER.TOTEM_PAD.get(), MSBlocks.TOTEM_LATHE.DOWEL_ROD.get(), MSBlocks.CRUXITE_DOWEL.get(), MSBlocks.EMERGING_CRUXITE_DOWEL.get());
        event.register((state, worldIn, pos, tintIndex) -> stemColor(state.getValue(StemBlock.AGE)), MSBlocks.STRAWBERRY_STEM.get());
        event.register((state, worldIn, pos, tintIndex) -> stemColor(7), MSBlocks.ATTACHED_STRAWBERRY_STEM.get()); //7 is equivalent to a fully grown stem block
    }
    
    public static int stemColor(int age)
    {
        int red = age * 32;
        int green = 255 - age * 8;
        int blue = age * 4;
        return red << 16 | green << 8 | blue;
    }

    protected static class FrogItemColor implements ItemColor
    {
        public int getColor(ItemStack stack, int tintIndex)
        {
            if(stack.has(MSItemComponents.FROG_TRAITS) && stack.get(MSItemComponents.FROG_TRAITS).variant().orElse(FrogEntity.FrogVariants.DEFAULT) == FrogEntity.FrogVariants.DEFAULT)
            {
				return switch(tintIndex)
				{
					case 0 -> FrogItem.getSkinColor(stack);
					case 1 -> FrogItem.getEyeColor(stack);
					case 2 -> FrogItem.getBellyColor(stack);
					default -> -1;
				};
            }
            return -1;
        }
    }
}
