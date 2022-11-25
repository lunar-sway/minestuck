package com.mraof.minestuck.client.model.blockentity;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.blockentity.HorseClockBlockEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class HorseClockModel extends AnimatedGeoModel<HorseClockBlockEntity> {
    @Override
    public ResourceLocation getModelLocation(HorseClockBlockEntity object) {
        return new ResourceLocation(Minestuck.MOD_ID, "geo/blockentity/horseclock.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(HorseClockBlockEntity object) {
        return new ResourceLocation(Minestuck.MOD_ID, "textures/block/horseclock.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(HorseClockBlockEntity animatable) {
        return new ResourceLocation(Minestuck.MOD_ID, "animations/blockentity/horseclock.animation.json");
    }
}
