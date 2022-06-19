package com.mraof.minestuck.client.model;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.tileentity.machine.TotemLatheDowelTileEntity;
import com.mraof.minestuck.tileentity.machine.TotemLatheTileEntity;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class TotemLatheModel extends AnimatedGeoModel<TotemLatheDowelTileEntity> {
    @Override
    public ResourceLocation getModelLocation(TotemLatheDowelTileEntity object) {
        return new ResourceLocation(Minestuck.MOD_ID, "geo/totemlathe.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(TotemLatheDowelTileEntity object) {
        return new ResourceLocation(Minestuck.MOD_ID, "textures/block/machine/totemlathe.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(TotemLatheDowelTileEntity animatable) {
        return new ResourceLocation(Minestuck.MOD_ID, "animations/totemlathe.animation.json");
    }
}
