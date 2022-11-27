package com.mraof.minestuck.client.model.blockentity;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.blockentity.HorseClockBlockEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class HorseClockModel extends AnimatedGeoModel<HorseClockBlockEntity> {
    @Override
    public ResourceLocation getModelLocation(HorseClockBlockEntity object) {
        return new ResourceLocation(Minestuck.MOD_ID, "geo/blockentity/horseclock.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(HorseClockBlockEntity object) {
        Level level = object.getLevel();
        if(level != null)
        {
            int dayTime = (int) (level.getDayTime() % 24000);
            if(dayTime < 3000)
                return new ResourceLocation(Minestuck.MOD_ID, "textures/block/horseclock0.png");
            else if(dayTime < 6000)
                return new ResourceLocation(Minestuck.MOD_ID, "textures/block/horseclock1.png");
            else if(dayTime < 9000)
                return new ResourceLocation(Minestuck.MOD_ID, "textures/block/horseclock2.png");
            else if(dayTime < 12000)
                return new ResourceLocation(Minestuck.MOD_ID, "textures/block/horseclock3.png");
            else if(dayTime < 15000)
                return new ResourceLocation(Minestuck.MOD_ID, "textures/block/horseclock4.png");
            else if(dayTime < 18000)
                return new ResourceLocation(Minestuck.MOD_ID, "textures/block/horseclock5.png");
            else if(dayTime < 21000)
                return new ResourceLocation(Minestuck.MOD_ID, "textures/block/horseclock6.png");
            else
                return new ResourceLocation(Minestuck.MOD_ID, "textures/block/horseclock7.png");
        }
        
        return new ResourceLocation(Minestuck.MOD_ID, "textures/block/horseclock0.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(HorseClockBlockEntity animatable) {
        return new ResourceLocation(Minestuck.MOD_ID, "animations/blockentity/horseclock.animation.json");
    }
}
