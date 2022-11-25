package com.mraof.minestuck.client.model.blockentity;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.blockentity.machine.TotemLatheDowelBlockEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class TotemLatheModel extends AnimatedGeoModel<TotemLatheDowelBlockEntity> {
    @Override
    public ResourceLocation getModelLocation(TotemLatheDowelBlockEntity object) {
        return new ResourceLocation(Minestuck.MOD_ID, "geo/blockentity/totemlathe.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(TotemLatheDowelBlockEntity object) {
        return new ResourceLocation(Minestuck.MOD_ID, "textures/block/machine/totemlathe.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(TotemLatheDowelBlockEntity animatable) {
        return new ResourceLocation(Minestuck.MOD_ID, "animations/blockentity/totemlathe.animation.json");
    }
}
