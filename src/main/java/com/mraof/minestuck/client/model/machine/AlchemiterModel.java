package com.mraof.minestuck.client.model.machine;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.tileentity.machine.AlchemiterTileEntity;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class AlchemiterModel extends AnimatedGeoModel<AlchemiterTileEntity> {
    @Override
    public ResourceLocation getModelLocation(AlchemiterTileEntity object) {
        return new ResourceLocation(Minestuck.MOD_ID, "geo/alchemiter.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(AlchemiterTileEntity object) {
        return new ResourceLocation(Minestuck.MOD_ID, "textures/block/machine/alchemiter.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(AlchemiterTileEntity animatable) {
        return new ResourceLocation(Minestuck.MOD_ID, "animations/alchemiter.animation.json");
    }
}
