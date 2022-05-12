package com.mraof.minestuck.client.renderer.entity;

import com.mraof.minestuck.client.model.ConsortModel;
import com.mraof.minestuck.entity.consort.ConsortEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class ConsortRenderer<T extends ConsortEntity> extends GeoEntityRenderer<T> {
    public ConsortRenderer(EntityRendererManager renderManager) {
        super(renderManager, new ConsortModel<>());
    }

    @Override
    protected float getDeathMaxRotation(T entityLivingBaseIn) {
        return 0;
    }

}
