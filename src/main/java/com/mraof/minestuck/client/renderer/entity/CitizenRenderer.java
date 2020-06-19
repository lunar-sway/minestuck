package com.mraof.minestuck.client.renderer.entity;

import com.mraof.minestuck.entity.carapacian.PassiveCarapacianEntity;
import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.util.ResourceLocation;

public class CitizenRenderer extends BipedRenderer<PassiveCarapacianEntity, BipedModel<PassiveCarapacianEntity>>
{

    public CitizenRenderer(EntityRendererManager manager, BipedModel modelBiped, float shadowSize)
    {
        super(manager, modelBiped, shadowSize);
    }

    //@Override
    protected ResourceLocation getEntityTexture(PassiveCarapacianEntity citizen)
    {
        return citizen.getTextureResource();
    }
}