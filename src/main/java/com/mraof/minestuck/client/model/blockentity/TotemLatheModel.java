package com.mraof.minestuck.client.model.blockentity;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.blockentity.machine.TotemLatheDowelBlockEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class TotemLatheModel extends GeoModel<TotemLatheDowelBlockEntity>
{
    @Override
    public ResourceLocation getModelResource(TotemLatheDowelBlockEntity object) {
        return new ResourceLocation(Minestuck.MOD_ID, "geo/blockentity/totemlathe.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(TotemLatheDowelBlockEntity object) {
        return new ResourceLocation(Minestuck.MOD_ID, "textures/block/machine/totemlathe.png");
    }

    @Override
    public ResourceLocation getAnimationResource(TotemLatheDowelBlockEntity animatable) {
        return new ResourceLocation(Minestuck.MOD_ID, "animations/blockentity/totemlathe.animation.json");
    }
}
