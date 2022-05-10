package com.mraof.minestuck.client.model;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.entity.underling.UnderlingEntity;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import java.util.Objects;

public class UnderlingModel<T extends UnderlingEntity> extends AnimatedGeoModel<T> {
    @Override
    public ResourceLocation getModelLocation(T entity) {
        return new ResourceLocation(Minestuck.MOD_ID, "geo/entity/underlings/" + getName(entity) + ".geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(T entity) {
        return new ResourceLocation(Minestuck.MOD_ID, "textures/entity/underlings/" + getName(entity) + ".png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(T entity) {
        return new ResourceLocation(Minestuck.MOD_ID, "animations/entity/underlings/" + getName(entity) + ".animation.json");
    }

    public static String getName(UnderlingEntity entity) {
        return Objects.requireNonNull(entity.getType().getRegistryName(), () -> "Getting texture for entity without a registry name! " + entity).getPath();
    }
}
